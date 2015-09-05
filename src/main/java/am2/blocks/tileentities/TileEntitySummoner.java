package am2.blocks.tileentities;

import am2.api.blocks.IKeystoneLockable;
import am2.api.power.PowerTypes;
import am2.damage.DamageSources;
import am2.items.ItemFocusCharge;
import am2.items.ItemFocusMana;
import am2.power.PowerNodeRegistry;
import am2.spell.SkillManager;
import am2.spell.components.Summon;
import am2.utility.DummyEntityPlayer;
import am2.utility.EntityUtilities;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntitySummoner extends TileEntityAMPower implements IInventory, IKeystoneLockable{

	private static final float summonCost = 2000;
	private static final float maintainCost = 7.5f;
	private ItemStack[] inventory;
	private int summonEntityID = -1;
	private DummyEntityPlayer dummyCaster;
	private int summonCooldown = 0;
	private int prevSummonCooldown = 0;
	private static final int maxSummonCooldown = 200;
	private static final int powerPadding = 500; //extra power to charge before summoning so that it can be maintained for a while

	private static final int SUMMON_SLOT = 3;

	public TileEntitySummoner(){
		super(2500);
		inventory = new ItemStack[getSizeInventory()];
	}

	private boolean isRedstonePowered(){
		return this.worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity(){
		super.updateEntity();

		prevSummonCooldown = summonCooldown;
		summonCooldown--;
		if (summonCooldown < 0) summonCooldown = 0;

		if (!worldObj.isRemote && summonCooldown == 0 && prevSummonCooldown > 0){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		if (!worldObj.isRemote){
			EntityLiving ent = getSummonedCreature();
			if (ent == null){
				summonEntityID = -1;
			}
			if (isRedstonePowered() && inventory[SUMMON_SLOT] != null){
				if (PowerNodeRegistry.For(this.worldObj).checkPower(this, maintainCost)){
					if (ent == null && canSummon()){
						summonCreature();
					}else{
						if (ent != null){
							PowerNodeRegistry.For(this.worldObj).consumePower(this, PowerNodeRegistry.For(this.worldObj).getHighestPowerType(this), maintainCost);
						}
					}
				}else{
					unsummonCreature();
				}
			}else{
				if (ent != null){
					unsummonCreature();
					PowerNodeRegistry.For(this.worldObj).insertPower(this, PowerTypes.NEUTRAL, summonCost / 2);
				}
			}
		}
	}

	public float getSummonCost(){
		int numManaFoci = numFociOfType(ItemFocusMana.class);
		return summonCost * (1.0f - 0.2f * numManaFoci);
	}

	public float getMaintainCost(){
		int numManaFoci = numFociOfType(ItemFocusMana.class);
		return maintainCost * (1.0f - 0.2f * numManaFoci);
	}

	public boolean canSummon(){
		if (this.worldObj == null)
			return false;
		return summonCooldown == 0 && PowerNodeRegistry.For(this.worldObj).checkPower(this, getSummonCost() + powerPadding);
	}

	public boolean hasSummon(){
		return this.summonEntityID != -1;
	}

	private void summonCreature(){
		if (worldObj.isRemote || this.summonEntityID != -1) return;
		if (dummyCaster == null){
			dummyCaster = new DummyEntityPlayer(worldObj);
		}
		EntityLiving summon = ((Summon)SkillManager.instance.getSkill("Summon")).summonCreature(inventory[SUMMON_SLOT], dummyCaster, dummyCaster, worldObj, xCoord, yCoord, zCoord);
		if (summon != null){
			if (summon instanceof EntityCreature)
				EntityUtilities.setGuardSpawnLocation((EntityCreature)summon, xCoord, yCoord, zCoord);
			this.summonEntityID = summon.getEntityId();
			PowerNodeRegistry.For(this.worldObj).consumePower(this, PowerNodeRegistry.For(this.worldObj).getHighestPowerType(this), summonCost);
			this.summonCooldown = this.maxSummonCooldown;
			EntityUtilities.setTileSpawned(summon, this);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	private void unsummonCreature(){
		if (worldObj.isRemote) return;
		EntityLiving ent = getSummonedCreature();
		if (ent == null) return;
		ent.attackEntityFrom(DamageSources.unsummon, 1000000);
		this.summonEntityID = -1;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	private EntityLiving getSummonedCreature(){
		if (this.summonEntityID == -1) return null;
		return (EntityLiving)worldObj.getEntityByID(this.summonEntityID);
	}

	@Override
	public int getSizeInventory(){
		return 7;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[4];
		runes[1] = inventory[5];
		runes[2] = inventory[6];
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
	public ItemStack getStackInSlot(int i){
		if (i < 0 || i >= getSizeInventory()) return null;
		return inventory[i];
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
	public ItemStack getStackInSlotOnClosing(int i){
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
	public String getInventoryName(){
		return "Summoner";
	}

	@Override
	public boolean hasCustomInventoryName(){
		return false;
	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this){
			return false;
		}
		return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openInventory(){
	}

	@Override
	public void closeInventory(){
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);

		NBTTagList nbttaglist = nbttagcompound.getTagList("SummonerInventory", Constants.NBT.TAG_COMPOUND);
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

		nbttagcompound.setTag("SummonerInventory", nbttaglist);
	}

	private int numFociOfType(Class type){
		int count = 0;
		for (int i = 0; i < 3; ++i){
			if (inventory[i] != null && type.isInstance(inventory[i].getItem())){
				count++;
			}
		}
		return count;
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.func_148857_g());
	}

	@Override
	public int getChargeRate(){
		int numChargeFoci = numFociOfType(ItemFocusCharge.class);
		return 100 + (50 * numChargeFoci);
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}
}
