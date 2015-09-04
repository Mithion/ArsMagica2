package am2.entities.renderers;

import am2.entities.EntityEarthElemental;
import am2.texture.ResourceManager;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEarthElemental extends RenderBiped{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("earth_golem.png"));

	public RenderEarthElemental(){
		super(new ModelZombie(), 0.5f);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
		this.func_82426_a((EntityEarthElemental)par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity){
		return rLoc;
	}

	public void func_82426_a(EntityEarthElemental dryad, double par2, double par4, double par6, float par8, float par9){
		super.doRender(dryad, par2, par4, par6, par8, par9);
	}

}
