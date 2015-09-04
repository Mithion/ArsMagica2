package am2.entities.renderers;

import am2.entities.EntityManaElemental;
import am2.entities.models.ModelManaElemental;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderManaElemental extends RenderBiped{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("ManaElemental.png"));

	public RenderManaElemental(){
		super(new ModelManaElemental(), 0.5f);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
		this.func_82426_a((EntityManaElemental)par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity){
		return rLoc;
	}

	public void func_82426_a(EntityManaElemental elem, double par2, double par4, double par6, float par8, float par9){
		super.doRender(elem, par2, par4, par6, par8, par9);
	}

}
