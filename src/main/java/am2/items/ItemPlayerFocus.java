package am2.items;

import am2.texture.ResourceManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemPlayerFocus extends ItemFilterFocus{

	protected ItemPlayerFocus(){
		super();
	}

	@Override
	public Class getFilterClass(){
		return EntityPlayer.class;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"L",
				"F",
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.essence, 1, 8),
				Character.valueOf('F'), ItemsCommonProxy.standardFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Player Focus";
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		this.itemIcon = ResourceManager.RegisterTexture("focus_seer_player", par1IconRegister);
	}

}
