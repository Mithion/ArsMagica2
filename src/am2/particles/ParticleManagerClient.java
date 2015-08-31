package am2.particles;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import am2.AMCore;
import am2.AMKeyBindings;
import am2.api.math.AMVector3;
import am2.buffs.BuffList;
import am2.codechicken.LightningBolt;
import am2.network.AMDataReader;
import am2.particles.ribbon.AMRibbon;
import am2.playerextensions.ExtendedProperties;
import am2.utility.MathUtilities;

public class ParticleManagerClient extends ParticleManagerServer {

	public static final byte PKT_BOLT_ENT_ENT = 64;
	public static final byte PKT_BOLT_PT_PT = 63;
	public static final byte PKT_BEAM_ENT_ENT = 62;
	public static final byte PKT_BEAM_PT_PT = 61;
	private final Random rand = new Random();
	private final ParticleRenderer particleRenderer;

	public ParticleManagerClient(){
		particleRenderer = new ParticleRenderer();
	}

	@Override
	public AMParticle spawn(World world, String name, double x, double y, double z){
		AMParticle particle = new AMParticle(world, x, y, z);
		particle.SetParticleTextureByName(name);

		particleRenderer.addAMParticle(particle);

		return particle;
	}

	@Override
	public AMLineArc spawn(World world, String name, double x, double y, double z, double targetX, double targetY, double targetZ){
		AMLineArc arc = new AMLineArc(world, x, y, z, targetX, targetY, targetZ, name);
		particleRenderer.addArcEffect(arc);
		return arc;
	}

	@Override
	public AMLineArc spawn(World world, String name, double x, double y, double z, Entity target){
		AMLineArc arc = new AMLineArc(world, x, y, z, target, name);
		particleRenderer.addArcEffect(arc);
		return arc;
	}

	@Override
	public AMLineArc spawn(World world, String name, Entity source, Entity target) {
		AMLineArc arc = new AMLineArc(world, source, target, name);
		particleRenderer.addArcEffect(arc);
		return arc;
	}
	
	public void registerEventHandlers(){
		MinecraftForge.EVENT_BUS.register(particleRenderer);
		FMLCommonHandler.instance().bus().register(particleRenderer);
	}

	@Override
	public void BoltFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage, int type, int color){
		double xx = target.posX;
		double yy = target.posY + target.getEyeHeight();
		double zz = target.posZ;

		double px = source.posX;
		double py = source.posY + source.getEyeHeight();
		double pz = source.posZ;
		px -= MathHelper.cos(source.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		py -= 0.1000000014901161D;
		pz -= MathHelper.sin(source.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		AMVector3 vec3d = MathUtilities.getLook(source, 1.0F);
		px += vec3d.x * 0.25D;
		py += vec3d.y * 0.25D;
		pz += vec3d.z * 0.25D;
		LightningBolt bolt = new LightningBolt(world, px, py, pz, xx, target.boundingBox.minY + target.height / 2.0F, zz, world.rand.nextLong(), 6, 0.3F, 6);

		bolt.defaultFractal();
		bolt.setSourceEntity(caster);
		bolt.setType(type);
		bolt.setDamage(0);
		bolt.setOverrideColor(color);
		bolt.finalizeBolt();
	}

	@Override
	public void BoltFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ){
		BoltFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 1, -1);
	}

	@Override
	public void BoltFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ){
		BoltFromEntityToPoint(world, source, endX, endY, endZ, 0, -1);
	}

	@Override
	public void BoltFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ, int type, int color){
		BoltFromPointToPoint(world, source.posX, source.posY + source.getEyeHeight(), source.posZ, endX, endY, endZ, type, color);
	}

	@Override
	public void BoltFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage){
		BoltFromEntityToEntity(world, caster, source, target, damage, 1, -1);
	}

	@Override
	public void BoltFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, int type, int color){

		if (AMCore.config.NoGFX()){
			return;
		}
		LightningBolt bolt = new LightningBolt(world, startX, startY, startZ, endX, endY, endZ, world.rand.nextLong(), 6, 0.3F, 6);

		bolt.defaultFractal();
		bolt.setSourceEntity(null);
		bolt.setType(type);
		bolt.setDamage(0);
		bolt.finalizeBolt();
	}

	@Override
	public Object BeamFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage, int color){
		if (AMCore.config.NoGFX()){
			return null;
		}
		double xx = target.posX;
		double yy = target.posY + target.getEyeHeight();
		double zz = target.posZ;

		double px = source.posX;
		double py = source.posY + source.getEyeHeight();
		double pz = source.posZ;
		px -= MathHelper.cos(source.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		py -= 0.1000000014901161D;
		pz -= MathHelper.sin(source.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		AMVector3 vec3d = MathUtilities.getLook(source, 1.0F);
		px += vec3d.x * 0.25D;
		py += vec3d.y * 0.25D;
		pz += vec3d.z * 0.25D;

		AMBeam fx = new AMBeam(world, px, py, pz, xx, target.boundingBox.minY + target.height / 2.0F, zz);
		fx.setRGBColor(color);
		Minecraft.getMinecraft().effectRenderer.addEffect(fx);

		return fx;
	}

	@Override
	public Object BeamFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, int color){
		if (AMCore.config.NoGFX()){
			return null;
		}
		AMBeam fx = new AMBeam(world, startX, startY, startZ, endX, endY, endZ);
		fx.setRGBColor(color);
		Minecraft.getMinecraft().effectRenderer.addEffect(fx);

		return fx;
	}

	@Override
	public Object BeamFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ){
		return BeamFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 0xFFFFFF);
	}

	@Override
	public Object BeamFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ){
		return BeamFromEntityToPoint(world, source, endX, endY, endZ, 0xFFFFFF);
	}

	@Override
	public Object BeamFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ, int color){
		if (!(source instanceof EntityPlayer) || source == AMCore.proxy.getLocalPlayer()){
			return BeamFromPointToPoint(world, source.posX, source.posY, source.posZ, endX, endY, endZ, color);
		}else{
			return BeamFromPointToPoint(world, source.posX, source.posY + source.getEyeHeight() - 0.2f, source.posZ, endX, endY, endZ, color);
		}
	}

	@Override
	public Object BeamFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage){
		return BeamFromEntityToEntity(world, caster, source, target, damage, 0xFFFFFF);
	}

	@Override
	public void RibbonFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage, int type){
		double xx = target.posX;
		double yy = target.posY + target.getEyeHeight();
		double zz = target.posZ;

		double px = source.posX;
		double py = source.posY + source.getEyeHeight();
		double pz = source.posZ;
		px -= MathHelper.cos(source.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		py -= 0.1000000014901161D;
		pz -= MathHelper.sin(source.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		AMVector3 vec3d = MathUtilities.getLook(source, 1.0F);
		px += vec3d.x * 0.25D;
		py += vec3d.y * 0.25D;
		pz += vec3d.z * 0.25D;
		AMRibbon ribbon = new AMRibbon(world, 0.5f, 0.05f, px, py, pz);

		Minecraft.getMinecraft().effectRenderer.addEffect(ribbon);
	}

	@Override
	public void RibbonFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ){
		RibbonFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 1);
	}

	@Override
	public void RibbonFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ){
		RibbonFromEntityToPoint(world, source, endX, endY, endZ, 0);
	}

	@Override
	public void RibbonFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ, int type){
		RibbonFromPointToPoint(world, source.posX, source.posY, source.posZ, endX, endY, endZ, 0);
	}

	@Override
	public void RibbonFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage){
		RibbonFromEntityToEntity(world, caster, source, target, damage, 1);
	}

	@Override
	public void RibbonFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, int type){

		if (AMCore.config.NoGFX()){
			return;
		}
		AMRibbon ribbon = new AMRibbon(world, 0.5f, 0.05f, startX, startY, startZ);

		Minecraft.getMinecraft().effectRenderer.addEffect(ribbon);
	}

	@Override
	public void handleClientPacketData(World world, byte[] data){
		AMDataReader rdr = new AMDataReader(data);

		byte sub_id = rdr.getByte();
		int type = 1;

		String name;

		int casterID;
		int sourceID;
		int targetID;
		int damage;

		int color;

		Entity caster;
		Entity source;
		Entity target;

		double startX;
		double startY;
		double startZ;
		double endX;
		double endY;
		double endZ;

		switch(sub_id){
		case PKT_BOLT_ENT_ENT:
			casterID = rdr.getInt();
			sourceID = rdr.getInt();
			targetID = rdr.getInt();
			damage = rdr.getInt();
			type = rdr.getInt();
			color = rdr.getInt();

			caster = world.getEntityByID(casterID);
			source = world.getEntityByID(sourceID);
			target = world.getEntityByID(targetID);

			if (caster == null || source == null || target == null){
				return;
			}
			BoltFromEntityToEntity(world, caster, source, target, damage, type, color);

			break;
		case PKT_BOLT_PT_PT:
			startX = rdr.getDouble();
			startY = rdr.getDouble();
			startZ = rdr.getDouble();
			endX = rdr.getDouble();
			endY = rdr.getDouble();
			endZ = rdr.getDouble();
			type = rdr.getInt();
			color = rdr.getInt();

			BoltFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, type, color);

			break;
		case PKT_BEAM_ENT_ENT:
			casterID = rdr.getInt();
			sourceID = rdr.getInt();
			targetID = rdr.getInt();
			damage = rdr.getInt();
			type = rdr.getInt();

			caster = world.getEntityByID(casterID);
			source = world.getEntityByID(sourceID);
			target = world.getEntityByID(targetID);

			if (caster == null || source == null || target == null){
				return;
			}
			BeamFromEntityToEntity(world, caster, source, target, damage, type);

			break;
		case PKT_BEAM_PT_PT:
			startX = rdr.getDouble();
			startY = rdr.getDouble();
			startZ = rdr.getDouble();
			endX = rdr.getDouble();
			endY = rdr.getDouble();
			endZ = rdr.getDouble();
			type = rdr.getInt();

			BeamFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, type);

			break;
		case PKT_RIBBON_PT_PT:
			startX = rdr.getDouble();
			startY = rdr.getDouble();
			startZ = rdr.getDouble();
			endX = rdr.getDouble();
			endY = rdr.getDouble();
			endZ = rdr.getDouble();
			type = rdr.getInt();

			RibbonFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, type);

			break;
		case PKT_RIBBON_ENT_ENT:
			casterID = rdr.getInt();
			sourceID = rdr.getInt();
			targetID = rdr.getInt();
			damage = rdr.getInt();
			type = rdr.getInt();

			caster = world.getEntityByID(casterID);
			source = world.getEntityByID(sourceID);
			target = world.getEntityByID(targetID);

			if (caster == null || source == null || target == null){
				return;
			}
			RibbonFromEntityToEntity(world, caster, source, target, damage, type);

			break;
		case PKT_ARC_PT_PT:
			name = rdr.getString();
			startX = rdr.getDouble();
			startY = rdr.getDouble();
			startZ = rdr.getDouble();
			endX = rdr.getDouble();
			endY = rdr.getDouble();
			endZ = rdr.getDouble();

			spawn(world, name, startX, startY, startZ, endX, endY, endZ);
			break;
		case PKT_ARC_PT_ENT:
			name = rdr.getString();
			startX = rdr.getDouble();
			startY = rdr.getDouble();
			startZ = rdr.getDouble();
			targetID = rdr.getInt();

			target = world.getEntityByID(targetID);
			if (target == null){
				return;
			}
			spawn(world, name, startX, startY, startZ, target);

			break;
		case PKT_ARC_ENT_ENT:
			name = rdr.getString();
			sourceID = rdr.getInt();
			targetID = rdr.getInt();

			source = world.getEntityByID(sourceID);
			target = world.getEntityByID(targetID);

			if (source == null || target == null){
				return;
			}
			spawn(world, name, source, target);
			break;
		}
	}

	@Override
	public void spawnAuraParticles(EntityLivingBase ent) {
		if (!ent.worldObj.isRemote) return;

		int particleIndex = 15;
		int particleBehaviour = 0;
		float particleScale = 0;
		float particleAlpha = 0;
		boolean particleDefaultColor = true;
		boolean particleRandomColor = true;
		int particleColor = 0xFFFFFF;
		int particleQuantity = 2;
		float particleSpeed = 0.02f;

		if (Minecraft.getMinecraft().thePlayer == ent){
			particleIndex = AMCore.config.getAuraIndex();
			particleBehaviour = AMCore.config.getAuraBehaviour();
			particleScale = AMCore.config.getAuraScale() / 10;
			particleAlpha = AMCore.config.getAuraAlpha();
			particleDefaultColor = AMCore.config.getAuraColorDefault();
			particleRandomColor = AMCore.config.getAuraColorRandom();
			particleColor = AMCore.config.getAuraColor();
			particleQuantity = AMCore.config.getAuraQuantity();
			particleSpeed = AMCore.config.getAuraSpeed() / 10;
		}else{
			particleIndex = ExtendedProperties.For(ent).getAuraIndex();
			particleBehaviour = ExtendedProperties.For(ent).getAuraBehaviour();
			particleScale = ExtendedProperties.For(ent).getAuraScale() / 10;
			particleAlpha = ExtendedProperties.For(ent).getAuraAlpha();
			particleDefaultColor = ExtendedProperties.For(ent).getAuraColorDefault();
			particleRandomColor = ExtendedProperties.For(ent).getAuraColorRandomize();
			particleColor =  ExtendedProperties.For(ent).getAuraColor();
			particleQuantity =  ExtendedProperties.For(ent).getAuraQuantity();
			particleSpeed =  ExtendedProperties.For(ent).getAuraSpeed() / 10;
		}

		if (particleIndex == 31) //fix radiant particle's scaling issues...
			particleScale /= 10;

		if (ent.worldObj.isRemote && ent instanceof EntityPlayer && AMCore.proxy.playerTracker.hasAA((EntityPlayer) ent)){
			if (Minecraft.getMinecraft().thePlayer != ent || Minecraft.getMinecraft().gameSettings.thirdPersonView > 0){
				if (AMParticle.particleTypes[particleIndex].startsWith("lightning_bolts")){
					int type = Integer.parseInt(new String(new char[] {AMParticle.particleTypes[particleIndex].charAt(AMParticle.particleTypes[particleIndex].length()-1)}));
					if (rand.nextInt(100) < 90){
						BoltFromPointToPoint(ent.worldObj, ent.posX + (rand.nextFloat() - 0.5f), ent.posY + ent.getEyeHeight() - ent.height + (rand.nextFloat() * ent.height), ent.posZ + (rand.nextFloat() - 0.5f), ent.posX + (rand.nextFloat() - 0.5f), ent.posY + ent.getEyeHeight() - ent.height + (rand.nextFloat() * ent.height), ent.posZ + (rand.nextFloat() - 0.5f), type, -1);
					}else{
						BoltFromPointToPoint(ent.worldObj, ent.posX, ent.posY + ent.getEyeHeight() - 0.4, ent.posZ, ent.posX + (rand.nextFloat() * 10 - 5), ent.posY + (rand.nextFloat() * 10 - 5), ent.posZ + (rand.nextFloat() * 10 - 5), type, -1);
					}
				}else{
					int offset = 0;
					for (int i = 0; i < particleQuantity; ++i){
						AMParticle effect = spawn(ent.worldObj, AMParticle.particleTypes[particleIndex], ent.posX + (rand.nextFloat() - 0.5f), ent.posY + ent.getEyeHeight() - 0.5f + offset - (rand.nextFloat() * 0.5), ent.posZ + (rand.nextFloat() - 0.5f));
						if (effect != null){
							effect.setIgnoreMaxAge(false);
							effect.setMaxAge(40);
							effect.setParticleScale(particleScale);
							effect.SetParticleAlpha(particleAlpha);
							effect.noClip = false;
							if (!particleDefaultColor){
								if (particleRandomColor){
									effect.setRGBColorF(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
								}else{
									effect.setRGBColorI(particleColor);
								}
							}
							switch (particleBehaviour){
							case 0: //fade
								effect.AddParticleController(new ParticleFadeOut(effect, 1, false).setFadeSpeed(particleSpeed));
								break;
							case 1: //float
								effect.AddParticleController(new ParticleFloatUpward(effect, 0.2f, particleSpeed, 1, false));
								break;
							case 2: //sink
								effect.AddParticleController(new ParticleFloatUpward(effect, 0.2f, -particleSpeed, 1, false));
								break;
							case 3: //orbit
								effect.AddParticleController(new ParticleOrbitEntity(effect, ent, particleSpeed, 1, false));
								break;
							case 4: //arc
								effect.AddParticleController(new ParticleArcToEntity(effect, 1, ent, false).generateControlPoints().SetSpeed(particleSpeed));
								break;
							case 5: //flee
								effect.AddParticleController(new ParticleFleeEntity(effect, ent, particleSpeed, 2D, 1, false));
								break;
							case 6: //forward
								effect.AddParticleController(new ParticleMoveOnHeading(effect, ent.rotationYaw + 90, ent.rotationPitch, particleSpeed, 1, false));
								break;
							case 7: //pendulum
								effect.AddParticleController(new ParticlePendulum(effect, 0.2f, particleSpeed, 1, false));
								break;
							case 8: //grow
								effect.AddParticleController(new ParticleGrow(effect, particleSpeed, 1, false));
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void spawnBuffParticles(EntityLivingBase entityliving) {
		Random rand = new Random();
		World world = entityliving.worldObj;

		if (!world.isRemote) return;

		if (entityliving == Minecraft.getMinecraft().thePlayer){
			if (entityliving.isPotionActive(BuffList.trueSight.id) && entityliving.ticksExisted % 20 == 0){
				int radius = 5;
				for (int i = -radius; i <= radius; ++i){
					for (int j= -radius; j <= radius; ++j){
						for (int k = -radius; k <= radius; ++k){
							if (entityliving.worldObj.getBlock((int)entityliving.posX + i, (int)entityliving.posY + j, (int)entityliving.posZ + k) == Blocks.air && entityliving.worldObj.getBlockLightValue((int)entityliving.posX + i, (int)entityliving.posY + j, (int)entityliving.posZ + k) <= 7){
								AMParticle effect = spawn(world, "hr_sparkles_1", (int)entityliving.posX - 1 + i + (rand.nextDouble() * 3), (int)entityliving.posY - 1 + j + (rand.nextDouble() * 3), (int)entityliving.posZ - 1 + k + (rand.nextDouble() * 3));
								if (effect != null){
									effect.setRGBColorF(rand.nextFloat() * 0.4f + 0.3f, 0.6f, rand.nextFloat() * 0.4f + 0.6f);
									effect.setIgnoreMaxAge(false);
									effect.setMaxAge(40);
									effect.AddParticleController(new ParticleFloatUpward(effect, 0.01f, 0.01f,  1, false));
									effect.AddParticleController(new ParticleFadeOut(effect, 2, false).setFadeSpeed(0.01f));
								}
							}
						}
					}
				}

			}
			if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
				return;
		}
	}

	@Override
	public ParticleController createDefaultParticleController(int type, Object eff, AMVector3 location, float modifier, int meta) {
		AMParticle effect = (AMParticle)eff;
		switch (type){
		default:
		case 0: //fade
			return new ParticleFadeOut(effect, 1, false).setFadeSpeed(0.02f * modifier);
		case 1: //float
			return new ParticleFloatUpward(effect, 0.01f, 0.03f * modifier, 1, false);
		case 2: //sink
			return new ParticleFloatUpward(effect, 0.01f, -0.03f * modifier, 1, false);
		case 3: //orbit
			return new ParticleOrbitPoint(effect, location.x, location.y, location.z, 1, false).SetOrbitSpeed(0.05f * modifier);
		case 4: //arc
			return new ParticleArcToPoint(effect, 1, location.x, location.y, location.z, false).generateControlPoints().SetSpeed(0.02f * modifier);
		case 5: //flee
			//return new ParticleFleePoint(effect, location.add(new AMVector3(0.5, 0.5, 0.5)), 0.02f * modifier, 1.5f, 1, false);
			return new ParticleMoveOnHeading(effect, effect.worldObj.rand.nextInt(360), 0, 0.02f * modifier, 1, false);
		case 6: //forward
			if ((meta & ~0x8) == 3)
				meta &= ~0x2;
			else if ((meta & ~0x8) == 1)
				meta |= 0x2;
			return new ParticleMoveOnHeading(effect, (meta & ~0x8) * 90, 0, 0.02f * modifier, 1, false);
		}
	}
	
	@Override
	public ParticleController createDefaultParticleController(int type, Object eff, EntityLivingBase ent) {
		AMParticle effect = (AMParticle)eff;
		switch (type){
		default:
		case 0: //fade
			return new ParticleFadeOut(effect, 1, false).setFadeSpeed(0.02f);
		case 1: //float
			return new ParticleFloatUpward(effect, 0.2f, 0.03f, 1, false);
		case 2: //sink
			return new ParticleFloatUpward(effect, 0.2f, -0.03f, 1, false);
		case 3: //orbit
			return new ParticleOrbitEntity(effect, ent, 0.05f, 1, false);
		case 4: //arc
			return new ParticleArcToEntity(effect, 1, ent, false).generateControlPoints();
		case 5: //flee
			return new ParticleFleeEntity(effect, ent, 0.05f, 2D, 1, false);
		case 6: //forward
			return new ParticleMoveOnHeading(effect, ent.rotationYaw + 90, ent.rotationPitch, 0.05f, 1, false);
		}		
	}
}
