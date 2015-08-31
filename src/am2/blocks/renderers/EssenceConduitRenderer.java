package am2.blocks.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityEssenceConduit;
import am2.models.ModelEssenceConduit;
import am2.texture.ResourceManager;


public class EssenceConduitRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation powered;
	private ResourceLocation unPowered;

	public EssenceConduitRenderer() {
		model = new ModelEssenceConduit();

		powered = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("essenceConduit.png"));
		unPowered = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("essenceConduitUnpowered.png"));
	}

	public void renderAModelAt(TileEntityEssenceConduit tile, double d, double d1, double d2, float f) {

		int i = 0;

		if (tile.getWorldObj() != null)
		{
			i = tile.getBlockMetadata();
		}

		GL11.glPushMatrix(); //start

		int meta = i;


		switch (meta){
		case 1:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 - 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(180, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			break;
		case 0:
		case 2:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 1.5F, (float)d2 + 0.5F); //size
			break;
		case 3:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5F, (float)d2 - 0.5F); //size
			GL11.glRotatef(270, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			break;
		case 4:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5F, (float)d2 + 1.5F); //size
			GL11.glRotatef(90, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			break;
		case 5:
			GL11.glTranslatef((float)d - 0.5f, (float)d1 + 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(90, 0.0F, 0.0F, 1.0F); //rotate based on metadata
			break;
		case 6:
			GL11.glTranslatef((float)d + 1.5F, (float)d1 + 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(270, 0.0F, 0.0F, 1.0F); //rotate based on metadata
			break;
		}


		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		//if (tile.hasNexusPath()){
		bindTexture(powered);
		//}else{
		//bindTexture(unPowered);
		//}

		tile.incrementRotations();
		model.renderModel(tile.getRotationX(), tile.getRotationY(), tile.getRotationZ(), 0 , 0.0625F); //renders and yes 0.0625 is a random number
		GL11.glPopMatrix(); //end
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		renderAModelAt((TileEntityEssenceConduit) tileentity, d, d1, d2, f); //where to render
	}

	private ModelEssenceConduit model;

}
