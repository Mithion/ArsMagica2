package am2.items;

import am2.blocks.BlocksCommonProxy;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemAMBucket extends ItemBucket{

	public ItemAMBucket(){
		super(BlocksCommonProxy.liquidEssence);
		this.setContainerItem(Items.bucket);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		this.itemIcon = ResourceManager.RegisterTexture("liquidEssenceBucket", par1IconRegister);
	}

	public ItemAMBucket setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

}
