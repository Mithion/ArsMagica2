package am2.particles;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;

public class ParticleManagerServer {
	public static final byte PKT_BOLT_ENT_ENT = 64;
	public static final byte PKT_BOLT_PT_PT = 63;
	public static final byte PKT_BEAM_ENT_ENT = 62;
	public static final byte PKT_BEAM_PT_PT = 61;
	public static final byte PKT_RIBBON_ENT_ENT = 60;
	public static final byte PKT_RIBBON_PT_PT = 59;

	public static final byte PKT_ARC_PT_PT = 58;
	public static final byte PKT_ARC_PT_ENT = 57;
	public static final byte PKT_ARC_ENT_ENT = 56;
	private final Random rand = new Random();

	public Object spawn(World world, String name, double x, double y, double z){
		return null;
	}

	public Object spawn(World world, String name, double x, double y, double z, double targetX, double targetY, double targetZ){
		if (!world.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_ARC_PT_PT);
			writer.add(name);
			writer.add(x);
			writer.add(y);
			writer.add(z);
			writer.add(targetX);
			writer.add(targetY);
			writer.add(targetZ);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, x, y, z, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
		return null;
	}

	public Object spawn(World world, String name, double x, double y, double z, Entity target){
		if (!world.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_ARC_PT_ENT);
			writer.add(name);
			writer.add(x);
			writer.add(y);
			writer.add(z);
			writer.add(target.getEntityId());
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, x, y, z, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
		return null;
	}

	public Object spawn(World world, String name, Entity source, Entity target){
		if (!world.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_ARC_ENT_ENT);
			writer.add(name);
			writer.add(source.getEntityId());
			writer.add(target.getEntityId());
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, source.posX, source.posY, source.posZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
		return null;
	}

	public void BoltFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage, int type, int color){
		if (!world.isRemote){
			//send a packet to all clients telling them to spawn this lightning bolt!
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_BOLT_ENT_ENT); //what kind of bolt are we doing?
			writer.add(caster.getEntityId());
			writer.add(source.getEntityId());
			writer.add(target.getEntityId());
			writer.add(damage);
			writer.add(type);
			writer.add(color);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, caster.posX, caster.posY, caster.posZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
	}

	public void BoltFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ){
		BoltFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 1, -1);
	}

	public void BoltFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ){
		BoltFromEntityToPoint(world, source, endX, endY, endZ, 0, -1);
	}

	public void BoltFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ, int type, int color){
		BoltFromPointToPoint(world, source.posX, source.posY + source.getEyeHeight(), source.posZ, endX, endY, endZ, type, color);
	}

	public void BoltFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage){
		BoltFromEntityToEntity(world, caster, source, target, damage, 1, -1);
	}

	public void BoltFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, int type, int color){
		if (!world.isRemote){
			//send a packet to all clients telling them to spawn this lightning bolt!
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_BOLT_PT_PT); //what kind of bolt are we doing?
			writer.add(startX);
			writer.add(startY);
			writer.add(startZ);
			writer.add(endX);
			writer.add(endY);
			writer.add(endZ);
			writer.add(type);
			writer.add(color);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, startX, startY, startZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
	}

	public Object BeamFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage, int color){
		if (!world.isRemote){
			//send a packet to all clients telling them to spawn this beam!
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_BEAM_ENT_ENT); //what kind of beam are we doing?
			writer.add(caster.getEntityId());
			writer.add(source.getEntityId());
			writer.add(target.getEntityId());
			writer.add(damage);
			writer.add(color);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, caster.posX, caster.posY, caster.posZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
		return null;
	}

	public Object BeamFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, int color){
		if (!world.isRemote){
			//send a packet to all clients telling them to spawn this beam!
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_BEAM_PT_PT); //what kind of beam are we doing?
			writer.add(startX);
			writer.add(startY);
			writer.add(startZ);
			writer.add(endX);
			writer.add(endY);
			writer.add(endZ);
			writer.add(color);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, startX, startY, startZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
		return null;
	}

	public Object BeamFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ){
		return BeamFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 0xFFFFFF);
	}

	public Object BeamFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ){
		return BeamFromEntityToPoint(world, source, endX, endY, endZ, 0xFFFFFF);
	}

	public Object BeamFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ, int color){
		if (!(source instanceof EntityPlayer) || source == AMCore.instance.proxy.getLocalPlayer()){
			return BeamFromPointToPoint(world, source.posX, source.posY, source.posZ, endX, endY, endZ, color);
		}else{
			return BeamFromPointToPoint(world, source.posX, source.posY + source.getEyeHeight() - 0.2f, source.posZ, endX, endY, endZ, color);
		}
	}

	public Object BeamFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage){
		return BeamFromEntityToEntity(world, caster, source, target, damage, 0xFFFFFF);
	}

	public void RibbonFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage, int type){
		if (!world.isRemote){
			//send a packet to all clients telling them to spawn this lightning bolt!
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_RIBBON_ENT_ENT); //what kind of bolt are we doing?
			writer.add(caster.getEntityId());
			writer.add(source.getEntityId());
			writer.add(target.getEntityId());
			writer.add(damage);
			writer.add(type);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, caster.posX, caster.posY, caster.posZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
	}

	public void RibbonFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ){
		RibbonFromPointToPoint(world, startX, startY, startZ, endX, endY, endZ, 1);
	}

	public void RibbonFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ){
		RibbonFromEntityToPoint(world, source, endX, endY, endZ, 0);
	}

	public void RibbonFromEntityToPoint(World world, Entity source, double endX, double endY, double endZ, int type){
		RibbonFromPointToPoint(world, source.posX, source.posY, source.posZ, endX, endY, endZ, 0);
	}

	public void RibbonFromEntityToEntity(World world, Entity caster, Entity source, Entity target, int damage){
		RibbonFromEntityToEntity(world, caster, source, target, damage, 1);
	}

	public void RibbonFromPointToPoint(World world, double startX, double startY, double startZ, double endX, double endY, double endZ, int type){
		if (!world.isRemote){
			//send a packet to all clients telling them to spawn this lightning bolt!
			AMDataWriter writer = new AMDataWriter();
			writer.add(PKT_RIBBON_PT_PT); //what kind of bolt are we doing?
			writer.add(startX);
			writer.add(startY);
			writer.add(startZ);
			writer.add(endX);
			writer.add(endY);
			writer.add(endZ);
			writer.add(type);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.dimensionId, startX, startY, startZ, 32, AMPacketIDs.PARTICLE_SPAWN_SPECIAL, writer.generate());
		}
	}

	public void handleClientPacketData(World world, byte[] data){

	}

	public void spawnAuraParticles(EntityLivingBase ent) {
	}

	public void spawnBuffParticles(EntityLivingBase entityliving) {
	}

	public ParticleController createDefaultParticleController(int type, Object effect, EntityLivingBase ent){
		return null;
	}

	public ParticleController createDefaultParticleController(int type, Object effect, AMVector3 location, float modifier, int meta){
		return null;
	}
}
