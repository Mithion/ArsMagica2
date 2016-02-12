package am2.blocks;

import am2.blocks.tileentities.TileEntitySlipstreamGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSlipstreamGenerator extends PoweredBlock{

	public BlockSlipstreamGenerator(){
		super(Material.wood);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntitySlipstreamGenerator();
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}
