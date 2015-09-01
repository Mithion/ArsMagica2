package am2.interop;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import am2.blocks.BlocksCommonProxy;
import am2.entities.EntityManager;
import am2.items.ItemsCommonProxy;
import cpw.mods.fml.common.FMLLog;

public class TC4Interop {
	public static void initialize(){
		FMLLog.info("Ars Magica 2 >> Initializing Thaumcraft Compatibility");
		initTCAspects();
		initPortableHoleBlacklists();
	}

	private static void initTCAspects(){
		//blocks
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.illusionBlock, 1, -1), new AspectList().add(Aspect.MAGIC, 1).add(Aspect.SENSES, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.essenceConduit, 1, -1), new AspectList().add(Aspect.AIR, 1).add(Aspect.ENERGY, 1).add(Aspect.CRYSTAL, 2));
		//ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.caster, 1, -1), new AspectList().add(Aspect.MAGIC, 3).add(Aspect.MECHANISM, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.calefactor, 1, -1), new AspectList().add(Aspect.MECHANISM, 2).add(Aspect.MAGIC, 1).add(Aspect.FIRE, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.keystoneRecepticle, 1, -1), new AspectList().add(Aspect.VOID, 1).add(Aspect.ELDRITCH, 4).add(Aspect.TRAVEL, 4).add(Aspect.ORDER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.astralBarrier, 1, -1), new AspectList().add(Aspect.ELDRITCH, 2).add(Aspect.ARMOR, 1).add(Aspect.TRAP, 1).add(Aspect.ORDER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.seerStone, 1, -1), new AspectList().add(Aspect.MECHANISM, 2).add(Aspect.SENSES, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_BLUE_TOPAZ_ORE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.ENERGY, 2).add(Aspect.CRYSTAL, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_BLUE_TOPAZ_BLOCK), new AspectList().add(Aspect.ENERGY, 9).add(Aspect.CRYSTAL, 9));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.cerublossom, 1, -1), new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIGHT, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.desertNova, 1, -1), new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIGHT, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_SUNSTONE_ORE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.FIRE, 6).add(Aspect.CRYSTAL, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_SUNSTONE_BLOCK), new AspectList().add(Aspect.FIRE, 5).add(Aspect.CRYSTAL, 10));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_MOONSTONE_ORE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.ELDRITCH, 2).add(Aspect.MAGIC, 2).add(Aspect.VOID, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK), new AspectList().add(Aspect.ELDRITCH, 9).add(Aspect.MAGIC, 9).add(Aspect.VOID, 9));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_CHIMERITE_ORE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.SENSES, 6).add(Aspect.EXCHANGE, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_CHIMERITE_BLOCK), new AspectList().add(Aspect.SENSES, 5).add(Aspect.EXCHANGE, 9));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_VINTEUM_ORE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.AMOres, 1, BlocksCommonProxy.AMOres.META_VINTEUM_BLOCK), new AspectList().add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.vinteumTorch, 1, -1), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.LIGHT, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.keystoneChest, 1, -1), new AspectList().add(Aspect.VOID, 4).add(Aspect.TREE, 2).add(Aspect.ORDER, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.blockLectern, 1, -1), new AspectList().add(Aspect.TREE, 3).add(Aspect.ORDER, 2).add(Aspect.MIND, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.blockArcaneReconstructor, 1, -1), new AspectList().add(Aspect.MECHANISM, 2).add(Aspect.MAGIC, 2).add(Aspect.CRAFT, 2).add(Aspect.TOOL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.manaBattery, 1, -1), new AspectList().add(Aspect.CRYSTAL, 3).add(Aspect.VOID, 2).add(Aspect.ENERGY, 2).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.magicWall, 1, -1), new AspectList().add(Aspect.EARTH, 1).add(Aspect.ARMOR, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.occulus, 1, -1), new AspectList().add(Aspect.SENSES, 4).add(Aspect.MIND, 4).add(Aspect.VOID, 1).add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.craftingAltar, 1, -1), new AspectList().add(Aspect.MAGIC, 5).add(Aspect.CRAFT, 4).add(Aspect.MIND, 1));
		//ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.casterRune, 1, -1), new AspectList().add(Aspect.MAGIC, 3).add(Aspect.MECHANISM, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.aum, 1, -1), new AspectList().add(Aspect.PLANT, 1).add(Aspect.HEAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.wakebloom, 1, -1), new AspectList().add(Aspect.PLANT, 1).add(Aspect.WATER, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.particleEmitter, 1, -1), new AspectList().add(Aspect.SENSES, 3).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.tarmaRoot, 1, -1), new AspectList().add(Aspect.PLANT, 1).add(Aspect.AIR, 1).add(Aspect.DARKNESS, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.witchwoodLog, 1, -1), new AspectList().add(Aspect.TREE, 3).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.witchwoodPlanks, 1, -1), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.witchwoodSingleSlab, 1, -1), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.witchwoodStairs, 1, -1), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.summoner, 1, -1), new AspectList().add(Aspect.ELDRITCH, 1).add(Aspect.BEAST, 1).add(Aspect.MECHANISM, 1).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.redstoneInlay, 1, -1), new AspectList().add(Aspect.MECHANISM, 2).add(Aspect.ENERGY, 2).add(Aspect.ORDER, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.ironInlay, 1, -1), new AspectList().add(Aspect.METAL, 4).add(Aspect.ORDER, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.goldInlay, 1, -1), new AspectList().add(Aspect.METAL, 3).add(Aspect.GREED, 1).add(Aspect.ORDER, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.essenceRefiner, 1, -1), new AspectList().add(Aspect.MECHANISM, 2).add(Aspect.CRAFT, 4).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.inscriptionTable, 1, -1), new AspectList().add(Aspect.TREE, 3).add(Aspect.CRAFT, 2).add(Aspect.MIND, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.celestialPrism, 1, -1), new AspectList().add(Aspect.ORDER, 2).add(Aspect.AIR, 4).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.obelisk, 1, -1), new AspectList().add(Aspect.ORDER, 2).add(Aspect.WATER, 4).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.blackAurem, 1, -1), new AspectList().add(Aspect.ENTROPY, 2).add(Aspect.DEATH, 4).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.witchwoodLeaves, 1, -1), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.liquidEssence, 1, -1), new AspectList().add(Aspect.MAGIC, 6).add(Aspect.WATER, 3));

		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.spellRune, 1, -1), new AspectList().add(Aspect.MAGIC, 1).add(Aspect.TRAP, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.witchwoodSapling, 1, -1), new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.magiciansWorkbench, 1, -1), new AspectList().add(Aspect.CRAFT, 4).add(Aspect.TREE, 3).add(Aspect.ORDER, 2).add(Aspect.VOID, 4).add(Aspect.MECHANISM, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.everstone, 1, -1), new AspectList().add(Aspect.EARTH, 2).add(Aspect.ORDER, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.keystoneDoor, 1, -1), new AspectList().add(Aspect.CRYSTAL, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.keystoneTrapDoor, 1, -1), new AspectList().add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.slipstreamGenerator, 1, -1), new AspectList().add(Aspect.FLIGHT, 4).add(Aspect.MAGIC, 2).add(Aspect.AIR, 2).add(Aspect.MOTION, 2).add(Aspect.MECHANISM, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.crystalMarker, 1, -1), new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.ORDER, 2).add(Aspect.MECHANISM, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.elementalAttuner, 1, -1), new AspectList().add(Aspect.LIFE, 2).add(Aspect.AURA, 1).add(Aspect.MECHANISM, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.wizardChalk, 1, -1), new AspectList().add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.inertSpawner, 1, -1), new AspectList().add(Aspect.MECHANISM, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.candle, 1, -1), new AspectList().add(Aspect.LIGHT, 4).add(Aspect.MAGIC, 2).add(Aspect.SENSES, 5).add(Aspect.EXCHANGE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(BlocksCommonProxy.armorInfuser, 1, -1), new AspectList().add(Aspect.ARMOR, 5).add(Aspect.CRAFT, 3).add(Aspect.MAGIC, 10).add(Aspect.EXCHANGE, 5).add(Aspect.SENSES, 2));

		//items
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_BLUETOPAZ), new AspectList().add(Aspect.ENERGY, 1).add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE), new AspectList().add(Aspect.FIRE, 3).add(Aspect.CRYSTAL, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_MOONSTONE), new AspectList().add(Aspect.ELDRITCH, 1).add(Aspect.MAGIC, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE), new AspectList().add(Aspect.SENSES, 3).add(Aspect.EXCHANGE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.ENTROPY, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANECOMPOUND), new AspectList().add(Aspect.MAGIC, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH), new AspectList().add(Aspect.MAGIC, 1).add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_PURIFIEDVINTEUM), new AspectList().add(Aspect.MAGIC, 6).add(Aspect.ENTROPY, 1).add(Aspect.ORDER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemAMBucket, 1, -1), new AspectList().add(Aspect.METAL, 8).add(Aspect.VOID, 1).add(Aspect.MAGIC, 4).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_GENERAL), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.VOID, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_ARCANE), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 8));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_WATER), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.WATER, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_FIRE), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.FIRE, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_EARTH), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.EARTH, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_AIR), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.AIR, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_LIGHTNING), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.ENERGY, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_ICE), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.SLIME, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_NATURE), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.PLANT, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_LIFE), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.AURA, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bookAffinity, 1, ItemsCommonProxy.bookAffinity.META_ENDER), new AspectList().add(Aspect.MIND, 4).add(Aspect.MAGIC, 4).add(Aspect.ELDRITCH, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE), new AspectList().add(Aspect.MAGIC, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.EARTH, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_AIR), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.AIR, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_FIRE), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.FIRE, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_NATURE), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.PLANT, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.SLIME, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIGHTNING), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.ENERGY, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.AURA, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER), new AspectList().add(Aspect.MAGIC, 2).add(Aspect.ELDRITCH, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_PURE), new AspectList().add(Aspect.ORDER, 10).add(Aspect.ELDRITCH, 5).add(Aspect.AURA, 5).add(Aspect.EARTH, 2).add(Aspect.AIR, 2).add(Aspect.FIRE, 2).add(Aspect.WATER, 2).add(Aspect.PLANT, 2).add(Aspect.SLIME, 2).add(Aspect.ENERGY, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_HIGH_CORE), new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ENERGY, 2).add(Aspect.PLANT, 2).add(Aspect.SLIME, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_BASE_CORE), new AspectList().add(Aspect.MAGIC, 9).add(Aspect.AIR, 2).add(Aspect.FIRE, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.spellBook, 1, -1), new AspectList().add(Aspect.MIND, 3).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.spellParchment, 1, -1), new AspectList().add(Aspect.MIND, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.arcaneCompendium, 1, -1), new AspectList().add(Aspect.MIND, 7).add(Aspect.MAGIC, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLANK), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BROWN), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_CYAN), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GRAY), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_LIGHTBLUE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_LIGHTGRAY), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_LIME), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_MAGENTA), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PINK), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_PURPLE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_YELLOW), new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_BLUE), new AspectList().add(Aspect.MIND, 5).add(Aspect.MAGIC, 5));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_GREEN), new AspectList().add(Aspect.MIND, 5).add(Aspect.MAGIC, 10));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_RED), new AspectList().add(Aspect.MIND, 5).add(Aspect.MAGIC, 15));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.mageHood, 1, -1), new AspectList().add(Aspect.CLOTH, 6).add(Aspect.CRAFT, 3).add(Aspect.MAGIC, 3).add(Aspect.ARMOR, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.mageArmor, 1, -1), new AspectList().add(Aspect.CLOTH, 7).add(Aspect.CRAFT, 3).add(Aspect.MAGIC, 3).add(Aspect.ARMOR, 6));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.mageLeggings, 1, -1), new AspectList().add(Aspect.CLOTH, 7).add(Aspect.CRAFT, 3).add(Aspect.MAGIC, 3).add(Aspect.ARMOR, 5));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.mageBoots, 1, -1), new AspectList().add(Aspect.CLOTH, 5).add(Aspect.CRAFT, 3).add(Aspect.MAGIC, 3).add(Aspect.ARMOR, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.battlemageHood, 1, -1), new AspectList().add(Aspect.CRAFT, 2).add(Aspect.MAGIC, 3).add(Aspect.AIR, 2).add(Aspect.ARMOR, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.battlemageArmor, 1, -1), new AspectList().add(Aspect.CRAFT, 2).add(Aspect.MAGIC, 3).add(Aspect.AURA, 2).add(Aspect.ARMOR, 10));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.battlemageLeggings, 1, -1), new AspectList().add(Aspect.CRAFT, 2).add(Aspect.MAGIC, 3).add(Aspect.MOTION, 2).add(Aspect.ARMOR, 8));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.battlemageBoots, 1, -1), new AspectList().add(Aspect.CRAFT, 2).add(Aspect.MAGIC, 3).add(Aspect.MOTION, 2).add(Aspect.ARMOR, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.wizardChalk, 1, -1), new AspectList().add(Aspect.SENSES, 1).add(Aspect.TOOL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.lesserFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.standardFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.greaterFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.manaFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.ENERGY, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.chargeFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.ENERGY, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.playerFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.MAN, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.mobFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.UNDEAD, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.itemFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.GREED, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.creatureFocus, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.ORDER, 2).add(Aspect.BEAST, 1).add(Aspect.SENSES, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.keystone, 1, -1), new AspectList().add(Aspect.TOOL, 1).add(Aspect.ORDER, 3).add(Aspect.METAL, 4).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.manaCake, 1, -1), new AspectList().add(Aspect.HUNGER, 1).add(Aspect.AURA, 1).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.lesserManaPotion, 1, -1), new AspectList().add(Aspect.WATER, 1).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.standardManaPotion, 1, -1), new AspectList().add(Aspect.WATER, 1).add(Aspect.MAGIC, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.greaterManaPotion, 1, -1), new AspectList().add(Aspect.WATER, 1).add(Aspect.MAGIC, 6));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.epicManaPotion, 1, -1), new AspectList().add(Aspect.WATER, 1).add(Aspect.MAGIC, 8));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.legendaryManaPotion, 1, -1), new AspectList().add(Aspect.WATER, 1).add(Aspect.MAGIC, 10));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.deficitCrystal, 1, -1), new AspectList().add(Aspect.AURA, 2).add(Aspect.MAGIC, 1).add(Aspect.VOID, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.essenceBag, 1, -1), new AspectList().add(Aspect.BEAST, 6).add(Aspect.ORDER, 4).add(Aspect.VOID, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.crystalWrench, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.crystalPhylactery, 1, -1), new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.TRAP, 2).add(Aspect.VOID, 2).add(Aspect.SOUL, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.spellStaffMagitech, 1, -1), new AspectList().add(Aspect.TOOL, 2).add(Aspect.SENSES, 2).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.liquidEssenceBottle, 1, -1), new AspectList().add(Aspect.WATER, 1).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.evilBook, 1, -1), new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ELDRITCH, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.woodenLeg, 1, -1), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.cowHorn, 1, -1), new AspectList().add(Aspect.BEAST, 5).add(Aspect.FIRE, 5));

		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.flickerFocus, 1, -1), new AspectList().add(Aspect.ORDER, 2).add(Aspect.MECHANISM, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.flickerJar, 1, -1), new AspectList().add(Aspect.TRAP, 2).add(Aspect.AURA, 1).add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.playerjournal, 1, -1), new AspectList().add(Aspect.MIND, 1).add(Aspect.ORDER, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.magicBroom, 1, -1), new AspectList().add(Aspect.GREED, 1).add(Aspect.SENSES, 1).add(Aspect.MOTION, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.workbenchUpgrade, 1, -1), new AspectList().add(Aspect.CRAFT, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.bindingCatalyst, 1, -1), new AspectList().add(Aspect.TOOL, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.runeBag, 1, -1), new AspectList().add(Aspect.ORDER, 1).add(Aspect.EARTH, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.magitechGoggles, 1, -1), new AspectList().add(Aspect.SENSES, 2).add(Aspect.ARMOR, 1));

		/*ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.scythe, 1, -1), new AspectList().add(Aspect.TOOL, 3).add(Aspect.MOTION, 3).add(Aspect.PLANT, 4));
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.arcaneSpellbook, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.winterGuardianArm, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.fireEars, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.airGuardianLower, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.earthGuardianArmor, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.waterGuardianOrbs, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.enderBoots, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.lightningCharm, 1, -1), new AspectList());
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemsCommonProxy.lifeWard, 1, -1), new AspectList());*/

		//mobs
		ThaumcraftApi.registerEntityTag(EntityManager.instance.ManaCreeperMobID, new AspectList().add(Aspect.PLANT, 2).add(Aspect.MAGIC, 2).add(Aspect.VOID, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.DryadMobID, new AspectList().add(Aspect.PLANT, 4).add(Aspect.TREE, 4).add(Aspect.MAN, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.HecateMobID, new AspectList().add(Aspect.DEATH, 2).add(Aspect.SOUL, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.MageVillagerMobID, new AspectList().add(Aspect.MAN, 3).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.ManaElemMobID, new AspectList().add(Aspect.MAGIC, 6));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.WaterElementalMobID, new AspectList().add(Aspect.WATER, 2).add(Aspect.MOTION, 2).add(Aspect.DEATH, 1));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.LightMageMobID, new AspectList().add(Aspect.MAN, 3).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.DarkMageMobID, new AspectList().add(Aspect.MAN, 3).add(Aspect.MAGIC, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.HellCowID, new AspectList().add(Aspect.BEAST, 4).add(Aspect.FIRE, 4));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.EarthGolemMobID, new AspectList().add(Aspect.EARTH, 2).add(Aspect.EARTH, 2).add(Aspect.ARMOR, 1));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.FireElementalMobID, new AspectList().add(Aspect.FIRE, 2).add(Aspect.ENTROPY, 2).add(Aspect.ENERGY, 1));

		ThaumcraftApi.registerEntityTag(EntityManager.instance.AirSledID, new AspectList().add(Aspect.FLIGHT, 4).add(Aspect.AIR, 2));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.BroomID, new AspectList().add(Aspect.ORDER, 1).add(Aspect.MIND, 1).add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.DarklingID, new AspectList().add(Aspect.DARKNESS, 2).add(Aspect.BEAST, 1));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.FlickerID, new AspectList().add(Aspect.ENTROPY, 3).add(Aspect.ENERGY, 1));
		//ThaumcraftApi.registerEntityTag(EntityManager.instance.HecateMobID, new AspectList());

		//bosses
		ThaumcraftApi.registerEntityTag(EntityManager.instance.ArcaneGuardianMobID, new AspectList().add(Aspect.MAGIC, 4).add(Aspect.ENERGY, 4).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.AirGuardianMobID, new AspectList().add(Aspect.AIR, 4).add(Aspect.MOTION, 4).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.EarthGuardianMobID, new AspectList().add(Aspect.EARTH, 4).add(Aspect.EARTH, 4).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.EnderGuardianMobID, new AspectList().add(Aspect.DEATH, 3).add(Aspect.DARKNESS, 3).add(Aspect.ENTROPY, 4).add(Aspect.ELDRITCH, 4));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.FireGuardianMobID, new AspectList().add(Aspect.FIRE, 4).add(Aspect.EARTH, 4).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.LifeGuardianMobID, new AspectList().add(Aspect.LIFE, 5).add(Aspect.HEAL, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.LightningGuardianMobID, new AspectList().add(Aspect.ENERGY, 5).add(Aspect.ENTROPY, 3).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.NatureGuardianMobID, new AspectList().add(Aspect.PLANT, 4).add(Aspect.TREE, 4).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.WaterGuardianMobID, new AspectList().add(Aspect.WATER, 4).add(Aspect.SENSES, 4).add(Aspect.LIFE, 3));
		ThaumcraftApi.registerEntityTag(EntityManager.instance.WinterGuardianMobID, new AspectList().add(Aspect.COLD, 4).add(Aspect.ENTROPY, 4).add(Aspect.LIFE, 3));
	}

	private static void initPortableHoleBlacklists(){
		ThaumcraftApi.portableHoleBlackList.add(BlocksCommonProxy.everstone);
	}
}
