package am2.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class ModelEarthGuardianChest extends ModelBiped{
	//fields
	ModelRenderer LeftShoulder1;
	ModelRenderer Shoulders;
	ModelRenderer LeftShoulder2;
	ModelRenderer RightShoulder2;
	ModelRenderer RightShoulder1;

	public ModelEarthGuardianChest(){
		textureWidth = 64;
		textureHeight = 64;

		LeftShoulder1 = new ModelRenderer(this, 0, 31);
		LeftShoulder1.addBox(2F, -5F, -4F, 8, 8, 8);
		LeftShoulder1.setRotationPoint(2.5F, 0F, 0F);
		LeftShoulder1.setTextureSize(64, 64);
		LeftShoulder1.mirror = true;
		setRotation(LeftShoulder1, 0F, 0F, 0F);
		LeftShoulder1.mirror = false;
		Shoulders = new ModelRenderer(this, 0, 21);
		Shoulders.addBox(-5F, -3F, -3F, 10, 3, 6);
		Shoulders.setRotationPoint(0F, 0F, 0F);
		Shoulders.setTextureSize(64, 64);
		Shoulders.mirror = true;
		setRotation(Shoulders, 0F, 0F, 0F);
		LeftShoulder2 = new ModelRenderer(this, 0, 48);
		LeftShoulder2.addBox(3F, -4F, -5F, 6, 6, 10);
		LeftShoulder2.setRotationPoint(2.5F, 0F, 0F);
		LeftShoulder2.setTextureSize(64, 64);
		LeftShoulder2.mirror = true;
		setRotation(LeftShoulder2, 0F, 0F, 0F);
		RightShoulder2 = new ModelRenderer(this, 0, 48);
		RightShoulder2.addBox(-9F, -4F, -5F, 6, 6, 10);
		RightShoulder2.setRotationPoint(-2.5F, 0F, 0F);
		RightShoulder2.setTextureSize(64, 64);
		RightShoulder2.mirror = true;
		setRotation(RightShoulder2, 0F, 0F, 0F);
		RightShoulder2.mirror = false;
		bipedBody = new ModelRenderer(this, 0, 31);
		bipedBody.addBox(-10F, 0F, -4F, 7, 8, 8);
		bipedBody.setRotationPoint(6.5F, 0F, 0F);
		bipedBody.setTextureSize(64, 64);
		bipedBody.mirror = true;
		setRotation(bipedBody, 0F, 0F, 0F);
		RightShoulder1 = new ModelRenderer(this, 0, 31);
		RightShoulder1.addBox(-10F, -5F, -4F, 8, 8, 8);
		RightShoulder1.setRotationPoint(-2.5F, 0F, 0F);
		RightShoulder1.setTextureSize(64, 64);
		RightShoulder1.mirror = true;
		setRotation(RightShoulder1, 0F, 0F, 0F);

		bipedRightArm = new ModelRenderer(this, 33, 18);
		bipedRightArm.addBox(-2.5F, 2F, -1F, 4, 8, 4);
		bipedRightArm.setRotationPoint(-2.5F, 0F, 0F);
		bipedRightArm.setTextureSize(64, 64);
		bipedRightArm.mirror = true;
		setRotation(bipedRightArm, 0F, 0F, 0F);
		bipedRightArm.mirror = false;

		bipedLeftArm = new ModelRenderer(this, 33, 18);
		bipedLeftArm.addBox(-1.5F, 2F, -2F, 4, 8, 4);
		bipedLeftArm.setRotationPoint(2.5F, 0F, 0F);
		bipedLeftArm.setTextureSize(64, 64);
		bipedLeftArm.mirror = true;
		setRotation(bipedLeftArm, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		LeftShoulder1.render(f5);
		//Shoulders.render(f5);
		LeftShoulder2.render(f5);
		RightShoulder2.render(f5);
		RightShoulder1.render(f5);

		GL11.glPushMatrix();
		GL11.glScalef(1.4f, 1.3f, 0.8f);
		GL11.glTranslatef(0, -0.01f, 0);
		bipedBody.render(f5);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glScalef(1.4f, 1.3f, 1.4f);

		if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHeldItem() == null)
			bipedRightArm.render(f5);
		bipedLeftArm.render(f5);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
