package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleConverge;
import am2.playerextensions.ExtendedProperties;

public class Mark implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		ExtendedProperties.For(caster).setMarkLocation(impactX, impactY, impactZ, caster.worldObj.provider.dimensionId);
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		if (!(target instanceof EntityLivingBase)){
			return false;
		}
		/*if (ExtendedProperties.For(caster).getMarkSet()){
			ExtendedProperties.For(caster).setNoMarkLocation();
			if (caster instanceof EntityPlayer && world.isRemote){
				((EntityPlayer)caster).addChatMessage("Mark Cleared");
			}
		}else{*/
		ExtendedProperties.For(caster).setMarkLocation(target.posX, target.posY, target.posZ, caster.worldObj.provider.dimensionId);
		/*if (caster instanceof EntityPlayer && world.isRemote){
			((EntityPlayer)caster).addChatMessage("Mark Set");
		}*/
		//}
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 5;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return 0;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {
		int offset = 1;

		SetupParticle(world, caster.posX-0.5f, caster.posY+offset, caster.posZ, 0.2, 0, colorModifier);
		SetupParticle(world, caster.posX+0.5f, caster.posY+offset, caster.posZ, -0.2, 0, colorModifier);
		SetupParticle(world, caster.posX, caster.posY+offset, caster.posZ-0.5f, 0, 0.2, colorModifier);
		SetupParticle(world, caster.posX, caster.posY+offset, caster.posZ+0.5f, 0, -0.2, colorModifier);
	}

	private void SetupParticle(World world, double x, double y, double z, double motionx, double motionz, int colorModifier){
		AMParticle effect = (AMParticle) AMCore.proxy.particleManager.spawn(world, "symbols", x, y, z);
		if (effect != null){
			effect.AddParticleController(new ParticleConverge(effect, motionx, -0.1, motionz, 1, true));
			effect.setMaxAge(40);
			effect.setIgnoreMaxAge(false);
			effect.setParticleScale(0.1f);
			if (colorModifier > -1){
				effect.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier& 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID() {
		return 37;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				new ItemStack(Items.map, 1, Short.MAX_VALUE)
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0;
	}
}
