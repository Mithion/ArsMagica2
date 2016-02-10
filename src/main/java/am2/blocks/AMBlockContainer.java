package am2.blocks;

import am2.lore.CompendiumUnlockHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class AMBlockContainer extends BlockContainer{
	protected AMBlockContainer(Material par2Material){
		super(par2Material);
	}

	public AMBlockContainer setUnlocalizedNameAndID(String name){
		setUnlocalizedName(name);
		setRegistryName(name);
		return this;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (world.isRemote)
			CompendiumUnlockHandler.unlockEntry(this.getUnlocalizedName().replace("tile.", "").replace("arsmagica2:", ""));
		return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
	}
}
