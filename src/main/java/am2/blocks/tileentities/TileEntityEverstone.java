package am2.blocks.tileentities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import am2.AMCore;
import am2.LogHelper;
import am2.blocks.BlocksCommonProxy;
import am2.particles.AMParticle;

public class TileEntityEverstone extends TileEntity implements ITickable{

	private int reconstructTimer = 0;
	private IBlockState facade = null;
	private static final int reconstructMax = AMCore.config.getEverstoneRepairRate();

	private boolean poweredFromEverstone = false;
	private boolean poweredFromRedstone = false;

	public void setFacade(IBlockState state){
		this.facade = state;

		if (!worldObj.isRemote){
			List<EntityPlayerMP> players = worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(64, 64, 64));
			for (EntityPlayerMP player : players){
				player.playerNetServerHandler.sendPacket(getDescriptionPacket());
			}
		}
		worldObj.markBlockForUpdate(pos);
	}

	private void propagatePoweredByEverstone(boolean powered, ArrayList<BlockPos> completedUpdates){
		if (completedUpdates.contains(pos)){
			return;
		}

		completedUpdates.add(pos);
		poweredFromEverstone = powered;
		onBreak();

		if (worldObj.isRemote){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "radiant", pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
			if (particle != null){
				particle.setMaxAge(20);
				particle.setDontRequireControllers();
				particle.setIgnoreMaxAge(false);
			}
		}

		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				for (int k = -1; k <= 1; k++){
					BlockPos targetPosition = pos.add(i, j, k);
					Block blockID = worldObj.getBlockState(targetPosition).getBlock();
					if (blockID.equals(BlocksCommonProxy.everstone) && !completedUpdates.contains(targetPosition)){
						TileEntityEverstone everstone = ((TileEntityEverstone)worldObj.getTileEntity(targetPosition));
						if (everstone != null)
							everstone.propagatePoweredByEverstone(powered, completedUpdates);
					}
				}
			}
		}
	}
	
	@Override
	public void update() {
		if (reconstructTimer <= 0 && worldObj.isBlockIndirectlyGettingPowered(pos) > 0){
			propagatePoweredByEverstone(true, new ArrayList<BlockPos>());
			poweredFromRedstone = true;
		}else if (poweredFromRedstone && worldObj.isBlockIndirectlyGettingPowered(pos) == 0){
			poweredFromRedstone = false;
			propagatePoweredByEverstone(false, new ArrayList<BlockPos>());
		}

		if (reconstructTimer <= 0)
			return;

		if (worldObj.isBlockIndirectlyGettingPowered(pos) == 0 && !poweredFromEverstone){
			reconstructTimer--;
			if (worldObj.isRemote){ //
				//worldObj.scheduleBlockUpdateWithPriority(xCoord, yCoord, zCoord, BlocksCommonProxy.everstone.blockID, 0, 0);
				worldObj.markBlockForUpdate(pos);
				if (reconstructTimer < reconstructMax - 20 && reconstructTimer > 20 && worldObj.rand.nextInt(10) < 8){
					AMCore.proxy.addDigParticle(worldObj, pos.getX(), pos.getY(), pos.getZ(), getFacade() == null ? BlocksCommonProxy.everstone : getFacade().getBlock(), getFacade().getBlock().getMetaFromState(facade));
				}
			}
		}
	}

	public int getFadeStrength(){
		if (reconstructTimer > reconstructMax / 2)
			return 0;
		return (int)(128 * ((float)(reconstructMax - reconstructTimer) / reconstructMax));
	}

	public IBlockState getFacade(){
		return facade;
	}

	public boolean isSolid(){
		return reconstructTimer == 0;
	}

	public void onBreak(){
		reconstructTimer = reconstructMax;
		if (!worldObj.isRemote){
			List<EntityPlayerMP> players = worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(64, 64, 64));
			for (EntityPlayerMP player : players){
				player.playerNetServerHandler.sendPacket(getDescriptionPacket());
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, worldObj.getBlockState(pos).getBlock().getMetaFromState(worldObj.getBlockState(pos)), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readFromNBT(par1nbtTagCompound);
		if (par1nbtTagCompound.hasKey("facade")){
			this.facade = Block.getBlockFromName(par1nbtTagCompound.getString("facade")).getStateFromMeta(par1nbtTagCompound.getInteger("facadeMeta"));
			
			LogHelper.debug("Facade: " + par1nbtTagCompound.getString("facade"));
		}
		this.poweredFromEverstone = par1nbtTagCompound.getBoolean("poweredFromEverstone");
		this.poweredFromRedstone = par1nbtTagCompound.getBoolean("poweredFromRedstone");
		this.reconstructTimer = par1nbtTagCompound.getInteger("reconstructTimer");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeToNBT(par1nbtTagCompound);
		if (facade != null){
			par1nbtTagCompound.setString("facade", facade.getBlock().getUnlocalizedName().replace("tile.", ""));
			par1nbtTagCompound.setInteger("facadeMeta", facade.getBlock().getMetaFromState(facade));
		}
		par1nbtTagCompound.setBoolean("poweredFromEverstone", poweredFromEverstone);
		par1nbtTagCompound.setBoolean("poweredFromRedstone", poweredFromRedstone);
		par1nbtTagCompound.setInteger("reconstructTimer", reconstructTimer);
	}
}
