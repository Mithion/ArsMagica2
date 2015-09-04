package am2.lore;

import am2.api.SkillTreeEntry;
import am2.api.spell.enums.SpellModifiers;
import am2.guis.GuiArcaneCompendium;
import am2.items.ItemsCommonProxy;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class CompendiumEntrySpellModifier extends CompendiumEntry{

	private int meta = -1;
	private SpellModifiers[] modifies;        //what stats does this modifier affect?

	public CompendiumEntrySpellModifier(){
		super(CompendiumEntryTypes.instance.SPELL_MODIFIER);
	}

	@Override
	protected void parseEx(Node node){
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i){
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("meta")){
				meta = Integer.parseInt(child.getTextContent());
			}else if (child.getNodeName().equals("modifies")){
				String[] modifierTypes = child.getTextContent().split(",");
				ArrayList<SpellModifiers> list = new ArrayList<SpellModifiers>();
				for (String s : modifierTypes){
					try{
						SpellModifiers modifier = Enum.valueOf(SpellModifiers.class, s);
						list.add(modifier);
					}catch (Throwable t){
						FMLLog.fine("Ars Magica 2 >> Compendium Parsing Error - No modifiable constant exists with the name '%s'", s);
					}
				}
				this.modifies = list.toArray(new SpellModifiers[list.size()]);
			}
		}
	}

	public SpellModifiers[] getModifies(){
		return this.modifies;
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
