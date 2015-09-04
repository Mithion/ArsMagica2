package am2.utility;

import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellUtils;
import net.minecraft.item.ItemStack;

public class NPCSpells{
	public static final NPCSpells instance = new NPCSpells();

	public final ItemStack lightMage_DiminishedAttack;
	public final ItemStack lightMage_NormalAttack;
	public final ItemStack lightMage_AugmentedAttack;

	public final ItemStack darkMage_DiminishedAttack;
	public final ItemStack darkMage_NormalAttack;
	public final ItemStack darkMage_AugmentedAttack;

	public final ItemStack enderGuardian_enderWave;
	public final ItemStack enderGuardian_enderBolt;
	public final ItemStack enderGuardian_enderTorrent;
	public final ItemStack enderGuardian_otherworldlyRoar;

	public final ItemStack dispel;
	public final ItemStack blink;
	public final ItemStack arcaneBolt;
	public final ItemStack meltArmor;
	public final ItemStack waterBolt;
	public final ItemStack fireBolt;
	public final ItemStack healSelf;
	public final ItemStack nauseate;
	public final ItemStack lightningRune;
	public final ItemStack scrambleSynapses;

	private NPCSpells(){
		lightMage_DiminishedAttack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(lightMage_DiminishedAttack, "Projectile", new String[]{"PhysicalDamage"}, new String[]{});

		lightMage_NormalAttack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(lightMage_NormalAttack, "Projectile", new String[]{"FrostDamage", "Slow"}, new String[]{});

		lightMage_AugmentedAttack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(lightMage_AugmentedAttack, "Projectile", new String[]{"MagicDamage", "Blind"}, new String[]{"Damage"});

		darkMage_DiminishedAttack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(darkMage_DiminishedAttack, "Projectile", new String[]{"MagicDamage"}, new String[]{});
		SpellUtils.instance.setForcedAffinity(darkMage_DiminishedAttack, Affinity.ENDER);

		darkMage_NormalAttack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(darkMage_NormalAttack, "Projectile", new String[]{"FireDamage", "Ignition"}, new String[]{});

		darkMage_AugmentedAttack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(darkMage_AugmentedAttack, "Projectile", new String[]{"LightningDamage", "Knockback"}, new String[]{"Damage"});

		enderGuardian_enderWave = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(enderGuardian_enderWave, "Wave", new String[]{}, new String[]{"Radius", "Radius"});
		SpellUtils.instance.addSpellStageToScroll(enderGuardian_enderWave, "Touch", new String[]{"MagicDamage", "Knockback"}, new String[0]);
		SpellUtils.instance.setForcedAffinity(enderGuardian_enderWave, Affinity.ENDER);

		enderGuardian_enderBolt = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(enderGuardian_enderBolt, "Projectile", new String[]{"MagicDamage", "RandomTeleport"}, new String[]{"Damage"});
		SpellUtils.instance.setForcedAffinity(enderGuardian_enderBolt, Affinity.ENDER);

		enderGuardian_otherworldlyRoar = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(enderGuardian_otherworldlyRoar, "AoE", new String[]{"Blind", "Silence", "Knockback"}, new String[]{"Radius", "Radius", "Radius", "Radius", "Radius"});
		SpellUtils.instance.setForcedAffinity(enderGuardian_otherworldlyRoar, Affinity.ENDER);

		enderGuardian_enderTorrent = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(enderGuardian_enderTorrent, "Projectile", new String[]{"Silence", "Knockback"}, new String[]{"Speed"});
		SpellUtils.instance.addSpellStageToScroll(enderGuardian_enderTorrent, "AoE", new String[]{"ManaDrain", "LifeDrain"}, new String[]{});
		SpellUtils.instance.setForcedAffinity(enderGuardian_enderTorrent, Affinity.ENDER);

		dispel = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(dispel, "Self", new String[]{"Dispel"}, new String[0]);

		blink = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(blink, "Self", new String[]{"Blink"}, new String[0]);

		arcaneBolt = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(arcaneBolt, "Projectile", new String[]{"MagicDamage"}, new String[0]);

		meltArmor = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(meltArmor, "Projectile", new String[]{"MeltArmor"}, new String[0]);

		waterBolt = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(waterBolt, "Projectile", new String[]{"WateryGrave", "Drown"}, new String[0]);

		fireBolt = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(fireBolt, "Projectile", new String[]{"FireDamage", "Ignition"}, new String[0]);

		healSelf = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(healSelf, "Self", new String[]{"Heal"}, new String[0]);

		nauseate = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(nauseate, "Projectile", new String[]{"Nauseate", "ScrambleSynapses"}, new String[0]);
		SpellUtils.instance.setForcedAffinity(nauseate, Affinity.LIFE);

		lightningRune = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(lightningRune, "Projectile", new String[0], new String[0]);
		SpellUtils.instance.addSpellStageToScroll(lightningRune, "Rune", new String[0], new String[0]);
		SpellUtils.instance.addSpellStageToScroll(lightningRune, "AoE", new String[]{"LightningDamage"}, new String[]{"Damage"});
		SpellUtils.instance.setForcedAffinity(lightningRune, Affinity.LIGHTNING);

		scrambleSynapses = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(scrambleSynapses, "Projectile", new String[]{"LightningDamage"}, new String[]{"Speed"});
		SpellUtils.instance.addSpellStageToScroll(scrambleSynapses, "AoE", new String[]{"ScrambleSynapses"}, new String[]{"Radius", "Radius", "Radius", "Radius", "Radius"});
		SpellUtils.instance.setForcedAffinity(scrambleSynapses, Affinity.LIGHTNING);
	}
}
