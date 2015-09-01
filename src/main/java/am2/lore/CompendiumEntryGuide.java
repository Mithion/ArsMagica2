package am2.lore;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Node;

import am2.guis.GuiArcaneCompendium;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompendiumEntryGuide extends CompendiumEntry{

	public CompendiumEntryGuide(){
		super(CompendiumEntryTypes.instance.GUIDE);
	}

	@Override
	protected void parseEx(Node node) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta) {
		return new GuiArcaneCompendium(searchID);
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta) {
		return null;
	}

}
