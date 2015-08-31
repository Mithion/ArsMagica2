package am2.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import am2.AMCore;

public class EntityShockwave extends Entity{

	private float movingSpeed;
	private float moveAngle;

	public EntityShockwave(World par1World) {
		super(par1World);
		this.setSize(3.0f, 0.2f);
	}	

	public void setMoveSpeedAndAngle(float moveSpeed, float angle){
		this.movingSpeed = moveSpeed;
		this.moveAngle = (float) Math.toRadians(angle);
	}

	@Override
	public void onUpdate() {

		this.ticksExisted++;

		if (this.ticksExisted >= 60)
			this.setDead();

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		int j = MathHelper.floor_double(this.posX);
		int i = MathHelper.floor_double(this.posY - 0.20000000298023224D - this.yOffset);
		int k = MathHelper.floor_double(this.posZ);
		Block l = this.worldObj.getBlock(j, i, k);
		if (l != Blocks.air)
			for (int h = 0; h < 5 * AMCore.config.getGFXLevel(); ++h)
				this.worldObj.spawnParticle("tilecrack_" + l + "_" + this.worldObj.getBlockMetadata(j, i, k), this.posX + (this.rand.nextFloat() - 0.5D) * this.width, this.boundingBox.minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);		

		double deltaX = Math.cos(moveAngle) * movingSpeed;
		double deltaZ = Math.sin(moveAngle) * movingSpeed;

		this.moveEntity(deltaX, 0, deltaZ);		
	}

	@Override
	public void applyEntityCollision(Entity par1Entity)
	{
		par1Entity.attackEntityFrom(DamageSource.magic, 2);
		super.applyEntityCollision(par1Entity);
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
		par1EntityPlayer.attackEntityFrom(DamageSource.generic, 2);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}



}
