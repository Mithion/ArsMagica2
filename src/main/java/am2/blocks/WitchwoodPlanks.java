package am2.blocks;

import java.util.List;

import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WitchwoodPlanks extends BlockPlanks{

	public WitchwoodPlanks(){
		super();
		this.setHardness(2.0f);
		this.setResistance(2.0f);
		this.setHarvestLevel("axe", 2);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Override
//	public void registerBlockIcons(IIconRegister IIconRegister){
//		this.blockIcon = ResourceManager.RegisterTexture("plankWitchwood", IIconRegister);
//	}
//
//	@Override
//	public IIcon getIcon(int par1, int par2){
//		return this.blockIcon;
//	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List){
		par3List.add(new ItemStack(this));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this);
	}
}
