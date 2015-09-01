package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.blocks.tileentities.TileEntityAMPower;
import am2.items.ItemsCommonProxy;
import am2.power.PowerNodeRegistry;

public abstract class PoweredBlock extends AMBlockContainer{
	public PoweredBlock(Material material){
		super(material);
	}

	protected boolean HandleSpecialItems(World world, EntityPlayer player, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof TileEntityAMPower)){
			return false;
		}

		if (player.getCurrentEquippedItem() != null && (player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.spellStaffMagitech || player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.crystalWrench)){
			return true;
		}

		return false;
	}

	protected String getColorNameFromPowerType(PowerTypes type){
		return StatCollector.translateToLocal("am2.gui.powerType" + type.name());
	}


	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		TileEntity myTE = par1World.getTileEntity(par2, par3, par4);
		if (myTE != null && myTE instanceof TileEntityAMPower){
			((TileEntityAMPower)myTE).onDeath(par1World);
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);

		if (HandleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return false;
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, elb, stack);

		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IPowerNode){
			PowerNodeRegistry.For(world).registerPowerNode((IPowerNode) te);
		}
	}
}
