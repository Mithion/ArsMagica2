package am2.utility;

import am2.guis.AMGuiHelper;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class RenderUtilities{
	public static void DrawIconInWorldAtOffset(IIcon icon, double x, double y, double z, double width, double height){
		Tessellator t = Tessellator.instance;

		GL11.glPushMatrix();
		GL11.glRotatef(180F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		int view = Minecraft.getMinecraft().gameSettings.thirdPersonView;
		if (view < 2)
			GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
		else
			GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

		GL11.glTranslated(x, y, z);
		GL11.glScaled(width, height, 1);
		renderIcon(icon, 0xFFFFFF);

		GL11.glPopMatrix();

	}

	public static void drawTextInWorldAtOffset(String text, double x, double y, double z, int color){
		Tessellator t = Tessellator.instance;

		FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		byte b0 = 0;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int j = fontrenderer.getStringWidth(text) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.75F);
		tessellator.addVertex(-j - 1, -1 + b0, 0.0D);
		tessellator.addVertex(-j - 1, 8 + b0, 0.0D);
		tessellator.addVertex(j + 1, 8 + b0, 0.0D);
		tessellator.addVertex(j + 1, -1 + b0, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0, 553648127);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0, -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();

	}

	private static void renderIcon(IIcon IIcon, int renderColor){
		Tessellator tessellator = Tessellator.instance;
		float f = 1.0F;
		float f1 = 0.5F;
		float f2 = 0.25F;

		tessellator.startDrawingQuads();
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tessellator.setColorRGBA_F((renderColor & 0xFF0000) >> 16, (renderColor & 0x00FF00) >> 8, renderColor & 0x0000FF, AMGuiHelper.instance.playerRunesAlpha);

		tessellator.addVertexWithUV(0.0F - f1, 0.0F - f2, 0.0D, IIcon.getMinU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(f - f1, 0.0F - f2, 0.0D, IIcon.getMaxU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(f - f1, f - f2, 0.0D, IIcon.getMaxU(), IIcon.getMinV());
		tessellator.addVertexWithUV(0.0F - f1, f - f2, 0.0D, IIcon.getMinU(), IIcon.getMinV());
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		RenderHelper.enableStandardItemLighting();
	}

	public static void setupShrinkRender(EntityLivingBase entity){
		if (entity == null)
			return;

		ExtendedProperties exProps = ExtendedProperties.For(entity);
		if (exProps.getShrinkPct() > 0f){

			float amt = 0.5f * exProps.getShrinkPct();
			GL11.glScalef(1 - amt, 1 - amt, 1 - amt);
		}
	}
}
