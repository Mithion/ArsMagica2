package am2.blocks.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;


public class MageLightRenderer extends TileEntitySpecialRenderer{

	private RenderManager renderManager;

	public MageLightRenderer(){
		renderManager = RenderManager.instance;
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
								   double var6, float var8){

	}

}
