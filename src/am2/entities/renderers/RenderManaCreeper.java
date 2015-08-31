package am2.entities.renderers;

import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class RenderManaCreeper extends RenderCreeper {

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("mana_creeper.png"));
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}

}
