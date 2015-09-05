package am2.entities.ai.selectors;

import am2.entities.EntityLightMage;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;

public class LightMageEntitySelector implements IEntitySelector{

	public static final LightMageEntitySelector instance = new LightMageEntitySelector();

	private LightMageEntitySelector(){
	}

	@Override
	public boolean isEntityApplicable(Entity entity){
		if (entity instanceof EntityCreeper || entity instanceof EntityLightMage)
			return false;
		return true;
	}

}
