package am2.blocks;

import am2.AMCore;
import am2.api.power.PowerTypes;
import am2.blocks.tileentities.TileEntityManaBattery;
import am2.entities.EntityDummyCaster;
import am2.power.PowerNodeRegistry;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockManaBattery extends PoweredBlock{

	@SideOnly(Side.CLIENT)
	private IIcon frameIcon;

	public BlockManaBattery(){
		super(Material.iron);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		super.registerBlockIcons(par1IconRegister);
		frameIcon = ResourceManager.RegisterTexture("mana_battery_frame", par1IconRegister);
	}

	@Override
	public IIcon getIcon(int side, int meta){
		if (meta == 15)
			return frameIcon;
		return blockIcon;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if (!super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9))
			return true;
		
		if (par5EntityPlayer.isSneaking()){
			//Cheating slightly.
			if (!par1World.isRemote){
				destroy(par1World, par2, par3, par4);
				par1World.setBlockToAir(par2, par3, par4);
			}
					
		}else{

			if (par1World.isRemote){
				TileEntityManaBattery te = getTileEntity(par1World, par2, par3, par4);
				if (te != null){
				
						if (AMCore.config.colourblindMode()){
							par5EntityPlayer.addChatMessage(new ChatComponentText(String.format("Charge Level: %.2f %% [%s]", PowerNodeRegistry.For(par1World).getPower(te, te.getPowerType()) / te.getCapacity() * 100, getColorNameFromPowerType(te.getPowerType()))));
						}else{
							par5EntityPlayer.addChatMessage(new ChatComponentText(String.format("Charge Level: %s%.2f \u00A7f%%", te.getPowerType().chatColor(), PowerNodeRegistry.For(par1World).getPower(te, te.getPowerType()) / te.getCapacity() * 100)));
						}
				}
			}
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityManaBattery();
	}

	private TileEntityManaBattery getTileEntity(World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityManaBattery){
			return (TileEntityManaBattery)te;
		}
		return null;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random){
		if (meta == 0)
			return 1;
		else
			return 0;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack stack){
		if (stack != null){
			TileEntityManaBattery te = getTileEntity(par1World, par2, par3, par4);
			if (stack.stackTagCompound != null){
				if (stack.stackTagCompound.hasKey("mana_battery_charge") && stack.stackTagCompound.hasKey("mana_battery_powertype"))
					PowerNodeRegistry.For(par1World).setPower(te, PowerTypes.getByID(stack.stackTagCompound.getInteger("mana_battery_powertype")), stack.stackTagCompound.getFloat("mana_battery_charge"));
				else
					te.setPowerType(PowerTypes.NONE, false);
			}

		}
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, stack);
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion){
		destroy(world, x, y, z);
		super.onBlockExploded(world, x, y, z, explosion);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta){
		//destroy(world, x, y, z);
	}

	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer){
		destroy(par1World, par2, par3, par4);
		super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
	}

	@Override
	public void onBlockPreDestroy(World par1World, int par2, int par3, int par4, int par5){
		//destroy(par1World, par2, par3, par4);
		super.onBlockPreDestroy(par1World, par2, par3, par4, par5);
	}

	private void destroy(World world, int i, int j, int k){
		TileEntityManaBattery te = getTileEntity(world, i, j, k);
		
		if (te != null && !world.isRemote){
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			int dmg = (int)((PowerNodeRegistry.For(world).getPower(te, te.getPowerType()) / te.getCapacity()) * 100);
			if (dmg == 0) dmg = 1;
			ItemStack stack = new ItemStack(this);
			stack.damageItem(stack.getMaxDamage() - dmg, new EntityDummyCaster(world));
			stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setFloat("mana_battery_charge", PowerNodeRegistry.For(world).getPower(te, te.getPowerType()));
			stack.stackTagCompound.setInteger("mana_battery_powertype", te.getPowerType().ID());

			if (!stack.stackTagCompound.hasKey("Lore"))
				stack.stackTagCompound.setTag("Lore", new NBTTagList());

			NBTTagList tagList = new NBTTagList();
			PowerTypes powerType = te.getPowerType();
			float amt = PowerNodeRegistry.For(world).getPower(te, powerType);
			tagList.appendTag(new NBTTagString(String.format("Contains %.2f %s%s etherium", amt, powerType.chatColor(), powerType.name())));
			stack.stackTagCompound.setTag("Lore", tagList);

			EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, stack);
			float f3 = 0.05F;
			entityitem.motionX = (float)world.rand.nextGaussian() * f3;
			entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
			world.spawnEntityInWorld(entityitem);
		}
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int meta){
		TileEntityManaBattery batt = getTileEntity(world, x, y, z);
		if (batt == null)
			return 0;

		//can simply use getHighest, as batteries can only have *one* type. 
		//the only time they have more, is when they are at zero, but then it doesn't matter
		//as all power types are zero.
		//Once they get power a single time, they lock to that power type.
		float pct = PowerNodeRegistry.For(world).getHighestPower(batt) / batt.getCapacity();

		return (int)Math.floor(15.0f * pct);
	}

	@Override
	public boolean hasComparatorInputOverride(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		ItemStack stack = new ItemStack(this);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setFloat("mana_battery_charge", new TileEntityManaBattery().getCapacity());

		par3List.add(stack);
	}

	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z){
		TileEntity te = blockAccess.getTileEntity(x, y, z);
		if (te instanceof TileEntityManaBattery){
			TileEntityManaBattery battery = (TileEntityManaBattery)te;
			if (battery.getPowerType() == PowerTypes.DARK)
				return 0x850e0e;
			else if (battery.getPowerType() == PowerTypes.LIGHT)
				return 0x61cfc3;
			else if (battery.getPowerType() == PowerTypes.NEUTRAL)
				return 0x2683d2;
			else
				return 0xFFFFFF;
		}
		return 0xFFFFFF;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5){
		return new ArrayList<ItemStack>();
	}
}
