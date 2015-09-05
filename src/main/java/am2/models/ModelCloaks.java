package am2.models;

import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModelCloaks extends ModelBase{
	ResourceLocation cloakLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("cloak.png"));
	private static final int MASK_HOOD = 0x1;
	private static final int MASK_SLEEVES = 0x2;
	private static final int MASK_COATTAIL = 0x4;
	ModelRenderer FlapRight;
	ModelRenderer FlapBackWhole;
	ModelRenderer FlapLeft;
	ModelRenderer FlapFrontRight;
	ModelRenderer FlapFrontLeft;
	ModelRenderer FlapBackHalfRight;
	ModelRenderer FlapBackHalfLeft;
	ModelRenderer Hood;
	ModelRenderer Jacket;
	ModelRenderer LeftSleeveLong;
	ModelRenderer RightSleeveShort;
	ModelRenderer RightSleeveLong;
	ModelRenderer LeftSleeveShort;

	public ModelCloaks(){
		textureWidth = 64;
		textureHeight = 96;
		setTextureOffset("Hood.HoodBottom", 35, 19);
		setTextureOffset("Hood.HoodBack", 41, 0);
		setTextureOffset("Hood.HoodLeft", 0, 0);
		setTextureOffset("Hood.HoodRight", 20, 0);
		setTextureOffset("Hood.HoodTop", 0, 19);
		setTextureOffset("Jacket.JacketBack", 18, 50);
		setTextureOffset("Jacket.JacketLeft", 0, 50);
		setTextureOffset("Jacket.JacketFrontLeft", 9, 50);
		setTextureOffset("Jacket.JacketFrontRight", 39, 50);
		setTextureOffset("Jacket.JacketRight", 48, 50);

		FlapRight = new ModelRenderer(this, 22, 66);
		FlapRight.addBox(-0.2F, 0F, -2.4F, 1, 9, 4);
		FlapRight.setRotationPoint(-4F, 12F, 0F);
		FlapRight.setTextureSize(64, 96);
		FlapRight.mirror = true;
		setRotation(FlapRight, 0.0872665F, 0F, 0.0872665F);
		FlapBackWhole = new ModelRenderer(this, 44, 66);
		FlapBackWhole.addBox(-4.5F, 0F, -0.5F, 9, 14, 1);
		FlapBackWhole.setRotationPoint(0F, 10F, 2F);
		FlapBackWhole.setTextureSize(64, 96);
		FlapBackWhole.mirror = true;
		setRotation(FlapBackWhole, 0F, 0F, 0F);
		FlapLeft = new ModelRenderer(this, 33, 66);
		FlapLeft.addBox(-0.7F, 0F, -2.4F, 1, 9, 4);
		FlapLeft.setRotationPoint(4F, 12F, 0F);
		FlapLeft.setTextureSize(64, 96);
		FlapLeft.mirror = true;
		setRotation(FlapLeft, 0.0872665F, 0F, -0.0872665F);
		FlapFrontRight = new ModelRenderer(this, 13, 67);
		FlapFrontRight.addBox(-4F, 0F, -0.5F, 3, 6, 1);
		FlapFrontRight.setRotationPoint(0F, 12F, -2F);
		FlapFrontRight.setTextureSize(64, 96);
		FlapFrontRight.mirror = true;
		setRotation(FlapFrontRight, 0F, 0F, 0.0872665F);
		FlapFrontLeft = new ModelRenderer(this, 4, 67);
		FlapFrontLeft.addBox(1F, 0F, -0.5F, 3, 6, 1);
		FlapFrontLeft.setRotationPoint(0F, 12F, -2F);
		FlapFrontLeft.setTextureSize(64, 96);
		FlapFrontLeft.mirror = true;
		setRotation(FlapFrontLeft, 0F, 0F, -0.0872665F);
		FlapBackHalfRight = new ModelRenderer(this, 33, 80);
		FlapBackHalfRight.addBox(-4F, 0F, -0.5F, 4, 14, 1);
		FlapBackHalfRight.setRotationPoint(0F, 10F, 2F);
		FlapBackHalfRight.setTextureSize(64, 96);
		FlapBackHalfRight.mirror = true;
		setRotation(FlapBackHalfRight, 0F, 0F, 0.0872665F);
		FlapBackHalfLeft = new ModelRenderer(this, 22, 80);
		FlapBackHalfLeft.addBox(0F, 0F, -0.5F, 4, 14, 1);
		FlapBackHalfLeft.setRotationPoint(0F, 10F, 2F);
		FlapBackHalfLeft.setTextureSize(64, 96);
		FlapBackHalfLeft.mirror = true;
		setRotation(FlapBackHalfLeft, 0F, 0F, -0.0872665F);
		Hood = new ModelRenderer(this, "Hood");
		Hood.setRotationPoint(0F, 0F, 0F);
		setRotation(Hood, 0F, 0F, 0F);
		Hood.mirror = true;
		Hood.addBox("HoodBottom", -4F, -1F, -2F, 8, 1, 6);
		Hood.addBox("HoodBack", -5F, -9F, 4F, 10, 9, 1);
		Hood.addBox("HoodLeft", 4F, -9F, -5F, 1, 9, 9);
		Hood.addBox("HoodRight", -5F, -9F, -5F, 1, 9, 9);
		Hood.addBox("HoodTop", -4F, -9F, -5F, 8, 1, 9);
		Jacket = new ModelRenderer(this, "Jacket");
		Jacket.setRotationPoint(0F, 0F, 0F);
		setRotation(Jacket, 0F, 0F, 0F);
		Jacket.mirror = true;
		Jacket.addBox("JacketBack", -4.5F, 0F, 1.5F, 9, 10, 1);
		Jacket.addBox("JacketLeft", 3.5F, 0F, -1.5F, 1, 12, 3);
		Jacket.addBox("JacketFrontLeft", 1.5F, 0F, -2.5F, 3, 12, 1);
		Jacket.addBox("JacketFrontRight", -4.5F, 0F, -2.5F, 3, 12, 1);
		Jacket.addBox("JacketRight", -4.5F, 0F, -1.5F, 1, 12, 3);
		LeftSleeveLong = new ModelRenderer(this, 42, 31);
		LeftSleeveLong.addBox(-1F, -2F, -2.5F, 5, 12, 5);
		LeftSleeveLong.setRotationPoint(5F, 2F, 0F);
		LeftSleeveLong.setTextureSize(64, 96);
		LeftSleeveLong.mirror = true;
		setRotation(LeftSleeveLong, 0F, 0F, 0F);
		RightSleeveShort = new ModelRenderer(this, 21, 30);
		RightSleeveShort.addBox(-4F, -2F, -2.5F, 5, 4, 5);
		RightSleeveShort.setRotationPoint(-5F, 2F, 0F);
		RightSleeveShort.setTextureSize(64, 96);
		RightSleeveShort.mirror = true;
		setRotation(RightSleeveShort, 0F, 0F, 0F);
		RightSleeveLong = new ModelRenderer(this, 0, 31);
		RightSleeveLong.addBox(-4F, -2F, -2.5F, 5, 12, 5);
		RightSleeveLong.setRotationPoint(-5F, 2F, 0F);
		RightSleeveLong.setTextureSize(64, 96);
		RightSleeveLong.mirror = true;
		setRotation(RightSleeveLong, 0F, 0F, 0F);
		LeftSleeveShort = new ModelRenderer(this, 21, 40);
		LeftSleeveShort.addBox(-1F, -2F, -2.5F, 5, 4, 5);
		LeftSleeveShort.setRotationPoint(5F, 2F, 0F);
		LeftSleeveShort.setTextureSize(64, 96);
		LeftSleeveShort.mirror = true;
		setRotation(LeftSleeveShort, 0F, 0F, 0F);

		Jacket.addChild(FlapRight);
		Jacket.addChild(FlapBackWhole);
		Jacket.addChild(FlapLeft);
		Jacket.addChild(FlapFrontRight);
		Jacket.addChild(FlapFrontLeft);
		Jacket.addChild(FlapBackHalfRight);
		Jacket.addChild(FlapBackHalfLeft);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
	}

	public void render(EntityPlayer player, ModelBiped mainModel, float f5, float partialTicks, ResourceLocation cloakLocation, int drawMask){

		if (cloakLocation == null)
			return;

		float fr = ExtendedProperties.For(player).getFlipRotation();
		if (fr > 0){
			return;
		}

		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);

		Minecraft.getMinecraft().renderEngine.bindTexture(cloakLocation);

		float f2 = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
		GL11.glRotatef(-f2, 0, 1, 0);

		float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
		GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
		copyRotations(mainModel);

		if (ExtendedProperties.For(player).getShrinkPct() > 0){
			float pct = ExtendedProperties.For(player).getShrinkPct();
			float amt = 0.5f * pct;
			GL11.glTranslatef(0, 1 * 1 - pct, 0);
			GL11.glScalef(1 - amt, 1 - amt, 1 - amt);
		}

		GL11.glScalef(1, -1, -1);

		float f7 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
		float f8 = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);

		float leftLegRotation = MathHelper.cos(f8 * 0.6662F) * 1.4F * f7;
		float rightLegRotation = MathHelper.cos(f8 * 0.6662F + (float)Math.PI) * 1.4F * f7;

		if ((drawMask & MASK_COATTAIL) == MASK_COATTAIL){
			FlapBackHalfRight.showModel = false;
			FlapBackHalfLeft.showModel = false;
			FlapBackWhole.showModel = true;

			FlapBackWhole.rotateAngleX = Math.max(leftLegRotation, rightLegRotation) + (float)Math.toRadians(player.isSneaking() ? -10 : 10);
			FlapLeft.rotateAngleX = FlapBackWhole.rotateAngleX;
			FlapRight.rotateAngleX = FlapBackWhole.rotateAngleX;
		}else{
			FlapBackHalfRight.showModel = true;
			FlapBackHalfLeft.showModel = true;
			FlapBackWhole.showModel = false;

			FlapBackHalfLeft.rotateAngleX = leftLegRotation + (float)Math.toRadians(player.isSneaking() ? -10 : 10);
			FlapBackHalfRight.rotateAngleX = rightLegRotation + (float)Math.toRadians(player.isSneaking() ? -10 : 10);

			FlapLeft.rotateAngleX = FlapBackHalfLeft.rotateAngleX;
			FlapRight.rotateAngleX = FlapBackHalfRight.rotateAngleX;
		}

		FlapFrontRight.rotateAngleX = Math.min(0, rightLegRotation) + (float)Math.toRadians(-8);
		FlapFrontLeft.rotateAngleX = Math.min(0, leftLegRotation) + (float)Math.toRadians(-8);

		GL11.glTranslatef(0, 0.15f, 0);
		Jacket.render(f5);

		if ((drawMask & MASK_HOOD) == MASK_HOOD){
			GL11.glTranslatef(0, 0.075f, 0);
			Hood.render(f5);
			GL11.glTranslatef(0, -0.075f, 0);
		}

		float offset = 0.05f;
		GL11.glTranslatef(-offset, 0, 0);
		if ((drawMask & MASK_SLEEVES) == MASK_SLEEVES)
			LeftSleeveLong.render(f5);
		else
			LeftSleeveShort.render(f5);
		GL11.glTranslatef(offset * 2, 0, 0);
		if ((drawMask & MASK_SLEEVES) == MASK_SLEEVES)
			RightSleeveLong.render(f5);
		else
			RightSleeveShort.render(f5);

		GL11.glPopAttrib();
		GL11.glPopMatrix();

	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	private void copyRotations(ModelBiped source){
		copyRotation(source.bipedHead, Hood);
		copyRotation(source.bipedBody, Jacket);
		copyRotation(source.bipedLeftArm, LeftSleeveLong);
		copyRotation(source.bipedRightArm, RightSleeveLong);
		copyRotation(source.bipedLeftArm, LeftSleeveShort);
		copyRotation(source.bipedRightArm, RightSleeveShort);
	}

	private void copyRotation(ModelRenderer source, ModelRenderer dest){
		dest.rotateAngleX = source.rotateAngleX;
		dest.rotateAngleY = source.rotateAngleY;
		dest.rotateAngleZ = source.rotateAngleZ;

		dest.offsetX = source.offsetX;
		dest.offsetY = source.offsetY;
		dest.offsetZ = source.offsetZ;
	}
}