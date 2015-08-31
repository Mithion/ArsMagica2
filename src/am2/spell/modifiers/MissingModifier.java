package am2.spell.modifiers;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;

public class MissingModifier implements ISpellModifier{

	@Override
	public EnumSet<SpellModifiers> getAspectsModified() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata) {
		return 0;
	}

	@Override
	public int getID() {
		return -1;
	}
	
	@Override
	public Object[] getRecipeItems() {
		return null;
	}
	
	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity) {
		return 1;
	}
	
	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe) {
		return null;
	}
}
