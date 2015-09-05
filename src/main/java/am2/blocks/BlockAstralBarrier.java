package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.blocks.tileentities.TileEntityAstralBarrier;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAstralBarrier extends AMSpecialRenderPoweredBlock{

	public BlockAstralBarrier(){
		super(Material.rock);
		setHardness(3.0f);
		setResistance(2.0f);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){

		TileEntity te = par1World.getTileEntity(par2, par3, par4);
		TileEntityAstralBarrier abte = null;
		if (te != null && te instanceof TileEntityAstralBarrier){
			abte = (TileEntityAstralBarrier)te;
		}else{
			return true;
		}

		if (HandleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return true;
		}
		if (!par1World.isRemote)
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4))))
				return true;

		if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4), par5EntityPlayer)){
			if (par5EntityPlayer.isSneaking()){
				if (par1World.isRemote){
					abte.ToggleAuraDisplay();
					par5EntityPlayer.addChatMessage(new ChatComponentText("Barrier Aura Toggled"));
				}
			}else{
				if (!par1World.isRemote){
					super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
					FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ASTRAL_BARRIER, par1World, par2, par3, par4);
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){
		if (world.isRemote){
			super.breakBlock(world, i, j, k, par5, metadata);
			return;
		}
		Random rand = new Random();
		TileEntityAstralBarrier barrier = (TileEntityAstralBarrier)world.getTileEntity(i, j, k);
		if (barrier == null) return;
		for (int l = 0; l < barrier.getSizeInventory() - 3; l++){
			ItemStack itemstack = barrier.getStackInSlot(l);
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

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityAstralBarrier();
	}

}
