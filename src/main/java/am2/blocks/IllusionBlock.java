package am2.blocks;

import am2.api.math.AMVector3;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class IllusionBlock extends AMBlock{

	//simple counter used for particle spawning
	private int tickCount;

	//The IIcon to show when "revealed"
	private IIcon revealedIcon;

	//types represent ethereal or standard illusion blocks
	public static final String[] illusion_block_types = new String[]{"default", "nobarrier"};

	//how far to search for a block to mimic
	private static final byte SEARCH_MAX_DIST = 8;

	public IllusionBlock(){
		super(Material.wood);
		setTickRandomly(true);
		setLightOpacity(255);

		this.setHardness(3.0f);
		this.setResistance(3.0f);
	}


	@Override
	public int tickRate(World par1World){
		return 20;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		revealedIcon = ResourceManager.RegisterTexture("illusionBlockRevealed", iconRegister);
	}

	/**
	 * Gets the direction the illusion block is facing
	 *
	 * @param blockAccess The block access instance, used to get metadata
	 * @param x           X coord of the block
	 * @param y           Y coord of the block
	 * @param z           Z coord of the block
	 * @return ForgeDirection representing the forward vector
	 */
	private ForgeDirection getFacing(IBlockAccess blockAccess, int x, int y, int z){
		int meta = blockAccess.getBlockMetadata(x, y, z) & 7;
		return ForgeDirection.values()[meta];
	}

	/**
	 * Gets the location of the block to mimic
	 *
	 * @param blockAccess The block access instance, used to get metadata
	 * @param x           X coord of the block
	 * @param y           Y coord of the block
	 * @param z           Z coord of the block
	 * @return the x,y,z coordinates of the mimic block, or null, if no mimic was found.
	 */
	public AMVector3 getMimicLocation(IBlockAccess blockAccess, int x, int y, int z){
		ForgeDirection dir = getFacing(blockAccess, x, y, z);
		AMVector3 position = new AMVector3(x, y, z);
		AMVector3 offset = new AMVector3(dir.offsetX, dir.offsetY, dir.offsetZ);
		int count = 0;
		boolean found = false;

		while (count++ < SEARCH_MAX_DIST){
			position = position.add(offset);
			Block block = blockAccess.getBlock((int)position.x, (int)position.y, (int)position.z);
			if (block != Blocks.air && block.renderAsNormalBlock() && block != this){
				return position;
			}
		}

		return null;
	}

	/**
	 * Gets the actual block to mimic
	 *
	 * @param blockAccess The block access instance, used to get metadata
	 * @param position    The position of the block to mimic
	 * @return The block instance to mimic, or null if not found.
	 */
	public Block getMimicBlock(IBlockAccess blockAccess, AMVector3 position){
		return blockAccess.getBlock((int)position.x, (int)position.y, (int)position.z);
	}

	/**
	 * Is the block at the specified location always passable?
	 *
	 * @return True if the meta of the block has the 4th bit on meta set
	 */
	public boolean alwaysPassable(IBlockAccess blockAccess, int x, int y, int z){
		int meta = blockAccess.getBlockMetadata(x, y, z);
		return (meta & 0x8) == 0x8;
	}

	/**
	 * Calculates a forward facing direction based on the yaw and pitch of the player who placed it
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack){
		//get the nearest 90 degree angle from the placing entity's yaw, as a bit flag
		int yaw = MathHelper.floor_double((entityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

		//get the pitch angle, again, as a bit flag
		Vec3 look = entityLiving.getLook(1.0f);
		int pitch = (int)(Math.round(look.yCoord * 0.6) + 1) & 3;

		int meta = 3;

		if (yaw == 0){
			meta = ForgeDirection.SOUTH.ordinal();
		}else if (yaw == 1){
			meta = ForgeDirection.WEST.ordinal();
		}else if (yaw == 2){
			meta = ForgeDirection.NORTH.ordinal();
		}else if (yaw == 3){
			meta = ForgeDirection.EAST.ordinal();
		}

		//pitch overrides, as at this point yaw wouldn't matter anyways
		if (pitch == 0){
			meta = ForgeDirection.DOWN.ordinal();
		}else if (pitch == 2){
			meta = ForgeDirection.UP.ordinal();
		}

		//ethereal blocks are always passable, indicated by the 4th bit of meta
		if (stack.getMetadata() == 1){
			meta |= 0x8;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, meta, 2);
		super.onBlockPlacedBy(par1World, x, y, z, entityLiving, stack);
	}

	/**
	 * Get the mimic'd hardness versus tools
	 */
	@Override
	public float getBlockHardness(World world, int x, int y, int z){
		AMVector3 mimicLocation = getMimicLocation(world, x, y, z);
		if (mimicLocation != null){
			Block mimicBlock = getMimicBlock(world, mimicLocation);
			if (mimicBlock != null && mimicBlock != Blocks.air){
				return mimicBlock.getBlockHardness(world, (int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z);
			}
		}
		return super.getBlockHardness(world, x, y, z);
	}

	/**
	 * Get the mimic'd explosion resistance
	 */
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ){
		AMVector3 mimicLocation = getMimicLocation(world, x, y, z);
		if (mimicLocation != null){
			Block mimicBlock = getMimicBlock(world, mimicLocation);
			if (mimicBlock != null && mimicBlock != Blocks.air){
				return mimicBlock.getExplosionResistance(par1Entity, world, (int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z, explosionX, explosionY, explosionZ);
			}
		}
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	/**
	 * Looks at mimic'd blocks, nearby players with true sight and meta flags to determine the IIcon to use.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int face){
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(BuffList.trueSight.id) && !alwaysPassable(blockAccess, x, y, z)){
			return revealedIcon;
		}
		AMVector3 mimicLocation = getMimicLocation(blockAccess, x, y, z);
		if (mimicLocation == null){
			return revealedIcon;
		}else{
			Block mimicBlock = getMimicBlock(blockAccess, mimicLocation);
			if (mimicBlock != null && mimicBlock != Blocks.air){
				return mimicBlock.getIcon(blockAccess, (int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z, face);
			}
		}
		return revealedIcon;
	}

	/**
	 * Drop as either an illusion block or an ethereal illusion block
	 */
	@Override
	public int damageDropped(int par1){
		if ((par1 & 0x8) == 0x8)
			return 1;
		return 0;
	}

	@Override
	protected boolean canSilkHarvest(){
		return false;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2){
		return revealedIcon;
	}

	/**
	 * Used in setting up recipes
	 */
	public Object[] GetRecipeComponents(boolean alwaysPassable){
		if (alwaysPassable){
			return new Object[]{
					"BRB", "RGR", "BRB",
					Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
					Character.valueOf('G'), Blocks.glass,
					Character.valueOf('B'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE)
			};
		}else{
			return new Object[]{
					"BRB", "R R", "BRB",
					Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
					Character.valueOf('B'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE)
			};
		}
	}

	public int GetCraftingQuantity(){
		return 4;
	}

	/**
	 * Spawns true sight particles randomly
	 */
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random){
		int meta = world.getBlockMetadata(i, j, k);

		EntityPlayer closest = world.getClosestPlayer(i, j, k, 5.0);
		if (closest == null){
			world.markBlockForUpdate(i, j, k);
			return;
		}else if (closest.isPotionActive(BuffList.trueSight.id)){
			world.markBlockForUpdate(i, j, k);
			Random rnd = new Random();
			if (tickCount++ == 20){
				this.tickCount = 0;
				for (int x = 0; x < 10; ++x){
					double factor = 1;
					double movement = 2;
					world.spawnParticle("reddust",
							i + ((rnd.nextDouble() * 2 - 1) * movement),
							j + ((rnd.nextDouble() * 2 - 1) * movement),
							k + ((rnd.nextDouble() * 2 - 1) * movement),
							rnd.nextDouble() / 4 + 0.75,
							0.5f,
							rnd.nextDouble() / 2 + 0.75);
				}
			}
		}else{
			world.markBlockForUpdate(i, j, k);
		}
	}

	/**
	 * Handles the actual collisions, or rather lack thereof
	 */
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity){

		if (alwaysPassable(par1World, par2, par3, par4))
			return;

		if (par7Entity instanceof EntityLivingBase && ((EntityLivingBase)par7Entity).isPotionActive(BuffList.trueSight.id)){
			return;
		}
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z){
		return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
	}

	/**
	 * Mimics color multiplier so as to blend in better
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z){
		AMVector3 mimicLocation = getMimicLocation(blockAccess, x, y, z);
		if (mimicLocation != null){
			Block mimicBlock = getMimicBlock(blockAccess, mimicLocation);
			if (mimicBlock != null && mimicBlock != Blocks.air){
				return mimicBlock.colorMultiplier(blockAccess, (int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z);
			}
		}
		return super.colorMultiplier(blockAccess, x, y, z);
	}

	/**
	 * Can't be normal since entities can pass through it - if it is normal then the "inside block" ui overlay happens and is annoying
	 */
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z){
		return false;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side){
		return false;
	}

	@Override
	public boolean isNormalCube(){
		return false;
	}

	/**
	 * Must not be opaque since the revealed IIcon is transparent; without this these blocks can be used as xray blocks
	 */
	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	/**
	 * Must be able to render with transparency, render pass 1 allows this.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z){
		AMVector3 mimicLocation = getMimicLocation(world, x, y, z);
		if (mimicLocation != null){
			Block mimicBlock = getMimicBlock(world, mimicLocation);
			if (mimicBlock != null && mimicBlock != Blocks.air){
				return mimicBlock.getLightOpacity(world, (int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z);
			}
		}
		return super.getLightOpacity(world, x, y, z);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		AMVector3 mimicLocation = getMimicLocation(world, x, y, z);
		if (mimicLocation != null){
			Block mimicBlock = getMimicBlock(world, mimicLocation);
			if (mimicBlock != null && mimicBlock != Blocks.air){
				return mimicBlock.getLightValue(world, (int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z);
			}
		}
		return super.getLightValue(world, x, y, z);
	}

	@Override
	public float getAmbientOcclusionLightValue(){
		return 1.0f;
	}

	@Override
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z){
		return super.getMixedBrightnessForBlock(world, x, y, z);
	}
}
