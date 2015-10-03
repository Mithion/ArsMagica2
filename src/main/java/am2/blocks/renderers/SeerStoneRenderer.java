package am2.blocks.renderers;

import am2.api.power.PowerTypes;
import am2.blocks.tileentities.TileEntitySeerStone;
import am2.models.ModelSeerStone;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SeerStoneRenderer extends TileEntitySpecialRenderer{

	private static final float TextureSize = 640;
	private static final float FrameSize = 64;

	private ResourceLocation block;
	private ResourceLocation eye;

	public SeerStoneRenderer(){
		model = new ModelSeerStone();
		block = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("blockSeerStone.png"));
		eye = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("eye_bw.png"));
	}

	public void renderAModelAt(TileEntitySeerStone tile, double d, double d1, double d2, float f){
		int i = 0;

		if (tile.getWorld() != null){
			i = tile.getBlockMetadata();
		}

		GL11.glPushMatrix(); //start

		int meta = i;


		switch (meta){
		case 1:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 - 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(180, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			break;
		case 0:
		case 2:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 1.5F, (float)d2 + 0.5F); //size
			break;
		case 3:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5F, (float)d2 - 0.5F); //size
			GL11.glRotatef(270, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			break;
		case 4:
			GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5F, (float)d2 + 1.5F); //size
			GL11.glRotatef(90, 1.0F, 0.0F, 0.0F); //rotate based on metadata
			break;
		case 5:
			GL11.glTranslatef((float)d - 0.5f, (float)d1 + 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(90, 0.0F, 0.0F, 1.0F); //rotate based on metadata
			break;
		case 6:
			GL11.glTranslatef((float)d + 1.5F, (float)d1 + 0.5F, (float)d2 + 0.5F); //size
			GL11.glRotatef(270, 0.0F, 0.0F, 1.0F); //rotate based on metadata
			break;
		}


		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		bindTexture(block);
		model.renderModel(0.0625F); //renders and yes 0.0625 is a random number
		GL11.glPopMatrix(); //end

		if (tile.ShouldAnimate())
			RenderEye(PowerTypes.NEUTRAL, tile.getAnimationIndex(), d, d1, d2, meta);
	}

	private void RenderEye(PowerTypes powerType, int index, double d, double d1, double d2, int meta){
		bindTexture(eye);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.5f, (float)d2 + 0.5f);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;

		if (powerType == PowerTypes.DARK)
			GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		else if (powerType == PowerTypes.LIGHT)
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		else if (powerType == PowerTypes.NEUTRAL)
			GL11.glColor4f(0.0f, 0.2f, 1.0f, 1.0f);

		renderArsMagicaEffect(tessellator, index, meta);

		GL11.glDisable(GL11.GL_BLEND);
		//GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glPopMatrix(); //end
	}

	private void renderArsMagicaEffect(Tessellator tessellator, int i, int meta){
		float iof = TextureSize / FrameSize;
		float foi = FrameSize / TextureSize;

		float TLX = (i % iof) * foi;
		float BRX = (i % iof) * foi + foi;
		float TLY = (float)(Math.floor(i / iof) * foi);
		float BRY = (float)(Math.floor(i / iof) * foi + foi);

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		tessellator.setBrightness(15728864);

		//GL11.glRotatef(90, 0.0f, 1.0f, 0f);
		//GL11.glRotatef(90, 0.0f, 1.0f, 0f);


		switch (meta){
		case 1:
			GL11.glTranslatef(0.0f, -0.4f, 0.0f);
			break;
		case 3:
			GL11.glTranslatef(0.0f, -0.2f, -0.4f);
			break;
		case 4:
			GL11.glTranslatef(0.0f, -0.2f, 0.4f);
			break;
		case 5:
			GL11.glTranslatef(-0.4f, -0.2f, 0.0f);
			break;
		case 6:
			GL11.glTranslatef(0.4f, -0.2f, 0.0f);
			break;
		}

		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

		tessellator.setNormal(0.0f, 0.0f, 1.0f);

		GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, TLX, BRY);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, BRX, BRY);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, BRX, TLY);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, TLX, TLY);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f){
		renderAModelAt((TileEntitySeerStone)tileentity, d, d1, d2, f); //where to render
	}

	private ModelSeerStone model;
}
