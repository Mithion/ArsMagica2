package am2.guis;

import am2.AMCore;
import am2.api.math.AMVector2;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.ContingencyTypes;
import am2.armor.ArmorHelper;
import am2.items.IBoundItem;
import am2.items.ItemSpellBook;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SkillManager;
import am2.spell.SpellUtils;
import am2.texture.ResourceManager;
import am2.texture.SpellIconManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class AMIngameGUI{
	private final Minecraft mc;
	private final RenderItem itemRenderer;
	private float zLevel;

	private static final short MANA_BAR_FLASH_SLOT = 4;
	private final PotionEffectDurationComparator durationComparator = new PotionEffectDurationComparator();

	private static final ResourceLocation inv_top = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("Inventory_Top.png"));
	private static final ResourceLocation mc_gui = new ResourceLocation("textures/gui/icons.png");
	private static final ResourceLocation spellbook_ui = new ResourceLocation("arsmagica2", ResourceManager.GetGuiTexturePath("spellbook_ui.png"));
	private static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
	private static final ResourceLocation items = new ResourceLocation("textures/atlas/items.png");

	public AMIngameGUI(){
		mc = Minecraft.getMinecraft();
		itemRenderer = new RenderItem();
	}

	public void renderGameOverlay(){
		ItemStack ci = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();

		boolean drawAMHud = !AMCore.config.showHudMinimally() || (ci != null && (ci.getItem() == ItemsCommonProxy.spellBook || ci.getItem() == ItemsCommonProxy.spell || ci.getItem() == ItemsCommonProxy.arcaneSpellbook || ci.getItem() instanceof IBoundItem));
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();

		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (drawAMHud)
			RenderBuffs(i, j);
		mc.renderEngine.bindTexture(items);
		if (drawAMHud)
			RenderContingency(i, j);
		if (drawAMHud)
			RenderArsMagicaGUIItems(i, j, mc.fontRenderer);
		if (drawAMHud)
			RenderAffinity(i, j);
		RenderArmorStatus(i, j, mc, mc.fontRenderer);
		if (drawAMHud)
			RenderMagicXP(i, j);

		ItemStack item = mc.thePlayer.getCurrentEquippedItem();
		if (item != null && item.getItem() instanceof ItemSpellBook){

			RenderSpellBookUI(i, j, mc.fontRenderer, mc.thePlayer.getCurrentEquippedItem());
		}

		GL11.glPopAttrib();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1, 1, 1, 1);
	}

	private void RenderArsMagicaGUIItems(int i, int j, FontRenderer fontRenderer){
		if (ExtendedProperties.For(mc.thePlayer).getMagicLevel() > 0 || mc.thePlayer.capabilities.isCreativeMode){
			RenderManaBar(i, j, fontRenderer);
		}
	}

	private void RenderSpellBookUI(int i, int j, FontRenderer fontrenderer, ItemStack bookStack){
		mc.renderEngine.bindTexture(spellbook_ui);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		AMVector2 spellbookVec = getShiftedVector(AMCore.config.getSpellBookPosition(), i, j);

		int spellUI_x = spellbookVec.iX;
		int spellUI_y = spellbookVec.iY;
		int spellUI_width = 148;
		int spellUI_height = 22;
		float activeSpellSize = 15f;

		int bookActiveSlot = ((ItemSpellBook)bookStack.getItem()).GetActiveSlot(bookStack);

		float x = spellUI_x + bookActiveSlot * 12.9f;
		float y = spellUI_y;

		this.zLevel = -5;
		drawTexturedModalRect_Classic(spellUI_x, spellUI_y, 0, 0, 106, 15, spellUI_width, spellUI_height);

		ItemStack[] activeScrolls = ((ItemSpellBook)bookStack.getItem()).getActiveScrollInventory(bookStack);

		mc.renderEngine.bindTexture(items);

		this.zLevel = 0;
		for (int n = 0; n < 8; ++n){
			float IIconX = spellUI_x + 1.5f + n * 12.9f;
			ItemStack stackItem = activeScrolls[n];
			if (stackItem == null){
				continue;
			}
			int d = 12;
			DrawIconAtXY(((ItemSpellBase)stackItem.getItem()).getIconFromDamageForRenderPass(stackItem.getItemDamage(), 0), "items", IIconX, spellUI_y + 1.5f, d, d, false);
			DrawIconAtXY(((ItemSpellBase)stackItem.getItem()).getIconFromDamageForRenderPass(stackItem.getItemDamage(), 1), "items", IIconX, spellUI_y + 1.5f, d, d, false);
		}

		mc.renderEngine.bindTexture(spellbook_ui);
		this.zLevel = 1000;
		drawTexturedModalRect_Classic(x, y, 148, 0, activeSpellSize, activeSpellSize, 20, 20);
		this.zLevel = 0;

		mc.renderEngine.bindTexture(mc_gui);
	}

	private void RenderManaBar(int i, int j, FontRenderer fontRenderer){

		int barWidth = i / 8;

		AMVector2 fatigue_hud = getShiftedVector(AMCore.config.getBurnoutHudPosition(), i, j);
		AMVector2 mana_hud = getShiftedVector(AMCore.config.getManaHudPosition(), i, j);

		float green = 0.5f;
		float blue = 1.0f;
		float red = 0.126f;

		ExtendedProperties props = ExtendedProperties.For(mc.thePlayer);

		//mana bar
		float mana = props.getCurrentMana();
		float bonusMana = props.getBonusCurrentMana();
		float maxMana = props.getMaxMana();

		float fatigueBarWidth = barWidth;
		float fatigue = props.getCurrentFatigue();
		float maxFatigue = props.getMaxFatigue();

		if (mana + bonusMana > maxMana)
			mana = maxMana;

		float progressScaled = (mana / (maxMana + 0.01f));

		if (AMCore.config.showHudBars()){
			//handle flashing of mana bar
			float flashTimer = AMGuiHelper.instance.getFlashTimer(MANA_BAR_FLASH_SLOT);
			if (flashTimer > 0){
				green = 0.0f;
				float redShift = 1.0f - red;

				float halfFlash = AMGuiHelper.instance.flashDuration / 2;

				if (flashTimer > halfFlash){
					float pct = (flashTimer - halfFlash) / halfFlash;
					red += redShift - (redShift * pct);
				}else{
					float pct = flashTimer / halfFlash;
					red += (redShift * pct);
				}
				GL11.glColor3f(red, green, blue);
			}else{
				if (bonusMana > 0)
					GL11.glColor3f(0.2f, 0.9f, 0.6f);
			}

			ItemStack curItem = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (curItem != null && (curItem.getItem() == ItemsCommonProxy.spell || curItem.getItem() == ItemsCommonProxy.spellBook || curItem.getItem() == ItemsCommonProxy.arcaneSpellbook)){
				ItemStack spellStack = curItem.getItem() == ItemsCommonProxy.spell ? curItem : ((ItemSpellBook)curItem.getItem()).GetActiveItemStack(curItem);
				if (spellStack != null){
					int[] parts = SpellUtils.instance.getShapeGroupParts(spellStack);
					int sx = mana_hud.iX - 2 * parts.length / 2;
					int sy = mana_hud.iY - 2 * parts.length / 2;
					for (int p : parts){
						IIcon icon = SpellIconManager.instance.getIcon(SkillManager.instance.getSkillName(SkillManager.instance.getSkill(p)));
						if (icon != null){
							DrawIconAtXY(icon, "items", sx, sy, false);
							sx += 3;
							sy += 3;
						}
					}
				}
			}

			DrawPartialIconAtXY(AMGuiIcons.manaLevel, progressScaled, 1, mana_hud.iX + 16, mana_hud.iY + 1f, (int)(barWidth * 0.97f), 40, false);
			DrawIconAtXY(AMGuiIcons.manaBar, "items", mana_hud.iX + 15, mana_hud.iY + 3, barWidth, 50, false);

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			progressScaled = (fatigue / (maxFatigue + 0.01f));
			DrawIconAtXY(AMGuiIcons.fatigueIcon, "items", fatigue_hud.iX + barWidth, fatigue_hud.iY, false);

			DrawPartialIconAtXY(AMGuiIcons.fatigueLevel, progressScaled, 1, fatigue_hud.iX, fatigue_hud.iY + 3f, fatigueBarWidth, 40, false);
			DrawIconAtXY(AMGuiIcons.fatigueBar, "items", fatigue_hud.iX, fatigue_hud.iY + 4, barWidth, 48, false);

			green = 0.5f;
			blue = 1.0f;
			red = 0.126f;
			//magic level
			int manaBarColor = Math.round(red * 255);
			manaBarColor = (manaBarColor << 8) + Math.round(green * 255);
			manaBarColor = (manaBarColor << 8) + Math.round(blue * 255);

			String magicLevel = (new StringBuilder()).append("").append(ExtendedProperties.For(mc.thePlayer).getMagicLevel()).toString();
			AMVector2 magicLevelPos = getShiftedVector(AMCore.config.getLevelPosition(), i, j);
			magicLevelPos.iX -= Minecraft.getMinecraft().fontRenderer.getStringWidth(magicLevel) / 2;
			fontRenderer.drawStringWithShadow(magicLevel, magicLevelPos.iX, magicLevelPos.iY, manaBarColor);

			if (flashTimer > 0){
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
			}
		}

		if (AMCore.config.getShowNumerics()){
			String manaStr = StatCollector.translateToLocal("am2.gui.mana") + ": " + (int)(mana + bonusMana) + "/" + (int)maxMana;
			String burnoutStr = StatCollector.translateToLocal("am2.gui.burnout") + ": " + (int)props.getCurrentFatigue() + "/" + (int)props.getMaxFatigue();
			AMVector2 manaNumericPos = getShiftedVector(AMCore.config.getManaNumericPosition(), i, j);
			AMVector2 burnoutNumericPos = getShiftedVector(AMCore.config.getBurnoutNumericPosition(), i, j);
			fontRenderer.drawString(manaStr, manaNumericPos.iX, manaNumericPos.iY, bonusMana > 0 ? 0xeae31c : 0x2080FF);
			fontRenderer.drawString(burnoutStr, burnoutNumericPos.iX + 25 - fontRenderer.getStringWidth(burnoutStr), burnoutNumericPos.iY, 0xFF2020);
		}
	}

	private ItemStack getSpellFromStack(ItemStack stack){
		if (stack.getItem() == ItemsCommonProxy.spell)
			return stack;
		else if (stack.getItem() == ItemsCommonProxy.spellBook || stack.getItem() == ItemsCommonProxy.arcaneSpellbook)
			return ((ItemSpellBook)stack.getItem()).GetActiveItemStack(stack);
		else
			return null;
	}

	private void RenderArmorStatus(int i, int j, Minecraft mc, FontRenderer fontRenderer){
		if (!AMCore.config.showArmorUI())
			return;

		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

		for (int slot = 0; slot < 4; ++slot){
			if (ArmorHelper.PlayerHasArmorInSlot(mc.thePlayer, 3 - slot)){

				AMVector2 position = getArmorSlotPosition(slot, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
				int blackoutTimer = AMGuiHelper.instance.getBlackoutTimer(3 - slot);
				int blackoutMaxTimer = AMGuiHelper.instance.getBlackoutTimerMax(3 - slot);
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				ItemStack armor = mc.thePlayer.inventory.armorInventory[3 - slot];
				float lineweight = 4f;
				//durability
				if (armor.isItemDamaged() && armor.getMaxDamage() > 0){
					float pct = 1 - (float)armor.getItemDamage() / (float)armor.getMaxDamage();
					AMGuiHelper.line2d(position.iX, position.iY + 10, position.iX + 10, position.iY + 10, this.zLevel + 100, lineweight, 0);

					int color = (int)(255.0f * (1 - pct)) << 16 | (int)(255.0f * pct) << 8;

					AMGuiHelper.line2d(position.iX, position.iY + 10, position.iX + (10 * pct), position.iY + 10, this.zLevel + 101, lineweight, color);
				}
				//cooldown
				if (blackoutMaxTimer > 0){
					float pct = (float)(blackoutMaxTimer - blackoutTimer) / (float)blackoutMaxTimer;
					AMGuiHelper.line2d(position.iX, position.iY + 11, position.iX + 10, position.iY + 11, this.zLevel + 100, lineweight, 0);
					AMGuiHelper.line2d(position.iX, position.iY + 11, position.iX + (10 * pct), position.iY + 11, this.zLevel + 101, lineweight, 0xFF0000);
				}else{
					AMGuiHelper.line2d(position.iX, position.iY + 11, position.iX + 10, position.iY + 11, this.zLevel + 101, lineweight, 0x0000FF);
				}

				IIcon icon = mc.thePlayer.inventory.armorInventory[3 - slot].getIconIndex();
				if (icon != null){
					AMGuiHelper.DrawIconAtXY(icon, position.iX, position.iY, this.zLevel, 10, 10, true);
				}else{
					AMGuiHelper.DrawItemAtXY(mc.thePlayer.inventory.armorInventory[3 - slot], position.iX, position.iY, this.zLevel, 0.63f);
				}
			}
		}
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

	private AMVector2 getArmorSlotPosition(int slot, int screenWidth, int screenHeight){
		switch (slot){
		case 0:
			return getShiftedVector(AMCore.config.getArmorPositionHead(), screenWidth, screenHeight);
		case 1:
			return getShiftedVector(AMCore.config.getArmorPositionChest(), screenWidth, screenHeight);
		case 2:
			return getShiftedVector(AMCore.config.getArmorPositionLegs(), screenWidth, screenHeight);
		case 3:
			return getShiftedVector(AMCore.config.getArmorPositionBoots(), screenWidth, screenHeight);
		}
		return new AMVector2(0, 0);
	}

	public void RenderAffinity(int i, int j){
		AMVector2 affinityPos = getShiftedVector(AMCore.config.getAffinityPosition(), i, j);

		int x = affinityPos.iX;
		int y = affinityPos.iY;

		AffinityData ad = AffinityData.For(Minecraft.getMinecraft().thePlayer);
		for (Affinity affinity : ad.getHighestAffinities()){
			if (affinity == null || affinity == Affinity.NONE) continue;
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			AMGuiHelper.instance.DrawIconAtXY(affinity.representItem.getIconFromDamage(affinity.representMeta), x, y, j, 12, 12, true);

			if (AMCore.config.getShowNumerics()){
				String display = String.format("%.2f%%", AffinityData.For(mc.thePlayer).getAffinityDepth(affinity) * 100);
				if (x < i / 2)
					Minecraft.getMinecraft().fontRenderer.drawString(display, x + 14, y + 2, affinity.color);
				else
					Minecraft.getMinecraft().fontRenderer.drawString(display, x - 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(display), y + 2, affinity.color);
			}
			y += 15;
		}
	}

	public void RenderContingency(int i, int j){

		AMVector2 contingencyPos = getShiftedVector(AMCore.config.getContingencyPosition(), i, j);

		IIcon icon = null;
		ContingencyTypes type = ExtendedProperties.For(Minecraft.getMinecraft().thePlayer).getContingencyType();
		switch (type){
		case DAMAGE_TAKEN:
			icon = SpellIconManager.instance.getIcon("Contingency_Damage");
			break;
		case FALL:
			icon = SpellIconManager.instance.getIcon("Contingency_Fall");
			break;
		case HEALTH_LOW:
			icon = SpellIconManager.instance.getIcon("Contingency_Health");
			break;
		case ON_FIRE:
			icon = SpellIconManager.instance.getIcon("Contingency_Fire");
			break;
		case DEATH:
			icon = SpellIconManager.instance.getIcon("Contingency_Death");
			break;
		case NONE:
		default:
			return;
		}

		DrawIconAtXY(icon, "items", contingencyPos.iX, contingencyPos.iY, 16, 16, true);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

	public void RenderBuffs(int i, int j){

		if (!AMCore.config.getShowBuffs()){
			return;
		}

		int barWidth = i / 8;

		AMVector2 posBuffStart = getShiftedVector(AMCore.config.getPositiveBuffsPosition(), i, j);
		AMVector2 negBuffStart = getShiftedVector(AMCore.config.getNegativeBuffsPosition(), i, j);

		int positive_buff_x = posBuffStart.iX;
		int positive_buff_y = posBuffStart.iY;

		int negative_buff_x = negBuffStart.iX;
		int negative_buff_y = negBuffStart.iY;
		for (PotionEffect pe : getPotionEffectsByTimeRemaining()){
			this.mc.renderEngine.bindTexture(inventory);

			int potionID = pe.getPotionID();
			if (potionID < 0 || potionID >= Potion.potionTypes.length)
				continue;

			Potion potion = Potion.potionTypes[potionID];

			if (potion == null)
				continue;

			if (potion.isBadEffect()){
				if (potion.hasStatusIcon()){
					int l = potion.getStatusIconIndex();
					if (pe.getDuration() < 100){
						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.fastFlashAlpha);
					}else if (pe.getDuration() < 200){
						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.slowFlashAlpha);
					}
					this.drawTexturedModalRect_Classic(negative_buff_x, negative_buff_y, 0 + l % 8 * 18, 198 + l / 8 * 18, 10, 10, 18, 18);
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					negative_buff_x -= 12;
					if (negative_buff_x <= negBuffStart.iX - 48){
						negative_buff_x = negBuffStart.iX;
						negative_buff_y += 12;
					}
				}
			}else{
				this.mc.renderEngine.bindTexture(inventory);
				if (potion.hasStatusIcon()){
					int l = potion.getStatusIconIndex();
					if (pe.getDuration() < 100){
						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.fastFlashAlpha);
					}else if (pe.getDuration() < 200){
						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.slowFlashAlpha);
					}
					this.drawTexturedModalRect_Classic(positive_buff_x, positive_buff_y, 0 + l % 8 * 18, 198 + l / 8 * 18, 10, 10, 18, 18);
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
					positive_buff_x += 12;
					if (positive_buff_x >= posBuffStart.iX + 48){
						positive_buff_x = posBuffStart.iX;
						positive_buff_y += 12;
					}
				}
			}
		}
	}

	public void RenderMagicXP(int i, int j){
		ExtendedProperties props = ExtendedProperties.For(Minecraft.getMinecraft().thePlayer);
		if (props.getMagicLevel() > 0){
			AMVector2 position = getShiftedVector(AMCore.config.getXPBarPosition(), i, j);
			AMVector2 dimensions = new AMVector2(182, 5);
			Minecraft.getMinecraft().renderEngine.bindTexture(mc_gui);
			GL11.glColor4f(0.5f, 0.5f, 1.0f, AMCore.config.showXPAlways() ? 1.0f : AMGuiHelper.instance.getMagicXPBarAlpha());

			//base XP bar
			drawTexturedModalRect_Classic(position.iX, position.iY, 0, 64, dimensions.iX, dimensions.iY, dimensions.iX, dimensions.iY);

			if (props.getMagicXP() > 0){
				float pctXP = props.getMagicXP() / props.getXPToNextLevel();
				if (pctXP > 1)
					pctXP = 1;
				int width = (int)((dimensions.iX + 1) * pctXP);
				drawTexturedModalRect_Classic(position.iX, position.iY, 0, 69, width, dimensions.iY, width, dimensions.iY);
			}

			if (AMCore.config.getShowNumerics() && (AMCore.config.showXPAlways() || AMGuiHelper.instance.getMagicXPBarAlpha() > 0)){
				String xpStr = StatCollector.translateToLocal("am2.gui.xp") + ": " + +(int)(props.getMagicXP() * 100) + "/" + (int)(props.getXPToNextLevel() * 100);
				AMVector2 numericPos = getShiftedVector(AMCore.config.getXPNumericPosition(), i, j);
				Minecraft.getMinecraft().fontRenderer.drawString(xpStr, numericPos.iX, numericPos.iY, 0x999999);
			}
		}
	}

	private ArrayList<PotionEffect> getPotionEffectsByTimeRemaining(){
		Iterator i = mc.thePlayer.getActivePotionEffects().iterator();
		ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();

		while (i.hasNext())
			potions.add((PotionEffect)i.next());

		Collections.sort(potions, durationComparator);
		return potions;
	}

	public void drawTexturedModalRect_Classic(int par1, int par2, int par3, int par4, int par5, int par6){
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(par1 + 0, par2 + par6, this.zLevel, (par3 + 0) * var7, (par4 + par6) * var8);
		var9.addVertexWithUV(par1 + par5, par2 + par6, this.zLevel, (par3 + par5) * var7, (par4 + par6) * var8);
		var9.addVertexWithUV(par1 + par5, par2 + 0, this.zLevel, (par3 + par5) * var7, (par4 + 0) * var8);
		var9.addVertexWithUV(par1 + 0, par2 + 0, this.zLevel, (par3 + 0) * var7, (par4 + 0) * var8);
		var9.draw();
	}

	/**
	 * Draw a section of the currently bound texture to the screen.
	 *
	 * @param dst_x      The x coordinate on the screen to draw to
	 * @param dst_y      The y coordinate on the screen to draw to
	 * @param src_x      The x coordinate on the texture to pull from
	 * @param src_y      The y coordinate on the texture to pull from
	 * @param dst_width  The width on screen to draw
	 * @param dst_height The height on screen to draw
	 * @param src_width  The width of the texture section
	 * @param src_height The height of the texture section
	 */
	public void drawTexturedModalRect_Classic(float dst_x, float dst_y, float src_x, float src_y, float dst_width, float dst_height, float src_width, float src_height){
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

	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6){
		float var9 = 0;
		float var10 = 1;
		float var11 = 0;
		float var12 = 1;

		Tessellator var8 = Tessellator.instance;
		//GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
		var8.startDrawingQuads();
		var8.setNormal(0.0F, 1.0F, 0.0F);
		var8.addVertexWithUV(par1 + 0, par2 + par6, this.zLevel, var9, var12);
		var8.addVertexWithUV(par1 + par5, par2 + par6, this.zLevel, var10, var12);
		var8.addVertexWithUV(par1 + par5, par2 + 0, this.zLevel, var10, var11);
		var8.addVertexWithUV(par1 + 0, par2 + 0, this.zLevel, var9, var11);
		var8.draw();
	}

	private void renderPortalOverlay(float par1, int par2, int par3){

	}

	private void DrawIconAtXY(IIcon icon, String base, float x, float y, boolean semitransparent){
		DrawIconAtXY(icon, base, x, y, 16, 16, semitransparent);
	}

	private void DrawIconAtXY(IIcon IIcon, String base, float x, float y, int w, int h, boolean semitransparent){

		if (IIcon == null) return;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.addVertexWithUV(x, y + h, this.zLevel, IIcon.getMinU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + w, y + h, this.zLevel, IIcon.getMaxU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + w, y, this.zLevel, IIcon.getMaxU(), IIcon.getMinV());
		tessellator.addVertexWithUV(x, y, this.zLevel, IIcon.getMinU(), IIcon.getMinV());

		tessellator.draw();
	}

	private void DrawPartialIconAtXY(IIcon IIcon, float pct_x, float pct_y, float x, float y, float w, float h, boolean semitransparent){
		if (IIcon == null) return;

		mc.renderEngine.bindTexture(items);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.addVertexWithUV(x, y + (h * pct_y), this.zLevel, IIcon.getMinU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + (w * pct_x), y + (h * pct_y), this.zLevel, IIcon.getMaxU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + (w * pct_x), y, this.zLevel, IIcon.getMaxU(), IIcon.getMinV());
		tessellator.addVertexWithUV(x, y, this.zLevel, IIcon.getMinU(), IIcon.getMinV());

		tessellator.draw();
	}

	private AMVector2 getShiftedVector(AMVector2 configVec, int screenWidth, int screenHeight){
		int x = (int)Math.round(configVec.x * screenWidth);
		int y = (int)Math.round(configVec.y * screenHeight);

		return new AMVector2(x, y);
	}

	class PotionEffectDurationComparator implements Comparator<PotionEffect>{


		public PotionEffectDurationComparator(){
		}

		@Override
		public int compare(PotionEffect o1, PotionEffect o2){
			if (o1.getDuration() < o2.getDuration()) return -1;
			else if (o1.getDuration() > o2.getDuration()) return 1;
			else return 0;
		}

	}
}
