package am2.spell.modifiers;

import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;

public class Damage implements ISpellModifier{
	@Override
	public EnumSet<SpellModifiers> getAspectsModified(){
		return EnumSet.of(SpellModifiers.DAMAGE);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata){
		return 2.2f;
	}

	@Override
	public int getID(){
		return 1;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
				Items.iron_sword,
				"P:!0 & !1 & 2 & 3" //harming potion
		};
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity){
		return 1.3f * quantity;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe){
		return null;
	}
}
