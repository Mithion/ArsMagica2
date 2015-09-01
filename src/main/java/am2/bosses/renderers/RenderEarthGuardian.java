package am2.bosses.renderers;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.bosses.models.ModelEarthGuardian;
import am2.texture.ResourceManager;

public class RenderEarthGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/earth_guardian.png"));

	public RenderEarthGuardian() {
		super(new ModelEarthGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}
}
