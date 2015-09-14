package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.blocks.tileentities.TileEntityMagiciansWorkbench;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMagiciansWorkbench extends AMSpecialRenderBlockContainer{

	protected BlockMagiciansWorkbench(){
		super(Material.wood);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityMagiciansWorkbench();
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack stack){
		int p = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

		byte byte0 = 3;

		if (p == 0){
			byte0 = 2;
		}
		if (p == 1){
			byte0 = 1;
		}
		if (p == 2){
			byte0 = 4;
		}
		if (p == 3){
			byte0 = 3;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);

		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, stack);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){

		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityMagiciansWorkbench){

			if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)te))
				return true;

			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)te, player)){

				super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.workbenchUpgrade){
					((TileEntityMagiciansWorkbench)te).setUpgradeStatus(TileEntityMagiciansWorkbench.UPG_CRAFT, true);

					if (!world.isRemote){
						ItemStack stack = player.getCurrentEquippedItem();
						stack.stackSize--;

						if (stack.stackSize <= 0)
							stack = null;

						player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
					}
					return true;
				}else{
					if (!world.isRemote){
						super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
						FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_MAGICIANS_WORKBENCH, world, x, y, z);
					}
				}
			}
		}

		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("plankWitchwood", par1IconRegister);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player){
	  TileEntityMagiciansWorkbench receptacle = (TileEntityMagiciansWorkbench)world.getTileEntity(x, y, z);
	  if (receptacle == null)
	    return;
	  if (KeystoneUtilities.instance.canPlayerAccess(receptacle, player)){
	    for (int i = receptacle.getSizeInventory() - 3; i < receptacle.getSizeInventory(); i++){
	      receptacle.decrStackSize(i, 9001);
	      // arbitrary number, just in case rune stack sizes increase in the future
	      // yes, it's hard-coded; yes, it's also less computationally intensive than a stack size lookup
	    }
	  }
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){

		if (world.isRemote){
			super.breakBlock(world, i, j, k, par5, metadata);
			return;
		}
		Random rand = new Random();
		TileEntityMagiciansWorkbench workbench = (TileEntityMagiciansWorkbench)world.getTileEntity(i, j, k);
		if (workbench == null || KeystoneUtilities.instance.getKeyFromRunes(workbench.getRunesInKey()) != 0) return;

		for (int l = 0; l < workbench.getSizeInventory() - 3; l++){
			ItemStack itemstack = workbench.getStackInSlot(l);
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
				ItemStack newItem = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
				newItem.setTagCompound(itemstack.getTagCompound());
				EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)rand.nextGaussian() * f3;
				entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}while (true);
		}

		if(workbench.getUpgradeStatus(TileEntityMagiciansWorkbench.UPG_CRAFT)){
			float f = rand.nextFloat() * 0.8F + 0.1F;
			float f1 = rand.nextFloat() * 0.8F + 0.1F;
			float f2 = rand.nextFloat() * 0.8F + 0.1F;
			ItemStack newItem = new ItemStack(ItemsCommonProxy.workbenchUpgrade, 1);
			EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newItem);
			float f3 = 0.05F;
			entityitem.motionX = (float)rand.nextGaussian() * f3;
			entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)rand.nextGaussian() * f3;
			world.spawnEntityInWorld(entityitem);
		}
		
		super.breakBlock(world, i, j, k, par5, metadata);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}
}
