package am2.items;

import am2.api.flickers.IFlickerFunctionality;
import am2.blocks.tileentities.flickers.FlickerOperatorRegistry;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.List;

public class ItemFlickerFocus extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private HashMap<Integer, IIcon> flickerIcons;

	public ItemFlickerFocus(){
		super();
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getMetadata();
		IFlickerFunctionality operator = FlickerOperatorRegistry.instance.getOperatorForMask(meta);
		return String.format(StatCollector.translateToLocal("item.arsmagica2:FlickerFocusPrefix"), StatCollector.translateToLocal("item.arsmagica2:" + operator.getClass().getSimpleName() + ".name"));
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i : FlickerOperatorRegistry.instance.getMasks()){
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		flickerIcons = new HashMap<Integer, IIcon>();
		for (int i : FlickerOperatorRegistry.instance.getMasks()){
			IFlickerFunctionality operator = FlickerOperatorRegistry.instance.getOperatorForMask(i);
			flickerIcons.put(i, ResourceManager.RegisterTexture(operator.getClass().getSimpleName(), par1IconRegister));
		}

		this.itemIcon = ResourceManager.RegisterTexture("flicker_focus_frame", par1IconRegister);
	}

	@Override
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	public int getRenderPasses(int metadata){
		return 2;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int par2){
		if (par2 == 0)
			return getIconFromDamage(par1);
		else
			return this.itemIcon;
	}

	@Override
	public IIcon getIconFromDamage(int par1){
		IIcon icon = flickerIcons.get(par1);
		if (icon != null)
			return icon;
		return this.itemIcon;
	}
}
