package am2.blocks.renderers;

import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityEverstone;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEverstone extends TileEntitySpecialRenderer{

	private final ResourceLocation block_atlas;
	private final RenderBlocks itemRenderBlocks = new RenderBlocks();

	public RenderEverstone(){
		block_atlas = new ResourceLocation("textures/atlas/blocks.png");
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f){
		if (tileentity instanceof TileEntityEverstone){
			renderEverstoneAt((TileEntityEverstone)tileentity, d0, d1, d2, f);
		}
	}

	private void renderEverstoneAt(TileEntityEverstone entity, double d0, double d1, double d2, float f){
		GL11.glPushMatrix();

		//GL11.glTranslated(d0, d1, d2);
		Minecraft.getMinecraft().renderEngine.bindTexture(block_atlas);

		if (entity.isSolid()){

			Block block = entity.getFacade();
			itemRenderBlocks.blockAccess = entity.getWorldObj();


			if (block == null) block = BlocksCommonProxy.everstone;

			renderBlock(BlocksCommonProxy.everstone, entity.xCoord + d0, entity.yCoord + d1, entity.zCoord + d2, block, entity);
		}
		GL11.glPopMatrix();
	}

	private void renderBlock(Block block, double x, double y, double z, Block fBlock, TileEntityEverstone te){
		itemRenderBlocks.renderFaceYPos(block, x, y, z, fBlock.getIcon(1, te.getFacadeMeta()));
		itemRenderBlocks.renderFaceYNeg(block, x, y, z, fBlock.getIcon(0, te.getFacadeMeta()));

		itemRenderBlocks.renderFaceXPos(block, x, y, z, fBlock.getIcon(2, te.getFacadeMeta()));
		itemRenderBlocks.renderFaceXNeg(block, x, y, z, fBlock.getIcon(3, te.getFacadeMeta()));

		itemRenderBlocks.renderFaceZPos(block, x, y, z, fBlock.getIcon(4, te.getFacadeMeta()));
		itemRenderBlocks.renderFaceZNeg(block, x, y, z, fBlock.getIcon(5, te.getFacadeMeta()));
	}
}
