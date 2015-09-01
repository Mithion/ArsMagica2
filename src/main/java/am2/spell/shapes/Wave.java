package am2.spell.shapes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.entities.EntitySpellEffect;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;

public class Wave implements ISpellShape {

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST),
				BlocksCommonProxy.magicWall,
				"E:*", 25000
		};
	}

	@Override
	public int getID() {
		return 17;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean giveXP, int useCount) {
		if (world.isRemote) return SpellCastResult.SUCCESS;
		double radius = SpellUtils.instance.getModifiedDouble_Add(1, stack, caster, target, world, 0, SpellModifiers.RADIUS);
		int duration = SpellUtils.instance.getModifiedInt_Mul(20, stack, caster, target, world, 0, SpellModifiers.DURATION);
		double speed = SpellUtils.instance.getModifiedDouble_Add(1f, stack, caster, target, world, 0, SpellModifiers.SPEED) * 0.5f;
		int gravityModifiers = SpellUtils.instance.countModifiers(SpellModifiers.GRAVITY, stack, 0);
		boolean hasPiercing = SpellUtils.instance.modifierIsPresent(SpellModifiers.PIERCING, stack, 0);

		EntitySpellEffect wave = new EntitySpellEffect(world);
		wave.setRadius((float) radius);
		wave.setTicksToExist(duration);
		wave.SetCasterAndStack(caster, SpellUtils.instance.popStackStage(stack));
		wave.setPosition(x, y+1, z);
		wave.setWave(caster.rotationYaw, (float) speed);
		wave.noClip = hasPiercing;
		wave.setGravity(gravityModifiers * 0.5f);
		world.spawnEntityInWorld(wave);
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		return 3f;
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
		switch(affinity){
		case AIR:
			return "arsmagica2:spell.cast.air";
		case ARCANE:
			return "arsmagica2:spell.cast.arcane";
		case EARTH:
			return "arsmagica2:spell.cast.earth";
		case ENDER:
			return "arsmagica2:spell.cast.ender";
		case FIRE:
			return "arsmagica2:spell.cast.fire";
		case ICE:
			return "arsmagica2:spell.cast.ice";
		case LIFE:
			return "arsmagica2:spell.cast.life";
		case LIGHTNING:
			return "arsmagica2:spell.cast.lightning";
		case NATURE:
			return "arsmagica2:spell.cast.nature";
		case WATER:
			return "arsmagica2:spell.cast.water";
		case NONE:
		default:
			return "arsmagica2:spell.cast.none";
		}
	}
}
