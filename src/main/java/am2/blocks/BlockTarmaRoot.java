package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockTarmaRoot extends AMFlower{

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
		return block == Blocks.stone || block == Blocks.cobblestone;
	}
}
