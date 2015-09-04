package am2.models;

import am2.buffs.BuffList;
import am2.entities.EntityDarkling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelDarkling extends ModelBase{
	ModelRenderer headMain;
	ModelRenderer body;
	ModelRenderer mane;
	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer leg3;
	ModelRenderer leg4;
	ModelRenderer tail;
	ModelRenderer Nose8;
	ModelRenderer Nose9;
	ModelRenderer Nose10;
	ModelRenderer Nose1;
	ModelRenderer Nose2;
	ModelRenderer Nose5;
	ModelRenderer Tail2;
	ModelRenderer Nose3;
	ModelRenderer Nose6;
	ModelRenderer Nose7;
	ModelRenderer Nose4;
	ModelRenderer FinR1;
	ModelRenderer FinR2;
	ModelRenderer FinR4;
	ModelRenderer FinR3;
	ModelRenderer FinL1;
	ModelRenderer FinL2;
	ModelRenderer FinL3;
	ModelRenderer FinL4;
	ModelRenderer Spike10;
	ModelRenderer Spike9;
	ModelRenderer Spike1;
	ModelRenderer Spike3;
	ModelRenderer Spike5;
	ModelRenderer Spike7;
	ModelRenderer Spike2;
	ModelRenderer Spike4;
	ModelRenderer Spike6;
	ModelRenderer Spike8;

	public ModelDarkling(){
		textureWidth = 64;
		textureHeight = 64;

		headMain = new ModelRenderer(this, 0, 0);
		headMain.addBox(-3F, -3F, -2F, 6, 6, 4);
		headMain.setRotationPoint(-1F, 13.5F, -7F);
		headMain.setTextureSize(64, 64);
		headMain.mirror = true;
		setRotation(headMain, 0F, 0F, 0F);
		body = new ModelRenderer(this, 18, 14);
		body.addBox(-4F, -2F, -3F, 6, 9, 6);
		body.setRotationPoint(0F, 14F, 2F);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 1.570796F, 0F, 0F);
		mane = new ModelRenderer(this, 21, 0);
		mane.addBox(-3F, -3F, -3F, 8, 6, 7);
		mane.setRotationPoint(-2F, 14F, -3F);
		mane.setTextureSize(64, 64);
		mane.mirror = true;
		setRotation(mane, 1.570796F, 0F, 0F);
		leg1 = new ModelRenderer(this, 0, 18);
		leg1.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg1.setRotationPoint(-2.5F, 16F, 7F);
		leg1.setTextureSize(64, 64);
		leg1.mirror = true;
		setRotation(leg1, 0F, 0F, 0F);
		leg2 = new ModelRenderer(this, 0, 18);
		leg2.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg2.setRotationPoint(0.5F, 16F, 7F);
		leg2.setTextureSize(64, 64);
		leg2.mirror = true;
		setRotation(leg2, 0F, 0F, 0F);
		leg3 = new ModelRenderer(this, 0, 18);
		leg3.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg3.setRotationPoint(-2.5F, 16F, -4F);
		leg3.setTextureSize(64, 64);
		leg3.mirror = true;
		setRotation(leg3, 0F, 0F, 0F);
		leg4 = new ModelRenderer(this, 0, 18);
		leg4.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg4.setRotationPoint(0.5F, 16F, -4F);
		leg4.setTextureSize(64, 64);
		leg4.mirror = true;
		setRotation(leg4, 0F, 0F, 0F);
		Nose8 = new ModelRenderer(this, 0, 10);
		Nose8.addBox(-1F, 0F, -8F, 1, 1, 2);
		Nose8.setRotationPoint(-0.5F, 13.5F, -7F);
		Nose8.setTextureSize(64, 64);
		Nose8.mirror = true;
		setRotation(Nose8, 0.2617994F, 0F, 0F);
		Nose9 = new ModelRenderer(this, 0, 10);
		Nose9.addBox(-2F, 0F, -4F, 3, 1, 2);
		Nose9.setRotationPoint(-0.5F, 14.5F, -7F);
		Nose9.setTextureSize(64, 64);
		Nose9.mirror = true;
		setRotation(Nose9, 0.2617994F, 0F, 0F);
		Nose10 = new ModelRenderer(this, 0, 10);
		Nose10.addBox(-1F, 0F, -6F, 1, 1, 2);
		Nose10.setRotationPoint(-0.5F, 14.5F, -7F);
		Nose10.setTextureSize(64, 64);
		Nose10.mirror = true;
		setRotation(Nose10, 0.2617994F, 0F, 0F);
		Nose1 = new ModelRenderer(this, 0, 10);
		Nose1.addBox(-2F, 0F, -4F, 3, 1, 2);
		Nose1.setRotationPoint(-0.5F, 11.5F, -7F);
		Nose1.setTextureSize(64, 64);
		Nose1.mirror = true;
		setRotation(Nose1, 0F, 0F, 0F);
		Nose2 = new ModelRenderer(this, 0, 10);
		Nose2.addBox(-1F, -1F, -6F, 1, 1, 2);
		Nose2.setRotationPoint(-0.5F, 12.5F, -7F);
		Nose2.setTextureSize(64, 64);
		Nose2.mirror = true;
		setRotation(Nose2, 0F, 0F, 0F);
		Nose5 = new ModelRenderer(this, 0, 10);
		Nose5.addBox(-1F, 0F, -8F, 1, 1, 2);
		Nose5.setRotationPoint(-0.5F, 12.5F, -7F);
		Nose5.setTextureSize(64, 64);
		Nose5.mirror = true;
		setRotation(Nose5, 0F, 0F, 0F);
		Tail2 = new ModelRenderer(this, 0, 36);
		Tail2.addBox(-0.5F, -0.5F, 3F, 2, 2, 3);
		Tail2.setRotationPoint(-1.5F, 13F, 9F);
		Tail2.setTextureSize(64, 64);
		Tail2.mirror = true;
		setRotation(Tail2, 0F, 0F, 0F);
		tail = new ModelRenderer(this, 0, 42);
		tail.addBox(0F, 0F, 0F, 1, 1, 3);
		tail.setRotationPoint(-1.5F, 13F, 9F);
		tail.setTextureSize(64, 64);
		tail.mirror = true;
		setRotation(tail, 0F, 0F, 0F);
		Nose3 = new ModelRenderer(this, 40, 44);
		Nose3.addBox(-3F, 0F, -4F, 5, 1, 2);
		Nose3.setRotationPoint(-0.5F, 12.5F, -7F);
		Nose3.setTextureSize(64, 64);
		Nose3.mirror = true;
		setRotation(Nose3, 0F, 0F, 0F);
		Nose6 = new ModelRenderer(this, 40, 40);
		Nose6.addBox(-3F, 0F, -4F, 5, 1, 2);
		Nose6.setRotationPoint(-0.5F, 13.5F, -7F);
		Nose6.setTextureSize(64, 64);
		Nose6.mirror = true;
		setRotation(Nose6, 0.2617994F, 0F, 0F);
		Nose7 = new ModelRenderer(this, 55, 40);
		Nose7.addBox(-2F, 0F, -6F, 3, 1, 2);
		Nose7.setRotationPoint(-0.5F, 13.5F, -7F);
		Nose7.setTextureSize(64, 64);
		Nose7.mirror = true;
		setRotation(Nose7, 0.2617994F, 0F, 0F);
		Nose4 = new ModelRenderer(this, 55, 44);
		Nose4.addBox(-2F, 0F, -6F, 3, 1, 2);
		Nose4.setRotationPoint(-0.5F, 12.5F, -7F);
		Nose4.setTextureSize(64, 64);
		Nose4.mirror = true;
		setRotation(Nose4, 0F, 0F, 0F);
		FinR1 = new ModelRenderer(this, 16, 36);
		FinR1.addBox(0F, -5F, 0F, 1, 5, 1);
		FinR1.setRotationPoint(-3.5F, 10F, -4F);
		FinR1.setTextureSize(64, 64);
		FinR1.mirror = true;
		setRotation(FinR1, -0.535372F, 0F, 0F);
		FinR2 = new ModelRenderer(this, 20, 36);
		FinR2.addBox(0F, -5F, 0F, 1, 5, 1);
		FinR2.setRotationPoint(-3.4F, 5.6F, -1.6F);
		FinR2.setTextureSize(64, 64);
		FinR2.mirror = true;
		setRotation(FinR2, -1.37881F, -0.2373648F, 0F);
		FinR4 = new ModelRenderer(this, 28, 36);
		FinR4.addBox(-1.5F, -2F, 0F, 3, 2, 2);
		FinR4.setRotationPoint(-5F, 5.7F, 7.2F);
		FinR4.setTextureSize(64, 64);
		FinR4.mirror = true;
		setRotation(FinR4, -1.963031F, -0.2676811F, 0F);
		FinR3 = new ModelRenderer(this, 24, 36);
		FinR3.addBox(0F, -5F, 0F, 1, 5, 1);
		FinR3.setRotationPoint(-4.6F, 4.6F, 3.2F);
		FinR3.setTextureSize(64, 64);
		FinR3.mirror = true;
		setRotation(FinR3, -1.963031F, -0.2676811F, 0F);
		FinL1 = new ModelRenderer(this, 16, 36);
		FinL1.addBox(0F, -5F, 0F, 1, 5, 1);
		FinL1.setRotationPoint(0.5F, 10F, -4F);
		FinL1.setTextureSize(64, 64);
		FinL1.mirror = true;
		setRotation(FinL1, -0.535372F, 0F, 0F);
		FinL2 = new ModelRenderer(this, 20, 36);
		FinL2.addBox(0F, -5F, 0F, 1, 5, 1);
		FinL2.setRotationPoint(0.5F, 5.6F, -1.6F);
		FinL2.setTextureSize(64, 64);
		FinL2.mirror = true;
		setRotation(FinL2, -1.397916F, 0.2379431F, 0F);
		FinL3 = new ModelRenderer(this, 24, 36);
		FinL3.addBox(0F, -5F, 0F, 1, 5, 1);
		FinL3.setRotationPoint(1.7F, 4.6F, 3.2F);
		FinL3.setTextureSize(64, 64);
		FinL3.mirror = true;
		setRotation(FinL3, -1.963031F, 0.267686F, 0F);
		FinL4 = new ModelRenderer(this, 28, 36);
		FinL4.addBox(-1.5F, -2F, 0F, 3, 2, 2);
		FinL4.setRotationPoint(3.2F, 5.7F, 7.2F);
		FinL4.setTextureSize(64, 64);
		FinL4.mirror = true;
		setRotation(FinL4, -1.963031F, 0.267686F, 0F);
		Spike10 = new ModelRenderer(this, 0, 32);
		Spike10.addBox(0F, -2F, 0F, 1, 2, 1);
		Spike10.setRotationPoint(-1.5F, 9.4F, 7.5F);
		Spike10.setTextureSize(64, 64);
		Spike10.mirror = true;
		setRotation(Spike10, -1.396263F, 0F, 0F);
		Spike9 = new ModelRenderer(this, 0, 32);
		Spike9.addBox(0F, 0F, 0F, 1, 2, 1);
		Spike9.setRotationPoint(-1.5F, 9.4F, 7.5F);
		Spike9.setTextureSize(64, 64);
		Spike9.mirror = true;
		setRotation(Spike9, -0.5235988F, 0F, 0F);
		Spike1 = new ModelRenderer(this, 0, 32);
		Spike1.addBox(0F, 0F, 0F, 1, 2, 1);
		Spike1.setRotationPoint(-1.5F, 8.5F, -8.5F);
		Spike1.setTextureSize(64, 64);
		Spike1.mirror = true;
		setRotation(Spike1, -0.1745329F, 0F, 0F);
		Spike3 = new ModelRenderer(this, 0, 32);
		Spike3.addBox(0F, 0F, 0F, 1, 2, 1);
		Spike3.setRotationPoint(-1.5F, 8F, -4.5F);
		Spike3.setTextureSize(64, 64);
		Spike3.mirror = true;
		setRotation(Spike3, -0.1745329F, 0F, 0F);
		Spike5 = new ModelRenderer(this, 0, 32);
		Spike5.addBox(0F, 0F, 0F, 1, 2, 1);
		Spike5.setRotationPoint(-1.5F, 8.2F, -0.5F);
		Spike5.setTextureSize(64, 64);
		Spike5.mirror = true;
		setRotation(Spike5, -0.3490659F, 0F, 0F);
		Spike7 = new ModelRenderer(this, 0, 32);
		Spike7.addBox(0F, 0F, 0F, 1, 2, 1);
		Spike7.setRotationPoint(-1.5F, 9.2F, 3.5F);
		Spike7.setTextureSize(64, 64);
		Spike7.mirror = true;
		setRotation(Spike7, -0.3490659F, 0F, 0F);
		Spike2 = new ModelRenderer(this, 0, 32);
		Spike2.addBox(0F, -2F, 0F, 1, 2, 1);
		Spike2.setRotationPoint(-1.5F, 8.5F, -8.5F);
		Spike2.setTextureSize(64, 64);
		Spike2.mirror = true;
		setRotation(Spike2, -1.047198F, 0F, 0F);
		Spike4 = new ModelRenderer(this, 0, 32);
		Spike4.addBox(0F, -2F, 0F, 1, 2, 1);
		Spike4.setRotationPoint(-1.5F, 8F, -4.5F);
		Spike4.setTextureSize(64, 64);
		Spike4.mirror = true;
		setRotation(Spike4, -1.047198F, 0F, 0F);
		Spike6 = new ModelRenderer(this, 0, 32);
		Spike6.addBox(0F, -2F, 0F, 1, 2, 1);
		Spike6.setRotationPoint(-1.5F, 8.2F, -0.5F);
		Spike6.setTextureSize(64, 64);
		Spike6.mirror = true;
		setRotation(Spike6, -1.22173F, 0F, 0F);
		Spike8 = new ModelRenderer(this, 0, 32);
		Spike8.addBox(0F, -2F, 0F, 1, 2, 1);
		Spike8.setRotationPoint(-1.5F, 9.2F, 3.5F);
		Spike8.setTextureSize(64, 64);
		Spike8.mirror = true;
		setRotation(Spike8, -1.22173F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glTranslatef(0, 1.5f, 0);
		long boundedTime = Minecraft.getMinecraft().theWorld.provider.getWorldTime() % 24000;
		float transparencyFromTime = boundedTime > 12500 && boundedTime < 23500
				? (float)Math.abs(Math.cos(Math.toRadians(((boundedTime - 12500f) / 11000f) * 180f))) - 0.2f
				: 1.0f;


		if (entity.hurtResistantTime > 0 || entity.isDead){
			transparencyFromTime = 1.0f;
		}

		if (transparencyFromTime < 0.3f){
			transparencyFromTime = 0.3f;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (transparencyFromTime >= 1.0f || Minecraft.getMinecraft().thePlayer.isPotionActive(BuffList.trueSight.id)){
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}else{
			GL11.glColor4f(1.0f, 0.6f, 0.6f, transparencyFromTime);
		}


		headMain.render(f5);
		body.render(f5);
		mane.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		Nose8.render(f5);
		Nose9.render(f5);
		Nose10.render(f5);
		Nose1.render(f5);
		Nose2.render(f5);
		Nose5.render(f5);
		Tail2.render(f5);
		tail.render(f5);
		Nose3.render(f5);
		Nose6.render(f5);
		Nose7.render(f5);
		Nose4.render(f5);
		FinR1.render(f5);
		FinR2.render(f5);
		FinR4.render(f5);
		FinR3.render(f5);
		FinL1.render(f5);
		FinL2.render(f5);
		FinL3.render(f5);
		FinL4.render(f5);
		Spike10.render(f5);
		Spike9.render(f5);
		//Spike1.render(f5);
		Spike3.render(f5);
		Spike5.render(f5);
		Spike7.render(f5);
		//Spike2.render(f5);
		Spike4.render(f5);
		Spike6.render(f5);
		Spike8.render(f5);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4){
		EntityDarkling darkling = (EntityDarkling)par1EntityLivingBase;

		this.tail.rotateAngleY = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;
		this.body.rotateAngleX = ((float)Math.PI / 2F);
		this.mane.rotateAngleX = this.body.rotateAngleX;
		this.leg1.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;
		this.leg2.rotateAngleX = MathHelper.cos(par2 * 0.6662F + (float)Math.PI) * 1.4F * par3;
		this.leg3.rotateAngleX = MathHelper.cos(par2 * 0.6662F + (float)Math.PI) * 1.4F * par3;
		this.leg4.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;

		this.Tail2.rotateAngleY = this.tail.rotateAngleY;
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity){
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
		this.headMain.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.headMain.rotateAngleY = par4 / (180F / (float)Math.PI);

		this.Nose1.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose1.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose2.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose2.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose3.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose3.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose4.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose4.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose5.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose5.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose6.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose6.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose7.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose7.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose8.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose8.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose9.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose9.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.Nose10.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.Nose10.rotateAngleY = par4 / (180F / (float)Math.PI);

		if (((EntityDarkling)par7Entity).isAngry()){
			this.Nose5.rotateAngleX += 15f;
			this.Nose6.rotateAngleX += 15f;
			this.Nose7.rotateAngleX += 15f;
			this.Nose8.rotateAngleX += 15f;
			this.Nose9.rotateAngleX += 15f;
			this.Nose10.rotateAngleX += 15f;
		}
		// this.tail.rotateAngleX = par3;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
