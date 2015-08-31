package am2.blocks.renderers;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityKeystoneRecepticle;
import am2.guis.AMGuiIcons;
import am2.models.ModelKeystoneRecepticle;
import am2.texture.ResourceManager;

public class KeystoneReceptacleRenderer extends TileEntitySpecialRenderer {

	private final ModelKeystoneRecepticle model;
	private static final ResourceLocation portal = new ResourceLocation("textures/atlas/items.png");
	private final ResourceLocation rLoc;

	public KeystoneReceptacleRenderer(){
		model = new ModelKeystoneRecepticle();
		
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("KeystoneReceptacle.png"));
	}

	public void renderAModelAt(TileEntityKeystoneRecepticle tile, double d, double d1, double d2, float f) {
		int i = 0;

		if (tile.getWorldObj() != null)
		{
			i = tile.getBlockMetadata() + 1;
		}
		int j = 0;

		if (i == 0 || i == 4)
		{
			j = 0;
		}

		if (i == 1)
		{
			j = 90;
		}

		if (i == 2)
		{
			j = 180;
		}

		if (i == 3)
		{
			j = 270;
		}

		bindTexture(rLoc);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F); //size
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		model.renderModel(0.0625F); //renders and yes 0.0625 is a random number
		GL11.glPopMatrix(); //end	
		
		if (tile.isActive()){
			bindTexture(portal);
			
			GL11.glPushMatrix();
			
			GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			GL11.glTranslatef((float)d + 0.5f, (float)d1 - 2.5F, (float)d2 + 0.5f);
			GL11.glScaled(4, 4, 4);
			GL11.glEnable (GL11.GL_BLEND);
			GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			GL11.glDepthMask(false);
			Tessellator tessellator = Tessellator.instance;

			GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata

			//apply portal colors here
			GL11.glColor4f(1, 1, 1, 1);
			
			renderArsMagicaEffect(tessellator);

			GL11.glDisable (GL11.GL_BLEND);
			
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		}
	}
	
	private void renderArsMagicaEffect(Tessellator tessellator)
	{		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);	
		renderSprite(tessellator);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	private void renderSprite(Tessellator tessellator)
	{

		IIcon IIcon = AMGuiIcons.instance.gatewayPortal;
		
		float min_u = IIcon.getMinU();
		float max_u = IIcon.getMaxU();
		float min_v = IIcon.getMinV();
		float max_v = IIcon.getMaxV();

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		tessellator.startDrawingQuads();
		tessellator.setBrightness(0xF00F0);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, min_u, max_v);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, max_u, max_v);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, max_u, min_v);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, min_u, min_v);
		tessellator.draw();	
		
		GL11.glRotatef(180, 0, 1, 0);
		
		tessellator.startDrawingQuads();
		tessellator.setBrightness(0xF00F0);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, min_u, max_v);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, max_u, max_v);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, max_u, min_v);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, min_u, min_v);
		tessellator.draw();	
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {

		renderAModelAt((TileEntityKeystoneRecepticle)var1, var2, var4, var6, var8);

	}

}
