package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;

public abstract class AMSpecialRenderBlock extends AMBlock{

	protected AMSpecialRenderBlock(Material material){
		super(material);
	}
	
	@Override
	public boolean isNormalCube() {
		return false;
	}
	
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override
	public int getRenderType(){
		return -1;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}
