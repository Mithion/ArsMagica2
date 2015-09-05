package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.api.power.PowerTypes;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.power.PowerNodeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Iterator;

public class TileEntitySlipstreamGenerator extends TileEntityAMPower{

	private ArrayList<EntityPlayer> levitatingEntities;
	private int updateTicks = 1;

	private static final int EFFECT_HEIGHT = 50;

	public TileEntitySlipstreamGenerator(){
		super(100);
		levitatingEntities = new ArrayList<EntityPlayer>();
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	@Override
	public void updateEntity(){
		super.updateEntity();

		if (updateTicks++ > 10){
			refreshPlayerList();
			updateTicks = 0;
			if (worldObj.isRemote && levitatingEntities.size() > 0)
				AMNetHandler.INSTANCE.sendPowerRequestToServer(new AMVector3(this));
		}

		Iterator<EntityPlayer> it = levitatingEntities.iterator();
		while (it.hasNext()){
			EntityPlayer player = it.next();
			if (!playerIsValid(player)){
				it.remove();
				continue;
			}

			if (PowerNodeRegistry.For(this.worldObj).getHighestPower(this) >= 0.25f){

				player.motionY *= 0.5999999;
				if (Math.abs(player.motionY) < 0.2){
					player.addVelocity(0, -player.motionY, 0);
					player.fallDistance = 0f;
				}else{
					player.fallDistance--;
				}

				if (!player.isSneaking()){
					float pitch = player.rotationPitch;
					float factor = (pitch > 0 ? (pitch - 10) : (pitch + 10)) / -180.0f;

					if (Math.abs(pitch) > 10f){
						player.moveEntity(0, factor, 0);
					}
				}

				if (worldObj.isRemote)
					spawnParticles(player);
				PowerNodeRegistry.For(this.worldObj).consumePower(this, PowerNodeRegistry.For(this.worldObj).getHighestPowerType(this), 0.25f);
			}
		}
	}

	private void spawnParticles(EntityPlayer player){
		AMParticle wind = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "wind", player.posX, player.posY - player.height, player.posZ);
		float pitch = player.rotationPitch;
		float factor = (pitch > 0 ? (pitch - 10) : (pitch + 10)) / -180.0f;
		if (player.isSneaking())
			factor = 0.01f;
		if (wind != null){
			wind.setMaxAge(10);
			wind.addRandomOffset(1, 1, 1);
			wind.setParticleScale(0.1f);
			wind.AddParticleController(new ParticleFloatUpward(wind, 0, Math.abs(factor) * 2, 1, false));
		}
	}

	private boolean playerIsValid(EntityPlayer player){
		if (player == null || player.isDead)
			return false;
		float tolerance = 0.2f;
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord - tolerance, yCoord + 1, zCoord - tolerance, xCoord + 1 + tolerance, yCoord + 1 + this.EFFECT_HEIGHT, zCoord + 1 + tolerance);
		Vec3 myLoc = Vec3.createVectorHelper(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
		Vec3 playerLoc = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
		return bb.intersectsWith(player.boundingBox) && worldObj.rayTraceBlocks(myLoc, playerLoc, true) == null;
	}

	private void refreshPlayerList(){
		levitatingEntities.clear();

		for (int i = 0; i < worldObj.playerEntities.size(); ++i){
			EntityPlayer player = (EntityPlayer)worldObj.playerEntities.get(i);
			if (playerIsValid(player) && !levitatingEntities.contains(player))
				levitatingEntities.add(player);
		}
	}

	@Override
	public boolean canRequestPower(){
		return true;
	}

	@Override
	public int getChargeRate(){
		return 12;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}
}
