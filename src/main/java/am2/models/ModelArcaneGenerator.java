package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelArcaneGenerator extends ModelBase{
	//fields
	ModelRenderer Base;
	ModelRenderer Red;
	ModelRenderer Purple;

	public ModelArcaneGenerator(){
		textureWidth = 64;
		textureHeight = 32;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(-2F, -1F, -2F, 6, 2, 6);
		Base.setRotationPoint(-1F, 23F, -1F);
		Base.setTextureSize(64, 32);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		Red = new ModelRenderer(this, 10, 9);
		Red.addBox(0F, 0F, 0F, 2, 1, 2);
		Red.setRotationPoint(-2F, 21F, -2F);
		Red.setTextureSize(64, 32);
		Red.mirror = true;
		setRotation(Red, 0F, 0F, 0F);
		Purple = new ModelRenderer(this, 0, 9);
		Purple.addBox(0F, 0F, 0F, 2, 1, 2);
		Purple.setRotationPoint(0F, 21F, 0F);
		Purple.setTextureSize(64, 32);
		Purple.mirror = true;
		setRotation(Purple, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Base.render(f5);
		Red.render(f5);
		Purple.render(f5);
	}

	public void renderModel(float f5){
		Base.render(f5);
		Red.render(f5);
		Purple.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity){
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
	}

}
