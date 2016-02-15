package am2.blocks.tileentities;

import am2.AMCore;
import am2.LogHelper;
import am2.api.events.SpellRecipeItemsEvent;
import am2.api.power.PowerTypes;
import am2.api.spell.component.interfaces.*;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlockInscriptionTable;
import am2.containers.ContainerInscriptionTable;
import am2.items.ItemsCommonProxy;
import am2.lore.Story;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleHoldPosition;
import am2.spell.SkillManager;
import am2.spell.SpellRecipeManager;
import am2.spell.SpellUtils;
import am2.spell.SpellValidator;
import am2.utility.KeyValuePair;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class TileEntityInscriptionTable extends TileEntity implements IInventory, ITickable{

	private ItemStack inscriptionTableItemStacks[];
	private final ArrayList<ISpellPart> currentRecipe;
	private final ArrayList<ArrayList<ISpellPart>> shapeGroups;
	private int numStageGroups = 2;
	public static final int MAX_STAGE_GROUPS = 5;
	public static int bookIndex = 0;
	public static int paperIndex = 1;
	public static int featherIndex = 2;
	public static int inkIndex = 3;
	private EntityPlayer currentPlayerUsing;
	private int ticksToNextParticle = 20;
	private final HashMap<SpellModifiers, Integer> modifierCount;
	private String currentSpellName;
	private boolean currentSpellIsReadOnly;

	private static final byte FULL_UPDATE = 0x1;
	private static final byte MAKE_SPELL = 0x2;
	private static final byte RESET_NAME = 0x4;

	public TileEntityInscriptionTable(){
		inscriptionTableItemStacks = new ItemStack[getSizeInventory()];
		currentPlayerUsing = null;
		currentSpellName = "";
		currentRecipe = new ArrayList<ISpellPart>();
		shapeGroups = new ArrayList<ArrayList<ISpellPart>>();

		for (int i = 0; i < MAX_STAGE_GROUPS; ++i){
			shapeGroups.add(new ArrayList<ISpellPart>());
		}

		modifierCount = new HashMap<SpellModifiers, Integer>();
		resetModifierCount();
	}

	public ArrayList<ISpellPart> getCurrentRecipe(){
		return this.currentRecipe;
	}

	@Override
	public int getSizeInventory(){
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		return inscriptionTableItemStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (inscriptionTableItemStacks[i] != null){
			if (inscriptionTableItemStacks[i].stackSize <= j){
				ItemStack itemstack = inscriptionTableItemStacks[i];
				inscriptionTableItemStacks[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inscriptionTableItemStacks[i].splitStack(j);
			if (inscriptionTableItemStacks[i].stackSize == 0){
				inscriptionTableItemStacks[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		inscriptionTableItemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "Inscription Table";
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

	public boolean isInUse(EntityPlayer player){
		return currentPlayerUsing != null && currentPlayerUsing.getEntityId() != player.getEntityId();
	}

	public void setInUse(EntityPlayer player){
		this.currentPlayerUsing = player;
		if (!this.worldObj.isRemote){
			worldObj.markBlockForUpdate(pos);
		}
	}

	public EntityPlayer getCurrentPlayerUsing(){
		return this.currentPlayerUsing;
	}

	private boolean isRenderingLeft(){
		return worldObj.getBlockState(getPos()).getValue(BlockInscriptionTable.LEFT);
	}

	@Override
	public void update(){
		if (worldObj.isRemote && getUpgradeState() >= 3)
			candleUpdate();

		if (this.numStageGroups > MAX_STAGE_GROUPS)
			this.numStageGroups = MAX_STAGE_GROUPS;
	}

	public int getUpgradeState(){
		return this.numStageGroups - 2;
	}

	private void candleUpdate(){
		ticksToNextParticle--;

		if (isRenderingLeft()){
			if (ticksToNextParticle == 0 || ticksToNextParticle == 15){

				EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockInscriptionTable.FACING);

				double particleX = 0;
				double particleZ = 0;

				switch (facing){
				case NORTH:
					particleX = this.pos.getX() + 0.15;
					particleZ = this.pos.getZ() + 0.22;
					break;
				case EAST:
					particleX = this.pos.getX() + 0.22;
					particleZ = this.pos.getZ() + 0.85;
					break;
				case SOUTH:
					particleX = this.pos.getX() + 0.83;
					particleZ = this.pos.getZ() + 0.78;
					break;
				case WEST:
					particleX = this.pos.getX() + 0.79;
					particleZ = this.pos.getZ() + 0.15;
					break;
				}

				ticksToNextParticle = 30;
				AMParticle effect = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "fire", particleX, pos.getY() + 1.32, particleZ);
				if (effect != null){
					effect.setParticleScale(0.025f, 0.1f, 0.025f);
					effect.AddParticleController(new ParticleHoldPosition(effect, 29, 1, false));
					effect.setIgnoreMaxAge(false);
					effect.setMaxAge(400);
				}

				if (worldObj.rand.nextInt(100) > 80){
					AMParticle smoke = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "smoke", particleX, pos.getY() + 1.4, particleZ);
					if (smoke != null){
						smoke.setParticleScale(0.025f);
						smoke.AddParticleController(new ParticleFloatUpward(smoke, 0.01f, 0.01f, 1, false));
						smoke.setIgnoreMaxAge(false);
						smoke.setMaxAge(20 + worldObj.rand.nextInt(10));
					}
				}
			}
			if (ticksToNextParticle == 10 || ticksToNextParticle == 25){

				EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockInscriptionTable.FACING);

				double particleX = 0;
				double particleZ = 0;

				switch (facing){
				case NORTH:
					particleX = this.pos.getX() + 0.59;
					particleZ = this.pos.getZ() - 0.72;
					break;
				case EAST:
					particleX = this.pos.getX() - 0.72;
					particleZ = this.pos.getZ() + 0.41;
					break;
				case SOUTH:
					particleX = this.pos.getX() + 0.41;
					particleZ = this.pos.getZ() + 1.72;
					break;
				case WEST:
					particleX = this.pos.getX() + 1.72;
					particleZ = this.pos.getZ() + 0.60;
					break;
				}

				AMParticle effect = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "fire", particleX, pos.getY() + 1.26, particleZ);
				if (effect != null){
					effect.setParticleScale(0.025f, 0.1f, 0.025f);
					effect.AddParticleController(new ParticleHoldPosition(effect, 29, 1, false));
					effect.setIgnoreMaxAge(false);
					effect.setMaxAge(400);
				}

				if (worldObj.rand.nextInt(100) > 80){
					AMParticle smoke = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "smoke", particleX, pos.getY() + 1.4, particleZ);
					if (smoke != null){
						smoke.setParticleScale(0.025f);
						smoke.AddParticleController(new ParticleFloatUpward(smoke, 0.01f, 0.01f, 1, false));
						smoke.setIgnoreMaxAge(false);
						smoke.setMaxAge(20 + worldObj.rand.nextInt(10));
					}
				}
			}

		}
	}

	private boolean InventorySlotHasItem(int index, Item item, int meta){
		if (inscriptionTableItemStacks[index] == null) return false;
		if (inscriptionTableItemStacks[index].getItem() == null) return false;
		if (inscriptionTableItemStacks[index].getItem() != item) return false;
		if (meta > -1 && inscriptionTableItemStacks[index].getItemDamage() != meta) return false;

		return true;
	}

	private ItemStack[] getCraftingGridContents(){
		ItemStack[] contents = new ItemStack[10];
		for (int i = 0; i < 10; ++i){
			contents[i] = inscriptionTableItemStacks[i];
		}

		return contents;
	}

	@Override
	public void openInventory(EntityPlayer p){
	}

	@Override
	public void closeInventory(EntityPlayer p){
	}

	@Override
	public ItemStack removeStackFromSlot(int i){
		if (inscriptionTableItemStacks[i] != null){
			ItemStack itemstack = inscriptionTableItemStacks[i];
			inscriptionTableItemStacks[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound){
		super.readFromNBT(par1NBTTagCompound);
		parseTagCompound(par1NBTTagCompound);
		clearCurrentRecipe();
	}

	private void parseTagCompound(NBTTagCompound par1NBTTagCompound){
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("InscriptionTableInventory", Constants.NBT.TAG_COMPOUND);
		inscriptionTableItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inscriptionTableItemStacks.length){
				inscriptionTableItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.numStageGroups = Math.max(par1NBTTagCompound.getInteger("numShapeGroupSlots"), 2);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound){
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inscriptionTableItemStacks.length; i++){
			if (inscriptionTableItemStacks[i] != null){
				String tag = String.format("ArrayIndex", i);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte(tag, (byte)i);
				inscriptionTableItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		par1NBTTagCompound.setTag("InscriptionTableInventory", nbttaglist);
		par1NBTTagCompound.setInteger("numShapeGroupSlots", this.numStageGroups);
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	public void HandleUpdatePacket(byte[] data){
		if (this.worldObj == null)
			return;
		AMDataReader rdr = new AMDataReader(data);
		switch (rdr.ID){
		case FULL_UPDATE:
			if (!rdr.getBoolean()){
				Entity e = this.worldObj.getEntityByID(rdr.getInt());
				if (e instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer)e;
					this.setInUse(player);
				}else{
					this.setInUse(null);
				}
			}else{
				this.setInUse(null);
			}

			currentRecipe.clear();

			int partLength = rdr.getInt();
			for (int i = 0; i < partLength; ++i){
				ISkillTreeEntry part = SkillManager.instance.getSkill(rdr.getInt());
				if (part instanceof ISpellPart)
					this.currentRecipe.add((ISpellPart)part);
			}

			this.shapeGroups.clear();
			int numGroups = rdr.getInt();
			for (int i = 0; i < numGroups; ++i){
				ArrayList<ISpellPart> group = new ArrayList<ISpellPart>();
				int[] partData = rdr.getIntArray();
				for (int n : partData){
					ISkillTreeEntry part = SkillManager.instance.getSkill(n);
					if (part instanceof ISpellPart)
						group.add((ISpellPart)part);
				}
				this.shapeGroups.add(group);
			}

			countModifiers();
			this.currentSpellName = rdr.getString();
			this.currentSpellIsReadOnly = rdr.getBoolean();
			break;
		case MAKE_SPELL:
			int entityID = rdr.getInt();
			EntityPlayer player = (EntityPlayer)worldObj.getEntityByID(entityID);
			if (player != null){
				createSpellForPlayer(player);
			}
			break;
		case RESET_NAME:
			entityID = rdr.getInt();
			player = (EntityPlayer)worldObj.getEntityByID(entityID);
			if (player != null){
				((ContainerInscriptionTable)player.openContainer).resetSpellNameAndIcon();
			}
			break;
		}
	}

	private byte[] GetUpdatePacketForServer(){
		AMDataWriter writer = new AMDataWriter();
		writer.add(FULL_UPDATE);
		writer.add(this.currentPlayerUsing == null);
		if (this.currentPlayerUsing != null) writer.add(this.currentPlayerUsing.getEntityId());

		writer.add(this.currentRecipe.size());
		for (int i = 0; i < this.currentRecipe.size(); ++i){
			ISpellPart part = this.currentRecipe.get(i);
			int id = part.getID();
			if (part instanceof ISpellComponent)
				id += SkillManager.COMPONENT_OFFSET;
			else if (part instanceof ISpellModifier)
				id += SkillManager.MODIFIER_OFFSET;
			writer.add(id);
		}

		writer.add(this.shapeGroups.size());
		for (ArrayList<ISpellPart> shapeGroup : this.shapeGroups){
			int[] groupData = new int[shapeGroup.size()];
			for (int i = 0; i < shapeGroup.size(); ++i){
				groupData[i] = SkillManager.instance.getShiftedPartID(shapeGroup.get(i));
			}
			writer.add(groupData);
		}

		writer.add(currentSpellName);
		writer.add(currentSpellIsReadOnly);

		return writer.generate();
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.parseTagCompound(pkt.getNbtCompound());
	}

	private void sendDataToServer(){
		AMDataWriter writer = new AMDataWriter();
		writer.add(pos.getX());
		writer.add(pos.getY());
		writer.add(pos.getZ());
		writer.add(GetUpdatePacketForServer());

		AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.INSCRIPTION_TABLE_UPDATE, writer.generate());
	}

	public void addSpellPartToStageGroup(int groupIndex, ISpellPart part){
		ArrayList<ISpellPart> group = this.shapeGroups.get(groupIndex);
		if (!currentSpellIsReadOnly && group.size() < 4 && !(part instanceof ISpellComponent)){
			group.add(part);
			if (this.worldObj.isRemote)
				this.sendDataToServer();
			countModifiers();
		}
	}

	public void removeSpellPartFromStageGroup(int index, int groupIndex){
		ArrayList<ISpellPart> group = this.shapeGroups.get(groupIndex);
		if (!this.currentSpellIsReadOnly){
			group.remove(index);
			if (this.worldObj.isRemote)
				this.sendDataToServer();
			countModifiers();
		}
	}

	public void removeMultipleSpellPartsFromStageGroup(int startIndex, int length, int groupIndex){
		ArrayList<ISpellPart> group = this.shapeGroups.get(groupIndex);
		if (!currentSpellIsReadOnly){
			for (int i = 0; i <= length; ++i)
				group.remove(startIndex);
			countModifiers();
			if (this.worldObj.isRemote)
				this.sendDataToServer();
		}
	}

	public void addSpellPart(ISpellPart part){
		if (!currentSpellIsReadOnly && this.currentRecipe.size() < 16){
			this.currentRecipe.add(part);
			if (this.worldObj.isRemote)
				this.sendDataToServer();
			countModifiers();
		}
	}

	public void removeSpellPart(int index){
		if (!this.currentSpellIsReadOnly){
			this.currentRecipe.remove(index);
			if (this.worldObj.isRemote)
				this.sendDataToServer();
			countModifiers();
		}
	}

	public void removeMultipleSpellParts(int startIndex, int length){
		if (!currentSpellIsReadOnly){
			for (int i = 0; i <= length; ++i)
				this.getCurrentRecipe().remove(startIndex);
			countModifiers();
			if (this.worldObj.isRemote)
				this.sendDataToServer();
		}
	}

	public int getNumStageGroups(){
		return this.numStageGroups;
	}

	private void countModifiers(){

		resetModifierCount();

		for (ArrayList<ISpellPart> shapeGroup : this.shapeGroups){
			countModifiersInList(shapeGroup);
		}

		ArrayList<ArrayList<ISpellPart>> stages = SpellValidator.splitToStages(currentRecipe);
		if (stages.size() == 0) return;
		ArrayList<ISpellPart> currentStage = stages.get(stages.size() - 1);
		countModifiersInList(currentStage);
	}

	private void countModifiersInList(ArrayList<ISpellPart> currentStage){
		for (ISpellPart part : currentStage){
			if (part instanceof ISpellModifier){
				EnumSet<SpellModifiers> modifiers = ((ISpellModifier)part).getAspectsModified();
				for (SpellModifiers modifier : modifiers){
					int count = modifierCount.get(modifier) + 1;
					modifierCount.put(modifier, count);
				}
			}
		}
	}

	private void resetModifierCount(){
		modifierCount.clear();
		for (SpellModifiers modifier : SpellModifiers.values()){
			modifierCount.put(modifier, 0);
		}
	}

	public int getModifierCount(SpellModifiers modifier){
		return modifierCount.get(modifier);
	}

	public void createSpellForPlayer(EntityPlayer player){
		if (worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(pos.getX());
			writer.add(pos.getY());
			writer.add(pos.getZ());
			writer.add(MAKE_SPELL);
			writer.add(player.getEntityId());
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.INSCRIPTION_TABLE_UPDATE, writer.generate());
		}else{

			ArrayList<ArrayList<KeyValuePair<ISpellPart, byte[]>>> shapeGroupSetup = new ArrayList<ArrayList<KeyValuePair<ISpellPart, byte[]>>>();
			ArrayList<KeyValuePair<ISpellPart, byte[]>> curRecipeSetup = new ArrayList<KeyValuePair<ISpellPart, byte[]>>();

			for (ArrayList<ISpellPart> arr : shapeGroups){
				shapeGroupSetup.add(new ArrayList<KeyValuePair<ISpellPart, byte[]>>());
				for (ISpellPart part : arr){
					shapeGroupSetup.get(shapeGroupSetup.size() - 1).add(new KeyValuePair<ISpellPart, byte[]>(part, new byte[0]));
				}
			}

			for (ISpellPart part : currentRecipe){
				curRecipeSetup.add(new KeyValuePair<ISpellPart, byte[]>(part, new byte[0]));
			}

			ItemStack stack = SpellUtils.instance.createSpellStack(shapeGroupSetup, curRecipeSetup);

			stack.getTagCompound().setString("suggestedName", currentSpellName);

			player.inventory.addItemStackToInventory(stack);
		}
	}

	public ItemStack writeRecipeAndDataToBook(ItemStack bookstack, EntityPlayer player, String title){
		if (bookstack.getItem() == Items.written_book && this.currentRecipe != null){
			if (!currentRecipeIsValid().valid)
				return bookstack;

			if (!bookstack.hasTagCompound())
				bookstack.setTagCompound(new NBTTagCompound());
			else if (bookstack.getTagCompound().getBoolean("spellFinalized")) //don't overwrite a completed spell
				return bookstack;

			LinkedHashMap<String, Integer> materialsList = new LinkedHashMap<String, Integer>();

			materialsList.put(ItemsCommonProxy.rune.getItemStackDisplayName(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLANK)), 1);

			ArrayList<ItemStack> componentRecipeList = new ArrayList<ItemStack>();
			int count = 0;

			ArrayList<ISpellPart> allRecipeItems = new ArrayList<ISpellPart>();

			for (ArrayList<ISpellPart> shapeGroup : shapeGroups){
				if (shapeGroup == null || shapeGroup.size() == 0)
					continue;
				allRecipeItems.addAll(shapeGroup);
			}

			allRecipeItems.addAll(currentRecipe);

			for (ISpellPart part : allRecipeItems){

				if (part == null){
					LogHelper.error("Unable to write recipe to book.  Recipe part is null!");
					return bookstack;
				}

				Object[] recipeItems = part.getRecipeItems();
				SpellRecipeItemsEvent event = new SpellRecipeItemsEvent(SkillManager.instance.getSkillName(part), SkillManager.instance.getShiftedPartID(part), recipeItems);
				MinecraftForge.EVENT_BUS.post(event);
				recipeItems = event.recipeItems;

				if (recipeItems == null){
					LogHelper.error("Unable to write recipe to book.  Recipe items are null for part %d!", part.getID());
					return bookstack;
				}
				for (int i = 0; i < recipeItems.length; ++i){
					Object o = recipeItems[i];
					String materialkey = "";
					int qty = 1;
					ItemStack recipeStack = null;
					if (o instanceof ItemStack){
						materialkey = ((ItemStack)o).getDisplayName();
						recipeStack = (ItemStack)o;
					}else if (o instanceof Item){
						recipeStack = new ItemStack((Item)o);
						materialkey = ((Item)o).getItemStackDisplayName(new ItemStack((Item)o));
					}else if (o instanceof Block){
						recipeStack = new ItemStack((Block)o);
						materialkey = ((Block)o).getLocalizedName();
					}else if (o instanceof String){
						if (((String)o).startsWith("P:")){
							String s = ((String)o).substring(2);
							int pfx = SpellRecipeManager.parsePotionMeta(s);
							recipeStack = new ItemStack(Items.potionitem, 1, pfx);
							materialkey = recipeStack.getDisplayName();
						}else if (((String)o).startsWith("E:")){
							int[] ids = SpellRecipeManager.ParseEssenceIDs((String)o);
							materialkey = "Essence (";
							for (int powerID : ids){
								PowerTypes type = PowerTypes.getByID(powerID);
								materialkey += type.name() + "/";
							}

							if (materialkey.equals("Essence (")){
								++i;
								continue;
							}

							o = recipeItems[++i];
							if (materialkey.startsWith("Essence (")){
								materialkey = materialkey.substring(0, materialkey.lastIndexOf("/")) + ")";
								qty = (Integer)o;
								int flag = 0;
								for (int f : ids){
									flag |= f;
								}

								recipeStack = new ItemStack(ItemsCommonProxy.essence, qty, ItemsCommonProxy.essence.META_MAX + flag);
							}

						}else{
							List<ItemStack> ores = OreDictionary.getOres((String)o);
							recipeStack = ores.size() > 0 ? ores.get(1) : null;
							materialkey = (String)o;
						}
					}

					if (materialsList.containsKey(materialkey)){
						int old = materialsList.get(materialkey);
						old += qty;
						materialsList.put(materialkey, old);
					}else{
						materialsList.put(materialkey, qty);
					}

					if (recipeStack != null)
						componentRecipeList.add(recipeStack);
				}
			}

			materialsList.put(ItemsCommonProxy.spellParchment.getItemStackDisplayName(new ItemStack(ItemsCommonProxy.spellParchment)), 1);

			StringBuilder sb = new StringBuilder();
			int sgCount = 0;
			int[][] shapeGroupCombos = new int[shapeGroups.size()][];
			for (ArrayList<ISpellPart> shapeGroup : shapeGroups){
				sb.append("Shape Group " + ++sgCount + "\n\n");
				Iterator<ISpellPart> it = shapeGroup.iterator();
				shapeGroupCombos[sgCount - 1] = SpellPartListToStringBuilder(it, sb, " -");
				sb.append("\n");
			}

			sb.append("Combination:\n\n");
			Iterator<ISpellPart> it = currentRecipe.iterator();
			ArrayList<Integer> outputCombo = new ArrayList<Integer>();
			int[] outputData = SpellPartListToStringBuilder(it, sb, null);

			ArrayList<NBTTagString> pages = Story.splitStoryPartIntoPages(sb.toString());

			sb = new StringBuilder();
			sb.append("\n\nMaterials List:\n\n");
			for (String s : materialsList.keySet()){
				sb.append(materialsList.get(s) + " x " + s + "\n");
			}

			pages.addAll(Story.splitStoryPartIntoPages(sb.toString()));

			sb = new StringBuilder();
			sb.append("Affinity Breakdown:\n\n");
			it = currentRecipe.iterator();
			HashMap<Affinity, Integer> affinityData = new HashMap<Affinity, Integer>();
			int cpCount = 0;
			while (it.hasNext()){
				ISpellPart part = it.next();
				if (part instanceof ISpellComponent){
					EnumSet<Affinity> aff = ((ISpellComponent)part).getAffinity();
					for (Affinity affinity : aff){
						int qty = 1;
						if (affinityData.containsKey(affinity)){
							qty = 1 + affinityData.get(affinity);
						}
						affinityData.put(affinity, qty);
					}
					cpCount++;
				}
			}
			ValueComparator vc = new ValueComparator(affinityData);
			TreeMap<Affinity, Integer> sorted = new TreeMap<Affinity, Integer>(vc);
			sorted.putAll(affinityData);
			for (Affinity aff : sorted.keySet()){
				float pct = (float)sorted.get(aff) / (float)cpCount * 100f;
				sb.append(String.format("%s: %.2f%%", aff.toString(), pct));
				sb.append("\n");
			}
			pages.addAll(Story.splitStoryPartIntoPages(sb.toString()));

			Story.WritePartToNBT(bookstack.getTagCompound(), pages);

			bookstack = Story.finalizeStory(bookstack, title, player.getName());

			int[] recipeData = new int[componentRecipeList.size() * 3];
			int idx = 0;
			for (ItemStack stack : componentRecipeList){
				recipeData[idx++] = Item.getIdFromItem(stack.getItem());
				recipeData[idx++] = stack.stackSize;
				recipeData[idx++] = stack.getItemDamage();
			}


			bookstack.getTagCompound().setIntArray("spell_combo", recipeData);
			bookstack.getTagCompound().setIntArray("output_combo", outputData);
			bookstack.getTagCompound().setInteger("numShapeGroups", shapeGroupCombos.length);
			int index = 0;
			for (int[] sgArray : shapeGroupCombos){
				bookstack.getTagCompound().setIntArray("shapeGroupCombo_" + index++, sgArray);
			}
			bookstack.getTagCompound().setString("spell_mod_version", AMCore.instance.getVersion());

			if (currentSpellName.equals(""))
				currentSpellName = "Spell Recipe";
			bookstack.setStackDisplayName(currentSpellName);

			this.currentRecipe.clear();
			for (ArrayList<ISpellPart> list : shapeGroups)
				list.clear();
			currentSpellName = "";

			bookstack.getTagCompound().setBoolean("spellFinalized", true);

			worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "arsmagica2:misc.inscriptiontable.takebook", 1.0f, 1.0f, true);

			worldObj.markBlockForUpdate(pos);
		}
		return bookstack;
	}

	private int[] concatenateArrays(int[] a, int[] b){
		int[] concat = new int[a.length + b.length];
		for (int i = 0; i < a.length; ++i)
			concat[i] = a[i];
		for (int i = 0; i < b.length; ++i)
			concat[i + a.length] = b[i];

		return concat;
	}

	private int[] SpellPartListToStringBuilder(Iterator<ISpellPart> it, StringBuilder sb, String prefix){
		ArrayList<Integer> outputCombo = new ArrayList<Integer>();
		while (it.hasNext()){
			ISpellPart part = it.next();
			String displayName = SkillManager.instance.getDisplayName(part);

			if (prefix != null){
				sb.append(prefix + displayName + "\n");
			}else{
				if (part instanceof ISpellShape){
					sb.append(displayName + "\n");
				}else{
					sb.append("-" + displayName + "\n");
				}
			}

			outputCombo.add(part.getID());
		}

		int[] outputData = new int[outputCombo.size()];
		int idx = 0;
		for (Integer I : outputCombo){
			outputData[idx++] = I;
		}

		return outputData;
	}

	public void clearCurrentRecipe(){
		this.currentRecipe.clear();
		for (ArrayList<ISpellPart> group : shapeGroups)
			group.clear();
		currentSpellName = "";
		currentSpellIsReadOnly = false;
		if (worldObj != null)
			worldObj.markBlockForUpdate(pos);
	}

	public SpellValidator.ValidationResult currentRecipeIsValid(){
		ArrayList<ArrayList<ISpellPart>> segmented = SpellValidator.splitToStages(currentRecipe);
		return SpellValidator.instance.spellDefIsValid(shapeGroups, segmented);
	}

	public boolean modifierCanBeAdded(ISpellModifier modifier){
		return false;
	}

	static class ValueComparator implements Comparator<Affinity>{

		Map<Affinity, Integer> base;

		ValueComparator(Map<Affinity, Integer> base){
			this.base = base;
		}

		@Override
		public int compare(Affinity a, Affinity b){
			Integer x = base.get(a);
			Integer y = base.get(b);
			if (x.equals(y)){
				return a.compareTo(b);
			}
			return x.compareTo(y);
		}
	}

	public void setSpellName(String name){
		this.currentSpellName = name;
		sendDataToServer();
	}

	public String getSpellName(){
		return this.currentSpellName != null ? this.currentSpellName : "";
	}

	public void reverseEngineerSpell(ItemStack stack){
		this.currentRecipe.clear();
		for (ArrayList<ISpellPart> group : shapeGroups){
			group.clear();
		}
		currentSpellName = "";

		this.currentSpellName = stack.getDisplayName();
		int numStages = SpellUtils.instance.numStages(stack);

		for (int i = 0; i < numStages; ++i){
			ISpellShape shape = SpellUtils.instance.getShapeForStage(stack, i);
			this.currentRecipe.add(shape);
			ISpellComponent[] components = SpellUtils.instance.getComponentsForStage(stack, i);
			for (ISpellComponent component : components)
				this.currentRecipe.add(component);
			ISpellModifier[] modifiers = SpellUtils.instance.getModifiersForStage(stack, i);
			for (ISpellModifier modifier : modifiers)
				this.currentRecipe.add(modifier);
		}

		int numShapeGroups = SpellUtils.instance.numShapeGroups(stack);
		for (int i = 0; i < numShapeGroups; ++i){
			int[] parts = SpellUtils.instance.getShapeGroupParts(stack, i);
			for (int partID : parts){
				ISkillTreeEntry entry = SkillManager.instance.getSkill(partID);
				if (entry != null && entry instanceof ISpellPart)
					this.shapeGroups.get(i).add((ISpellPart)entry);
			}
		}

		currentSpellIsReadOnly = true;
	}

	public boolean currentSpellDefIsReadOnly(){
		return this.currentSpellIsReadOnly;
	}

	public void resetSpellNameAndIcon(ItemStack stack, EntityPlayer player){
		if (worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(pos.getX());
			writer.add(pos.getY());
			writer.add(pos.getZ());
			writer.add(RESET_NAME);
			writer.add(player.getEntityId());
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.INSCRIPTION_TABLE_UPDATE, writer.generate());
		}
		stack.setItemDamage(0);
		stack.clearCustomName();
	}

	public int getShapeGroupSize(int groupIndex){
		return this.shapeGroups.get(groupIndex).size();
	}

	public ISpellPart getShapeGroupPartAt(int groupIndex, int index){
		return this.shapeGroups.get(groupIndex).get(index);
	}


	public void incrementUpgradeState(){
		this.numStageGroups++;
		if (!this.worldObj.isRemote){
			List<EntityPlayerMP> players = this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(256, 256, 256));
			for (EntityPlayerMP player : players){
				player.playerNetServerHandler.sendPacket(getDescriptionPacket());
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
