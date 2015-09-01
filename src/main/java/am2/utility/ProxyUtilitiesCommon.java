package am2.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ProxyUtilitiesCommon {
	
	public static final String KEY_COMPENDIUM_UNLOCK_STATUS = "AM2_CUS";
	
	public EntityPlayer getLocalPlayer(){
		return null;
	}
	
	public boolean isLocalPlayerInFirstPerson(){
		return false;
	}
	
}
