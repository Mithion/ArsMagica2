package am2.bosses.renderers;

import am2.bosses.models.ModelThrownRock;
import am2.entities.EntityThrownRock;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderThrownRock extends RenderLiving{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/earth_guardian.png"));

	public RenderThrownRock(){
		super(new ModelThrownRock(), 0.5f);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
		if (!((EntityThrownRock)par1Entity).getIsShootingStar())
			super.doRender(par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}

}
