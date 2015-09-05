package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class modelBlockCaster extends ModelBase{
	//fields
	ModelRenderer Base;
	ModelRenderer Pillar;
	ModelRenderer Top1;
	ModelRenderer Top2;
	ModelRenderer Top3;
	ModelRenderer Rotator4;
	ModelRenderer Rotator1;
	ModelRenderer Rotator2;
	ModelRenderer Rotator3;

	public modelBlockCaster(){
		textureWidth = 64;
		textureHeight = 32;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(-8F, 0F, -8F, 16, 3, 16);
		Base.setRotationPoint(0F, 21F, 0F);
		Base.setTextureSize(64, 32);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		Pillar = new ModelRenderer(this, 0, 0);
		Pillar.addBox(0F, 0F, 0F, 7, 8, 7);
		Pillar.setRotationPoint(-3.5F, 13F, -3.5F);
		Pillar.setTextureSize(64, 32);
		Pillar.mirror = true;
		setRotation(Pillar, 0F, 0F, 0F);
		Top1 = new ModelRenderer(this, 0, 0);
		Top1.addBox(0F, 0F, 0F, 16, 5, 5);
		Top1.setRotationPoint(-8F, 9F, -8F);
		Top1.setTextureSize(64, 32);
		Top1.mirror = true;
		setRotation(Top1, 0F, 0F, 0F);
		Top2 = new ModelRenderer(this, 0, 0);
		Top2.addBox(0F, 0F, 0F, 16, 5, 5);
		Top2.setRotationPoint(-8F, 9F, 3F);
		Top2.setTextureSize(64, 32);
		Top2.mirror = true;
		setRotation(Top2, 0F, 0F, 0F);
		Top3 = new ModelRenderer(this, 0, 21);
		Top3.addBox(0F, 0F, 0F, 11, 5, 6);
		Top3.setRotationPoint(-7F, 8F, -3F);
		Top3.setTextureSize(64, 32);
		Top3.mirror = true;
		setRotation(Top3, 0F, 0F, 0F);
		Rotator4 = new ModelRenderer(this, 47, 21);
		Rotator4.addBox(-7F, -2F, 0F, 1, 5, 1);
		Rotator4.setRotationPoint(0F, 17F, 0F);
		Rotator4.setTextureSize(64, 32);
		Rotator4.mirror = true;
		setRotation(Rotator4, 0F, 0F, 0F);
		Rotator1 = new ModelRenderer(this, 52, 21);
		Rotator1.addBox(0F, -2F, -7F, 1, 5, 1);
		Rotator1.setRotationPoint(0F, 17F, 0F);
		Rotator1.setTextureSize(64, 32);
		Rotator1.mirror = true;
		setRotation(Rotator1, 0F, 0F, 0F);
		Rotator2 = new ModelRenderer(this, 42, 21);
		Rotator2.addBox(7F, -2F, 0F, 1, 5, 1);
		Rotator2.setRotationPoint(0F, 17F, 0F);
		Rotator2.setTextureSize(64, 32);
		Rotator2.mirror = true;
		setRotation(Rotator2, 0F, 0F, 0F);
		Rotator3 = new ModelRenderer(this, 37, 21);
		Rotator3.addBox(0F, -2F, 7F, 1, 5, 1);
		Rotator3.setRotationPoint(0F, 17F, 0F);
		Rotator3.setTextureSize(64, 32);
		Rotator3.mirror = true;
		setRotation(Rotator3, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Base.render(f5);
		Pillar.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);
		Rotator4.render(f5);
		Rotator1.render(f5);
		Rotator2.render(f5);
		Rotator3.render(f5);
	}

	public void renderModel(float rotation, float f5){
		Base.render(f5);
		Pillar.render(f5);
		Top1.render(f5);
		Top2.render(f5);
		Top3.render(f5);

		Rotator1.rotateAngleY = rotation;
		Rotator2.rotateAngleY = rotation;
		Rotator3.rotateAngleY = rotation;
		Rotator4.rotateAngleY = rotation;

		Rotator4.render(f5);
		Rotator1.render(f5);
		Rotator2.render(f5);
		Rotator3.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
