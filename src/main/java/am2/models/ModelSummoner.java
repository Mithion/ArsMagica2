package am2.models;

import am2.blocks.tileentities.TileEntitySummoner;
import am2.texture.ResourceManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class ModelSummoner extends ModelBase{
	//fields
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape4;
	ModelRenderer Shape3;
	ModelRenderer Shape6;
	ModelRenderer Shape5;
	ModelRenderer Shape7;
	ModelRenderer Shape10;
	ModelRenderer Shape8;
	ModelRenderer Shape9;
	ModelRenderer Shape11;

	private IModelCustom crystalmodel;

	public ModelSummoner(){
		textureWidth = 64;
		textureHeight = 32;

		Shape1 = new ModelRenderer(this, 0, 19);
		Shape1.addBox(-4.5F, 0F, -4.5F, 9, 4, 9);
		Shape1.setRotationPoint(0F, 20F, 0F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 37, 17);
		Shape2.addBox(-2.5F, 0F, -2.5F, 5, 10, 5);
		Shape2.setRotationPoint(0F, 10F, 0F);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 52, 12);
		Shape4.addBox(0F, 0F, 0F, 5, 1, 1);
		Shape4.setRotationPoint(-2.5F, 14F, 2.5F);
		Shape4.setTextureSize(64, 32);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 52, 9);
		Shape3.addBox(0F, 0F, 0F, 5, 1, 1);
		Shape3.setRotationPoint(-2.5F, 14F, -3.5F);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape6 = new ModelRenderer(this, 48, 0);
		Shape6.addBox(0F, 0F, 0F, 1, 1, 7);
		Shape6.setRotationPoint(-3.5F, 14F, -3.5F);
		Shape6.setTextureSize(64, 32);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 31, 0);
		Shape5.addBox(0F, 0F, 0F, 1, 1, 7);
		Shape5.setRotationPoint(2.5F, 14F, -3.5F);
		Shape5.setTextureSize(64, 32);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 0, 0);
		Shape7.addBox(-2.5F, 0F, -2.5F, 5, 1, 5);
		Shape7.setRotationPoint(0F, 5F, 0F);
		Shape7.setTextureSize(64, 32);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		Shape10 = new ModelRenderer(this, 5, 8);
		Shape10.addBox(0F, 0F, 0F, 1, 4, 1);
		Shape10.setRotationPoint(1.5F, 6F, -2.5F);
		Shape10.setTextureSize(64, 32);
		Shape10.mirror = true;
		setRotation(Shape10, 0F, 0F, 0F);
		Shape8 = new ModelRenderer(this, 15, 8);
		Shape8.addBox(0F, 0F, 0F, 1, 4, 1);
		Shape8.setRotationPoint(1.5F, 6F, 1.5F);
		Shape8.setTextureSize(64, 32);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, 0F);
		Shape9 = new ModelRenderer(this, 10, 8);
		Shape9.addBox(0F, 0F, 0F, 1, 4, 1);
		Shape9.setRotationPoint(-2.5F, 6F, 1.5F);
		Shape9.setTextureSize(64, 32);
		Shape9.mirror = true;
		setRotation(Shape9, 0F, 0F, 0F);
		Shape11 = new ModelRenderer(this, 0, 8);
		Shape11.addBox(0F, 0F, 0F, 1, 4, 1);
		Shape11.setRotationPoint(-2.5F, 6F, -2.5F);
		Shape11.setTextureSize(64, 32);
		Shape11.mirror = true;
		setRotation(Shape11, 0F, 0F, 0F);

		ResourceLocation path = ResourceManager.getOBJFilePath("crystal.obj");
		crystalmodel = AdvancedModelLoader.loadModel(path);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape4.render(f5);
		Shape3.render(f5);
		Shape6.render(f5);
		Shape5.render(f5);
		Shape7.render(f5);
		Shape10.render(f5);
		Shape8.render(f5);
		Shape9.render(f5);
		Shape11.render(f5);
	}

	public void renderModel(float f5){
		Shape1.render(f5);
		Shape2.render(f5);
		Shape4.render(f5);
		Shape3.render(f5);
		Shape6.render(f5);
		Shape5.render(f5);
		Shape7.render(f5);
		Shape10.render(f5);
		Shape8.render(f5);
		Shape9.render(f5);
		Shape11.render(f5);
	}

	public void renderCrystal(TileEntitySummoner summoner, float f5){
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslatef(0f, 0.6f, 0f);
		GL11.glScalef(2f, -2f, 2f);
		if (summoner.hasSummon()){
			GL11.glColor3f(0.14902f, 0.48627f, 0.70196f);
		}else if (summoner.canSummon()){
			GL11.glColor3f(0, 1, 0);
		}else{
			GL11.glColor3f(1, 0, 0);
		}
		try{
			crystalmodel.renderAll();
		}catch (Throwable t){
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
