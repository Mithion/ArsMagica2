package am2.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTowerGuardian extends ModelBiped{
	//fields
	//ModelRenderer bipedBody;
	//ModelRenderer bipedHead;
	ModelRenderer LShoulder;
	ModelRenderer RShoulder;
	//ModelRenderer bipedLeftArm;
	//ModelRenderer bipedRightArm;
	//ModelRenderer bipedRightLeg;
	//ModelRenderer bipedLeftLeg;

    /*public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedEars;
    public ModelRenderer bipedCloak;*/

	public ModelTowerGuardian(){
		textureWidth = 128;
		textureHeight = 64;
		setTextureOffset("bipedBody.body1", 60, 0);
		setTextureOffset("bipedBody.body4", 90, 0);
		setTextureOffset("bipedBody.body3", 60, 10);
		setTextureOffset("bipedBody.body2", 90, 8);
		setTextureOffset("bipedHead.head2", 60, 44);
		setTextureOffset("bipedHead.head1", 60, 32);
		setTextureOffset("bipedHead.head8", 60, 24);
		setTextureOffset("bipedHead.head3", 66, 44);
		setTextureOffset("bipedHead.head4", 72, 44);
		setTextureOffset("bipedHead.head5", 60, 53);
		setTextureOffset("bipedHead.head6", 42, 32);
		setTextureOffset("bipedHead.head7", 42, 45);
		setTextureOffset("bipedLeftArm.larm2", 40, 16);
		setTextureOffset("bipedLeftArm.larm3", 40, 12);
		setTextureOffset("bipedLeftArm.larm1", 40, 0);
		setTextureOffset("bipedRightArm.rarm2", 40, 0);
		setTextureOffset("bipedRightArm.rarm3", 40, 12);
		setTextureOffset("bipedRightArm.rarm", 40, 16);
		setTextureOffset("bipedRightLeg.rleg1", 0, 0);
		setTextureOffset("bipedRightLeg.rleg2", 0, 16);
		setTextureOffset("bipedLeftLeg.lleg2", 0, 16);
		setTextureOffset("bipedLeftLeg.lleg1", 0, 0);

		bipedBody = new ModelRenderer(this, "bipedBody");
		bipedBody.setRotationPoint(0F, 0F, 0F);
		setRotation(bipedBody, 0.1784573F, 0.1189716F, 0.0892287F);
		bipedBody.mirror = true;
		bipedBody.addBox("body1", -5F, 0F, -2F, 10, 4, 4);
		bipedBody.addBox("body4", -4F, 8F, -2F, 8, 2, 4);
		bipedBody.addBox("body3", -3F, 10F, -2F, 6, 2, 4);
		bipedBody.addBox("body2", 0F, 0F, 0F, 1, 1, 1);
		bipedHead = new ModelRenderer(this, "bipedHead");
		bipedHead.setRotationPoint(0F, 0F, 0F);
		setRotation(bipedHead, 0F, 0F, 0F);
		bipedHead.mirror = true;
		bipedHead.addBox("head2", 2F, -6F, -4F, 1, 7, 1);
		bipedHead.addBox("head1", -3F, -5F, -3F, 6, 5, 6);
		bipedHead.addBox("head8", -3F, -5F, 3F, 6, 5, 1);
		bipedHead.addBox("head3", -3F, -6F, -4F, 1, 7, 1);
		bipedHead.addBox("head4", -2F, -6F, -4F, 4, 1, 1);
		bipedHead.addBox("head5", -4F, -6F, -3F, 8, 1, 7);
		bipedHead.addBox("head6", -4F, -5F, -3F, 1, 5, 7);
		bipedHead.addBox("head7", 3F, -5F, -3F, 1, 5, 7);
		LShoulder = new ModelRenderer(this, 0, 32);
		LShoulder.addBox(0F, -0.5F, -2F, 6, 2, 4);
		LShoulder.setRotationPoint(5F, -1F, 0F);
		LShoulder.setTextureSize(128, 64);
		LShoulder.mirror = true;
		setRotation(LShoulder, 0F, 0F, 0.3866576F);
		RShoulder = new ModelRenderer(this, 0, 40);
		RShoulder.addBox(0F, -0.5F, -2F, 6, 2, 4);
		RShoulder.setRotationPoint(-5F, 0F, 0F);
		RShoulder.setTextureSize(128, 64);
		RShoulder.mirror = true;
		setRotation(RShoulder, 0F, 0F, 2.75762F);
		bipedLeftArm = new ModelRenderer(this, "bipedLeftArm");
		bipedLeftArm.setRotationPoint(8F, 2F, 0F);
		setRotation(bipedLeftArm, 0F, 0F, 0F);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox("larm2", -2F, 7F, -2F, 4, 6, 4);
		bipedLeftArm.addBox("larm3", -1F, 6F, -1F, 2, 1, 2);
		bipedLeftArm.addBox("larm1", -2F, 0F, -2F, 4, 6, 4);
		bipedRightArm = new ModelRenderer(this, "bipedRightArm");
		bipedRightArm.setRotationPoint(-8F, 2F, 0F);
		setRotation(bipedRightArm, 0F, 0F, 0F);
		bipedRightArm.mirror = true;
		bipedRightArm.addBox("rarm2", -2F, 7F, -2F, 4, 6, 4);
		bipedRightArm.addBox("rarm3", -1F, 6F, -1F, 2, 1, 2);
		bipedRightArm.addBox("rarm", -2F, 0F, -2F, 4, 6, 4);
		bipedRightLeg = new ModelRenderer(this, "bipedRightLeg");
		bipedRightLeg.setRotationPoint(-3F, 12F, 0F);
		setRotation(bipedRightLeg, 0F, 0F, 0F);
		bipedRightLeg.mirror = true;
		bipedRightLeg.addBox("rleg1", -2F, 0F, -2F, 4, 9, 4);
		bipedRightLeg.addBox("rleg2", -3F, 9F, -3F, 6, 3, 6);
		bipedLeftLeg = new ModelRenderer(this, "bipedLeftLeg");
		bipedLeftLeg.setRotationPoint(3F, 12F, 0F);
		setRotation(bipedLeftLeg, 0F, 0F, 0F);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox("lleg2", -3F, 9F, -3F, 6, 3, 6);
		bipedLeftLeg.addBox("lleg1", -2F, 0F, -2F, 4, 9, 4);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		//bipedBody.render(f5);
		//bipedHead.render(f5);

		this.bipedHead.render(f5);
		this.bipedBody.render(f5);
		this.bipedRightArm.render(f5);
		this.bipedLeftArm.render(f5);
		this.bipedRightLeg.render(f5);
		this.bipedLeftLeg.render(f5);
		LShoulder.render(f5);
		RShoulder.render(f5);
    /*bipedLeftArm.render(f5);
    bipedRightArm.render(f5);
    bipedRightLeg.render(f5);
    bipedLeftLeg.render(f5);*/
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
