package am2.spell.components;

import am2.api.power.PowerTypes;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.entities.EntityBattleChicken;
import am2.entities.EntityHellCow;
import am2.items.ItemCrystalPhylactery;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.utility.EntityUtilities;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Summon implements ISpellComponent{

	@Override
	public int getID(){
		return 61;
	}

	public EntityLiving summonCreature(ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z){
		Class clazz = getSummonType(stack);
		EntityLiving entity = null;
		try{
			entity = (EntityLiving)clazz.getConstructor(World.class).newInstance(world);
		}catch (Throwable t){
			t.printStackTrace();
			return null;
		}

		if (entity == null){
			return null;
		}
		if (entity instanceof EntitySkeleton){
			((EntitySkeleton)entity).setSkeletonType(0);
			((EntitySkeleton)entity).setCurrentItemOrArmor(0, new ItemStack(Items.bow));
		}else if (entity instanceof EntityHorse && caster instanceof EntityPlayer){
			((EntityHorse)entity).setTamedBy(((EntityPlayer)caster));
		}
		entity.setPosition(x, y, z);
		world.spawnEntityInWorld(entity);
		if (caster instanceof EntityPlayer){
			EntityUtilities.makeSummon_PlayerFaction((EntityCreature)entity, (EntityPlayer)caster, false);
		}else{
			EntityUtilities.makeSummon_MonsterFaction((EntityCreature)entity, false);
		}
		EntityUtilities.setOwner(entity, caster);

		int duration = SpellUtils.instance.getModifiedInt_Mul(4800, stack, caster, target, world, 0, SpellModifiers.DURATION);

		EntityUtilities.setSummonDuration(entity, duration);

		SpellHelper.instance.applyStageToEntity(stack, caster, world, entity, 0, false);

		return entity;
	}

	@Override
	public Object[] getRecipeItems(){
		//Chimerite, purified vinteum, blue orchid, monster focus, any filled crystal phylactery, 1500 dark power
		return new Object[]{
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_PURIFIEDVINTEUM),
				BlocksCommonProxy.cerublossom,
				ItemsCommonProxy.mobFocus,
				new ItemStack(ItemsCommonProxy.crystalPhylactery, 1, ItemsCommonProxy.crystalPhylactery.META_FULL),
				String.format("E:%d", PowerTypes.DARK.ID()), 1500
		};
	}

	public void setSummonType(ItemStack stack, ItemStack phylacteryStack){
		if (phylacteryStack.getItemDamage() == ItemsCommonProxy.crystalPhylactery.META_FULL && phylacteryStack.getItem() instanceof ItemCrystalPhylactery){
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());

			setSummonType(stack, ItemsCommonProxy.crystalPhylactery.getSpawnClass(phylacteryStack));
		}
	}

	public Class getSummonType(ItemStack stack){
		String s = SpellUtils.instance.getSpellMetadata(stack, "SummonType");
		if (s == null || s == "")
			s = "Skeleton"; //default!  default!  default!
		Class clazz = (Class)EntityList.stringToClassMapping.get(s);
		return clazz;
	}

	public void setSummonType(ItemStack stack, String s){
		Class clazz = (Class)EntityList.stringToClassMapping.get(s);
		setSummonType(stack, clazz);
	}

	public void setSummonType(ItemStack stack, Class clazz){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		clazz = checkForSpecialSpawns(stack, clazz);

		String s = (String)EntityList.classToStringMapping.get(clazz);
		if (s == null)
			s = "";

		SpellUtils.instance.setSpellMetadata(stack, "SpawnClassName", s);
		SpellUtils.instance.setSpellMetadata(stack, "SummonType", s);
	}

	private Class checkForSpecialSpawns(ItemStack stack, Class clazz){
		if (clazz == EntityChicken.class){
			if (SpellUtils.instance.modifierIsPresent(SpellModifiers.DAMAGE, stack, 0) && SpellUtils.instance.componentIsPresent(stack, Haste.class, 0)){
				return EntityBattleChicken.class;
			}
		}else if (clazz == EntityCow.class){
			if (SpellUtils.instance.modifierIsPresent(SpellModifiers.DAMAGE, stack, 0) && SpellUtils.instance.componentIsPresent(stack, AstralDistortion.class, 0)){
				return EntityHellCow.class;
			}
		}
		return clazz;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		if (!world.isRemote){
			if (ExtendedProperties.For(caster).getCanHaveMoreSummons()){
				if (summonCreature(stack, caster, caster, world, impactX, impactY, impactZ) == null){
					return false;
				}
			}else{
				if (caster instanceof EntityPlayer){
					((EntityPlayer)caster).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.noMoreSummons")));
				}
			}
		}

		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){

		if (target instanceof EntityLivingBase && EntityUtilities.isSummon((EntityLivingBase)target))
			return false;

		if (!world.isRemote){
			if (ExtendedProperties.For(caster).getCanHaveMoreSummons()){
				if (summonCreature(stack, caster, caster, world, target.posX, target.posY, target.posZ) == null){
					return false;
				}
			}else{
				if (caster instanceof EntityPlayer){
					((EntityPlayer)caster).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.noMoreSummons")));
				}
			}
		}

		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 400;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 120;
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
		return EnumSet.of(Affinity.ENDER, Affinity.LIFE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}
}
