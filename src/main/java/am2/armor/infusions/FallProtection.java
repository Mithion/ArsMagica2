package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.buffs.BuffEffectSlowfall;
import am2.buffs.BuffList;
import am2.playerextensions.ExtendedProperties;
import am2.utility.MathUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;

public class FallProtection implements IArmorImbuement{

	@Override
	public String getID(){
		return "fallprot";
	}

	@Override
	public int getIconIndex(){
		return 27;
	}

	@Override
	public ImbuementTiers getTier(){
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.ON_TICK);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){
		if (world.isRemote)
			return false;

		int distanceToGround = MathUtilities.getDistanceToGround(player, world);
		ExtendedProperties extendedProperties = ExtendedProperties.For(player);
		if (player.fallDistance >= extendedProperties.getFallProtection() + 4f && distanceToGround < -8 * player.motionY){
			if (!player.isPotionActive(BuffList.slowfall.id) && !player.capabilities.isFlying){

				BuffEffectSlowfall sf = new BuffEffectSlowfall(distanceToGround * 3, 1);
				player.addPotionEffect(sf);

				stack.damageItem((int)(player.fallDistance * 6), player);

				player.fallDistance = 0;
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] getValidSlots(){
		return new int[]{ImbuementRegistry.SLOT_BOOTS};
	}

	@Override
	public boolean canApplyOnCooldown(){
		return false;
	}

	@Override
	public int getCooldown(){
		return 900;
	}

	@Override
	public int getArmorDamage(){
		return 0;
	}
}
