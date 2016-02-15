package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityMagiciansWorkbench;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockMagiciansWorkbench extends AMSpecialRenderBlockContainer{

	protected BlockMagiciansWorkbench(){
		super(Material.wood);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityMagiciansWorkbench();
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int p = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        byte byte0 = 3;

        if (p == 0){
            byte0 = 2;
        }
        if (p == 1){
            byte0 = 1;
        }
        if (p == 2){
            byte0 = 4;
        }
        if (p == 3){
            byte0 = 3;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2);
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean isFullCube(){
		return false;
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityMagiciansWorkbench){

            if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)te))
                return true;

            if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)te, player, KeystoneAccessType.USE)){

                super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

                if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.workbenchUpgrade){
                    ((TileEntityMagiciansWorkbench)te).setUpgradeStatus(TileEntityMagiciansWorkbench.UPG_CRAFT, true);

                    if (!world.isRemote){
                        ItemStack stack = player.getCurrentEquippedItem();
                        stack.stackSize--;

                        if (stack.stackSize <= 0)
                            stack = null;

                        player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
                    }
                    return true;
                }else{
                    if (!world.isRemote){
                        super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
                        FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_MAGICIANS_WORKBENCH, world, pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntityMagiciansWorkbench receptacle = (TileEntityMagiciansWorkbench)world.getTileEntity(pos);
        if (receptacle == null)
            return;
        if (KeystoneUtilities.instance.canPlayerAccess(receptacle, player, KeystoneAccessType.BREAK)){
            for (int i = receptacle.getSizeInventory() - 3; i < receptacle.getSizeInventory(); i++){
                receptacle.decrStackSize(i, 9001);
                // arbitrary number, just in case rune stack sizes increase in the future
                // yes, it's hard-coded; yes, it's also less computationally intensive than a stack size lookup
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote){
            super.breakBlock(world, pos, state);
            return;
        }
        TileEntityMagiciansWorkbench workbench = (TileEntityMagiciansWorkbench)world.getTileEntity(pos);
        if (workbench == null || KeystoneUtilities.instance.getKeyFromRunes(workbench.getRunesInKey()) != 0) return;

        for (int l = 0; l < workbench.getSizeInventory() - 3; l++){
            ItemStack itemstack = workbench.getStackInSlot(l);
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

        if(workbench.getUpgradeStatus(TileEntityMagiciansWorkbench.UPG_CRAFT)){
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            ItemStack newItem = new ItemStack(ItemsCommonProxy.workbenchUpgrade, 1);
            EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, newItem);
            float f3 = 0.05F;
            entityitem.motionX = (float)world.rand.nextGaussian() * f3;
            entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
            world.spawnEntityInWorld(entityitem);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }
}
