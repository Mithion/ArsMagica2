package am2.blocks.tileentities;

import am2.AMChunkLoader;
import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.math.AMVector3;
import am2.api.power.PowerTypes;
import am2.blocks.BlocksCommonProxy;
import am2.buffs.BuffList;
import am2.multiblock.IMultiblockStructureController;
import am2.power.PowerNodeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityKeystoneRecepticle extends TileEntityAMPower implements IInventory, IMultiblockStructureController, IKeystoneLockable{

	private boolean isActive;
	private long key;
	private final int boltType = 2;
	private int surroundingCheckTicks = 20;

	private final MultiblockStructureDefinition primary = new MultiblockStructureDefinition("gateways_alt");
	private final MultiblockStructureDefinition secondary = new MultiblockStructureDefinition("gateways");

	public static int keystoneSlot = 0;

	private ItemStack[] inventory;

	public TileEntityKeystoneRecepticle(){
		super(250000);
		this.isActive = false;
		inventory = new ItemStack[getSizeInventory()];
		initMultiblock();
	}

	public void initMultiblock(){
		//primary
		//row 0
		primary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.keystoneRecepticle, 0);
		primary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.keystoneRecepticle, 2);
		primary.addAllowedBlock(0, 0, -1, Blocks.stone_brick_stairs, 2);
		primary.addAllowedBlock(0, 0, 1, Blocks.stone_brick_stairs, 3);
		primary.addAllowedBlock(0, 0, -1, Blocks.stonebrick);
		primary.addAllowedBlock(0, 0, 1, Blocks.stonebrick);
		//row 1
		primary.addAllowedBlock(0, -1, -1, Blocks.stone_brick_stairs, 7);
		primary.addAllowedBlock(0, -1, -2, Blocks.stone_brick_stairs, 2);
		primary.addAllowedBlock(0, -1, 1, Blocks.stone_brick_stairs, 6);
		primary.addAllowedBlock(0, -1, 2, Blocks.stone_brick_stairs, 3);
		primary.addAllowedBlock(0, -1, 2, Blocks.stonebrick);
		primary.addAllowedBlock(0, -1, -2, Blocks.stonebrick);
		//row 2
		primary.addAllowedBlock(0, -2, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, -2, 2, Blocks.stonebrick, 0);
		//row 3
		primary.addAllowedBlock(0, -3, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, -3, 2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, -3, -1, Blocks.stone_brick_stairs, 3);
		primary.addAllowedBlock(0, -3, 1, Blocks.stone_brick_stairs, 2);
		//row 4
		primary.addAllowedBlock(0, -4, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, -4, -1, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, -4, 0, Blocks.stonebrick, 3);
		primary.addAllowedBlock(0, -4, 1, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, -4, 2, Blocks.stonebrick, 0);

		//secondary
		//row 0
		secondary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.keystoneRecepticle, 1);
		secondary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.keystoneRecepticle, 3);
		secondary.addAllowedBlock(-1, 0, 0, Blocks.stone_brick_stairs, 0);
		secondary.addAllowedBlock(1, 0, 0, Blocks.stone_brick_stairs, 1);
		secondary.addAllowedBlock(-1, 0, 0, Blocks.stonebrick);
		secondary.addAllowedBlock(1, 0, 0, Blocks.stonebrick);
		//row 1
		secondary.addAllowedBlock(-1, -1, 0, Blocks.stone_brick_stairs, 5);
		secondary.addAllowedBlock(-2, -1, 0, Blocks.stone_brick_stairs, 0);
		secondary.addAllowedBlock(1, -1, 0, Blocks.stone_brick_stairs, 4);
		secondary.addAllowedBlock(2, -1, 0, Blocks.stone_brick_stairs, 1);
		secondary.addAllowedBlock(2, -1, 0, Blocks.stonebrick);
		secondary.addAllowedBlock(-2, -1, 0, Blocks.stonebrick);
		//row 2
		secondary.addAllowedBlock(-2, -2, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -2, 0, Blocks.stonebrick, 0);
		//row 3
		secondary.addAllowedBlock(-2, -3, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -3, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(-1, -3, 0, Blocks.stone_brick_stairs, 1);
		secondary.addAllowedBlock(1, -3, 0, Blocks.stone_brick_stairs, 0);
		//row 4
		secondary.addAllowedBlock(-2, -4, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(-1, -4, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(0, -4, 0, Blocks.stonebrick, 3);
		secondary.addAllowedBlock(1, -4, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -4, 0, Blocks.stonebrick, 0);
	}

	public void onPlaced(){
		if (!worldObj.isRemote){
			AMChunkLoader.INSTANCE.requestStaticChunkLoad(this.getClass(), this.xCoord, this.yCoord, this.zCoord, this.worldObj);
		}
	}

	@Override
	public void invalidate(){
		AMCore.instance.proxy.blocks.removeKeystonePortal(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);

		if (!worldObj.isRemote){
			AMChunkLoader.INSTANCE.releaseStaticChunkLoad(this.getClass(), this.xCoord, this.yCoord, this.zCoord, this.worldObj);
		}

		super.invalidate();
	}

	public void setActive(long key){
		this.isActive = true;
		this.key = key;
		int myMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (PowerNodeRegistry.For(worldObj).getHighestPowerType(this) == PowerTypes.DARK){
			myMeta |= 8;
		}else if (PowerNodeRegistry.For(worldObj).getHighestPowerType(this) == PowerTypes.LIGHT){
			myMeta |= 4;
		}

		if (!this.worldObj.isRemote){
			for (Object player : this.worldObj.playerEntities){
				if (player instanceof EntityPlayerMP && new AMVector3((EntityPlayerMP)player).distanceSqTo(new AMVector3(this)) <= 4096){
					((EntityPlayerMP)player).playerNetServerHandler.sendPacket(getDescriptionPacket());
				}
			}
		}else{
			worldObj.playSound(xCoord, yCoord, zCoord, "arsmagica2:misc.gateway.open", 1.0f, 1.0f, true);
		}
	}

	public boolean isActive(){
		return this.isActive;
	}

	@Override
	public void updateEntity(){
		super.updateEntity();

		AxisAlignedBB bb = new AxisAlignedBB(xCoord + 0.3, yCoord - 3, zCoord + 0.3, xCoord + 0.7, yCoord, zCoord + 0.7);
		ArrayList<Entity> entities = (ArrayList<Entity>)worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);

		if (this.isActive){
			surroundingCheckTicks--;
			if (surroundingCheckTicks <= 0){
				surroundingCheckTicks = 20;
				checkSurroundings();
			}
			if (entities.size() == 1){
				doTeleport(entities.get(0));
			}
		}else{
			if (entities.size() == 1 && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord)){
				Entity entity = entities.get(0);
				if (entity instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer)entity;
					if (player.isPotionActive(BuffList.haste) && player.isPotionActive(Potion.moveSpeed.id) && player.isSprinting()){
						//if (worldObj.isRemote)
						//player.addStat(AMCore.achievements.EightyEightMilesPerHour, 1);
						this.key = 0;
						if (!worldObj.isRemote){
							EntityLightningBolt elb = new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord);
							worldObj.spawnEntityInWorld(elb);
						}
						doTeleport(player);
					}

				}
			}
		}
	}

	public boolean canActivate(){
		boolean allGood = true;
		allGood &= worldObj.isAirBlock(xCoord, yCoord - 1, zCoord);
		allGood &= worldObj.isAirBlock(xCoord, yCoord - 2, zCoord);
		allGood &= worldObj.isAirBlock(xCoord, yCoord - 3, zCoord);
		allGood &= checkStructure();
		allGood &= PowerNodeRegistry.For(this.worldObj).checkPower(this);
		allGood &= !this.isActive;
		return allGood;
	}

	private void checkSurroundings(){
		if (!checkStructure()){
			deactivate();
		}
	}

	private boolean checkStructure(){
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 0x3;
		boolean remainActive = true;

		switch (meta){
		case 0:
		case 2:
			remainActive &= primary.checkStructure(worldObj, xCoord, yCoord, zCoord);
			break;
		case 1:
		default:
			remainActive &= secondary.checkStructure(worldObj, xCoord, yCoord, zCoord);
			break;
		}
		return remainActive;
	}

	public void deactivate(){
		this.isActive = false;
		if (!this.worldObj.isRemote){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	private void doTeleport(Entity entity){
		deactivate();

		AMVector3 newLocation = AMCore.instance.proxy.blocks.getNextKeystonePortalLocation(this.worldObj, xCoord, yCoord, zCoord, false, this.key);
		AMVector3 myLocation = new AMVector3(xCoord, yCoord, zCoord);

		double distance = myLocation.distanceTo(newLocation);
		float essenceCost = (float)(Math.pow(distance, 2) * 0.00175f);

		int meta = worldObj.getBlockMetadata((int)newLocation.x, (int)newLocation.y, (int)newLocation.z);

		if (AMCore.config.getHazardousGateways()){
			//uh-oh!  Not enough power!  The teleporter will still send you though, but I wonder where...
			float charge = PowerNodeRegistry.For(this.worldObj).getHighestPower(this);
			if (charge < essenceCost){
				essenceCost = charge;
				//get the distance that our charge *will* take us towards the next point
				double distanceWeCanGo = MathHelper.sqrt_double(charge / 0.00175);
				//get the angle between the 2 vectors
				double deltaZ = newLocation.z - myLocation.z;
				double deltaX = newLocation.x - myLocation.x;
				double angleH = Math.atan2(deltaZ, deltaX);
				//interpolate the distance at that angle - this is the new position
				double newX = myLocation.x + (Math.cos(angleH) * distanceWeCanGo);
				double newZ = myLocation.z + (Math.sin(angleH) * distanceWeCanGo);
				double newY = myLocation.y;

				while (worldObj.isAirBlock((int)newX, (int)newY, (int)newZ)){
					newY++;
				}

				newLocation = new AMVector3(newX, newY, newZ);
			}
		}else{
			this.worldObj.playSoundEffect(newLocation.x, newLocation.y, newLocation.z, "mob.endermen.portal", 1.0F, 1.0F);
			return;
		}


		float newRotation = 0;
		switch (meta){
		case 0:
			newRotation = 270;
			break;
		case 1:
			newRotation = 180;
			break;
		case 2:
			newRotation = 90;
			break;
		case 3:
			newRotation = 0;
			break;
		}
		entity.setPositionAndRotation(newLocation.x + 0.5, newLocation.y - entity.height, newLocation.z + 0.5, newRotation, entity.rotationPitch);

		PowerNodeRegistry.For(this.worldObj).consumePower(this, PowerNodeRegistry.For(this.worldObj).getHighestPowerType(this), essenceCost);

		this.worldObj.playSoundEffect(myLocation.x, myLocation.y, myLocation.z, "mob.endermen.portal", 1.0F, 1.0F);
		this.worldObj.playSoundEffect(newLocation.x, newLocation.y, newLocation.z, "mob.endermen.portal", 1.0F, 1.0F);
	}

	@Override
	public int getSizeInventory(){
		return 3;
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
		return "Keystone Recepticle";
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
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("KeystoneRecepticleInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		AMCore.instance.proxy.blocks.registerKeystonePortal(xCoord, yCoord, zCoord, nbttagcompound.getInteger("keystone_receptacle_dimension_id"));

		this.isActive = nbttagcompound.getBoolean("isActive");
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

		nbttagcompound.setInteger("keystone_receptacle_dimension_id", worldObj.provider.dimensionId);
		nbttagcompound.setTag("KeystoneRecepticleInventory", nbttaglist);
		nbttagcompound.setBoolean("isActive", this.isActive);
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
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
	public boolean hasCustomInventoryName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public MultiblockStructureDefinition getDefinition(){
		return secondary;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return new AxisAlignedBB(xCoord - 3, yCoord - 3, zCoord - 3, xCoord + 3, yCoord + 3, zCoord + 3);
	}

	@Override
	public int getChargeRate(){
		return 5;
	}

	@Override
	public int getRequestInterval(){
		return 0;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}
}
