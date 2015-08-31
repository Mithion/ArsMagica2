package am2.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBoundItem {
	public float maintainCost();
	public void UnbindItem(ItemStack itemstack, EntityPlayer player, int inventoryIndex);
	static final float diminishedMaintain = 0.1f;
	static final float normalMaintain = 0.4f;
	static final float augmentedMaintain = 1.0f;
}
