package am2.entities.renderers;

import org.lwjgl.opengl.GL11;

import am2.entities.EntityWhirlwind;
import am2.particles.AMParticleIcons;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class RenderWhirlwind extends RenderLiving{

	private static final ResourceLocation rLoc = new ResourceLocation("textures/atlas/items.png");
	
	public RenderWhirlwind() {
		super(null, 0.5f);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		renderWhirlwind((EntityWhirlwind) par1Entity, par2, par4, par6, par8, par9);
	}
	
	private void renderWhirlwind(EntityWhirlwind whirlwind, double x, double y, double z, float f, float f1){
		GL11.glPushMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(rLoc);
		
		Tessellator tessellator = Tessellator.instance;
		IIcon IIcon = AMParticleIcons.instance.getIconByName("wind");
		
		GL11.glTranslated(x, y, z);
		
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		float min_u = IIcon.getMinU();
		float min_v = IIcon.getMinV();
		float max_u = IIcon.getMaxU();
		float max_v = IIcon.getMaxV();

		GL11.glRotatef(180F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);		

		tessellator.startDrawingQuads();
		tessellator.setBrightness(0xF00F0);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, min_u, max_v);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, max_u, max_v);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, max_u, min_v);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, min_u, min_v);
		tessellator.draw();	
		
		GL11.glPopMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}

}
