package am2.bosses.models;

import am2.bosses.EntityEarthGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelEarthGuardian extends ModelBase{
	//fields
	AM2ModelRenderer Core2;
	AM2ModelRenderer Rod3;
	AM2ModelRenderer Rod4;
	AM2ModelRenderer Rod2;
	AM2ModelRenderer Rod1;
	AM2ModelRenderer Core1;
	AM2ModelRenderer LeftShoulder1;
	AM2ModelRenderer Shoulders;
	AM2ModelRenderer RightArmUpper;
	AM2ModelRenderer LeftArm1;
	AM2ModelRenderer RightArmLower;
	AM2ModelRenderer LeftArm2;
	AM2ModelRenderer RodMain;
	AM2ModelRenderer Neck;
	AM2ModelRenderer Head;
	AM2ModelRenderer LeftShoulder2;
	AM2ModelRenderer RightShoulder2;
	AM2ModelRenderer RightShoulder1;
	AM2ModelRenderer Rock1;
	AM2ModelRenderer Rock2;
	AM2ModelRenderer Rock3;

	public ModelEarthGuardian(){
		textureWidth = 64;
		textureHeight = 64;

		Core2 = new AM2ModelRenderer(this, 33, 2);
		Core2.addBox(-3F, -3F, -3F, 6, 6, 6);
		Core2.setRotationPoint(0F, -2F, 0F);
		Core2.setTextureSize(64, 64);
		Core2.mirror = true;
		setRotation(Core2, -0.7853982F, -1.570796F, 0.7853982F);
		Core2.mirror = false;

		Rod3 = new AM2ModelRenderer(this, 42, 31);
		Rod3.addBox(-2.5F, -5F, 1.5F, 1, 10, 1);
		Rod3.setRotationPoint(0F, 11F, 0F);
		Rod3.setTextureSize(64, 64);
		Rod3.mirror = true;
		setRotation(Rod3, 0F, 0F, 0F);
		Rod3.mirror = false;

		Rod4 = new AM2ModelRenderer(this, 42, 31);
		Rod4.addBox(1.5F, -5F, 1.5F, 1, 10, 1);
		Rod4.setRotationPoint(0F, 11F, 0F);
		Rod4.setTextureSize(64, 64);
		Rod4.mirror = true;
		setRotation(Rod4, 0F, 0F, 0F);
		Rod4.mirror = false;

		Rod2 = new AM2ModelRenderer(this, 42, 31);
		Rod2.addBox(-2.5F, -5F, -2.5F, 1, 10, 1);
		Rod2.setRotationPoint(0F, 11F, 0F);
		Rod2.setTextureSize(64, 64);
		Rod2.mirror = true;
		setRotation(Rod2, 0F, 0F, 0F);
		Rod2.mirror = false;

		Rod1 = new AM2ModelRenderer(this, 42, 31);
		Rod1.addBox(1.5F, -5F, -2.5F, 1, 10, 1);
		Rod1.setRotationPoint(0F, 11F, 0F);
		Rod1.setTextureSize(64, 64);
		Rod1.mirror = true;
		setRotation(Rod1, 0F, 0F, 0F);
		Rod1.mirror = false;

		Core1 = new AM2ModelRenderer(this, 33, 2);
		Core1.addBox(-3F, -3F, -3F, 6, 6, 6);
		Core1.setRotationPoint(0F, -2F, 0F);
		Core1.setTextureSize(64, 64);
		Core1.mirror = true;
		setRotation(Core1, -0.7853982F, -1.570796F, -0.7853982F);
		Core1.mirror = false;

		LeftShoulder1 = new AM2ModelRenderer(this, 0, 31);
		LeftShoulder1.addBox(-1F, -1F, -4F, 8, 8, 8);
		LeftShoulder1.setRotationPoint(2.5F, -7F, 0F);
		LeftShoulder1.setTextureSize(64, 64);
		LeftShoulder1.mirror = true;
		setRotation(LeftShoulder1, 0F, 0F, -0.6108652F);
		LeftShoulder1.mirror = false;

		Shoulders = new AM2ModelRenderer(this, 0, 21);
		Shoulders.addBox(-5F, -3F, -3F, 10, 3, 6);
		Shoulders.setRotationPoint(0F, -7F, 0F);
		Shoulders.setTextureSize(64, 64);
		Shoulders.mirror = true;
		setRotation(Shoulders, 0F, 0F, 0F);
		Shoulders.mirror = false;

		RightArmUpper = new AM2ModelRenderer(this, 33, 18);
		RightArmUpper.addBox(-8.5F, 3F, -2F, 4, 8, 4);
		RightArmUpper.setRotationPoint(-2.5F, -7F, 0F);
		RightArmUpper.setTextureSize(64, 64);
		RightArmUpper.mirror = true;
		setRotation(RightArmUpper, 0F, 0F, 0F);
		RightArmUpper.mirror = false;

		LeftArm1 = new AM2ModelRenderer(this, 33, 18);
		LeftArm1.addBox(4.5F, 3F, -2F, 4, 8, 4);
		LeftArm1.setRotationPoint(2.5F, -7F, 0F);
		LeftArm1.setTextureSize(64, 64);
		LeftArm1.mirror = true;
		setRotation(LeftArm1, 0F, 0F, 0F);
		LeftArm1.mirror = false;

		RightArmLower = new AM2ModelRenderer(this, 50, 17);
		RightArmLower.addBox(-8F, 11F, -1.5F, 3, 10, 3);
		RightArmLower.setRotationPoint(-2.5F, -7F, 0F);
		RightArmLower.setTextureSize(64, 64);
		RightArmLower.mirror = true;
		setRotation(RightArmLower, 0F, 0F, 0F);
		RightArmLower.mirror = false;

		LeftArm2 = new AM2ModelRenderer(this, 50, 17);
		LeftArm2.addBox(5F, 11F, -1.5F, 3, 10, 3);
		LeftArm2.setRotationPoint(2.5F, -7F, 0F);
		LeftArm2.setTextureSize(64, 64);
		LeftArm2.mirror = true;
		setRotation(LeftArm2, 0F, 0F, 0F);
		LeftArm2.mirror = false;

		RodMain = new AM2ModelRenderer(this, 33, 31);
		RodMain.addBox(-1F, -7F, -1F, 2, 16, 2);
		RodMain.setRotationPoint(0F, 11F, 0F);
		RodMain.setTextureSize(64, 64);
		RodMain.mirror = true;
		setRotation(RodMain, 0F, 0F, 0F);
		RodMain.mirror = false;

		Neck = new AM2ModelRenderer(this, 0, 15);
		Neck.addBox(-2F, -1F, -2F, 4, 1, 4);
		Neck.setRotationPoint(0F, -10F, 0F);
		Neck.setTextureSize(64, 64);
		Neck.mirror = true;
		setRotation(Neck, 0F, 0F, 0F);
		Neck.mirror = false;

		Head = new AM2ModelRenderer(this, 0, 0);
		Head.addBox(-4F, -6F, -4F, 8, 6, 8);
		Head.setRotationPoint(0F, -11F, 0F);
		Head.setTextureSize(64, 64);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);
		Head.mirror = false;

		LeftShoulder2 = new AM2ModelRenderer(this, 0, 48);
		LeftShoulder2.addBox(0F, 0F, -5F, 6, 6, 10);
		LeftShoulder2.setRotationPoint(2.5F, -7F, 0F);
		LeftShoulder2.setTextureSize(64, 64);
		LeftShoulder2.mirror = true;
		setRotation(LeftShoulder2, 0F, 0F, -0.6108652F);
		LeftShoulder2.mirror = false;

		RightShoulder2 = new AM2ModelRenderer(this, 0, 48);
		RightShoulder2.addBox(-6F, 0F, -5F, 6, 6, 10);
		RightShoulder2.setRotationPoint(-2.5F, -7F, 0F);
		RightShoulder2.setTextureSize(64, 64);
		RightShoulder2.mirror = true;
		setRotation(RightShoulder2, 0F, 0F, 0.6108652F);
		RightShoulder2.mirror = false;

		RightShoulder1 = new AM2ModelRenderer(this, 0, 31);
		RightShoulder1.addBox(-7F, -1F, -4F, 8, 8, 8);
		RightShoulder1.setRotationPoint(-2.5F, -7F, 0F);
		RightShoulder1.setTextureSize(64, 64);
		RightShoulder1.mirror = true;
		setRotation(RightShoulder1, 0F, 0F, 0.6108652F);
		RightShoulder1.mirror = false;

		Rock1 = new AM2ModelRenderer(this, 0, 31);
		Rock1.addBox(-2F, 17.76F, -4F, 7, 8, 8);
		Rock1.setRotationPoint(2.5F, -7F, 0F);
		Rock1.setTextureSize(64, 64);
		Rock1.mirror = true;
		setRotation(Rock1, 0F, 0F, 0F);
		Rock2 = new AM2ModelRenderer(this, 1, 32);
		Rock2.addBox(-10F, 18.76F, -4F, 8, 6, 7);
		Rock2.setRotationPoint(2.5F, -7F, 0F);
		Rock2.setTextureSize(64, 64);
		Rock2.mirror = true;
		setRotation(Rock2, 0F, 0F, 0F);
		Rock3 = new AM2ModelRenderer(this, 1, 32);
		Rock3.addBox(-6F, 16.76F, -2F, 8, 6, 7);
		Rock3.setRotationPoint(2.5F, -7F, 0F);
		Rock3.setTextureSize(64, 64);
		Rock3.mirror = true;
		setRotation(Rock3, 0F, 0F, 0F);
		Rock3.mirror = false;

		RightShoulder1.storeRestRotations();
		RightShoulder2.storeRestRotations();
		RightArmUpper.storeRestRotations();
		RightArmLower.storeRestRotations();

		LeftShoulder1.storeRestRotations();
		LeftShoulder2.storeRestRotations();
		LeftArm1.storeRestRotations();
		LeftArm2.storeRestRotations();

		Rod1.storeRestRotations();
		Rod2.storeRestRotations();
		Rod3.storeRestRotations();
		Rod4.storeRestRotations();
		RodMain.storeRestRotations();

		Rock1.storeRestRotations();
		Rock2.storeRestRotations();
		Rock3.storeRestRotations();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		if (entity instanceof EntityEarthGuardian){

			GL11.glPushMatrix();
			GL11.glScalef(1.5f, 1.5f, 1.5f);
			GL11.glTranslatef(0, -0.5f, 0);
			setHeadRotations(f3, f4);
			setRotations((EntityEarthGuardian)entity, f, f1, f2, f3, f4, f5);

			float bodyOffset = (float)Math.sin(f2 / 7f) / 15f;

			GL11.glPushMatrix();
			GL11.glTranslatef(0, bodyOffset, 0);

//			Core2.render(f5);

			Core1.render(f5);
			LeftShoulder1.render(f5);
			Shoulders.render(f5);
			RightArmUpper.render(f5);
			LeftArm1.render(f5);
			RightArmLower.render(f5);
			LeftArm2.render(f5);
			Neck.render(f5);
			Head.render(f5);
			LeftShoulder2.render(f5);
			RightShoulder2.render(f5);
			RightShoulder1.render(f5);

			GL11.glPushMatrix();
			GL11.glTranslatef(0, -bodyOffset / 2, 0);

			RodMain.render(f5);
			Rod3.render(f5);
			Rod4.render(f5);
			Rod2.render(f5);
			Rod1.render(f5);

			if (((EntityEarthGuardian)entity).shouldRenderRock()){
				Rock1.render(f5);
				Rock2.render(f5);
				Rock3.render(f5);
			}

			GL11.glPopMatrix();
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void setRotations(EntityEarthGuardian guardian, float f, float f1, float f2, float f3, float f4, float f5){
		float ticksInCurrentAction = guardian.getTicksInCurrentAction() + (f2 - guardian.ticksExisted);
		Rod1.rotateAngleY = guardian.getRodRotations() + (f2 - guardian.ticksExisted) * 0.02f;
		Rod2.rotateAngleY = guardian.getRodRotations() + (f2 - guardian.ticksExisted) * 0.02f;
		Rod3.rotateAngleY = guardian.getRodRotations() + (f2 - guardian.ticksExisted) * 0.02f;
		Rod4.rotateAngleY = guardian.getRodRotations() + (f2 - guardian.ticksExisted) * 0.02f;
		RodMain.rotateAngleY = guardian.getRodRotations() + (f2 - guardian.ticksExisted) * 0.02f;

		float left_arm_rotation_x = 0;
		float right_arm_rotation_x = 0;

		float left_arm_rotation_y = 0;
		float right_arm_rotation_y = 0;

		float left_arm_rotation_z = 0;
		float right_arm_rotation_z = 0;

		float left_shoulder_rotation_z = 0;
		float right_shoulder_rotation_z = 0;

		switch (guardian.getCurrentAction()){
		case SMASH:
			float max_degrees_x = 120;
			float final_degrees = 90;
			float action_ticks = 16;
			float fast_action_ticks = 4;
			float hold_ticks = 10;
			float rise_ticks = 10;
			if (ticksInCurrentAction < action_ticks){
				left_arm_rotation_x = (float)Math.toRadians(-max_degrees_x * ((float)ticksInCurrentAction / action_ticks));
			}else if (ticksInCurrentAction < (action_ticks + fast_action_ticks)){
				float pct = ((ticksInCurrentAction - action_ticks) / fast_action_ticks);
				float degrees = -max_degrees_x + (final_degrees * pct);
				left_arm_rotation_x = (float)Math.toRadians(degrees);
				GL11.glRotatef((max_degrees_x + degrees) / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f * pct, 0);
			}else if (ticksInCurrentAction < (action_ticks + fast_action_ticks + hold_ticks)){
				left_arm_rotation_x = (float)Math.toRadians(-final_degrees);
				GL11.glRotatef(final_degrees / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f, 0);
			}else{
				float pct = 1.0f - ((ticksInCurrentAction - action_ticks - fast_action_ticks - hold_ticks) / rise_ticks);
				float degrees = -max_degrees_x + (final_degrees * pct);
				float degrees2 = -max_degrees_x + (final_degrees * (1.0f - pct));
				left_arm_rotation_x = (float)Math.toRadians(degrees2);
				GL11.glRotatef((max_degrees_x + degrees) / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f * pct, 0);
			}
			right_arm_rotation_x = left_arm_rotation_x;
			right_arm_rotation_y = left_arm_rotation_y = left_arm_rotation_x / 2;
			left_shoulder_rotation_z = left_arm_rotation_z;
			right_shoulder_rotation_z = right_arm_rotation_z;
			break;
		case STRIKE:
			max_degrees_x = 50;
			action_ticks = 4;
			fast_action_ticks = 11;
			if (ticksInCurrentAction < action_ticks){
				left_arm_rotation_z = (float)Math.toRadians(-max_degrees_x * ((float)ticksInCurrentAction / action_ticks));
			}else if (ticksInCurrentAction < (action_ticks + fast_action_ticks)){
				float pct = ((ticksInCurrentAction - action_ticks) / fast_action_ticks);
				float degrees = -max_degrees_x + (max_degrees_x * pct);
				left_arm_rotation_z = (float)Math.toRadians(degrees);
				GL11.glRotatef(guardian.leftArm ? 360 * pct : -360 * pct, 0, 1, 0);
			}
			if (!guardian.leftArm){
				right_arm_rotation_z = -left_arm_rotation_z;
				right_shoulder_rotation_z = left_arm_rotation_z;
				left_arm_rotation_z = 0;
			}else{
				left_shoulder_rotation_z = left_arm_rotation_z;
			}
			break;
		case THROWING_ROCK:
			max_degrees_x = 120;
			final_degrees = 60;
			action_ticks = 8;
			fast_action_ticks = 10;
			hold_ticks = 7;
			rise_ticks = 5;
			if (ticksInCurrentAction < action_ticks){
				float pct = (ticksInCurrentAction / action_ticks);
				float degrees = -max_degrees_x * pct;
				left_arm_rotation_x = (float)Math.toRadians(degrees);
				GL11.glRotatef(-degrees / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f * pct, 0);

			}else if (ticksInCurrentAction < (action_ticks + fast_action_ticks)){
				float pct = 1.0f - ((ticksInCurrentAction - action_ticks) / fast_action_ticks);
				float degrees = (-final_degrees * (1.0f - pct)) - max_degrees_x;
				left_arm_rotation_x = (float)Math.toRadians(degrees);
				GL11.glRotatef((max_degrees_x * pct) / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f * pct, 0);
			}else if (ticksInCurrentAction < (action_ticks + fast_action_ticks + hold_ticks)){
				float degrees = -final_degrees - max_degrees_x;
				left_arm_rotation_x = (float)Math.toRadians(degrees);
			}else{
				float pct = 1.0f - ((ticksInCurrentAction - action_ticks - fast_action_ticks - hold_ticks) / rise_ticks);
				float degrees = (-final_degrees - max_degrees_x) * pct;
				left_arm_rotation_x = (float)Math.toRadians(degrees);
			}
			right_arm_rotation_x = left_arm_rotation_x;
			right_arm_rotation_y = left_arm_rotation_y = left_arm_rotation_x / 4;
			break;
		}

		LeftArm1.rotateAngleX = LeftArm1.getRestRotationX() + left_arm_rotation_x;
		LeftArm2.rotateAngleX = LeftArm2.getRestRotationX() + left_arm_rotation_x;

		LeftArm1.rotateAngleZ = LeftArm1.getRestRotationZ() + left_arm_rotation_z;
		LeftArm2.rotateAngleZ = LeftArm2.getRestRotationZ() + left_arm_rotation_z;

		LeftShoulder1.rotateAngleX = LeftShoulder1.getRestRotationX() + left_arm_rotation_x;
		LeftShoulder2.rotateAngleX = LeftShoulder2.getRestRotationX() + left_arm_rotation_x;

		LeftShoulder1.rotateAngleY = LeftShoulder1.getRestRotationY() + left_arm_rotation_y;
		LeftShoulder2.rotateAngleY = LeftShoulder2.getRestRotationY() + left_arm_rotation_y;

		LeftShoulder1.rotateAngleZ = LeftShoulder1.getRestRotationZ() + left_shoulder_rotation_z;
		LeftShoulder2.rotateAngleZ = LeftShoulder2.getRestRotationZ() + left_shoulder_rotation_z;

		RightArmUpper.rotateAngleX = RightArmUpper.getRestRotationX() + right_arm_rotation_x;
		RightArmLower.rotateAngleX = RightArmLower.getRestRotationX() + right_arm_rotation_x;

		RightArmUpper.rotateAngleZ = RightArmUpper.getRestRotationZ() + right_arm_rotation_z;
		RightArmLower.rotateAngleZ = RightArmLower.getRestRotationZ() + right_arm_rotation_z;

		RightShoulder1.rotateAngleX = RightShoulder1.getRestRotationX() + right_arm_rotation_x;
		RightShoulder2.rotateAngleX = RightShoulder2.getRestRotationX() + right_arm_rotation_x;

		RightShoulder1.rotateAngleY = RightShoulder1.getRestRotationY() - right_arm_rotation_y;
		RightShoulder2.rotateAngleY = RightShoulder2.getRestRotationY() - right_arm_rotation_y;

		RightShoulder1.rotateAngleZ = RightShoulder1.getRestRotationZ() - right_shoulder_rotation_z;
		RightShoulder2.rotateAngleZ = RightShoulder2.getRestRotationZ() - right_shoulder_rotation_z;

		Rock1.rotateAngleX = Rock1.getRestRotationX() + right_arm_rotation_x;
		Rock2.rotateAngleX = Rock2.getRestRotationX() + right_arm_rotation_x;
		Rock3.rotateAngleX = Rock3.getRestRotationX() + right_arm_rotation_x;
	}

	private void setHeadRotations(float yaw, float pitch){
		Head.rotateAngleX = (float)Math.toRadians(pitch);
		Head.rotateAngleY = (float)Math.toRadians(yaw);
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
