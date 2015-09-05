package am2.items;

import am2.guis.AMGuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArcaneCompendium extends ArsMagicaItem{

	public ItemArcaneCompendium(){
		super();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if (world.isRemote){
			AMGuiHelper.OpenCompendiumGui(stack);
		}
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		return false;
	}

}
