package thehippomaster.AnimationExample.client;

import thehippomaster.AnimationExample.CommonProxy;
import thehippomaster.AnimationExample.EntityTest;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, new RenderTest());
	}
}
