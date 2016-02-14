package am2.blocks.tileentities;

import am2.api.blocks.IKeystoneLockable;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.blocks.BlocksCommonProxy;
import am2.spell.SpellUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class TileEntitySpellSealedDoor extends TileEntity implements IInventory, IKeystoneLockable, ITickable{

	private ItemStack[] inventory;

	private int lastAppliedTime = -1;
	private int closeTime = -1;
	private int curTime = 0;
	private int opentime = 40;

	private ArrayList<ISpellComponent> appliedParts;
	private ArrayList<ISpellComponent> key;

	public TileEntitySpellSealedDoor(){
		inventory = new ItemStack[getSizeInventory()];
		appliedParts = new ArrayList<ISpellComponent>();
		key = new ArrayList<ISpellComponent>();
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[0];
		runes[1] = inventory[1];
		runes[2] = inventory[2];
		return runes;
	}

	@Override
	public boolean keystoneMustBeHeld(){
		return false;
	}

	@Override
	public boolean keystoneMustBeInActionBar(){
		return false;
	}

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
	public ItemStack removeStackFromSlot(int i){
		if (inventory[i] != null){
			ItemStack itemstack = inventory[i];
			inventory[i] = null;
			return itemstack;
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
		return "Spell Sealed Door";
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
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public void openInventory(EntityPlayer p){
	}

	@Override
	public void closeInventory(EntityPlayer p){
		analyzeSpellForKey();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("SpellSealedDoorInventory", Constants.NBT.TAG_COMPOUND);
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

		nbttagcompound.setTag("SpellSealedDoorInventory", nbttaglist);
	}

	@Override
	public void update(){
		if (!worldObj.isRemote){
			curTime++;

			if (closeTime == -1 && lastAppliedTime != -1){
				if (curTime > lastAppliedTime + 10){
					clearAppliedParts();
					return;
				}
				if (checkKey()){
					clearAppliedParts();
					setOpenState(true);
					this.closeTime = curTime + opentime;
				}
			}

			if (closeTime != -1 && curTime > closeTime){
				clearAppliedParts();
				setOpenState(false);
				closeTime = -1;
			}
		}
	}

	private void setOpenState(boolean open){
		BlocksCommonProxy.spellSealedDoor.setDoorState(worldObj, pos, null, open);
	}

	public void addPartToCurrentKey(ISpellComponent component){
		this.appliedParts.add(component);
		this.lastAppliedTime = curTime;
	}

	private boolean checkKey(){
		if (key.size() != appliedParts.size()) return false;
		if (key.equals(appliedParts)) return true;
		return false;
	}

	private void clearAppliedParts(){
		appliedParts.clear();
		lastAppliedTime = -1;
	}

	public void analyzeSpellForKey(){
		ItemStack spell = this.inventory[3];

		if (spell == null) return;

		//if we're here, we have a spell to analyze!
		key.clear();
		int stages = SpellUtils.instance.numStages(spell);

		for (int i = 0; i < stages; ++i){
			ISpellComponent[] components = SpellUtils.instance.getComponentsForStage(spell, i);
			for (ISpellComponent comp : components){
				key.add(comp);
			}
		}

	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
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
}
