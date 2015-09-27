package am2.guis.controls;

import am2.api.math.AMVector2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiButtonVariableDims extends GuiButton{

	boolean renderBorderOnly = false;
	protected ArrayList<String> hoverTextLines = new ArrayList<String>();

	public GuiButtonVariableDims(int par1, int par2, int par3, String par4Str){
		super(par1, par2, par3, par4Str);
	}

	public GuiButtonVariableDims setDimensions(int width, int height){
		this.width = width;
		this.height = height;
		return this;
	}

	public void setPosition(int x, int y){
		this.xPosition = x;
		this.yPosition = y;
	}

	public GuiButtonVariableDims setBorderOnly(boolean borderOnly){
		this.renderBorderOnly = borderOnly;
		return this;
	}

	public AMVector2 getPosition(){
		return new AMVector2(this.xPosition, this.yPosition);
	}

	public GuiButtonVariableDims setPopupText(String text){
		this.hoverTextLines.clear();
		String[] split = text.split("\\.");
		for (String s : split){
			hoverTextLines.add(s.trim() + ".");
		}
		return this;
	}

	public AMVector2 getDimensions(){
		return new AMVector2(width, height);
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3){
		boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
		if (!renderBorderOnly){
			super.drawButton(par1Minecraft, par2, par3);
		}else{
			int color = this.enabled ? 0xFFFFFF : 0x660000;
			line(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition, color);
			line(this.xPosition + this.width, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
			line(this.xPosition + this.width, this.yPosition + this.height, this.xPosition, this.yPosition + this.height, color);
			line(this.xPosition, this.yPosition + this.height, this.xPosition, this.yPosition, color);
		}

		if (isMousedOver && this.hoverTextLines.size() > 0){
			drawHoveringText(hoverTextLines, par2, par3, Minecraft.getMinecraft().fontRendererObj);
		}
	}

	protected void line(int startX, int startY, int endX, int endY, int color){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(4f);
		GL11.glColor3f((color & 0xFF0000) >> 16, (color & 0x00FF00) >> 8, color & 0x0000FF);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(startX, startY, this.zLevel);
		GL11.glVertex3f(endX, endY, this.zLevel);
		GL11.glEnd();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font){
		if (!par1List.isEmpty()){
			GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
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
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glPopAttrib();
		}
	}
}
