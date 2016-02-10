package am2;

import am2.blocks.BlocksCommonProxy;
import am2.bosses.BossSpawnHelper;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class EntityItemWatcher{
	private final ArrayList<EntityItem> watchedItems;
	private final ArrayList<EntityItem> toRemove;

	private final ArrayList<Block> inlayBlocks;
	private final ArrayList<Item> itemsToWatch;

	public static EntityItemWatcher instance = new EntityItemWatcher();

	private EntityItemWatcher(){
		watchedItems = new ArrayList<EntityItem>();
		toRemove = new ArrayList<EntityItem>();
		inlayBlocks = new ArrayList<Block>();
		itemsToWatch = new ArrayList<Item>();
	}

	public void init(){
		registerInlayBlock(BlocksCommonProxy.redstoneInlay);
		registerInlayBlock(BlocksCommonProxy.ironInlay);
		registerInlayBlock(BlocksCommonProxy.goldInlay);

		registerWatchableItem(Items.boat);
		registerWatchableItem(Items.water_bucket);
		registerWatchableItem(ItemsCommonProxy.essence);
		registerWatchableItem(ItemsCommonProxy.itemOre);
		registerWatchableItem(Items.emerald);
		registerWatchableItem(Items.ender_eye);
	}

	public void tick(){
		watchedItems.removeAll(toRemove);
		toRemove.clear();

		for (EntityItem item : watchedItems){
			if (item.isDead){
				removeWatchedItem(item);
				continue;
			}
			if (!item.isBurning() && (Math.abs(item.motionX) > 0.01 || Math.abs(item.motionY) > 0.01 || Math.abs(item.motionZ) > 0.01))
				continue;
			BlockPos pos = new BlockPos(item);

			if (item.isBurning()) pos = pos.up();

			boolean insideRing = true;
			Block ringType = null;

			for (int i = -1; i <= 1 && insideRing; i++){
				for (int j = -1; j <= 1 && insideRing; ++j){
					if (i == 0 && j == 0) continue;
					Block blockID1 = item.worldObj.getBlockState(pos.add(i, 0, j)).getBlock();
					Block blockID2 = item.worldObj.getBlockState(pos.add(i, 1, j)).getBlock();
					Block blockID3 = item.worldObj.getBlockState(pos.add(i, -1, j)).getBlock();
					if (inlayBlocks.contains(blockID1) || inlayBlocks.contains(blockID2) || inlayBlocks.contains(blockID3)){
						if (ringType == null){
							ringType = inlayBlocks.contains(blockID1) ? blockID1 : inlayBlocks.contains(blockID2) ? blockID2 : blockID3;
						}else if (ringType != blockID1 && ringType != blockID2 && ringType != blockID3){
							insideRing = false;
						}
					}else{
						insideRing = false;
					}
				}
			}

			if (insideRing){
				BossSpawnHelper.instance.onItemInRing(item, ringType);
			}
			removeWatchedItem(item);
		}
	}

	public void addWatchedItem(EntityItem item){
		if (this.itemsToWatch.contains(item.getEntityItem().getItem()))
			watchedItems.add(item);
	}

	public void registerInlayBlock(Block inlayBlock){
		if (!this.inlayBlocks.contains(inlayBlock))
			this.inlayBlocks.add(inlayBlock);
	}

	public void registerWatchableItem(Item item){
		if (!this.itemsToWatch.contains(item))
			this.itemsToWatch.add(item);
	}

	private void removeWatchedItem(EntityItem item){
		toRemove.add(item);
	}
}
