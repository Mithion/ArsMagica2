package am2.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityBlackAurem;
import am2.blocks.tileentities.TileEntityCelestialPrism;
import am2.blocks.tileentities.TileEntityObelisk;
import am2.guis.ArsMagicaGuiIdList;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEssenceGenerator extends AMSpecialRenderPoweredBlock {

	private int NexusType;

	public static final int NEXUS_STANDARD = 0;
	public static final int NEXUS_DARK = 1;
	public static final int NEXUS_LIGHT = 2;

	public BlockEssenceGenerator(int nexusType) {
	  super(Material.cloth);
	  setLightLevel(0.73f);
	  setTickRandomly(true);
	  setHardness(2f);
	  setResistance(2f);
	  this.NexusType = nexusType;
	  switch(this.NexusType){
	  case NEXUS_STANDARD:
		  setBlockBounds(0f, 0.0f, 0f, 1f, 2f, 1f);
		  break;
	  case NEXUS_LIGHT:
		  setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2f, 1.0f);
		  break;
	  case NEXUS_DARK:
		  setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 2f, 1.0f);
		  break;
	  }
	}

	private TileEntityObelisk getTileEntity(IBlockAccess blockAccess, int x, int y, int z){
		TileEntity te = blockAccess.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityObelisk){
			return (TileEntityObelisk)te;
		}
		return null;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int quantityDropped(Random random) {
	  return 1;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (this == BlocksCommonProxy.obelisk )
			drops.add(new ItemStack(BlocksCommonProxy.obelisk));
		else if (this == BlocksCommonProxy.blackAurem)
			drops.add(new ItemStack(BlocksCommonProxy.blackAurem));
		else if (this == BlocksCommonProxy.celestialPrism)
			drops.add(new ItemStack(BlocksCommonProxy.celestialPrism));
		return drops;
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		if (this == BlocksCommonProxy.blackAurem)
			return;
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		if (this == BlocksCommonProxy.blackAurem)
			return null;
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float interactX, float interactY, float interactZ) {

		if (HandleSpecialItems(world, player, x, y, z))
			return true;

		if (world.getBlock(x, y, z) == BlocksCommonProxy.obelisk)
			FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_OBELISK, world, x, y, z);		
		
		return super.onBlockActivated(world, x, y, z, player, face, interactX, interactY, interactZ);
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i) {
		if (this.NexusType == NEXUS_DARK)
			return new TileEntityBlackAurem();
		else if (this.NexusType == NEXUS_LIGHT)
			return new TileEntityCelestialPrism();
		else
			return new TileEntityObelisk();
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {

		if (par1World.isRemote){
			super.breakBlock(par1World, par2, par3, par4, par5, par6);
			return;
		}
		Random rand = new Random();
		TileEntityObelisk obelisk = getTileEntity(par1World, par2, par3, par4);
		if (obelisk == null) return;
		for(int l = 0; l < obelisk.getSizeInventory(); l++)
		{
			ItemStack itemstack = obelisk.getStackInSlot(l);
			if(itemstack == null)
			{
				continue;
			}
			float f = rand.nextFloat() * 0.8F + 0.1F;
			float f1 = rand.nextFloat() * 0.8F + 0.1F;
			float f2 = rand.nextFloat() * 0.8F + 0.1F;
			do
			{
				if(itemstack.stackSize <= 0)
				{
					break;
				}
				int i1 = rand.nextInt(21) + 10;
				if(i1 > itemstack.stackSize)
				{
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				ItemStack newItem = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
				newItem.setTagCompound(itemstack.getTagCompound());
				EntityItem entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)rand.nextGaussian() * f3;
				entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)rand.nextGaussian() * f3;
				par1World.spawnEntityInWorld(entityitem);
			} while(true);
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {

		int p = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
		
		byte meta = 3;
		if (p == 0){
			meta = 2;
		}else if (p == 1){
			meta = 1;
		}else if (p == 2){
			meta = 4;
		}else if (p == 3){
			meta = 3;
		}

		world.setBlockMetadataWithNotify(i, j, k, meta, 2);

		super.onBlockPlacedBy(world, i, j, k, entityliving, stack);
	}

}
