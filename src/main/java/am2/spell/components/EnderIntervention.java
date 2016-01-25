package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.utility.DimensionUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class EnderIntervention implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (world.isRemote || !(target instanceof EntityLivingBase)) return true;

		if (((EntityLivingBase)target).isPotionActive(BuffList.astralDistortion.id)){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new ChatComponentText("The distortion around you prevents you from teleporting"));
			return true;
		}

		if (target.dimension == 1){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new ChatComponentText("Nothing happens..."));
			return true;
		}else if (target.dimension == -1){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new ChatComponentText("You are already in the nether."));
			return false;
		}else{
//			DimensionUtilities.doDimensionTransfer((EntityLivingBase)target, -1);
			AMCore.proxy.addDeferredDimensionTransfer((EntityLivingBase)target, -1);
		}

		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 400;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return new ItemStack[]{new ItemStack(ItemsCommonProxy.essence, 1, 9)};
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 100; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "ghost", x, y - 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
				particle.setMaxAge(25 + rand.nextInt(10));
				particle.setRGBColorF(0.7f, 0.2f, 0.2f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
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
		return 13;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				Blocks.obsidian,
				Blocks.obsidian,
				Items.flint_and_steel,
				Items.ender_pearl
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.4f;
	}
}
