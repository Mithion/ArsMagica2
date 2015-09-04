package am2.spell.components;

import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.entities.EntitySpellEffect;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class Blizzard implements ISpellComponent{

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_BLUETOPAZ),
				Blocks.ice,
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE)
		};
	}

	@Override
	public int getID(){
		return 63;
	}

	private boolean spawnBlizzard(ItemStack stack, World world, EntityLivingBase caster, Entity target, double x, double y, double z){

		List<EntitySpellEffect> zones = world.getEntitiesWithinAABB(EntitySpellEffect.class, AxisAlignedBB.getBoundingBox(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10));

		for (EntitySpellEffect zone : zones){
			if (zone.isBlizzard())
				return false;
		}

		if (!world.isRemote){
			int radius = SpellUtils.instance.getModifiedInt_Add(2, stack, caster, target, world, 0, SpellModifiers.RADIUS);
			double damage = SpellUtils.instance.getModifiedDouble_Mul(1, stack, caster, target, world, 0, SpellModifiers.DAMAGE);
			int duration = SpellUtils.instance.getModifiedInt_Mul(100, stack, caster, target, world, 0, SpellModifiers.DURATION);

			EntitySpellEffect blizzard = new EntitySpellEffect(world);
			blizzard.setPosition(x, y, z);
			blizzard.setBlizzard();
			blizzard.setRadius(radius);
			blizzard.setTicksToExist(duration);
			blizzard.setDamageBonus((float)damage);
			blizzard.SetCasterAndStack(caster, stack);
			world.spawnEntityInWorld(blizzard);
		}
		return true;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return spawnBlizzard(stack, world, caster, caster, impactX, impactY, impactZ);
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return spawnBlizzard(stack, world, caster, target, target.posX, target.posY, target.posZ);
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 1200;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 200;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){

	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.ICE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.1f;
	}

}
