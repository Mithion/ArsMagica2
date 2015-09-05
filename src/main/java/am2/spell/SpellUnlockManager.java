package am2.spell;

import am2.AMCore;
import am2.api.events.SpellCastingEvent;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SpellModifiers;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class SpellUnlockManager{

	private ArrayList<UnlockEntry> entries;

	public SpellUnlockManager(){
		init();
	}

	@SubscribeEvent
	public void onSpellCast(SpellCastingEvent.Pre event){
		if (event.caster instanceof EntityPlayer){
			if (ExtendedProperties.For(event.caster).getCurrentMana() < event.manaCost)
				return;
			for (UnlockEntry entry : entries){
				//check unlocks
				if (!event.caster.worldObj.isRemote){
					if (entry.unlockIsInPrimaryTree((EntityPlayer)event.caster) && entry.willSpellUnlock(event.stack)){
						entry.unlockFor((EntityPlayer)event.caster);
					}
				}
				if (!(entry.unlock instanceof ISpellModifier)){
					//lock out casting of spells that contain "silver" skills you don't know, or pre-learned ones (somehow) that aren't in your primary tree
					if (entry.unlockIsInSpell(event.stack) && (!SkillData.For((EntityPlayer)event.caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(entry.unlock)) || !entry.unlockIsInPrimaryTree((EntityPlayer)event.caster))){
						event.setCanceled(true);
						return;
					}
				}
			}
		}
	}

	public void init(){
		entries = new ArrayList<UnlockEntry>();
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("FallingStar"), SkillManager.instance.getSkill("MagicDamage"), SkillManager.instance.getSkill("Gravity"), SkillManager.instance.getSkill("AstralDistortion")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("Blizzard"), SkillManager.instance.getSkill("Storm"), SkillManager.instance.getSkill("FrostDamage"), SkillManager.instance.getSkill("Freeze"), SkillManager.instance.getSkill("Damage")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("FireRain"), SkillManager.instance.getSkill("Storm"), SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("Ignition"), SkillManager.instance.getSkill("Damage")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("Dismembering"), SkillManager.instance.getSkill("Piercing"), SkillManager.instance.getSkill("Damage")));

		entries.add(new UnlockEntry(SkillManager.instance.getSkill("ManaLink"), SkillManager.instance.getSkill("ManaDrain"), SkillManager.instance.getSkill("Entangle")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("ManaShield"), SkillManager.instance.getSkill("Shield"), SkillManager.instance.getSkill("Reflect"), SkillManager.instance.getSkill("LifeTap")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("BuffPower"), SkillManager.instance.getSkill("Haste"), SkillManager.instance.getSkill("Slowfall"), SkillManager.instance.getSkill("SwiftSwim"), SkillManager.instance.getSkill("GravityWell"), SkillManager.instance.getSkill("Leap")));

		entries.add(new UnlockEntry(SkillManager.instance.getSkill("Daylight"), SkillManager.instance.getSkill("TrueSight"), SkillManager.instance.getSkill("DivineIntervention"), SkillManager.instance.getSkill("Light")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("Moonrise"), SkillManager.instance.getSkill("NightVision"), SkillManager.instance.getSkill("EnderIntervention"), SkillManager.instance.getSkill("Lunar")));
		entries.add(new UnlockEntry(SkillManager.instance.getSkill("Prosperity"), SkillManager.instance.getSkill("Dig"), SkillManager.instance.getSkill("FeatherTouch"), SkillManager.instance.getSkill("MiningPower")));
	}

	class UnlockEntry{
		private ISkillTreeEntry unlock;
		private ISkillTreeEntry[] requiredComponents;

		public UnlockEntry(ISkillTreeEntry unlock, ISkillTreeEntry... components){
			this.unlock = unlock;
			this.requiredComponents = components;
		}

		public boolean unlockIsInSpell(ItemStack spell){
			for (int i = 0; i < SpellUtils.instance.numStages(spell); ++i)
				if (partIsInStage(spell, unlock, i))
					return true;
			return false;
		}

		public boolean partIsInStage(ItemStack spell, ISkillTreeEntry part, int stage){
			if (part instanceof ISpellComponent && !SpellUtils.instance.componentIsPresent(spell, part.getClass(), stage)){
				return false;
			}else if (part instanceof ISpellModifier){
				for (SpellModifiers modifier : ((ISpellModifier)part).getAspectsModified()){
					if (!SpellUtils.instance.modifierIsPresent(modifier, spell, stage)){
						return false;
					}
				}
			}
			return true;
		}

		public boolean unlockIsInPrimaryTree(EntityPlayer caster){
			if (AMCore.config.getSkillTreeSecondaryTierCap() >= SkillTreeManager.instance.getHighestTier())
				return true;
			return SkillData.For(caster).getPrimaryTree() == SkillTreeManager.instance.getSkillTreeEntry(unlock).tree;
		}

		public boolean willSpellUnlock(ItemStack spell){
			for (int i = 0; i < SpellUtils.instance.numStages(spell); ++i){

				boolean found = true;

				for (ISkillTreeEntry part : requiredComponents){
					if (!partIsInStage(spell, part, i)){
						found = false;
						break;
					}
				}

				if (found)
					return true;
			}

			return false;
		}

		public void unlockFor(EntityPlayer player){
			if (!player.worldObj.isRemote){
				SkillData.For(player).incrementSpellPoints(SkillPointTypes.SILVER);
				SkillData.For(player).learn(SkillTreeManager.instance.getSkillTreeEntry(unlock).registeredItem);
				SkillData.For(player).forceSync();
			}
		}
	}
}
