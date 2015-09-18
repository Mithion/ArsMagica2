package am2.blocks;

import am2.AMCore;
// import am2.api.blocks.IKeystoneLockable;
// import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityArmorImbuer;
import am2.guis.ArsMagicaGuiIdList;
import am2.texture.ResourceManager;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockArmorInfuser extends PoweredBlock{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	private String[] textureNames = {"armor_infuser_side", "armor_infuser_top", "armor_infuser_bottom"};

	protected BlockArmorInfuser(){
		super(Material.iron);
		setHardness(4.0f);
		setResistance(4.0f);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister IIconRegister){
		this.icons = new IIcon[textureNames.length];

		for (int i = 0; i < textureNames.length; ++i){
			this.icons[i] = ResourceManager.RegisterTexture(textureNames[i], IIconRegister);
		}
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int side){
		if (side == 1) //top
		{
			return icons[1];
		}
		if (side == 0) //bottom
		{
			return icons[2];
		}

		return icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if (side == 1) //top
		{
			return icons[1];
		}
		if (side == 0) //bottom
		{
			return icons[2];
		}

		return icons[0];
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityArmorImbuer();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
		if (HandleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return true;
		}
		if (!par1World.isRemote){
			super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
			FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ARMOR_INFUSION, par1World, par2, par3, par4);
			/*
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4), par5EntityPlayer, KeystoneAccessType.USE)){
				super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
				FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ARMOR_INFUSION, par1World, par2, par3, par4);
			}
			*/
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
		TileEntityArmorImbuer imbuer = (TileEntityArmorImbuer)world.getTileEntity(i, j, k);
		if (imbuer == null) return;
		for (int l = 0; l < imbuer.getSizeInventory() - 3; l++){
			ItemStack itemstack = imbuer.getStackInSlot(l);
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
