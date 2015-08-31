package am2.entities.models;

import am2.entities.EntityManaElemental;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelManaElemental extends ModelBiped
{
	//fields
	ModelRenderer Core;
	ModelRenderer Pelvis;

	public ModelManaElemental()
	{		
		textureWidth = 64;
		textureHeight = 32;

		bipedHead = new ModelRenderer(this, 32, 0);
		bipedHead.addBox(-4F, -4F, -4F, 8, 4, 8);
		bipedHead.setRotationPoint(0F, -9F, 0F);
		bipedHead.setTextureSize(64, 32);
		bipedHead.mirror = true;
		setRotation(bipedHead, 0F, 0F, 0F);
		bipedBody = new ModelRenderer(this, 0, 26);
		bipedBody.addBox(-6F, -1F, -2F, 12, 2, 4);
		bipedBody.setRotationPoint(0F, -7F, 0F);
		bipedBody.setTextureSize(64, 32);
		bipedBody.mirror = true;
		setRotation(bipedBody, 0F, 0F, 0F);
		Core = new ModelRenderer(this, 0, 20);
		Core.addBox(-1.5F, 3.5F, -1.5F, 3, 3, 3);
		Core.setRotationPoint(0F, -7F, 0F);
		Core.setTextureSize(64, 32);
		Core.mirror = true;
		setRotation(Core, 0F, 0F, 0F);
		Pelvis = new ModelRenderer(this, 0, 0);
		Pelvis.addBox(-5F, 9F, -2F, 10, 2, 4);
		Pelvis.setRotationPoint(0F, -7F, 0F);
		Pelvis.setTextureSize(64, 32);
		Pelvis.mirror = true;
		setRotation(Pelvis, 0F, 0F, 0F);
		bipedRightArm = new ModelRenderer(this, 33, 16);
		bipedRightArm.addBox(-1.5F, 9F, -1.5F, 3, 2, 3);
		bipedRightArm.setRotationPoint(-8F, -7F, 0F);
		bipedRightArm.setTextureSize(64, 32);
		bipedRightArm.mirror = true;
		setRotation(bipedRightArm, 0F, 0F, 0F);
		bipedLeftLeg = new ModelRenderer(this, 47, 22);
		bipedLeftLeg.addBox(-1.5F, 14F, -1.5F, 3, 2, 3);
		bipedLeftLeg.setRotationPoint(-4F, 3F, 0F);
		bipedLeftLeg.setTextureSize(64, 32);
		bipedLeftLeg.mirror = true;
		setRotation(bipedLeftLeg, 0F, 0F, 0F);
		bipedLeftArm = new ModelRenderer(this, 33, 22);
		bipedLeftArm.addBox(-1.5F, 9F, -1.5F, 3, 2, 3);
		bipedLeftArm.setRotationPoint(8F, -7F, 0F);
		bipedLeftArm.setTextureSize(64, 32);
		bipedLeftArm.mirror = true;
		setRotation(bipedLeftArm, 0F, 0F, 0F);
		bipedRightLeg = new ModelRenderer(this, 47, 16);
		bipedRightLeg.addBox(-1.5F, 14F, -1.5F, 3, 2, 3);
		bipedRightLeg.setRotationPoint(4F, 3F, 0F);
		bipedRightLeg.setTextureSize(64, 32);
		bipedRightLeg.mirror = true;
		setRotation(bipedRightLeg, 0F, 0F, 0F);		
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{		
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
		bipedBody.render(f5);
		Core.render(f5);
		Pelvis.render(f5);
		bipedRightArm.render(f5);
		bipedLeftArm.render(f5);
		bipedRightLeg.render(f5);
		bipedLeftLeg.render(f5);
		if (entity instanceof EntityManaElemental){
			((EntityManaElemental)entity).setOnGroudFloat(this.onGround);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		setRotation(Core, bipedBody.rotateAngleX, bipedBody.rotateAngleY, bipedBody.rotateAngleZ);
		setRotation(Pelvis, bipedBody.rotateAngleX, bipedBody.rotateAngleY, bipedBody.rotateAngleZ);
		
		bipedHead.setRotationPoint(0F, -9F, 0F);
		bipedBody.setRotationPoint(0F, -7F, 0F);
		bipedRightArm.setRotationPoint(-8F, -7F, 0F);
		bipedLeftArm.setRotationPoint(8F, -7F, 0F);
		bipedRightLeg.setRotationPoint(4F, 7F, 0F);
		bipedLeftLeg.setRotationPoint(-4F, 7F, 0F);
	}

}
