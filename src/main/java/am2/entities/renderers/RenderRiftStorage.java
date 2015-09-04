package am2.entities.renderers;

import am2.entities.EntityRiftStorage;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderRiftStorage extends Render{

	private static final ResourceLocation rift = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("Rift.png"));
	private static final ResourceLocation rift2 = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("Rift2.png"));

	public RenderRiftStorage(){
	}

	public void renderRift(EntityRiftStorage entityRiftStorage, double d, double d1, double d2,
						   float f, float f1){

		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glScalef(entityRiftStorage.getScale(0) * 2, entityRiftStorage.getScale(1) * 2, entityRiftStorage.getScale(0) * 2);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_SRC_ALPHA);
		//GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		Tessellator tessellator = Tessellator.instance;

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);

		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);

		Minecraft.getMinecraft().renderEngine.bindTexture(rift);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		GL11.glRotatef(entityRiftStorage.getRotation(), 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(0.0f, -0.25f, 0.0f);
		renderSprite(tessellator);
		GL11.glPopMatrix();

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		Minecraft.getMinecraft().renderEngine.bindTexture(rift2);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		GL11.glRotatef(-entityRiftStorage.getRotation(), 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(0.0f, -0.25f, 0.1f);
		renderSprite(tessellator);
		GL11.glPopMatrix();


		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
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

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1){
		renderRift((EntityRiftStorage)entity, d, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return rift;
	}

}
