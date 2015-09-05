package am2.navigation;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public final class PathFinder{
	public static boolean showPaths = true;

	public static BreadCrumb FindPath(World world, Point3D start, Point3D end, Entity entity) throws Exception{
		//note we just flip start and end here so you don't have to.
		BreadCrumb bc = FindPathReversed(world, end, start, entity);
		return bc;
	}

	public static BreadCrumb FindPath(World world, Point3D start, Point3D end) throws Exception{
		return FindPath(world, start, end, null);
	}

	private static BreadCrumb FindPathReversed(World world, Point3D start, Point3D end, Entity entity) throws Exception{
		MinHeap openList = new MinHeap(256);

		//bounds checking - are the points within a valid distance?
		if (Math.abs(end.x) - Math.abs(start.x) > 19 || Math.abs(end.y) - Math.abs(start.y) > 19 || Math.abs(end.z) - Math.abs(start.z) > 19){
			return null;
		}

		BreadCrumb[][][] brWorld = new BreadCrumb[25][25][25];
		BreadCrumb node;
		Point3D tmp;
		int cost;
		int diff;

		//shift the coordinates so that they are based on a zero-index (this is because we use the point coordinates as an index into the path array)
		if (start.x < end.x){
			end.shiftX(start.shiftX());
		}else{
			start.shiftX(end.shiftX());
		}

		if (start.y < end.y){
			end.shiftY(start.shiftY());
		}else{
			start.shiftY(end.shiftY());
		}

		if (start.z < end.z){
			end.shiftZ(start.shiftZ());
		}else{
			start.shiftZ(end.shiftZ());
		}

		//start the path
		BreadCrumb current = new BreadCrumb(start);
		//origin point has 0 cost
		current.cost = 0;

		//create the end point
		BreadCrumb finish = new BreadCrumb(end);

		//set the start point into the path array
		brWorld[current.position.x][current.position.y][current.position.z] = current;
		//add the current point into the open list
		openList.Add(current);

		//loop as long as we have have items in the open list (points we haven't checked yet)
		while (openList.Count() > 0){
			//Find best item and switch it to the 'closedList' - best being the item currently on the open list with the lowest cost
			current = openList.ExtractFirst();
			current.onClosedList = true;

			//Find neighbours
			for (int i = 0; i < surrounding.length; i++){
				tmp = current.position.add(surrounding[i]);
				if (tmp.x < 0 || tmp.y < 0 || tmp.z < 0 || tmp.x > 20 || tmp.y > 20 || tmp.z > 20){
					continue;
				}
				if (BlockIsNavigateable(world, tmp, current.position, entity)){
					//Check if we've already examined a neighbour, if not create a new node for it.
					if (brWorld[tmp.x][tmp.y][tmp.z] == null){
						node = new BreadCrumb(tmp);
						brWorld[tmp.x][tmp.y][tmp.z] = node;
					}else{
						node = brWorld[tmp.x][tmp.y][tmp.z];
					}

					//If the node is not on the 'closedList' check it's new score, keep the best
					if (!node.onClosedList){
						diff = 0;
						if (current.position.x != node.position.x){
							diff += 1;
						}
						if (current.position.y != node.position.y){
							diff += 1;
						}
						if (current.position.z != node.position.z){
							diff += 1;
						}
						cost = (int)(current.cost + diff + node.position.GetDistanceSq(end));

						if (cost < node.cost){
							node.cost = cost;
							node.next = current;
						}

						//If the node wasn't on the openList yet, add it
						if (!node.onOpenList){
							//Check to see if we're done
							if (node.equals(finish)){
								node.next = current;
								return node;
							}
							node.onOpenList = true;
							openList.Add(node);
						}
					}
				}
			}
		}
		return null; //no path found
	}

	private static boolean BlockIsNavigateable(World world, Point3D point, Point3D Current, Entity entity){
		int height = (int)Math.ceil((entity == null) ? 1f : entity.height);

		Point3D unshifted = point.Unshift();
		Point3D currentUnshifted = Current.Unshift();
		boolean allBlocksAreValid = true;

		if (!isDiagonalMovement(point, Current)){
			Block[] blocks = new Block[height];
			for (int i = 0; i < height; ++i){
				Block block = world.getBlock(unshifted.x, unshifted.y + i, unshifted.z);
				blocks[i] = block;
			}
			for (Block b : blocks){
				boolean currentBlockValid = false;
				for (Block i : validBlockIDs){
					if (b == i){
						currentBlockValid = true;
					}
				}
				allBlocksAreValid &= currentBlockValid;
			}
		}else{
			Block[] blocks = new Block[height * 4];
			for (int i = 0; i < height; i += 4){
				int deltaX = unshifted.x - currentUnshifted.x;
				int deltaY = unshifted.y - currentUnshifted.y;
				int deltaZ = unshifted.z - currentUnshifted.z;
				Block block = world.getBlock(unshifted.x, unshifted.y + i, unshifted.z);
				blocks[i] = block;
				block = world.getBlock(unshifted.x + deltaX, unshifted.y + i, unshifted.z);
				blocks[i + 1] = block;
				block = world.getBlock(unshifted.x, unshifted.y + i, unshifted.z + deltaZ);
				blocks[i + 2] = block;
				block = world.getBlock(unshifted.x, unshifted.y + i + deltaY, unshifted.z);
				blocks[i + 3] = block;
			}
			for (Block b : blocks){
				boolean currentBlockValid = false;
				for (Block i : validBlockIDs){
					if (b == i){
						currentBlockValid = true;
					}
				}
				allBlocksAreValid &= currentBlockValid;
			}
		}
		return allBlocksAreValid;
	}

	private static boolean isDiagonalMovement(Point3D start, Point3D end){
		boolean hasDeltaX = end.x - start.x != 0;
		boolean hasDeltaY = end.y - start.y != 0;
		boolean hasDeltaZ = end.z - start.z != 0;

		return (hasDeltaX && hasDeltaY) || (hasDeltaX && hasDeltaZ) || (hasDeltaZ && hasDeltaY) || (hasDeltaX && hasDeltaY && hasDeltaZ);
	}

	private static Block[] validBlockIDs = new Block[]{
			Blocks.air,
			Blocks.ladder,
			Blocks.deadbush,
			Blocks.red_flower,
			Blocks.yellow_flower,
			Blocks.rail,
			Blocks.stone_button,
			Blocks.wooden_button,
			Blocks.lever,
			Blocks.detector_rail,
			Blocks.torch,
			Blocks.vine
	};

	private static Point3D[] surrounding = new Point3D[]{
			//Top slice (Y=1)
			new Point3D(-1, 1, 1), new Point3D(0, 1, 1), new Point3D(1, 1, 1),
			new Point3D(-1, 1, 0), new Point3D(0, 1, 0), new Point3D(1, 1, 0),
			new Point3D(-1, 1, -1), new Point3D(0, 1, -1), new Point3D(1, 1, -1),
			//Middle slice (Y=0)
			new Point3D(-1, 0, 1), new Point3D(0, 0, 1), new Point3D(1, 0, 1),
			new Point3D(-1, 0, 0), new Point3D(1, 0, 0), //(0,0,0) is self
			new Point3D(-1, 0, -1), new Point3D(0, 0, -1), new Point3D(1, 0, -1),
			//Bottom slice (Y=-1)
			new Point3D(-1, -1, 1), new Point3D(0, -1, 1), new Point3D(1, -1, 1),
			new Point3D(-1, -1, 0), new Point3D(0, -1, 0), new Point3D(1, -1, 0),
			new Point3D(-1, -1, -1), new Point3D(0, -1, -1), new Point3D(1, -1, -1)
	};
}
