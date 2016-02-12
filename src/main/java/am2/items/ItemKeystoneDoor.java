package am2.items;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemKeystoneDoor extends Item{

	public static final int KEYSTONE_DOOR = 0;
	public static final int SPELL_SEALED_DOOR = 1;

	public ItemKeystoneDoor(){
		super();
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}


	@Override
	public String getItemStackDisplayName(ItemStack stack){
		switch (stack.getItemDamage()){
		case KEYSTONE_DOOR:
			return StatCollector.translateToLocal("item.arsmagica2:keystoneDoor.name");
		case SPELL_SEALED_DOOR:
			return StatCollector.translateToLocal("item.arsmagica2:spellSealedDoor.name");
		default:
			return StatCollector.translateToLocal("item.arsmagica2:unknown.name");
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (side != EnumFacing.UP){
			return false;
		}else{
			Block block;
			if (stack.getItemDamage() == KEYSTONE_DOOR)
				block = BlocksCommonProxy.keystoneDoor;
			else
				block = BlocksCommonProxy.spellSealedDoor;

			if (player.canPlayerEdit(pos, side, stack) && player.canPlayerEdit(pos.up(), side, stack)){
				if (!block.canPlaceBlockAt(world, pos)){
					return false;
				}else{
					int i1 = MathHelper.floor_double((player.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
					placeDoorBlock(world, pos.getX(), pos.getY(), pos.getZ(), i1, block);
					--stack.stackSize;
					return true;
				}
			}else{
				return false;
			}
		}
	}

	public static void placeDoorBlock(World world, int par1, int par2, int par3, int par4, Block block){
		byte b0 = 0;
		byte b1 = 0;

		if (par4 == 0){
			b1 = 1;
		}

		if (par4 == 1){
			b0 = -1;
		}

		if (par4 == 2){
			b1 = -1;
		}

		if (par4 == 3){
			b0 = 1;
		}

		int i1 = (world.isBlockNormalCubeDefault(par1 - b0, par2, par3 - b1, true) ? 1 : 0) + (world.isBlockNormalCubeDefault(par1 - b0, par2 + 1, par3 - b1, true) ? 1 : 0);
		int j1 = (world.isBlockNormalCubeDefault(par1 + b0, par2, par3 + b1, true) ? 1 : 0) + (world.isBlockNormalCubeDefault(par1 + b0, par2 + 1, par3 + b1, true) ? 1 : 0);
		boolean flag = world.getBlock(par1 - b0, par2, par3 - b1) == block || world.getBlock(par1 - b0, par2 + 1, par3 - b1) == block;
		boolean flag1 = world.getBlock(par1 + b0, par2, par3 + b1) == block || world.getBlock(par1 + b0, par2 + 1, par3 + b1) == block;
		boolean flag2 = false;

		if (flag && !flag1){
			flag2 = true;
		}else if (j1 > i1){
			flag2 = true;
		}

		world.setBlock(par1, par2, par3, block, par4, 2);
		world.setBlock(par1, par2 + 1, par3, block, 8 | (flag2 ? 1 : 0), 2);
		world.notifyBlocksOfNeighborChange(par1, par2, par3, block);
		world.notifyBlocksOfNeighborChange(par1, par2 + 1, par3, block);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, KEYSTONE_DOOR));
		list.add(new ItemStack(this, 1, SPELL_SEALED_DOOR));
	}
}
