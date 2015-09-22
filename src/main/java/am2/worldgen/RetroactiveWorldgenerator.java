package am2.worldgen;

import am2.AMCore;
import am2.LogHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class RetroactiveWorldgenerator{
	public static final HashMap<Integer, ArrayList<ChunkCoordIntPair>> deferredChunkGeneration = new HashMap<Integer, ArrayList<ChunkCoordIntPair>>();
	public static final RetroactiveWorldgenerator instance = new RetroactiveWorldgenerator();

	public void continueRetrogen(World world){
		if (world == null)
			return;

		int dimensionID = world.provider.dimensionId;

		int count = 0;
		ArrayList<ChunkCoordIntPair> chunks = deferredChunkGeneration.get(dimensionID);
		if (chunks != null && chunks.size() > 0){
			int amt = Math.min(5, chunks.size());
			for (int i = 0; i < amt; ++i){
				count++;
				ChunkCoordIntPair chunkPos = chunks.get(0);
				AMCore.proxy.worldGen.generate(world.rand, chunkPos.chunkXPos, chunkPos.chunkZPos, world, world.getChunkProvider(), world.getChunkProvider());
				chunks.remove(0);
			}

			deferredChunkGeneration.put(dimensionID, chunks);

			LogHelper.info("Retro-genned %d chunks, %d left to generate.", count, chunks.size());
		}
	}
}
