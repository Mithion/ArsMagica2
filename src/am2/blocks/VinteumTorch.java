package am2.blocks;

import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class VinteumTorch extends BlockTorch{

	@SideOnly(Side.CLIENT)
	private IIcon[] torchIcons;

	public VinteumTorch() {
		super();
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return 14;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (!par1World.isRemote) return;

		int l = par1World.getBlockMetadata(par2, par3, par4) & 0x0F;
		double d0 = par2 + 0.5F;
		double d1 = par3 + 0.6F;
		double d2 = par4 + 0.5F;
		double d3 = 0.2199999988079071D;
		double d4 = 0.27000001072883606D;

		if (l == 1)
		{
			spawnParticle(par1World, d0 - d4, d1 + d3, d2);
			spawnParticle(par1World, d0 - d4, d1 + d3, d2);
		}
		else if (l == 2)
		{
			spawnParticle(par1World, d0 + d4, d1 + d3, d2);
			spawnParticle(par1World, d0 + d4, d1 + d3, d2);
		}
		else if (l == 3)
		{
			spawnParticle(par1World, d0, d1 + d3, d2 - d4);
			spawnParticle(par1World, d0, d1 + d3, d2 - d4);
		}
		else if (l == 4)
		{
			spawnParticle(par1World, d0, d1 + d3, d2 + d4);
			spawnParticle(par1World, d0, d1 + d3, d2 + d4);
		}
		else
		{
			spawnParticle(par1World, d0, d1, d2);
			spawnParticle(par1World, d0, d1, d2);
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticle(World world, double x, double y, double z){
		AMParticle effect = (AMParticle) AMCore.instance.proxy.particleManager.spawn(world, "light", x, y, z);
		if (effect != null){
			effect.setMaxAge(3);
			effect.setIgnoreMaxAge(false);
			effect.setRGBColorF(0.69f, 0.89f, 1.0f);
			effect.AddParticleController(new ParticleFloatUpward(effect, 0, 0.01f, 1, false));
		}
	}

	@Override
	public int getRenderType()
	{
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister IIconRegister) {
		this.blockIcon = ResourceManager.RegisterTexture("arsmagica2:vinteumTorch", IIconRegister);
	}
}
