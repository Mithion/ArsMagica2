package am2.blocks.renderers;

import am2.api.math.AMVector3;
import am2.blocks.tileentities.TileEntityArcaneReconstructor;
import am2.bosses.renderers.RenderItemNoBob;
import am2.guis.AMGuiHelper;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class ArcaneReconstructorRenderer extends TileEntitySpecialRenderer{

	private final IModelCustom model;
	private ItemStack renderStack;

	private final ResourceLocation rLoc;

	RenderItem renderItem;

	public ArcaneReconstructorRenderer(){
		model = AdvancedModelLoader.loadModel(ResourceManager.getOBJFilePath("Arcane_Reconstructor.obj"));

		rLoc = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("Arcane_Reconstructor.png"));

		renderStack = new ItemStack(Items.wooden_shovel);

		renderItem = new RenderItemNoBob();
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8){
		doRender((TileEntityArcaneReconstructor)var1, var2, var4, var6, var8);
	}

	private void doRender(TileEntityArcaneReconstructor te, double d, double d1, double d2, float f){
		float floatingOffset = te.getOffset();//(float) (Math.sin(te.getOffset()) * (Math.PI/ 180) * 1.4f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if ((d != 0 || d1 != 0 || d2 != 0) && te.shouldRenderItemStack()){
			renderStack = te.getCurrentItem();
			if (renderStack != null)
				RenderItemAtCoords(renderStack, d + 0.5f, d1 + 0.85f, d2 + 0.5f, f);
		}

		bindTexture(rLoc);

		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)d + 0.5f, (float)d1 + 0.22f, (float)d2 + 0.5f);

		try{
			model.renderPart("Main");
		}catch (Throwable t){

		}
		GL11.glTranslatef(0, 0.22f + floatingOffset, 0);
		RenderRotatedModelGroup("Ring03", te.getInnerRingRotation());
		RenderRotatedModelGroup("Ring01", te.getMiddleRingRotation());
		RenderRotatedModelGroup("Ring02", te.getOuterRingRotation());

		if (te.shouldRenderRotateOffset()){

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
			RenderRotatedModelGroup("Ring03", te.getInnerRingRotation().copy().sub(te.getInnerRingRotationSpeed().copy().scale(te.getRotateOffset())));
			RenderRotatedModelGroup("Ring01", te.getMiddleRingRotation().copy().sub(te.getMiddleRingRotationSpeed().copy().scale(te.getRotateOffset())));
			RenderRotatedModelGroup("Ring02", te.getOuterRingRotation().copy().sub(te.getOuterRingRotationSpeed().copy().scale(te.getRotateOffset())));

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.15f);

			RenderRotatedModelGroup("Ring03", te.getInnerRingRotation().copy().sub(te.getInnerRingRotationSpeed().copy().scale(te.getRotateOffset() * 2)));
			RenderRotatedModelGroup("Ring01", te.getMiddleRingRotation().copy().sub(te.getMiddleRingRotationSpeed().copy().scale(te.getRotateOffset() * 2)));
			RenderRotatedModelGroup("Ring02", te.getOuterRingRotation().copy().sub(te.getOuterRingRotationSpeed().copy().scale(te.getRotateOffset() * 2)));
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix(); //end

		//GL11.glDisable(GL11.GL_BLEND);
	}

	private void RenderRotatedModelGroup(String groupName, AMVector3 rotation){
		GL11.glPushMatrix();

		GL11.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(rotation.y, 1.0f, 1.0f, 0.0f);
		GL11.glRotatef(rotation.z, 1.0f, 0.0f, 1.0f);
		try{
			model.renderPart(groupName);
		}catch (Throwable t){

		}
		GL11.glPopMatrix();
	}

	private void RenderItemAtCoords(ItemStack item, double x, double y, double z, float partialTick){
		item.stackSize = 1;
		AMGuiHelper.instance.dummyItem.setEntityItemStack(item);
		renderItem.doRender(AMGuiHelper.instance.dummyItem, x, y, z, AMGuiHelper.instance.dummyItem.rotationYaw, partialTick);
	}
}
