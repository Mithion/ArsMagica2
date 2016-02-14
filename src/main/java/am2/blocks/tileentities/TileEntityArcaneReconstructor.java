package am2.blocks.tileentities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.events.ReconstructorRepairEvent;
import am2.api.math.AMVector3;
import am2.api.power.PowerTypes;
import am2.entities.EntityDummyCaster;
import am2.items.ItemFocusCharge;
import am2.items.ItemFocusMana;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.power.PowerNodeRegistry;

public class TileEntityArcaneReconstructor extends TileEntityAMPower implements IInventory, ISidedInventory, IKeystoneLockable{

	private ItemStack[] inventory;
	private boolean active;
	private int repairCounter;
	private final static float repairCostPerDamagePoint = 250;
	private float ringOffset;
	private final AMVector3 outerRingRotation;
	private final AMVector3 middleRingRotation;
	private final AMVector3 innerRingRotation;
	private float rotateOffset = 0;
	private int deactivationDelayTicks = 0;

	private EntityLiving dummyEntity;

	private AMVector3 outerRingRotationSpeeds;
	private AMVector3 middleRingRotationSpeeds;
	private AMVector3 innerRingRotationSpeeds;

	private static final int SLOT_ACTIVE = 3;
	private boolean isFirstTick = true;

	public TileEntityArcaneReconstructor(){
		super(500);
		inventory = new ItemStack[getSizeInventory()];
		active = false;
		repairCounter = 0;

		outerRingRotation = new AMVector3(0, 0, 0);
		middleRingRotation = new AMVector3(0, 0, 0);
		innerRingRotation = new AMVector3(0, 0, 0);

	}

	@Override
	public float particleOffset(Axis axis){
		if (axis.equals(Axis.Y))
			return 0.25f;
		return 0.5f;
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void update(){
		if (isFirstTick) {
			outerRingRotationSpeeds = new AMVector3(worldObj.rand.nextDouble() * 4 - 2, worldObj.rand.nextDouble() * 4 - 2, worldObj.rand.nextDouble() * 4 - 2);
			middleRingRotationSpeeds = new AMVector3(worldObj.rand.nextDouble() * 4 - 2, worldObj.rand.nextDouble() * 4 - 2, worldObj.rand.nextDouble() * 4 - 2);
			innerRingRotationSpeeds = new AMVector3(worldObj.rand.nextDouble() * 4 - 2, worldObj.rand.nextDouble() * 4 - 2, worldObj.rand.nextDouble() * 4 - 2);
			isFirstTick = false;
		}

		if (PowerNodeRegistry.For(this.worldObj).checkPower(this, this.getRepairCost())) {// has enough power
			if ((repairCounter++ % getRepairRate() == 0) && (!queueRepairableItem())) {// has ticked and already has item queued
				if (performRepair()){// something to repair
					if (!worldObj.isRemote){
						PowerNodeRegistry.For(this.worldObj).consumePower(this, PowerNodeRegistry.For(worldObj).getHighestPowerType(this), this.getRepairCost());
					}
				}
			}
			deactivationDelayTicks = 0;
		} else if (!worldObj.isRemote && active){// out of power, on server and active
			if (deactivationDelayTicks++ > 100) {// 5 seconds
				deactivationDelayTicks = 0;
				this.active = false;
				if (!worldObj.isRemote)
					worldObj.markBlockForUpdate(pos);
			}
		}
		if (worldObj.isRemote){
			updateRotations();
			if (shouldRenderItemStack()){
				AMParticle p = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "sparkle2", pos.getX() + 0.2 + (worldObj.rand.nextDouble() * 0.6), pos.getY() + 0.4, pos.getZ() + 0.2 + (worldObj.rand.nextDouble() * 0.6));
				if (p != null){
					p.AddParticleController(new ParticleFloatUpward(p, 0.0f, 0.02f, 1, false));
					p.setIgnoreMaxAge(true);
					p.setParticleScale(0.1f);
					p.AddParticleController(new ParticleFadeOut(p, 1, false).setFadeSpeed(0.035f).setKillParticleOnFinish(true));
					p.setRGBColorF(1, 0, 1);
				}
			}
		}

		super.update();
	}

	public AMVector3 getOuterRingRotationSpeed(){
		return this.outerRingRotationSpeeds;
	}

	public AMVector3 getMiddleRingRotationSpeed(){
		return this.middleRingRotationSpeeds;
	}

	public AMVector3 getInnerRingRotationSpeed(){
		return this.innerRingRotationSpeeds;
	}

	public float getRotateOffset(){
		return this.rotateOffset;
	}

	public boolean shouldRenderRotateOffset(){
		return this.ringOffset >= 0.5 && this.rotateOffset > 0;
	}

	private void updateRotations(){
		if (active){
			if (ringOffset < 0.5f){
				ringOffset += 0.015f;
			}else{
				ringOffset = 0.51f;
				outerRingRotation.add(outerRingRotationSpeeds);
				middleRingRotation.add(middleRingRotationSpeeds);
				innerRingRotation.add(innerRingRotationSpeeds);
			}

			if (rotateOffset < 10){
				rotateOffset += 0.0625f;
			}
		}else{
			if (rotateOffset > 0){
				rotateOffset -= 0.025f;
			}
			if (!outerRingRotation.isZero() || !middleRingRotation.isZero() || !innerRingRotation.isZero()){
				outerRingRotation.x = easeCoordinate(outerRingRotation.x, outerRingRotationSpeeds.x);
				outerRingRotation.y = easeCoordinate(outerRingRotation.y, outerRingRotationSpeeds.y);
				outerRingRotation.z = easeCoordinate(outerRingRotation.z, outerRingRotationSpeeds.z);

				middleRingRotation.x = easeCoordinate(middleRingRotation.x, middleRingRotationSpeeds.x);
				middleRingRotation.y = easeCoordinate(middleRingRotation.y, middleRingRotationSpeeds.y);
				middleRingRotation.z = easeCoordinate(middleRingRotation.z, middleRingRotationSpeeds.z);

				innerRingRotation.x = easeCoordinate(innerRingRotation.x, innerRingRotationSpeeds.x);
				innerRingRotation.y = easeCoordinate(innerRingRotation.y, innerRingRotationSpeeds.y);
				innerRingRotation.z = easeCoordinate(innerRingRotation.z, innerRingRotationSpeeds.z);
			}else{
				if (ringOffset > 0f){
					ringOffset -= 0.03f;
				} else {
					ringOffset = 0f;
				}
			}
		}
	}

	private float easeCoordinate(float coord, float step){
		step = Math.abs(step);
		step = 4;
		float calc = coord;
		if (Math.abs(coord % 360) > -4 && Math.abs(coord % 360) < 4){
			return 0;
		}
		if (calc < 0){
			calc %= -360;
			if (calc <= -180){
				coord -= step;
			}else{
				coord += step;
			}
		}else{
			calc %= 360;
			if (calc >= 180){
				coord += step;
			}else{
				coord -= step;
			}
		}
		return coord;
	}

	public float getOffset(){
		return this.ringOffset;
	}

	public boolean shouldRenderItemStack(){
		return active && ringOffset >= 0.5;
	}

	public ItemStack getCurrentItem(){
		return inventory[SLOT_ACTIVE];
	}

	public AMVector3 getInnerRingRotation(){
		return innerRingRotation;
	}

	public AMVector3 getMiddleRingRotation(){
		return middleRingRotation;
	}

	public AMVector3 getOuterRingRotation(){
		return outerRingRotation;
	}

	private boolean queueRepairableItem(){
		if (inventory[SLOT_ACTIVE] != null) {
			if (!active) {
				this.active = true;
				if (!worldObj.isRemote)
					worldObj.markBlockForUpdate(pos);
			}
			return false;
		}
		for (int i = 4; i < 10; ++i){
			if (itemStackIsValid(inventory[i])){
				inventory[SLOT_ACTIVE] = inventory[i].copy();
				inventory[i] = null;
				this.active = true;
				if (!worldObj.isRemote)
					worldObj.markBlockForUpdate(pos);
				return true;
			}
		}
		this.active = false;
		if (!worldObj.isRemote)
			worldObj.markBlockForUpdate(pos);
		return true;
	}

	private boolean itemStackIsValid(ItemStack stack){
		return stack != null && !(stack.getItem() instanceof ItemBlock) && stack.getItem().isRepairable();
	}

	private EntityLiving getDummyEntity(){
		if (dummyEntity == null)
			dummyEntity = new EntityDummyCaster(this.worldObj);
		return dummyEntity;
	}

	private boolean performRepair(){
		if (inventory[SLOT_ACTIVE] == null) return false;

		ReconstructorRepairEvent event = new ReconstructorRepairEvent(inventory[SLOT_ACTIVE]);
		if (MinecraftForge.EVENT_BUS.post(event)){
			return true;
		}

		if (inventory[SLOT_ACTIVE].isItemDamaged()){
			if (!worldObj.isRemote)
				inventory[SLOT_ACTIVE].damageItem(-1, getDummyEntity());
			return true;
		}else{
			boolean did_copy = false;
			for (int i = 10; i < 16; ++i){
				if (inventory[i] == null){
					if (!worldObj.isRemote){
						inventory[i] = inventory[SLOT_ACTIVE].copy();
						inventory[SLOT_ACTIVE] = null;
					}
					did_copy = true;
					break;
				}
			}
			worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "arsmagica2:misc.reconstructor.complete", 1.0f, 1.0f, true);

			return did_copy;
		}
	}

	@Override
	public int getSizeInventory(){
		return 19;
	}

	@Override
	public ItemStack getStackInSlot(int var1){
		if (var1 >= inventory.length){
			return null;
		}
		return inventory[var1];
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
		return "ArcaneReconstructor";
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
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("ArcaneReconstructorInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		active = nbttagcompound.getBoolean("ArcaneReconstructorActive");
		repairCounter = nbttagcompound.getInteger("ArcaneReconstructorRepairCounter");
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

		nbttagcompound.setTag("ArcaneReconstructorInventory", nbttaglist);
		nbttagcompound.setBoolean("ArcaneReconstructorActive", active);
		nbttagcompound.setInteger("ArcaneReconstructorRepairCounter", repairCounter);
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

	private int getRepairRate(){
		int numFoci = numFociOfType(ItemFocusCharge.class);
		int base = 20;
		if (numFoci > 0){
			base -= 5 * numFoci;
		}

		return base;
	}

	private float getRepairCost(){
		int numFoci = numFociOfType(ItemFocusMana.class);
		float base = repairCostPerDamagePoint;
		float deduction = 100;
		for (int i = 0; i < numFoci; ++i){
			base -= deduction;
			deduction /= 2;
		}

		return base;
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return i >= 4 && i < 10 && itemStackIsValid(itemstack);
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing var1){
		return new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing j){
		return i >= 4 && i < 10 && itemStackIsValid(itemstack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing j){
		return i >= 10 && i < 16;
	}

	@Override
	public int getChargeRate(){
		return 250;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[16];
		runes[1] = inventory[17];
		runes[2] = inventory[18];

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
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = inventory[index].copy();
		inventory[index] = null;
		return stack;
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
