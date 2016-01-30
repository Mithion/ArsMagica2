package am2.entities;

import am2.AMCore;
import am2.guis.ArsMagicaGuiIdList;
import am2.playerextensions.RiftStorage;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityRiftStorage extends EntityLiving{

	private static final int STORAGE_LEVEL_ID = 25;
	private static final int LIVE_TICKS_ID = 26;
	private int ticksExisted = 0;

	private float rotation = 0f;
	private float scale = 0.0f;
	private float scale2 = 1.0f;

	public EntityRiftStorage(World par1World){
		super(par1World);
		this.setSize(1.5f, 1.5f);
	}

	@Override
	public void onUpdate(){
		if (this.ticksExisted++ >= getTicksToLive()) this.setDead();
		this.rotation += (Math.sin((float)this.ticksExisted / 100) + 0.5f);

		if (getTicksToLive() - this.ticksExisted <= 20){
			this.scale -= 1f / 20f;
		}else if (this.scale < 0.99f){
			this.scale = (float)(Math.sin((float)this.ticksExisted / 50));
		}


		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		super.onUpdate();
	}

	@Override
	public boolean isEntityInvulnerable(){
		return true;
	}

	private int getTicksToLive(){
		return this.dataWatcher.getWatchableObjectInt(LIVE_TICKS_ID);
	}

	private void setTicksToLive(int ticks){
		this.dataWatcher.updateObject(LIVE_TICKS_ID, ticks);
	}

	public float getScale(int type){
		switch (type){
		case 0:
			return this.scale;
		case 1:
		default:
			return this.scale2;
		}
	}

	public float getRotation(){
		return this.rotation;
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(STORAGE_LEVEL_ID, 0);
		this.dataWatcher.addObject(LIVE_TICKS_ID, 1200);
	}

	public void setStorageLevel(int newStorageLevel){
		this.dataWatcher.updateObject(STORAGE_LEVEL_ID, newStorageLevel);
	}

	public int getStorageLevel(){
		return this.dataWatcher.getWatchableObjectInt(STORAGE_LEVEL_ID);
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer){
		if (par1EntityPlayer.isSneaking()){
			this.setTicksToLive(this.ticksExisted + 20);
			return true;
		}
		RiftStorage.For(par1EntityPlayer).setAccessEntity(this);
		FMLNetworkHandler.openGui(par1EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_RIFT, par1EntityPlayer.worldObj, (int)par1EntityPlayer.posX, (int)par1EntityPlayer.posY, (int)par1EntityPlayer.posZ);
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		return false;
	}

}
