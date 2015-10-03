package am2.items;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class OreItem extends ItemBlock{

	public OreItem(){
		this(BlocksCommonProxy.AMOres);
	}

	public OreItem(Block block){
		super(block);
		setMaxDurability(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int par1){
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack){
		return super.getUnlocalizedName() + "." + par1ItemStack.getMetadata();
	}
}
