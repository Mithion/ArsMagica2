package am2.bosses.renderers;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.bosses.models.ModelAirGuardian;
import am2.texture.ResourceManager;

public class RenderAirGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/air_guardian.png"));

	public RenderAirGuardian() {
		super(new ModelAirGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}
}
