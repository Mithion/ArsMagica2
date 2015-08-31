package am2.api.flickers;

import net.minecraft.world.World;
import am2.api.spell.enums.Affinity;

public interface IFlickerFunctionality {
	boolean RequiresPower();
	int PowerPerOperation();
	boolean DoOperation(World worldObj, IFlickerController controller, boolean powered);
	boolean DoOperation(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers);
	void RemoveOperator(World worldObj, IFlickerController controller, boolean powered);
	int TimeBetweenOperation(boolean powered, Affinity[] flickers);
	void RemoveOperator(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers);
	Object[] getRecipe();
}
