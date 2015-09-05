package am2.entities.ai.selectors;

import am2.utility.EntityUtilities;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class SummonEntitySelector implements IEntitySelector{

	public static final SummonEntitySelector instance = new SummonEntitySelector();

	private SummonEntitySelector(){
	}

	@Override
	public boolean isEntityApplicable(Entity entity){
		if (entity instanceof EntityLivingBase){
			if (EntityUtilities.isSummon((EntityLivingBase)entity))
				return false;
			return true;
		}
		return false;
	}

}
