package am2.entities.renderers;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.entities.models.ModelBroom;
import am2.texture.ResourceManager;

public class RenderBroom extends RenderLiving{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("broom.png"));

	public RenderBroom() {
		super(new ModelBroom(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return rLoc;
	}
}
