package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.ArsMagicaApi;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityOtherworldAura;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleArcToPoint;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Transplace implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		Block block = world.getBlock(blockx, blocky, blockz);
		if (!world.isRemote && caster instanceof EntityPlayer && block == BlocksCommonProxy.inertSpawner){
			ItemStack[] items = RitualShapeHelper.instance.checkForRitual(this, world, blockx, blocky, blockz);
			if (items != null){
				RitualShapeHelper.instance.consumeRitualReagents(this, world, blockx, blocky, blockz);
				RitualShapeHelper.instance.consumeRitualShape(this, world, blockx, blocky, blockz);
				world.setBlock(blockx, blocky, blockz, BlocksCommonProxy.otherworldAura);
				TileEntity te = world.getTileEntity(blockx, blocky, blockz);
				if (te != null && te instanceof TileEntityOtherworldAura){
					((TileEntityOtherworldAura)te).setPlacedByUsername(((EntityPlayer)caster).getName());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!world.isRemote && target != null && !target.isDead){
			double tPosX = target.posX;
			double tPosY = target.posY;
			double tPosZ = target.posZ;

			double cPosX = caster.posX;
			double cPosY = caster.posY;
			double cPosZ = caster.posZ;

			caster.setPositionAndUpdate(tPosX, tPosY, tPosZ);
			if (target instanceof EntityLiving)
				((EntityLiving)target).setPositionAndUpdate(cPosX, cPosY, cPosZ);
			else
				target.setPosition(cPosX, cPosY, cPosZ);

		}
		if (target instanceof EntityLiving)
			((EntityLiving)target).faceEntity(caster, 180f, 180f);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 100;
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
		for (int i = 0; i < 15; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle2", caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.AddParticleController(new ParticleArcToPoint(particle, 1, target.posX, target.posY + target.getEyeHeight(), target.posZ, false).SetSpeed(0.05f).generateControlPoints());
				particle.setMaxAge(40);
				particle.setParticleScale(0.2f);
				particle.setRGBColorF(1, 0, 0);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}

		for (int i = 0; i < 15; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle2", target.posX, target.posY + target.getEyeHeight(), target.posZ);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.AddParticleController(new ParticleArcToPoint(particle, 1, caster.posX, caster.posY + caster.getEyeHeight(), caster.posZ, false).SetSpeed(0.05f).generateControlPoints());
				particle.setMaxAge(40);
				particle.setParticleScale(0.2f);
				particle.setRGBColorF(0, 0, 1);
				if (colorModifier > -1){
					particle.setRGBColorF((0xFF - ((colorModifier >> 16) & 0xFF)) / 255.0f, (0xFF - ((colorModifier >> 8) & 0xFF)) / 255.0f, (0xFF - (colorModifier & 0xFF)) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.ENDER);
	}

	@Override
	public int getID(){
		return 55;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				Items.compass,
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				Items.ender_pearl
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.02f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.ringedCross;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_PURIFIEDVINTEUM),
				new ItemStack(ItemsCommonProxy.mageArmor),
				new ItemStack(ItemsCommonProxy.mageBoots),
				new ItemStack(ItemsCommonProxy.mageHood),
				new ItemStack(ItemsCommonProxy.mageLeggings),
				new ItemStack(ItemsCommonProxy.playerFocus)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
