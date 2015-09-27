package am2.blocks;

import am2.texture.ResourceManager;
import net.minecraft.block.BlockWood;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class WitchwoodPlanks extends BlockWood{

	public WitchwoodPlanks(){
		super();
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face){
		return 0;
	}

	@Override
	public void registerIcons(IIconRegister IIconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("plankWitchwood", IIconRegister);
	}

	@Override
	public IIcon getIcon(int par1, int par2){
		return this.blockIcon;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return new ItemStack(this);
	}

	@Override
	public int getHarvestLevel(int metadata){
		return 2;
	}

	@Override
	public String getHarvestTool(int metadata){
		return "axe";
	}
}
