package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class BlockMagicWall extends AMBlock{

	public BlockMagicWall(){
		super(Material.glass);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5){
		return new ArrayList<ItemStack>();
	}

	@Override
	public int quantityDropped(Random par1Random){
		return 0;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random){
		return 0;
	}
}
