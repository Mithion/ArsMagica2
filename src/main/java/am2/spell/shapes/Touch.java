package am2.spell.shapes;

import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class Touch implements ISpellShape{

	@Override
	public int getID(){
		return 9;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount){
		if (target != null){
			Entity e = target;
			if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
				e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;

			SpellCastResult result = SpellHelper.instance.applyStageToEntity(stack, caster, world, e, 0, giveXP);
			return result;
		}

		boolean targetWater = SpellUtils.instance.modifierIsPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS, stack, 0);
		MovingObjectPosition mop = item.getMovingObjectPosition(caster, world, 2.5f, true, targetWater);

		if (mop == null){
			return SpellCastResult.EFFECT_FAILED;
		}else{
			if (mop.typeOfHit == MovingObjectType.ENTITY){
				Entity e = mop.entityHit;
				if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
					e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;
				SpellCastResult result = SpellHelper.instance.applyStageToEntity(stack, caster, world, (target == null) ? e : target, 0, giveXP);
				if (result != SpellCastResult.SUCCESS){
					return result;
				}
				ItemStack newItemStack = SpellUtils.instance.popStackStage(stack);
				return SpellHelper.instance.applyStackStage(newItemStack, caster, target, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, EnumFacing.DOWN, world, true, giveXP, 0);
			}else{
				SpellCastResult result = SpellHelper.instance.applyStageToGround(stack, caster, world, mop.getBlockPos(), mop.sideHit, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, 0, giveXP);
				if (result != SpellCastResult.SUCCESS){
					return result;
				}
				ItemStack newItemStack = SpellUtils.instance.popStackStage(stack);
				return SpellHelper.instance.applyStackStage(newItemStack, caster, target, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ(), mop.sideHit, world, true, giveXP, 0);
			}
		}
	}

	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST),
				Items.feather,
				Items.fish,
				Items.clay_ball
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack){
		return 1;
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
