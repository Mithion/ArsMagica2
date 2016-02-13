package am2.blocks;

import am2.api.spell.enums.Affinity;
import am2.blocks.tileentities.TileEntityGroundRuneSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockGroundRuneSpell extends BlockGroundRune{

	protected BlockGroundRuneSpell(){
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityGroundRuneSpell();
	}

	@Override
	protected boolean ActivateRune(World world, List<Entity> entitiesInRange, BlockPos pos){
		TileEntityGroundRuneSpell te = getTileEntity(world, pos);
		if (te == null)
			return false;
		for (Entity e : entitiesInRange){
			if (e instanceof EntityLivingBase){
				te.applySpellEffect((EntityLivingBase)e);
				break;
			}
		}
		return true;
	}

	@Override
	protected boolean isPermanent(World world, BlockPos pos, int metadata){
		TileEntityGroundRuneSpell te = getTileEntity(world, new BlockPos(pos));
		if (te == null)
			return false;
		return te.getPermanent();
	}

	@Override
	protected int getNumTriggers(World world, BlockPos pos, int metadata){
		TileEntityGroundRuneSpell te = getTileEntity(world, new BlockPos(pos));
		if (te == null)
			return 1;
		return te.getNumTriggers();
	}

	@Override
	public void setNumTriggers(World world, BlockPos pos, int meta, int numTriggers){
		TileEntityGroundRuneSpell te = getTileEntity(world, pos);
		if (te == null)
			return;
		te.setNumTriggers(numTriggers);
		if (numTriggers == -1)
			te.setPermanent(true);
	}

	private TileEntityGroundRuneSpell getTileEntity(World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityGroundRuneSpell){
			return (TileEntityGroundRuneSpell)te;
		}
		return null;
	}

	public void setSpellStack(World world, BlockPos pos, ItemStack effect){
		TileEntityGroundRuneSpell te = getTileEntity(world, pos);
		if (te == null)
			return;
		te.setSpellStack(effect);
	}

	public void setPlacedBy(World world, BlockPos pos, EntityLivingBase caster){
		TileEntityGroundRuneSpell te = getTileEntity(world, pos);
		if (te == null)
			return;
		te.setPlacedBy(caster);
	}

	@Override
	public boolean placeAt(World world, BlockPos pos, int meta){
		if (!canPlaceBlockAt(world, pos)) return false;
		if (!world.isRemote)
			world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(meta), 2);
		return true;
	}
}
