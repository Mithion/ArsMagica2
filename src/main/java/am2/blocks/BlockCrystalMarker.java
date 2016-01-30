package am2.blocks;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.blocks.tileentities.TileEntityCrystalMarker;
import am2.blocks.tileentities.TileEntityCrystalMarkerSpellExport;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockCrystalMarker extends BlockContainer{

	public static final int META_IN = 0;
	public static final int META_OUT = 1;
	public static final int META_LIKE_EXPORT = 2;
	public static final int META_SET_EXPORT = 3;
	public static final int META_REGULATE_EXPORT = 4;
	public static final int META_REGULATE_MULTI = 5;
	public static final int META_SET_IMPORT = 6;
	public static final int META_FINAL_DEST = 7;
	public static final int META_SPELL_EXPORT = 8;


	private int facingHolder = 0;
	private int xCoord = 0;
	private int yCoord = 0;
	private int zCoord = 0;

	public static final String[] crystalMarkerTypes = {
			"cm_import",
			"cm_export",
			"cm_likeExport",
			"cm_setExport",
			"cm_regulateExport",
			"cm_regulateMulti",
			"cm_setImport",
			"cm_final",
			"cm_spellExport"
	};

	protected BlockCrystalMarker(){
		super(Material.glass);
	}

	private int getCrystalTier(int meta){
		switch (meta){
		case META_IN:
		case META_OUT:
		case META_FINAL_DEST:
			return 0;
		case META_LIKE_EXPORT:
		case META_SET_EXPORT:
		case META_SET_IMPORT:
			return 1;
		case META_REGULATE_EXPORT:
		case META_REGULATE_MULTI:
			return 2;
		case META_SPELL_EXPORT:
			return 3;
		}
		return 0;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
		return 3 + getCrystalTier(meta);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this, 1, META_IN));
		par3List.add(new ItemStack(this, 1, META_OUT));
		par3List.add(new ItemStack(this, 1, META_LIKE_EXPORT));
		par3List.add(new ItemStack(this, 1, META_SET_EXPORT));
		par3List.add(new ItemStack(this, 1, META_REGULATE_EXPORT));
		par3List.add(new ItemStack(this, 1, META_REGULATE_MULTI));
		par3List.add(new ItemStack(this, 1, META_SET_IMPORT));
		par3List.add(new ItemStack(this, 1, META_FINAL_DEST));
		par3List.add(new ItemStack(this, 1, META_SPELL_EXPORT));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		int meta = world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		if (meta == META_SPELL_EXPORT)
			return new TileEntityCrystalMarkerSpellExport(meta);
		return new TileEntityCrystalMarker(meta);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
									EntityPlayer player, int facing, float impx, float impy, float impz){
		int operandType = world.getBlockMetadata(x, y, z);

		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.crystalWrench){
			player.swingItem();

			if (world.isRemote){
				return true;
			}

			return false;
		}else if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.spellStaffMagitech){
			//if we're here, we are changing the crystal's priority level
			//swing the item, first off.
			player.swingItem();
			//do nothing on client worlds other than this.
			if (world.isRemote){
				return true;
			}

			//input node?  Nothing more to do here than notify the player.
			if (operandType == META_IN){
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.noPriIn")));
				return false;
			}

			//resolve the crystal marker's Tile Entity
			TileEntityCrystalMarker crystalMarkerTE = GetTileEntity(world, x, y, z);
			TileEntityFlickerHabitat elementalAttunerTE = null;

			if (crystalMarkerTE != null){
				//store the old priority for use later
				int oldPriority = crystalMarkerTE.getPriority();
				//cycle the priority (increment with limits)
				crystalMarkerTE.cyclePriority();
				//resolve the attached attuner for this crystal
				AMVector3 elementalAttunerVector = crystalMarkerTE.getElementalAttuner();

				if (elementalAttunerVector != null){
					//if we are here, there is an attached attuner to this marker.  Resolve the location into the tile entity.
					elementalAttunerTE = GetElementalAttunerTileEntity(world,
							(int)elementalAttunerVector.x,
							(int)elementalAttunerVector.y,
							(int)elementalAttunerVector.z);
					if (elementalAttunerTE != null){
						//Notify the attuner that this crystal now has a new priority
						elementalAttunerTE.switchMarkerPriority(new AMVector3(x, y, z), oldPriority, crystalMarkerTE.getPriority());
					}
				}

				//finally, notify the player of the new priority
				player.addChatMessage(
						new ChatComponentText(String.format(
								StatCollector.translateToLocal("am2.tooltip.priSet"),
								String.format("%d", crystalMarkerTE.getPriority()) //need to put this as a string, because for some reason %d doesn't work when used in a localized string, but %s does
						)));
			}

			return false;
		}else if (operandType == this.META_SET_EXPORT || operandType == this.META_REGULATE_EXPORT || operandType == this.META_REGULATE_MULTI || operandType == this.META_SET_IMPORT){
			FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_CRYSTAL_MARKER, world, x, y, z);
			return true;
		}

		return super.onBlockActivated(world, x, y, z, player, facing, impx,
				impy, impz);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
							 float hitX, float hitY, float hitZ, int meta){
		facingHolder = side;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;

		return super
				.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
								EntityLivingBase player, ItemStack itemStack){
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null && te instanceof TileEntityCrystalMarker){
			((TileEntityCrystalMarker)te).setFacing(facingHolder);

			Block attachedTo = null;
			double minx = 0;
			double miny = 0;
			double minz = 0;
			double maxx = 1;
			double maxy = 1;
			double maxz = 1;
			switch (facingHolder){
			case 0: //Bottom, Inventory is above
				attachedTo = world.getBlock(x, y + 1, z);
				break;
			case 1: //Top, Inventory is below
				attachedTo = world.getBlock(x, y - 1, z);
				break;
			case 2: //North, Inventory is to the south
				attachedTo = world.getBlock(x, y, z + 1);
				break;
			case 3: //South, Inventory is to the north
				attachedTo = world.getBlock(x, y, z - 1);
				break;
			case 4: //West, Inventory is to the east
				attachedTo = world.getBlock(x + 1, y, z);
				break;
			case 5: //East, Inventory is to the west
				attachedTo = world.getBlock(x - 1, y, z);
				break;
			}

			if (attachedTo != null){
				minx = attachedTo.getBlockBoundsMinX();
				miny = attachedTo.getBlockBoundsMinY();
				minz = attachedTo.getBlockBoundsMinZ();
				maxx = attachedTo.getBlockBoundsMaxX();
				maxy = attachedTo.getBlockBoundsMaxY();
				maxz = attachedTo.getBlockBoundsMaxZ();
				((TileEntityCrystalMarker)te).SetConnectedBoundingBox(minx, miny, minz, maxx, maxy, maxz);
			}
		}
		super.onBlockPlacedBy(world, x, y, z, player, itemStack);
	}

	private TileEntityCrystalMarker GetTileEntity(World world, int x, int y,
												  int z){
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null && te instanceof TileEntityCrystalMarker){
			return (TileEntityCrystalMarker)te;
		}else{
			return null;
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta){
		TileEntityCrystalMarker crystalMarkerTE = GetTileEntity(world, x, y, z);
		TileEntityFlickerHabitat elementalAttunerTE = null;

		if (crystalMarkerTE != null){
			AMVector3 elementalAttunerVector = crystalMarkerTE.getElementalAttuner();

			if (elementalAttunerVector != null){
				elementalAttunerTE = GetElementalAttunerTileEntity(world,
						(int)elementalAttunerVector.x,
						(int)elementalAttunerVector.y,
						(int)elementalAttunerVector.z);
				if (elementalAttunerTE != null){
					int operandType = world.getBlockMetadata(x, y, z);

					if (operandType == META_IN){
						elementalAttunerTE.removeInMarkerLocation(x, y, z);
					}else if (isOutputMarker(operandType)){
						elementalAttunerTE.removeOutMarkerLocation(x, y, z);
					}
				}
			}
		}

		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	private TileEntityFlickerHabitat GetElementalAttunerTileEntity(
			World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null && te instanceof TileEntityFlickerHabitat){
			return (TileEntityFlickerHabitat)te;
		}else{
			return null;
		}
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess,
										   int x, int y, int z){
		TileEntity te = par1iBlockAccess.getTileEntity(x, y, z);
		TileEntityCrystalMarker cm = (TileEntityCrystalMarker)te;
		int facing = cm.getFacing();


		switch (facing){
		case 0: //Bottom, Inventory is above
			this.setBlockBounds(0.35f, (float)((1 + cm.GetConnectedBoundingBox().minY)) - 0.1f, 0.32f, 0.65f, (float)((1 + cm.GetConnectedBoundingBox().minY)), 0.68f);
			break;
		case 1: //Top, Inventory is below
			this.setBlockBounds(0.35f, (float)(-1 * (1 - cm.GetConnectedBoundingBox().maxY)), 0.3f, 0.65f, (float)(-1 * (1 - cm.GetConnectedBoundingBox().maxY)) + 0.1f, 0.7f);
			break;
		case 2: //North, Inventory is to the south
			this.setBlockBounds(0.35f, 0.32f, (float)(1 + (1 - cm.GetConnectedBoundingBox().maxZ)) - 0.1f, 0.65f, 0.68f, (float)(1 + (1 - cm.GetConnectedBoundingBox().maxZ)));
			break;
		case 3: //South, Inventory is to the north
			this.setBlockBounds(0.35f, 0.32f, (float)(0 - cm.GetConnectedBoundingBox().minZ), 0.65f, 0.68f, (float)(0 - cm.GetConnectedBoundingBox().minZ) + 0.1f);
			break;
		case 4: //West, Inventory is to the east
			this.setBlockBounds((float)(1 + cm.GetConnectedBoundingBox().minX) - 0.1f, 0.32f, 0.35f, (float)(1 + cm.GetConnectedBoundingBox().minX), 0.68f, 0.65f);
			break;
		case 5: //East, Inventory is to the west
			this.setBlockBounds((float)(-1 * (1 - cm.GetConnectedBoundingBox().maxX)), 0.32f, 0.35f, (float)(-1 * (1 - cm.GetConnectedBoundingBox().maxX)) + 0.1f, 0.68f, 0.65f);
			break;
		}
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3,
										int par4, AxisAlignedBB par5AxisAlignedBB, List par6List,
										Entity par7Entity){
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block blockID){
		if (world.isRemote){
			return;
		}
		TileEntity te = world.getTileEntity(x, y, z);
		TileEntityCrystalMarker cm = null;
		boolean mustDrop = false;

		if (te != null && te instanceof TileEntityCrystalMarker){
			cm = (TileEntityCrystalMarker)te;
		}else{
			return;
		}

		switch (cm.getFacing()){
		case 0: //Bottom, Inventory is above
			mustDrop = world.isAirBlock(x, y + 1, z);
			break;
		case 1: //Top, Inventory is below
			mustDrop = world.isAirBlock(x, y - 1, z);
			break;
		case 2: //North, Inventory is to the south
			mustDrop = world.isAirBlock(x, y, z + 1);
			break;
		case 3: //South, Inventory is to the north
			mustDrop = world.isAirBlock(x, y, z - 1);
			break;
		case 4: //West, Inventory is to the east
			mustDrop = world.isAirBlock(x + 1, y, z);
			break;
		case 5: //East, Inventory is to the west
			mustDrop = world.isAirBlock(x - 1, y, z);
			break;
		}

		if (mustDrop){
			Block block = world.getBlock(x, y, z);
			ItemStack itemStack = new ItemStack(block, 1, world.getBlockMetadata(x, y, z));
			EntityItem entityItem = new EntityItem(world, x, y, z, itemStack);
			world.spawnEntityInWorld(entityItem);
			world.setBlockToAir(x, y, z);
		}
	}

	public static boolean isOutputMarker(int operandType){
		return
				operandType == META_OUT ||
						operandType == META_LIKE_EXPORT ||
						operandType == META_SET_EXPORT ||
						operandType == META_REGULATE_EXPORT ||
						operandType == META_REGULATE_MULTI ||
						operandType == META_FINAL_DEST ||
						operandType == META_SPELL_EXPORT;
	}

	public static boolean isInputMarker(int operandType){
		return
				operandType == META_IN ||
						operandType == META_REGULATE_MULTI ||
						operandType == META_SET_IMPORT ||
						operandType == META_SPELL_EXPORT;
	}

	@Override
	public int damageDropped(int metadata){
		return metadata;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_){
		//intentionally left blank
	}
}

