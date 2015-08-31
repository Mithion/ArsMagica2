package am2.bosses.renderers;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.bosses.models.ModelEnderGuardian;
import am2.texture.ResourceManager;

public class RenderEnderGuardian extends RenderBoss {

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/ender_guardian.png"));

	public RenderEnderGuardian() {
		super(new ModelEnderGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}
}
