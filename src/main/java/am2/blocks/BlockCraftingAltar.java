package am2.blocks;

import am2.blocks.tileentities.TileEntityCraftingAltar;
import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCraftingAltar extends PoweredBlock{

	@SideOnly(Side.CLIENT)
	private IIcon IIcon;

	public BlockCraftingAltar(){
		super(Material.rock);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityCraftingAltar();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess iBlockAccess, BlockPos pos, int meta){
		return IIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if (meta == 1)
			return IIcon;
		return blockIcon;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

	@SideOnly(Side.CLIENT)
	public Block getAltarMimicBlock(IBlockAccess world, int x, int y, int z){

		TileEntity te = world.getTileEntity(x, y, z);

		if (te == null || !(te instanceof TileEntityCraftingAltar) || !((TileEntityCraftingAltar)te).structureValid())
			return this;

		Block[] blocks = new Block[4];
		blocks[0] = world.getBlock(x - 1, y, z);
		blocks[1] = world.getBlock(x + 1, y, z);
		blocks[2] = world.getBlock(x, y, z + 1);
		blocks[3] = world.getBlock(x, y, z - 1);


		if (blocks[0] != Blocks.air && blocks[0] == blocks[1]){
			return blocks[0];
		}else if (blocks[2] != Blocks.air && blocks[2] == blocks[3]){
			return blocks[2];
		}

		return this;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister){
		blockIcon = ResourceManager.RegisterTexture("CasterRuneSide", par1IconRegister);
		IIcon = ResourceManager.RegisterTexture("RuneStone", par1IconRegister);
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if (super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9)){
			TileEntity te = par1World.getTileEntity(par2, par3, par4);
			if (te != null && te instanceof TileEntityCraftingAltar){
				((TileEntityCraftingAltar)te).deactivate();
			}
			return false;
		}
		return true;
	}
}
