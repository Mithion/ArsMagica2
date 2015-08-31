package am2.api.events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import am2.api.spell.enums.SpellModifiers;

/**
 * Called whenever a spell's modified variable is calculated
 * @author Mithion
 *
 */
public class ModifierCalculatedEvent extends Event {
	//The attribute being modified
	public final SpellModifiers attribute;
	//The caster of the spell
	public final EntityLivingBase caster;
	//The spell being cast.  This is a copy of the ItemStack.
	public final ItemStack spell;
	//the initial value of the variable
	public final double initialValue;
	//the type of operation being used to modify the value
	public final OperationType operation;

	//the final modified value of the variable
	public double modifiedValue;

	public enum OperationType{
		ADD,
		MULTIPLY
	}

	public ModifierCalculatedEvent(ItemStack spell, EntityLivingBase caster, SpellModifiers attribute, double initialValue, double modifiedValue, OperationType operation){
		this.spell = spell;
		this.caster = caster;
		this.attribute = attribute;
		this.initialValue = initialValue;
		this.modifiedValue = modifiedValue;
		this.operation = operation;
	}
}
