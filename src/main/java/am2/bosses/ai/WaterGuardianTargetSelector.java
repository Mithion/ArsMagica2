package am2.bosses.ai;

import am2.bosses.EntityWaterGuardian;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public class WaterGuardianTargetSelector implements Predicate{

    @Override
    public boolean apply(@Nullable Object input) {
        return !((Entity) input instanceof EntityWaterGuardian);
    }
}
