package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.power.IPowerNode;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.buffs.BuffEffectIllumination;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.power.PowerNodeRegistry;
import am2.spell.SpellUtils;
import am2.spell.modifiers.Colour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Light implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		if (world.getBlockState(pos) == BlocksCommonProxy.obelisk){
			ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, pos);
			if (reagents != null){
				if (!world.isRemote){
					RitualShapeHelper.instance.consumeRitualReagents(this, world, pos);
					RitualShapeHelper.instance.consumeRitualShape(this, world, pos);
					world.setBlockState(pos, BlocksCommonProxy.celestialPrism.getDefaultState());
					PowerNodeRegistry.For(world).registerPowerNode((IPowerNode)world.getTileEntity(pos));
				}else{

				}

				return true;
			}
		}

		if (world.isAirBlock(pos)) blockFace = null;
		if (blockFace != null){
			switch (blockFace){
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
            case EAST:
				pos = pos.east();
				break;
            case WEST:
				pos = pos.west();
				break;
			}
		}

		if (!world.isAirBlock(pos)){
			return false;
		}

		if (!world.isRemote){
			world.setBlockState(pos, BlocksCommonProxy.blockMageTorch.getStateFromMeta(getColorMeta(stack)), 2);
		}


		return true;
	}

	private int getColorMeta(ItemStack spell){
		int meta = 15;
		int color = 0xFFFFFF;
		if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, spell, 0)){
			ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(spell, 0);
			int ordinalCount = 0;
			for (ISpellModifier mod : mods){
				if (mod instanceof Colour){
					byte[] data = SpellUtils.instance.getModifierMetadataFromStack(spell, mod, 0, ordinalCount++);
					color = (int)mod.getModifier(SpellModifiers.COLOR, null, null, null, data);
				}
			}
		}

		for (int i = 0; i < 16; ++i){
			if (((ItemDye)Items.dye).dyeColors[i] == color){
				meta = i;
				break;
			}
		}

		return meta;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			int duration = SpellUtils.instance.getModifiedInt_Mul(BuffList.default_buff_duration, stack, caster, target, world, 0, SpellModifiers.DURATION);
			duration = SpellUtils.instance.modifyDurationBasedOnArmor(caster, duration);
			if (!world.isRemote)
				((EntityLivingBase)target).addPotionEffect(new BuffEffectIllumination(duration, SpellUtils.instance.countModifiers(SpellModifiers.BUFF_POWER, stack, 0)));
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 50;
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
		for (int i = 0; i < 5; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2, rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				particle.setRGBColorF(0.6f, 0.2f, 0.8f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID(){
		return 33;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
				BlocksCommonProxy.cerublossom,
				Blocks.torch,
				BlocksCommonProxy.vinteumTorch
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.purification;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_MOONSTONE),
				new ItemStack(ItemsCommonProxy.manaFocus)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
