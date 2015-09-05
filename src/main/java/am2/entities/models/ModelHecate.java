package am2.entities.models;

import am2.entities.EntityHecate;
import am2.models.ModelSantaHat;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Calendar;

@SideOnly(Side.CLIENT)
public class ModelHecate extends ModelBase{
	private ModelSantaHat hat;

	private static final ResourceLocation hatLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("SantaHat.png"));

	//fields
	ModelRenderer Main;
	//left arm
	ModelRenderer LeftShoulder;
	ModelRenderer LeftElbow;
	ModelRenderer LeftHand;
	ModelRenderer LeftOuterFinger;
	ModelRenderer LOF1;
	ModelRenderer LOF2;
	ModelRenderer LeftInnerFinger;
	ModelRenderer LIF1;
	ModelRenderer LIF2;
	ModelRenderer LeftThumb;
	ModelRenderer LT1;
	ModelRenderer LT2;
	//right arm
	ModelRenderer RightShoulder;
	ModelRenderer RightElbow;
	ModelRenderer RightHand;
	ModelRenderer RightOuterFinger;
	ModelRenderer ROF1;
	ModelRenderer ROF2;
	ModelRenderer RightInnerFinger;
	ModelRenderer RIF1;
	ModelRenderer RIF2;
	ModelRenderer RightThumb;
	ModelRenderer RT1;
	ModelRenderer RT2;

	ModelRenderer Head;
	ModelRenderer H1;


	public ModelHecate(){
		hat = new ModelSantaHat();

		setupUVMapping();
		initRenderers();
		addBoxes();
		setupHierarchy();
	}

	private void setupHierarchy(){
		//left arm
		//********************************************
		//left outer finger
		LOF1.addChild(LOF2);
		LeftOuterFinger.addChild(LOF1);
		//left inner finger
		LIF1.addChild(LIF2);
		LeftInnerFinger.addChild(LIF1);
		//left thumb
		LT1.addChild(LT2);
		LeftThumb.addChild(LT1);
		//left hand
		LeftHand.addChild(LeftThumb);
		LeftHand.addChild(LeftInnerFinger);
		LeftHand.addChild(LeftOuterFinger);
		//remainder
		LeftElbow.addChild(LeftHand);
		LeftShoulder.addChild(LeftElbow);
		//********************************************
		//right arm
		//********************************************
		//right outer finger
		ROF1.addChild(ROF2);
		RightOuterFinger.addChild(ROF1);
		//right inner finger
		RIF1.addChild(RIF2);
		RightInnerFinger.addChild(RIF1);
		//right thumb
		RT1.addChild(RT2);
		RightThumb.addChild(RT1);
		//right hand
		RightHand.addChild(RightOuterFinger);
		RightHand.addChild(RightInnerFinger);
		RightHand.addChild(RightThumb);
		//remainder
		RightElbow.addChild(RightHand);
		RightShoulder.addChild(RightElbow);
		//********************************************
		//head
		//********************************************
		Head.addChild(H1);
		//********************************************
		//main
		//********************************************
		Main.addChild(LeftShoulder);
		Main.addChild(RightShoulder);
		Main.addChild(Head);
	}

	private void setupUVMapping(){
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("LeftShoulder.LeftArmUpper", 0, 8);
		setTextureOffset("LeftElbow.LeftArmLower", 0, 13);
		setTextureOffset("LeftHand.LeftPalm", 20, 11);
		setTextureOffset("LeftOuterFinger.LeftOuterFingerUpper", 20, 17);
		setTextureOffset("LOF1.LeftOuterFingerTip", 24, 14);
		setTextureOffset("LOF2.LeftOuterFingerKnuckle", 20, 14);
		setTextureOffset("LeftInnerFinger.LeftIndexFingerUpper", 24, 17);
		setTextureOffset("LIF1.LeftIndexFingerKnuckle", 24, 14);
		setTextureOffset("LIF2.LeftIndexFingerTip", 24, 14);
		setTextureOffset("LeftThumb.LeftThumbUpper", 24, 14);
		setTextureOffset("LT1.LeftThumbKnuckle", 24, 14);
		setTextureOffset("LT2.LeftThumbTip", 24, 14);
		setTextureOffset("Main.Body", 32, 15);
		setTextureOffset("RightElbow.RightArmLower", 9, 13);
		setTextureOffset("RightOuterFinger.RightOuterFingerUpper", 20, 17);
		setTextureOffset("ROF1.RightOuterFingerKnuckle", 20, 14);
		setTextureOffset("ROF2.RightOuterFingerTip", 24, 14);
		setTextureOffset("RightHand.RightPalm", 20, 11);
		setTextureOffset("RIF2.RightIndexFingerTip", 24, 14);
		setTextureOffset("RIF1.RightIndexFingerKnuckle", 24, 14);
		setTextureOffset("RightInnerFinger.RightIndexFingerUpper", 24, 17);
		setTextureOffset("RT2.RightThumbTip", 24, 14);
		setTextureOffset("RT1.RightThumbKnuckle", 24, 14);
		setTextureOffset("RightThumb.RightThumbUpper", 24, 14);
		setTextureOffset("RightShoulder.RightArmUpper", 9, 8);
		setTextureOffset("Head.Cowl", 15, 0);
		setTextureOffset("H1.Face", 0, 0);
	}

	private void initRenderers(){
		Main = new ModelRenderer(this, "Main");
		Main.setRotationPoint(0F, 0F, 0F);
		setRotation(Main, 0F, 0F, 0F);
		Main.mirror = true;

		LeftShoulder = new ModelRenderer(this, "LeftShoulder");
		LeftShoulder.setRotationPoint(5.5F, 1F, 0F);
		setRotation(LeftShoulder, 0F, 0F, 0F);
		LeftShoulder.mirror = true;

		LeftElbow = new ModelRenderer(this, "LeftElbow");
		LeftElbow.setRotationPoint(0F, 3F, 0F);
		setRotation(LeftElbow, 0F, 0F, 0F);
		LeftElbow.mirror = true;

		LeftHand = new ModelRenderer(this, "LeftHand");
		LeftHand.setRotationPoint(0F, 4F, 0F);
		setRotation(LeftHand, 0F, 0F, 0F);
		LeftHand.mirror = true;

		LeftOuterFinger = new ModelRenderer(this, "LeftOuterFinger");
		LeftOuterFinger.setRotationPoint(0.5F, 0.6F, 0.5F);
		setRotation(LeftOuterFinger, 0F, 0F, 0F);
		LeftOuterFinger.mirror = true;

		LOF1 = new ModelRenderer(this, "LOF1");
		LOF1.setRotationPoint(0F, 0.6F, 0F);
		setRotation(LOF1, 0F, 0F, 0F);
		LOF1.mirror = true;

		LOF2 = new ModelRenderer(this, "LOF2");
		LOF2.setRotationPoint(0F, 0.6F, 0F);
		setRotation(LOF2, 0F, 0F, 0F);
		LOF2.mirror = true;

		LeftInnerFinger = new ModelRenderer(this, "LeftInnerFinger");
		LeftInnerFinger.setRotationPoint(-0.5F, 0.6F, 0.5F);
		setRotation(LeftInnerFinger, 0F, 0F, 0F);
		LeftInnerFinger.mirror = true;

		LIF1 = new ModelRenderer(this, "LIF1");
		LIF1.setRotationPoint(0F, 0.6F, 0F);
		setRotation(LIF1, 0F, 0F, 0F);
		LIF1.mirror = true;

		LIF2 = new ModelRenderer(this, "LIF2");
		LIF2.setRotationPoint(0F, 0.6F, 0F);
		setRotation(LIF2, 0F, 0F, 0F);
		LIF2.mirror = true;

		LeftThumb = new ModelRenderer(this, "LeftThumb");
		LeftThumb.setRotationPoint(-1F, 0F, 0.5F);
		setRotation(LeftThumb, 0F, 0F, 0F);
		LeftThumb.mirror = true;

		LT1 = new ModelRenderer(this, "LT1");
		LT1.setRotationPoint(0F, 0.5F, 0F);
		setRotation(LT1, 0F, 0F, 0F);
		LT1.mirror = true;

		LT2 = new ModelRenderer(this, "LT2");
		LT2.setRotationPoint(0F, 0.5F, 0F);
		setRotation(LT2, 0F, 0F, 0F);
		LT2.mirror = true;

		RightShoulder = new ModelRenderer(this, "RightShoulder");
		RightShoulder.setRotationPoint(-5.5F, 1F, 0F);
		setRotation(RightShoulder, 0F, 0F, 0F);
		RightShoulder.mirror = true;

		RightElbow = new ModelRenderer(this, "RightElbow");
		RightElbow.setRotationPoint(0F, 3F, 0F);
		setRotation(RightElbow, 0F, 0F, 0F);
		RightElbow.mirror = true;

		RightHand = new ModelRenderer(this, "RightHand");
		RightHand.setRotationPoint(0F, 4F, 0F);
		setRotation(RightHand, 0F, 0F, 0F);
		RightHand.mirror = true;

		RightOuterFinger = new ModelRenderer(this, "RightOuterFinger");
		RightOuterFinger.setRotationPoint(-0.5F, 0.6F, 0.5F);
		setRotation(RightOuterFinger, 0F, 0F, 0F);
		RightOuterFinger.mirror = true;

		ROF1 = new ModelRenderer(this, "ROF1");
		ROF1.setRotationPoint(0F, 0.6F, 0F);
		setRotation(ROF1, 0F, 0F, 0F);
		ROF1.mirror = true;

		ROF2 = new ModelRenderer(this, "ROF2");
		ROF2.setRotationPoint(0F, 0.6F, 0F);
		setRotation(ROF2, 0F, 0F, 0F);
		ROF2.mirror = true;

		RightInnerFinger = new ModelRenderer(this, "RightInnerFinger");
		RightInnerFinger.setRotationPoint(0.5F, 0.6F, 0.5F);
		setRotation(RightInnerFinger, 0F, 0F, 0F);
		RightInnerFinger.mirror = true;

		RIF1 = new ModelRenderer(this, "RIF1");
		RIF1.setRotationPoint(0F, 0.6F, 0F);
		setRotation(RIF1, 0F, 0F, 0F);
		RIF1.mirror = true;

		RIF2 = new ModelRenderer(this, "RIF2");
		RIF2.setRotationPoint(0F, 0.6F, 0F);
		setRotation(RIF2, 0F, 0F, 0F);
		RIF2.mirror = true;

		RightThumb = new ModelRenderer(this, "RightThumb");
		RightThumb.setRotationPoint(1F, 0F, 0.5F);
		setRotation(RightThumb, 0F, 0F, 0F);
		RightThumb.mirror = true;

		RT1 = new ModelRenderer(this, "RT1");
		RT1.setRotationPoint(0F, 0.5F, 0F);
		setRotation(RT1, 0F, 0F, 0F);
		RT1.mirror = true;

		RT2 = new ModelRenderer(this, "RT2");
		RT2.setRotationPoint(0F, 0.5F, 0F);
		setRotation(RT2, 0F, 0F, 0F);
		RT2.mirror = true;

		Head = new ModelRenderer(this, "Head");
		Head.setRotationPoint(0F, -1F, 0.5F);
		setRotation(Head, 0F, 0F, 0F);
		Head.mirror = true;

		H1 = new ModelRenderer(this, "H1");
		H1.setRotationPoint(0F, 0F, -1.9F);
		setRotation(H1, 0F, 0F, 0F);
		H1.mirror = true;
	}

	private void addBoxes(){
		//body
		Main.addBox("Body", -4F, 0F, -1.5F, 8, 10, 4);

		//head
		Head.addBox("Cowl", -3F, -3F, -2F, 6, 4, 4);
		H1.addBox("Face", -2F, -2F, 0F, 4, 4, 1);

		//left arm
		LeftShoulder.addBox("LeftArmUpper", -1.5F, 0F, -1F, 3, 3, 2);
		LeftElbow.addBox("LeftArmLower", -1.5F, 0F, -1F, 3, 4, 2);
		LeftHand.addBox("LeftPalm", -1F, 0F, -0.5F, 2, 1, 1);
		LeftOuterFinger.addBox("LeftOuterFingerUpper", -0.5F, -0.5F, -1F, 1, 1, 1);
		LOF2.addBox("LeftOuterFingerKnuckle", -0.5F, -0.5F, -1F, 1, 1, 1);
		LOF1.addBox("LeftOuterFingerTip", -0.5F, -0.5F, -1F, 1, 1, 1);
		LeftInnerFinger.addBox("LeftIndexFingerUpper", -0.5F, -0.5F, -1F, 1, 1, 1);
		LIF1.addBox("LeftIndexFingerKnuckle", -0.5F, -0.5F, -1F, 1, 1, 1);
		LIF2.addBox("LeftIndexFingerTip", -0.5F, -0.5F, -1F, 1, 1, 1);
		LeftThumb.addBox("LeftThumbUpper", -0.5F, -0.5F, -1F, 1, 1, 1);
		LT1.addBox("LeftThumbKnuckle", -0.5F, -0.5F, -1F, 1, 1, 1);
		LT2.addBox("LeftThumbTip", -0.5F, -0.5F, -1F, 1, 1, 1);

		//right arm
		RightShoulder.addBox("RightArmUpper", -1.5F, 0F, -1F, 3, 3, 2);
		RightElbow.addBox("RightArmLower", -1.5F, 0F, -1F, 3, 4, 2);
		RightHand.addBox("RightPalm", -1F, 0F, -0.5F, 2, 1, 1);
		RightOuterFinger.addBox("RightOuterFingerUpper", -0.5F, -0.5F, -1F, 1, 1, 1);
		ROF1.addBox("RightOuterFingerKnuckle", -0.5F, -0.5F, -1F, 1, 1, 1);
		ROF2.addBox("RightOuterFingerTip", -0.5F, -0.5F, -1F, 1, 1, 1);
		RightInnerFinger.addBox("RightIndexFingerUpper", -0.5F, -0.5F, -1F, 1, 1, 1);
		RIF1.addBox("RightIndexFingerKnuckle", -0.5F, -0.5F, -1F, 1, 1, 1);
		RIF2.addBox("RightIndexFingerTip", -0.5F, -0.5F, -1F, 1, 1, 1);
		RightThumb.addBox("RightThumbUpper", -0.5F, -0.5F, -1F, 1, 1, 1);
		RT1.addBox("RightThumbKnuckle", -0.5F, -0.5F, -1F, 1, 1, 1);
		RT2.addBox("RightThumbTip", -0.5F, -0.5F, -1F, 1, 1, 1);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		GL11.glPushMatrix();

		if (((EntityHecate)entity).isChild()){
			GL11.glScalef(0.5f, 0.5f, 0.5f);
		}
		Main.render(f5);

		if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) >= 23 && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) <= 27){
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			GL11.glTranslatef(0, -0.1f, 0.1f);
			Minecraft.getMinecraft().renderEngine.bindTexture(hatLoc);
			hat.renderModel(f5);
		}

		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setMainRotationAngle(float angle){
		//base this on movement speed
		Main.rotateAngleX = angle;

		setBaseRotation();
	}

	public void setLeftArmRotationOffset(float angle){
		LeftShoulder.rotateAngleX += angle;
	}

	public void setRightArmRotationOffset(float angle){
		RightShoulder.rotateAngleX += angle;
	}

	private void setBaseRotation(){
		Head.rotateAngleX = -Main.rotateAngleX;

		LeftShoulder.rotateAngleX = (float)Math.toRadians(-80) - Main.rotateAngleX;
		RightShoulder.rotateAngleX = (float)Math.toRadians(-80) - Main.rotateAngleX;
		LeftElbow.rotateAngleX = (float)Math.toRadians(-15);
		RightElbow.rotateAngleX = (float)Math.toRadians(-15);

		LeftOuterFinger.rotateAngleX = (float)Math.toRadians(-19);
		LeftOuterFinger.rotateAngleY = 0;
		LeftOuterFinger.rotateAngleZ = (float)Math.toRadians(-19);

		LOF1.rotateAngleX = -LeftOuterFinger.rotateAngleX;
		LOF2.rotateAngleX = -LeftOuterFinger.rotateAngleX;

		LeftInnerFinger.rotateAngleX = (float)Math.toRadians(-19);
		LeftInnerFinger.rotateAngleY = 0;
		LeftInnerFinger.rotateAngleZ = (float)Math.toRadians(19);

		LIF1.rotateAngleX = -LeftInnerFinger.rotateAngleX;
		LIF2.rotateAngleX = -LeftInnerFinger.rotateAngleX;

		LeftThumb.rotateAngleZ = (float)Math.toRadians(45);

		RightOuterFinger.rotateAngleX = (float)Math.toRadians(-19);
		RightOuterFinger.rotateAngleY = 0;
		RightOuterFinger.rotateAngleZ = (float)Math.toRadians(19);

		ROF1.rotateAngleX = -RightOuterFinger.rotateAngleX;
		ROF2.rotateAngleX = -RightOuterFinger.rotateAngleX;

		RightInnerFinger.rotateAngleX = (float)Math.toRadians(-19);
		RightInnerFinger.rotateAngleY = 0;
		RightInnerFinger.rotateAngleZ = (float)Math.toRadians(-19);

		RIF1.rotateAngleX = -RightInnerFinger.rotateAngleX;
		RIF2.rotateAngleX = -RightInnerFinger.rotateAngleX;

		RightThumb.rotateAngleZ = (float)Math.toRadians(-45);
	}

}
