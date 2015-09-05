package am2.guis;

import am2.blocks.tileentities.TileEntitySummoner;
import am2.containers.ContainerSummoner;
import am2.power.PowerNodeRegistry;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiSummoner extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("SummonerGui.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiSummoner(InventoryPlayer inventoryplayer, TileEntitySummoner summoner){
		super(new ContainerSummoner(inventoryplayer, summoner));
		summonerInventory = summoner;
		xSize = 176;
		ySize = 245;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		String essenceString = StatCollector.translateToLocal("am2.gui.summonCost") + ":";
		String maintainString = StatCollector.translateToLocal("am2.gui.maintainCost") + ":";
		float cost = summonerInventory.getSummonCost();
		float maintainCost = summonerInventory.getMaintainCost() * 20;
		String essenceCostString = cost >= 0 ? String.format("%.2f/s", maintainCost) : "N/A";
		int color = cost >= 0 ? cost <= PowerNodeRegistry.For(Minecraft.getMinecraft().theWorld).getHighestPower(summonerInventory) ? 0x007700 : 0x770000 : 0x333333;

		int offset = fontRendererObj.getStringWidth(essenceString) + 25;


		fontRendererObj.drawString(maintainString, xSize - offset, ySize - 130, 0x777777);
		fontRendererObj.drawString(essenceCostString, xSize - offset, ySize - 120, color);

		essenceCostString = cost >= 0 ? String.format("%.2f", cost) : "N/A";
		color = cost >= 0 ? cost <= PowerNodeRegistry.For(Minecraft.getMinecraft().theWorld).getHighestPower(summonerInventory) ? 0x007700 : 0x770000 : 0x333333;

		fontRendererObj.drawString(essenceString, 20, ySize - 130, 0x777777);
		fontRendererObj.drawString(essenceCostString, 20, ySize - 120, color);

		String readyString = summonerInventory.canSummon() ? StatCollector.translateToLocal("am2.gui.summonReady") : StatCollector.translateToLocal("am2.gui.summonNotReady");
		color = summonerInventory.canSummon() ? 0x007700 : 0x770000;

		fontRendererObj.drawString(readyString, xSize / 2 - (fontRendererObj.getStringWidth(readyString) / 2), ySize - 107, color);
	}

	private void drawCostString(String tip, int x, int y){
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int var4 = this.fontRendererObj.getStringWidth(tip);
		int var5 = x + 12;
		int var6 = y - 12;
		byte var8 = 8;
		this.zLevel = 300.0F;
		itemRender.zLevel = 300.0F;
		int var9 = -267386864;
		this.drawGradientRect(var5 - 3, var6 - 4, var5 + var4 + 3, var6 - 3, var9, var9);
		this.drawGradientRect(var5 - 3, var6 + var8 + 3, var5 + var4 + 3, var6 + var8 + 4, var9, var9);
		this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 + var8 + 3, var9, var9);
		this.drawGradientRect(var5 - 4, var6 - 3, var5 - 3, var6 + var8 + 3, var9, var9);
		this.drawGradientRect(var5 + var4 + 3, var6 - 3, var5 + var4 + 4, var6 + var8 + 3, var9, var9);
		int var10 = 1347420415;
		int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
		this.drawGradientRect(var5 - 3, var6 - 3 + 1, var5 - 3 + 1, var6 + var8 + 3 - 1, var10, var11);
		this.drawGradientRect(var5 + var4 + 2, var6 - 3 + 1, var5 + var4 + 3, var6 + var8 + 3 - 1, var10, var11);
		this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 - 3 + 1, var10, var10);
		this.drawGradientRect(var5 - 3, var6 + var8 + 2, var5 + var4 + 3, var6 + var8 + 3, var11, var11);
		this.fontRendererObj.drawStringWithShadow(tip, var5, var6, -1);
		this.zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	private final TileEntitySummoner summonerInventory;

}
