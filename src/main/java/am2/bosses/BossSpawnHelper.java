package am2.bosses;

import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityLectern;
import am2.entities.EntityDryad;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.HashMap;
import java.util.List;

public class BossSpawnHelper{
	public int dryadsKilled;
	public int ticksSinceLastDryadDeath;

	public static final BossSpawnHelper instance = new BossSpawnHelper();

	private final HashMap<EntityLivingBase, World> queuedBosses;

	private BossSpawnHelper(){
		queuedBosses = new HashMap<EntityLivingBase, World>();
	}

	public void onDryadKilled(EntityDryad dryad){
		ticksSinceLastDryadDeath = 0;
		dryadsKilled++;
		if (dryadsKilled >= 5){
			spawnNatureGuardian(dryad.worldObj, dryad.posX, dryad.posY, dryad.posZ);
			dryadsKilled = 0;
		}
	}

	public void onVillagerChildKilled(EntityVillager villager){
		int x = (int)Math.floor(villager.posX);
		int y = (int)Math.floor(villager.posY);
		int z = (int)Math.floor(villager.posZ);

		World world = villager.worldObj;

		long time = world.getWorldTime() % 24000;
		if (time < 12500 || time > 23500) //night time
			return;

		int phase = getMoonPhaseProxiedProperly(world.provider.getWorldTime());
		if (phase != 0) // full moon
			return;

		//initial validation
		if (!world.isAirBlock(x, y, z))
			return;

		//shift to center of circle
		if (world.getBlock(x - 1, y, z) == BlocksCommonProxy.wizardChalk){
			x++;
		}
		if (world.getBlock(x + 1, y, z) == BlocksCommonProxy.wizardChalk){
			x--;
		}
		if (world.getBlock(x, y, z + 1) == BlocksCommonProxy.wizardChalk){
			z--;
		}
		if (world.getBlock(x, y, z - 1) == BlocksCommonProxy.wizardChalk){
			z++;
		}

		if (!chalkCircleIsValid(world, x, y, z))
			return;

		if (!world.isRemote){
			EntityLifeGuardian guardian = new EntityLifeGuardian(world);
			guardian.setPosition(x, y, z);
			queuedBosses.put(guardian, world);
		}
	}

	private int getMoonPhaseProxiedProperly(long worldTime){
		return (int)(worldTime / 24000L) % 8;
	}

	private boolean chalkCircleIsValid(World world, int x, int y, int z){
		//check for candles
		if (world.getBlock(x - 3, y, z) != BlocksCommonProxy.candle)
			return false;
		if (world.getBlock(x + 3, y, z) != BlocksCommonProxy.candle)
			return false;
		if (world.getBlock(x, y, z - 3) != BlocksCommonProxy.candle)
			return false;
		if (world.getBlock(x, y, z + 3) != BlocksCommonProxy.candle)
			return false;

		//check for chalk circle
		int xOff = -2;
		int zOff = -2;
		while (xOff <= 2)
			if (world.getBlock(x + xOff++, y, z + zOff) != BlocksCommonProxy.wizardChalk)
				return false;
		xOff--;
		while (zOff <= 2)
			if (world.getBlock(x + xOff, y, z + zOff++) != BlocksCommonProxy.wizardChalk)
				return false;
		zOff--;
		while (xOff >= -2)
			if (world.getBlock(x + xOff--, y, z + zOff) != BlocksCommonProxy.wizardChalk)
				return false;
		xOff++;
		while (zOff >= -2)
			if (world.getBlock(x + xOff, y, z + zOff--) != BlocksCommonProxy.wizardChalk)
				return false;

		return true;
	}

	private void spawnNatureGuardian(World world, double x, double y, double z){
		if (!world.isRemote){
			EntityNatureGuardian eng = new EntityNatureGuardian(world);
			eng.setPosition(x, y, z);
			queuedBosses.put(eng, world);
		}
	}

	public void tick(){
		ticksSinceLastDryadDeath++;
		if (ticksSinceLastDryadDeath >= 400){
			ticksSinceLastDryadDeath = 0;
			dryadsKilled = 0;
		}

		for (EntityLivingBase ent : queuedBosses.keySet()){
			World world = queuedBosses.get(ent);
			if (!world.isRemote){
				world.spawnEntityInWorld(ent);
				onBossSpawn(ent, world, (int)Math.floor(ent.posX), (int)Math.floor(ent.posY), (int)Math.floor(ent.posZ));
			}
		}
		queuedBosses.clear();
	}

	public void onItemInRing(EntityItem item, Block ringID){
		if (ringID == BlocksCommonProxy.redstoneInlay){
			checkForWaterGuardianSpawn(item.worldObj, (int)Math.floor(item.posX), (int)Math.floor(item.posY), (int)Math.floor(item.posZ));
		}else if (ringID == BlocksCommonProxy.ironInlay){
			checkForArcaneGuardianSpawn(item.worldObj, (int)Math.floor(item.posX), (int)Math.floor(item.posY), (int)Math.floor(item.posZ));
			checkForEarthGuardianSpawn(item.worldObj, (int)Math.floor(item.posX), (int)Math.floor(item.posY), (int)Math.floor(item.posZ));
		}else if (ringID == BlocksCommonProxy.goldInlay){
			checkForAirGuardianSpawn(item.worldObj, (int)Math.floor(item.posX), (int)Math.floor(item.posY), (int)Math.floor(item.posZ));
			checkForFireGuardianSpawn(item, item.worldObj, (int)Math.floor(item.posX), (int)Math.floor(item.posY), (int)Math.floor(item.posZ));
			checkForEnderGuardianSpawn(item.worldObj, (int)Math.floor(item.posX), (int)Math.floor(item.posY), (int)Math.floor(item.posZ));
		}
	}

	private void checkForWaterGuardianSpawn(World world, int x, int y, int z){

		if (!world.isRaining()) return;

		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		Type[] types = BiomeDictionary.getTypesForBiome(biome);

		boolean containsWaterType = false;

		for (Type type : types){
			if (type == Type.WATER || type == Type.SWAMP || type == Type.BEACH || type == Type.OCEAN || type == Type.RIVER || type == Type.WET){
				containsWaterType = true;
				break;
			}
		}

		if (!containsWaterType) return;

		for (int i = -1; i <= 1; ++i)
			for (int j = -1; j <= 1; ++j)
				if (!world.canBlockSeeTheSky(x + i, y + 1, z + j))
					return;

		List<EntityItem> itemsInRange = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
		if (itemsInRange.size() != 2) return;
		boolean hasBucket = false;
		boolean hasBoat = false;

		for (EntityItem item : itemsInRange){
			if (item.isDead) continue;
			if (item.getEntityItem().getItem() == Items.boat)
				hasBoat = true;
			else if (item.getEntityItem().getItem() == Items.water_bucket)
				hasBucket = true;
		}

		if (hasBoat && hasBucket && !world.isRemote){
			for (EntityItem item : itemsInRange){
				item.setDead();
			}
			EntityWaterGuardian guardian = new EntityWaterGuardian(world);
			guardian.setPosition(x + 0.5, y + 1, z + 0.5);
			queuedBosses.put(guardian, world);
		}
	}

	private void checkForAirGuardianSpawn(World world, int x, int y, int z){
		if (y < 150) return;
		List<EntityItem> itemsInRange = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
		if (itemsInRange.size() != 1) return;
		if (itemsInRange.get(0).getEntityItem().getItem() != ItemsCommonProxy.essence || itemsInRange.get(0).getEntityItem().getItemDamage() != ItemsCommonProxy.essence.META_AIR)
			return;

		itemsInRange.get(0).setDead();
		EntityAirGuardian guardian = new EntityAirGuardian(world);
		guardian.setPosition(x + 0.5, y + 1, z + 0.5);
		queuedBosses.put(guardian, world);
	}

	private void checkForArcaneGuardianSpawn(World world, int x, int y, int z){
		List<EntityItem> itemsInRange = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
		if (itemsInRange.size() != 1) return;
		if (itemsInRange.get(0).getEntityItem().getItem() != ItemsCommonProxy.essence || itemsInRange.get(0).getEntityItem().getItemDamage() != ItemsCommonProxy.essence.META_ARCANE)
			return;
		boolean hasStructure = false;
		TileEntityLectern lectern = null;
		//+z check
		if (world.getBlock(x - 1, y, z + 2) == Blocks.bookshelf && world.getBlock(x - 1, y + 1, z + 2) == Blocks.bookshelf && world.getBlock(x - 1, y + 2, z + 2) == Blocks.bookshelf && world.getBlock(x + 1, y, z + 2) == Blocks.bookshelf && world.getBlock(x + 1, y + 1, z + 2) == Blocks.bookshelf && world.getBlock(x + 1, y + 2, z + 2) == Blocks.bookshelf){
			if (world.getBlock(x, y, z + 2) == BlocksCommonProxy.blockLectern){
				lectern = (TileEntityLectern)world.getTileEntity(x, y, z + 2);
				hasStructure = true;
			}
		}
		//-z check
		if (world.getBlock(x - 1, y, z - 2) == Blocks.bookshelf && world.getBlock(x - 1, y + 1, z - 2) == Blocks.bookshelf && world.getBlock(x - 1, y + 2, z - 2) == Blocks.bookshelf && world.getBlock(x + 1, y, z - 2) == Blocks.bookshelf && world.getBlock(x + 1, y + 1, z - 2) == Blocks.bookshelf && world.getBlock(x + 1, y + 2, z - 2) == Blocks.bookshelf){
			if (world.getBlock(x, y, z - 2) == BlocksCommonProxy.blockLectern){
				lectern = (TileEntityLectern)world.getTileEntity(x, y, z - 2);
				hasStructure = true;
			}
		}
		//+x check
		if (world.getBlock(x + 2, y, z - 1) == Blocks.bookshelf && world.getBlock(x + 2, y + 1, z - 1) == Blocks.bookshelf && world.getBlock(x + 2, y + 2, z - 1) == Blocks.bookshelf && world.getBlock(x + 2, y, z + 1) == Blocks.bookshelf && world.getBlock(x + 2, y + 1, z + 1) == Blocks.bookshelf && world.getBlock(x + 2, y + 2, z + 1) == Blocks.bookshelf){
			if (world.getBlock(x + 2, y, z) == BlocksCommonProxy.blockLectern){
				lectern = (TileEntityLectern)world.getTileEntity(x + 2, y, z);
				hasStructure = true;
			}
		}
		//-x check
		if (world.getBlock(x - 2, y, z - 1) == Blocks.bookshelf && world.getBlock(x - 2, y + 1, z - 1) == Blocks.bookshelf && world.getBlock(x - 2, y + 2, z - 1) == Blocks.bookshelf && world.getBlock(x - 2, y, z + 1) == Blocks.bookshelf && world.getBlock(x - 2, y + 1, z + 1) == Blocks.bookshelf && world.getBlock(x - 2, y + 2, z + 1) == Blocks.bookshelf){
			if (world.getBlock(x - 2, y, z) == BlocksCommonProxy.blockLectern){
				lectern = (TileEntityLectern)world.getTileEntity(x - 2, y, z);
				hasStructure = true;
			}
		}

		if (hasStructure && lectern != null && lectern.hasStack() && lectern.getStack().getItem() == ItemsCommonProxy.arcaneCompendium){
			itemsInRange.get(0).setDead();
			EntityArcaneGuardian guardian = new EntityArcaneGuardian(world);
			guardian.setPosition(x + 0.5, y + 1, z + 0.5);
			queuedBosses.put(guardian, world);
		}
	}

	private void checkForEarthGuardianSpawn(World world, int x, int y, int z){
		List<EntityItem> itemsInRange = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
		if (itemsInRange.size() != 3) return;
		boolean hasEmerald = false;
		boolean hasTopaz = false;
		boolean hasChimerite = false;
		boolean hasStructure = false;

		for (EntityItem item : itemsInRange){
			if (item.isDead) continue;
			if (item.getEntityItem().getItem() == Items.emerald)
				hasEmerald = true;
			else if (item.getEntityItem().getItem() == ItemsCommonProxy.itemOre && item.getEntityItem().getItemDamage() == ItemsCommonProxy.itemOre.META_BLUETOPAZ)
				hasTopaz = true;
			else if (item.getEntityItem().getItem() == ItemsCommonProxy.itemOre && item.getEntityItem().getItemDamage() == ItemsCommonProxy.itemOre.META_CHIMERITE)
				hasChimerite = true;
		}

		hasStructure = world.getBlock(x, y - 1, z) == Blocks.stonebrick && world.getBlockMetadata(x, y - 1, z) == 3;

		if (!hasStructure) return;

		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				if (i == 0 && j == 0) continue;
				hasStructure &= world.getBlock(x + i, y - 1, z + j) == Blocks.obsidian;
			}
		}

		hasStructure &= world.getBlock(x - 2, y, z) == BlocksCommonProxy.vinteumTorch;
		hasStructure &= world.getBlock(x + 2, y, z) == BlocksCommonProxy.vinteumTorch;
		hasStructure &= world.getBlock(x, y, z - 2) == BlocksCommonProxy.vinteumTorch;
		hasStructure &= world.getBlock(x, y, z + 2) == BlocksCommonProxy.vinteumTorch;

		if (!world.isRemote && hasEmerald && hasTopaz && hasChimerite && hasStructure){
			for (EntityItem item : itemsInRange){
				item.setDead();
			}

			EntityEarthGuardian guardian = new EntityEarthGuardian(world);
			guardian.setPosition(x + 0.5, y + 1, z + 0.5);
			queuedBosses.put(guardian, world);
		}
	}

	private void checkForFireGuardianSpawn(EntityItem item, World world, int x, int y, int z){
		if (item.getEntityItem().getItem() != ItemsCommonProxy.essence || item.getEntityItem().getItemDamage() != ItemsCommonProxy.essence.META_WATER)
			return;
		boolean hasStructure = false;
		boolean hasDimension = world.provider.dimensionId == -1;

		hasStructure = world.getBlock(x, y - 1, z) == Blocks.coal_block;

		if (!hasStructure) return;

		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				if (i == 0 && j == 0) continue;
				hasStructure &= world.getBlock(x + i, y - 1, z + j) == Blocks.obsidian;
			}
		}

		if (!world.isRemote && hasStructure && hasDimension){
			item.setDead();

			EntityFireGuardian guardian = new EntityFireGuardian(world);
			guardian.setPosition(x + 0.5, y + 1, z + 0.5);
			queuedBosses.put(guardian, world);
		}
	}

	private void onBossSpawn(EntityLivingBase boss, World world, int x, int y, int z){
		if (boss instanceof EntityEarthGuardian){
			if (world.getGameRules().getGameRuleBooleanValue("mobGriefing")){
				for (int i = -10; i <= 10; ++i){
					for (int j = 0; j <= 4; ++j){
						for (int k = -10; k <= 10; ++k){
							if (world.getBlock(x + i, y + j, z + k) != Blocks.bedrock)
								world.func_147478_e(x + i, y + j, z + k, true);
						}
					}
				}
			}
		}else if (boss instanceof EntityFireGuardian){
			for (int i = -20; i <= 20; ++i){
				for (int k = -20; k <= 20; ++k){
					Block block = world.getBlock(x + i, y - 1, z + k);
					if (block == Blocks.lava || block == Blocks.flowing_lava)
						world.setBlock(x + i, y - 1, z + k, Blocks.netherrack, 0, 2);
				}
			}
		}
	}

	public void onIceEffigyBuilt(World world, int x, int y, int z){
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		Type[] types = BiomeDictionary.getTypesForBiome(biome);

		boolean containsIceType = false;

		for (Type type : types){
			if (type == Type.COLD){
				containsIceType = true;
				break;
			}
		}

		if (!containsIceType) return;

		for (int i = -1; i <= 1; ++i)
			for (int j = -1; j <= 1; ++j)
				if (i == 0 && j == 0) continue;
				else if (!world.canBlockSeeTheSky(x + i, y + 1, z + j))
					return;

		world.setBlockToAir(x, y, z);
		world.setBlockToAir(x, y + 1, z);
		world.setBlockToAir(x, y + 2, z);

		EntityWinterGuardian guardian = new EntityWinterGuardian(world);
		guardian.setPosition(x + 0.5, y + 1, z + 0.5);
		world.spawnEntityInWorld(guardian);
	}

	public void onLightningEffigyBuilt(World world, int x, int y, int z){
		for (int i = -1; i <= 1; ++i)
			for (int j = -1; j <= 1; ++j)
				if (i == 0 && j == 0) continue;
				else if (!world.canBlockSeeTheSky(x + i, y + 1, z + j))
					return;

		world.setBlockToAir(x, y, z);
		world.setBlockToAir(x, y + 1, z);
		world.setBlockToAir(x, y + 2, z);

		EntityLightningGuardian guardian = new EntityLightningGuardian(world);
		guardian.setPosition(x + 0.5, y + 1, z + 0.5);
		world.spawnEntityInWorld(guardian);

		world.thunderingStrength = 1.0f;
	}

	public void checkForEnderGuardianSpawn(World world, int x, int y, int z){
		if (world.provider.dimensionId != 1) return;

		for (int i = -1; i <= 1; ++i)
			for (int j = -1; j <= 1; ++j)
				if (i == 0 && j == 0) continue;
				else if (!world.canBlockSeeTheSky(x + i, y + 1, z + j))
					return;

		List<EntityItem> itemsInRange = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));
		if (itemsInRange.size() != 2) return;

		boolean hasEnderEssence = false;
		boolean hasEyeofEnder = false;
		boolean hasStructure = false;

		for (EntityItem item : itemsInRange){
			if (item.isDead) continue;
			if (item.getEntityItem().getItem() == Items.ender_eye)
				hasEyeofEnder = true;
			else if (item.getEntityItem().getItem() == ItemsCommonProxy.essence && item.getEntityItem().getItemDamage() == ItemsCommonProxy.essence.META_ENDER)
				hasEnderEssence = true;
		}

		hasStructure = true;

		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				hasStructure &= world.getBlock(x + i, y - 1, z + j) == Blocks.coal_block;
			}
		}

		hasStructure &= world.getBlock(x - 2, y, z) == Blocks.fire;
		hasStructure &= world.getBlock(x + 2, y, z) == Blocks.fire;
		hasStructure &= world.getBlock(x, y, z - 2) == Blocks.fire;
		hasStructure &= world.getBlock(x, y, z + 2) == Blocks.fire;
		hasStructure &= world.getBlock(x, y, z) == BlocksCommonProxy.blackAurem;

		if (!hasStructure || !hasEnderEssence || !hasEyeofEnder)
			return;

		if (!world.isRemote){
			world.setBlockToAir(x - 2, y, z);
			world.setBlockToAir(x + 2, y, z);
			world.setBlockToAir(x, y, z - 2);
			world.setBlockToAir(x, y, z + 2);
			world.setBlockToAir(x, y, z);

			for (EntityItem item : itemsInRange){
				item.setDead();
			}

			EntityEnderGuardian guardian = new EntityEnderGuardian(world);
			guardian.setPosition(x + 0.5, y + 1, z + 0.5);
			world.spawnEntityInWorld(guardian);
		}
	}

}
