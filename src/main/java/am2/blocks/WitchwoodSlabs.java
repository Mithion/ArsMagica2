package am2.blocks;

import am2.texture.ResourceManager;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class WitchwoodSlabs extends BlockWoodSlab{

	public WitchwoodSlabs(boolean par2){
		super(par2);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
		this.setHarvestLevel("axe", 2);
		this.setStepSound(Block.soundTypeWood);
	}

	@Override
	public void registerBlockIcons(IIconRegister IIconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("plankWitchwood", IIconRegister);
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face){
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(BlocksCommonProxy.witchwoodSingleSlab));
		return drops;
	}

	@Override
	protected ItemStack createStackedBlock(int par1){
		return new ItemStack(BlocksCommonProxy.witchwoodSingleSlab, 2, par1 & 7);
	}

	@Override
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5){
		return this.blockIcon;
	}

	@Override
	public IIcon getIcon(int par1, int par2){
		return this.blockIcon;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}
}
