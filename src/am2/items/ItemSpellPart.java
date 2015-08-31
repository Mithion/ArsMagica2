package am2.items;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.spell.SkillManager;
import am2.texture.ResourceManager;
import am2.texture.SpellIconManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpellPart extends ArsMagicaItem{

	public ItemSpellPart() {
		super();
		this.setMaxDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister IIconRegister) {
		ArrayList<Integer> parts = SkillManager.instance.getAllShapes();
		parts.addAll(SkillManager.instance.getAllComponents());
		parts.addAll(SkillManager.instance.getAllModifiers());
		parts.addAll(SkillManager.instance.getAllTalents());
		int count = 0;

		for (Integer i : parts){
			if (i == null) continue;
			ISkillTreeEntry entry = SkillManager.instance.getSkill(i);
			String subfolder = "";
			if (entry instanceof ISpellShape)
				subfolder="shapes";
			else if (entry instanceof ISpellComponent)
				subfolder="components";
			else if (entry instanceof ISpellModifier)
				subfolder="modifiers";
			else
				subfolder = "skills";

			String skillName = SkillManager.instance.getSkillName(entry);
			IIcon IIcon = ResourceManager.RegisterTexture(String.format("spells/%s/%s.png", subfolder, skillName), IIconRegister);

			SpellIconManager.instance.registerIcon(skillName, IIcon);
		}
	}

	@Override
	public IIcon getIconFromDamage(int par1) {
		return SpellIconManager.instance.getIcon(SkillManager.instance.getSkillName(SkillManager.instance.getSkill(par1)));
	}
}
