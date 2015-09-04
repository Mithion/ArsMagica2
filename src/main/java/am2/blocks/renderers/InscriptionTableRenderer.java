package am2.blocks.renderers;

import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.models.ModelInscriptionTableLeft;
import am2.models.ModelInscriptionTableRight;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class InscriptionTableRenderer extends TileEntitySpecialRenderer{

	public static final ResourceLocation inscriptionTableTexture = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("InscriptionTable.png"));

	public InscriptionTableRenderer(){
		modelLeft = new ModelInscriptionTableLeft();
		modelRight = new ModelInscriptionTableRight();
	}

	public void renderAModelAt(TileEntityInscriptionTable tile, double d, double d1, double d2, float f){
		int i = 0;

		if (tile.getWorldObj() != null){
			i = tile.getBlockMetadata();
		}
		int j = (i & ~0x8) * 90;

		Minecraft.getMinecraft().renderEngine.bindTexture(inscriptionTableTexture);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F); //size
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens

		if (d == 0 && d1 == 0 && d2 == 0){
			GL11.glScalef(0.8f, 0.8f, 0.8f);
			GL11.glTranslatef(-0.4f, 0.3f, 0.0f);
			modelLeft.renderModel(0.0625f, tile.getUpgradeState());
			GL11.glTranslatef(1f, 0.0f, 0.0f);
			modelRight.renderMode(0.0625f, tile.getUpgradeState());
		}else{
			if ((i & 0x8) == 0x8)
				modelLeft.renderModel(0.0625F, tile.getUpgradeState()); //renders and yes 0.0625 is a random number
			else
				modelRight.renderMode(0.0625F, tile.getUpgradeState());
		}

		GL11.glPopMatrix(); //end


	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f){
		renderAModelAt((TileEntityInscriptionTable)tileentity, d, d1, d2, f); //where to render
	}

	private ModelInscriptionTableLeft modelLeft;
	private ModelInscriptionTableRight modelRight;
}
