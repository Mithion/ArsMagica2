package am2;

import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.blocks.MultiblockStructureDefinition.StructureGroup;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.blocks.BlocksCommonProxy;
import am2.utility.InventoryUtilities;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.*;

public class RitualShapeHelper{
	public MultiblockStructureDefinition ringedCross;
	public MultiblockStructureDefinition hourglass;
	public MultiblockStructureDefinition corruption;
	public MultiblockStructureDefinition purification;

	public static final RitualShapeHelper instance = new RitualShapeHelper();

	private RitualShapeHelper(){
		init();
	}

	public ItemStack[] checkForRitual(IRitualInteraction interaction, World world, int x, int y, int z, boolean itemsMustMatch){
		if (interaction.getRitualShape().checkStructure(world, x, y, z)){
			ItemStack[] reagents = interaction.getReagents();
			if (reagents.length == 0)
				return reagents;

			int r = interaction.getReagentSearchRadius();
			ArrayList<EntityItem> items = (ArrayList<EntityItem>)world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - r, y, z - r, x + r + 1, y + 1, z + r + 1));

			Collections.sort(items, new EntityItemComparator());

			if (!itemsMustMatch || matchReagents((List<EntityItem>)items.clone(), reagents)){
				ItemStack[] toReturn = new ItemStack[items.size()];
				for (int i = 0; i < items.size(); ++i)
					toReturn[i] = items.get(i).getEntityItem();
				return toReturn;
			}
		}
		return null;
	}

	public ItemStack[] checkForRitual(IRitualInteraction interaction, World world, int x, int y, int z){
		return checkForRitual(interaction, world, x, y, z, true);
	}

	private boolean matchReagents(List<EntityItem> items, ItemStack[] check){
		if (items.size() != check.length)
			return false;

		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		for (ItemStack stack : check)
			itemList.add(stack);

		Iterator it = itemList.iterator();
		while (it.hasNext()){
			ItemStack stack = (ItemStack)it.next();
			Iterator eIt = items.iterator();
			boolean found = false;
			while (eIt.hasNext()){
				EntityItem eItem = (EntityItem)eIt.next();
				if (InventoryUtilities.compareItemStacks(eItem.getEntityItem(), stack, true, false, true, true)){
					found = true;
					eIt.remove();
					break;
				}
			}
			if (!found)
				return false;
		}

		return true;
	}

	public void consumeRitualShape(IRitualInteraction interaction, World world, int x, int y, int z){
		for (int i : interaction.getRitualShape().getMutexList()){
			interaction.getRitualShape().removeMutex(i, world, x, y, z);
		}
	}

	public void consumeRitualReagents(IRitualInteraction interaction, World world, int x, int y, int z){
		int r = interaction.getReagentSearchRadius();
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - r, y, z - r, x + r + 1, y + 1, z + r + 1));
		for (EntityItem item : items)
			item.setDead();
	}

	public void init(){
		initRingedCross();
		initHourglass();
		initCorruption();
		initPurification();
	}

	private void initRingedCross(){
		ringedCross = new MultiblockStructureDefinition("ringedCross");
		//center
		//ringedCross.addAllowedBlock(0, 0, 0, BlocksCommonProxy.wizardChalk);

		//cross
		ringedCross.addAllowedBlock(1, 0, 0, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(-1, 0, 0, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(0, 0, 1, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(0, 0, -1, BlocksCommonProxy.wizardChalk);

		//rings
		ringedCross.addAllowedBlock(1, 0, 2, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(0, 0, 2, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(-1, 0, 2, BlocksCommonProxy.wizardChalk);

		ringedCross.addAllowedBlock(1, 0, -2, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(0, 0, -2, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(-1, 0, -2, BlocksCommonProxy.wizardChalk);

		ringedCross.addAllowedBlock(2, 0, 1, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(2, 0, 0, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(2, 0, -1, BlocksCommonProxy.wizardChalk);

		ringedCross.addAllowedBlock(-2, 0, 1, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(-2, 0, 0, BlocksCommonProxy.wizardChalk);
		ringedCross.addAllowedBlock(-2, 0, -1, BlocksCommonProxy.wizardChalk);
	}

	private void initHourglass(){
		hourglass = new MultiblockStructureDefinition("hourglass");

		StructureGroup eastWest = hourglass.createGroup("WE", 2);
		StructureGroup northSouth = hourglass.createGroup("NS", 2);

		//center
		hourglass.addAllowedBlock(eastWest, 0, 0, 0, BlocksCommonProxy.wizardChalk);

		//offset
		hourglass.addAllowedBlock(eastWest, -1, 0, 1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, -1, 0, -1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, 1, 0, 1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, 1, 0, -1, BlocksCommonProxy.wizardChalk);

		//edges
		hourglass.addAllowedBlock(eastWest, -2, 0, 1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, -2, 0, 0, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, -2, 0, -1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, 2, 0, 1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, 2, 0, 0, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(eastWest, 2, 0, -1, BlocksCommonProxy.wizardChalk);

		//center
		hourglass.addAllowedBlock(northSouth, 0, 0, 0, BlocksCommonProxy.wizardChalk);

		//offset
		hourglass.addAllowedBlock(northSouth, 1, 0, -1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, -1, 0, -1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, 1, 0, 1, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, -1, 0, 1, BlocksCommonProxy.wizardChalk);

		//edges
		hourglass.addAllowedBlock(northSouth, 1, 0, -2, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, 0, 0, -2, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, -1, 0, -2, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, 1, 0, 2, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, 0, 0, 2, BlocksCommonProxy.wizardChalk);
		hourglass.addAllowedBlock(northSouth, -1, 0, 2, BlocksCommonProxy.wizardChalk);

	}

	private void initCorruption(){
		corruption = new MultiblockStructureDefinition("corruption");
		StructureGroup NS = corruption.createGroup("NS", 2);
		StructureGroup WE = corruption.createGroup("WE", 2);

		//North - South
		corruption.addAllowedBlock(NS, 0, 0, 1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, 0, 0, -1, BlocksCommonProxy.wizardChalk);

		corruption.addAllowedBlock(NS, 1, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, 1, 0, -2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -1, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -1, 0, -2, BlocksCommonProxy.wizardChalk);

		corruption.addAllowedBlock(NS, 2, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, 2, 0, 1, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(NS, 2, 0, 0, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, 2, 0, -1, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(NS, 2, 0, -2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -2, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -2, 0, 1, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(NS, -2, 0, 0, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -2, 0, -1, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(NS, -2, 0, -2, BlocksCommonProxy.wizardChalk);

		corruption.addAllowedBlock(NS, 3, 0, 1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, 3, 0, -1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -3, 0, 1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(NS, -3, 0, -1, BlocksCommonProxy.wizardChalk);

		//West - East
		corruption.addAllowedBlock(WE, 1, 0, 0, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -1, 0, 0, BlocksCommonProxy.wizardChalk);

		corruption.addAllowedBlock(WE, 2, 0, 1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -2, 0, 1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, 2, 0, -1, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -2, 0, -1, BlocksCommonProxy.wizardChalk);

		corruption.addAllowedBlock(WE, 2, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, 1, 0, 2, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(WE, 0, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -1, 0, 2, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(WE, -2, 0, 2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, 2, 0, -2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, 1, 0, -2, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(WE, 0, 0, -2, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -1, 0, -2, BlocksCommonProxy.candle);
		corruption.addAllowedBlock(WE, -2, 0, -2, BlocksCommonProxy.wizardChalk);

		corruption.addAllowedBlock(WE, 1, 0, 3, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -1, 0, 3, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, 1, 0, -3, BlocksCommonProxy.wizardChalk);
		corruption.addAllowedBlock(WE, -1, 0, -3, BlocksCommonProxy.wizardChalk);
	}

	private void initPurification(){
		purification = new MultiblockStructureDefinition("purification");

		purification.addAllowedBlock(-1, 0, 1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-1, 0, -1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(1, 0, 1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(1, 0, -1, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(-2, 0, 1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-2, 0, -1, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(2, 0, 1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(2, 0, -1, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(1, 0, -2, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-1, 0, -2, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(1, 0, 2, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-1, 0, 2, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(-3, 0, 1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-3, 0, 0, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-3, 0, -1, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(3, 0, 1, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(3, 0, 0, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(3, 0, -1, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(1, 0, -3, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(0, 0, -3, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-1, 0, -3, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(1, 0, 3, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(0, 0, 3, BlocksCommonProxy.wizardChalk);
		purification.addAllowedBlock(-1, 0, 3, BlocksCommonProxy.wizardChalk);

		purification.addAllowedBlock(-2, 0, 2, BlocksCommonProxy.candle);
		purification.addAllowedBlock(-2, 0, -2, BlocksCommonProxy.candle);
		purification.addAllowedBlock(2, 0, 2, BlocksCommonProxy.candle);
		purification.addAllowedBlock(2, 0, -2, BlocksCommonProxy.candle);
	}

	private class EntityItemComparator implements Comparator<EntityItem>{

		@Override
		public int compare(EntityItem o1, EntityItem o2){
			if (o1.ticksExisted == o2.ticksExisted)
				return 0;
			else if (o1.ticksExisted > o2.ticksExisted)
				return -1;
			else
				return 1;
		}

	}
}
