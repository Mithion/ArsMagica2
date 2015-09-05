package am2.entities.models;

import am2.entities.EntityBroom;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBroom extends ModelBase{
	//fields
	ModelRenderer Handle;
	ModelRenderer Brush;

	public ModelBroom(){
		textureWidth = 32;
		textureHeight = 32;

		Handle = new ModelRenderer(this, 0, 9);
		Handle.addBox(0F, 0F, 0F, 1, 21, 1);
		Handle.setRotationPoint(0F, -4F, 0F);
		Handle.setTextureSize(32, 32);
		Handle.mirror = true;
		setRotation(Handle, 0F, 0F, 0F);
		Brush = new ModelRenderer(this, 0, 0);
		Brush.addBox(-4F, 21F, 0F, 9, 7, 1);
		Brush.setRotationPoint(0F, -4F, 0F);
		Brush.setTextureSize(32, 32);
		Brush.mirror = true;
		setRotation(Brush, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		renderBroom((EntityBroom)entity, f5);
	}

	private void renderBroom(EntityBroom broom, float f5){
		Handle.rotateAngleX = broom.getRotation();
		Brush.rotateAngleX = broom.getRotation();
		Handle.render(f5);
		Brush.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
