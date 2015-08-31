package am2.bosses.renderers;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.bosses.models.ModelLifeGuardian;
import am2.texture.ResourceManager;

public class RenderLifeGuardian extends RenderBoss{

private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/life_guardian.png"));

	public RenderLifeGuardian() {
		super(new ModelLifeGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}

}
