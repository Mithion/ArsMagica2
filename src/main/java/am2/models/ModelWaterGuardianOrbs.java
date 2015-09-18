package am2.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelWaterGuardianOrbs extends ModelBiped{
	//fields
	ModelRenderer ornament4;
	ModelRenderer ornament2;
	ModelRenderer ornament6;
	ModelRenderer ornament7;
	ModelRenderer ornament8;
	ModelRenderer ornament1;
	ModelRenderer ornament5;
	ModelRenderer ornament3;

	public ModelWaterGuardianOrbs(){
		textureWidth = 64;
		textureHeight = 64;

		ornament4 = new ModelRenderer(this, 0, 15);
		ornament4.addBox(0F, 9.5F, -2F, 2, 1, 2);
		ornament4.setRotationPoint(0F, 7.5F, 0F);
		ornament4.setTextureSize(64, 64);
		ornament4.mirror = true;
		setRotation(ornament4, 1.570796F, -2.356194F, 0F);
		ornament2 = new ModelRenderer(this, 0, 15);
		ornament2.addBox(0F, 9.5F, -2.2F, 2, 1, 2);
		ornament2.setRotationPoint(0F, 7.5F, 0F);
		ornament2.setTextureSize(64, 64);
		ornament2.mirror = true;
		setRotation(ornament2, 1.570796F, -0.7853982F, 0F);
		ornament6 = new ModelRenderer(this, 0, 15);
		ornament6.addBox(0F, 9.5F, -2F, 2, 1, 2);
		ornament6.setRotationPoint(0F, 7.5F, 0F);
		ornament6.setTextureSize(64, 64);
		ornament6.mirror = true;
		setRotation(ornament6, 1.570796F, 0.7853982F, 0F);
		ornament7 = new ModelRenderer(this, 0, 15);
		ornament7.addBox(0F, 9.5F, -2F, 2, 1, 2);
		ornament7.setRotationPoint(0F, 7.5F, 0F);
		ornament7.setTextureSize(64, 64);
		ornament7.mirror = true;
		setRotation(ornament7, 1.570796F, 2.356194F, 0F);
		ornament8 = new ModelRenderer(this, 0, 19);
		ornament8.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament8.setRotationPoint(0F, 7.5F, 0F);
		ornament8.setTextureSize(64, 64);
		ornament8.mirror = true;
		setRotation(ornament8, 0F, 0.7853982F, 0F);
		ornament1 = new ModelRenderer(this, 0, 19);
		ornament1.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament1.setRotationPoint(0F, 7.5F, 0F);
		ornament1.setTextureSize(64, 64);
		ornament1.mirror = true;
		setRotation(ornament1, 0F, -0.7853982F, 0F);
		ornament5 = new ModelRenderer(this, 0, 19);
		ornament5.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament5.setRotationPoint(0F, 7.5F, 0F);
		ornament5.setTextureSize(64, 64);
		ornament5.mirror = true;
		setRotation(ornament5, 0F, 2.356194F, 0F);
		ornament3 = new ModelRenderer(this, 0, 19);
		ornament3.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament3.setRotationPoint(0F, 7.5F, 0F);
		ornament3.setTextureSize(64, 64);
		ornament3.mirror = true;
		setRotation(ornament3, 0F, -2.356194F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		GL11.glPushMatrix();

		if (entity != null)
			GL11.glRotatef(f2, 0, 1, 0);

		ornament4.render(f5);
		ornament2.render(f5);
		ornament6.render(f5);
		ornament7.render(f5);
		ornament8.render(f5);
		ornament1.render(f5);
		ornament5.render(f5);
		ornament3.render(f5);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
