package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockInscriptionTable extends AMSpecialRenderBlockContainer{

	public BlockInscriptionTable(){
		super(Material.wood);

		//setTextureFile(AMCore.proxy.getOverrideBlockTexturePath());
		setHardness(2.0f);
		setResistance(2.0f);
		setLightLevel(0.8f);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.3f, 1.0f);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack){
		int p = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;

		byte byte0 = 3;

		int nX = x;
		int nZ = z;

		if (p == 0){
			byte0 = 3;
			nZ++;
		}
		if (p == 1){
			byte0 = 2;
			nX--;
		}
		if (p == 2){
			byte0 = 1;
			nZ--;
		}
		if (p == 3){
			byte0 = 4;
			nX++;
		}

		if (!world.isAirBlock(nX, y, nZ)){
			world.setBlock(x, y, z, Blocks.air);
			if (entity instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer)entity;
				player.inventory.addItemStackToInventory(new ItemStack(BlocksCommonProxy.inscriptionTable));
			}
			return;
		}

		world.setBlockMetadataWithNotify(x, y, z, byte0 | 0x8, 2);

		world.setBlock(nX, y, nZ, this);
		world.setBlockMetadataWithNotify(nX, y, nZ, byte0, 2);

		super.onBlockPlacedBy(world, x, y, z, entity, stack);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		return 12;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k){
		super.onBlockAdded(world, i, j, k);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);

		if (par1World.isRemote){
			return true;
		}

		TileEntityInscriptionTable te = (TileEntityInscriptionTable)par1World.getTileEntity(par2, par3, par4);
		TileEntityInscriptionTable tealt = te;

		int meta = par1World.getBlockMetadata(par2, par3, par4);
		boolean isLeft = (meta & 0x8) == 0x0;
		if (te != null){
			int checkMeta = meta & ~0x8;
			if (!isLeft){
				switch (checkMeta){
				case 1:
					par4--;
					break;
				case 2:
					par2--;
					break;
				case 3:
					par4++;
					break;
				case 4:
					par2++;
					break;
				}

				te = (TileEntityInscriptionTable)par1World.getTileEntity(par2, par3, par4);
			}else{
				int tx = par2;
				int ty = par3;
				int tz = par4;
				switch (checkMeta){
				case 1:
					tz++;
					break;
				case 2:
					tx++;
					break;
				case 3:
					tz--;
					break;
				case 4:
					tx--;
					break;
				}

				tealt = (TileEntityInscriptionTable)par1World.getTileEntity(tx, ty, tz);
			}
		}

		if (te == null)
			return true;

		if (te.isInUse(par5EntityPlayer)){
			par5EntityPlayer.addChatMessage(new ChatComponentText("Someone else is using this."));
			return true;
		}

		ItemStack curItem = par5EntityPlayer.getCurrentEquippedItem();
		if (curItem != null && curItem.getItem() == ItemsCommonProxy.inscriptionUpgrade){
			if (te.getUpgradeState() == curItem.getItemDamage()){
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
				te.incrementUpgradeState();
				tealt.incrementUpgradeState();
				return true;
			}
		}

		FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_INSCRIPTION_TABLE, par1World, par2, par3, par4);

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityInscriptionTable();
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){
		TileEntityInscriptionTable insc = (TileEntityInscriptionTable)world.getTileEntity(i, j, k);

		if (insc == null) return;

		int x = i;
		int z = k;
		boolean main = (metadata & 0x8) == 0x8;

		metadata &= ~0x8;

		switch (metadata){
		case 1:
			z += (main) ? -1 : 1;
			break;
		case 2:
			x += (main) ? -1 : 1;
			break;
		case 3:
			z += (main) ? 1 : -1;
			break;
		case 4:
			x += (main) ? 1 : -1;
			break;
		}

		if (world.getBlock(x, j, z) == this)
			world.setBlock(x, j, z, Blocks.air);

		if (!world.isRemote && main){
			for (int l = 0; l < insc.getSizeInventory(); l++){
				ItemStack itemstack = insc.getStackInSlot(l);
				if (itemstack == null){
					continue;
				}
				spawnItemOnBreak(world, i, j, k, par5, metadata, itemstack);
			}

			int stat = insc.getUpgradeState();
			for (int m = 0; m < stat; ++m)
				spawnItemOnBreak(world, i, j, k, par5, metadata, new ItemStack(ItemsCommonProxy.inscriptionUpgrade, 1, m));
		}

		super.breakBlock(world, i, j, k, par5, metadata);
	}

	private void spawnItemOnBreak(World world, int i, int j, int k, Block par5, int metadata, ItemStack itemstack){
		float f = world.rand.nextFloat() * 0.8F + 0.1F;
		float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
		float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
		do{
			if (itemstack.stackSize <= 0){
				break;
			}
			int i1 = world.rand.nextInt(21) + 10;
			if (i1 > itemstack.stackSize){
				i1 = itemstack.stackSize;
			}
			itemstack.stackSize -= i1;
			ItemStack newItem = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
			newItem.setTagCompound(itemstack.getTagCompound());
			EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newItem);
			float f3 = 0.05F;
			entityitem.motionX = (float)world.rand.nextGaussian() * f3;
			entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
			world.spawnEntityInWorld(entityitem);
		}while (true);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("Witchwood", par1IconRegister);
	}
}
