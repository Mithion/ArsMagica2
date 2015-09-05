package am2.blocks;

import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public abstract class BlockGroundRune extends AMBlockContainer{
	private EntityPlayer placedBy;
	private Block blockBelow;

	protected BlockGroundRune(){
		super(Material.wood);
		setTickRandomly(true);
		float f = 0.0625F;
		setBlockBounds(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
		setHardness(10);
		setResistance(0.5f);
		//setBlockBounds(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
	}

	protected boolean triggerOnCaster(){
		return false;
	}

	@Override
	public abstract TileEntity createNewTileEntity(World var1, int i);

	protected int getCastingMode(World world, int x, int y, int z){
		return world.getBlockMetadata(x, y, z);
	}

	public int quantityDropped(Random random){
		return 0;
	}

	public void SetPlacedBy(EntityPlayer placedBy){
		this.placedBy = placedBy;
	}

	public abstract String GetRuneTexture();

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture(GetRuneTexture(), par1IconRegister);
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l){
			/*if (l == 1 || l == 0){
	    		return this.blockIcon;
	    	}else{
	    		return null;
	    	}*/
		return this.blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2){
	    	/*if (par1 == 1 || par1 == 0){
	    		return this.blockIcon;
	    	}else{
	    		return null;
	    	}*/
		return this.blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5){
		return par5 == 1 || par5 == 0;
	}

	public int tickRate(){
		return 20;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k){
		return null;
	}

	public boolean isOpaqueCube(){
		return false;
	}

	public boolean renderAsNormalBlock(){
		return false;
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k){
		return world.getBlock(i, j - 1, k).isNormalCube(world, i, j - 1, k) && world.isAirBlock(i, j, k);
	}

	public boolean placeAt(World world, int x, int y, int z, int meta){
		if (!canPlaceBlockAt(world, x, y, z)) return false;
		world.setBlock(x, y, z, this, meta, 2);
		return true;
	}

	public void onBlockAdded(World world, int i, int j, int k){
		blockBelow = world.getBlock(i, j, k);
	}

	public void updateTick(World world, int i, int j, int k, Random random){
		if (world.isRemote){
			return;
		}
		if (world.getBlockMetadata(i, j, k) == 0){
			return;
		}else{
			setStateIfMobInteractsWithPlate(world, i, j, k);
			return;
		}
	}

	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity){
		if (world.isRemote){
			return;
		}
		setStateIfMobInteractsWithPlate(world, i, j, k);
		return;
	}

	private void setStateIfMobInteractsWithPlate(World world, int i, int j, int k){
		float f = 0.125F;
		List list = null;
		list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox((float)i - f, j, (float)k - f, (float)(i + 1 + f), (double)j + 2D, (float)(k + 1 + f)));
		if (!triggerOnCaster() && list.contains(placedBy)){
			list.remove(placedBy);
		}
		if (list.size() > 0){
			if (ActivateRune(world, list, i, j, k)){
				int meta = world.getBlockMetadata(i, j, k);
				if (!isPermanent(world, i, j, k, meta)){
					int numTriggers = getNumTriggers(world, i, j, k, meta);
					if (--numTriggers <= 0)
						world.setBlock(i, j, k, Blocks.air);
					else
						setNumTriggers(world, i, j, k, meta, numTriggers);
				}

			}
		}
	}

	protected abstract boolean ActivateRune(World world, List<Entity> entitiesInRange, int x, int y, int z);

	@Override
	public void breakBlock(World world, int i, int j, int k, Block m, int l){
		//int l = world.getBlockMetadata(i, j, k);
		if (l > 0){
			world.notifyBlocksOfNeighborChange(i, j, k, m);
			world.notifyBlocksOfNeighborChange(i, j - 1, k, m);
		}
		super.breakBlock(world, i, j, k, m, l);
	}

	public void setBlockBoundsForItemRender(){
		float f = 0.5F;
		float f1 = 0.125F;
		float f2 = 0.5F;
		setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
	}

	public int getMobilityFlag(){
		return 1;
	}

	protected abstract boolean isPermanent(World world, int x, int y, int z, int metadata);

	protected abstract int getNumTriggers(World world, int x, int y, int z, int metadata);

	public abstract void setNumTriggers(World world, int x, int y, int z, int meta, int numTriggers);
}
