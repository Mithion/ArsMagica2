package am2.blocks;

import am2.blocks.tileentities.TileEntityFlickerLure;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFlickerLure extends PoweredBlock{

	public BlockFlickerLure(){
		super(Material.rock);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityFlickerLure();
	}
}
