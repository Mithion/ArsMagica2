package am2.blocks.renderers;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityCalefactor;
import am2.guis.AMGuiHelper;
import am2.models.ModelCalefactor;
import am2.texture.ResourceManager;

public class CalefactorRenderer extends TileEntitySpecialRenderer{

	private final ResourceLocation rLoc;
	RenderItem renderItem;

	public CalefactorRenderer() {
		model = new ModelCalefactor();

		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("blockCalefactor.png"));
		renderItem = (RenderItem) RenderManager.instance.entityRenderMap.get(EntityItem.class);
	}

	public void renderAModelAt(TileEntityCalefactor tile, double d, double d1, double d2, float f) {

		int i = 0;

		if (tile.getWorldObj() != null)
		{
			i = tile.getBlockMetadata();
		}

		GL11.glPushMatrix(); //start

		int meta = i;

		double offsetX, offsetY, offsetZ;

		offsetX = 0;
		offsetY = 0;
		offsetZ = 0;


		switch (meta){
		case 1:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 - 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(180, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			offsetX = 0.5;
			offsetY = -1.5;
			offsetZ = 0.5;
			break;
		case 0:
		case 2:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 1.5F, (float)d2 + 0.5F); //size
			offsetX = 0.5;
			offsetY = 0.0;
			offsetZ = 0.5;
			break;
		case 3:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5F, (float)d2 - 0.5F); //size
			GL11.glRotatef(270, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			offsetX = 0.5;
			offsetY = -0.8;
			offsetZ = -0.2;
			break;
		case 4:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5F, (float)d2 + 1.5F); //size
			GL11.glRotatef(90, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			offsetX = 0.5;
			offsetY = -0.8;
			offsetZ = 1.2;
			break;
		case 5:
			GL11.glTranslatef((float)d - 0.5f, (float)d1 + 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(90, 0.0F, 0.0F, 1.0F); //rotate based on metadata
			offsetX = -0.2;
			offsetY = -0.8;
			offsetZ = 0.5;
			break;
		case 6:
			GL11.glTranslatef((float)d + 1.5F, (float)d1 + 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(270, 0.0F, 0.0F, 1.0F); //rotate based on metadata
			offsetX = 1.2;
			offsetY = -0.8;
			offsetZ = 0.5;
			break;
		}


		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		bindTexture(rLoc);

		if (tile.xCoord == 0 && tile.yCoord == 0 && tile.zCoord == 0)
			GL11.glTranslatef(0, 0.2f, 0);

		model.renderModel(tile.getRotationX(), tile.getRotationY(), tile.getRotationZ(), 0.0625F); //renders and yes 0.0625 is a random number
		GL11.glPopMatrix(); //end

		//render item being cooked, if any
		ItemStack item = tile.getItemBeingCooked();
		if (item != null){
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 1f, 0);
			RenderItemAtCoords(item, d + offsetX, d1 + offsetY, d2 + offsetZ, f);
			GL11.glPopMatrix();
		}

	}

	private void RenderItemAtCoords(ItemStack item, double x, double y, double z, float partialTicks){
		AMGuiHelper.instance.dummyItem.setEntityItemStack(item);
		renderItem.doRender(AMGuiHelper.instance.dummyItem, x, y, z, AMGuiHelper.instance.dummyItem.rotationYaw, partialTicks);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		renderAModelAt((TileEntityCalefactor) tileentity, d, d1, d2, f); //where to render
	}

	private final ModelCalefactor model;
}
