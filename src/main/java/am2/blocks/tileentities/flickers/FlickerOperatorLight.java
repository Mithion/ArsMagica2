package am2.blocks.tileentities.flickers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;

public class FlickerOperatorLight implements IFlickerFunctionality{

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
		if (!worldObj.isRemote){
			int radius = 16;
			int yRadius = radius/4;
			int checksPerOperation = 8;

			int checkX = ((TileEntity)habitat).xCoord - radius;
			int checkY = ((TileEntity)habitat).yCoord - yRadius;
			int checkZ = ((TileEntity)habitat).zCoord - radius;

			byte[] meta = habitat.getMetadata(this);

			if (meta.length != 0){
				AMDataReader rdr = new AMDataReader(meta, false);
				checkX = rdr.getInt();
				checkY = rdr.getInt();
				checkZ = rdr.getInt();
			}

			for (int i = 0; i < checksPerOperation; ++i){

				int light = worldObj.getBlockLightValue(checkX, checkY, checkZ);

				if (light < 10 && worldObj.isAirBlock(checkX, checkY, checkZ)){
					worldObj.setBlock(checkX, checkY, checkZ, BlocksCommonProxy.invisibleUtility, 10, 2);
				}

				checkX++;
				if (checkX > ((TileEntity)habitat).xCoord + radius)
				{
					checkX = ((TileEntity)habitat).xCoord - radius;
					checkY++;
					if (checkY > ((TileEntity)habitat).yCoord + yRadius){
						checkY = ((TileEntity)habitat).yCoord - yRadius;
						checkZ++;
						if (checkZ > ((TileEntity)habitat).zCoord + radius){
							checkZ = ((TileEntity)habitat).zCoord - radius;
						}
					}
				}
			}

			AMDataWriter writer = new AMDataWriter();
			writer.add(checkX).add(checkY).add(checkZ);

			habitat.setMetadata(this, writer.generate());
		}else{
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "sparkle", ((TileEntity)habitat).xCoord + 0.5, ((TileEntity)habitat).yCoord + 1, ((TileEntity)habitat).zCoord + 0.5);
			if (particle != null){
				particle.addRandomOffset(0.5, 0.4, 0.5);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.02f, 1, false));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f));
				particle.setMaxAge(20);
			}
		}

		return true;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered) {
		habitat.removeMetadata(this);

		if (!worldObj.isRemote){
			int radius = 28;
			int yRadius = radius/4;

			for (int i = ((TileEntity)habitat).xCoord-radius; i <= ((TileEntity)habitat).xCoord+radius; ++i){
				for (int j = ((TileEntity)habitat).yCoord-yRadius; j <= ((TileEntity)habitat).yCoord+yRadius; ++j){
					for (int k = ((TileEntity)habitat).zCoord-radius; k <= ((TileEntity)habitat).zCoord+radius; ++k){
						Block block = worldObj.getBlock(i, j, k);
						if (block == BlocksCommonProxy.invisibleUtility){
							int meta = worldObj.getBlockMetadata(i, j, k);
							if (meta == 10){
								worldObj.setBlockToAir(i, j, k);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers) {
		return 10;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
		RemoveOperator(worldObj, habitat, powered);
	}


	@Override
	public Object[] getRecipe() {
		return new Object[]{
				"ISI",
				"F L",
				"ISI",
				Character.valueOf('F'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.FIRE.ordinal()),
				Character.valueOf('S'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE),
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.LIGHTNING.ordinal()),
				Character.valueOf('I'), ItemsCommonProxy.liquidEssenceBottle

			};
	}

}
