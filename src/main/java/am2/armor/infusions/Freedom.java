package am2.armor.infusions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;

public class Freedom implements IArmorImbuement{

	@Override
	public String getID() {
		return "freedom";
	}

	@Override
	public int getIconIndex() {
		return 29;
	}

	@Override
	public ImbuementTiers getTier() {
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes() {
		return EnumSet.of(ImbuementApplicationTypes.ON_TICK);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params) {
		ModifiableAttributeInstance instance = (ModifiableAttributeInstance)player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);

		ArrayList<AttributeModifier> toRemove = new ArrayList<AttributeModifier>();

		Collection c = instance.func_111122_c();
		ArrayList arraylist = new ArrayList(c);
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            AttributeModifier attributemodifier = (AttributeModifier)iterator.next();
            if (attributemodifier.getOperation() == 2 && attributemodifier.getAmount() < 0.0f){
            	toRemove.add(attributemodifier);
            }
        }

		for (AttributeModifier modifier : toRemove){
			instance.removeModifier(modifier);
		}

		return toRemove.size() > 0;
	}

	@Override
	public int[] getValidSlots() {
		return new int[] { ImbuementRegistry.SLOT_BOOTS };
	}

	@Override
	public boolean canApplyOnCooldown() {
		return true;
	}

	@Override
	public int getCooldown() {
		return 0;
	}

	@Override
	public int getArmorDamage() {
		return 1;
	}
}
