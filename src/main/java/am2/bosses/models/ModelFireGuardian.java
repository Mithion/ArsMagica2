package am2.bosses.models;

import am2.bosses.BossActions;
import am2.bosses.EntityFireGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class ModelFireGuardian extends ModelBase{
	//fields
	AM2ModelRenderer Body9;
	AM2ModelRenderer Body8;
	AM2ModelRenderer Body7;
	AM2ModelRenderer Body6;
	AM2ModelRenderer Body5;
	AM2ModelRenderer Body4;
	AM2ModelRenderer Body3;
	AM2ModelRenderer Body2;
	AM2ModelRenderer Body1;
	AM2ModelRenderer Neck1;
	AM2ModelRenderer Neck2;
	AM2ModelRenderer Neck3;
	AM2ModelRenderer Neck4;
	AM2ModelRenderer Neck5;
	AM2ModelRenderer Neck6;
	AM2ModelRenderer RightArm1;
	AM2ModelRenderer RightArm2;
	AM2ModelRenderer LeftArm1;
	AM2ModelRenderer LeftArm2;
	AM2ModelRenderer Head1;
	AM2ModelRenderer Head6;
	AM2ModelRenderer Head4;
	AM2ModelRenderer Head5;
	AM2ModelRenderer Head7;
	AM2ModelRenderer Head2;
	AM2ModelRenderer Head3;

	public ModelFireGuardian(){
		textureWidth = 128;
		textureHeight = 128;

		Body9 = new AM2ModelRenderer(this, 24, 15);
		Body9.addBox(-0.5F, 0F, -0.5F, 1, 7, 1);
		Body9.setRotationPoint(0F, -3F, 1.5F);
		Body9.setTextureSize(128, 128);
		Body9.mirror = true;
		setRotation(Body9, -0.2617994F, 0F, 0F);
		Body8 = new AM2ModelRenderer(this, 23, 15);
		Body8.addBox(-1F, 0F, -1F, 2, 6, 2);
		Body8.setRotationPoint(0F, -8.5F, 2.5F);
		Body8.setTextureSize(128, 128);
		Body8.mirror = true;
		setRotation(Body8, -0.1745329F, 0F, 0F);
		Body7 = new AM2ModelRenderer(this, 21, 15);
		Body7.addBox(-1.5F, 0F, -1.5F, 3, 5, 3);
		Body7.setRotationPoint(0F, -13F, 3F);
		Body7.setTextureSize(128, 128);
		Body7.mirror = true;
		setRotation(Body7, -0.0872665F, 0F, 0F);
		Body6 = new AM2ModelRenderer(this, 19, 15);
		Body6.addBox(-2F, 0F, -2F, 4, 4, 4);
		Body6.setRotationPoint(0F, -17F, 3F);
		Body6.setTextureSize(128, 128);
		Body6.mirror = true;
		setRotation(Body6, 0F, 0F, 0F);
		Body5 = new AM2ModelRenderer(this, 16, 14);
		Body5.addBox(-3F, 0F, -3F, 6, 3, 6);
		Body5.setRotationPoint(0F, -20F, 3F);
		Body5.setTextureSize(128, 128);
		Body5.mirror = true;
		setRotation(Body5, 0.0872665F, 0F, 0F);
		Body4 = new AM2ModelRenderer(this, 12, 13);
		Body4.addBox(-4F, -2F, -4F, 8, 2, 8);
		Body4.setRotationPoint(0F, -19.5F, 2.5F);
		Body4.setTextureSize(128, 128);
		Body4.mirror = true;
		setRotation(Body4, 0.1745329F, 0F, 0F);
		Body3 = new AM2ModelRenderer(this, 8, 10);
		Body3.addBox(-5F, -3F, -5F, 10, 3, 10);
		Body3.setRotationPoint(0F, -21F, 2F);
		Body3.setTextureSize(128, 128);
		Body3.mirror = true;
		setRotation(Body3, 0.2617994F, 0F, 0F);
		Body2 = new AM2ModelRenderer(this, 4, 7);
		Body2.addBox(-6F, -4F, -6F, 12, 4, 12);
		Body2.setRotationPoint(0F, -23F, 1F);
		Body2.setTextureSize(128, 128);
		Body2.mirror = true;
		setRotation(Body2, 0.3490659F, 0F, 0F);
		Body1 = new AM2ModelRenderer(this, 0, 3);
		Body1.addBox(-7F, -6F, -7F, 14, 6, 14);
		Body1.setRotationPoint(0F, -26F, 0F);
		Body1.setTextureSize(128, 128);
		Body1.mirror = true;
		setRotation(Body1, 0.4363323F, 0F, 0F);
		Neck1 = new AM2ModelRenderer(this, 63, 115);
		Neck1.addBox(-6F, -1F, -6F, 12, 1, 12);
		Neck1.setRotationPoint(0F, -31F, -3F);
		Neck1.setTextureSize(128, 128);
		Neck1.mirror = true;
		setRotation(Neck1, 0.5061455F, 0F, 0F);
		Neck2 = new AM2ModelRenderer(this, 67, 117);
		Neck2.addBox(-5F, -1F, -5F, 10, 1, 10);
		Neck2.setRotationPoint(0F, -31.5F, -4F);
		Neck2.setTextureSize(128, 128);
		Neck2.mirror = true;
		setRotation(Neck2, 0.5585054F, 0F, 0F);
		Neck3 = new AM2ModelRenderer(this, 71, 119);
		Neck3.addBox(-4F, -1F, -4F, 8, 1, 8);
		Neck3.setRotationPoint(0F, -32.5F, -4.5F);
		Neck3.setTextureSize(128, 128);
		Neck3.mirror = true;
		setRotation(Neck3, 0.5934119F, 0F, 0F);
		Neck4 = new AM2ModelRenderer(this, 75, 121);
		Neck4.addBox(-3F, -1F, -3F, 6, 1, 6);
		Neck4.setRotationPoint(0F, -33.5F, -5F);
		Neck4.setTextureSize(128, 128);
		Neck4.mirror = true;
		setRotation(Neck4, 0.6632251F, 0F, 0F);
		Neck5 = new AM2ModelRenderer(this, 79, 123);
		Neck5.addBox(-2F, -1F, -2F, 4, 1, 4);
		Neck5.setRotationPoint(0F, -34F, -6F);
		Neck5.setTextureSize(128, 128);
		Neck5.mirror = true;
		setRotation(Neck5, 0.7853982F, 0F, 0F);
		Neck6 = new AM2ModelRenderer(this, 83, 125);
		Neck6.addBox(-1F, -1F, -1F, 2, 1, 2);
		Neck6.setRotationPoint(0F, -34.5F, -7F);
		Neck6.setTextureSize(128, 128);
		Neck6.mirror = true;
		setRotation(Neck6, 0.9075712F, 0F, 0F);
		RightArm1 = new AM2ModelRenderer(this, 33, 110);
		RightArm1.addBox(-2F, -1F, -1F, 2, 16, 2);
		RightArm1.setRotationPoint(-8F, -29F, -1F);
		RightArm1.setTextureSize(128, 128);
		RightArm1.mirror = true;
		setRotation(RightArm1, 0F, 0F, 0F);
		RightArm2 = new AM2ModelRenderer(this, 42, 108);
		RightArm2.addBox(-4F, 15F, -2F, 6, 16, 4);
		RightArm2.setRotationPoint(-8F, -29F, -1F);
		RightArm2.setTextureSize(128, 128);
		RightArm2.mirror = true;
		setRotation(RightArm2, 0F, 0F, 0F);
		LeftArm1 = new AM2ModelRenderer(this, 33, 110);
		LeftArm1.addBox(0F, -1F, -1F, 2, 16, 2);
		LeftArm1.setRotationPoint(8F, -29F, -1F);
		LeftArm1.setTextureSize(128, 128);
		LeftArm1.mirror = true;
		setRotation(LeftArm1, 0F, 0F, 0F);
		LeftArm1.mirror = false;
		LeftArm2 = new AM2ModelRenderer(this, 42, 108);
		LeftArm2.addBox(-2F, 15F, -2F, 6, 16, 4);
		LeftArm2.setRotationPoint(8F, -29F, -1F);
		LeftArm2.setTextureSize(128, 128);
		LeftArm2.mirror = true;
		setRotation(LeftArm2, 0F, 0F, 0F);
		LeftArm2.mirror = false;
		Head1 = new AM2ModelRenderer(this, 63, 43);
		Head1.addBox(-4F, -3F, -8F, 8, 5, 8);
		Head1.setRotationPoint(0F, -35F, -8F);
		Head1.setTextureSize(128, 128);
		Head1.mirror = true;
		setRotation(Head1, 0F, 0F, 0F);
		Head6 = new AM2ModelRenderer(this, 63, 65);
		Head6.addBox(4F, -1.5F, -5F, 2, 3, 6);
		Head6.setRotationPoint(0F, -35F, -8F);
		Head6.setTextureSize(128, 128);
		Head6.mirror = true;
		setRotation(Head6, 0F, 0F, 0F);
		Head4 = new AM2ModelRenderer(this, 63, 65);
		Head4.addBox(-6F, -1.5F, -5F, 2, 3, 6);
		Head4.setRotationPoint(0F, -35F, -8F);
		Head4.setTextureSize(128, 128);
		Head4.mirror = true;
		setRotation(Head4, 0F, 0F, 0F);
		Head5 = new AM2ModelRenderer(this, 63, 57);
		Head5.addBox(-5.5F, -1F, 1F, 1, 2, 5);
		Head5.setRotationPoint(0F, -35F, -8F);
		Head5.setTextureSize(128, 128);
		Head5.mirror = true;
		setRotation(Head5, 0F, 0F, 0F);
		Head7 = new AM2ModelRenderer(this, 63, 57);
		Head7.addBox(4.5F, -1F, 1F, 1, 2, 5);
		Head7.setRotationPoint(0F, -35F, -8F);
		Head7.setTextureSize(128, 128);
		Head7.mirror = true;
		setRotation(Head7, 0F, 0F, 0F);
		Head2 = new AM2ModelRenderer(this, 14, 16);
		Head2.addBox(-3.5F, -2F, -11F, 7, 4, 3);
		Head2.setRotationPoint(0F, -35F, -8F);
		Head2.setTextureSize(128, 128);
		Head2.mirror = true;
		setRotation(Head2, 0F, 0F, 0F);
		Head3 = new AM2ModelRenderer(this, 63, 29);
		Head3.addBox(-2.5F, -1.5F, -13F, 5, 3, 2);
		Head3.setRotationPoint(0F, -35F, -8F);
		Head3.setTextureSize(128, 128);
		Head3.mirror = true;
		setRotation(Head3, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){

		if (entity instanceof EntityFireGuardian){
			if (((EntityFireGuardian)entity).getIsUnderground() && ((EntityFireGuardian)entity).getCurrentAction() != BossActions.SPINNING)
				return;
			GL11.glPushMatrix();
			setHeadRotations(f3, f4);
			setModelRotations((EntityFireGuardian)entity, f, f1, f2, f3, f4, f5);
			Body9.render(f5);
			Body8.render(f5);
			Body7.render(f5);
			Body6.render(f5);
			Body5.render(f5);
			Body4.render(f5);
			Body3.render(f5);
			Body2.render(f5);
			Body1.render(f5);
			Neck1.render(f5);
			Neck2.render(f5);
			Neck3.render(f5);
			Neck4.render(f5);
			Neck5.render(f5);
			Neck6.render(f5);
			RightArm1.render(f5);
			RightArm2.render(f5);
			LeftArm1.render(f5);
			LeftArm2.render(f5);
			Head1.render(f5);
			Head6.render(f5);
			Head4.render(f5);
			Head5.render(f5);
			Head7.render(f5);
			Head2.render(f5);
			Head3.render(f5);
			GL11.glPopMatrix();
		}
	}

	private void setHeadRotations(float pitch, float yaw){
		yaw = (float)Math.toRadians(yaw);
		pitch = (float)Math.toRadians(pitch);

		setRotation(Head1, pitch, yaw, 0);
		setRotation(Head2, pitch, yaw, 0);
		setRotation(Head3, pitch, yaw, 0);
		setRotation(Head4, pitch, yaw, 0);
		setRotation(Head5, pitch, yaw, 0);
		setRotation(Head6, pitch, yaw, 0);
		setRotation(Head7, pitch, yaw, 0);
	}

	@SuppressWarnings("incomplete-switch")
	private void setModelRotations(EntityFireGuardian entity, float f, float f1, float f2, float f3, float f4, float f5){

		float entityOffset = 0;
		float leftArmRotation = 0;
		float rightArmRotation = 0;

		switch (entity.getCurrentAction()){
		case SPINNING:
			/*entityOffset = (float) Math.cos((entity.getTicksInCurrentAction() + 10) / 10f) * 5;
			GL11.glTranslatef(0, entityOffset, 0);*/

			float stage = 35;

			if (!entity.getIsUnderground()){
				if (entity.getTicksInCurrentAction() > stage){
					float offset = 3 * ((entity.getTicksInCurrentAction() + (f2 - entity.ticksExisted) - stage) / 5);
					GL11.glTranslatef(0, -offset, 0);
				}
			}else{
				GL11.glTranslatef(0, 2, 0);
			}

			stage = 10;

			if (entity.getTicksInCurrentAction() > stage){
				float rotation = 180 * ((entity.getTicksInCurrentAction() + (f2 - entity.ticksExisted) - stage) / 20);
				if (rotation > 180) rotation = 180;
				Vec3 look = entity.getLook(1.0f);
				GL11.glRotatef(rotation, 0.5f, 0, 0);
			}


			rightArmRotation = leftArmRotation = (float)Math.toRadians(180);
			break;
		case LONG_CASTING:
			float max_degrees_x = 160;
			float final_degrees_x = 80;

			float max_pants_degrees = 45;

			float action_ticks = 10;
			float fast_action_ticks = 3;
			float final_action_ticks = 6;

			if (entity.getTicksInCurrentAction() < action_ticks){
				rightArmRotation = (float)Math.toRadians(-max_degrees_x * ((entity.getTicksInCurrentAction() + (f2 - entity.ticksExisted)) / action_ticks));
			}else if (entity.getTicksInCurrentAction() < action_ticks + fast_action_ticks){
				rightArmRotation = (float)Math.toRadians(-max_degrees_x + (final_degrees_x * ((entity.getTicksInCurrentAction() + (f2 - entity.ticksExisted) - action_ticks) / fast_action_ticks)));
			}else{
				rightArmRotation = final_degrees_x;
			}

			leftArmRotation = rightArmRotation;

			break;
		case CASTING:
			max_degrees_x = 160;
			final_degrees_x = 80;

			max_pants_degrees = 45;

			action_ticks = 10;
			fast_action_ticks = 3;
			final_action_ticks = 6;

			if (entity.getTicksInCurrentAction() < action_ticks){
				rightArmRotation = (float)Math.toRadians(-max_degrees_x * ((entity.getTicksInCurrentAction() + (f2 - entity.ticksExisted)) / action_ticks));
			}else if (entity.getTicksInCurrentAction() < action_ticks + fast_action_ticks){
				rightArmRotation = (float)Math.toRadians(-max_degrees_x + (final_degrees_x * ((entity.getTicksInCurrentAction() + (f2 - entity.ticksExisted) - action_ticks) / fast_action_ticks)));
			}else{
				rightArmRotation = final_degrees_x;
			}
			break;
		}

		RightArm1.rotateAngleX = RightArm1.getRestRotationX() + rightArmRotation;
		RightArm2.rotateAngleX = RightArm2.getRestRotationX() + rightArmRotation;

		LeftArm1.rotateAngleX = LeftArm1.getRestRotationX() + leftArmRotation;
		LeftArm2.rotateAngleX = LeftArm2.getRestRotationX() + leftArmRotation;

		float angle = (entity.ticksExisted % 33f) / 10f;
		float ambient = (float)Math.sin(angle) / 10f;
		RightArm1.rotateAngleZ = ambient;
		RightArm2.rotateAngleZ = ambient;

		LeftArm1.rotateAngleZ = -ambient;
		LeftArm2.rotateAngleZ = -ambient;
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
