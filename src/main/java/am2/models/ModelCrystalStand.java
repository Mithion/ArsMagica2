package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrystalStand extends ModelBase{
	//fields
	ModelRenderer Pillar1;
	ModelRenderer Pillar2;
	ModelRenderer Pillar3;
	ModelRenderer Pillar4;
	ModelRenderer Stand1;
	ModelRenderer Stand2;
	ModelRenderer Stand3;
	ModelRenderer Stand4;
	ModelRenderer Foot4;
	ModelRenderer Foot1;
	ModelRenderer Foot2;
	ModelRenderer Foot3;

	public ModelCrystalStand(){
		textureWidth = 64;
		textureHeight = 32;

		Pillar1 = new ModelRenderer(this, 19, 8);
		Pillar1.addBox(0F, 0F, 0F, 1, 9, 2);
		Pillar1.setRotationPoint(3F, 15F, -1F);
		Pillar1.setTextureSize(64, 32);
		Pillar1.mirror = true;
		setRotation(Pillar1, 0F, 0F, 0F);
		Pillar2 = new ModelRenderer(this, 26, 8);
		Pillar2.addBox(0F, 0F, 0F, 2, 9, 1);
		Pillar2.setRotationPoint(-1F, 15F, -4F);
		Pillar2.setTextureSize(64, 32);
		Pillar2.mirror = true;
		setRotation(Pillar2, 0F, 0F, 0F);
		Pillar3 = new ModelRenderer(this, 40, 8);
		Pillar3.addBox(0F, 0F, 0F, 1, 9, 2);
		Pillar3.setRotationPoint(-4F, 15F, -1F);
		Pillar3.setTextureSize(64, 32);
		Pillar3.mirror = true;
		setRotation(Pillar3, 0F, 0F, 0F);
		Pillar4 = new ModelRenderer(this, 33, 8);
		Pillar4.addBox(0F, 0F, 0F, 2, 9, 1);
		Pillar4.setRotationPoint(-1F, 15F, 3F);
		Pillar4.setTextureSize(64, 32);
		Pillar4.mirror = true;
		setRotation(Pillar4, 0F, 0F, 0F);
		Stand1 = new ModelRenderer(this, 0, 15);
		Stand1.addBox(0F, 0F, 0F, 8, 1, 1);
		Stand1.setRotationPoint(-4F, 14F, -4F);
		Stand1.setTextureSize(64, 32);
		Stand1.mirror = true;
		setRotation(Stand1, 0F, 0F, 0F);
		Stand2 = new ModelRenderer(this, 0, 12);
		Stand2.addBox(0F, 0F, 0F, 8, 1, 1);
		Stand2.setRotationPoint(-4F, 14F, 3F);
		Stand2.setTextureSize(64, 32);
		Stand2.mirror = true;
		setRotation(Stand2, 0F, 0F, 0F);
		Stand3 = new ModelRenderer(this, 20, 0);
		Stand3.addBox(0F, 0F, 0F, 1, 1, 6);
		Stand3.setRotationPoint(-4F, 14F, -3F);
		Stand3.setTextureSize(64, 32);
		Stand3.mirror = true;
		setRotation(Stand3, 0F, 0F, 0F);
		Stand4 = new ModelRenderer(this, 35, 0);
		Stand4.addBox(0F, 0F, 0F, 1, 1, 6);
		Stand4.setRotationPoint(3F, 14F, -3F);
		Stand4.setTextureSize(64, 32);
		Stand4.mirror = true;
		setRotation(Stand4, 0F, 0F, 0F);
		Foot4 = new ModelRenderer(this, 0, 0);
		Foot4.addBox(0F, 0F, 0F, 3, 1, 2);
		Foot4.setRotationPoint(4F, 23F, -1F);
		Foot4.setTextureSize(64, 32);
		Foot4.mirror = true;
		setRotation(Foot4, 0F, 0F, 0F);
		Foot1 = new ModelRenderer(this, 10, 0);
		Foot1.addBox(0F, 0F, 0F, 2, 1, 3);
		Foot1.setRotationPoint(-1F, 23F, 4F);
		Foot1.setTextureSize(64, 32);
		Foot1.mirror = true;
		setRotation(Foot1, 0F, 0F, 0F);
		Foot2 = new ModelRenderer(this, 10, 4);
		Foot2.addBox(0F, 0F, 0F, 2, 1, 3);
		Foot2.setRotationPoint(-1F, 23F, -7F);
		Foot2.setTextureSize(64, 32);
		Foot2.mirror = true;
		setRotation(Foot2, 0F, 0F, 0F);
		Foot3 = new ModelRenderer(this, 0, 3);
		Foot3.addBox(0F, 0F, 0F, 3, 1, 2);
		Foot3.setRotationPoint(-7F, 23F, -1F);
		Foot3.setTextureSize(64, 32);
		Foot3.mirror = true;
		setRotation(Foot3, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		Pillar1.render(f5);
		Pillar2.render(f5);
		Pillar3.render(f5);
		Pillar4.render(f5);
		Stand1.render(f5);
		Stand2.render(f5);
		Stand3.render(f5);
		Stand4.render(f5);
		Foot4.render(f5);
		Foot1.render(f5);
		Foot2.render(f5);
		Foot3.render(f5);
	}

	public void renderBlock(float f5){
		Pillar1.render(f5);
		Pillar2.render(f5);
		Pillar3.render(f5);
		Pillar4.render(f5);
		Stand1.render(f5);
		Stand2.render(f5);
		Stand3.render(f5);
		Stand4.render(f5);
		Foot4.render(f5);
		Foot1.render(f5);
		Foot2.render(f5);
		Foot3.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
