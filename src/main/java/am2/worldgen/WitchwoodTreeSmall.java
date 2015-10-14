package am2.worldgen;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class WitchwoodTreeSmall extends WorldGenAbstractTree{
  
  private final int minTreeHeight = 4;
  
  private final Block logBlock = BlocksCommonProxy.witchwoodLog;
  private final int logMeta = 0;
  private final Block leafBlock = BlocksCommonProxy.witchwoodLeaves;
  private final int leafMeta = 0;
  
  // this function is copied from WorldGenTrees.java
  // variable names have been changed for readability, and some un-used parts (vine, cocoa generation) have been removed
  public boolean generate(World world, Random random, int x, int y, int z)
  {
    int height = random.nextInt(3) + this.minTreeHeight;
    boolean isValidPlantingSpot = true;
    
    if (y >= 1 && y + height + 1 <= 256)
    {
      byte leafMaxRadius;
      Block block;
      
      for (int j = y; j <= y + 1 + height; j++)
      {
	leafMaxRadius = 1;
	
	if (j == y)
	{
	  leafMaxRadius = 0;
	}
	
	if (j >= y + 1 + height - 2)
	{
	  leafMaxRadius = 2;
	}
	
	for (int i = x - leafMaxRadius; i <= x + leafMaxRadius && isValidPlantingSpot; i++)
	{
	  for (int k = z - leafMaxRadius; k <= z + leafMaxRadius && isValidPlantingSpot; k++)
	  {
	    if (j >= 0 && j < 256)
	    {
	      block = world.getBlock(i, j, k);
	      
	      if (!this.isReplaceable(world, i, j, k))
	      {
		isValidPlantingSpot = false;
	      }
	    }
	    else
	    {
	      isValidPlantingSpot = false;
	    }
	  }
	}
      }
      
      if (!isValidPlantingSpot)
      {
	return false;
      }
      else
      {
	Block block2 = world.getBlock(x, y - 1, z);
	
	boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
	if (isSoil && y < 256 - height - 1)
	{
	  block2.onPlantGrow(world, x, y - 1, z, x, y, z);
	  leafMaxRadius = 3;
	  byte b1 = 0;
	  
	  for (int j = y - leafMaxRadius + height; j <= y + height; j++)
	  {
	    int treeHeightLayer = j - (y + height);
	    int leafGenRadius = b1 + 1 - treeHeightLayer / 2;
	    
	    for (int i = x - leafGenRadius; i <= x + leafGenRadius; i++)
	    {
	      int leafGenDeltaX = i - x;
	      
	      for (int k = z - leafGenRadius; k <= z + leafGenRadius; k++)
	      {
		int leafGenDeltaZ = k - z;
		
		if (Math.abs(leafGenDeltaX) != leafGenRadius || Math.abs(leafGenDeltaZ) != leafGenRadius || random.nextInt(2) != 0 && treeHeightLayer != 0)
		{
		  Block block1 = world.getBlock(i, j, k);
		  
		  if (block1.isAir(world, i, j, k) || block1.isLeaves(world, i, j, k))
		  {
		    this.setBlockAndNotifyAdequately(world, i, j, k, leafBlock, leafMeta);
		  }
		}
	      }
	    }
	  }
	  
	  for (int j = 0; j < height; j++)
	  {
	    block = world.getBlock(x, y + j, z);
	    
	    if (block.isAir(world, x, y + j, z) || block.isLeaves(world, x, y + j, z))
	    {
	      this.setBlockAndNotifyAdequately(world, x, y + j, z, logBlock, logMeta);
	    }
	  }
	  
	  return true;
	}
	else
	{
	  return false;
	}
      }
    }
    else
    {
      return false;
    }
  }
  
  
  public WitchwoodTreeSmall(boolean par1){
    super(par1);
  }
  
}
