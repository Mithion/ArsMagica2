package am2.blocks.tileentities;

import am2.LogHelper;
import am2.api.math.AMVector3;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.flickers.FlickerOperatorRegistry;
import am2.blocks.tileentities.flickers.TileEntityFlickerControllerBase;
import am2.items.ItemFlickerJar;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TileEntityFlickerHabitat extends TileEntityFlickerControllerBase implements IInventory{
	private static final float FULL_CIRCLE = 360.0f;
	private static final float ROATATION_RATE = 1.0f;
	private static final float FLOAT_RATE = 0.001f;
	private static final float MAX_FLOAT_UP = 0.1f;
	private static final float MAX_FLOAT_DOWN = -0.04f;

	public static final int PRIORITY_LEVELS = 10;
	public static final int PRIORITY_FINAL = PRIORITY_LEVELS + 1;

	private static final float MAX_SHIFT_TICKS = 20.0f;

	private int colorCounter = 0;
	private int fadeCounter = 0;

	private ArrayList<AMVector3> inList;
	private HashMap<Integer, ArrayList<AMVector3>> outList;
	private int timer;
	private int inListPosition = 0;
	private HashMap<Integer, Integer> outListPositions = new HashMap<Integer, Integer>();
	private int defoutListPosition = 0;
	private ItemStack flickerJar;
	private float rotateOffset = 0;
	private float floatOffset = 0;
	private boolean floatUp = true;
	private boolean isUpgrade = false;
	private EnumFacing mainHabitatDirection = null;


	public boolean isUpgrade(){
		return isUpgrade;
	}

	public void setUpgrade(boolean isUpgrade, EnumFacing direction){
		this.isUpgrade = isUpgrade;
		this.mainHabitatDirection = direction;
	}

	public EnumFacing getMainHabitatDirection(){
		return mainHabitatDirection;
	}

	public TileEntityFlickerHabitat(){
		initLocationLists();

		if (worldObj != null && worldObj.isRemote){
			rotateOffset = worldObj.rand.nextFloat() * FULL_CIRCLE - 1;
			floatOffset = MAX_FLOAT_DOWN + (worldObj.rand.nextFloat() * (MAX_FLOAT_UP - MAX_FLOAT_DOWN) + 1);
		}
	}

	private void initLocationLists(){
		inList = new ArrayList<AMVector3>();
		outList = new HashMap<Integer, ArrayList<AMVector3>>();

		for (int i = 0; i < PRIORITY_LEVELS; ++i)
			outList.put(i, new ArrayList<AMVector3>());
	}

	public Affinity getSelectedAffinity(){
		if (flickerJar != null){
			return Affinity.getByID(flickerJar.getItemDamage());
		}else{
			return null;
		}
	}

	public boolean hasFlicker(){
		return flickerJar != null;
	}

	/**
	 * @return the inListPosition
	 */
	public int getInListPosition(){
		return inListPosition;
	}

	/**
	 * @param inListPosition the inListPosition to set
	 */
	public void setInListPosition(int inListPosition){
		this.inListPosition = inListPosition;
	}


	/**
	 * @return the outListPosition
	 */
	public int getOutListPosition(int priority){
		if (!outListPositions.containsKey(priority))
			outListPositions.put(priority, 0);

		return outListPositions.get(priority);
	}

	/**
	 * @param outListPosition the outListPosition to set
	 */
	public void setOutListPosition(int priority, int outListPosition){
		this.outListPositions.put(priority, outListPosition);
	}

	public int getDeferredOutListPosition(){
		return defoutListPosition;
	}

	public void setDeferredOutListPosition(int position){
		this.defoutListPosition = position;
	}

	/**
	 * @return the inList
	 */
	public AMVector3 getInListAt(int index){
		return inList.get(index);
	}

	public void setInListAt(int index, AMVector3 value){
		this.inList.set(index, value);
	}

	public void removeInListAt(int index){
		this.inList.remove(index);
	}

	public void removeInListAt(AMVector3 value){
		this.inList.remove(value);
	}

	public int getInListSize(){
		return inList.size();
	}

	public AMVector3 getOutListAt(int priority, int index){
		if (outList != null && outList.containsKey(priority) && outList.get(priority).size() > 0)
			return outList.get(priority).get(index);
		return new AMVector3(this);
	}

	public void setOutListAt(int priority, int index, AMVector3 value){
		if (outList.containsKey(priority))
			this.outList.get(priority).set(index, value);
	}

	public void removeOutListAt(int priority, int index){
		if (this.outList.containsKey(priority))
			this.outList.get(priority).remove(index);
	}

	public void removeOutListAt(int priority, AMVector3 value){
		if (!this.outList.containsKey(priority))
			return;
		this.outList.get(priority).remove(value);
	}

	public int getOutListSize(int priority){
		if (this.outList.containsKey(priority))
			return this.outList.get(priority).size();
		return 0;
	}

	/**
	 * @return the rotateOffset
	 */
	public float getRotateOffset(){
		return rotateOffset;
	}

	public float getFloatOffset(){
		return floatOffset;
	}

	public void AddMarkerLocationIn(AMVector3 markerLocation){
		if (!inList.contains(markerLocation)){
			inList.add(markerLocation);
			LogHelper.trace("In Link Created");
		}else{
			LogHelper.trace("Link Already Exists");
		}
	}

	public void AddMarkerLocationOut(AMVector3 markerLocation){

		Block out = this.worldObj.getBlockState(new BlockPos((int)markerLocation.x, (int)markerLocation.y, (int)markerLocation.z)).getBlock();
		if (out != BlocksCommonProxy.crystalMarker)
			return;

		TileEntity te = this.worldObj.getTileEntity(new BlockPos((int)markerLocation.x, (int)markerLocation.y, (int)markerLocation.z));
		if (te == null || !(te instanceof TileEntityCrystalMarker))
			return;

		int priority = ((TileEntityCrystalMarker)te).getPriority();

		if (!outList.containsKey(priority))
			outList.put(priority, new ArrayList<AMVector3>());

		if (!outList.get(priority).contains(markerLocation)){
			outList.get(priority).add(markerLocation);
			LogHelper.trace("Out Link Create");
		}else{
			LogHelper.trace("Link Already Exists");
		}
	}

	public void removeInMarkerLocation(int x, int y, int z){
		inList.remove(new AMVector3(x, y, z));
	}

	public void removeOutMarkerLocation(int x, int y, int z){
		outList.remove(new AMVector3(x, y, z));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);

		//write in list
		NBTTagList inItems = new NBTTagList();
		for (AMVector3 inItem : inList){
			NBTTagCompound vectorItem = new NBTTagCompound();
			vectorItem.setFloat("x", inItem.x);
			vectorItem.setFloat("y", inItem.y);
			vectorItem.setFloat("z", inItem.z);
			inItems.appendTag(vectorItem);
		}
		nbttagcompound.setTag("InList", inItems);

		//write out list
		writeOutList(nbttagcompound);

		if (flickerJar != null){
			NBTTagCompound jar = new NBTTagCompound();
			flickerJar.writeToNBT(jar);
			nbttagcompound.setTag("flickerJar", jar);
		}

		//write upgrade status
		nbttagcompound.setBoolean("upgrade", isUpgrade);
		if (this.isUpgrade){
			nbttagcompound.setInteger("mainHabitatDirection", mainHabitatDirection.ordinal()); // TODO find what to actually put here, ForgeDirection.flag was replaced
		}
	}

	private void writeOutList(NBTTagCompound compound){
		NBTTagList outputList = new NBTTagList();

		for (int priority : outList.keySet()){
			//create a compound for the priority
			NBTTagCompound priorityCompound = new NBTTagCompound();
			//attach the priority to the compound
			priorityCompound.setInteger("priority", priority);

			//get the list of locations for this priority
			ArrayList<AMVector3> priorityList = outList.get(priority);
			if (priorityList == null)
				continue;

			//create a tag list to store the vectors in
			NBTTagList vectors = new NBTTagList();
			//spin through the list
			for (AMVector3 vec : priorityList){
				//create a compound to hold the individual vector
				NBTTagCompound vectorItem = new NBTTagCompound();
				//write the vector to the newly created compound
				vec.writeToNBT(vectorItem);
				//attach the vector tag to the vectors list
				vectors.appendTag(vectorItem);
			}
			//attach the vectors to the priority compound
			priorityCompound.setTag("vectors", vectors);

			//attach the priority compound to the final output list
			outputList.appendTag(priorityCompound);
		}

		//store the final output list in the parent compound
		compound.setTag("outList", outputList);
	}

	private void readOutList(NBTTagCompound compound){
		//valid compound?
		if (!compound.hasKey("outList"))
			return;
		//get the tag list for the output data
		NBTTagList outputList = compound.getTagList("outList", Constants.NBT.TAG_COMPOUND);
		//spin through em
		for (int i = 0; i < outputList.tagCount(); ++i){
			//get the current compound tag - this should contain a priority level and a list of vectors
			NBTTagCompound priorityCompound = outputList.getCompoundTagAt(i);
			//create the list to hold the output locations
			ArrayList<AMVector3> locationsInPriority = new ArrayList<AMVector3>();
			//does the current compound tag contain the values we're looking for?
			if (!priorityCompound.hasKey("priority") || !priorityCompound.hasKey("vectors")){
				LogHelper.warn("Malformed save data for flicker item transport controller - cannot process records.");
				continue;
			}
			//get the priority from the compound
			int priority = priorityCompound.getInteger("priority");
			//get the list of vectors from the compound
			NBTTagList vectors = priorityCompound.getTagList("vectors", Constants.NBT.TAG_COMPOUND);
			//spin through the vectors
			for (int x = 0; x < vectors.tagCount(); ++x){
				//get the current vector tag
				NBTTagCompound vectorItem = vectors.getCompoundTagAt(x);
				//read the vector from the NBT compound
				AMVector3 vec = AMVector3.readFromNBT(vectorItem);
				//add the vector location if it read correctly
				if (vec != null && vec != AMVector3.zero()){
					locationsInPriority.add(vec);
				}
			}

			//insert the list into the output list at the specified priority
			this.outList.put(priority, locationsInPriority);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);

		initLocationLists();

		//read in list
		if (nbttagcompound.hasKey("InList")){
			NBTTagList inItems = nbttagcompound.getTagList("InList", Constants.NBT.TAG_COMPOUND);

			for (int i = 0; i < inItems.tagCount(); ++i){
				NBTTagCompound inItem = (NBTTagCompound)inItems.getCompoundTagAt(i);
				if (inItem == null){
					continue;
				}

				float x = 0;
				float y = 0;
				float z = 0;
				boolean success = true;

				if (inItem.hasKey("x")){
					x = inItem.getFloat("x");
				}else{
					success = false;
				}

				if (success && inItem.hasKey("y")){
					y = inItem.getFloat("y");
				}else{
					success = false;
				}

				if (success && inItem.hasKey("z")){
					z = inItem.getFloat("z");
				}else{
					success = false;
				}

				if (success){
					inList.add(new AMVector3(x, y, z));
				}
			}//end for(int i = 0; i < inItems.tagCount(); ++i)
		}//end if(nbttagcompound.hasKey("InList"))

		readOutList(nbttagcompound);

		if (nbttagcompound.hasKey("upgrade")){
			isUpgrade = nbttagcompound.getBoolean("upgrade");
		}

		if (nbttagcompound.hasKey("flickerJar")){
			NBTTagCompound jar = nbttagcompound.getCompoundTag("flickerJar");
			flickerJar = ItemStack.loadItemStackFromNBT(jar);

			if (!this.isUpgrade){
				setOperatorBasedOnFlicker();
			}
		}


		if (this.isUpgrade){
			int flag = nbttagcompound.getInteger("mainHabitatDirection");

			for (EnumFacing direction : EnumFacing.VALUES){
				if (direction.flag == flag){
					this.mainHabitatDirection = direction;
					break;
				}
			}
		}
	}

	public int getCrystalColor(){
		if (this.flickerJar == null)
			return 0;

		if (this.flickerJar.getItem() == ItemsCommonProxy.flickerJar)
			return ((ItemFlickerJar)flickerJar.getItem()).getColorFromItemStack(flickerJar, 0);
		else if (this.flickerJar.getItem() == ItemsCommonProxy.flickerFocus){
			ArrayList<Affinity> affinities = new ArrayList<Affinity>();
			int meta = this.flickerJar.getItemDamage();
			for (Affinity aff : Affinity.values()){
				if (aff == Affinity.NONE)
					continue;
				if ((meta & aff.getAffinityMask()) == aff.getAffinityMask())
					affinities.add(aff);
			}

			if (affinities.size() > 0){
				int firstColor = affinities.get(colorCounter % affinities.size()).color;
				int secondColor = affinities.get((colorCounter + 1) % affinities.size()).color;
				if (firstColor == secondColor)
					return firstColor;
				if (this.fadeCounter > this.MAX_SHIFT_TICKS)
					return secondColor;
				return colorShift(firstColor, secondColor);
			}
		}

		return 0;
	}

	private int colorShift(int f, int s){
		int fr = (f >> 16) & 0xFF;
		int fg = (f >> 8) & 0xFF;
		int fb = (f) & 0xFF;

		int sr = (s >> 16) & 0xFF;
		int sg = (s >> 8) & 0xFF;
		int sb = (s) & 0xFF;

		float dr = (sr - fr) / MAX_SHIFT_TICKS;
		float dg = (sg - fg) / MAX_SHIFT_TICKS;
		float db = (sb - fb) / MAX_SHIFT_TICKS;

		int combined =
				(((fr + (int)(dr * fadeCounter)) & 0xFF) << 16) |
						(((fg + (int)(dg * fadeCounter)) & 0xFF) << 8) |
						(((fb + (int)(db * fadeCounter)) & 0xFF));

		return combined;
	}

	@Override
	public int getSizeInventory(){
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		if (i <= getSizeInventory() && flickerJar != null){
			return flickerJar;
		}

		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (i <= getSizeInventory() && flickerJar != null){
			ItemStack jar = flickerJar;
			flickerJar = null;
			return jar;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int i){
		if (i <= getSizeInventory() && flickerJar != null){
			ItemStack jar = flickerJar;
			flickerJar = null;
			return jar;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		flickerJar = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
			itemstack.stackSize = getInventoryStackLimit();
		}

	}

	@Override
	public String getName(){
		return "Flicker Habitat";
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
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
		if (!this.isUpgrade){
			setOperatorBasedOnFlicker();
			scanForNearbyUpgrades();
		}else{
			setUpgradeOfMainHabitat();
		}
	}

	private void setUpgradeOfMainHabitat(){
		if (this.mainHabitatDirection != null){
			TileEntity te = worldObj.getTileEntity(this.xCoord + this.mainHabitatDirection.offsetX, this.yCoord + this.mainHabitatDirection.offsetY, this.zCoord + this.mainHabitatDirection.offsetZ);
			if (te != null && te instanceof TileEntityFlickerHabitat){
				((TileEntityFlickerHabitat)te).notifyOfNearbyUpgradeChange(this);
			}
		}
	}

	private void setOperatorBasedOnFlicker(){
		if (flickerJar != null && flickerJar.getItem() == ItemsCommonProxy.flickerFocus){
			this.setOperator(FlickerOperatorRegistry.instance.getOperatorForMask(flickerJar.getItemDamage()));
		}else{
			this.setOperator(null);
		}

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
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
	public void update(){
		super.update();

		if (fadeCounter++ >= 30){
			colorCounter++;
			fadeCounter = 0;
		}

		if (worldObj.isRemote && this.hasFlicker()){
			rotateOffset += ROATATION_RATE;

			if (rotateOffset >= FULL_CIRCLE){
				rotateOffset -= FULL_CIRCLE;
			}

			if (floatUp){
				floatOffset += FLOAT_RATE;

				if (floatOffset >= MAX_FLOAT_UP){
					invertDirection();
				}
			}else{
				floatOffset -= FLOAT_RATE;
				if (floatOffset <= MAX_FLOAT_DOWN){
					invertDirection();
				}
			}
		}
	}

	private void invertDirection(){
		floatUp = !floatUp;
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	public void switchMarkerPriority(AMVector3 vec, int oldPriority, int priority){
		if (this.outList.containsKey(oldPriority))
			this.removeOutListAt(oldPriority, vec);

		if (!this.outList.containsKey(priority))
			this.outList.put(priority, new ArrayList<AMVector3>());

		this.outList.get(priority).add(vec);
	}
}


