package am2.buffs;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class ArsMagicaPotion extends Potion{

	private ResourceLocation potionTexture;

	protected ArsMagicaPotion(int par1, boolean par2, int par3){
		super(par1, par2, par3);
	}

	public void _setIconIndex(int row, int col){
		this.setIconIndex(row, col);
	}

	public void setTextureSheet(String texturesheet){
		potionTexture = new ResourceLocation("arsmagica2", texturesheet);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex(){
		if (potionTexture != null)
			Minecraft.getMinecraft().renderEngine.bindTexture(potionTexture);
		return super.getStatusIconIndex();
	}
}
