package am2;

import am2.api.math.AMVector3;
import am2.blocks.tileentities.flickers.FlickerOperatorMoonstoneAttractor;
import am2.entities.EntityThrownRock;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Random;

public class MeteorSpawnHelper{
	private final Random rand = new Random();
	private int ticksSinceLastMeteor = 0;

	public static MeteorSpawnHelper instance = new MeteorSpawnHelper();

	public void tick(){
		if (ticksSinceLastMeteor == 0){
			if (MinecraftServer.getServer().worldServers.length < 1) return;
			WorldServer ws = MinecraftServer.getServer().worldServers[0];
			if (rand.nextInt(2500 + (1000 * ws.provider.getMoonPhase(ws.provider.getWorldTime()))) == 0){
				spawnMeteor();
			}
		}else{
			ticksSinceLastMeteor--;
		}
	}

	public void spawnMeteor(){
		ticksSinceLastMeteor = 48000;
		if (MinecraftServer.getServer().worldServers.length < 1) return;

		WorldServer ws = null;
		for (WorldServer world : MinecraftServer.getServer().worldServers){
			if (world.provider.getDimensionId() == 0){
				ws = world;
				break;
			}
		}
		if (ws == null) return;

		long time = ws.getWorldTime() % 24000;
		if (time > 14500 && time < 21500){ //night time range (just past dusk and just before dawn)
			if (ws.playerEntities.size() < 1) return;

			int playerID = rand.nextInt(ws.playerEntities.size());
			EntityPlayer player = (EntityPlayer)ws.playerEntities.get(playerID);

			if (ExtendedProperties.For(player).getMagicLevel() < AMCore.config.getMeteorMinSpawnLevel()) return;

			AMVector3 spawnCoord = new AMVector3(player);
			boolean found = false;
			int meteorOffsetRadius = 64;

			AMVector3 attractorCoord = FlickerOperatorMoonstoneAttractor.getMeteorAttractor(spawnCoord);
			if (attractorCoord != null){
				spawnCoord = attractorCoord;
				meteorOffsetRadius = 4;
			}
			for (int i = 0; i < 10; ++i){
				AMVector3 offsetCoord = spawnCoord.copy().add(new AMVector3(rand.nextInt(meteorOffsetRadius) - (meteorOffsetRadius / 2), 0, rand.nextInt(meteorOffsetRadius) - (meteorOffsetRadius / 2)));
				offsetCoord.y = correctYCoord(ws, offsetCoord.toBlockPos()).getY();

				if (offsetCoord.y < 0)
					return;

				if (topBlockIsBiomeGeneric(ws, offsetCoord.toBlockPos())){
					spawnCoord = offsetCoord;
					found = true;
					break;
				}
			}

			if (!found) return;

			EntityThrownRock meteor = new EntityThrownRock(ws);
			meteor.setPosition(spawnCoord.x + rand.nextInt(meteorOffsetRadius) - (meteorOffsetRadius / 2), ws.getActualHeight(), spawnCoord.z + rand.nextInt(meteorOffsetRadius) - (meteorOffsetRadius / 2));
			meteor.setMoonstoneMeteor();
			meteor.setMoonstoneMeteorTarget(spawnCoord);
			ws.spawnEntityInWorld(meteor);
		}

	}

	private boolean topBlockIsBiomeGeneric(World world, BlockPos pos){
		if (world == null)
			return false;

		pos = correctYCoord(world, pos);

		if (pos.getY() < 0) return false;

		BiomeGenBase biome = world.getBiomeGenForCoords(pos);

		Block block = world.getBlockState(pos).getBlock();

		return (block == Blocks.obsidian || block == biome.topBlock) && world.canBlockSeeSky(pos.up());
	}

	private BlockPos correctYCoord(World world, BlockPos pos){
		if (world == null)
			return pos;

		while (pos.getY() < world.getActualHeight() && world.canBlockSeeSky(pos))
			pos = pos.up();

		while (world.isAirBlock(pos) && pos.getY() > -1)
			pos = pos.down();

		return pos;
	}
}
