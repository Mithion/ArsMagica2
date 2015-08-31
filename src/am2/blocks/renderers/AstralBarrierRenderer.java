package am2.blocks.renderers;

import org.lwjgl.opengl.GL11;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityAstralBarrier;
import am2.models.ModelAstralBarrier;
import am2.texture.ResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class AstralBarrierRenderer extends TileEntitySpecialRenderer {

	private ModelAstralBarrier model;
	private ResourceLocation rLoc;

	public AstralBarrierRenderer(){
		model = new ModelAstralBarrier();
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("blockAstralBarrier.png"));
	}

	public void renderAModelAt(TileEntityAstralBarrier tile, double d, double d1, double d2, float f) {
		int i = 0;

		if (tile.getWorldObj() != null)
		{
			i = tile.getBlockMetadata();
		}
		int j = 0;

		if (i == 0)
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
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {

		renderAModelAt((TileEntityAstralBarrier)var1, var2, var4, var6, var8);

	}
	
}
