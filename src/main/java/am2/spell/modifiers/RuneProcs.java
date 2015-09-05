package am2.spell.modifiers;

import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;

public class RuneProcs implements ISpellModifier{
	@Override
	public EnumSet<SpellModifiers> getAspectsModified(){
		return EnumSet.of(SpellModifiers.PROCS);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata){
		return 4;
	}

	@Override
	public int getID(){
		return 7;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK)
		};
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity){
		return 1.65f * quantity;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe){
		return null;
	}
}
