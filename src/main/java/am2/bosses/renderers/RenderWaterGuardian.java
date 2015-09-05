package am2.bosses.renderers;

import am2.bosses.models.ModelWaterGuardian;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderWaterGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/water_guardian.png"));

	public RenderWaterGuardian(){
		super(new ModelWaterGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}
}
