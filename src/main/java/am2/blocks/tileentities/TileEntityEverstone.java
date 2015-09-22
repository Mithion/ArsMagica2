package am2.blocks.tileentities;

import am2.AMCore;
import am2.blocks.BlocksCommonProxy;
import am2.particles.AMParticle;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkPosition;

import java.util.ArrayList;
import java.util.List;

public class TileEntityEverstone extends TileEntity{

	private int reconstructTimer = 0;
	private Block facade = null;
	private int facadeMeta = -1;
	private static final int reconstructMax = AMCore.config.getEverstoneRepairRate();

	private boolean poweredFromEverstone = false;
	private boolean poweredFromRedstone = false;

	public void setFacade(Block block, int meta){
		this.facade = block;
		this.facadeMeta = meta;

		if (!worldObj.isRemote){
			List<EntityPlayerMP> players = worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(64, 64, 64));
			for (EntityPlayerMP player : players){
				player.playerNetServerHandler.sendPacket(getDescriptionPacket());
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	private void propagatePoweredByEverstone(boolean powered, ArrayList<ChunkPosition> completedUpdates){
		ChunkPosition myPosition = new ChunkPosition(xCoord, yCoord, zCoord);
		if (completedUpdates.contains(myPosition)){
			return;
		}

		completedUpdates.add(myPosition);
		poweredFromEverstone = powered;
		onBreak();

		if (worldObj.isRemote){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "radiant", xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f);
			if (particle != null){
				particle.setMaxAge(20);
				particle.setDontRequireControllers();
				particle.setIgnoreMaxAge(false);
			}
		}

		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				for (int k = -1; k <= 1; k++){
					ChunkPosition targetPosition = new ChunkPosition(xCoord + i, yCoord + j, zCoord + k);
					Block blockID = worldObj.getBlock(targetPosition.chunkPosX, targetPosition.chunkPosY, targetPosition.chunkPosZ);
					if (blockID == BlocksCommonProxy.everstone && !completedUpdates.contains(targetPosition)){
						TileEntityEverstone everstone = ((TileEntityEverstone)worldObj.getTileEntity(targetPosition.chunkPosX, targetPosition.chunkPosY, targetPosition.chunkPosZ));
						if (everstone != null)
							everstone.propagatePoweredByEverstone(powered, completedUpdates);
					}
				}
			}
		}
	}

	@Override
	public void updateEntity(){

		if (reconstructTimer <= 0 && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)){
			propagatePoweredByEverstone(true, new ArrayList<ChunkPosition>());
			poweredFromRedstone = true;
		}else if (poweredFromRedstone && !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)){
			poweredFromRedstone = false;
			propagatePoweredByEverstone(false, new ArrayList<ChunkPosition>());
		}

		if (reconstructTimer <= 0)
			return;

		if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && !poweredFromEverstone){
			reconstructTimer--;
			if (worldObj.isRemote){ //
				//worldObj.scheduleBlockUpdateWithPriority(xCoord, yCoord, zCoord, BlocksCommonProxy.everstone.blockID, 0, 0);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				if (reconstructTimer < reconstructMax - 20 && reconstructTimer > 20 && worldObj.rand.nextInt(10) < 8){
					AMCore.proxy.addDigParticle(worldObj, xCoord, yCoord, zCoord, getFacade() == null ? BlocksCommonProxy.everstone : getFacade(), getFacadeMeta());
				}
			}
		}
	}

	public int getFadeStrength(){
		if (reconstructTimer > reconstructMax / 2)
			return 0;
		return (int)(128 * ((float)(reconstructMax - reconstructTimer) / reconstructMax));
	}

	public Block getFacade(){
		return facade;
	}

	public int getFacadeMeta(){
		return facadeMeta;
	}

	public boolean isSolid(){
		return reconstructTimer == 0;
	}

	public void onBreak(){
		reconstructTimer = reconstructMax;
		if (!worldObj.isRemote){
			List<EntityPlayerMP> players = worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(64, 64, 64));
			for (EntityPlayerMP player : players){
				player.playerNetServerHandler.sendPacket(getDescriptionPacket());
			}
		}
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.func_148857_g());
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readFromNBT(par1nbtTagCompound);
		if (par1nbtTagCompound.hasKey("facade")){
			this.facade = Block.getBlockFromName(par1nbtTagCompound.getString("facade"));
			AMCore.log.debug("Facade: " + par1nbtTagCompound.getString("facade"));
			this.facadeMeta = par1nbtTagCompound.getInteger("facadeMeta");
		}
		this.poweredFromEverstone = par1nbtTagCompound.getBoolean("poweredFromEverstone");
		this.poweredFromRedstone = par1nbtTagCompound.getBoolean("poweredFromRedstone");
		this.reconstructTimer = par1nbtTagCompound.getInteger("reconstructTimer");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeToNBT(par1nbtTagCompound);
		if (facade != null){
			par1nbtTagCompound.setString("facade", facade.getUnlocalizedName().replace("tile.", ""));
			par1nbtTagCompound.setInteger("facadeMeta", facadeMeta);
		}
		par1nbtTagCompound.setBoolean("poweredFromEverstone", poweredFromEverstone);
		par1nbtTagCompound.setBoolean("poweredFromRedstone", poweredFromRedstone);
		par1nbtTagCompound.setInteger("reconstructTimer", reconstructTimer);
	}
}
