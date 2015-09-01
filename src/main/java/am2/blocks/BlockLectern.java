package am2.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import am2.blocks.tileentities.TileEntityLectern;
import am2.texture.ResourceManager;

public class BlockLectern extends AMSpecialRenderBlockContainer{

	protected BlockLectern() {
		super(Material.wood);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityLectern();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

		super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

		TileEntityLectern te = getTileEntity(world, x, y, z);
		if (te == null){
			return true;
		}
		Random rand = new Random();
		if (te.hasStack()){
			if (player.isSneaking()){
				if (!world.isRemote && ((player instanceof EntityPlayerMP) && ((EntityPlayerMP)player).theItemInWorldManager.getGameType() != GameType.ADVENTURE)){
					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;
					ItemStack newItem = new ItemStack(te.getStack().getItem(), 1, te.getStack().getItemDamage());
					if (te.getStack().stackTagCompound != null)
						newItem.setTagCompound((NBTTagCompound) te.getStack().stackTagCompound.copy());
					EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, newItem);
					float f3 = 0.05F;
					entityitem.motionX = (float)rand.nextGaussian() * f3;
					entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float)rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
					te.setStack(null);
				}
			}else{
				te.getStack().getItem().onItemRightClick(te.getStack(), world, player);
			}
		}else{
			if (player.getCurrentEquippedItem() != null){
				if (te.setStack(player.getCurrentEquippedItem())){
					player.getCurrentEquippedItem().stackSize --;
					if (player.getCurrentEquippedItem().stackSize <= 0){
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
				}
			}
		}

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		TileEntityLectern te = getTileEntity(world, x, y, z);
		if (te == null){
			return;
		}
		if (!world.isRemote){
			Random rand = new Random();
			if (te.hasStack()){
				float f = rand.nextFloat() * 0.8F + 0.1F;
				float f1 = rand.nextFloat() * 0.8F + 0.1F;
				float f2 = rand.nextFloat() * 0.8F + 0.1F;
				ItemStack newItem = new ItemStack(te.getStack().getItem(), 1, te.getStack().getItemDamage());
				if (te.getStack().stackTagCompound != null)
					newItem.setTagCompound((NBTTagCompound) te.getStack().stackTagCompound.copy());
				EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)rand.nextGaussian() * f3;
				entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private TileEntityLectern getTileEntity(World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityLectern){
			return (TileEntityLectern)te;
		}
		return null;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
		int p = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3; 

		byte byte0 = 3;

		if (p == 0)
		{
			byte0 = 4;
		}
		if (p == 1)
		{
			byte0 = 3;
		}
		if (p == 2)
		{
			byte0 = 2;
		}
		if (p == 3)
		{
			byte0 = 1;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = ResourceManager.RegisterTexture("Witchwood", par1IconRegister);
	}

}
