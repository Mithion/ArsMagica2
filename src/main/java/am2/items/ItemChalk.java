package am2.items;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChalk extends ArsMagicaItem{

	public ItemChalk() {
        setMaxDamage(50);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != EnumFacing.UP || !canBeUsed(world, pos.up())){
            return false;
        }

        if (!world.isRemote){
            world.setBlockState(pos.up(), BlocksCommonProxy.wizardChalk.getDefaultState(), world.rand.nextInt(16));
            stack.damageItem(1, player);
        }

        return false;
    }

    public boolean canBeUsed(World world, BlockPos pos){
		if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.wizardChalk){
			return false;
		}
		if (!world.isAirBlock(pos)){
			return false;
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D(){
		return true;
	}
}