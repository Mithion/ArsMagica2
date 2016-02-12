package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityOcculus;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOcculus extends AMSpecialRenderBlockContainer{

	protected BlockOcculus(){
		super(Material.rock);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityOcculus();
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int p = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        byte byte0 = 3;

        if (p == 0){
            byte0 = 4;
        }
        if (p == 1){
            byte0 = 3;
        }
        if (p == 2){
            byte0 = 2;
        }
        if (p == 3){
            byte0 = 1;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2);

        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

        if (world.isRemote){
            if (ExtendedProperties.For(player).getMagicLevel() > 0)
                AMCore.proxy.openSkillTreeUI(world, player);
            else
                player.addChatMessage(new ChatComponentText("You cannot comprehend what you see inside the occulus."));
        }
        return true;
    }
}
