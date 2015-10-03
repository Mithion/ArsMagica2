package am2.guis.controls;

import am2.api.math.AMVector2;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class GuiStatedImageButton extends GuiButtonVariableDims{

	public enum States{
		IDLE,
		MOUSEOVER,
		HOVER,
		CLICK,
		ACTIVE
	}

	private HashMap<States, AMVector2> stateImages;
	private ResourceLocation buttonTextureLocation;

	private boolean isActive = false;
	private boolean currentlyClicked = false;

	public GuiStatedImageButton(int id, int x, int y, ResourceLocation textureLoc, int idleU, int idleV){
		super(id, x, y, "");
		stateImages = new HashMap<GuiStatedImageButton.States, AMVector2>();
		addStateCoords(States.IDLE, idleU, idleV);
	}

	public void addStateCoords(States state, int u, int v){
		stateImages.put(state, new AMVector2(u, v));
	}

	public void toggle(){
		this.isActive = !this.isActive;
	}

	public boolean getActive(){
		return this.isActive;
	}

	public void setActive(boolean active){
		this.isActive = active;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3){
		boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
		if (!renderBorderOnly){
			if (currentlyClicked){
				drawButtonFromState(States.CLICK);
			}else if (isMousedOver){
				drawButtonFromState(States.MOUSEOVER);
			}else if (getActive()){
				drawButtonFromState(States.ACTIVE);
			}else{
				drawButtonFromState(States.IDLE);
			}
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

	private void drawButtonFromState(States state){
		AMVector2 vec = this.stateImages.get(state);
		if (vec == null)
			vec = this.stateImages.get(States.IDLE);

		drawTexturedModalRect(this.xPosition, this.yPosition, vec.iX, vec.iY, width, height);
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3){
		boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
		if (isMousedOver)
			currentlyClicked = true;
		return super.mousePressed(par1Minecraft, par2, par3);
	}

	@Override
	public void mouseReleased(int par1, int par2){
		currentlyClicked = false;
		super.mouseReleased(par1, par2);
	}

}
