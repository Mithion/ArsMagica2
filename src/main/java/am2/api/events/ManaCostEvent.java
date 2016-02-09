package am2.api.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Fired whenever mana cost of a spell is calculated, after all predefined calculations.
 *
 * @author Mithion
 */
public class ManaCostEvent extends Event{
	public final ItemStack spell;
	public final EntityLivingBase caster;
	public float manaCost;
	public float burnout;

	public ManaCostEvent(ItemStack spell, EntityLivingBase caster, float manaCost, float burnout){
		this.spell = spell;
		this.caster = caster;
		this.manaCost = manaCost;
		this.burnout = burnout;
	}
}
