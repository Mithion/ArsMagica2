package am2.entities.renderers;

import am2.entities.models.ModelFlicker;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFlicker extends RenderLiving{
	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("flicker.png"));

	public RenderFlicker(){
		super(new ModelFlicker(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity){
		return rLoc;
	}

	@Override
	public void doRenderShadowAndFire(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
	}
}
