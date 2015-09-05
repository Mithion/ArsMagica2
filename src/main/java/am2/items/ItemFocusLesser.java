package am2.items;

import am2.texture.ResourceManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class ItemFocusLesser extends ItemFocus implements ISpellFocus{

	public ItemFocusLesser(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				" G ", "GRG", " G ",
				'G', Items.gold_nugget,
				'R', Blocks.glass
		};
	}

	@Override
	public String getInGameName(){
		return "Lesser Focus";
	}

	@Override
	public int getFocusLevel(){
		return 0;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		this.itemIcon = ResourceManager.RegisterTexture("focus_lesser", par1IconRegister);
	}
}
