package am2.codechicken;

import am2.api.math.AMVector3;
import am2.texture.ResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

public class LightningBolt extends EntityFX{
	private int type = 0;
	private int overrideColor = -1;
	private LightningBoltCommon main;

	private static final ResourceLocation bolt = new ResourceLocation("arsmagica2", ResourceManager.GetFXTexturePath("smoke.png"));

	public LightningBolt(World world, AMVector3 jammervec, AMVector3 targetvec, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, jammervec, targetvec, seed);
		setupFromMain();
	}

	public LightningBolt(World world, Entity detonator, Entity target, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, target, seed);
		setupFromMain();
	}

	public LightningBolt(World world, Entity detonator, Entity target, long seed, int speed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, target, seed, speed);
		setupFromMain();
	}

	public LightningBolt(World world, TileEntity detonator, Entity target, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, target, seed);
		setupFromMain();
	}

	public LightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi);
		setupFromMain();
	}

	public LightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
		setupFromMain();
	}

	public LightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, 1.0F);
		setupFromMain();
	}

	public LightningBolt(World world, TileEntity detonator, double x, double y, double z, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, x, y, z, seed);
		setupFromMain();
	}

	private void setupFromMain(){
		this.main.setWrapper(this);
		this.particleAge = this.main.particleMaxAge;
		setPosition(this.main.start.x, this.main.start.y, this.main.start.z);
		setVelocity(0.0D, 0.0D, 0.0D);
	}

	public void defaultFractal(){
		this.main.defaultFractal();
	}

	public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle){
		this.main.fractal(splits, amount, splitchance, splitlength, splitangle);
	}

	public void finalizeBolt(){
		this.main.finalizeBolt();
		Minecraft.getMinecraft().effectRenderer.addEffect(this);
	}

	public void setOverrideColor(int overrideColor){
		this.overrideColor = overrideColor;
	}

	public void setSourceEntity(Entity entity){
		this.main.wrapper = entity;
	}

	public void setType(int type){
		this.type = type;
		this.main.type = type;
	}

	public void setDamage(int dmg){
		this.main.damage = dmg;
	}

	public void setNonLethal(){
		this.main.nonLethal = true;
	}

	public void setMultiplier(float m){
		this.main.multiplier = m;
	}

	@Override
	public void onUpdate(){
		this.main.onUpdate();
		if (this.main.particleAge >= this.main.particleMaxAge){
			setDead();
		}
	}

	private static AMVector3 getRelativeViewVector(AMVector3 pos){
		EntityPlayer renderentity = Minecraft.getMinecraft().thePlayer;
		return new AMVector3((float)renderentity.posX - pos.x, (float)renderentity.posY - pos.y, (float)renderentity.posZ - pos.z);
	}

	private void renderBolt(Tessellator tessellator, float partialframe, float cosyaw, float cospitch, float sinyaw, float cossinpitch, int pass){
		AMVector3 playervec = new AMVector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
		float boltage = this.main.particleAge >= 0 ? this.main.particleAge / this.main.particleMaxAge : 0.0F;
		float mainalpha = 1.0F;
		if (pass == 0)
			mainalpha = (1.0F - boltage) * 0.9F;
		else if (pass == 1)
			mainalpha = 1.0F - boltage * 0.6F;
		else
			mainalpha = 1.0F - boltage * 0.3F;
		int renderlength = (int)((this.main.particleAge + partialframe + (int)(this.main.length * 3.0F)) / (int)(this.main.length * 3.0F) * this.main.numsegments0);
		for (Iterator iterator = this.main.segments.iterator(); iterator.hasNext(); ){
			LightningBoltCommon.Segment rendersegment = (LightningBoltCommon.Segment)iterator.next();
			if (rendersegment.segmentno <= renderlength){
				float width = 0.03F * (getRelativeViewVector(rendersegment.startpoint.point).length() / 10.0F + 1.0F) * (1.0F + rendersegment.light) * 0.5F;
				if (width > 0.05F) width = 0.05F;
				if (pass == 1) width += 0.025f;
				else if (pass == 1) width += 0.05f;
				AMVector3 diff1 = AMVector3.crossProduct(playervec, rendersegment.prevdiff).scale(width / rendersegment.sinprev);
				AMVector3 diff2 = AMVector3.crossProduct(playervec, rendersegment.nextdiff).scale(width / rendersegment.sinnext);
				AMVector3 startvec = rendersegment.startpoint.point;
				AMVector3 endvec = rendersegment.endpoint.point;
				float rx1 = (float)(startvec.x - interpPosX);
				float ry1 = (float)(startvec.y - interpPosY);
				float rz1 = (float)(startvec.z - interpPosZ);
				float rx2 = (float)(endvec.x - interpPosX);
				float ry2 = (float)(endvec.y - interpPosY);
				float rz2 = (float)(endvec.z - interpPosZ);
				tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light);
				tessellator.addVertexWithUV(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z, 0.5D, 0.0D);
				tessellator.addVertexWithUV(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z, 0.5D, 0.0D);
				tessellator.addVertexWithUV(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z, 0.5D, 1.0D);
				tessellator.addVertexWithUV(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z, 0.5D, 1.0D);
				if (rendersegment.next == null){
					AMVector3 roundend = rendersegment.endpoint.point.copy().add(rendersegment.diff.copy().normalize().scale(width));
					float rx3 = (float)(roundend.x - interpPosX);
					float ry3 = (float)(roundend.y - interpPosY);
					float rz3 = (float)(roundend.z - interpPosZ);
					tessellator.addVertexWithUV(rx3 - diff2.x, ry3 - diff2.y, rz3 - diff2.z, 0.0D, 0.0D);
					tessellator.addVertexWithUV(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z, 0.5D, 0.0D);
					tessellator.addVertexWithUV(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z, 0.5D, 1.0D);
					tessellator.addVertexWithUV(rx3 + diff2.x, ry3 + diff2.y, rz3 + diff2.z, 0.0D, 1.0D);
				}
				if (rendersegment.prev == null){
					AMVector3 roundend = rendersegment.startpoint.point.copy().sub(rendersegment.diff.copy().normalize().scale(width));
					float rx3 = (float)(roundend.x - interpPosX);
					float ry3 = (float)(roundend.y - interpPosY);
					float rz3 = (float)(roundend.z - interpPosZ);
					tessellator.addVertexWithUV(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z, 0.5D, 0.0D);
					tessellator.addVertexWithUV(rx3 - diff1.x, ry3 - diff1.y, rz3 - diff1.z, 0.0D, 0.0D);
					tessellator.addVertexWithUV(rx3 + diff1.x, ry3 + diff1.y, rz3 + diff1.z, 0.0D, 1.0D);
					tessellator.addVertexWithUV(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z, 0.5D, 1.0D);
				}
			}
		}
	}

	@Override
	public void renderParticle(Tessellator tessellator, float partialframe, float cosyaw, float cospitch, float sinyaw, float sinsinpitch, float cossinpitch){
		EntityPlayer renderentity = Minecraft.getMinecraft().thePlayer;
		int visibleDistance = 100;
		if ((!Minecraft.getMinecraft().gameSettings.fancyGraphics)) visibleDistance = 50;
		if (renderentity.getDistance(this.posX, this.posY, this.posZ) > visibleDistance) return;

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);

		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(bolt);

		this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);

		int brightness = 0xF00F0;

		switch (this.type){
		case 0:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.6F;
			GL11.glBlendFunc(770, 1);
			break;
		case 1:
			this.particleRed = 0.6F;
			this.particleGreen = 0.6F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 1);
			break;
		case 2:
			this.particleRed = 0.6F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.6F;
			GL11.glBlendFunc(770, 1);
			break;
		case 3:
			this.particleRed = 0.1F;
			this.particleGreen = 1.0F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 1);
			break;
		case 4:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			brightness = 0xF0060;
			break;
		case 5:
			this.particleRed = 0.6F;
			this.particleGreen = 0.3F;
			this.particleBlue = 0.6F;
			GL11.glBlendFunc(770, 771);
			break;
		case 6:
			this.particleRed = 0.1f;
			this.particleGreen = 0.1f;
			this.particleBlue = 0.1f;
			GL11.glBlendFunc(770, 1);
			brightness = 0xF0000;
			break;
		}

		if (overrideColor != -1){
			this.particleRed = ((overrideColor >> 16) & 0xFF) / 255.0f;
			this.particleGreen = ((overrideColor >> 8) & 0xFF) / 255.0f;
			this.particleBlue = (overrideColor & 0xFF) / 255.0f;
		}

		tessellator.setBrightness(brightness);
		renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 0);
		try{
			tessellator.draw();
		}catch (Throwable t){
		}

		brightness = 0xF00F0;

		switch (this.type){
		case 0:
			this.particleRed = 1.0F;
			this.particleGreen = 0.6F;
			this.particleBlue = 1.0F;
			break;
		case 1:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 1.0F;
			break;
		case 2:
			this.particleRed = 0.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
			break;
		case 3:
			this.particleRed = 0.1F;
			this.particleGreen = 0.6F;
			this.particleBlue = 0.1F;
			break;
		case 4:
			this.particleRed = 0.6F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 5:
			this.particleRed = 1.0F;
			this.particleGreen = 1.0F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 6:
			this.particleRed = 0.6f;
			this.particleGreen = 0.1f;
			this.particleBlue = 0.6f;
			GL11.glBlendFunc(770, 1);
			brightness = 0xF0060;
			break;
		}

		if (overrideColor != -1){
			this.particleRed = ((overrideColor >> 16) & 0xFF) / 255.0f;
			this.particleGreen = ((overrideColor >> 8) & 0xFF) / 255.0f;
			this.particleBlue = (overrideColor & 0xFF) / 255.0f;
		}

		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		;
		tessellator.setBrightness(brightness);
		renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 1);
		try{
			tessellator.draw();
		}catch (Throwable t){
		}

		brightness = 0xF00F0;

		switch (this.type){
		case 0:
			this.particleRed = 1.0F;
			this.particleGreen = 0.6F;
			this.particleBlue = 1.0F;
			break;
		case 1:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 1.0F;
			break;
		case 2:
			this.particleRed = 0.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
			break;
		case 3:
			this.particleRed = 0.1F;
			this.particleGreen = 0.6F;
			this.particleBlue = 0.1F;
			break;
		case 4:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
			brightness = 0xF0060;
			GL11.glBlendFunc(770, 771);
			break;
		case 5:
			this.particleRed = 1.0F;
			this.particleGreen = 1.0F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 6:
			this.particleRed = 0.6f;
			this.particleGreen = 0.1f;
			this.particleBlue = 0.6f;
			GL11.glBlendFunc(770, 1);
			brightness = 0xF0060;
			break;
		}

		if (overrideColor != -1){
			this.particleRed = ((overrideColor >> 16) & 0xFF) / 255.0f;
			this.particleGreen = ((overrideColor >> 8) & 0xFF) / 255.0f;
			this.particleBlue = (overrideColor & 0xFF) / 255.0f;
		}

		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}
		tessellator.setBrightness(brightness);
		renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 2);
		try{
			tessellator.draw();
		}catch (Throwable t){
		}

		try{
			tessellator.startDrawingQuads();
		}catch (Throwable t){
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	public int getRenderPass(){
		return 2;
	}
}
