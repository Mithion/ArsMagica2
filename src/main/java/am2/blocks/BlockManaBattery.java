package am2.blocks;

import am2.AMCore;
import am2.api.power.PowerTypes;
import am2.blocks.tileentities.TileEntityManaBattery;
import am2.entities.EntityDummyCaster;
import am2.power.PowerNodeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockManaBattery extends PoweredBlock{
	public BlockManaBattery(){
		super(Material.iron);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ))
            return true;

        if (world.isRemote){
            TileEntityManaBattery te = getTileEntity(world, pos);
            if (te != null){
                if (AMCore.config.colourblindMode()){
                    player.addChatMessage(new ChatComponentText(String.format("Charge Level: %.2f %% [%s]", PowerNodeRegistry.For(world).getPower(te, te.getPowerType()) / te.getCapacity() * 100, getColorNameFromPowerType(te.getPowerType()))));
                }else{
                    player.addChatMessage(new ChatComponentText(String.format("Charge Level: %s%.2f \u00A7f%%", te.getPowerType().chatColor(), PowerNodeRegistry.For(world).getPower(te, te.getPowerType()) / te.getCapacity() * 100)));
                }
            }
        }

        return true;
    }

    @Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityManaBattery();
	}

	private TileEntityManaBattery getTileEntity(World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityManaBattery){
			return (TileEntityManaBattery)te;
		}
		return null;
	}

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack != null){
            TileEntityManaBattery te = getTileEntity(world, pos);
            if (stack.getTagCompound() != null){
                if (stack.getTagCompound().hasKey("mana_battery_charge") && stack.getTagCompound().hasKey("mana_battery_powertype"))
                    PowerNodeRegistry.For(world).setPower(te, PowerTypes.getByID(stack.getTagCompound().getInteger("mana_battery_powertype")), stack.getTagCompound().getFloat("mana_battery_charge"));
                else
                    te.setPowerType(PowerTypes.NONE, false);
            }

        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        destroy(world, pos);
        super.onBlockExploded(world, pos, explosion);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        destroy(world, pos);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        destroy(world, pos);
        super.onBlockHarvested(world, pos, state, player);
    }

    private void destroy(World world, BlockPos pos){
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
		TileEntityManaBattery te = getTileEntity(world, pos);
		if (te != null && !world.isRemote){
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			int dmg = (int)((PowerNodeRegistry.For(world).getPower(te, te.getPowerType()) / te.getCapacity()) * 100);
			if (dmg == 0) dmg = 1;
			ItemStack stack = new ItemStack(this);
			stack.damageItem(stack.getMaxDamage() - dmg, new EntityDummyCaster(world));
			stack.getTagCompound().setFloat("mana_battery_charge", PowerNodeRegistry.For(world).getPower(te, te.getPowerType()));
			stack.getTagCompound().setInteger("mana_battery_powertype", te.getPowerType().ID());

			if (!stack.getTagCompound().hasKey("Lore"))
				stack.getTagCompound().setTag("Lore", new NBTTagList());

			NBTTagList tagList = new NBTTagList();
			PowerTypes powerType = te.getPowerType();
			float amt = PowerNodeRegistry.For(world).getPower(te, powerType);
			tagList.appendTag(new NBTTagString(String.format("Contains %.2f %s%s etherium", amt, powerType.chatColor(), powerType.name())));
			stack.getTagCompound().setTag("Lore", tagList);

			EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, stack);
			float f3 = 0.05F;
			entityitem.motionX = (float)world.rand.nextGaussian() * f3;
			entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
			world.spawnEntityInWorld(entityitem);
		}
	}

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntityManaBattery battery = getTileEntity(worldIn, pos);
        if (battery == null) return 0;
        float pct = PowerNodeRegistry.For(worldIn).getHighestPower(battery) / battery.getCapacity();
        return (int) Math.floor(15.0F * pct);
    }

    @Override
	public boolean hasComparatorInputOverride(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		ItemStack stack = new ItemStack(this);
//		stack.stackTagCompound = new NBTTagCompound();
		stack.getTagCompound().setFloat("mana_battery_charge", new TileEntityManaBattery().getCapacity());

		par3List.add(stack);
	}

    @Override
    public int colorMultiplier(IBlockAccess blockAccess, BlockPos pos, int renderPass) {
        TileEntity te = blockAccess.getTileEntity(pos);
        if (te instanceof TileEntityManaBattery){
            TileEntityManaBattery battery = (TileEntityManaBattery)te;
            if (battery.getPowerType() == PowerTypes.DARK)
                return 0x850e0e;
            else if (battery.getPowerType() == PowerTypes.LIGHT)
                return 0x61cfc3;
            else if (battery.getPowerType() == PowerTypes.NEUTRAL)
                return 0x2683d2;
            else
                return 0xFFFFFF;
        }
        return 0xFFFFFF;
    }

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<ItemStack>();
    }
}
