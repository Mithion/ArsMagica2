package am2.items;

import am2.api.power.PowerTypes;
import am2.particles.AMParticleIcons;
import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemEssence extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private String[] textures;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public static final int META_ARCANE = 0;
	public static final int META_EARTH = 1;
	public static final int META_AIR = 2;
	public static final int META_FIRE = 3;
	public static final int META_WATER = 4;
	public static final int META_NATURE = 5;
	public static final int META_ICE = 6;
	public static final int META_LIGHTNING = 7;
	public static final int META_LIFE = 8;
	public static final int META_ENDER = 9;
	public static final int META_PURE = 10;
	public static final int META_HIGH_CORE = 11;
	public static final int META_BASE_CORE = 12;
	public static final int META_MAX = 12;

	public ItemEssence(){
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();
		switch (meta){
		case 0:
			return StatCollector.translateToLocal("item.arsmagica2:arcaneEssence.name");
		case 1:
			return StatCollector.translateToLocal("item.arsmagica2:earthEssence.name");
		case 2:
			return StatCollector.translateToLocal("item.arsmagica2:airEssence.name");
		case 3:
			return StatCollector.translateToLocal("item.arsmagica2:fireEssence.name");
		case 4:
			return StatCollector.translateToLocal("item.arsmagica2:waterEssence.name");
		case 5:
			return StatCollector.translateToLocal("item.arsmagica2:plantEssence.name");
		case 6:
			return StatCollector.translateToLocal("item.arsmagica2:iceEssence.name");
		case 7:
			return StatCollector.translateToLocal("item.arsmagica2:lightningEssence.name");
		case 8:
			return StatCollector.translateToLocal("item.arsmagica2:lifeEssence.name");
		case 9:
			return StatCollector.translateToLocal("item.arsmagica2:enderEssence.name");
		case 10:
			return StatCollector.translateToLocal("item.arsmagica2:pureEssence.name");
		case 11:
			return StatCollector.translateToLocal("item.arsmagica2:highEssenceCore.name");
		case 12:
			return StatCollector.translateToLocal("item.arsmagica2:baseEssenceCore.name");
		}
		int mask = meta - META_MAX;
		String s = "";
		for (PowerTypes type : PowerTypes.all()){
			if ((mask & type.ID()) == type.ID()){
				if (s.equals(""))
					s = StatCollector.translateToLocal("item.arsmagica2:rawEssence.name") + " (" + type.name() + ")";
				else
					s += "\n" + StatCollector.translateToLocal("am2.gui.or") + StatCollector.translateToLocal("item.arsmagica2:rawEssence.name") + " (" + type.name() + ")";
			}
		}
		return s;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		textures = new String[]{"essence_arcane", "essence_earth", "essence_air", "essence_fire", "essence_water", "essence_plant", "essence_ice", "essence_lightning", "essence_life", "essence_ender", "pure_essence", "high_essence_core", "base_essence_core"};
		this.icons = new IIcon[this.textures.length];

		for (int i = 0; i < this.textures.length; ++i){
			this.icons[i] = ResourceManager.RegisterTexture(this.textures[i], iconRegister);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage){
		if (this.icons != null && damage <= META_MAX){
			return this.icons[damage];
		}else{
			return AMParticleIcons.instance.getIconByName("mystic");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2){
		if (par1ItemStack.getItemDamage() > META_MAX){
			int flags = par1ItemStack.getItemDamage() - META_MAX;
			int color = 0;

			if ((flags & PowerTypes.DARK.ID()) == PowerTypes.DARK.ID()){
				color |= 0x770000;
			}
			if ((flags & PowerTypes.NEUTRAL.ID()) == (flags & PowerTypes.NEUTRAL.ID())){
				color |= 0x00AA00;
			}
			if ((flags & PowerTypes.LIGHT.ID()) == (flags & PowerTypes.LIGHT.ID())){
				color |= 0x0077FF;
			}

			return color;
		}
		return super.getColorFromItemStack(par1ItemStack, par2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < this.textures.length; ++i){
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	public int getNumEssences(){
		return 13;
	}

}
