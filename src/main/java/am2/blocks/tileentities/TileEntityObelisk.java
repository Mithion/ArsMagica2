package am2.blocks.tileentities;

import am2.ObeliskFuelHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.blocks.MultiblockStructureDefinition.StructureGroup;
import am2.api.power.PowerTypes;
import am2.blocks.BlocksCommonProxy;
import am2.buffs.BuffEffectManaRegen;
import am2.buffs.BuffList;
import am2.multiblock.IMultiblockStructureController;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.power.PowerNodeRegistry;
import am2.utility.InventoryUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityObelisk extends TileEntityAMPower implements IMultiblockStructureController, IInventory{
	protected static int pillarBlockID = 98; //stone brick
	protected static int pillarBlockMeta = 3; //arcane texture
	protected int surroundingCheckTicks;
	private ItemStack[] inventory;
	protected float powerMultiplier = 1f;
	protected float powerBase = 5.0f;

	public float offsetY = 0;
	public float lastOffsetY = 0;

	public int burnTimeRemaining = 0;
	public int maxBurnTime = 1;

	private static final byte PK_BURNTIME_CHANGE = 1;

	protected MultiblockStructureDefinition structure;
	protected StructureGroup wizardChalkCircle;
	protected StructureGroup pillars;
	protected HashMap<StructureGroup, Float> caps;

	private static final int GROUP_CHISELED_STONE = 0;

	public TileEntityObelisk(){
		this(5000);
		inventory = new ItemStack[this.getSizeInventory()];
	}

	protected void checkNearbyBlockState(){
		ArrayList<StructureGroup> groups = structure.getMatchedGroups(7, worldObj, xCoord, yCoord, zCoord);

		float capsLevel = 1;
		boolean pillarsFound = false;
		boolean wizChalkFound = false;

		for (StructureGroup group : groups){
			if (group == pillars)
				pillarsFound = true;
			else if (group == wizardChalkCircle)
				wizChalkFound = true;

			for (StructureGroup cap : caps.keySet()){
				if (group == cap){
					capsLevel = caps.get(cap);
					break;
				}
			}
		}

		powerMultiplier = 1;

		if (wizChalkFound)
			powerMultiplier = 1.25f;

		if (pillarsFound)
			powerMultiplier *= capsLevel;
	}

	public TileEntityObelisk(int capacity){
		super(capacity);
		setNoPowerRequests();
		surroundingCheckTicks = 0;

		structure = new MultiblockStructureDefinition("obelisk_structure");

		pillars = structure.createGroup("pillars", 2);
		caps = new HashMap<StructureGroup, Float>();
		StructureGroup chiseled = structure.createGroup("caps_chiseled_stone", 4);
		caps.put(chiseled, 1.35f);

		structure.addAllowedBlock(0, 0, 0, BlocksCommonProxy.obelisk);

		structure.addAllowedBlock(pillars, -2, 0, -2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(pillars, -2, 1, -2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(chiseled, -2, 2, -2, Blocks.stonebrick, 3);

		structure.addAllowedBlock(pillars, 2, 0, -2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(pillars, 2, 1, -2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(chiseled, 2, 2, -2, Blocks.stonebrick, 3);

		structure.addAllowedBlock(pillars, -2, 0, 2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(pillars, -2, 1, 2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(chiseled, -2, 2, 2, Blocks.stonebrick, 3);

		structure.addAllowedBlock(pillars, 2, 0, 2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(pillars, 2, 1, 2, Blocks.stonebrick, 0);
		structure.addAllowedBlock(chiseled, 2, 2, 2, Blocks.stonebrick, 3);

		wizardChalkCircle = addWizChalkGroupToStructure(structure, 1);
	}

	public boolean isActive(){
		return burnTimeRemaining > 0 || inventory[0] != null;
	}

	public boolean isHighPowerActive(){
		return burnTimeRemaining > 200 && inventory[0] != null;
	}

	public int getCookProgressScaled(int par1){
		return burnTimeRemaining * par1 / maxBurnTime;
	}

	protected StructureGroup addWizChalkGroupToStructure(MultiblockStructureDefinition def, int mutex){
		StructureGroup group = def.createGroup("wizardChalkCircle", mutex);

		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				if (i == 0 && j == 0) continue;
				def.addAllowedBlock(group, i, 0, j, BlocksCommonProxy.wizardChalk);
			}
		}

		return group;
	}

	protected void callSuperUpdate(){
		super.updateEntity();
	}

	private void setMaxBurnTime(int burnTime){
		if (burnTime == 0)
			burnTime = 1;
		maxBurnTime = burnTime;
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
		this.readFromNBT(pkt.getNbtCompound());
	}

	private void sendCookUpdateToClients(){
		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendObeliskUpdate(this, new AMDataWriter().add(PK_BURNTIME_CHANGE).add(this.burnTimeRemaining).generate());
		}
	}

	public void handlePacket(byte[] data){
		AMDataReader rdr = new AMDataReader(data);
		if (rdr.ID == this.PK_BURNTIME_CHANGE)
			this.burnTimeRemaining = rdr.getInt();
	}

	@Override
	public void updateEntity(){
		surroundingCheckTicks++;

		if (isActive()){
			if (surroundingCheckTicks % 100 == 0){
				checkNearbyBlockState();
				surroundingCheckTicks = 1;
				if (!worldObj.isRemote && PowerNodeRegistry.For(this.worldObj).checkPower(this, this.capacity * 0.1f)){
					List<EntityPlayer> nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord - 2, this.yCoord, this.zCoord - 2, this.xCoord + 2, this.yCoord + 3, this.zCoord + 2));
					for (EntityPlayer p : nearbyPlayers){
						if (p.isPotionActive(BuffList.manaRegen.id)) continue;
						p.addPotionEffect(new BuffEffectManaRegen(600, 2));
					}
				}
			}

			float powerAmt = PowerNodeRegistry.For(worldObj).getPower(this, PowerTypes.NEUTRAL);
			float powerAdded = inventory[0] != null ? ObeliskFuelHelper.instance.getFuelBurnTime(inventory[0]) * (powerBase * powerMultiplier) : 0;

			float chargeThreshold = Math.max(this.getCapacity() - powerAdded, this.getCapacity() * 0.75f);

			if (burnTimeRemaining <= 0 && powerAmt < chargeThreshold){
				burnTimeRemaining = ObeliskFuelHelper.instance.getFuelBurnTime(inventory[0]);
				if (burnTimeRemaining > 0){
					setMaxBurnTime(burnTimeRemaining);

					if (this.inventory[0].getItem().hasContainerItem(this.inventory[0]))
						this.inventory[0] = new ItemStack(this.inventory[0].getItem().getContainerItem());
					else
						InventoryUtilities.decrementStackQuantity(this, 0, 1);

					sendCookUpdateToClients();
				}
			}

			if (burnTimeRemaining > 0){
				burnTimeRemaining--;
				PowerNodeRegistry.For(worldObj).insertPower(this, PowerTypes.NEUTRAL, powerBase * powerMultiplier);

				if (burnTimeRemaining % 20 == 0)
					sendCookUpdateToClients();
			}
		}else{
			surroundingCheckTicks = 1;
		}

		if (worldObj.isRemote){
			lastOffsetY = offsetY;
			offsetY = (float)Math.max(Math.sin(worldObj.getTotalWorldTime() / 20f) / 5, 0.25f);
			if (burnTimeRemaining > 0)
				burnTimeRemaining--;

		}

		super.updateEntity();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 0.3, zCoord + 2);
	}

	@Override
	public MultiblockStructureDefinition getDefinition(){
		return structure;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("burnTimeRemaining", burnTimeRemaining);
		nbttagcompound.setInteger("maxBurnTime", maxBurnTime);

		if (inventory != null){
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

			nbttagcompound.setTag("BurnInventory", nbttaglist);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		burnTimeRemaining = nbttagcompound.getInteger("burnTimeRemaining");
		setMaxBurnTime(nbttagcompound.getInteger("maxBurnTime"));

		if (nbttagcompound.hasKey("BurnInventory")){
			NBTTagList nbttaglist = nbttagcompound.getTagList("BurnInventory", Constants.NBT.TAG_COMPOUND);
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
	}

	@Override
	public int getChargeRate(){
		return 0;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return type == PowerTypes.NEUTRAL;
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return new PowerTypes[]{PowerTypes.NEUTRAL};
	}

	@Override
	public boolean canRequestPower(){
		return false;
	}

	@Override
	public int getSizeInventory(){
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		if (i < 0 || i >= this.getSizeInventory())
			return null;
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
		return "obelisk";
	}

	@Override
	public boolean hasCustomInventoryName(){
		return false;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
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
		return ObeliskFuelHelper.instance.getFuelBurnTime(itemstack) > 0;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}
}
