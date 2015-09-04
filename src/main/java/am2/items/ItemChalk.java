package am2.items;

import am2.blocks.BlocksCommonProxy;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemChalk extends ArsMagicaItem{

	public ItemChalk(){
		setMaxDamage(50);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	public int getRenderPasses(int metadata){
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		this.itemIcon = ResourceManager.RegisterTexture("chalk_wizard", par1IconRegister);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){

		if (side != 1 || !canBeUsed(world, x, y + 1, z)){
			return false;
		}

		if (!world.isRemote){
			world.setBlock(x, y + 1, z, BlocksCommonProxy.wizardChalk, world.rand.nextInt(16), 2);
			stack.damageItem(1, player);
		}

		return false;
	}

	public boolean canBeUsed(World world, int x, int y, int z){
		if (world.getBlock(x, y - 1, z) == BlocksCommonProxy.wizardChalk){
			return false;
		}
		if (!world.isAirBlock(x, y, z)){
			return false;
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D(){
		return true;
	}
}