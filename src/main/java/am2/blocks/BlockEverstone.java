package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityEverstone;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.texture.ResourceManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		TileEntityEverstone everstone = getTE(world, x, y, z);
		if (everstone == null){
			if (player.capabilities.isCreativeMode){
				world.setTileEntity(x, y, z, null);
				world.setBlockToAir(x, y, z);
				return true;
			}
			return false;
		}
		everstone.onBreak();
		if (player.capabilities.isCreativeMode){
			world.setTileEntity(x, y, z, null);
			world.setBlockToAir(x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5){
		return new ArrayList<ItemStack>();
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ){
		TileEntityEverstone everstone = getTE(world, x, y, z);
		if (everstone != null){
			everstone.onBreak();
		}
		return 10000f;
	}

	private TileEntityEverstone getTE(IBlockAccess world, int x, int y, int z){
		if (world == null)
			return null;

		TileEntity te = world.getTileEntity(x, y, z);
		if (te == null)
			return null;

		return (TileEntityEverstone)te;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if (player.getHeldItem() != null){
			Block block = null;
			int meta = -1;
			TileEntityEverstone everstone = getTE(world, x, y, z);
			if (everstone == null) return false;

			if (player.getHeldItem().getItem() == ItemsCommonProxy.crystalWrench){
				if (!world.isRemote){
					if (everstone.getFacade() != null){
						everstone.setFacade(null, -1);
						return true;
					}else{
						world.setBlockToAir(x, y, z);
						this.dropBlockAsItem(world, x, y, z, new ItemStack(BlocksCommonProxy.everstone));
						return true;
					}
				}
			}else if (player.getHeldItem().getItem() instanceof ItemBlock){
				ItemBlock itemblock = (ItemBlock)player.getHeldItem().getItem();
				block = itemblock.field_150939_a;
				if (block.isOpaqueCube()){
					meta = itemblock.getMetadata(player.getHeldItem().getItemDamage());
				}
			}
			if (everstone.getFacade() == null && block != null){
				everstone.setFacade(block, meta);
				world.notifyBlockChange(x, y, z, this);
				return true;
			}
		}
		return false;
	}

	@Override
	public IIcon getIcon(int par1, int par2){
		return blockIcon;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return true;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
		TileEntityEverstone everstone = getTE(par1World, par2, par3, par4);
		if (everstone == null || everstone.isSolid())
			return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
		return null;
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3,
										int par4, AxisAlignedBB par5AxisAlignedBB, List par6List,
										Entity par7Entity){
		TileEntityEverstone everstone = getTE(par1World, par2, par3, par4);
		if (everstone == null || everstone.isSolid())
			super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int x, int y, int z, int face){
		TileEntityEverstone everstone = getTE(par1iBlockAccess, x, y, z);
		if (everstone != null){
			if (everstone.isSolid()){
				Block block = everstone.getFacade();
				if (block != null){
					return block.getIcon(face, everstone.getFacadeMeta());
				}
			}else{
				return this.blockIcon;
			}
		}
		return this.blockIcon;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z){
		TileEntityEverstone everstone = getTE(world, x, y, z);
		if (everstone == null) return true;
		return everstone.isSolid();
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5){
		if (par1iBlockAccess instanceof World){
			return ((World)par1iBlockAccess).getBlockPowerInput(par2, par3, par4);
		}else{
			return 0;
		}
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z){
		TileEntityEverstone everstone = getTE(world, x, y, z);
		if (everstone == null) return this.blockHardness;
		Block block = everstone.getFacade();
		if (block == null || block == this) return this.blockHardness;
		return block.getBlockHardness(world, x, y, z);
	}

	@Override
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer){

		TileEntityEverstone everstone = getTE(world, x, y, z);

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

			effectRenderer.addEffect(new EntityDiggingFX(world,
					x + world.rand.nextDouble(),
					y + world.rand.nextDouble(),
					z + world.rand.nextDouble(),
					0,
					0,
					0,
					block,
					blockMeta,
					0
			));

		}

		return true;
	}

	@Override
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer){
		TileEntityEverstone everstone = getTE(worldObj, target.blockX, target.blockY, target.blockZ);
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
		effectRenderer.addEffect(new EntityDiggingFX(worldObj,
				target.blockX + worldObj.rand.nextDouble(),
				target.blockY + worldObj.rand.nextDouble(),
				target.blockZ + worldObj.rand.nextDouble(),
				0,
				0,
				0,
				block,
				blockMeta,
				0
		));

		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("everstone", par1IconRegister);
	}

	@Override
	public int getRenderBlockPass(){
		return 1;
	}
}
