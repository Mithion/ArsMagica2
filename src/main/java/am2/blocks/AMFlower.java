package am2.blocks;

import am2.texture.ResourceManager;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class AMFlower extends BlockFlower{

	protected AMFlower(){
		super(1);
		setStepSound(soundTypeGrass);
	}

	public AMFlower setUnlocalizedNameAndID(String name){
		setBlockName(name);
		setBlockTextureName(name);
		return this;
	}

	@Override
	public void registerBlockIcons(IIconRegister register){
		this.blockIcon = ResourceManager.RegisterTexture(this.textureName, register);
	}

	@Override
	public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_){
		return this.blockIcon;
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_){
		return blockIcon;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
	}

	public boolean canGrowOn(World worldIn, BlockPos pos) {
		return canBlockStay(worldIn, pos);
	}
}
