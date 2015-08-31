package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import am2.entities.EntityAirSled;

public class ModelAirGuardianHoverball extends ModelBase
{
	//fields
	ModelRenderer Ball1;
	ModelRenderer Ball2;

	public ModelAirGuardianHoverball()
	{
		textureWidth = 64;
		textureHeight = 64;

		Ball1 = new ModelRenderer(this, 21, 0);
		Ball1.addBox(-3F, -3F, -3F, 6, 6, 6);
		Ball1.setRotationPoint(0F, 10F, 0F);
		Ball1.setTextureSize(64, 64);
		Ball1.mirror = true;
		setRotation(Ball1, -0.7853982F, -1.570796F, 0.7853982F);
		Ball2 = new ModelRenderer(this, 21, 0);
		Ball2.addBox(-3F, -3F, -3F, 6, 6, 6);
		Ball2.setRotationPoint(0F, 10F, 0F);
		Ball2.setTextureSize(64, 64);
		Ball2.mirror = true;
		setRotation(Ball2, -0.7853982F, -1.570796F, -0.7853982F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		if (entity != null){
			Ball1.rotateAngleX = (float) Math.toRadians(((EntityAirSled)entity).getRotation());
			Ball1.rotateAngleY =  (float) -Math.toRadians(-((EntityAirSled)entity).getRotation());
			Ball1.rotateAngleZ =  (float) -Math.toRadians(-((EntityAirSled)entity).getRotation());

			Ball2.rotateAngleX = (float) -Math.toRadians(((EntityAirSled)entity).getRotation());
			Ball2.rotateAngleY = (float) Math.toRadians(((EntityAirSled)entity).getRotation());
			Ball1.rotateAngleZ =  (float) Math.toRadians(-((EntityAirSled)entity).getRotation());
		}
		Ball1.render(f5);
		Ball2.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
