package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntitySeerStone;
import am2.guis.ArsMagicaGuiIdList;
import am2.texture.ResourceManager;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockSeerStone extends AMSpecialRenderPoweredBlock{

	public BlockSeerStone(){
		super(Material.glass);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4){
		int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		switch (meta){
		case 1:
			this.setBlockBounds(0.0f, 0.6f, 0.0f, 1.0f, 1.0f, 1.0f);
			break;
		case 2:
			this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.4f, 1.0f);
			break;
		case 3:
			this.setBlockBounds(0.0f, 0.0f, 0.6f, 1.0f, 1.0f, 1.0f);
			break;
		case 4:
			this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.4f);
			break;
		case 5:
			this.setBlockBounds(0.6f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			break;
		case 6:
			this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.4f, 1.0f, 1.0f);
			break;
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int meta){
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return (dir == ForgeDirection.DOWN && world.getBlock(x, y + 1, z).isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN)) ||
				(dir == ForgeDirection.UP && world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP)) ||
				(dir == ForgeDirection.NORTH && world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, ForgeDirection.NORTH)) ||
				(dir == ForgeDirection.SOUTH && world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, ForgeDirection.SOUTH)) ||
				(dir == ForgeDirection.WEST && world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, ForgeDirection.WEST)) ||
				(dir == ForgeDirection.EAST && world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, ForgeDirection.EAST));
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z){
		return world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, ForgeDirection.EAST) ||
				world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, ForgeDirection.WEST) ||
				world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, ForgeDirection.SOUTH) ||
				world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, ForgeDirection.NORTH) ||
				world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP) ||
				world.getBlock(x, y + 1, z).isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float impX, float impY, float impZ, int meta){
		int var10 = meta;
		var10 = -1;

		if (side == 0 && world.getBlock(x, y + 1, z).isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN)){
			var10 = 1;
		}

		if (side == 1 && world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP)){
			var10 = 2;
		}

		if (side == 2 && world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, ForgeDirection.NORTH)) //-z
		{
			var10 = 3;
		}

		if (side == 3 && world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, ForgeDirection.SOUTH)) //+z
		{
			var10 = 4;
		}

		if (side == 4 && world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, ForgeDirection.WEST)) //-x
		{
			var10 = 5;
		}

		if (side == 5 && world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, ForgeDirection.EAST)) //+x
		{
			var10 = 6;
		}

		return var10;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);

		TileEntity te = par1World.getTileEntity(par2, par3, par4);
		TileEntitySeerStone sste = null;
		if (te != null && te instanceof TileEntitySeerStone){
			sste = (TileEntitySeerStone)te;
		}else{
			return true;
		}

		if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, sste)){
			return true;
		}

		if (!KeystoneUtilities.instance.canPlayerAccess(sste, par5EntityPlayer, KeystoneAccessType.USE)){
			return true;
		}

		if (par5EntityPlayer.isSneaking()){
			sste.invertDetection();
			if (par1World.isRemote){
				par5EntityPlayer.addChatMessage(new ChatComponentText("Inverting detection mode: " + ((TileEntitySeerStone)te).isInvertingDetection()));
			}
			return true;
		}

		if (HandleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return true;
		}
		if (!par1World.isRemote)
			FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_SEER_STONE, par1World, par2, par3, par4);
		return true;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntitySeerStone();
	}

	@Override
	public boolean canProvidePower(){
		return true;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess par1iBlockAccess, int x, int y, int z, int l){
		TileEntity myTE = par1iBlockAccess.getTileEntity(x, y, z);
		if (myTE == null || !(myTE instanceof TileEntitySeerStone))
			return 0;
		return ((TileEntitySeerStone)myTE).HasSight() ? 15 : 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess par1iBlockAccess, int x, int y, int z, int l){
		TileEntity myTE = par1iBlockAccess.getTileEntity(x, y, z);
		if (myTE == null || !(myTE instanceof TileEntitySeerStone))
			return 0;
		return ((TileEntitySeerStone)myTE).HasSight() ? 15 : 0;
	}

	@Override
	public int quantityDropped(Random random){
		return 1;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){
		if (world.isRemote){
			super.breakBlock(world, i, j, k, par5, metadata);
			return;
		}
		Random rand = new Random();
		TileEntitySeerStone myTE = (TileEntitySeerStone)world.getTileEntity(i, j, k);
		if (myTE == null) return;
		for (int l = 0; l < myTE.getSizeInventory() - 3; l++){
			ItemStack itemstack = myTE.getStackInSlot(l);
			if (itemstack == null){
				continue;
			}
			float f = rand.nextFloat() * 0.8F + 0.1F;
			float f1 = rand.nextFloat() * 0.8F + 0.1F;
			float f2 = rand.nextFloat() * 0.8F + 0.1F;
			do{
				if (itemstack.stackSize <= 0){
					break;
				}
				int i1 = rand.nextInt(21) + 10;
				if (i1 > itemstack.stackSize){
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				ItemStack newItem = new ItemStack(itemstack.getItem(), i1, itemstack.getMetadata());
				newItem.setTagCompound(itemstack.getTagCompound());
				EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)rand.nextGaussian() * f3;
				entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}while (true);
		}
		super.breakBlock(world, i, j, k, par5, metadata);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("CasterRuneSide", par1IconRegister);
	}
}
