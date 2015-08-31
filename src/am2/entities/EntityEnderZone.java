package am2.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import am2.buffs.BuffEffectEntangled;

public class EntityEnderZone extends Entity{

	public EntityEnderZone(World par1World) {
		super(par1World);
		setSize(1.5f, 1.5f);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void onUpdate() {
		if (this.ticksExisted++ >= 100 && !this.worldObj.isRemote)
			this.setDead();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
		par1EntityPlayer.addPotionEffect(new BuffEffectEntangled(60, 2));
	}

}
