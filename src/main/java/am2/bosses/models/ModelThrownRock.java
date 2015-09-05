package am2.bosses.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelThrownRock extends ModelBase{
	//fields
	ModelRenderer Rock3;
	ModelRenderer Rock1;
	ModelRenderer Rock2;

	public ModelThrownRock(){
		textureWidth = 64;
		textureHeight = 64;

		Rock3 = new ModelRenderer(this, 1, 32);
		Rock3.addBox(-6F, -1F, -2F, 8, 6, 7);
		Rock3.setRotationPoint(0F, 0F, 0F);
		Rock3.setTextureSize(64, 64);
		Rock3.mirror = true;
		setRotation(Rock3, 0F, 0F, 0F);
		Rock3.mirror = false;
		Rock1 = new ModelRenderer(this, 0, 31);
		Rock1.addBox(-2F, 0F, -4F, 7, 8, 8);
		Rock1.setRotationPoint(0F, 0F, 0F);
		Rock1.setTextureSize(64, 64);
		Rock1.mirror = true;
		setRotation(Rock1, 0F, 0F, 0F);
		Rock1.mirror = false;
		Rock2 = new ModelRenderer(this, 1, 32);
		Rock2.addBox(-10F, 0F, -4F, 8, 6, 7);
		Rock2.setRotationPoint(0F, 0F, 0F);
		Rock2.setTextureSize(64, 64);
		Rock2.mirror = true;
		setRotation(Rock2, 0F, 0F, 0F);
		Rock2.mirror = false;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		GL11.glPushMatrix();
		float rotation = (entity.ticksExisted * 36) % 360;
		GL11.glRotatef(rotation, 1, 1, 1);


		Rock3.render(f5);
		Rock1.render(f5);
		Rock2.render(f5);

		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
