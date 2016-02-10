package am2.lore;

import am2.api.SkillTreeEntry;
import am2.guis.GuiArcaneCompendium;
import am2.items.ItemsCommonProxy;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CompendiumEntryTalent extends CompendiumEntry{

	private int meta = -1;

	public CompendiumEntryTalent(){
		super(CompendiumEntryTypes.instance.TALENT);
	}

	@Override
	protected void parseEx(Node node){
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i){
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("meta")){
				meta = Integer.parseInt(child.getTextContent());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
		return new GuiArcaneCompendium(searchID, ItemsCommonProxy.spell_component, 0);
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){
		SkillTreeEntry entry = SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill(searchID));
		if (entry != null){
			return new ItemStack(ItemsCommonProxy.spell_component, 1, SkillManager.instance.getShiftedPartID(entry.registeredItem));
		}
		return null;
	}

}
