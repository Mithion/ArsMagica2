package am2.lore;

import am2.AMCore;
import am2.api.events.PlayerMagicLevelChangeEvent;
import am2.api.events.SkillLearnedEvent;
import am2.api.events.SpellCastingEvent;
import am2.api.spell.enums.SkillPointTypes;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SkillTreeManager;
import am2.spell.components.Summon;
import am2.spell.components.TrueSight;
import am2.spell.shapes.Binding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

/**
 * This class should handle compendium unlocks wherever possible through events.
 * If it is not possible, then all calls should use the static utility methods here.
 *
 * @author Mithion
 */
public class CompendiumUnlockHandler{
	/**
	 * This is a catch all method - it's genericized to attempt to unlock a compendium entry for anything AM2 based that the player picks up
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerPickupItem(EntityItemPickupEvent event){
	}

	/**
	 * Any magic level based unlocks should go in here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerMagicLevelChange(PlayerMagicLevelChangeEvent event){
		if (event.entity.worldObj.isRemote && event.entity == AMCore.proxy.getLocalPlayer()){
			if (event.level >= 5){
				ArcaneCompendium.instance.unlockCategory("talents");
				//ArcaneCompendium.instance.unlockEntry("dungeonsAndExploring");
				ArcaneCompendium.instance.unlockEntry("enchantments");
			}
			if (event.level >= 10){
				ArcaneCompendium.instance.unlockEntry("armorMage");
				ArcaneCompendium.instance.unlockEntry("playerjournal");
			}
			if (event.level >= 15){
				ArcaneCompendium.instance.unlockEntry("BossWaterGuardian");
				ArcaneCompendium.instance.unlockEntry("BossEarthGuardian");
				ArcaneCompendium.instance.unlockEntry("rituals");
				ArcaneCompendium.instance.unlockEntry("inlays");
				ArcaneCompendium.instance.unlockEntry("inlays_structure");
			}
			if (event.level >= 20){
				ArcaneCompendium.instance.unlockEntry("armorBattlemage");
			}
			if (event.level >= 25){
				ArcaneCompendium.instance.unlockEntry("BossAirGuardian");
				ArcaneCompendium.instance.unlockEntry("BossArcaneGuardian");
				ArcaneCompendium.instance.unlockEntry("BossLifeGuardian");
			}
			if (event.level >= 35){
				ArcaneCompendium.instance.unlockEntry("BossNatureGuardian");
				ArcaneCompendium.instance.unlockEntry("BossWinterGuardian");
				ArcaneCompendium.instance.unlockEntry("BossFireGuardian");
				ArcaneCompendium.instance.unlockEntry("BossLightningGuardian");
				ArcaneCompendium.instance.unlockEntry("BossEnderGuardian");
			}
		}
	}

	/**
	 * This should handle all mobs and the Astral Barrier
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){
		if (event.entityLiving.worldObj.isRemote && event.source.getSourceOfDamage() instanceof EntityPlayer){
			if (event.entity instanceof EntityEnderman){
				ArcaneCompendium.instance.unlockEntry("blockastralbarrier");
			}else{
				EntityRegistration reg = EntityRegistry.instance().lookupModSpawn(event.entityLiving.getClass(), true);
				if (reg != null && reg.getContainer().matches(AMCore.instance)){
					String id = reg.getEntityName();
					ArcaneCompendium.instance.unlockEntry(id);
				}
			}
		}
	}


	/**
	 * Any skill-based unlocks should go in here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onSkillLearned(SkillLearnedEvent event){
		if (event.player == AMCore.proxy.getLocalPlayer()){
			if (event.skill instanceof Summon){
				ArcaneCompendium.instance.unlockEntry("crystal_phylactery");
			}else if (event.skill instanceof TrueSight){
				ArcaneCompendium.instance.unlockEntry("illusionBlocks");
			}else if (event.skill instanceof Binding){
				ArcaneCompendium.instance.unlockEntry("bindingcatalysts");
			}else if (event.skill instanceof Summon){
				ArcaneCompendium.instance.unlockEntry("summoner");
			}else if (SkillTreeManager.instance.getSkillPointTypeForPart(event.skill) == SkillPointTypes.SILVER){
				ArcaneCompendium.instance.unlockEntry("silver_skills");
			}
		}
	}

	/**
	 * Any spell-based unlocks should go here (eg, low mana based unlocks, affinity, etc.)
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onSpellCast(SpellCastingEvent.Pre event){
		if (event.caster == AMCore.proxy.getLocalPlayer()){
			ArcaneCompendium.instance.unlockEntry("unlockingPowers");
			ArcaneCompendium.instance.unlockEntry("affinity");
			if (ExtendedProperties.For(event.caster).getCurrentMana() < ExtendedProperties.For(event.caster).getMaxMana() / 2)
				ArcaneCompendium.instance.unlockEntry("mana_potion");
		}
	}

	/**
	 * This is another genericized method, which attempts to unlock any entry for something the player crafts
	 */
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event){
		if (event.player.worldObj.isRemote){
			ArcaneCompendium.instance.unlockRelatedItems(event.crafting);
		}
	}

	/**
	 * Helper method (auto-proxied) that will unlock a compendium entry.  If the entry is found to be a category, it wil be unlocked instead.
	 *
	 * @param id The ID used to identify the entry to unlock.
	 */
	public static void unlockEntry(String id){
		AMCore.proxy.unlockCompendiumEntry(id);
	}

	/**
	 * Helper method (auto-proxied) that will unlock a compendium category.
	 *
	 * @param id The ID used to identify the entry to unlock.
	 */
	public static void unlockCategory(String id){
		AMCore.proxy.unlockCompendiumCategory(id);
	}
}
