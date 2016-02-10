package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntitySeerStone;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSeerStone extends AMSpecialRenderPoweredBlock{

	public BlockSeerStone(){
		super(Material.glass);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos){
		int meta = world.getBlockState(pos).getBlock().getMetaFromState(); // TODO figure out how to make this work
		switch (meta){
		case 1:
			this.setBlockBounds(0.0f, 0.6f, 0.0f, 1.0f, 1.0f, 1.0f);
			break;
		case 2:
			this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.4f, 1.0f);
			break;
		case 3:
			this.setBlockBounds(0.0f, 0.0f, 0.6f, 1.0f, 1.0f, 1.0f);
			break;
		case 4:
			this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.4f);
			break;
		case 5:
			this.setBlockBounds(0.6f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			break;
		case 6:
			this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.4f, 1.0f, 1.0f);
			break;
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
		return (facing == EnumFacing.DOWN && world.getBlockState(pos.add(x, y + 1, z)).getBlock().isSideSolid(world, pos.add(x, y + 1, z), EnumFacing.DOWN)) ||
				(facing == EnumFacing.UP && world.getBlockState(pos.add(x, y - 1, z)).getBlock().isSideSolid(world, pos.add(x, y - 1, z), EnumFacing.UP)) ||
				(facing == EnumFacing.NORTH && world.getBlockState(pos.add(x, y, z + 1)).getBlock().isSideSolid(world, pos.add(x, y, z + 1), EnumFacing.NORTH)) ||
				(facing == EnumFacing.SOUTH && world.getBlockState(pos.add(x, y, z - 1)).getBlock().isSideSolid(world, pos.add(x, y, z - 1), EnumFacing.SOUTH)) ||
				(facing == EnumFacing.WEST && world.getBlockState(pos.add(x + 1, y, z)).getBlock().isSideSolid(world, pos.add(x + 1, y, z), EnumFacing.WEST)) ||
				(facing == EnumFacing.EAST && world.getBlockState(pos.add(x - 1, y, z)).getBlock().isSideSolid(world, pos.add(x - 1, y, z), EnumFacing.EAST));
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
		return world.getBlockState(pos.add(x - 1, y, z)).getBlock().isSideSolid(world, pos.add(x - 1, y, z), EnumFacing.EAST) ||
				world.getBlockState(pos.add(x + 1, y, z)).getBlock().isSideSolid(world, pos.add(x + 1, y, z), EnumFacing.WEST) ||
				world.getBlockState(pos.add(x, y, z - 1)).getBlock().isSideSolid(world, pos.add(x, y, z - 1), EnumFacing.SOUTH) ||
				world.getBlockState(pos.add(x, y, z + 1)).getBlock().isSideSolid(world, pos.add(x, y, z + 1), EnumFacing.NORTH) ||
				world.getBlockState(pos.add(x, y - 1, z)).getBlock().isSideSolid(world, pos.add(x, y - 1, z), EnumFacing.UP) ||
				world.getBlockState(pos.add(x, y + 1, z)).getBlock().isSideSolid(world, pos.add(x, y + 1, z), EnumFacing.DOWN);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

		int var10 = meta;
		var10 = -1;

		if (facing == EnumFacing.DOWN && world.getBlockState(pos.add(x, y + 1, z)).getBlock().isSideSolid(world, pos.add(x, y + 1, z), EnumFacing.DOWN)){
			var10 = 1;
		}

		if (facing == EnumFacing.UP && world.getBlockState(pos.add(x, y - 1, z)).getBlock().isSideSolid(world, pos.add(x, y - 1, z), EnumFacing.UP)){
			var10 = 2;
		}

		if (facing == EnumFacing.NORTH && world.getBlockState(pos.add(x, y, z + 1)).getBlock().isSideSolid(world, pos.add(x, y, z + 1), EnumFacing.NORTH)) //-z
		{
			var10 = 3;
		}

		if (facing == EnumFacing.SOUTH && world.getBlockState(pos.add(x, y, z - 1)).getBlock().isSideSolid(world, pos.add(x, y, z - 1), EnumFacing.SOUTH)) //+z
		{
			var10 = 4;
		}

		if (facing == EnumFacing.WEST && world.getBlockState(pos.add(x + 1, y, z)).getBlock().isSideSolid(world, pos.add(x + 1, y, z), EnumFacing.WEST)) //-x
		{
			var10 = 5;
		}

		if (facing == EnumFacing.EAST && world.getBlockState(pos.add(x - 1, y, z)).getBlock().isSideSolid(world, pos.add(x - 1, y, z), EnumFacing.EAST)) //+x
		{
			var10 = 6;
		}

		return getStateFromMeta(var10);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

		TileEntity te = world.getTileEntity(pos);
		TileEntitySeerStone sste = null;
		if (te != null && te instanceof TileEntitySeerStone){
			sste = (TileEntitySeerStone)te;
		}else{
			return true;
		}

		if (KeystoneUtilities.HandleKeystoneRecovery(player, sste)){
			return true;
		}

		if (!KeystoneUtilities.instance.canPlayerAccess(sste, player, KeystoneAccessType.USE)){
			return true;
		}

		if (player.isSneaking()){
			sste.invertDetection();
			if (world.isRemote){
                player.addChatMessage(new ChatComponentText("Inverting detection mode: " + ((TileEntitySeerStone)te).isInvertingDetection()));
			}
			return true;
		}

		if (handleSpecialItems(world, player, pos)){
			return true;
		}
		if (!world.isRemote)
			FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_SEER_STONE, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, pos, player, willHarvest);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntitySeerStone();
	}

	@Override
	public boolean canProvidePower(){
		return true;
	}

	@Override
	public int quantityDropped(Random random){
		return 1;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		if (world.isRemote){
			super.breakBlock(world, pos, state);
			return;
		}
		TileEntitySeerStone myTE = (TileEntitySeerStone)world.getTileEntity(pos);
		if (myTE == null) return;
		for (int l = 0; l < myTE.getSizeInventory() - 3; l++){
			ItemStack itemstack = myTE.getStackInSlot(l);
			if (itemstack == null){
				continue;
			}
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			do{
				if (itemstack.stackSize <= 0){
					break;
				}
				int i1 = world.rand.nextInt(21) + 10;
				if (i1 > itemstack.stackSize){
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				ItemStack newItem = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
				newItem.setTagCompound(itemstack.getTagCompound());
				EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)world.rand.nextGaussian() * f3;
				entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}while (true);
		}
		super.breakBlock(world, pos, state);
	}
}
