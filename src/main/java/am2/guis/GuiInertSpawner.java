/**
 *
 */
package am2.guis;

import am2.blocks.tileentities.TileEntityInertSpawner;
import am2.containers.ContainerInertSpawner;
import am2.texture.ResourceManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiInertSpawner extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("FlickerHabitat.png"));
	private final TileEntityInertSpawner flickerHabitat;

	/**
	 * @param par1Container
	 */
	public GuiInertSpawner(EntityPlayer player, TileEntityInertSpawner tileEntityFlickerHabitat){
		super(new ContainerInertSpawner(player, tileEntityFlickerHabitat));
		flickerHabitat = tileEntityFlickerHabitat;
		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	private void drawCenteredString(String s, int yCoord){
		int w = this.fontRendererObj.getStringWidth(s);
		int xPos = this.xSize / 2 - w / 2;
		if (w > 170){
			xPos = 3;
		}
		this.fontRendererObj.drawSplitString(s, xPos, yCoord, 170, 0);
	}

}
