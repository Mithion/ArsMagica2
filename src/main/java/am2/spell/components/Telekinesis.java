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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class Telekinesis implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return doTK_Extrapolated(stack, world, impactX, impactY, impactZ, caster);
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return doTK_Extrapolated(stack, world, target.posX, target.posY, target.posZ, caster);
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

		double distance = 16;
		int hDist = 3;
		List<Entity> entities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(impactX - distance, impactY - hDist, impactZ - distance, impactX + distance, impactY + hDist, impactZ + distance));
		entities.addAll(world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(impactX - distance, impactY - hDist, impactZ - distance, impactX + distance, impactY + hDist, impactZ + distance)));
		for (Entity e : entities){
			if (e.ticksExisted < 20){
				continue;
			}
			AMVector3 movement = MathUtilities.GetMovementVectorBetweenPoints(new AMVector3(e), new AMVector3(impactX, impactY, impactZ));

			if (!world.isRemote){
				float factor = 0.15f;
				if (movement.y > 0) movement.y = 0;
				double x = -(movement.x * factor);
				double y = -(movement.y * factor);
				double z = -(movement.z * factor);

				e.addVelocity(x, y, z);
				if (Math.abs(e.motionX) > Math.abs(x * 2)){
					e.motionX = x * (e.motionX / e.motionX);
				}
				if (Math.abs(e.motionY) > Math.abs(y * 2)){
					e.motionY = y * (e.motionY / e.motionY);
				}
				if (Math.abs(e.motionZ) > Math.abs(z * 2)){
					e.motionZ = z * (e.motionZ / e.motionZ);
				}
			}
		}
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 6;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){

		if (caster instanceof EntityPlayer){
			double range = ExtendedProperties.For(caster).TK_Distance;
			MovingObjectPosition mop = ItemsCommonProxy.spell.getMovingObjectPosition(caster, world, range, false, false);
			if (mop == null){
				x = caster.posX + (Math.cos(Math.toRadians(caster.rotationYaw + 90)) * range);
				z = caster.posZ + (Math.sin(Math.toRadians(caster.rotationYaw + 90)) * range);
				y = caster.posY + (-Math.sin(Math.toRadians(caster.rotationPitch)) * range);
			}
		}


		AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(world, "arcane", x - 0.5 + rand.nextDouble(), y - 0.5 + rand.nextDouble(), z - 0.5 + rand.nextDouble());
		if (effect != null){
			effect.AddParticleController(new ParticleApproachPoint(effect, x, y, z, 0.025f, 0.025f, 1, false));
			effect.setRGBColorF(0.8f, 0.3f, 0.7f);
			if (colorModifier > -1){
				effect.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.ARCANE);
	}

	@Override
	public int getID(){
		return 54;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				Blocks.sticky_piston,
				Blocks.chest
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.001f;
	}
}
