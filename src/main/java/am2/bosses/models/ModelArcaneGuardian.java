package am2.bosses.models;

import am2.bosses.EntityArcaneGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelArcaneGuardian extends ModelBase{
	//fields
	AM2ModelRenderer FootLeft;
	AM2ModelRenderer FootRight;
	AM2ModelRenderer PantsUpper;
	AM2ModelRenderer Chest;
	AM2ModelRenderer Spine;
	AM2ModelRenderer Cube1;
	AM2ModelRenderer Cube2;
	AM2ModelRenderer Cube3;
	AM2ModelRenderer Cube4;
	AM2ModelRenderer PantsFront;
	AM2ModelRenderer PantsBackLeft;
	AM2ModelRenderer PantsBackRight;
	AM2ModelRenderer PantsLeft;
	AM2ModelRenderer RightUpperArm;
	AM2ModelRenderer LeftUpperArm;
	AM2ModelRenderer RightForearm;
	AM2ModelRenderer LeftLowerArm;
	AM2ModelRenderer LeftHand;
	AM2ModelRenderer Book;
	AM2ModelRenderer RightHand;
	AM2ModelRenderer Wand;
	AM2ModelRenderer Head;
	AM2ModelRenderer Neck;
	AM2ModelRenderer HoodRight;
	AM2ModelRenderer HoodLeft;
	AM2ModelRenderer HoodBack;
	AM2ModelRenderer HoodTop;
	AM2ModelRenderer Sigil;
	AM2ModelRenderer SigilHelper;
	AM2ModelRenderer PantsRight;

	public ModelArcaneGuardian(){
		textureWidth = 128;
		textureHeight = 128;

		FootLeft = new AM2ModelRenderer(this, 120, 68);
		FootLeft.addBox(-1F, 0F, -1F, 2, 12, 2);
		FootLeft.setRotationPoint(2F, 0F, -1F);
		FootLeft.setTextureSize(128, 128);
		FootLeft.mirror = true;
		setRotation(FootLeft, 0.122173F, 0F, 0F);
		FootRight = new AM2ModelRenderer(this, 120, 68);
		FootRight.addBox(-1F, 0F, -1F, 2, 12, 2);
		FootRight.setRotationPoint(-2F, 0F, -1F);
		FootRight.setTextureSize(128, 128);
		FootRight.mirror = true;
		setRotation(FootRight, 0.2617994F, 0F, 0F);
		PantsUpper = new AM2ModelRenderer(this, 104, 83);
		PantsUpper.addBox(-4F, -4F, -2F, 8, 4, 4);
		PantsUpper.setRotationPoint(0F, 0F, -1F);
		PantsUpper.setTextureSize(128, 128);
		PantsUpper.mirror = true;
		setRotation(PantsUpper, 0F, 0F, 0F);
		Chest = new AM2ModelRenderer(this, 104, 59);
		Chest.addBox(-4F, -4F, -2F, 8, 4, 4);
		Chest.setRotationPoint(0F, -8F, -1F);
		Chest.setTextureSize(128, 128);
		Chest.mirror = true;
		setRotation(Chest, 0F, 0F, 0F);
		Spine = new AM2ModelRenderer(this, 103, 8);
		Spine.addBox(-1F, -3F, -1F, 2, 6, 2);
		Spine.setRotationPoint(0F, -6F, -1F);
		Spine.setTextureSize(128, 128);
		Spine.mirror = true;
		setRotation(Spine, 0F, 0F, 0F);
		Cube1 = new AM2ModelRenderer(this, 118, 1);
		Cube1.addBox(-2F, -2F, -0.5F, 4, 4, 1);
		Cube1.setRotationPoint(0F, -6F, -1F);
		Cube1.setTextureSize(128, 128);
		Cube1.mirror = true;
		setRotation(Cube1, 0F, 0F, -0.7853982F);
		Cube2 = new AM2ModelRenderer(this, 101, 0);
		Cube2.addBox(-0.5F, -2F, -2F, 1, 4, 4);
		Cube2.setRotationPoint(0F, -6F, -1F);
		Cube2.setTextureSize(128, 128);
		Cube2.mirror = true;
		setRotation(Cube2, -0.7853982F, 0F, 0F);
		Cube3 = new AM2ModelRenderer(this, 101, 0);
		Cube3.addBox(-0.5F, -2F, -2F, 1, 4, 4);
		Cube3.setRotationPoint(0F, -6F, -1F);
		Cube3.setTextureSize(128, 128);
		Cube3.mirror = true;
		setRotation(Cube3, -0.7853982F, -0.7853982F, 0F);
		Cube4 = new AM2ModelRenderer(this, 101, 0);
		Cube4.addBox(-0.5F, -2F, -2F, 1, 4, 4);
		Cube4.setRotationPoint(0F, -6F, -1F);
		Cube4.setTextureSize(128, 128);
		Cube4.mirror = true;
		setRotation(Cube4, -0.7853982F, 0.7853982F, 0F);
		PantsFront = new AM2ModelRenderer(this, 110, 105);
		PantsFront.addBox(-4F, 0F, -1F, 8, 9, 1);
		PantsFront.setRotationPoint(0F, 0F, -2F);
		PantsFront.setTextureSize(128, 128);
		PantsFront.mirror = true;
		setRotation(PantsFront, -0.0019622F, 0F, 0F);
		PantsBackLeft = new AM2ModelRenderer(this, 99, 105);
		PantsBackLeft.addBox(-2F, 0F, 0F, 4, 9, 1);
		PantsBackLeft.setRotationPoint(2F, 0F, 0F);
		PantsBackLeft.setTextureSize(128, 128);
		PantsBackLeft.mirror = true;
		setRotation(PantsBackLeft, 0.1570796F, 0.1919862F, 0.0349066F);
		PantsBackRight = new AM2ModelRenderer(this, 88, 105);
		PantsBackRight.addBox(-2F, 0F, 0F, 4, 9, 1);
		PantsBackRight.setRotationPoint(-2F, 0F, 0F);
		PantsBackRight.setTextureSize(128, 128);
		PantsBackRight.mirror = true;
		setRotation(PantsBackRight, 0.3141593F, 0.0698132F, 0F);
		PantsLeft = new AM2ModelRenderer(this, 121, 92);
		PantsLeft.addBox(-1F, 0F, -1.5F, 1, 9, 3);
		PantsLeft.setRotationPoint(4F, 0F, -1F);
		PantsLeft.setTextureSize(128, 128);
		PantsLeft.mirror = true;
		setRotation(PantsLeft, 0.2094395F, 0F, 0F);
		RightUpperArm = new AM2ModelRenderer(this, 103, 68);
		RightUpperArm.addBox(-4F, -2F, -2F, 4, 5, 4);
		RightUpperArm.setRotationPoint(-4F, -10F, -1F);
		RightUpperArm.setTextureSize(128, 128);
		RightUpperArm.mirror = true;
		//setRotation(RightUpperArm, -0.6457718F, 0F, 0.4363323F);
		setRotation(RightUpperArm, 0F, 0F, 0.4363323F);
		LeftUpperArm = new AM2ModelRenderer(this, 103, 68);
		LeftUpperArm.addBox(0F, -2F, -2F, 4, 5, 4);
		LeftUpperArm.setRotationPoint(4F, -10F, -1F);
		LeftUpperArm.setTextureSize(128, 128);
		LeftUpperArm.mirror = true;
		setRotation(LeftUpperArm, -0.6108652F, 0F, 0F);
		RightForearm = new AM2ModelRenderer(this, 69, 68);
		RightForearm.addBox(-5F, 1F, -1.3F, 4, 5, 4);
		RightForearm.setRotationPoint(-4F, -10F, -1F);
		RightForearm.setTextureSize(128, 128);
		RightForearm.mirror = true;
		//setRotation(RightForearm, -1.27409F, -0.2268928F, 0.122173F);
		setRotation(RightForearm, 0F, -0.2268928F, 0.122173F);
		LeftLowerArm = new AM2ModelRenderer(this, 86, 68);
		LeftLowerArm.addBox(0F, -2F, -2F, 4, 5, 4);
		LeftLowerArm.setRotationPoint(6F, -8F, -3F);
		LeftLowerArm.setTextureSize(128, 128);
		LeftLowerArm.mirror = true;
		setRotation(LeftLowerArm, 1.570796F, -1.553343F, 2.181662F);
		LeftHand = new AM2ModelRenderer(this, 113, 78);
		LeftHand.addBox(-1F, -1F, -1F, 1, 2, 2);
		LeftHand.setRotationPoint(3F, -6.5F, -4F);
		LeftHand.setTextureSize(128, 128);
		LeftHand.mirror = true;
		setRotation(LeftHand, -0.7853982F, 0F, 0F);
		Book = new AM2ModelRenderer(this, 91, 120);
		Book.addBox(-4F, -3F, -1F, 4, 6, 2);
		Book.setRotationPoint(2F, -6F, -4F);
		Book.setTextureSize(128, 128);
		Book.mirror = true;
		setRotation(Book, 0F, 0F, 0F);
		RightHand = new AM2ModelRenderer(this, 106, 78);
		RightHand.addBox(-4.5F, 0F, -7F, 2, 2, 1);
		RightHand.setRotationPoint(-4F, -10F, -1F);
		RightHand.setTextureSize(128, 128);
		RightHand.mirror = true;
		//setRotation(RightHand, 0.2443461F, -0.296706F, 0.1745329F);
		setRotation(RightHand, 1.5707964F, -0.296706F, 0.1745329F);
		Wand = new AM2ModelRenderer(this, 98, 97);
		Wand.addBox(-2.5F, 1.5F, -13F, 1, 1, 6);
		Wand.setRotationPoint(-4F, -10F, -1F);
		Wand.setTextureSize(128, 128);
		Wand.mirror = true;
		//setRotation(Wand, 0.0523599F, -0.1396263F, 0.2094395F);
		setRotation(Wand, 1.5707964F, -0.1396263F, 0.2094395F);
		Head = new AM2ModelRenderer(this, 100, 44);
		Head.addBox(-3.5F, -8F, -3.5F, 7, 7, 7);
		Head.setRotationPoint(0F, -12F, -1F);
		Head.setTextureSize(128, 128);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);
		Neck = new AM2ModelRenderer(this, 95, 116);
		Neck.addBox(-1F, -1F, -1F, 2, 1, 2);
		Neck.setRotationPoint(0F, -12F, -1F);
		Neck.setTextureSize(128, 128);
		Neck.mirror = true;
		setRotation(Neck, 0F, 0F, 0F);
		HoodRight = new AM2ModelRenderer(this, 91, 17);
		HoodRight.addBox(-4.5F, -8F, -4F, 1, 8, 8);
		HoodRight.setRotationPoint(0F, -12F, -1F);
		HoodRight.setTextureSize(128, 128);
		HoodRight.mirror = true;
		setRotation(HoodRight, 0F, 0F, 0F);
		HoodLeft = new AM2ModelRenderer(this, 110, 17);
		HoodLeft.addBox(3.5F, -8F, -4F, 1, 8, 8);
		HoodLeft.setRotationPoint(0F, -12F, -1F);
		HoodLeft.setTextureSize(128, 128);
		HoodLeft.mirror = true;
		setRotation(HoodLeft, 0F, 0F, 0F);
		HoodBack = new AM2ModelRenderer(this, 112, 7);
		HoodBack.addBox(-3.5F, -8F, 3F, 7, 8, 1);
		HoodBack.setRotationPoint(0F, -12F, -1F);
		HoodBack.setTextureSize(128, 128);
		HoodBack.mirror = true;
		setRotation(HoodBack, 0F, 0F, 0F);
		HoodTop = new AM2ModelRenderer(this, 94, 34);
		HoodTop.addBox(-4.5F, -9F, -4F, 9, 1, 8);
		HoodTop.setRotationPoint(0F, -12F, -1F);
		HoodTop.setTextureSize(128, 128);
		HoodTop.mirror = true;
		setRotation(HoodTop, 0F, 0F, 0F);
		Sigil = new AM2ModelRenderer(this, 0, 0);
		Sigil.addBox(-22F, -22F, 7F, 44, 44, 0);
		Sigil.setRotationPoint(0F, 0F, 0F);
		Sigil.setTextureSize(128, 128);
		Sigil.mirror = true;
		setRotation(Sigil, 0F, 0F, 0F);
		SigilHelper = new AM2ModelRenderer(this, 0, 0);
		SigilHelper.setRotationPoint(0F, 0F, 0F);
		setRotation(SigilHelper, 0F, 0F, 0F);
		SigilHelper.addChild(Sigil);
		PantsRight = new AM2ModelRenderer(this, 113, 92);
		PantsRight.addBox(-1F, 0F, -1.5F, 1, 9, 3);
		PantsRight.setRotationPoint(-3F, 0F, -1F);
		PantsRight.setTextureSize(128, 128);
		PantsRight.mirror = true;
		setRotation(PantsRight, 0.2094395F, 0F, 0F);

		Cube1.storeRestRotations();
		Cube2.storeRestRotations();
		Cube3.storeRestRotations();
		Cube4.storeRestRotations();
		Sigil.storeRestRotations();
		SigilHelper.storeRestRotations();

		RightForearm.storeRestRotations();
		RightUpperArm.storeRestRotations();
		RightHand.storeRestRotations();
		Wand.storeRestRotations();

		PantsFront.storeRestRotations();
		PantsBackLeft.storeRestRotations();
		PantsBackRight.storeRestRotations();
		PantsLeft.storeRestRotations();
		PantsRight.storeRestRotations();
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){

		if (entity instanceof EntityArcaneGuardian){
			GL11.glPushMatrix();
			GL11.glTranslatef(0, (float)Math.sin(f2 / 10f) / 10f, 0);
			setHeadRotations(f3, f4);

			updateRotations((EntityArcaneGuardian)entity, f, f1, f2, f3, f4, f5);

			FootLeft.render(f5);
			FootRight.render(f5);
			PantsUpper.render(f5);
			Chest.render(f5);
			Spine.render(f5);
			Cube1.render(f5);
			Cube2.render(f5);
			Cube3.render(f5);
			Cube4.render(f5);
			PantsFront.render(f5);
			PantsBackLeft.render(f5);
			PantsBackRight.render(f5);
			PantsLeft.render(f5);
			RightUpperArm.render(f5);
			LeftUpperArm.render(f5);
			RightForearm.render(f5);
			LeftLowerArm.render(f5);
			LeftHand.render(f5);
			Book.render(f5);
			RightHand.render(f5);
			Wand.render(f5);
			Head.render(f5);
			Neck.render(f5);
			HoodRight.render(f5);
			HoodLeft.render(f5);
			HoodBack.render(f5);
			HoodTop.render(f5);
			PantsRight.render(f5);
//			Sigil.render(f5);
			GL11.glPushMatrix();
			GL11.glTranslatef(0, (float)Math.sin(f2 / 10f) / 10f - 0.25f, 0);
			SigilHelper.render(f5);
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

	private void setHeadRotations(float yaw, float pitch){

		yaw = (float)Math.toRadians(yaw);
		pitch = (float)Math.toRadians(pitch);

		Head.rotateAngleX = pitch;
		Head.rotateAngleY = yaw;

		HoodLeft.rotateAngleX = pitch;
		HoodLeft.rotateAngleY = yaw;

		HoodRight.rotateAngleX = pitch;
		HoodRight.rotateAngleY = yaw;

		HoodTop.rotateAngleX = pitch;
		HoodTop.rotateAngleY = yaw;

		HoodBack.rotateAngleX = pitch;
		HoodBack.rotateAngleY = yaw;
	}

	@SuppressWarnings("incomplete-switch")
	private void updateRotations(EntityArcaneGuardian guardian, float f, float f1, float f2, float f3, float f4, float f5){

		float rotationSpeed = 0.3f;
		if (guardian.getTarget() != null){
			rotationSpeed = 0.085f;
		}

		if (Math.abs(SigilHelper.rotateAngleY - guardian.getRuneRotationY()) < rotationSpeed){
			rotationSpeed = 0;
		}

		if (SigilHelper.rotateAngleY < guardian.getRuneRotationY()){
			rotationSpeed *= -1f;
		}

		Sigil.rotateAngleZ = guardian.getRuneRotationZ() + (f2 / 30f);
		SigilHelper.rotateAngleY = guardian.getRuneRotationY() + ((f2 - guardian.ticksExisted) * rotationSpeed);

		float main_arm_rotation_x = 0;
		float pants_rotation = 0;

		switch (guardian.getCurrentAction()){
		case CASTING:
			float max_degrees_x = 160;
			float final_degrees_x = 80;

			float max_pants_degrees = 45;

			float action_ticks = 10;
			float fast_action_ticks = 3;
			float final_action_ticks = 6;

			if (guardian.getTicksInCurrentAction() < action_ticks){
				main_arm_rotation_x = (float)Math.toRadians(-max_degrees_x * ((guardian.getTicksInCurrentAction() + (f2 - guardian.ticksExisted)) / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < action_ticks + fast_action_ticks){
				main_arm_rotation_x = (float)Math.toRadians(-max_degrees_x + (final_degrees_x * ((guardian.getTicksInCurrentAction() + (f2 - guardian.ticksExisted) - action_ticks) / fast_action_ticks)));
				pants_rotation = (float)Math.toRadians(-max_pants_degrees * ((guardian.getTicksInCurrentAction() + (f2 - guardian.ticksExisted) - action_ticks) / fast_action_ticks));
			}else{
				main_arm_rotation_x = final_degrees_x;
				if (guardian.getTicksInCurrentAction() < action_ticks + fast_action_ticks + final_action_ticks)
					pants_rotation = (float)Math.toRadians(-max_pants_degrees + (max_pants_degrees * ((guardian.getTicksInCurrentAction() + (f2 - guardian.ticksExisted) - action_ticks - fast_action_ticks) / final_action_ticks)));
			}
			break;
		}

		RightUpperArm.rotateAngleX = RightUpperArm.getRestRotationX() + main_arm_rotation_x;
		RightForearm.rotateAngleX = RightForearm.getRestRotationX() + main_arm_rotation_x;
		RightHand.rotateAngleX = RightHand.getRestRotationX() + main_arm_rotation_x;
		Wand.rotateAngleX = Wand.getRestRotationX() + main_arm_rotation_x;

		PantsLeft.rotateAngleZ = PantsLeft.getRestRotationZ() + pants_rotation;
		PantsRight.rotateAngleZ = PantsRight.getRestRotationZ() - pants_rotation;

		PantsFront.rotateAngleX = PantsFront.getRestRotationZ() + pants_rotation;
		PantsBackLeft.rotateAngleX = PantsBackLeft.getRestRotationZ() - pants_rotation;
		PantsBackRight.rotateAngleX = PantsBackRight.getRestRotationZ() - pants_rotation;
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5){
	}

}
