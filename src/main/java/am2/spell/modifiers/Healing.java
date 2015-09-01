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

public class Healing implements ISpellModifier{
	@Override
	public EnumSet<SpellModifiers> getAspectsModified() {
		return EnumSet.of(SpellModifiers.HEALING);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata) {
		return 2;
	}

	@Override
	public int getID() {
		return 4;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE),
			Items.egg,
			"P:0 & !1 & 2 & !3" //healing potion
		};
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity) {
		return 1 * quantity;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe) {
		return null;
	}
}
