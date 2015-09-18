package am2.bosses.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelLifeGuardian extends ModelBase{
	//fields
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape9;
	ModelRenderer Shape10;
	ModelRenderer Shape11;
	ModelRenderer Shape12;
	ModelRenderer Shape13;
	ModelRenderer Shape14;
	ModelRenderer Shape15;
	ModelRenderer Shape16;
	ModelRenderer Shape17;
	ModelRenderer Shape18;
	ModelRenderer Shape19;
	ModelRenderer Shape20;
	ModelRenderer Shape21;
	ModelRenderer Shape22;
	ModelRenderer Shape23;
	ModelRenderer Shape24;

	public ModelLifeGuardian(){
		textureWidth = 128;
		textureHeight = 128;

		Shape1 = new ModelRenderer(this, 0, 87);
		Shape1.addBox(-4F, -4F, -4F, 8, 8, 8);
		Shape1.setRotationPoint(0F, 16F, 0F);
		Shape1.setTextureSize(128, 128);
		Shape1.mirror = true;
		setRotation(Shape1, -0.7853982F, -1.570796F, -0.7853982F);
		Shape2 = new ModelRenderer(this, 0, 87);
		Shape2.addBox(-4F, -4F, -4F, 8, 8, 8);
		Shape2.setRotationPoint(0F, 16F, 0F);
		Shape2.setTextureSize(128, 128);
		Shape2.mirror = true;
		setRotation(Shape2, -0.7853982F, -1.570796F, 0.7853982F);
		Shape3 = new ModelRenderer(this, 0, 79);
		Shape3.addBox(-3F, -1F, -3F, 6, 1, 6);
		Shape3.setRotationPoint(0F, 6F, 0F);
		Shape3.setTextureSize(128, 128);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 0, 69);
		Shape4.addBox(-4F, -1F, -4F, 8, 1, 8);
		Shape4.setRotationPoint(0F, 5F, 0F);
		Shape4.setTextureSize(128, 128);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 0F, 0F);
		Shape5 = new ModelRenderer(this, 0, 57);
		Shape5.addBox(-5F, -1F, -5F, 10, 1, 10);
		Shape5.setRotationPoint(0F, 4F, 0F);
		Shape5.setTextureSize(128, 128);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape6 = new ModelRenderer(this, 0, 38);
		Shape6.addBox(-6F, -6F, -6F, 12, 6, 12);
		Shape6.setRotationPoint(0F, 3F, 0F);
		Shape6.setTextureSize(128, 128);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 0, 24);
		Shape7.addBox(-5F, -3F, -5F, 10, 3, 10);
		Shape7.setRotationPoint(0F, -3F, 0F);
		Shape7.setTextureSize(128, 128);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		Shape8 = new ModelRenderer(this, 0, 13);
		Shape8.addBox(-4F, -2F, -4F, 8, 2, 8);
		Shape8.setRotationPoint(0F, -6F, 0F);
		Shape8.setTextureSize(128, 128);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, 0F);
		Shape9 = new ModelRenderer(this, 0, 5);
		Shape9.addBox(-3F, -1F, -3F, 6, 1, 6);
		Shape9.setRotationPoint(0F, -8F, 0F);
		Shape9.setTextureSize(128, 128);
		Shape9.mirror = true;
		setRotation(Shape9, 0F, 0F, 0F);
		Shape10 = new ModelRenderer(this, 0, 0);
		Shape10.addBox(-1.5F, -1F, -1.5F, 3, 1, 3);
		Shape10.setRotationPoint(0F, -9F, 0F);
		Shape10.setTextureSize(128, 128);
		Shape10.mirror = true;
		setRotation(Shape10, 0F, 0F, 0F);
		Shape11 = new ModelRenderer(this, 0, 104);
		Shape11.addBox(-8F, -1F, -1F, 16, 2, 2);
		Shape11.setRotationPoint(0F, 6F, 0F);
		Shape11.setTextureSize(128, 128);
		Shape11.mirror = true;
		setRotation(Shape11, -0.7853982F, 0F, 0F);
		Shape12 = new ModelRenderer(this, 0, 104);
		Shape12.addBox(-8F, -1F, -1F, 16, 2, 2);
		Shape12.setRotationPoint(0F, 6F, 0F);
		Shape12.setTextureSize(128, 128);
		Shape12.mirror = true;
		setRotation(Shape12, -0.7853982F, -1.570796F, 0F);
		Shape13 = new ModelRenderer(this, 9, 109);
		Shape13.addBox(-1F, -10F, -1F, 2, 11, 2);
		Shape13.setRotationPoint(7F, 5F, 0F);
		Shape13.setTextureSize(128, 128);
		Shape13.mirror = true;
		setRotation(Shape13, 0F, 0F, 0F);
		Shape14 = new ModelRenderer(this, 27, 109);
		Shape14.addBox(-1F, -10F, -1F, 2, 11, 2);
		Shape14.setRotationPoint(-7F, 5F, 0F);
		Shape14.setTextureSize(128, 128);
		Shape14.mirror = true;
		setRotation(Shape14, 0F, 0F, 0F);
		Shape15 = new ModelRenderer(this, 18, 109);
		Shape15.addBox(-1F, -10F, -1F, 2, 11, 2);
		Shape15.setRotationPoint(0F, 5F, 7F);
		Shape15.setTextureSize(128, 128);
		Shape15.mirror = true;
		setRotation(Shape15, 0F, 0F, 0F);
		Shape16 = new ModelRenderer(this, 0, 109);
		Shape16.addBox(-1F, -10F, -1F, 2, 11, 2);
		Shape16.setRotationPoint(0F, 5F, -7F);
		Shape16.setTextureSize(128, 128);
		Shape16.mirror = true;
		setRotation(Shape16, 0F, 0F, 0F);
		Shape17 = new ModelRenderer(this, 36, 114);
		Shape17.addBox(-0.5F, -6F, -0.5F, 1, 7, 1);
		Shape17.setRotationPoint(-7F, -5F, 0F);
		Shape17.setTextureSize(128, 128);
		Shape17.mirror = true;
		setRotation(Shape17, 0F, 0F, 0.4537856F);
		Shape18 = new ModelRenderer(this, 36, 114);
		Shape18.addBox(-0.5F, -6F, -0.5F, 1, 7, 1);
		Shape18.setRotationPoint(7F, -5F, 0F);
		Shape18.setTextureSize(128, 128);
		Shape18.mirror = true;
		setRotation(Shape18, 0F, 0F, -0.4537856F);
		Shape19 = new ModelRenderer(this, 36, 114);
		Shape19.addBox(-0.5F, -6F, -0.5F, 1, 7, 1);
		Shape19.setRotationPoint(0F, -5F, -7F);
		Shape19.setTextureSize(128, 128);
		Shape19.mirror = true;
		setRotation(Shape19, -0.4537856F, 0F, 0F);
		Shape20 = new ModelRenderer(this, 36, 114);
		Shape20.addBox(-0.5F, -6F, -0.5F, 1, 7, 1);
		Shape20.setRotationPoint(0F, -5F, 7F);
		Shape20.setTextureSize(128, 128);
		Shape20.mirror = true;
		setRotation(Shape20, 0.4537856F, 0F, 0F);
		Shape21 = new ModelRenderer(this, 41, 114);
		Shape21.addBox(-7F, -7F, 0F, 14, 14, 0);
		Shape21.setRotationPoint(0F, 0F, -9F);
		Shape21.setTextureSize(128, 128);
		Shape21.mirror = true;
		setRotation(Shape21, 0F, 0F, 0F);
		Shape22 = new ModelRenderer(this, 41, 114);
		Shape22.addBox(-7F, -7F, 0F, 14, 14, 0);
		Shape22.setRotationPoint(0F, 0F, 9F);
		Shape22.setTextureSize(128, 128);
		Shape22.mirror = true;
		setRotation(Shape22, 0F, -3.141593F, 0F);
		Shape23 = new ModelRenderer(this, 41, 100);
		Shape23.addBox(0F, -7F, -7F, 0, 14, 14);
		Shape23.setRotationPoint(9F, 0F, 0F);
		Shape23.setTextureSize(128, 128);
		Shape23.mirror = true;
		setRotation(Shape23, 0F, -3.141593F, 0F);
		Shape24 = new ModelRenderer(this, 41, 100);
		Shape24.addBox(0F, -7F, -7F, 0, 14, 14);
		Shape24.setRotationPoint(-9F, 0F, 0F);
		Shape24.setTextureSize(128, 128);
		Shape24.mirror = true;
		setRotation(Shape24, 0F, 0F, 0F);
	}

	public void setAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5){
		float angle = (f2 + f5) * 0.05f;
		setRotation(Shape21, 0, 0, angle);
		setRotation(Shape22, 0, 0, angle);
		setRotation(Shape23, angle, 0, 0);
		setRotation(Shape24, angle, 0, 0);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);

		setAngles(entity, f, f1, f2, f3, f4, f5);

		float angle = (f2 + f5) * 2;

		//bottom bits
		//Shape1.render(f5);

		//main body
		GL11.glPushMatrix();
		GL11.glRotatef(angle, 0, 1, 0);
		GL11.glTranslatef(0, (float)Math.sin(f2 / 15f) / 10f, 0);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
		Shape9.render(f5);
		Shape10.render(f5);
		/*GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glRotatef(-angle, 0, 1, 0);*/
		//pillars
		Shape11.render(f5);
		Shape12.render(f5);
		Shape13.render(f5);
		Shape14.render(f5);
		Shape15.render(f5);
		Shape16.render(f5);
		Shape17.render(f5);
		Shape18.render(f5);
		Shape19.render(f5);
		Shape20.render(f5);
		GL11.glPopMatrix();

		//runes

		Shape21.render(f5);
		Shape22.render(f5);
		Shape23.render(f5);
		Shape24.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
