package am2.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import am2.AMCore;
import am2.api.math.AMVector3;

@SideOnly(Side.CLIENT)
public class AMLineArc extends EntityFX{

	AMVector3 targetPoint;
	AMVector3 currentTargetPoint;
	AMVector3 sourcePoint;
	Entity targetEntity;
	Entity sourceEntity;

	boolean hadSource = false;
	boolean hadTarget = false;
	boolean ignoreAge = true;

	double deviation;
	float speed;
	float width;
	boolean extendToTarget;
	float extensionProgress;
	ResourceLocation rl;
	float forwardFactor;

	public AMLineArc(World world, double x, double y, double z, double targetX, double targetY, double targetZ, String IIconName) {
		super(world, x, y, z);
		targetPoint = new AMVector3(targetX, targetY, targetZ);
		currentTargetPoint = targetPoint.copy();
		sourcePoint = new AMVector3(x, y, z);
		deviation = 1.0;
		speed = 0.01f;
		width = 0.05f;
		rl = new ResourceLocation("arsmagica2", IIconName);
		this.particleMaxAge = 100;
		forwardFactor = 0;
	}

	public AMLineArc(World world, double x, double y, double z, Entity targetEntity, String IIconName) {
		this(world, x, y, z, targetEntity.posX, targetEntity.posY + targetEntity.getEyeHeight() - (targetEntity.height * 0.2f), targetEntity.posZ, IIconName);
		this.targetEntity = targetEntity;

		hadTarget = true;
	}

	public AMLineArc(World world, Entity sourceEntity, Entity targetEntity, String IIconName){
		this(world, sourceEntity.posX, sourceEntity.posY + sourceEntity.getEyeHeight()- (sourceEntity.height * 0.2f), sourceEntity.posZ, targetEntity.posX, targetEntity.posY + targetEntity.getEyeHeight() - (targetEntity.height * 0.2f), targetEntity.posZ, IIconName);
		this.targetEntity = targetEntity;
		this.sourceEntity = sourceEntity;

		hadSource = true;
		hadTarget = true;
	}

	public AMLineArc setExtendToTarget(){
		extendToTarget = true;
		return this;
	}

	public AMLineArc setIgnoreAge(boolean ignore){
		ignoreAge = ignore;
		return this;
	}

	@Override
	public void onUpdate() {
		this.ticksExisted++;

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (this.ticksExisted >= this.particleMaxAge){
			this.setDead();
			return;
		}

		if (targetEntity != null){
			if (ignoreAge)
				this.ticksExisted = 0;
			if (targetEntity.isDead){
				this.setDead();
				return;
			}
			targetPoint = new AMVector3(targetEntity.posX, targetEntity.posY + targetEntity.getEyeHeight() - (targetEntity.height * 0.2f), targetEntity.posZ);
			currentTargetPoint = targetPoint.copy();
		}else if (hadTarget){
			this.setDead();
			return;
		}

		if (sourceEntity != null){
			if (ignoreAge)
				this.ticksExisted = 0;
			if (sourceEntity.isDead){
				this.setDead();
				return;
			}
			sourcePoint = new AMVector3(sourceEntity.posX, sourceEntity.posY + sourceEntity.getEyeHeight() - (sourceEntity.height * 0.2f), sourceEntity.posZ);
		}else if (hadSource){
			this.setDead();
			return;
		}


		if (extendToTarget && extensionProgress < 1.0f){
			extensionProgress += 0.08;
			AMVector3 delta = targetPoint.copy().sub(sourcePoint);
			delta.scale(extensionProgress);
			currentTargetPoint = delta.add(sourcePoint);
		}
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float partialTicks, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
		if (targetEntity != null && sourceEntity != null){
			drawArcingLine(
					sourceEntity.prevPosX + (sourceEntity.posX - sourceEntity.prevPosX) * partialTicks,
					(sourceEntity.prevPosY + (sourceEntity.posY - sourceEntity.prevPosY) * partialTicks) + (sourceEntity.getEyeHeight() - (sourceEntity.height * 0.2f)),
					sourceEntity.prevPosZ + (sourceEntity.posZ - sourceEntity.prevPosZ) * partialTicks,
					targetEntity.prevPosX + (targetEntity.posX - targetEntity.prevPosX) * partialTicks,
					(targetEntity.prevPosY + (targetEntity.posY - targetEntity.prevPosY) * partialTicks) + (targetEntity.getEyeHeight() - (targetEntity.height * 0.2f)),
					targetEntity.prevPosZ + (targetEntity.posZ - targetEntity.prevPosZ) * partialTicks,
					partialTicks, speed, deviation);
		}else if (targetEntity != null){
			drawArcingLine(
					prevPosX + (posX - prevPosX) * partialTicks,
					prevPosY + (posY - prevPosY) * partialTicks,
					prevPosZ + (posZ - prevPosZ) * partialTicks,
					targetEntity.prevPosX + (targetEntity.posX - targetEntity.prevPosX) * partialTicks,
					(targetEntity.prevPosY + (targetEntity.posY - targetEntity.prevPosY) * partialTicks) + (targetEntity.getEyeHeight() - (targetEntity.height * 0.2f)),
					targetEntity.prevPosZ + (targetEntity.posZ - targetEntity.prevPosZ) * partialTicks,
					partialTicks, speed, deviation);
		}else{
			drawArcingLine(prevPosX + (posX - prevPosX) * partialTicks, prevPosY + (posY - prevPosY) * partialTicks, prevPosZ + (posZ - prevPosZ) * partialTicks, currentTargetPoint.x, currentTargetPoint.y, currentTargetPoint.z, partialTicks, speed, deviation);
		}
	}

	public void drawArcingLine(double srcX, double srcY, double srcZ, double dstX, double dstY, double dstZ, float partialTicks, float speed, double distance)
	{

		GL11.glPushMatrix();

		Minecraft.getMinecraft().renderEngine.bindTexture(rl);

		int fxQuality = AMCore.config.getGFXLevel() * 8;

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		double interpolatedX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double interpolatedY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double interpolatedZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		Tessellator tessellator = Tessellator.instance;

		double deltaX = srcX - dstX;
		double deltaY = srcY - dstY;
		double deltaZ = srcZ - dstZ;

		float time = System.nanoTime() / 10000000L;

		float dist = MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
		float blocks = Math.round(dist);
		float length = blocks * (fxQuality / 2.0F);

		float vMin = 0.0F;
		float VMax = 1.0F;

		GL11.glTranslated(-interpolatedX + dstX, -interpolatedY + dstY, -interpolatedZ + dstZ);

		tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);

		double wGain = (width * 3) / (length * distance);
		float curWidth = width * 3;

		for (int i = 0; i <= length * distance; i++)
		{
			float lengthFactor = i / length;
			float f3 = 1.0F - Math.abs(i - length / 2.0F) / (length / 2.0F);

			//ZXY
			double dx = deltaX + MathHelper.sin((float)((srcX % 16.0D + dist * (1.0F - lengthFactor) * fxQuality / 2.0F - time % 32767.0F / 5.0F) / 4.0D)) * 0.5F * f3;
			double dy = deltaY + MathHelper.sin((float)((srcY % 16.0D + dist * (1.0F - lengthFactor) * fxQuality / 2.0F - time % 32767.0F / 5.0F) / 3.0D)) * 0.5F * f3;
			double dz = deltaZ + MathHelper.sin((float)((srcZ % 16.0D + dist * (1.0F - lengthFactor) * fxQuality / 2.0F - time % 32767.0F / 5.0F) / 2.0D)) * 0.5F * f3;

			tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);

			float u = (1.0F - lengthFactor) * dist - time * speed;

			tessellator.addVertexWithUV(dx * lengthFactor - curWidth, dy * lengthFactor, dz * lengthFactor, u, 1.0f);
			tessellator.addVertexWithUV(dx * lengthFactor + curWidth, dy * lengthFactor, dz * lengthFactor, u, 0.0f);

			curWidth -= wGain;
		}
		tessellator.draw();

		forwardFactor = (forwardFactor + 0.01f) % 1.0f;
		GL11.glPopMatrix();
	}
}
