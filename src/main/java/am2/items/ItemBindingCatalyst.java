package am2.items;

import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

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
	public void registerIcons(IIconRegister IIconRegister){
		itemIcon = ResourceManager.RegisterTexture("bindingCatalyst", IIconRegister);
	}

	@Override
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage){
		return itemIcon;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int dmg, int pass){
		if (pass == 0){
			switch (dmg){
			case META_PICK:
				return ItemsCommonProxy.BoundPickaxe.getIconFromDamage(0);
			case META_AXE:
				return ItemsCommonProxy.BoundAxe.getIconFromDamage(0);
			case META_SHOVEL:
				return ItemsCommonProxy.BoundShovel.getIconFromDamage(0);
			case META_SWORD:
				return ItemsCommonProxy.BoundSword.getIconFromDamage(0);
			case META_HOE:
				return ItemsCommonProxy.BoundHoe.getIconFromDamage(0);
			case META_BOW:
				return Items.bow.getIconFromDamage(0);
			}
		}

		return itemIcon;
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
