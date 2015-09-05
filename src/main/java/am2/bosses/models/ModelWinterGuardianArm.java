package am2.bosses.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelWinterGuardianArm extends ModelBase{
	//fields
	ModelRenderer leftarm;
	ModelRenderer lefthand2;
	ModelRenderer lefthand3;
	ModelRenderer lefthand4;
	ModelRenderer lefthand1;

	public ModelWinterGuardianArm(){
		textureWidth = 128;
		textureHeight = 128;

		leftarm = new ModelRenderer(this, 59, 0);
		leftarm.addBox(-3F, -26F, -3F, 6, 26, 6);
		leftarm.setRotationPoint(0F, 0F, 0F);
		leftarm.setTextureSize(128, 128);
		leftarm.mirror = true;
		setRotation(leftarm, -1.570796F, 0F, 0F);
		leftarm.mirror = false;
		lefthand2 = new ModelRenderer(this, 81, 59);
		lefthand2.addBox(-3F, 1F, -2F, 6, 1, 5);
		lefthand2.setRotationPoint(0F, 0F, 0F);
		lefthand2.setTextureSize(128, 128);
		lefthand2.mirror = true;
		setRotation(lefthand2, -1.570796F, 0F, 0F);
		lefthand3 = new ModelRenderer(this, 91, 66);
		lefthand3.addBox(2F, 0F, -2F, 1, 1, 5);
		lefthand3.setRotationPoint(0F, 0F, 0F);
		lefthand3.setTextureSize(128, 128);
		lefthand3.mirror = true;
		setRotation(lefthand3, -1.570796F, 0F, 0F);
		lefthand4 = new ModelRenderer(this, 91, 66);
		lefthand4.addBox(-3F, 0F, -2F, 1, 1, 5);
		lefthand4.setRotationPoint(0F, 0F, 0F);
		lefthand4.setTextureSize(128, 128);
		lefthand4.mirror = true;
		setRotation(lefthand4, -1.570796F, 0F, 0F);
		lefthand1 = new ModelRenderer(this, 120, 118);
		lefthand1.addBox(-3F, 0F, -3F, 3, 2, 1);
		lefthand1.setRotationPoint(0F, 0F, 0F);
		lefthand1.setTextureSize(128, 128);
		lefthand1.mirror = true;
		setRotation(lefthand1, -1.570796F, 0F, 0F);
		lefthand1.mirror = false;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		GL11.glPushMatrix();
		if (entity != null)
			GL11.glRotatef(-entity.rotationYaw, 0, 1, 0);
		leftarm.render(f5);
		lefthand3.render(f5);
		lefthand2.render(f5);
		lefthand4.render(f5);
		lefthand1.render(f5);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5){
	}

}
