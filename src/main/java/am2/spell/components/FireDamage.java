package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.power.IPowerNode;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.damage.DamageSources;
import am2.entities.EntityDarkling;
import am2.entities.EntityFireElemental;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.power.PowerNodeRegistry;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class FireDamage implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		Block block = world.getBlock(blockx, blocky, blockz);

		if (block == BlocksCommonProxy.obelisk){
			ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, blockx, blocky, blockz);
			if (reagents != null){
				if (!world.isRemote){
					RitualShapeHelper.instance.consumeRitualReagents(this, world, blockx, blocky, blockz);
					RitualShapeHelper.instance.consumeRitualShape(this, world, blockx, blocky, blockz);
					world.setBlock(blockx, blocky, blockz, BlocksCommonProxy.blackAurem);
					PowerNodeRegistry.For(world).registerPowerNode((IPowerNode)world.getTileEntity(blockx, blocky, blockz));
				}else{

				}

				return true;
			}
		}
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)) return false;
		float baseDamage = 6;
		double damage = SpellUtils.instance.getModifiedDouble_Add(baseDamage, stack, caster, target, world, 0, SpellModifiers.DAMAGE);
		if (isNetherMob(target))
			return true;
		return SpellHelper.instance.attackTargetSpecial(stack, target, DamageSources.causeEntityFireDamage(caster), SpellUtils.instance.modifyDamage(caster, (float)damage));
	}

	private boolean isNetherMob(Entity target){
		return target instanceof EntityPigZombie || target instanceof EntityDarkling || target instanceof EntityFireElemental || target instanceof EntityGhast;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 120;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 20;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 5; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "explosion_2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2, rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.FIRE);
	}

	@Override
	public int getID(){
		return 15;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				Items.flint_and_steel,
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST),
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.corruption;
	}


	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.mobFocus),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
