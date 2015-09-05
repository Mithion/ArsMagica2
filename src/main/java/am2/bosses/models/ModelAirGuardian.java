package am2.bosses.models;

import am2.bosses.BossActions;
import am2.bosses.EntityAirGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelAirGuardian extends ModelBase{
	//fields
	AM2ModelRenderer Ball1;
	AM2ModelRenderer Ball2;
	AM2ModelRenderer LowerTorso;
	AM2ModelRenderer Torso;
	AM2ModelRenderer RightShoulder;
	AM2ModelRenderer LeftShoulder;
	AM2ModelRenderer RightArm;
	AM2ModelRenderer LeftArm;
	AM2ModelRenderer Head;

	public ModelAirGuardian(){
		textureWidth = 64;
		textureHeight = 64;

		Ball1 = new AM2ModelRenderer(this, 21, 0);
		Ball1.addBox(-3F, -3F, -3F, 6, 6, 6);
		Ball1.setRotationPoint(0F, 10F, 0F);
		Ball1.setTextureSize(64, 64);
		Ball1.mirror = true;
		setRotation(Ball1, -0.7853982F, -1.570796F, 0.7853982F);
		Ball2 = new AM2ModelRenderer(this, 21, 0);
		Ball2.addBox(-3F, -3F, -3F, 6, 6, 6);
		Ball2.setRotationPoint(0F, 10F, 0F);
		Ball2.setTextureSize(64, 64);
		Ball2.mirror = true;
		setRotation(Ball2, -0.7853982F, -1.570796F, -0.7853982F);
		LowerTorso = new AM2ModelRenderer(this, 0, 21);
		LowerTorso.addBox(-2F, -6F, -2F, 4, 6, 4);
		LowerTorso.setRotationPoint(0F, 5F, 0F);
		LowerTorso.setTextureSize(64, 64);
		LowerTorso.mirror = true;
		setRotation(LowerTorso, 0F, 0F, 0F);
		Torso = new AM2ModelRenderer(this, 0, 32);
		Torso.addBox(-3F, -8F, -3F, 6, 8, 6);
		Torso.setRotationPoint(0F, -1F, 0F);
		Torso.setTextureSize(64, 64);
		Torso.mirror = true;
		setRotation(Torso, 0F, 0F, 0F);
		RightShoulder = new AM2ModelRenderer(this, 0, 47);
		RightShoulder.addBox(-4F, -2F, -2F, 4, 4, 4);
		RightShoulder.setRotationPoint(-3F, -7F, 0F);
		RightShoulder.setTextureSize(64, 64);
		RightShoulder.mirror = true;
		setRotation(RightShoulder, 0F, 0F, 0F);
		LeftShoulder = new AM2ModelRenderer(this, 0, 56);
		LeftShoulder.addBox(0F, -2F, -2F, 4, 4, 4);
		LeftShoulder.setRotationPoint(3F, -7F, 0F);
		LeftShoulder.setTextureSize(64, 64);
		LeftShoulder.mirror = true;
		setRotation(LeftShoulder, 0F, 0F, 0F);
		RightArm = new AM2ModelRenderer(this, 17, 55);
		RightArm.addBox(-3.5F, 2F, -1.5F, 3, 6, 3);
		RightArm.setRotationPoint(-3F, -7F, 0F);
		RightArm.setTextureSize(64, 64);
		RightArm.mirror = true;
		setRotation(RightArm, 0F, 0F, 0F);
		LeftArm = new AM2ModelRenderer(this, 17, 55);
		LeftArm.addBox(0.5F, 2F, -1.5F, 3, 6, 3);
		LeftArm.setRotationPoint(3F, -7F, 0F);
		LeftArm.setTextureSize(64, 64);
		LeftArm.mirror = true;
		setRotation(LeftArm, 0F, 0F, 0F);
		Head = new AM2ModelRenderer(this, 17, 17);
		Head.addBox(-3.5F, -7F, -3.5F, 7, 7, 7);
		Head.setRotationPoint(0F, -9F, 0F);
		Head.setTextureSize(64, 64);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);

		Ball1.storeRestRotations();
		Ball2.storeRestRotations();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		if (entity instanceof EntityAirGuardian){
			GL11.glPushMatrix();
			setHeadRotations(f3, f4);

			EntityAirGuardian guardian = (EntityAirGuardian)entity;
			if (guardian.getCurrentAction() == BossActions.SPINNING){
				GL11.glRotatef(guardian.spinRotation, 0, 1, 0);
			}

			updateRotations(guardian);
			Ball1.render(f5);
			Ball2.render(f5);
			LowerTorso.render(f5);
			Torso.render(f5);
			RightShoulder.render(f5);
			LeftShoulder.render(f5);
			RightArm.render(f5);
			LeftArm.render(f5);
			Head.render(f5);

			GL11.glPopMatrix();
		}
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	private void setHeadRotations(float yaw, float pitch){

		yaw = (float)Math.toRadians(yaw);
		pitch = (float)Math.toRadians(pitch);

		Head.rotateAngleX = pitch;
		Head.rotateAngleY = yaw;
	}

	@SuppressWarnings("incomplete-switch")
	private void updateRotations(EntityAirGuardian guardian){

		float rot1 = (float)Math.toRadians(guardian.getOrbitRotation());

		Ball1.rotateAngleY = Ball1.getRestRotationY() + rot1;
		Ball2.rotateAngleY = Ball2.getRestRotationY() + rot1;

		float right_arm_rotation_x = 0;
		float left_arm_rotation_x = 0;

		switch (guardian.getCurrentAction()){
		case CASTING:
			float max_degrees_x = 160;
			float final_degrees_x = 80;

			float max_pants_degrees = 45;

			float action_ticks = 10;
			float fast_action_ticks = 3;
			float final_action_ticks = 6;

			if (guardian.getTicksInCurrentAction() < action_ticks){
				right_arm_rotation_x = (float)Math.toRadians(-max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < action_ticks + fast_action_ticks){
				right_arm_rotation_x = (float)Math.toRadians(-max_degrees_x + (final_degrees_x * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks)));
			}else{
				right_arm_rotation_x = final_degrees_x;
			}

			if (guardian.useLeftArm()){
				left_arm_rotation_x = right_arm_rotation_x;
				right_arm_rotation_x = 0;
			}


			break;
		case LONG_CASTING:
			max_degrees_x = 160;
			final_degrees_x = 80;

			max_pants_degrees = 45;

			action_ticks = 10;
			fast_action_ticks = 3;
			final_action_ticks = 6;

			if (guardian.getTicksInCurrentAction() < action_ticks){
				right_arm_rotation_x = (float)Math.toRadians(-max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < action_ticks + fast_action_ticks){
				right_arm_rotation_x = (float)Math.toRadians(-max_degrees_x + (final_degrees_x * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks)));
			}else{
				right_arm_rotation_x = final_degrees_x;
			}
			left_arm_rotation_x = right_arm_rotation_x;
			break;
		case SPINNING:
			max_degrees_x = 180;
			float degrees = guardian.getTicksInCurrentAction() < 20 ? max_degrees_x * ((float)guardian.getTicksInCurrentAction() / 20f) : max_degrees_x;
			right_arm_rotation_x = (float)Math.toRadians(degrees);
			left_arm_rotation_x = right_arm_rotation_x;
			break;
		}

		RightArm.rotateAngleX = right_arm_rotation_x;
		RightShoulder.rotateAngleX = right_arm_rotation_x;

		LeftArm.rotateAngleX = left_arm_rotation_x;
		LeftShoulder.rotateAngleX = left_arm_rotation_x;
	}
}
