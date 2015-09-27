package am2.blocks.renderers;

import am2.blocks.tileentities.TileEntityKeystoneChest;
import am2.texture.ResourceManager;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class KeystoneChestRenderer extends TileEntitySpecialRenderer{

	private ModelChest model;

	private ResourceLocation rLoc;

	public KeystoneChestRenderer(){
		model = new ModelChest();
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("keystoneChest.png"));
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float var8){
		if (tileentity instanceof TileEntityKeystoneChest){
			renderKeystoneChestAt((TileEntityKeystoneChest)tileentity, d, d1, d2, var8);
		}
	}

	private void renderKeystoneChestAt(TileEntityKeystoneChest chest, double d, double d1, double d2, float var8){
		int i = 0;

		if (chest.getWorld() != null){
			i = chest.getBlockMetadata() & 3;
		}
		int j = 0;

		if (i == 0){
			j = 90;
		}else if (i == 1){
			j = 180;
		}else if (i == 2){
			j = 270;
		}else if (i == 3){
			j = 0;
		}

		bindTexture(rLoc);
		GL11.glPushMatrix(); //start

		if (i == 1){
			GL11.glTranslatef((float)d + 1f, (float)d1 + 1f, (float)d2); //size
		}else if (i == 2){
			GL11.glTranslatef((float)d, (float)d1 + 1f, (float)d2); //size
		}else if (i == 3){
			GL11.glTranslatef((float)d, (float)d1 + 1f, (float)d2 + 1f); //size
		}else{
			GL11.glTranslatef((float)d + 1f, (float)d1 + 1f, (float)d2 + 1f); //size
		}
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens

		float f1 = chest.getPrevLidAngle() + (chest.getLidAngle() - chest.getPrevLidAngle()) * var8;
		f1 = 1.0F - f1;
		f1 = 1.0F - f1 * f1 * f1;
		model.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
		model.renderAll();
		GL11.glPopMatrix(); //end
	}

}
