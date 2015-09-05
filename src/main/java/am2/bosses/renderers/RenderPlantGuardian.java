package am2.bosses.renderers;

import am2.bosses.models.ModelPlantGuardian;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderPlantGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/plant_guardian.png"));

	public RenderPlantGuardian(){
		super(new ModelPlantGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}

}
