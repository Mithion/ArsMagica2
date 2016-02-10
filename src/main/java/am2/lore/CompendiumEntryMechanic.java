package am2.lore;

import am2.guis.GuiArcaneCompendium;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;

public class CompendiumEntryMechanic extends CompendiumEntry{

	public CompendiumEntryMechanic(){
		super(CompendiumEntryTypes.instance.MECHANIC);
	}

	@Override
	protected void parseEx(Node node){
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
		return new GuiArcaneCompendium(searchID);
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){
		return null;
	}

}
