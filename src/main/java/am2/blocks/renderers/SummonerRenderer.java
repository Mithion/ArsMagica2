package am2.blocks.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntitySummoner;
import am2.models.ModelSummoner;
import am2.texture.ResourceManager;

public class SummonerRenderer extends TileEntitySpecialRenderer{
	private ResourceLocation rLoc;
	private ResourceLocation powered;
	private ModelSummoner model;

	public SummonerRenderer(){
		model = new ModelSummoner();
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("blockSummoner.png"));
		powered = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("essenceConduit.png"));
	}

	public void renderAModelAt(TileEntitySummoner tile, double d, double d1, double d2, float f) {
		int i = 2;
		int y = 0;

		if (tile.getWorldObj() != null)
		{
			i = tile.getBlockMetadata() & 3;
			y = (tile.getBlockMetadata() & 12) >> 2;
		}
		int j = (i+1) * 90;
		int n = 0;
		if (y == 1){
			n = 90;
		}else if (y == 2){
			n = -90;
		}

		bindTexture(rLoc);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5f, (float)d1 + 1.5f, (float)d2 + 0.5f); //size
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glTranslatef(0, -1f, 0);
		GL11.glRotatef(n, 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(0, 1f, 0);
		GL11.glScalef(1.0F, -1F, -1F);
		model.renderModel(0.0625F);
		bindTexture(powered);
		model.renderCrystal(tile, 0.0625f);
		GL11.glPopMatrix(); //end
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		renderAModelAt((TileEntitySummoner) tileentity, d, d1, d2, f); //where to render
	}
}
