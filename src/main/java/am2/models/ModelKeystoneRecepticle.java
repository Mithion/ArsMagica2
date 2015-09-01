package am2.models;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelKeystoneRecepticle extends ModelBase
{
	//fields
	ModelRenderer Main;
	ModelRenderer BoxBottom;
	ModelRenderer BoxRight;
	ModelRenderer BoxTop;
	ModelRenderer BoxLeft;

	public ModelKeystoneRecepticle()
	{
		textureWidth = 64;
		textureHeight = 64;

		Main = new ModelRenderer(this, 0, 36);
		Main.addBox(-8F, 0F, -8F, 16, 16, 12);
		Main.setRotationPoint(0F, 8F, 4F);
		Main.setTextureSize(64, 64);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
		BoxBottom = new ModelRenderer(this, 0, 0);
		BoxBottom.addBox(-8F, -2F, -2F, 16, 4, 4);
		BoxBottom.setRotationPoint(0F, 22F, -6F);
		BoxBottom.setTextureSize(64, 64);
		BoxBottom.mirror = true;
		setRotation(BoxBottom, 0F, 0F, 0F);
		BoxRight = new ModelRenderer(this, 48, 0);
		BoxRight.addBox(-2F, -2F, -2F, 4, 8, 4);
		BoxRight.setRotationPoint(6F, 14F, -6F);
		BoxRight.setTextureSize(64, 64);
		BoxRight.mirror = true;
		setRotation(BoxRight, 0F, 0F, 0F);
		BoxTop = new ModelRenderer(this, 0, 8);
		BoxTop.addBox(-8F, -2F, -2F, 16, 4, 4);
		BoxTop.setRotationPoint(0F, 10F, -6F);
		BoxTop.setTextureSize(64, 64);
		BoxTop.mirror = true;
		setRotation(BoxTop, 0F, 0F, 0F);
		BoxLeft = new ModelRenderer(this, 48, 12);
		BoxLeft.addBox(-2F, -2F, -2F, 4, 8, 4);
		BoxLeft.setRotationPoint(-6F, 14F, -6F);
		BoxLeft.setTextureSize(64, 64);
		BoxLeft.mirror = true;
		setRotation(BoxLeft, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Main.render(f5);
		BoxBottom.render(f5);
		BoxRight.render(f5);
		BoxTop.render(f5);
		BoxLeft.render(f5);
	}

	public void renderModel(float f5){
		Main.render(f5);
		BoxBottom.render(f5);
		BoxRight.render(f5);
		BoxTop.render(f5);
		BoxLeft.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
