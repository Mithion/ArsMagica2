package am2.bosses.renderers;

import am2.bosses.models.ModelLifeGuardian;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLifeGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/life_guardian.png"));

	public RenderLifeGuardian(){
		super(new ModelLifeGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}

}
