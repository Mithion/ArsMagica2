package am2.blocks.tileentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import am2.AMCore;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.blocks.MultiblockStructureDefinition.BlockCoord;
import am2.api.blocks.MultiblockStructureDefinition.BlockDec;
import am2.api.blocks.MultiblockStructureDefinition.StructureGroup;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellPart;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemEssence;
import am2.items.ItemsCommonProxy;
import am2.multiblock.IMultiblockStructureController;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import am2.power.PowerNodeRegistry;
import am2.spell.SkillManager;
import am2.spell.SpellRecipeManager;
import am2.spell.SpellUtils;
import am2.spell.components.Summon;
import am2.spell.shapes.Binding;
import am2.utility.KeyValuePair;

public class TileEntityCraftingAltar extends TileEntityAMPower implements IMultiblockStructureController{

	private MultiblockStructureDefinition primary;
	private MultiblockStructureDefinition secondary;

	private boolean isCrafting;
	private final ArrayList<ItemStack> allAddedItems;
	private final ArrayList<ItemStack> currentAddedItems;

	private final ArrayList<KeyValuePair<ISpellPart, byte[]>> spellDef;
	private final ArrayList<ArrayList<KeyValuePair<ISpellPart, byte[]>>> shapeGroups;
	private boolean allShapeGroupsAdded = false;

	private int currentKey = -1;
	private int checkCounter;
	private boolean structureValid;
	private BlockCoord podiumLocation;
	private BlockCoord switchLocation;
	private int maxEffects;

	private ItemStack addedPhylactery = null;
	private ItemStack addedBindingCatalyst = null;

	private int[] spellGuide;
	private int[] outputCombo;
	private int[][] shapeGroupGuide;

	private int currentConsumedPower = 0;
	private int ticksExisted = 0;
	private PowerTypes currentMainPowerTypes = PowerTypes.NONE;

	private static final byte CRAFTING_CHANGED = 1;
	private static final byte COMPONENT_ADDED = 2;
	private static final byte FULL_UPDATE = 3;

	private static final int augmatl_mutex = 2;
	private static final int lectern_mutex = 4;
	//==============================================
	// Structure Groups
	//==============================================
	private StructureGroup[] lecternGroup_primary;
	private StructureGroup[] augMatl_primary;
	private StructureGroup wood_primary;
	private StructureGroup quartz_primary;
	private StructureGroup netherbrick_primary;
	private StructureGroup cobble_primary;
	private StructureGroup brick_primary;
	private StructureGroup sandstone_primary;
	private StructureGroup witchwood_primary;

	private StructureGroup[] lecternGroup_secondary;
	private StructureGroup[] augMatl_secondary;
	private StructureGroup wood_secondary;
	private StructureGroup quartz_secondary;
	private StructureGroup netherbrick_secondary;
	private StructureGroup cobble_secondary;
	private StructureGroup brick_secondary;
	private StructureGroup sandstone_secondary;
	private StructureGroup witchwood_secondary;

	private String currentSpellName = "";

	public TileEntityCraftingAltar(){
		super(500);
		setupMultiblock();
		allAddedItems = new ArrayList<ItemStack>();
		currentAddedItems = new ArrayList<ItemStack>();
		isCrafting = false;
		structureValid = false;
		checkCounter = 0;
		setNoPowerRequests();
		maxEffects = 2;

		spellDef = new ArrayList<KeyValuePair<ISpellPart,byte[]>>();
		shapeGroups = new ArrayList<ArrayList<KeyValuePair<ISpellPart,byte[]>>>();

		for (int i = 0; i < 5; ++i){
			shapeGroups.add(new ArrayList<KeyValuePair<ISpellPart,byte[]>>());
		}
	}

	private void setupMultiblock(){
		primary = new MultiblockStructureDefinition("craftingAltar_alt");

		Block[] augMatls = new Block[]{
				Blocks.glass,
				Blocks.coal_block,
				Blocks.redstone_block,
				Blocks.iron_block,
				Blocks.lapis_block,
				Blocks.gold_block,
				Blocks.diamond_block,
				Blocks.emerald_block,
				BlocksCommonProxy.AMOres,
				BlocksCommonProxy.AMOres
		};

		int[] augMetas = new int[]{
				0, //glass
				0, //coal
				0, //redstone
				0, //iron
				0, //lapis
				0, //gold
				0, //diamond
				0,  //emerald
				BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK,
				BlocksCommonProxy.AMOres.META_SUNSTONE_BLOCK
		};

		lecternGroup_primary = new StructureGroup[4];

		for (int i = 0; i < lecternGroup_primary.length; ++i){
			lecternGroup_primary[i] = primary.createGroup("lectern" + i, lectern_mutex);
		}

		int count = 0;
		for (int i = -2; i <= 2; i+=4){
			primary.addAllowedBlock(lecternGroup_primary[count], i, -3, i, BlocksCommonProxy.blockLectern);
			primary.addAllowedBlock(lecternGroup_primary[count], i, -2, -i, Blocks.lever, (count < 2) ? 2 : 1);
			primary.addAllowedBlock(lecternGroup_primary[count], i, -2, -i, Blocks.lever, (count < 2) ? 10 : 9);
			count++;
			primary.addAllowedBlock(lecternGroup_primary[count], i, -3, -i, BlocksCommonProxy.blockLectern);
			primary.addAllowedBlock(lecternGroup_primary[count], i, -2, i, Blocks.lever, (count < 2) ? 2 : 1);
			primary.addAllowedBlock(lecternGroup_primary[count], i, -2, i, Blocks.lever, (count < 2) ? 10 : 9);
			count++;
		}

		augMatl_primary = new StructureGroup[augMatls.length];
		for (int i = 0; i < augMatls.length; ++i) augMatl_primary[i] = primary.createGroup("augmatl" + i, augmatl_mutex);

		//row 0
		for (int i = 0; i < augMatls.length; ++i) primary.addAllowedBlock(augMatl_primary[i], -1, 0, -2, augMatls[i], augMetas[i]);

		primary.addAllowedBlock(-1, 0, -1, Blocks.stone_brick_stairs, 0);
		primary.addAllowedBlock(-1, 0, 0, Blocks.stone_brick_stairs, 0);
		primary.addAllowedBlock(-1, 0, 1, Blocks.stone_brick_stairs, 0);

		for (int i = 0; i < augMatls.length; ++i) primary.addAllowedBlock(augMatl_primary[i], -1, 0, 2, augMatls[i], augMetas[i]);

		primary.addAllowedBlock(0, 0, -2, Blocks.stone_brick_stairs, 2);
		primary.addAllowedBlock(0, 0, -1, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.craftingAltar);
		primary.addAllowedBlock(0, 0, 1, Blocks.stonebrick, 0);
		primary.addAllowedBlock(0, 0, 2, Blocks.stone_brick_stairs, 3);

		for (int i = 0; i < augMatls.length; ++i) primary.addAllowedBlock(augMatl_primary[i], 1, 0, -2, augMatls[i], augMetas[i]);

		primary.addAllowedBlock(1, 0, -1, Blocks.stone_brick_stairs, 1);
		primary.addAllowedBlock(1, 0, 0, Blocks.stone_brick_stairs, 1);
		primary.addAllowedBlock(1, 0, 1, Blocks.stone_brick_stairs, 1);

		for (int i = 0; i < augMatls.length; ++i) primary.addAllowedBlock(augMatl_primary[i], 1, 0, 2, augMatls[i], augMetas[i]);

		//row 1
		primary.addAllowedBlock(1, -1, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(1, -1, -1, Blocks.stone_brick_stairs, 7);
		primary.addAllowedBlock(1, -1, 1, Blocks.stone_brick_stairs, 6);
		primary.addAllowedBlock(1, -1, 2, Blocks.stonebrick, 0);

		primary.addAllowedBlock(0, -1, -2, BlocksCommonProxy.magicWall, 0);
		primary.addAllowedBlock(0, -1, 2, BlocksCommonProxy.magicWall, 0);

		primary.addAllowedBlock(-1, -1, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(-1, -1, -1, Blocks.stone_brick_stairs, 7);
		primary.addAllowedBlock(-1, -1, 1, Blocks.stone_brick_stairs, 6);
		primary.addAllowedBlock(-1, -1, 2, Blocks.stonebrick, 0);

		//row 2
		primary.addAllowedBlock(1, -2, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(1, -2, 2, Blocks.stonebrick, 0);

		primary.addAllowedBlock(0, -2, -2, BlocksCommonProxy.magicWall, 0);
		primary.addAllowedBlock(0, -2, 2, BlocksCommonProxy.magicWall, 0);

		primary.addAllowedBlock(-1, -2, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(-1, -2, 2, Blocks.stonebrick, 0);


		//row 3
		primary.addAllowedBlock(1, -3, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(1, -3, 2, Blocks.stonebrick, 0);

		primary.addAllowedBlock(0, -3, -2, BlocksCommonProxy.magicWall, 0);
		primary.addAllowedBlock(0, -3, 2, BlocksCommonProxy.magicWall, 0);

		primary.addAllowedBlock(-1, -3, -2, Blocks.stonebrick, 0);
		primary.addAllowedBlock(-1, -3, 2, Blocks.stonebrick, 0);

		//row 4
		for (int i = -2; i <= 2; ++i){
			for (int j = -2; j <= 2; ++j){
				if (i == 0 && j == 0){
					for (int n = 0; n < augMatls.length; ++n) primary.addAllowedBlock(augMatl_primary[n], i, -4, j, augMatls[n], augMetas[n]);
				}else{
					primary.addAllowedBlock(i, -4, j, Blocks.stonebrick, 0);
				}
			}
		}

		wood_primary = primary.copyGroup("main", "main_wood");
		wood_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.planks);
		wood_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.oak_stairs);

		quartz_primary = primary.copyGroup("main", "main_quartz");
		quartz_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.quartz_block);
		quartz_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.quartz_stairs);

		netherbrick_primary = primary.copyGroup("main", "main_netherbrick");
		netherbrick_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.nether_brick);
		netherbrick_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.nether_brick_stairs);

		cobble_primary = primary.copyGroup("main", "main_cobble");
		cobble_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.cobblestone);
		cobble_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.stone_stairs);

		brick_primary = primary.copyGroup("main", "main_brick");
		brick_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.brick_block);
		brick_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.brick_stairs);

		sandstone_primary = primary.copyGroup("main", "main_sandstone");
		sandstone_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.sandstone);
		sandstone_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.sandstone_stairs);

		witchwood_primary = primary.copyGroup("main", "main_witchwood");
		witchwood_primary.replaceAllBlocksOfType(Blocks.stonebrick, BlocksCommonProxy.witchwoodPlanks);
		witchwood_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, BlocksCommonProxy.witchwoodStairs);

		//Secondary
		secondary = new MultiblockStructureDefinition("craftingAltar");

		lecternGroup_secondary = new StructureGroup[4];

		for (int i = 0; i < lecternGroup_secondary.length; ++i){
			lecternGroup_secondary[i] = secondary.createGroup("lectern" + i, lectern_mutex);
		}

		count = 0;
		for (int i = -2; i <= 2; i+=4){
			secondary.addAllowedBlock(lecternGroup_secondary[count], i, -3, i, BlocksCommonProxy.blockLectern);
			secondary.addAllowedBlock(lecternGroup_secondary[count], -i, -2, i, Blocks.lever, (count < 2) ? 4 : 3);
			secondary.addAllowedBlock(lecternGroup_secondary[count], -i, -2, i, Blocks.lever, (count < 2) ? 12 : 11);
			count++;
			secondary.addAllowedBlock(lecternGroup_secondary[count], -i, -3, i, BlocksCommonProxy.blockLectern);
			secondary.addAllowedBlock(lecternGroup_secondary[count], i, -2, i, Blocks.lever, (count < 2) ? 4 : 3);
			secondary.addAllowedBlock(lecternGroup_secondary[count], i, -2, i, Blocks.lever, (count < 2) ? 12 : 11);
			count++;
		}

		augMatl_secondary = new StructureGroup[augMatls.length];
		for (int i = 0; i < augMatls.length; ++i) augMatl_secondary[i] = secondary.createGroup("augmatl" + i, augmatl_mutex);

		//row 0
		for (int i = 0; i < augMatls.length; ++i) secondary.addAllowedBlock(augMatl_secondary[i], -2, 0, -1, augMatls[i], augMetas[i]);

		secondary.addAllowedBlock(-1, 0, -1, Blocks.stone_brick_stairs, 2);
		secondary.addAllowedBlock(0, 0, -1, Blocks.stone_brick_stairs, 2);
		secondary.addAllowedBlock(1, 0, -1, Blocks.stone_brick_stairs, 2);

		for (int i = 0; i < augMatls.length; ++i) secondary.addAllowedBlock(augMatl_secondary[i], 2, 0, -1, augMatls[i], augMetas[i]);

		secondary.addAllowedBlock(-2, 0, 0, Blocks.stone_brick_stairs, 0);
		secondary.addAllowedBlock(-1, 0, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.craftingAltar);
		secondary.addAllowedBlock(1, 0, 0, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, 0, 0, Blocks.stone_brick_stairs, 1);

		for (int i = 0; i < augMatls.length; ++i) secondary.addAllowedBlock(augMatl_secondary[i], -2, 0, 1, augMatls[i], augMetas[i]);

		secondary.addAllowedBlock(-1, 0, 1, Blocks.stone_brick_stairs, 3);
		secondary.addAllowedBlock(0, 0, 1, Blocks.stone_brick_stairs, 3);
		secondary.addAllowedBlock(1, 0, 1, Blocks.stone_brick_stairs, 3);

		for (int i = 0; i < augMatls.length; ++i) secondary.addAllowedBlock(augMatl_secondary[i], 2, 0, 1, augMatls[i], augMetas[i]);

		//row 1
		secondary.addAllowedBlock(-2, -1, 1, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(-1, -1, 1, Blocks.stone_brick_stairs, 5);
		secondary.addAllowedBlock(1, -1, 1, Blocks.stone_brick_stairs, 4);
		secondary.addAllowedBlock(2, -1, 1, Blocks.stonebrick, 0);

		secondary.addAllowedBlock(-2, -1, 0, BlocksCommonProxy.magicWall, 0);
		secondary.addAllowedBlock(2, -1, 0, BlocksCommonProxy.magicWall, 0);

		secondary.addAllowedBlock(-2, -1, -1, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(-1, -1, -1, Blocks.stone_brick_stairs, 5);
		secondary.addAllowedBlock(1, -1, -1, Blocks.stone_brick_stairs, 4);
		secondary.addAllowedBlock(2, -1, -1, Blocks.stonebrick, 0);

		//row 2
		secondary.addAllowedBlock(-2, -2, 1, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -2, 1, Blocks.stonebrick, 0);

		secondary.addAllowedBlock(-2, -2, 0, BlocksCommonProxy.magicWall, 0);
		secondary.addAllowedBlock(2, -2, 0, BlocksCommonProxy.magicWall, 0);

		secondary.addAllowedBlock(-2, -2, -1, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -2, -1, Blocks.stonebrick, 0);


		//row 3
		secondary.addAllowedBlock(-2, -3, 1, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -3, 1, Blocks.stonebrick, 0);

		secondary.addAllowedBlock(-2, -3, 0, BlocksCommonProxy.magicWall, 0);
		secondary.addAllowedBlock(2, -3, 0, BlocksCommonProxy.magicWall, 0);

		secondary.addAllowedBlock(-2, -3, -1, Blocks.stonebrick, 0);
		secondary.addAllowedBlock(2, -3, -1, Blocks.stonebrick, 0);

		//row 4
		for (int i = -2; i <= 2; ++i){
			for (int j = -2; j <= 2; ++j){
				if (i == 0 && j == 0){
					for (int n = 0; n < augMatls.length; ++n) secondary.addAllowedBlock(augMatl_secondary[n], i, -4, j, augMatls[n], augMetas[n]);
				}else{
					secondary.addAllowedBlock(i, -4, j, Blocks.stonebrick, 0);
				}
			}
		}

		wood_secondary = secondary.copyGroup("main", "main_wood");
		wood_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.planks);
		wood_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.oak_stairs);

		quartz_secondary = secondary.copyGroup("main", "main_quartz");
		quartz_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.quartz_block);
		quartz_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.quartz_stairs);

		netherbrick_secondary = secondary.copyGroup("main", "main_netherbrick");
		netherbrick_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.nether_brick);
		netherbrick_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.nether_brick_stairs);

		cobble_secondary = secondary.copyGroup("main", "main_cobble");
		cobble_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.cobblestone);
		cobble_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.stone_stairs);

		brick_secondary = secondary.copyGroup("main", "main_brick");
		brick_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.brick_block);
		brick_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.brick_stairs);

		sandstone_secondary = secondary.copyGroup("main", "main_sandstone");
		sandstone_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.sandstone);
		sandstone_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.sandstone_stairs);

		witchwood_secondary = secondary.copyGroup("main", "main_witchwood");
		witchwood_secondary.replaceAllBlocksOfType(Blocks.stonebrick, BlocksCommonProxy.witchwoodPlanks);
		witchwood_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, BlocksCommonProxy.witchwoodStairs);

	}

	@Override
	public MultiblockStructureDefinition getDefinition() {
		return secondary;
	}

	public ItemStack getNextPlannedItem(){
		if (spellGuide != null){
			if ((this.allAddedItems.size()) * 3 < spellGuide.length){
				int guide_id = spellGuide[(this.allAddedItems.size()) * 3];
				int guide_qty = spellGuide[((this.allAddedItems.size()) * 3) + 1];
				int guide_meta = spellGuide[((this.allAddedItems.size()) * 3) + 2];
				ItemStack stack = new ItemStack(Item.getItemById(guide_id), guide_qty, guide_meta);
				return stack;
			}else{
				return new ItemStack(ItemsCommonProxy.spellParchment);
			}
		}
		return null;
	}

	private int getNumPartsInSpell(){
		int parts = 0;
		if (outputCombo != null)
			parts = outputCombo.length;

		if (shapeGroupGuide != null){
			for (int i = 0; i < shapeGroupGuide.length; ++i){
				if (shapeGroupGuide[i] != null)
					parts += shapeGroupGuide[i].length;
			}
		}
		return parts;
	}

	private boolean spellGuideIsWithinStructurePower(){
		return getNumPartsInSpell() <= maxEffects;
	}

	private boolean currentDefinitionIsWithinStructurePower(){
		int count = this.spellDef.size();
		for (ArrayList<KeyValuePair<ISpellPart, byte[]>> part : shapeGroups)
			count += part.size();

		return count <= this.maxEffects;
	}

	public boolean structureValid(){
		return this.structureValid;
	}

	public boolean isCrafting(){
		return this.isCrafting;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		this.ticksExisted++;

		checkStructure();
		checkForStartCondition();
		updateLecternInformation();
		if (isCrafting){
			checkForEndCondition();
			updatePowerRequestData();
			if (!worldObj.isRemote && !currentDefinitionIsWithinStructurePower() && this.ticksExisted > 100){
				worldObj.newExplosion(null, xCoord + 0.5, yCoord - 1.5, zCoord + 0.5, 5, false, true);
				setCrafting(false);
				return;
			}
			if (worldObj.isRemote && checkCounter == 1){
				AMCore.proxy.particleManager.RibbonFromPointToPoint(worldObj, xCoord + 0.5, yCoord - 2, zCoord + 0.5, xCoord + 0.5, yCoord - 3, zCoord + 0.5);
			}
			List<EntityItem> components = lookForValidItems();
			ItemStack stack = getNextPlannedItem();
			for (EntityItem item : components){
				if (item.isDead) continue;
				ItemStack entityItemStack = item.getEntityItem();
				if (stack != null && compareItemStacks(stack, entityItemStack)){
					if (!worldObj.isRemote){
						updateCurrentRecipe(item);
						item.setDead();
					}else{
						worldObj.playSound(xCoord, yCoord, zCoord, "arsmagica2:misc.craftingaltar.component_added", 1.0f, 0.4f + worldObj.rand.nextFloat() * 0.6f, false);
						for (int i = 0; i < 5 * AMCore.config.getGFXLevel(); ++i){
							AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "radiant", item.posX, item.posY, item.posZ);
							if (particle != null){
								particle.setMaxAge(40);
								particle.AddParticleController(new ParticleMoveOnHeading(particle, worldObj.rand.nextFloat() * 360, worldObj.rand.nextFloat() * 360, 0.01f, 1, false));
								particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
								particle.setParticleScale(0.02f);
								particle.setRGBColorF(worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), worldObj.rand.nextFloat());
							}
						}
					}
				}
			}
		}
	}

	private void updateLecternInformation(){
		if (podiumLocation == null) return;
		TileEntityLectern lectern = (TileEntityLectern) worldObj.getTileEntity(xCoord + podiumLocation.getX(), yCoord + podiumLocation.getY(), zCoord + podiumLocation.getZ());
		if (lectern != null){
			if (lectern.hasStack()){
				ItemStack lecternStack = lectern.getStack();
				if (lecternStack.hasTagCompound()){
					spellGuide = lecternStack.getTagCompound().getIntArray("spell_combo");
					outputCombo = lecternStack.getTagCompound().getIntArray("output_combo");
					currentSpellName = lecternStack.getDisplayName();

					int numShapeGroups = lecternStack.getTagCompound().getInteger("numShapeGroups");
					shapeGroupGuide = new int[numShapeGroups][];

					for (int i = 0; i < numShapeGroups; ++i){
						shapeGroupGuide[i] = lecternStack.getTagCompound().getIntArray("shapeGroupCombo_" + i);
					}
				}

				if (isCrafting){
					if (spellGuide != null){
						lectern.setNeedsBook(false);
						lectern.setTooltipStack(getNextPlannedItem());
					}else{
						lectern.setNeedsBook(true);
					}
				}else{
					lectern.setTooltipStack(null);
				}
				if (spellGuideIsWithinStructurePower()){
					lectern.setOverpowered(false);
				}else{
					lectern.setOverpowered(true);
				}
			}else{
				if (isCrafting){
					lectern.setNeedsBook(true);
				}
				lectern.setTooltipStack(null);
			}
		}
	}

	public BlockCoord getSwitchLocation(){
		return this.switchLocation;
	}
	
	public boolean switchIsOn(){
		if (switchLocation == null) return false;
		Block block = worldObj.getBlock(xCoord + switchLocation.getX(), yCoord + switchLocation.getY(), zCoord + switchLocation.getZ());
		boolean b = false;
		if (block == Blocks.lever){
			for (int i = 0; i < 6; ++i){
				b |= (Blocks.lever.isProvidingStrongPower(worldObj, xCoord + switchLocation.getX(), yCoord + switchLocation.getY(), zCoord + switchLocation.getZ(), i) > 0);
				if (b) break;
			}
		}
		return b;
	}

	public void flipSwitch(){
		if (switchLocation == null) return;
		Block block = worldObj.getBlock(xCoord + switchLocation.getX(), yCoord + switchLocation.getY(), zCoord + switchLocation.getZ());
		if (block == Blocks.lever){
			Blocks.lever.onBlockActivated(worldObj, xCoord + switchLocation.getX(), yCoord + switchLocation.getY(), zCoord + switchLocation.getZ(), null, 0, 0, 0, 0);
		}
	}

	private void updatePowerRequestData(){
		ItemStack stack = getNextPlannedItem();
		if (stack != null && stack.getItem() instanceof ItemEssence && stack.getItemDamage() > ItemEssence.META_MAX){
			if (switchIsOn()){
				int flags = stack.getItemDamage() - ItemEssence.META_MAX;
				setPowerRequests();
				pickPowerType(stack);
				if (this.currentMainPowerTypes != PowerTypes.NONE && PowerNodeRegistry.For(this.worldObj).checkPower(this, this.currentMainPowerTypes, 100)){
					currentConsumedPower += PowerNodeRegistry.For(worldObj).consumePower(this, this.currentMainPowerTypes, Math.min(100, stack.stackSize - currentConsumedPower));
				}
				if (currentConsumedPower >= stack.stackSize){
					PowerNodeRegistry.For(this.worldObj).setPower(this, this.currentMainPowerTypes, 0);
					if (!worldObj.isRemote)
						addItemToRecipe(new ItemStack(ItemsCommonProxy.essence, stack.stackSize, ItemEssence.META_MAX + flags));
					currentConsumedPower = 0;
					currentMainPowerTypes = PowerTypes.NONE;
					setNoPowerRequests();
					flipSwitch();
				}
			}else{
				setNoPowerRequests();
			}
		}else{
			setNoPowerRequests();
		}
	}

	@Override
	protected void setNoPowerRequests() {
		currentConsumedPower = 0;
		currentMainPowerTypes = PowerTypes.NONE;

		super.setNoPowerRequests();
	}

	private void pickPowerType(ItemStack stack){
		if (this.currentMainPowerTypes != PowerTypes.NONE)
			return;
		int flags = stack.getItemDamage() - ItemEssence.META_MAX;
		PowerTypes highestValid = PowerTypes.NONE;
		float amt = 0;
		for (PowerTypes type : PowerTypes.all()){
			float tmpAmt = PowerNodeRegistry.For(worldObj).getPower(this, type);
			if (tmpAmt > amt)
				highestValid = type;
		}

		this.currentMainPowerTypes = highestValid;
	}

	private void updateCurrentRecipe(EntityItem item){
		ItemStack stack = item.getEntityItem();
		addItemToRecipe(stack);
	}

	private void addItemToRecipe(ItemStack stack){
		allAddedItems.add(stack);
		currentAddedItems.add(stack);

		if (!worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(xCoord);
			writer.add(yCoord);
			writer.add(zCoord);
			writer.add(COMPONENT_ADDED);
			writer.add(stack);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32, AMPacketIDs.CRAFTING_ALTAR_DATA, writer.generate());
		}

		if (matchCurrentRecipe()){
			currentAddedItems.clear();
			return;
		}
	}

	private boolean matchCurrentRecipe(){
		ISpellPart part = SpellRecipeManager.instance.getPartByRecipe(currentAddedItems);
		if (part == null) return false;

		ArrayList<KeyValuePair<ISpellPart, byte[]>> currentShapeGroupList = getShapeGroupToAddTo();

		if (part instanceof Summon)
			handleSummonShape();
		if (part instanceof Binding)
			handleBindingShape();
		
		byte[] metaData = new byte[0];
		if (part instanceof ISpellModifier){
			metaData = ((ISpellModifier)part).getModifierMetadata(currentAddedItems.toArray(new ItemStack[currentAddedItems.size()]));
			if (metaData == null){
				metaData = new byte[0];
			}
		}		

		//if this is null, then we have already completed all of the shape groups that the book identifies
		//we're now creating the body of the spell
		if (currentShapeGroupList == null){
			spellDef.add(new KeyValuePair<ISpellPart, byte[]>(part, metaData));
		}else{
			currentShapeGroupList.add(new KeyValuePair<ISpellPart, byte[]>(part, metaData));
		}
		return true;
	}

	private ArrayList<KeyValuePair<ISpellPart, byte[]>> getShapeGroupToAddTo(){
		for (int i = 0; i < shapeGroupGuide.length; ++i){
			int guideLength = shapeGroupGuide[i].length;
			int addedLength = shapeGroups.get(i).size();
			if (addedLength < guideLength)
				return shapeGroups.get(i);
		}

		return null;
	}

	private void handleSummonShape(){
		if (currentAddedItems.size() > 2)
			addedPhylactery = currentAddedItems.get(currentAddedItems.size() - 2);
	}

	private void handleBindingShape(){
		if (currentAddedItems.size() == 7)
			addedBindingCatalyst = currentAddedItems.get(currentAddedItems.size() - 1);
	}

	private List<EntityItem> lookForValidItems(){
		if (!isCrafting) return new ArrayList<EntityItem>();
		double radius = worldObj.isRemote ? 2.1 : 2;
		List<EntityItem> items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - radius, yCoord - 3, zCoord - radius, xCoord + radius, yCoord, zCoord + radius));
		return items;
	}

	private void checkStructure(){

		if ((isCrafting && checkCounter++ < 50) || (!isCrafting && checkCounter++ < 200)){
			return;
		}
		checkCounter = 0;

		boolean primaryvalid = primary.checkStructure(worldObj, xCoord, yCoord, zCoord);
		boolean secondaryvalid = secondary.checkStructure(worldObj, xCoord, yCoord, zCoord);
		if (!primaryvalid && !secondaryvalid){
			if (isCrafting) setCrafting(false);
		}

		//locate lectern and lever & material groups
		if (primaryvalid || secondaryvalid){
			maxEffects = 0;
			ArrayList<StructureGroup> lecternGroups = null;
			ArrayList<StructureGroup> augmatlGroups = null;
			ArrayList<StructureGroup> mainmatlGroups = null;
			if (primaryvalid){
				lecternGroups = primary.getMatchedGroups(lectern_mutex, worldObj, xCoord, yCoord, zCoord);
				augmatlGroups = primary.getMatchedGroups(augmatl_mutex, worldObj, xCoord, yCoord, zCoord);
				mainmatlGroups = primary.getMatchedGroups(MultiblockStructureDefinition.MAINGROUP_MUTEX, worldObj, xCoord, yCoord, zCoord);
			}else if (secondaryvalid){
				lecternGroups = secondary.getMatchedGroups(lectern_mutex, worldObj, xCoord, yCoord, zCoord);
				augmatlGroups = secondary.getMatchedGroups(augmatl_mutex, worldObj, xCoord, yCoord, zCoord);
				mainmatlGroups = secondary.getMatchedGroups(MultiblockStructureDefinition.MAINGROUP_MUTEX, worldObj, xCoord, yCoord, zCoord);
			}
			if (lecternGroups != null && lecternGroups.size() > 0){
				StructureGroup group = lecternGroups.get(0);
				HashMap<BlockCoord, ArrayList<BlockDec>> blocks = group.getAllowedBlocks();

				for (BlockCoord bc : blocks.keySet()){
					Block block = worldObj.getBlock(xCoord + bc.getX(), yCoord + bc.getY(), zCoord + bc.getZ());
					if (block == BlocksCommonProxy.blockLectern){
						podiumLocation = bc;
					}else if (block == Blocks.lever){
						switchLocation = bc;
					}
				}
			}
			if (augmatlGroups != null && augmatlGroups.size() == 1){
				StructureGroup group = augmatlGroups.get(0);
				int index = -1;
				for (StructureGroup augmatlGroup : primaryvalid ? augMatl_primary : augMatl_secondary){
					index++;
					if (augmatlGroup == group){
						break;
					}
				}
				maxEffects = index + 1;
			}
			if (mainmatlGroups != null && mainmatlGroups.size() == 1){
				StructureGroup group = mainmatlGroups.get(0);
				if (group == wood_primary || group == wood_secondary)
					maxEffects += 1;
				else if (group == cobble_primary || group == cobble_secondary || group == sandstone_primary || group == sandstone_secondary)
					maxEffects += 1;
				else if (group == brick_primary || group == brick_secondary || group == witchwood_primary || group == witchwood_secondary)
					maxEffects += 2;
				else if (group == netherbrick_primary || group == netherbrick_secondary || group == quartz_primary || group == quartz_secondary)
					maxEffects += 3;
				else //default of stone brick
					maxEffects += 2;
			}
		}else{
			podiumLocation = null;
			switchLocation = null;
			maxEffects = 0;
		}

		//maxEffects = 2;
		setStructureValid(primaryvalid || secondaryvalid);
	}

	private void checkForStartCondition(){
		if (this.worldObj.isRemote || !structureValid || this.isCrafting) return;

		List<Entity> items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord - 3, zCoord - 2, xCoord + 2, yCoord, zCoord + 2));
		if (items.size() == 1){
			EntityItem item = (EntityItem)items.get(0);
			if (item != null && !item.isDead && item.getEntityItem().getItem() == ItemsCommonProxy.rune && item.getEntityItem().getItemDamage() == ItemsCommonProxy.rune.META_BLANK){
				item.setDead();
				setCrafting(true);
			}
		}
	}

	private void checkForEndCondition(){
		if (!structureValid || !this.isCrafting || worldObj == null) return;

		double radius = worldObj.isRemote ? 2.2 : 2;

		List<Entity> items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - radius, yCoord - 3, zCoord - radius, xCoord + radius, yCoord, zCoord + radius));
		if (items.size() == 1){
			EntityItem item = (EntityItem)items.get(0);
			if (item != null && !item.isDead && item.getEntityItem() != null && item.getEntityItem().getItem() == ItemsCommonProxy.spellParchment){
				if (!worldObj.isRemote){					
					item.setDead();
					setCrafting(false);
					EntityItem craftedItem = new EntityItem(worldObj);
					craftedItem.setPosition(xCoord + 0.5, yCoord - 1.5, zCoord + 0.5);

					ItemStack craftStack = SpellUtils.instance.createSpellStack(shapeGroups, spellDef);
					if (!craftStack.hasTagCompound())
						craftStack.stackTagCompound = new NBTTagCompound();
					AddSpecialMetadata(craftStack);

					craftStack.stackTagCompound.setString("suggestedName", currentSpellName != null ? currentSpellName : "");
					craftedItem.setEntityItemStack(craftStack);
					worldObj.spawnEntityInWorld(craftedItem);

					allAddedItems.clear();
					currentAddedItems.clear();
				}else{
					worldObj.playSound(xCoord, yCoord, zCoord, "arsmagica2:misc.craftingaltar.create_spell", 1.0f, 1.0f, true);
				}
			}
		}
	}

	private void AddSpecialMetadata(ItemStack craftStack){
		if (addedPhylactery != null){
			Summon summon = (Summon) SkillManager.instance.getSkill("Summon");
			summon.setSummonType(craftStack, addedPhylactery);
		}
		if (addedBindingCatalyst != null){
			Binding binding = (Binding) SkillManager.instance.getSkill("Binding");
			binding.setBindingType(craftStack, addedBindingCatalyst);
		}
		
		
	}

	private void setCrafting(boolean crafting){
		this.isCrafting = crafting;
		if (!worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(xCoord);
			writer.add(yCoord);
			writer.add(zCoord);
			writer.add(CRAFTING_CHANGED);
			writer.add(crafting);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32, AMPacketIDs.CRAFTING_ALTAR_DATA, writer.generate());
		}
		if (crafting){
			allAddedItems.clear();
			currentAddedItems.clear();

			spellDef.clear();
			for (ArrayList<KeyValuePair<ISpellPart, byte[]>> groups : shapeGroups)
				groups.clear();
			
			//find otherworld auras
			IPowerNode[] nodes = PowerNodeRegistry.For(worldObj).getAllNearbyNodes(worldObj, new AMVector3(this), PowerTypes.DARK);
			for (IPowerNode node : nodes){
				if (node instanceof TileEntityOtherworldAura){
					((TileEntityOtherworldAura) node).setActive(true, this);
					break;
				}
			}
		}
	}

	private void setStructureValid(boolean valid){
		if (this.structureValid == valid) return;
		this.structureValid = valid;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public void deactivate(){
		if (!worldObj.isRemote){
			this.setCrafting(false);
			for (ItemStack stack : allAddedItems){
				if (stack.getItem() == ItemsCommonProxy.essence && stack.getItemDamage() > ItemsCommonProxy.essence.META_MAX)
					continue;
				EntityItem eItem = new EntityItem(worldObj);
				eItem.setPosition(xCoord, yCoord-1, zCoord);
				eItem.setEntityItemStack(stack);
				worldObj.spawnEntityInWorld(eItem);
			}
			allAddedItems.clear();
		}
	}

	@Override
	public boolean canProvidePower(PowerTypes type) {
		return false;
	}

	private boolean compareItemStacks(ItemStack target, ItemStack input){
		if (target.getItem() == Items.potionitem && input.getItem() == Items.potionitem){
			return (target.getItemDamage() & 0xF) == (input.getItemDamage() & 0xF);
		}
		return target.getItem() == input.getItem() && (target.getItemDamage() == input.getItemDamage() || target.getItemDamage() == Short.MAX_VALUE) && target.stackSize == input.stackSize;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		NBTTagCompound altarCompound = new NBTTagCompound();
		altarCompound.setBoolean("isCrafting", this.isCrafting);
		altarCompound.setInteger("currentKey", this.currentKey);
		altarCompound.setString("currentSpellName", currentSpellName);

		NBTTagList allAddedItemsList = new NBTTagList();
		for (ItemStack stack : allAddedItems){
			NBTTagCompound addedItem = new NBTTagCompound();
			stack.writeToNBT(addedItem);
			allAddedItemsList.appendTag(addedItem);
		}

		altarCompound.setTag("allAddedItems", allAddedItemsList);

		NBTTagList currentAddedItemsList = new NBTTagList();
		for (ItemStack stack : currentAddedItems){
			NBTTagCompound addedItem = new NBTTagCompound();
			stack.writeToNBT(addedItem);
			currentAddedItemsList.appendTag(addedItem);
		}

		altarCompound.setTag("currentAddedItems", currentAddedItemsList);

		if (addedPhylactery != null)
		{
			NBTTagCompound phylactery = new NBTTagCompound();
			addedPhylactery.writeToNBT(phylactery);
			altarCompound.setTag("phylactery", phylactery);
		}

		if (addedBindingCatalyst != null){
			NBTTagCompound catalyst = new NBTTagCompound();
			addedBindingCatalyst.writeToNBT(catalyst);
			altarCompound.setTag("catalyst", catalyst);
		}

		NBTTagList shapeGroupData = new NBTTagList();
		for (ArrayList<KeyValuePair<ISpellPart, byte[]>> list : shapeGroups){
			shapeGroupData.appendTag(ISpellPartListToNBT(list));
		}
		altarCompound.setTag("shapeGroups", shapeGroupData);

		NBTTagCompound spellDefSave = ISpellPartListToNBT(this.spellDef);
		altarCompound.setTag("spellDef", spellDefSave);

		nbttagcompound.setTag("altarData", altarCompound);
	}

	private NBTTagCompound ISpellPartListToNBT(ArrayList<KeyValuePair<ISpellPart, byte[]>> list){
		NBTTagCompound shapeGroupData = new NBTTagCompound();
		int[] ids = new int[list.size()];
		byte[][] meta = new byte[list.size()][];
		for (int d = 0; d < list.size(); ++d){
			ids[d] = SkillManager.instance.getShiftedPartID(list.get(d).getKey());
			meta[d] = list.get(d).getValue();
		}
		shapeGroupData.setIntArray("group_ids", ids);
		for (int i = 0; i < meta.length; ++i){
			shapeGroupData.setByteArray("meta_" + i, meta[i]);
		}
		return shapeGroupData;
	}

	private ArrayList<KeyValuePair<ISpellPart, byte[]>> NBTToISpellPartList(NBTTagCompound compound){
		int[] ids = compound.getIntArray("group_ids");
		ArrayList<KeyValuePair<ISpellPart, byte[]>> list = new ArrayList<KeyValuePair<ISpellPart, byte[]>>();
		for (int i = 0; i < ids.length; ++i){
			int partID = ids[i];
			ISkillTreeEntry part = SkillManager.instance.getSkill(i);
			byte[] partMeta = compound.getByteArray("meta_" + i);
			if (part instanceof ISpellPart){
				list.add(new KeyValuePair<ISpellPart, byte[]>((ISpellPart) part, partMeta));
			}
		}
		return list;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		if (!nbttagcompound.hasKey("altarData"))
			return;

		NBTTagCompound altarCompound = nbttagcompound.getCompoundTag("altarData");

		NBTTagList allAddedItems = altarCompound.getTagList("allAddedItems", Constants.NBT.TAG_COMPOUND);
		NBTTagList currentAddedItems = altarCompound.getTagList("currentAddedItems", Constants.NBT.TAG_COMPOUND);

		this.isCrafting = altarCompound.getBoolean("isCrafting");
		this.currentKey = altarCompound.getInteger("currentKey");
		this.currentSpellName = altarCompound.getString("currentSpellName");

		if (altarCompound.hasKey("phylactery")){
			NBTTagCompound phylactery = altarCompound.getCompoundTag("phylactery");
			if (phylactery != null)
				this.addedPhylactery = ItemStack.loadItemStackFromNBT(phylactery);
		}

		if (altarCompound.hasKey("catalyst")){
			NBTTagCompound catalyst = altarCompound.getCompoundTag("catalyst");
			if (catalyst != null)
				this.addedBindingCatalyst = ItemStack.loadItemStackFromNBT(catalyst);
		}

		this.allAddedItems.clear();
		for (int i = 0; i < allAddedItems.tagCount(); ++i){
			NBTTagCompound addedItem = (NBTTagCompound) allAddedItems.getCompoundTagAt(i);
			if (addedItem == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(addedItem);
			if (stack == null)
				continue;
			this.allAddedItems.add(stack);
		}

		this.currentAddedItems.clear();
		for (int i = 0; i < currentAddedItems.tagCount(); ++i){
			NBTTagCompound addedItem = (NBTTagCompound) currentAddedItems.getCompoundTagAt(i);
			if (addedItem == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(addedItem);
			if (stack == null)
				continue;
			this.currentAddedItems.add(stack);
		}

		this.spellDef.clear();
		for (ArrayList<KeyValuePair<ISpellPart, byte[]>> groups : shapeGroups)
			groups.clear();

		NBTTagCompound currentSpellDef = altarCompound.getCompoundTag("spellDef");
		this.spellDef.addAll(NBTToISpellPartList(currentSpellDef));

		NBTTagList currentShapeGroups = altarCompound.getTagList("shapeGroups", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < currentShapeGroups.tagCount(); ++i){
			NBTTagCompound compound = (NBTTagCompound) currentShapeGroups.getCompoundTagAt(i);
			shapeGroups.get(i).addAll(NBTToISpellPartList(compound));
		}
	}

	@Override
	public int getChargeRate() {
		return 250;
	}

	@Override
	public boolean canRelayPower(PowerTypes type) {
		return false;
	}

	
	public void HandleUpdatePacket(byte[] remainingBytes) {
		AMDataReader rdr = new AMDataReader(remainingBytes, false);
		byte subID = rdr.getByte();
		switch(subID){
		case FULL_UPDATE:
			this.isCrafting = rdr.getBoolean();
			this.currentKey = rdr.getInt();

			this.allAddedItems.clear();
			this.currentAddedItems.clear();

			int itemCount = rdr.getInt();
			for (int i = 0; i < itemCount; ++i)
				this.allAddedItems.add(rdr.getItemStack());
			break;
		case CRAFTING_CHANGED:
			this.setCrafting(rdr.getBoolean());
			break;
		case COMPONENT_ADDED:
			this.allAddedItems.add(rdr.getItemStack());
			break;
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
		return packet;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

}
