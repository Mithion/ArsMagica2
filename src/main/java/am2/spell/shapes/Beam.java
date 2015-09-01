package am2.spell.shapes;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.power.PowerTypes;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMBeam;
import am2.particles.AMParticle;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleMoveOnHeading;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.spell.modifiers.Colour;
import am2.utility.MathUtilities;
import cpw.mods.fml.common.FMLLog;

public class Beam implements ISpellShape {

	private final HashMap beams;

	public Beam(){
		beams = new HashMap();
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean giveXP, int useCount) {

		boolean shouldApplyEffect = useCount % 10 == 0;

		double range = SpellUtils.instance.getModifiedDouble_Add(SpellModifiers.RANGE, stack, caster, target, world, 0);
		boolean targetWater = SpellUtils.instance.modifierIsPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS, stack, 0);
		MovingObjectPosition mop = item.getMovingObjectPosition(caster, world, range, true, targetWater);

		SpellCastResult result = null;
		Vec3 beamHitVec = null;
		Vec3 spellVec = null;

		if (mop == null){
			beamHitVec = MathUtilities.extrapolateEntityLook(world, caster, range);
			spellVec = beamHitVec;
		}else if (mop.typeOfHit == MovingObjectType.ENTITY){
			if (shouldApplyEffect){
				Entity e = mop.entityHit;
				if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
					e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;
				result = SpellHelper.instance.applyStageToEntity(stack, caster, world, e, 0, giveXP);
				if (result != SpellCastResult.SUCCESS){
					return result;
				}
			}
			float rng = (float) mop.hitVec.distanceTo(Vec3.createVectorHelper(caster.posX, caster.posY, caster.posZ));
			beamHitVec = MathUtilities.extrapolateEntityLook(world, caster, rng);
			spellVec = beamHitVec;
		}else{
			if (shouldApplyEffect){
				result = SpellHelper.instance.applyStageToGround(stack, caster, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, 0, giveXP);
				if (result != SpellCastResult.SUCCESS){
					return result;
				}
			}
			beamHitVec = mop.hitVec;
			spellVec = Vec3.createVectorHelper(mop.blockX, mop.blockY, mop.blockZ);
		}

		if (world.isRemote && beamHitVec != null){
			AMBeam beam = (AMBeam)beams.get(caster.getEntityId());
			double startX = caster.posX;
			double startY = caster.posY + caster.getEyeHeight() - 0.2f;
			double startZ = caster.posZ;
			Affinity affinity = SpellUtils.instance.mainAffinityFor(stack);

			int color = -1;
			if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, stack, 0)){
				ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(stack, 0);
				int ordinalCount = 0;
				for (ISpellModifier mod : mods){
					if (mod instanceof Colour){
						byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(stack, mod, 0, ordinalCount++);
						color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
					}
				}
			}

			if (beam != null){
				if (beam.isDead || beam.getDistanceSqToEntity(caster) > 4){
					beams.remove(caster.getEntityId());
				}else{
					beam.setBeamLocationAndTarget(startX, startY, startZ, beamHitVec.xCoord, beamHitVec.yCoord, beamHitVec.zCoord);
				}
			}else{
				if (affinity == Affinity.LIGHTNING){
					AMCore.instance.proxy.particleManager.BoltFromEntityToPoint(world, caster, beamHitVec.xCoord, beamHitVec.yCoord, beamHitVec.zCoord, 1, color == -1 ? affinity.color : color);
				}else{
					beam = (AMBeam) AMCore.instance.proxy.particleManager.BeamFromEntityToPoint(world, caster, beamHitVec.xCoord, beamHitVec.yCoord, beamHitVec.zCoord, color == -1 ? affinity.color : color);
					if (beam != null){
						if (AMCore.instance.proxy.getProxyUtils().isLocalPlayerInFirstPerson())
							beam.setFirstPersonPlayerCast();
						beams.put(caster.getEntityId(), beam);
					}
				}
			}
			for (int i = 0; i < AMCore.config.getGFXLevel()+1; ++i){
				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, AMParticleIcons.instance.getParticleForAffinity(affinity), beamHitVec.xCoord, beamHitVec.yCoord, beamHitVec.zCoord);
				if (particle != null){
					particle.setMaxAge(2);
					particle.setParticleScale(0.1f);
					particle.setIgnoreMaxAge(false);
					if (color != -1)
						particle.setRGBColorI(color);
					particle.AddParticleController(new ParticleMoveOnHeading(particle, world.rand.nextDouble() * 360, world.rand.nextDouble() * 360, world.rand.nextDouble() * 0.2 + 0.02f, 1, false));
				}
			}
		}

		if (result != null && spellVec != null && shouldApplyEffect){
			ItemStack newItemStack = SpellUtils.instance.popStackStage(stack);
			return SpellHelper.instance.applyStackStage(newItemStack, caster, target, spellVec.xCoord, spellVec.yCoord, spellVec.zCoord, mop != null ? mop.sideHit : 0, world, true, giveXP, 0);
		}else{
			return SpellCastResult.SUCCESS_REDUCE_MANA;
		}
	}

	@Override
	public boolean isChanneled() {
		return true;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
				ItemsCommonProxy.standardFocus,
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_BLUETOPAZ),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_BLUETOPAZ),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_PURIFIEDVINTEUM),
				BlocksCommonProxy.aum,
				String.format("E:%d", PowerTypes.NEUTRAL.ID()), 500
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		return 0.1f;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
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
