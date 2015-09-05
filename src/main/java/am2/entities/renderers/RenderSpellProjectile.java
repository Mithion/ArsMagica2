package am2.entities.renderers;

import am2.entities.EntitySpellProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSpellProjectile extends Render{

	private static final ResourceLocation projectile = new ResourceLocation("textures/atlas/items.png");

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1){
		doRenderSpellProjectile((EntitySpellProjectile)entity, d0, d1, d2, f, f1);
	}

	private void doRenderSpellProjectile(EntitySpellProjectile entity, double d, double d1, double d2, float f, float f1){
		IIcon icon = entity.getIcon();
		if (icon == null){
			return;
		}

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(32826); /* RESCALE_NORMAL_EXT */
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		GL11.glDepthMask(false);
		//RenderHelper.disableStandardItemLighting();

		Minecraft.getMinecraft().renderEngine.bindTexture(projectile);

		int renderColor = entity.getColor();

		GL11.glTranslated(d, d1, d2);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glRotatef(180F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

		renderIcon(icon, renderColor);

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private void renderIcon(IIcon IIcon, int renderColor){
		Tessellator tessellator = Tessellator.instance;
		float f = 1.0F;
		float f1 = 0.5F;
		float f2 = 0.25F;

		tessellator.startDrawingQuads();
		RenderHelper.disableStandardItemLighting();
		tessellator.setColorRGBA((renderColor & 0xFF0000) >> 16, (renderColor & 0x00FF00) >> 8, renderColor & 0x0000FF, 255);

		tessellator.addVertexWithUV(0.0F - f1, 0.0F - f2, 0.0D, IIcon.getMinU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(f - f1, 0.0F - f2, 0.0D, IIcon.getMaxU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(f - f1, f - f2, 0.0D, IIcon.getMaxU(), IIcon.getMinV());
		tessellator.addVertexWithUV(0.0F - f1, f - f2, 0.0D, IIcon.getMinU(), IIcon.getMinV());
		tessellator.draw();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return null;
	}

}
