package am2.blocks;

import net.minecraft.block.material.Material;

public abstract class AMSpecialRenderPoweredBlock extends PoweredBlock{

	public AMSpecialRenderPoweredBlock(Material material) {
		super(material);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
