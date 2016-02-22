package am2.power;

import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class PowerNodePathfinder extends AStar<AMVector3>{

	private World world;
	private AMVector3 start;
	private AMVector3 end;
	private PowerTypes powerType;

	PowerNodePathfinder(World world, AMVector3 start, AMVector3 end, PowerTypes type){
		this.world = world;
		this.start = start;
		this.end = end;
		this.powerType = type;
	}

	private IPowerNode getPowerNode(World world, AMVector3 location){
		if (world.isAreaLoaded(location.toBlockPos(), location.toBlockPos())){
			Chunk chunk = world.getChunkFromBlockCoords(location.toBlockPos());
			if (chunk.isLoaded()){
				TileEntity te = world.getTileEntity(location.toBlockPos());
				if (te instanceof IPowerNode)
					return (IPowerNode)te;
			}
		}
		return null;
	}

	@Override
	protected boolean isGoal(AMVector3 node){
		return node.equals(end);
	}

	@Override
	protected Double g(AMVector3 from, AMVector3 to){
		return from.distanceSqTo(to);
	}

	@Override
	protected Double h(AMVector3 from, AMVector3 to){
		return from.distanceSqTo(to);
	}

	@Override
	protected List<AMVector3> generateSuccessors(AMVector3 node){
		IPowerNode powerNode = getPowerNode(world, node);
		if (powerNode == null)
			return new ArrayList<AMVector3>();

		IPowerNode[] candidates = PowerNodeRegistry.For(world).getAllNearbyNodes(world, node, powerType);

		ArrayList<AMVector3> prunedCandidates = new ArrayList<AMVector3>();
		for (IPowerNode candidate : candidates){
			if (verifyCandidate(candidate)){
				prunedCandidates.add(new AMVector3((TileEntity)candidate));
			}
		}

		return prunedCandidates;
	}

	private boolean verifyCandidate(IPowerNode powerNode){
		if (new AMVector3((TileEntity)powerNode).equals(end)){
			for (PowerTypes type : powerNode.getValidPowerTypes())
				if (type == powerType)
					return true;
		}
		return powerNode.canRelayPower(powerType);
	}
}
