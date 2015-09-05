package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.blocks.BlocksCommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;

public class Lightstep implements IArmorImbuement{

	@Override
	public String getID(){
		return "lightstep";
	}

	@Override
	public int getIconIndex(){
		return 30;
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

		if (player.isSneaking())
			return false;
		int x = (int)Math.floor(player.posX);
		int y = (int)Math.floor(player.posY) + 1;
		int z = (int)Math.floor(player.posZ);
		int ll = world.getBlockLightValue(x, y, z);
		if (ll < 7 && world.isAirBlock(x, y, z)){
			world.setBlock(x, y, z, BlocksCommonProxy.blockMageTorch, 15, 2);
			return true;
		}
		return false;
	}

	@Override
	public int[] getValidSlots(){
		return new int[]{ImbuementRegistry.SLOT_BOOTS};
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
		return 1;
	}
}
