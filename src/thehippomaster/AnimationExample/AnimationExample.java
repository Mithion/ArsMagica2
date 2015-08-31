package thehippomaster.AnimationExample;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = "AnimationExample", name = "Animation Example", version = "1.0.0")
public class AnimationExample {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		EntityRegistry.registerGlobalEntityID(EntityTest.class, "EntityTest", 106, 0, 0);
		
		proxy.registerRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}
	
	@Instance("AnimationExample")
	public static AnimationExample instance;
	@SidedProxy(clientSide="thehippomaster.AnimationExample.client.ClientProxy", serverSide="thehippomaster.AnimationExample.CommonProxy")
	public static CommonProxy proxy;
}
