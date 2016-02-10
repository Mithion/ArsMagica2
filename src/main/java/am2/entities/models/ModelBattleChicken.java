package am2.entities.models;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelBattleChicken extends ModelBase{
	//fields
	ModelRenderer head;
	ModelRenderer bill;
	ModelRenderer chin;
	ModelRenderer rightLeg;
	ModelRenderer leftLeg;
	ModelRenderer rightWing;
	ModelRenderer leftWing;
	ModelRenderer body;
	ModelRenderer helmet;
	ModelRenderer cigar;

	public ModelBattleChicken(){
		textureWidth = 64;
		textureHeight = 32;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2F, -3F, -2F, 4, 4, 3);
		head.setRotationPoint(0F, 11F, -2F);
		head.setTextureSize(64, 32);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		bill = new ModelRenderer(this, 14, 0);
		bill.addBox(-2F, -1F, -4F, 4, 2, 2);
		bill.setRotationPoint(0F, 11F, -2F);
		bill.setTextureSize(64, 32);
		bill.mirror = true;
		setRotation(bill, 0F, 0F, 0F);
		chin = new ModelRenderer(this, 14, 4);
		chin.addBox(-1F, 1F, -4F, 2, 2, 2);
		chin.setRotationPoint(0F, 11F, -2F);
		chin.setTextureSize(64, 32);
		chin.mirror = true;
		setRotation(chin, 0F, 0F, 0F);
		rightLeg = new ModelRenderer(this, 26, 0);
		rightLeg.addBox(0F, 0F, 0F, 3, 5, 3);
		rightLeg.setRotationPoint(0F, 18F, 0F);
		rightLeg.setTextureSize(64, 32);
		rightLeg.mirror = true;
		setRotation(rightLeg, 0F, 0F, 0F);
		leftLeg = new ModelRenderer(this, 26, 0);
		leftLeg.addBox(-3F, 0F, 0F, 3, 5, 3);
		leftLeg.setRotationPoint(0F, 18F, 0F);
		leftLeg.setTextureSize(64, 32);
		leftLeg.mirror = true;
		setRotation(leftLeg, 0F, 0F, 0F);
		rightWing = new ModelRenderer(this, 24, 13);
		rightWing.addBox(0F, 0F, 0F, 1, 4, 6);
		rightWing.setRotationPoint(3F, 12F, -2F);
		rightWing.setTextureSize(64, 32);
		rightWing.mirror = true;
		setRotation(rightWing, 0F, 0F, 0F);
		leftWing = new ModelRenderer(this, 24, 13);
		leftWing.addBox(0F, 0F, 0F, 1, 4, 6);
		leftWing.setRotationPoint(-4F, 12F, -2F);
		leftWing.setTextureSize(64, 32);
		leftWing.mirror = true;
		setRotation(leftWing, 0F, 0F, 0F);
		body = new ModelRenderer(this, 0, 9);
		body.addBox(-3F, -3F, -3F, 6, 8, 6);
		body.setRotationPoint(0F, 15F, 0F);
		body.setTextureSize(64, 32);
		body.mirror = true;
		setRotation(body, 1.570796F, 0F, 0F);
		helmet = new ModelRenderer(this, 39, 0);
		helmet.addBox(-3F, -4F, -3F, 6, 3, 5);
		helmet.setRotationPoint(0F, 11F, -2F);
		helmet.setTextureSize(64, 32);
		helmet.mirror = true;
		setRotation(helmet, 0F, 0F, 0F);
		cigar = new ModelRenderer(this, 39, 9);
		cigar.addBox(-2F, 0F, -7F, 1, 1, 3);
		cigar.setRotationPoint(0F, 11F, -2F);
		cigar.setTextureSize(64, 32);
		cigar.mirror = true;
		setRotation(cigar, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		head.render(f5);
		bill.render(f5);
		chin.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
		rightWing.render(f5);
		leftWing.render(f5);
		body.render(f5);
		helmet.render(f5);
		cigar.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6){
		head.rotateAngleX = -(par5 / (180F / (float)Math.PI));
		head.rotateAngleY = par4 / (180F / (float)Math.PI);

		helmet.rotateAngleX = head.rotateAngleX;
		helmet.rotateAngleY = head.rotateAngleY;

		cigar.rotateAngleX = head.rotateAngleX;
		cigar.rotateAngleY = head.rotateAngleY;

		bill.rotateAngleX = head.rotateAngleX;
		bill.rotateAngleY = head.rotateAngleY;
		chin.rotateAngleX = head.rotateAngleX;
		chin.rotateAngleY = head.rotateAngleY;
		body.rotateAngleX = ((float)Math.PI / 2F);
		rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
		rightWing.rotateAngleZ = par3;
		leftWing.rotateAngleZ = -par3;
	}

}
