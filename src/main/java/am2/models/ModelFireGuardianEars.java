package am2.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelFireGuardianEars extends ModelBiped{
	//fields
	ModelRenderer Head6;
	ModelRenderer Head4;
	ModelRenderer Head5;
	ModelRenderer Head7;

	public ModelFireGuardianEars(){
		textureWidth = 128;
		textureHeight = 128;

		Head6 = new ModelRenderer(this, 63, 65);
		Head6.addBox(4F, -1.5F, -5F, 2, 3, 6);
		Head6.setRotationPoint(0F, 0F, 0F);
		Head6.setTextureSize(128, 128);
		Head6.mirror = true;
		setRotation(Head6, 0F, 0F, 0F);
		Head4 = new ModelRenderer(this, 63, 65);
		Head4.addBox(-6F, -1.5F, -5F, 2, 3, 6);
		Head4.setRotationPoint(0F, 0F, 0F);
		Head4.setTextureSize(128, 128);
		Head4.mirror = true;
		setRotation(Head4, 0F, 0F, 0F);
		Head5 = new ModelRenderer(this, 63, 57);
		Head5.addBox(-5.5F, -1F, 1F, 1, 2, 5);
		Head5.setRotationPoint(0F, 0F, 0F);
		Head5.setTextureSize(128, 128);
		Head5.mirror = true;
		setRotation(Head5, 0F, 0F, 0F);
		Head7 = new ModelRenderer(this, 63, 57);
		Head7.addBox(4.5F, -1F, 1F, 1, 2, 5);
		Head7.setRotationPoint(0F, 0F, 0F);
		Head7.setTextureSize(128, 128);
		Head7.mirror = true;
		setRotation(Head7, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		GL11.glPushMatrix();

		if (entity != null){
			super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

			GL11.glTranslatef(0, -0.3f, 0);

			setRotation(Head4, bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ);
			setRotation(Head5, bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ);
			setRotation(Head6, bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ);
			setRotation(Head7, bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ);
		}
		Head6.render(f5);
		Head4.render(f5);
		Head5.render(f5);
		Head7.render(f5);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
