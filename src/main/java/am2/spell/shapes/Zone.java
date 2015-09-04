package am2.spell.shapes;

import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.entities.EntitySpellEffect;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Zone implements ISpellShape{

	@Override
	public int getID(){
		return 10;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean giveXP, int useCount){
		if (world.isRemote) return SpellCastResult.SUCCESS;
		int radius = SpellUtils.instance.getModifiedInt_Add(2, stack, caster, target, world, 0, SpellModifiers.RADIUS);
		double gravity = SpellUtils.instance.getModifiedDouble_Add(0, stack, caster, target, world, 0, SpellModifiers.GRAVITY);
		int duration = SpellUtils.instance.getModifiedInt_Mul(100, stack, caster, target, world, 0, SpellModifiers.DURATION);
		EntitySpellEffect zone = new EntitySpellEffect(world);
		zone.setRadius(radius);
		zone.setTicksToExist(duration);
		zone.setGravity(gravity);
		zone.SetCasterAndStack(caster, SpellUtils.instance.popStackStage(stack));
		zone.setPosition(x, y, z);
		world.spawnEntityInWorld(zone);
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				BlocksCommonProxy.tarmaRoot,
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_MOONSTONE),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE),
				Items.diamond
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack){
		return 4.5f;
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
		switch (affinity){
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
