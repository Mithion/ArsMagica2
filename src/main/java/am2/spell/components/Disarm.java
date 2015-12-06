package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.enchantments.AMEnchantments;
import am2.entities.EntityDarkMage;
import am2.entities.EntityLightMage;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;

public class Disarm implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){

		if (target instanceof EntityLightMage || target instanceof EntityDarkMage)
			return false;

		if (target instanceof EntityPlayer && (!AMCore.config.getDisarmAffectsPlayers() || (!world.isRemote && !MinecraftServer.getServer().isPVPEnabled())))
			return false;

		if (target instanceof EntityPlayer && ((EntityPlayer)target).getHeldItem() != null && !target.worldObj.isRemote){
			if (EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound.effectId, ((EntityPlayer)target).getHeldItem()) > 0)
				return true;
			((EntityPlayer)target).dropOneItem(true);
			return true;
		}else if (target instanceof EntityMob && ((EntityMob)target).getHeldItem() != null){

			if (EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound.effectId, ((EntityMob)target).getHeldItem()) > 0)
				return true;

			if (!world.isRemote){
				EntityItem item = new EntityItem(world);
				ItemStack dropstack = ((EntityMob)target).getHeldItem().copy();
				if (dropstack.getMaxDamage() > 0)
					dropstack.setItemDamage((int)Math.floor(dropstack.getMaxDamage() * (0.8f + (world.rand.nextFloat() * 0.19f))));
				item.setEntityItemStack(dropstack);
				item.setPosition(target.posX, target.posY, target.posZ);
				world.spawnEntityInWorld(item);
			}
			((EntityMob)target).setCurrentItemOrArmor(0, null);

			((EntityMob)target).setAttackTarget(caster);

			Iterator it = ((EntityMob)target).tasks.taskEntries.iterator();
			boolean removed = false;
			while (it.hasNext()){
				EntityAITaskEntry task = (EntityAITaskEntry)it.next();
				if (task.action instanceof EntityAIArrowAttack){
					it.remove();
					removed = true;
					break;
				}
			}

			if (removed){
				((EntityMob)target).tasks.addTask(5, new EntityAIAttackOnCollide((EntityCreature)target, 0.5, true));
				((EntityMob)target).setCanPickUpLoot(true);
			}
		}else if (target instanceof EntityEnderman){
			int blockID = ((EntityEnderman)target).getCarryingData();
			int meta = ((EntityEnderman)target).getCarryingData();

			if (blockID > 0){
				((EntityEnderman)target).setCarryingData(0);
				ItemStack dropstack = new ItemStack(Block.getBlockById(blockID), 1, meta);
				EntityItem item = new EntityItem(world);
				item.setEntityItemStack(dropstack);
				item.setPosition(target.posX, target.posY, target.posZ);
				world.spawnEntityInWorld(item);
			}
			((EntityMob)target).setAttackTarget(caster);
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 130;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 26;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleMoveOnHeading(particle, MathHelper.wrapAngleTo180_double((target instanceof EntityLivingBase ? ((EntityLivingBase)target).rotationYawHead : target.rotationYaw) + 90), MathHelper.wrapAngleTo180_double(target.rotationPitch), 0.1 + rand.nextDouble() * 0.5, 1, false));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f));
				particle.setAffectedByGravity();
				if (rand.nextBoolean())
					particle.setRGBColorF(0.7f, 0.7f, 0.1f);
				else
					particle.setRGBColorF(0.1f, 0.7f, 0.1f);
				particle.setMaxAge(40);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID(){
		return 9;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				Items.iron_sword
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}
}
