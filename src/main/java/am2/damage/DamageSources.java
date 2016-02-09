package am2.damage;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class DamageSources{
	public static DamageSourceUnsummon unsummon = new DamageSourceUnsummon();
	public static DamageSourceWTFBoom wtfBoom = new DamageSourceWTFBoom();
	public static DamageSourceDarkNexus darkNexus = new DamageSourceDarkNexus();

	public enum DamageSourceTypes{
		PHYSICAL,
		FIRE,
		FROST,
		LIGHTNING,
		MAGIC,
		WITHER,
		THORNS,
		CACTUS,
		HOLY
	}

	public static DamageSourceFire causeEntityFireDamage(EntityLivingBase source){
		DamageSourceFire fire = new DamageSourceFire(source);
		fire.setDifficultyScaled();
		return fire;
	}

	public static DamageSourceFrost causeEntityFrostDamage(EntityLivingBase source){
		DamageSourceFrost frost = new DamageSourceFrost(source);
		frost.setDifficultyScaled();
		return frost;
	}

	public static DamageSourceLightning causeEntityLightningDamage(EntityLivingBase source){
		DamageSourceLightning lightning = new DamageSourceLightning(source);
		lightning.setDifficultyScaled();
		return lightning;
	}

	public static DamageSourceWind causeEntityWindDamage(EntityLivingBase source){
		DamageSourceWind wind = new DamageSourceWind(source);
		wind.setDifficultyScaled();
		return wind;
	}

	public static DamageSourceHoly causeEntityHolyDamage(EntityLivingBase source){
		DamageSourceHoly holy = new DamageSourceHoly(source);
		holy.setDifficultyScaled();
		return holy;
	}

	public static DamageSource causeDamage(DamageSourceTypes type, EntityLivingBase source){
		switch (type){
		case FIRE:
			DamageSourceFire fire = new DamageSourceFire(source);
			fire.setDifficultyScaled();
			return fire;
		case FROST:
			DamageSourceFrost frost = new DamageSourceFrost(source);
			frost.setDifficultyScaled();
			return frost;
		case LIGHTNING:
			DamageSourceLightning lightning = new DamageSourceLightning(source);
			lightning.setDifficultyScaled();
			return lightning;
		case MAGIC:
			return DamageSource.causeIndirectMagicDamage(source, source);
		case WITHER:
			return causeEntityWitherDamage(source);
		case THORNS:
			return causeEntityThornsDamage(source);
		case CACTUS:
			return causeEntityCactusDamage(source);
		case PHYSICAL:
		default:
			return DamageSource.causeMobDamage(source);
		}
	}

	public static DamageSource causeDamage(DamageSourceTypes type, EntityLivingBase source, boolean unblockable){
		DamageSource ds = causeDamage(type, source);
		if (unblockable)
			setDamageSourceUnblockable(ds);
		return ds;
	}

	public static DamageSource causeEntityWitherDamage(EntityLivingBase source){
		return (new EntityDamageSource("wither", source));
	}

	public static DamageSource causeEntityThornsDamage(EntityLivingBase source){
		return (new EntityDamageSource("thorns", source));
	}

	public static DamageSource causeEntityCactusDamage(EntityLivingBase source){
		return (new EntityDamageSource("cactus", source));
	}

	public static DamageSource causeEntityMagicDamage(EntityLivingBase source){
		DamageSource ds = new EntityDamageSource("magic", source);
		ds.setMagicDamage();
		setDamageSourceUnblockable(ds);
		return ds;
	}

	public static DamageSource causeEntityPhysicalDamage(Entity source){
		if (source instanceof EntityPlayer)
			return (new EntityDamageSource("player", source));
		return (new EntityDamageSource("mob", source));
	}

	public static DamageSource causeEntityWitherDamage(EntityLivingBase source, boolean unblockable){
		DamageSource ds = causeEntityWitherDamage(source);
		if (unblockable)
			setDamageSourceUnblockable(ds);
		return ds;
	}

	public static DamageSource causeEntityThornsDamage(EntityLivingBase source, boolean unblockable){
		DamageSource ds = causeEntityThornsDamage(source);
		if (unblockable)
			setDamageSourceUnblockable(ds);
		return ds;
	}

	public static DamageSource causeEntityCactusDamage(EntityLivingBase source, boolean unblockable){
		DamageSource ds = causeEntityCactusDamage(source);
		if (unblockable)
			setDamageSourceUnblockable(ds);
		return ds;
	}

	public static DamageSource setDamageSourceUnblockable(DamageSource original){
		ReflectionHelper.setPrivateValue(DamageSource.class, original, true, "field_76374_o", "isUnblockable");
		return original;
	}

	public static DamageSource causeEntityDrownDamage(EntityLivingBase source){
		return (new EntityDamageSource("drown", source));
	}


}
