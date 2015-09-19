package am2.blocks;

import am2.AMCore;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityKeystoneChest;
import am2.guis.ArsMagicaGuiIdList;
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


public class BlockKeystoneChest extends AMSpecialRenderBlockContainer{

	protected BlockKeystoneChest(){
		super(Material.wood);
		this.setCreativeTab(BlocksCommonProxy.blockTab);
		this.setResistance(900000);
		this.setHardness(3.0f);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4){
		super.onBlockAdded(par1World, par2, par3, par4);
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityKeystoneChest();
	}

	@Override
	public boolean onBlockActivated(World world, int par2, int par3, int par4, EntityPlayer entityplayer, int par6, float par7, float par8, float par9){

		super.onBlockActivated(world, par2, par3, par4, entityplayer, par6, par7, par8, par9);

		if (!world.isRemote){

			TileEntity myTE = world.getTileEntity(par2, par3, par4);

			if (!(myTE instanceof TileEntityKeystoneChest)) return false;

			TileEntityKeystoneChest te = (TileEntityKeystoneChest)myTE;
			ItemStack currentItem = entityplayer.getCurrentEquippedItem();

			if (KeystoneUtilities.HandleKeystoneRecovery(entityplayer, te)){
				return true;
			}

			if (!KeystoneUtilities.instance.canPlayerAccess(te, entityplayer, KeystoneAccessType.USE)){
				return true;
			}

			/*PacketDispatcher.sendPacketToPlayer(AMCore.instance.proxy.packetSender.createArsMagicaClientPacket(AMPacketIDs.KEYSTONE_CHEST_GUI_OPEN,
					new AMDataWriter().add(par2).add(par3).add(par4).generate()), (Player)entityplayer);*/

			FMLNetworkHandler.openGui(entityplayer, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE_CHEST, world, par2, par3, par4);

			return true;
		}else{
			return true;
		}
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player){
		TileEntityKeystoneChest receptacle = (TileEntityKeystoneChest)world.getTileEntity(x, y, z);
		if (receptacle == null)
			return;
		if (KeystoneUtilities.instance.canPlayerAccess(receptacle, player, KeystoneAccessType.BREAK)){
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
		TileEntityKeystoneChest receptacle = (TileEntityKeystoneChest)world.getTileEntity(i, j, k);

		if (receptacle == null) return;

		if (KeystoneUtilities.instance.getKeyFromRunes(receptacle.getRunesInKey()) == 0){

			for (int l = 0; l < receptacle.getSizeInventory() - 3; l++){
				ItemStack itemstack = receptacle.getStackInSlot(l);
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
			super.breakBlock(world, i, j, k, par5, metadata);
		}
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack stack){
		int p = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

		byte byte0 = 3;

		if (p == 0){
			byte0 = 1;
		}
		if (p == 1){
			byte0 = 4;
		}
		if (p == 2){
			byte0 = 3;
		}
		if (p == 3){
			byte0 = 2;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, stack);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		TileEntityKeystoneChest chest = (TileEntityKeystoneChest)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(chest, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4){
		int var5 = 0;

		if (par1World.getBlock(par2 - 1, par3, par4) == this){
			++var5;
		}

		if (par1World.getBlock(par2 + 1, par3, par4) == this){
			++var5;
		}

		if (par1World.getBlock(par2, par3, par4 - 1) == this){
			++var5;
		}

		if (par1World.getBlock(par2, par3, par4 + 1) == this){
			++var5;
		}

		return !(var5 > 0);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
	}
}
