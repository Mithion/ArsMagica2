package am2.entities;

import am2.AMCore;
import am2.api.entities.IEntityManager;
import am2.bosses.*;
import am2.bosses.renderers.*;
import am2.entities.models.ModelBattleChicken;
import am2.entities.models.ModelHecate;
import am2.entities.renderers.*;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class EntityManager implements IEntityManager{

	public static final EntityManager instance = new EntityManager();

	private EntityManager(){
	}

	public String WispMobID = "MobWisp";
	public String ManaElemMobID = "MobManaElemental";
	public String MageVillagerMobID = "MobMageVillager";
	public String HecateMobID = "MobHecate";
	public String ManaCreeperMobID = "MobManaCreeper";
	public String DryadMobID = "MobDryad";
	public String LightMageMobID = "MobLightMage";
	public String DarkMageMobID = "MobDarkMage";
	public String SummonedSkeletonMobID = "SummonedSkeleton";
	public String SummonedLichMobID = "SummonedLich";
	public String EarthGolemMobID = "EarthElemental";
	public String SummonedBattleChickenMobID = "BattleChicken";
	public String InsectSwarmMobID = "InsectSwarm";
	public String ZoneSpellID = "ZoneSpell";
	public String WaterElementalMobID = "MobWaterElemental";
	public String FireElementalMobID = "MobFireElemental";
	public String SummonedShadowMobID = "SummonedShadow";
	public String ManaVortexID = "ManaVortex";
	public String GatewayPortalID = "GatewayPortal";
	public String SpellProjectileID = "SpellProjectile";
	public String DarklingID = "MobDarkling";
	public String RiftStorageID = "RiftStorage";
	public String WhirlwindID = "Whirlwind";
	public String ShockwaveID = "Shockwave";
	public String HellCowID = "HellCow";
	public String BroomID = "DaBroom";
	public String AirSledID = "AirSled";
	public String FlickerID = "Flicker";
	public String ShadowHelperID = "ShadowHelper";

	public String NatureGuardianMobID = "BossNatureGuardian";
	public String ArcaneGuardianMobID = "BossArcaneGuardian";
	public String EarthGuardianMobID = "BossEarthGuardian";
	public String WaterGuardianMobID = "BossWaterGuardian";
	public String WinterGuardianMobID = "BossWinterGuardian";
	public String AirGuardianMobID = "BossAirGuardian";
	public String FireGuardianMobID = "BossFireGuardian";
	public String LifeGuardianMobID = "BossLifeGuardian";
	public String LightningGuardianMobID = "BossLightningGuardian";
	public String EnderGuardianMobID = "BossEnderGuardian";

	public String ThrownSickleID = "ThrownSickle";
	public String ThrownRockID = "ThrownRock";
	public String ThrownArmID = "ThrownArm";

	private static final SpawnListEntry flickerSpawns = new SpawnListEntry(EntityFlicker.class, 3, 2, 4);

	public void registerEntities(){

		int updateFrequency = 2;
		int updateDistance = 64;
		boolean updateVelocity = true;

		EntityRegistry.registerModEntity(EntityEarthElemental.class, EarthGolemMobID, 119, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityFireElemental.class, FireElementalMobID, 117, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityBattleChicken.class, SummonedBattleChickenMobID, 113, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityDryad.class, DryadMobID, 112, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, SpellProjectileID, 111, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityHecate.class, HecateMobID, 110, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityManaElemental.class, ManaElemMobID, 109, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityManaCreeper.class, ManaCreeperMobID, 108, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityManaVortex.class, ManaVortexID, 107, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityWaterElemental.class, WaterElementalMobID, 106, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityLightMage.class, LightMageMobID, 105, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityDarkMage.class, DarkMageMobID, 104, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntitySpellEffect.class, ZoneSpellID, 101, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityMageVillager.class, MageVillagerMobID, 100, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityDarkling.class, DarklingID, 99, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityRiftStorage.class, RiftStorageID, 98, AMCore.instance, updateDistance, updateFrequency, false);
		EntityRegistry.registerModEntity(EntityNatureGuardian.class, NatureGuardianMobID, 97, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityThrownSickle.class, ThrownSickleID, 96, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityArcaneGuardian.class, ArcaneGuardianMobID, 95, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityEarthGuardian.class, EarthGuardianMobID, 94, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityThrownRock.class, ThrownRockID, 93, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityWaterGuardian.class, WaterGuardianMobID, 92, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityWinterGuardian.class, WinterGuardianMobID, 91, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityWinterGuardianArm.class, ThrownArmID, 90, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityAirGuardian.class, AirGuardianMobID, 89, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityFireGuardian.class, FireGuardianMobID, 88, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityWhirlwind.class, WhirlwindID, 87, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityShockwave.class, ShockwaveID, 86, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityHellCow.class, HellCowID, 85, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityBroom.class, BroomID, 84, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityAirSled.class, AirSledID, 83, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityLifeGuardian.class, LifeGuardianMobID, 82, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityFlicker.class, FlickerID, 81, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityLightningGuardian.class, LightningGuardianMobID, 80, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityEnderGuardian.class, EnderGuardianMobID, 79, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
		EntityRegistry.registerModEntity(EntityShadowHelper.class, ShadowHelperID, 78, AMCore.instance, updateDistance, updateFrequency, updateVelocity);
	}

	@SideOnly(Side.CLIENT)
	public void registerRenderInformation(){
		RenderingRegistry.registerEntityRenderingHandler(EntityEarthElemental.class, new RenderEarthElemental());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireElemental.class, new RenderFireElemental());
		//RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, new RenderWisp(new ModelWisp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBattleChicken.class, new RenderBattleChicken(new ModelBattleChicken(), 0.5f));
		RenderingRegistry.registerEntityRenderingHandler(EntityHecate.class, new RenderHecate(new ModelHecate(), 0.5f));
		RenderingRegistry.registerEntityRenderingHandler(EntityManaElemental.class, new RenderManaElemental());
		RenderingRegistry.registerEntityRenderingHandler(EntityManaVortex.class, new RenderManaVortex());
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterElemental.class, new RenderWaterElemental());

		RenderingRegistry.registerEntityRenderingHandler(EntityLightMage.class, new RenderMage());
		RenderingRegistry.registerEntityRenderingHandler(EntityDarkMage.class, new RenderMage());
		RenderingRegistry.registerEntityRenderingHandler(EntityMageVillager.class, new RenderMageWizard());
		RenderingRegistry.registerEntityRenderingHandler(EntityRiftStorage.class, new RenderRiftStorage());
		RenderingRegistry.registerEntityRenderingHandler(EntityManaCreeper.class, new RenderManaCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderSpellProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellEffect.class, new RenderHidden());
		RenderingRegistry.registerEntityRenderingHandler(EntityDryad.class, new RenderDryad());

		RenderingRegistry.registerEntityRenderingHandler(EntityNatureGuardian.class, new RenderPlantGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityArcaneGuardian.class, new RenderArcaneGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityEarthGuardian.class, new RenderEarthGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterGuardian.class, new RenderWaterGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityWinterGuardian.class, new RenderIceGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityAirGuardian.class, new RenderAirGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireGuardian.class, new RenderFireGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningGuardian.class, new RenderLightningGuardian());

		RenderingRegistry.registerEntityRenderingHandler(EntityThrownSickle.class, new RenderThrownSickle());
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownRock.class, new RenderThrownRock());
		RenderingRegistry.registerEntityRenderingHandler(EntityWinterGuardianArm.class, new RenderWinterGuardianArm());

		RenderingRegistry.registerEntityRenderingHandler(EntityWhirlwind.class, new RenderWhirlwind());
		RenderingRegistry.registerEntityRenderingHandler(EntityShockwave.class, new RenderHidden());

		RenderingRegistry.registerEntityRenderingHandler(EntityHellCow.class, new RenderHellCow());
		RenderingRegistry.registerEntityRenderingHandler(EntityDarkling.class, new RenderDarkling());
		RenderingRegistry.registerEntityRenderingHandler(EntityBroom.class, new RenderBroom());
		RenderingRegistry.registerEntityRenderingHandler(EntityAirSled.class, new RenderAirSled());

		RenderingRegistry.registerEntityRenderingHandler(EntityLifeGuardian.class, new RenderLifeGuardian());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlicker.class, new RenderFlicker());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderGuardian.class, new RenderEnderGuardian());

		RenderingRegistry.registerEntityRenderingHandler(EntityShadowHelper.class, new RenderShadowHelper());
	}

	public void initializeSpawns(){
		BiomeDictionary.registerAllBiomes();

		//SpawnListEntry wisps = new SpawnListEntry(EntityWisp.class, 1, 1, 1);
		SpawnListEntry manaElementals = new SpawnListEntry(EntityManaElemental.class, AMCore.config.GetManaElementalSpawnRate(), 1, 1);
		SpawnListEntry dryads = new SpawnListEntry(EntityDryad.class, AMCore.config.GetDryadSpawnRate(), 1, 2);
		SpawnListEntry hecates_nonHell = new SpawnListEntry(EntityHecate.class, AMCore.config.GetHecateSpawnRate(), 1, 1);
		SpawnListEntry hecates_hell = new SpawnListEntry(EntityHecate.class, AMCore.config.GetHecateSpawnRate() * 2, 1, 2);
		SpawnListEntry manaCreepers = new SpawnListEntry(EntityManaCreeper.class, AMCore.config.GetManaCreeperSpawnRate(), 1, 1);
		SpawnListEntry lightMages = new SpawnListEntry(EntityLightMage.class, AMCore.config.GetMageSpawnRate(), 1, 3);
		SpawnListEntry darkMages = new SpawnListEntry(EntityDarkMage.class, AMCore.config.GetMageSpawnRate(), 1, 3);
		SpawnListEntry waterElementals = new SpawnListEntry(EntityWaterElemental.class, AMCore.config.GetWaterElementalSpawnRate(), 1, 3);
		SpawnListEntry darklings = new SpawnListEntry(EntityDarkling.class, AMCore.config.GetDarklingSpawnRate(), 4, 8);
		SpawnListEntry earthElementals = new SpawnListEntry(EntityEarthElemental.class, AMCore.config.GetEarthElementalSpawnRate(), 1, 2);
		SpawnListEntry fireElementals = new SpawnListEntry(EntityFireElemental.class, AMCore.config.GetFireElementalSpawnRate(), 1, 1);

		initSpawnsForBiomeTypes(manaElementals, EnumCreatureType.monster, new Type[]{Type.BEACH, Type.DESERT, Type.FOREST, Type.FROZEN, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(dryads, EnumCreatureType.creature, new Type[]{Type.BEACH, Type.FOREST, Type.MAGICAL, Type.HILLS, Type.JUNGLE, Type.MOUNTAIN, Type.PLAINS}, new Type[]{Type.END, Type.FROZEN, Type.MUSHROOM, Type.NETHER, Type.WASTELAND, Type.SWAMP, Type.DESERT});

		initSpawnsForBiomeTypes(hecates_nonHell, EnumCreatureType.monster, new Type[]{Type.BEACH, Type.DESERT, Type.FOREST, Type.FROZEN, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(hecates_hell, EnumCreatureType.monster, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

		initSpawnsForBiomeTypes(darklings, EnumCreatureType.monster, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

		initSpawnsForBiomeTypes(manaCreepers, EnumCreatureType.monster, new Type[]{Type.BEACH, Type.DESERT, Type.FOREST, Type.FROZEN, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(lightMages, EnumCreatureType.monster, new Type[]{Type.BEACH, Type.DESERT, Type.FOREST, Type.FROZEN, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(darkMages, EnumCreatureType.monster, new Type[]{Type.BEACH, Type.DESERT, Type.FOREST, Type.FROZEN, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(waterElementals, EnumCreatureType.monster, new Type[]{Type.WATER}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});
		initSpawnsForBiomeTypes(waterElementals, EnumCreatureType.waterCreature, new Type[]{Type.WATER}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(earthElementals, EnumCreatureType.monster, new Type[]{Type.HILLS, Type.MOUNTAIN}, new Type[]{Type.MUSHROOM});
		initSpawnsForBiomeTypes(fireElementals, EnumCreatureType.monster, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

	}

	private void initSpawnsForBiomeTypes(SpawnListEntry spawnListEntry, EnumCreatureType creatureType, Type[] types, Type[] exclusions){
		if (spawnListEntry.itemWeight == 0){
			FMLLog.info("Ars Magica 2 >> Skipping spawn list entry for %s (as type %s), as the weight is set to 0.  This can be changed in config.", spawnListEntry.entityClass.getName(), creatureType.toString());
			return;
		}
		for (Type type : types){
			initSpawnsForBiomes(BiomeDictionary.getBiomesForType(type), spawnListEntry, creatureType, exclusions);
		}
	}

	private void initSpawnsForBiomes(BiomeGenBase[] biomes, SpawnListEntry spawnListEntry, EnumCreatureType creatureType, Type[] exclusions){
		if (biomes == null) return;
		for (BiomeGenBase biome : biomes){
			if (biomeIsExcluded(biome, exclusions)) continue;
			if (!biome.getSpawnableList(creatureType).contains(spawnListEntry))
				biome.getSpawnableList(creatureType).add(spawnListEntry);
		}
	}

	private boolean biomeIsExcluded(BiomeGenBase biome, Type[] exclusions){

		Type biomeTypes[] = BiomeDictionary.getTypesForBiome(biome);

		for (Type exclusion : exclusions){
			for (Type biomeType : biomeTypes){
				if (biomeType == exclusion) return true;
			}
		}
		return false;
	}

	@Override
	public void addButcheryBlacklist(Class... clazz){
		for (Class l_clazz : clazz)
			SpawnBlacklists.addButcheryBlacklist(l_clazz);
	}

	@Override
	public void addProgenyBlacklist(Class... clazz){
		for (Class l_clazz : clazz)
			SpawnBlacklists.addProgenyBlacklist(l_clazz);
	}
}
