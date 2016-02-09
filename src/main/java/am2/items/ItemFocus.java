package am2.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public abstract class ItemFocus extends ArsMagicaItem{

	protected ItemFocus(){
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public abstract void registerIcons(IIconRegister par1IconRegister);

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int dmg, int pass){
		return this.itemIcon;
	}

	public abstract Object[] getRecipeItems();

	public abstract String getInGameName();
}
