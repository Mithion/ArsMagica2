package am2.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticlePendulum;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWitchwoodLeaves extends BlockLeaves{

	@SideOnly(Side.CLIENT)
	private IIcon opaqueIcon;

	protected BlockWitchwoodLeaves() {
		super();
		setHardness(0.2F);
		setLightOpacity(1);
		this.setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1)
	{
		return 0xFFFFFF;
	}

	@Override
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		return 0xFFFFFF;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return par1Random.nextInt(300) == 0 ? 1 : 0;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		return drops;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7)
	{
		if (!world.isRemote)
		{
			int j1 = 300;

			if (world.rand.nextInt(j1) == 0)
			{
				dropBlockAsItem(world, x, y, z, new ItemStack(BlocksCommonProxy.witchwoodSapling));
			}
		}
	}

	@Override
	public int damageDropped(int par1)
	{
		return 0;
	}

	@Override
	public IIcon getIcon(int par1, int par2)
	{
		return this.blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this));
	}

	@Override
	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(this, 1, par1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = ResourceManager.RegisterTexture("WitchwoodLeaves", par1IconRegister);
		opaqueIcon = ResourceManager.RegisterTexture("WitchwoodLeavesOpaque", par1IconRegister);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {

		if (!AMCore.config.witchwoodLeafPFX())
			return;

		if (par5Random.nextInt(300) == 0 && par1World.isAirBlock(par2, par3-1, par4)){
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(par1World, "leaf", par2 + par5Random.nextDouble(), par3 + par5Random.nextDouble(), par4 + par5Random.nextDouble());
			if (particle != null){
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, -0.05f, 1, false));
				particle.setMaxAge(120);
				particle.noClip = false;
				particle.setParticleScale(0.1f + par5Random.nextFloat() * 0.1f);
				particle.AddParticleController(new ParticlePendulum(particle, 0.2f, 0.15f + par5Random.nextFloat() * 0.2f, 2, false));
			}
		}
	}

	@Override
	public String[] func_150125_e() {
		return new String[] { "Witchwood" };
	}

}
