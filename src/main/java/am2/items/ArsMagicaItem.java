package am2.items;

import net.minecraft.item.Item;

//class to identify items that are part of the Ars Magica mod, mainly used for rendering
public class ArsMagicaItem extends Item{

	public ArsMagicaItem setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}
}
