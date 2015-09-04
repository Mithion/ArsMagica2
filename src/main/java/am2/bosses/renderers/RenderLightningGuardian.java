package am2.bosses.renderers;

import am2.bosses.models.ModelLightningGuardian;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLightningGuardian extends RenderBoss{

	private static final ResourceLocation lightning = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/lightning_guardian_lt.png"));
	private static final ResourceLocation armor = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/lightning_guardian.png"));

	public RenderLightningGuardian(){
		super(new ModelLightningGuardian(armor, lightning));
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return lightning;
	}

}
