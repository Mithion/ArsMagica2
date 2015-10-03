package am2.items;

import am2.api.spell.enums.Affinity;
import am2.playerextensions.AffinityData;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemAffinityBook extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textures;

	public static final int META_GENERAL = 0;
	public static final int META_ARCANE = 1;
	public static final int META_WATER = 2;
	public static final int META_FIRE = 3;
	public static final int META_EARTH = 4;
	public static final int META_AIR = 5;
	public static final int META_LIGHTNING = 6;
	public static final int META_ICE = 7;
	public static final int META_NATURE = 8;
	public static final int META_LIFE = 9;
	public static final int META_ENDER = 10;

	public ItemAffinityBook(){
		super();
		setMaxDurability(0);
		setMaxStackSize(16);
		this.setHasSubtypes(true);
	}

	public int getNumTomes(){
		return 11;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		Affinity affinity = Affinity.getByID(par1ItemStack.getMetadata());
		if (affinity == null) return StatCollector.translateToLocal("item.arsmagica2:affinityTome.name");

		return StatCollector.translateToLocal("item.arsmagica2:affinityTome.name") + ": " + affinity.toString();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		textures = new String[]{"affinity_tome_general", "affinity_tome_arcane", "affinity_tome_water", "affinity_tome_fire", "affinity_tome_earth", "affinity_tome_air", "affinity_tome_lightning", "affinity_tome_ice", "affinity_tome_nature", "affinity_tome_life", "affinity_tome_ender"};
		icons = new IIcon[textures.length];
		for (int i = 0; i < textures.length; ++i){
			icons[i] = ResourceManager.RegisterTexture(textures[i], par1IconRegister);
		}
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1){
		return icons[par1];
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return true;
	}

	@Override
	public boolean getHasSubtypes(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < textures.length; ++i){
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){

		if (par1ItemStack.getMetadata() == META_GENERAL){
			AffinityData data = AffinityData.For(par3EntityPlayer);
			data.setLocked(false);
			for (Affinity aff : Affinity.values()){
				data.setAffinityAndDepth(aff, data.getAffinityDepth(aff) - 20);
			}
		}else{
			AffinityData.For(par3EntityPlayer).incrementAffinity(Affinity.getByID(par1ItemStack.getMetadata()), 20);
		}

		if (!par2World.isRemote)
			par1ItemStack.stackSize--;

		/*if (par2World.isRemote){
			par3EntityPlayer.addStat(AMCore.achievements.Specialized, 1);
		}*/

		return par1ItemStack;
	}
}
