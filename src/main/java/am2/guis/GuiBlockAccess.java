package am2.guis;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiBlockAccess implements IBlockAccess{

	private Block fakeBlock = Blocks.air;
	private int fakeBlockMeta = 0;
	private TileEntity controllingTileEntity;
	private IBlockAccess outerBlockAccess;

	private int overridex, overridey, overridez;

	public void setControllingTileEntity(TileEntity controllingTileEntity){
		this.controllingTileEntity = controllingTileEntity;
	}

	public void setOuterBlockAccess(IBlockAccess outer){
		this.outerBlockAccess = outer;
	}

	public void setOverrideCoords(int x, int y, int z){
		overridex = x;
		overridey = y;
		overridez = z;
	}

	public void setFakeBlockAndMeta(Block block, int meta){
		this.fakeBlock = block;
		this.fakeBlockMeta = meta;
	}

	@Override
	public Block getBlock(int i, int j, int k){
		if (i == overridex && j == overridey && k == overridez)
			return this.fakeBlock;
		if (outerBlockAccess != null)
			return outerBlockAccess.getBlock(i, j, k);
		return Blocks.air;
	}

	@Override
	public TileEntity getTileEntity(int i, int j, int k){
		if (i == overridex && j == overridey && k == overridez) return controllingTileEntity;
		if (outerBlockAccess != null)
			return outerBlockAccess.getTileEntity(i, j, k);
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l){
		if (outerBlockAccess != null)
			return outerBlockAccess.getLightBrightnessForSkyBlocks(i, j, k, l);
		return 15728704;
	}

	@Override
	public int getBlockMetadata(int i, int j, int k){
		if (i == overridex && j == overridey && k == overridez)
			return this.fakeBlockMeta;
		if (outerBlockAccess != null)
			return outerBlockAccess.getBlockMetadata(i, j, k);
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isAirBlock(int i, int j, int k){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int i, int j){
		return BiomeGenBase.plains;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight(){
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache(){
		return false;
	}

	@Override
	public int isBlockProvidingPowerTo(int i, int j, int k, int l){
		return 0;
	}

	@Override
	public boolean isSideSolid(int arg0, int arg1, int arg2, ForgeDirection arg3, boolean arg4){
		return false;
	}

}
