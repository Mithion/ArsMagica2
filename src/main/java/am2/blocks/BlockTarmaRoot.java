package am2.blocks;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

public class BlockTarmaRoot extends AMFlower{

	static HashSet<Block> blockStones = null;

	protected BlockTarmaRoot(){
		super();
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z){
		return EnumPlantType.Cave;
	}

	//EoD: restrict Tarma Roots growth by the blocks in canPlaceBlockOn()
	@Override
	public boolean canBlockStay(World worldIn, int x, int y, int z){
		return canPlaceBlockOn(worldIn.getBlock(x, y - 1, z)) && super.canBlockStay(worldIn, x, y, z);
	}

	@Override
	protected boolean canPlaceBlockOn(Block block){
		if (block == Blocks.stone || block == Blocks.cobblestone){
			return true;
		}
		if (blockStones == null){// stone and cobblestone are defined by Forge, hence only first call will be 'true'
			HashSet<ItemStack> itemStackStones = new HashSet<ItemStack>();
			itemStackStones.addAll(OreDictionary.getOres("stone", false));
			itemStackStones.addAll(OreDictionary.getOres("stoneCobble", false));
			itemStackStones.addAll(OreDictionary.getOres("cobblestone", false));
			blockStones = new HashSet<Block>(itemStackStones.size());
			for (ItemStack itemStack : itemStackStones){
				Block oreBlock = Block.getBlockFromItem(itemStack.getItem());
				if (oreBlock != Blocks.air){
					blockStones.add(oreBlock);
				}
			}
		}
		return blockStones != null && blockStones.contains(block);
	}

	//EoD: Tarmas should only grow in dark places
	@Override
	public boolean canGrowOn(World worldIn, int x, int y, int z) {
		return canBlockStay(worldIn, x, y, z) && worldIn.getFullBlockLightValue(x, y, z) < 4;
	}
}
