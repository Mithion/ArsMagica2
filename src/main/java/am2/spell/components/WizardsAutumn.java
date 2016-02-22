package am2.spell.components;

import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import am2.utility.DummyEntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class WizardsAutumn implements ISpellComponent{

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				Blocks.sapling,
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
				Items.stick,
				Items.iron_ingot
		};
	}

	@Override
	public int getID(){
		return 70;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing side, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		if (!world.isRemote){
			int radius = 2;
			radius = SpellUtils.instance.getModifiedInt_Add(radius, stack, caster, caster, world, 0, SpellModifiers.RADIUS);
			for (int i = -radius; i <= radius; ++i){
				for (int j = -radius; j <= radius; ++j){
					for (int k = -radius; k <= radius; ++k){
						Block block = world.getBlockState(pos.add(i, j, k)).getBlock();
						int meta = world.getBlockState(pos.add(i, j, k)).getBlock().getMetaFromState(world.getBlockState(pos));
						if (block != null && block.isLeaves(world, pos.add(i, j, k))){
							if (block.removedByPlayer(world, pos.add(i, j, k), DummyEntityPlayer.fromEntityLiving(caster), true)){
								block.onBlockDestroyedByPlayer(world, pos.add(i, j, k), world.getBlockState(pos).getBlock().getStateFromMeta(meta));
								block.harvestBlock(world, DummyEntityPlayer.fromEntityLiving(caster), pos.add(i, j, k), world.getBlockState(pos).getBlock().getStateFromMeta(meta), world.getTileEntity(pos));
								//TODO: play sound
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return applyEffectBlock(stack, world, target.getPosition(), EnumFacing.DOWN, target.posX, target.posY, target.posZ, caster);
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 15;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.NATURE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

}
