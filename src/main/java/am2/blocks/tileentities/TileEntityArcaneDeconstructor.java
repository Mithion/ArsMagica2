package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.power.PowerTypes;
import am2.api.spell.component.interfaces.*;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleHoldPosition;
import am2.power.PowerNodeRegistry;
import am2.spell.SkillManager;
import am2.spell.SpellUtils;
import am2.spell.components.Summon;
import am2.spell.shapes.Binding;
import am2.utility.InventoryUtilities;
import am2.utility.RecipeUtilities;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class TileEntityArcaneDeconstructor extends TileEntityAMPower implements ISidedInventory, IKeystoneLockable{

	private int particleCounter;
	private static final float DECONSTRUCTION_POWER_COST = 1.25f; //power cost per tick
	private static final int DECONSTRUCTION_TIME = 200; //how long does it take to deconstruct something?
	private int current_deconstruction_time = 0; //how long have we been deconstructing something?

	private static final PowerTypes[] validPowerTypes = new PowerTypes[]{PowerTypes.DARK};

	@SideOnly(Side.CLIENT)
	AMParticle radiant;

	private ItemStack[] inventory;

	private ItemStack[] deconstructionRecipe;

	public TileEntityArcaneDeconstructor(){
		super(500);
		inventory = new ItemStack[getSizeInventory()];
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 250;
	}
	
	@Override
	public void update(){
		super.update();

		if (worldObj.isRemote){
			if (particleCounter == 0 || particleCounter++ > 1000){
				particleCounter = 1;
				radiant = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "radiant", pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
				if (radiant != null){
					radiant.setMaxAge(1000);
					radiant.setRGBColorF(0.1f, 0.1f, 0.1f);
					radiant.setParticleScale(0.1f);
					radiant.AddParticleController(new ParticleHoldPosition(radiant, 1000, 1, false));
				}
			}
		}else{
			if (!isActive()){
				if (inventory[0] != null){
					current_deconstruction_time = 1;
				}
			}else{
				if (inventory[0] == null){
					current_deconstruction_time = 0;
					deconstructionRecipe = null;
					worldObj.markBlockForUpdate(pos);
				}else{
					if (PowerNodeRegistry.For(worldObj).checkPower(this, PowerTypes.DARK, DECONSTRUCTION_POWER_COST)){
						if (deconstructionRecipe == null){
							if (!getDeconstructionRecipe()){
								transferOrEjectItem(inventory[0]);
								setInventorySlotContents(0, null);
							}
						}else{
							if (current_deconstruction_time++ >= DECONSTRUCTION_TIME){
							        if(getDeconstructionRecipe() == true){
									for (ItemStack stack : deconstructionRecipe){
										transferOrEjectItem(stack);
									}
								}
								deconstructionRecipe = null;
								decrStackSize(0, 1);
								current_deconstruction_time = 0;
							}
							if (current_deconstruction_time % 10 == 0)
								worldObj.markBlockForUpdate(pos);
						}
						PowerNodeRegistry.For(worldObj).consumePower(this, PowerTypes.DARK, DECONSTRUCTION_POWER_COST);
					}
				}
			}
		}
	}

	private boolean getDeconstructionRecipe(){
		ItemStack checkStack = getStackInSlot(0);
		ArrayList<ItemStack> recipeItems = new ArrayList<ItemStack>();
		if (checkStack == null)
			return false;
		if (checkStack.getItem() == ItemsCommonProxy.spell){
			int numStages = SpellUtils.instance.numStages(checkStack);

			for (int i = 0; i < numStages; ++i){
				ISpellShape shape = SpellUtils.instance.getShapeForStage(checkStack, i);
				Object[] componentParts = shape.getRecipeItems();
				if (componentParts != null){
					for (Object o : componentParts){
						ItemStack stack = objectToItemStack(o);
						if (stack != null){
							if (stack.getItem() == ItemsCommonProxy.bindingCatalyst){
								stack.setItemDamage(((Binding)SkillManager.instance.getSkill("Binding")).getBindingType(checkStack));
							}
							recipeItems.add(stack.copy());
						}
					}
				}
				ISpellComponent[] components = SpellUtils.instance.getComponentsForStage(checkStack, i);
				for (ISpellComponent component : components){
					componentParts = component.getRecipeItems();
					if (componentParts != null){
						for (Object o : componentParts){
							ItemStack stack = objectToItemStack(o);
							if (stack != null){
								if (stack.getItem() == ItemsCommonProxy.crystalPhylactery){
									ItemsCommonProxy.crystalPhylactery.setSpawnClass(stack,
											((Summon)SkillManager.instance.getSkill("Summon")).getSummonType(checkStack));
									ItemsCommonProxy.crystalPhylactery.addFill(stack, 100);
								}
								recipeItems.add(stack.copy());
							}
						}
					}
				}
				ISpellModifier[] modifiers = SpellUtils.instance.getModifiersForStage(checkStack, i);
				for (ISpellModifier modifier : modifiers){
					componentParts = modifier.getRecipeItems();
					if (componentParts != null){
						for (Object o : componentParts){
							ItemStack stack = objectToItemStack(o);
							if (stack != null)
								recipeItems.add(stack.copy());
						}
					}
				}
			}

			int numShapeGroups = SpellUtils.instance.numShapeGroups(checkStack);
			for (int i = 0; i < numShapeGroups; ++i){
				int[] parts = SpellUtils.instance.getShapeGroupParts(checkStack, i);
				for (int partID : parts){
					ISkillTreeEntry entry = SkillManager.instance.getSkill(partID);
					if (entry != null && entry instanceof ISpellPart){
						Object[] componentParts = ((ISpellPart)entry).getRecipeItems();
						if (componentParts != null){
							for (Object o : componentParts){
								ItemStack stack = objectToItemStack(o);
								if (stack != null){
									if (stack.getItem() == ItemsCommonProxy.bindingCatalyst){
										stack.setItemDamage(((Binding)SkillManager.instance.getSkill("Binding")).getBindingType(checkStack));
									}
									recipeItems.add(stack.copy());
								}
							}
						}
					}
				}
			}

			deconstructionRecipe = recipeItems.toArray(new ItemStack[recipeItems.size()]);
			return true;
		}else{
			IRecipe recipe = RecipeUtilities.getRecipeFor(checkStack);
			if (recipe == null)
				return false;
			Object[] recipeParts = RecipeUtilities.getRecipeItems(recipe);
			if (recipeParts != null && checkStack != null && recipe.getRecipeOutput() != null){
				if (recipe.getRecipeOutput().getItem() == checkStack.getItem() && recipe.getRecipeOutput().getItemDamage() == checkStack.getItemDamage() && recipe.getRecipeOutput().stackSize > 1)
					return false;

				for (Object o : recipeParts){
					ItemStack stack = objectToItemStack(o);
					if (stack != null && !stack.getItem().hasContainerItem(stack)){
						stack.stackSize = 1;
						recipeItems.add(stack.copy());
					}
				}
			}
			deconstructionRecipe = recipeItems.toArray(new ItemStack[recipeItems.size()]);
			return true;
		}
	}

	private ItemStack objectToItemStack(Object o){
		ItemStack output = null;
		if (o instanceof ItemStack)
			output = (ItemStack)o;
		else if (o instanceof Item)
			output = new ItemStack((Item)o);
		else if (o instanceof Block)
			output = new ItemStack((Block)o);
		else if (o instanceof ArrayList)
			output = objectToItemStack(((ArrayList)o).get(0));

		if (output != null){
			if (output.stackSize == 0)
				output.stackSize = 1;
		}

		return output;
	}

	private void transferOrEjectItem(ItemStack stack){
		if (worldObj.isRemote)
			return;

		boolean eject = false;
		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				for (int k = -1; k <= 1; ++k){
					if (i == 0 && j == 0 && k == 0)
						continue;
					TileEntity te = worldObj.getTileEntity(pos.add(i, j, k));
					if (te != null && te instanceof IInventory){
						for (EnumFacing side : EnumFacing.values()){
							if (InventoryUtilities.mergeIntoInventory((IInventory)te, stack, stack.stackSize, side))
								return;
						}
					}
				}
			}
		}

		//eject the remainder
		EntityItem item = new EntityItem(worldObj);
		item.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
		item.setEntityItemStack(stack);
		worldObj.spawnEntityInWorld(item);
	}

	public boolean isActive(){
		return current_deconstruction_time > 0;
	}

	@Override
	public int getSizeInventory(){
		return 16;
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
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (worldObj.getTileEntity(pos) != this){
			return false;
		}
		return entityplayer.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return i <= 9;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		return new ItemStack[]{
				inventory[13],
				inventory[14],
				inventory[15]
		};
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
	public PowerTypes[] getValidPowerTypes(){
		return validPowerTypes;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("DeconstructorInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.current_deconstruction_time = nbttagcompound.getInteger("DeconstructionTime");

		if (current_deconstruction_time > 0)
			getDeconstructionRecipe();
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

		nbttagcompound.setTag("DeconstructorInventory", nbttaglist);

		nbttagcompound.setInteger("DeconstructionTime", current_deconstruction_time);
	}

	public int getProgressScaled(int i){
		return current_deconstruction_time * i / DECONSTRUCTION_TIME;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = inventory[index].copy();
		inventory[index] = null;
		return stack;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < getSizeInventory(); i++)
			inventory[i] = null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ArcaneDeconstructor";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction) {
		return index >= 1 && index <= 9;
	}
}
