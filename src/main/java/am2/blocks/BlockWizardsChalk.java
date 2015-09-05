package am2.blocks;

import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockWizardsChalk extends AMSpecialRenderBlock{

	@SideOnly(Side.CLIENT)
	private IIcon[] blockIcons;

	public BlockWizardsChalk(){
		super(Material.circuits);
		setBlockBounds(0, 0, 0, 1, 0.02f, 1);
	}

	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z){
		return 0;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		blockIcons = new IIcon[16];

		for (int i = 0; i < 16; ++i)
			blockIcons[i] = ResourceManager.RegisterTexture("wizardchalk/WizardChalk" + (i + 1), par1IconRegister);
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

	@Override
	public IIcon getIcon(int side, int meta){
		return blockIcons[meta];
	}

	@Override
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		return new ArrayList<ItemStack>();
	}

	@Override
	public float getAmbientOcclusionLightValue(){
		return 1.0f;
	}

	@Override
	public int getBlockColor(){
		return 0xFFFFFF;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
								  int x, int y, int z){
		return new ItemStack(ItemsCommonProxy.wizardChalk);
	}
}
