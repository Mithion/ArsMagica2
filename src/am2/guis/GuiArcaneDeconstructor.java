package am2.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentities.TileEntityArcaneDeconstructor;
import am2.containers.ContainerArcaneDeconstructor;
import am2.texture.ResourceManager;

public class GuiArcaneDeconstructor extends GuiContainer{

	private TileEntityArcaneDeconstructor deconstructor;

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("deconstruction_table_GUI.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		mc.renderEngine.bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

        int overlayHeight = this.deconstructor.getProgressScaled(18);
		if (overlayHeight > 0)
			this.drawTexturedModalRect(l + 79, i1 + 65, 176, 0, 17, overlayHeight);
	}

	public GuiArcaneDeconstructor(InventoryPlayer inventoryplayer, TileEntityArcaneDeconstructor deconstructorEntity)
    {
        super(new ContainerArcaneDeconstructor(inventoryplayer, deconstructorEntity));
        this.deconstructor = deconstructorEntity;
        xSize = 176;
        ySize = 198;
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }
}
