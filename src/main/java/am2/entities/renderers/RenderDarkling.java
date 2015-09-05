package am2.entities.renderers;

import am2.models.ModelDarkling;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDarkling extends RenderLiving{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("darkling.png"));

	public RenderDarkling(){
		super(new ModelDarkling(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity){
		return rLoc;
	}
}
