package am2.guis;

import am2.AMCore;
import am2.containers.ContainerSpellCustomization;
import am2.guis.controls.GuiButtonVariableDims;
import am2.guis.controls.GuiSpellImageButton;
import am2.network.SeventhSanctum;
import am2.spell.SpellTextureHelper;
import am2.texture.ResourceManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiSpellCustomization extends GuiContainer{

	private int page = 0;
	private int numPages = 0;
	private int curIndex = 0;
	private String curName = "";

	private GuiButtonVariableDims btnNext;
	private GuiButtonVariableDims btnPrev;
	private GuiButtonVariableDims btnRandomName;
	private GuiTextField spellName;

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("SpellCustomization.png"));

	public GuiSpellCustomization(EntityPlayer player){
		super(new ContainerSpellCustomization(player));
		this.xSize = 176;
		this.ySize = 255;
	}

	@Override
	protected void keyTyped(char par1, int par2){
		if (spellName.isFocused()){
			if (spellName.textboxKeyTyped(par1, par2)){
				this.curName = spellName.getText();
				((ContainerSpellCustomization)this.inventorySlots).setNameAndIndex(curName, curIndex);
			}
		}else{
			super.keyTyped(par1, par2);
		}
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton){
		super.actionPerformed(par1GuiButton);

		if (par1GuiButton.id == btnPrev.id){
			if (page > 0){
				page--;
				for (Object btn : this.buttonList){
					if (btn instanceof GuiSpellImageButton){
						if (((GuiSpellImageButton)btn).getPage() == page) ((GuiSpellImageButton)btn).visible = true;
						else ((GuiSpellImageButton)btn).visible = false;
					}
				}
			}
		}else if (par1GuiButton.id == btnNext.id){
			if (page < numPages){
				page++;
				for (Object btn : this.buttonList){
					if (btn instanceof GuiSpellImageButton){
						if (((GuiSpellImageButton)btn).getPage() == page) ((GuiSpellImageButton)btn).visible = true;
						else ((GuiSpellImageButton)btn).visible = false;
					}
				}
			}
		}else if (par1GuiButton.id == btnRandomName.id){
			spellName.setText(SeventhSanctum.instance.getNextSuggestion());
			curName = spellName.getText();
			((ContainerSpellCustomization)this.inventorySlots).setNameAndIndex(curName, curIndex);
		}

		if (par1GuiButton instanceof GuiSpellImageButton){
			this.curIndex = ((GuiSpellImageButton)par1GuiButton).getIndex();
			((ContainerSpellCustomization)this.inventorySlots).setNameAndIndex(curName, curIndex);
			for (Object btn : this.buttonList){
				if (btn instanceof GuiSpellImageButton){
					((GuiSpellImageButton)btn).setSelected(false);
				}
			}
			((GuiSpellImageButton)par1GuiButton).setSelected(true);
		}
	}

	@Override
	public void initGui(){
		super.initGui();

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		if (AMCore.config.suggestSpellNames())
			spellName = new GuiTextField(fontRendererObj, l + 8, i1 + 8, xSize - 36, 16);
		else
			spellName = new GuiTextField(fontRendererObj, l + 8, i1 + 8, xSize - 16, 16);

		String suggestion = ((ContainerSpellCustomization)this.inventorySlots).getInitialSuggestedName();
		spellName.setText(suggestion);
		if (!suggestion.equals("")){
			curName = suggestion;
			((ContainerSpellCustomization)this.inventorySlots).setNameAndIndex(curName, curIndex);
		}


		btnPrev = new GuiButtonVariableDims(0, l + 8, i1 + 26, StatCollector.translateToLocal("am2.gui.prev")).setDimensions(48, 20);
		btnNext = new GuiButtonVariableDims(1, l + xSize - 56, i1 + 26, StatCollector.translateToLocal("am2.gui.next")).setDimensions(48, 20);

		btnRandomName = new GuiButtonVariableDims(2, l + xSize - 24, i1 + 5, "???");
		btnRandomName.setDimensions(20, 20);

		this.buttonList.add(btnPrev);
		this.buttonList.add(btnNext);

		if (AMCore.config.suggestSpellNames())
			this.buttonList.add(btnRandomName);

		int IIcon_start_x = l + 12;
		int IIcon_start_y = i1 + 50;

		int btnX = IIcon_start_x;
		int btnY = IIcon_start_y;
		int id = 3;
		int IIconCount = 0;
		int curPage = 0;

		for (IIcon icon : SpellTextureHelper.instance.getAllIcons()){
			GuiSpellImageButton spellButton = new GuiSpellImageButton(id++, btnX, btnY, icon, IIconCount++, curPage);
			if (curPage != 0){
				spellButton.visible = false;
			}
			this.buttonList.add(spellButton);
			btnX += 14;
			if (btnX > (l + xSize) - 15){
				btnX = IIcon_start_x;
				btnY += 14;
				if (btnY > (i1 + ySize - 10)){
					btnY = IIcon_start_y;
					curPage++;
				}
			}
		}

		this.numPages = curPage;
	}

	@Override
	protected void mouseClicked(int x, int y, int par3){
		super.mouseClicked(x, y, par3);
		spellName.mouseClicked(x, y, par3);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		spellName.drawTextBox();
	}

}
