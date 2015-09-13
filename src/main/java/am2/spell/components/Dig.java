package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils;
import am2.utility.DummyEntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class Dig implements ISpellComponent{

	private static final float hardnessManaFactor = 1.28f;
	private ArrayList<Block> disallowedBlocks = new ArrayList<Block>();

	public Dig(){
		disallowedBlocks = new ArrayList<Block>();
		disallowedBlocks.add(Blocks.bedrock);
		disallowedBlocks.add(Blocks.command_block);
		disallowedBlocks.add(BlocksCommonProxy.everstone);

		for (String i : AMCore.config.getDigBlacklist()){
			if (i == null || i == "") continue;
			disallowedBlocks.add(Block.getBlockFromName(i.replace("tile.", "")));
		}
	}

	public void addDisallowedBlock(String block){
		disallowedBlocks.add(Block.getBlockFromName(block));
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		Block block = world.getBlock(blockx, blocky, blockz);
		if (block == Blocks.air){
			return false;
		}

		if (world.getTileEntity(blockx, blocky, blockz) != null && !AMCore.config.getDigBreaksTileEntities())
			return false;

		if (disallowedBlocks.contains(block)) return false;

		if (block.getBlockHardness(world, blockx, blocky, blockz) == -1) return false;

		int meta = world.getBlockMetadata(blockx, blocky, blockz);

		int harvestLevel = block.getHarvestLevel(meta);
		int miningLevel = 2 + SpellUtils.instance.countModifiers(SpellModifiers.MINING_POWER, stack, 0);
		if (harvestLevel > miningLevel) return false;

		if (ForgeEventFactory.doPlayerHarvestCheck(DummyEntityPlayer.fromEntityLiving(caster), block, true)){
			float xMana = block.getBlockHardness(world, blockx, blocky, blockz) * hardnessManaFactor;
			float xBurnout = ArsMagicaApi.instance.getBurnoutFromMana(xMana);

			if (!world.isRemote){
				EntityPlayerMP casterPlayer = (EntityPlayerMP)DummyEntityPlayer.fromEntityLiving(caster);
				BreakEvent event = ForgeHooks.onBlockBreakEvent(world, casterPlayer.theItemInWorldManager.getGameType(), casterPlayer, blockx, blocky, blockz);
				if (event.isCanceled()){
					return false;
				}
				block.onBlockHarvested(world, blockx, blocky, blockz, meta, DummyEntityPlayer.fromEntityLiving(caster));
				block.removedByPlayer(world, DummyEntityPlayer.fromEntityLiving(caster), blockx, blocky, blockz, true);
				block.onBlockDestroyedByPlayer(world, blockx, blocky, blockz, meta);
				block.harvestBlock(world, DummyEntityPlayer.fromEntityLiving(caster), blockx, blocky, blockz, meta);

			}

			ExtendedProperties.For(caster).deductMana(xMana);
			ExtendedProperties.For(caster).addBurnout(xBurnout);

			return true;
		}

		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 10;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 10;
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
		return EnumSet.of(Affinity.EARTH);
	}

	@Override
	public int getID(){
		return 8;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				Items.iron_shovel,
				Items.iron_pickaxe
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.001f;
	}
}
