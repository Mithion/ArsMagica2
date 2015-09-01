package am2.entities.ai.selectors;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import am2.utility.EntityUtilities;

public class SummonEntitySelector implements IEntitySelector{

	public static final SummonEntitySelector instance = new SummonEntitySelector();

	private SummonEntitySelector(){ }
	
	@Override
	public boolean isEntityApplicable(Entity entity) {
		if (entity instanceof EntityLivingBase){
			if (EntityUtilities.isSummon((EntityLivingBase) entity))
				return false;
			return true;
		}
		return false;
	}
	
}
