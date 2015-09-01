package am2.blocks.tileentities.flickers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.utility.InventoryUtilities;

public class FlickerOperatorFishing implements IFlickerFunctionality {

	private static final List common_items = Arrays.asList(new WeightedRandomFishable[] {(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10)});
    private static final List rare_items = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).func_150707_a()});
    private static final List uncommon_items = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13)});
	
	@Override
	public boolean RequiresPower() {
		return true;
	}

	@Override
	public int PowerPerOperation() {
		return 500;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController controller, boolean powered) {
		return DoOperation(worldObj, controller, powered, new Affinity[0]);
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers) {
		TileEntity te = (TileEntity)controller;
		if (!powered || !checkSurroundings(worldObj, te.xCoord, te.yCoord, te.zCoord) || !worldObj.isBlockIndirectlyGettingPowered(te.xCoord, te.yCoord, te.zCoord))
			return false;		

		transferOrEjectItem(
				worldObj, 
				pickFishingItem(worldObj.rand), 
				te.xCoord, 
				te.yCoord, 
				te.zCoord
				);

		return true;
	}
	
	private boolean checkSurroundings(World world, int x, int y, int z){
		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j >= -2; --j){
				for (int k = -1; k <= 1; ++k){
					Block block = world.getBlock(x+i, y+j, z+k); 
					if (block != Blocks.water && block != Blocks.flowing_water)
						return false;
				}
			}
		}
		return true;
	}

	private ItemStack pickFishingItem(Random rand){
		float dropChance = rand.nextFloat();
		float minorUpgradeChance = 0.1F;
		float majorUpgradeChance = 0.05F;
		minorUpgradeChance = MathHelper.clamp_float(minorUpgradeChance, 0.0F, 1.0F);
		majorUpgradeChance = MathHelper.clamp_float(majorUpgradeChance, 0.0F, 1.0F);

		if (dropChance < minorUpgradeChance)
		{
			return ((WeightedRandomFishable)WeightedRandom.getRandomItem(rand, common_items)).func_150708_a(rand);
		}
		else
		{
			dropChance -= minorUpgradeChance;

			if (dropChance < majorUpgradeChance)
			{
				return ((WeightedRandomFishable)WeightedRandom.getRandomItem(rand, rare_items)).func_150708_a(rand);
			}
			else
			{
				return ((WeightedRandomFishable)WeightedRandom.getRandomItem(rand, uncommon_items)).func_150708_a(rand);
			}
		}
	}

	private void transferOrEjectItem(World worldObj, ItemStack stack, int xCoord, int yCoord, int zCoord){
		if (worldObj.isRemote)
			return;

		boolean eject = false;
		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				for (int k = -1; k <= 1; ++k){
					if (i == 0 && j == 0 && k == 0)
						continue;
					TileEntity te = worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
					if (te != null && te instanceof IInventory){
						for (int side = 0; side < 6; ++side){
							if (InventoryUtilities.mergeIntoInventory((IInventory)te, stack, stack.stackSize, side))
								return;
						}
					}
				}
			}
		}

		//eject the remainder
		EntityItem item = new EntityItem(worldObj);
		item.setPosition(xCoord + 0.5, yCoord + 1.5, zCoord + 0.5);
		item.setEntityItemStack(stack);
		worldObj.spawnEntityInWorld(item);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered) { }

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers) {
		int time = 2000;
		for (Affinity aff : flickers){
			if (aff == Affinity.LIGHTNING)
				time *= 0.8;
		}
		return time;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers) {		
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				" F ",
				"N W",
				" R ",
				Character.valueOf('F'), Items.fish,
				Character.valueOf('W'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.WATER.ordinal()),
				Character.valueOf('N'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.NATURE.ordinal()),
				Character.valueOf('R'), Items.fishing_rod
		};
	}

}
