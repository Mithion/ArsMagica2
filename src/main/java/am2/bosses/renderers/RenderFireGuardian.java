package am2.bosses.renderers;

import am2.bosses.models.ModelFireGuardian;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFireGuardian extends RenderBoss{
	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/fire_guardian.png"));

	public RenderFireGuardian(){
		super(new ModelFireGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}
}
