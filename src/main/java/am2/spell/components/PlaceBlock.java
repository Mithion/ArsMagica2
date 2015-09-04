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
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(KEY_BLOCKID)){
			return RitualShapeHelper.instance.hourglass.new BlockDec(Block.getBlockById(stack.stackTagCompound.getInteger(KEY_BLOCKID)), stack.stackTagCompound.getInteger(KEY_META));
		}
		return null;
	}

	private void setPlaceBlock(ItemStack stack, Block block, int meta){
		if (!stack.hasTagCompound())
			stack.stackTagCompound = new NBTTagCompound();

		stack.stackTagCompound.setInteger(KEY_BLOCKID, Block.getIdFromBlock(block));
		stack.stackTagCompound.setInteger(KEY_META, meta);

		//set lore entry so that the stack displays the name of the block to place
		if (!stack.stackTagCompound.hasKey("Lore"))
			stack.stackTagCompound.setTag("Lore", new NBTTagList());

		ItemStack blockStack = new ItemStack(block, 1, meta);

		NBTTagList tagList = stack.stackTagCompound.getTagList("Lore", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); ++i){
			String str = tagList.getStringTagAt(i);
			if (str.startsWith(String.format(StatCollector.translateToLocal("am2.tooltip.placeBlockSpell"), ""))){
				tagList.removeTag(i);
			}
		}
		tagList.appendTag(new NBTTagString(String.format(StatCollector.translateToLocal("am2.tooltip.placeBlockSpell"), blockStack.getDisplayName())));

		stack.stackTagCompound.setTag("Lore", tagList);
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		if (!(caster instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer)caster;
		ItemStack spellStack = player.getCurrentEquippedItem();
		if (spellStack == null || spellStack.getItem() != ItemsCommonProxy.spell || !SpellUtils.instance.componentIsPresent(spellStack, PlaceBlock.class, SpellUtils.instance.numStages(spellStack) - 1))
			return false;

		BlockDec bd = getPlaceBlock(spellStack);

		if (bd != null && !caster.isSneaking()){
			if (world.isAirBlock(blockx, blocky, blockz) || !world.getBlock(blockx, blocky, blockz).getMaterial().isSolid())
				blockFace = -1;
			if (blockFace != -1){
				switch (blockFace){
				case 0:
					blocky--;
					break;
				case 1:
					blocky++;
					break;
				case 2:
					blockz--;
					break;
				case 3:
					blockz++;
					break;
				case 4:
					blockx--;
					break;
				case 5:
					blockx++;
					break;
				}
			}
			if (world.isAirBlock(blockx, blocky, blockz) || !world.getBlock(blockx, blocky, blockz).getMaterial().isSolid()){
				ItemStack searchStack = new ItemStack(bd.getBlock(), 1, bd.getMeta());
				if (!world.isRemote && (player.capabilities.isCreativeMode || InventoryUtilities.inventoryHasItem(player.inventory, searchStack, 1))){
					world.setBlock(blockx, blocky, blockz, bd.getBlock(), bd.getMeta(), 3);
					if (!player.capabilities.isCreativeMode)
						InventoryUtilities.deductFromInventory(player.inventory, searchStack, 1);
				}
				return true;
			}
		}else if (caster.isSneaking()){
			if (!world.isRemote && !world.isAirBlock(blockx, blocky, blockz)){
				setPlaceBlock(spellStack, world.getBlock(blockx, blocky, blockz), world.getBlockMetadata(blockx, blocky, blockz));
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
