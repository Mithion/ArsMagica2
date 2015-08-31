package am2.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.containers.ContainerSpellBook;
import am2.containers.InventorySpellBook;
import am2.items.ItemSpellBook;
import am2.texture.ResourceManager;


public class GuiSpellBook extends GuiContainer {

	private int bookActiveSlot;

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("spellBookGui.png"));
	private static final ResourceLocation extras = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("spellBookGui_2.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiSpellBook(InventoryPlayer inventoryplayer, ItemStack spellBookStack, InventorySpellBook inventoryspellbook)
	{
		super(new ContainerSpellBook(inventoryplayer, spellBookStack, inventoryspellbook));
		spellBookInventory = inventoryspellbook;
		bookActiveSlot = ((ItemSpellBook)spellBookStack.getItem()).GetActiveSlot(spellBookStack);
		xSize = 256;
		ySize = 250;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		for (int i = 0; i < 8; ++i){
			ItemStack stack = spellBookInventory.getStackInSlot(i);
			if (stack == null){
				continue;
			}
			String[] nameParts = stack.getDisplayName().split(" ");
			int X = 37;
			int Y = 5 + i * 18;
			int maxWidth = 120;
			int line = 1;
			for (String s : nameParts){
				int width = fontRendererObj.getStringWidth(s);
				if (X + width > maxWidth && line == 1){
					Y += fontRendererObj.FONT_HEIGHT;
					line++;
					X = 37;
				}
				fontRendererObj.drawString(s.replace("\247b", ""), X, Y, 0x404040);
				X += fontRendererObj.getStringWidth(s + " ");
			}
		}

		int x = 16;
		int y = 3 + bookActiveSlot * 18;
		mc.renderEngine.bindTexture(extras);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(x, y, 0, 0, 20, 20);

		//special slot
		int index = ((ContainerSpellBook)this.inventorySlots).specialSlotIndex - 67;
		x = 48 + 18 * index;
		y = 229;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		drawTexturedModalRect(x, y, 0, 20, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (!Character.isDigit(par1))
			super.keyTyped(par1, par2);
	}

	private InventorySpellBook spellBookInventory;

}
