package am2.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.enums.SpellModifiers;

public interface ISpellUtils {

	public double getModifiedDouble_Mul(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check);

	public int getModifiedInt_Mul(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check);

	public double getModifiedDouble_Mul(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world);

	public int getModifiedInt_Mul(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world);

	public double getModifiedDouble_Add(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check);

	public int getModifiedInt_Add(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check);

	public double getModifiedDouble_Add(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world);

	public int getModifiedInt_Add(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world);

	public boolean modifierIsPresent(SpellModifiers check, ItemStack stack);

	public int countModifiers(SpellModifiers check, ItemStack stack);
	
}
