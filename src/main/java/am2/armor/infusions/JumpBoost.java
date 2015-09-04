package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.EnumSet;

public class JumpBoost implements IArmorImbuement{

	@Override
	public String getID(){
		return "highjump";
	}

	@Override
	public int getIconIndex(){
		return 24;
	}

	@Override
	public ImbuementTiers getTier(){
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.ON_JUMP, ImbuementApplicationTypes.ON_TICK);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){
		if (matchedType == ImbuementApplicationTypes.ON_JUMP){
			Vec3 vec = player.getLookVec().normalize();
			double yVelocity = 1;
			double xVelocity = player.motionX * 3.5 * Math.abs(vec.xCoord);
			double zVelocity = player.motionZ * 3.5 * Math.abs(vec.zCoord);
			float maxHorizontalVelocity = 1.45f;

			if (ExtendedProperties.For(player).getIsFlipped()){
				yVelocity *= -1;
			}

			player.addVelocity(xVelocity, yVelocity, zVelocity);
		}else if (matchedType == ImbuementApplicationTypes.ON_TICK){
			ExtendedProperties.For(player).setFallProtection(20);
		}
		return true;
	}

	@Override
	public int[] getValidSlots(){
		return new int[]{ImbuementRegistry.SLOT_LEGS};
	}

	@Override
	public boolean canApplyOnCooldown(){
		return true;
	}

	@Override
	public int getCooldown(){
		return 0;
	}

	@Override
	public int getArmorDamage(){
		return 0;
	}
}
