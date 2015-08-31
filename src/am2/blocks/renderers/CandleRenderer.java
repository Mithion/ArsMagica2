package am2.blocks.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityCandle;
import am2.models.ModelCandle;
import am2.texture.ResourceManager;

public class CandleRenderer extends TileEntitySpecialRenderer{

	private ModelCandle candle;
	private ResourceLocation rLoc;

	public CandleRenderer(){
		candle = new ModelCandle();
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("candle.png"));
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {
		if (tileentity instanceof TileEntityCandle){
			renderCandle((TileEntityCandle)tileentity, d0, d1, d2, f);
		}
	}

	private void renderCandle(TileEntityCandle tileentity, double d, double d1, double d2, float var8) {
		 bindTexture(rLoc);
	     GL11.glPushMatrix(); //start

	     GL11.glTranslated(d + 0.5f, d1 + 1.5f, d2 + 0.5f);
	     GL11.glScalef(1, -1, -1);

	     candle.render(0.0625f);
	     GL11.glPopMatrix();
	}

}
