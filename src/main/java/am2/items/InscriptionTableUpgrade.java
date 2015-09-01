package am2.items;

import java.util.List;

import am2.texture.ResourceManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InscriptionTableUpgrade extends ArsMagicaItem{
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textures;
	
	public InscriptionTableUpgrade(){
		super();
		setMaxDamage(0);
		setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		textures = new String[]{ "inscrip_upgrade_1", "inscrip_upgrade_2", "inscrip_upgrade_3"};
		icons = new IIcon[textures.length];
		for (int i = 0; i < textures.length; ++i){
			icons[i] = ResourceManager.RegisterTexture(textures[i], par1IconRegister);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return icons[par1];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int i = 0; i < textures.length; ++i){
			par3List.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		int meta = p_77653_1_.getItemDamage();
		switch(meta){
		case 2:
			return StatCollector.translateToLocal("item.arsmagica2:inscup_3.name");
		case 1:
			return StatCollector.translateToLocal("item.arsmagica2:inscup_2.name");			
		case 0:
		default:
			return StatCollector.translateToLocal("item.arsmagica2:inscup_1.name");
		}
	}
}
