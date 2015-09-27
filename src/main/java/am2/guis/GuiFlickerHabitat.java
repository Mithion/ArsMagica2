/**
 *
 */
package am2.guis;

import am2.api.flickers.IFlickerFunctionality;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.blocks.tileentities.flickers.FlickerOperatorRegistry;
import am2.containers.ContainerFlickerHabitat;
import am2.power.PowerNodeRegistry;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * @author Zero, Mithion
 */
public class GuiFlickerHabitat extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("FlickerHabitat.png"));
	private final TileEntityFlickerHabitat flickerHabitat;

	/**
	 * @param par1Container
	 */
	public GuiFlickerHabitat(EntityPlayer player, TileEntityFlickerHabitat tileEntityFlickerHabitat){
		super(new ContainerFlickerHabitat(player, tileEntityFlickerHabitat));
		flickerHabitat = tileEntityFlickerHabitat;
		xSize = 176;
		ySize = 166;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		ItemStack stack = flickerHabitat.getStackInSlot(0);

		if (stack == null) return;

		IFlickerFunctionality func = FlickerOperatorRegistry.instance.getOperatorForMask(stack.getMetadata());

		if (func == null)
			return;

		String colorCode = Minecraft.getMinecraft().theWorld.isBlockIndirectlyGettingPowered(flickerHabitat.xCoord, flickerHabitat.yCoord, flickerHabitat.zCoord) ? "\2474" : "\2472";

		int yPos = 5;
		String curLine = "";

		if (func.RequiresPower()){
			curLine = StatCollector.translateToLocal("am2.gui.flicker_needspower");
		}else{
			curLine = StatCollector.translateToLocal("am2.gui.flicker_doesntneedpower");
		}

		drawCenteredString(curLine, yPos);
		yPos += 12 * (int)Math.ceil(this.fontRendererObj.getStringWidth(curLine) / 170.0f);

		drawCenteredString(
				String.format(
						StatCollector.translateToLocal("am2.gui.flicker_powerperop"),
						String.format("%s%d\2470", colorCode, func.PowerPerOperation())),
				yPos);

		yPos += 12;

		boolean powered = PowerNodeRegistry.For(flickerHabitat.getWorld()).checkPower(flickerHabitat, func.PowerPerOperation());

		if (yPos > 40)
			yPos += 27;

		drawCenteredString(
				String.format(
						StatCollector.translateToLocal("am2.gui.flicker_optime"),
						String.format("%s%.2f\2470", colorCode,
								func.TimeBetweenOperation(powered, flickerHabitat.getNearbyUpgrades()) / 20.0f
						)), yPos);
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
