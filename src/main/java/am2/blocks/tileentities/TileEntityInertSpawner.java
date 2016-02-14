package am2.blocks.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import am2.api.power.PowerTypes;
import am2.items.ItemCrystalPhylactery;
import am2.items.ItemsCommonProxy;
import am2.power.PowerNodeRegistry;

public class TileEntityInertSpawner extends TileEntityAMPower implements ISidedInventory{

	private ItemStack phylactery;
	private float powerConsumed = 0.0f;

	private static final PowerTypes[] valid = new PowerTypes[]{PowerTypes.DARK};

	private static final float SUMMON_REQ = 6000;

	public TileEntityInertSpawner(){
		super(500);
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 100;
	}

	@Override
	public int getSizeInventory(){
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		if (i < getSizeInventory() && phylactery != null){
			return phylactery;
		}

		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (i < getSizeInventory() && phylactery != null){
			ItemStack jar = phylactery;
			phylactery = null;
			return jar;
		}
		return null;
	}

	/*@Override
	public ItemStack getStackInSlotOnClosing(int i){
		if (i < getSizeInventory() && phylactery != null){
			ItemStack jar = phylactery;
			phylactery = null;
			return jar;
		}
		return null;
	}*/

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		phylactery = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
			itemstack.stackSize = getInventoryStackLimit();
		}

	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (worldObj.getTileEntity(pos) != this){
			return false;
		}

		return entityplayer.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack){
		return i == 0 && stack != null && stack.getItem() == ItemsCommonProxy.crystalPhylactery;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing facing){
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack stack, EnumFacing face){
		return
				i == 0 &&
						this.getStackInSlot(0) == null &&
						stack != null &&
						stack.getItem() == ItemsCommonProxy.crystalPhylactery &&
						stack.stackSize == 1 &&
						((ItemCrystalPhylactery)stack.getItem()).isFull(stack);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, EnumFacing p_102008_3_){
		return true;
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);

		if (phylactery != null){
			NBTTagCompound phy = new NBTTagCompound();
			phylactery.writeToNBT(phy);
			nbttagcompound.setTag("phylactery", phy);
		}

		nbttagcompound.setFloat("powerConsumed", powerConsumed);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);


		if (nbttagcompound.hasKey("phylactery")){
			NBTTagCompound phy = nbttagcompound.getCompoundTag("phylactery");
			phylactery = ItemStack.loadItemStackFromNBT(phy);
		}

		this.powerConsumed = nbttagcompound.getFloat("powerConsumed");
	}

	public void updateEntity(){
		super.worldObj.updateEntity((Entity) worldObj.playerEntities);

		if (!worldObj.isRemote && phylactery != null && ((ItemCrystalPhylactery)phylactery.getItem()).isFull(phylactery) && worldObj.isBlockIndirectlyGettingPowered(pos.add(pos.getX(), pos.getY(), pos.getZ())) == 0){
			if (this.powerConsumed < this.SUMMON_REQ){
				this.powerConsumed += PowerNodeRegistry.For(worldObj).consumePower(
						this,
						PowerTypes.DARK,
						Math.min(this.getCapacity(), this.SUMMON_REQ - this.powerConsumed)
				);
			}else{
				this.powerConsumed = 0;
				ItemCrystalPhylactery item = (ItemCrystalPhylactery)this.phylactery.getItem();
				if (item.isFull(phylactery)){
					String clazzName = item.getSpawnClass(phylactery);
					if (clazzName != null){
						Class clazz = (Class)EntityList.stringToClassMapping.get(clazzName);
						if (clazz != null){
							EntityLiving entity = null;
							try{
								entity = (EntityLiving)clazz.getConstructor(World.class).newInstance(worldObj);
							}catch (Throwable t){
								t.printStackTrace();
								return;
							}
							if (entity == null)
								return;
							setEntityPosition(entity);
							worldObj.spawnEntityInWorld(entity);
						}
					}
				}
			}
		}
	}

	private void setEntityPosition(EntityLiving e){
		for (EnumFacing dir : EnumFacing.values()){
			if (worldObj.isAirBlock(pos.add(pos.getX() + dir.getFrontOffsetX(), pos.getY() + dir.getFrontOffsetY(), pos.getZ() + dir.getFrontOffsetZ()))){
				e.setPosition(pos.getX() + dir.getFrontOffsetX(), pos.getY() + dir.getFrontOffsetY(), pos.getZ() + dir.getFrontOffsetZ());
				return;
			}
		}
		e.setPosition(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return valid;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack tmp = phylactery.copy();
		phylactery = null;
		return tmp;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Inert Spawner";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
}
