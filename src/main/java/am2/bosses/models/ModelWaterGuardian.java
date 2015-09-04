package am2.bosses.models;

import am2.bosses.EntityWaterGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelWaterGuardian extends ModelBase{
	//fields
	AM2ModelRenderer Shape1;
	AM2ModelRenderer Shape2;
	AM2ModelRenderer Shape4;
	AM2ModelRenderer Shape5;
	AM2ModelRenderer Shape6;
	AM2ModelRenderer Shape7;
	AM2ModelRenderer tentaclefrontleft;
	AM2ModelRenderer tentaclebackleft;
	AM2ModelRenderer Shape11;
	AM2ModelRenderer tentaclebackright;
	AM2ModelRenderer tentaclefrontright;
	AM2ModelRenderer Shape3;
	AM2ModelRenderer Shape8;
	AM2ModelRenderer Shape14;
	AM2ModelRenderer Shape15;
	AM2ModelRenderer Shape16;
	AM2ModelRenderer tentacleright;
	AM2ModelRenderer tentacleleft;
	AM2ModelRenderer tentaclefront;
	AM2ModelRenderer tentacleback;
	AM2ModelRenderer bar3;
	AM2ModelRenderer bar1;
	AM2ModelRenderer bar2;
	AM2ModelRenderer bar4;
	AM2ModelRenderer ornament4;
	AM2ModelRenderer ornament2;
	AM2ModelRenderer ornament6;
	AM2ModelRenderer ornament7;
	AM2ModelRenderer ornament8;
	AM2ModelRenderer ornament1;
	AM2ModelRenderer ornament5;
	AM2ModelRenderer ornament3;

	public ModelWaterGuardian(){
		textureWidth = 64;
		textureHeight = 64;

		Shape1 = new AM2ModelRenderer(this, 10, 56);
		Shape1.addBox(-2F, -2F, -2F, 4, 4, 4);
		Shape1.setRotationPoint(0F, 7.5F, 0F);
		Shape1.setTextureSize(64, 64);
		Shape1.mirror = true;
		setRotation(Shape1, -0.7853982F, -1.570796F, 0.7853982F);
		Shape2 = new AM2ModelRenderer(this, 10, 56);
		Shape2.addBox(-2F, -2F, -2F, 4, 4, 4);
		Shape2.setRotationPoint(0F, 7.5F, 0F);
		Shape2.setTextureSize(64, 64);
		Shape2.mirror = true;
		setRotation(Shape2, -0.7853982F, 1.570796F, 0.7853982F);
		Shape4 = new AM2ModelRenderer(this, 0, 42);
		Shape4.addBox(-5F, 0F, -1F, 10, 5, 1);
		Shape4.setRotationPoint(0F, 4F, -4F);
		Shape4.setTextureSize(64, 64);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape5 = new AM2ModelRenderer(this, 0, 42);
		Shape5.addBox(-5F, 0F, 0F, 10, 5, 1);
		Shape5.setRotationPoint(0F, 4F, 4F);
		Shape5.setTextureSize(64, 64);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape6 = new AM2ModelRenderer(this, 0, 28);
		Shape6.addBox(0F, 0F, -4F, 1, 5, 8);
		Shape6.setRotationPoint(4F, 4F, 0F);
		Shape6.setTextureSize(64, 64);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape7 = new AM2ModelRenderer(this, 0, 28);
		Shape7.addBox(-1F, 0F, -4F, 1, 5, 8);
		Shape7.setRotationPoint(-4F, 4F, 0F);
		Shape7.setTextureSize(64, 64);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		tentaclefrontleft = new AM2ModelRenderer(this, 0, 49);
		tentaclefrontleft.addBox(0F, 0F, -1F, 1, 14, 1);
		tentaclefrontleft.setRotationPoint(3F, 9F, -3F);
		tentaclefrontleft.setTextureSize(64, 64);
		tentaclefrontleft.mirror = true;
		setRotation(tentaclefrontleft, 0F, 0F, 0F);
		tentaclebackleft = new AM2ModelRenderer(this, 0, 49);
		tentaclebackleft.addBox(0F, 0F, 0F, 1, 14, 1);
		tentaclebackleft.setRotationPoint(3F, 9F, 3F);
		tentaclebackleft.setTextureSize(64, 64);
		tentaclebackleft.mirror = true;
		setRotation(tentaclebackleft, 0F, 0F, 0F);
		Shape11 = new AM2ModelRenderer(this, 27, 62);
		Shape11.addBox(-4F, 0F, 0F, 8, 1, 1);
		Shape11.setRotationPoint(0F, 8F, -4F);
		Shape11.setTextureSize(64, 64);
		Shape11.mirror = true;
		setRotation(Shape11, 0F, 0F, 0F);
		tentaclebackright = new AM2ModelRenderer(this, 0, 49);
		tentaclebackright.addBox(-1F, 0F, 0F, 1, 14, 1);
		tentaclebackright.setRotationPoint(-3F, 9F, 3F);
		tentaclebackright.setTextureSize(64, 64);
		tentaclebackright.mirror = true;
		setRotation(tentaclebackright, 0F, 0F, 0F);
		tentaclefrontright = new AM2ModelRenderer(this, 0, 49);
		tentaclefrontright.addBox(-1F, 0F, -1F, 1, 14, 1);
		tentaclefrontright.setRotationPoint(-3F, 9F, -3F);
		tentaclefrontright.setTextureSize(64, 64);
		tentaclefrontright.mirror = true;
		setRotation(tentaclefrontright, 0F, 0F, 0F);
		Shape3 = new AM2ModelRenderer(this, 23, 42);
		Shape3.addBox(-5F, -1F, -5F, 10, 1, 10);
		Shape3.setRotationPoint(0F, 4F, 0F);
		Shape3.setTextureSize(64, 64);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape8 = new AM2ModelRenderer(this, 19, 32);
		Shape8.addBox(-4F, -1F, -4F, 8, 1, 8);
		Shape8.setRotationPoint(0F, 3F, 0F);
		Shape8.setTextureSize(64, 64);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, 0F);
		Shape14 = new AM2ModelRenderer(this, 27, 62);
		Shape14.addBox(-4F, 0F, -1F, 8, 1, 1);
		Shape14.setRotationPoint(0F, 8F, 4F);
		Shape14.setTextureSize(64, 64);
		Shape14.mirror = true;
		setRotation(Shape14, 0F, 0F, 0F);
		Shape15 = new AM2ModelRenderer(this, 27, 54);
		Shape15.addBox(0F, 0F, -3F, 1, 1, 6);
		Shape15.setRotationPoint(-4F, 8F, 0F);
		Shape15.setTextureSize(64, 64);
		Shape15.mirror = true;
		setRotation(Shape15, 0F, 0F, 0F);
		Shape16 = new AM2ModelRenderer(this, 27, 54);
		Shape16.addBox(-1F, 0F, -3F, 1, 1, 6);
		Shape16.setRotationPoint(4F, 8F, 0F);
		Shape16.setTextureSize(64, 64);
		Shape16.mirror = true;
		setRotation(Shape16, 0F, 0F, 0F);
		tentacleright = new AM2ModelRenderer(this, 5, 53);
		tentacleright.addBox(-0.5F, 0F, -0.5F, 1, 10, 1);
		tentacleright.setRotationPoint(-3.5F, 9F, 0F);
		tentacleright.setTextureSize(64, 64);
		tentacleright.mirror = true;
		setRotation(tentacleright, 0F, 0F, 0F);
		tentacleleft = new AM2ModelRenderer(this, 5, 53);
		tentacleleft.addBox(-0.5F, 0F, -0.5F, 1, 10, 1);
		tentacleleft.setRotationPoint(3.5F, 9F, 0F);
		tentacleleft.setTextureSize(64, 64);
		tentacleleft.mirror = true;
		setRotation(tentacleleft, 0F, 0F, 0F);
		tentaclefront = new AM2ModelRenderer(this, 5, 53);
		tentaclefront.addBox(-0.5F, 0F, -0.5F, 1, 10, 1);
		tentaclefront.setRotationPoint(0F, 9F, -3.5F);
		tentaclefront.setTextureSize(64, 64);
		tentaclefront.mirror = true;
		setRotation(tentaclefront, 0F, 0F, 0F);
		tentacleback = new AM2ModelRenderer(this, 5, 53);
		tentacleback.addBox(-0.5F, 0F, -0.5F, 1, 10, 1);
		tentacleback.setRotationPoint(0F, 9F, 3.5F);
		tentacleback.setTextureSize(64, 64);
		tentacleback.mirror = true;
		setRotation(tentacleback, 0F, 0F, 0F);
		bar3 = new AM2ModelRenderer(this, 0, 0);
		bar3.addBox(8F, -0.5F, -3.5F, 1, 1, 10);
		bar3.setRotationPoint(0F, 7.5F, 0F);
		bar3.setTextureSize(64, 64);
		bar3.mirror = true;
		setRotation(bar3, 1.570796F, 1.570796F, 0F);
		bar3.mirror = false;
		bar1 = new AM2ModelRenderer(this, 0, 0);
		bar1.addBox(-9F, -0.5F, -6.5F, 1, 1, 10);
		bar1.setRotationPoint(0F, 7.5F, 0F);
		bar1.setTextureSize(64, 64);
		bar1.mirror = true;
		setRotation(bar1, -1.570796F, 0F, 0F);
		ornament4 = new AM2ModelRenderer(this, 0, 15);
		ornament4.addBox(0F, 9.5F, -2F, 2, 1, 2);
		ornament4.setRotationPoint(0F, 7.5F, 0F);
		ornament4.setTextureSize(64, 64);
		ornament4.mirror = true;
		setRotation(ornament4, 1.570796F, -2.356194F, 0F);
		ornament2 = new AM2ModelRenderer(this, 0, 15);
		ornament2.addBox(0F, 9.5F, -2.2F, 2, 1, 2);
		ornament2.setRotationPoint(0F, 7.5F, 0F);
		ornament2.setTextureSize(64, 64);
		ornament2.mirror = true;
		setRotation(ornament2, 1.570796F, -0.7853982F, 0F);
		ornament6 = new AM2ModelRenderer(this, 0, 15);
		ornament6.addBox(0F, 9.5F, -2F, 2, 1, 2);
		ornament6.setRotationPoint(0F, 7.5F, 0F);
		ornament6.setTextureSize(64, 64);
		ornament6.mirror = true;
		setRotation(ornament6, 1.570796F, 0.7853982F, 0F);
		ornament7 = new AM2ModelRenderer(this, 0, 15);
		ornament7.addBox(0F, 9.5F, -2F, 2, 1, 2);
		ornament7.setRotationPoint(0F, 7.5F, 0F);
		ornament7.setTextureSize(64, 64);
		ornament7.mirror = true;
		setRotation(ornament7, 1.570796F, 2.356194F, 0F);
		ornament8 = new AM2ModelRenderer(this, 0, 19);
		ornament8.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament8.setRotationPoint(0F, 7.5F, 0F);
		ornament8.setTextureSize(64, 64);
		ornament8.mirror = true;
		setRotation(ornament8, 0F, 0.7853982F, 0F);
		ornament1 = new AM2ModelRenderer(this, 0, 19);
		ornament1.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament1.setRotationPoint(0F, 7.5F, 0F);
		ornament1.setTextureSize(64, 64);
		ornament1.mirror = true;
		setRotation(ornament1, 0F, -0.7853982F, 0F);
		ornament5 = new AM2ModelRenderer(this, 0, 19);
		ornament5.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament5.setRotationPoint(0F, 7.5F, 0F);
		ornament5.setTextureSize(64, 64);
		ornament5.mirror = true;
		setRotation(ornament5, 0F, 2.356194F, 0F);
		ornament3 = new AM2ModelRenderer(this, 0, 19);
		ornament3.addBox(9F, 0.5F, -1.5F, 2, 1, 1);
		ornament3.setRotationPoint(0F, 7.5F, 0F);
		ornament3.setTextureSize(64, 64);
		ornament3.mirror = true;
		setRotation(ornament3, 0F, -2.356194F, 0F);
		bar2 = new AM2ModelRenderer(this, 0, 0);
		bar2.addBox(8F, -0.5F, -3.5F, 1, 1, 10);
		bar2.setRotationPoint(0F, 7.5F, 0F);
		bar2.setTextureSize(64, 64);
		bar2.mirror = true;
		setRotation(bar2, 1.570796F, -1.570796F, 0F);
		bar4 = new AM2ModelRenderer(this, 0, 0);
		bar4.addBox(8F, -0.5F, -3.5F, 1, 1, 10);
		bar4.setRotationPoint(0F, 7.5F, 0F);
		bar4.setTextureSize(64, 64);
		bar4.mirror = true;
		setRotation(bar4, 1.570796F, 0F, 0F);

		Shape1.storeRestRotations();
		Shape2.storeRestRotations();
		Shape4.storeRestRotations();
		Shape5.storeRestRotations();
		Shape6.storeRestRotations();
		Shape7.storeRestRotations();
		tentaclefrontleft.storeRestRotations();
		tentaclebackleft.storeRestRotations();
		Shape11.storeRestRotations();
		tentaclebackright.storeRestRotations();
		tentaclefrontright.storeRestRotations();
		Shape3.storeRestRotations();
		Shape8.storeRestRotations();
		Shape14.storeRestRotations();
		Shape15.storeRestRotations();
		Shape16.storeRestRotations();
		tentacleright.storeRestRotations();
		tentacleleft.storeRestRotations();
		tentaclefront.storeRestRotations();
		tentacleback.storeRestRotations();
		bar3.storeRestRotations();
		bar1.storeRestRotations();
		bar2.storeRestRotations();
		bar4.storeRestRotations();
		ornament4.storeRestRotations();
		ornament2.storeRestRotations();
		ornament6.storeRestRotations();
		ornament7.storeRestRotations();
		ornament8.storeRestRotations();
		ornament1.storeRestRotations();
		ornament5.storeRestRotations();
		ornament3.storeRestRotations();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		if (!(entity instanceof EntityWaterGuardian)) return;

		EntityWaterGuardian guardian = ((EntityWaterGuardian)entity);

		float offset1 = (((float)Math.sin((float)guardian.ticksExisted / 40f)) / 4f) - 0.19f;
		float rot1 = (float)Math.toRadians(guardian.getOrbitRotation());

		GL11.glPushMatrix();

		GL11.glTranslatef(0, offset1, 0);

		ornament2.rotateAngleY = ornament2.getRestRotationY() + rot1;
		ornament4.rotateAngleY = ornament4.getRestRotationY() + rot1;
		ornament6.rotateAngleY = ornament6.getRestRotationY() + rot1;
		ornament7.rotateAngleY = ornament7.getRestRotationY() + rot1;

		ornament8.rotateAngleY = ornament8.getRestRotationY() + rot1;
		ornament1.rotateAngleY = ornament1.getRestRotationY() + rot1;
		ornament5.rotateAngleY = ornament5.getRestRotationY() + rot1;
		ornament3.rotateAngleY = ornament3.getRestRotationY() + rot1;

		bar1.rotateAngleY = bar1.getRestRotationY() + rot1;
		bar2.rotateAngleY = bar2.getRestRotationY() + rot1;
		bar3.rotateAngleY = bar3.getRestRotationY() + rot1;
		bar4.rotateAngleY = bar4.getRestRotationY() + rot1;

		updateRotations(guardian);

		Shape1.render(f5);
		Shape2.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		tentaclefrontleft.render(f5);
		tentaclebackleft.render(f5);
		Shape11.render(f5);
		tentaclebackright.render(f5);
		tentaclefrontright.render(f5);
		Shape3.render(f5);
		Shape8.render(f5);
		Shape14.render(f5);
		Shape15.render(f5);
		Shape16.render(f5);
		tentacleright.render(f5);
		tentacleleft.render(f5);
		tentaclefront.render(f5);
		tentacleback.render(f5);
		bar3.render(f5);
		bar1.render(f5);
		bar2.render(f5);
		bar4.render(f5);

		float offset = (((float)Math.sin((float)guardian.ticksExisted / 20f)) / 4f) - 0.19f;

		GL11.glPushMatrix();
		GL11.glTranslatef(0, offset, 0);

		ornament8.render(f5);
		ornament1.render(f5);
		ornament5.render(f5);
		ornament3.render(f5);
		ornament4.render(f5);
		ornament2.render(f5);
		ornament6.render(f5);
		ornament7.render(f5);

		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	@SuppressWarnings("incomplete-switch")
	private void updateRotations(EntityWaterGuardian guardian){
		switch (guardian.getCurrentAction()){
		case IDLE:
			float rot2 = ((float)Math.sin((float)guardian.ticksExisted / 10f) / 6);

			tentacleleft.rotateAngleZ = tentacleleft.getRestRotationZ() - rot2;
			tentacleright.rotateAngleZ = tentacleright.getRestRotationZ() + rot2;

			tentaclefront.rotateAngleX = tentaclefront.getRestRotationX() + rot2;
			tentacleback.rotateAngleX = tentacleback.getRestRotationX() - rot2;

			tentaclefrontright.rotateAngleX = -tentaclefront.rotateAngleX;
			tentaclefrontright.rotateAngleZ = -tentacleright.rotateAngleZ;

			tentaclebackright.rotateAngleX = -tentacleback.rotateAngleX;
			tentaclebackright.rotateAngleZ = -tentacleright.rotateAngleZ;

			tentaclebackleft.rotateAngleX = -tentacleback.rotateAngleX;
			tentaclebackleft.rotateAngleZ = -tentacleleft.rotateAngleZ;

			tentaclefrontleft.rotateAngleX = -tentaclefront.rotateAngleX;
			tentaclefrontleft.rotateAngleZ = -tentacleleft.rotateAngleZ;
			break;
		case SPINNING:
		case CASTING:
			float maxAngle = 45;
			float angle = (float)Math.toRadians(guardian.getTicksInCurrentAction() < 11 ? maxAngle * ((float)guardian.getTicksInCurrentAction() / 10f) : maxAngle);

			//float halfAngle = angle / 2;

			tentacleleft.rotateAngleZ = tentacleleft.getRestRotationZ() - angle;
			tentacleright.rotateAngleZ = tentacleright.getRestRotationZ() + angle;

			tentaclefront.rotateAngleX = tentaclefront.getRestRotationX() - angle;
			tentacleback.rotateAngleX = tentacleback.getRestRotationX() + angle;

			tentaclefrontright.rotateAngleX = -angle;
			tentaclefrontright.rotateAngleZ = angle;

			tentaclebackright.rotateAngleX = angle;
			tentaclebackright.rotateAngleZ = angle;

			tentaclebackleft.rotateAngleX = angle;
			tentaclebackleft.rotateAngleZ = -angle;

			tentaclefrontleft.rotateAngleX = -angle;
			tentaclefrontleft.rotateAngleZ = -angle;

			GL11.glRotatef(guardian.spinRotation, 0, 1, 0);
			break;
		case CLONE:
			break;
		}
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
