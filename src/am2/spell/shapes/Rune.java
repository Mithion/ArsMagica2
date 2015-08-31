package am2.spell.shapes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellCastResult;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;

public class Rune implements ISpellShape{

	@Override
	public int getID() {
		return 6;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, int side, boolean consumeMBR, int useCount) {
		int procs = SpellUtils.instance.getModifiedInt_Add(1, stack, caster, target, world, 0, SpellModifiers.PROCS);
		boolean targetWater = SpellUtils.instance.modifierIsPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS, stack, 0);
		MovingObjectPosition mop = item.getMovingObjectPosition(caster, world, 8.0f, true, targetWater);
		if (mop == null || mop.typeOfHit == MovingObjectType.ENTITY) return SpellCastResult.EFFECT_FAILED;

		if (!BlocksCommonProxy.spellRune.placeAt(world, mop.blockX, mop.blockY+1, mop.blockZ, SpellUtils.instance.mainAffinityFor(stack).ordinal())){
			return SpellCastResult.EFFECT_FAILED;
		}
		if (!world.isRemote){
			world.setTileEntity(mop.blockX, mop.blockY+1, mop.blockZ, BlocksCommonProxy.spellRune.createTileEntity(world, 0));
			BlocksCommonProxy.spellRune.setSpellStack(world, mop.blockX, mop.blockY+1, mop.blockZ, SpellUtils.instance.popStackStage(stack));
			BlocksCommonProxy.spellRune.setPlacedBy(world, mop.blockX, mop.blockY+1, mop.blockZ, caster);
			int meta = world.getBlockMetadata(mop.blockX, mop.blockY+1, mop.blockZ);
			BlocksCommonProxy.spellRune.setNumTriggers(world, mop.blockX, mop.blockY+1, mop.blockZ, meta, procs);
		}

		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public Object[] getRecipeItems() {
		//Blue Rune, Red Rune, White Rune, Black Rune, Orange Rune, Purple Rune, Yellow Rune
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE),
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_YELLOW)
		};
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		return 1.8f;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		return true;
	}

	@Override
	public String getSoundForAffinity(Affinity affinity, ItemStack stack, World world) {
		return "arsmagica2:spell.rune.cast";
	}
}
