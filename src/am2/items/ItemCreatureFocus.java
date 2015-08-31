package am2.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import am2.texture.ResourceManager;

public class ItemCreatureFocus extends ItemFilterFocus {

	protected ItemCreatureFocus() {
		super();
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			" P ",
			"LFT",
			" W ",
			Character.valueOf('P'), Items.porkchop,
			Character.valueOf('B'), Items.leather,
			Character.valueOf('F'), ItemsCommonProxy.standardFocus,
			Character.valueOf('T'), Items.feather,
			Character.valueOf('W'), Blocks.wool,
		};
	}

	@Override
	public String getInGameName() {
		return "Creature Focus";
	}

	@Override
	public Class getFilterClass() {
		return EntityCreature.class;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("focus_seer_creature", par1IconRegister);
	}

}
