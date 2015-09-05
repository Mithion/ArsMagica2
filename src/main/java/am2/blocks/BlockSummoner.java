package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.blocks.tileentities.TileEntitySummoner;
import am2.guis.ArsMagicaGuiIdList;
import am2.texture.ResourceManager;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSummoner extends AMSpecialRenderPoweredBlock{

	public BlockSummoner(){
		super(Material.wood);
		setHardness(2.0f);
		setResistance(2.0f);
		setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 1.2f, 0.75f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntitySummoner();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){

		super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);

		if (HandleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return true;
		}

		if (!par1World.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, (IKeystoneLockable)par1World.getTileEntity(par2, par3, par4)))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4), par5EntityPlayer)){
				super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
				FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_SUMMONER, par1World, par2, par3, par4);
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack){
		int y = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;
		Vec3 look = par5EntityLiving.getLook(1.0f);
		int p = (int)(Math.round(look.yCoord * 0.6) + 1) & 3;

		byte byte0 = 3;

		if (y == 0){
			byte0 = 0;
		}else if (y == 1){
			byte0 = 3;
		}else if (y == 2){
			byte0 = 2;
		}else if (y == 3){
			byte0 = 1;
		}

		if (p == 0){
			byte0 |= 0x4;
		}else if (p == 2){
			byte0 |= 0x8;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){
		if (world.isRemote){
			super.breakBlock(world, i, j, k, par5, metadata);
			return;
		}
		Random rand = new Random();
		TileEntitySummoner summoner = (TileEntitySummoner)world.getTileEntity(i, j, k);
		if (summoner == null) return;
		for (int l = 0; l < summoner.getSizeInventory() - 3; l++){
			ItemStack itemstack = summoner.getStackInSlot(l);
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
				ItemStack newStack = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
				if (itemstack.hasTagCompound()){
					newStack.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
				}
				EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newStack);
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
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("essence_refiner_side", par1IconRegister);
	}
}
