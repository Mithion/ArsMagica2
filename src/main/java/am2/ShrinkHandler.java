package am2;

import am2.api.math.AMVector2;
import am2.buffs.BuffList;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.utility.EntityUtilities;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ShrinkHandler{

	@SubscribeEvent
	public void onEntityLiving(LivingEvent event){
		if (!(event.entityLiving instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.entityLiving;

		if (SkillTreeManager.instance.isSkillDisabled(SkillManager.instance.getSkill("Shrink")))
			return;


		ExtendedProperties exProps = null;

		try{
			exProps = ExtendedProperties.For(player);
		}catch (Throwable t){
			return;
		}

		if (exProps.originalSize == null){
			exProps.originalSize = new AMVector2(player.width, player.height);
		}

		boolean shrunk = exProps.getIsShrunk();

		if (!player.worldObj.isRemote && shrunk && !player.isPotionActive(BuffList.shrink)){
			exProps.setIsShrunk(false);
			shrunk = false;
			player.yOffset = (float)exProps.getOriginalSize().y * 0.9f;
		}else if (!player.worldObj.isRemote && !shrunk && player.isPotionActive(BuffList.shrink)){
			exProps.setIsShrunk(true);
			shrunk = true;
			player.yOffset = 0.0F;
		}

		float shrinkPct = exProps.getShrinkPct();
		if (shrunk && shrinkPct < 1f){
			shrinkPct = Math.min(1f, shrinkPct + 0.005f);
		}else if (!shrunk && shrinkPct > 0f){
			shrinkPct = Math.max(0f, shrinkPct - 0.005f);
		}
		exProps.setShrinkPct(shrinkPct);

		if (exProps.getShrinkPct() > 0f){
			if (exProps.shrinkAmount == 0f || //shrink hasn't yet been applied
					exProps.getOriginalSize().x * 0.5 != player.width || //width has changed through other means
					exProps.getOriginalSize().y * 0.5 != player.height){ //height has changed through other means
				exProps.setOriginalSize(new AMVector2(player.width, player.height));
				exProps.shrinkAmount = 0.5f;
				EntityUtilities.setSize(player, player.width * exProps.shrinkAmount, player.height * exProps.shrinkAmount);
				player.eyeHeight = player.getDefaultEyeHeight() * exProps.shrinkAmount;
				player.yOffset = 0.0f;
			}
		}else{
			if (exProps.shrinkAmount != 0f){
				AMVector2 size = ExtendedProperties.For(player).getOriginalSize();
				EntityUtilities.setSize(player, (float)(size.x), (float)(size.y));
				exProps.shrinkAmount = 0f;
				player.eyeHeight = player.getDefaultEyeHeight();
				player.yOffset = 0.0f;
				if (exProps.getIsFlipped()){
					event.entityLiving.moveEntity(0, -1, 0);
				}
			}
		}

		// update Y offset
		if (player.worldObj.isRemote && exProps.getPrevShrinkPct() != exProps.getShrinkPct()){
			// Vanilla player is 1.8f height with 1.62f yOffset => 0.9f
			player.yOffset = (float)exProps.getOriginalSize().y * 0.9f * (1f - 0.5f * exProps.getShrinkPct());
		}
	}
}
