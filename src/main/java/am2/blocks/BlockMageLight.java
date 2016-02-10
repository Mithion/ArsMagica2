package am2.blocks;

import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleGrow;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockMageLight extends AMSpecialRenderBlock{

	protected BlockMageLight(){
		super(Material.circuits);
		setBlockBounds(0.35f, 0.35f, 0.35f, 0.65f, 0.65f, 0.65f);
		this.setTickRandomly(true);
	}

	@Override
	public int tickRate(World par1World){
		return 20 - 5 * AMCore.config.getGFXLevel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random rand){
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		int color = ItemDye.field_150922_c[meta];

		AMParticle particle = (AMParticle)AMCore.instance.proxy.particleManager.spawn(par1World, "sparkle", par2 + 0.5 + (rand.nextDouble() * 0.2f - 0.1f), par3 + 0.5, par4 + 0.5 + (rand.nextDouble() * 0.2f - 0.1f));
		if (particle != null){
			particle.setIgnoreMaxAge(false);
			particle.setMaxAge(10 + rand.nextInt(20));
			particle.AddParticleController(new ParticleFloatUpward(particle, 0f, -0.01f, 1, false));
			particle.AddParticleController(new ParticleGrow(particle, -0.005f, 1, false));
			particle.setRGBColorI(color);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float interactX, float interactY, float interactZ){

		if (!world.isRemote && player.getCurrentEquippedItem() != null){

			int id = OreDictionary.getOreID(player.getCurrentEquippedItem());
			if (id > -1){
				ArrayList<ItemStack> ores = OreDictionary.getOres(id);
				for (ItemStack stack : ores){
					if (stack.getItem() == Items.dye){
						world.setBlockMetadataWithNotify(x, y, z, player.getCurrentEquippedItem().getItemDamage() % 15, 2);
						break;
					}
				}
			}
		}

		return super.onBlockActivated(world, x, y, z, player, face, interactX, interactY, interactZ);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		return 15;
	}

	@Override
	public int quantityDropped(Random random){
		return 0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
		return new AxisAlignedBB(-0.2, -0.2, -0.2, 0.2, 0.2, 0.2);
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity){
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return null;
	}

	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z){
		return false;
	}
}
