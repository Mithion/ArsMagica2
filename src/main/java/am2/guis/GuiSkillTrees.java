package am2.guis;

import am2.AMCore;
import am2.api.SkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.LearnStates;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTrees;
import am2.guis.controls.GuiButtonSkillTreeTab;
import am2.lore.ArcaneCompendium;
import am2.lore.CompendiumEntryTypes;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.texture.SpellIconManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class GuiSkillTrees extends GuiScreen{
	int xSize = 210;
	int ySize = 246;


	private GuiButtonSkillTreeTab offense;
	private GuiButtonSkillTreeTab defense;
	private GuiButtonSkillTreeTab utility;
	private GuiButtonSkillTreeTab talents;
	private GuiButtonSkillTreeTab affinity;
	private final int buttonPadding = 2;

	private final EntityPlayer player;

	private boolean isDragging = false;
	private int offsetX = 105;
	private int offsetY = 0;

	private final int IIcondims = 32;

	private int lastMouseX;
	private int lastMouseY;

	private SkillTrees activeTree;
	private SkillTreeEntry hoveredItem;
	private Affinity hoveredAffinity;

	private ArrayList<SkillTreeEntry> skillTree;

	private static final ResourceLocation rl_background = new ResourceLocation("arsmagica2", "textures/guis/SkillTreeUI.png");
	private static final ResourceLocation rl_offense = new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Offense.png");
	private static final ResourceLocation rl_defense = new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Defense.png");
	private static final ResourceLocation rl_utility = new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Utility.png");
	private static final ResourceLocation rl_talents = new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Talents.png");
	private static final ResourceLocation rl_items = new ResourceLocation("textures/atlas/items.png");


	public GuiSkillTrees(EntityPlayer player){
		CompendiumEntryTypes.instance.initTextures();

		this.player = player;
		activeTree = SkillTrees.Offense;
		skillTree = SkillTreeManager.instance.getTree(activeTree);
	}

	@Override
	public void initGui(){
		super.initGui();

		int l = (width - xSize) / 2 + 8;
		int i1 = (height - ySize) / 2;

		offense = new GuiButtonSkillTreeTab(0, l, i1, SkillTrees.Offense);
		defense = new GuiButtonSkillTreeTab(0, l + GuiButtonSkillTreeTab.buttonWidth + buttonPadding, i1, SkillTrees.Defense);
		utility = new GuiButtonSkillTreeTab(0, l + GuiButtonSkillTreeTab.buttonWidth * 2 + buttonPadding * 2, i1, SkillTrees.Utility);
		talents = new GuiButtonSkillTreeTab(0, l + GuiButtonSkillTreeTab.buttonWidth * 3 + buttonPadding * 3, i1, SkillTrees.Talents);
		affinity = new GuiButtonSkillTreeTab(0, l + GuiButtonSkillTreeTab.buttonWidth * 4 + buttonPadding * 4, i1, SkillTrees.Affinity);

		offense.setActive(true);

		this.buttonList.add(offense);
		this.buttonList.add(defense);
		this.buttonList.add(utility);
		if (ExtendedProperties.For(Minecraft.getMinecraft().thePlayer).getMagicLevel() >= 5)
			this.buttonList.add(talents);
		if (!ArcaneCompendium.instance.getEntry("affinity").isLocked())
			this.buttonList.add(affinity);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) throws IOException{
		if (par1GuiButton instanceof GuiButtonSkillTreeTab){
			this.activeTree = ((GuiButtonSkillTreeTab)par1GuiButton).getTree();
			skillTree = SkillTreeManager.instance.getTree(activeTree);

			switch (activeTree){
			case Defense:
				offsetX = 91;
				offsetY = 0;
				break;
			case Offense:
				offsetX = 105;
				offsetY = 0;
				break;
			case Utility:
				offsetX = 94;
				offsetY = 3;
				break;
			}
			for (Object button : this.buttonList){
				if (button instanceof GuiButtonSkillTreeTab){
					((GuiButtonSkillTreeTab)button).setActive(false);
				}
			}
			((GuiButtonSkillTreeTab)par1GuiButton).setActive(true);
		}
		super.actionPerformed(par1GuiButton);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3){

		SkillData sk = SkillData.For(Minecraft.getMinecraft().thePlayer);

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		if (AMCore.config.getSkillTreeSecondaryTierCap() < SkillTreeManager.instance.getHighestTier() && (sk.getPrimaryTree() == null || sk.getPrimaryTree() == SkillTrees.None) && sk.getSpellPoints(SkillPointTypes.BLUE) > 0){
			String s = StatCollector.translateToLocal("am2.gui.lockWarning");
			fontRendererObj.drawSplitString(s, l - 120, i1 + 20, 110, 0xbf6325);
		}


		if (isDragging){
			int dx = lastMouseX - par1;
			int dy = lastMouseY - par2;

			this.offsetX += dx;
			this.offsetY += dy;

			if (this.offsetX < 0) this.offsetX = 0;
			if (this.offsetX > 180) this.offsetX = 180;

			if (this.offsetY < 0) this.offsetY = 0;
			if (this.offsetY > 180) this.offsetY = 180;
		}
		lastMouseX = par1;
		lastMouseY = par2;

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		if (activeTree == SkillTrees.Offense)
			Minecraft.getMinecraft().renderEngine.bindTexture(rl_offense);
		else if (activeTree == SkillTrees.Defense)
			Minecraft.getMinecraft().renderEngine.bindTexture(rl_defense);
		else if (activeTree == SkillTrees.Utility)
			Minecraft.getMinecraft().renderEngine.bindTexture(rl_utility);
		else if (activeTree == SkillTrees.Talents || activeTree == SkillTrees.Affinity)
			Minecraft.getMinecraft().renderEngine.bindTexture(rl_talents);
		else return;

		drawTexturedModalRect_Classic(l + 5, i1 + GuiButtonSkillTreeTab.buttonHeight + 5, offsetX, offsetY, 200, 200, 75, 75);

		if (activeTree == SkillTrees.Affinity){
			drawAffinity();
		}else{
			drawSkillTree();
		}

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		Minecraft.getMinecraft().renderEngine.bindTexture(rl_background);
		drawTexturedModalRect(l, i1 + GuiButtonSkillTreeTab.buttonHeight, 0, 0, 210, 210);

		drawTexturedModalRect(l + xSize - 29, i1, 210, 0, 37, 37);


		String quantity = String.format("%d", sk.getSpellPoints(SkillPointTypes.BLUE));
		Minecraft.getMinecraft().fontRendererObj.drawString(quantity, l + xSize - 24 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(quantity) / 2), i1 + 5, 0x0000FF);

		quantity = String.format("%d", sk.getSpellPoints(SkillPointTypes.GREEN));
		Minecraft.getMinecraft().fontRendererObj.drawString(quantity, l + xSize - 12 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(quantity) / 2), i1 + 5, 0x00FF00);

		quantity = String.format("%d", sk.getSpellPoints(SkillPointTypes.RED));
		Minecraft.getMinecraft().fontRendererObj.drawString(quantity, l + xSize - 18 - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(quantity) / 2), i1 + 15, 0xFF0000);

		super.drawScreen(par1, par2, par3);

		if (hoveredItem != null){
			ArrayList<String> text = new ArrayList<String>();
			FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
			String s = SkillManager.instance.getDisplayName(hoveredItem.registeredItem);
			LearnStates state = sk.getLearnState(hoveredItem, Minecraft.getMinecraft().thePlayer);
			if (state == LearnStates.LEARNED) s += " (" + StatCollector.translateToLocal("am2.gui.known") + ")";
			else if (state == LearnStates.CAN_LEARN)
				s += " (" + StatCollector.translateToLocal("am2.gui.notLearned") + ")";
			else if (state == LearnStates.DISABLED) s = StatCollector.translateToLocal("am2.gui.cfgDisabled");
			//else fr = Minecraft.getMinecraft().standardGalacticFontRenderer;
			text.add(s);
			SkillPointTypes type = SkillTreeManager.instance.getSkillPointTypeForPart(hoveredItem.registeredItem);

			if (hoveredItem.registeredItem instanceof ISpellComponent){
				EnumSet<Affinity> aff = ((ISpellComponent)hoveredItem.registeredItem).getAffinity();

				int affX = lastMouseX + 14;
				int affY = lastMouseY - 34;
				drawGradientAround(affX - 2, affY - 2, 18 * aff.size() + 2, 18);
				for (Affinity a : aff){
					if (a == Affinity.NONE)
						continue;
					DrawIconAtXY(a.representItem.getIconFromDamage(a.representMeta), "item", affX, affY, 16, 16, false);
					affX += 18;
				}
			}

			if (!AMCore.config.colourblindMode()){
				drawHoveringText(text, lastMouseX, lastMouseY, fr, state == LearnStates.LEARNED ? 0xFFFFFF : type == SkillPointTypes.SILVER ? 0x888888 : type == SkillPointTypes.BLUE ? 0x4444FF : type == SkillPointTypes.GREEN ? 0x44FF44 : 0xFF4444);
			}else{
				text.add(StatCollector.translateToLocal("am2.gui." + type.toString().toLowerCase() + "Point"));
				drawHoveringText(text, lastMouseX, lastMouseY, fr, 0xFFFFFF);
			}
		}

		if (hoveredAffinity != null){
			List list = AffinityData.For(Minecraft.getMinecraft().thePlayer).getColoredAffinityEffects(hoveredAffinity);
			//list.add(0, "\247f" + hoveredAffinity.name());
			drawHoveringText(list, lastMouseX, lastMouseY, fontRendererObj, 0xFFFFFF);
		}
	}

	private void drawSkillTree(){
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2 + GuiButtonSkillTreeTab.buttonHeight;

		SkillData sk = SkillData.For(Minecraft.getMinecraft().thePlayer);

		for (SkillTreeEntry entry : skillTree){
			if (entry.prerequisites == null || entry.prerequisites.length == 0) continue;
			for (SkillTreeEntry prerequisite : entry.prerequisites){
				int color = 0;
				if (sk.isEntryKnown(prerequisite) || sk.getLearnState(prerequisite, Minecraft.getMinecraft().thePlayer) == LearnStates.DISABLED){
					color = 0x006600;
				}else{
					continue;
				}
				line2d(l + entry.x - offsetX * 2 + 16, i1 + entry.y - offsetY * 2 + 16, l + entry.x - offsetX * 2 + 16, i1 + prerequisite.y - offsetY * 2 + 16, color);
				line2d(l + entry.x - offsetX * 2 + 16, i1 + prerequisite.y - offsetY * 2 + 16, l + prerequisite.x - offsetX * 2 + 16, i1 + prerequisite.y - offsetY * 2 + 16, color);
			}
		}

		for (SkillTreeEntry entry : skillTree){
			if (entry.prerequisites == null || entry.prerequisites.length == 0) continue;
			for (SkillTreeEntry prerequisite : entry.prerequisites){
				int color = 0;
				LearnStates state = sk.getLearnState(entry, Minecraft.getMinecraft().thePlayer);
				if ((sk.isEntryKnown(prerequisite) && (state == LearnStates.CAN_LEARN || state == LearnStates.LEARNED)) || sk.getLearnState(prerequisite, Minecraft.getMinecraft().thePlayer) == LearnStates.DISABLED){
					continue;
				}else{
					color = 0x220000;
				}
				line2d(l + entry.x - offsetX * 2 + 16, i1 + entry.y - offsetY * 2 + 16, l + entry.x - offsetX * 2 + 16, i1 + prerequisite.y - offsetY * 2 + 16, color);
				line2d(l + entry.x - offsetX * 2 + 16, i1 + prerequisite.y - offsetY * 2 + 16, l + prerequisite.x - offsetX * 2 + 16, i1 + prerequisite.y - offsetY * 2 + 16, color);
			}
		}

		SkillTreeEntry hovered = null;

		for (SkillTreeEntry entry : skillTree){
			if (entry.registeredItem == null)
				continue;
			String name = SkillManager.instance.getSkillName(entry.registeredItem);
			if (name == null)
				name = "";

			IIcon IIcon = SpellIconManager.instance.getIcon(name);

			LearnStates state = sk.getLearnState(entry, Minecraft.getMinecraft().thePlayer);

			if (state == LearnStates.LEARNED){
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
			}else if (state == LearnStates.CAN_LEARN){
				float min = 0.2f;
				float delta = 0.45f * (1f - (Math.abs(AMGuiHelper.instance.getFastTicker() - 20f) / 20f));
				float colorShift = 0.25f;
				SkillPointTypes type = SkillTreeManager.instance.getSkillPointTypeForPart(entry.registeredItem);
				if (type == SkillPointTypes.SILVER){
					GL11.glColor3f(min + delta + colorShift,
							min + delta + colorShift,
							min + delta + colorShift);
				}else{
					GL11.glColor3f(min + delta + ((type == SkillPointTypes.RED) ? colorShift : 0f),
							min + delta + ((type == SkillPointTypes.GREEN) ? colorShift : 0f),
							min + delta + ((type == SkillPointTypes.BLUE) ? colorShift : 0f));
				}
			}else{
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
			}

			DrawConstrainedIconAtXY(IIcon, l + entry.x - offsetX * 2, i1 + entry.y - offsetY * 2, IIcondims, IIcondims, false);

			if (state == LearnStates.LOCKED){
				GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f);
				DrawConstrainedIconAtXY(AMGuiIcons.padlock, l + entry.x - offsetX * 2 + 8.5f, i1 + entry.y - offsetY * 2 + 8, 15, 15, true);
			}else if (state == LearnStates.DISABLED){
				GL11.glColor4f(1.0f, 0.3f, 0.3f, 0.8f);
				DrawConstrainedIconAtXY(AMGuiIcons.padlock, l + entry.x - offsetX * 2 + 8.5f, i1 + entry.y - offsetY * 2 + 8, 15, 15, true);
			}

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			if (lastMouseX > l && lastMouseX < l + xSize && lastMouseY > i1 && lastMouseY < i1 + ySize){
				if (lastMouseX > l + entry.x - offsetX * 2 && lastMouseX < l + entry.x - offsetX * 2 + IIcondims){
					if (lastMouseY > i1 + entry.y - offsetY * 2 && lastMouseY < i1 + entry.y - offsetY * 2 + IIcondims){
						hovered = entry;
					}
				}
			}
		}

		hoveredItem = hovered;
	}

	private void drawAffinity(){

		hoveredAffinity = null;

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		int IIconsize = 16;
		int halfIconSize = IIconsize / 2;

		int cx = l + xSize / 2 - halfIconSize;
		int cy = i1 + ySize / 2 - halfIconSize;

		int angle = -90;
		int distance = 85;
		int smallDist = 15;
		float angleOffset = 17.5f;

		AffinityData ad = AffinityData.For(Minecraft.getMinecraft().thePlayer);

		for (Affinity aff : Affinity.getOrderedAffinities()){
			IIcon IIcon = aff.representItem.getIconFromDamage(aff.representMeta);
			int newX = (int)(cx + Math.cos(Math.toRadians(angle)) * distance);
			int newY = (int)(cy + Math.sin(Math.toRadians(angle)) * distance);

			int cx1 = (int)(cx + Math.cos(Math.toRadians(angle + angleOffset)) * smallDist);
			int cx2 = (int)(cx + Math.cos(Math.toRadians(angle - angleOffset)) * smallDist);
			int cy1 = (int)(cy + Math.sin(Math.toRadians(angle + angleOffset)) * smallDist);
			int cy2 = (int)(cy + Math.sin(Math.toRadians(angle - angleOffset)) * smallDist);

			float depthDist = ((distance - smallDist) * ad.getAffinityDepth(aff)) + smallDist;

			int lx = (int)(cx + Math.cos(Math.toRadians(angle)) * depthDist);
			int ly = (int)(cy + Math.sin(Math.toRadians(angle)) * depthDist);

			int displace = (int)((Math.max(cx1, cx2) - Math.min(cx1, cx2) + Math.max(cy1, cy2) - Math.min(cy1, cy2)) / 3f);

			if (ad.getAffinityDepth(aff) > 0.01f){
				AMGuiHelper.instance.fractalLine2d(lx + halfIconSize, ly + halfIconSize, cx1 + halfIconSize, cy1 + halfIconSize, this.zLevel, aff.color, displace, 1.1f);
				AMGuiHelper.instance.fractalLine2d(lx + halfIconSize, ly + halfIconSize, cx2 + halfIconSize, cy2 + halfIconSize, this.zLevel, aff.color, displace, 1.1f);

				AMGuiHelper.instance.fractalLine2d(cx1 + halfIconSize, cy1 + halfIconSize, lx + halfIconSize, ly + halfIconSize, this.zLevel, aff.color, displace, 0.8f);
				AMGuiHelper.instance.fractalLine2d(cx2 + halfIconSize, cy2 + halfIconSize, lx + halfIconSize, ly + halfIconSize, this.zLevel, aff.color, displace, 0.8f);
			}else{
				AMGuiHelper.instance.line2d(lx + halfIconSize, ly + halfIconSize, cx1 + halfIconSize, cy1 + halfIconSize, this.zLevel, aff.color);
				AMGuiHelper.instance.line2d(lx + halfIconSize, ly + halfIconSize, cx2 + halfIconSize, cy2 + halfIconSize, this.zLevel, aff.color);
			}

			DrawIconAtXY(IIcon, "items", newX, newY, IIconsize, IIconsize, false);
			String depthString = String.format("%.2f", ad.getAffinityDepth(aff) * 100);
			fontRendererObj.drawString(depthString, newX + halfIconSize - fontRendererObj.getStringWidth(depthString) / 2, newY + IIconsize, aff.color, false);
			angle += 36;

			//if (lastMouseX > l && lastMouseX < l + xSize && lastMouseY > i1 && lastMouseY < i1 + ySize){
			if (lastMouseX > newX && lastMouseX < newX + IIconsize){
				if (lastMouseY > newY && lastMouseY < newY + IIconsize){
					hoveredAffinity = aff;
				}
			}
			//}
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException{
		super.mouseClicked(par1, par2, par3);
		if (par3 == 0){
			if (hoveredItem != null){
				SkillData sk = SkillData.For(Minecraft.getMinecraft().thePlayer);
				if (!sk.isEntryKnown(hoveredItem)){
					if (sk.getLearnState(hoveredItem, Minecraft.getMinecraft().thePlayer) == LearnStates.CAN_LEARN){
						sk.learn(hoveredItem.registeredItem);
					}
				}

			}else{
				if (this.activeTree != SkillTrees.Affinity)
					isDragging = true;
				lastMouseX = par1;
				lastMouseY = par2;
			}
		}
	}



	@Override
	protected void mouseReleased(int par1, int par2, int par3){
		super.mouseReleased(par1, par2, par3);
		if (par3 != -1){
			this.isDragging = false;
		}
	}

	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}

	public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height){
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

	private void DrawIconAtXY(IIcon IIcon, String base, float x, float y, int w, int h, boolean semitransparent){

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		if (semitransparent){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		mc.renderEngine.bindTexture(rl_items);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.addVertexWithUV(x, y + h, this.zLevel, IIcon.getMinU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + w, y + h, this.zLevel, IIcon.getMaxU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + w, y, this.zLevel, IIcon.getMaxU(), IIcon.getMinV());
		tessellator.addVertexWithUV(x, y, this.zLevel, IIcon.getMinU(), IIcon.getMinV());

		tessellator.draw();

		if (semitransparent){
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void DrawConstrainedIconAtXY(IIcon IIcon, float x, float y, int w, int h, boolean semitransparent){

		int l = (width - xSize) / 2 + 3;
		int i1 = ((height - ySize) / 2) + GuiButtonSkillTreeTab.buttonHeight;

		int wid = xSize - 10;
		int hei = ySize - 40;

		if (x + w < l || x > l + wid) return;
		if (y + h < i1 || y > i1 + hei) return;

		float wFactor = 1.0f;
		float hFactor = 1.0f;

		float minU = IIcon.getMinU();
		float minV = IIcon.getMinV();
		float maxU = IIcon.getMaxU();
		float maxV = IIcon.getMaxV();

		float deltaU = maxU - minU;
		float deltaV = maxV - minV;

		if (x < l){
			float delta = l - x;
			x += delta;
			float tempW = w - delta;
			wFactor = tempW / w;
			if (wFactor <= 0) return;
			minU += (deltaU - (deltaU * wFactor));
			w -= delta;
		}else if (x + w > l + wid){
			float delta = (x + w) - (l + wid);
			float tempW = w - delta;
			wFactor = tempW / w;
			if (wFactor <= 0) return;
			maxU -= (deltaU - (deltaU * wFactor));
			w -= delta;
		}

		if (y < i1){
			float delta = i1 - y;
			y += delta;
			float tempH = h - delta;
			hFactor = tempH / h;
			if (hFactor <= 0) return;
			minV += (deltaV - (deltaV * hFactor));
			h -= delta;
		}else if (y + h > i1 + hei){
			float delta = (y + h) - (i1 + hei);
			float tempH = h - delta;
			hFactor = tempH / h;
			if (hFactor <= 0) return;
			maxV -= (deltaV - (deltaV * hFactor));
			h -= delta;
		}

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		if (semitransparent){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		mc.renderEngine.bindTexture(rl_items);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.addVertexWithUV(x, y + h, this.zLevel, minU, maxV);
		tessellator.addVertexWithUV(x + w, y + h, this.zLevel, maxU, maxV);
		tessellator.addVertexWithUV(x + w, y, this.zLevel, maxU, minV);
		tessellator.addVertexWithUV(x, y, this.zLevel, minU, minV);

		tessellator.draw();

		if (semitransparent){
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void line2d(int src_x, int src_y, int dst_x, int dst_y, int color){
		int l = (width - xSize) / 2 + 3;
		int i1 = ((height - ySize) / 2) + GuiButtonSkillTreeTab.buttonHeight;

		int w = xSize - 5;
		int h = ySize - 40;

		if ((src_x < l && dst_x < l) || (src_x > l + w && dst_x > l + w)) return;
		if ((src_y < i1 && dst_y < i1) || (src_y > i1 + h && dst_y > i1 + h)) return;

		if (src_x < l) src_x = l;
		else if (src_x > l + w) src_x = l + w;

		if (dst_x < l) dst_x = l;
		else if (dst_x > l + w) dst_x = l + w;

		if (src_y < i1) src_y = i1;
		else if (src_y > i1 + h) src_y = i1 + h;

		if (dst_y < i1) dst_y = i1;
		else if (dst_y > i1 + h) dst_y = i1 + h;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(4f);
		GL11.glColor3f((color & 0xFF0000) >> 16, (color & 0x00FF00) >> 8, color & 0x0000FF);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(src_x, src_y, this.zLevel);
		GL11.glVertex3f(dst_x, dst_y, this.zLevel);
		GL11.glEnd();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	private void drawGradientAround(int x, int y, int width, int height){
		int pad = 3;

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		int l1 = -267386864;
		this.drawGradientRect(x - pad, y - 4, x + width + pad, y - pad, l1, l1);
		this.drawGradientRect(x - pad, y + height + pad, x + width + pad, y + height + 4, l1, l1);
		this.drawGradientRect(x - pad, y - pad, x + width + pad, y + height + pad, l1, l1);
		this.drawGradientRect(x - 4, y - pad, x - pad, y + height + pad, l1, l1);
		this.drawGradientRect(x + width + pad, y - pad, x + width + 4, y + height + pad, l1, l1);

		int i2 = 1347420415;
		int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
		this.drawGradientRect(x - pad, y - pad + 1, x - pad + 1, y + height + pad - 1, i2, j2);
		this.drawGradientRect(x + width + 2, y - pad + 1, x + width + pad, y + height + pad - 1, i2, j2);
		this.drawGradientRect(x - pad, y - pad, x + width + pad, y - pad + 1, i2, i2);
		this.drawGradientRect(x - pad, y + height + 2, x + width + pad, y + height + pad, j2, j2);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font, int color){
		if (!par1List.isEmpty()){
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = par1List.iterator();

			while (iterator.hasNext()){
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);

				if (l > k){
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (par1List.size() > 1){
				k1 += 2 + (par1List.size() - 1) * 10;
			}

			if (i1 + k > this.width){
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > this.height){
				j1 = this.height - k1 - 6;
			}

			this.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < par1List.size(); ++k2){
				String s1 = (String)par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, color);

				if (k2 == 0){
					j1 += 2;
				}

				j1 += 10;
			}

			this.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}
