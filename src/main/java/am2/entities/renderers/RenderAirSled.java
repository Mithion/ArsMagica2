package am2.entities.renderers;

import am2.models.ModelAirGuardianHoverball;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderAirSled extends RenderLiving{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/air_guardian.png"));

	private ModelAirGuardianHoverball model;

	public RenderAirSled(){
		super(new ModelAirGuardianHoverball(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}

}
