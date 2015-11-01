package am2.blocks.liquid;

import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockLiquidEssence extends BlockFluidClassic{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public static final Fluid liquidEssenceFluid = new FluidEssence();
	public static final Material liquidEssenceMaterial = new MaterialLiquid(MapColor.iceColor);

	public BlockLiquidEssence(){
		super(liquidEssenceFluid, liquidEssenceMaterial);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		return 9;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		icons = new IIcon[2];

		icons[0] = ResourceManager.RegisterTexture("liquidEssenceStill", par1IconRegister);
		icons[1] = ResourceManager.RegisterTexture("liquidEssenceFlowing", par1IconRegister);

		liquidEssenceFluid.setIcons(icons[0], icons[1]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if (side <= 1)
			return icons[0]; //still
		else
			return icons[1]; //flowing
	}

}
