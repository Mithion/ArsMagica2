package am2.bosses.models;

import am2.bosses.BossActions;
import am2.bosses.EntityEnderGuardian;
import am2.entities.renderers.AM2ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.AnimationAPI.client.Animator;

//
//import thehippomaster.AnimationAPI.client.Animator;

public class ModelEnderGuardian extends ModelBase{
	//fields
	AM2ModelRenderer Collar;
	AM2ModelRenderer RibsTop;
	AM2ModelRenderer RibsMiddle;
	AM2ModelRenderer RibsBottom;
	AM2ModelRenderer RightArmUpper;
	AM2ModelRenderer LeftArmUpper;
	AM2ModelRenderer RightArmLower;
	AM2ModelRenderer LeftArmLower;
	AM2ModelRenderer SpineBottom;
	AM2ModelRenderer SpineMiddleBottom;
	AM2ModelRenderer SpineMiddleTop;
	AM2ModelRenderer SpineTop;
	AM2ModelRenderer LeftWingLower;
	AM2ModelRenderer RightWingLower;
	AM2ModelRenderer LeftWingUpper;
	AM2ModelRenderer RightWingUpper;
	AM2ModelRenderer LeftWing;
	AM2ModelRenderer RightWing;
	AM2ModelRenderer Head;
	AM2ModelRenderer Body;
	AM2ModelRenderer Tail;

	private Animator animator;

	public ModelEnderGuardian(){
		textureWidth = 128;
		textureHeight = 128;

		instantiateParts();
		addBoxes();
		setupMirroring();
		setTextureSizes();
		setRotationPoints();
		setInitialRotations();
		//hierarchy
		setHeirarchy();

		animator = new Animator(this);
	}

	private void setRotationPoints(){
		Body.setRotationPoint(0F, -18F, -2F);

		Head.setRotationPoint(-4, -8, -4);

		Collar.setRotationPoint(-6F, 0F, -1F);

		RibsTop.setRotationPoint(-4F, 3F, -1F);
		RibsMiddle.setRotationPoint(-4F, 6F, -1F);
		RibsBottom.setRotationPoint(-4F, 9F, -1F);

		RightArmUpper.setRotationPoint(-2F, 0F, -0.5F);
		RightArmLower.setRotationPoint(0F, 10F, 0.5F);

		LeftArmUpper.setRotationPoint(12F, 0F, -0.5F);
		LeftArmLower.setRotationPoint(0F, 10F, 0.5F);

		SpineBottom.setRotationPoint(-1F, 10F, 2F);
		SpineMiddleBottom.setRotationPoint(-1F, 7F, 2F);
		SpineMiddleTop.setRotationPoint(-1F, 4F, 2F);
		SpineTop.setRotationPoint(-1F, 1F, 2F);

		LeftWingLower.setRotationPoint(3F, 0F, 3F);
		LeftWingUpper.setRotationPoint(-10F, 0F, 0F);
		LeftWing.setRotationPoint(-15F, 2F, 2F);

		RightWingLower.setRotationPoint(-3F, 0F, 3F);
		RightWingUpper.setRotationPoint(10F, 0F, 0F);
		RightWing.setRotationPoint(-1F, 2F, 2F);

		Tail.setRotationPoint(-1F, 14F, 0F);
	}

	private void addBoxes(){
		Body.addBox(-2F, 0F, -2F, 4, 14, 4);
		Head.addBox(0F, 0F, 0F, 8, 8, 8);
		Collar.addBox(0F, 0F, 0F, 12, 2, 2);
		RibsTop.addBox(0F, 0F, 0F, 8, 2, 2);
		RibsMiddle.addBox(0F, 0F, 0F, 8, 2, 2);
		RibsBottom.addBox(0F, 0F, 0F, 8, 2, 2);
		RightArmUpper.addBox(0F, 0F, 0F, 2, 11, 3);
		LeftArmUpper.addBox(0F, 0F, 0F, 2, 11, 3);
		RightArmLower.addBox(0F, 0F, 0F, 2, 10, 2);
		LeftArmLower.addBox(0F, 0F, 0F, 2, 10, 2);
		SpineBottom.addBox(0F, 0F, 0F, 2, 2, 1);
		SpineMiddleBottom.addBox(0F, 0F, 0F, 2, 2, 1);
		SpineMiddleTop.addBox(0F, 0F, 0F, 2, 2, 1);
		SpineTop.addBox(0F, 0F, 0F, 2, 2, 1);
		LeftWingLower.addBox(-10F, 0F, 0F, 10, 2, 2);
		RightWingLower.addBox(0F, 0F, 0F, 10, 2, 2);
		LeftWingUpper.addBox(-14F, 0F, 0F, 14, 2, 2);
		RightWingUpper.addBox(0F, 0F, 0F, 14, 2, 2);
		LeftWing.addBox(-15F, 0F, 0F, 15, 20, 0);
		RightWing.addBox(0F, 0F, 0F, 15, 20, 0);
		Tail.addBox(0F, 0F, 0F, 2, 16, 2);
	}

	private void setTextureSizes(){
		Body.setTextureSize(128, 128);
		Head.setTextureSize(128, 128);
		Collar.setTextureSize(128, 128);
		RibsTop.setTextureSize(128, 128);
		RibsMiddle.setTextureSize(128, 128);
		RibsBottom.setTextureSize(128, 128);
		RightArmUpper.setTextureSize(128, 128);
		LeftArmUpper.setTextureSize(128, 128);
		RightArmLower.setTextureSize(128, 128);
		LeftArmLower.setTextureSize(128, 128);
		SpineBottom.setTextureSize(128, 128);
		SpineMiddleBottom.setTextureSize(128, 128);
		SpineMiddleTop.setTextureSize(128, 128);
		SpineTop.setTextureSize(128, 128);
		LeftWingLower.setTextureSize(128, 128);
		RightWingLower.setTextureSize(128, 128);
		LeftWingUpper.setTextureSize(128, 128);
		RightWingUpper.setTextureSize(128, 128);
		LeftWing.setTextureSize(128, 128);
		RightWing.setTextureSize(128, 128);
		Tail.setTextureSize(128, 128);
	}

	private void instantiateParts(){
		Body = new AM2ModelRenderer(this, 0, 17);
		Head = new AM2ModelRenderer(this, 0, 0);
		Collar = new AM2ModelRenderer(this, 0, 36);
		RibsTop = new AM2ModelRenderer(this, 0, 41);
		RibsMiddle = new AM2ModelRenderer(this, 0, 41);
		RibsBottom = new AM2ModelRenderer(this, 0, 41);
		RightArmUpper = new AM2ModelRenderer(this, 0, 65);
		LeftArmUpper = new AM2ModelRenderer(this, 11, 65);
		RightArmLower = new AM2ModelRenderer(this, 0, 80);
		LeftArmLower = new AM2ModelRenderer(this, 9, 80);
		SpineBottom = new AM2ModelRenderer(this, 0, 93);
		SpineMiddleBottom = new AM2ModelRenderer(this, 0, 93);
		SpineMiddleTop = new AM2ModelRenderer(this, 0, 93);
		SpineTop = new AM2ModelRenderer(this, 0, 93);
		LeftWingLower = new AM2ModelRenderer(this, 0, 97);
		RightWingLower = new AM2ModelRenderer(this, 0, 97);
		LeftWingUpper = new AM2ModelRenderer(this, 0, 102);
		RightWingUpper = new AM2ModelRenderer(this, 0, 102);
		LeftWing = new AM2ModelRenderer(this, 0, 107);
		RightWing = new AM2ModelRenderer(this, 0, 107);
		Tail = new AM2ModelRenderer(this, 0, 46);
	}

	private void setupMirroring(){
		Body.mirror = true;
		Head.mirror = true;
		Collar.mirror = true;
		RibsTop.mirror = true;
		RibsMiddle.mirror = true;
		RibsBottom.mirror = true;
		RightArmUpper.mirror = true;
		LeftArmUpper.mirror = true;
		RightArmLower.mirror = true;
		LeftArmLower.mirror = true;
		SpineBottom.mirror = true;
		SpineMiddleBottom.mirror = true;
		SpineMiddleTop.mirror = true;
		SpineTop.mirror = true;
		LeftWingLower.mirror = true;
		RightWingLower.mirror = true;
		LeftWingUpper.mirror = true;
		RightWingUpper.mirror = true;
		LeftWing.mirror = true;
		Tail.mirror = true;
		RightWing.mirror = false;
	}

	private void setInitialRotations(){
		setRotation(SpineMiddleBottom, 0F, 0F, 0F);
		setRotation(SpineBottom, 0F, 0F, 0F);
		setRotation(LeftArmLower, 0F, 0F, 0F);
		setRotation(RightArmLower, 0F, 0F, 0F);
		setRotation(LeftArmUpper, 0F, 0F, 0F);
		setRotation(RightArmUpper, 0F, 0F, 0F);
		setRotation(RibsBottom, 0F, 0F, 0F);
		setRotation(RibsMiddle, 0F, 0F, 0F);
		setRotation(RibsTop, 0F, 0F, 0F);
		setRotation(Collar, 0F, 0F, 0F);
		setRotation(Head, 0F, 0F, 0F);
		setRotation(Body, 0F, 0F, 0F);
		setRotation(SpineMiddleTop, 0F, 0F, 0F);
		setRotation(SpineTop, 0F, 0F, 0F);

		setRotation(LeftWingLower, 0.7F, -4.14F, 0F);
		setRotation(RightWingLower, 0.7F, 4.14F, 0F);
		setRotation(LeftWingUpper, -0.5F, 1.2F, 0F);
		setRotation(RightWingUpper, -0.5F, -1.2F, 0F);
		setRotation(LeftWing, 0F, 3.1415926F, 0F);
		setRotation(RightWing, 0F, 0F, 0F);

		setRotation(Tail, 0.5585054F, 0F, 0F);

		Collar.storeRestRotations();
		RibsTop.storeRestRotations();
		RibsMiddle.storeRestRotations();
		RibsBottom.storeRestRotations();
		RightArmUpper.storeRestRotations();
		LeftArmUpper.storeRestRotations();
		RightArmLower.storeRestRotations();
		LeftArmLower.storeRestRotations();
		SpineBottom.storeRestRotations();
		SpineMiddleBottom.storeRestRotations();
		SpineMiddleTop.storeRestRotations();
		SpineTop.storeRestRotations();
		LeftWingLower.storeRestRotations();
		RightWingLower.storeRestRotations();
		LeftWingUpper.storeRestRotations();
		RightWingUpper.storeRestRotations();
		LeftWing.storeRestRotations();
		RightWing.storeRestRotations();
		Head.storeRestRotations();
		Body.storeRestRotations();
		Tail.storeRestRotations();
	}

	private void setHeirarchy(){
		Body.addChild(Head);
		Body.addChild(Collar);
		Body.addChild(RibsBottom);
		Body.addChild(RibsMiddle);
		Body.addChild(RibsTop);
		Body.addChild(SpineTop);
		Body.addChild(SpineMiddleTop);
		Body.addChild(SpineMiddleBottom);
		Body.addChild(SpineBottom);
		Body.addChild(Tail);

		Body.addChild(LeftWingLower);
		LeftWingLower.addChild(LeftWingUpper);
		LeftWingUpper.addChild(LeftWing);

		Body.addChild(RightWingLower);
		RightWingLower.addChild(RightWingUpper);
		RightWingUpper.addChild(RightWing);

		Collar.addChild(LeftArmUpper);
		Collar.addChild(RightArmUpper);

		LeftArmUpper.addChild(LeftArmLower);
		RightArmUpper.addChild(RightArmLower);
	}

	private void flapWings(EntityEnderGuardian entity, float f, float f1, float f2, float f3, float f4, float f5){
		float angle = MathHelper.sin((entity.getWingFlapTime() + f2 - entity.ticksExisted + f5) * entity.getWingFlapSpeed()) / 3 * 2;
		float halfangle = angle * 1.5f;

		setOffsetRotation(LeftWingLower, angle, -angle, 0);
		setOffsetRotation(LeftWingUpper, 0, -halfangle, 0);
		setOffsetRotation(RightWingLower, angle, angle, 0);
		setOffsetRotation(RightWingUpper, 0, halfangle, 0);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		animate((IAnimatedEntity)entity, f, f1, f2, f3, f4, f5);

		if (((EntityEnderGuardian)entity).shouldFlapWings())
			flapWings((EntityEnderGuardian)entity, f, f1, f2, f3, f4, f5);

		Body.render(f5);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
	}

	private void setAngles(){
		Body.resetToRestRotations();

		Head.resetToRestRotations();

		Collar.resetToRestRotations();

		RibsTop.resetToRestRotations();
		RibsMiddle.resetToRestRotations();
		RibsBottom.resetToRestRotations();

		RightArmUpper.resetToRestRotations();
		RightArmLower.resetToRestRotations();

		LeftArmUpper.resetToRestRotations();
		LeftArmLower.resetToRestRotations();

		SpineBottom.resetToRestRotations();
		SpineMiddleBottom.resetToRestRotations();
		SpineMiddleTop.resetToRestRotations();
		SpineTop.resetToRestRotations();

		LeftWingLower.resetToRestRotations();
		LeftWingUpper.resetToRestRotations();
		LeftWing.resetToRestRotations();

		RightWingLower.resetToRestRotations();
		RightWingUpper.resetToRestRotations();
		RightWing.resetToRestRotations();

		Tail.resetToRestRotations();
	}

	public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5){
		animator.update(entity);
		setAngles();

		//braces inside this function are purely for organization
		float angle = 0;
		float angle2 = 0;

		animator.setAnim(BossActions.CASTING.ordinal()); //hands of the dead
		animator.startPhase(10);
		{
			angle = -Angles.RADS_20;
			angle2 = -Angles.RADS_90;
			animator.rotate(Body, -angle, 0, 0);
			animator.rotate(Head, angle, 0, 0);
			animator.rotate(Tail, -angle, 0, 0);
			animator.rotate(LeftArmUpper, -angle, 0, 0);
			animator.rotate(RightArmUpper, -angle, 0, 0);
			animator.rotate(LeftArmLower, angle2, 0, 0);
			animator.rotate(RightArmLower, angle2, 0, 0);
		}
		animator.endPhase();
		animator.startPhase(50);
		{
			angle = -Angles.RADS_145;
			angle2 = -Angles.RADS_45;
			animator.rotate(Body, 0, 0, 0);
			animator.rotate(Head, -Angles.RADS_30, 0, 0);
			animator.rotate(Tail, 0, 0, 0);
			animator.rotate(LeftArmUpper, angle, 0, 0);
			animator.rotate(RightArmUpper, angle, 0, 0);
			animator.rotate(LeftArmLower, angle2, 0, 0);
			animator.rotate(RightArmLower, angle2, 0, 0);
		}
		animator.endPhase();
		animator.setStationaryPhase(40);
		animator.resetPhase(10);

		animator.setAnim(BossActions.STRIKE.ordinal()); //ender wave
		animator.startPhase(7);
		{
			animator.rotate(LeftArmUpper, -Angles.RADS_30, 0, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_90, 0, 0);
			animator.rotate(Collar, 0, -Angles.RADS_15, 0);
			animator.rotate(Body, 0, -Angles.RADS_15, 0);
			animator.rotate(Tail, -Angles.RADS_15, 0, 0);
		}
		animator.endPhase();
		animator.startPhase(7);
		{
			animator.rotate(LeftArmUpper, -Angles.RADS_90, 0, 0);
			animator.rotate(LeftArmLower, 0, 0, 0);
			animator.rotate(Collar, 0, Angles.RADS_15, 0);
			animator.rotate(Body, 0, Angles.RADS_15, 0);
			animator.rotate(Tail, Angles.RADS_15, 0, 0);
		}
		animator.endPhase();
		animator.setStationaryPhase(6);
		animator.resetPhase(10);

		animator.setAnim(BossActions.SMASH.ordinal()); //ender beam
		animator.startPhase(10);
		{
			angle = -Angles.RADS_145;
			angle2 = -Angles.RADS_45;
			animator.rotate(Body, -Angles.RADS_15, 0, 0);
			animator.rotate(Head, -Angles.RADS_30, 0, 0);
			animator.rotate(Tail, Angles.RADS_15, 0, 0);
			animator.rotate(LeftArmUpper, angle, 0, 0);
			animator.rotate(RightArmUpper, angle, 0, 0);
			animator.rotate(LeftArmLower, angle2, 0, 0);
			animator.rotate(RightArmLower, angle2, 0, 0);
		}
		animator.endPhase();
		animator.startPhase(5);
		{
			angle = -Angles.RADS_75;
			angle2 = -Angles.RADS_15;
			animator.rotate(Body, Angles.RADS_15, 0, 0);
			animator.rotate(Head, 0, 0, 0);
			animator.rotate(Tail, angle, 0, 0);
			animator.rotate(LeftArmUpper, angle, 0, 0);
			animator.rotate(RightArmUpper, angle, 0, 0);
			animator.rotate(LeftArmLower, angle2, 0, 0);
			animator.rotate(RightArmLower, angle2, 0, 0);
		}
		animator.endPhase();
		animator.setStationaryPhase(120);
		animator.resetPhase(20);

		animator.setAnim(BossActions.SPINNING.ordinal()); //shadowstep
		animator.startPhase(15);
		{
			angle = -Angles.RADS_145;
			angle2 = -Angles.RADS_45;
			animator.rotate(Body, 0, Angles.RADS_360, 0);
			animator.rotate(LeftArmUpper, angle, 0, 0);
			animator.rotate(LeftArmLower, angle2, 0, 0);
		}
		animator.endPhase();
		animator.resetPhase(0);

		animator.setAnim(BossActions.LONG_CASTING.ordinal()); //otherworldly roar
		animator.startPhase(30);
		{
			animator.rotate(Body, Angles.RADS_30, 0, 0);
			animator.rotate(Head, Angles.RADS_30, 0, 0);
			animator.rotate(Tail, -Angles.RADS_45, 0, 0);
			animator.rotate(LeftArmUpper, -Angles.RADS_15, Angles.RADS_15, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(RightArmUpper, -Angles.RADS_15, Angles.RADS_15, 0);
			animator.rotate(RightArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(LeftWingLower, 0, Angles.RADS_90, 0);
			animator.rotate(LeftWingUpper, 0, Angles.RADS_90, 0);
			animator.rotate(RightWingLower, 0, -Angles.RADS_90, 0);
			animator.rotate(RightWingUpper, 0, -Angles.RADS_90, 0);
		}
		animator.endPhase();
		animator.startPhase(3);
		{
			animator.rotate(Body, -Angles.RADS_15, 0, 0);
			animator.rotate(Head, -Angles.RADS_15, 0, 0);
			animator.rotate(Tail, Angles.RADS_15, 0, 0);
			animator.rotate(LeftArmUpper, -Angles.RADS_15, -Angles.RADS_30, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(RightArmUpper, -Angles.RADS_15, Angles.RADS_30, 0);
			animator.rotate(RightArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(LeftWingLower, 0, -Angles.RADS_45, 0);
			animator.rotate(LeftWingUpper, 0, -Angles.RADS_30, 0);
			animator.rotate(RightWingLower, 0, Angles.RADS_45, 0);
			animator.rotate(RightWingUpper, 0, Angles.RADS_30, 0);
		}
		animator.endPhase();
		animator.setStationaryPhase(20);
		animator.resetPhase(10);

		animator.setAnim(BossActions.SHIELD_BASH.ordinal()); //protect
		animator.startPhase(5);
		{
			animator.rotate(Body, Angles.RADS_30, 0, 0);
			animator.rotate(Head, Angles.RADS_30, 0, 0);
			animator.rotate(Tail, -Angles.RADS_45, 0, 0);
			animator.rotate(LeftArmUpper, -Angles.RADS_15, Angles.RADS_30, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(RightArmUpper, -Angles.RADS_15, -Angles.RADS_30, 0);
			animator.rotate(RightArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(LeftWingLower, 0, Angles.RADS_90, 0);
			animator.rotate(LeftWingUpper, 0, Angles.RADS_75, 0);
			animator.rotate(RightWingLower, 0, -Angles.RADS_90, 0);
			animator.rotate(RightWingUpper, 0, -Angles.RADS_75, 0);
		}
		animator.endPhase();
		animator.setStationaryPhase(20);
		animator.resetPhase(10);

		animator.setAnim(BossActions.LAUNCHING.ordinal()); //ender grip
		animator.startPhase(4);
		{
			animator.rotate(LeftArmUpper, -Angles.RADS_30, 0, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_90, 0, 0);
			animator.rotate(Collar, 0, -Angles.RADS_15, 0);
			animator.rotate(Body, 0, -Angles.RADS_15, 0);
			animator.rotate(Tail, -Angles.RADS_15, 0, 0);
		}
		animator.endPhase();
		animator.startPhase(4);
		{
			animator.rotate(LeftArmUpper, -Angles.RADS_90, 0, 0);
			animator.rotate(LeftArmLower, 0, 0, 0);
			animator.rotate(Collar, 0, Angles.RADS_15, 0);
			animator.rotate(Body, 0, Angles.RADS_15, 0);
			animator.rotate(Tail, Angles.RADS_15, 0, 0);
		}
		animator.endPhase();
		animator.setStationaryPhase(10);
		animator.resetPhase(10);

		animator.setAnim(BossActions.CHARGE.ordinal()); //ender rush
		animator.startPhase(15);
		{
			animator.rotate(Body, Angles.RADS_30, 0, 0);
			animator.rotate(Head, Angles.RADS_30, 0, 0);
			animator.rotate(Tail, -Angles.RADS_45, 0, 0);
			animator.rotate(LeftArmUpper, -Angles.RADS_15, Angles.RADS_30, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_45, 0, 0);
			animator.rotate(RightArmUpper, -Angles.RADS_15, -Angles.RADS_30, 0);
			animator.rotate(RightArmLower, -Angles.RADS_45, 0, 0);
		}
		animator.endPhase();
		animator.startPhase(5);
		{
			angle = -Angles.RADS_145;
			animator.rotate(Body, Angles.RADS_90, 0, 0);
			animator.rotate(Head, 0, 0, 0);
			animator.rotate(LeftArmUpper, angle, 0, 0);
			animator.rotate(RightArmUpper, angle, 0, 0);
			animator.rotate(LeftArmLower, -Angles.RADS_15, 0, 0);
			animator.rotate(RightArmLower, -Angles.RADS_15, 0, 0);
			animator.rotate(Tail, -Angles.RADS_45, 0, 0);
		}
		animator.endPhase();
		animator.startPhase(10);
		{
			animator.rotate(Body, 0, -6.28318531f, 0);
		}
		animator.endPhase();
		animator.startPhase(1);
		{
			animator.rotate(Body, 0, 0, 0);
		}
		animator.endPhase();
		animator.resetPhase(5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	private void setOffsetRotation(AM2ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = model.getRestRotationX() + x;
		model.rotateAngleY = model.getRestRotationY() + y;
		model.rotateAngleZ = model.getRestRotationZ() + z;
	}

}
