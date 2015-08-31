package am2.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import am2.api.spell.enums.Affinity;
import am2.entities.EntityFlicker;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlickerJar extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private IIcon flickerIcon;

	public ItemFlickerJar() {
		super();
		this.setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = ResourceManager.RegisterTexture("flickerjar_bottle", par1IconRegister);
		flickerIcon = ResourceManager.RegisterTexture("flickerjar_flicker", par1IconRegister);
	}

	@Override
	public int getRenderPasses(int metadata) {
		return metadata == 0 ? 1 : 2;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (stack.getItemDamage() == 0)
			return itemIcon;

		if (pass == 0)
			return flickerIcon;
		else
			return itemIcon;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		int meta = stack.getItemDamage();
		String baseName = StatCollector.translateToLocal("am2.item.flickerJar");
		if (meta == 0)
			return String.format(StatCollector.translateToLocal("item.arsmagica2:flickerJar.name"), StatCollector.translateToLocal("am2.tooltip.empty"));

		Affinity aff = Affinity.values()[meta];
		baseName = String.format(StatCollector.translateToLocal("item.arsmagica2:flickerJar.name"), toProperCase(aff.name()));

		return baseName;
	}

	private String toProperCase(String name) {
	    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
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
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
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
