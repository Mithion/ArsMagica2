package am2.blocks.tileentities.flickers;

import am2.api.flickers.IFlickerController;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FlickerOperatorInterdiction extends FlickerOperatorContainment{

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
		if (worldObj.isRemote)
			return true;

		boolean hasEnderAugment = false;
		for (Affinity aff : flickers){
			if (aff == Affinity.ENDER){
				hasEnderAugment = true;
				break;
			}
		}

		int lastRadius = getLastRadius(habitat);
		int calcRadius = calculateRadius(flickers);

		if (lastRadius != calcRadius){
			RemoveOperator(worldObj, habitat, powered, flickers);
		}

		for (int i = 0; i < calcRadius * 2 + 1; ++i){
			if (hasEnderAugment){
				//-x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius + i, 9);
				//+x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius + i, 9);
				//-z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius, 9);
				//+z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1 - i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + calcRadius + 1, 9);
			}else{
				//-x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius + i, 9);
				//+x
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius + i, 9);
				//-z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - calcRadius + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - calcRadius, 9);
				//+z
				setUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + calcRadius + 1 - i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + calcRadius + 1, 9);
			}
		}

		setLastRadius(habitat, calcRadius);

		return true;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered){
		int radius = 6;

		for (int i = 0; i < radius * 2 + 1; ++i){
			//-x
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - radius, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - radius + i);
			//+x
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + radius + 1, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - radius + i);
			//-z
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord - radius + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord - radius);
			//+z
			clearUtilityBlock(worldObj, ((TileEntity)habitat).xCoord + radius + 1 - i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + radius + 1);
		}
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"FWF",
				"ARN",
				"IWI",
				Character.valueOf('F'), Blocks.fence,
				Character.valueOf('W'), Blocks.cobblestone_wall,
				Character.valueOf('A'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.ARCANE.ordinal()),
				Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				Character.valueOf('N'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.AIR.ordinal()),
				Character.valueOf('I'), Blocks.iron_bars

		};
	}
}
