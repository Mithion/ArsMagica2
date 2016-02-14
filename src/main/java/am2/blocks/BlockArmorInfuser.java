package am2.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityArmorImbuer;
import am2.guis.ArsMagicaGuiIdList;

public class BlockArmorInfuser extends PoweredBlock{

	@SideOnly(Side.CLIENT)

	protected BlockArmorInfuser(){
		super(Material.iron);
		setHardness(4.0f);
		setResistance(4.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityArmorImbuer();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos , IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);
		if (handleSpecialItems(par1World, par5EntityPlayer, pos)){
			return true;
		}
		if (!par1World.isRemote){
			super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);
			par5EntityPlayer.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_ARMOR_INFUSION, par1World, pos.getX(), pos.getY(), pos.getZ());
			/*
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4), par5EntityPlayer, KeystoneAccessType.USE)){
				super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
				FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ARMOR_INFUSION, par1World, par2, par3, par4);
			}
			*/
		}
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos,
			IBlockState state, int fortune) {
		TileEntityArmorImbuer imbuer = (TileEntityArmorImbuer)world.getTileEntity(pos);
		if (imbuer == null)
			return super.getDrops(world, pos, state, fortune);
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		for (int i = 0; i < imbuer.getSizeInventory() -3; i++)
			stack.add(imbuer.getStackInSlot(i));
		
		return stack;
	}
}
