package am2.blocks;

import net.minecraft.block.material.Material;

public abstract class AMSpecialRenderBlock extends AMBlock{

	protected AMSpecialRenderBlock(Material material){
		super(material);
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
