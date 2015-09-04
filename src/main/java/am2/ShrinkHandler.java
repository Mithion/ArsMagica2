package am2;

import am2.api.math.AMVector2;
import am2.playerextensions.ExtendedProperties;
import am2.utility.EntityUtilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import scala.collection.mutable.HashMap;

public class ShrinkHandler{

	private HashMap<Integer, Float> currentSizes = new HashMap<Integer, Float>();
	private HashMap<Integer, Float> sizeModifiers = new HashMap<Integer, Float>();

	@SubscribeEvent
	public void onEntityLiving(LivingEvent event){
		ExtendedProperties exProps = null;

		if (!(event.entityLiving instanceof EntityPlayer)) return;
		EntityLivingBase ent = event.entityLiving;

		try{
			exProps = ExtendedProperties.For(event.entityLiving);
		}catch (Throwable t){
			return;
		}

		if (exProps.originalSize == null){
			exProps.originalSize = new AMVector2(event.entityLiving.width, event.entityLiving.height);
		}

		float shrinkPct = 1 - (0.5f * exProps.getShrinkPct());

		float calculatedSizeW = (float)(exProps.originalSize.x * shrinkPct);
		float calculatedSizeH = (float)(exProps.originalSize.y * shrinkPct);

		exProps.shrinkTick();

		if (exProps.getShrinkPct() > 0f){
			if (exProps.shrinkAmount == 0f || //shrink hasn't yet been applied
					exProps.getOriginalSize().x * 0.5 != ent.width || //width has changed through other means
					exProps.getOriginalSize().y * 0.5 != ent.height){ //height has changed through other means
				exProps.setOriginalSize(new AMVector2(ent.width, ent.height));
				EntityUtilities.setSize(ent, ent.width * 0.5f, ent.height * 0.5f);
				exProps.shrinkAmount = 0.5f;
			}
		}else{
			if (exProps.shrinkAmount != 0f){
				AMVector2 size = ExtendedProperties.For(ent).getOriginalSize();
				EntityUtilities.setSize(ent, (float)(size.x), (float)(size.y));
				exProps.shrinkAmount = 0f;
				if (exProps.getIsFlipped()){
					ent.moveEntity(0, -1, 0);
				}
			}
		}
	}
}
