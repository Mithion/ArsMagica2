package am2.blocks.renderers;

import java.util.Random;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityLectern;
import am2.guis.AMGuiHelper;
import am2.models.ModelArchmagePodium;
import am2.texture.ResourceManager;

public class LecternRenderer extends TileEntitySpecialRenderer{

	private final ResourceLocation rLoc;
	private final ResourceLocation book;
	private final ResourceLocation item_atlas;
	private final ResourceLocation block_atlas;
	RenderItem renderItem;


	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		renderTileEntityArchmagePodiumAt((TileEntityLectern) var1, var2, var4, var6, var8);
	}

	private final ModelArchmagePodium archmagePodium = new ModelArchmagePodium();
	private final ModelBook enchantmentBook = new ModelBook();

	public LecternRenderer(){
		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("archmagePodium.png"));
		book = new ResourceLocation("textures/entity/enchanting_table_book.png");
		item_atlas = new ResourceLocation("textures/atlas/items.png");
		block_atlas = new ResourceLocation("textures/atlas/blocks.png");

		renderItem = (RenderItem) RenderManager.instance.entityRenderMap.get(EntityItem.class);
	}

	public void renderTileEntityArchmagePodiumAt(TileEntityLectern podium, double d, double d1, double d2, float f1)
	{
		int meta = 0;
		if (podium.getWorldObj() != null) meta = podium.getWorldObj().getBlockMetadata(podium.xCoord, podium.yCoord, podium.zCoord) - 1;

		int i = 0;

		if (podium.getWorldObj() != null)
		{
			i = podium.getBlockMetadata();
		}
		int j = i * 90;

		bindTexture(rLoc);
		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.9F, (float)d2 + 0.5F); //size
		GL11.glRotatef(j, 0.0F, 1.0F, 0.0F); //rotate based on metadata
		GL11.glScalef(1.0F, -1F, -1F); //if you read this comment out this line and you can see what happens
		GL11.glScalef(1.0F, 0.6F, 1.0F);
		archmagePodium.renderModel(0.0625F); //renders and yes 0.0625 is a random number
		GL11.glPopMatrix(); //end

		if (podium.hasStack()){
			if (podium.getOverpowered())
				GL11.glColor4f(0.7f, 0.2f, 0.2f, 1.0f);
			else
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			RenderBook(podium, d, d1, d2, f1, meta);
		}else if (podium.getNeedsBook()){
			GL11.glColor4f(0.7f, 0.2f, 0.2f, 0.2f);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderBook(podium, d, d1, d2, f1, meta);
			GL11.glDisable(GL11.GL_BLEND);
		}

		renderHelperIcon(podium, d, d1, d2, f1);
	}

	private void renderHelperIcon(TileEntityLectern podium, double d, double d1, double d2, float f){
		if (podium.getTooltipStack() == null){
			podium.resetParticleAge();
			return;
		}

		ItemStack stack = podium.getTooltipStack().copy();
		stack.stackSize = 1;
		AMGuiHelper.instance.dummyItem.setEntityItemStack(stack);		
		renderItem.doRender(AMGuiHelper.instance.dummyItem, d + 0.5f, d1 + 1.4f, d2 + 0.5f, AMGuiHelper.instance.dummyItem.rotationYaw, f);

		GL11.glPushMatrix();
		GL11.glTranslated(d + 0.5f, d1 + 1f, d2 + 0.5f);
		float scale = 0.2f;
		GL11.glScalef(scale, scale, scale);
		renderRadiant(Tessellator.instance, f, podium);
		GL11.glPopMatrix();
	}

	private void renderRadiant(Tessellator tessellator, float partialFrame, TileEntityLectern podium){
		RenderHelper.disableStandardItemLighting();
		float var4 = (podium.particleAge + partialFrame) / podium.particleMaxAge;
		float var5 = 0.0F;

		if (var4 > 0.8F)
		{
			var5 = (var4 - 0.8F) / 0.2F;
		}

		Random var6 = new Random(432L);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glPushMatrix();

		for (int var7 = 0; var7 < 20.0F; ++var7)
		{
			float rH = 90f;
			float oH = 0f;
			GL11.glPushMatrix();
			GL11.glRotatef(podium.field_145926_a % 360, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var6.nextFloat() * 180, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var6.nextFloat() * -180, 1.0F, 0.0F, 0.0F);
			tessellator.startDrawing(6);
			float var8 = var6.nextFloat() * 2.0F + 2.0F + var5 * 0.5F;
			float var9 = var6.nextFloat() * 2.0F + 1.0F + var5 * 2.0F;
			if (!podium.getOverpowered())
				tessellator.setColorRGBA_F(0.2f, 0.2f, 1.0f, 0.2f);
			else
				tessellator.setColorRGBA_F(1.0f, 0.2f, 0.2f, 0.2f);
			tessellator.addVertex(0.0D, 0.0D, 0.0D);
			if (!podium.getOverpowered())
				tessellator.setColorRGBA_F(0.2f, 0.2f, 1.0f, 0.0f);
			else
				tessellator.setColorRGBA_F(1.0f, 0.2f, 0.2f, 0.0f);
			tessellator.addVertex(-0.866D * var9, var8, -0.5F * var9);
			tessellator.addVertex(0.866D * var9, var8, -0.5F * var9);
			tessellator.addVertex(0.0D, var8, 1.0F * var9);
			tessellator.addVertex(-0.866D * var9, var8, -0.5F * var9);
			tessellator.draw();
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	private void RenderBook(TileEntityLectern p_147539_1_, double p_147539_2_, double p_147539_4_, double p_147539_6_, float p_147539_8_, int meta){
		GL11.glPushMatrix();
		GL11.glTranslatef((float)p_147539_2_ + 0.5F, (float)p_147539_4_ + 0.75F, (float)p_147539_6_ + 0.5F);
		float var9 = (float)p_147539_1_.field_145926_a + p_147539_8_;
		GL11.glTranslatef(0.0F, 0.1F + MathHelper.sin(var9 * 0.01F) * 0.01F, 0.0F);
		float f2;

		for (f2 = p_147539_1_.field_145928_o - p_147539_1_.field_145925_p; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
		{
			;
		}

		while (f2 < -(float)Math.PI)
		{
			f2 += ((float)Math.PI * 2F);
		}

		float var11 = p_147539_1_.field_145925_p + f2 * p_147539_8_;
		//GL11.glRotatef(-var11 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
		if (meta == 1 || meta == 3)
			GL11.glRotatef(-90 * meta, 0.0F, 1.0F, 0.0F);
		else if (meta == 2)
			GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		else
			GL11.glRotatef(-180, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(80.0F, 0.0F, 0.0F, 1.0F);
		//GL11.glRotatef(-9, 0, 0, 1);
		bindTexture(book);
		float var12 = p_147539_1_.field_145931_j + (p_147539_1_.field_145933_i - p_147539_1_.field_145931_j) * p_147539_8_ + 0.25F;
		float var13 = p_147539_1_.field_145931_j + (p_147539_1_.field_145933_i - p_147539_1_.field_145931_j) * p_147539_8_ + 0.75F;
		var12 = (var12 - MathHelper.truncateDoubleToInt(var12)) * 1.6F - 0.3F;
		var13 = (var13 - MathHelper.truncateDoubleToInt(var13)) * 1.6F - 0.3F;

		if (var12 < 0.0F)
		{
			var12 = 0.0F;
		}

		if (var13 < 0.0F)
		{
			var13 = 0.0F;
		}

		if (var12 > 1.0F)
		{
			var12 = 1.0F;
		}

		if (var13 > 1.0F)
		{
			var13 = 1.0F;
		}

		float var14 = p_147539_1_.field_145927_n + (p_147539_1_.field_145930_m - p_147539_1_.field_145927_n) * p_147539_8_;		
		this.enchantmentBook.setRotationAngles(var9, var12, var13, 1f, 0.0F, 0.0625F, (Entity)null);
		this.enchantmentBook.coverRight.render(0.0625F);
		this.enchantmentBook.coverLeft.render(0.0625F);
		this.enchantmentBook.bookSpine.render(0.0625F);
		this.enchantmentBook.pagesRight.render(0.0625F);
		this.enchantmentBook.pagesLeft.render(0.0625F);
		this.enchantmentBook.flippingPageRight.render(0.0625F);
		this.enchantmentBook.flippingPageLeft.render(0.0625F);
		GL11.glPopMatrix();
	}

	/*
	 * GL11.glPushMatrix();
        GL11.glTranslatef((float)p_147539_2_ + 0.5F, (float)p_147539_4_ + 0.75F, (float)p_147539_6_ + 0.5F);
        float f1 = (float)p_147539_1_.field_145926_a + p_147539_8_;
        GL11.glTranslatef(0.0F, 0.1F + MathHelper.sin(f1 * 0.1F) * 0.01F, 0.0F);
        float f2;

        for (f2 = p_147539_1_.field_145928_o - p_147539_1_.field_145925_p; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
        {
            ;
        }

        while (f2 < -(float)Math.PI)
        {
            f2 += ((float)Math.PI * 2F);
        }

        float f3 = p_147539_1_.field_145925_p + f2 * p_147539_8_;
        GL11.glRotatef(-f3 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(80.0F, 0.0F, 0.0F, 1.0F);
        this.bindTexture(book);
        float f4 = p_147539_1_.field_145931_j + (p_147539_1_.field_145933_i - p_147539_1_.field_145931_j) * p_147539_8_ + 0.25F;
        float f5 = p_147539_1_.field_145931_j + (p_147539_1_.field_145933_i - p_147539_1_.field_145931_j) * p_147539_8_ + 0.75F;
        f4 = (f4 - (float)MathHelper.truncateDoubleToInt((double)f4)) * 1.6F - 0.3F;
        f5 = (f5 - (float)MathHelper.truncateDoubleToInt((double)f5)) * 1.6F - 0.3F;

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f5 < 0.0F)
        {
            f5 = 0.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        if (f5 > 1.0F)
        {
            f5 = 1.0F;
        }

        float f6 = p_147539_1_.field_145927_n + (p_147539_1_.field_145930_m - p_147539_1_.field_145927_n) * p_147539_8_;
        GL11.glEnable(GL11.GL_CULL_FACE);
        this.enchantmentBook.render((Entity)null, f1, f4, f5, f6, 0.0F, 0.0625F);
        GL11.glPopMatrix();
	 */
}
