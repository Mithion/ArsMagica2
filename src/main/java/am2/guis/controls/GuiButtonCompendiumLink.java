package am2.guis.controls;

import am2.guis.AMGuiHelper;
import am2.guis.AMGuiIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiButtonCompendiumLink extends GuiButton{
	private final FontRenderer fontRendererObj;
	private final String entryID;
	private final String category;
	private final int pageNum;
	private final boolean hasSubItems;
	private boolean isNewItem = false;
	private boolean displayOnAllPages = false;

	public GuiButtonCompendiumLink(int id, int xPos, int yPos, FontRenderer fontRendererObj, String text, String entryID, String category, boolean hasSubItems, int pageNum){
		super(id, xPos, yPos, fontRendererObj.getStringWidth(text), 10, text);
		this.fontRendererObj = fontRendererObj;
		this.entryID = entryID;
		this.category = category;
		this.hasSubItems = hasSubItems;
		this.pageNum = pageNum;
	}

	public int getPageNum(){
		return pageNum;
	}

	public void setNewItem(){
		isNewItem = true;
	}

	public boolean getDisplayOnAllPages(){
		return this.displayOnAllPages;
	}

	public void setDimensions(int width, int height){
		this.width = width;
		this.height = height;
	}

	public String getEntryID(){
		return this.entryID;
	}

	public String getCategory(){
		return this.category;
	}

	public boolean hasSubItems(){
		return this.hasSubItems;
	}

	public void setShowOnAllPages(){
		this.displayOnAllPages = true;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3){
		if (this.visible){
			boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

			int textColor = 0x000000;
			if (isMousedOver){
				textColor = 0x6600FF;
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			fontRendererObj.drawString(this.displayString, xPosition, yPosition, textColor);
			if (isNewItem){
				GL11.glColor4f(1, 1, 1, 1);
				AMGuiHelper.instance.DrawIconAtXY(AMGuiIcons.newEntry, xPosition - 6, yPosition + 2, this.zLevel, 5, 5, true);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
}
