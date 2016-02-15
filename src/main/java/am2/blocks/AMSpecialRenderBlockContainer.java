package am2.blocks;

import net.minecraft.block.material.Material;

public abstract class AMSpecialRenderBlockContainer extends AMBlockContainer{

	protected AMSpecialRenderBlockContainer(Material material){
		super(material);
	}

	@Override
	public boolean isFullCube(){
		return false;
	}

    @Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}
