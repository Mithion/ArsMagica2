package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;

public class BlockFlickerHabitat extends PoweredBlock{

	protected BlockFlickerHabitat(){
		super(Material.rock);
		setHardness(2);
		setResistance(3);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityFlickerHabitat();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing face, float impx, float impy, float impz){
		super.onBlockActivated(world, pos, state, player, face, impx, impy, impz);

		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.crystalWrench){
			if (world.isRemote){
				player.swingItem();
			}
			return false;
		}else{
			player.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_FLICKER_HABITAT, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}
	
	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		setBlockMode(world, pos);
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		setBlockMode(worldIn, pos);
		super.onBlockAdded(worldIn, pos, state);
	}

	protected void setBlockMode(World world, BlockPos pos){
		if (world.isRemote)
			return;

		TileEntity ent = world.getTileEntity(x, y, z);
		int habCount = 0;

		if (ent instanceof TileEntityFlickerHabitat){
			TileEntityFlickerHabitat hab = (TileEntityFlickerHabitat)ent;
			for (EnumFacing direction : Enu.VALID_DIRECTIONS){
				Block block = world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				TileEntity te = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				if (block == BlocksCommonProxy.elementalAttuner && te != null && te instanceof TileEntityFlickerHabitat){
					TileEntityFlickerHabitat foundHab = (TileEntityFlickerHabitat)te;
					if (foundHab.isUpgrade() == false){
						habCount++;
						if (habCount == 1){
							hab.setUpgrade(true, direction);
						}else{
							world.func_147480_a(x, y, z, true);
						}
					}else{
						world.func_147480_a(x, y, z, true);
					}
				}
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block neighborBlockID){
		if (world.isRemote)
			return;

		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityFlickerHabitat){
			TileEntityFlickerHabitat hab = (TileEntityFlickerHabitat)te;

			if (hab.isUpgrade()){
				int habCount = 0;
				for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
					te = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
					if (te != null && te instanceof TileEntityFlickerHabitat){
						TileEntityFlickerHabitat foundHab = (TileEntityFlickerHabitat)te;
						if (foundHab.isUpgrade() == false){
							habCount++;
							if (habCount == 1){
							}else{
								world.func_147480_a(x, y, z, true);
							}
						}else{
							world.func_147480_a(x, y, z, true);
						}
					}
				}

				if (habCount == 0){
					world.func_147480_a(x, y, z, true);
				}
			}else{
				hab.scanForNearbyUpgrades();

				if (!hab.isUpgrade()){
					hab.scanForNearbyUpgrades();
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, Block oldBlockID, int oldMetadata){
		TileEntityFlickerHabitat habitat = (TileEntityFlickerHabitat)world.getTileEntity(x, y, z);

		//if there is no habitat at the location break out
		if (habitat == null)
			return;

		//if the habitat has a flicker throw it on the ground
		if (habitat.hasFlicker()){
			ItemStack stack = habitat.getStackInSlot(0);

			float offsetX = world.rand.nextFloat() * 0.8F + 0.1F;
			float offsetY = world.rand.nextFloat() * 0.8F + 0.1F;
			float offsetZ = world.rand.nextFloat() * 0.8F + 0.1F;
			float force = 0.05F;

			EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, stack);
			entityItem.motionX = (float)world.rand.nextGaussian() * force;
			entityItem.motionY = (float)world.rand.nextGaussian() * force + 0.2F;
			entityItem.motionZ = (float)world.rand.nextGaussian() * force;
			world.spawnEntityInWorld(entityItem);
		}

		if (!habitat.isUpgrade()){
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
				TileEntity te = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				if (te != null && te instanceof TileEntityFlickerHabitat){
					TileEntityFlickerHabitat upgHab = (TileEntityFlickerHabitat)te;

					if (upgHab.isUpgrade()){
						world.func_147480_a(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, true);
						world.setTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, null);
					}
				}
			}
		}

		super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
		return;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_){
		//intentionally do nothing
	}

	@Override
	public IIcon getIcon(int par1, int par2){
		return Blocks.iron_bars.getIcon(par1, par2);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, int meta){
		return true;
	}
}
