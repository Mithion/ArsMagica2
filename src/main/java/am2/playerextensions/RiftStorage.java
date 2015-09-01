package am2.playerextensions;

import am2.entities.EntityRiftStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants;

public class RiftStorage implements IExtendedEntityProperties, IInventory{

	private ItemStack[] inventory;
	private EntityRiftStorage accessEntity;
	
	public static final String identifier = "ArsMagicaVoidStorage";

	public static RiftStorage For(EntityPlayer player){
		return (RiftStorage) player.getExtendedProperties(identifier);
	}
	
	public int getAccessLevel(){
		if (accessEntity != null) return accessEntity.getStorageLevel();
		return -1;
	}
	
	public void setAccessEntity(EntityRiftStorage entity){
		this.accessEntity = entity;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] != null)
			{
				String tag = String.format("ArrayIndex", i);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte(tag, (byte)i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		compound.setTag("PlayerRiftStorage", nbttaglist);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagList nbttaglist = compound.getTagList("PlayerRiftStorage", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for(int i = 0; i < nbttaglist.tagCount(); i++)
		{
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte index = nbttagcompound1.getByte(tag);
			if(index >= 0 && index < inventory.length)
			{
				inventory[index] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void init(Entity entity, World world) {
		inventory = new ItemStack[getSizeInventory()];
	}

	@Override
	public int getSizeInventory() {
		return 54;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(inventory[i] != null){
			
			if(inventory[i].stackSize <= j){
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			
			ItemStack itemstack1 = inventory[i].splitStack(j);
			
			if(inventory[i].stackSize == 0){
				inventory[i] = null;
			}
			
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (inventory[i] != null){
			ItemStack itemstack = inventory[i];
			inventory[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "Void Storage";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (accessEntity == null || accessEntity.isDead) return false;
		return entityplayer.getDistanceSqToEntity(accessEntity) < 64;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public void markDirty() {
	}

}
