package am2.items;

import net.minecraft.item.Item;

//class to identify items that are part of the Ars Magica mod, mainly used for rendering
public class ArsMagicaRotatedItem extends Item{

	public ArsMagicaRotatedItem(){
		super();
	}

	public ArsMagicaRotatedItem setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	@Override
	public boolean isFull3D(){
		return true;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
}
