package am2.guis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import am2.blocks.tileentities.TileEntityMagiciansWorkbench;
import am2.blocks.tileentities.TileEntityMagiciansWorkbench.RememberedRecipe;
import am2.containers.ContainerMagiciansWorkbench;
import am2.guis.controls.GuiButtonVariableDims;
import am2.texture.ResourceManager;

public class GuiMagiciansWorkbench extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("magicians_workbench.png"));

	private final TileEntityMagiciansWorkbench workbenchInventory;
	private GuiButtonVariableDims clearGridOne;
	private GuiButtonVariableDims clearGridTwo;

	private final ArrayList<String> ttList;
	int color = -1;
	private int recipeIndex = -1;
	private int hoverRecipeIndex = -1;

	public GuiMagiciansWorkbench(InventoryPlayer playerInventory, TileEntityMagiciansWorkbench tileEntity) {
		super(new ContainerMagiciansWorkbench(playerInventory, tileEntity));
		this.workbenchInventory = tileEntity;
		xSize = 220;
		ySize = 251;
		ttList = new ArrayList<String>();
	}

	@Override
	public void initGui() {
		super.initGui();

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		clearGridOne = new GuiButtonVariableDims(0, l+40, i1+15, "X");
		clearGridTwo = new GuiButtonVariableDims(0, l+114, i1+15, "X");

		int s = 10;
		clearGridOne.setDimensions(s, s);
		clearGridTwo.setDimensions(s, s);

		this.buttonList.add(clearGridOne);
		this.buttonList.add(clearGridTwo);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		if (par1GuiButton == clearGridOne){
			for (int i = 0; i < 9; ++i){
				Slot slot = this.inventorySlots.getSlot(i);
				this.mc.playerController.windowClick(this.inventorySlots.windowId, slot.slotNumber, 0, 1, this.mc.thePlayer);
			}
		}else if (par1GuiButton == clearGridTwo){
			int stop = ((ContainerMagiciansWorkbench)this.inventorySlots).getWorkbench().getUpgradeStatus(TileEntityMagiciansWorkbench.UPG_CRAFT) ? 9 : 4;
			for (int i = 10; i < 10 + stop; ++i){
				Slot slot = this.inventorySlots.getSlot(i);
				this.mc.playerController.windowClick(this.inventorySlots.windowId, slot.slotNumber, 0, 1, this.mc.thePlayer);
			}
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if (hoverRecipeIndex > -1 && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			((ContainerMagiciansWorkbench)this.inventorySlots).getWorkbench().toggleRecipeLocked(hoverRecipeIndex);
		}else if (recipeIndex > -1){
			((ContainerMagiciansWorkbench)this.inventorySlots).moveRecipeToCraftingGrid(recipeIndex);
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		drawTexturedModalRect(l, i1, 0, 0, 166, 136); //background
		drawTexturedModalRect(l+166, i1, 202, 0, 54, 162); //internal inventory
		drawTexturedModalRect(l + 12, i1 + 162, 0, 137, 176, 89); //player inventory
		drawTexturedModalRect(l + 191, i1 + 172, 178, 0, 24, 72); //keystone lockable slots

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//crafting grid and output 1
		drawTexturedModalRect(l + 18, i1 + 28, 202, 162, 54, 54); //grid
		drawTexturedModalRect(l + 35, i1 + 87, 19, 225, 20, 20); //output
		//crafting grid and output 2
		if (this.workbenchInventory.getUpgradeStatus(workbenchInventory.UPG_CRAFT))
			drawTexturedModalRect(l + 92, i1 + 28, 202, 162, 54, 54); //grid 3x3
		else
			drawTexturedModalRect(l + 101, i1 + 37, 202, 162, 36, 36); //grid 2x2
		drawTexturedModalRect(l + 109, i1 + 87, 19, 225, 20, 20); //output

		GL11.glDisable(GL11.GL_BLEND);

		//tabs
		Iterator it = this.workbenchInventory.getRememberedRecipeItems().iterator();
		int n = 0;
		recipeIndex = -1;
		hoverRecipeIndex = -1;
		for (n = 0; n < this.workbenchInventory.getRememberedRecipeItems().size(); ++n){
			int x = l + n*20;
			int y = i1 + 136;
			drawTexturedModalRect(x, y, 0, 229, 19, 24);

			if (this.workbenchInventory.getRememberedRecipeItems().get(n).isLocked()){
				drawTexturedModalRect(x + 5, y + 20, 39, 225, 8, 10);
			}

			if (mouseX >= x && mouseX < x + 19){
				if (mouseY >= y && mouseY < y + 24){
					recipeIndex = n;
					hoverRecipeIndex = n;
				}
			}
		}
		n = 0;
		ttList.clear();
		while(it.hasNext()){
			RememberedRecipe stack = (RememberedRecipe) it.next();

			renderItemStack(stack.output, l + 1 + n*20, i1 + 140);
			if (n == recipeIndex){
				if (((ContainerMagiciansWorkbench)this.inventorySlots).isRecipeAlreadyInGrid(recipeIndex)){
					ttList.add(stack.output.getDisplayName());
					color = 0x40FF40;
				}else if (!((ContainerMagiciansWorkbench)this.inventorySlots).gridIsFreeFor(recipeIndex)){
					ttList.add("\247o" + StatCollector.translateToLocal("am2.gui.gridInUse1"));
					ttList.add("\247o" + StatCollector.translateToLocal("am2.gui.gridInUse2"));
					color = 0x666666;
					recipeIndex = -1;
				}else if (((ContainerMagiciansWorkbench)this.inventorySlots).hasComponents(recipeIndex)){
					ttList.add(stack.output.getDisplayName());
					color = -1;
				}else{
					ttList.add(stack.output.getDisplayName() + " (" + StatCollector.translateToLocal("am2.gui.componentsMissing") + ")");
					color = 0xFF0000;
					recipeIndex = -1;
				}
				if (stack.isLocked())
					ttList.add(StatCollector.translateToLocal("am2.tooltip.mwUnlock"));
				else
					ttList.add(StatCollector.translateToLocal("am2.tooltip.mwLock"));

				if (stack.is2x2)
					ttList.add("(2x2)");
				else
					ttList.add("(3x3)");
			}
			n++;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		if (ttList.size() > 0){
			drawHoveringText(ttList, mouseX - l, mouseY - i1, fontRendererObj, color);
		}
	}

	private void renderItemStack(ItemStack stack, int x, int y){
		if (stack != null){
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x, y);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}

	protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font, int color)
	{
		if (!par1List.isEmpty())
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
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

			if (j1 + k1 + 6 > this.height)
			{
				j1 = this.height - k1 - 6;
			}

			this.zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
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
				font.drawStringWithShadow(s1, i1, j1, color);

				if (k2 == 0)
				{
					j1 += 2;
				}

				j1 += 10;
			}

			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}
