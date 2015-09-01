package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSpellResearchTable extends ModelBase
{
	//fields
	ModelRenderer Post4;
	ModelRenderer Post1;
	ModelRenderer Post2;
	ModelRenderer Post3;
	ModelRenderer Side1;
	ModelRenderer Side2;
	ModelRenderer Main;

	public ModelSpellResearchTable()
	{
		textureWidth = 64;
		textureHeight = 64;

		Post4 = new ModelRenderer(this, 27, 22);
		Post4.addBox(-1F, 0F, -1F, 2, 13, 2);
		Post4.setRotationPoint(-6F, 11F, 6F);
		Post4.setTextureSize(64, 64);
		Post4.mirror = true;
		setRotation(Post4, 0F, 0F, 0F);
		Post1 = new ModelRenderer(this, 18, 22);
		Post1.addBox(-1F, 0F, -1F, 2, 13, 2);
		Post1.setRotationPoint(6F, 11F, 6F);
		Post1.setTextureSize(64, 64);
		Post1.mirror = true;
		setRotation(Post1, 0F, 0F, 0F);
		Post2 = new ModelRenderer(this, 9, 22);
		Post2.addBox(-1F, 0F, -1F, 2, 13, 2);
		Post2.setRotationPoint(6F, 11F, -6F);
		Post2.setTextureSize(64, 64);
		Post2.mirror = true;
		setRotation(Post2, 0F, 0F, 0F);
		Post3 = new ModelRenderer(this, 0, 22);
		Post3.addBox(-1F, 0F, -1F, 2, 13, 2);
		Post3.setRotationPoint(-6F, 11F, -6F);
		Post3.setTextureSize(64, 64);
		Post3.mirror = true;
		setRotation(Post3, 0F, 0F, 0F);
		Side1 = new ModelRenderer(this, 0, 0);
		Side1.addBox(0F, 0F, -5F, 2, 11, 10);
		Side1.setRotationPoint(5F, 12F, 0F);
		Side1.setTextureSize(64, 64);
		Side1.mirror = true;
		setRotation(Side1, 0F, 0F, 0F);
		Side2 = new ModelRenderer(this, 25, 0);
		Side2.addBox(0F, 0F, -5F, 2, 11, 10);
		Side2.setRotationPoint(-7F, 12F, 0F);
		Side2.setTextureSize(64, 64);
		Side2.mirror = true;
		setRotation(Side2, 0F, 0F, 0F);
		Main = new ModelRenderer(this, 0, 38);
		Main.addBox(-5F, 0F, -7F, 10, 11, 14);
		Main.setRotationPoint(0F, 12F, 0F);
		Main.setTextureSize(64, 64);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		Post4.render(f5);
		Post1.render(f5);
		Post2.render(f5);
		Post3.render(f5);
		Side1.render(f5);
		Side2.render(f5);
		Main.render(f5);
	}

	public void renderModel(float f5){
		Post4.render(f5);
		Post1.render(f5);
		Post2.render(f5);
		Post3.render(f5);
		Side1.render(f5);
		Side2.render(f5);
		Main.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
