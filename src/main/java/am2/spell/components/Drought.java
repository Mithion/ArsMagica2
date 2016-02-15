package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Drought implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockFlower || block instanceof BlockTallGrass){
			world.setBlockState(pos, Blocks.tallgrass.getDefaultState());
			return true;
		}else if (block == Blocks.grass || block == Blocks.mycelium || block == Blocks.sandstone || block == Blocks.dirt){
			world.setBlockState(pos, Blocks.sand.getDefaultState());
			return true;
		}else if (block == Blocks.stone){
			world.setBlockState(pos, Blocks.cobblestone.getDefaultState());
			return true;
		}else if (block == Blocks.stonebrick && world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) != 2){
			world.setBlockMetadataWithNotify(blockx, blocky, blockz, 2, 2);
			return true;
		}

		if (block == Blocks.water || block == Blocks.flowing_water){
			world.setBlockState(pos, Blocks.air.getDefaultState());
			return true;
		}else{
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
				case DOWN:
					pos = pos.down();
					break;
				case UP:
					pos = pos.up();
					break;
			}
			block = world.getBlockState(pos).getBlock();
			if (block == Blocks.water || block == Blocks.flowing_water){
				world.setBlockState(pos, Blocks.air.getDefaultState());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 60;
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
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "ember", x + 0.5, y + 1, z + 0.5);
			if (particle != null){
				particle.addRandomOffset(1, 0, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f));
				particle.setAffectedByGravity();
				particle.setRGBColorF(0.9f, 0.8f, 0.5f);
				particle.setMaxAge(40);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.FIRE, Affinity.AIR);
	}

	@Override
	public int getID(){
		return 12;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				Blocks.sand,
				Blocks.deadbush
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		if (affinity == Affinity.FIRE)
			return 0.008f;
		else
			return 0.004f;
	}

}
