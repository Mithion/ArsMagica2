package am2.bosses.renderers;

import am2.bosses.models.ModelWinterGuardian;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderIceGuardian extends RenderBoss{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/ice_guardian.png"));

	public RenderIceGuardian(){
		super(new ModelWinterGuardian());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}
}
