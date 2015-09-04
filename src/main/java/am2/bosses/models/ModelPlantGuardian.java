package am2.bosses.models;

import am2.bosses.BossActions;
import am2.bosses.EntityNatureGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelPlantGuardian extends ModelBase{
	//fields
	AM2ModelRenderer Head;
	AM2ModelRenderer Tendril1;
	AM2ModelRenderer Tendril2;
	AM2ModelRenderer Tendril3;
	AM2ModelRenderer Tendril4;
	AM2ModelRenderer Tendril5;
	AM2ModelRenderer Tendril6;
	AM2ModelRenderer Tendril7;
	AM2ModelRenderer Tendril8;
	AM2ModelRenderer Tendril9;
	AM2ModelRenderer Tendril10;
	AM2ModelRenderer Tendril11;
	AM2ModelRenderer Tendril12;
	AM2ModelRenderer LowerCube1;
	AM2ModelRenderer LowerCube2;
	AM2ModelRenderer Body4;
	AM2ModelRenderer Body3;
	AM2ModelRenderer Body2;
	AM2ModelRenderer Body1;
	AM2ModelRenderer Body;
	AM2ModelRenderer Shoulders;
	AM2ModelRenderer SheildArm1;
	AM2ModelRenderer SickleArm1;
	AM2ModelRenderer ShieldArm2;
	AM2ModelRenderer SickleArm2;
	AM2ModelRenderer Tendril13;
	AM2ModelRenderer Tendril14;
	AM2ModelRenderer Tendril15;
	AM2ModelRenderer Tendril16;
	AM2ModelRenderer Shape39;
	AM2ModelRenderer Sickle2;
	AM2ModelRenderer Sickle3;
	AM2ModelRenderer Sickle4;
	AM2ModelRenderer Sickle5;
	AM2ModelRenderer Shield;

	int z_interp_ticks = 20;

	public ModelPlantGuardian(){
		textureWidth = 128;
		textureHeight = 128;
		setTextureOffset("Head.Shape34", 7, 51);
		setTextureOffset("Head.Shape33", 5, 30);
		setTextureOffset("Head.Shape32", 5, 30);
		setTextureOffset("Head.Shape31", 5, 38);
		setTextureOffset("Head.Shape30", 5, 38);
		setTextureOffset("Head.Shape29", 0, 64);
		setTextureOffset("Head.Shape28", 5, 49);
		setTextureOffset("Head.Shape27", 5, 49);
		setTextureOffset("Head.Shape26", 65, 24);
		setTextureOffset("Head.Neck", 36, 48);

		Head = new AM2ModelRenderer(this, "Head");
		Head.setRotationPoint(0F, -39F, 0F);
		setRotation(Head, 0F, 0F, 0F, true);
		Head.mirror = true;
		Head.addBox("Shape34", -1F, -7F, 3F, 2, 2, 10);
		Head.addBox("Shape33", -5F, -5.5F, 3F, 2, 2, 5);
		Head.addBox("Shape32", 3F, -5.5F, 3F, 2, 2, 5);
		Head.addBox("Shape31", -5F, -8.5F, 0F, 2, 2, 8);
		Head.addBox("Shape30", 3F, -8.5F, 2F, 2, 2, 8);
		Head.addBox("Shape29", -2F, -11F, -2F, 4, 2, 20);
		Head.addBox("Shape28", -5F, -11F, 0F, 2, 2, 12);
		Head.addBox("Shape27", 3F, -11F, 0F, 2, 2, 12);
		Head.addBox("Shape26", -4F, -10F, -6F, 8, 6, 12);
		Head.addBox("Neck", -2F, -4F, -2F, 4, 4, 4);
		Tendril1 = new AM2ModelRenderer(this, 0, 0);
		Tendril1.addBox(3.5F, 0F, -0.5F, 1, 5, 1);
		Tendril1.setRotationPoint(0F, 0F, 0F);
		Tendril1.setTextureSize(128, 128);
		Tendril1.mirror = true;
		setRotation(Tendril1, 0.2617994F, 0F, 0F, true);
		Tendril2 = new AM2ModelRenderer(this, 0, 0);
		Tendril2.addBox(-0.5F, 0F, -4.5F, 1, 5, 1);
		Tendril2.setRotationPoint(0F, 0F, 0F);
		Tendril2.setTextureSize(128, 128);
		Tendril2.mirror = true;
		setRotation(Tendril2, 0F, 0F, -0.2617994F, true);
		Tendril3 = new AM2ModelRenderer(this, 0, 0);
		Tendril3.addBox(-0.5F, 0F, 3.5F, 1, 5, 1);
		Tendril3.setRotationPoint(0F, 0F, 0F);
		Tendril3.setTextureSize(128, 128);
		Tendril3.mirror = true;
		setRotation(Tendril3, 0F, 0F, 0.2617994F, true);
		Tendril4 = new AM2ModelRenderer(this, 0, 0);
		Tendril4.addBox(-4.5F, 0F, -0.5F, 1, 5, 1);
		Tendril4.setRotationPoint(0F, 0F, 0F);
		Tendril4.setTextureSize(128, 128);
		Tendril4.mirror = true;
		setRotation(Tendril4, -0.2617994F, 0F, 0F, true);
		Tendril5 = new AM2ModelRenderer(this, 0, 0);
		Tendril5.addBox(-5.5F, 3.7F, -0.8F, 1, 5, 1);
		Tendril5.setRotationPoint(0F, 0F, 0F);
		Tendril5.setTextureSize(128, 128);
		Tendril5.mirror = true;
		setRotation(Tendril5, -0.2617994F, 0F, -0.2617994F, true);
		Tendril6 = new AM2ModelRenderer(this, 0, 0);
		Tendril6.addBox(-0.5F, 3.7F, -5.5F, 1, 5, 1);
		Tendril6.setRotationPoint(0F, 0F, 0F);
		Tendril6.setTextureSize(128, 128);
		Tendril6.mirror = true;
		setRotation(Tendril6, 0.2617994F, 0F, -0.2617994F, true);
		Tendril7 = new AM2ModelRenderer(this, 0, 0);
		Tendril7.addBox(4.6F, 3.7F, -0.2F, 1, 5, 1);
		Tendril7.setRotationPoint(0F, 0F, 0F);
		Tendril7.setTextureSize(128, 128);
		Tendril7.mirror = true;
		setRotation(Tendril7, 0.2617994F, 0F, 0.2617994F, true);
		Tendril8 = new AM2ModelRenderer(this, 0, 0);
		Tendril8.addBox(-0.5F, 3.6F, 4.6F, 1, 5, 1);
		Tendril8.setRotationPoint(0F, 0F, 0F);
		Tendril8.setTextureSize(128, 128);
		Tendril8.mirror = true;
		setRotation(Tendril8, -0.2617994F, 0F, 0.2617994F, true);
		Tendril9 = new AM2ModelRenderer(this, 0, 0);
		Tendril9.addBox(-7.4F, 7.2F, -1.2F, 1, 5, 1);
		Tendril9.setRotationPoint(0F, 0F, 0F);
		Tendril9.setTextureSize(128, 128);
		Tendril9.mirror = true;
		setRotation(Tendril9, -0.2617994F, 0F, -0.5235988F, true);
		Tendril10 = new AM2ModelRenderer(this, 0, 0);
		Tendril10.addBox(-0.5F, 7.1F, -7.5F, 1, 5, 1);
		Tendril10.setRotationPoint(0F, 0F, 0F);
		Tendril10.setTextureSize(128, 128);
		Tendril10.mirror = true;
		setRotation(Tendril10, 0.5235988F, 0F, -0.2617994F, true);
		Tendril11 = new AM2ModelRenderer(this, 0, 0);
		Tendril11.addBox(-0.5F, 7F, 6.5F, 1, 5, 1);
		Tendril11.setRotationPoint(0F, 0F, 0F);
		Tendril11.setTextureSize(128, 128);
		Tendril11.mirror = true;
		setRotation(Tendril11, -0.5235988F, 0F, 0.2617994F, true);
		Tendril12 = new AM2ModelRenderer(this, 0, 0);
		Tendril12.addBox(6.5F, 7F, 0.3F, 1, 5, 1);
		Tendril12.setRotationPoint(0F, 0F, 0F);
		Tendril12.setTextureSize(128, 128);
		Tendril12.mirror = true;
		setRotation(Tendril12, 0.2617994F, 0F, 0.5235988F, true);
		LowerCube1 = new AM2ModelRenderer(this, 5, 0);
		LowerCube1.addBox(-2F, -2F, -2F, 4, 4, 4);
		LowerCube1.setRotationPoint(0F, 5F, 0F);
		LowerCube1.setTextureSize(128, 128);
		LowerCube1.mirror = true;
		setRotation(LowerCube1, -0.7853982F, -1.570796F, -0.7853982F, true);
		LowerCube2 = new AM2ModelRenderer(this, 5, 0);
		LowerCube2.addBox(-2F, -2F, -2F, 4, 4, 4);
		LowerCube2.setRotationPoint(0F, 5F, 0F);
		LowerCube2.setTextureSize(128, 128);
		LowerCube2.mirror = true;
		setRotation(LowerCube2, -0.7853982F, -1.570796F, 0.7853982F, true);
		Body4 = new AM2ModelRenderer(this, 36, 0);
		Body4.addBox(-4F, -8F, -4F, 8, 8, 8);
		Body4.setRotationPoint(0F, 0F, 0F);
		Body4.setTextureSize(128, 128);
		Body4.mirror = true;
		setRotation(Body4, 0F, 0F, 0F, true);
		Body3 = new AM2ModelRenderer(this, 36, 17);
		Body3.addBox(-3.5F, -8F, -3.5F, 7, 8, 7);
		Body3.setRotationPoint(0F, -8F, 0F);
		Body3.setTextureSize(128, 128);
		Body3.mirror = true;
		setRotation(Body3, -0.1396263F, 0F, 0F, true);
		Body2 = new AM2ModelRenderer(this, 36, 33);
		Body2.addBox(-3F, -8F, -3F, 6, 8, 6);
		Body2.setRotationPoint(0F, -16F, 1F);
		Body2.setTextureSize(128, 128);
		Body2.mirror = true;
		setRotation(Body2, -0.0698132F, 0F, 0F, true);
		Body1 = new AM2ModelRenderer(this, 36, 0);
		Body1.addBox(-4F, -8F, -4F, 8, 8, 8);
		Body1.setRotationPoint(0F, -23.5F, 1.5F);
		Body1.setTextureSize(128, 128);
		Body1.mirror = true;
		setRotation(Body1, 0.0698132F, 0F, 0F, true);
		Body = new AM2ModelRenderer(this, 69, 0);
		Body.addBox(-6F, -8F, -5F, 12, 8, 10);
		Body.setRotationPoint(0F, -31F, 1F);
		Body.setTextureSize(128, 128);
		Body.mirror = true;
		setRotation(Body, 0.0698132F, 0F, 0F, true);
		Shoulders = new AM2ModelRenderer(this, 69, 19);
		Shoulders.addBox(-8F, -1F, -1F, 16, 2, 2);
		Shoulders.setRotationPoint(0F, -37F, 0.5F);
		Shoulders.setTextureSize(128, 128);
		Shoulders.mirror = true;
		setRotation(Shoulders, 0F, 0F, 0F, true);
		SheildArm1 = new AM2ModelRenderer(this, 0, 9);
		SheildArm1.addBox(-4F, -2F, -3F, 4, 14, 6);
		SheildArm1.setRotationPoint(-8F, -37F, 0.5F);
		SheildArm1.setTextureSize(128, 128);
		SheildArm1.mirror = true;
		setRotation(SheildArm1, -0.0872665F, -0.3839724F, 0.2792527F, true);
		SickleArm1 = new AM2ModelRenderer(this, 0, 9);
		SickleArm1.addBox(0F, -2F, -3F, 4, 14, 6);
		SickleArm1.setRotationPoint(8F, -37F, 0.5F);
		SickleArm1.setTextureSize(128, 128);
		SickleArm1.mirror = true;
		setRotation(SickleArm1, 0.2617994F, -0.3141593F, -0.1047198F, true);
		ShieldArm2 = new AM2ModelRenderer(this, 114, 0);
		ShieldArm2.addBox(-4.5F, 1F, 9F, 3, 12, 4);
		ShieldArm2.setRotationPoint(-8F, -37F, 0.5F);
		ShieldArm2.setTextureSize(128, 128);
		ShieldArm2.mirror = true;
		setRotation(ShieldArm2, -1.32645F, -0.7504916F, 0.1745329F, true);
		SickleArm2 = new AM2ModelRenderer(this, 114, 0);
		SickleArm2.addBox(0.5F, 9F, 4F, 3, 12, 4);
		SickleArm2.setRotationPoint(8F, -37F, 0.5F);
		SickleArm2.setTextureSize(128, 128);
		SickleArm2.mirror = true;
		setRotation(SickleArm2, -0.2617994F, -0.373645F, -0.1189716F, true);
		Tendril13 = new AM2ModelRenderer(this, 0, 30);
		Tendril13.addBox(2.5F, -1.3F, 3F, 1, 14, 1);
		Tendril13.setRotationPoint(0F, 0F, 0F);
		Tendril13.setTextureSize(128, 128);
		Tendril13.mirror = true;
		setRotation(Tendril13, 0F, 0F, 0.418879F, true);
		Tendril14 = new AM2ModelRenderer(this, 0, 30);
		Tendril14.addBox(-4F, -1.2F, 2.5F, 1, 14, 1);
		Tendril14.setRotationPoint(0F, 0F, 0F);
		Tendril14.setTextureSize(128, 128);
		Tendril14.mirror = true;
		setRotation(Tendril14, -0.418879F, 0F, 0F, true);
		Tendril15 = new AM2ModelRenderer(this, 0, 30);
		Tendril15.addBox(-3.7F, -1.4F, -4F, 1, 14, 1);
		Tendril15.setRotationPoint(0F, 0F, 0F);
		Tendril15.setTextureSize(128, 128);
		Tendril15.mirror = true;
		setRotation(Tendril15, 0F, 0F, -0.418879F, true);
		Tendril16 = new AM2ModelRenderer(this, 0, 30);
		Tendril16.addBox(3F, -1.2F, -3.7F, 1, 14, 1);
		Tendril16.setRotationPoint(0F, 0F, 0F);
		Tendril16.setTextureSize(128, 128);
		Tendril16.mirror = true;
		setRotation(Tendril16, 0.418879F, 0F, 0F, true);
		Shape39 = new AM2ModelRenderer(this, 0, 90);
		Shape39.addBox(0F, 20F, -27F, 2, 2, 36);
		Shape39.setRotationPoint(8F, -37F, 0.5F);
		Shape39.setTextureSize(128, 128);
		Shape39.mirror = true;
		setRotation(Shape39, 0.2617994F, -0.3316126F, -0.1745329F, true);
		Sickle2 = new AM2ModelRenderer(this, 114, 112);
		Sickle2.addBox(-0.5F, 15F, -31F, 3, 12, 4);
		Sickle2.setRotationPoint(8F, -37F, 0.5F);
		Sickle2.setTextureSize(128, 128);
		Sickle2.mirror = true;
		setRotation(Sickle2, 0.2617994F, -0.3316126F, -0.1745329F, true);
		Sickle3 = new AM2ModelRenderer(this, 120, 78);
		Sickle3.addBox(0.5F, 13F, -29.5F, 1, 30, 3);
		Sickle3.setRotationPoint(8F, -37F, 0.5F);
		Sickle3.setTextureSize(128, 128);
		Sickle3.mirror = true;
		setRotation(Sickle3, 0.2617994F, -0.3316126F, -0.1745329F, true);
		Sickle4 = new AM2ModelRenderer(this, 107, 114);
		Sickle4.addBox(0.5F, 18F, -48.5F, 1, 12, 2);
		Sickle4.setRotationPoint(8F, -37F, 0.5F);
		Sickle4.setTextureSize(128, 128);
		Sickle4.mirror = true;
		setRotation(Sickle4, 0.8901179F, -0.3316126F, -0.1745329F, true);
		Sickle5 = new AM2ModelRenderer(this, 102, 119);
		Sickle5.addBox(0.5F, 3F, -56.5F, 1, 8, 1);
		Sickle5.setRotationPoint(8F, -37F, 0.5F);
		Sickle5.setTextureSize(128, 128);
		Sickle5.mirror = true;
		setRotation(Sickle5, 1.396263F, -0.3316126F, -0.1745329F, true);
		Shield = new AM2ModelRenderer(this, 84, 45);
		Shield.addBox(-3F, -2F, -9F, 20, 30, 2);
		Shield.setRotationPoint(-8F, -37F, 0.5F);
		Shield.setTextureSize(128, 128);
		Shield.mirror = true;
		setRotation(Shield, -0.0872665F, 0.6806784F, 0.0872665F, true);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		if (!(entity instanceof EntityNatureGuardian)) return;
		updateRotations((EntityNatureGuardian)entity);

		GL11.glPushMatrix();

		EntityNatureGuardian guardian = (EntityNatureGuardian)entity;
		if (guardian.getCurrentAction() == BossActions.SPINNING){
			GL11.glRotatef(guardian.spinRotation, 0, 1, 0);
		}
		setHeadRotation(f3, f4);
		Head.render(f5);
		Tendril1.render(f5);
		Tendril2.render(f5);
		Tendril3.render(f5);
		Tendril4.render(f5);
		Tendril5.render(f5);
		Tendril6.render(f5);
		Tendril7.render(f5);
		Tendril8.render(f5);
		Tendril9.render(f5);
		Tendril10.render(f5);
		Tendril11.render(f5);
		Tendril12.render(f5);
		LowerCube1.render(f5);
		LowerCube2.render(f5);
		Body4.render(f5);
		Body3.render(f5);
		Body2.render(f5);
		Body1.render(f5);
		Body.render(f5);
		Shoulders.render(f5);
		SheildArm1.render(f5);
		SickleArm1.render(f5);
		ShieldArm2.render(f5);
		SickleArm2.render(f5);
		Tendril13.render(f5);
		Tendril14.render(f5);
		Tendril15.render(f5);
		Tendril16.render(f5);

		if (((EntityNatureGuardian)entity).hasSickle){
			Shape39.render(f5);
			Sickle2.render(f5);
			Sickle3.render(f5);
			Sickle4.render(f5);
			Sickle5.render(f5);
		}

		Shield.render(f5);

		GL11.glPopMatrix();
	}

	@SuppressWarnings("incomplete-switch")
	private void updateRotations(EntityNatureGuardian guardian){
		setTendrilRotations(guardian.tendrilRotation);

		float main_arm_rotation_z = 0;
		float shield_arm_rotation_z = 0;

		float main_arm_rotation_x = 0;
		float shield_arm_rotation_x = 0;

		float main_arm_rotation_y = 0;
		float shield_arm_rotation_y = 0;

		switch (guardian.getCurrentAction()){
		case IDLE:
			if (guardian.getTicksInCurrentAction() < 20){
				if (guardian.last_rotation_z_main != 0){
					main_arm_rotation_z = guardian.last_rotation_z_main / ((float)z_interp_ticks - (float)guardian.getTicksInCurrentAction());
				}
				if (guardian.last_rotation_z_shield != 0){
					shield_arm_rotation_z = guardian.last_rotation_z_shield / ((float)z_interp_ticks - (float)guardian.getTicksInCurrentAction());
				}
			}
			break;
		case SPINNING:
			float degrees = guardian.getTicksInCurrentAction() < 20 ? 60f * ((float)guardian.getTicksInCurrentAction() / 20f) : 60;
			main_arm_rotation_z = (float)-Math.toRadians(degrees);
			shield_arm_rotation_z = (float)Math.toRadians(degrees);
			break;
		case STRIKE:
			float max_degrees_x = 120;
			float max_degrees_y = 60;
			float action_ticks = 10;
			float fast_action_ticks = 3;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				main_arm_rotation_x = (float)Math.toRadians(-max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
				main_arm_rotation_y = (float)Math.toRadians(max_degrees_y * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				main_arm_rotation_x = (float)Math.toRadians(-max_degrees_x + (max_degrees_x * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks)));
				main_arm_rotation_y = (float)Math.toRadians(max_degrees_y - (max_degrees_y * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks)));
			}
			shield_arm_rotation_y = main_arm_rotation_y;
			GL11.glRotatef((float)Math.toDegrees(shield_arm_rotation_y), 0, 1, 0);
			break;
		case SHIELD_BASH:
			max_degrees_x = 120;
			max_degrees_y = 60;
			action_ticks = 10;
			fast_action_ticks = 3;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				//main_arm_rotation_x = (float) Math.toRadians(-max_degrees_x * ((float)guardian.getTicksInCurrentAction() / action_ticks));
				shield_arm_rotation_y = (float)Math.toRadians(-max_degrees_y * ((float)guardian.getTicksInCurrentAction() / action_ticks));
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				//main_arm_rotation_x = (float) Math.toRadians(-max_degrees_x + (max_degrees_x * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks)));
				shield_arm_rotation_y = (float)Math.toRadians(-max_degrees_y + (max_degrees_y * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks)));
			}
			main_arm_rotation_y = shield_arm_rotation_y / 2;
			GL11.glRotatef((float)Math.toDegrees(shield_arm_rotation_y), 0, 1, 0);
			break;
		case THROWING_SICKLE:
			degrees = 0;
			float max_degrees = 180;
			action_ticks = 10;
			fast_action_ticks = 3;
			if (guardian.getTicksInCurrentAction() < action_ticks){
				if (guardian.last_rotation_z_main != 0){
					main_arm_rotation_z = guardian.last_rotation_z_main / ((float)z_interp_ticks - (float)guardian.getTicksInCurrentAction());
				}
				if (guardian.last_rotation_z_shield != 0){
					shield_arm_rotation_z = guardian.last_rotation_z_shield / ((float)z_interp_ticks - (float)guardian.getTicksInCurrentAction());
				}
				degrees = -max_degrees * ((float)guardian.getTicksInCurrentAction() / action_ticks);
			}else if (guardian.getTicksInCurrentAction() < (action_ticks + fast_action_ticks)){
				degrees = -max_degrees + (max_degrees * ((float)(guardian.getTicksInCurrentAction() - action_ticks) / fast_action_ticks));
			}
			main_arm_rotation_x = (float)Math.toRadians(degrees);
			break;
		}

		guardian.last_rotation_z_main = main_arm_rotation_z;
		guardian.last_rotation_z_shield = shield_arm_rotation_z;

		guardian.last_rotation_x_main = main_arm_rotation_x;
		guardian.last_rotation_x_shield = shield_arm_rotation_x;

		guardian.last_rotation_y_main = main_arm_rotation_y;
		guardian.last_rotation_y_shield = shield_arm_rotation_y;

		SickleArm1.rotateAngleZ = SickleArm1.getRestRotationZ() + main_arm_rotation_z;
		SickleArm2.rotateAngleZ = SickleArm2.getRestRotationZ() + main_arm_rotation_z;
		Shape39.rotateAngleZ = Shape39.getRestRotationZ() + main_arm_rotation_z;
		Sickle2.rotateAngleZ = Sickle2.getRestRotationZ() + main_arm_rotation_z;
		Sickle3.rotateAngleZ = Sickle3.getRestRotationZ() + main_arm_rotation_z;
		Sickle4.rotateAngleZ = Sickle4.getRestRotationZ() + main_arm_rotation_z;
		Sickle5.rotateAngleZ = Sickle5.getRestRotationZ() + main_arm_rotation_z;

		SickleArm1.rotateAngleX = SickleArm1.getRestRotationX() + main_arm_rotation_x;
		SickleArm2.rotateAngleX = SickleArm2.getRestRotationX() + main_arm_rotation_x;
		Shape39.rotateAngleX = Shape39.getRestRotationX() + main_arm_rotation_x;
		Sickle2.rotateAngleX = Sickle2.getRestRotationX() + main_arm_rotation_x;
		Sickle3.rotateAngleX = Sickle3.getRestRotationX() + main_arm_rotation_x;
		Sickle4.rotateAngleX = Sickle4.getRestRotationX() + main_arm_rotation_x;
		Sickle5.rotateAngleX = Sickle5.getRestRotationX() + main_arm_rotation_x;

		SickleArm1.rotateAngleY = SickleArm1.getRestRotationY() + main_arm_rotation_y;
		SickleArm2.rotateAngleY = SickleArm2.getRestRotationY() + main_arm_rotation_y;
		Shape39.rotateAngleY = Shape39.getRestRotationY() + main_arm_rotation_y;
		Sickle2.rotateAngleY = Sickle2.getRestRotationY() + main_arm_rotation_y;
		Sickle3.rotateAngleY = Sickle3.getRestRotationY() + main_arm_rotation_y;
		Sickle4.rotateAngleY = Sickle4.getRestRotationY() + main_arm_rotation_y;
		Sickle5.rotateAngleY = Sickle5.getRestRotationY() + main_arm_rotation_y;

		SheildArm1.rotateAngleZ = SheildArm1.getRestRotationZ() + shield_arm_rotation_z;
		ShieldArm2.rotateAngleZ = ShieldArm2.getRestRotationZ() + shield_arm_rotation_z;
		Shield.rotateAngleZ = Shield.getRestRotationZ() + shield_arm_rotation_z;

		SheildArm1.rotateAngleX = SheildArm1.getRestRotationX() + shield_arm_rotation_x;
		ShieldArm2.rotateAngleX = ShieldArm2.getRestRotationX() + shield_arm_rotation_x;
		Shield.rotateAngleX = Shield.getRestRotationX() + shield_arm_rotation_x;

		SheildArm1.rotateAngleY = SheildArm1.getRestRotationY() + shield_arm_rotation_y;
		ShieldArm2.rotateAngleY = ShieldArm2.getRestRotationY() + shield_arm_rotation_y;
		Shield.rotateAngleY = Shield.getRestRotationY() + shield_arm_rotation_y;
	}

	private void interp_to_zero_zrot(AM2ModelRenderer model, int ticks){
		float delta = model.rotateAngleZ / (z_interp_ticks - ticks);
		model.rotateAngleZ -= delta;
	}

	private void setTendrilRotations(float r){
		//r = (float) Math.toRadians(r);
		Tendril1.rotateAngleY = r;
		Tendril2.rotateAngleY = r;
		Tendril3.rotateAngleY = r;
		Tendril4.rotateAngleY = r;
		Tendril5.rotateAngleY = r;
		Tendril6.rotateAngleY = r;
		Tendril7.rotateAngleY = r;
		Tendril8.rotateAngleY = r;
		Tendril9.rotateAngleY = r;
		Tendril10.rotateAngleY = r;
		Tendril11.rotateAngleY = r;
		Tendril12.rotateAngleY = r;
		Tendril13.rotateAngleY = r;
		Tendril14.rotateAngleY = r;
		Tendril15.rotateAngleY = r;
		Tendril16.rotateAngleY = r;
	}

	private void setHeadRotation(float y, float p){
		Head.rotateAngleY = (float)Math.toRadians(y);
		Head.rotateAngleX = (float)Math.toRadians(p);
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z, boolean storeRest){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;

		if (storeRest){
			model.storeRestRotations();
		}
	}

}
