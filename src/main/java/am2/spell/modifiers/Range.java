package am2.spell.modifiers;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;

public class Range implements ISpellModifier{
	@Override
	public EnumSet<SpellModifiers> getAspectsModified() {
		return EnumSet.of(SpellModifiers.RANGE);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata) {
		return 4f;
	}

	@Override
	public int getID() {
		return 6;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE),
			Items.arrow,
			Items.snowball,
			Items.redstone
		};
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity) {
		return 1.2f * quantity;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe) {
		return null;
	}
}
