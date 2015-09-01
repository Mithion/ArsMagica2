package am2.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityArcaneReconstructor;
import am2.containers.ContainerArcaneReconstructor;
import am2.texture.ResourceManager;

public class GuiArcaneReconstructor extends GuiContainer{

	private TileEntityArcaneReconstructor reconstructorInventory;

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("arcaneReconstructorGUI.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		mc.renderEngine.bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiArcaneReconstructor(InventoryPlayer inventoryplayer, TileEntityArcaneReconstructor reconstructorEntity)
    {
        super(new ContainerArcaneReconstructor(inventoryplayer, reconstructorEntity));
        reconstructorInventory = reconstructorEntity;
        xSize = 176;
        ySize = 200;
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }
}
