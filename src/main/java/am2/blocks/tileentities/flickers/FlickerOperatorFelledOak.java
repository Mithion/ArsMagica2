package am2.blocks.tileentities.flickers;

import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.math.AMVector3;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.utility.DummyEntityPlayer;
import am2.utility.InventoryUtilities;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FlickerOperatorFelledOak implements IFlickerFunctionality{

	private DummyEntityPlayer dummyPlayer;

	private static final int radius_horiz = 6;
	private static final int radius_vert = 1;

	public FlickerOperatorFelledOak(){

	}

	void destroyTree(World world, int x, int y, int z, Block block, int meta){
		for (int xPos = x - 1; xPos <= x + 1; xPos++){
			for (int yPos = y; yPos <= y + 1; yPos++){
				for (int zPos = z - 1; zPos <= z + 1; zPos++){
					Block localblock = world.getBlock(xPos, yPos, zPos);
					if (block == localblock){
						meta = world.getBlockMetadata(xPos, yPos, zPos);
						if (localblock == block && world.getBlockMetadata(xPos, yPos, zPos) % 4 == meta % 4){
							if (block.removedByPlayer(world, dummyPlayer, xPos, yPos, zPos)){
								block.onBlockDestroyedByPlayer(world, xPos, yPos, zPos, meta);
							}
							block.harvestBlock(world, dummyPlayer, xPos, yPos, zPos, meta);
							block.onBlockHarvested(world, xPos, yPos, zPos, meta, dummyPlayer);
							destroyTree(world, xPos, yPos, zPos, block, meta);
						}
					}
				}
			}
		}
	}

	void beginTreeFelling(World world, int x, int y, int z){
		Block wood = world.getBlock(x, y, z);
		while (wood.isWood(world, x, y, z)){
			y--;
			wood = world.getBlock(x, y, z);
		}

		y++;

		wood = world.getBlock(x, y, z);

		if (wood.isWood(world, x, y, z)){
			int height = y;
			boolean foundTop = false;
			do{
				height++;
				Block block = world.getBlock(x, height, z);
				if (block != wood){
					height--;
					foundTop = true;
				}
			}while (!foundTop);

			int numLeaves = 0;
			if (height - y < 50){
				for (int xPos = x - 1; xPos <= x + 1; xPos++){
					for (int yPos = height - 1; yPos <= height + 1; yPos++){
						for (int zPos = z - 1; zPos <= z + 1; zPos++){
							Block leaves = world.getBlock(xPos, yPos, zPos);
							if (leaves != null && leaves.isLeaves(world, xPos, yPos, zPos))
								numLeaves++;
						}
					}
				}
			}

			int meta = world.getBlockMetadata(x, y, z);
			if (numLeaves > 3)
				destroyTree(world, x, y, z, wood, meta);


			if (!world.isRemote)
				world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(wood) + (world.getBlockMetadata(x, y, z) << 12));
		}
	}

	private void plantTree(World worldObj, IFlickerController habitat, boolean powered){
		if (!powered || worldObj.isRemote)
			return;

		ItemStack sapling = getSaplingFromNearbyChest(worldObj, habitat);
		if (sapling == null)
			return;

		AMVector3 plantLoc = getPlantLocation(worldObj, habitat, sapling);

		if (plantLoc == null)
			return;

		deductSaplingFromNearbyChest(worldObj, habitat);
		ItemBlock block = (ItemBlock)sapling.getItem();

		worldObj.setBlock((int)plantLoc.x, (int)plantLoc.y, (int)plantLoc.z, block.blockInstance, block.getMetadata(sapling.getItemDamage()), 3);
	}

	private AMVector3 getPlantLocation(World worldObj, IFlickerController habitat, ItemStack sapling){
		if (sapling.getItem() instanceof ItemBlock == false)
			return null;
		TileEntity te = (TileEntity)habitat;
		byte[] data = habitat.getMetadata(this);
		AMVector3 offset = null;
		if (data == null || data.length == 0){
			offset = new AMVector3(te.xCoord - radius_horiz, te.yCoord - radius_vert, te.zCoord - radius_horiz);
		}else{
			AMDataReader reader = new AMDataReader(data, false);
			offset = new AMVector3(reader.getInt(), te.yCoord - radius_vert, reader.getInt());
		}

		Block treeBlock = ((ItemBlock)sapling.getItem()).blockInstance;

		for (int i = (int)offset.x; i <= te.xCoord + radius_horiz; i += 2){
			for (int k = (int)offset.z; k <= te.zCoord + radius_horiz; k += 2){
				for (int j = (int)offset.y; j <= te.yCoord + radius_vert; ++j){
					Block block = worldObj.getBlock(i, j, k);
					if (block.isReplaceable(worldObj, i, j, k) && treeBlock.canPlaceBlockAt(worldObj, i, j, k)){
						AMDataWriter writer = new AMDataWriter();
						writer.add(i).add(k);
						habitat.setMetadata(this, writer.generate());
						return new AMVector3(i, j, k);
					}
				}
			}
		}

		AMDataWriter writer = new AMDataWriter();
		writer.add(te.xCoord - radius_horiz).add(te.zCoord - radius_horiz);
		habitat.setMetadata(this, writer.generate());

		return null;
	}

	/**
	 * Gets a single sapling from an adjacent chest
	 *
	 * @return
	 */
	private ItemStack getSaplingFromNearbyChest(World worldObj, IFlickerController habitat){
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			IInventory inv = getOffsetInventory(worldObj, habitat, dir);
			if (inv == null)
				continue;
			int index = InventoryUtilities.getInventorySlotIndexFor(inv, new ItemStack(Blocks.sapling, 1, Short.MAX_VALUE));
			if (index > -1){
				ItemStack stack = inv.getStackInSlot(index).copy();
				stack.stackSize = 1;
				return stack;
			}
		}
		return null;
	}

	private void deductSaplingFromNearbyChest(World worldObj, IFlickerController habitat){
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			IInventory inv = getOffsetInventory(worldObj, habitat, dir);
			if (inv == null)
				continue;
			int index = InventoryUtilities.getInventorySlotIndexFor(inv, new ItemStack(Blocks.sapling, 1, Short.MAX_VALUE));
			if (index > -1){
				InventoryUtilities.decrementStackQuantity(inv, index, 1);
				return;
			}
		}
	}

	/**
	 * Gets an instance of the adjacent IInventory at direction offset.  Returns null if not found or invalid type adjacent.
	 */
	private IInventory getOffsetInventory(World worldObj, IFlickerController habitat, ForgeDirection direction){
		TileEntity te = (TileEntity)habitat;
		TileEntity adjacent = worldObj.getTileEntity(te.xCoord + direction.offsetX, te.yCoord + direction.offsetY, te.zCoord + direction.offsetZ);
		if (adjacent != null && adjacent instanceof IInventory)
			return (IInventory)adjacent;
		return null;
	}

	@Override
	public boolean RequiresPower(){
		return false;
	}

	@Override
	public int PowerPerOperation(){
		return 100;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered){
		int radius = 6;

		dummyPlayer = new DummyEntityPlayer(worldObj);

		for (int i = -radius; i <= radius; ++i){
			for (int j = -radius; j <= radius; ++j){
				Block block = worldObj.getBlock(((TileEntity)habitat).xCoord + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + j);
				if (block == Blocks.air) continue;
				if (block.isWood(worldObj, ((TileEntity)habitat).xCoord + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + j)){
					if (!worldObj.isRemote)
						beginTreeFelling(worldObj, ((TileEntity)habitat).xCoord + i, ((TileEntity)habitat).yCoord, ((TileEntity)habitat).zCoord + j);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){

		boolean hasNatureAugment = false;
		for (Affinity aff : flickers){
			if (aff == Affinity.NATURE){
				hasNatureAugment = true;
				break;
			}
		}

		if (hasNatureAugment){
			plantTree(worldObj, habitat, powered);
		}

		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered){
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		int base = powered ? 300 : 3600;
		float augments = 1.0f;
		for (Affinity aff : flickers){
			if (aff == Affinity.LIGHTNING)
				augments *= 0.5f;
		}
		return (int)Math.ceil(base * augments);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"WG ",
				"NCL",
				" OW",
				Character.valueOf('W'), BlocksCommonProxy.witchwoodLog,
				Character.valueOf('G'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
				Character.valueOf('N'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.NATURE.ordinal()),
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.LIGHTNING.ordinal()),
				Character.valueOf('G'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				Character.valueOf('G'), new ItemStack(ItemsCommonProxy.bindingCatalyst, 1, ItemsCommonProxy.bindingCatalyst.META_AXE)
		};
	}
}
