package am2.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntitySeerStone;
import am2.containers.ContainerSeerStone;
import am2.texture.ResourceManager;

public class GuiSeerStone extends GuiContainer {

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("seerStoneGui.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		mc.renderEngine.bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiSeerStone(InventoryPlayer inventoryplayer, TileEntitySeerStone seerStoneEntity)
    {
        super(new ContainerSeerStone(inventoryplayer, seerStoneEntity));
        seerStoneInventory = seerStoneEntity;
        xSize = 176;
        ySize = 180;
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }

    private TileEntitySeerStone seerStoneInventory;
}
