package am2.affinity;

import am2.AMCore;
import am2.api.events.ManaCostEvent;
import am2.api.events.SpellCastingEvent;
import am2.api.math.AMVector3;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.buffs.*;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.utility.MathUtilities;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.*;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.Random;

public class AffinityHelper{

	@SubscribeEvent
	public void onEntityLivingBase(LivingUpdateEvent event){
		EntityLivingBase ent = event.entityLiving;

		if (ent instanceof EntityEnderman){
			if (ent.getLastAttacker() != ent.getAITarget() && ent.getAITarget() instanceof EntityPlayer){
				AffinityData affinityData = AffinityData.For(ent.getAITarget());
				float enderDepth = affinityData.getAffinityDepth(Affinity.ENDER);
				if (enderDepth == 1.0f){
					ent.setRevengeTarget(null);
				}
			}
		}

		if (!(ent instanceof EntityPlayer)) return;

		AffinityData affinityData = AffinityData.For(ent);

		affinityData.tickDiminishingReturns();

		float waterDepth = affinityData.getAffinityDepth(Affinity.WATER);
		float fireDepth = affinityData.getAffinityDepth(Affinity.FIRE);
		float natureDepth = affinityData.getAffinityDepth(Affinity.NATURE);
		float iceDepth = affinityData.getAffinityDepth(Affinity.ICE);
		float lifeDepth = affinityData.getAffinityDepth(Affinity.LIFE);
		float enderDepth = affinityData.getAffinityDepth(Affinity.ENDER);
		float lightningDepth = affinityData.getAffinityDepth(Affinity.LIGHTNING);

		AffinityModifiers.instance.applySpeedModifiersBasedOnDepth((EntityPlayer)ent, natureDepth, iceDepth, lightningDepth);
		AffinityModifiers.instance.applyHealthModifiers((EntityPlayer)ent, enderDepth, waterDepth, fireDepth, lightningDepth);
		applyFulmintion((EntityPlayer)ent, lightningDepth);

		if (lightningDepth >= 0.5f){
			ent.stepHeight = 1.014f;
		}else if (ent.stepHeight == 1.014f){
			ent.stepHeight = 0.5f;
		}

		affinityData.accumulatedLifeRegen += 0.025 * lifeDepth;
		if (affinityData.accumulatedLifeRegen > 1.0f){
			affinityData.accumulatedLifeRegen -= 1.0f;
			ent.heal(1);
		}

		if (natureDepth == 1.0f){
			if (ent.worldObj.canBlockSeeSky(new BlockPos(ent)) && ent.worldObj.isDaytime()){
				affinityData.accumulatedHungerRegen += 0.02f;
				if (affinityData.accumulatedHungerRegen > 1.0f){
					((EntityPlayer)ent).getFoodStats().addStats(1, 0.025f);
					affinityData.accumulatedHungerRegen -= 1;
				}
			}else{
				((EntityPlayer)ent).addExhaustion(0.025f);
			}

			if (ent.isCollidedHorizontally){
				if (!ent.isSneaking()){
					float movement = ExtendedProperties.For(ent).getIsFlipped() ? -0.25f : 0.25f;
					ent.moveEntity(0, movement, 0);
					ent.motionY = 0;
				}else{
					ent.motionY *= 0.79999999;
				}
				ent.fallDistance = 0;
			}
		}

		//Ender Affinity
		if (enderDepth >= 0.75f && affinityData.hasActivatedNightVision()){
			if (!ent.worldObj.isRemote && (!ent.isPotionActive(Potion.nightVision.id) || ent.getActivePotionEffect(Potion.nightVision).getDuration() <= 220)){
				ent.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, 1));
			}
		}

		if (ent.onGround)
			affinityData.setLastGroundPosition(new AMVector3(ent));
		affinityData.tickCooldown();

		//End Ender Affinity

		if (ent.isInWater()){
			float earthDepth = affinityData.getAffinityDepth(Affinity.EARTH);
			if (earthDepth > 0.25f && ent.motionY > -0.3f){
				ent.addVelocity(0, -0.01f * earthDepth, 0);
			}

			if (waterDepth > 0.5f){
				if (!ent.isPotionActive(BuffList.swiftSwim.id) || ent.getActivePotionEffect(BuffList.swiftSwim).getDuration() < 10){
					ent.addPotionEffect(new BuffEffectSwiftSwim(100, waterDepth > 0.75f ? 2 : 1));
				}
			}

			if (waterDepth > 0.4 && ent.worldObj.rand.nextInt(20) < 4)
				ent.setAir(ent.getAir() + 1);

			if (!ent.worldObj.isRemote && ent.worldObj.rand.nextInt(100) < 5){
				ent.setAir(ent.getAir() + 1);
				byte[] data = new AMDataWriter().add(ent.getEntityId()).add(ent.getAir()).generate();
				AMNetHandler.INSTANCE.sendPacketToClientPlayer((EntityPlayerMP)ent, AMPacketIDs.SYNC_AIR_CHANGE, data);
			}

			boolean waterMovementFlag = false;

			if ((ent instanceof EntityPlayer && ((EntityPlayer)ent).inventory.armorInventory[1] != null && ((EntityPlayer)ent).inventory.armorInventory[1].getItem() == ItemsCommonProxy.waterGuardianOrbs)){
				waterMovementFlag = true;

				if (!ent.worldObj.isRemote && (!ent.isPotionActive(BuffList.waterBreathing) || ent.getActivePotionEffect(BuffList.waterBreathing).getDuration() <= 200))
					ent.addPotionEffect(new BuffEffectWaterBreathing(400, 2));
			}

			if (waterDepth > 0.5f || waterMovementFlag){
				applyReverseWaterMovement(ent);
			}

		}

		if (ent.worldObj.isRaining() && !ent.worldObj.isRemote && ent.worldObj.getBiomeGenForCoords(new BlockPos(ent)).canSpawnLightningBolt()){
			float airDepth = affinityData.getAffinityDepth(Affinity.AIR);
			if (airDepth > 0.5f && airDepth < 0.85f && !ent.worldObj.isRemote && ent.worldObj.rand.nextInt(100) < 10){
				if (!ent.isSneaking() && !ent.isPotionActive(BuffList.gravityWell) && !ent.isInsideOfMaterial(Material.water) && ent.isWet()){
					double velX = ent.worldObj.rand.nextDouble() - 0.5;
					double velY = ent.worldObj.rand.nextDouble() - 0.5;
					double velZ = ent.worldObj.rand.nextDouble() - 0.5;
					ent.addVelocity(velX, velY, velZ);
					AMNetHandler.INSTANCE.sendVelocityAddPacket(ent.worldObj, ent, velX, velY, velZ);
				}
			}
		}

		if (ent.isSneaking()){
			if (iceDepth >= 0.5f){
				makeIceBridge((EntityPlayer)ent, iceDepth);
			}
		}
	}

	private Block getBlockInFrontOf(EntityLivingBase ent){
		double dx = Math.cos(ent.rotationYaw);
		double dz = Math.sin(ent.rotationYaw);

		int posX = (int)Math.floor(ent.posX + dx);
		int posZ = (int)Math.floor(ent.posZ + dz);
		int posY = (int)Math.floor(ent.posY);

		return ent.worldObj.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
	}

	private void applyFulmintion(EntityPlayer ent, float lightningDepth){
		//chance to light nearby TNT
		if (!ent.worldObj.isRemote){
			if (lightningDepth > 0.5f && lightningDepth <= 0.8f){
				BlockPos offsetPos = new BlockPos((int)ent.posX - 5 + ent.getRNG().nextInt(11),
					(int)ent.posY - 5 + ent.getRNG().nextInt(11),
					(int)ent.posZ - 5 + ent.getRNG().nextInt(11)
				);

				Block block = ent.worldObj.getBlockState(offsetPos).getBlock();
				if (block == Blocks.tnt){
					ent.worldObj.setBlockToAir(offsetPos);
					((BlockTNT)Blocks.tnt).explode(ent.worldObj, offsetPos, ent.worldObj.getBlockState(offsetPos), ent);
				}
			}
			//chance to supercharge nearby creepers
			if (lightningDepth >= 0.7f && lightningDepth <= 0.95f && ent.getRNG().nextDouble() < 0.05f){
				List<EntityCreeper> creepers = ent.worldObj.getEntitiesWithinAABB(EntityCreeper.class, ent.getEntityBoundingBox().expand(5, 5, 5));
				for (EntityCreeper creeper : creepers){
					creeper.getDataWatcher().updateObject(17, (byte)1);
					AMCore.proxy.particleManager.BoltFromEntityToEntity(ent.worldObj, ent, ent, creeper, 0, 1, -1);
				}
			}
		}

		if (lightningDepth > 0.25f && ent.isWet()){
			ExtendedProperties.For(ent).deductMana(100);
			if (ent.worldObj.isRemote){
				AMCore.proxy.particleManager.BoltFromEntityToPoint(ent.worldObj, ent, ent.posX - 2 + ent.getRNG().nextDouble() * 4, ent.posY + ent.getEyeHeight() - 2 + ent.getRNG().nextDouble() * 4, ent.posZ - 2 + ent.getRNG().nextDouble() * 4);
			}else{
				if (ent.getRNG().nextDouble() < 0.4f)
					ent.worldObj.playSoundAtEntity(ent, "arsmagica2:misc.event.mana_shield_block", 1.0f, ent.worldObj.rand.nextFloat() + 0.5f);
			}
		}
	}

	private void makeIceBridge(EntityPlayer ent, float iceDepth){
		AMVector3[] blocks = MathUtilities.GetHorizontalBlocksInFrontOfCharacter(ent, 1, (int)Math.round(ent.posX), (int)Math.floor(ent.posY) - 1, (int)Math.round(ent.posZ));
		for (int i = 0; i < blocks.length; ++i){
			AMVector3 current = blocks[i];
			for (int n = -1; n <= 1; ++n){
				for (int p = -1; p <= 1; ++p){
					BlockPos pos = new BlockPos((int)current.x + n, (int)current.y, (int)current.z + p);
					Block block = ent.worldObj.getBlockState(pos).getBlock();

					if (iceDepth == 1.0f && block == Blocks.lava)
						ent.worldObj.setBlockState(pos, Blocks.obsidian.getDefaultState());
					else if (iceDepth == 1.0f && block == Blocks.flowing_lava)
						ent.worldObj.setBlockState(pos, Blocks.cobblestone.getDefaultState());
					else if (block == Blocks.water)
						ent.worldObj.setBlockState(pos, Blocks.ice.getDefaultState());
					else if (block == Blocks.flowing_water)
						ent.worldObj.setBlockState(pos, Blocks.ice.getDefaultState());
					block = ent.worldObj.getBlockState(pos.up()).getBlock();
					if (block == Blocks.fire){
						ent.worldObj.setBlockState(pos.up(), Blocks.air.getDefaultState());
					}
				}
			}
		}
	}

	private void applyReverseWaterMovement(EntityLivingBase entity){

		AxisAlignedBB par1AxisAlignedBB = entity.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D);

		int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

		if (!entity.worldObj.isAreaLoaded(new BlockPos(i, k, i1), new BlockPos(j, l, j1))){
			return;
		}else{
			boolean flag = false;
			Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);

			for (int k1 = i; k1 < j; ++k1){
				for (int l1 = k; l1 < l; ++l1){
					for (int i2 = i1; i2 < j1; ++i2){
						BlockPos pos = new BlockPos(k1, l1, i2);
						Block block = entity.worldObj.getBlockState(pos).getBlock();

						if (block != null && block.getMaterial() == Material.water){
							PropertyInteger prop = (block == Blocks.water || block == Blocks.flowing_water) ? BlockLiquid.LEVEL : BlockFluidBase.LEVEL;
							double d0 = l1 + 1 - BlockLiquid.getLiquidHeightPercent(entity.worldObj.getBlockState(pos).getValue(prop));

							if (l >= d0){
								flag = true;
								block.modifyAcceleration(entity.worldObj, pos, entity, vec3);
							}
						}
					}
				}
			}

			if (vec3.lengthVector() > 0.0D && entity.isInWater()){
				vec3 = vec3.normalize();
				double d1 = -0.014D;
				entity.motionX += vec3.xCoord * d1;
				entity.motionY += vec3.yCoord * d1;
				entity.motionZ += vec3.zCoord * d1;
				//AMCore.proxy.packetSender.SendVelocityAddPacket(entity.worldObj, entity, vec3.xCoord * d1, vec3.yCoord * d1, vec3.zCoord * d1);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event){
		EntityLivingBase ent = event.entityLiving;
		if (!(ent instanceof EntityPlayer)) return;

		AffinityData affinityData = AffinityData.For(ent);

		float airDepth = affinityData.getAffinityDepth(Affinity.AIR);
		if (airDepth >= 0.5f){
			float velocity = airDepth * 0.35f;
			if (ExtendedProperties.For(ent).getIsFlipped())
				velocity *= -1;
			ent.addVelocity(0, velocity, 0);
		}
	}

	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event){
		EntityLivingBase ent = event.entityLiving;
		if (!(ent instanceof EntityPlayer)) return;

		AffinityData affinityData = AffinityData.For(ent);

		float earthDepth = affinityData.getAffinityDepth(Affinity.EARTH);
		if (earthDepth > 0.25f){
			event.distance += 1.25 * (earthDepth);
		}

		float airDepth = affinityData.getAffinityDepth(Affinity.AIR);
		if (airDepth >= 0.5f){
			event.distance -= 2 * (airDepth);
			if (event.distance < 0) event.distance = 0;
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event){
		EntityLivingBase ent = event.entityLiving;
		AffinityData affinityData = AffinityData.For(ent);

		if (event.source.getSourceOfDamage() instanceof EntityPlayer){
			float attackerFireDepth = AffinityData.For((EntityLivingBase)event.source.getSourceOfDamage()).getAffinityDepth(Affinity.FIRE);
			if (attackerFireDepth > 0.8f && ((EntityPlayer)event.source.getSourceOfDamage()).getCurrentEquippedItem() == null){
				ent.setFire(4);
				event.ammount += 3;
			}
			float attackerLightningDepth = AffinityData.For((EntityLivingBase)event.source.getSourceOfDamage()).getAffinityDepth(Affinity.LIGHTNING);
			if (attackerLightningDepth > 0.75f && !ent.worldObj.isRemote && ((EntityPlayer)event.source.getSourceOfDamage()).getCurrentEquippedItem() == null){
				EntityLightningBolt elb = new EntityLightningBolt(ent.worldObj, ent.posX, ent.posY, ent.posZ);
				elb.setPosition(ent.posX, ent.posY, ent.posZ);
				ent.worldObj.addWeatherEffect(elb);
			}
		}

		if (!(ent instanceof EntityPlayer)) return;

		float earthDepth = affinityData.getAffinityDepth(Affinity.EARTH);
		if (earthDepth > 0.25f){
			float reduction = 0.1f * earthDepth;
			event.ammount -= event.ammount * reduction;
		}

		float arcaneDepth = affinityData.getAffinityDepth(Affinity.ARCANE);
		if (arcaneDepth > 0.25f){
			event.ammount *= 1.1f;
		}

		float waterDepth = affinityData.getAffinityDepth(Affinity.WATER);
		if (waterDepth > 0.9f && event.source.getSourceOfDamage() instanceof EntityEnderman){
			event.source.getSourceOfDamage().attackEntityFrom(DamageSource.drown, 2);
		}

		float fireDepth = affinityData.getAffinityDepth(Affinity.FIRE);
		if (event.source.isFireDamage()){
			float reduction = 1 - (0.6f * fireDepth);
			event.ammount = event.ammount * reduction;
		}

		float enderDepth = affinityData.getAffinityDepth(Affinity.ENDER);
		if (event.source == DamageSource.magic || event.source == DamageSource.wither){
			float reduction = 1 - (0.75f * enderDepth);
			event.ammount = event.ammount * reduction;
		}

		if (event.source.getSourceOfDamage() instanceof EntityLivingBase){
			float natureDepth = affinityData.getAffinityDepth(Affinity.NATURE);
			if (natureDepth == 1.0f){
				((EntityLivingBase)event.source.getSourceOfDamage()).attackEntityFrom(DamageSource.cactus, 3);
			}else if (natureDepth >= 0.75f){
				((EntityLivingBase)event.source.getSourceOfDamage()).attackEntityFrom(DamageSource.cactus, 2);
			}else if (natureDepth >= 0.5f){
				((EntityLivingBase)event.source.getSourceOfDamage()).attackEntityFrom(DamageSource.cactus, 1);
			}

			float iceDepth = affinityData.getAffinityDepth(Affinity.ICE);
			BuffEffectFrostSlowed effect = null;
			if (iceDepth == 1.0f){
				effect = new BuffEffectFrostSlowed(200, 2);
			}else if (iceDepth >= 0.75f){
				effect = new BuffEffectFrostSlowed(160, 1);
			}else if (iceDepth >= 0.5f){
				effect = new BuffEffectFrostSlowed(100, 0);
			}
			if (effect != null){
				((EntityLivingBase)event.source.getSourceOfDamage()).addPotionEffect(effect);
			}
		}

		if (event.ammount == 0) event.setCanceled(true);
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){
		if (event.entityLiving.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD && event.source.getSourceOfDamage() instanceof EntityPlayer){
			EntityPlayer source = (EntityPlayer)event.source.getSourceOfDamage();
			float lifeDepth = AffinityData.For(source).getAffinityDepth(Affinity.LIFE);
			if (lifeDepth >= 0.6f){
				source.addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 1));
				source.addPotionEffect(new PotionEffect(Potion.hunger.id, 40, 1));
				source.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 100, 1));
				source.addPotionEffect(new PotionEffect(Potion.weakness.id, 100, 1));
			}
		}
	}

	@SubscribeEvent
	public void onPreSpellCast(SpellCastingEvent.Pre event){
		if (event.caster.isPotionActive(BuffList.clarity.id)){
			if (!event.caster.worldObj.isRemote){
				event.caster.removePotionEffect(BuffList.clarity.id);
			}
		}
	}

	@SubscribeEvent
	public void onSpellCast(SpellCastingEvent.Post event){
		if (event.caster instanceof EntityPlayer && event.castResult == SpellCastResult.SUCCESS){
			float affinityDepth = AffinityData.For(event.caster).getAffinityDepth(Affinity.ARCANE);
			if (affinityDepth > 0.4f){
				if (event.caster.worldObj.rand.nextInt(100) < 5 && !event.caster.worldObj.isRemote){
					event.caster.addPotionEffect(new BuffEffectClarity(140, 0));
				}
			}
		}
	}

	@SubscribeEvent
	public void onSpellManaCost(ManaCostEvent event){
		if (event.caster instanceof EntityPlayer && AffinityData.For(event.caster).getAffinityDepth(Affinity.ARCANE) > 0.5f){
			event.manaCost *= 0.95f;
			event.burnout *= 0.95f;

			if (event.caster.isPotionActive(BuffList.clarity.id)){
				event.manaCost = 0f;
				event.burnout = 0f;
			}
		}
	}
}
