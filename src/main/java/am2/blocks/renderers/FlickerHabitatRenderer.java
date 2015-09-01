/**
 *
 */
package am2.blocks.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.texture.ResourceManager;

/**
 * @author Zero
 *
 */
public class FlickerHabitatRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation rloc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("habitat.png"));
	private IModelCustom model;

	public FlickerHabitatRenderer(){
		model = AdvancedModelLoader.loadModel(ResourceManager.getOBJFilePath("flickerHabitat.obj"));
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		doRender((TileEntityFlickerHabitat) tileentity, d0, d1, d2, f);
	}

	public void doRender(TileEntityFlickerHabitat tileentity, double x, double y, double z, float partialTicks){

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		Minecraft.getMinecraft().renderEngine.bindTexture(rloc);
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		try{
			model.renderOnly("frame");
		}catch(Throwable t){}

		if(tileentity.hasFlicker()){

			if(tileentity.isUpgrade()){
				switch(tileentity.getMainHabitatDirection()){
				case DOWN:
					GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
					GL11.glTranslatef(0.0f, -0.9f, 0.0f);
					break;
				case EAST:
					GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
					GL11.glTranslatef(-0.5f, -0.45f, 0.0f);
					break;
				case NORTH:
					GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glTranslatef(0.0f, -0.45f, 0.5f);
					break;
				case SOUTH:
					GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
					GL11.glTranslatef(0.0f, -0.45f, -0.5f);
					break;
				case UNKNOWN:
					break;
				case UP:
					break;
				case WEST:
					GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
					GL11.glTranslatef(0.5f, -0.45f, 0.0f);
					break;
				default:
					break;
				}

				GL11.glScalef(0.85f, 0.85f, 0.85f);
			}

			GL11.glRotatef(tileentity.getRotateOffset(), 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(0.0f, tileentity.getFloatOffset(), 0.0f);
			try{
				int color = tileentity.getCrystalColor();
				GL11.glColor3f(
						((color >> 16) & 0xFF) / 255.0f, //isolate red  & convert to normalized float
						((color >> 8) & 0xFF) / 255.0f, //isolate green & convert to normalized float
						(color & 0xFF) / 255.0f //isolate blue & convert to normalized float
				);
			model.renderOnly("crystal");
			}catch (Throwable t){}
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

}
