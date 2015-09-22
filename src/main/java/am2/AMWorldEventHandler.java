package am2;

import am2.entities.EntityFlicker;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.List;

public class AMWorldEventHandler{

	private static final String genKey = "HasGenerated";

	@SubscribeEvent
	public void onChunkSaved(ChunkDataEvent.Save event){
		NBTTagCompound compound = new NBTTagCompound();
		if (event.getData().hasKey("ArsMagica2")){
			compound = event.getData().getCompoundTag("ArsMagica2");
		}
		compound.setBoolean(genKey, true);
		event.getData().setTag("ArsMagica2", compound);
	}

	@SubscribeEvent
	public void onChunkLoaded(ChunkDataEvent.Load event){
		int dimensionID = event.world.provider.dimensionId;
		ChunkCoordIntPair chunkLocation = event.getChunk().getChunkCoordIntPair();

		NBTTagCompound compound = (NBTTagCompound)event.getData().getTag("ArsMagica2");

		if (AMCore.config.retroactiveWorldgen() && (compound == null || !compound.hasKey(genKey))){
			AMCore.log.info("Detected a chunk that requires retrogen.  Adding to retrogen list.");
			AMCore.proxy.addQueuedRetrogen(dimensionID, chunkLocation);
		}
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event){
		if (!event.world.isRemote){
			for (List l : event.getChunk().entityLists){
				for (Object o : l){
					if (o instanceof EntityFlicker){
						((EntityFlicker)o).setDead();
					}
				}
			}
		}
	}
}
