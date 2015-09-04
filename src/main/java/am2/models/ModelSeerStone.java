package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSeerStone extends ModelBase{
	//fields
	ModelRenderer Shape1;
	ModelRenderer Shape3;

	public ModelSeerStone(){
		textureWidth = 64;
		textureHeight = 32;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(-8F, 0F, -8F, 16, 3, 16);
		Shape1.setRotationPoint(0F, 21F, 0F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 19);
		Shape3.addBox(0F, 0F, 0F, 10, 2, 10);
		Shape3.setRotationPoint(-5F, 19F, -5F);
		Shape3.setTextureSize(64, 32);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		Shape1.render(f5);
		Shape3.render(f5);
	}

	public void renderModel(float f5){
		Shape1.render(f5);
		Shape3.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
