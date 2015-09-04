package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSantaHat extends ModelBase{
	//fields
	ModelRenderer Hat1;
	ModelRenderer Hat2;
	ModelRenderer Hat3;
	ModelRenderer Hat4;
	ModelRenderer Hat5;
	ModelRenderer Hat6;

	public ModelSantaHat(){
		textureWidth = 64;
		textureHeight = 32;

		Hat1 = new ModelRenderer(this, 0, 0);
		Hat1.addBox(0F, 0F, 0F, 10, 2, 10);
		Hat1.setRotationPoint(-5F, -8.8F, -5F);
		Hat1.setTextureSize(64, 32);
		Hat1.mirror = true;
		setRotation(Hat1, 0F, 0F, 0F);
		Hat2 = new ModelRenderer(this, 0, 21);
		Hat2.addBox(0F, 0F, 0F, 8, 3, 8);
		Hat2.setRotationPoint(-4F, -11.8F, -4F);
		Hat2.setTextureSize(64, 32);
		Hat2.mirror = true;
		setRotation(Hat2, 0F, 0F, 0F);
		Hat3 = new ModelRenderer(this, 0, 21);
		Hat3.addBox(0F, 0F, 0F, 6, 2, 6);
		Hat3.setRotationPoint(-3F, -13.8F, -3F);
		Hat3.setTextureSize(64, 32);
		Hat3.mirror = true;
		setRotation(Hat3, 0F, 0F, 0F);
		Hat4 = new ModelRenderer(this, 0, 21);
		Hat4.addBox(0F, 0F, 0F, 2, 2, 3);
		Hat4.setRotationPoint(-1F, -17.2F, -0.5F);
		Hat4.setTextureSize(64, 32);
		Hat4.mirror = true;
		setRotation(Hat4, 0F, 0F, 0F);
		Hat5 = new ModelRenderer(this, 0, 0);
		Hat5.addBox(0F, 0F, 0F, 2, 2, 2);
		Hat5.setRotationPoint(-1F, -18F, 2.3F);
		Hat5.setTextureSize(64, 32);
		Hat5.mirror = true;
		setRotation(Hat5, 0F, 0F, 0F);
		Hat6 = new ModelRenderer(this, 0, 21);
		Hat6.addBox(0F, 0F, 0F, 4, 2, 4);
		Hat6.setRotationPoint(-2F, -15.8F, -2F);
		Hat6.setTextureSize(64, 32);
		Hat6.mirror = true;
		setRotation(Hat6, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		Hat1.render(f5);
		Hat2.render(f5);
		Hat3.render(f5);
		Hat4.render(f5);
		Hat5.render(f5);
		Hat6.render(f5);
	}

	public void renderModel(float f5){
		Hat1.render(f5);
		Hat2.render(f5);
		Hat3.render(f5);
		Hat4.render(f5);
		Hat5.render(f5);
		Hat6.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
