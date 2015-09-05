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

public class Solar implements ISpellModifier{

	@Override
	public int getID(){
		return 12;
	}

	@Override
	public EnumSet<SpellModifiers> getAspectsModified(){
		return EnumSet.of(SpellModifiers.RANGE, SpellModifiers.RADIUS, SpellModifiers.DAMAGE, SpellModifiers.DURATION, SpellModifiers.HEALING);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, byte[] metadata){
		switch (type){
		case RANGE:
			return modifyValueOnInverseLunarCycle(world, 3f);
		case RADIUS:
			return modifyValueOnInverseLunarCycle(world, 3f);
		case DAMAGE:
			return modifyValueOnTime(world, 2.4f);
		case DURATION:
			return modifyValueOnTime(world, 5f);
		case HEALING:
			return modifyValueOnTime(world, 2f);
		}
		return 1.0f;
	}

	private float modifyValueOnTime(World world, float value){
		long x = world.provider.getWorldTime() % 24000;
		float multiplierFromTime = (float)(Math.cos(((x / 3800f) * (x / 24000f) - 13000f) * (180f / Math.PI)) * 1.5f) + 1;
		if (multiplierFromTime < 0)
			multiplierFromTime *= -0.5f;
		return value * multiplierFromTime;
	}

	private float modifyValueOnInverseLunarCycle(World world, float value){
		long boundedTime = world.provider.getWorldTime() % 24000;
		int phase = 8 - (8 - world.provider.getMoonPhase(world.getWorldInfo().getWorldTime()));
		if (boundedTime > 23500 && boundedTime < 12500){
			return value + (phase / 2);
		}
		return Math.abs(value - 1);
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_NATURE),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE),
				Items.clock
		};
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity){
		return 4.0f * quantity;
	}

	@Override
	public byte[] getModifierMetadata(ItemStack[] matchedRecipe){
		return null;
	}
}
