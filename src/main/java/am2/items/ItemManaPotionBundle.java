package am2.items;

import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemManaPotionBundle extends ArsMagicaItem{

	private IIcon[] icons;
	private final String[] textureFiles = {"potion_bundle_lesser", "potion_bundle_standard", "potion_bundle_greater", "potion_bundle_epic", "potion_bundle_legendary"};

	public ItemManaPotionBundle(){
		super();
		this.setMaxDurability(0);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		icons = new IIcon[textureFiles.length];

		for (int i = 0; i < textureFiles.length; ++i){
			icons[i] = ResourceManager.RegisterTexture(textureFiles[i], par1IconRegister);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1){
		Item potion = getPotion(par1);
		if (potion == ItemsCommonProxy.lesserManaPotion){
			return icons[0];
		}else if (potion == ItemsCommonProxy.standardManaPotion){
			return icons[1];
		}else if (potion == ItemsCommonProxy.greaterManaPotion){
			return icons[2];
		}else if (potion == ItemsCommonProxy.epicManaPotion){
			return icons[3];
		}else if (potion == ItemsCommonProxy.legendaryManaPotion){
			return icons[4];
		}
		return icons[0];
	}

	private Item getPotion(int damage){
		int id = damage >> 8;
		switch (id){
		case 0:
			return ItemsCommonProxy.lesserManaPotion;
		case 1:
			return ItemsCommonProxy.standardManaPotion;
		case 2:
			return ItemsCommonProxy.greaterManaPotion;
		case 3:
			return ItemsCommonProxy.epicManaPotion;
		case 4:
			return ItemsCommonProxy.legendaryManaPotion;
		}
		return ItemsCommonProxy.lesserManaPotion;
	}

	private int getUses(int damage){
		return (damage & 0x0F);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack){
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		ExtendedProperties props = ExtendedProperties.For(par3EntityPlayer);
		if (props.getCurrentMana() < props.getMaxMana()){
			par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		}
		return par1ItemStack;
	}


	@Override
	public ItemStack onItemUseFinish(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		Item potion = getPotion(par1ItemStack.getMetadata());
		if (potion == ItemsCommonProxy.lesserManaPotion){
			ItemsCommonProxy.lesserManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemsCommonProxy.standardManaPotion){
			ItemsCommonProxy.standardManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemsCommonProxy.greaterManaPotion){
			ItemsCommonProxy.greaterManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemsCommonProxy.epicManaPotion){
			ItemsCommonProxy.epicManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemsCommonProxy.legendaryManaPotion){
			ItemsCommonProxy.legendaryManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}

		par1ItemStack.setMetadata(((par1ItemStack.getMetadata() >> 8) << 8) + getUses(par1ItemStack.getMetadata()) - 1);

		if (getUses(par1ItemStack.getMetadata()) == 0){
			giveOrDropItem(par3EntityPlayer, new ItemStack(Items.string));
			par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null);
		}

		giveOrDropItem(par3EntityPlayer, new ItemStack(Items.glass_bottle));

		return par1ItemStack;
	}

	private void giveOrDropItem(EntityPlayer player, ItemStack stack){
		if (!player.inventory.addItemStackToInventory(stack))
			player.dropPlayerItemWithRandomChoice(stack, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		Item potion = getPotion(par1ItemStack.getMetadata());
		if (potion == ItemsCommonProxy.lesserManaPotion){
			par3List.add("Lesser Mana Restoration");
		}else if (potion == ItemsCommonProxy.standardManaPotion){
			par3List.add("Standard Mana Restoration");
		}else if (potion == ItemsCommonProxy.greaterManaPotion){
			par3List.add("Greater Mana Restoration");
		}else if (potion == ItemsCommonProxy.epicManaPotion){
			par3List.add("Epic Mana Restoration");
		}else if (potion == ItemsCommonProxy.legendaryManaPotion){
			par3List.add("Legendary Mana Restoration");
		}
		par3List.add("" + getUses(par1ItemStack.getMetadata()) + " " + StatCollector.translateToLocal("am2.tooltip.uses") + ".");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(ItemsCommonProxy.manaPotionBundle, 1, (0 << 8) + 3));
		par3List.add(new ItemStack(ItemsCommonProxy.manaPotionBundle, 1, (1 << 8) + 3));
		par3List.add(new ItemStack(ItemsCommonProxy.manaPotionBundle, 1, (2 << 8) + 3));
		par3List.add(new ItemStack(ItemsCommonProxy.manaPotionBundle, 1, (3 << 8) + 3));
		par3List.add(new ItemStack(ItemsCommonProxy.manaPotionBundle, 1, (4 << 8) + 3));
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		Item potion = getPotion(par1ItemStack.getMetadata());
		if (potion == ItemsCommonProxy.lesserManaPotion){
			return String.format("%s %s", StatCollector.translateToLocal("item.arsmagica2:mana_potion_lesser.name"), StatCollector.translateToLocal("item.arsmagica2:potionBundle.name"));
		}else if (potion == ItemsCommonProxy.standardManaPotion){
			return String.format("%s %s", StatCollector.translateToLocal("item.arsmagica2:mana_potion_standard.name"), StatCollector.translateToLocal("item.arsmagica2:potionBundle.name"));
		}else if (potion == ItemsCommonProxy.greaterManaPotion){
			return String.format("%s %s", StatCollector.translateToLocal("item.arsmagica2:mana_potion_greater.name"), StatCollector.translateToLocal("item.arsmagica2:potionBundle.name"));
		}else if (potion == ItemsCommonProxy.epicManaPotion){
			return String.format("%s %s", StatCollector.translateToLocal("item.arsmagica2:mana_potion_epic.name"), StatCollector.translateToLocal("item.arsmagica2:potionBundle.name"));
		}else if (potion == ItemsCommonProxy.legendaryManaPotion){
			return String.format("%s %s", StatCollector.translateToLocal("item.arsmagica2:mana_potion_legendary.name"), StatCollector.translateToLocal("item.arsmagica2:potionBundle.name"));
		}
		return "? " + StatCollector.translateToLocal("am2.items.bundle");
	}

	@Override
	public boolean getHasSubtypes(){
		return true;
	}

}
