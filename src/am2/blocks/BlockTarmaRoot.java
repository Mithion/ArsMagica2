package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockTarmaRoot extends AMFlower{

	protected BlockTarmaRoot() {
		super();
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Cave;
	}

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		Block block = par1World.getBlock(par2, par3-1, par4);
		return block == Blocks.stone || block == Blocks.cobblestone;
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block block) {
		return block == Blocks.stone || block == Blocks.cobblestone;
	}
}
