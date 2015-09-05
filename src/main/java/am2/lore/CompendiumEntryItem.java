package am2.lore;

import am2.AMCore;
import am2.guis.GuiArcaneCompendium;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CompendiumEntryItem extends CompendiumEntry{

	int lowerMetaRange, upperMetaRange;

	public CompendiumEntryItem(){
		super(CompendiumEntryTypes.instance.ITEM);
	}

	@Override
	protected void parseEx(Node node){
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i){
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("metarange")){
				String[] metarange = child.getTextContent().split(",");
				if (metarange.length == 2){
					lowerMetaRange = Integer.parseInt(metarange[0]);
					upperMetaRange = Integer.parseInt(metarange[0]);
				}
			}
		}
	}

	public boolean hasMetaItems(){
		return (lowerMetaRange - upperMetaRange) > 0;
	}

	public ItemStack[] getMetaItems(Item item){
		ItemStack[] items = new ItemStack[upperMetaRange - lowerMetaRange];
		for (int i = lowerMetaRange; i < upperMetaRange; ++i){
			items[i] = new ItemStack(item, 1, i);
		}
		return items;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
		String[] split = searchID.split(":");
		if (split.length == 2){
			Item item = GameRegistry.findItem(split[0], split[1]);
			if (item != null){
				if (meta == -1)
					return new GuiArcaneCompendium(item);
				else
					return new GuiArcaneCompendium(searchID + "@" + meta, item, meta);
			}
		}else{
			for (Item item : AMCore.instance.proxy.items.getArsMagicaItems()){
				if (item.getUnlocalizedName() == null) continue;
				String itemID = item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
				if (itemID.equals(searchID)){
					if (meta == -1)
						return new GuiArcaneCompendium(item);
					else
						return new GuiArcaneCompendium(searchID + "@" + meta, item, meta);
				}
			}
		}
		return new GuiArcaneCompendium(searchID);
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){

		String[] split = searchID.split(":");
		if (split.length == 2){
			return GameRegistry.findItemStack(split[0], split[1], 1);
		}else{
			for (Item item : AMCore.instance.proxy.items.getArsMagicaItems()){
				if (item.getUnlocalizedName() == null) continue;
				String itemID = item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
				if (itemID.equals(searchID)){
					if (meta == -1)
						return new ItemStack(item);
					else
						return new ItemStack(item, 1, meta);
				}
			}
		}

		return null;
	}
}
