package am2.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemMagitechGoggles extends AMArmor{

	public ItemMagitechGoggles(int renderIndex){
		super(ArmorMaterial.LEATHER, ArsMagicaArmorMaterial.UNIQUE, renderIndex, 0);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2){
		return 0xFFFFFF;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return 2;
	}

	@Override
	public int GetDamageReduction(){
		return 2;
	}
}
