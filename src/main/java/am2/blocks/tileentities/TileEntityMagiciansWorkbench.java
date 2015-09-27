package am2.blocks.tileentities;

import am2.api.blocks.IKeystoneLockable;
import am2.blocks.BlockMagiciansWorkbench;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.LinkedList;

public class TileEntityMagiciansWorkbench extends TileEntity implements IInventory, IKeystoneLockable, ISidedInventory{

	private ItemStack[] inventory;
	public IInventory firstCraftResult;
	public IInventory secondCraftResult;

	private final LinkedList<RememberedRecipe> rememberedRecipes;
	private byte upgradeState = 0;
	public static final byte UPG_CRAFT = 0x1;
	public static final byte UPG_ADJ_INV = 0x2;
	private int numPlayersUsing = 0;
	private float drawerOffset = 0;
	private float prevDrawerOffset = 0;
	private static final float drawerIncrement = 0.05f;
	private static final float drawerMax = 0.5f;
	private static final float drawerMin = 0.0f;

	private static final byte REMEMBER_RECIPE = 0x0;
	private static final byte FORGET_RECIPE = 0x1;
	private static final byte SYNC_REMEMBERED_RECIPES = 0x2;
	private static final byte LOCK_RECIPE = 0x4;

	public TileEntityMagiciansWorkbench(){
		inventory = new ItemStack[getSizeInventory()];
		firstCraftResult = new InventoryCraftResult();
		secondCraftResult = new InventoryCraftResult();

		rememberedRecipes = new LinkedList<RememberedRecipe>();
	}

	@Override
	public void updateEntity(){
		setPrevDrawerOffset(getDrawerOffset());

		if (numPlayersUsing > 0){
			if (getDrawerOffset() == drawerMin){
				//sound could go here
			}
			if (getDrawerOffset() < drawerMax){
				setDrawerOffset(getDrawerOffset() + drawerIncrement);
			}else{
				setDrawerOffset(drawerMax);
			}
		}else{
			if (getDrawerOffset() == drawerMax){
				this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (getDrawerOffset() - drawerIncrement > drawerMin){
				setDrawerOffset(getDrawerOffset() - drawerIncrement);
			}else{
				setDrawerOffset(drawerMin);
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int par1, int par2){
		if (par1 == 1){
			this.numPlayersUsing = par2;
			return true;
		}else{
			return super.receiveClientEvent(par1, par2);
		}
	}

	public float getPrevDrawerOffset(){
		return prevDrawerOffset;
	}

	public void setPrevDrawerOffset(float prevDrawerOffset){
		this.prevDrawerOffset = prevDrawerOffset;
	}

	public float getDrawerOffset(){
		return drawerOffset;
	}

	public void setDrawerOffset(float drawerOffset){
		this.drawerOffset = drawerOffset;
	}

	@Override
	public void openInventory(){
		if (this.numPlayersUsing < 0){
			this.numPlayersUsing = 0;
		}

		++this.numPlayersUsing;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
	}

	@Override
	public void closeInventory(){
		if (this.getBlockType() != null && this.getBlockType() instanceof BlockMagiciansWorkbench){
			--this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
		}
	}

	public boolean getUpgradeStatus(byte flag){
		return (upgradeState & flag) == flag;
	}

	public void setUpgradeStatus(byte flag, boolean set){
		if (set)
			upgradeState |= flag;
		else
			upgradeState &= ~flag;

		if (!worldObj.isRemote)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public void rememberRecipe(ItemStack output, ItemStack[] recipeItems, boolean is2x2){
		for (RememberedRecipe recipe : rememberedRecipes){
			if (recipe.output.isItemEqual(output))
				return;
		}
		if (!popRecipe()){
			return;
		}

		for (ItemStack stack : recipeItems)
			if (stack != null)
				stack.stackSize = 1;

		rememberedRecipes.add(new RememberedRecipe(output, recipeItems, is2x2));

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	private boolean popRecipe(){

		if (rememberedRecipes.size() < 8)
			return true;

		int index = 0;
		while (index < rememberedRecipes.size()){
			if (!rememberedRecipes.get(index).isLocked){
				rememberedRecipes.remove(index);
				return true;
			}
			index++;
		}

		return false;
	}

	public LinkedList<RememberedRecipe> getRememberedRecipeItems(){
		return rememberedRecipes;
	}

	@Override
	public int getSizeInventory(){
		return 48;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		if (i < 0 || i >= getSizeInventory())
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
		return "Magician's Workbench";
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		if (i > getStorageStart())
			return true;
		return false;
	}

	public class RememberedRecipe{
		public final ItemStack output;
		public final ItemStack[] components;
		private boolean isLocked;
		public final boolean is2x2;

		public RememberedRecipe(ItemStack output, ItemStack[] components, boolean is2x2){
			this.output = output;
			this.components = components;
			this.isLocked = false;
			this.is2x2 = is2x2;
		}

		public void lock(){
			this.isLocked = true;
		}

		public void unlock(){
			this.isLocked = false;
		}

		public boolean isLocked(){
			return isLocked;
		}
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

	public void setRecipeLocked(int index, boolean locked){
		if (index >= 0 && index < rememberedRecipes.size())
			rememberedRecipes.get(index).isLocked = locked;

		if (worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(xCoord);
			writer.add(yCoord);
			writer.add(zCoord);
			writer.add(index);
			writer.add(locked);
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.M_BENCH_LOCK_RECIPE, writer.generate());
		}
	}

	public void toggleRecipeLocked(int index){
		if (index >= 0 && index < rememberedRecipes.size())
			setRecipeLocked(index, !rememberedRecipes.get(index).isLocked);
	}

	public int getStorageStart(){
		return 18;
	}

	public int getStorageSize(){
		return 27;
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

		NBTTagList recall = nbttagcompound.getTagList("rememberedRecipes", Constants.NBT.TAG_COMPOUND);
		rememberedRecipes.clear();
		for (int i = 0; i < recall.tagCount(); ++i){
			NBTTagCompound rememberedRecipe = (NBTTagCompound)recall.getCompoundTagAt(i);
			ItemStack output = ItemStack.loadItemStackFromNBT(rememberedRecipe);
			boolean is2x2 = rememberedRecipe.getBoolean("is2x2");
			NBTTagList componentNBT = rememberedRecipe.getTagList("components", Constants.NBT.TAG_COMPOUND);
			ItemStack[] components = new ItemStack[componentNBT.tagCount()];
			for (int n = 0; n < componentNBT.tagCount(); ++n){
				NBTTagCompound componentTAG = (NBTTagCompound)componentNBT.getCompoundTagAt(n);
				if (componentTAG.getBoolean("componentExisted")){
					ItemStack component = ItemStack.loadItemStackFromNBT(componentTAG);
					components[n] = component;
				}else{
					components[n] = null;
				}
			}

			RememberedRecipe rec = new RememberedRecipe(output, components, is2x2);
			rec.isLocked = rememberedRecipe.getBoolean("isLocked");
			rememberedRecipes.add(rec);
		}

		this.upgradeState = nbttagcompound.getByte("upgradestate");
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

		//remembered recipes
		NBTTagList recall = new NBTTagList();
		int count = 0;
		for (RememberedRecipe recipe : rememberedRecipes){
			try{
				NBTTagCompound output = new NBTTagCompound();
				recipe.output.writeToNBT(output);
				output.setBoolean("is2x2", recipe.is2x2);
				NBTTagList components = new NBTTagList();
				for (int i = 0; i < recipe.components.length; ++i){
					NBTTagCompound component = new NBTTagCompound();
					component.setBoolean("componentExisted", recipe.components[i] != null);
					if (recipe.components[i] != null)
						recipe.components[i].writeToNBT(component);
					components.appendTag(component);
				}
				output.setTag("components", components);
				output.setBoolean("isLocked", recipe.isLocked);
				recall.appendTag(output);
			}catch (Throwable t){
				//no log, as this is likely due to a mod being removed and the recipe no longer exists.
			}
		}

		nbttagcompound.setTag("rememberedRecipes", recall);
		nbttagcompound.setByte("upgradestate", upgradeState);
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[45];
		runes[1] = inventory[46];
		runes[2] = inventory[47];
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
	public int[] getAccessibleSlotsFromSide(int var1){
		int[] slots = new int[getStorageSize()];
		for (int i = 0; i < slots.length; ++i){
			slots[i] = i + getStorageStart();
		}
		return slots;
	}


	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j){
		if (i > getStorageStart())
			return true;
		return false;
	}


	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j){
		if (i > getStorageStart())
			return true;
		return false;
	}
}
