package am2.guis.controls;

import am2.items.ItemsCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSpellImageButton extends GuiButtonVariableDims{
	/**
	 * True for pointing right (next page), false for pointing left (previous page).
	 */
	private final IIcon icon;
	private final int index;
	private boolean isSelected = false;
	private final int page;
	private final static int sourceWidth = 12;
	private final static int sourceHeight = 12;

	private static final ResourceLocation buttonImage = new ResourceLocation("textures/atlas/items.png");

	public GuiSpellImageButton(int id, int xPos, int yPos, IIcon imageIcon, int index, int page){
		super(id, sourceWidth, sourceHeight, "");
		setPosition(xPos, yPos);
		this.icon = imageIcon;
		this.index = index;
		this.page = page;
		this.setDimensions(12, 12);
	}

	public int getIndex(){
		return this.index;
	}

	public void setSelected(boolean selected){
		this.isSelected = selected;
	}

	public int getPage(){
		return this.page;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3){
		if (this.visible){
			boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			par1Minecraft.renderEngine.bindTexture(buttonImage);

			if (isMousedOver){
				GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
				if (this.hoverTextLines.size() > 0){
					drawHoveringText(hoverTextLines, par2, par3, Minecraft.getMinecraft().fontRendererObj);
				}
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			this.drawTexturedModelRectFromIcon(this.xPosition, this.yPosition, this.icon, this.width, this.height);
			if (this.isSelected){
				this.drawTexturedModelRectFromIcon(this.xPosition, this.yPosition, ItemsCommonProxy.spell.getIconFromDamage(-1), this.width, this.height);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
}
