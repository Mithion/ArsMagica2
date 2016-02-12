package am2.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBindingCatalyst extends ArsMagicaItem{

	public static final int META_PICK = 0;
	public static final int META_AXE = 1;
	public static final int META_SWORD = 2;
	public static final int META_SHOVEL = 3;
	public static final int META_HOE = 4;
	public static final int META_BOW = 5;

	public ItemBindingCatalyst(){
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();

		String baseName = StatCollector.translateToLocal("item.arsmagica2:bindingCatalyst.name");

		switch (meta){
		case META_PICK:
			return baseName + StatCollector.translateToLocal("item.arsmagica2:bindingCatalystPick.name");
		case META_AXE:
			return baseName + StatCollector.translateToLocal("item.arsmagica2:bindingCatalystAxe.name");
		case META_SWORD:
			return baseName + StatCollector.translateToLocal("item.arsmagica2:bindingCatalystSword.name");
		case META_SHOVEL:
			return baseName + StatCollector.translateToLocal("item.arsmagica2:bindingCatalystShovel.name");
		case META_HOE:
			return baseName + StatCollector.translateToLocal("item.arsmagica2:bindingCatalystHoe.name");
		case META_BOW:
			return baseName + StatCollector.translateToLocal("item.arsmagica2:bindingCatalystBow.name");
		}
		return baseName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(par1, 1, META_PICK));
		par3List.add(new ItemStack(par1, 1, META_AXE));
		par3List.add(new ItemStack(par1, 1, META_SHOVEL));
		par3List.add(new ItemStack(par1, 1, META_SWORD));
		par3List.add(new ItemStack(par1, 1, META_HOE));
		//par3List.add(new ItemStack(par1, 1, META_BOW));
	}

}
