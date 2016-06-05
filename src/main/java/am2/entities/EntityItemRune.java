package am2.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemRune extends EntityItem{
	public EntityItemRune(World world, double x, double y, double z, ItemStack item){
		super(world, x, y, z, item);
	}

	@Override
	public boolean combineItems(EntityItem item){
		return false;
	}
}
