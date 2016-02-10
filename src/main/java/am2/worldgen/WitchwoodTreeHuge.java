package am2.worldgen;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class WitchwoodTreeHuge extends WorldGenAbstractTree{
	/**
	 * Contains three sets of two values that provide complimentary indices for a given 'major' index - 1 and 2 for 0, 0
	 * and 2 for 1, and 0 and 1 for 2.
	 */
	static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};

	/**
	 * random seed for GenBigTree
	 */
	Random rand = new Random();

	/**
	 * Reference to the World object.
	 */
	World worldObj;
	int[] basePos = new int[]{0, 0, 0};
	int heightLimit;
	int height;
	double heightAttenuation = 0.818D;
	double branchDensity = 1.6D;
	double branchSlope = 0.68D;
	double scaleWidth = 1.0D;
	double leafDensity = 1.0D;

	/**
	 * Currently always 1, can be set to 2 in the class constructor to generate a double-sized tree trunk for big trees.
	 */
	int trunkSize = 1;

	/**
	 * Sets the limit of the random value used to initialize the height limit.
	 */
	int heightLimitLimit = 14;

	/**
	 * Sets the distance limit for how far away the generator will populate leaves from the base leaf node.
	 */
	int leafDistanceLimit = 5;

	/**
	 * Contains a list of a points at which to generate groups of leaves.
	 */
	int[][] leafNodes;

	public WitchwoodTreeHuge(boolean par1){
		super(par1);
	}

	/**
	 * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
	 */
	void generateLeafNodeList(){
		this.height = (int)((double)this.heightLimit * this.heightAttenuation);

		if (this.height >= this.heightLimit){
			this.height = this.heightLimit - 1;
		}

		int i = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));

		if (i < 1){
			i = 1;
		}

		int[][] aint = new int[i * this.heightLimit][4];
		int j = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
		int k = 1;
		int l = this.basePos[1] + this.height;
		int i1 = j - this.basePos[1];
		aint[0][0] = this.basePos[0];
		aint[0][1] = j;
		aint[0][2] = this.basePos[2];
		aint[0][3] = l;
		--j;

		while (i1 >= 0){
			int j1 = 0;
			float f = this.layerSize(i1);

			if (f < 0.0F){
				--j;
				--i1;
			}else{
				for (double d0 = 0.5D; j1 < i; ++j1){
					double d1 = this.scaleWidth * (double)f * ((double)this.rand.nextFloat() + 0.328D);
					double d2 = (double)this.rand.nextFloat() * 2.0D * Math.PI;
					int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + (double)this.basePos[0] + d0);
					int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + (double)this.basePos[2] + d0);
					int[] aint1 = new int[]{k1, j, l1};
					int[] aint2 = new int[]{k1, j + this.leafDistanceLimit, l1};

					if (this.checkBlockLine(aint1, aint2) == -1){
						int[] aint3 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
						double d3 = Math.sqrt(Math.pow((double)Math.abs(this.basePos[0] - aint1[0]), 2.0D) + Math.pow((double)Math.abs(this.basePos[2] - aint1[2]), 2.0D));
						double d4 = d3 * this.branchSlope;

						if ((double)aint1[1] - d4 > (double)l){
							aint3[1] = l;
						}else{
							aint3[1] = (int)((double)aint1[1] - d4);
						}

						if (this.checkBlockLine(aint3, aint1) == -1){
							aint[k][0] = k1;
							aint[k][1] = j;
							aint[k][2] = l1;
							aint[k][3] = aint3[1];
							++k;
						}
					}
				}

				--j;
				--i1;
			}
		}

		this.leafNodes = new int[k][4];
		System.arraycopy(aint, 0, this.leafNodes, 0, k);
	}

	void genTreeLayer(int par1, int par2, int par3, float par4, int par5, Block par6){
		int i1 = (int)((double)par4 + 0.618D);
		byte b1 = otherCoordPairs[par5];
		byte b2 = otherCoordPairs[par5 + 3];
		int[] aint = new int[]{par1, par2, par3};
		int[] aint1 = new int[]{0, 0, 0};
		int j1 = -i1;
		int k1 = -i1;

		for (aint1[par5] = aint[par5]; j1 <= i1; ++j1){
			aint1[b1] = aint[b1] + j1;
			k1 = -i1;

			while (k1 <= i1){
				double d0 = Math.pow((double)Math.abs(j1) + 0.5D, 2.0D) + Math.pow((double)Math.abs(k1) + 0.5D, 2.0D);

				if (d0 > (double)(par4 * par4)){
					++k1;
				}else{
					aint1[b2] = aint[b2] + k1;
					Block l1 = this.worldObj.getBlockState(new BlockPos(aint1[0], aint1[1], aint1[2])).getBlock();

					if (l1 != Blocks.air && l1 != BlocksCommonProxy.witchwoodLeaves){
						++k1;
					}else{
						this.setBlockAndNotifyAdequately(this.worldObj, new BlockPos(aint1[0], aint1[1], aint1[2]), par6.getDefaultState());
						++k1;
					}
				}
			}
		}
	}

	/**
	 * Gets the rough size of a layer of the tree.
	 */
	float layerSize(int par1){
		if ((double)par1 < (double)((float)this.heightLimit) * 0.3D){
			return -1.618F;
		}else{
			float f = (float)this.heightLimit / 2.0F;
			float f1 = (float)this.heightLimit / 2.0F - (float)par1;
			float f2;

			if (f1 == 0.0F){
				f2 = f;
			}else if (Math.abs(f1) >= f){
				f2 = 0.0F;
			}else{
				f2 = (float)Math.sqrt(Math.pow((double)Math.abs(f), 2.0D) - Math.pow((double)Math.abs(f1), 2.0D));
			}

			f2 *= 0.5F;
			return f2;
		}
	}

	float leafSize(int par1){
		return par1 >= 0 && par1 < this.leafDistanceLimit ? (par1 != 0 && par1 != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
	}

	/**
	 * Generates the leaves surrounding an individual entry in the leafNodes list.
	 */
	void generateLeafNode(int par1, int par2, int par3){
		int l = par2;

		for (int i1 = par2 + this.leafDistanceLimit; l < i1; ++l){
			float f = this.leafSize(l - par2);
			this.genTreeLayer(par1, l, par3, f, 1, BlocksCommonProxy.witchwoodLeaves);
		}
	}

	/**
	 * Places a line of the specified block ID into the world from the first coordinate triplet to the second.
	 */
	void placeBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger, Block par3){
		int[] aint2 = new int[]{0, 0, 0};
		byte b0 = 0;
		byte b1;

		for (b1 = 0; b0 < 3; ++b0){
			aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];

			if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])){
				b1 = b0;
			}
		}

		if (aint2[b1] != 0){
			byte b2 = otherCoordPairs[b1];
			byte b3 = otherCoordPairs[b1 + 3];
			byte b4;

			if (aint2[b1] > 0){
				b4 = 1;
			}else{
				b4 = -1;
			}

			double d0 = (double)aint2[b2] / (double)aint2[b1];
			double d1 = (double)aint2[b3] / (double)aint2[b1];
			int[] aint3 = new int[]{0, 0, 0};
			int j = 0;

			for (int k = aint2[b1] + b4; j != k; j += b4){
				aint3[b1] = MathHelper.floor_double((double)(par1ArrayOfInteger[b1] + j) + 0.5D);
				aint3[b2] = MathHelper.floor_double((double)par1ArrayOfInteger[b2] + (double)j * d0 + 0.5D);
				aint3[b3] = MathHelper.floor_double((double)par1ArrayOfInteger[b3] + (double)j * d1 + 0.5D);
				byte b5 = 0;
				int l = Math.abs(aint3[0] - par1ArrayOfInteger[0]);
				int i1 = Math.abs(aint3[2] - par1ArrayOfInteger[2]);
				int j1 = Math.max(l, i1);

				if (j1 > 0){
					if (l == j1){
						b5 = 4;
					}else if (i1 == j1){
						b5 = 8;
					}
				}

				this.setBlockAndNotifyAdequately(this.worldObj, aint3[0], aint3[1], aint3[2], par3, b5);
			}
		}
	}

	/**
	 * Generates the leaf portion of the tree as specified by the leafNodes list.
	 */
	void generateLeaves(){
		int i = 0;

		for (int j = this.leafNodes.length; i < j; ++i){
			int k = this.leafNodes[i][0];
			int l = this.leafNodes[i][1];
			int i1 = this.leafNodes[i][2];
			this.generateLeafNode(k, l, i1);
		}
	}

	/**
	 * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
	 */
	boolean leafNodeNeedsBase(int par1){
		return (double)par1 >= (double)this.heightLimit * 0.2D;
	}

	/**
	 * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
	 * field that is always 1 to 2.
	 */
	void generateTrunk(){
		int i = this.basePos[0];
		int j = this.basePos[1];
		int k = this.basePos[1] + this.height;
		int l = this.basePos[2];
		int[] aint = new int[]{i, j, l};
		int[] aint1 = new int[]{i, k, l};
		this.placeBlockLine(aint, aint1, BlocksCommonProxy.witchwoodLog);

		if (this.trunkSize == 2){
			++aint[0];
			++aint1[0];
			this.placeBlockLine(aint, aint1, BlocksCommonProxy.witchwoodLog);
			++aint[2];
			++aint1[2];
			this.placeBlockLine(aint, aint1, BlocksCommonProxy.witchwoodLog);
			aint[0] += -1;
			aint1[0] += -1;
			this.placeBlockLine(aint, aint1, BlocksCommonProxy.witchwoodLog);
		}
	}

	/**
	 * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
	 */
	void generateLeafNodeBases(){
		int i = 0;
		int j = this.leafNodes.length;

		for (int[] aint = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; i < j; ++i){
			int[] aint1 = this.leafNodes[i];
			int[] aint2 = new int[]{aint1[0], aint1[1], aint1[2]};
			aint[1] = aint1[3];
			int k = aint[1] - this.basePos[1];

			if (this.leafNodeNeedsBase(k)){
				this.placeBlockLine(aint, aint2, BlocksCommonProxy.witchwoodLog);
			}
		}
	}

	/**
	 * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
	 * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
	 */
	int checkBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger){
		int[] aint2 = new int[]{0, 0, 0};
		byte b0 = 0;
		byte b1;

		for (b1 = 0; b0 < 3; ++b0){
			aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];

			if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])){
				b1 = b0;
			}
		}

		if (aint2[b1] == 0){
			return -1;
		}else{
			byte b2 = otherCoordPairs[b1];
			byte b3 = otherCoordPairs[b1 + 3];
			byte b4;

			if (aint2[b1] > 0){
				b4 = 1;
			}else{
				b4 = -1;
			}

			double d0 = (double)aint2[b2] / (double)aint2[b1];
			double d1 = (double)aint2[b3] / (double)aint2[b1];
			int[] aint3 = new int[]{0, 0, 0};
			int i = 0;
			int j;

			for (j = aint2[b1] + b4; i != j; i += b4){
				aint3[b1] = par1ArrayOfInteger[b1] + i;
				aint3[b2] = MathHelper.floor_double((double)par1ArrayOfInteger[b2] + (double)i * d0);
				aint3[b3] = MathHelper.floor_double((double)par1ArrayOfInteger[b3] + (double)i * d1);
				Block k = this.worldObj.getBlock(aint3[0], aint3[1], aint3[2]);

				if (k != Blocks.air && k != BlocksCommonProxy.witchwoodLeaves){
					break;
				}
			}

			return i == j ? -1 : Math.abs(i);
		}
	}

	/**
	 * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
	 * limit, is valid.
	 */
	boolean validTreeLocation(){
		int[] aint = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
		int[] aint1 = new int[]{this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2]};
		Block soil = this.worldObj.getBlock(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);

		boolean isValidSoil = (soil != null && soil.canSustainPlant(worldObj, basePos[0], basePos[1] - 1, basePos[2], ForgeDirection.UP, (BlockSapling)Blocks.sapling));
		if (!isValidSoil){
			return false;
		}else{
			int j = this.checkBlockLine(aint, aint1);

			if (j == -1){
				return true;
			}else if (j < 6){
				return false;
			}else{
				this.heightLimit = j;
				return true;
			}
		}
	}

	/**
	 * Rescales the generator settings, only used in WorldGenBigTree
	 */
	public void setScale(double par1, double par3, double par5){
		this.heightLimitLimit = (int)(par1 * 12.0D);

		if (par1 > 0.5D){
			this.leafDistanceLimit = 5;
		}

		this.scaleWidth = par3;
		this.leafDensity = par5;
	}

	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5){
		this.worldObj = par1World;
		long l = par2Random.nextLong();
		this.rand.setSeed(l);
		this.basePos[0] = par3;
		this.basePos[1] = par4;
		this.basePos[2] = par5;

		if (this.heightLimit == 0){
			this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
		}

		if (!this.validTreeLocation()){
			return false;
		}else{
			this.generateLeafNodeList();
			this.generateLeaves();
			this.generateTrunk();
			this.generateLeafNodeBases();
			return true;
		}
	}
}
