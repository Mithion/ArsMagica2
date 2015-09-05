package am2.items;

import am2.buffs.BuffList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;


public class ItemManaMartini extends ItemFood{
	public ItemManaMartini(){
		super(0, 0, false);
		this.setPotionEffect(BuffList.burnoutReduction.getId(), 300, 0, 1.0f);
	}

	public ItemManaMartini setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_){
		return EnumAction.drink;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean iHaveNoIdea){
		super.addInformation(stack, player, lines, iHaveNoIdea);
		lines.add(StatCollector.translateToLocal("am2.tooltip.shaken"));
	}
}
