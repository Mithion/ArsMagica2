package am2.blocks;

import net.minecraft.block.material.Material;

public abstract class AMSpecialRenderBlockContainer extends AMBlockContainer{

	protected AMSpecialRenderBlockContainer(Material material){
		super(material);
	}

	@Override
	public boolean isNormalCube(){
		return false;
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
