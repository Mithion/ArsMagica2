package am2.guis;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import am2.AMCore;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementTiers;
import am2.armor.ArmorHelper;
import am2.armor.infusions.ImbuementRegistry;
import am2.blocks.tileentities.TileEntityArmorImbuer;
import am2.containers.ContainerArmorInfuser;
import am2.network.AMNetHandler;
import am2.texture.ResourceManager;

public class GuiArmorImbuer extends GuiContainer {

	private TileEntityArmorImbuer tileEntity;

	private static final ResourceLocation foreground = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("ArmorUpgradeGUI.png"));
	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("ArmorUpgradeGUIIcons.png"));
	private static final ResourceLocation background_bw = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("ArmorUpgradeGUIIconsBW.png"));

	int spriteHeight = 24;
	int spriteWidth = 43;
	String hoveredID;

	public GuiArmorImbuer(EntityPlayer player, TileEntityArmorImbuer infuser) {
		super(new ContainerArmorInfuser(player, infuser));
		this.tileEntity = infuser;
		xSize = 247;
		ySize = 250;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (hoveredID != null){
			AMNetHandler.INSTANCE.sendImbueToServer(tileEntity, hoveredID);
			tileEntity.imbueCurrentArmor(hoveredID);
		}
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		ItemStack stack = tileEntity.getStackInSlot(0);

		int startX = l + 22;
		int stepX = 52;
		int startY = i1 + 23;
		int stepY = 45;

		int drawX = startX;
		int drawY = startY;

		ArrayList<String> hoverLines = new ArrayList<String>();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glColor3f(0, 0, 0);
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		GL11.glColor3f(1,1,1);
		hoveredID = null;
		if (stack != null){
			mc.renderEngine.bindTexture(background_bw);
			for (ImbuementTiers tier : ImbuementTiers.values()){
				IArmorImbuement[] infusions = ImbuementRegistry.instance.getImbuementsForTier(tier, ((ItemArmor)stack.getItem()).armorType);
				for (IArmorImbuement infusion : infusions){
					drawInfusionIconAt(drawX, drawY, infusion.getIconIndex());

					if (i >= drawX && i <= drawX + spriteWidth){
						if (j >= drawY && j <= drawY + spriteHeight){
							hoverLines.add(StatCollector.translateToLocal("am2.tooltip." + infusion.getID()));
						}
					}

					drawX += stepX;
				}
				drawY += stepY;
				drawX = startX;
			}

			drawX = startX;
			drawY = startY;

			int highestSelectedTier = 0;
			mc.renderEngine.bindTexture(background);
			for (ImbuementTiers tier : ImbuementTiers.values()){
				IArmorImbuement[] infusions = ImbuementRegistry.instance.getImbuementsForTier(tier, ((ItemArmor)stack.getItem()).armorType);
				IArmorImbuement[] existingInfusions = ArmorHelper.getInfusionsOnArmor(stack);
				IArmorImbuement tierInfusion = null;

				for (IArmorImbuement infusion : existingInfusions){
					if (infusion == null) continue;
					if (infusion.getTier() == tier){
						tierInfusion = infusion;
						if (tier.ordinal() >= highestSelectedTier)
							highestSelectedTier = tier.ordinal()+1;
						break;
					}
				}
				for (IArmorImbuement infusion : infusions){
					if ((tierInfusion == null && infusion.getTier().ordinal() <= highestSelectedTier)){
						if (tileEntity.isCreativeAllowed() || ArmorHelper.getArmorLevel(stack) >= ArmorHelper.getImbueCost(tier)){
							drawInfusionIconAt(drawX, drawY, infusion.getIconIndex());
							if (i >= drawX && i <= drawX + spriteWidth){
								if (j >= drawY && j <= drawY + spriteHeight){
									hoveredID = infusion.getID();
								}
							}
						}
					}else if (tierInfusion == infusion){
						drawInfusionIconAt(drawX, drawY, infusion.getIconIndex());
					}

					drawX += stepX;
				}
				drawY += stepY;
				drawX = startX;
			}
		}

		mc.renderEngine.bindTexture(foreground);
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		if (hoverLines.size() > 0)
			AMGuiHelper.instance.drawHoveringText(hoverLines, i, j, fontRendererObj, width, height);
	}

	private void drawInfusionIconAt(int x, int y, int index){
		drawTexturedModalRect(x, y, (index % 5) * spriteWidth, (int)Math.floor(index / 5) * spriteHeight, spriteWidth, spriteHeight);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{

	}

}
