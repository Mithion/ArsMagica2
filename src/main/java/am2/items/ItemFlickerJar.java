package am2.items;

import am2.api.spell.enums.Affinity;
import am2.entities.EntityFlicker;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemFlickerJar extends ArsMagicaItem{

	public ItemFlickerJar(){
		super();
		this.setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();
		String baseName = StatCollector.translateToLocal("am2.item.flickerJar");
		if (meta == 0)
			return String.format(StatCollector.translateToLocal("item.arsmagica2:flickerJar.name"), StatCollector.translateToLocal("am2.tooltip.empty"));

		Affinity aff = Affinity.values()[meta];
		baseName = String.format(StatCollector.translateToLocal("item.arsmagica2:flickerJar.name"), toProperCase(aff.name()));

		return baseName;
	}

	private String toProperCase(String name){
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass){
		if (stack.getItemDamage() > 0 && pass == 0){
			int meta = stack.getItemDamage();
			Affinity aff = Affinity.values()[meta];
			return aff.color;
		}else{
			return 0xFFFFFF;
		}
	}

	public void setFlickerJarTypeFromFlicker(ItemStack stack, EntityFlicker flick){
		stack.setItemDamage(flick.getFlickerAffinity().ID);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (Affinity aff : Affinity.values()){
			par3List.add(new ItemStack(this, 1, aff.ID));
		}
	}

	public int getMask(ItemStack stack){
		if (stack == null)
			return 0;
		if (stack.getItem() instanceof ItemFlickerJar){
			Affinity aff = Affinity.getByID(stack.getItemDamage());
			if (aff != null){
				return aff.getAffinityMask();
			}
		}
		return 0;
	}
}
