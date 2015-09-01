package am2.particles.ribbon;

import java.util.LinkedList;
import java.util.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import am2.api.math.AMVector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AMRibbon extends EntityFX{

	int NUMCURVES = 5;	//number of ribbonCurves per ribbon
	int CURVERESOLUTION = 25; //lower -> faster
	float RIBBONWIDTH = 2.25f;
	float NOISESTEP = 0.005f;
	float MAXSEPARATION = 2f;

	PerlinNoise noise;

	float ribbonSeparation,noisePosn;
	Vector pts;
	LinkedList curves;
	float ribbonColor;
	float ribbonWidth;
	RibbonCurve currentCurve; //current RibbonCurve
	int stepId;

	int movement = 2;

	AMVector3 ribbonTarget;

	public AMRibbon(World world, float pcolor, float width, double x, double y, double z){
		super(world, x, y, z, 0.0, 0.0, 0.0);
		this.setPosition(x, y, z);
		this.setVelocity(0, 0, 0);
		//this.setSize(5, 5);
		noise = new PerlinNoise();
		curves = new LinkedList();
		pts = new Vector();

		ribbonColor = pcolor;
		ribbonWidth = width;
		stepId = 0;

		ribbonTarget = new AMVector3(random(-movement,movement), random(-movement,movement), random(-movement,movement));
		ribbonSeparation = lerp(-MAXSEPARATION,MAXSEPARATION, noise.noise1(noisePosn+=NOISESTEP));

		pts.addElement(getRandPt());
		pts.addElement(getRandPt());
		pts.addElement(getRandPt());

		this.particleAge = 0;
		this.particleMaxAge = 200;
		this.renderDistanceWeight = 1.0f;

		addRibbonCurve();
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float partialframe, float cosyaw, float cospitch, float sinyaw, float sinsinpitch, float cossinpitch) {

		par1Tessellator.draw();

		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

		GL11.glPushMatrix();
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);

		double d = (this.prevPosX + (this.posX - this.prevPosX) * partialframe) - RenderManager.instance.viewerPosX;
		double d1 = (this.prevPosY + (this.posY - this.prevPosY) * partialframe) - RenderManager.instance.viewerPosY;
		double d2 = (this.prevPosZ + (this.posZ - this.prevPosZ) * partialframe) - RenderManager.instance.viewerPosZ;

		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glColor4f(1, 1, 1, 1f);
		//GL11.glScalef(0.1f, 0.1f, 0.1f);

		draw();

		GL11.glPopMatrix();
		GL11.glPopAttrib();

		par1Tessellator.startDrawingQuads();
	}

	@Override
	public int getFXLayer() {
		return 2;
	}

	void draw(){
		ribbonTarget = new AMVector3(random(-movement,movement), random(-movement,movement), random(-movement,movement));
		ribbonSeparation = lerp(-MAXSEPARATION,MAXSEPARATION, noise.noise1(noisePosn+=NOISESTEP));

		currentCurve.addSegment();

		int size = curves.size();
		if (size > NUMCURVES-1) {
			RibbonCurve c = (RibbonCurve) curves.get(0);
			c.removeSegment();
		}
		stepId++;

		if (stepId > CURVERESOLUTION) addRibbonCurve();

		//draw curves
		for (int i = 0; i < size; i++) {
			RibbonCurve c = (RibbonCurve) curves.get(i);
			c.draw();
		}
	}

	private float random(float min, float max){
		return (float) ((Math.random() * max) - min);
	}

	private float lerp(float start, float stop, float amount){
		return start + ((stop - start) * amount);
	}

	void addRibbonCurve(){
		//add new point
		pts.addElement(getRandPt());

		AMVector3 nextPt = (AMVector3) pts.elementAt(pts.size()-1);
		AMVector3 curPt = (AMVector3) pts.elementAt(pts.size()-2);
		AMVector3 lastPt = (AMVector3) pts.elementAt(pts.size()-3);

		AMVector3 lastMidPt = new AMVector3 ((curPt.x + lastPt.x)/2,
				(curPt.y + lastPt.y)/2,
				(curPt.z + lastPt.z)/2);

		AMVector3 midPt = new AMVector3 ((curPt.x + nextPt.x)/2,
				(curPt.y + nextPt.y)/2,
				(curPt.z + nextPt.z)/2);

		/*float width = 0.00003F * (getRelativeViewVector(midPt).length()) * 1.5f;
		if (width > 0.2f)
			width = 0.2f;
		if (width < 0.1f)
			width = 0.1f;*/

		float width = 0.2f;

		currentCurve = new RibbonCurve(lastMidPt, midPt,curPt,width,CURVERESOLUTION,ribbonColor);
		curves.add(currentCurve);

		//remove old curves
		if (curves.size() > NUMCURVES){
			curves.removeFirst();
		}

		stepId = 0;

	}

	private static AMVector3 getRelativeViewVector(AMVector3 pos)
	{
		EntityPlayer renderentity = Minecraft.getMinecraft().thePlayer;
		return new AMVector3((float)renderentity.posX - pos.x, (float)renderentity.posY - pos.y, (float)renderentity.posZ - pos.z);
	}

	AMVector3 getRandPt(){
		return new AMVector3(ribbonTarget.x + random(-ribbonSeparation,ribbonSeparation),
				ribbonTarget.y + random(-ribbonSeparation,ribbonSeparation),
				ribbonTarget.z + random(-ribbonSeparation,ribbonSeparation));
	}
	
	@Override
	public boolean isInRangeToRenderDist(double par1) {
		return true;
	}
	
	@Override
	public boolean isInRangeToRender3d(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
		return true;
	}

	@Override
	public void onUpdate() {
		//intentionally left blank
		super.onUpdate();
	}
}