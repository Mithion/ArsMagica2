package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityArcaneReconstructor;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockArcaneReconstructor extends AMSpecialRenderPoweredBlock{

	private final Random rand;

	protected BlockArcaneReconstructor(){
		super(Material.rock);
		rand = new Random();
		setHardness(3.0f);
		setResistance(3.0f);
		setBlockBounds(0, 0, 0, 1, 0.52f, 1);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityArcaneReconstructor();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if (HandleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return true;
		}
		if (!par1World.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4), par5EntityPlayer, KeystoneAccessType.USE)){
				FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ARCANE_RECONSTRUCTOR, par1World, par2, par3, par4);
				super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){
		if (!world.isRemote){
			TileEntityArcaneReconstructor reconstructor = (TileEntityArcaneReconstructor)world.getTileEntity(i, j, k);
			if (reconstructor == null) return;
			for (int l = 0; l < reconstructor.getSizeInventory() - 3; l++){
				ItemStack itemstack = reconstructor.getStackInSlot(l);
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
		}
		super.breakBlock(world, i, j, k, par5, metadata);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}
}
