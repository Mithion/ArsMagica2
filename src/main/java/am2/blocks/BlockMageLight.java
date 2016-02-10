package am2.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleGrow;

public class BlockMageLight extends AMSpecialRenderBlock{
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String[] dyes = { "Black", "Red", "Green", "Brown",
			"Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime",
			"Yellow", "LightBlue", "Magenta", "Orange", "White" };
	
	protected BlockMageLight(){
		super(Material.circuits);
		setBlockBounds(0.35f, 0.35f, 0.35f, 0.65f, 0.65f, 0.65f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
		this.setTickRandomly(true);
	}

	@Override
	public int tickRate(World par1World){
		return 20 - 5 * AMCore.config.getGFXLevel();
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, COLOR);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		EnumDyeColor color =  state.getValue(COLOR);
		return color.getDyeDamage();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(COLOR, EnumDyeColor.values()[meta]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, BlockPos pos, IBlockState state, Random rand){
		int meta = state.getBlock().getMetaFromState(state);
		int color = ItemDye.dyeColors[meta];

		AMParticle particle = (AMParticle)AMCore.instance.proxy.particleManager.spawn(par1World, "sparkle", pos.getX() + 0.5 + (rand.nextDouble() * 0.2f - 0.1f), pos.getY() + 0.5, pos.getZ() + 0.5 + (rand.nextDouble() * 0.2f - 0.1f));
		if (particle != null){
			particle.setIgnoreMaxAge(false);
			particle.setMaxAge(10 + rand.nextInt(20));
			particle.AddParticleController(new ParticleFloatUpward(particle, 0f, -0.01f, 1, false));
			particle.AddParticleController(new ParticleGrow(particle, -0.005f, 1, false));
			particle.setRGBColorI(color);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing face, float interactX, float interactY, float interactZ){

		if (!world.isRemote && player.getCurrentEquippedItem() != null){
			for (int i = 0; i < 16; i++) {
				List<ItemStack> ores = OreDictionary.getOres("dye" + dyes[i], false);
				for (ItemStack stack : ores){
					if (ItemStack.areItemsEqual(stack, player.getCurrentEquippedItem())){
						world.setBlockState(pos, this.getDefaultState().withProperty(COLOR, EnumDyeColor.values()[i]));
						break;
					}
				}
			}
		}

		return super.onBlockActivated(world, pos, state, player, face, interactX, interactY, interactZ);
	}
	
	@Override
	public int getLightValue() {
		return 15;
	}
	
	@Override
	public int quantityDropped(Random random){
		return 0;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}
	
	@Override
	public boolean isAir(IBlockAccess world, BlockPos pos) {
		return false;
	}

}
