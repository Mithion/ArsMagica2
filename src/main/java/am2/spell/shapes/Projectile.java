package am2.spell.shapes;

import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.entities.EntitySpellProjectile;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Projectile implements ISpellShape{

	@Override
	public int getID(){
		return 5;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean giveXP, int useCount){
		if (!world.isRemote){
			double projectileSpeed = SpellUtils.instance.getModifiedDouble_Mul(SpellModifiers.SPEED, stack, caster, target, world, 0);
			double projectileGravity = SpellUtils.instance.getModifiedDouble_Add(SpellModifiers.GRAVITY, stack, caster, target, world, 0);
			int projectileBounce = SpellUtils.instance.getModifiedInt_Add(SpellModifiers.BOUNCE, stack, caster, target, world, 0);
			int projectileLife = SpellUtils.instance.getModifiedInt_Mul(SpellModifiers.DURATION, stack, caster, target, world, 0);
			int pierces = SpellUtils.instance.getModifiedInt_Add(0, stack, caster, target, world, 0, SpellModifiers.PIERCING);

			boolean tWater = SpellUtils.instance.modifierIsPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS, stack, 0);

			EntitySpellProjectile projectile = new EntitySpellProjectile(world, caster, projectileSpeed);
			projectile.setShootingEntity(caster);
			projectile.setBounces(projectileBounce);
			projectile.setEffectStack(stack);
			if (tWater)
				projectile.setTargetWater();
			projectile.setGravity(projectileGravity);
			projectile.setNumPierces(pierces);

			world.spawnEntityInWorld(projectile);
		}

		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST),
				Items.arrow,
				Items.snowball
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack){
		return 1.25f;
	}

	@Override
	public boolean isTerminusShape(){
		return false;
	}

	@Override
	public boolean isPrincipumShape(){
		return false;
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
