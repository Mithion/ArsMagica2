package am2.lore;

import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.guis.GuiArcaneCompendium;
import am2.spell.SkillManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;

public class CompendiumEntryRitual extends CompendiumEntry{

	private String ritualShape;
	private String ritualController;

	public CompendiumEntryRitual(){
		super(CompendiumEntryTypes.instance.RITUAL);
	}

	@Override
	protected void parseEx(Node node){
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i){
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("ritualName")){
				this.ritualShape = child.getTextContent();
			}else if (child.getNodeName().equals("ritualController")){
				this.ritualController = child.getTextContent();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
		if (this.ritualShape != null){
			try{
				Field f = RitualShapeHelper.class.getField(ritualShape);
				MultiblockStructureDefinition def = (MultiblockStructureDefinition)f.get(RitualShapeHelper.instance);
				ISkillTreeEntry entry = SkillManager.instance.getSkill(ritualController);
				if (entry instanceof IRitualInteraction){
					IRitualInteraction controller = (IRitualInteraction)entry;
					return new GuiArcaneCompendium(searchID, def, controller);
				}
			}catch (Throwable e){
				e.printStackTrace();
			}
		}
		return new GuiArcaneCompendium(searchID);
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){
		return null;
	}

}
