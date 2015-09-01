package am2.blocks.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import am2.api.blocks.IKeystoneLockable;
import am2.blocks.BlockKeystoneChest;

public class TileEntityKeystoneChest extends TileEntity implements IInventory, IKeystoneLockable {

	private ItemStack[] inventory;
	public static final int keystoneSlot = 27;
	private float prevLidAngle = 0f;
	private float lidAngle = 0f;
	private int numPlayersUsing = 0;

	private static final float lidIncrement = 0.1f;

	public TileEntityKeystoneChest(){
		inventory = new ItemStack[getSizeInventory()];
	}

	@Override
	public int getSizeInventory() {
		return 30;
	}

	@Override
	public void updateEntity() {
		setPrevLidAngle(getLidAngle());
		if (numPlayersUsing > 0){
			if (getLidAngle() == 0){
				this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (getLidAngle() < 1.0f){
				setLidAngle(getLidAngle() + lidIncrement);
			}else{
				setLidAngle(1.0f);
			}
		}else{
			if (getLidAngle() == 1.0f){
				this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (getLidAngle() - lidIncrement > 0f){
				setLidAngle(getLidAngle() - lidIncrement);
			}else{
				setLidAngle(0f);
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numPlayersUsing = par2;
            return true;
        }
        else
        {
            return super.receiveClientEvent(par1, par2);
        }
    }

	@Override
	public void openInventory()
    {
        if (this.numPlayersUsing < 0)
        {
            this.numPlayersUsing = 0;
        }

        ++this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
    }

	@Override
    public void closeInventory()
    {
        if (this.getBlockType() != null && this.getBlockType() instanceof BlockKeystoneChest)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        }
    }

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot >= inventory.length)
			return null;
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(inventory[i] != null)
		{
			if(inventory[i].stackSize <= j)
			{
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if(inventory[i].stackSize == 0)
			{
				inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (inventory[i] != null)
		{
			ItemStack itemstack = inventory[i];
			inventory[i] = null;
			return itemstack;
		}
		else
		{
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
		return "KeystoneChestInventory";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}

		return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("KeystoneChestInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for(int i = 0; i < nbttaglist.tagCount(); i++)
		{
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if(byte0 >= 0 && byte0 < inventory.length)
			{
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
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

		nbttagcompound.setTag("KeystoneChestInventory", nbttaglist);
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	public float getPrevLidAngle() {
		return prevLidAngle;
	}

	public void setPrevLidAngle(float prevLidAngle) {
		this.prevLidAngle = prevLidAngle;
	}

	public float getLidAngle() {
		return lidAngle;
	}

	public void setLidAngle(float lidAngle) {
		this.lidAngle = lidAngle;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public ItemStack[] getRunesInKey() {
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[27];
		runes[1] = inventory[28];
		runes[2] = inventory[29];
		return runes;
	}

	@Override
	public boolean keystoneMustBeHeld() {
		return false;
	}

	@Override
	public boolean keystoneMustBeInActionBar() {
		return false;
	}
}
