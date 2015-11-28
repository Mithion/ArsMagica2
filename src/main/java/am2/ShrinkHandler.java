package am2;

import am2.api.math.AMVector2;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.utility.EntityUtilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ShrinkHandler{

	@SubscribeEvent
	public void onEntityLiving(LivingEvent event){
		if (!(event.entityLiving instanceof EntityPlayer)) return;

		if (SkillTreeManager.instance.isSkillDisabled(SkillManager.instance.getSkill("Shrink")))
			return;


		ExtendedProperties exProps = null;

		try{
			exProps = ExtendedProperties.For(event.entityLiving);
		}catch (Throwable t){
			return;
		}

		if (exProps.originalSize == null){
			exProps.originalSize = new AMVector2(event.entityLiving.width, event.entityLiving.height);
		}

		exProps.shrinkTick();

		if (exProps.getShrinkPct() > 0f){
			if (exProps.shrinkAmount == 0f || //shrink hasn't yet been applied
					exProps.getOriginalSize().x * 0.5 != event.entityLiving.width || //width has changed through other means
					exProps.getOriginalSize().y * 0.5 != event.entityLiving.height){ //height has changed through other means
				exProps.setOriginalSize(new AMVector2(event.entityLiving.width, event.entityLiving.height));
				EntityUtilities.setSize(event.entityLiving, event.entityLiving.width * 0.5f, event.entityLiving.height * 0.5f);
				exProps.shrinkAmount = 0.5f;
			}
		}else{
			if (exProps.shrinkAmount != 0f){
				AMVector2 size = ExtendedProperties.For(event.entityLiving).getOriginalSize();
				EntityUtilities.setSize(event.entityLiving, (float)(size.x), (float)(size.y));
				exProps.shrinkAmount = 0f;
				if (exProps.getIsFlipped()){
					event.entityLiving.moveEntity(0, -1, 0);
				}
			}
		}
		// always update offset
		event.entityLiving.yOffset = (float)(exProps.getOriginalSize().y) * 0.8f * (1f - 0.5f * exProps.getShrinkPct());
	}
}
