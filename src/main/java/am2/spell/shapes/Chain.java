package am2.spell.shapes;

import am2.AMCore;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.spell.modifiers.Colour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Chain implements ISpellShape{

	@Override
	public int getID(){
		return 3;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount){

		MovingObjectPosition mop = item.getMovingObjectPosition(caster, world, 8.0f, true, false);
		double range = SpellUtils.instance.getModifiedDouble_Mul(4, stack, caster, target, world, 0, SpellModifiers.RANGE);
		int num_targets = SpellUtils.instance.getModifiedInt_Add(3, stack, caster, target, world, 0, SpellModifiers.PROCS);

		ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();

		if (target != null){
			mop = new MovingObjectPosition(target);
		}

		if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit != null){
			Entity e = mop.entityHit;
			if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
				e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;
			if (e instanceof EntityLivingBase){
				do{
					targets.add((EntityLivingBase)e);

					List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, e.getEntityBoundingBox().expand(range, range, range));
					EntityLivingBase closest = null;
					for (EntityLivingBase near : nearby){
						if (targets.contains(near) || near == caster) continue;

						if (closest == null || closest.getDistanceSqToEntity(e) > near.getDistanceSqToEntity(e)){
							closest = near;
						}
					}

					e = closest;

				}while (e != null && targets.size() < num_targets);
			}
		}

		ItemStack newItemStack = SpellUtils.instance.popStackStage(stack);
		boolean atLeastOneApplication = false;
		SpellCastResult result = SpellCastResult.SUCCESS;

		EntityLivingBase prevEntity = null;

		for (EntityLivingBase e : targets){
			if (e == caster)
				continue;
			result = SpellHelper.instance.applyStageToEntity(stack, caster, world, e, 0, giveXP);
			SpellHelper.instance.applyStackStage(newItemStack, caster, e, e.posX, e.posY, e.posZ, EnumFacing.DOWN, world, true, giveXP, 0);

			if (world.isRemote){
				if (prevEntity == null)
					spawnChainParticles(world, x, y, z, e.posX, e.posY + e.getEyeHeight(), e.posZ, stack);
				else
					spawnChainParticles(world, prevEntity.posX, prevEntity.posY + e.getEyeHeight(), prevEntity.posZ, e.posX, e.posY + e.getEyeHeight(), e.posZ, stack);
			}
			prevEntity = e;

			if (result == SpellCastResult.SUCCESS){
				atLeastOneApplication = true;
			}
		}

		if (atLeastOneApplication){
			return SpellCastResult.SUCCESS;
		}
		return result;
	}

	private void spawnChainParticles(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, ItemStack spellStack){
		int color = getPFXColor(spellStack);

		Affinity aff = SpellUtils.instance.mainAffinityFor(spellStack);

		if (aff == Affinity.LIGHTNING){
			AMCore.proxy.particleManager.BoltFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 1, color);
		}else{
			if (color == -1)
				color = aff.color;
			AMCore.proxy.particleManager.BeamFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, color);
		}
	}

	private int getPFXColor(ItemStack stack){
		int color = -1;
		if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, stack, 0)){
			ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(stack, 0);
			int ordinalCount = 0;
			for (ISpellModifier mod : mods){
				if (mod instanceof Colour){
					byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(stack, mod, 0, ordinalCount++);
					color = (int)mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
				}
			}
		}
		return color;
	}

	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE),
				Items.lead,
				Items.iron_ingot,
				Blocks.tripwire_hook,
				Items.string
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack){
		return 1.5f;
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
