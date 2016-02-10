package am2.blocks.renderers;

import am2.blocks.BlockEssenceConduit;
import am2.blocks.BlocksClientProxy;
import am2.blocks.BlocksCommonProxy;
import am2.guis.GuiBlockAccess;
import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TechneBlockRenderHandler implements ISimpleBlockRenderingHandler{

	private final RenderBlocks inventoryRenderBlocks = new RenderBlocks(new GuiBlockAccess());
	private final RenderBlocks renderBlocks = new RenderBlocks();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer){
		if (block instanceof BlockEssenceConduit){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.essenceConduitInventoryRender, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.occulus){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.occulusInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.obelisk){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.obeliskInventoryRender, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.celestialPrism){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.celestialPrismInventoryRender, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.blackAurem){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.blackAuremInventoryRender, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.calefactor){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.calefactorInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.keystoneRecepticle){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.keystoneRecepticleInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.astralBarrier){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.astralBarrierInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.seerStone){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.seerStoneInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.keystoneChest){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.keystoneChestInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.blockLectern){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.blockLecternInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.blockArcaneReconstructor){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.blockReconstructorInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.inscriptionTable){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.inscriptionTableInventoryRenderer, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == BlocksCommonProxy.summoner){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.summonerInventoryRenderer, 0, 0, 0, 0);
		}else if (block == BlocksCommonProxy.magiciansWorkbench){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.magiciansWorkbenchInventoryRenderer, 0, 0, 0, 0);
		}else if (block == BlocksCommonProxy.crystalMarker){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.crystalMarkerInventoryRenderer, 0, 0, 0, metadata);
		}else if (block == BlocksCommonProxy.elementalAttuner){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.elementalAttunerInventoryRenderer, 0, 0, 0, 0);
		}else if (block == BlocksCommonProxy.candle){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.candleInventoryRenderer, 0, 0, 0, 0);
		}else if (block == BlocksCommonProxy.otherworldAura){
			TileEntityRendererDispatcher.instance.renderTileEntityAt(BlocksClientProxy.otherworldAuraInventoryRenderer, 0, 0, 0, 0);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		return false;
	}

	@Override
	public int getRenderId(){
		return BlocksClientProxy.blockRenderID;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId){
		return true;
	}
}
