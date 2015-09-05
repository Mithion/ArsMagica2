package am2.items;

import am2.entities.EntityAirSled;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemAirSled extends ArsMagicaItem{

	public ItemAirSled(){
		super();
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.air_sled"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		if (!world.isRemote){
			EntityAirSled sled = new EntityAirSled(world);
			sled.setPosition(x + hitX, y + hitY + 0.5, z + hitZ);
			world.spawnEntityInWorld(sled);

			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			return true;
		}
		return false;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(ItemsCommonProxy.airSledEnchanted.copy());
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}

}
