package am2.guis.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import am2.api.spell.enums.SkillTrees;

public class GuiButtonSkillTreeTab extends GuiButton{

	private final SkillTrees skillTree;
	private boolean isActive;

	private static final int IIconDims = 19;
	public static final int buttonWidth = 22;
	public static final int buttonHeight = 22;

	private static final ResourceLocation buttonImage = new ResourceLocation("arsmagica2", "textures/guis/SkillTreeUI.png");

	private final ArrayList<String> lines = new ArrayList<String>();

	private static final int u = 0;
	private static final int v = 210;

	public GuiButtonSkillTreeTab(int id, int x, int y, SkillTrees skillTree) {
		super(id, x, y, "");
		this.skillTree = skillTree;
		this.isActive = false;
		this.height = buttonHeight;
		this.width = buttonWidth;
		lines.add(StatCollector.translateToLocal("am2.gui." + skillTree.toString()));
	}

	public void setActive(boolean isActive){
		this.isActive = isActive;
	}

	public SkillTrees getTree(){
		return this.skillTree;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.visible)
		{
			boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			par1Minecraft.renderEngine.bindTexture(buttonImage);

			if (isActive){
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}else{
				GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			this.drawTexturedModalRect_Classic(this.xPosition, this.yPosition, u, v, buttonWidth, buttonHeight, buttonWidth, buttonHeight);

			int iconU = u + buttonWidth * 2;
			int iconV = v;

			switch (skillTree){
			case Defense:
				iconU += IIconDims;
				break;
			case Utility:
				iconU += IIconDims * 2;
				break;
			case Talents:
				iconU += IIconDims * 3;
				break;
			case Affinity:
				iconU += IIconDims * 4;
				break;
			case Offense:
			default:
				break;
			}

			this.drawTexturedModalRect_Classic(this.xPosition + 1.5, this.yPosition + 2, iconU, iconV, IIconDims, IIconDims, IIconDims, IIconDims);
			if (isActive)
				this.drawTexturedModalRect_Classic(this.xPosition, this.yPosition + this.buttonHeight, this.buttonWidth, v, buttonWidth, 7, buttonWidth, 7);

			if (isMousedOver){
				drawHoveringText(lines, par2, par3, Minecraft.getMinecraft().fontRenderer);
			}

			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}



	public void drawTexturedModalRect_Classic(double dst_x, double dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height)
	{
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(dst_x + 0, dst_y + dst_height, this.zLevel, (src_x + 0) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + dst_height, this.zLevel, (src_x + src_width) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + 0, this.zLevel, (src_x + src_width) * var7, (src_y + 0) * var8);
		var9.addVertexWithUV(dst_x + 0, dst_y + 0, this.zLevel, (src_x + 0) * var7, (src_y + 0) * var8);
		var9.draw();
	}

	protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font)
	{
		if (!par1List.isEmpty())
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = par1List.iterator();

			while (iterator.hasNext())
			{
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);

				if (l > k)
				{
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (par1List.size() > 1)
			{
				k1 += 2 + (par1List.size() - 1) * 10;
			}

			if (i1 + k > this.width)
			{
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

			for (int k2 = 0; k2 < par1List.size(); ++k2)
			{
				String s1 = (String)par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0)
				{
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
