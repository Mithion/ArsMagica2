package am2;

import am2.api.ArsMagicaApi;
import am2.api.events.ManaCostEvent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.BuffPowerLevel;
import am2.api.spell.enums.ContingencyTypes;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityAstralBarrier;
import am2.bosses.BossSpawnHelper;
import am2.buffs.BuffEffectTemporalAnchor;
import am2.buffs.BuffList;
import am2.buffs.BuffStatModifiers;
import am2.damage.DamageSources;
import am2.enchantments.EnchantmentSoulbound;
import am2.entities.EntityFlicker;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.RiftStorage;
import am2.playerextensions.SkillData;
import am2.utility.*;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.brewing.PotionBrewedEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AMEventHandler{

	private final Random rand = new Random();

	@SubscribeEvent
	public void onPotionBrewed(PotionBrewedEvent brewEvent){
		for (ItemStack stack : brewEvent.brewingStacks){
			if (stack == null) continue;
			if (stack.getItem() instanceof ItemPotion){
				ItemPotion ptn = ((ItemPotion)stack.getItem());
				List<PotionEffect> fx = ptn.getEffects(stack.getMetadata());
				if (fx == null) return;
				for (PotionEffect pe : fx){
					if (pe.getPotionID() == BuffList.greaterManaPotion.id){
						stack = InventoryUtilities.replaceItem(stack, ItemsCommonProxy.greaterManaPotion);
						break;
					}else if (pe.getPotionID() == BuffList.epicManaPotion.id){
						stack = InventoryUtilities.replaceItem(stack, ItemsCommonProxy.epicManaPotion);
						break;
					}else if (pe.getPotionID() == BuffList.legendaryManaPotion.id){
						stack = InventoryUtilities.replaceItem(stack, ItemsCommonProxy.legendaryManaPotion);
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEndermanTeleport(EnderTeleportEvent event){
		EntityLivingBase ent = event.entityLiving;


		ArrayList<Long> keystoneKeys = KeystoneUtilities.instance.GetKeysInInvenory(ent);
		TileEntityAstralBarrier blockingBarrier = DimensionUtilities.GetBlockingAstralBarrier(event.entityLiving.worldObj, (int)event.targetX, (int)event.targetY, (int)event.targetZ, keystoneKeys);

		if (ent.isPotionActive(BuffList.astralDistortion.id) || blockingBarrier != null){
			event.setCanceled(true);
			if (blockingBarrier != null){
				blockingBarrier.onEntityBlocked(ent);
			}
			return;
		}

		if (!ent.worldObj.isRemote && ent instanceof EntityEnderman && ent.worldObj.rand.nextDouble() < 0.01f){
			EntityFlicker flicker = new EntityFlicker(ent.worldObj);
			flicker.setPosition(ent.posX, ent.posY, ent.posZ);
			flicker.setFlickerType(Affinity.ENDER);
			ent.worldObj.spawnEntityInWorld(flicker);
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		if (event.entity instanceof EntityLivingBase){
			event.entity.registerExtendedProperties(ExtendedProperties.identifier, new ExtendedProperties());
			((EntityLivingBase)event.entity).getAttributeMap().registerAttribute(ArsMagicaApi.maxManaBonus);
			((EntityLivingBase)event.entity).getAttributeMap().registerAttribute(ArsMagicaApi.maxBurnoutBonus);
			((EntityLivingBase)event.entity).getAttributeMap().registerAttribute(ArsMagicaApi.xpGainModifier);
			((EntityLivingBase)event.entity).getAttributeMap().registerAttribute(ArsMagicaApi.burnoutReductionRate);
			((EntityLivingBase)event.entity).getAttributeMap().registerAttribute(ArsMagicaApi.manaRegenTimeModifier);

			if (event.entity instanceof EntityPlayer){
				event.entity.registerExtendedProperties(RiftStorage.identifier, new RiftStorage());
				event.entity.registerExtendedProperties(AffinityData.identifier, new AffinityData());
				event.entity.registerExtendedProperties(SkillData.identifier, new SkillData((EntityPlayer)event.entity));
			}
		}else if (event.entity instanceof EntityItemFrame){
			AMCore.proxy.itemFrameWatcher.startWatchingFrame((EntityItemFrame)event.entity);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityDeath(LivingDeathEvent event){
		String s = EnchantmentSoulbound.class.getName();
		EntityLivingBase soonToBeDead = event.entityLiving;
		if (soonToBeDead.isPotionActive(BuffList.temporalAnchor.id)){
			event.setCanceled(true);
			PotionEffect pe = soonToBeDead.getActivePotionEffect(BuffList.temporalAnchor);
			if (pe instanceof BuffEffectTemporalAnchor){
				BuffEffectTemporalAnchor buff = (BuffEffectTemporalAnchor)pe;
				buff.stopEffect(soonToBeDead);
			}
			soonToBeDead.removePotionEffect(BuffList.temporalAnchor.id);
			return;
		}

		if (ExtendedProperties.For(soonToBeDead).getContingencyType() == ContingencyTypes.DEATH){
			ExtendedProperties.For(soonToBeDead).procContingency();
		}

		if (soonToBeDead instanceof EntityPlayer){
			AMCore.proxy.playerTracker.onPlayerDeath((EntityPlayer)soonToBeDead);
		}else if (soonToBeDead instanceof EntityCreature){
			if (!EntityUtilities.isSummon(soonToBeDead) && EntityUtilities.isAIEnabled((EntityCreature)soonToBeDead) && event.source.getSourceOfDamage() instanceof EntityPlayer){
				EntityUtilities.handleCrystalPhialAdd((EntityCreature)soonToBeDead, (EntityPlayer)event.source.getSourceOfDamage());
			}
		}

		if (EntityUtilities.isSummon(soonToBeDead)){
			ReflectionHelper.setPrivateValue(EntityLivingBase.class, soonToBeDead, 0, "field_70718_bc", "recentlyHit");
			int ownerID = EntityUtilities.getOwner(soonToBeDead);
			Entity e = soonToBeDead.worldObj.getEntityByID(ownerID);
			if (e != null & e instanceof EntityLivingBase){
				ExtendedProperties.For((EntityLivingBase)e).removeSummon();
			}
		}

		if (soonToBeDead instanceof EntityVillager && ((EntityVillager)soonToBeDead).isChild()){
			BossSpawnHelper.instance.onVillagerChildKilled((EntityVillager)soonToBeDead);
		}
	}

	@SubscribeEvent
	public void onPlayerGetAchievement(AchievementEvent event){
		if (!event.entityPlayer.worldObj.isRemote && event.achievement == AchievementList.theEnd2){
			AMCore.instance.proxy.playerTracker.storeExtendedPropertiesForRespawn(event.entityPlayer);
			// AMCore.instance.proxy.playerTracker.storeSoulboundItemsForRespawn(event.entityPlayer);
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event){
		if (EntityUtilities.isSummon(event.entityLiving) && !(event.entityLiving instanceof EntityHorse)){
			event.setCanceled(true);
		}
		if (event.source == DamageSources.darkNexus){
			event.setCanceled(true);
		}
		if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPig && event.entityLiving.getRNG().nextDouble() < 0.3f){
			EntityItem animalFat = new EntityItem(event.entityLiving.worldObj);
			ItemStack stack = new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ANIMALFAT);
			animalFat.setPosition(event.entity.posX, event.entity.posY, event.entity.posZ);
			animalFat.setEntityItemStack(stack);
			event.drops.add(animalFat);
		}
	}

	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event){
		if (event.entityLiving.isPotionActive(BuffList.agility.id)){
			event.entityLiving.motionY *= 1.5f;
		}
		if (event.entityLiving.isPotionActive(BuffList.leap.id)){

			Entity velocityTarget = event.entityLiving;

			if (event.entityLiving.ridingEntity != null){
				if (event.entityLiving.ridingEntity instanceof EntityMinecart){
					event.entityLiving.ridingEntity.setPosition(event.entityLiving.ridingEntity.posX, event.entityLiving.ridingEntity.posY + 1.5, event.entityLiving.ridingEntity.posZ);
				}
				velocityTarget = event.entityLiving.ridingEntity;
			}

			double yVelocity = 0;
			double xVelocity = 0;
			double zVelocity = 0;

			Random rand = new Random();

			Vec3 vec = event.entityLiving.getLookVec().normalize();
			switch (event.entityLiving.getActivePotionEffect(BuffList.leap).getAmplifier() + 1){
			case BuffPowerLevel.Low:
				yVelocity = 0.4;
				xVelocity = velocityTarget.motionX * 1.08 * Math.abs(vec.xCoord);
				zVelocity = velocityTarget.motionZ * 1.08 * Math.abs(vec.zCoord);
				break;
			case BuffPowerLevel.Medium:
				yVelocity = 0.7;
				xVelocity = velocityTarget.motionX * 1.25 * Math.abs(vec.xCoord);
				zVelocity = velocityTarget.motionZ * 1.25 * Math.abs(vec.zCoord);
				break;
			case BuffPowerLevel.High:
				yVelocity = 1;
				xVelocity = velocityTarget.motionX * 1.75 * Math.abs(vec.xCoord);
				zVelocity = velocityTarget.motionZ * 1.75 * Math.abs(vec.zCoord);
				break;
			}

			float maxHorizontalVelocity = 1.45f;

			if (event.entityLiving.ridingEntity != null && (event.entityLiving.ridingEntity instanceof EntityMinecart || event.entityLiving.ridingEntity instanceof EntityBoat) || event.entityLiving.isPotionActive(BuffList.haste.id)){
				maxHorizontalVelocity += 25;
				xVelocity *= 2.5;
				zVelocity *= 2.5;
			}

			if (xVelocity > maxHorizontalVelocity){
				xVelocity = maxHorizontalVelocity;
			}else if (xVelocity < -maxHorizontalVelocity){
				xVelocity = -maxHorizontalVelocity;
			}

			if (zVelocity > maxHorizontalVelocity){
				zVelocity = maxHorizontalVelocity;
			}else if (zVelocity < -maxHorizontalVelocity){
				zVelocity = -maxHorizontalVelocity;
			}

			if (ExtendedProperties.For(event.entityLiving).getIsFlipped()){
				yVelocity *= -1;
			}

			velocityTarget.addVelocity(xVelocity, yVelocity, zVelocity);
		}
		if (event.entityLiving.isPotionActive(BuffList.entangled.id)){
			event.entityLiving.motionY = 0;
		}

		if (event.entityLiving instanceof EntityPlayer){
			ItemStack boots = ((EntityPlayer)event.entityLiving).inventory.armorInventory[0];
			if (boots != null && boots.getItem() == ItemsCommonProxy.enderBoots && event.entityLiving.isSneaking()){
				ExtendedProperties.For(event.entityLiving).toggleFlipped();
			}

			if (ExtendedProperties.For(event.entityLiving).getFlipRotation() > 0)
				((EntityPlayer)event.entityLiving).addVelocity(0, -2 * event.entityLiving.motionY, 0);
		}
	}

	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event){

		EntityLivingBase ent = event.entityLiving;
		float f = event.distance;
		ent.isAirBorne = false;

		//slowfall buff
		if (ent.isPotionActive(BuffList.slowfall.id) || ent.isPotionActive(BuffList.shrink.id) || (ent instanceof EntityPlayer && AffinityData.For(ent).getAffinityDepth(Affinity.NATURE) == 1.0f)){
			event.setCanceled(true);
			ent.fallDistance = 0;
			return;
		}

		//gravity well
		if (ent.isPotionActive(BuffList.gravityWell.id)){
			ent.fallDistance *= 1.5f;
		}

		//fall protection stat
		f -= ExtendedProperties.For(ent).getFallProtection();
		ExtendedProperties.For(ent).setFallProtection(0);
		if (f <= 0){
			ent.fallDistance = 0;
			event.setCanceled(true);
			return;
		}
	}

	@SubscribeEvent
	public void onEntityLiving(LivingUpdateEvent event){

		EntityLivingBase ent = event.entityLiving;

		World world = ent.worldObj;

		boolean isRemote = world.isRemote;

		BuffStatModifiers.instance.applyStatModifiersBasedOnBuffs(ent);

		ExtendedProperties extendedProperties;
		extendedProperties = ExtendedProperties.For(ent);
		extendedProperties.handleSpecialSyncData();
		extendedProperties.manaBurnoutTick();

		//archmage armor effects & infusion
		if (ent instanceof EntityPlayer){

			extendedProperties.overrideEyeHeight();

			if (ent.worldObj.isRemote){
				int divisor = ExtendedProperties.For(ent).getAuraDelay() > 0 ? ExtendedProperties.For(ent).getAuraDelay() : 1;
				if (ent.ticksExisted % divisor == 0)
					AMCore.instance.proxy.particleManager.spawnAuraParticles(ent);
				AMCore.proxy.setViewSettings();
			}

			ArmorHelper.HandleArmorInfusion((EntityPlayer)ent);
			ArmorHelper.HandleArmorEffects((EntityPlayer)ent, world);

			extendedProperties.flipTick();

			if (extendedProperties.getIsFlipped()){
				if (((EntityPlayer)ent).motionY < 2)
					((EntityPlayer)ent).motionY += 0.15f;

				double posY = ent.posY + ent.height;
				if (!world.isRemote)
					posY += ent.getEyeHeight();
				if (world.rayTraceBlocks(Vec3.createVectorHelper(ent.posX, posY, ent.posZ), Vec3.createVectorHelper(ent.posX, posY + 1, ent.posZ), true) != null){
					if (!ent.onGround){
						if (ent.fallDistance > 0){
							try{
								Method m = ReflectionHelper.findMethod(Entity.class, ent, new String[]{"func_70069_a", "fall"}, float.class);
								m.setAccessible(true);
								m.invoke(ent, ent.fallDistance);
							}catch (Throwable e){
								e.printStackTrace();
							}
							ent.fallDistance = 0;
						}
					}
					ent.onGround = true;
				}else{
					if (ent.motionY > 0){
						if (world.isRemote)
							ent.fallDistance += ent.posY - ent.prevPosY;
						else
							ent.fallDistance += (((EntityPlayer)ent).field_71095_bQ - ((EntityPlayer)ent).field_71096_bN) * 2;
					}
					ent.onGround = false;
				}
			}

			if (ArmorHelper.isInfusionPreset(((EntityPlayer)ent).getCurrentArmor(1), GenericImbuement.stepAssist)){
				ent.stepHeight = 1.0111f;
			}else if (ent.stepHeight == 1.0111f){
				ent.stepHeight = 0.5f;
			}

			IAttributeInstance attr = ent.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			if (ArmorHelper.isInfusionPreset(((EntityPlayer)ent).getCurrentArmor(0), GenericImbuement.runSpeed)){
				if (attr.getModifier(GenericImbuement.imbuedHasteID) == null){
					attr.applyModifier(GenericImbuement.imbuedHaste);
				}
			}else{
				if (attr.getModifier(GenericImbuement.imbuedHasteID) != null){
					attr.removeModifier(GenericImbuement.imbuedHaste);
				}
			}
		}

		if (extendedProperties.getContingencyType() == ContingencyTypes.FALL && !ent.onGround && extendedProperties.getContingencyEffect() != null && ent.fallDistance >= 4f){
			int distanceToGround = MathUtilities.getDistanceToGround(ent, world);
			if (distanceToGround < -8 * ent.motionY){
				extendedProperties.procContingency();
			}
		}
		if (extendedProperties.getContingencyType() == ContingencyTypes.ON_FIRE && ent.isBurning()){
			extendedProperties.procContingency();
		}

		if (!ent.worldObj.isRemote && ent.ticksExisted % 200 == 0){
			extendedProperties.setSyncAuras();
		}

		//buff particles
		//if (ent.worldObj.isRemote)
		//	AMCore.instance.proxy.particleManager.spawnBuffParticles(ent);

		//data sync
		extendedProperties.handleExtendedPropertySync();

		if (ent instanceof EntityPlayer){
			AffinityData.For(ent).handleExtendedPropertySync();
			SkillData.For((EntityPlayer)ent).handleExtendedPropertySync();

			if (ent.isPotionActive(BuffList.flight.id) || ent.isPotionActive(BuffList.levitation.id) || ((EntityPlayer)ent).capabilities.isCreativeMode){
				extendedProperties.hadFlight = true;
				if (ent.isPotionActive(BuffList.levitation)){
					if (((EntityPlayer)ent).capabilities.isFlying){
						float factor = 0.4f;
						ent.motionX *= factor;
						ent.motionZ *= factor;
						ent.motionY *= 0.0001f;
					}
				}
			}else if (extendedProperties.hadFlight){
				((EntityPlayer)ent).capabilities.allowFlying = false;
				((EntityPlayer)ent).capabilities.isFlying = false;
				extendedProperties.hadFlight = false;
			}
		}

		if (ent.isPotionActive(BuffList.agility.id)){
			ent.stepHeight = 1.01f;
		}else if (ent.stepHeight == 1.01f){
			ent.stepHeight = 0.5f;
		}

		if (!ent.worldObj.isRemote && EntityUtilities.isSummon(ent) && !EntityUtilities.isTileSpawnedAndValid(ent)){
			int owner = EntityUtilities.getOwner(ent);
			Entity ownerEnt = ent.worldObj.getEntityByID(owner);
			if (!EntityUtilities.decrementSummonDuration(ent)){
				ent.attackEntityFrom(DamageSources.unsummon, 5000);
			}
			if (owner == -1 || ownerEnt == null || ownerEnt.isDead || ownerEnt.getDistanceSqToEntity(ent) > 900){
				if (ent instanceof EntityLiving && !((EntityLiving)ent).getCustomNameTag().equals("")){
					EntityUtilities.setOwner(ent, null);
					EntityUtilities.setSummonDuration(ent, -1);
					EntityUtilities.revertAI((EntityCreature)ent);
				}else{
					ent.attackEntityFrom(DamageSources.unsummon, 5000);
				}
			}
		}

		//leap buff
		if (event.entityLiving.isPotionActive(BuffList.leap)){
			int amplifier = event.entityLiving.getActivePotionEffect(BuffList.leap).getAmplifier() + 1;

			switch (amplifier){
			case BuffPowerLevel.Low:
				extendedProperties.setFallProtection(8);
				break;
			case BuffPowerLevel.Medium:
				extendedProperties.setFallProtection(20);
				break;
			case BuffPowerLevel.High:
				extendedProperties.setFallProtection(45);
				break;
			}
		}

		if (event.entityLiving.isPotionActive(BuffList.gravityWell)){
			if (event.entityLiving.motionY < 0 && event.entityLiving.motionY > -3f){
				event.entityLiving.motionY *= 1.59999999999999998D;
			}
		}


		//slowfall/shrink buff
		if (event.entityLiving.isPotionActive(BuffList.slowfall) || event.entityLiving.isPotionActive(BuffList.shrink) || (!ent.isSneaking() && ent instanceof EntityPlayer && AffinityData.For(ent).getAffinityDepth(Affinity.NATURE) == 1.0f)){
			if (!event.entityLiving.onGround && event.entityLiving.motionY < 0.0D){
				event.entityLiving.motionY *= 0.79999999999999998D;
			}
		}

		//watery grave
		if (event.entityLiving.isPotionActive(BuffList.wateryGrave)){
			if (event.entityLiving.isInWater()){
				double pullVel = -0.5f;
				pullVel *= (event.entityLiving.getActivePotionEffect(BuffList.wateryGrave).getAmplifier() + 1);
				if (event.entityLiving.motionY > pullVel)
					event.entityLiving.motionY -= 0.1;
			}
		}

		//mana link pfx
		if (ent.worldObj.isRemote)
			extendedProperties.spawnManaLinkParticles();

		if (ent.ticksExisted % 20 == 0)
			extendedProperties.cleanupManaLinks();

		if (world.isRemote){
			AMCore.proxy.sendLocalMovementData(ent);
		}
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event){
		ItemStack result = attemptFill(event.world, event.target);

		if (result != null){
			event.result = result;
			event.setResult(Result.ALLOW);
		}
	}

	private ItemStack attemptFill(World world, MovingObjectPosition p){
		Block block = world.getBlock(p.blockX, p.blockY, p.blockZ);

		if (block == BlocksCommonProxy.liquidEssence){
			if (world.getBlockMetadata(p.blockX, p.blockY, p.blockZ) == 0) // Check that it is a source block
			{
				world.setBlock(p.blockX, p.blockY, p.blockZ, Blocks.air); // Remove the fluid block

				return new ItemStack(ItemsCommonProxy.itemAMBucket);
			}
		}

		return null;
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event){
		if (event.target instanceof EntityItemFrame){
			AMCore.proxy.itemFrameWatcher.startWatchingFrame((EntityItemFrame)event.target);
		}
	}

	@SubscribeEvent
	public void onPlayerTossItem(ItemTossEvent event){
		if (!event.entityItem.worldObj.isRemote)
			EntityItemWatcher.instance.addWatchedItem(event.entityItem);
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event){
		if (event.source.isFireDamage() && event.entityLiving instanceof EntityPlayer && ((EntityPlayer)event.entityLiving).inventory.armorInventory[3] != null && ((EntityPlayer)event.entityLiving).inventory.armorInventory[3].getItem() == ItemsCommonProxy.fireEars){
			event.setCanceled(true);
			return;
		}

		if (event.entityLiving.isPotionActive(BuffList.manaShield)){
			if (ExtendedProperties.For(event.entityLiving).getCurrentMana() >= event.ammount * 250f){
				ExtendedProperties.For(event.entityLiving).deductMana(event.ammount * 100f);
				ExtendedProperties.For(event.entityLiving).forceSync();
				for (int i = 0; i < Math.min(event.ammount, 5 * AMCore.config.getGFXLevel()); ++i)
					AMCore.proxy.particleManager.BoltFromPointToPoint(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY + rand.nextFloat() * event.entityLiving.getEyeHeight(), event.entityLiving.posZ, event.entityLiving.posX - 1 + rand.nextFloat() * 2, event.entityLiving.posY - 1 + rand.nextFloat() * 2, event.entityLiving.posZ - 1 + rand.nextFloat() * 2, 6, -1);
				event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "arsmagica2:misc.event.mana_shield_block", 1.0f, rand.nextFloat() + 0.5f);
				event.setCanceled(true);
				return;
			}
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event){

		if (event.source.isFireDamage() && event.entityLiving instanceof EntityPlayer && ((EntityPlayer)event.entityLiving).inventory.armorInventory[3] != null && ((EntityPlayer)event.entityLiving).inventory.armorInventory[3].getItem() == ItemsCommonProxy.fireEars){
			event.setCanceled(true);
			return;
		}

		if (event.entityLiving.isPotionActive(BuffList.magicShield))
			event.ammount *= 0.25f;

		if (event.entityLiving.isPotionActive(BuffList.manaShield)){
			float manaToTake = Math.min(ExtendedProperties.For(event.entityLiving).getCurrentMana(), event.ammount * 250f);
			event.ammount -= manaToTake / 250f;
			ExtendedProperties.For(event.entityLiving).deductMana(manaToTake);
			ExtendedProperties.For(event.entityLiving).forceSync();
			for (int i = 0; i < Math.min(event.ammount, 5 * AMCore.config.getGFXLevel()); ++i)
				AMCore.proxy.particleManager.BoltFromPointToPoint(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY + rand.nextFloat() * event.entityLiving.getEyeHeight(), event.entityLiving.posZ, event.entityLiving.posX - 1 + rand.nextFloat() * 2, event.entityLiving.posY + event.entityLiving.getEyeHeight() - 1 + rand.nextFloat() * 2, event.entityLiving.posZ - 1 + rand.nextFloat() * 2, 6, -1);
			event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "arsmagica2:misc.event.mana_shield_block", 1.0f, rand.nextFloat() + 0.5f);
			if (event.ammount <= 0){
				event.setCanceled(true);
				return;
			}
		}

		if (event.source.getSourceOfDamage() instanceof EntityPlayer && ((EntityPlayer)event.source.getSourceOfDamage()).inventory.armorInventory[2] != null && ((EntityPlayer)event.source.getSourceOfDamage()).inventory.armorInventory[2].getItem() == ItemsCommonProxy.earthGuardianArmor && ((EntityPlayer)event.source.getSourceOfDamage()).getCurrentEquippedItem() == null){
			event.ammount += 4;

			double deltaZ = event.entityLiving.posZ - event.source.getSourceOfDamage().posZ;
			double deltaX = event.entityLiving.posX - event.source.getSourceOfDamage().posX;
			double angle = Math.atan2(deltaZ, deltaX);
			double speed = ((EntityPlayer)event.source.getSourceOfDamage()).isSprinting() ? 3 : 2;
			double vertSpeed = ((EntityPlayer)event.source.getSourceOfDamage()).isSprinting() ? 0.5 : 0.325;

			if (event.entityLiving instanceof EntityPlayer){
				AMNetHandler.INSTANCE.sendVelocityAddPacket(event.entityLiving.worldObj, event.entityLiving, speed * Math.cos(angle), vertSpeed, speed * Math.sin(angle));
			}else{
				event.entityLiving.motionX += (speed * Math.cos(angle));
				event.entityLiving.motionZ += (speed * Math.sin(angle));
				event.entityLiving.motionY += vertSpeed;
			}
			event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "arsmagica2:spell.cast.earth", 0.4f, event.entityLiving.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		ExtendedProperties extendedProperties = ExtendedProperties.For(event.entityLiving);
		EntityLivingBase ent = event.entityLiving;
		if (extendedProperties.getContingencyType() == ContingencyTypes.DAMAGE_TAKEN){
			extendedProperties.procContingency();
		}
		if (extendedProperties.getContingencyType() == ContingencyTypes.HEALTH_LOW && ent.getHealth() <= ent.getMaxHealth() / 3){
			extendedProperties.procContingency();
		}

		if (ent.isPotionActive(BuffList.fury.id))
			event.ammount /= 2;

		if (event.source.getSourceOfDamage() != null &&
				event.source.getSourceOfDamage() instanceof EntityLivingBase &&
				((EntityLivingBase)event.source.getSourceOfDamage()).isPotionActive(BuffList.shrink))
			event.ammount /= 2;
	}

	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed event){
		EntityPlayer player = event.entityPlayer;
		if (player.isPotionActive(BuffList.fury.id))
			event.newSpeed = event.originalSpeed * 5;
	}

	@SubscribeEvent
	public void onManaCost(ManaCostEvent event){
		if (event.caster.getHeldItem() != null && event.caster.getHeldItem().getItem() == ItemsCommonProxy.arcaneSpellbook){
			event.manaCost *= 0.75f;
			event.burnout *= 0.4f;
		}
	}

	@SubscribeEvent
	public void onPlayerPickupItem(EntityItemPickupEvent event){
		if (!(event.entityPlayer instanceof EntityPlayer))
			return;

		if (!event.entityPlayer.worldObj.isRemote && ExtendedProperties.For(event.entityPlayer).getMagicLevel() <= 0 && event.item.getEntityItem().getItem() == ItemsCommonProxy.arcaneCompendium){
			event.entityPlayer.addChatMessage(new ChatComponentText("You have unlocked the secrets of the arcane!"));
			AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP)event.entityPlayer, "shapes", true);
			AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP)event.entityPlayer, "components", true);
			AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP)event.entityPlayer, "modifiers", true);
			ExtendedProperties.For(event.entityPlayer).setMagicLevelWithMana(1);
			ExtendedProperties.For(event.entityPlayer).forceSync();
			return;
		}

		if (event.item.getEntityItem().getItem() == ItemsCommonProxy.spell){
			if (event.entityPlayer.worldObj.isRemote){
				AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP)event.entityPlayer, "spell_book", false);
			}
		}else{
			Item item = event.item.getEntityItem().getItem();
			int meta = event.item.getEntityItem().getMetadata();

			if (event.entityPlayer.worldObj.isRemote &&
					item.getUnlocalizedName() != null && (
					AMCore.proxy.items.getArsMagicaItems().contains(item)) ||
					(item instanceof ItemBlock && AMCore.proxy.blocks.getArsMagicaBlocks().contains(((ItemBlock)item).blockInstance))){
				AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP)event.entityPlayer, item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "").replace("tile.", "") + ((meta > -1) ? "@" + meta : ""), false);
			}
		}
	}
}

