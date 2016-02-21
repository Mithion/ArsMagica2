package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleOrbitEntity;
import am2.particles.ParticleOrbitPoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class DivineIntervention implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing side, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (world.isRemote || !(target instanceof EntityLivingBase)) return true;

		if (((EntityLivingBase)target).isPotionActive(BuffList.astralDistortion.id)){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new ChatComponentText("The distortion around you prevents you from teleporting"));
			return true;
		}

		if (target.dimension == 1){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new ChatComponentText("Nothing happens..."));
			return true;
		}else if (target.dimension == 0){
			BlockPos coords = target instanceof EntityPlayer ? ((EntityPlayer)target).getBedLocation(target.dimension) : null;
			if (coords == null || (coords.getX() == 0 && coords.getY() == 0 && coords.getZ() == 0)){
				coords = world.getSpawnPoint();
			}
			int yPos = coords.getY();
			while (world.getBlockState(coords.add(0, yPos, 0)) != Blocks.air && world.getBlockState(coords.up()) != Blocks.air){
				yPos++;
			}
			((EntityLivingBase)target).setPositionAndUpdate(coords.getX() + 0.5, yPos, coords.getZ() + 0.5);
		}else{
			//DimensionUtilities.doDimensionTransfer((EntityLivingBase)target, 0);
			AMCore.proxy.addDeferredDimensionTransfer((EntityLivingBase)target, 0);
		}

		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 400;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return new ItemStack[]{new ItemStack(ItemsCommonProxy.essence, 1, 9)};
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 100; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "arcane", x, y - 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				if (rand.nextBoolean())
					particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.1f, 1, false).SetTargetDistance(rand.nextDouble() + 0.5));
				else
					particle.AddParticleController(new ParticleOrbitPoint(particle, x, y, z, 1, false).SetOrbitSpeed(0.1f).SetTargetDistance(rand.nextDouble() + 0.5));
				particle.setMaxAge(25 + rand.nextInt(10));
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.ENDER);
	}

	@Override
	public int getID(){
		return 11;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				Items.bed,
				Items.ender_pearl
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.4f;
	}
}
