package am2.multiblock;

import am2.api.blocks.MultiblockStructureDefinition;


public interface IMultiblockStructureController{
	/**
	 * Returns a completely initialized multiblock structure definition.
	 */
	public MultiblockStructureDefinition getDefinition();
}
