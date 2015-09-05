package am2.spell.modifiers;

import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;

public class BuffPower implements ISpellModifier{
	@Override
	public EnumSet<SpellModifiers> getAspectsModified(){
		return EnumSet.of(SpellModifiers.BUFF_POWER);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata){
		return 1;
	}

	@Override
	public int getID(){
		return 17;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				Items.glass_bottle,
				Items.redstone,
				Items.glowstone_dust,
				Items.ghast_tear,
				Items.spider_eye,
				Items.nether_wart,
				Items.gunpowder,
				Items.emerald
		};
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity){
		return 1.25f * quantity;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe){
		return null;
	}
}
