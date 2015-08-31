package am2.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.blocks.tileentities.TileEntityCandle;
import am2.items.ItemsCommonProxy;

public class BlockCandle extends AMSpecialRenderBlockContainer{

	protected BlockCandle() {
		super(Material.wood);
		setHardness(1.0f);
		setResistance(1.0f);
		setBlockBounds(0.35f, 0f, 0.35f, 0.65f, 0.45f, 0.65f);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return 14;
	}

	@Override
	public int getRenderType() {
		return BlocksCommonProxy.blockRenderID;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(ItemsCommonProxy.wardingCandle));
		return drops;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(ItemsCommonProxy.wardingCandle);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityCandle();
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		world.spawnParticle("flame", x + 0.5, y + 0.65, z + 0.5, 0, 0, 0);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
	}
}
