package am2.spell;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import am2.AMCore;
import am2.api.events.ManaCostEvent;
import am2.api.events.SpellCastingEvent;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.armor.ArmorHelper;
import am2.armor.ArsMagicaArmorMaterial;
import am2.blocks.BlocksCommonProxy;
import am2.buffs.BuffList;
import am2.entities.EntityDarkMage;
import am2.entities.EntityLightMage;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils.SpellRequirements;
import am2.spell.modifiers.Colour;
import am2.utility.EntityUtilities;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class SpellHelper {

	public static final SpellHelper instance = new SpellHelper();

	private final Random rand;

	private SpellHelper(){
		rand = new Random();
	}

	public SpellCastResult applyStageToGround(ItemStack stack, EntityLivingBase caster, World world, int blockX, int blockY, int blockZ, int blockFace, double impactX, double impactY, double impactZ, int stage, boolean consumeMBR){
		ISpellShape stageShape = SpellUtils.instance.getShapeForStage(stack, 0);
		if (stageShape == null || stageShape == SkillManager.instance.missingShape){
			return SpellCastResult.MALFORMED_SPELL_STACK;
		}

		ISpellComponent[] components = SpellUtils.instance.getComponentsForStage(stack, 0);

		for (ISpellComponent component : components){

			if (SkillTreeManager.instance.isSkillDisabled(component))
				continue;
			
			//special logic for spell sealed doors
			if (BlocksCommonProxy.spellSealedDoor.applyComponentToDoor(world, component, blockX, blockY, blockZ))
				continue;

			if (component.applyEffectBlock(stack, world, blockX, blockY, blockZ, blockFace, impactX, impactY, impactZ, caster)){
				if (world.isRemote){
					int color = -1;
					if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, stack, 0)){
						ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(stack, 0);
						int ordinalCount = 0;
						for (ISpellModifier mod : mods){
							if (mod instanceof Colour){
								byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(stack, mod, 0, ordinalCount++);
								color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
							}
						}
					}
					component.spawnParticles(world, blockX, blockY, blockZ, caster, caster, rand, color);
				}
				if (consumeMBR)
					SpellUtils.instance.doAffinityShift(caster, component, stageShape);
			}
		}

		return SpellCastResult.SUCCESS;
	}

	public SpellCastResult applyStageToEntity(ItemStack stack, EntityLivingBase caster, World world, Entity target, int stage, boolean shiftAffinityAndXP){
		ISpellShape stageShape = SpellUtils.instance.getShapeForStage(stack, 0);
		if (stageShape == null) return SpellCastResult.MALFORMED_SPELL_STACK;

		ISpellComponent[] components = SpellUtils.instance.getComponentsForStage(stack, 0);

		boolean appliedOneComponent = false;

		for (ISpellComponent component : components){

			if (SkillTreeManager.instance.isSkillDisabled(component))
				continue;

			if (component.applyEffectEntity(stack, world, caster, target)){
				appliedOneComponent = true;
				if (world.isRemote){
					int color = -1;
					if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, stack, 0)){
						ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(stack, 0);
						int ordinalCount = 0;
						for (ISpellModifier mod : mods){
							if (mod instanceof Colour){
								byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(stack, mod, 0, ordinalCount++);
								color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
							}
						}
					}
					component.spawnParticles(world, target.posX, target.posY + target.getEyeHeight(), target.posZ, caster, target, rand, color);
				}
				if (shiftAffinityAndXP)
					SpellUtils.instance.doAffinityShift(caster, component, stageShape);
			}
		}

		if (appliedOneComponent)
			return SpellCastResult.SUCCESS;
		else
			return SpellCastResult.EFFECT_FAILED;
	}

	private SpellCastingEvent.Pre preSpellCast(ItemStack stack, EntityLivingBase caster, boolean isChanneled){

		SpellRequirements reqs = SpellUtils.instance.getSpellRequirements(stack, caster);

		float manaCost = reqs.manaCost;
		float burnout = reqs.burnout;
		ArrayList<ItemStack> reagents = reqs.reagents;

		ManaCostEvent mce = new ManaCostEvent(stack, caster, manaCost, burnout);

		MinecraftForge.EVENT_BUS.post(mce);

		manaCost = mce.manaCost;
		burnout = mce.burnout;

		SpellCastingEvent.Pre event = new SpellCastingEvent().new Pre(stack, (ItemSpellBase) stack.getItem(), caster, manaCost, burnout, isChanneled);

		if (MinecraftForge.EVENT_BUS.post(event)){
			event.castResult = SpellCastResult.EFFECT_FAILED;
			return event;
		}

		event.castResult = SpellCastResult.SUCCESS;

		if (!SpellUtils.instance.casterHasAllReagents(caster, reagents))
			event.castResult = SpellCastResult.REAGENTS_MISSING;
		if (!SpellUtils.instance.casterHasMana(caster, manaCost))
			event.castResult = SpellCastResult.NOT_ENOUGH_MANA;

		return event;
	}

	public SpellCastResult applyStackStage(ItemStack stack, EntityLivingBase caster, EntityLivingBase target, double x, double y, double z, int side, World world, boolean consumeMBR, boolean giveXP, int ticksUsed){

		if (caster.isPotionActive(BuffList.silence.id))
			return SpellCastResult.SILENCED;

		ItemStack parsedStack = SpellUtils.instance.constructSpellStack(stack);

		if (SpellUtils.instance.numStages(parsedStack) == 0){
			return SpellCastResult.SUCCESS;
		}
		ISpellShape shape = SpellUtils.instance.getShapeForStage(parsedStack, 0);
		ItemSpellBase item = (ItemSpellBase)parsedStack.getItem();

		if (SkillTreeManager.instance.isSkillDisabled(shape))
			return SpellCastResult.EFFECT_FAILED;

		if (!(caster instanceof EntityPlayer)){
			consumeMBR = false;
		}
		
		SpellCastingEvent.Pre checkEvent = null;
		if (consumeMBR){
			checkEvent = preSpellCast(parsedStack, caster, false);
			if (checkEvent.castResult != SpellCastResult.SUCCESS){
				if (checkEvent.castResult == SpellCastResult.NOT_ENOUGH_MANA && caster.worldObj.isRemote && caster instanceof EntityPlayer){
					AMCore.proxy.flashManaBar();
				}
				SpellCastingEvent.Post event = new SpellCastingEvent().new Post(parsedStack, (ItemSpellBase) parsedStack.getItem(), caster, checkEvent.manaCost, checkEvent.burnout, false, checkEvent.castResult);
				MinecraftForge.EVENT_BUS.post(event);
				
				return checkEvent.castResult;
			}
		}

		SpellCastResult result = SpellCastResult.MALFORMED_SPELL_STACK;

		if (shape != null){
			result = shape.beginStackStage(item, parsedStack, caster, target, world, x, y, z, side, giveXP, ticksUsed);

			if (!world.isRemote){
				AMDataWriter writer = new AMDataWriter();
				writer.add(parsedStack);
				writer.add(caster.getEntityId());
				if (target != null){
					writer.add(true);
					writer.add(target.getEntityId());
				}else{
					writer.add(false);
				}
				writer.add(x).add(y).add(z);
				writer.add(side);
				writer.add(ticksUsed);
				
				AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, x, y, z, 32, AMPacketIDs.SPELL_CAST, writer.generate());
			}
		}

		float manaCost = 0;
		float burnout = 0;

		if (consumeMBR){
			manaCost = checkEvent.manaCost;
			burnout = checkEvent.burnout;

			if (result == SpellCastResult.SUCCESS_REDUCE_MANA){
				result = SpellCastResult.SUCCESS;
				manaCost *= 0.2f;
				burnout *= 0.2f;
			}
		}

		if (result == SpellCastResult.SUCCESS){
			if (consumeMBR){
				ExtendedProperties.For(caster).deductMana(manaCost);
				ExtendedProperties.For(caster).addBurnout(burnout);
			}
			if (world.isRemote){
				String sfx = shape.getSoundForAffinity(SpellUtils.instance.mainAffinityFor(parsedStack), parsedStack, null);
				if (sfx != null){
					if (!shape.isChanneled()){
						world.playSound(caster.posX, caster.posY, caster.posZ, sfx, 0.4f, world.rand.nextFloat() * 0.1F + 0.9F, false);
					}else{						
						//SoundHelper.instance.loopSound(world, (float)x, (float)y, (float)z, sfx, 0.6f);
					}
				}
			}
		}
		
		SpellCastingEvent.Post event = new SpellCastingEvent().new Post(parsedStack, (ItemSpellBase) parsedStack.getItem(), caster, manaCost, burnout, false, result);
		MinecraftForge.EVENT_BUS.post(event);

		return result;
	}

	public SpellCastResult applyStackStageOnUsing(ItemStack stack, EntityLivingBase caster, EntityLivingBase target, double x, double y, double z, World world, boolean consumeMBR, boolean giveXP, int ticks){
		if (SpellUtils.instance.numStages(stack) == 0){
			return SpellCastResult.SUCCESS;
		}

		if (!SpellUtils.instance.spellIsChanneled(stack)){
			return SpellCastResult.EFFECT_FAILED;
		}

		return applyStackStage(stack, caster, target, x, y, z, 0, world, consumeMBR, giveXP, ticks);
	}

	public boolean attackTargetSpecial(ItemStack spellStack, Entity target, DamageSource damagesource, float magnitude) {

		if (target.worldObj.isRemote)
			return true;

		EntityPlayer dmgSrcPlayer = null;

		if (damagesource.getSourceOfDamage() != null){
			if (damagesource.getSourceOfDamage() instanceof EntityLivingBase){
				EntityLivingBase source = (EntityLivingBase)damagesource.getSourceOfDamage();
				if ((source instanceof EntityLightMage || source instanceof EntityDarkMage) && target.getClass() == EntityCreeper.class){
					return false;
				}else if (source instanceof EntityLightMage && target instanceof EntityLightMage){
					return false;
				}else if (source instanceof EntityDarkMage && target instanceof EntityDarkMage){
					return false;
				}else if (source instanceof EntityPlayer && target instanceof EntityPlayer && !target.worldObj.isRemote && (!MinecraftServer.getServer().isPVPEnabled() || ((EntityPlayer)target).capabilities.isCreativeMode)){
					return false;
				}

				if (source.isPotionActive(BuffList.fury))
					magnitude += 4;
			}

			if (damagesource.getSourceOfDamage() instanceof EntityPlayer){
				dmgSrcPlayer = (EntityPlayer) damagesource.getSourceOfDamage();
				int armorSet = ArmorHelper.getFullArsMagicaArmorSet(dmgSrcPlayer);
				if (armorSet == ArsMagicaArmorMaterial.MAGE.getMaterialID()){
					magnitude *= 1.05f;
				}else if (armorSet == ArsMagicaArmorMaterial.BATTLEMAGE.getMaterialID()){
					magnitude *= 1.025f;
				}else if (armorSet == ArsMagicaArmorMaterial.ARCHMAGE.getMaterialID()){
					magnitude *= 1.1f;
				}

				ItemStack equipped = (dmgSrcPlayer).getCurrentEquippedItem();
				if (equipped != null && equipped.getItem() == ItemsCommonProxy.arcaneSpellbook){
					magnitude *= 1.1f;
				}
			}
		}

		if (target instanceof EntityLivingBase){
			if (EntityUtilities.isSummon((EntityLivingBase)target) && damagesource.damageType.equals("magic")){
				magnitude *= 3.0f;
			}
		}

		magnitude *= AMCore.config.getDamageMultiplier();

		ItemStack oldItemStack = null;

		boolean success = false;
		if (target instanceof EntityDragon){
			success = ((EntityDragon)target).attackEntityFromPart(((EntityDragon)target).dragonPartBody, damagesource, magnitude);
		}else{
			success = target.attackEntityFrom(damagesource, magnitude);
		}

		if (dmgSrcPlayer != null){
			if (spellStack != null && target instanceof EntityLivingBase){
				if (!target.worldObj.isRemote &&
						((EntityLivingBase)target).getHealth() <= 0 &&
						SpellUtils.instance.modifierIsPresent(SpellModifiers.DISMEMBERING_LEVEL, spellStack, 0)){
					double chance = SpellUtils.instance.getModifiedDouble_Add(0, spellStack, dmgSrcPlayer, target, dmgSrcPlayer.worldObj, 0, SpellModifiers.DISMEMBERING_LEVEL);
					if (dmgSrcPlayer.worldObj.rand.nextDouble() <= chance){
						dropHead(target, dmgSrcPlayer.worldObj);
					}
				}
			}
		}

		return success;
	}

	private void dropHead(Entity target, World world){
		if (target.getClass() == EntitySkeleton.class){
			if (((EntitySkeleton)target).getSkeletonType() == 1){
				dropHead_do(world, target.posX, target.posY, target.posZ, 1);
			}else{
				dropHead_do(world, target.posX, target.posY, target.posZ, 0);
			}
		}else if (target.getClass() == EntityZombie.class){
			dropHead_do(world, target.posX, target.posY, target.posZ, 2);
		}else if (target.getClass() == EntityCreeper.class){
			dropHead_do(world, target.posX, target.posY, target.posZ, 4);
		}else if (target instanceof EntityPlayer){
			dropHead_do(world, target.posX, target.posY, target.posZ, 3);
		}
	}

	private void dropHead_do(World world, double x, double y, double z, int type){
		EntityItem item = new EntityItem(world);
		ItemStack stack = new ItemStack(Items.skull, 1, type);
		item.setEntityItemStack(stack);
		item.setPosition(x, y, z);
		world.spawnEntityInWorld(item);
	}
}
