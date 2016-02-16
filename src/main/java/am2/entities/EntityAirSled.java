package am2.entities;

import am2.AMCore;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAirSled extends EntityLiving{

	private float rotation;

	public EntityAirSled(World par1World){
		super(par1World);
		this.setSize(0.5f, 1);
		this.stepHeight = 1.02f;
	}

	@Override
	public void onUpdate(){
		this.stepHeight = 1.02f;

		if (worldObj.isRemote){
			rotation += 1f;
			if (this.worldObj.isAirBlock(getPosition().down())){
				for (int i = 0; i < AMCore.config.getGFXLevel(); ++i){
					AMParticle cloud = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "sparkle2", posX, posY + 0.5, posZ);
					if (cloud != null){
						cloud.addRandomOffset(1, 1, 1);
						cloud.AddParticleController(new ParticleFadeOut(cloud, 1, false).setFadeSpeed(0.01f));
					}
				}
			}
		}
		super.onUpdate();
	}

	public float getRotation(){
		return rotation;
	}

	@Override
	public boolean shouldRiderSit(){
		return false;
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer){
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer){
			return true;
		}else{
			if (!this.worldObj.isRemote){
				if (par1EntityPlayer.isSneaking()){
					this.setDead();
					EntityItem item = new EntityItem(worldObj);
					item.setPosition(posX, posY, posZ);
					item.setEntityItemStack(ItemsCommonProxy.airSledEnchanted.copy());
					worldObj.spawnEntityInWorld(item);
				}else{
					par1EntityPlayer.mountEntity(this);
				}
			}

			return true;
		}
	}

	@Override
	public void updateRiderPosition(){
		if (this.riddenByEntity != null){
			double d0 = Math.cos(this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			double d1 = Math.sin(this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
		}
	}

	@Override
	public void moveEntityWithHeading(float par1, float par2){
		if (this.riddenByEntity != null){
			this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
			this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
			par1 = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
			par2 = ((EntityLivingBase)this.riddenByEntity).moveForward;

			if (par2 <= 0.0F){
				par2 *= 0.25F;
			}

			this.stepHeight = 1.0F;
			this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

			if (!this.worldObj.isRemote){
				par2 *= 0.06f;
				if (par1 != 0){
					float f4 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
					float f5 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
					this.motionX += (par1 * f5 - par2 * f4) * 0.06f;
					this.motionZ += (par2 * f5 + par1 * f4) * 0.06f;
				}

				this.motionX += -Math.sin(Math.toRadians(this.rotationYaw)) * par2;
				this.motionY += -Math.sin(Math.toRadians(this.rotationPitch)) * par2;
				this.motionZ += Math.cos(Math.toRadians(this.rotationYaw)) * par2;
			}
		}else{
			if (!this.onGround && !this.isInWater())
				this.motionY = -0.1f;
			else
				this.motionY = 0f;

			this.motionX *= 0.7f;
			this.motionZ *= 0.7f;

		}

		if (this.riddenByEntity != null)
			this.setSize(0.5f, 3);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if (this.riddenByEntity != null)
			this.setSize(0.5f, 1);

		float f2 = 0.91F;

		this.motionY *= f2;//0.9800000190734863D;
		this.motionX *= f2;
		this.motionZ *= f2;
	}

	@Override
	public double getMountedYOffset(){
		return 1.6f;
	}

	@Override
	protected void entityInit(){
		super.entityInit();
	}

	@Override
	public void playSound(String par1Str, float par2, float par3) {
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
	public boolean canBeCollidedWith(){
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity){
		return null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if (this.ridingEntity != null)
			this.ridingEntity.mountEntity(null);
		return false;
	}
}
