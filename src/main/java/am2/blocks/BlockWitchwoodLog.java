package am2.blocks;

import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;
import java.util.Random;

public class BlockWitchwoodLog extends BlockLog{

	@SideOnly(Side.CLIENT)
	private IIcon tree_side;
	@SideOnly(Side.CLIENT)
	private IIcon tree_top;

	protected BlockWitchwoodLog(){
		super();
		setHardness(3.0f);
		setResistance(3.0f);
		setHarvestLevel("axe", 2);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random par1Random){
		return 1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected IIcon getSideIcon(int par1){
		return this.tree_side;
	}

	@Override
	protected IIcon getTopIcon(int p_150161_1_){
		return this.tree_top;
	}

	/**
	 * returns a number between 0 and 3
	 */
	public static int limitToValidMetadata(int par0){
		return par0 & 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		tree_side = ResourceManager.RegisterTexture("Witchwood", par1IconRegister);
		tree_top = ResourceManager.RegisterTexture("WitchwoodTop", par1IconRegister);
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z){
		return true;
	}

	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z){
		return true;
	}
}
