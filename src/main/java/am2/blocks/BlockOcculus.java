package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityOcculus;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOcculus extends AMSpecialRenderBlockContainer{

	protected BlockOcculus(){
		super(Material.rock);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityOcculus();
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack stack){
		int p = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

		byte byte0 = 3;

		if (p == 0){
			byte0 = 4;
		}
		if (p == 1){
			byte0 = 3;
		}
		if (p == 2){
			byte0 = 2;
		}
		if (p == 3){
			byte0 = 1;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0, 2);

		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, stack);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);

		if (par1World.isRemote){
			if (ExtendedProperties.For(par5EntityPlayer).getMagicLevel() > 0)
				AMCore.proxy.openSkillTreeUI(par1World, par5EntityPlayer);
			else
				par5EntityPlayer.addChatMessage(new ChatComponentText("You cannot comprehend what you see inside the occulus."));
		}
		return true;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}
}
