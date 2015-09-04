package am2.bosses.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelPlantGuardianSickle extends ModelBase{
	//fields
	ModelRenderer Shape39;
	ModelRenderer Sickle2;
	ModelRenderer Sickle3;
	ModelRenderer Sickle4;
	ModelRenderer Sickle5;

	private boolean spin;

	public ModelPlantGuardianSickle(){
		textureWidth = 128;
		textureHeight = 128;

		spin = true;

		Shape39 = new ModelRenderer(this, 0, 90);
		Shape39.addBox(0F, 0F, 0F, 2, 2, 36);
		Shape39.setRotationPoint(0F, -6F, 2F);
		Shape39.setTextureSize(128, 128);
		Shape39.mirror = true;
		setRotation(Shape39, -3.141593F, 0F, 0F);
		Sickle2 = new ModelRenderer(this, 114, 112);
		Sickle2.addBox(-0.5F, -12F, -40F, 3, 12, 4);
		Sickle2.setRotationPoint(0F, 0F, 0F);
		Sickle2.setTextureSize(128, 128);
		Sickle2.mirror = true;
		setRotation(Sickle2, 0F, 0F, 0F);
		Sickle3 = new ModelRenderer(this, 120, 78);
		Sickle3.addBox(0.5F, -17F, -37.18667F, 1, 30, 3);
		Sickle3.setRotationPoint(0F, 0F, 0F);
		Sickle3.setTextureSize(128, 128);
		Sickle3.mirror = true;
		setRotation(Sickle3, 0F, 0F, 0F);
		Sickle4 = new ModelRenderer(this, 107, 114);
		Sickle4.addBox(0.5F, -11F, -37F, 1, 12, 2);
		Sickle4.setRotationPoint(0F, 0F, 0F);
		Sickle4.setTextureSize(128, 128);
		Sickle4.mirror = true;
		setRotation(Sickle4, 0.6283185F, 0F, 0F);
		Sickle5 = new ModelRenderer(this, 102, 119);
		Sickle5.addBox(0.5F, -17F, -32.5F, 1, 8, 1);
		Sickle5.setRotationPoint(0F, 0F, 0F);
		Sickle5.setTextureSize(128, 128);
		Sickle5.mirror = true;
		setRotation(Sickle5, 1.134464F, 0F, 0F);
	}

	public void setNoSpin(){
		this.spin = false;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		GL11.glPushMatrix();

		if (spin){
			GL11.glRotatef(-entity.rotationYaw, 0, 1, 0);

			float rotation = (entity.ticksExisted * 36) % 360;
			GL11.glRotatef(rotation, 1, 0, 0);
		}

		Shape39.render(f5);
		Sickle2.render(f5);
		Sickle3.render(f5);
		Sickle4.render(f5);
		Sickle5.render(f5);
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
