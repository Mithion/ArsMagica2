package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityEverstone;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockEverstone extends PoweredBlock{

	private boolean wiresProvidePower = true;
	private Set blocksNeedingUpdate = new HashSet();

	protected BlockEverstone(){
		super(Material.rock);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityEverstone();
	}

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TileEntityEverstone everstone = getTE(world, pos);
        if (everstone == null){
            if (player.capabilities.isCreativeMode){
                world.setTileEntity(pos, null);
                world.setBlockToAir(pos);
                return true;
            }
            return false;
        }
        everstone.onBreak();
        if (player.capabilities.isCreativeMode){
            world.setTileEntity(pos, null);
            world.setBlockToAir(pos);
            return true;
        }
        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        TileEntityEverstone everstone = getTE(world, pos);
        if (everstone != null)
            everstone.onBreak();
        return 10000f;
    }

	private TileEntityEverstone getTE(IBlockAccess world, BlockPos pos){
		if (world == null)
			return null;

		TileEntity te = world.getTileEntity(pos);
		if (te == null)
			return null;

		return (TileEntityEverstone)te;
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (player.getHeldItem() != null){
			Block block = null;
			int meta = -1;
			TileEntityEverstone everstone = getTE(world, pos);
			if (everstone == null) return false;

			if (player.getHeldItem().getItem() == ItemsCommonProxy.crystalWrench){
				if (!world.isRemote){
					if (everstone.getFacade() != null){
						everstone.setFacade(null, -1);
						return true;
					}else{
						world.setBlockToAir(pos);
						this.dropBlockAsItem(world, pos, BlocksCommonProxy.everstone.getDefaultState(), 1);
						return true;
					}
				}
			}else if (player.getHeldItem().getItem() instanceof ItemBlock){
				ItemBlock itemblock = (ItemBlock)player.getHeldItem().getItem();
				block = itemblock.block;
				if (block.isOpaqueCube()){
					meta = itemblock.getMetadata(player.getHeldItem().getItemDamage());
				}
			}
			if (everstone.getFacade() == null && block != null){
				everstone.setFacade(block, meta);
				world.notifyBlockOfStateChange(pos, this);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean isFullCube(){
		return true;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityEverstone everstone = getTE(worldIn, pos);
        if (everstone == null || everstone.isSolid())
            return super.getCollisionBoundingBox(worldIn, pos, state);
        return null;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        TileEntityEverstone everstone = getTE(worldIn, pos);
        if (everstone == null || everstone.isSolid())
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

	@Override
	public boolean isNormalCube(IBlockAccess world, BlockPos pos){
		TileEntityEverstone everstone = getTE(world, pos);
		if (everstone == null) return true;
		return everstone.isSolid();
	}

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (worldIn instanceof World)
            return ((World)worldIn).getRedstonePower(pos, side);
        else
            return 0;
    }

    @Override
    public float getBlockHardness(World world, BlockPos pos) {
        TileEntityEverstone everstone = getTE(world, pos);
        if (everstone == null) return this.blockHardness;
        Block block = everstone.getFacade();
        if (block == null || block == this) return this.blockHardness;
        return block.getBlockHardness(world, pos);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer){

		TileEntityEverstone everstone = getTE(world, pos);

		for (int i = 0; i < 5 * AMCore.config.getGFXLevel(); ++i){
			Block block = Blocks.air;
			int blockMeta = 0;
			if (everstone == null || everstone.getFacade() == null){
				block = this;
			}else{
				block = everstone.getFacade();
				if (block == null) block = this;
				blockMeta = everstone.getFacadeMeta();
			}
                // TODO need to find a way to make this work without using access transformers
			/*effectRenderer.addEffect(new EntityDiggingFX(world,
					x + world.rand.nextDouble(),
					y + world.rand.nextDouble(),
					z + world.rand.nextDouble(),
					0,
					0,
					0,
					block,
					blockMeta,
					0
			));*/

		}

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer){
		TileEntityEverstone everstone = getTE(worldObj, target.getBlockPos());
		AMParticle particle;
		Block block;
		int blockMeta = 0;
		if (everstone == null || everstone.getFacade() == null){
			block = this;
		}else{
			block = everstone.getFacade();
			if (block == null) block = this;
			blockMeta = everstone.getFacadeMeta();
		}
		/*effectRenderer.addEffect(new EntityDiggingFX(worldObj,
				target.getBlockPos().getX() + worldObj.rand.nextDouble(),
				target.getBlockPos().getY() + worldObj.rand.nextDouble(),
				target.getBlockPos().getZ() + worldObj.rand.nextDouble(),
				0,
				0,
				0,
				block,
				blockMeta,
				0
		));*/

		return true;
	}
}
