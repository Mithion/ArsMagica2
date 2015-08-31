package thehippomaster.AnimationExample.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTest extends RenderLiving {
	
	public RenderTest() {
		super(new ModelTest(), 0.25F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
	
	private static final ResourceLocation texture = new ResourceLocation("none");
}
