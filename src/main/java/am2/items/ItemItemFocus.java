package am2.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import am2.texture.ResourceManager;

public class ItemItemFocus extends ItemFilterFocus{

	protected ItemItemFocus() {
		super();
	}

	@Override
	public Class getFilterClass() {
		return EntityItem.class;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
				" C ",
				"PFS",
				" W ",
				Character.valueOf('C'), Blocks.cobblestone,
				Character.valueOf('B'), Items.stone_pickaxe,
				Character.valueOf('F'), ItemsCommonProxy.standardFocus,
				Character.valueOf('T'), Items.iron_shovel,
				Character.valueOf('W'), Blocks.crafting_table
		};
	}

	@Override
	public String getInGameName() {
		return "Item Focus";
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("focus_seer_item", par1IconRegister);
	}
}
