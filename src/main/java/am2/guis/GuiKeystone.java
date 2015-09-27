package am2.guis;

import am2.guis.controls.GuiSlideControl;
import am2.guis.controls.GuiStatedImageButton;
import am2.items.ContainerKeystone;
import am2.items.InventoryKeyStone;
import am2.items.InventoryRuneBag;
import am2.items.ItemKeystone.KeystoneCombination;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;


public class GuiKeystone extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("keystone_GUI.png"));
	private static final ResourceLocation extras = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("spellBookGui_2.png"));

	private GuiTextField combinationName;
	private GuiStatedImageButton addCombination;
	private GuiStatedImageButton forgetCombination;

	private GuiStatedImageButton nextCombination;
	private GuiStatedImageButton prevCombination;

	private GuiSlideControl scrollBar;

	private int currentCombination = 0;
	private int displayTime = 0;
	private String displayMessage = "";
	private int displayColor = 0xff0000;
	private int hoveredCombo = -1;
	private int comboScrollOffset = 0;

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		if (((ContainerKeystone)this.inventorySlots).runebagSlot > -1){
			drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		}else{
			drawTexturedModalRect(l, i1, 0, 0, xSize, 107);
			drawTexturedModalRect(l, i1 + 107, 0, 144, xSize, 96);
		}
		drawTexturedModalRect(l + xSize, i1 + 5, 176, 6, 76, 176);

		hoveredCombo = -1;
		int numCombos = ItemsCommonProxy.keystone.numCombinations(((ContainerKeystone)this.inventorySlots).getKeystoneStack());

		if (i >= l + xSize && i <= l + xSize + 50){
			if (j >= i1 && j <= i1 + 175){
				int comboIndex = (int)Math.floor(((j - 20) / 18)) + comboScrollOffset;
				if (comboIndex >= 0 && comboIndex < numCombos){
					hoveredCombo = comboIndex;
				}
			}
		}
	}

	private void recalculateSlider(){
		int sliderMax = Math.max(0, ItemsCommonProxy.keystone.numCombinations(((ContainerKeystone)this.inventorySlots).getKeystoneStack()) - 9);
		scrollBar.setMaximum(Math.max(sliderMax, 1));

		if (sliderMax == 0)
			scrollBar.enabled = false;
		else
			scrollBar.enabled = true;
	}

	@Override
	public void initGui(){
		super.initGui();
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		combinationName = new GuiTextField(fontRendererObj, 44, 86, 88, 16);

		int sliderMax = Math.max(0, ItemsCommonProxy.keystone.numCombinations(((ContainerKeystone)this.inventorySlots).getKeystoneStack()) - 9);

		scrollBar = new GuiSlideControl(4, l + xSize + 58, i1 + 14, 159, "", 0, 0, Math.max(sliderMax, 1));
		scrollBar.setVertical();
		scrollBar.setButtonOnly();
		scrollBar.setOverrideTexture(background);
		scrollBar.setButtonProperties(184, 193, 190, 193, 6, 15);
		scrollBar.setScale(1.0f);
		scrollBar.setNoDynamicDisplay(true);

		if (sliderMax == 0)
			scrollBar.enabled = false;

		prevCombination = new GuiStatedImageButton(3, l + 8, i1 + 86, background, 208, 192);
		nextCombination = new GuiStatedImageButton(2, l + 152, i1 + 86, background, 208, 208);
		addCombination = new GuiStatedImageButton(0, l + 26, i1 + 86, background, 208, 224);
		forgetCombination = new GuiStatedImageButton(1, l + 134, i1 + 86, background, 208, 240);

		prevCombination.addStateCoords(GuiStatedImageButton.States.MOUSEOVER, 224, 192);
		nextCombination.addStateCoords(GuiStatedImageButton.States.MOUSEOVER, 224, 208);
		addCombination.addStateCoords(GuiStatedImageButton.States.MOUSEOVER, 224, 224);
		forgetCombination.addStateCoords(GuiStatedImageButton.States.MOUSEOVER, 224, 240);

		prevCombination.addStateCoords(GuiStatedImageButton.States.CLICK, 240, 192);
		nextCombination.addStateCoords(GuiStatedImageButton.States.CLICK, 240, 208);
		addCombination.addStateCoords(GuiStatedImageButton.States.CLICK, 240, 224);
		forgetCombination.addStateCoords(GuiStatedImageButton.States.CLICK, 240, 240);

		prevCombination.setDimensions(16, 16);
		nextCombination.setDimensions(16, 16);
		addCombination.setDimensions(16, 16);
		forgetCombination.setDimensions(16, 16);

		this.buttonList.add(addCombination);
		this.buttonList.add(forgetCombination);
		this.buttonList.add(nextCombination);
		this.buttonList.add(prevCombination);
		this.buttonList.add(scrollBar);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button == scrollBar){
			comboScrollOffset = Math.round(scrollBar.getShiftedValue());
			return;
		}
		if (button == addCombination){
			if (combinationName.getText() == null || combinationName.getText().trim().equals("")){
				displayMessage = StatCollector.translateToLocal("am2.gui.nameRequired");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0xff0000;
			}else{
				displayMessage = StatCollector.translateToLocal("am2.gui.comboStored");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0x00ff00;

				int[] metas = new int[InventoryKeyStone.inventorySize];
				for (int i = 0; i < InventoryKeyStone.inventorySize; ++i){
					ItemStack stack = this.keystoneInventory.getStackInSlot(i);
					metas[i] = stack != null ? stack.getMetadata() : -1;
				}
				AMDataWriter writer = new AMDataWriter();
				writer.add(true);
				writer.add(combinationName.getText());
				for (int i = 0; i < metas.length; ++i)
					writer.add(metas[i]);
				AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SAVE_KEYSTONE_COMBO, writer.generate());

				ItemsCommonProxy.keystone.addCombination(((ContainerKeystone)this.inventorySlots).getKeystoneStack(), combinationName.getText(), metas);

				recalculateSlider();
			}
		}else if (button == forgetCombination){
			KeystoneCombination matchedCombo = ((ContainerKeystone)this.inventorySlots).getCurrentMatchedCombination();
			if (matchedCombo == null){
				displayMessage = StatCollector.translateToLocal("am2.gui.comboNotSaved");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0xff0000;
			}else{
				displayMessage = StatCollector.translateToLocal("am2.gui.comboRemoved");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0x00ff00;

				combinationName.setText("");

				AMDataWriter writer = new AMDataWriter();
				writer.add(false);
				writer.add(matchedCombo.name);
				for (int i = 0; i < matchedCombo.metas.length; ++i)
					writer.add(matchedCombo.metas[i]);
				AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SAVE_KEYSTONE_COMBO, writer.generate());

				ItemsCommonProxy.keystone.removeCombination(((ContainerKeystone)this.inventorySlots).getKeystoneStack(), matchedCombo.name);

				recalculateSlider();
			}
		}else{

			int numCombinatons = ItemsCommonProxy.keystone.numCombinations(((ContainerKeystone)this.inventorySlots).getKeystoneStack());

			int originalCombo = currentCombination;
			boolean changed = false;
			boolean skipped = false;

			if (numCombinatons == 0) return;
			if (numCombinatons == 1){
				currentCombination = 0;
				if (((ContainerKeystone)this.inventorySlots).setInventoryToCombination(currentCombination))
					changed = true;
			}else{
				if (button == nextCombination){
					currentCombination++;
					if (currentCombination >= numCombinatons)
						currentCombination = 0;

					while (currentCombination != originalCombo){
						if (!((ContainerKeystone)this.inventorySlots).setInventoryToCombination(currentCombination)){
							currentCombination++;
							if (currentCombination >= numCombinatons)
								currentCombination = 0;
							skipped = true;
						}else{
							changed = true;
							break;
						}
					}
				}else if (button == prevCombination){
					currentCombination--;
					if (currentCombination < 0)
						currentCombination = numCombinatons - 1;

					while (currentCombination != originalCombo){
						if (!((ContainerKeystone)this.inventorySlots).setInventoryToCombination(currentCombination)){
							currentCombination--;
							if (currentCombination < 0)
								currentCombination = numCombinatons - 1;
							skipped = true;
						}else{
							changed = true;
							break;
						}
					}
				}
			}

			if (!changed){
				displayMessage = StatCollector.translateToLocal("am2.gui.comboMissingRunes");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0xff0000;
			}else if (skipped){
				displayMessage = StatCollector.translateToLocal("am2.gui.oneOrMoreSkipped");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0xff0000;
			}
		}

	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3){
		super.mouseClicked(par1, par2, par3);

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		par1 -= l;
		par2 -= i1;
		combinationName.mouseClicked(par1, par2, par3);

		if (hoveredCombo > -1){
			if (!((ContainerKeystone)this.inventorySlots).setInventoryToCombination(hoveredCombo)){
				displayMessage = StatCollector.translateToLocal("am2.gui.comboMissingRunes");
				displayTime = AMGuiHelper.instance.getSlowTicker() + 3;
				displayColor = 0xff0000;
			}
		}
	}

	@Override
	protected void mouseReleased(int par1, int par2, int par3){
		super.mouseReleased(par1, par2, par3);
		for (Object button : this.buttonList){
			((GuiButton)button).mouseReleased(par1, par2);
		}
	}

	@Override
	protected void mouseClickMove(int par1, int par2, int par3, long par4){
		super.mouseClickMove(par1, par2, par3, par4);
		if (scrollBar.dragging){
			comboScrollOffset = Math.round(scrollBar.getShiftedValue());
			return;
		}
	}

	@Override
	protected void keyTyped(char par1, int par2){
		if (combinationName.isFocused()){
			combinationName.textboxKeyTyped(par1, par2);
		}else{
			if (!Character.isDigit(par1))
				super.keyTyped(par1, par2);
		}
	}

	public GuiKeystone(InventoryPlayer inventoryplayer, ItemStack keystoneStack, ItemStack runebagStack, InventoryKeyStone inventorykeystone, InventoryRuneBag runeBag, int runeBagIndex){
		super(new ContainerKeystone(inventoryplayer, keystoneStack, runebagStack, inventorykeystone, runeBag, runeBagIndex));
		keystoneInventory = inventorykeystone;
		xSize = 176;
		ySize = 240;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;


		mc.renderEngine.bindTexture(new ResourceLocation("textures/atlas/items.png"));

		int numCombos = Math.min(ItemsCommonProxy.keystone.numCombinations(((ContainerKeystone)this.inventorySlots).getKeystoneStack()), comboScrollOffset + 9);

		int cx = xSize;
		int cy = 13;

		Tessellator t = Tessellator.instance;

		KeystoneCombination matchedCombo = ((ContainerKeystone)this.inventorySlots).getCurrentMatchedCombination();

		for (int i = comboScrollOffset; i < numCombos; ++i){
			KeystoneCombination combo = ItemsCommonProxy.keystone.getCombinationAt(((ContainerKeystone)this.inventorySlots).getKeystoneStack(), i);

			if (matchedCombo != null && combo.equals(matchedCombo)){
				currentCombination = i;
				for (int n = 0; n < combo.metas.length; ++n){
					if (combo.metas[n] > -1){
						IIcon icon = AMGuiIcons.selectedRunes;
						AMGuiHelper.DrawIconAtXY(icon, cx, cy, this.zLevel, 16, 16, true);
					}
					cx += 18;
				}
				mc.renderEngine.bindTexture(new ResourceLocation("textures/atlas/items.png"));
				cx = xSize;
			}
			for (int n = 0; n < combo.metas.length; ++n){
				if (combo.metas[n] > -1){
					IIcon icon = ItemsCommonProxy.rune.getIconFromDamage(combo.metas[n]);
					AMGuiHelper.DrawIconAtXY(icon, cx, cy, this.zLevel, 16, 16, true);
				}
				cx += 18;
			}
			cy += 18;
			cx = xSize;
		}

		mc.renderEngine.bindTexture(extras);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		//special slot(s)
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int index = ((ContainerKeystone)this.inventorySlots).specialSlotIndex - 32;
		int x = 8 + 18 * index;
		int y = (((ContainerKeystone)this.inventorySlots).runebagSlot > -1) ? 216 : 179;
		drawTexturedModalRect(x, y, 0, 20, 16, 16);

		if (((ContainerKeystone)this.inventorySlots).runebagSlot > -1){
			index = ((ContainerKeystone)this.inventorySlots).runebagSlot;
			x = 8 + 18 * (index % 9);
			y = index < 9 ? 216 : 140 + 18 * (int)Math.floor(index / 9f);
			drawTexturedModalRect(x, y, 0, 20, 16, 16);
		}
		GL11.glDisable(GL11.GL_BLEND);

		combinationName.drawTextBox();

		if (AMGuiHelper.instance.getSlowTicker() < displayTime){
			fontRendererObj.drawSplitString(displayMessage, -90, 0, 90, displayColor);
		}else{
			displayTime = 0;
		}


		if (matchedCombo != null){
			combinationName.setText(matchedCombo.name);
		}


		if (hoveredCombo > -1){
			KeystoneCombination combo = ItemsCommonProxy.keystone.getCombinationAt(((ContainerKeystone)this.inventorySlots).getKeystoneStack(), hoveredCombo);
			ArrayList<String> lines = new ArrayList<String>();
			lines.add(combo.name);
			lines.add("\2477\247o" + StatCollector.translateToLocal("am2.gui.keystoneComboClick"));
			lines.add("\2477\247o" + StatCollector.translateToLocal("am2.gui.keystoneComboClick2") + "\247r");
			AMGuiHelper.drawHoveringText(lines, par1 - 25, par2 + 18, Minecraft.getMinecraft().fontRendererObj, this.xSize, this.ySize);
		}
	}

	private final InventoryKeyStone keystoneInventory;

}
