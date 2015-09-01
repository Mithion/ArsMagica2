package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleHoldPosition;
import am2.utility.EntityUtilities;

public class Forge implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		if (!CanApplyFurnaceToBlockAtCoords(caster, world, blockx, blocky, blockz)) return false;
		ApplyFurnaceToBlockAtCoords(caster, world, blockx, blocky, blockz);
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		if (target instanceof EntityVillager && AMCore.config.forgeSmeltsVillagers()){
			if (!world.isRemote && !EntityUtilities.isSummon((EntityLivingBase) target))
				target.dropItem(Items.emerald, 1);
			if (caster instanceof EntityPlayer)
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)caster), 5000);
			else
				target.attackEntityFrom(DamageSource.causeMobDamage(caster), 5000);
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 55;
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
		AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, "radiant", x + 0.5, y + 0.5, z + 0.5);
		if (particle != null){
			particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, false));
			particle.setMaxAge(20);
			particle.setParticleScale(0.3f);
			particle.setRGBColorF(0.7f, 0.4f, 0.2f);
			particle.SetParticleAlpha(0.1f);
			if (colorModifier > -1){
				particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier& 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.FIRE);
	}

	private boolean ApplyFurnaceToBlockAtCoords(EntityLivingBase entity, World world, int x, int y, int z){

		Block block = world.getBlock(x, y, z);

		if (block == Blocks.air){
			return false;
		}

		if (block == Blocks.ice){
			if (!world.isRemote){
				world.setBlock(x, y, z, Blocks.water);
			}
			return true;
		}

		int meta = world.getBlockMetadata(x, y, z);
		ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(block, 1, meta));
		if (smelted == null){
			return false;
		}
		if(!world.isRemote){
			if (ItemIsBlock(smelted.getItem())){
				world.setBlock(x, y, z, ((ItemBlock)smelted.getItem()).field_150939_a);
			}
			else{
				entity.entityDropItem(new ItemStack(smelted.getItem(), 1, smelted.getItemDamage()), 0);
				world.setBlock(x, y, z, Blocks.air);
			}
		}
		return true;
	}

	private boolean CanApplyFurnaceToBlockAtCoords(EntityLivingBase entity, World world, int x, int y, int z){

		Block block = world.getBlock(x, y, z);

		if (block == Blocks.air){
			return false;
		}

		if (block == Blocks.ice){
			return true;
		}

		int meta = world.getBlockMetadata(x, y, z);
		ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(block, 1, meta));
		if (smelted == null){
			return false;
		}
		return true;
	}

	public boolean ItemIsBlock(Item smelted){
		return smelted instanceof ItemBlock;
	}

	@Override
	public int getID() {
		return 18;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
			Blocks.furnace
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.01f;
	}
}
