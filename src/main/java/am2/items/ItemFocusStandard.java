package am2.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import am2.texture.ResourceManager;

public class ItemFocusStandard extends ItemFocus implements ISpellFocus {

	public ItemFocusStandard() {
		super();
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			" R ", "RFR", " R ",
			'R', Items.redstone,
			'F', ItemsCommonProxy.lesserFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Focus";
	}

	@Override
	public int getFocusLevel() {
		return 1;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("focus_standard", par1IconRegister);
	}
}
