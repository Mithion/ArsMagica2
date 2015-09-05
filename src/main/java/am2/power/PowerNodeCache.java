package am2.power;

import am2.AMCore;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class PowerNodeCache{

	public static String extension = ".amc";
	public static String folder = "AM2PowerData";
	private static final String pndID = "pnd_%d_%d";

	private static HashMap<Integer, File> saveDirs = new HashMap<Integer, File>();
	private static HashMap<String, File> saveFilesCached = new HashMap<String, File>();
	private static HashMap<RegionCoordinates, NBTTagCompound> dataCache = new HashMap<RegionCoordinates, NBTTagCompound>();

	public static final PowerNodeCache instance = new PowerNodeCache();

	private File getFileFromChunk(World world, ChunkCoordIntPair chunk, boolean createNew){

		File saveFolder = saveDirs.get(world.provider.dimensionId);
		if (saveFolder == null){
			ISaveHandler handler = world.getSaveHandler();
			if (handler instanceof SaveHandler){
				saveFolder = new File(((SaveHandler)handler).getWorldDirectory(), folder);
				saveFolder.mkdirs();
				saveFolder = new File(saveFolder, String.format("DIM%d", world.provider.dimensionId));
				saveFolder.mkdirs();
				saveDirs.put(world.provider.dimensionId, saveFolder);
			}else{
				return null;
			}
		}

		int rX = (int)Math.floor(chunk.chunkXPos / 32);
		int rZ = (int)Math.floor(chunk.chunkZPos / 32);

		String fileName = String.format("%d_%d%s", rX, rZ, extension);

		File file = saveFilesCached.get(fileName);

		if (file != null)
			return file;


		file = new File(saveFolder, fileName);

		if (!file.exists()){
			if (createNew){
				try{
					file.createNewFile();
				}catch (Throwable t){
					t.printStackTrace();
				}
			}else{
				return null;
			}
		}

		saveFilesCached.put(fileName, file);

		return file;
	}

	public NBTTagCompound getNBTForChunk(World world, ChunkCoordIntPair chunk){
		RegionCoordinates rc = new RegionCoordinates(chunk, world.provider.dimensionId);
		if (dataCache.containsKey(rc)){
			NBTTagCompound compound = dataCache.get(rc);
			if (compound.hasKey("AM2PowerData"))
				return compound;
			dataCache.remove(rc);
		}
		return LoadNBTFromFile(world, chunk);
	}

	private void SaveNBTToFile(World world, ChunkCoordIntPair chunk, NBTTagCompound compound, boolean flushImmediate){

		RegionCoordinates rc = new RegionCoordinates(chunk, world.provider.dimensionId);

		NBTTagCompound dataCompound = dataCache.get(rc);

		if (dataCompound == null){
			File file = getFileFromChunk(world, chunk, true);
			if (file == null || (!file.canWrite() && !file.setWritable(true)) || (!file.canRead() && !file.setReadable(true))){
				FMLLog.severe("Ars Magica 2 >> Unable to obtain file handle!  The power system data for the chunk at %d, %d will NOT be saved!  To fix this, make sure you have read/write access to the Minecraft instance folder.", chunk.chunkXPos, chunk.chunkZPos);
				return;
			}
			try{
				//read the existing data out
				dataCompound = CompressedStreamTools.read(file);
			}catch (Throwable e){
				//recover
				dataCompound = new NBTTagCompound();
			}
		}

		//set the new compound in the NBT
		dataCompound.setTag(getPNDIdentifier(chunk), compound);

		if (flushImmediate){
			File file = getFileFromChunk(world, chunk, true);
			if (file == null || (!file.canWrite() && !file.setWritable(true)) || (!file.canRead() && !file.setReadable(true))){
				FMLLog.severe("Ars Magica 2 >> Unable to obtain file handle!  The power system data for the chunk at %d, %d will NOT be saved!  To fix this, make sure you have read/write access to the Minecraft instance folder.", chunk.chunkXPos, chunk.chunkZPos);
				return;
			}
			try{
				//write the modified compound back to the file
				CompressedStreamTools.write(dataCompound, file);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private NBTTagCompound LoadNBTFromFile(World world, ChunkCoordIntPair chunk){

		RegionCoordinates rc = new RegionCoordinates(chunk, world.provider.dimensionId);

		NBTTagCompound dataCompound = dataCache.get(rc);

		if (dataCompound == null){
			File file = getFileFromChunk(world, chunk, false);
			if (file == null){
				return null;
			}
			if ((!file.canRead() && !file.setReadable(true))){
				FMLLog.severe("Ars Magica 2 >> Unable to obtain readable file handle!  The power system data for the chunk at %d, %d will NOT be saved!  To fix this, make sure you have read access to the Minecraft instance folder.", chunk.chunkXPos, chunk.chunkZPos);
				return null;
			}

			try{
				//read the existing data out
				dataCompound = CompressedStreamTools.read(file);
			}catch (Throwable e){
				//recover
				dataCompound = new NBTTagCompound();
			}

			dataCache.put(rc, dataCompound);
		}

		if (dataCompound == null){
			dataCompound = new NBTTagCompound();
			dataCache.put(rc, dataCompound);
		}

		NBTTagCompound innerCompound = dataCompound.getCompoundTag(getPNDIdentifier(chunk));
		return innerCompound;
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event){
		if (!event.world.isRemote && PowerNodeRegistry.For(event.world).hasDataForChunk(event.getChunk())){
			NBTTagCompound dataCompound = new NBTTagCompound();
			PowerNodeRegistry.For(event.world).SaveChunkToNBT(event.getChunk().getChunkCoordIntPair(), dataCompound);
			PowerNodeRegistry.For(event.world).unloadChunk(event.getChunk());
			SaveNBTToFile(event.world, event.getChunk().getChunkCoordIntPair(), dataCompound, false);
		}
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event){
		World world = event.world;

		if (world.isRemote)
			return;

		HashMap<ChunkCoordIntPair, NBTTagCompound> saveData = PowerNodeRegistry.For(world).saveAll();
		for (ChunkCoordIntPair pair : saveData.keySet()){
			SaveNBTToFile(world, pair, saveData.get(pair), AMCore.config.savePowerDataOnWorldSave());
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event){
		World world = event.world;
		saveWorldToFile(world);
	}

	private void cacheToFile(World world, RegionCoordinates coords){
		NBTTagCompound cachedRegion = dataCache.get(coords);
		if (!cachedRegion.hasKey("AM2PowerData"))
			return;
		int xBase = coords.x * 32;
		int zBase = coords.z * 32;
		for (int x = 0; x < 32; ++x){
			for (int z = 0; z < 32; ++z){
				ChunkCoordIntPair pair = new ChunkCoordIntPair(xBase + x, zBase + z);
				NBTTagCompound compound = cachedRegion.getCompoundTag(getPNDIdentifier(pair));
				if (compound != null){
					SaveNBTToFile(world, pair, compound, true);
				}
			}
		}
	}

	public void saveWorldToFile(World world){
		if (world.isRemote)
			return;

		FMLLog.finer("Ars Magica 2 >> Saving all cached power data for DIM %d to disk", world.provider.dimensionId);

		//cached data to file
		Iterator<RegionCoordinates> it = dataCache.keySet().iterator();
		while (it.hasNext()){
			RegionCoordinates rc = it.next();
			if (rc.dimension == world.provider.dimensionId){
				it.remove();
			}
		}

		//live data to file (may override cache, but that's what we want as live would be newer)
		HashMap<ChunkCoordIntPair, NBTTagCompound> saveData = PowerNodeRegistry.For(world).saveAll();
		for (ChunkCoordIntPair pair : saveData.keySet()){
			SaveNBTToFile(world, pair, saveData.get(pair), true);
		}
		PowerNodeRegistry.For(world).unloadAll();
		saveDirs.remove(world.provider.dimensionId);
	}

	private String getPNDIdentifier(ChunkCoordIntPair chunk){
		return String.format(pndID, chunk.chunkXPos, chunk.chunkZPos);
	}
}
