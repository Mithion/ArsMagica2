package am2.entities.renderers;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class RenderHidden extends Render{

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) { }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
