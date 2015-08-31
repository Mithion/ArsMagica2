package am2.blocks.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
import am2.blocks.tileentities.TileEntityBlackAurem;
import am2.blocks.tileentities.TileEntityCelestialPrism;
import am2.blocks.tileentities.TileEntityObelisk;
import am2.render3d.OBJModel;
import am2.texture.ResourceManager;

public class EssenceGeneratorRenderer extends TileEntitySpecialRenderer{

	private ResourceLocation rLoc_obelisk;
	private ResourceLocation rLoc_obelisk_active;
	private ResourceLocation rLoc_obelisk_active_highpower;
	private ResourceLocation rLoc_obelisk_runes;
	private ResourceLocation rLoc_celestial;
	private ResourceLocation rLoc_black;

	private final WavefrontObject model_obelisk;
	private final WavefrontObject model_celestial;

	public EssenceGeneratorRenderer(){
		rLoc_obelisk = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("obelisk.png"));
		rLoc_obelisk_active = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("obelisk_active.png"));
		rLoc_obelisk_active_highpower = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("obelisk_active_highpower.png"));
		rLoc_obelisk_runes = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("obelisk_runes.png"));
		rLoc_celestial = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("celestial_prism.png"));

		rLoc_black = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("black_aurem.png"));

		model_obelisk = (WavefrontObject) AdvancedModelLoader.loadModel(ResourceManager.getOBJFilePath("Obelisk.obj"));
		model_celestial = (WavefrontObject) AdvancedModelLoader.loadModel(ResourceManager.getOBJFilePath("celestial_prism.obj"));
	}

	public void renderAModelAt(TileEntityObelisk tile, double d, double d1, double d2, float f) {

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_TEXTURE_BIT);
		if (tile instanceof TileEntityCelestialPrism)
			renderCelestial((TileEntityCelestialPrism) tile, d, d1, d2, f);
		else if (tile instanceof TileEntityBlackAurem)
			renderBlackAurem((TileEntityBlackAurem) tile, d, d1, d2, f);
		else
			renderObelisk(tile, d, d1, d2, f);

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private void renderObelisk(TileEntityObelisk tile, double d, double d1, double d2, float f){

		if (tile.xCoord == 0 && tile.yCoord == 0 && tile.zCoord == 0){
			GL11.glScalef(0.75f, 0.75f, 0.75f);
			GL11.glTranslatef(0, -0.5f, 0);
		}

		int i = 2;

		if (tile.getWorldObj() != null)
		{
			i = tile.getBlockMetadata();
		}
		int j = i * 90;
		
		GL11.glTranslated(d+0.5, d1, d2+0.5);
		GL11.glEnable (GL11.GL_BLEND);
		GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		if (tile.isHighPowerActive())
			bindTexture(rLoc_obelisk_active_highpower);
		else if (tile.isActive())
			bindTexture(rLoc_obelisk_active);
		else
			bindTexture(rLoc_obelisk);
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		try{
			model_obelisk.renderAll();
		}catch(Throwable t){
			
		}

		//runes
		if (tile.isActive()){
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			bindTexture(rLoc_obelisk_runes);
			float normx = (System.currentTimeMillis() % 32000) / 32000.0f;
			float normy = (System.currentTimeMillis() % 28000) / 28000.0f;
			GL11.glTranslatef(normx, normy, 0);		
			float transp = (float) Math.abs(Math.sin(System.currentTimeMillis() / 1000.0));
			GL11.glColor4f(1, 1, 1, transp);
			try{
				model_obelisk.renderAll();
			}catch(Throwable t){
				
			}
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
	}

	private void renderCelestial(TileEntityCelestialPrism tile, double d, double d1, double d2, float f){
		bindTexture(rLoc_celestial);
		if (tile.xCoord != 0 && tile.yCoord != 0 && tile.zCoord != 0){
			GL11.glTranslatef((float)d + 0.5f, (float)d1, (float)d2 + 0.5f);
			GL11.glScalef(2, 2, 2);
		}else{
			GL11.glTranslatef((float)d + 0.5f, (float)d1 - 0.3f, (float)d2 + 0.5f);
			GL11.glScalef(1.75f, 1.75f, 1.75f);
		}
		GL11.glEnable (GL11.GL_BLEND);
		GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_CULL_FACE);
		RenderHelper.disableStandardItemLighting();
		Tessellator tessellator = Tessellator.instance;

		try{
			model_celestial.renderAll();
		}catch(Throwable t){
			
		}

		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable (GL11.GL_BLEND);
	}

	private void renderBlackAurem(TileEntityBlackAurem tile, double d, double d1, double d2, float f){
		GL11.glTranslatef((float)d + 0.5f, (float)d1 + 1f, (float)d2 + 0.5f);
		GL11.glDepthMask(false);
		GL11.glEnable (GL11.GL_BLEND);
		GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


		RenderHelper.disableStandardItemLighting();

		Tessellator tessellator = Tessellator.instance;

		renderArsMagicaEffect(tessellator, tile.xCoord + tile.yCoord + tile.zCoord, 1);

		RenderHelper.enableStandardItemLighting();
		GL11.glDisable (GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	private void renderArsMagicaEffect(Tessellator tessellator, float offset, float scale)
	{
		if (offset != 0){
			GL11.glRotatef(180F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
		}else{
			GL11.glRotatef(35, 0, 1, 0);
			GL11.glTranslatef(0, -0.75f, 0);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(rLoc_black);
		GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted, 0, 0, 1);
		GL11.glScalef(scale*2, scale*2, scale*2);
		GL11.glTranslatef(0.0f, -0.25f, 0.0f);
		renderSprite(tessellator);

	}

	private void renderSprite(Tessellator tessellator)
	{

		float TLX = 0;
		float BRX = 1;
		float TLY = 0;
		float BRY = 1;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		try{
		tessellator.startDrawingQuads();
		tessellator.setBrightness(15728863);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, TLX, BRY);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, BRX, BRY);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, BRX, TLY);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, TLX, TLY);
		tessellator.draw();
		}catch(Throwable t) {}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		renderAModelAt((TileEntityObelisk) tileentity, d, d1, d2, f); //where to render
	}
}
