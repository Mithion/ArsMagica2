package am2.bosses.ai;

import am2.bosses.EntityWaterGuardian;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;

public class WaterGuardianTargetSelector implements IEntitySelector{

	@Override
	public boolean isEntityApplicable(Entity entity){
		return !(entity instanceof EntityWaterGuardian);
	}

}
