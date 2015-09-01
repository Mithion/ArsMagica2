package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;

public class HarvestPlants implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		Block block = world.getBlock(blockx, blocky, blockz);										
		if (!(block instanceof IGrowable)) return false;
		if (!world.isRemote){
			block.breakBlock(world, blockx, blocky, blockz, block, 0);
			block.dropBlockAsItem(world, blockx, blocky, blockz, world.getBlockMetadata(blockx, blocky, blockz), Block.getIdFromBlock(block));
			world.setBlock(blockx, blocky, blockz, Blocks.air);
		}
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 60;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, "plant", x, y + 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.3f, 1, false));
				particle.setAffectedByGravity();
				particle.setMaxAge(20);
				particle.setParticleScale(0.1f);
				particle.setRGBColorF(0.7f, 0.2f, 0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier& 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.NATURE);
	}

	@Override
	public int getID() {
		return 23;
	}
	
	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
			Items.shears
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.02f;
	}
}
