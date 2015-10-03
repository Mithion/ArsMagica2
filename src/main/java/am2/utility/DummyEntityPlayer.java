package am2.utility;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.UUID;

public class DummyEntityPlayer extends EntityPlayer{

	private EntityLivingBase trackEntity = null;

	public DummyEntityPlayer(World world){
		super(world, new GameProfile(UUID.randomUUID(), "dummyplayer"));
	}

	public static EntityPlayer fromEntityLiving(EntityLivingBase entity){
		if (entity instanceof EntityPlayer) return (EntityPlayer)entity;

		DummyEntityPlayer dep = new DummyEntityPlayer(entity.worldObj);
		dep.setPosition(entity.posX, entity.posY, entity.posZ);
		dep.setRotation(entity.rotationYaw, entity.rotationPitch);
		dep.trackEntity = entity;

		return dep;
	}

	public void copyEntityLiving(EntityLivingBase entity){
		this.setPosition(entity.posX, entity.posY, entity.posZ);
		this.setRotation(entity.rotationYaw, entity.rotationPitch);
		this.trackEntity = entity;
		this.worldObj = entity.worldObj;
	}

	@Override
	public void onUpdate(){
		if (trackEntity != null){
			this.setPosition(trackEntity.posX, trackEntity.posY, trackEntity.posZ);
			this.setRotation(trackEntity.rotationYaw, trackEntity.rotationPitch);

			this.motionX = trackEntity.motionX;
			this.motionY = trackEntity.motionY;
			this.motionZ = trackEntity.motionZ;
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(int i, String s){
		return false;
	}

	@Override
	public ChunkCoordinates getCommandSenderPosition(){
		return null;
	}

	@Override
	public void addChatMessage(IChatComponent arg0){

	}

}
