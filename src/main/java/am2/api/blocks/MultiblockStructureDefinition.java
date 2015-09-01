package am2.api.blocks;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class MultiblockStructureDefinition{
	public class BlockDec{
		Block block;
		int meta;

		public BlockDec(Block block, int meta){
			this.block = block;
			this.meta = meta;
		}

		public Block getBlock(){
			return block;
		}

		public int getMeta(){
			return meta;
		}

		@Override
		public String toString() {
			String blockName = "";
			if (block != null){
				blockName = block.getLocalizedName();
			}else{
				blockName = "Unknown";
			}
			return String.format("Block: %s, meta: %d", blockName, meta);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof BlockDec){
				return this.block == ((BlockDec)obj).block && (this.meta == -1 || ((BlockDec)obj).meta == -1 || this.meta == ((BlockDec)obj).meta);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Block.getIdFromBlock(block);
		}
	}

	public class BlockCoord implements Comparable<BlockCoord>{
		public int x;
		public int y;
		public int z;

		public BlockCoord(int offsetX, int offsetY, int offsetZ){
			this.x = offsetX;
			this.y = offsetY;
			this.z = offsetZ;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof BlockCoord){
				return this.x == ((BlockCoord)obj).x && this.y == ((BlockCoord)obj).y && this.z == ((BlockCoord)obj).z;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.x + this.y + this.z;
		}


		public int getX(){
			return this.x;
		}

		public int getY(){
			return this.y;
		}

		public int getZ(){
			return this.z;
		}

		@Override
		public String toString() {
			return String.format("BlockCoord: %d, %d, %d", x, y, z);
		}

		@Override
		public int compareTo(BlockCoord o) {
			return this.z > o.z ? 1 : this.z < o.z ? -1 : this.x > o.x ? 1 : this.x < o.x ? -1 : this.y > o.y ? 1 : this.y < o.y ? -1 : 0;
		}
	}

	public class StructureGroup
	{
		String name;
		int mutex;
		HashMap<BlockCoord, ArrayList<BlockDec>> allowedBlocks;

		public StructureGroup(String name, int mutex){
			this.name = name;
			this.mutex = mutex;
			allowedBlocks = new HashMap<BlockCoord, ArrayList<BlockDec>>();
		}

		void addAllowedBlock(int offsetX, int offsetY, int offsetZ, Block block, int meta){
			BlockCoord originOffset = new BlockCoord(offsetX, offsetY, offsetZ);
			if (!allowedBlocks.containsKey(originOffset)){
				allowedBlocks.put(originOffset, new ArrayList<BlockDec>());
			}
			ArrayList<BlockDec> positionReplacements = allowedBlocks.get(originOffset);
			positionReplacements.add(new BlockDec(block, meta));
		}

		ArrayList<BlockDec> getAllowedBlocksAt(BlockCoord coord){
			return allowedBlocks.get(coord);
		}

		boolean matchGroup(World world, int originX, int originY, int originZ){
			for (BlockCoord offset : allowedBlocks.keySet()){
				Block block = world.getBlock(originX + offset.x, originY + offset.y, originZ + offset.z);
				int meta = world.getBlockMetadata(originX + offset.x, originY + offset.y, originZ + offset.z);
				ArrayList<BlockDec> positionReplacements = allowedBlocks.get(offset);
				boolean valid = false;
				for (BlockDec bd : positionReplacements){
					if (bd.block == block && (bd.meta == -1 || bd.meta == meta)){
						valid = true;
					}
				}
				if (!valid) return false;
			}
			return true;
		}

		HashMap<BlockCoord, ArrayList<BlockDec>> getStructureLayer(int layer){
			HashMap<BlockCoord, ArrayList<BlockDec>> toReturn = new HashMap<MultiblockStructureDefinition.BlockCoord, ArrayList<BlockDec>>();

			if (layer > getMaxLayer() || layer < getMinLayer()){
				return toReturn;
			}

			for (BlockCoord bc : allowedBlocks.keySet()){
				if (bc.y == layer){
					toReturn.put(bc, allowedBlocks.get(bc));
				}
			}
			return toReturn;
		}

		public void replaceAllBlocksOfType(Block originalBlock, Block newBlock){
			replaceAllBlocksOfType(originalBlock, -1, newBlock, -1);
		}

		public void replaceAllBlocksOfType(Block originalBlock, int originalMeta, Block newBlock, int newMeta){
			for (BlockCoord bc : allowedBlocks.keySet()){
				for (BlockDec bd : allowedBlocks.get(bc)){
					if (bd.block == originalBlock){
						if (bd.meta == originalMeta || originalMeta == -1){
							bd.block = newBlock;
							if (newMeta != -1){
								bd.meta = newMeta;
							}
						}
					}
				}
			}
		}

		public HashMap<BlockCoord, ArrayList<BlockDec>> getAllowedBlocks(){
			return (HashMap<BlockCoord, ArrayList<BlockDec>>) allowedBlocks.clone();
		}

		public void deleteBlocksFromWorld(World world, int x, int y, int z) {
			for (BlockCoord offset : allowedBlocks.keySet()){
				world.setBlockToAir(x + offset.x, y + offset.y, z + offset.z);
			}
		}
	}

	private StructureGroup mainGroup;
	private ArrayList<StructureGroup> blockGroups;
	private ArrayList<Integer> mutexCache;

	public static final int MAINGROUP_MUTEX = 1;

	private String id;

	private int maxX = 0;
	private int minX = 0;
	private int maxY = 0;
	private int minY = 0;
	private int maxZ = 0;
	private int minZ = 0;

	public MultiblockStructureDefinition(String id){
		blockGroups = new ArrayList<StructureGroup>();
		mutexCache = new ArrayList<Integer>();
		this.id = id;

		//default group
		mainGroup = createGroup("main", MAINGROUP_MUTEX);
	}

	public String getID(){
		return this.id;
	}

	public ArrayList<Integer> getMutexList(){
		return mutexCache;
	}

	public ArrayList<StructureGroup> getGroupsForMutex(int mutex){
		ArrayList<StructureGroup> toReturn = new ArrayList<StructureGroup>();

		for (StructureGroup group : blockGroups){
			if (group.mutex == mutex){
				toReturn.add(group);
			}
		}

		return toReturn;
	}

	public ArrayList<BlockDec> getAllowedBlocksAt(StructureGroup group, BlockCoord coord){
		return group.getAllowedBlocksAt(coord);
	}

	public ArrayList<BlockDec> getAllowedBlocksAt(BlockCoord coord){
		return mainGroup.getAllowedBlocksAt(coord);
	}

	public void addAllowedBlock(int offsetX, int offsetY, int offsetZ, Block block, int meta){

		if (offsetY > maxY){
			maxY = offsetY;
		}else if (offsetY < minY){
			minY = offsetY;
		}

		if (offsetX > maxX){
			maxX = offsetX;
		}else if (offsetX < minX){
			minX = offsetX;
		}

		if (offsetZ > maxZ){
			maxZ = offsetZ;
		}else if (offsetZ < minZ){
			minZ = offsetZ;
		}

		mainGroup.addAllowedBlock(offsetX, offsetY, offsetZ, block, meta);
	}

	public void addAllowedBlock(int offsetX, int offsetY, int offsetZ, Block block){
		addAllowedBlock(offsetX, offsetY, offsetZ, block, -1);
	}

	public void addAllowedBlock(StructureGroup group, int offsetX, int offsetY, int offsetZ, Block block, int meta){
		if (!blockGroups.contains(group)){
			blockGroups.add(group);
		}

		if (offsetY > maxY){
			maxY = offsetY;
		}else if (offsetY < minY){
			minY = offsetY;
		}

		if (offsetX > maxX){
			maxX = offsetX;
		}else if (offsetX < minX){
			minX = offsetX;
		}

		if (offsetZ > maxZ){
			maxZ = offsetZ;
		}else if (offsetZ < minZ){
			minZ = offsetZ;
		}

		group.addAllowedBlock(offsetX, offsetY, offsetZ, block, meta);
	}

	public void addAllowedBlock(StructureGroup group, int offsetX, int offsetY, int offsetZ, Block block){
		addAllowedBlock(group, offsetX, offsetY, offsetZ, block, -1);
	}

	public StructureGroup createGroup(String name, int mutex){
		if (!mutexCache.contains(mutex)){
			mutexCache.add(mutex);
		}
		StructureGroup group = new StructureGroup(name, mutex);
		blockGroups.add(group);
		return group;
	}

	public StructureGroup copyGroup(String originalName, String destinationName, int newMutex){
		StructureGroup copyGroup = null;
		for (StructureGroup group : blockGroups){
			if (group.name.equals(originalName)){
				copyGroup = group;
				break;
			}
		}

		if (copyGroup == null) return null;

		StructureGroup newGroup;

		if (newMutex > -1){
			newGroup = new StructureGroup(destinationName, newMutex);
		}else{
			newGroup = new StructureGroup(destinationName, copyGroup.mutex);
		}

		for (BlockCoord bc : copyGroup.allowedBlocks.keySet()){
			for (BlockDec bd : copyGroup.allowedBlocks.get(bc)){
				newGroup.addAllowedBlock(bc.x, bc.y, bc.z, bd.block, bd.meta);
			}
		}

		blockGroups.add(newGroup);

		return newGroup;
	}

	public StructureGroup copyGroup(String originalName, String destinationName){
		return copyGroup(originalName, destinationName, -1);
	}

	public ArrayList<StructureGroup> getMatchedGroups(int mutex, World world, int originX, int originY, int originZ){
		ArrayList<StructureGroup> toReturn = new ArrayList<StructureGroup>();
		for (StructureGroup group : blockGroups){
			if ((group.mutex & mutex) == group.mutex){
				if (group.matchGroup(world, originX, originY, originZ)){
					toReturn.add(group);
				}
			}
		}
		return toReturn;
	}

	private boolean matchMutex(int mutex, World world, int originX, int originY, int originZ){
		for (StructureGroup group : blockGroups){
			if ((group.mutex & mutex) == group.mutex){
				if (group.matchGroup(world, originX, originY, originZ)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkStructure(World world, int originX, int originY, int originZ){
		boolean valid = true;
		for (int i: mutexCache){
			valid &= matchMutex(i, world, originX, originY, originZ);
			if (!valid) break;
		}
		return valid;
	}

	public int getMinLayer(){
		return this.minY;
	}

	public int getMaxLayer(){
		return this.maxY;
	}

	public int getHeight(){
		return this.maxY - this.minY;
	}

	public int getWidth(){
		return this.maxX - this.minX;
	}

	public int getLength(){
		return this.maxZ - this.minZ;
	}

	public HashMap<BlockCoord, ArrayList<BlockDec>> getStructureLayer(int layer){
		return mainGroup.getStructureLayer(layer);
	}

	public HashMap<BlockCoord, ArrayList<BlockDec>> getStructureLayer(StructureGroup group, int layer){
		return group.getStructureLayer(layer);
	}

	public void removeMutex(int mutex, World world, int x, int y, int z){
		for (StructureGroup group : blockGroups){
			if (group.mutex == mutex){
				group.deleteBlocksFromWorld(world, x, y, z);
			}
		}
	}
}
