package am2.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public class BlockFlickerHabitat extends PoweredBlock {

	protected BlockFlickerHabitat() {
		super(Material.rock);
		setHardness(2);
		setResistance(3);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityFlickerHabitat();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float impx, float impy, float impz) {
		super.onBlockActivated(world, x, y, z, player, meta, impx, impy, impz);

		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.crystalWrench){
			if(world.isRemote){
				player.swingItem();
			}
			return false;
		} else {
			FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_FLICKER_HABITAT, world, x, y, z);
			return true;
		}
	}

	@Override
	public int getRenderType() {
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
		setBlockMode(world, x, y, z);
		super.onBlockPlacedBy(world, x, y, z, elb, stack);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		setBlockMode(world, x, y, z);
		super.onBlockAdded(world, x, y, z);
	}

	protected void setBlockMode(World world, int x, int y, int z){
		if(world.isRemote)
			return;

		TileEntity ent = world.getTileEntity(x, y, z);
		int habCount = 0;

		if(ent instanceof TileEntityFlickerHabitat){
			TileEntityFlickerHabitat hab = (TileEntityFlickerHabitat)ent;
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
				Block block = world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				TileEntity te = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				if (block == BlocksCommonProxy.elementalAttuner && te != null && te instanceof TileEntityFlickerHabitat){
					TileEntityFlickerHabitat foundHab = (TileEntityFlickerHabitat) te;
					if(foundHab.isUpgrade() == false){
						habCount++;
						if(habCount == 1){
							hab.setUpgrade(true, direction);
						} else {
							world.func_147480_a(x, y, z, true);
						}
					} else {
						world.func_147480_a(x, y, z, true);
					}
				}
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlockID) {
		if(world.isRemote)
			return;

		TileEntity te = world.getTileEntity(x, y, z);

		if(te instanceof TileEntityFlickerHabitat){
			TileEntityFlickerHabitat hab = (TileEntityFlickerHabitat)te;

			if(hab.isUpgrade()){
				int habCount = 0;
				for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
					te = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
					if (te != null && te instanceof TileEntityFlickerHabitat){
						TileEntityFlickerHabitat foundHab = (TileEntityFlickerHabitat) te;
						if(foundHab.isUpgrade() == false){
							habCount++;
							if(habCount == 1){
							} else {
								world.func_147480_a(x, y, z, true);
							}
						} else {
							world.func_147480_a(x, y, z, true);
						}
					}
				}

				if(habCount == 0){
					world.func_147480_a(x, y, z, true);
				}
			} else {
				hab.scanForNearbyUpgrades();

				if(!hab.isUpgrade()){
					hab.scanForNearbyUpgrades();
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata) {
		TileEntityFlickerHabitat habitat = (TileEntityFlickerHabitat)world.getTileEntity(x, y, z);

		//if there is no habitat at the location break out
		if(habitat == null)
			return;

		//if the habitat has a flicker throw it on the ground
		if(habitat.hasFlicker()){
			Random rand = new Random();
			ItemStack stack = habitat.getStackInSlot(0);

			float offsetX = rand.nextFloat() * 0.8F + 0.1F;
			float offsetY = rand.nextFloat() * 0.8F + 0.1F;
			float offsetZ = rand.nextFloat() * 0.8F + 0.1F;
			float force = 0.05F;

			EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, stack);
			entityItem.motionX = (float)rand.nextGaussian() * force;
			entityItem.motionY = (float)rand.nextGaussian() * force + 0.2F;
			entityItem.motionZ = (float)rand.nextGaussian() * force;
			world.spawnEntityInWorld(entityItem);
		}

		if(!habitat.isUpgrade()){
			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
				TileEntity te = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				if (te != null && te instanceof TileEntityFlickerHabitat){
					TileEntityFlickerHabitat upgHab = (TileEntityFlickerHabitat)te;

					if(upgHab.isUpgrade()){
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
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		//intentionally do nothing
	}
	
	@Override
	public IIcon getIcon(int par1, int par2) {
		return Blocks.iron_bars.getIcon(par1, par2);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int meta) {
		return true;
	}
}
