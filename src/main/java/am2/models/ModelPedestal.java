package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPedestal extends ModelBase
{
	//fields
	ModelRenderer Shape6;
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape5;

	public ModelPedestal()
	{
		textureWidth = 64;
		textureHeight = 32;

		Shape6 = new ModelRenderer(this, 32, 15);
		Shape6.addBox(-2F, 0F, -2F, 1, 1, 6);
		Shape6.setRotationPoint(4F, 9F, -1F);
		Shape6.setTextureSize(64, 32);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape1 = new ModelRenderer(this, 17, 0);
		Shape1.addBox(-3F, 0F, -3F, 6, 2, 6);
		Shape1.setRotationPoint(0F, 22F, 0F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 17, 9);
		Shape2.addBox(-2F, 0F, -2F, 4, 1, 4);
		Shape2.setRotationPoint(0F, 21F, 0F);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 14);
		Shape3.addBox(-1F, 0F, -1F, 2, 10, 2);
		Shape3.setRotationPoint(0F, 11F, 0F);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 0, 8);
		Shape4.addBox(-2F, 0F, -2F, 4, 1, 4);
		Shape4.setRotationPoint(0F, 10F, 0F);
		Shape4.setTextureSize(64, 32);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 0, 0);
		Shape7.addBox(-2F, 0F, -2F, 4, 1, 1);
		Shape7.setRotationPoint(0F, 9F, -1F);
		Shape7.setTextureSize(64, 32);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		Shape8 = new ModelRenderer(this, 0, 3);
		Shape8.addBox(-2F, 0F, -2F, 4, 1, 1);
		Shape8.setRotationPoint(0F, 9F, 4F);
		Shape8.setTextureSize(64, 32);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 17, 15);
		Shape5.addBox(-2F, 0F, -2F, 1, 1, 6);
		Shape5.setRotationPoint(-1F, 9F, -1F);
		Shape5.setTextureSize(64, 32);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		Shape6.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
		Shape5.render(f5);
	}

	public void renderModel(float f5){
		Shape6.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
		Shape5.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}


}
