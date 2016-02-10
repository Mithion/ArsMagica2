package am2.blocks.liquid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidEssence extends Fluid{

	public FluidEssence(){
		super("liquidEssence", new ResourceLocation("arsmagica2:textures/blocks/liquidEssenceStill"), new ResourceLocation("arsmagica2:textures/blocks/liquidEssenceFlowing"));
		setDensity(8);
		setViscosity(3000);

		FluidRegistry.registerFluid(this);
	}

}
