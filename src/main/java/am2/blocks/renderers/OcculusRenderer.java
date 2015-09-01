package am2.blocks.renderers;

import org.lwjgl.opengl.GL11;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityOcculus;
import am2.models.ModelOcculus;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;

public class OcculusRenderer extends TileEntitySpecialRenderer{

	private ResourceLocation rLoc;
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		renderTileEntityOcculusAt((TileEntityOcculus) var1, var2, var4, var6, var8);
	}

	private ModelOcculus occulus = new ModelOcculus();
	
	public OcculusRenderer(){
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("Occulus.png"));
	}

	public void renderTileEntityOcculusAt(TileEntityOcculus podium, double d, double d1, double d2, float f1)
	{
		
		int meta = 0;
		if (podium.getWorldObj() != null) meta = podium.getWorldObj().getBlockMetadata(podium.xCoord, podium.yCoord, podium.zCoord) - 1;

		int i = 2;

		if (podium.getWorldObj() != null)
		{
			i = podium.getBlockMetadata();
		}
		int j = i * 90;

		bindTexture(rLoc);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.9F, (float)d2 + 0.5F); //size
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		GL11.glScalef(1.0F, 0.6F, 1.0F);
		occulus.renderModel(0.0625F); //renders and yes 0.0625 is a random number
		GL11.glPopMatrix(); //end
	}

}
