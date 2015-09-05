package am2.guis;

import am2.blocks.tileentities.TileEntityKeystoneChest;
import am2.containers.ContainerKeystoneChest;
import am2.texture.ResourceManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiKeystoneChest extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("keystoneChestGUI.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiKeystoneChest(InventoryPlayer inventoryplayer, TileEntityKeystoneChest chest){
		super(new ContainerKeystoneChest(inventoryplayer, chest));
		chestInventory = chest;
		xSize = 176;
		ySize = 180;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
	}

	private final TileEntityKeystoneChest chestInventory;
}
