package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityParticleEmitter;
import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockParticleEmitter extends AMBlockContainer{

	protected BlockParticleEmitter(){
		super(Material.glass);
	}

	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z){
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
		return null;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		TileEntityParticleEmitter tile = (TileEntityParticleEmitter)world.getTileEntity(x, y, z);
		if(tile != null && !tile.getShow())
			return false;
		
		return super.removedByPlayer(world, player, x, y, z);
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
		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);

		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, stack);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityParticleEmitter();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int x, int y, int z, int l){
		int meta = par1iBlockAccess.getBlockMetadata(x, y, z);
		if ((meta & 0x8) == 0x8)
			return null;
		else
			return blockIcon;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z){
		int meta = access.getBlockMetadata(x, y, z);
		if ((meta & 0x8) == 0x8){
			this.setBlockBounds(0, 0, 0, 0.01f, 0.01f, 0.01f);
		}else{
			this.setBlockBounds(0, 0, 0, 1, 1, 1);
		}
	}

	@Override
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int meta, int pass){
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("decoBlockFrame", par1IconRegister);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if (par1World.isRemote){
			TileEntity te = par1World.getTileEntity(par2, par3, par4);
			if (te != null && te instanceof TileEntityParticleEmitter){
				if (par5EntityPlayer.inventory.getCurrentItem() != null && par5EntityPlayer.inventory.getCurrentItem().getItem() == ItemsCommonProxy.crystalWrench){
					if (AMCore.proxy.cwCopyLoc == null){
						par5EntityPlayer.addChatMessage(new ChatComponentText("Settings Copied."));
						AMCore.proxy.cwCopyLoc = new NBTTagCompound();
						((TileEntityParticleEmitter)te).writeSettingsToNBT(AMCore.proxy.cwCopyLoc);
					}else{
						((TileEntityParticleEmitter)te).readSettingsFromNBT(AMCore.proxy.cwCopyLoc);
						((TileEntityParticleEmitter)te).syncWithServer();
						AMCore.proxy.cwCopyLoc = null;
					}
				}else{
					AMCore.proxy.openParticleBlockGUI(par1World, par5EntityPlayer, (TileEntityParticleEmitter)te);
				}
			}
		}
		return true;
	}
}
