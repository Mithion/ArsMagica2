package am2.bosses.renderers;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.bosses.models.ModelArcaneGuardian;
import am2.texture.ResourceManager;

public class RenderArcaneGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/arcane_guardian.png"));

	public RenderArcaneGuardian() {
		super(new ModelArcaneGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}

}
