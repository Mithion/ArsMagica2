package am2.bosses.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.AnimationAPI.client.Animator;
//
//import thehippomaster.AnimationAPI.client.Animator;
import am2.bosses.BossActions;
import am2.entities.renderers.AM2ModelRenderer;

public class ModelLightningGuardian extends ModelBase
{
	//fields
	AM2ModelRenderer ChestLower;
	AM2ModelRenderer leftarmwristband3;
	AM2ModelRenderer leftarmlower;
	AM2ModelRenderer leftarmwristband;
	AM2ModelRenderer leftarmwristband6;
	AM2ModelRenderer leftarmwristband2;
	AM2ModelRenderer leftarmwristband4;
	AM2ModelRenderer leftarmwristband5;
	AM2ModelRenderer leftarmwristband1;
	AM2ModelRenderer leftarmupper;
	AM2ModelRenderer leftshoulder;
	AM2ModelRenderer ChestArmor3;
	AM2ModelRenderer ChestMiddle;
	AM2ModelRenderer rightshoulder;
	AM2ModelRenderer rightarmupper;
	AM2ModelRenderer rightarmlower;
	AM2ModelRenderer rightarmwristband5;
	AM2ModelRenderer rightarmwristband;
	AM2ModelRenderer rightarmwristband2;
	AM2ModelRenderer rightarmwristband6;
	AM2ModelRenderer rightarmwristband1;
	AM2ModelRenderer rightarmwristband3;
	AM2ModelRenderer rightarmwristband4;
	AM2ModelRenderer Head;
	AM2ModelRenderer ChestArmor9;
	AM2ModelRenderer ChestArmor6;
	AM2ModelRenderer ChestArmor8;
	AM2ModelRenderer ChestArmor2;
	AM2ModelRenderer ChestArmor4;
	AM2ModelRenderer ChestArmor7;
	AM2ModelRenderer ChestArmor5;
	AM2ModelRenderer ChestArmor1;
	AM2ModelRenderer ChestUpper;

	ResourceLocation armor;
	ResourceLocation lightning;

	private Animator animator;

	public ModelLightningGuardian(ResourceLocation armor, ResourceLocation lightning)
	{
		textureWidth = 256;
		textureHeight = 256;

		this.armor = armor;
		this.lightning = lightning;

		initializeParts();
		addBoxes();
		setRotationPoints();
		setMirrorStates();
		setInitialRotations();
		setupHierarchy();
		
		animator = new Animator(this);
	}

	private void initializeParts(){
		ChestLower = new AM2ModelRenderer(this, 49, 32);
		leftarmwristband3 = new AM2ModelRenderer(this, 80, 12);
		leftarmlower = new AM2ModelRenderer(this, 98, 32);
		leftarmwristband = new AM2ModelRenderer(this, 0, 0);
		leftarmwristband6 = new AM2ModelRenderer(this, 36, 16);
		leftarmwristband2 = new AM2ModelRenderer(this, 32, 12);
		leftarmwristband4 = new AM2ModelRenderer(this, 92, 12);
		leftarmwristband5 = new AM2ModelRenderer(this, 44, 12);
		leftarmwristband1 = new AM2ModelRenderer(this, 44, 16);
		leftarmupper = new AM2ModelRenderer(this, 68, 32);
		leftshoulder = new AM2ModelRenderer(this, 56, 37);
		ChestArmor3 = new AM2ModelRenderer(this, 0, 9);
		ChestMiddle = new AM2ModelRenderer(this, 36, 32);
		rightshoulder = new AM2ModelRenderer(this, 56, 32);
		rightarmupper = new AM2ModelRenderer(this, 78, 32);
		rightarmlower = new AM2ModelRenderer(this, 88, 32);
		rightarmwristband5 = new AM2ModelRenderer(this, 20, 12);
		rightarmwristband = new AM2ModelRenderer(this, 7, 0);
		rightarmwristband2 = new AM2ModelRenderer(this, 56, 12);
		rightarmwristband6 = new AM2ModelRenderer(this, 28, 16);
		rightarmwristband1 = new AM2ModelRenderer(this, 20, 16);
		rightarmwristband3 = new AM2ModelRenderer(this, 68, 12);
		rightarmwristband4 = new AM2ModelRenderer(this, 104, 12);
		Head = new AM2ModelRenderer(this, 21, 32);
		ChestArmor9 = new AM2ModelRenderer(this, 15, 0);
		ChestArmor6 = new AM2ModelRenderer(this, 74, 0);
		ChestArmor8 = new AM2ModelRenderer(this, 15, 3);
		ChestArmor2 = new AM2ModelRenderer(this, 0, 9);
		ChestArmor4 = new AM2ModelRenderer(this, 45, 0);
		ChestArmor7 = new AM2ModelRenderer(this, 67, 0);
		ChestArmor5 = new AM2ModelRenderer(this, 56, 0);
		ChestArmor1 = new AM2ModelRenderer(this, 30, 0);
		ChestUpper = new AM2ModelRenderer(this, 0, 32);
	}

	private void addBoxes(){
		ChestLower.addBox(-1F, 0F, -1.5F, 2, 6, 1);
		leftarmlower.addBox(-1F, 0F, -1F, 2, 6, 2);
		leftarmupper.addBox(-1F, 0F, -1F, 2, 5, 2);
		leftshoulder.addBox(-1F, 0F, -1F, 3, 2, 2);
		ChestMiddle.addBox(-2F, 0F, -2F, 4, 4, 2);
		rightshoulder.addBox(-1F, 0F, -1F, 3, 2, 2);
		rightarmupper.addBox(-1F, 0F, -1F, 2, 5, 2);
		rightarmlower.addBox(-1F, 0F, -1F, 2, 6, 2);
		ChestUpper.addBox(-3F, 0F, -2F, 6, 4, 4);
		Head.addBox(-1.5F, 0F, -2F, 3, 3, 4);

		leftarmwristband3.addBox(-1F, 0F, -1F, 4, 1, 1);
		leftarmwristband.addBox(-1F, 0F, -1F, 1, 4, 2);
		leftarmwristband6.addBox(-1F, 0F, -2F, 1, 1, 2);
		leftarmwristband2.addBox(-1F, 0F, -1F, 4, 1, 1);
		leftarmwristband4.addBox(-1F, 0F, -1F, 4, 1, 1);
		leftarmwristband5.addBox(-1F, 0F, -1F, 4, 1, 1);
		leftarmwristband1.addBox(-1F, 0F, -2F, 1, 1, 2);
		ChestArmor3.addBox(4F, 0F, -2F, 0, 3, 5);
		rightarmwristband5.addBox(-1F, 0F, -1F, 4, 1, 1);
		rightarmwristband.addBox(-1F, 0F, -1F, 1, 4, 2);
		rightarmwristband2.addBox(-1F, 0F, -1F, 4, 1, 1);
		rightarmwristband6.addBox(-1F, 0F, -1F, 1, 1, 2);
		rightarmwristband1.addBox(-1F, 0F, -1F, 1, 1, 2);
		rightarmwristband3.addBox(-1F, 0F, -1F, 4, 1, 1);
		rightarmwristband4.addBox(-1F, 0F, -1F, 4, 1, 1);
		ChestArmor9.addBox(-2.5F, 0F, 2F, 5, 2, 0);
		ChestArmor6.addBox(4F, 0F, 0F, 0, 1, 3);
		ChestArmor8.addBox(-3.5F, 0F, 3F, 7, 7, 0);
		ChestArmor2.addBox(-3F, 0F, -2F, 0, 3, 5);
		ChestArmor4.addBox(4F, 0F, -2F, 0, 1, 5);
		ChestArmor7.addBox(-3F, 0F, 0F, 0, 1, 3);
		ChestArmor5.addBox(-3F, 0F, -2F, 0, 1, 5);
		ChestArmor1.addBox(-3.5F, 0F, -2F, 7, 5, 0);
	}

	private void setRotationPoints(){
		ChestMiddle.setRotationPoint(0F, 11F, 1F);
		ChestUpper.setRotationPoint(0F, -4F, -1F);
		Head.setRotationPoint(0F, -3F, 0F);
		ChestLower.setRotationPoint(0F, 4F, 1F);

		ChestArmor1.setRotationPoint(0F, 0F, -0.5F);
		ChestArmor2.setRotationPoint(-0.5F, 2F, -0.5F);
		ChestArmor3.setRotationPoint(-0.5F, 2F, -0.5F);
		ChestArmor4.setRotationPoint(-0.5F, -1F, -0.5F);
		ChestArmor5.setRotationPoint(-0.5F, -1F, -0.5F);
		ChestArmor6.setRotationPoint(-0.5F, -2F, -0.5F);
		ChestArmor7.setRotationPoint(-0.5F, -2F, -0.5F);
		ChestArmor8.setRotationPoint(0F, -2F, -0.5F);
		ChestArmor9.setRotationPoint(0F, -4F, 0.5F);

		leftshoulder.setRotationPoint(4F, 0F, 0F);

		leftarmupper.setRotationPoint(1F, 2F, 0F);
		leftarmlower.setRotationPoint(0F, 5F, 0F);

		leftarmwristband.setRotationPoint(2F, 1F, 0F);
		leftarmwristband1.setRotationPoint(-1F, 1F, 1F);
		leftarmwristband2.setRotationPoint(-1F, 4F, -1F);
		leftarmwristband3.setRotationPoint(-1F, 1F, 2F);
		leftarmwristband4.setRotationPoint(-1F, 4F, 2F);
		leftarmwristband5.setRotationPoint(-1F, 1F, -1F);
		leftarmwristband6.setRotationPoint(-1F, 4F, 1F);

		rightshoulder.setRotationPoint(-5F, 0F, 0F);

		rightarmupper.setRotationPoint(0F, 2F, 0F);
		rightarmlower.setRotationPoint(0F, 5F, 0F);

		rightarmwristband.setRotationPoint(-1F, 1F, 0F);
		rightarmwristband1.setRotationPoint(2F, 1F, 0F);
		rightarmwristband2.setRotationPoint(-1F, 4F, -1F);
		rightarmwristband3.setRotationPoint(-1F, 1F, 2F);
		rightarmwristband4.setRotationPoint(-1F, 4F, 2F);
		rightarmwristband5.setRotationPoint(-1F, 1F, -1F);
		rightarmwristband6.setRotationPoint(2F, 4F, 0F);

	}

	private void setTextureSizes(){
		ChestLower.setTextureSize(256, 256);
		leftarmwristband3.setTextureSize(256, 256);
		leftarmlower.setTextureSize(256, 256);
		leftarmwristband.setTextureSize(256, 256);
		leftarmwristband6.setTextureSize(256, 256);
		leftarmwristband2.setTextureSize(256, 256);
		leftarmwristband4.setTextureSize(256, 256);
		leftarmwristband5.setTextureSize(256, 256);
		leftarmwristband1.setTextureSize(256, 256);
		leftarmupper.setTextureSize(256, 256);
		leftshoulder.setTextureSize(256, 256);
		ChestArmor3.setTextureSize(256, 256);
		ChestMiddle.setTextureSize(256, 256);
		rightshoulder.setTextureSize(256, 256);
		rightarmupper.setTextureSize(256, 256);
		rightarmlower.setTextureSize(256, 256);
		rightarmwristband5.setTextureSize(256, 256);
		rightarmwristband.setTextureSize(256, 256);
		rightarmwristband2.setTextureSize(256, 256);
		rightarmwristband6.setTextureSize(256, 256);
		rightarmwristband1.setTextureSize(256, 256);
		rightarmwristband3.setTextureSize(256, 256);
		rightarmwristband4.setTextureSize(256, 256);
		Head.setTextureSize(256, 256);
		ChestArmor9.setTextureSize(256, 256);
		ChestArmor6.setTextureSize(256, 256);
		ChestArmor8.setTextureSize(256, 256);
		ChestArmor2.setTextureSize(256, 256);
		ChestArmor4.setTextureSize(256, 256);
		ChestArmor7.setTextureSize(256, 256);
		ChestArmor5.setTextureSize(256, 256);
		ChestArmor1.setTextureSize(256, 256);
		ChestUpper.setTextureSize(256, 256);
	}

	private void setMirrorStates(){
		ChestLower.mirror = true;
		leftarmwristband3.mirror = true;
		leftarmlower.mirror = true;
		leftarmwristband.mirror = true;
		leftarmwristband6.mirror = true;
		leftarmwristband2.mirror = true;
		leftarmwristband4.mirror = true;
		leftarmwristband5.mirror = true;
		leftarmwristband1.mirror = true;
		leftarmupper.mirror = true;
		leftshoulder.mirror = true;
		ChestArmor3.mirror = true;
		ChestMiddle.mirror = true;
		rightshoulder.mirror = true;
		rightarmupper.mirror = true;
		rightarmlower.mirror = true;
		rightarmwristband5.mirror = true;
		rightarmwristband.mirror = true;
		rightarmwristband2.mirror = true;
		rightarmwristband6.mirror = true;
		rightarmwristband1.mirror = true;
		rightarmwristband3.mirror = true;
		rightarmwristband4.mirror = true;
		Head.mirror = true;
		ChestArmor9.mirror = true;
		ChestArmor6.mirror = true;
		ChestArmor8.mirror = true;
		ChestArmor2.mirror = true;
		ChestArmor4.mirror = true;
		ChestArmor7.mirror = true;
		ChestArmor5.mirror = true;
		ChestArmor1.mirror = true;
		ChestUpper.mirror = true;
	}

	private void setInitialRotations(){
		setRotation(ChestLower, 0F, 0F, 0F);
		setRotation(leftarmwristband3, 0F, 0F, 0F);
		setRotation(leftarmlower, 0F, 0F, 0F);
		setRotation(leftarmwristband, 0F, 0F, 0F);
		setRotation(leftarmwristband6, 0F, 0F, 0F);
		setRotation(leftarmwristband2, 0F, 0F, 0F);
		setRotation(leftarmwristband4, 0F, 0F, 0F);
		setRotation(leftarmwristband5, 0F, 0F, 0F);
		setRotation(leftarmwristband1, 0F, 0F, 0F);
		setRotation(leftarmupper, 0F, 0F, 0F);
		setRotation(leftshoulder, 0F, 0F, 0F);
		setRotation(ChestArmor3, 0F, 0F, 0F);
		setRotation(ChestMiddle, 0F, 0F, 0F);
		setRotation(rightshoulder, 0F, 0F, 0F);
		setRotation(rightarmupper, 0F, 0F, 0F);
		setRotation(rightarmlower, 0F, 0F, 0F);
		setRotation(rightarmwristband5, 0F, 0F, 0F);
		setRotation(rightarmwristband, 0F, 0F, 0F);
		setRotation(rightarmwristband2, 0F, 0F, 0F);
		setRotation(rightarmwristband6, 0F, 0F, 0F);
		setRotation(rightarmwristband1, 0F, 0F, 0F);
		setRotation(rightarmwristband3, 0F, 0F, 0F);
		setRotation(rightarmwristband4, 0F, 0F, 0F);
		setRotation(Head, 0F, 0F, 0F);
		setRotation(ChestArmor9, 0F, 0F, 0F);
		setRotation(ChestArmor6, 0F, 0F, 0F);
		setRotation(ChestArmor8, 0F, 0F, 0F);
		setRotation(ChestArmor2, 0F, 0F, 0F);
		setRotation(ChestArmor4, 0F, 0F, 0F);
		setRotation(ChestArmor7, 0F, 0F, 0F);
		setRotation(ChestArmor5, 0F, 0F, 0F);
		setRotation(ChestArmor1, 0F, 0F, 0F);
		setRotation(ChestUpper, 0F, 0F, 0F);

		ChestLower.storeRestRotations();
		leftarmwristband3.storeRestRotations();
		leftarmlower.storeRestRotations();
		leftarmwristband.storeRestRotations();
		leftarmwristband6.storeRestRotations();
		leftarmwristband2.storeRestRotations();
		leftarmwristband4.storeRestRotations();
		leftarmwristband5.storeRestRotations();
		leftarmwristband1.storeRestRotations();
		leftarmupper.storeRestRotations();
		leftshoulder.storeRestRotations();
		ChestArmor3.storeRestRotations();
		ChestMiddle.storeRestRotations();
		rightshoulder.storeRestRotations();
		rightarmupper.storeRestRotations();
		rightarmlower.storeRestRotations();
		rightarmwristband5.storeRestRotations();
		rightarmwristband.storeRestRotations();
		rightarmwristband2.storeRestRotations();
		rightarmwristband6.storeRestRotations();
		rightarmwristband1.storeRestRotations();
		rightarmwristband3.storeRestRotations();
		rightarmwristband4.storeRestRotations();
		Head.storeRestRotations();
		ChestArmor9.storeRestRotations();
		ChestArmor6.storeRestRotations();
		ChestArmor8.storeRestRotations();
		ChestArmor2.storeRestRotations();
		ChestArmor4.storeRestRotations();
		ChestArmor7.storeRestRotations();
		ChestArmor5.storeRestRotations();
		ChestArmor1.storeRestRotations();
		ChestUpper.storeRestRotations();
	}

	private void setupHierarchy(){
		ChestMiddle.addChild(ChestLower);
		ChestMiddle.addChild(ChestUpper);

		ChestUpper.addChild(Head);
		ChestUpper.addChild(ChestArmor1);
		ChestUpper.addChild(ChestArmor2);
		ChestUpper.addChild(ChestArmor3);
		ChestUpper.addChild(ChestArmor4);
		ChestUpper.addChild(ChestArmor5);
		ChestUpper.addChild(ChestArmor6);
		ChestUpper.addChild(ChestArmor7);
		ChestUpper.addChild(ChestArmor8);
		ChestUpper.addChild(ChestArmor9);
		ChestUpper.addChild(leftshoulder);
		ChestUpper.addChild(rightshoulder);

		leftshoulder.addChild(leftarmupper);
		leftarmupper.addChild(leftarmlower);
		leftarmlower.addChild(leftarmwristband);
		leftarmlower.addChild(leftarmwristband1);
		leftarmlower.addChild(leftarmwristband2);
		leftarmlower.addChild(leftarmwristband3);
		leftarmlower.addChild(leftarmwristband4);
		leftarmlower.addChild(leftarmwristband5);
		leftarmlower.addChild(leftarmwristband6);

		rightshoulder.addChild(rightarmupper);
		rightarmupper.addChild(rightarmlower);
		rightarmlower.addChild(rightarmwristband);
		rightarmlower.addChild(rightarmwristband1);
		rightarmlower.addChild(rightarmwristband2);
		rightarmlower.addChild(rightarmwristband3);
		rightarmlower.addChild(rightarmwristband4);
		rightarmlower.addChild(rightarmwristband5);
		rightarmlower.addChild(rightarmwristband6);
	}

	private void setupModelForRenderPass(int pass){
		if (pass == 0){ //lightning pass

			ChestArmor1.showModel = false;
			ChestArmor2.showModel = false;
			ChestArmor3.showModel = false;
			ChestArmor4.showModel = false;
			ChestArmor5.showModel = false;
			ChestArmor6.showModel = false;
			ChestArmor7.showModel = false;
			ChestArmor8.showModel = false;
			ChestArmor9.showModel = false;

			leftarmwristband.showModel = false;
			leftarmwristband1.showModel = false;
			leftarmwristband2.showModel = false;
			leftarmwristband3.showModel = false;
			leftarmwristband4.showModel = false;
			leftarmwristband5.showModel = false;
			leftarmwristband6.showModel = false;

			rightarmwristband.showModel = false;
			rightarmwristband1.showModel = false;
			rightarmwristband2.showModel = false;
			rightarmwristband3.showModel = false;
			rightarmwristband4.showModel = false;
			rightarmwristband5.showModel = false;
			rightarmwristband6.showModel = false;
		}else{ //armor Pass

			ChestArmor1.showModel = true;
			ChestArmor2.showModel = true;
			ChestArmor3.showModel = true;
			ChestArmor4.showModel = true;
			ChestArmor5.showModel = true;
			ChestArmor6.showModel = true;
			ChestArmor7.showModel = true;
			ChestArmor8.showModel = true;
			ChestArmor9.showModel = true;

			leftarmwristband.showModel = true;
			leftarmwristband1.showModel = true;
			leftarmwristband2.showModel = true;
			leftarmwristband3.showModel = true;
			leftarmwristband4.showModel = true;
			leftarmwristband5.showModel = true;
			leftarmwristband6.showModel = true;

			rightarmwristband.showModel = true;
			rightarmwristband1.showModel = true;
			rightarmwristband2.showModel = true;
			rightarmwristband3.showModel = true;
			rightarmwristband4.showModel = true;
			rightarmwristband5.showModel = true;
			rightarmwristband6.showModel = true;
		}
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);

		GL11.glPushMatrix();
		GL11.glTranslatef(0, -2, 0);
		GL11.glScalef(2, 2, 2);
		GL11.glEnable(GL11.GL_BLEND);

		//render lightning layer
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float xOff = entity.ticksExisted * 0.008F;
		float yOff = entity.ticksExisted * 0.005F;
		GL11.glTranslatef(xOff, yOff, 0.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		setupModelForRenderPass(0);
		ChestMiddle.render(f5);

		//render armor layer
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		Minecraft.getMinecraft().renderEngine.bindTexture(armor);
		setupModelForRenderPass(1);
		ChestMiddle.render(f5);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		animator.update(entity);
		setToRestRotations();

		//animations
		//braces are used purely for organization

		//idle
		animator.setAnim(BossActions.IDLE.ordinal());
		{
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(ChestUpper, Angles.RADS_5, 0, 0);
			animator.endPhase();
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, Angles.RADS_5);
			animator.endPhase();
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(ChestUpper, Angles.RADS_5, 0, 0);
			animator.endPhase();
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, Angles.RADS_5);
			animator.endPhase();
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(ChestUpper, Angles.RADS_5, 0, 0);
			animator.endPhase();
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, Angles.RADS_5);
			animator.endPhase();
			animator.startPhase(25);
			animator.rotate(leftshoulder, 0, 0, Angles.RADS_5);
			animator.rotate(rightshoulder, 0, 0, -Angles.RADS_5);
			animator.rotate(ChestUpper, Angles.RADS_5, 0, 0);
			animator.endPhase();
			animator.resetPhase(25);
		}
		//basic attack
		animator.setAnim(BossActions.STRIKE.ordinal());
		{
			animator.startPhase(4); //wind up
			animator.rotate(ChestUpper, 0, -Angles.RADS_15, 0);
			animator.rotate(leftshoulder, Angles.RADS_45, Angles.RADS_15, 0);
			animator.rotate(leftarmlower, -Angles.RADS_90, 0, 0);

			animator.rotate(rightshoulder, -Angles.RADS_15, 0, 0);
			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.startPhase(3); //boom
			animator.rotate(ChestUpper, 0, Angles.RADS_15, 0);
			animator.rotate(leftshoulder, -Angles.RADS_75, 0, 0);

			animator.rotate(rightshoulder, Angles.RADS_15, 0, 0);
			animator.rotate(rightarmlower, -Angles.RADS_30, 0, 0);
			animator.endPhase();
			animator.setStationaryPhase(4);
			animator.resetPhase(4);
		}
		//dive into lightning
		animator.setAnim(BossActions.SPINNING.ordinal());
		{
			animator.startPhase(5); //setup
			animator.rotate(leftshoulder, Angles.RADS_45, Angles.RADS_15, 0);
			animator.rotate(leftarmlower, -Angles.RADS_90, 0, 0);

			animator.rotate(rightshoulder, Angles.RADS_45, Angles.RADS_15, 0);
			animator.rotate(rightarmlower, -Angles.RADS_90, 0, 0);

			animator.rotate(ChestUpper, Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.startPhase(10); //arms up, into spin
			animator.rotate(rightshoulder, -Angles.RADS_145, 0, 0);
			animator.rotate(leftshoulder, -Angles.RADS_145, 0, 0);

			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.setStationaryPhase(30); //hold for the spin
		}
		//continue lightning
		animator.setAnim(BossActions.SHIELD_BASH.ordinal());
		{
			animator.startPhase(0);
			animator.rotate(rightshoulder, -Angles.RADS_145, 0, 0);
			animator.rotate(leftshoulder, -Angles.RADS_145, 0, 0);

			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.setStationaryPhase(600); //max 30 seconds
		}
		//exit lightning
		animator.setAnim(BossActions.LAUNCHING.ordinal());
		{
			animator.startPhase(0);//set initial anim
			animator.rotate(rightshoulder, -Angles.RADS_145, 0, 0);
			animator.rotate(leftshoulder, -Angles.RADS_145, 0, 0);

			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.resetPhase(10);
		}
		//static charge
		animator.setAnim(BossActions.CHARGE.ordinal());
		{
			animator.startPhase(40); //charge up
			animator.rotate(rightshoulder, -Angles.RADS_30, Angles.RADS_45, 0);
			animator.rotate(leftshoulder, -Angles.RADS_30, -Angles.RADS_45, 0);

			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.setStationaryPhase(15);
			animator.startPhase(6); //cross arms

			animator.rotate(rightshoulder, -Angles.RADS_45, -Angles.RADS_30, 0);
			animator.rotate(leftshoulder, -Angles.RADS_45, Angles.RADS_30, 0);

			animator.rotate(rightarmlower, -Angles.RADS_90, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_75, 0, 0);

			animator.rotate(ChestUpper, Angles.RADS_15, 0, 0);

			animator.endPhase();
			animator.setStationaryPhase(5);
			animator.startPhase(6); //release

			animator.rotate(rightarmupper, 0, 0, Angles.RADS_75);
			animator.rotate(leftarmupper, 0, 0, -Angles.RADS_75);

			animator.rotate(rightarmlower, -Angles.RADS_30, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_30, 0, 0);

			animator.rotate(ChestUpper, -Angles.RADS_30, 0, 0);

			animator.endPhase();
			animator.setStationaryPhase(20);
			animator.resetPhase(15);
		}
		//lightning rune
		animator.setAnim(BossActions.CASTING.ordinal());
		{
			animator.startPhase(10); //wind up
			animator.rotate(ChestUpper, 0, -Angles.RADS_15, 0);
			animator.rotate(rightshoulder, Angles.RADS_45, Angles.RADS_15, 0);
			animator.rotate(rightarmlower, -Angles.RADS_90, 0, 0);

			animator.rotate(leftshoulder, -Angles.RADS_15, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.startPhase(3); //cast_1 (the 4 casts cause a circle motion with the arm)
			animator.rotate(ChestUpper, 0, 0, 0);
			animator.rotate(rightshoulder, -Angles.RADS_45, Angles.RADS_15, Angles.RADS_15);
			animator.rotate(rightarmlower, -Angles.RADS_45, 0, 0);

			animator.rotate(leftshoulder, 0, 0, 0);
			animator.rotate(leftarmlower, 0, 0, 0);
			animator.endPhase();
			animator.startPhase(3); //cast_2
			animator.rotate(ChestUpper, 0, 0, 0);
			animator.rotate(rightshoulder, -Angles.RADS_75, 0, Angles.RADS_15);
			animator.rotate(rightarmlower, -Angles.RADS_45, 0, 0);

			animator.rotate(leftshoulder, Angles.RADS_15, Angles.RADS_15, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, Angles.RADS_15, 0);
			animator.endPhase();
			animator.startPhase(3); //cast_3
			animator.rotate(ChestUpper, 0, Angles.RADS_15, 0);
			animator.rotate(rightshoulder, -Angles.RADS_45, -Angles.RADS_30, Angles.RADS_15);
			animator.rotate(rightarmlower, -Angles.RADS_45, 0, 0);

			animator.rotate(leftshoulder, 0, 0, 0);
			animator.rotate(leftarmlower, 0, 0, 0);
			animator.endPhase();
			animator.startPhase(3); //cast_4

			animator.rotate(rightshoulder, -Angles.RADS_30, 0, Angles.RADS_15);
			animator.rotate(rightarmlower, -Angles.RADS_45, 0, 0);

			animator.rotate(leftshoulder, 0, 0, 0);
			animator.rotate(leftarmlower, 0, 0, 0);

			animator.endPhase();
			animator.resetPhase(5);
		}
		//lightning rod
		animator.setAnim(BossActions.LONG_CASTING.ordinal());
		{
			animator.startPhase(20); //prepare
			animator.rotate(ChestUpper, 0, -Angles.RADS_15, 0);
			animator.rotate(leftshoulder, Angles.RADS_45, Angles.RADS_15, 0);
			animator.rotate(leftarmlower, -Angles.RADS_90, 0, 0);

			animator.rotate(rightshoulder, -Angles.RADS_15, 0, 0);
			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);
			animator.endPhase();
			animator.startPhase(5); //extend arm
			animator.rotate(ChestUpper, 0, Angles.RADS_15, 0);
			animator.rotate(leftshoulder, -Angles.RADS_75, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);

			animator.rotate(rightshoulder, Angles.RADS_15, 0, 0);
			animator.rotate(rightarmlower, -Angles.RADS_30, 0, 0);
			animator.endPhase();
			animator.startPhase(60); //slow arm raise
			animator.rotate(leftshoulder, -Angles.RADS_90, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_30, 0, 0);

			animator.rotate(rightshoulder, Angles.RADS_15, 0, 0);
			animator.rotate(rightarmlower, -Angles.RADS_30, 0, 0);
			animator.endPhase();
			animator.startPhase(20); //right arm draw back
			animator.rotate(leftshoulder, -Angles.RADS_90, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_30, 0, 0);

			animator.rotate(rightshoulder, Angles.RADS_45, Angles.RADS_15, 0);
			animator.rotate(rightarmlower, -Angles.RADS_90, 0, 0);
			animator.endPhase();
			animator.startPhase(5); //ZAP!

			animator.rotate(leftshoulder, -Angles.RADS_90, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_30, 0, 0);

			animator.rotate(rightshoulder, -Angles.RADS_90, 0, Angles.RADS_15);
			animator.rotate(rightarmlower, -Angles.RADS_30, 0, 0);

			animator.endPhase();
			animator.setStationaryPhase(40);
			animator.startPhase(5); //slam down lead-in

			animator.rotate(ChestUpper, Angles.RADS_15, 0, 0);
			animator.rotate(Head, Angles.RADS_15, 0, 0);

			animator.rotate(leftshoulder, -Angles.RADS_90, 0, 0);
			animator.rotate(leftarmlower, -Angles.RADS_75, 0, 0);

			animator.rotate(rightshoulder, -Angles.RADS_90, 0, 0);
			animator.rotate(rightarmlower, -Angles.RADS_75, 0, 0);

			animator.endPhase();
			animator.startPhase(3); //slam down

			animator.rotate(ChestUpper, Angles.RADS_30, 0, 0);
			animator.rotate(Head, Angles.RADS_15, 0, 0);

			animator.rotate(leftshoulder, -Angles.RADS_30, 0, -Angles.RADS_15);
			animator.rotate(leftarmlower, -Angles.RADS_15, 0, 0);

			animator.rotate(rightshoulder, -Angles.RADS_30, 0, Angles.RADS_15);
			animator.rotate(rightarmlower, -Angles.RADS_15, 0, 0);

			animator.endPhase();
			animator.resetPhase(15);
		}
		//scramble synapses
		animator.setAnim(BossActions.SMASH.ordinal());
		{
			animator.startPhase(40); //arms up

			animator.rotate(leftshoulder, -Angles.RADS_75, 0, -Angles.RADS_75);
			animator.rotate(leftarmupper, -Angles.RADS_90, Angles.RADS_90, -Angles.RADS_75);
			animator.rotate(leftarmlower, 0, 0, -Angles.RADS_90);

			animator.rotate(rightshoulder, -Angles.RADS_75, 0, Angles.RADS_75);
			animator.rotate(rightarmupper, -Angles.RADS_90, -Angles.RADS_90, Angles.RADS_75);
			animator.rotate(rightarmlower, 0, 0, Angles.RADS_90);

			animator.rotate(ChestUpper, Angles.RADS_30, 0, 0);

			animator.endPhase();
			animator.setStationaryPhase(10); //hold pose for effect
			animator.resetPhase(10);
		}
	}

	private void setToRestRotations(){
		ChestLower.resetToRestRotations();
		leftarmwristband3.resetToRestRotations();
		leftarmlower.resetToRestRotations();
		leftarmwristband.resetToRestRotations();
		leftarmwristband6.resetToRestRotations();
		leftarmwristband2.resetToRestRotations();
		leftarmwristband4.resetToRestRotations();
		leftarmwristband5.resetToRestRotations();
		leftarmwristband1.resetToRestRotations();
		leftarmupper.resetToRestRotations();
		leftshoulder.resetToRestRotations();
		ChestArmor3.resetToRestRotations();
		ChestMiddle.resetToRestRotations();
		rightshoulder.resetToRestRotations();
		rightarmupper.resetToRestRotations();
		rightarmlower.resetToRestRotations();
		rightarmwristband5.resetToRestRotations();
		rightarmwristband.resetToRestRotations();
		rightarmwristband2.resetToRestRotations();
		rightarmwristband6.resetToRestRotations();
		rightarmwristband1.resetToRestRotations();
		rightarmwristband3.resetToRestRotations();
		rightarmwristband4.resetToRestRotations();
		Head.resetToRestRotations();
		ChestArmor9.resetToRestRotations();
		ChestArmor6.resetToRestRotations();
		ChestArmor8.resetToRestRotations();
		ChestArmor2.resetToRestRotations();
		ChestArmor4.resetToRestRotations();
		ChestArmor7.resetToRestRotations();
		ChestArmor5.resetToRestRotations();
		ChestArmor1.resetToRestRotations();
		ChestUpper.resetToRestRotations();
	}

	private void setRotation(AM2ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
