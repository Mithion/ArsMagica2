package am2.blocks.liquid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidEssence extends Fluid{

	public FluidEssence() {
		super("liquidEssence");
		setDensity(8);
		setViscosity(3000);
		
		FluidRegistry.registerFluid(this);
	}

}
