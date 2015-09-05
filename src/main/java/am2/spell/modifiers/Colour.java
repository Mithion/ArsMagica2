package am2.spell.modifiers;

import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.nio.ByteBuffer;
import java.util.EnumSet;

public class Colour implements ISpellModifier{

	@Override
	public int getID(){
		return 14;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE),
				new ItemStack(Items.dye, 1, Short.MAX_VALUE)
		};
	}

	@Override
	public EnumSet<SpellModifiers> getAspectsModified(){
		return EnumSet.of(SpellModifiers.COLOR);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata){
		if (metadata.length != 4) return 0;
		return ByteBuffer.wrap(metadata).getInt();
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity){
		return 1.0f;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe){
		for (ItemStack stack : matchedRecipe){
			if (stack.getItem() == Items.dye)
				return ByteBuffer.allocate(4).putInt(((ItemDye)stack.getItem()).field_150922_c[stack.getItemDamage()]).array();
		}
		return null;
	}

}
