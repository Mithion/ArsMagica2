package am2.items;

import am2.api.flickers.IFlickerFunctionality;
import am2.blocks.tileentities.flickers.FlickerOperatorRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemFlickerFocus extends ArsMagicaItem{

	public ItemFlickerFocus(){
		super();
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();
		IFlickerFunctionality operator = FlickerOperatorRegistry.instance.getOperatorForMask(meta);
		return String.format(StatCollector.translateToLocal("item.arsmagica2:FlickerFocusPrefix"), StatCollector.translateToLocal("item.arsmagica2:" + operator.getClass().getSimpleName() + ".name"));
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i : FlickerOperatorRegistry.instance.getMasks()){
			par3List.add(new ItemStack(this, 1, i));
		}
	}
}
