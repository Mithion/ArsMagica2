package am2.bosses.renderers;

import am2.bosses.models.ModelPlantGuardianSickle;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderThrownSickle extends RenderLiving{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/plant_guardian.png"));

	public RenderThrownSickle(){
		super(new ModelPlantGuardianSickle(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}

}
