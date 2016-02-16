package am2.entities.ai.selectors;

import am2.entities.EntityDarkMage;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;

import javax.annotation.Nullable;

public class DarkMageEntitySelector implements Predicate{

	public static final DarkMageEntitySelector instance = new DarkMageEntitySelector();

	private DarkMageEntitySelector(){
	}

    @Override
    public boolean apply(@Nullable Object entity) { // may work, not sure
        return (entity instanceof EntityDarkMage || (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD));
    }
}
