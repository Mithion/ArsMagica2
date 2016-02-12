package am2.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class InscriptionTableUpgrade extends ArsMagicaItem{

	public InscriptionTableUpgrade(){
		super();
		setMaxDamage(0);
		setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < 2; ++i){
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_){
		int meta = p_77653_1_.getItemDamage();
		switch (meta){
		case 2:
			return StatCollector.translateToLocal("item.arsmagica2:inscup_3.name");
		case 1:
			return StatCollector.translateToLocal("item.arsmagica2:inscup_2.name");
		case 0:
		default:
			return StatCollector.translateToLocal("item.arsmagica2:inscup_1.name");
		}
	}
}
