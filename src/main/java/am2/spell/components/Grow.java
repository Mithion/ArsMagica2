package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.blocks.AMFlower;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleOrbitPoint;
import am2.utility.DummyEntityPlayer;
import jdk.nashorn.internal.codegen.Emitter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;


public class Grow implements ISpellComponent{

	private final static ArrayList<AMFlower> growableAMflowers = new ArrayList<AMFlower>(Arrays.asList(
			BlocksCommonProxy.cerublossom, BlocksCommonProxy.desertNova, BlocksCommonProxy.wakebloom, BlocksCommonProxy.aum, BlocksCommonProxy.tarmaRoot));

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing face,double impactX, double impactY, double impactZ, EntityLivingBase caster){

		Block block = world.getBlockState(pos).getBlock();

		BonemealEvent event = new BonemealEvent(DummyEntityPlayer.fromEntityLiving(caster), world, pos, block.getDefaultState());
		if (MinecraftForge.EVENT_BUS.post(event)){
			return false;
		}
		if (event.getResult() == Result.ALLOW){
			return true;
		}

		//EoD: Spawn AM2 flowers with 3% chance. This has to be the first one in the list to override all others
		if (world.rand.nextInt(100) < 3 && block.isNormalCube() &&
				(world.isAirBlock(pos.up()) || world.getBlockState(pos.up()) == Blocks.tallgrass.getDefaultState())){
			// shuffle the flower list every time we want to try to find one.
			Collections.shuffle(growableAMflowers);

			for (AMFlower flower : growableAMflowers){
				if (flower.canGrowOn(world, pos.up(), world.getBlockState(pos))){
					if (!world.isRemote){
						world.setBlockState(pos.up(), flower.getStateFromMeta(0), 2);
					}
					return true;
				}
			}
			// We did not find a flower or we have been executed on the wrong block. Either way, we continue
		}

		//Grow huge mushrooms 10% of the time.
		if (block instanceof BlockMushroom){
			if (!world.isRemote && world.rand.nextInt(10) < 1){
				((BlockMushroom)block).grow(world, world.rand, pos, Blocks.brown_mushroom.getDefaultState()); // TODO fine tune
			}

			return true;
		}


		//If the spell is executed in water, check if we have space for a wakebloom above and create one 3% of the time.
		if (block == Blocks.water){
			if (world.isAirBlock(pos.up())){
				if (!world.isRemote && world.rand.nextInt(100) < 3){
					world.setBlockState(pos.up(), BlocksCommonProxy.wakebloom.getDefaultState());
				}
				return true;
			}
		}

		//EoD: If there is already tallgrass present, let's grow it further 20% of the time.
		if (block == Blocks.tallgrass){
			if (Blocks.tallgrass.canBlockStay(world, pos.up(), Blocks.tallgrass.getDefaultState())){
				if (!world.isRemote && world.rand.nextInt(10) < 2){
					world.setBlockState(pos, Blocks.tallgrass.getStateFromMeta(1), 2);
				}
				return true;
			}
		}

		//EoD: If there is already deadbush present, let's revitalize it 20% of the time.
		//     This works only on podzol in vanilla MC.
		if (block == Blocks.deadbush){
			if (Blocks.tallgrass.canBlockStay(world, pos, Blocks.tallgrass.getDefaultState())){
				if (!world.isRemote && world.rand.nextInt(10) < 2){
                    world.setBlockState(pos, Blocks.tallgrass.getStateFromMeta(1), 2);
				}
				return true;
			}
		}

		// EoD: Apply vanilla bonemeal effect to growables 30% of the time. This is the generic grow section.
		//      See ItemDye.applyBonemeal().
		if (block instanceof IGrowable){
			IGrowable igrowable = (IGrowable)block;
			//AMCore.log.getLogger().info("Grow component found IGrowable");

			if (igrowable.canGrow(world, pos, world.getBlockState(pos), world.isRemote)){
				if (!world.isRemote && world.rand.nextInt(10) < 3){
					if (igrowable.canUseBonemeal(world, world.rand, pos, world.getBlockState(pos))){
						igrowable.grow(world, world.rand, pos, world.getBlockState(pos));
					}
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
