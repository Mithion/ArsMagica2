package am2.blocks.liquid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockLiquidEssence extends BlockFluidClassic{

	public static final Fluid liquidEssenceFluid = new FluidEssence();
	public static final Material liquidEssenceMaterial = new MaterialLiquid(MapColor.iceColor);

	public BlockLiquidEssence(){
		super(liquidEssenceFluid, liquidEssenceMaterial);
	}

	@Override
	public int getLightValue(){
		return 9;
	}

}
