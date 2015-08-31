package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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

public class Rift implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {

		if (world.getBlock(blockx, blocky, blockz) == Blocks.mob_spawner){
			ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, blockx,blocky,blockz);
			if (reagents != null){
				if (!world.isRemote){
					world.setBlockToAir(blockx, blocky, blockz);
					RitualShapeHelper.instance.consumeRitualReagents(this, world, blockx, blocky, blockz);
					RitualShapeHelper.instance.consumeRitualShape(this, world, blockx, blocky, blockz);
					EntityItem item = new EntityItem(world);
					item.setPosition(blockx + 0.5, blocky + 0.5, blockz + 0.5);
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
		if (blockFace == 1){
			storage.setPosition(blockx+0.5, blocky+1.5, blockz+0.5);
		}else if (blockFace == 2){
			storage.setPosition(blockx+0.5, blocky+0.5, blockz-1.5);
		}else if (blockFace == 3){
			storage.setPosition(blockx+0.5, blocky+0.5, blockz+1.5);
		}else if (blockFace == 4){
			storage.setPosition(blockx-1.5, blocky+0.5, blockz+0.5);
		}else if (blockFace == 5){
			storage.setPosition(blockx+1.5, blocky+0.5, blockz+0.5);
		}else{
			storage.setPosition(blockx+0.5, blocky-1.5, blockz+0.5);
		}
		world.spawnEntityInWorld(storage);
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 90;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID() {
		return 48;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
			new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
			Blocks.chest,
			Items.ender_eye
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape() {
		return RitualShapeHelper.instance.corruption;
	}

	@Override
	public ItemStack[] getReagents() {
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.mobFocus),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER)
			};
	}

	@Override
	public int getReagentSearchRadius() {
		return 3;
	}
}
