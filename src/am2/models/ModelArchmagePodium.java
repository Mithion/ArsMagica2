package am2.models;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelRenderer;

public class ModelArchmagePodium extends ModelBase
{
	//fields
	ModelRenderer Base;
	ModelRenderer Pillar;
	ModelRenderer DecMoldA;
	ModelRenderer DecMoldB;
	ModelRenderer DecMoldC;
	ModelRenderer DecMoldD;
	ModelRenderer Top;
	ModelRenderer TopDecMold;
	
	public ModelArchmagePodium()
	{
		textureWidth = 64;
		textureHeight = 64;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(-8F, -1F, -8F, 16, 2, 16);
		Base.setRotationPoint(0F, 23F, 0F);
		Base.setTextureSize(64, 64);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		Pillar = new ModelRenderer(this, 0, 0);
		Pillar.addBox(-6F, 0F, -5F, 12, 18, 12);
		Pillar.setRotationPoint(0F, 4F, -1F);
		Pillar.setTextureSize(64, 64);
		Pillar.mirror = true;
		setRotation(Pillar, 0F, 0F, 0F);
		DecMoldA = new ModelRenderer(this, 55, 0);
		DecMoldA.addBox(0F, 0F, 0F, 1, 9, 1);
		DecMoldA.setRotationPoint(-4F, 7F, -7F);
		DecMoldA.setTextureSize(64, 64);
		DecMoldA.mirror = true;
		setRotation(DecMoldA, 0F, 0F, 0F);
		DecMoldB = new ModelRenderer(this, 49, 0);
		DecMoldB.addBox(0F, 0F, 0F, 1, 9, 1);
		DecMoldB.setRotationPoint(3F, 7F, -7F);
		DecMoldB.setTextureSize(64, 64);
		DecMoldB.mirror = true;
		setRotation(DecMoldB, 0F, 0F, 0F);
		DecMoldC = new ModelRenderer(this, 50, 13);
		DecMoldC.addBox(0F, 0F, 0F, 6, 1, 1);
		DecMoldC.setRotationPoint(-3F, 15F, -7F);
		DecMoldC.setTextureSize(64, 64);
		DecMoldC.mirror = true;
		setRotation(DecMoldC, 0F, 0F, 0F);
		DecMoldD = new ModelRenderer(this, 50, 11);
		DecMoldD.addBox(0F, 0F, 0F, 6, 1, 1);
		DecMoldD.setRotationPoint(-3F, 7F, -7F);
		DecMoldD.setTextureSize(64, 64);
		DecMoldD.mirror = true;
		setRotation(DecMoldD, 0F, 0F, 0F);
		Top = new ModelRenderer(this, 0, 32);
		Top.addBox(-8F, 0F, -8F, 16, 2, 16);
		Top.setRotationPoint(0F, 2.9F, 0F);
		Top.setTextureSize(64, 64);
		Top.mirror = true;
		setRotation(Top, -0.1784573F, 0F, 0F);
		TopDecMold = new ModelRenderer(this, 16, 33);
		TopDecMold.addBox(-8F, -1F, 0F, 15, 2, 1);
		TopDecMold.setRotationPoint(0.5F, 4F, 6.8F);
		TopDecMold.setTextureSize(64, 64);
		TopDecMold.mirror = true;
		setRotation(TopDecMold, -0.1784599F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Base.render(f5);
		Pillar.render(f5);
		DecMoldA.render(f5);
		DecMoldB.render(f5);
		DecMoldC.render(f5);
		DecMoldD.render(f5);
		Top.render(f5);
		TopDecMold.render(f5);
	}

	public void renderModel(float f5)
	{
		Base.render(f5);
		Pillar.render(f5);
		DecMoldA.render(f5);
		DecMoldB.render(f5);
		DecMoldC.render(f5);
		DecMoldD.render(f5);
		Top.render(f5);
		TopDecMold.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
