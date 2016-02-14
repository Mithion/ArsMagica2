package am2.blocks.tileentities;

import am2.api.power.PowerTypes;
import am2.blocks.BlocksCommonProxy;
import am2.power.PowerNodeRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityManaBattery extends TileEntityAMPower{

	private boolean active;
	public static int storageCapacity;
	private PowerTypes outputPowerType = PowerTypes.NONE;
	private int tickCounter = 0;

	public TileEntityManaBattery(){
		super(250000);
		this.storageCapacity = 250000;
		active = false;
	}

	public PowerTypes getPowerType(){
		return outputPowerType;
	}

	public void setPowerType(PowerTypes type, boolean forceSubNodes){
		this.outputPowerType = type;
		if (worldObj != null && worldObj.isRemote)
			worldObj.markBlockForUpdate(pos);
	}

	public void setActive(boolean active){
		this.active = active;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return true;
	}

	@Override
	public void update(){
		if (worldObj.isBlockIndirectlyGettingPowered(pos) > 0){
			this.setPowerRequests();
		}else{
			this.setNoPowerRequests();
		}

		if (this.outputPowerType == PowerTypes.NONE && !this.worldObj.isRemote){
			PowerTypes highest = PowerNodeRegistry.For(worldObj).getHighestPowerType(this);
			float amt = PowerNodeRegistry.For(worldObj).getPower(this, highest);
			if (amt > 0){
				this.outputPowerType = highest;
				worldObj.markBlockForUpdate(pos);
			}
		}

		tickCounter++;
		if (tickCounter % 600 == 0){
			worldObj.notifyBlockOfStateChange(pos, BlocksCommonProxy.manaBattery);
			tickCounter = 0;
		}

		super.update();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("isActive", active);
		nbttagcompound.setInteger("outputType", outputPowerType.ID());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		active = nbttagcompound.getBoolean("isActive");
		if (nbttagcompound.hasKey("outputType"))
			outputPowerType = PowerTypes.getByID(nbttagcompound.getInteger("outputType"));
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public int getChargeRate(){
		return 1000;
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		if (this.outputPowerType == PowerTypes.NONE)
			return PowerTypes.all();
		return new PowerTypes[]{this.outputPowerType};
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}
}
