package am2.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import am2.api.spell.enums.Affinity;
import am2.entities.EntityFlicker;

public class ModelFlicker extends ModelBase
{
	ModelRenderer Body;
	ModelRenderer Head;
	ModelRenderer RightArm;
	ModelRenderer LeftArm;
	ModelRenderer LegBase;
	ModelRenderer LeftHorn;
	ModelRenderer RightHorn;

	public ModelFlicker()
	{
		textureWidth = 64;
		textureHeight = 32;

		Body = new ModelRenderer(this, 0, 16);
		Body.addBox(-1.5F, 0F, -1.5F, 3, 3, 3);
		Body.setRotationPoint(1.5F, 0F, 1.5F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		Head = new ModelRenderer(this, 0, 0);
		Head.addBox(0F, 0F, 0F, 5, 4, 5);
		Head.setRotationPoint(-1F, -4F, -1F);
		Head.setTextureSize(64, 32);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);
		RightArm = new ModelRenderer(this, 0, 10);
		RightArm.addBox(-0.5F, 0F, -0.5F, 1, 4, 1);
		RightArm.setRotationPoint(-0.5F, 0F, 1.5F);
		RightArm.setTextureSize(64, 32);
		RightArm.mirror = true;
		setRotation(RightArm, 0F, 0F, 0.2602503F);
		LeftArm = new ModelRenderer(this, 5, 10);
		LeftArm.addBox(-0.5F, 0F, -0.5F, 1, 4, 1);
		LeftArm.setRotationPoint(3.5F, 0F, 1.5F);
		LeftArm.setTextureSize(64, 32);
		LeftArm.mirror = true;
		setRotation(LeftArm, 0F, 0F, -0.2230717F);
		LegBase = new ModelRenderer(this, 21, 0);
		LegBase.addBox(-0.5F, 3F, -0.5F, 1, 2, 1);
		LegBase.setRotationPoint(1.5F, 0F, 1.5F);
		LegBase.setTextureSize(64, 32);
		LegBase.mirror = true;
		setRotation(LegBase, 0F, 0F, 0F);
		LeftHorn = new ModelRenderer(this, 10, 10);
		LeftHorn.addBox(0F, 0F, 0F, 1, 2, 1);
		LeftHorn.setRotationPoint(4F, -5F, 1F);
		LeftHorn.setTextureSize(64, 32);
		LeftHorn.mirror = true;
		setRotation(LeftHorn, 0F, 0F, 0F);
		RightHorn = new ModelRenderer(this, 10, 10);
		RightHorn.addBox(0F, 0F, 0F, 1, 2, 1);
		RightHorn.setRotationPoint(-2F, -5F, 1F);
		RightHorn.setTextureSize(64, 32);
		RightHorn.mirror = true;
		setRotation(RightHorn, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);

		Affinity aff = ((EntityFlicker)entity).getFlickerAffinity();
		GL11.glPushMatrix();
		GL11.glColor3f(((aff.color >> 16) & 0xFF) / 255.0f, ((aff.color >> 8) & 0xFF) / 255.0f, (aff.color & 0xFF) / 255.0f);
		GL11.glTranslatef(-0.05f, 1.3f, -0.05f);
		GL11.glRotatef(-entity.rotationPitch, 1, 0, 0);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Body.render(f5);
		Head.render(f5);
		RightArm.render(f5);
		LeftArm.render(f5);
		LegBase.render(f5);
		RightHorn.render(f5);
		LeftHorn.render(f5);

		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
