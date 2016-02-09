package am2.particles;

import am2.AMCore;
import am2.api.particles.IBeamParticle;
import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AMBeam extends EntityFX implements IBeamParticle{

	int type;
	double dX, dY, dZ;

	double updateX, updateY, updateZ;

	private float yaw, pitch;
	private float prevYaw, prevPitch;

	private float rotateSpeed = 5f;
	private final double offset = 0d;

	private float length;
	private final float endMod = 1.0F;
	private int maxLengthAge = 10;

	private boolean positionChanged = false;

	private static final ResourceLocation beam1 = new ResourceLocation("arsmagica2", ResourceManager.GetFXTexturePath("beam.png"));
	private static final ResourceLocation beam2 = new ResourceLocation("arsmagica2", ResourceManager.GetFXTexturePath("beam1.png"));
	private static final ResourceLocation beam3 = new ResourceLocation("arsmagica2", ResourceManager.GetFXTexturePath("beam2.png"));
	private static final ResourceLocation oldLoc = new ResourceLocation("textures/particle/particles.png");

	private boolean fppc = false; //first person player cast

	public AMBeam(World world, double x, double y, double z, double destX, double destY, double destZ){
		this(world, x, y, z, destX, destY, destZ, 0);
	}

	public AMBeam(World world, double x, double y, double z, double destX, double destY, double destZ, int color){
		super(world, x, y, z);
		this.type = 0;
		this.dX = destX;
		this.dY = destY;
		this.dZ = destZ;

		this.motionX = 0D;
		this.motionY = 0D;
		this.motionZ = 0D;

		setSize(0.2f, 0.2f);

		calculateLengthAndRotation();
		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;

		rotateSpeed = 30;
		maxLengthAge = 10;

		this.particleMaxAge = 10;
	}

	/**
	 * Sets the beam to be instantly full size instead of playing the grow
	 * animation.
	 *
	 * @return
	 */
	public AMBeam setInstantSpawn(){
		this.maxLengthAge = 1;
		return this;
	}

	public void setMaxAge(int maxAge){
		this.particleMaxAge = maxAge;
	}

	private void calculateLengthAndRotation(){
		float deltaX = (float)(this.posX - this.dX);
		float deltaY = (float)(this.posY - this.dY);
		float deltaZ = (float)(this.posZ - this.dZ);

		this.length = MathHelper.sqrt_float(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
		double hDist = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		this.yaw = ((float)(Math.atan2(deltaX, deltaZ) * 180.0D / 3.141592653589793D));
		this.pitch = ((float)(Math.atan2(deltaY, hDist) * 180.0D / 3.141592653589793D));

	}

	public void setBeamLocationAndTarget(double posX, double posY, double posZ, double targetX, double targetY, double targetZ){
		this.updateX = posX;
		this.updateY = posY;
		this.updateZ = posZ;
		this.dX = targetX;
		this.dY = targetY;
		this.dZ = targetZ;

		if (this.particleAge > this.particleMaxAge - 5){
			this.particleMaxAge = this.particleAge + 5;
		}

		positionChanged = true;
	}

	private void storePrevInformation(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.positionChanged){
			this.posX = this.updateX;
			this.posY = this.updateY;
			this.posZ = this.updateZ;
			if (this.fppc){
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				if (player != null){
					float yaw = player.rotationYaw;
					float rotationYaw = (float)(yaw * Math.PI / 180);
					float offsetX = (float)Math.cos(rotationYaw) * 0.06f;
					float offsetZ = (float)Math.sin(rotationYaw) * 0.06f;
					this.posX -= offsetX;
					this.posZ -= offsetZ;
					this.posY += 0.06f;
					this.prevPosX = this.posX;
					this.prevPosY = this.posY;
					this.prevPosZ = this.posZ;
				}
			}
			this.positionChanged = false;
		}

		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;
	}

	@Override
	public void setFirstPersonPlayerCast(){
		this.fppc = true;
	}

	private void handleAging(){
		this.particleAge++;
		if (this.particleAge >= this.particleMaxAge){
			this.setDead();
		}
	}

	@Override
	public void setType(int type){
		this.type = type;
	}

	@Override
	public void onUpdate(){
		storePrevInformation();
		calculateLengthAndRotation();
		handleAging();
	}

	@Override
	public int getFXLayer(){
		return 2;
	}

	@Override
	public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7){

		tessellator.draw();

		GL11.glPushMatrix();
		float scaleFactor = 1.0F;
		float slide = this.worldObj.getTotalWorldTime();
		float rot = this.worldObj.provider.getWorldTime() % (360 / this.rotateSpeed) * this.rotateSpeed + this.rotateSpeed * par2;

		float size = (float)this.particleAge / (float)this.maxLengthAge;
		if (size > 1) size = 1;

		float op = 0.4F;
		float widthMod = 1.0f;

		IIcon beamIcon = null;

		switch (this.type){
		default:
			beamIcon = AMParticleIcons.instance.getHiddenIconByName("beam");
			break;
		case 1:
			beamIcon = AMParticleIcons.instance.getHiddenIconByName("beam1");
			break;
		case 2:
			beamIcon = AMParticleIcons.instance.getHiddenIconByName("beam2");
		}

		GL11.glTexParameterf(3553, 10242, 10497.0F);
		GL11.glTexParameterf(3553, 10243, 10497.0F);

		float var11 = slide + par2;
		float var12 = -var11 * 0.2F - MathHelper.floor_float(-var11 * 0.1F);

		float xx = (float)(this.prevPosX + (this.posX - this.prevPosX) * par2 - interpPosX);
		float yy = (float)(this.prevPosY + (this.posY - this.prevPosY) * par2 - interpPosY);
		float zz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * par2 - interpPosZ);
		GL11.glTranslated(xx, yy, zz);

		if (fppc){
			widthMod = 0.3f;
		}

		float deltaYaw = Math.abs(this.yaw) - Math.abs(this.prevYaw);

		float ry = this.prevYaw + (deltaYaw) * par2;
		float rp = this.prevPitch + (this.pitch - this.prevPitch) * par2;
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
		GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);

		double offset1 = (-0.15D * widthMod) * size;
		double offset2 = (0.15D * widthMod) * size;
		double offset3 = (-0.15D * widthMod) * size * this.endMod;
		double offset4 = (0.15D * widthMod) * size * this.endMod;

		GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
		int i = 5;
		float inc = 36.0F;
		if (AMCore.config.LowGFX()){
			i = 3;
			inc = 90;
		}else if (AMCore.config.NoGFX()){
			i = 1;
			inc = 180;
		}
		for (int t = 0; t < i; t++){
			double l = this.length * size * scaleFactor;
			double tl = beamIcon.getMinU();
			double br = beamIcon.getMaxU();
			double mU = beamIcon.getMinV(); //-1.0F + var12 + t / 3.0F;
			double mV = beamIcon.getMaxV(); //this.length * size * scaleFactor + mU;

			GL11.glRotatef(36.0F, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.setBrightness(200);
			if (t % 2 == 0){
				tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, op);
			}else{
				tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.4f);
			}
			tessellator.addVertexWithUV(offset3, l, 0.0D, br, mV);
			tessellator.addVertexWithUV(offset1, 0.0D, 0.0D, br, mU);
			tessellator.addVertexWithUV(offset2, 0.0D, 0.0D, tl, mU);
			tessellator.addVertexWithUV(offset4, l, 0.0D, tl, mV);
			tessellator.draw();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glPopMatrix();


		tessellator.startDrawingQuads();
	}

	@Override
	public void setRGBColorF(float r, float g, float b){
		this.particleGreen = g;
		this.particleRed = r;
		this.particleBlue = b;
	}

	@Override
	public void setRGBColor(int color){
		this.particleRed = ((color >> 16) & 0xFF) / 255.0F;
		this.particleGreen = ((color >> 8) & 0xFF) / 255.0F;
		this.particleBlue = (color & 0xFF) / 255.0F;
	}

	@Override
	public void setRGBColorI(int r, int g, int b){
		this.particleRed = r / 255.0f;
		this.particleGreen = g / 255.0f;
		this.particleBlue = b / 255.0f;
	}

}
