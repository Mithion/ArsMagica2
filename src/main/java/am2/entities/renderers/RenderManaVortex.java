package am2.entities.renderers;

import am2.entities.EntityManaVortex;
import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderManaVortex extends Render{

	private final float textureImageSize = 512;
	private final int textureFrameSize = 64;

	private static final ResourceLocation vortex = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("Magic_Fabricator_2.png"));
	private static final ResourceLocation wisp = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("mob_wisp.png"));

	@Override
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9){
		doRenderManaVortex((EntityManaVortex)var1, var2, var4, var6, var8, var9);
	}

	private void doRenderManaVortex(EntityManaVortex vortex, double d, double d1, double d2, float f, float f1){
		if (vortex == null || !vortex.worldObj.isRemote){
			return;
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (this.renderManager.renderEngine == null){
			return;
		}
		Tessellator tessellator = Tessellator.instance;

		renderArsMagicaEffect(tessellator, vortex.ticksExisted, vortex.getTicksToExist(), vortex.getRotation(), vortex.getScale(), vortex.getManaStolenPercent());

		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}

	private void renderArsMagicaEffect(Tessellator tessellator, int ticks, int life, float rotation, float scale, float percent){
		GL11.glPushMatrix();
		GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		GL11.glColor4f(0.2f, 0.5f, 1.0f, 1.0f);

		Minecraft.getMinecraft().renderEngine.bindTexture(vortex);
		GL11.glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
		GL11.glScalef(scale * 2, scale * 2, scale * 2);
		GL11.glTranslatef(0.0f, -0.25f, 0.0f);
		renderSprite(tessellator);
		GL11.glPopMatrix();

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		Minecraft.getMinecraft().renderEngine.bindTexture(wisp);
		GL11.glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
		GL11.glScalef(scale * 3, scale * 3, scale * 3);
		GL11.glTranslatef(0.0f, -0.25f, 0.0f);
		renderSprite(tessellator);
		GL11.glPopMatrix();

		GL11.glPopMatrix();

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		RenderHelper.disableStandardItemLighting();
		float var4 = ((float)ticks) / life;
		float var5 = 0.0F;

		if (var4 > 0.8F){
			var5 = (var4 - 0.8F) / 0.2F;
		}

		Random var6 = new Random(432L);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.3F, 0.0F);

		int color = ((int)(percent * 255) << 16) & ((255 - (int)(percent * 255)) << 8) & (int)(255 - percent * 255);

		for (int var7 = 0; var7 < (var4 + var4 * var4) / 2.0F * 60.0F; ++var7){
			GL11.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var6.nextFloat() * 360.0F + var4 * 90.0F, 0.0F, 0.0F, 1.0F);
			tessellator.startDrawing(6);
			float var8 = var6.nextFloat() * 2.0F + 2.0F + var5 * 0.5F;
			float var9 = var6.nextFloat() * 2.0F + 1.0F + var5 * 2.0F;
			//tessellator.setColorRGBA_I(color, (int)(255.0F * (0.2F - (var5 * 0.2f))));
			tessellator.setColorRGBA((int)(percent * 255), (int)(255 - (percent * 255)), (int)(255 - (percent * 255)), (int)(255.0F * (0.2F - (var5 * 0.2f))));
			tessellator.addVertex(0.0D, 0.0D, 0.0D);
			//tessellator.setColorRGBA_I(color, 0);
			tessellator.setColorRGBA((int)(percent * 255), (int)(255 - (percent * 255)), (int)(255 - (percent * 255)), 0);
			tessellator.addVertex(-0.866D * var9, var8, -0.5F * var9);
			tessellator.addVertex(0.866D * var9, var8, -0.5F * var9);
			tessellator.addVertex(0.0D, var8, 1.0F * var9);
			tessellator.addVertex(-0.866D * var9, var8, -0.5F * var9);
			tessellator.draw();
		}

		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	private void renderSprite(Tessellator tessellator){

		float TLX = 0;
		float BRX = 1;
		float TLY = 0;
		float BRY = 1;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		tessellator.startDrawingQuads();
		tessellator.setBrightness(15728863);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, TLX, BRY);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, BRX, BRY);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, BRX, TLY);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, TLX, TLY);
		tessellator.draw();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return vortex;
	}
}
