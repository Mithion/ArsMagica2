package am2.entities.renderers;

import am2.models.ModelHellCow;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderHellCow extends RenderBiped{

	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("HellCow.png"));

	public RenderHellCow(){
		super(new ModelHellCow(), 0.5f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rLoc;
	}
}
