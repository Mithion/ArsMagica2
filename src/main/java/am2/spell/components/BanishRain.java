package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class BanishRain implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, blockx, blocky, blockz, true);
		if (reagents != null && reagents.length > 0){
			RitualShapeHelper.instance.consumeRitualReagents(this, world, blockx, blocky, blockz);
			world.getWorldInfo().setRainTime(0);
			world.getWorldInfo().setRaining(true);
			return true;
		}
		if (!world.isRaining()) return false;
		world.getWorldInfo().setRainTime(24000);
		world.getWorldInfo().setRaining(false);
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, (int)Math.floor(target.posX), (int)Math.floor(target.posY), (int)Math.floor(target.posZ), true);
		if (reagents != null && reagents.length > 0){
			RitualShapeHelper.instance.consumeRitualReagents(this, world, (int)Math.floor(target.posX), (int)Math.floor(target.posY), (int)Math.floor(target.posZ));
			world.getWorldInfo().setRainTime(0);
			world.getWorldInfo().setRaining(true);
			return true;
		}
		if (!world.isRaining()) return false;
		world.getWorldInfo().setRainTime(24000);
		world.getWorldInfo().setRaining(false);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 750;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 250;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return new ItemStack[]{new ItemStack(ItemsCommonProxy.essence, 1, 4)};
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "water_ball", x, y, z);
			if (particle != null){
				particle.addRandomOffset(5, 4, 5);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0f, 0.5f, 1, false));
				particle.setMaxAge(25 + rand.nextInt(10));
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.WATER);
	}

	@Override
	public int getID(){
		return 3;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				Items.gold_ingot
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.3f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(Items.water_bucket),
				new ItemStack(Blocks.snow)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
