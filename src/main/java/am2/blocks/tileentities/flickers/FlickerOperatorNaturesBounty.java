package am2.blocks.tileentities.flickers;

import am2.AMCore;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class FlickerOperatorNaturesBounty implements IFlickerFunctionality{

	@Override
	public boolean RequiresPower(){
		return false;
	}

	@Override
	public int PowerPerOperation(){
		return 5;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered){
		return DoOperation(worldObj, habitat, powered, new Affinity[0]);
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
		int radius = 6;
		int diameter = radius * 2 + 1;
		boolean updatedOnce = false;
		if (!worldObj.isRemote){
			for (int i = 0; i < (powered ? 5 : 1); ++i){
				int effectX = ((TileEntity)habitat).xCoord - radius + (worldObj.rand.nextInt(diameter));
				int effectZ = ((TileEntity)habitat).zCoord - radius + (worldObj.rand.nextInt(diameter));
				int effectY = ((TileEntity)habitat).yCoord;

				while (worldObj.isAirBlock(effectX, effectY, effectZ) && effectY > 0){
					effectY--;
				}

				while (!worldObj.isAirBlock(effectX, effectY, effectZ) && effectY > 0){
					effectY++;
				}

				effectY--;


				Block block = worldObj.getBlock(effectX, effectY, effectZ);
				if (block instanceof IPlantable || block instanceof IGrowable){
					block.updateTick(worldObj, effectX, effectY, effectZ, worldObj.rand);
					updatedOnce = true;
				}
			}
		}else{
			int posY = ((TileEntity)habitat).yCoord;
			while (!worldObj.isAirBlock(((TileEntity)habitat).xCoord, posY, ((TileEntity)habitat).zCoord)){
				posY++;
			}
			posY--;
			for (int i = 0; i < AMCore.config.getGFXLevel() * 2; ++i){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "plant", ((TileEntity)habitat).xCoord + 0.5, posY + 0.5f, ((TileEntity)habitat).zCoord + 0.5);
				if (particle != null){

					particle.addRandomOffset(diameter, 0, diameter);
					particle.AddParticleController(new ParticleFloatUpward(particle, 0.01f, 0.04f, 1, false));
					particle.setMaxAge(16);
					particle.setParticleScale(0.08f);
				}
			}
		}

		if (powered){
			for (Affinity aff : flickers){
				if (aff == Affinity.WATER)
					FlickerOperatorRegistry.instance.getOperatorForMask(Affinity.WATER.getAffinityMask()).DoOperation(worldObj, habitat, powered);
			}
		}

		return updatedOnce;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered){
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return powered ? 1 : 100;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers){
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"BAB",
				"LNW",
				"BGB",
				Character.valueOf('B'), new ItemStack(Items.dye, 1, 15),
				Character.valueOf('G'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
				Character.valueOf('N'), new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_NATURE),
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.LIFE.ordinal()),
				Character.valueOf('A'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.NATURE.ordinal()),
				Character.valueOf('W'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.WATER.ordinal())

		};
	}

}
