package am2.blocks.tileentities;

// import am2.api.blocks.IKeystoneLockable;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.power.PowerTypes;
import am2.armor.ArmorHelper;
import am2.multiblock.IMultiblockStructureController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

// public class TileEntityArmorImbuer extends TileEntityAMPower implements IInventory, IKeystoneLockable, IMultiblockStructureController{
public class TileEntityArmorImbuer extends TileEntityAMPower implements IInventory, IMultiblockStructureController{

	private ItemStack[] inventory;
	private MultiblockStructureDefinition def;
	private boolean creativeModeAllowed;

	public TileEntityArmorImbuer(){
		super(5);
		inventory = new ItemStack[getSizeInventory()];
	}

	private void setupMultiblock(){
		def = new MultiblockStructureDefinition("armorInfuser");

	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 10;
	}

	/*
	@Override
	public ItemStack[] getRunesInKey(){
		return new ItemStack[]{
				inventory[1],
				inventory[2],
				inventory[3]
		};
	}
	*/
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (worldObj.getTileEntity(pos) != this){
			return false;
		}
		return entityplayer.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, worldObj.getBlockState(pos).getBlock().getMetaFromState(worldObj.getBlockState(pos)), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	/*
	@Override
	public boolean keystoneMustBeHeld(){
		return false;
	}

	@Override
	public boolean keystoneMustBeInActionBar(){
		return false;
	}
	*/

	@Override
	public int getSizeInventory(){
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int slot){
		if (slot >= inventory.length)
			return null;
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (inventory[i] != null){
			if (inventory[i].stackSize <= j){
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0){
				inventory[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "ArmorInfuserInventory";
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("ArmorInfuserInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++){
			if (inventory[i] != null){
				String tag = String.format("ArrayIndex", i);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte(tag, (byte)i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("ArmorInfuserInventory", nbttaglist);
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public MultiblockStructureDefinition getDefinition(){
		return def;
	}

	public void imbueCurrentArmor(String string){
		ItemStack armorStack = inventory[0];
		ArmorHelper.imbueArmor(armorStack, string, this.creativeModeAllowed);
	}

	public boolean isCreativeAllowed(){
		return this.creativeModeAllowed;
	}

	public void setCreativeModeAllowed(boolean creative){
		this.creativeModeAllowed = creative;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (inventory[index] != null){
			ItemStack itemstack = inventory[index];
			inventory[index] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}

}
