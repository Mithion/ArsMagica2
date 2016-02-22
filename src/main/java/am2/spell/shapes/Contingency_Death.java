package am2.spell.shapes;

import am2.api.power.PowerTypes;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.ContingencyTypes;
import am2.api.spell.enums.SpellCastResult;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Contingency_Death implements ISpellShape{

	@Override
	public int getID(){
		return 15;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				Items.clock,
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
				Blocks.stone,
				Blocks.stone_slab,
				Blocks.stone_slab,
				Blocks.stone_slab,
				Items.blaze_powder,
				BlocksCommonProxy.tarmaRoot,
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				String.format("E:%d", PowerTypes.DARK.ID()), 5000
		};
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount){
		ExtendedProperties.For(target != null ? target : caster).setContingency(ContingencyTypes.DEATH, SpellUtils.instance.popStackStage(stack));
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack){
		return 10;
	}

	@Override
	public boolean isTerminusShape(){
		return false;
	}

	@Override
	public boolean isPrincipumShape(){
		return true;
	}

	@Override
	public String getSoundForAffinity(Affinity affinity, ItemStack stack, World world){
		return "arsmagica2:spell.contingency.contingency";
	}
}