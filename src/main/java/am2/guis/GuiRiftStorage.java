package am2.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.containers.ContainerRiftStorage;
import am2.playerextensions.RiftStorage;
import am2.texture.ResourceManager;

public class GuiRiftStorage extends GuiContainer {
	
	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("riftGUI.png"));
	private static final ResourceLocation extras = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("spellBookGui_2.png"));
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {		
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiRiftStorage(InventoryPlayer inventoryplayer, RiftStorage storage)
	{
		super(new ContainerRiftStorage(inventoryplayer, storage));
		this.storage = storage;
		xSize = 176;
		ySize = 212;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{

		mc.renderEngine.bindTexture(extras);
		
		int rows = 1;

		switch(storage.getAccessLevel()){
		case 1:
			rows = 1;
			break;
		case 2:
			rows = 3;
			break;
		case 3:
			rows = 6;
			break;
		}

		for (int j = rows; j < 6; ++j){
			for (int i = 0; i < 9; ++i){
				int index = i + j * 9;
				int x = 8 + (18 * i);
				int y = 13 + (j * 18);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
				drawTexturedModalRect(x, y, 0, 20, 16, 16);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}

	private final RiftStorage storage;
}
