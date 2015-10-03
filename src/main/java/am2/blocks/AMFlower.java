package am2.blocks;

import am2.texture.ResourceManager;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class AMFlower extends BlockFlower{

	protected AMFlower(){
		super(1);
		setStepSound(soundTypeGrass);
	}

	public AMFlower setUnlocalizedNameAndID(String name){
		setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	@Override
	public void registerIcons(IIconRegister register){
		this.blockIcon = ResourceManager.RegisterTexture(this.textureName, register);
	}

	@Override
	public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side){
		return this.blockIcon;
	}

	@Override
	public IIcon getIcon(int side, int meta){
		return blockIcon;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
	}


}
