package am2.spell.shapes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.spell.components.Attract;
import am2.spell.components.Repel;
import am2.spell.components.Telekinesis;

public class Channel implements ISpellShape{

	@Override
	public int getID() {
		return 4;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean giveXP, int useCount) {
		boolean shouldApplyEffect = useCount % 10 == 0 || SpellUtils.instance.componentIsPresent(stack, Telekinesis.class, 0) || SpellUtils.instance.componentIsPresent(stack, Attract.class, 0) || SpellUtils.instance.componentIsPresent(stack, Repel.class, 0);
		if (shouldApplyEffect){
			SpellCastResult result = SpellHelper.instance.applyStageToEntity(stack, caster, world, caster, 0, giveXP);
			if (result != SpellCastResult.SUCCESS){
				return result;
			}
		}

		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled() {
		return true;
	}

	@Override
	public Object[] getRecipeItems() {
		//Arcane Ash, Arcane Essence, Tarma Root, 500 any power
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE),
				BlocksCommonProxy.tarmaRoot,
				"E:*", 500
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		return 1;
	}

	@Override
	public boolean isTerminusShape() {
		return true;
	}

	@Override
	public boolean isPrincipumShape() {
		return false;
	}

	@Override
	public String getSoundForAffinity(Affinity affinity, ItemStack stack, World world) {
		switch(affinity){
		case AIR:
			return "arsmagica2:spell.loop.air";
		case ARCANE:
			return "arsmagica2:spell.loop.arcane";
		case EARTH:
			return "arsmagica2:spell.loop.earth";
		case ENDER:
			return "arsmagica2:spell.loop.ender";
		case FIRE:
			return "arsmagica2:spell.loop.fire";
		case ICE:
			return "arsmagica2:spell.loop.ice";
		case LIFE:
			return "arsmagica2:spell.loop.life";
		case LIGHTNING:
			return "arsmagica2:spell.loop.lightning";
		case NATURE:
			return "arsmagica2:spell.loop.nature";
		case WATER:
			return "arsmagica2:spell.loop.water";
		case NONE:
		default:
			return "arsmagica2:spell.loop.none";
		}
	}
}
