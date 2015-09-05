package am2.blocks.renderers;

import am2.blocks.BlockCrystalMarker;
import am2.blocks.tileentities.TileEntityCrystalMarker;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class CrystalMarkerRenderer extends TileEntitySpecialRenderer{
	private ResourceLocation rloc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("crystalmarker.png"));
	private IModelCustom model;

	public CrystalMarkerRenderer(){
		model = AdvancedModelLoader.loadModel(ResourceManager.getOBJFilePath("Crystal_Marker.obj"));
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
								   double d2, float f){
		doRender((TileEntityCrystalMarker)tileentity, d0, d1, d2, f);
	}

	public void doRender(TileEntityCrystalMarker tileentity, double x, double y, double z, float partialTicks){
		int facing = 0;

		if (tileentity.getWorldObj() != null){
			facing = tileentity.getFacing();
		}

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		Minecraft.getMinecraft().renderEngine.bindTexture(rloc);
		GL11.glDisable(GL11.GL_CULL_FACE);
		RenderHelper.disableStandardItemLighting();

		if (tileentity.xCoord != 0 || tileentity.yCoord != 0 || tileentity.zCoord != 0){
			switch (facing){
			case 0: //Bottom, Inventory is above
				GL11.glTranslated(x + 0.5, y + 1.0 + tileentity.GetConnectedBoundingBox().minY, z + 0.5);
				GL11.glRotated(90, 1, 0, 0);
				break;
			case 1: //Top, Inventory is below
				GL11.glTranslated(x + 0.5, y - (1.0 - tileentity.GetConnectedBoundingBox().maxY), z + 0.5);
				GL11.glRotated(270, 1, 0, 0);
				break;
			case 2: //North, Inventory is to the south
				GL11.glTranslated(x + 0.5, y + 0.5, z + 1.0 + (1.0 - tileentity.GetConnectedBoundingBox().maxZ));
				GL11.glRotated(180, 0, 1, 0);
				break;
			case 3: //South, Inventory is to the north
				GL11.glTranslated(x + 0.5, y + 0.5, z - tileentity.GetConnectedBoundingBox().minZ);
				break;
			case 4: //West, Inventory is to the east
				GL11.glTranslated(x + 1 + tileentity.GetConnectedBoundingBox().minX, y + 0.5, z + 0.5);
				GL11.glRotated(270, 0, 1, 0);
				break;
			case 5: //East, Inventory is to the west
				GL11.glTranslated(x - (1.0 - tileentity.GetConnectedBoundingBox().maxX), y + 0.5, z + 0.5);
				GL11.glRotated(90, 0, 1, 0);
				break;
			}

			GL11.glScalef(0.5f, 0.5f, 0.5f);
		}else{
			GL11.glScalef(1.4f, 1.4f, 1.4f);
		}

		int blockType = 0;

		if (tileentity.getWorldObj() != null){
			blockType = tileentity.getBlockMetadata();
		}else{
			blockType = (int)partialTicks;
		}

		switch (blockType){
		case BlockCrystalMarker.META_IN:
			GL11.glColor3f(0.94f, 0.69f, 0.01f); //yellow
			break;
		case BlockCrystalMarker.META_LIKE_EXPORT:
			GL11.glColor3f(0.10f, 0.65f, 0.0f); //green
			break;
		case BlockCrystalMarker.META_OUT:
			GL11.glColor3f(0.10f, 0.10f, 0.88f); //blue
			break;
		case BlockCrystalMarker.META_REGULATE_EXPORT:
			GL11.glColor3f(0.56f, 0.08f, 0.66f); //purple
			break;
		case BlockCrystalMarker.META_SET_EXPORT:
			GL11.glColor3f(0.085f, 0.72f, 0.88f); //light blue
			break;
		case BlockCrystalMarker.META_REGULATE_MULTI:
			GL11.glColor3f(0.92f, 0.61f, 0.3f); //orange
			break;
		case BlockCrystalMarker.META_SET_IMPORT:
			GL11.glColor3f(1.0f, 0.0f, 0.0f); //red
			break;
		case BlockCrystalMarker.META_SPELL_EXPORT:
			GL11.glColor3f(0.0f, 0.5f, 1.0f); //cyan
			break;
		}

		try{
			model.renderAll();
		}catch (Throwable t){

		}

		GL11.glPopAttrib();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

}
