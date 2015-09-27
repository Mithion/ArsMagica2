package am2.guis.controls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSlideControl extends GuiButton{
	/**
	 * The value of this slider control.
	 */
	public float sliderValue = 1.0F;

	/**
	 * Is this slider control being dragged.
	 */
	public boolean dragging = false;

	private String displayStringBase;
	private float sliderMin;
	private float sliderMax;
	private boolean isPercent;
	private boolean isInteger;
	private boolean noDynamicDisplay;
	private boolean isVertical;
	private boolean buttonOnly;
	private float scale = 0.5f;

	private ResourceLocation buttonImage = new ResourceLocation("textures/gui/widgets.png");
	private int buttonUL = 0;
	private int buttonVL = 66;
	private int buttonUR = 196;
	private int buttonVR = 66;
	private int buttonW = 4;
	private int buttonH = 20;

	public GuiSlideControl(int par1, int par2, int par3, int width, String par5Str, float initialValue, float sliderMin, float sliderMax){
		super(par1, par2, par3, width, 10, par5Str);
		this.sliderValue = (initialValue - sliderMin) / (sliderMax - sliderMin);
		this.displayStringBase = par5Str;
		this.sliderMin = sliderMin;
		this.sliderMax = sliderMax;
		this.isPercent = false;
		this.isInteger = false;
		this.zLevel = 5000;

		formatDisplayString();
	}

	public GuiSlideControl(int par1, int par2, int par3, int width, int height, String par5Str, float initialValue, float sliderMin, float sliderMax){
		super(par1, par2, par3, width, height, par5Str);
		this.sliderValue = (initialValue - sliderMin) / (sliderMax - sliderMin);
		this.displayStringBase = par5Str;
		this.sliderMin = sliderMin;
		this.sliderMax = sliderMax;
		this.isPercent = false;
		this.isInteger = false;

		formatDisplayString();
	}

	public void setMaximum(float maximum){
		this.sliderMax = maximum;
		if (this.sliderValue > sliderMax)
			this.sliderValue = sliderMax;
	}

	public void setVertical(){
		this.isVertical = true;
		int tmp = this.width;
		this.width = this.height;
		this.height = tmp;
	}

	public void setButtonOnly(){
		this.buttonOnly = true;
	}

	public void setButtonProperties(int UL, int VL, int UR, int VR, int W, int H){
		buttonUL = UL;
		buttonVL = VL;

		buttonUR = UR;
		buttonVR = VR;

		buttonW = W;
		buttonH = H;
	}

	public void setOverrideTexture(ResourceLocation rLoc){
		buttonImage = rLoc;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3){
		if (this.visible){
			FontRenderer fontrenderer = par1Minecraft.fontRendererObj;
			par1Minecraft.renderEngine.bindTexture(buttonImage);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.dragging = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			int k = this.getHoverState(this.dragging);
			if (!this.buttonOnly){
				if (this.isVertical){
					this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46, this.width / 2, this.height);
					this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46, this.width / 2, this.height);
				}else{
					this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
					this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46, this.width / 2, this.height);
				}
			}
			this.mouseDragged(par1Minecraft, par2, par3);
			int l = 14737632;

			if (!this.enabled){
				l = -6250336;
			}else if (this.dragging){
				l = 16777120;
			}

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 6) / 2, l);
		}
	}

	public GuiSlideControl setPercent(boolean percent){
		this.isPercent = percent;
		formatDisplayString();
		return this;
	}

	public GuiSlideControl setNoDynamicDisplay(boolean dynamic){
		this.noDynamicDisplay = dynamic;
		formatDisplayString();
		return this;
	}

	public GuiSlideControl setInteger(boolean integer){
		this.isInteger = integer;
		formatDisplayString();
		return this;
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
	 * this button.
	 */
	@Override
	public int getHoverState(boolean par1){
		return 0;
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
	 */
	@Override
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3){
		if (this.visible){
			if (this.dragging){
				if (this.isVertical){
					this.sliderValue = (float)(par3 - (this.yPosition)) / (float)(this.height);
				}else{
					this.sliderValue = (float)(par2 - (this.xPosition + buttonW)) / (float)(this.width - 2 * buttonW);
				}

				if (this.sliderValue < 0.0F){
					this.sliderValue = 0.0F;
				}

				if (this.sliderValue > 1.0F){
					this.sliderValue = 1.0F;
				}
				formatDisplayString();
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GL11.glScalef(1.0f, scale, 0.0f);
			int yPos = (int)(this.yPosition / scale);
			int xPos = (int)(this.xPosition / scale);
			if (this.isVertical){
				this.drawTexturedModalRect(xPos, this.yPosition + (int)(this.sliderValue / scale * (this.height - this.buttonH)), buttonUL, buttonVL, buttonW, buttonH);
				this.drawTexturedModalRect(xPos + buttonW, this.yPosition + (int)(this.sliderValue / scale * (this.height - this.buttonH)), buttonUR, buttonVR, buttonW, buttonH);
			}else{
				this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - buttonW * 2)), yPos, buttonUL, buttonVL, buttonW, buttonH);
				this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - buttonW * 2)) + buttonW, yPos, buttonUR, buttonVR, buttonW, buttonH);
			}
			GL11.glPopMatrix();
		}
	}

	public float getShiftedValue(){
		return ((this.sliderMax - this.sliderMin) * this.sliderValue) + this.sliderMin;
	}

	private void formatDisplayString(){

		if (noDynamicDisplay){
			this.displayString = displayStringBase;
			return;
		}

		float value = ((this.sliderMax - this.sliderMin) * this.sliderValue) + this.sliderMin;

		if (!isInteger){
			this.displayString = displayStringBase + ": " + String.format("%.2f", value) + (isPercent ? "%" : "");
		}else{
			this.displayString = displayStringBase + ": " + String.format("%d", (int)value) + (isPercent ? "%" : "");
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
	 * e).
	 */
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3){
		if (super.mousePressed(par1Minecraft, par2, par3)){
			if (this.isVertical){
				this.sliderValue = (float)(par3 - (this.yPosition + 4)) / (float)(this.height - 8);
			}else{
				this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
			}

			if (this.sliderValue < 0.0F){
				this.sliderValue = 0.0F;
			}

			if (this.sliderValue > 1.0F){
				this.sliderValue = 1.0F;
			}
			formatDisplayString();
			this.dragging = true;
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	@Override
	public void mouseReleased(int par1, int par2){
		this.dragging = false;
	}

	public void setScale(float f){
		this.scale = f;
	}
}
