package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.power.PowerTypes;
import am2.blocks.BlocksCommonProxy;
import am2.items.ISpellFocus;
import am2.items.ItemFilterFocus;
import am2.models.SpriteRenderInfo;
import am2.network.AMDataWriter;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleMoveOnHeading;
import am2.power.PowerNodeRegistry;
import am2.utility.KeystoneUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import java.util.Random;

public class TileEntitySeerStone extends TileEntityAMPower implements IInventory, IKeystoneLockable{

	private boolean hasSight;
	private ArrayList<SpriteRenderInfo> animations;
	private ArrayList<Integer> animationWeighting;
	private SpriteRenderInfo currentAnimation;
	private int ticksToNextCheck;
	private int maxTicksToCheck = 20;
	private ItemStack[] inventory;
	int tickCounter;
	public static int keystoneSlot = 1;

	private PowerTypes[] validTypes = new PowerTypes[]{
			PowerTypes.LIGHT
	};

	boolean swapDetectionMode = false;

	public TileEntitySeerStone(){
		super(100);
		hasSight = false;
		tickCounter = 0;
		animations = new ArrayList<SpriteRenderInfo>();
		animationWeighting = new ArrayList<Integer>();

		animations.add(new SpriteRenderInfo(50, 59, 2)); //blink
		animationWeighting.add(0);

		animations.add(new SpriteRenderInfo(20, 29, 2)); //idle
		animationWeighting.add(60);

		animations.add(new SpriteRenderInfo(30, 39, 2)); //look left
		animationWeighting.add(11);

		animations.add(new SpriteRenderInfo(40, 49, 2)); //look right
		animationWeighting.add(11);

		animations.add(new SpriteRenderInfo(0, 14, 2)); //flare 1
		animationWeighting.add(6);

		animations.add(new SpriteRenderInfo(60, 74, 2)); //flare 2
		animationWeighting.add(6);

		animations.add(new SpriteRenderInfo(80, 94, 2)); //flare 3
		animationWeighting.add(6);

		currentAnimation = animations.get(0);
		currentAnimation.isDone = true;

		inventory = new ItemStack[getSizeInventory()];
		ticksToNextCheck = maxTicksToCheck;
	}

	@Override
	public float particleOffset(int axis){
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (axis == 0){
			switch (meta){
			case 6:
				return 0.15f;
			case 5:
				return 0.85f;
			default:
				return 0.5f;
			}
		}else if (axis == 1){
			switch (meta){
			case 1:
				return 0.85f;
			case 2:
				return 0.2f;
			default:
				return 0.5f;
			}
		}else if (axis == 2){
			switch (meta){
			case 4:
				return 0.15f;
			case 3:
				return 0.85f;
			default:
				return 0.5f;
			}
		}

		return 0.5f;
	}

	public void invertDetection(){
		this.swapDetectionMode = !this.swapDetectionMode;
	}

	public boolean isInvertingDetection(){
		return this.swapDetectionMode;
	}

	private SpriteRenderInfo GetWeightedRandomAnimation(){
		currentAnimation.reset(false);

		int randomNumber = new Random().nextInt(100);
		int index = 0;

		SpriteRenderInfo current = animations.get(0);

		for (Integer i : animationWeighting){
			if (randomNumber < i){
				current = animations.get(index);
				break;
			}
			index++;
			randomNumber -= i;
		}

		return current == animations.get(0) ? animations.get(1) : current;
	}

	public boolean isActive(){
		if (this.worldObj == null)
			return false;
		return PowerNodeRegistry.For(this.worldObj).checkPower(this, PowerTypes.LIGHT, this.hasSight ? 2 : 1) && GetSearchRadius() > 0;
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void updateEntity(){
		super.updateEntity();

		if (!worldObj.isRemote && isActive()){
			if (hasSight)
				PowerNodeRegistry.For(worldObj).consumePower(this, PowerTypes.LIGHT, 0.25f);
			else
				PowerNodeRegistry.For(worldObj).consumePower(this, PowerTypes.LIGHT, 0.125f);
		}

		ticksToNextCheck--;
		if (ticksToNextCheck <= 0 && isActive()){
			ticksToNextCheck = maxTicksToCheck;

			long key = KeystoneUtilities.instance.getKeyFromRunes(getRunesInKey());

			int radius = GetSearchRadius();
			Class searchClass = GetSearchClass();
			ArrayList<Entity> nearbyMobs = new ArrayList<Entity>();
			if (searchClass != null){
				nearbyMobs = (ArrayList<Entity>)this.worldObj.getEntitiesWithinAABB(searchClass, AxisAlignedBB.getBoundingBox(xCoord - radius, yCoord - radius, zCoord - radius, xCoord + radius, yCoord + radius, zCoord + radius));

				if (key > 0){
					ArrayList<Entity> mobsToIgnore = new ArrayList<Entity>();
					for (Entity e : nearbyMobs){
						if (swapDetectionMode){
							if (!(e instanceof EntityPlayer)){
								mobsToIgnore.add(e);
								continue;
							}
							if (!KeystoneUtilities.instance.GetKeysInInvenory((EntityLivingBase)e).contains(key)){
								mobsToIgnore.add(e);
							}
						}else{
							if (!(e instanceof EntityPlayer)) continue;
							if (KeystoneUtilities.instance.GetKeysInInvenory((EntityLivingBase)e).contains(key)){
								mobsToIgnore.add(e);
							}
						}
					}
					for (Entity e : mobsToIgnore) nearbyMobs.remove(e);
				}
			}

			if (nearbyMobs.size() > 0){
				if (!hasSight){
					hasSight = true;
					notifyNeighborsOfPowerChange();

					if (worldObj.isRemote){
						currentAnimation.reset(false);
						currentAnimation = animations.get(0);
						currentAnimation.reset(true);
					}
				}
			}else{
				if (hasSight){
					hasSight = false;
					notifyNeighborsOfPowerChange();

					if (worldObj.isRemote){
						currentAnimation.reset(false);
						currentAnimation = animations.get(0);
						currentAnimation.reset(false);
					}
				}
			}
		}else{
			if (hasSight && !isActive()){
				hasSight = false;
				notifyNeighborsOfPowerChange();

				if (worldObj.isRemote){
					currentAnimation.reset(false);
					currentAnimation = animations.get(0);
					currentAnimation.reset(false);
				}
			}
		}

		//animations
		if (worldObj.isRemote){
			if (!currentAnimation.isDone){
				tickCounter++;
				if (tickCounter == currentAnimation.speed){
					tickCounter = 0;
					currentAnimation.incrementIndex();
				}
			}else{
				if (isActive() && hasSight){
					currentAnimation = GetWeightedRandomAnimation();
				}
			}

			if (isActive() && hasSight){

				int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

				double yaw = 0;
				double y = yCoord + 0.5;
				double x = xCoord + 0.5;
				double z = zCoord + 0.5;

				switch (meta){
				case 1:
					y += 0.3;
					break;
				case 2:
					y -= 0.3;
					break;
				case 3:
					yaw = 270;
					z += 0.3;
					break;
				case 4:
					yaw = 90;
					z -= 0.3;
					break;
				case 5:
					yaw = 180;
					x += 0.3;
					break;
				case 6:
					yaw = 0;
					x -= 0.3;
					break;
				}

				AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "sparkle2", x, y, z);
				if (effect != null){
					effect.setIgnoreMaxAge(false);
					effect.setMaxAge(35);
					//effect.setRGBColorF(0.9f, 0.7f, 0.0f);

					switch (meta){
					case 1:
						effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, -0.01f, 1, false));
						break;
					case 2:
						effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, 0.01f, 1, false));
						break;
					case 3:
					case 4:
					case 5:
					case 6:
						effect.AddParticleController(new ParticleMoveOnHeading(effect, yaw, 0, 0.01f, 1, false));
						effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, 0, 1, false));
					}
				}
			}
		}
	}

	private void notifyNeighborsOfPowerChange(){
		this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, BlocksCommonProxy.seerStone);
		switch (this.getBlockMetadata()){
		case 0:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, BlocksCommonProxy.seerStone);
			break;
		case 1:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, BlocksCommonProxy.seerStone);
			break;
		case 2:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, BlocksCommonProxy.seerStone);
			break;
		case 3:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord + 1, BlocksCommonProxy.seerStone);
			break;
		case 4:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord - 1, BlocksCommonProxy.seerStone);
			break;
		case 5:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord + 1, yCoord, zCoord, BlocksCommonProxy.seerStone);
			break;
		case 6:
			this.worldObj.notifyBlocksOfNeighborChange(xCoord - 1, yCoord, zCoord, BlocksCommonProxy.seerStone);
			break;
		}
	}

	public boolean ShouldAnimate(){
		return (isActive() && hasSight) || !currentAnimation.isDone;
	}

	public int getAnimationIndex(){
		return currentAnimation.curFrame;
	}

	private int GetSearchRadius(){
		int focusLevel = -1;
		int inventoryIndex = 0;

		if (inventory[inventoryIndex] != null && inventory[inventoryIndex].getItem() instanceof ISpellFocus){
			int tempFocusLevel = ((ISpellFocus)inventory[inventoryIndex].getItem()).getFocusLevel();
			if (tempFocusLevel > focusLevel){
				focusLevel = tempFocusLevel;
			}
		}
		int radius = (focusLevel + 1) * 5;
		return radius;
	}

	private Class GetSearchClass(){
		if (inventory[1] != null && inventory[1].getItem() instanceof ItemFilterFocus){
			return ((ItemFilterFocus)inventory[1].getItem()).getFilterClass();
		}
		return null;
	}

	public boolean HasSight(){
		return isActive() && this.hasSight;
	}

	@Override
	public int getSizeInventory(){
		return 5;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[2];
		runes[1] = inventory[3];
		runes[2] = inventory[4];
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
		return "Seer Stone";
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
	public void openChest(){
	}

	@Override
	public void closeChest(){
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		this.swapDetectionMode = nbttagcompound.getBoolean("seerStoneIsInverting");
		NBTTagList nbttaglist = nbttagcompound.getTagList("SeerStoneInventory", Constants.NBT.TAG_COMPOUND);
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

		nbttagcompound.setBoolean("seerStoneIsInverting", this.isInvertingDetection());

		nbttagcompound.setTag("SeerStoneInventory", nbttaglist);
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	private void writeInventory(AMDataWriter writer){
		for (ItemStack stack : inventory){
			if (stack == null){
				writer.add(false);
				continue;
			}else{
				writer.add(true);
				writer.add(stack);
			}
		}
	}

	@Override
	public boolean isCustomInventoryName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 20;
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return validTypes;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}
}
