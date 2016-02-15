package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityOcculus;
import am2.playerextensions.ExtendedProperties;

public class BlockOcculus extends AMSpecialRenderBlockContainer{
	
	public final static PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	
	protected BlockOcculus(){
		super(Material.rock);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityOcculus();
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, FACING);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);

		if (par1World.isRemote){
			if (ExtendedProperties.For(par5EntityPlayer).getMagicLevel() > 0)
				AMCore.proxy.openSkillTreeUI(par1World, par5EntityPlayer);
			else
				par5EntityPlayer.addChatMessage(new ChatComponentText("You cannot comprehend what you see inside the occulus."));
		}
		return true;
	}
}
