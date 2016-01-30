package am2.utility;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.blocks.tileentities.TileEntitySummoner;
import am2.buffs.BuffList;
import am2.entities.ai.EntityAIGuardSpawnLocation;
import am2.entities.ai.EntityAISummonFollowOwner;
import am2.entities.ai.selectors.SummonEntitySelector;
import am2.items.ItemCrystalPhylactery;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityUtilities{
	private static final HashMap<Integer, ArrayList> storedTasks = new HashMap<Integer, ArrayList>();
	private static final HashMap<Integer, ArrayList> storedAITasks = new HashMap<Integer, ArrayList>();
	private static final String isSummonKey = "AM2_Entity_Is_Made_Summon";
	private static final String summonEntityIDs = "AM2_Summon_Entity_IDs";
	private static final String summonDurationKey = "AM2_Summon_Duration";
	private static final String summonOwnerKey = "AM2_Summon_Owner";
	private static final String summonTileXKey = "AM2_Summon_Tile_X";
	private static final String summonTileYKey = "AM2_Summon_Tile_Y";
	private static final String summonTileZKey = "AM2_Summon_Tile_Z";

	private static Method ptrSetSize = null;

	public static boolean isAIEnabled(EntityCreature ent){
		Method m = null;
		try{
			m = EntityLiving.class.getDeclaredMethod("func_70650_aV");
		}catch (NoSuchMethodException nex){
			try{
				m = EntityLiving.class.getDeclaredMethod("isAIEnabled");
			}catch (Throwable t){
				t.printStackTrace();
				return false;
			}
		}catch (Throwable t){
			t.printStackTrace();
			return false;
		}
		m.setAccessible(true);
		boolean b;
		try{
			b = (Boolean)m.invoke(ent);
		}catch (Throwable e){
			e.printStackTrace();
			return false;
		}
		m.setAccessible(false);
		return b;
	}

	public static void handleCrystalPhialAdd(EntityCreature entityliving, EntityPlayer player){
		for (ItemStack stack : player.inventory.mainInventory){
			if (stack != null && stack.getItem() instanceof ItemCrystalPhylactery){
				if (ItemsCommonProxy.crystalPhylactery.canStore(stack, entityliving)){
					ItemsCommonProxy.crystalPhylactery.setSpawnClass(stack, entityliving.getClass());
					ItemsCommonProxy.crystalPhylactery.addFill(stack);
					return;
				}
			}
		}
	}

	public static void makeSummon_PlayerFaction(EntityCreature entityliving, EntityPlayer player, boolean storeForRevert){
		if (isAIEnabled(entityliving) && !(entityliving instanceof IBossDisplayData) && ExtendedProperties.For(player).getCanHaveMoreSummons()){
			if (storeForRevert)
				storedTasks.put(entityliving.getEntityId(), new ArrayList(entityliving.targetTasks.taskEntries));

			boolean addMeleeAttack = false;
			ArrayList<EntityAITaskEntry> toRemove = new ArrayList<EntityAITaskEntry>();
			for (Object task : entityliving.tasks.taskEntries){
				EntityAITaskEntry base = (EntityAITaskEntry)task;
				if (base.action instanceof EntityAIAttackOnCollide){
					toRemove.add(base);
					addMeleeAttack = true;
				}
			}

			entityliving.tasks.taskEntries.removeAll(toRemove);

			if (storeForRevert)
				storedAITasks.put(entityliving.getEntityId(), toRemove);

			if (addMeleeAttack){
				float speed = entityliving.getAIMoveSpeed();
				if (speed <= 0) speed = 1.0f;
				entityliving.tasks.addTask(3, new EntityAIAttackOnCollide(entityliving, EntityMob.class, speed, true));
				entityliving.tasks.addTask(3, new EntityAIAttackOnCollide(entityliving, IMob.class, speed, true));
				entityliving.tasks.addTask(3, new EntityAIAttackOnCollide(entityliving, EntitySlime.class, speed, true));
			}

			entityliving.targetTasks.taskEntries.clear();

			entityliving.targetTasks.addTask(1, new EntityAIHurtByTarget(entityliving, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entityliving, EntityMob.class, 0, true, false, SummonEntitySelector.instance));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entityliving, EntitySlime.class, 0, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entityliving, EntityGhast.class, 0, true));

			if (!entityliving.worldObj.isRemote && entityliving.getAttackTarget() != null && entityliving.getAttackTarget() instanceof EntityPlayer)
				AMCore.proxy.addDeferredTargetSet(entityliving, null);

			if (entityliving instanceof EntityTameable){
				((EntityTameable)entityliving).setTamed(true);
				((EntityTameable)entityliving).func_152115_b(player.getCommandSenderName());
			}

			entityliving.getEntityData().setBoolean(isSummonKey, true);
			ExtendedProperties.For(player).addSummon(entityliving);
		}
	}

	public static void makeSummon_MonsterFaction(EntityCreature entityliving, boolean storeForRevert){
		if (isAIEnabled(entityliving) && !(entityliving instanceof IBossDisplayData)){
			if (storeForRevert)
				storedTasks.put(entityliving.getEntityId(), new ArrayList(entityliving.targetTasks.taskEntries));
			entityliving.targetTasks.taskEntries.clear();
			entityliving.targetTasks.addTask(1, new EntityAIHurtByTarget(entityliving, true));
			entityliving.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entityliving, EntityPlayer.class, 0, true));
			if (!entityliving.worldObj.isRemote && entityliving.getAttackTarget() != null && entityliving.getAttackTarget() instanceof EntityMob)
				AMCore.proxy.addDeferredTargetSet(entityliving, null);

			entityliving.getEntityData().setBoolean(isSummonKey, true);
		}
	}

	public static boolean revertAI(EntityCreature entityliving){

		int ownerID = getOwner(entityliving);
		Entity owner = entityliving.worldObj.getEntityByID(ownerID);
		if (owner != null && owner instanceof EntityLivingBase){
			ExtendedProperties.For((EntityLivingBase)owner).removeSummon();
			if (ExtendedProperties.For((EntityLivingBase)owner).isManaLinkedTo(entityliving)){
				ExtendedProperties.For((EntityLivingBase)owner).updateManaLink(entityliving);
				ExtendedProperties.For((EntityLivingBase)owner).forceSync();
			}
		}

		entityliving.getEntityData().setBoolean(isSummonKey, false);
		setOwner(entityliving, null);

		if (storedTasks.containsKey(entityliving.getEntityId())){
			entityliving.targetTasks.taskEntries.clear();
			entityliving.targetTasks.taskEntries.addAll(storedTasks.get(entityliving.getEntityId()));
			storedTasks.remove(entityliving.getEntityId());

			if (storedAITasks.get(entityliving.getEntityId()) != null){
				ArrayList<EntityAITaskEntry> toRemove = new ArrayList<EntityAITaskEntry>();
				for (Object task : entityliving.tasks.taskEntries){
					EntityAITaskEntry base = (EntityAITaskEntry)task;
					if (base.action instanceof EntityAIAttackOnCollide || base.action instanceof EntityAISummonFollowOwner){
						toRemove.add(base);
					}
				}

				entityliving.tasks.taskEntries.removeAll(toRemove);

				entityliving.tasks.taskEntries.addAll(storedAITasks.get(entityliving.getEntityId()));
				storedAITasks.remove(entityliving.getEntityId());
			}
			if (!entityliving.worldObj.isRemote && entityliving.getAttackTarget() != null)
				AMCore.proxy.addDeferredTargetSet(entityliving, null);
			if (entityliving instanceof EntityTameable){
				((EntityTameable)entityliving).setTamed(false);
			}
			return true;
		}

		return false;
	}

	public static boolean isSummon(EntityLivingBase entityliving){
		return entityliving.getEntityData().getBoolean(isSummonKey);
	}

	public static void setOwner(EntityLivingBase entityliving, EntityLivingBase owner){
		if (owner == null){
			entityliving.getEntityData().removeTag(summonOwnerKey);
			return;
		}

		entityliving.getEntityData().setInteger(summonOwnerKey, owner.getEntityId());
		if (entityliving instanceof EntityCreature){
			float speed = entityliving.getAIMoveSpeed();
			if (speed <= 0) speed = 1.0f;
			((EntityCreature)entityliving).tasks.addTask(1, new EntityAISummonFollowOwner((EntityCreature)entityliving, speed, 10, 20));
		}
	}

	public static void setTileSpawned(EntityLivingBase entityliving, TileEntitySummoner summoner){
		entityliving.getEntityData().setInteger(summonTileXKey, summoner.xCoord);
		entityliving.getEntityData().setInteger(summonTileYKey, summoner.yCoord);
		entityliving.getEntityData().setInteger(summonTileZKey, summoner.zCoord);
	}

	public static boolean isTileSpawnedAndValid(EntityLivingBase entityliving){
		Integer tileX = entityliving.getEntityData().getInteger(summonTileXKey);
		Integer tileY = entityliving.getEntityData().getInteger(summonTileYKey);
		Integer tileZ = entityliving.getEntityData().getInteger(summonTileZKey);
		if (tileX == null || tileY == null || tileZ == null) return false;

		TileEntity te = entityliving.worldObj.getTileEntity(tileX, tileY, tileZ);

		return te != null && te instanceof TileEntitySummoner;
	}

	public static void setGuardSpawnLocation(EntityCreature entity, double x, double y, double z){
		float speed = entity.getAIMoveSpeed();
		if (speed <= 0) speed = 1.0f;
		entity.tasks.addTask(1, new EntityAIGuardSpawnLocation(entity, speed, 3, 16, new AMVector3(x, y, z)));
	}

	public static int getOwner(EntityLivingBase entityliving){
		if (!isSummon(entityliving)) return -1;
		Integer ownerID = entityliving.getEntityData().getInteger(summonOwnerKey);
		return ownerID == null ? -1 : ownerID;
	}

	public static float[] getSize(EntityLivingBase entityliving){
		float[] ret = new float[2];
		ret[0] = ReflectionHelper.getPrivateValue(Entity.class, entityliving, "field_70130_N", "width");
		ret[1] = ReflectionHelper.getPrivateValue(Entity.class, entityliving, "field_70131_O", "height");
		return ret;
	}

	public static void setSize(EntityLivingBase entityliving, float width, float height){
		if (entityliving.width == width && entityliving.height == height)
			return;
		if (ptrSetSize == null){
			try{
				ptrSetSize = ReflectionHelper.findMethod(Entity.class, entityliving, new String[]{"func_70105_a", "setSize"}, Float.TYPE, Float.TYPE);
			}catch (Throwable t){
				t.printStackTrace();
				return;
			}
		}
		if (ptrSetSize != null){
			try{
				ptrSetSize.setAccessible(true);
				ptrSetSize.invoke(entityliving, width, height);
				entityliving.yOffset = entityliving.height * 0.8f;
			}catch (Throwable t){
				t.printStackTrace();
				return;
			}
		}
	}

	public static void setSummonDuration(EntityLivingBase entity, int duration){
		entity.getEntityData().setInteger(summonDurationKey, duration);
	}

	/**
	 * If returns true, the summon has more duration and can continue to exist
	 */
	public static boolean decrementSummonDuration(EntityLivingBase ent){
		if (!isTileSpawnedAndValid(ent)){
			int duration = ent.getEntityData().getInteger(summonDurationKey);
			if (duration == -1)
				return true;
			duration--;
			setSummonDuration(ent, duration);
			return duration > 0;
		}else{
			return true;
		}

	}

	public static int getRuneCombo(EntityPlayer player){
		int hash = player.getCommandSenderName().toLowerCase().hashCode();
		return hash & 0xFFFF;
	}

	public static EntityPlayer getPlayerForCombo(World world, int combo){
		if (world.isRemote)
			return null;
		combo &= 0xFFFF; //convert to a valid key; drop all other bits.
		for (WorldServer ws : MinecraftServer.getServer().worldServers){
			for (Object o : ws.playerEntities){
				EntityPlayer player = (EntityPlayer)o;
				if (getRuneCombo(player) == combo)
					return player;
			}
		}
		return null;
	}

	public static int getLevelFromXP(float totalXP){
		int level = 0;
		int xp = (int)Math.floor(totalXP);
		do{
			int cap = xpBarCap(level);
			xp -= cap;
			if (xp < 0)
				break;
			level++;
		}while (true);

		return level;
	}

	public static int getXPFromLevel(int level){
		int totalXP = 0;
		for(int i = 0; i < level; i++){
			totalXP += xpBarCap(i);
		}
		return totalXP;
	}

	public static int xpBarCap(int experienceLevel){
		return experienceLevel >= 30 ? 62 + (experienceLevel - 30) * 7 : (experienceLevel >= 15 ? 17 + (experienceLevel - 15) * 3 : 17);
	}

	public static int deductXP(int amount, EntityPlayer player){
		// the problem with the enchanting table is that it directly "adds" experience levels
		// doing it like this does not update experienceTotal
		// we therefore need to go and calculate an effective total ourselves
		int effectiveTotal = getXPFromLevel(player.experienceLevel) + (int)(player.experience * (float)xpBarCap(player.experienceLevel));
		
		// since we already calculate the player's total XP here,
		// we may as well do the zero check and pass the result (how much was actually deducted) back
		// there's no sense in duplicating work
		int removedXP = effectiveTotal >= amount ? amount : effectiveTotal;
		
		int newTotal = effectiveTotal - amount;
		if(newTotal < 0)
			newTotal = 0;
		
		player.experience = 0.0F;
		player.experienceLevel = 0;
		player.experienceTotal -= removedXP;
		if(player.experienceTotal < 0)
			player.experienceTotal = 0;
		player.addExperience(newTotal);
		
		return removedXP;
	}

	public static float modifySoundPitch(Entity e, float p){
		if (e instanceof EntityLivingBase && ((EntityLivingBase)e).isPotionActive(BuffList.shrink)){
			return p + ExtendedProperties.For((EntityLivingBase)e).getShrinkPct();
		}
		return p;
	}
}
