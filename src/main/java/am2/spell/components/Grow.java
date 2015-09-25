package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleOrbitPoint;
import am2.utility.DummyEntityPlayer;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.util.EnumSet;
import java.util.Random;

public class Grow implements ISpellComponent{

	private final Random random = new Random();

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		Block block = world.getBlock(blockx, blocky, blockz);

		if (block == Blocks.farmland){
			blocky++;
			block = world.getBlock(blockx, blocky, blockz);
		}

		BonemealEvent event = new BonemealEvent(DummyEntityPlayer.fromEntityLiving(caster), world, block, blockx, blocky, blockz);
		if (MinecraftForge.EVENT_BUS.post(event)){
			return false;
		}
		if (event.getResult() == Result.ALLOW){
			return true;
		}

		if (block == Blocks.sapling){
			if (!world.isRemote){
				((BlockSapling)Blocks.sapling).func_149878_d(world, blockx, blocky, blockz, world.rand);
			}

			return true;
		}

		if (block == Blocks.brown_mushroom || block == Blocks.red_mushroom){
			if (!world.isRemote){
				((BlockMushroom)block).func_149884_c(world, blockx, blocky, blockz, world.rand);
			}

			return true;
		}

		if (block == Blocks.melon_stem || block == Blocks.pumpkin_stem){
			if (world.getBlockMetadata(blockx, blocky, blockz) == 7){
				return false;
			}

			if (!world.isRemote){
				((BlockStem)block).func_149874_m(world, blockx, blocky, blockz);
			}

			return true;
		}

		if (block instanceof BlockCrops){
			if (world.getBlockMetadata(blockx, blocky, blockz) == 7){
				return false;
			}

			if (!world.isRemote){
				((BlockCrops)block).func_149863_m(world, blockx, blocky, blockz);
			}

			return true;
		}

		if (block == Blocks.cocoa){
			if (!world.isRemote){
				int meta = world.getBlockMetadata(blockx, blocky, blockz);
				int direction = BlockDirectional.getDirection(meta);
				int something = BlockCocoa.func_149987_c(meta);

				if (something >= 2){
					return false;
				}else{
					if (!world.isRemote){
						++something;
						world.setBlockMetadataWithNotify(blockx, blocky, blockz, something << 2 | direction, 2);
					}

					return true;
				}
			}

			return true;
		}

		if (world.rand.nextInt(10) < 8)
			return true;

		// EoD: Apply vanilla bonemeal effect to growables. See ItemDye.applyBonemeal()
		if (block instanceof IGrowable){
			IGrowable igrowable = (IGrowable)block;
			//AMCore.log.getLogger().info("Grow component found IGrowable");

			if (igrowable.func_149851_a(world, blockx, blocky, blockz, world.isRemote)){
				if (!world.isRemote){
					if (igrowable.func_149852_a(world, world.rand, blockx, blocky, blockz)){
						igrowable.func_149853_b(world, world.rand, blockx, blocky, blockz);
					}
				}
				return true;
			}
		}

		//If the spell is executed in water, check if we have space for a wakebloom above
		if (block == Blocks.water){
			if (world.getBlock(blockx, blocky + 1, blockz) == Blocks.air){
				if (!world.isRemote){
					world.setBlock(blockx, blocky + 1, blockz, BlocksCommonProxy.wakebloom);
				}
				return true;
			}
		}

		//EoD: If there is already tallgrass present, let's grow it further.
		if (block == Blocks.tallgrass){
			if (Blocks.tallgrass.canBlockStay(world, blockx, blocky + 1, blockz)){
				if (!world.isRemote && random.nextInt(10) > 6){
					world.setBlock(blockx, blocky, blockz, Blocks.tallgrass, 1, 2);
				}
				return true;
			}
		}

		//EoD: If there is already deadbush present, let's revitalize it. This works only on podzol in vanilla MC.
		if (block == Blocks.deadbush){
			if (Blocks.tallgrass.canBlockStay(world, blockx, blocky, blockz)){
				if (!world.isRemote && random.nextInt(10) > 6){
					world.setBlock(blockx, blocky, blockz, Blocks.tallgrass, 1, 2);
				}
				return true;
			}
		}

		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 17.4f;
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
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "plant", x + 0.5, y + 1, z + 0.5);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
				particle.AddParticleController(new ParticleOrbitPoint(particle, x + 0.5, y + 0.5, z + 0.5, 2, false).setIgnoreYCoordinate(true).SetOrbitSpeed(0.1f).SetTargetDistance(0.3f + rand.nextDouble() * 0.3));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
				particle.setMaxAge(20);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.NATURE);
	}

	@Override
	public int getID(){
		return 22;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
				new ItemStack(Items.dye, 1, 15),
				BlocksCommonProxy.witchwoodLog
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.02f;
	}
}
