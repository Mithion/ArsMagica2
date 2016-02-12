package am2.items;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemAMBucket extends ItemBucket{

	public ItemAMBucket(){
		super(BlocksCommonProxy.liquidEssence);
		this.setContainerItem(Items.bucket);
	}

	public ItemAMBucket setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		return this;
	}

}
