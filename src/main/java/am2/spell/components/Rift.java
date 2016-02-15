package am2.spell.components;

import am2.RitualShapeHelper;
import am2.api.ArsMagicaApi;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.entities.EntityRiftStorage;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Rift implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {

		if (world.getBlockState(pos).getBlock() == Blocks.mob_spawner){
			ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, pos.getX(), pos.getY(), pos.getZ());
			if (reagents != null){
				if (!world.isRemote){
					world.setBlockToAir(pos);
					RitualShapeHelper.instance.consumeRitualReagents(this, world, pos.getX(), pos.getY(), pos.getZ());
					RitualShapeHelper.instance.consumeRitualShape(this, world, pos.getX(), pos.getY(), pos.getZ());
					EntityItem item = new EntityItem(world);
					item.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
					item.setEntityItemStack(new ItemStack(BlocksCommonProxy.inertSpawner));
					world.spawnEntityInWorld(item);
				}else{

				}

				return true;
			}
		}

		if (world.isRemote){
			return true;
		}
		EntityRiftStorage storage = new EntityRiftStorage(world);
		int storageLevel = Math.min(1 + SpellUtils.instance.countModifiers(SpellModifiers.BUFF_POWER, stack, 0), 3);
		storage.setStorageLevel(storageLevel);
		if (facing == EnumFacing.UP){
			storage.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ()+ 0.5);
		}else if (facing == EnumFacing.NORTH){
			storage.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() - 1.5);
		}else if (facing == EnumFacing.SOUTH){
			storage.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 1.5);
		}else if (facing == EnumFacing.WEST){
			storage.setPosition(pos.getX() - 1.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		}else if (facing == EnumFacing.EAST){
			storage.setPosition(pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		}else{
			storage.setPosition(pos.getX() + 0.5, pos.getY() - 1.5, pos.getZ() + 0.5);
		}
		world.spawnEntityInWorld(storage);
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 90;
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
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID(){
		return 48;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				Blocks.chest,
				Items.ender_eye
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.corruption;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.mobFocus),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
