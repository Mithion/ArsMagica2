package am2.blocks;

import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.blocks.tileentities.TileEntityAMPower;
import am2.items.ItemsCommonProxy;
import am2.power.PowerNodeRegistry;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class PoweredBlock extends AMBlockContainer{
	public PoweredBlock(Material material){
		super(material);
	}

	protected boolean handleSpecialItems(World world, EntityPlayer player, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

		if (handleSpecialItems(world, player, pos)){
			return false;
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof IPowerNode){
			PowerNodeRegistry.For(world).registerPowerNode((IPowerNode)te);
		}
	}
}
