package am2.spell.modifiers;

import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;

public class TargetNonSolidBlocks implements ISpellModifier{

	@Override
	public int getID() {
		return 11;
	}

	@Override
	public EnumSet<SpellModifiers> getAspectsModified() {
		return EnumSet.of(SpellModifiers.TARGET_NONSOLID_BLOCKS);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata) {
		return 1.0f;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER),
			Items.potionitem,
			Blocks.red_flower,
			Blocks.brown_mushroom,
			BlocksCommonProxy.cerublossom
		};
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
