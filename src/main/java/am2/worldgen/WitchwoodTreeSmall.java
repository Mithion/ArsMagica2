package am2.worldgen;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class WitchwoodTreeSmall extends WorldGenAbstractTree{
  
  private final int minTreeHeight = 4;
  
  private final Block logBlock = BlocksCommonProxy.witchwoodLog;
  private final int logMeta = 0;
  private final Block leafBlock = BlocksCommonProxy.witchwoodLeaves;
  private final int leafMeta = 0;
  
  // this function is copied from WorldGenTrees.java
  // variable names have been changed for readability, and some un-used parts (vine, cocoa generation) have been removed
  public boolean generate(World world, Random random, BlockPos pos){
	  int height = random.nextInt(3) + this.minTreeHeight;
	  boolean isValidPlantingSpot = true;
	  
	  if (pos.getY() >= 1 && pos.getY() + height + 1 <= 256){
		  byte leafMaxRadius;
		  Block block;
		  
		  for (int j = pos.getY(); j <= pos.getY() + 1 + height; j++){
			  leafMaxRadius = 1;
			  
			  if (j == pos.getY()){
				  leafMaxRadius = 0;
			  }
			  
			  if (j >= pos.getY() + 1 + height - 2){
				  leafMaxRadius = 2;
			  }
			  
			  for (int i = pos.getX() - leafMaxRadius; i <= pos.getX() + leafMaxRadius && isValidPlantingSpot; i++){
				  for (int k = pos.getZ() - leafMaxRadius; k <= pos.getZ() + leafMaxRadius && isValidPlantingSpot; k++){
					  if (j >= 0 && j < 256){
						  block = world.getBlockState(pos).getBlock();
						  
						  if (!this.isReplaceable(world, pos)){
							  isValidPlantingSpot = false;
						  }
					  }
					  else{
						  isValidPlantingSpot = false;
					  }
				  }
			  }
		  }
		  
		  if (!isValidPlantingSpot){
			  return false;
		  }
		  else{
			  Block block2 = world.getBlockState(pos.down()).getBlock();
			  
			  boolean isSoil = block2.canSustainPlant(world, pos.down(), EnumFacing.UP, (BlockSapling)Blocks.sapling);
			  if (isSoil && pos.getY() < 256 - height - 1){
				  block2.onPlantGrow(world, pos.down(), pos);
				  leafMaxRadius = 3;
				  byte b1 = 0;
				  
				  for (int j = pos.getY() - leafMaxRadius + height; j <= pos.getY() + height; j++){
					  int treeHeightLayer = j - (pos.getY() + height);
					  int leafGenRadius = b1 + 1 - treeHeightLayer / 2;
					  
					  for (int i = pos.getX() - leafGenRadius; i <= pos.getX() + leafGenRadius; i++){
						  int leafGenDeltaX = i - pos.getX();
						  
						  for (int k = pos.getZ() - leafGenRadius; k <= pos.getZ() + leafGenRadius; k++){
							  int leafGenDeltaZ = k - pos.getZ();
							  
							  if (Math.abs(leafGenDeltaX) != leafGenRadius || Math.abs(leafGenDeltaZ) != leafGenRadius || random.nextInt(2) != 0 && treeHeightLayer != 0){
								  Block block1 = world.getBlockState(pos.add(i, j, k)).getBlock();
								  
								  if (block1.isAir(world, pos.add(i, j, k)) || block1.isLeaves(world, pos.add(i, j, k))){
									  this.setBlockAndNotifyAdequately(world, new BlockPos(i, j, k), leafBlock.getStateFromMeta(leafMeta));
								  }
							  }
						  }
					  }
				  }
				  
				  for (int j = 0; j < height; j++){
					  BlockPos up1 = pos.up(j);
					  block = world.getBlockState(up1).getBlock();
					  
					  if (block.isAir(world, up1) || block.isLeaves(world, up1)){
						  this.setBlockAndNotifyAdequately(world, pos.up(j), logBlock.getStateFromMeta(logMeta));
					  }
				  }
				  
				  return true;
			  }
			  else{
				  return false;
			  }
		  }
	  }
	  else{
		  return false;
	  }
  }
  
  
  public WitchwoodTreeSmall(boolean par1){
	super(par1);
  }
  
}
