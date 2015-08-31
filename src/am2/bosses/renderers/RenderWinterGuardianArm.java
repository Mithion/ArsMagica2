package am2.bosses.renderers;

import am2.bosses.models.ModelWinterGuardianArm;
import am2.texture.ResourceManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderWinterGuardianArm extends RenderLiving {

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/ice_guardian.png"));

	public RenderWinterGuardianArm() {
		super(new ModelWinterGuardianArm(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}

}
