package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.blocks.tileentities.TileEntitySpellSealedDoor;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import java.util.Random;


public class BlockSpellSealedDoor extends BlockDoor implements ITileEntityProvider{
	protected BlockSpellSealedDoor(){
		super(Material.wood);
		this.setHardness(2.5f);
		this.setResistance(2.0f);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.spellSealedDoor)
			pos = pos.down();

		TileEntity te = world.getTileEntity(pos);

		player.swingItem();

		if (!world.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)te))
				return true;

			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)te, player, KeystoneAccessType.USE)){
				if (player.isSneaking()){
					FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_SPELL_SEALED_DOOR, world, pos.getX(), pos.getY(), pos.getZ());
				}else{
					return false;
				}
			}
		}

		return false;
	}


	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (world.isRemote){
			super.breakBlock(world, pos, state);
			return;
		}

		if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.spellSealedDoor)
			pos = pos.down();

		TileEntitySpellSealedDoor door = (TileEntitySpellSealedDoor)world.getTileEntity(pos);
		if (door == null) return;
		ItemStack itemstack = door.getStackInSlot(3);
		if (itemstack == null){
			return;
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

		world.setBlockToAir(pos.up());

		super.breakBlock(world, pos, state);
	}

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (world.isRemote)
            return false;

        if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.keystoneDoor)
            pos = pos.down();

        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);

        if (lockable == null)
            return false;

        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (world.isRemote)
            return;

        if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.keystoneDoor)
            pos = pos.down();

        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);

        if (lockable == null)
            return;

        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK))
            return;
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemsCommonProxy.itemKeystoneDoor;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return ItemsCommonProxy.itemKeystoneDoor.SPELL_SEALED_DOOR;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntitySpellSealedDoor();
	}

	public boolean applyComponentToDoor(IBlockAccess access, ISpellComponent component, BlockPos pos){
		if (access.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.spellSealedDoor)
			pos = pos.down();


		TileEntity te = access.getTileEntity(pos);
		if (te == null || te instanceof TileEntitySpellSealedDoor == false){
			return false;
		}

		((TileEntitySpellSealedDoor)te).addPartToCurrentKey(component);

		return true;
	}

	public void setDoorState(World world, BlockPos pos, EntityPlayer player,  IBlockState state){ // new method might now work, not sure yet
		int i1 = this.getMetaFromState(state);
		int j1 = i1 & 7;
		j1 ^= 4;

		if ((i1 & 8) == 0){
			world.setBlockState(pos, getStateFromMeta(j1), 2);
			world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		}else{
			world.setBlockState(pos.down(), getStateFromMeta(j1), 2);
			world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		}

		world.playAuxSFXAtEntity(player, 1003, pos, 0);
	}
}
