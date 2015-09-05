package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.math.AMVector3;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleApproachPoint;
import am2.playerextensions.ExtendedProperties;
import am2.utility.MathUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Attract implements ISpellComponent{

	private final HashMap arcs;

	public Attract(){
		arcs = new HashMap();
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		doTK_Extrapolated(stack, world, impactX, impactY, impactZ, caster);
		return true;
	}

	private boolean doTK_Extrapolated(ItemStack stack, World world, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		if (caster instanceof EntityPlayer){
			double range = ExtendedProperties.For(caster).TK_Distance;
			MovingObjectPosition mop = ItemsCommonProxy.spell.getMovingObjectPosition(caster, world, range, false, false);
			if (mop == null){
				impactX = caster.posX + (Math.cos(Math.toRadians(caster.rotationYaw + 90)) * range);
				impactZ = caster.posZ + (Math.sin(Math.toRadians(caster.rotationYaw + 90)) * range);
				impactY = caster.posY + caster.getEyeHeight() + (-Math.sin(Math.toRadians(caster.rotationPitch)) * range);
			}
		}

		EntityLivingBase target = getClosestEntityToPointWithin(caster, world, new AMVector3(impactX, impactY, impactZ), 16);

		if (target == null) return false;

		int hDist = 3;
		AMVector3 movement = MathUtilities.GetMovementVectorBetweenPoints(new AMVector3(target), new AMVector3(impactX, impactY, impactZ));

		if (!world.isRemote){
			float factor = 0.75f;
			double x = -(movement.x * factor);
			double y = -(movement.y * factor);
			double z = -(movement.z * factor);

			target.addVelocity(x, y, z);
			if (Math.abs(target.motionX) > Math.abs(x * 2)){
				target.motionX = x * (target.motionX / target.motionX);
			}
			if (Math.abs(target.motionY) > Math.abs(y * 2)){
				target.motionY = y * (target.motionY / target.motionY);
			}
			if (Math.abs(target.motionZ) > Math.abs(z * 2)){
				target.motionZ = z * (target.motionZ / target.motionZ);
			}
		}
		return true;
	}

	private EntityLivingBase getClosestEntityToPointWithin(EntityLivingBase caster, World world, AMVector3 point, double radius){
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(point.x - radius, point.y - radius, point.z - radius, point.x + radius, point.y + radius, point.z + radius);
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		EntityLivingBase closest = null;
		for (EntityLivingBase e : entities){
			if (e == caster) continue;
			if (closest == null || point.distanceSqTo(new AMVector3(e)) < point.distanceSqTo(new AMVector3(closest)))
				closest = e;
		}
		return closest;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		doTK_Extrapolated(stack, world, target.posX, target.posY, target.posZ, caster);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 2.6f;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.instance.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(world, "arcane", x, y, z);
		if (effect != null){
			effect.addRandomOffset(1, 1, 1);
			effect.AddParticleController(new ParticleApproachPoint(effect, x, y, z, 0.025f, 0.025f, 1, false));
			effect.setRGBColorF(0.8f, 0.3f, 0.7f);
			if (colorModifier > -1){
				effect.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID(){
		return 2;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
				Items.iron_ingot
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}
}
