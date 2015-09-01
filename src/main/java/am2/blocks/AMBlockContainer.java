package am2.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import am2.lore.CompendiumUnlockHandler;

public abstract class AMBlockContainer extends BlockContainer{
	protected AMBlockContainer(Material par2Material) {
		super(par2Material);
	}

	public AMBlockContainer setUnlocalizedNameAndID(String name){
		setBlockTextureName(name);
		setBlockName(name);
		return this;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float interactX, float interactY, float interactZ) {
		if (world.isRemote)
			CompendiumUnlockHandler.unlockEntry(this.getUnlocalizedName().replace("tile.", "").replace("arsmagica2:", ""));
		return super.onBlockActivated(world, x, y, z, player, face, interactX, interactY, interactZ);
	}
}
