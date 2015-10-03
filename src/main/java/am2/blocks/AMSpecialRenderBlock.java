package am2.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public abstract class AMSpecialRenderBlock extends AMBlock{

	protected AMSpecialRenderBlock(Material material){
		super(material);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5){
		return null;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}
