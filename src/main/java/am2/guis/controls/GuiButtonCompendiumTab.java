package am2.guis.controls;

import am2.guis.AMGuiHelper;
import am2.guis.GuiArcaneCompendium;
import am2.particles.AMParticleIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiButtonCompendiumTab extends GuiButton{

	private final static int sourceWidth = 12;
	private final static int sourceHeight = 12;

	private boolean isActive;
	private boolean hasNew;
	public final String categoryID;
	private final IIcon displayIcon;
	//private static final int renderTextureID = AMGuiHelper.createRenderTexture();
	//private static final int depthBufferTextureID = AMGuiHelper.instance.createFBO(renderTextureID, 100, 20, true);

	private static final ResourceLocation buttonImage = new ResourceLocation("arsmagica2", "textures/guis/ArcaneCompendiumIndexGui.png");
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	public GuiButtonCompendiumTab(int id, int xPos, int yPos, String categoryDisplay, String categoryID, IIcon displayIcon){
		super(id, xPos, yPos, sourceWidth, sourceHeight, categoryDisplay);
		this.width = displayIcon == null ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(categoryDisplay) : 20;
		this.categoryID = categoryID;
		this.displayIcon = displayIcon;
	}

	public void setActive(boolean active){
		this.isActive = active;
	}

	public void setHasNewSubitems(boolean hasNew){
		this.hasNew = hasNew;
	}

	public void setDimensions(int width, int height){
		this.width = width;
		this.height = height;
	}

	public int getHeight(){
		return this.height;
	}

	public int getWidth(){
		return this.width;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3){
		if (this.visible){
			boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
			par1Minecraft.renderEngine.bindTexture(buttonImage);

			if (isMousedOver || isActive){
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			int u = 0;
			int v = 240;

			GL11.glDisable(GL11.GL_LIGHTING);
			this.drawTexturedModalRect_Classic(this.xPosition, this.yPosition, u, v, this.width * 4, this.height, 138, 16);
			if (hasNew){
				GL11.glColor4f(1, 1, 1, 0.7f);
				AMGuiHelper.DrawIconAtXY(AMParticleIcons.instance.getIconByName("lights"), this.xPosition + (this.width / 3) - 1, this.yPosition + 1, this.zLevel, 16, 16, true);
			}
			if (displayIcon != null){
				par1Minecraft.renderEngine.bindTexture(GuiArcaneCompendium.items);
				AMGuiHelper.DrawIconAtXY(displayIcon, this.xPosition + (this.width / 3), this.yPosition + 2, this.zLevel, 14, 14, true);
			}else{
				Minecraft.getMinecraft().fontRendererObj.drawString(this.displayString, this.xPosition + (this.width / 4), this.yPosition + 2, 0x000000);
			}
			GL11.glEnable(GL11.GL_LIGHTING);

			if (isMousedOver){
				List list = new ArrayList<String>();
				list.add(this.displayString);

				drawHoveringText(list, par2, par3, Minecraft.getMinecraft().fontRendererObj);
			}
		}
	}


	public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height){
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		//AMGuiHelper.bindFBOTexture(depthBufferTextureID, 100, 100);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer var9 = tessellator.getWorldRenderer();

		var9.startDrawingQuads();
		var9.addVertexWithUV(dst_x + 0, dst_y + dst_height, this.zLevel, (src_x + 0) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + dst_height, this.zLevel, (src_x + src_width) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + 0, this.zLevel, (src_x + src_width) * var7, (src_y + 0) * var8);
		var9.addVertexWithUV(dst_x + 0, dst_y + 0, this.zLevel, (src_x + 0) * var7, (src_y + 0) * var8);
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font){
		if (!par1List.isEmpty()){
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = par1List.iterator();

			while (iterator.hasNext()){
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);

				if (l > k){
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (par1List.size() > 1){
				k1 += 2 + (par1List.size() - 1) * 10;
			}

			if (i1 + k > this.width){
				i1 -= 28 + k;
			}

			this.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < par1List.size(); ++k2){
				String s1 = (String)par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0){
					j1 += 2;
				}

				j1 += 10;
			}

			this.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}
