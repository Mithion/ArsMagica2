package am2.guis;

import am2.blocks.tileentities.TileEntityEssenceRefiner;
import am2.containers.ContainerEssenceRefiner;
import am2.power.PowerNodeRegistry;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


public class GuiEssenceRefiner extends GuiContainer{

	private float rotation = 0;

	private final float baseColorBlue = 0.858823f; //(23 R)
	private final float baseColorRed = 0.094117f; //(224 G)
	private final float baseColorGreen = 0.878431f; //(219 B)

	private final float colorShiftRed = 0.36470f; //(93 R)
	private final float colorShiftGreen = -0.84705f; // (-216 G)
	private final float colorShiftBlue = -0.29803f; //(-76 B)

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("essenceExtractorGui.png"));
	private static final ResourceLocation extras = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("essenceExtractorGui_2.png"));

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		float percentComplete = essenceExtractorInventory.getRefinementPercentage();
		boolean active = percentComplete > 0;

		//rune circle
		mc.renderEngine.bindTexture(extras);

		if (active && PowerNodeRegistry.For(Minecraft.getMinecraft().theWorld).getHighestPower(essenceExtractorInventory) >= essenceExtractorInventory.TICK_REFINE_COST)
			this.rotation += 0.05f;

		if (essenceExtractorInventory.isRefining() && PowerNodeRegistry.For(Minecraft.getMinecraft().theWorld).getHighestPower(essenceExtractorInventory) < essenceExtractorInventory.TICK_REFINE_COST && AMGuiHelper.instance.getFastTicker() % 20 < 10){
			GL11.glColor4f(1, 0, 0, 0.5f);
		}else{
			GL11.glColor4f(baseColorRed + (colorShiftRed * percentComplete), baseColorGreen + (colorShiftGreen * percentComplete), baseColorBlue + (colorShiftBlue * percentComplete), 0.5f);
		}

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTranslatef(0.5f, 0.5f, 0.0f);
		GL11.glRotatef(this.rotation, 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
		drawTexturedModalRect_Classic(l + 34, i1 + 28, 0, 0, 108, 108, 108, 108);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public GuiEssenceRefiner(InventoryPlayer inventoryplayer, TileEntityEssenceRefiner tileEntityEssenceExtractor){
		super(new ContainerEssenceRefiner(inventoryplayer, tileEntityEssenceExtractor));
		essenceExtractorInventory = tileEntityEssenceExtractor;
		xSize = 176;
		ySize = 232;
	}

	public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height){

		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(dst_x + 0, dst_y + dst_height, this.zLevel, 0, 1);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + dst_height, this.zLevel, 1, 1);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + 0, this.zLevel, 1, 0);
		var9.addVertexWithUV(dst_x + 0, dst_y + 0, this.zLevel, 0, 0);
		var9.draw();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
	}

	private final TileEntityEssenceRefiner essenceExtractorInventory;

}
