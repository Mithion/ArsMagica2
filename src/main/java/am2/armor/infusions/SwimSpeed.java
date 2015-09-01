package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.buffs.BuffEffectSwiftSwim;
import am2.buffs.BuffList;

public class SwimSpeed implements IArmorImbuement{

	@Override
	public String getID() {
		return "swimspd";
	}

	@Override
	public int getIconIndex() {
		return 27;
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
		if (world.isRemote)
			return false;

		if (player.isInsideOfMaterial(Material.water) && !player.isPotionActive(BuffList.swiftSwim.id)){
			player.addPotionEffect(new BuffEffectSwiftSwim(10, 1));
			return true;
		}
		return false;
	}

	@Override
	public int[] getValidSlots() {
		return new int[] {ImbuementRegistry.SLOT_LEGS};
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
		return 0;
	}
}
