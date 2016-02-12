package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import am2.blocks.tileentities.TileEntityCraftingAltar;

public class BlockCraftingAltar extends PoweredBlock{

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
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public int getRenderType(){
		return 2;
	}

	@SideOnly(Side.CLIENT)
	public Block getAltarMimicBlock(IBlockAccess world, BlockPos pos){

		TileEntity te = world.getTileEntity(pos);

		if (te == null || !(te instanceof TileEntityCraftingAltar) || !((TileEntityCraftingAltar)te).structureValid())
			return this;

		Block[] blocks = new Block[4];
		blocks[0] = world.getBlockState(pos).getBlock();
		blocks[1] = world.getBlockState(pos).getBlock();
		blocks[2] = world.getBlockState(pos).getBlock();
		blocks[3] = world.getBlockState(pos).getBlock();


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
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		if (super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9)){
			TileEntity te = par1World.getTileEntity(pos);
			if (te != null && te instanceof TileEntityCraftingAltar){
				((TileEntityCraftingAltar)te).deactivate();
			}
			return false;
		}
		return true;
	}
}
