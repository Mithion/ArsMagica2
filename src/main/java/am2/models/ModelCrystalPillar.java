package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelCrystalPillar extends ModelBase{
	//fields
	ModelRenderer Base;
	ModelRenderer Shaft;
	ModelRenderer Claw11;
	ModelRenderer Claw12;
	ModelRenderer Claw13;
	ModelRenderer Claw14;
	ModelRenderer Crystal1;
	ModelRenderer Crystal2;
	ModelRenderer Crystal3;
	ModelRenderer Crystal4;
	ModelRenderer Crystal5;
	ModelRenderer Crystal6;
	ModelRenderer Crystal7;
	ModelRenderer Crystal8;
	ModelRenderer Crystal9;
	ModelRenderer Crystal10;

	public ModelCrystalPillar(){
		textureWidth = 64;
		textureHeight = 32;

		Base = new ModelRenderer(this, 32, 0);
		Base.addBox(0F, 0F, 0F, 8, 1, 8);
		Base.setRotationPoint(-4F, 23F, -4F);
		Base.setTextureSize(64, 32);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		Shaft = new ModelRenderer(this, 56, 10);
		Shaft.addBox(0F, 0F, 0F, 2, 8, 2);
		Shaft.setRotationPoint(-1F, 15F, -1F);
		Shaft.setTextureSize(64, 32);
		Shaft.mirror = true;
		setRotation(Shaft, 0F, 0F, 0F);
		Claw11 = new ModelRenderer(this, 49, 10);
		Claw11.addBox(0F, 0F, 0F, 1, 3, 2);
		Claw11.setRotationPoint(1.5F, 13F, -1F);
		Claw11.setTextureSize(64, 32);
		Claw11.mirror = true;
		setRotation(Claw11, 0F, 0F, 0.5235988F);
		Claw12 = new ModelRenderer(this, 42, 10);
		Claw12.addBox(0F, 0F, 0F, 1, 3, 2);
		Claw12.setRotationPoint(1.5F, 11F, -1F);
		Claw12.setTextureSize(64, 32);
		Claw12.mirror = true;
		setRotation(Claw12, 0F, 0F, 0F);
		Claw13 = new ModelRenderer(this, 35, 10);
		Claw13.addBox(0F, 0F, 0F, 1, 3, 2);
		Claw13.setRotationPoint(0F, 9F, -1F);
		Claw13.setTextureSize(64, 32);
		Claw13.mirror = true;
		setRotation(Claw13, 0F, 0F, -0.5235988F);
		Claw14 = new ModelRenderer(this, 47, 16);
		Claw14.addBox(0F, 0F, 0F, 2, 1, 2);
		Claw14.setRotationPoint(-1F, 8F, -1F);
		Claw14.setTextureSize(64, 32);
		Claw14.mirror = true;
		setRotation(Claw14, 0F, 0F, 0F);
		Crystal1 = new ModelRenderer(this, 0, 23);
		Crystal1.addBox(-1F, -1.5F, -1F, 2, 2, 2);
		Crystal1.setRotationPoint(-0.5F, 12.5F, 0F);
		Crystal1.setTextureSize(64, 32);
		Crystal1.mirror = true;
		setRotation(Crystal1, 0F, 0.7853982F, 0F);
		Crystal2 = new ModelRenderer(this, 0, 28);
		Crystal2.addBox(-1F, -1.5F, -1F, 2, 2, 2);
		Crystal2.setRotationPoint(-0.5F, 12.5F, 0F);
		Crystal2.setTextureSize(64, 32);
		Crystal2.mirror = true;
		setRotation(Crystal2, 0F, 0F, 0F);
		Crystal3 = new ModelRenderer(this, 0, 19);
		Crystal3.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal3.setRotationPoint(-0.5F, 12F, 0F);
		Crystal3.setTextureSize(64, 32);
		Crystal3.mirror = true;
		setRotation(Crystal3, 0F, 0F, 0.7853982F);
		Crystal4 = new ModelRenderer(this, 0, 15);
		Crystal4.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal4.setRotationPoint(-0.5F, 12F, 0F);
		Crystal4.setTextureSize(64, 32);
		Crystal4.mirror = true;
		setRotation(Crystal4, 0F, 1.570796F, 0.7853982F);
		Crystal5 = new ModelRenderer(this, 0, 11);
		Crystal5.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal5.setRotationPoint(-0.5F, 12F, 0F);
		Crystal5.setTextureSize(64, 32);
		Crystal5.mirror = true;
		setRotation(Crystal5, 0F, 0.7853982F, 0.7853982F);
		Crystal6 = new ModelRenderer(this, 0, 7);
		Crystal6.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal6.setRotationPoint(-0.5F, 12F, 0F);
		Crystal6.setTextureSize(64, 32);
		Crystal6.mirror = true;
		setRotation(Crystal6, 0F, 2.356194F, 0.7853982F);
		Crystal7 = new ModelRenderer(this, 7, 7);
		Crystal7.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal7.setRotationPoint(-0.5F, 13.5F, 0F);
		Crystal7.setTextureSize(64, 32);
		Crystal7.mirror = true;
		setRotation(Crystal7, 0F, 0F, 0.7853982F);
		Crystal8 = new ModelRenderer(this, 7, 19);
		Crystal8.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal8.setRotationPoint(-0.5F, 13.5F, 0F);
		Crystal8.setTextureSize(64, 32);
		Crystal8.mirror = true;
		setRotation(Crystal8, 0F, 0.7853982F, 0.7853982F);
		Crystal9 = new ModelRenderer(this, 7, 15);
		Crystal9.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal9.setRotationPoint(-0.5F, 13.5F, 0F);
		Crystal9.setTextureSize(64, 32);
		Crystal9.mirror = true;
		setRotation(Crystal9, 0F, 1.570796F, 0.7853982F);
		Crystal10 = new ModelRenderer(this, 7, 11);
		Crystal10.addBox(-1F, -1F, -1F, 1, 1, 2);
		Crystal10.setRotationPoint(-0.5F, 13.5F, 0F);
		Crystal10.setTextureSize(64, 32);
		Crystal10.mirror = true;
		setRotation(Crystal10, 0F, 2.356194F, 0.7853982F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Base.render(f5);
		Shaft.render(f5);
		//Claw11.render(f5);
		//Claw12.render(f5);
		//Claw13.render(f5);
		//Claw14.render(f5);
		Crystal1.render(f5);
		Crystal2.render(f5);
		Crystal3.render(f5);
		Crystal4.render(f5);
		Crystal5.render(f5);
		Crystal6.render(f5);
		Crystal7.render(f5);
		Crystal8.render(f5);
		Crystal9.render(f5);
		Crystal10.render(f5);
	}

	public void renderModel(float rotation, float f5){
		Base.render(f5);
		Shaft.render(f5);
		/*Claw11.render(f5);
		Claw12.render(f5);
		Claw13.render(f5);
		Claw14.render(f5);*/

		Crystal1.rotateAngleY = rotation;
		Crystal2.rotateAngleX = -rotation + 90;
		Crystal3.rotateAngleZ = rotation;
		Crystal4.rotateAngleY = -rotation;
		Crystal5.rotateAngleZ = rotation;
		Crystal6.rotateAngleY = -rotation - 90;
		Crystal7.rotateAngleX = rotation;
		Crystal8.rotateAngleY = -rotation;
		Crystal9.rotateAngleY = rotation;
		Crystal10.rotateAngleY = -rotation + 90;


		GL11.glPushMatrix();
		GL11.glTranslatef(0.025f, 0f, 0f);
		Crystal1.render(f5);
		GL11.glTranslatef(-0.2f, 0.15f, -0.08f);
		Crystal2.render(f5);
		GL11.glTranslatef(0f, -0.25f, 0.2f);
		Crystal3.render(f5);
		GL11.glTranslatef(0.4f, -0.05f, 0.0f);
		Crystal4.render(f5);
		GL11.glTranslatef(0.0f, 0.1f, 0.24f);
		Crystal5.render(f5);
		GL11.glTranslatef(-0.08f, 0.1f, -0.7f);
		Crystal6.render(f5);
		GL11.glTranslatef(-0.18f, -0.25f, -0.0f);
		Crystal7.render(f5);
		GL11.glTranslatef(-0.18f, -0.0f, 0.123f);
		Crystal8.render(f5);
		GL11.glTranslatef(0.6f, 0.2f, 0.015f);
		Crystal9.render(f5);
		GL11.glTranslatef(0.0f, 0.1f, 0.5f);
		Crystal10.render(f5);

		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
