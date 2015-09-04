package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.EnumSet;

public class Recoil implements IArmorImbuement{

	@Override
	public String getID(){
		return "recoil";
	}

	@Override
	public int getIconIndex(){
		return 14;
	}

	@Override
	public ImbuementTiers getTier(){
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.ON_HIT);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){
		LivingHurtEvent event = (LivingHurtEvent)params[0];
		Entity e = event.source.getSourceOfDamage();
		if (e != null && e instanceof EntityLivingBase){
			((EntityLivingBase)e).knockBack(player, 10, player.posX - e.posX, player.posZ - e.posZ);
		}
		return true;
	}

	@Override
	public int[] getValidSlots(){
		return new int[]{ImbuementRegistry.SLOT_CHEST};
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
		return 2;
	}
}
