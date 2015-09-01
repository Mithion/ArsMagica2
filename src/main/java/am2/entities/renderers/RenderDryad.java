package am2.entities.renderers;

import am2.entities.EntityDryad;
import am2.texture.ResourceManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class RenderDryad extends RenderBiped{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("mobDryad.png"));
	
	public RenderDryad() {
		super(new ModelBiped(), 0.5f);
	}
	
	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.func_82426_a((EntityDryad)par1Entity, par2, par4, par6, par8, par9);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return rLoc;
	}
	
	public void func_82426_a(EntityDryad dryad, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(dryad, par2, par4, par6, par8, par9);
    }

}
