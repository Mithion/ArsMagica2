package am2.blocks.tileentities;

import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.power.PowerNodeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public abstract class TileEntityAMPower extends TileEntity implements IPowerNode, ITickable{
	protected int capacity;
	private boolean canRequestPower = true;
	private int tickCounter;

	private static final int REQUEST_INTERVAL = 20;

	public TileEntityAMPower(int capacity){
		this.capacity = capacity;
	}

	protected void setNoPowerRequests(){
		canRequestPower = false;
	}

	protected void setPowerRequests(){
		canRequestPower = true;
	}

	/***
	 * Whether or not the tile entity is *capable* of providing power.
	 */
	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	@Override
	public void invalidate(){
		PowerNodeRegistry.For(this.worldObj).removePowerNode(this);
		super.invalidate();
	}

	@Override
	public void update(){
		if (!worldObj.isRemote && this.canRequestPower() && tickCounter++ >= getRequestInterval()){
			tickCounter = 0;
			PowerTypes[] powerTypes = this.getValidPowerTypes();
			for (PowerTypes type : powerTypes){
				float amtObtained = PowerNodeRegistry.For(worldObj).requestPower(this, type, this.getChargeRate());
				if (amtObtained > 0)
					PowerNodeRegistry.For(worldObj).insertPower(this, type, amtObtained);
			}
		}
	}
	
	@Override
	public float particleOffset(EnumFacing.Axis axis) {
		return 0.5F;
	}

	public int getRequestInterval(){
		return REQUEST_INTERVAL;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void setWorldObj(World par1World){
		super.setWorldObj(par1World);
		PowerNodeRegistry.For(this.worldObj).registerPowerNode(this);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
	}

	@Override
	public float getCapacity(){
		return this.capacity;
	}

	public void setPower(PowerTypes type, float amount){
		PowerNodeRegistry.For(this.worldObj).setPower(this, type, amount);
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return PowerTypes.all();
	}

	@Override
	public boolean canRequestPower(){
		return true;
	}

	@Override
	public boolean isSource(){
		return false;
	}
}
