package am2.blocks.renderers;

import am2.blocks.tileentities.TileEntityMagiciansWorkbench;
import am2.models.ModelMagiciansWorkbench;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MagiciansWorkbenchRenderer extends TileEntitySpecialRenderer{

	private final ResourceLocation rLoc;

	public MagiciansWorkbenchRenderer(){
		model = new ModelMagiciansWorkbench();
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("magiciansWorkbench.png"));
	}

	public void renderAModelAt(TileEntityMagiciansWorkbench tile, double d, double d1, double d2, float f){
		int i = 0;

		if (tile.getWorld() != null){
			i = tile.getBlockMetadata();
		}
		int j = i * 90;

		bindTexture(rLoc);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F); //size
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		model.drawerOffset = tile.getDrawerOffset();
		model.renderModel(i);
		GL11.glPopMatrix(); //end

	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f){
		renderAModelAt((TileEntityMagiciansWorkbench)tileentity, d, d1, d2, f); //where to render
	}

	private final ModelMagiciansWorkbench model;

}
