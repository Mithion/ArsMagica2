package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityKeystoneRecepticle;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemKeystone;
import am2.texture.ResourceManager;
import am2.utility.KeystoneUtilities;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockKeystoneReceptacle extends AMSpecialRenderPoweredBlock{
	public BlockKeystoneReceptacle(){
		super(Material.rock);
		setHardness(4.5f);
		setResistance(10f);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityKeystoneRecepticle();
	}

	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, x, y, z, par5EntityPlayer, par6, par7, par8, par9);

		if (HandleSpecialItems(par1World, par5EntityPlayer, x, y, z)){
			return true;
		}


		TileEntity myTE = par1World.getTileEntity(x, y, z);
		if (myTE == null || !(myTE instanceof TileEntityKeystoneRecepticle)){
			return true;
		}
		TileEntityKeystoneRecepticle receptacle = (TileEntityKeystoneRecepticle)myTE;

		if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, receptacle)){
			return true;
		}


		if (par5EntityPlayer.isSneaking()){
			if (!par1World.isRemote && KeystoneUtilities.instance.canPlayerAccess(receptacle, par5EntityPlayer, KeystoneAccessType.USE)){
				FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE_LOCKABLE, par1World, x, y, z);
			}
		}else{
			if (receptacle.canActivate()){
				long key = 0;
				ItemStack rightClickItem = par5EntityPlayer.getCurrentEquippedItem();
				if (rightClickItem != null && rightClickItem.getItem() instanceof ItemKeystone){
					key = ((ItemKeystone)rightClickItem.getItem()).getKey(rightClickItem);
				}
				receptacle.setActive(key);
			}else if (receptacle.isActive()){
				receptacle.deactivate();
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack stack){
		int p = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

		byte byte0 = 3;

		if (p == 0){
			byte0 = 1;
		}
		if (p == 1){
			byte0 = 0;
		}
		if (p == 2){
			byte0 = 3;
		}
		if (p == 3){
			byte0 = 2;
		}

		AMCore.instance.proxy.blocks.registerKeystonePortal(par2, par3, par4, par1World.provider.dimensionId);

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);

		TileEntityKeystoneRecepticle receptacle = (TileEntityKeystoneRecepticle)par1World.getTileEntity(par2, par3, par4);
		receptacle.onPlaced();

		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, stack);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (KeystoneUtilities.instance.getKeyFromRunes(lockable.getRunesInKey()) != 0){
			if (!world.isRemote)
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.clearKey")));
			return false;
		}

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("custom/KeystoneReceptacle.png", par1IconRegister);
	}
}
