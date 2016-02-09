package am2.blocks;

import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockAMOre extends BlockOre{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textures;

	public static final int META_VINTEUM_ORE = 0;
	public static final int META_CHIMERITE_ORE = 1;
	public static final int META_BLUE_TOPAZ_ORE = 2;
	public static final int META_MOONSTONE_ORE = 3;
	public static final int META_SUNSTONE_ORE = 4;
	public static final int META_MOONSTONE_BLOCK = 5;
	public static final int META_VINTEUM_BLOCK = 6;
	public static final int META_BLUE_TOPAZ_BLOCK = 7;
	public static final int META_SUNSTONE_BLOCK = 8;
	public static final int META_CHIMERITE_BLOCK = 9;

	public static final int NUM_TYPES = 10;

	public BlockAMOre(){
		super();
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister){
		textures = new String[]{"orevinteum", "orechimerite", "orebluetopaz", "oremoonstone", "oresunstone", "oreblockmoonstone", "oreblockvinteum", "oreblockbluetopaz", "oreblocksunstone", "oreblockchimerite"};
		icons = new IIcon[NUM_TYPES];

		int count = 0;
		for (String s : textures){
			icons[count++] = ResourceManager.RegisterTexture(s, par1IconRegister);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2){
		return icons[par2];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5){
		int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < NUM_TYPES; ++i){
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta){
		switch (meta){
		case META_CHIMERITE_ORE:
			return ItemsCommonProxy.itemOre.META_CHIMERITE;
		case META_MOONSTONE_ORE:
			return ItemsCommonProxy.itemOre.META_MOONSTONE;
		case META_SUNSTONE_ORE:
			return ItemsCommonProxy.itemOre.META_SUNSTONE;
		case META_BLUE_TOPAZ_ORE:
			return ItemsCommonProxy.itemOre.META_BLUETOPAZ;
		}
		return meta;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		switch (meta){
		case META_VINTEUM_ORE:
			drops.add(new ItemStack(this, 1, this.META_VINTEUM_ORE));
			break;
		case META_CHIMERITE_ORE:
			drops.add(new ItemStack(ItemsCommonProxy.itemOre, world.rand.nextInt(4) + 1 + fortune, ItemsCommonProxy.itemOre.META_CHIMERITE));
			break;
		case META_MOONSTONE_ORE:
			drops.add(new ItemStack(ItemsCommonProxy.itemOre, world.rand.nextInt(2) + 1 + fortune, ItemsCommonProxy.itemOre.META_MOONSTONE));
			break;
		case META_SUNSTONE_ORE:
			drops.add(new ItemStack(ItemsCommonProxy.itemOre, world.rand.nextInt(3) + 2 + fortune, ItemsCommonProxy.itemOre.META_SUNSTONE));
			break;
		case META_BLUE_TOPAZ_ORE:
			drops.add(new ItemStack(ItemsCommonProxy.itemOre, world.rand.nextInt(4) + 1 + fortune, ItemsCommonProxy.itemOre.META_BLUETOPAZ));
			break;
		default:
			drops.add(new ItemStack(this, 1, meta));
		}

		return drops;
	}

	@Override
	public int getDamageValue(World par1World, int par2, int par3, int par4){
		return par1World.getBlockMetadata(par2, par3, par4);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random){
		switch (meta){
		case META_CHIMERITE_ORE:
		case META_BLUE_TOPAZ_ORE:
			return MathHelper.clamp_int(random.nextInt(4) + random.nextInt(fortune + 1), 1, 8);
		case META_MOONSTONE_ORE:
		case META_SUNSTONE_ORE:
			return MathHelper.clamp_int(random.nextInt(2) + random.nextInt(fortune + 1), 1, 6);
		}
		return 1;
	}
}
