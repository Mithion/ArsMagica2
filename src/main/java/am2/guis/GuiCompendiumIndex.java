package am2.guis;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import am2.AMCore;
import am2.api.SkillTreeEntry;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.LearnStates;
import am2.guis.AMGuiHelper.CompendiumBreadcrumb;
import am2.guis.controls.GuiButtonCompendiumLink;
import am2.guis.controls.GuiButtonCompendiumNext;
import am2.guis.controls.GuiButtonCompendiumTab;
import am2.guis.controls.GuiSpellImageButton;
import am2.lore.ArcaneCompendium;
import am2.lore.CompendiumEntry;
import am2.lore.CompendiumEntrySpellComponent;
import am2.lore.CompendiumEntrySpellModifier;
import am2.lore.CompendiumEntrySpellShape;
import am2.lore.CompendiumEntryType;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;

public class GuiCompendiumIndex extends GuiScreen implements GuiYesNoCallback{
	private String activeCategory;
	private String activeCategoryID;
	private final LinkedHashSet<CompendiumEntryType> categories;
	private HashMap<String, Integer> pagingData;

	int xSize = 360;
	int ySize = 256;

	int page = 0;

	private CompendiumEntry currentParentEntry = null;
	private String currentParentEntryName = null;
	ArrayList<String> lines;
	int lineWidth = 140;
	int maxLines = 22;

	GuiButtonCompendiumNext nextPage;
	GuiButtonCompendiumNext prevPage;
	GuiButtonCompendiumTab backToIndex;

	GuiSpellImageButton updateButton;

	SkillData sk;

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", "textures/guis/ArcaneCompendiumIndexGui.png");

	public GuiCompendiumIndex(){
		categories = ArcaneCompendium.instance.getCategories();
		activeCategory = categories.iterator().next().getCategoryLabel();
		activeCategoryID = categories.iterator().next().getCategoryName();
		pagingData = new HashMap<String, Integer>();
		lines = new ArrayList<String>();

		sk = SkillData.For(Minecraft.getMinecraft().thePlayer);
	}

	public GuiCompendiumIndex(CompendiumBreadcrumb breadcrumb){
		this();
		this.page = breadcrumb.page;
		this.activeCategory = (String) breadcrumb.refData[0];
		this.activeCategoryID = (String) breadcrumb.refData[1];
		this.currentParentEntryName = (String) breadcrumb.refData[2];
		this.pagingData = (HashMap<String, Integer>) breadcrumb.refData[3];
		if (currentParentEntryName != null){
			this.currentParentEntry = ArcaneCompendium.instance.getEntry(currentParentEntryName);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		int idCount = 0;
		idCount = initTabs(idCount);
		for (CompendiumEntryType category : categories){
			idCount = initButtonsForCategory(category.getCategoryName(), idCount, category.equals(activeCategory));
		}

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		nextPage = new GuiButtonCompendiumNext(idCount++, l + 320, i1 + 13, true);
		prevPage = new GuiButtonCompendiumNext(idCount++, l + 33, i1 + 13, false);

		backToIndex = new GuiButtonCompendiumTab(idCount++, l - 10, i1 + 20, StatCollector.translateToLocal("am2.gui.back"), "back", null);
		backToIndex.setActive(true);
		backToIndex.visible = false;

		updateButton = new GuiSpellImageButton(idCount++, l - 5, i1 + 10, ArcaneCompendium.instance.isModUpdateAvailable() ? AMGuiIcons.warning : AMGuiIcons.checkmark, 0, 0);
		updateButton.setDimensions(10, 10);
		updateButton.setPopupText(ArcaneCompendium.instance.isModUpdateAvailable() ? StatCollector.translateToLocal("am2.tooltip.updateAvailable") : StatCollector.translateToLocal("am2.tooltip.upToDate"));

		this.buttonList.add(nextPage);
		this.buttonList.add(prevPage);
		this.buttonList.add(backToIndex);
		if (AMCore.config.allowVersionChecks())
			this.buttonList.add(updateButton);

		switchCategoryAndPage();

		if (this.currentParentEntry != null && this.currentParentEntry.hasSubItems()){
			for (Object btn : this.buttonList){
				if (btn instanceof GuiButtonCompendiumTab){
					((GuiButtonCompendiumTab) btn).visible = false;
				}
			}
			backToIndex.visible = true;
		}

		if (page < getNumPages()){
			nextPage.visible = true;
		}else{
			nextPage.visible = false;
		}

		if (this.page > 0){
			prevPage.visible = true;
		}else{
			prevPage.visible = false;
		}
	}

	protected int getNumPages(){
		Integer pages = this.pagingData.get(activeCategoryID);
		return pages != null ? pages.intValue() : 0;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
			for (int l = 0; l < this.buttonList.size(); ++l)
			{
				GuiButton guibutton = (GuiButton)this.buttonList.get(l);

				if (guibutton.mousePressed(Minecraft.getMinecraft(), par1, par2))
				{
					if (guibutton.id == updateButton.id){
						this.storeBreadcrumb();
						if (par3 == 0){ //left click
							this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, ArcaneCompendium.instance.getModDownloadLink(), 0, false));
						}else if (par3 == 1){ //right click
							this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, ArcaneCompendium.instance.getPatchNotesLink(), 1, false));
						}
					}
					if (par3 == 0)
					{
						this.actionPerformed(guibutton);
						return;
					}
				}
			}
	}

	@Override
	public void confirmClicked(boolean par1, int par2) {
		if (par1){
			switch(par2){
			case 0:
				openLink(URI.create(ArcaneCompendium.instance.getModDownloadLink()));
				break;
			case 1:
				openLink(URI.create(ArcaneCompendium.instance.getPatchNotesLink()));
				break;
			}
		}
		this.mc.displayGuiScreen(this);
	}

	private void openLink(URI par1URI)
    {
        try
        {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {par1URI});
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

	private void storeBreadcrumb(){
		AMGuiHelper.instance.pushCompendiumBreadcrumb("", this.page, CompendiumBreadcrumb.TYPE_INDEX, activeCategory, activeCategoryID, currentParentEntryName, pagingData);
	}

	@Override
	protected void actionPerformed(GuiButton buttonClicked) {

		if (buttonClicked.id == backToIndex.id){
			CompendiumBreadcrumb crumb = AMGuiHelper.instance.popCompendiumBreadcrumb();
			Minecraft.getMinecraft().displayGuiScreen(new GuiCompendiumIndex(crumb));
			return;
		}

		if (buttonClicked instanceof GuiButtonCompendiumTab){
			this.currentParentEntry = null;
			this.currentParentEntryName = null;
			this.activeCategory = ((GuiButtonCompendiumTab)buttonClicked).displayString;
			activeCategoryID = ((GuiButtonCompendiumTab)buttonClicked).categoryID;
			int visibleButtons = 0;
			for (Object button : this.buttonList){
				if (button instanceof GuiButtonCompendiumTab){
					((GuiButtonCompendiumTab)button).setActive(false);
				}else if (button instanceof GuiButtonCompendiumLink){
					if (((GuiButtonCompendiumLink)button).getCategory().equals(activeCategoryID) && ((GuiButtonCompendiumLink)button).getPageNum() == 0){
						((GuiButtonCompendiumLink)button).visible = true;
						visibleButtons++;
					}else{
						((GuiButtonCompendiumLink)button).visible = false;
					}
				}
			}

			if (visibleButtons == 0){
				String zeroItemText = ArcaneCompendium.instance.getZeroItemText(activeCategoryID);
				lines = GuiArcaneCompendium.splitStringToLines(fontRendererObj, zeroItemText, lineWidth, maxLines);
			}else{
				lines = null;
			}

			((GuiButtonCompendiumTab)buttonClicked).setActive(true);

			page = 0;
			prevPage.visible = false;
			if (getNumPages() > 0){
				nextPage.visible = true;
			}else{
				nextPage.visible = false;
			}

		}else if (buttonClicked instanceof GuiButtonCompendiumLink){
			this.currentParentEntry = null;
			if (((GuiButtonCompendiumLink)buttonClicked).hasSubItems()){
				storeBreadcrumb();

				for (Object btn : this.buttonList){
					if (btn instanceof GuiButtonCompendiumTab){
						((GuiButtonCompendiumTab) btn).visible = false;
					}
				}

				backToIndex.visible = true;

				this.currentParentEntryName = ((GuiButtonCompendiumLink)buttonClicked).getEntryID();
				this.currentParentEntry = ArcaneCompendium.instance.getEntry(currentParentEntryName);
				this.activeCategoryID = this.activeCategoryID + ":" + this.currentParentEntryName;

				currentParentEntry.setIsNew(false);

				switchCategoryAndPage();
			}else{
				CompendiumEntry entry = ArcaneCompendium.instance.getEntry(((GuiButtonCompendiumLink)buttonClicked).getEntryID());
				if (entry != null){
					storeBreadcrumb();
					GuiArcaneCompendium gui = entry.getCompendiumGui(((GuiButtonCompendiumLink)buttonClicked).getEntryID());
					if (gui != null){
						Minecraft.getMinecraft().displayGuiScreen(gui);
					}
				}
			}
		}

		if (getNumPages() > 0){
			if (buttonClicked.id == nextPage.id && page < getNumPages()){
				page++;
				switchCategoryAndPage();
				if (page == getNumPages()){
					nextPage.visible = false;
				}
				prevPage.visible = true;
			}else if (buttonClicked.id == prevPage.id && page > 0){
				page--;
				switchCategoryAndPage();
				if (page == 0){
					prevPage.visible = false;
				}

				nextPage.visible = true;
			}
		}
	}

	private void switchCategoryAndPage(){
		if (currentParentEntry != null){
			lines = GuiArcaneCompendium.splitStringToLines(fontRendererObj, currentParentEntry.getDescription(), lineWidth, maxLines);
			int numPages = currentParentEntry.hasSubItems() ? lines.size() : lines.size()-1;

			Integer existingPages = pagingData.get(activeCategoryID);
			if (existingPages == null || existingPages < numPages){
				pagingData.put(activeCategoryID, numPages);
				page = 0;
				prevPage.visible = false;
				if (numPages > 1){
					nextPage.visible = true;
				}
			}
		}

		for (Object button : this.buttonList){
			if (button == backToIndex) continue;
			if (button instanceof GuiButtonCompendiumLink){
				if (((GuiButtonCompendiumLink)button).getCategory().equals(activeCategoryID) && (((GuiButtonCompendiumLink)button).getPageNum() == this.page || ((GuiButtonCompendiumLink)button).getDisplayOnAllPages())){
					((GuiButtonCompendiumLink)button).visible = true;
				}else{
					((GuiButtonCompendiumLink)button).visible = false;
				}
			}
		}
	}

	private void setCategoryHasNewItems(String category, boolean hasNew){
		for (Object button : this.buttonList){
			if (button instanceof GuiButtonCompendiumTab && ((GuiButtonCompendiumTab)button).categoryID.equals(category)){
				((GuiButtonCompendiumTab)button).setHasNewSubitems(true);
				break;
			}
		}
	}

	private int initTabs(int idCount){
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		int tabX = l + 10;
		int tabY = i1 + 40;
		int tabWidth = 1;
		int tabHeight = 16;

		for (CompendiumEntryType category : this.categories){

			int unlockedSubItems = 0;

			ArrayList<CompendiumEntry> itemsInCategory = ArcaneCompendium.instance.getEntriesForCategory(category.getCategoryName());
			for (CompendiumEntry entry : itemsInCategory)
				if (!entry.isLocked())
					unlockedSubItems++;

			if (unlockedSubItems == 0)
				continue;

			GuiButtonCompendiumTab tab = new GuiButtonCompendiumTab(idCount++, tabX, tabY, category.getCategoryLabel(), category.getCategoryName(), category.getRepresentItem());
			if (this.activeCategory.equals(category)){
				tab.setActive(true);
			}
			if (tabWidth < tab.getWidth())
				tabWidth = tab.getWidth();
			tabY += 18;
			tabX--;
			this.buttonList.add(tab);
		}

		for (Object button : this.buttonList){
			((GuiButtonCompendiumTab)button).setDimensions(tabWidth, tabHeight);
		}

		return idCount;
	}

	private int initButtonsForCategory(String category, int idCount, boolean display){
		ArrayList<CompendiumEntry> itemsInCategory = ArcaneCompendium.instance.getEntriesForCategory(category);

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		int buttonX = l + 40;
		int buttonY = i1 + 35;

		int numPages = 0;

		for (int i = 0; i < itemsInCategory.size(); ++i){
			CompendiumEntry entry = itemsInCategory.get(i);
			if (entry.isLocked() || !checkSCMLimit(entry)) continue;
			String buttonLabel = entry.getName();
			while (fontRendererObj.getStringWidth(buttonLabel) > 125){
				buttonLabel = buttonLabel.substring(0, buttonLabel.length() - 4) + "...";
			}
			GuiButtonCompendiumLink link = new GuiButtonCompendiumLink(idCount++, buttonX, buttonY, fontRendererObj, buttonLabel, entry.getID(), category, entry.hasSubItems(), numPages);
			if (entry.isNew()){
				setCategoryHasNewItems(category, true);
				link.setNewItem();
			}
			link.visible = display && page == 0;
			buttonY += 12;
			if (buttonY > i1 + (ySize) - 25){
				if (buttonX > l + 40){
					numPages++;
					buttonX = l + 40;
				}else{
					buttonX += 155;
				}
				buttonY = i1 + 30;
			}

			this.buttonList.add(link);

			if (entry.hasSubItems()){
				idCount = initSubItemButtonsForEntry(entry, idCount, category);
			}
		}
		this.pagingData.put(category, numPages);

		return idCount;
	}

	private int initSubItemButtonsForEntry(CompendiumEntry entry, int idCount, String category){
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		boolean entryHasDescription = !entry.getDescription().equals("");
		int buttonXStart = entryHasDescription ? l + 185 : l + 40;

		int buttonX = buttonXStart;
		int buttonY = i1 + 35;

		int numPages = 0;

		for (CompendiumEntry subItem : entry.getSubItems()){
			if (!checkSCMLimit(subItem)) continue;
			String newCategory = category + ":" + entry.getID();
			GuiButtonCompendiumLink link = new GuiButtonCompendiumLink(idCount++, buttonX, buttonY, fontRendererObj, subItem.getName(), subItem.getID(), newCategory, subItem.hasSubItems(), numPages);
			if (entryHasDescription && entry.getSubItems().length < maxLines)
				link.setShowOnAllPages();
			link.visible = false;
			buttonY += 12;
			if (buttonY > i1 + (ySize) - 25){
				if (buttonX > l + 40){
					numPages++;
					buttonX = buttonXStart;
				}else{
					buttonX += 145;
				}
				buttonY = i1 + 30;
			}

			this.buttonList.add(link);

			if (subItem.hasSubItems()){
				idCount = initSubItemButtonsForEntry(subItem, idCount, category);
			}
		}

		this.pagingData.put(category, numPages);

		return idCount;
	}

	private boolean checkSCMLimit(CompendiumEntry entry){
		if (entry instanceof CompendiumEntrySpellComponent || entry instanceof CompendiumEntrySpellModifier || entry instanceof CompendiumEntrySpellShape){
			ISkillTreeEntry part = SkillManager.instance.getSkill(entry.getID());
			if (part == null)
				return false;
			SkillTreeEntry ste = SkillTreeManager.instance.getSkillTreeEntry(part);
			if (ste == null)
				return false;
			return sk.isEntryKnown(ste) || sk.getLearnState(ste, Minecraft.getMinecraft().thePlayer) == LearnStates.CAN_LEARN;
		}
		return true;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1){
			storeBreadcrumb();
			onGuiClosed();
		}
		super.keyTyped(par1, par2);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {

		this.drawDefaultBackground();

		RenderHelper.enableGUIStandardItemLighting();

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		mc.renderEngine.bindTexture(background);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		this.drawTexturedModalRect_Classic(l, i1, 0, 0, xSize, ySize, 256, 240);

		String compendiumTitle = "\247nArcane Compendium";

		int y_start_title = i1 + 20;
		int x_start_title = l + 100 - (fontRendererObj.getStringWidth(compendiumTitle) / 2);

		fontRendererObj.drawString(compendiumTitle, x_start_title, y_start_title, 0);

		int x_start_line = l + 35;
		int y_start_line = i1 + 35;

		if (lines != null && lines.size() > page){
			AMGuiHelper.drawCompendiumText(lines.get(page), x_start_line, y_start_line, lineWidth, 0x000000, fontRendererObj);
		}

		super.drawScreen(par1, par2, par3);
	}

	public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height)
	{
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(dst_x + 0, dst_y + dst_height, this.zLevel, (src_x + 0) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + dst_height, this.zLevel, (src_x + src_width) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + 0, this.zLevel, (src_x + src_width) * var7, (src_y + 0) * var8);
		var9.addVertexWithUV(dst_x + 0, dst_y + 0, this.zLevel, (src_x + 0) * var7, (src_y + 0) * var8);
		var9.draw();
	}

	@Override
	public void onGuiClosed() {
		ArcaneCompendium.instance.saveUnlockData();
		super.onGuiClosed();
	}
}
