package am2;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.HashMap;
import java.util.List;

public class AMChunkLoader implements LoadingCallback{

	public static final AMChunkLoader INSTANCE = new AMChunkLoader();

	private HashMap<TicketIdentifier, ForgeChunkManager.Ticket> tickets;

	private AMChunkLoader(){
		tickets = new HashMap<TicketIdentifier, ForgeChunkManager.Ticket>();
	}

	private Ticket requestTicket(BlockPos pos, World world){
		Ticket ticket = ForgeChunkManager.requestTicket(AMCore.instance, world, ForgeChunkManager.Type.NORMAL);
		if (ticket != null){
			cacheTicket(new TicketIdentifier(pos, world.provider.getDimensionId()), ticket);
			return ticket;
		}
		return null;
	}

	private void cacheTicket(TicketIdentifier identifier, Ticket ticket){
		if (tickets.containsKey(identifier)){
			LogHelper.warn("Attempted to register duplicate tickets for the same world - this shouldn't really happen.");
			return;
		}
		tickets.put(identifier, ticket);
	}

	private Ticket getTicket(TicketIdentifier identifier){
		return tickets.get(identifier);
	}

	/**
	 * Requests a static chunk load.  This is a non-moving chunk load, and will be assumed that the specified class will exist as a tile entity
	 *
	 * @param clazz the class to look for
	 * @param x     the x coordinate of the tile entity
	 * @param y     the y coordinate of the tile entity
	 * @param z     the z coordinate of the tile entity
	 * @param world The world object of the tile entity
	 */
	public void requestStaticChunkLoad(Class clazz, BlockPos pos, World world){
		Ticket ticket = requestTicket(pos, world);
		if (ticket == null){
			LogHelper.warn("Unable to get a ticket for chunk loading!  The chunk identified by %d, %d is *not* loaded!", pos.getX(), pos.getZ());
			return;
		}

		NBTTagCompound compound = ticket.getModData();
		compound.setIntArray("StaticLoadCoords", new int[]{pos.getX(), pos.getY(), pos.getZ()});
		compound.setString("ChunkLoadClass", clazz.getName());

		ChunkCoordIntPair pair = new ChunkCoordIntPair(pos.getX() >> 4, pos.getZ() >> 4);
		ForgeChunkManager.forceChunk(ticket, pair);
	}

	public void releaseStaticChunkLoad(Class clazz, BlockPos pos, World world){
		Ticket ticket = getTicket(new TicketIdentifier(pos, world.provider.getDimensionId()));
		if (ticket == null){
			LogHelper.warn("No ticket for specified location.  No chunk to unload!");
			return;
		}

		ChunkCoordIntPair pair = new ChunkCoordIntPair(pos.getX() >> 4, pos.getZ() >> 4);
		ForgeChunkManager.unforceChunk(ticket, pair);
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world){
		for (Ticket ticket : tickets){
			NBTTagCompound compound = ticket.getModData();
			int[] coords = compound.getIntArray("StaticLoadCoords");
			String clazzName = compound.getString("ChunkLoadClass");
			Class clazz = null;
			try{
				clazz = Class.forName(clazzName);
			}catch (ClassNotFoundException e){
				LogHelper.info("Cached class not found (%s) when attempting to load a chunk loading ticket.  This ticket will be discarded, and the chunk may not be loaded.  Block Coords: %d, %d", clazzName, coords[0], coords[2]);
				ForgeChunkManager.releaseTicket(ticket);
				continue;
			}
			TileEntity te = world.getTileEntity(new BlockPos(coords[0], coords[1], coords[2]));
			if (te != null && te.getClass().isAssignableFrom(clazz)){
				ChunkCoordIntPair pair = new ChunkCoordIntPair(coords[0] >> 4, coords[2] >> 4);
				ForgeChunkManager.forceChunk(ticket, pair);
			}else{
				LogHelper.info("Either no tile entity was found or it did not match the cached class.  This chunk loading ticket will be discarded, and the chunk may not be loaded.  Block Coords: %d, %d", coords[0], coords[2]);
				ForgeChunkManager.releaseTicket(ticket);
			}
		}
	}

	private class TicketIdentifier implements Comparable<TicketIdentifier>{
		public final int dimension;
		public final BlockPos pos;

		public TicketIdentifier(BlockPos pos, int dimension){
			this.dimension = dimension;
			this.pos = pos;
		}

		@Override
		public int compareTo(TicketIdentifier o){
			if (this.pos.equals(o.pos) && o.dimension == this.dimension)
				return 0;
			return -1;
		}
	}
}
