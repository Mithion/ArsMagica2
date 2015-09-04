/**
 *
 */
package am2.guis;

import am2.blocks.tileentities.TileEntityCrystalMarker;
import am2.containers.ContainerCrystalMarker;
import am2.texture.ResourceManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author Zero
 */
public class GuiCrystalMarker extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("crystalMarker.png"));
	private final TileEntityCrystalMarker crystalMarker;

	public GuiCrystalMarker(EntityPlayer player, TileEntityCrystalMarker tileEntityCrystalMarker){
		super(new ContainerCrystalMarker(player, tileEntityCrystalMarker));
		crystalMarker = tileEntityCrystalMarker;
		xSize = 176;
		ySize = 165;
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}
}
