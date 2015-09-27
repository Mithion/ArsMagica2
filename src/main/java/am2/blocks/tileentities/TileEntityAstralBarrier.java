package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.power.PowerTypes;
import am2.items.ISpellFocus;
import am2.network.AMDataWriter;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleHoldPosition;
import am2.particles.ParticleOrbitPoint;
import am2.power.PowerNodeRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TileEntityAstralBarrier extends TileEntityAMPower implements IInventory, IKeystoneLockable{

	private ItemStack[] inventory;
	private static final int maxRadius = 20;
	private static final int minRadius = 5;
	private boolean displayAura;
	private Random rand;
	private int particleTickCounter;

	public static int keystoneSlot = 0;

	public TileEntityAstralBarrier(){
		super(250);
		inventory = new ItemStack[getSizeInventory()];
		displayAura = false;
		rand = new Random();
		particleTickCounter = 0;
	}

	public void ToggleAuraDisplay(){
		this.displayAura = !this.displayAura;
	}

	public int getRadius(){
		if (this.inventory[0] != null && this.inventory[0].getItem() instanceof ISpellFocus){
			ISpellFocus focus = (ISpellFocus)this.inventory[0].getItem();
			return (focus.getFocusLevel() + 1) * 5;
		}
		return 0;
	}

	public boolean IsActive(){
		return PowerNodeRegistry.For(this.worldObj).checkPower(this, 0.35f * getRadius()) && worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && getRadius() > 0;
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void updateEntity(){
		super.updateEntity();

		int radius = getRadius();

		if (IsActive()){
			PowerNodeRegistry.For(this.worldObj).consumePower(this, PowerNodeRegistry.For(worldObj).getHighestPowerType(this), 0.35f * radius);
		}

		if (worldObj.isRemote){
			if (IsActive()){
				if (displayAura){
					AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "symbols", xCoord, yCoord + 0.5, zCoord);
					if (effect != null){
						effect.setIgnoreMaxAge(false);
						effect.setMaxAge(100);
						effect.setParticleScale(0.5f);
						effect.AddParticleController(new ParticleOrbitPoint(effect, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 1, false).SetOrbitSpeed(0.03).SetTargetDistance(radius));
					}
				}

				particleTickCounter++;

				if (particleTickCounter >= 15){

					particleTickCounter = 0;

					String particleName = "";
					AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "sparkle", xCoord + 0.5, yCoord + 0.1 + rand.nextDouble() * 0.5, zCoord + 0.5);
					if (effect != null){
						effect.setIgnoreMaxAge(false);
						effect.setMaxAge(100);
						effect.setParticleScale(0.5f);
						float color = rand.nextFloat() * 0.2f + 0.8f;
						effect.AddParticleController(new ParticleOrbitPoint(effect, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 1, false).SetOrbitSpeed(0.005).SetTargetDistance(rand.nextDouble() * 0.6 - 0.3));
						effect.AddParticleController(new ParticleHoldPosition(effect, 80, 2, true));
						effect.AddParticleController(new ParticleFadeOut(effect, 3, false).setFadeSpeed(0.05f));
					}
				}
			}
		}
	}

	public void onEntityBlocked(EntityLivingBase entity){
		if (this.worldObj.isRemote){
			if (PowerNodeRegistry.For(getWorldObj()).checkPower(this, PowerTypes.DARK, 50)){
				entity.attackEntityFrom(DamageSource.magic, 5);
				PowerNodeRegistry.For(getWorldObj()).consumePower(this, PowerTypes.DARK, 50);
			}
		}
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
		return "Astral Barrier";
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
		NBTTagList nbttaglist = nbttagcompound.getTagList("AstralBarrierInventory", Constants.NBT.TAG_COMPOUND);
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

		nbttagcompound.setTag("AstralBarrierInventory", nbttaglist);
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
	public boolean hasCustomInventoryName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 50;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[1];
		runes[1] = inventory[2];
		runes[2] = inventory[3];
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
}
