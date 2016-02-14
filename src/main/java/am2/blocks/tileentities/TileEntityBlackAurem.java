package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.blocks.MultiblockStructureDefinition.StructureGroup;
import am2.api.math.AMVector3;
import am2.api.power.PowerTypes;
import am2.blocks.BlocksCommonProxy;
import am2.buffs.BuffEffectManaRegen;
import am2.buffs.BuffList;
import am2.damage.DamageSources;
import am2.entities.*;
import am2.multiblock.IMultiblockStructureController;
import am2.particles.AMLineArc;
import am2.power.PowerNodeRegistry;
import am2.utility.EntityUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TileEntityBlackAurem extends TileEntityObelisk implements IMultiblockStructureController{

	private final HashMap arcs;

	private static final int GROUP_CHIMERITE = 0;
	private static final int GROUP_OBSIDIAN = 1;
	private static final int GROUP_SUNSTONE = 2;
	private final ArrayList<EntityLivingBase> cachedEntities;
	private int ticksSinceLastEntityScan = 0;
	private float rotation = 0.0f;
	private float rotationIncrement = 0.15f;

	public TileEntityBlackAurem(){
		super(10000);

		arcs = new HashMap();

		cachedEntities = new ArrayList<EntityLivingBase>();

		structure = new MultiblockStructureDefinition("blackaurem_structure");

		pillars = structure.createGroup("pillars", 4);

		caps = new HashMap<StructureGroup, Float>();
		StructureGroup chimerite = structure.createGroup("caps_chimerite", 2);
		StructureGroup obsidian = structure.createGroup("caps_obsidian", 2);
		StructureGroup sunstone = structure.createGroup("caps_sunstone", 2);
		caps.put(chimerite, 1.1f);
		caps.put(obsidian, 1.5f);
		caps.put(sunstone, 2f);

		structure.addAllowedBlock(0, 0, 0, BlocksCommonProxy.blackAurem);

		structure.addAllowedBlock(pillars, -2, 0, -2, Blocks.nether_brick);
		structure.addAllowedBlock(pillars, -2, 1, -2, Blocks.nether_brick);
		structure.addAllowedBlock(chimerite, -2, 2, -2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_CHIMERITE_BLOCK);
		structure.addAllowedBlock(obsidian, -2, 2, -2, Blocks.obsidian);
		structure.addAllowedBlock(sunstone, -2, 2, -2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_SUNSTONE_BLOCK);

		structure.addAllowedBlock(pillars, 2, 0, -2, Blocks.nether_brick);
		structure.addAllowedBlock(pillars, 2, 1, -2, Blocks.nether_brick);
		structure.addAllowedBlock(chimerite, 2, 2, -2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_CHIMERITE_BLOCK);
		structure.addAllowedBlock(obsidian, 2, 2, -2, Blocks.obsidian);
		structure.addAllowedBlock(sunstone, 2, 2, -2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_SUNSTONE_BLOCK);

		structure.addAllowedBlock(pillars, -2, 0, 2, Blocks.nether_brick);
		structure.addAllowedBlock(pillars, -2, 1, 2, Blocks.nether_brick);
		structure.addAllowedBlock(chimerite, -2, 2, 2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_CHIMERITE_BLOCK);
		structure.addAllowedBlock(obsidian, -2, 2, 2, Blocks.obsidian);
		structure.addAllowedBlock(sunstone, -2, 2, 2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_SUNSTONE_BLOCK);

		structure.addAllowedBlock(pillars, 2, 0, 2, Blocks.nether_brick);
		structure.addAllowedBlock(pillars, 2, 1, 2, Blocks.nether_brick);
		structure.addAllowedBlock(chimerite, 2, 2, 2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_CHIMERITE_BLOCK);
		structure.addAllowedBlock(obsidian, 2, 2, 2, Blocks.obsidian);
		structure.addAllowedBlock(sunstone, 2, 2, 2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_SUNSTONE_BLOCK);

		wizardChalkCircle = addWizChalkGroupToStructure(structure, 1);
	}

	@Override
	protected void checkNearbyBlockState(){
		ArrayList<StructureGroup> groups = structure.getMatchedGroups(7, worldObj, pos);

		float capsLevel = 1;
		boolean pillarsFound = false;
		boolean wizChalkFound = false;

		for (StructureGroup group : groups){
			if (group == pillars)
				pillarsFound = true;
			else if (group == wizardChalkCircle)
				wizChalkFound = true;

			for (StructureGroup cap : caps.keySet()){
				if (group == cap){
					capsLevel = caps.get(cap);
					break;
				}
			}
		}

		powerMultiplier = 1;

		if (wizChalkFound)
			powerMultiplier = 1.25f;

		if (pillarsFound)
			powerMultiplier *= capsLevel;
	}

	@Override
	public void update(){
		if (worldObj.isRemote){
			this.rotation += this.rotationIncrement;
		}else{
			surroundingCheckTicks++;
		}

		if (worldObj.isRemote || ticksSinceLastEntityScan++ > 25){
			updateNearbyEntities();
			ticksSinceLastEntityScan = 0;
		}

		Iterator<EntityLivingBase> it = cachedEntities.iterator();
		while (it.hasNext()){

			EntityLivingBase ent = it.next();

			if (ent.isDead){
				it.remove();
				continue;
			}

			MovingObjectPosition mop = this.worldObj.rayTraceBlocks(new Vec3(pos.add(0.5, 1.5, 0.5)), new Vec3(ent.posX, ent.posY + ent.getEyeHeight(), ent.posZ), false);

			if (EntityUtilities.isSummon(ent) || mop != null){
				continue;
			}

			ent.motionY = 0;
			ent.motionX = 0;
			ent.motionZ = 0;
			double deltaX = pos.getX() + 0.5f - ent.posX;
			double deltaZ = pos.getY() + 0.5f - ent.posZ;
			double deltaY = pos.getZ() - ent.posY;
			double angle = Math.atan2(deltaZ, deltaX);

			double offsetX = Math.cos(angle) * 0.1;
			double offsetZ = Math.sin(angle) * 0.1;
			double offsetY = 0.05f;

			double distanceHorizontal = deltaX * deltaX + deltaZ * deltaZ;
			double distanceVertical = pos.getY() - ent.posY;
			boolean spawnedParticles = false;

			if (distanceHorizontal < 1.3){
				if (distanceVertical < -1.5){
					if (worldObj.isRemote && worldObj.rand.nextInt(10) < 3){
						AMCore.proxy.particleManager.BoltFromPointToPoint(worldObj, pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, ent.posX, ent.posY, ent.posZ, 4, 0x000000);
					}
				}
				if (distanceVertical < -2){
					offsetY = 0;
					if (!worldObj.isRemote){
						if (ent.attackEntityFrom(DamageSources.darkNexus, 4)){
							if (ent.getHealth() <= 0){
								ent.setDead();
								float power = ((int)Math.ceil((ent.getMaxHealth() * (ent.ticksExisted / 20)) % 5000)) * this.powerMultiplier;
								PowerNodeRegistry.For(this.worldObj).insertPower(this, PowerTypes.DARK, power);
							}
						}
					}
				}
			}

			if (worldObj.isRemote){
				if (!arcs.containsKey(ent)){
					AMLineArc arc = (AMLineArc)AMCore.proxy.particleManager.spawn(worldObj, "textures/blocks/oreblocksunstone.png", pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, ent);
					if (arc != null){
						arc.setExtendToTarget();
						arc.setRBGColorF(1, 1, 1);
					}
					arcs.put(ent, arc);
				}
				Iterator arcIterator = arcs.keySet().iterator();
				ArrayList<Entity> toRemove = new ArrayList<Entity>();
				while (arcIterator.hasNext()){
					Entity arcEnt = (Entity)arcIterator.next();
					AMLineArc arc = (AMLineArc)arcs.get(arcEnt);
					if (arcEnt == null || arcEnt.isDead || arc == null || arc.isDead || new AMVector3(ent).distanceSqTo(new AMVector3(pos)) > 100)
						toRemove.add(arcEnt);
				}

				for (Entity e : toRemove){
					arcs.remove(e);
				}
			}
			if (!worldObj.isRemote)
				ent.moveEntity(offsetX, offsetY, offsetZ);
		}
		if (surroundingCheckTicks % 100 == 0){
			checkNearbyBlockState();
			surroundingCheckTicks = 1;
			if (!worldObj.isRemote && PowerNodeRegistry.For(this.worldObj).checkPower(this, this.capacity * 0.1f)){
				List<EntityPlayer> nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-2, 0, -2), pos.add(2, 3, 2)));
				for (EntityPlayer p : nearbyPlayers){
					if (p.isPotionActive(BuffList.manaRegen.id)) continue;
					p.addPotionEffect(new BuffEffectManaRegen(600, 3));
				}
			}

			//TODO:
			/*if (rand.nextDouble() < (this.getCharge() / this.getCapacity()) * 0.01){
					int maxSev = (int)Math.ceil((this.getCharge() / this.getCapacity()) * 2) + rand.nextInt(2);
					IllEffectsManager.instance.ApplyRandomBadThing(this, IllEffectSeverity.values()[maxSev], BadThingTypes.DARKNEXUS);
				}*/
		}

		super.callSuperUpdate();
	}

	private void updateNearbyEntities(){
		List<EntityLivingBase> nearbyEntities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-10, -10, -10), pos.add(10, 10, 10)));
		for (EntityLivingBase entity : nearbyEntities){
			if (entity.isEntityInvulnerable(DamageSource.generic) ||
					entity instanceof IBossDisplayData ||
					entity instanceof EntityDarkling ||
					entity instanceof EntityPlayer ||
					entity instanceof EntityAirSled ||
					entity instanceof EntityWinterGuardianArm ||
					entity instanceof EntityThrownSickle ||
					entity instanceof EntityFlicker ||
					entity instanceof EntityShadowHelper)
				continue;
			if (!cachedEntities.contains(entity))
				cachedEntities.add(entity);
		}

		ticksSinceLastEntityScan = 0;
	}

	@Override
	public MultiblockStructureDefinition getDefinition(){
		return structure;
	}

	@Override
	public boolean canRequestPower(){
		return false;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return type == PowerTypes.DARK;
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return new PowerTypes[]{
				PowerTypes.DARK
		};
	}

	@Override
	public int getSizeInventory(){
		return 0;
	}
}
