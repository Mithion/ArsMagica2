package am2.spell.shapes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.ContingencyTypes;
import am2.api.spell.enums.SpellCastResult;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils;

public class Contingency_Hit implements ISpellShape{

	@Override
	public int getID() {
		return 13;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
				Items.clock,
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIGHTNING),
				Items.diamond_sword,
				BlocksCommonProxy.tarmaRoot,
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_MOONSTONE),
				"E:*", 5000
		};
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean giveXP, int useCount) {
		ExtendedProperties.For(target != null ? target : caster).setContingency(ContingencyTypes.DAMAGE_TAKEN, SpellUtils.instance.popStackStage(stack));
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		return 10;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		return true;
	}

	@Override
	public String getSoundForAffinity(Affinity affinity, ItemStack stack, World world) {
		return "arsmagica2:spell.contingency.contingency";
	}
}