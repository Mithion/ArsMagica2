package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.particles.AMParticle;
import am2.particles.ParticleHoldPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class MeltArmor implements ISpellComponent{

	private static final String mmpsNBTTagName = "mmmpsmod";
	private static final String mmpsChargeTagName = "Current Energy";

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(Blocks.sponge)
		};
	}

	@Override
	public int getID(){
		return 60;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityPlayer && !world.isRemote){
			doMeltArmor(caster, ((EntityPlayer)target).inventory.armorInventory);
			return true;
		}
		return false;
	}

	private void doMeltArmor(EntityLivingBase caster, ItemStack[] armor){
		double mmpsCharge = getMMPSCharge(armor);
		for (ItemStack stack : armor){
			if (stack == null) continue;
			if (!stack.hasTagCompound() || !stack.stackTagCompound.hasKey(mmpsNBTTagName)){
				stack.damageItem((int)Math.ceil(stack.getItem().getMaxDurability() * 0.25f), caster);
			}else{
				NBTTagCompound subCompound = (NBTTagCompound)stack.stackTagCompound.getTag(mmpsNBTTagName);
				double charge = stack.stackTagCompound.getDouble(mmpsChargeTagName);
				charge -= mmpsCharge * 0.75f;
				if (charge < 0) charge = 0;
				subCompound.setDouble(mmpsChargeTagName, charge);
			}
		}
	}

	private double getMMPSCharge(ItemStack[] armor){
		double total = -1;
		for (ItemStack stack : armor){
			if (stack != null && stack.hasTagCompound()){
				NBTTagCompound subCompound = (NBTTagCompound)stack.stackTagCompound.getTag(mmpsNBTTagName);
				if (subCompound != null && subCompound.hasKey(mmpsChargeTagName)){
					total += subCompound.getDouble(mmpsChargeTagName);
				}
			}
		}
		return total;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 15;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 15;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "radiant", x + 0.5, y + 0.5, z + 0.5);
		if (particle != null){
			particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, false));
			particle.setMaxAge(20);
			particle.setParticleScale(0.3f);
			particle.setRGBColorF(0.7f, 0.4f, 0.2f);
			particle.SetParticleAlpha(0.1f);
			if (colorModifier > -1){
				particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.FIRE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}

}
