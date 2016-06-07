package am2.blocks.renderers;

import am2.AMCore;
import am2.blocks.BlockInlay;
import am2.blocks.BlocksClientProxy;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityEverstone;
import am2.blocks.tileentities.TileEntityManaBattery;
import am2.guis.GuiBlockAccess;
import am2.items.ItemsCommonProxy;
import am2.models.ModelCandle;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class SimpleBlockRenderHandler implements ISimpleBlockRenderingHandler{

	GuiBlockAccess blockAccess;
	ModelCandle modelCandle;

	public SimpleBlockRenderHandler(){
		blockAccess = new GuiBlockAccess();
		modelCandle = new ModelCandle();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer){
		Tessellator tessellator = Tessellator.instance;
		boolean isInlay = block instanceof BlockInlay;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		if (isInlay)
			GL11.glTranslatef(0, 0.5f, 0);

		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}

		if (!isInlay){
			if (block == BlocksCommonProxy.craftingAltar){
				renderStandardBlock(block, 0, renderer);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				renderStandardBlock(block, 1, renderer);
				GL11.glDisable(GL11.GL_BLEND);
			}else{
				renderStandardBlock(block, metadata, renderer);
			}
		}
	}

	private void renderStandardBlock(Block block, int metadata, RenderBlocks renderer){
		Tessellator tessellator = Tessellator.instance;
		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}
		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}
		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}
		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}
		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}
		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		try{
			tessellator.draw();
		}catch (Throwable t){
		}
	}

	private void renderWizardChalk(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		GL11.glColor4f(1, 1, 1, 1);

		int meta = world.getBlockMetadata(x, y, z);
		//Tessellator.instance.setColorOpaque_I(0xFFFFFF);

		Tessellator tessellator = Tessellator.instance;
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.setColorOpaque_I(0xFFFFFF);
		tessellator.setBrightness(15728864);
		//renderer.renderFaceYPos(block, x, y, z, BlocksCommonProxy.wizardChalk.getIcon(1, meta));

		IIcon par8Icon = BlocksCommonProxy.wizardChalk.getIcon(1, meta);

		double d3 = par8Icon.getInterpolatedU(renderer.renderMinX * 16.0D);
		double d4 = par8Icon.getInterpolatedU(renderer.renderMaxX * 16.0D);
		double d5 = par8Icon.getInterpolatedV(renderer.renderMinZ * 16.0D);
		double d6 = par8Icon.getInterpolatedV(renderer.renderMaxZ * 16.0D);
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		double d11 = x + renderer.renderMinX;
		double d12 = x + renderer.renderMaxX;
		double d13 = y + renderer.renderMaxY;
		double d14 = z + renderer.renderMinZ;
		double d15 = z + renderer.renderMaxZ;

		tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
		tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
		tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
	}

	private void renderCraftingAltar(Block block, Block mimic, int x, int y, int z, int metadata, RenderBlocks renderer){
		IIcon icon = mimic.getIcon(1, metadata);
		if (icon == null)
			icon = block.getIcon(1, 0);
		renderer.renderFaceYPos(block, x, y, z, icon);

		icon = mimic.getIcon(0, metadata);
		if (icon == null)
			icon = block.getIcon(0, 0);
		renderer.renderFaceYNeg(block, x, y, z, icon);

		icon = mimic.getIcon(2, metadata);
		if (icon == null)
			icon = block.getIcon(2, 0);
		renderer.renderFaceXPos(block, x, y, z, icon);

		icon = mimic.getIcon(3, metadata);
		if (icon == null)
			icon = block.getIcon(3, 0);
		renderer.renderFaceXNeg(block, x, y, z, icon);

		icon = mimic.getIcon(4, metadata);
		if (icon == null)
			icon = block.getIcon(4, 0);
		renderer.renderFaceZPos(block, x, y, z, icon);

		icon = mimic.getIcon(5, metadata);
		if (icon == null)
			icon = block.getIcon(5, 0);
		renderer.renderFaceZNeg(block, x, y, z, icon);

		renderer.renderFaceYPos(block, x, y, z, block.getIcon(1, 1));
		renderer.renderFaceYNeg(block, x, y, z, block.getIcon(0, 1));

		renderer.renderFaceXPos(block, x, y, z, block.getIcon(2, 1));
		renderer.renderFaceXNeg(block, x, y, z, block.getIcon(3, 1));

		renderer.renderFaceZPos(block, x, y, z, block.getIcon(4, 1));
		renderer.renderFaceZNeg(block, x, y, z, block.getIcon(5, 1));
	}

	private void renderBlock(Block block, int meta, RenderBlocks renderer){

		if (meta < 0) meta = 0;
		if (meta > 15) meta = 15;

		GL11.glPushMatrix();
		this.blockAccess.setFakeBlockAndMeta(block, meta);
		renderer.renderAllFaces = true;

		if (block.getRenderType() == BlocksCommonProxy.blockRenderID){
			renderer.useInventoryTint = false;
			RenderInlayBlock(blockAccess, 0, 0, 0, block, 0, renderer);
		}else if (block == BlocksCommonProxy.everstone){
			renderer.useInventoryTint = false;
			renderEverstoneBlock(blockAccess, 0, 0, 0, block, 0, renderer);
		}

		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		if (block == BlocksCommonProxy.wizardChalk){
			renderWizardChalk(world, x, y, z, block, modelId, renderer);
			return true;
		}else if (block instanceof BlockInlay){
			RenderInlayBlock(world, x, y, z, block, modelId, renderer);
			return true;
		}else if (block == BlocksCommonProxy.everstone){
			renderEverstoneBlock(world, x, y, z, block, modelId, renderer);
			return true;
		}else if (block == BlocksCommonProxy.craftingAltar){
			Block mimic = BlocksCommonProxy.craftingAltar.getAltarMimicBlock(world, x, y, z);
			renderCraftingAltar(block, mimic, x, y, z, world.getBlockMetadata(x, y, z), renderer);
		}else if (block == BlocksCommonProxy.manaBattery){
			renderManaBattery(world, x, y, z, block, modelId, renderer);
		}else if (block == BlocksCommonProxy.brokenLinkBlock){
			RenderBrokenPowerLink(renderer, x, y, z);
			return true;
		}
		return false;
	}

	private void RenderBrokenPowerLink(RenderBlocks renderer, int x, int y, int z){
		EntityPlayer player = AMCore.proxy.getLocalPlayer();
		if ((x == 0 && y == 0 && z == 0) || (player != null && player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles)){
			Block block = BlocksCommonProxy.brokenLinkBlock;
			renderer.overrideBlockTexture = block.getIcon(0, 0);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.overrideBlockTexture = null;
		}
	}

	private void RenderInlayBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		int meta = world != null ? world.getBlockMetadata(x, y, z) : 0;
		IIcon renderIcon = block.getIcon(0, meta);

		renderer.uvRotateTop = 0;
		renderer.uvRotateBottom = 0;

		switch (meta){
		case 0:
			break;
		case 1:
			renderer.uvRotateTop = 2;
			renderer.uvRotateBottom = 2;
			break;
		case 6:

			break;
		case 7:
			renderer.uvRotateTop = 1;
			renderer.uvRotateBottom = 2;
			break;
		case 8:
			renderer.uvRotateTop = 3;
			renderer.uvRotateBottom = 3;
			break;
		case 9:
			renderer.uvRotateTop = 2;
			renderer.uvRotateBottom = 1;
			break;
		}

		renderer.overrideBlockTexture = renderIcon;
		renderer.renderStandardBlock(block, x, y, z);
		renderer.overrideBlockTexture = null;

		renderer.uvRotateTop = 0;
		renderer.uvRotateBottom = 0;
	}

	private void renderEverstoneBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){

		if (world == null)
			return;
		if (block == null)
			return;

		GL11.glColor4f(1, 1, 1, 1);

		TileEntityEverstone te = (TileEntityEverstone)Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
		if (te != null){
			Block fBlock = te.getFacade();
			if (fBlock != null){
				if (te.isSolid()){
					IBlockAccess old = renderer.blockAccess;
					renderer.blockAccess = this.blockAccess;
					this.blockAccess.setControllingTileEntity(te);
					this.blockAccess.setFakeBlockAndMeta(te.getFacade(), te.getFacadeMeta());
					this.blockAccess.setOuterBlockAccess(world);
					this.blockAccess.setOverrideCoords(x, y, z);
					renderer.renderStandardBlock(fBlock, x, y, z);
					this.blockAccess.setOuterBlockAccess(null);
					renderer.blockAccess = old;
				}else{
					Tessellator.instance.setColorRGBA(te.getFadeStrength(), te.getFadeStrength(), te.getFadeStrength(), te.getFadeStrength());

					renderer.renderFaceYPos(block, x, y, z, fBlock.getIcon(1, te.getFacadeMeta()));
					renderer.renderFaceYNeg(block, x, y, z, fBlock.getIcon(0, te.getFacadeMeta()));

					renderer.renderFaceXPos(block, x, y, z, fBlock.getIcon(2, te.getFacadeMeta()));
					renderer.renderFaceXNeg(block, x, y, z, fBlock.getIcon(3, te.getFacadeMeta()));

					renderer.renderFaceZPos(block, x, y, z, fBlock.getIcon(4, te.getFacadeMeta()));
					renderer.renderFaceZNeg(block, x, y, z, fBlock.getIcon(5, te.getFacadeMeta()));
				}
			}else{
				renderer.renderStandardBlock(block, x, y, z);
			}
		}
	}

	private void renderManaBattery(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		GL11.glColor4f(1, 1, 1, 1);

		TileEntityManaBattery te = (TileEntityManaBattery)world.getTileEntity(x, y, z);
		if (te != null){

			IBlockAccess old = renderer.blockAccess;
			renderer.blockAccess = this.blockAccess;
			this.blockAccess.setControllingTileEntity(te);
			this.blockAccess.setFakeBlockAndMeta(BlocksCommonProxy.manaBattery, te.getPowerType().ID());
			this.blockAccess.setOuterBlockAccess(world);
			this.blockAccess.setOverrideCoords(x, y, z);
			renderer.renderStandardBlock(BlocksCommonProxy.manaBattery, x, y, z);
			this.blockAccess.setOuterBlockAccess(null);
			renderer.blockAccess = old;

			Tessellator.instance.setColorOpaque_I(0xFFFFFF);

			renderer.renderFaceYPos(block, x, y, z, BlocksCommonProxy.manaBattery.getIcon(1, 15));
			renderer.renderFaceYNeg(block, x, y, z, BlocksCommonProxy.manaBattery.getIcon(0, 15));

			renderer.renderFaceXPos(block, x, y, z, BlocksCommonProxy.manaBattery.getIcon(2, 15));
			renderer.renderFaceXNeg(block, x, y, z, BlocksCommonProxy.manaBattery.getIcon(3, 15));

			renderer.renderFaceZPos(block, x, y, z, BlocksCommonProxy.manaBattery.getIcon(4, 15));
			renderer.renderFaceZNeg(block, x, y, z, BlocksCommonProxy.manaBattery.getIcon(5, 15));

		}
	}

	@Override
	public boolean shouldRender3DInInventory(int arg0){
		return true;
	}

	@Override
	public int getRenderId(){
		return BlocksClientProxy.blockRenderID;
	}

}
