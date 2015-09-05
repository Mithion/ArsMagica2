package am2.entities.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelWisp extends ModelBase{
	//fields
	ModelRenderer WispPart1;
	ModelRenderer WispPart2;
	ModelRenderer WispPart3;

	public ModelWisp(){
		textureWidth = 64;
		textureHeight = 32;

		WispPart1 = new ModelRenderer(this, 0, 0);
		WispPart1.addBox(-1F, -4F, -1F, 2, 8, 2);
		WispPart1.setRotationPoint(0F, 20F, 0F);
		WispPart1.setTextureSize(64, 32);
		WispPart1.mirror = true;
		setRotation(WispPart1, 0F, 0F, 0F);
		WispPart2 = new ModelRenderer(this, 0, 0);
		WispPart2.addBox(-4F, -1F, -1F, 8, 2, 2);
		WispPart2.setRotationPoint(0F, 20F, 0F);
		WispPart2.setTextureSize(64, 32);
		WispPart2.mirror = true;
		setRotation(WispPart2, 0F, 0F, 0F);
		WispPart3 = new ModelRenderer(this, 0, 0);
		WispPart3.addBox(-1F, -1F, -4F, 2, 2, 8);
		WispPart3.setRotationPoint(0F, 20F, 0F);
		WispPart3.setTextureSize(64, 32);
		WispPart3.mirror = true;
		setRotation(WispPart3, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		WispPart1.render(f5);
		WispPart2.render(f5);
		WispPart3.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5){
		WispPart1.rotateAngleX = (float)(WispPart1.rotateAngleX + 0.01) % 360;
		WispPart1.rotateAngleZ = (float)(WispPart1.rotateAngleZ + 0.01) % 360;

		WispPart2.rotateAngleX = (float)(WispPart2.rotateAngleX + 0.01) % 360;
		WispPart2.rotateAngleY = (float)(WispPart2.rotateAngleY + 0.01) % 360;

		WispPart3.rotateAngleY = (float)(WispPart3.rotateAngleY + 0.01) % 360;
		WispPart3.rotateAngleZ = (float)(WispPart3.rotateAngleZ + 0.01) % 360;
	}

}
