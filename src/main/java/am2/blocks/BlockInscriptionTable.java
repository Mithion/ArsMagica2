package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockInscriptionTable extends AMSpecialRenderBlockContainer{

	public BlockInscriptionTable(){
		super(Material.wood);

		//setTextureFile(AMCore.proxy.getOverrideBlockTexturePath());
		setHardness(2.0f);
		setResistance(2.0f);
		setLightLevel(0.8f);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.3f, 1.0f);
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        int p = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;

        byte byte0 = 3;

        int nX = pos.getX();
        int y = pos.getY();
        int nZ = pos.getZ();

        if (p == 0){
            byte0 = 3;
            nZ++;
        }
        if (p == 1){
            byte0 = 2;
            nX--;
        }
        if (p == 2){
            byte0 = 1;
            nZ--;
        }
        if (p == 3){
            byte0 = 4;
            nX++;
        }

        if (!world.isAirBlock(pos)){
            world.setBlockToAir(pos);
            if (entity instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer)entity;
                player.inventory.addItemStackToInventory(new ItemStack(BlocksCommonProxy.inscriptionTable));
            }
            return;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0 | 0x8), 2);

        world.setBlockState(new BlockPos(nX, pos.getY(), nZ), this.getDefaultState());
        world.setBlockState(new BlockPos(nX, y, nZ), world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2);

        super.onBlockPlacedBy(world, pos, state, entity, stack);
    }

    @Override
    public int getLightValue() {
        return 12;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) { // TODO rewrite the logic for this
        super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);

        if (par1World.isRemote){
            return true;
        }

        TileEntityInscriptionTable te = (TileEntityInscriptionTable)par1World.getTileEntity(par2, par3, par4);
        TileEntityInscriptionTable tealt = te;

        int meta = par1World.getBlockMetadata(par2, par3, par4);
        boolean isLeft = (meta & 0x8) == 0x0;
        if (te != null){
            int checkMeta = meta & ~0x8;
            if (!isLeft){
                switch (checkMeta){
                    case 1:
                        par4--;
                        break;
                    case 2:
                        par2--;
                        break;
                    case 3:
                        par4++;
                        break;
                    case 4:
                        par2++;
                        break;
                }

                te = (TileEntityInscriptionTable)par1World.getTileEntity(par2, par3, par4);
            }else{
                int tx = par2;
                int ty = par3;
                int tz = par4;
                switch (checkMeta){
                    case 1:
                        tz++;
                        break;
                    case 2:
                        tx++;
                        break;
                    case 3:
                        tz--;
                        break;
                    case 4:
                        tx--;
                        break;
                }

                tealt = (TileEntityInscriptionTable)par1World.getTileEntity(tx, ty, tz);
            }
        }

        if (te == null)
            return true;

        if (te.isInUse(par5EntityPlayer)){
            par5EntityPlayer.addChatMessage(new ChatComponentText("Someone else is using this."));
            return true;
        }

        ItemStack curItem = par5EntityPlayer.getCurrentEquippedItem();
        if (curItem != null && curItem.getItem() == ItemsCommonProxy.inscriptionUpgrade){
            if (te.getUpgradeState() == curItem.getItemDamage()){
                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                te.incrementUpgradeState();
                tealt.incrementUpgradeState();
                return true;
            }
        }

        FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_INSCRIPTION_TABLE, par1World, par2, par3, par4);

        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityInscriptionTable();
	}

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityInscriptionTable insc = (TileEntityInscriptionTable)world.getTileEntity(pos);
        int metadata = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));

        if (insc == null) return;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getY();
        boolean main = (metadata & 0x8) == 0x8;

        metadata &= ~0x8;

        switch (metadata){
            case 1:
                z += (main) ? -1 : 1;
                break;
            case 2:
                x += (main) ? -1 : 1;
                break;
            case 3:
                z += (main) ? 1 : -1;
                break;
            case 4:
                x += (main) ? 1 : -1;
                break;
        }

        if (world.getBlockState(pos).getBlock() == this)
            world.setBlockToAir(pos);

        if (!world.isRemote && main){
            for (int l = 0; l < insc.getSizeInventory(); l++){
                ItemStack itemstack = insc.getStackInSlot(l);
                if (itemstack == null){
                    continue;
                }
                spawnItemOnBreak(world, x, y, z, world.getBlockState(pos).getBlock(), metadata, itemstack);
            }

            int stat = insc.getUpgradeState();
            for (int m = 0; m < stat; ++m)
                spawnItemOnBreak(world, x, y, z, world.getBlockState(pos).getBlock(), metadata, new ItemStack(ItemsCommonProxy.inscriptionUpgrade, 1, m));
        }

        super.breakBlock(world, pos, state);
    }

	private void spawnItemOnBreak(World world, int i, int j, int k, Block par5, int metadata, ItemStack itemstack){
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
			EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newItem);
			float f3 = 0.05F;
			entityitem.motionX = (float)world.rand.nextGaussian() * f3;
			entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
			world.spawnEntityInWorld(entityitem);
		}while (true);
	}
}
