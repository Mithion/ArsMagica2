package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.ArsMagicaApi;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.math.AMVector3;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleExpandingCollapsingRingAtPoint;
import am2.playerextensions.ExtendedProperties;
import am2.utility.DimensionUtilities;
import am2.utility.EntityUtilities;
import am2.utility.KeystoneUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class Recall implements ISpellComponent, IRitualInteraction{

    @Override
    public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
        return false;
    }

    @Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){

		if (!(target instanceof EntityLivingBase)){
			return false;
		}

		if (caster.isPotionActive(BuffList.astralDistortion.id) || ((EntityLivingBase)target)
				.isPotionActive(BuffList.astralDistortion.id)){
			if (caster instanceof EntityPlayer)
				((EntityPlayer)caster)
						.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.cantTeleport")));
			return false;
		}

		ItemStack[] ritualRunes = RitualShapeHelper.instance.checkForRitual(this, world, target.getPosition(), false);
		if (ritualRunes != null){
			return handleRitualReagents(ritualRunes, world, target.getPosition(), caster, target);
		}

		ExtendedProperties casterProperties = ExtendedProperties.For(caster);
		if (!casterProperties.getMarkSet()){
			if (caster instanceof EntityPlayer && !world.isRemote)
				((EntityPlayer)caster).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.noMark")));
			return false;
		}else if (casterProperties.getMarkDimension() != caster.dimension){
			if (caster instanceof EntityPlayer && !world.isRemote)
				((EntityPlayer)caster).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.diffDimMark")));
			return false;
		}
		if (!world.isRemote){
			((EntityLivingBase)target).setPositionAndUpdate(casterProperties.getMarkX(), casterProperties.getMarkY(), casterProperties.getMarkZ());
		}
		return true;
	}

	private boolean handleRitualReagents(ItemStack[] ritualRunes, World world, BlockPos pos, EntityLivingBase caster, Entity target){

		boolean hasVinteumDust = false;
		for (ItemStack stack : ritualRunes){
			if (stack.getItem() == ItemsCommonProxy.itemOre && stack.getItemDamage() == ItemsCommonProxy.itemOre.META_VINTEUMDUST){
				hasVinteumDust = true;
				break;
			}
		}

		if (!hasVinteumDust && ritualRunes.length == 3){
			long key = KeystoneUtilities.instance.getKeyFromRunes(ritualRunes);
			AMVector3 vector = AMCore.proxy.blocks.getNextKeystonePortalLocation(world, pos, false, key);
			if (vector == null || vector.equals(new AMVector3(pos.getX(), pos.getY(), pos.getZ()))){
				if (caster instanceof EntityPlayer && !world.isRemote)
					((EntityPlayer)caster).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.noMatchingGate")));
				return false;
			}else{
				RitualShapeHelper.instance.consumeRitualReagents(this, world, pos);
				RitualShapeHelper.instance.consumeRitualShape(this, world, pos);
				((EntityLivingBase)target).setPositionAndUpdate(vector.x, vector.y - target.height, vector.z);
				return true;
			}
		}else if (hasVinteumDust){
			ArrayList<ItemStack> copy = new ArrayList<ItemStack>();
			for (ItemStack stack : ritualRunes){
				if (stack.getItem() == ItemsCommonProxy.rune && stack.getItemDamage() <= 16){
					copy.add(stack);
				}
			}
			ItemStack[] newRunes = copy.toArray(new ItemStack[copy.size()]);
			long key = KeystoneUtilities.instance.getKeyFromRunes(newRunes);
			EntityPlayer player = EntityUtilities.getPlayerForCombo(world, (int)key);
			if (player == null){
				if (caster instanceof EntityPlayer && !world.isRemote)
					((EntityPlayer)caster).addChatMessage(new ChatComponentText("am2.tooltip.noMatchingPlayer"));
				return false;
			}else if (player == caster){
				if (caster instanceof EntityPlayer && !world.isRemote)
					((EntityPlayer)caster).addChatMessage(new ChatComponentText("am2.tooltip.cantSummonSelf"));
				return false;
			}else{
				RitualShapeHelper.instance.consumeRitualReagents(this, world, pos);
				if (target.worldObj.provider.getDimensionId() != caster.worldObj.provider.getDimensionId()){
					DimensionUtilities.doDimensionTransfer(player, caster.worldObj.provider.getDimensionId());
				}
				((EntityLivingBase)target).setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 500;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "arcane", x, y - 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 0, 1);
				particle.AddParticleController(new ParticleExpandingCollapsingRingAtPoint(particle, x, y - 1, z, 0.1, 3, 0.3, 1, false).setCollapseOnce());
				particle.setMaxAge(20);
				particle.setParticleScale(0.2f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.ARCANE);
	}

	@Override
	public int getID(){
		return 44;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				Items.compass,
				new ItemStack(Items.map, 1, Short.MAX_VALUE),
				Items.ender_pearl
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.1f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.ringedCross;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.rune, 1, AMCore.ANY_META),
				new ItemStack(ItemsCommonProxy.rune, 1, AMCore.ANY_META),
				new ItemStack(ItemsCommonProxy.rune, 1, AMCore.ANY_META)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return RitualShapeHelper.instance.ringedCross.getWidth();
	}
}
