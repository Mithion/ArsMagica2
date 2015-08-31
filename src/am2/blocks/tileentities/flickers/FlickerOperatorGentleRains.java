package am2.blocks.tileentities.flickers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;

public class FlickerOperatorGentleRains implements IFlickerFunctionality{

	@Override
	public boolean RequiresPower() {
		return false;
	}

	@Override
	public int PowerPerOperation() {
		return 0;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered) {
		int radius = 6;
		int diameter = radius*2+1;
		if (!worldObj.isRemote){
			int effectX = ((TileEntity)habitat).xCoord - radius + (worldObj.rand.nextInt(diameter));
			int effectZ = ((TileEntity)habitat).zCoord - radius + (worldObj.rand.nextInt(diameter));
			int effectY = ((TileEntity)habitat).yCoord - 1;
			
			while (worldObj.isAirBlock(effectX, effectY, effectZ) && effectY > 0){
				effectY --;
			}
			
			while (!worldObj.isAirBlock(effectX, effectY, effectZ) && worldObj.getBlock(effectX, effectY, effectZ) != Blocks.farmland && effectY > 0){
				effectY ++;
			}
			
			effectY--;

			Block block = worldObj.getBlock(effectX, effectY, effectZ);
			if (block == Blocks.farmland && worldObj.getBlockMetadata(effectX, effectY, effectZ) < 15){
				worldObj.setBlockMetadataWithNotify(effectX, effectY, effectZ, 15, 2);
				return true;
			}
		}else{
			for (int i = 0; i < AMCore.config.getGFXLevel() * 2; ++i){
				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "water_ball", ((TileEntity)habitat).xCoord+0.5, ((TileEntity)habitat).yCoord+3, ((TileEntity)habitat).zCoord+0.5);
				if (particle != null){
					particle.setAffectedByGravity();
					particle.setMaxAge(10);
					particle.setDontRequireControllers();
					particle.setParticleScale(0.03f);
					particle.noClip = false;
					particle.addRandomOffset(diameter, 0, diameter);
				}
			}
		}

		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered) {
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers) {
		return 1;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				" B ",
				"CWT",
				" B ",
				Character.valueOf('C'), BlocksCommonProxy.essenceConduit,
				Character.valueOf('T'), BlocksCommonProxy.tarmaRoot,
				Character.valueOf('W'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.WATER.ordinal()),
				Character.valueOf('B'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE)
			};
	}

}
