package am2.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import am2.texture.ResourceManager;

public class ItemFocusMana extends ItemFocus {

	public ItemFocusMana() {
		super();
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			"P", "F", "P",
			'P', new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST),
			'F', ItemsCommonProxy.standardFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Mana Focus";
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("focus_machinery_mana", par1IconRegister);
	}
}
