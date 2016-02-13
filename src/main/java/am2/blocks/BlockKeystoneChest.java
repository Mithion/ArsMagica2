package am2.blocks;

import am2.AMCore;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityKeystoneChest;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
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


public class BlockKeystoneChest extends AMSpecialRenderBlockContainer{

	protected BlockKeystoneChest(){
		super(Material.wood);
		this.setCreativeTab(BlocksCommonProxy.blockTab);
		this.setResistance(900000);
		this.setHardness(3.0f);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityKeystoneChest();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

        if (!world.isRemote){

            TileEntity myTE = world.getTileEntity(pos);

            if (!(myTE instanceof TileEntityKeystoneChest)) return false;

            TileEntityKeystoneChest te = (TileEntityKeystoneChest)myTE;

            if (KeystoneUtilities.HandleKeystoneRecovery(player, te)){
                return true;
            }

            if (!KeystoneUtilities.instance.canPlayerAccess(te, player, KeystoneAccessType.USE)){
                return true;
            }

			/*PacketDispatcher.sendPacketToPlayer(AMCore.instance.proxy.packetSender.createArsMagicaClientPacket(AMPacketIDs.KEYSTONE_CHEST_GUI_OPEN,
					new AMDataWriter().add(par2).add(par3).add(par4).generate()), (Player)entityplayer);*/

            FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE_CHEST, world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }else{
            return true;
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntityKeystoneChest receptacle = (TileEntityKeystoneChest)world.getTileEntity(pos);
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
        TileEntityKeystoneChest receptacle = (TileEntityKeystoneChest)world.getTileEntity(pos);

        if (receptacle == null) return;

        if (KeystoneUtilities.instance.getKeyFromRunes(receptacle.getRunesInKey()) == 0){

            for (int l = 0; l < receptacle.getSizeInventory() - 3; l++){
                ItemStack itemstack = receptacle.getStackInSlot(l);
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

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int p = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        byte newMeta = 3;

        if (p == 0){
            newMeta = 1;
        }
        if (p == 1){
            newMeta = 4;
        }
        if (p == 2){
            newMeta = 3;
        }
        if (p == 3){
            newMeta = 2;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(newMeta), 2);
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TileEntityKeystoneChest chest = (TileEntityKeystoneChest)world.getTileEntity(pos);
        if (!KeystoneUtilities.instance.canPlayerAccess(chest, player, KeystoneAccessType.BREAK)) return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        int var5 = 0;

        if (world.getBlockState(pos.west()).getBlock() == this){
            ++var5;
        }

        if (world.getBlockState(pos.east()).getBlock() == this){
            ++var5;
        }

        if (world.getBlockState(pos.north()).getBlock() == this){
            ++var5;
        }

        if (world.getBlockState(pos.south()).getBlock() == this){
            ++var5;
        }

        return !(var5 > 0);
    }
}
