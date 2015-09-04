package am2.entities.renderers;

import am2.entities.EntityDarkMage;
import am2.entities.EntityLightMage;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class RenderMage extends RenderBiped{

	private final HashMap<String, ResourceLocation> resourceLocations;

	public RenderMage(){
		super(new ModelBiped(), 0.5f);
		resourceLocations = new HashMap<String, ResourceLocation>();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
		if (par1Entity instanceof EntityLightMage)
			this.func_82426_a((EntityLightMage)par1Entity, par2, par4, par6, par8, par9);
		else if (par1Entity instanceof EntityDarkMage)
			this.func_82426_a((EntityDarkMage)par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity ent){
		String tex = "unknown";
		if (ent instanceof EntityLightMage){
			tex = ((EntityLightMage)ent).getTexture();
		}else if (ent instanceof EntityDarkMage){
			tex = ((EntityDarkMage)ent).getTexture();
		}
		if (resourceLocations.containsKey(tex)){
			return resourceLocations.get(tex);
		}else{
			ResourceLocation rLoc = new ResourceLocation("arsmagica2", tex);
			resourceLocations.put(tex, rLoc);
			return rLoc;
		}
	}

	public void func_82426_a(EntityLightMage mage, double par2, double par4, double par6, float par8, float par9){
		super.doRender(mage, par2, par4, par6, par8, par9);
	}

	public void func_82426_a(EntityDarkMage mage, double par2, double par4, double par6, float par8, float par9){
		super.doRender(mage, par2, par4, par6, par8, par9);
	}

}
