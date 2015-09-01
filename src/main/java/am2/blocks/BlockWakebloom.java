package am2.blocks;

import java.util.ArrayList;
import java.util.Random;

import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWakebloom extends AMFlower{

	protected BlockWakebloom() {
		super();
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Water;
	}
	
	@Override
	protected boolean canPlaceBlockOn(Block block) {
		return block == Blocks.water;
	}

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return par3 >= 0 && par3 < 256 ? par1World.getBlock(par2, par3 - 1, par4).getMaterial() == Material.water && par1World.getBlockMetadata(par2, par3 - 1, par4) == 0 : false;
    }

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(BlocksCommonProxy.wakebloom));
		return drops;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(BlocksCommonProxy.wakebloom);
	}
}
