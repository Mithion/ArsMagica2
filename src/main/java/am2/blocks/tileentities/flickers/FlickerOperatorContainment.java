package am2.blocks.tileentities.flickers;

import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FlickerOperatorContainment implements IFlickerFunctionality{

	protected static final int BASE_RADIUS = 6;

	protected void setUtilityBlock(World world, int x, int y, int z, int meta){

		if (world.getBlock(x, y, z) == BlocksCommonProxy.invisibleUtility){
			int exMeta = world.getBlockMetadata(x, y, z);
			if (meta == exMeta) return;
			if ((meta == 3 || meta == 4) && exMeta == 7) return;
			if ((meta == 6 || meta == 5) && exMeta == 8) return;
			if ((meta == 3 && exMeta == 4) || (meta == 4 && exMeta == 3))
				meta = 7;
			else if ((meta == 5 && exMeta == 6) || (meta == 6 && exMeta == 5))
				meta = 8;

			world.setBlock(x, y, z, BlocksCommonProxy.invisibleUtility, meta, 2);
		}else{
			if (world.isAirBlock(x, y, z))
				world.setBlock(x, y, z, BlocksCommonProxy.invisibleUtility, meta, 2);
		}
	}

	protected void clearUtilityBlock(World world, int x, int y, int z){
		if (world.getBlock(x, y, z) == BlocksCommonProxy.invisibleUtility){
			world.setBlockToAir(x, y, z);
		}
	}

	protected void setLastRadius(IFlickerController habitat, int radius){
		habitat.setMetadata(this, new AMDataWriter().add(radius).generate());
	}

	protected int getLastRadius(IFlickerController habitat){
		byte[] meta = habitat.getMetadata(this);
		if (meta == null || meta.length == 0)
			return BASE_RADIUS;
		AMDataReader rdr = new AMDataReader(meta, false);
		return rdr.getInt();
	}

	protected int calculateRadius(Affinity[] flickers){
		int rad = BASE_RADIUS;
		for (Affinity aff : flickers){
			if (aff == Affinity.ICE){
				rad++;
			}
		}
		return rad;
	}

	@Override
	public boolean RequiresPower(){
		return true;
	}

	@Override
	public int PowerPerOperation(){
		return 5;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered){
		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
		if (worldObj.isRemote)
			return true;

		int lastRadius = getLastRadius(habitat);
		int calcRadius = calculateRadius(flickers);

		if (lastRadius != calcRadius){
			RemoveOperator(worldObj, habitat, powered, flickers);
		}

		boolean hasArcaneAugment = false;
		for (Affinity aff : flickers){
			if (aff == Affinity.ARCANE){
				hasArcaneAugment = true;
				break;
			}
		}

		for (int i = 0; i < calcRadius * 2 + 1; ++i){

			if (hasArcaneAugment){
				//-x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + calcRadius + 1 - i, 9);
				//+x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius + i, 9);
				//-z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius, 9);
				//+z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1 - i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + calcRadius + 1, 9);
			}else{
				//-x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + calcRadius + 1 - i, i == 0 ? 9 : 3);
				//+x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius + i, i == 0 ? 9 : 4);
				//-z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius, i == 0 ? 9 : 5);
				//+z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1 - i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + calcRadius + 1, i == 0 ? 9 : 6);
			}
		}

		setLastRadius(habitat, calcRadius);

		return true;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered){
		int radius = getLastRadius(habitat);

		for (int i = 0; i < radius * 2 + 1; ++i){
			//-x
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - radius, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + radius + 1 - i);
			//+x
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + radius + 1, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - radius + i);
			//-z
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - radius + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - radius);
			//+z
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + radius + 1 - i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + radius + 1);
		}
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return 200;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
		RemoveOperator(worldObj, habitat, powered);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"FWF",
				"ARN",
				"IWI",
				Character.valueOf('F'), Blocks.fence,
				Character.valueOf('W'), Blocks.cobblestone_wall,
				Character.valueOf('A'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.AIR.ordinal()),
				Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				Character.valueOf('N'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.ENDER.ordinal()),
				Character.valueOf('I'), Blocks.iron_bars

		};
	}
}
