package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.spell.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Ignition implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		switch (facing){
			case EAST:
				pos = pos.east();
				break;
			case NORTH:
				pos = pos.north();
				break;
			case SOUTH:
				pos = pos.south();
				break;
			case WEST:
				pos = pos.west();
				break;
		}

		Block block = world.getBlockState(pos).getBlock();

		if (world.isAirBlock(pos) || block == Blocks.snow || block instanceof BlockFlower){
			if (!world.isRemote) world.setBlockState(pos, Blocks.fire.getDefaultState());
			return true;
		}else{
			pos = pos.up();
			block = world.getBlockState(pos).getBlock();
			if (world.isAirBlock(pos) || block == Blocks.snow || block instanceof BlockFlower){
				if (!world.isRemote) world.setBlockState(pos, Blocks.fire.getDefaultState());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		int burnTime = SpellUtils.instance.getModifiedInt_Mul(3, stack, caster, target, world, 0, SpellModifiers.DURATION);
		burnTime = SpellUtils.instance.modifyDurationBasedOnArmor(caster, burnTime);
		if (target.isBurning()){
			return false;
		}
		target.setFire(burnTime);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 35;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 10;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 5; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "explosion_2", x + 0.5, y + 0.5, z + 0.5);
			if (particle != null){
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, 0.3, rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.FIRE);
	}

	@Override
	public int getID(){
		return 26;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				Items.flint_and_steel
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

}
