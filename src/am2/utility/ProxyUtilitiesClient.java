package am2.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ProxyUtilitiesClient extends ProxyUtilitiesCommon{
	@Override
	public boolean isLocalPlayerInFirstPerson() {
		return Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;
	}
	
	@Override
	public EntityPlayer getLocalPlayer(){
		return Minecraft.getMinecraft().thePlayer;
	}
	
}
