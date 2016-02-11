package am2.blocks;

import am2.blocks.tileentities.TileEntityEssenceConduit;
import am2.texture.ResourceManager;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEssenceConduit extends AMSpecialRenderPoweredBlock{

	public BlockEssenceConduit(){
		super(Material.cloth);
		setHardness(3.0f);
		setBlockBounds(0.125f, 0.0f, 0.125f, 0.875f, 1.0f, 0.875f);
	}

	@Override
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9){
		int var10 = par9;
		var10 = -1;

		if (par5 == 0){
			var10 = 1;
		}

		if (par5 == 1){
			var10 = 2;
		}

		if (par5 == 2){
			var10 = 3;
		}

		if (par5 == 3){
			var10 = 4;
		}

		if (par5 == 4){
			var10 = 5;
		}

		if (par5 == 5){
			var10 = 6;
		}

		return var10;
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityEssenceConduit();
	}
}
