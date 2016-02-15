package am2.spell.components;

import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition.BlockDec;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import am2.utility.InventoryUtilities;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.EnumSet;
import java.util.Random;

public class PlaceBlock implements ISpellComponent{

	private static final String KEY_BLOCKID = "PlaceBlockID";
	private static final String KEY_META = "PlaceMeta";

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				Items.stone_axe,
				Items.stone_pickaxe,
				Items.stone_shovel,
				Blocks.chest
		};
	}

	@Override
	public int getID(){
		return 75;
	}

	private BlockDec getPlaceBlock(ItemStack stack){
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(KEY_BLOCKID)){
			return RitualShapeHelper.instance.hourglass.new BlockDec(Block.getBlockById(stack.getTagCompound().getInteger(KEY_BLOCKID)), stack.stackTagCompound.getInteger(KEY_META));
		}
		return null;
	}

	private void setPlaceBlock(ItemStack stack, Block block, int meta){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setInteger(KEY_BLOCKID, Block.getIdFromBlock(block));
		stack.getTagCompound().setInteger(KEY_META, meta);

		//set lore entry so that the stack displays the name of the block to place
		if (!stack.getTagCompound().hasKey("Lore"))
			stack.getTagCompound().setTag("Lore", new NBTTagList());

		ItemStack blockStack = new ItemStack(block, 1, meta);

		NBTTagList tagList = stack.getTagCompound().getTagList("Lore", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); ++i){
			String str = tagList.getStringTagAt(i);
			if (str.startsWith(String.format(StatCollector.translateToLocal("am2.tooltip.placeBlockSpell"), ""))){
				tagList.removeTag(i);
			}
		}
		tagList.appendTag(new NBTTagString(String.format(StatCollector.translateToLocal("am2.tooltip.placeBlockSpell"), blockStack.getDisplayName())));

		stack.getTagCompound().setTag("Lore", tagList);
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer)caster;
		ItemStack spellStack = player.getCurrentEquippedItem();
		if (spellStack == null || spellStack.getItem() != ItemsCommonProxy.spell || !SpellUtils.instance.componentIsPresent(spellStack, PlaceBlock.class, SpellUtils.instance.numStages(spellStack) - 1))
			return false;

		BlockDec bd = getPlaceBlock(spellStack);

		if (bd != null && !caster.isSneaking()){
			if (world.isAirBlock(pos) || !world.getBlockState(pos).getBlock().getMaterial().isSolid())
				facing = null;
			if (facing != null){
				switch (facing){
					case DOWN:
						pos = pos.down();
						break;
					case UP:
						pos = pos.up();
						break;
					case NORTH:
						pos = pos.north();
						break;
					case SOUTH:
						pos = pos.south();
						break;
					case WEST:
						pos = pos.west();
						break;
					case EAST:
						pos = pos.east();
						break;
				}
			}
			if (world.isAirBlock(pos) || !world.getBlockState(pos).getBlock().getMaterial().isSolid()){
				ItemStack searchStack = new ItemStack(bd.getBlock(), 1, bd.getMeta());
				if (!world.isRemote && (player.capabilities.isCreativeMode || InventoryUtilities.inventoryHasItem(player.inventory, searchStack, 1))){
					world.setBlock(blockx, blocky, blockz, bd.getBlock(), bd.getMeta(), 3);
					if (!player.capabilities.isCreativeMode)
						InventoryUtilities.deductFromInventory(player.inventory, searchStack, 1);
				}
				return true;
			}
		}else if (caster.isSneaking()){
			if (!world.isRemote && !world.isAirBlock(pos)){
				setPlaceBlock(spellStack, world.getBlockState(pos).getBlock(), world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)));
			}
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
		return 5;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 1;
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
		return EnumSet.of(Affinity.EARTH, Affinity.ENDER);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}

}
