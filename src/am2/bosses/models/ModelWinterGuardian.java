package am2.bosses.models;

import org.lwjgl.opengl.GL11;

import com.google.common.util.concurrent.Monitor.Guard;

import am2.bosses.EntityWinterGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelWinterGuardian extends ModelBase
{
	//fields
	AM2ModelRenderer Shape1;
	AM2ModelRenderer Shape2;
	AM2ModelRenderer Shape3;
	AM2ModelRenderer Shape4;
	AM2ModelRenderer Shape5;
	AM2ModelRenderer head1;
	AM2ModelRenderer head5;
	AM2ModelRenderer head4;
	AM2ModelRenderer head3;
	AM2ModelRenderer head2;
	AM2ModelRenderer rightshoulder1;
	AM2ModelRenderer rightshoulder2;
	AM2ModelRenderer leftshoulder2;
	AM2ModelRenderer leftshoulder1;
	AM2ModelRenderer lowerball2;
	AM2ModelRenderer lowerball1;
	AM2ModelRenderer bar2;
	AM2ModelRenderer bar4;
	AM2ModelRenderer bar1;
	AM2ModelRenderer bar3;
	AM2ModelRenderer ornament1;
	AM2ModelRenderer ornament4;
	AM2ModelRenderer ornament2;
	AM2ModelRenderer ornament3;
	AM2ModelRenderer leftarm;
	AM2ModelRenderer rightarm;
	AM2ModelRenderer righthand3;
	AM2ModelRenderer lefthand2;
	AM2ModelRenderer righthand2;
	AM2ModelRenderer righthand4;
	AM2ModelRenderer lefthand3;
	AM2ModelRenderer lefthand4;
	AM2ModelRenderer lefthand1;
	AM2ModelRenderer righthand1;

	public ModelWinterGuardian()
	{
		textureWidth = 128;
		textureHeight = 128;

		Shape1 = new AM2ModelRenderer(this, 96, 42);
		Shape1.addBox(-4F, -4F, -4F, 8, 8, 8);
		Shape1.setRotationPoint(0F, -5F, 0F);
		Shape1.setTextureSize(128, 128);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, -0.7853982F);
		Shape2 = new AM2ModelRenderer(this, 92, 23);
		Shape2.addBox(-4F, -4F, -5F, 8, 8, 10);
		Shape2.setRotationPoint(0F, -9F, 0F);
		Shape2.setTextureSize(128, 128);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, -0.7853982F);
		Shape3 = new AM2ModelRenderer(this, 84, 0);
		Shape3.addBox(-5F, -5F, -6F, 10, 10, 12);
		Shape3.setRotationPoint(-7F, -14.9F, 0F);
		Shape3.setTextureSize(128, 128);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, -0.7853982F);
		Shape3.mirror = false;
		Shape4 = new AM2ModelRenderer(this, 84, 0);
		Shape4.addBox(-5F, -5F, -6F, 10, 10, 12);
		Shape4.setRotationPoint(7F, -14.9F, 0F);
		Shape4.setTextureSize(128, 128);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, -0.7853982F);
		Shape5 = new AM2ModelRenderer(this, 69, 110);
		Shape5.addBox(-7F, -7F, -5.5F, 14, 7, 11);
		Shape5.setRotationPoint(0F, -15F, 0F);
		Shape5.setTextureSize(128, 128);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		head1 = new AM2ModelRenderer(this, 79, 95);
		head1.addBox(-5F, -4F, -5F, 10, 4, 10);
		head1.setRotationPoint(0F, -22F, 0F);
		head1.setTextureSize(128, 128);
		head1.mirror = true;
		setRotation(head1, 0F, 0F, 0F);
		head5 = new AM2ModelRenderer(this, 79, 95);
		head5.addBox(-5F, -10F, -5F, 10, 4, 10);
		head5.setRotationPoint(0F, -22F, 0F);
		head5.setTextureSize(128, 128);
		head5.mirror = true;
		setRotation(head5, 0F, 0F, 0F);
		head4 = new AM2ModelRenderer(this, 89, 73);
		head4.addBox(2F, -6F, -4.5F, 2, 1, 9);
		head4.setRotationPoint(0F, -22F, 0F);
		head4.setTextureSize(128, 128);
		head4.mirror = true;
		setRotation(head4, 0F, 0F, 0F);
		head3 = new AM2ModelRenderer(this, 89, 73);
		head3.addBox(-4F, -6F, -4.5F, 2, 1, 9);
		head3.setRotationPoint(0F, -22F, 0F);
		head3.setTextureSize(128, 128);
		head3.mirror = true;
		setRotation(head3, 0F, 0F, 0F);
		head2 = new AM2ModelRenderer(this, 83, 84);
		head2.addBox(-5F, -6F, -3F, 10, 2, 8);
		head2.setRotationPoint(0F, -22F, 0F);
		head2.setTextureSize(128, 128);
		head2.mirror = true;
		setRotation(head2, 0F, 0F, 0F);
		rightshoulder1 = new AM2ModelRenderer(this, 112, 72);
		rightshoulder1.addBox(-2F, -2F, -2F, 4, 4, 4);
		rightshoulder1.setRotationPoint(-12F, -22F, 0F);
		rightshoulder1.setTextureSize(128, 128);
		rightshoulder1.mirror = true;
		setRotation(rightshoulder1, 0.7853982F, -1.570796F, -0.7853982F);
		rightshoulder2 = new AM2ModelRenderer(this, 112, 72);
		rightshoulder2.addBox(-2F, -2F, -2F, 4, 4, 4);
		rightshoulder2.setRotationPoint(-12F, -22F, 0F);
		rightshoulder2.setTextureSize(128, 128);
		rightshoulder2.mirror = true;
		setRotation(rightshoulder2, -0.7853982F, -1.570796F, 0.7853982F);
		leftshoulder2 = new AM2ModelRenderer(this, 112, 72);
		leftshoulder2.addBox(-2F, -2F, -2F, 4, 4, 4);
		leftshoulder2.setRotationPoint(12F, -22F, 0F);
		leftshoulder2.setTextureSize(128, 128);
		leftshoulder2.mirror = true;
		setRotation(leftshoulder2, -0.7853982F, -1.570796F, 0.7853982F);
		leftshoulder1 = new AM2ModelRenderer(this, 112, 72);
		leftshoulder1.addBox(-2F, -2F, -2F, 4, 4, 4);
		leftshoulder1.setRotationPoint(12F, -22F, 0F);
		leftshoulder1.setTextureSize(128, 128);
		leftshoulder1.mirror = true;
		setRotation(leftshoulder1, 0.7853982F, -1.570796F, -0.7853982F);
		lowerball2 = new AM2ModelRenderer(this, 104, 59);
		lowerball2.addBox(-3F, -3F, -3F, 6, 6, 6);
		lowerball2.setRotationPoint(0F, 8F, 0F);
		lowerball2.setTextureSize(128, 128);
		lowerball2.mirror = true;
		setRotation(lowerball2, -0.7853982F, -1.570796F, -0.7853982F);
		lowerball1 = new AM2ModelRenderer(this, 104, 59);
		lowerball1.addBox(-3F, -3F, -3F, 6, 6, 6);
		lowerball1.setRotationPoint(0F, 8F, 0F);
		lowerball1.setTextureSize(128, 128);
		lowerball1.mirror = true;
		setRotation(lowerball1, -0.7853982F, -1.570796F, 0.7853982F);
		bar2 = new AM2ModelRenderer(this, 120, 86);
		bar2.addBox(6F, -6F, -1F, 2, 12, 2);
		bar2.setRotationPoint(0F, 8F, 0F);
		bar2.setTextureSize(128, 128);
		bar2.mirror = true;
		setRotation(bar2, 0F, 0F, 0F);
		bar4 = new AM2ModelRenderer(this, 120, 86);
		bar4.addBox(-8F, -6F, -1F, 2, 12, 2);
		bar4.setRotationPoint(0F, 8F, 0F);
		bar4.setTextureSize(128, 128);
		bar4.mirror = true;
		setRotation(bar4, 0F, 0F, 0F);
		bar1 = new AM2ModelRenderer(this, 120, 101);
		bar1.addBox(-1F, -7F, -8F, 2, 14, 2);
		bar1.setRotationPoint(0F, 8F, 0F);
		bar1.setTextureSize(128, 128);
		bar1.mirror = true;
		setRotation(bar1, 0F, 0F, 0F);
		bar3 = new AM2ModelRenderer(this, 120, 101);
		bar3.addBox(-1F, -7F, 6F, 2, 14, 2);
		bar3.setRotationPoint(0F, 8F, 0F);
		bar3.setTextureSize(128, 128);
		bar3.mirror = true;
		setRotation(bar3, 0F, 0F, 0F);
		ornament1 = new AM2ModelRenderer(this, 120, 81);
		ornament1.addBox(-0.5F, -1.5F, -10.00667F, 3, 3, 1);
		ornament1.setRotationPoint(0F, 8F, 0F);
		ornament1.setTextureSize(128, 128);
		ornament1.mirror = true;
		setRotation(ornament1, 0F, 0.7853982F, 0F);
		ornament4 = new AM2ModelRenderer(this, 120, 81);
		ornament4.addBox(-1.5F, -1.5F, 8.5F, 3, 3, 1);
		ornament4.setRotationPoint(0F, 8F, 0F);
		ornament4.setTextureSize(128, 128);
		ornament4.mirror = true;
		setRotation(ornament4, 0F, 0.7853982F, 0F);
		ornament2 = new AM2ModelRenderer(this, 120, 81);
		ornament2.addBox(-1.5F, -1.5F, -9.5F, 3, 3, 1);
		ornament2.setRotationPoint(0F, 8F, 0F);
		ornament2.setTextureSize(128, 128);
		ornament2.mirror = true;
		setRotation(ornament2, 0F, -0.7853982F, 0F);
		ornament3 = new AM2ModelRenderer(this, 120, 81);
		ornament3.addBox(-1.5F, -1.5F, 8.5F, 3, 3, 1);
		ornament3.setRotationPoint(0F, 8F, 0F);
		ornament3.setTextureSize(128, 128);
		ornament3.mirror = true;
		setRotation(ornament3, 0F, -0.7853982F, 0F);
		leftarm = new AM2ModelRenderer(this, 59, 0);
		leftarm.addBox(-3F, 0F, -3F, 6, 26, 6);
		leftarm.setRotationPoint(20F, -17F, 0F);
		leftarm.setTextureSize(128, 128);
		leftarm.mirror = true;
		setRotation(leftarm, 0F, 0F, 0F);
		leftarm.mirror = false;
		rightarm = new AM2ModelRenderer(this, 59, 0);
		rightarm.addBox(-3F, 0F, -3F, 6, 26, 6);
		rightarm.setRotationPoint(-20F, -17F, 0F);
		rightarm.setTextureSize(128, 128);
		rightarm.mirror = true;
		setRotation(rightarm, 0F, 0F, 0F);
		righthand3 = new AM2ModelRenderer(this, 81, 59);
		righthand3.addBox(-3F, 27F, -2F, 6, 1, 5);
		righthand3.setRotationPoint(-20F, -17F, 0F);
		righthand3.setTextureSize(128, 128);
		righthand3.mirror = true;
		setRotation(righthand3, 0F, 0F, 0F);
		lefthand2 = new AM2ModelRenderer(this, 81, 59);
		lefthand2.addBox(-3F, 27F, -2F, 6, 1, 5);
		lefthand2.setRotationPoint(20F, -17F, 0F);
		lefthand2.setTextureSize(128, 128);
		lefthand2.mirror = true;
		setRotation(lefthand2, 0F, 0F, 0F);
		righthand2 = new AM2ModelRenderer(this, 91, 66);
		righthand2.addBox(-3F, 26F, -2F, 1, 1, 5);
		righthand2.setRotationPoint(-20F, -17F, 0F);
		righthand2.setTextureSize(128, 128);
		righthand2.mirror = true;
		setRotation(righthand2, 0F, 0F, 0F);
		righthand4 = new AM2ModelRenderer(this, 91, 66);
		righthand4.addBox(2F, 26F, -2F, 1, 1, 5);
		righthand4.setRotationPoint(-20F, -17F, 0F);
		righthand4.setTextureSize(128, 128);
		righthand4.mirror = true;
		setRotation(righthand4, 0F, 0F, 0F);
		lefthand3 = new AM2ModelRenderer(this, 91, 66);
		lefthand3.addBox(2F, 26F, -2F, 1, 1, 5);
		lefthand3.setRotationPoint(20F, -17F, 0F);
		lefthand3.setTextureSize(128, 128);
		lefthand3.mirror = true;
		setRotation(lefthand3, 0F, 0F, 0F);
		lefthand4 = new AM2ModelRenderer(this, 91, 66);
		lefthand4.addBox(-3F, 26F, -2F, 1, 1, 5);
		lefthand4.setRotationPoint(20F, -17F, 0F);
		lefthand4.setTextureSize(128, 128);
		lefthand4.mirror = true;
		setRotation(lefthand4, 0F, 0F, 0F);
		lefthand1 = new AM2ModelRenderer(this, 120, 118);
		lefthand1.addBox(-3F, 26F, -1F, 3, 2, 1);
		lefthand1.setRotationPoint(20F, -17F, -2F);
		lefthand1.setTextureSize(128, 128);
		lefthand1.mirror = true;
		setRotation(lefthand1, 0F, 0F, 0F);
		lefthand1.mirror = false;
		righthand1 = new AM2ModelRenderer(this, 120, 118);
		righthand1.addBox(0F, 26F, -3F, 3, 2, 1);
		righthand1.setRotationPoint(-20F, -17F, 0F);
		righthand1.setTextureSize(128, 128);
		righthand1.mirror = true;
		setRotation(righthand1, 0F, 0F, 0F);

		Shape1.storeRestRotations();
		Shape2.storeRestRotations();
		Shape3.storeRestRotations();
		Shape4.storeRestRotations();
		Shape5.storeRestRotations();
		head1.storeRestRotations();
		head5.storeRestRotations();
		head4.storeRestRotations();
		head3.storeRestRotations();
		head2.storeRestRotations();
		rightshoulder1.storeRestRotations();
		rightshoulder2.storeRestRotations();
		leftshoulder2.storeRestRotations();
		leftshoulder1.storeRestRotations();
		lowerball2.storeRestRotations();
		lowerball1.storeRestRotations();
		bar2.storeRestRotations();
		bar4.storeRestRotations();
		bar1.storeRestRotations();
		bar3.storeRestRotations();
		ornament1.storeRestRotations();
		ornament4.storeRestRotations();
		ornament2.storeRestRotations();
		ornament3.storeRestRotations();
		leftarm.storeRestRotations();
		rightarm.storeRestRotations();
		righthand3.storeRestRotations();
		lefthand2.storeRestRotations();
		righthand2.storeRestRotations();
		righthand4.storeRestRotations();
		lefthand3.storeRestRotations();
		lefthand4.storeRestRotations();
		lefthand1.storeRestRotations();
		righthand1.storeRestRotations();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		if (!(entity instanceof EntityWinterGuardian)){
			return;
		}

		setHeadRotations(f3, f4);
		setRotations((EntityWinterGuardian) entity);

		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
		head1.render(f5);
		head5.render(f5);
		head4.render(f5);
		head3.render(f5);
		head2.render(f5);
		rightshoulder1.render(f5);
		rightshoulder2.render(f5);
		leftshoulder2.render(f5);
		leftshoulder1.render(f5);
		lowerball2.render(f5);
		lowerball1.render(f5);
		bar2.render(f5);
		bar4.render(f5);
		bar1.render(f5);
		bar3.render(f5);
		ornament1.render(f5);
		ornament4.render(f5);
		ornament2.render(f5);
		ornament3.render(f5);

		if (((EntityWinterGuardian)entity).hasLeftArm()){
			leftarm.render(f5);	
			lefthand1.render(f5);
			lefthand2.render(f5);		
			lefthand3.render(f5);
			lefthand4.render(f5);		
		}


		if (((EntityWinterGuardian)entity).hasRightArm()){
			rightarm.render(f5);
			righthand1.render(f5);
			righthand2.render(f5);		
			righthand3.render(f5);
			righthand4.render(f5);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void setRotations(EntityWinterGuardian guardian){

		float rot1 = (float) Math.toRadians(guardian.getOrbitRotation());

		ornament1.rotateAngleY = ornament1.getRestRotationY() + rot1;
		ornament2.rotateAngleY = ornament2.getRestRotationY() + rot1;
		ornament3.rotateAngleY = ornament3.getRestRotationY() + rot1;
		ornament4.rotateAngleY = ornament4.getRestRotationY() + rot1;

		bar1.rotateAngleY = bar1.getRestRotationY() + rot1;
		bar2.rotateAngleY = bar2.getRestRotationY() + rot1;
		bar3.rotateAngleY = bar3.getRestRotationY() + rot1;
		bar4.rotateAngleY = bar4.getRestRotationY() + rot1;

		rightshoulder1.rotateAngleX = rightshoulder1.getRestRotationX() + rot1;
		rightshoulder1.rotateAngleY = rightshoulder1.getRestRotationY() + rot1;
		leftshoulder1.rotateAngleX = leftshoulder1.getRestRotationX() + rot1;
		leftshoulder1.rotateAngleY = leftshoulder1.getRestRotationY() + rot1;

		rightshoulder2.rotateAngleX = rightshoulder2.getRestRotationX() - rot1;
		rightshoulder2.rotateAngleY = rightshoulder2.getRestRotationY() - rot1;
		leftshoulder2.rotateAngleX = leftshoulder2.getRestRotationX() - rot1;
		leftshoulder2.rotateAngleY = leftshoulder2.getRestRotationY() - rot1;

		lowerball1.rotateAngleX = lowerball1.getRestRotationX() + rot1;
		lowerball1.rotateAngleY = lowerball1.getRestRotationY() + rot1;

		lowerball2.rotateAngleX = lowerball2.getRestRotationX() + rot1;
		lowerball2.rotateAngleY = lowerball2.getRestRotationY() + rot1;

		float left_arm_rotation_x = 0;
		float right_arm_rotation_x = 0;

		switch (guardian.getCurrentAction()){
		case SMASH:
			float max_degrees_x = 120;
			float final_degrees = 90;
			float action_ticks = 16;
			float fast_action_ticks = 4;
			float hold_ticks = 10;
			float rise_ticks = 10;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				left_arm_rotation_x = (float) Math.toRadians(-max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				float pct = ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks);
				float degrees = -max_degrees_x + (final_degrees * pct);
				left_arm_rotation_x = (float) Math.toRadians(degrees);
				GL11.glRotatef((max_degrees_x + degrees) / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f * pct, 0);
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks + hold_ticks)){
				left_arm_rotation_x = (float) Math.toRadians(-final_degrees);
				GL11.glRotatef(final_degrees / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f, 0);
			}else{
				float pct = 1.0f - ((float)(guardian.getTicksInCurrentAction() - action_ticks - fast_action_ticks - hold_ticks) / rise_ticks);
				float degrees = -max_degrees_x + (final_degrees * pct);
				float degrees2 = -max_degrees_x + (final_degrees * (1.0f - pct));
				left_arm_rotation_x = (float) Math.toRadians(degrees2);
				GL11.glRotatef((max_degrees_x + degrees) / 2, 1, 0, 0);
				GL11.glTranslatef(0, 1f * pct, 0);
			}
			right_arm_rotation_x = left_arm_rotation_x;
			break;
		case STRIKE:
			max_degrees_x = -110;
			action_ticks = 9;
			fast_action_ticks = 6;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				left_arm_rotation_x = (float) Math.toRadians(max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				float pct = ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks);
				float degrees = max_degrees_x + (-max_degrees_x * pct);
				left_arm_rotation_x = (float) Math.toRadians(degrees);
				GL11.glRotatef(40 * pct, 1, 0, 0);
				if (guardian.hasLeftArm())
					GL11.glRotatef(40 * pct, 0, 1, 0);
				else
					GL11.glRotatef(-40 * pct, 0, 1, 0);
			}
			if (!guardian.hasLeftArm()){
				right_arm_rotation_x = left_arm_rotation_x;
				left_arm_rotation_x = 0;
			}
			break;
		case LAUNCHING:
			max_degrees_x = -95;
			action_ticks = 12;
			fast_action_ticks = 4;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				left_arm_rotation_x = (float) Math.toRadians(max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				float pct = ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks);				
				if (guardian.hasLeftArm()){
					GL11.glRotatef(-40 * pct, 0, 1, 0);
				}else{
					GL11.glRotatef(40 * pct, 0, 1, 0);
				}
				left_arm_rotation_x = (float) Math.toRadians(max_degrees_x);				
			}else{
				float pct = ((float)(guardian.getTicksInCurrentAction() - action_ticks - fast_action_ticks) / fast_action_ticks);				
				if (guardian.hasLeftArm()){
					GL11.glRotatef(-40 + (40 * pct), 0, 1, 0);
				}else{
					GL11.glRotatef(40 - (40 * pct), 0, 1, 0);
				}
				left_arm_rotation_x = (float) Math.toRadians(max_degrees_x);	
			}
			if (!guardian.hasLeftArm()){
				right_arm_rotation_x = left_arm_rotation_x;
				left_arm_rotation_x = 0;
			}
			break;
		case SPINNING:
			max_degrees_x = 50;
			action_ticks = 4;
			fast_action_ticks = 11;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				left_arm_rotation_x = (float) Math.toRadians(-max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				float pct = ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks);
				float degrees = -max_degrees_x + (max_degrees_x * pct);
				left_arm_rotation_x = (float) Math.toRadians(degrees);
				GL11.glRotatef(guardian.hasLeftArm() ? 360 * pct : -360 * pct, 0, 1, 0);
			}
			if (!guardian.hasLeftArm()){
				right_arm_rotation_x = -left_arm_rotation_x;						
			}
			break;
		}

		leftarm.rotateAngleX = left_arm_rotation_x;
		lefthand1.rotateAngleX = left_arm_rotation_x;
		lefthand2.rotateAngleX = left_arm_rotation_x;
		lefthand3.rotateAngleX = left_arm_rotation_x;
		lefthand4.rotateAngleX = left_arm_rotation_x;

		rightarm.rotateAngleX = right_arm_rotation_x;
		righthand1.rotateAngleX = right_arm_rotation_x;
		righthand2.rotateAngleX = right_arm_rotation_x;
		righthand3.rotateAngleX = right_arm_rotation_x;
		righthand4.rotateAngleX = right_arm_rotation_x;

	}

	private void setHeadRotations(float yaw, float pitch){
		head1.rotateAngleX = (float) Math.toRadians(pitch);
		head1.rotateAngleY = (float) Math.toRadians(yaw);

		head2.rotateAngleX = (float) Math.toRadians(pitch);
		head2.rotateAngleY = (float) Math.toRadians(yaw);

		head3.rotateAngleX = (float) Math.toRadians(pitch);
		head3.rotateAngleY = (float) Math.toRadians(yaw);

		head4.rotateAngleX = (float) Math.toRadians(pitch);
		head4.rotateAngleY = (float) Math.toRadians(yaw);

		head5.rotateAngleX = (float) Math.toRadians(pitch);
		head5.rotateAngleY = (float) Math.toRadians(yaw);
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
