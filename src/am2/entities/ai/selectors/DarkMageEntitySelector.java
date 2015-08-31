package am2.entities.ai.selectors;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import am2.entities.EntityDarkMage;

public class DarkMageEntitySelector implements IEntitySelector{

	public static final DarkMageEntitySelector instance = new DarkMageEntitySelector();

	private DarkMageEntitySelector(){ }
	
	@Override
	public boolean isEntityApplicable(Entity entity) {
		if (entity instanceof EntityDarkMage || (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD))
			return false;
		return true;
	}
	
}
