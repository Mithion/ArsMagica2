package am2.items;

import am2.AMCore;
import am2.AMCreativeTab;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.armor.*;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.flickers.FlickerOperatorRegistry;
import am2.enchantments.AMEnchantmentHelper;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class ItemsCommonProxy{

	//Core Crafting Components
	public static ItemOre itemOre;

	//Essences
	public static ItemEssence essence;

	public static ItemEssenceBag essenceBag;

	//spell casting items
	public static ArsMagicaItem spellParchment;
	public static ItemSpellBook spellBook;

	//runes
	public static ItemRune rune;

	public static AMSpawnEgg spawnEgg;

	public static ItemFocusLesser lesserFocus;
	public static ItemFocusStandard standardFocus;
	public static ItemFocusGreater greaterFocus;
	public static ItemFocusMana manaFocus;
	public static ItemFocusCharge chargeFocus;
	public static ItemPlayerFocus playerFocus;
	public static ItemMobFocus mobFocus;
	public static ItemItemFocus itemFocus;
	public static ItemCreatureFocus creatureFocus;
	public static ItemFlickerFocus flickerFocus;

	public static Item wizardChalk;

	public static ItemManaCake manaCake;

	public static ItemKeystone keystone;

	//affinity books
	public static ItemAffinityBook bookAffinity;

	public static ItemArcaneCompendium arcaneCompendium;

	public static ItemManaPotion lesserManaPotion;
	public static ItemManaPotion standardManaPotion;
	public static ItemManaPotion greaterManaPotion;
	public static ItemManaPotion epicManaPotion;
	public static ItemManaPotion legendaryManaPotion;
	public static ItemManaMartini manaMartini;
	public static ItemLiquidEssenceBottle liquidEssenceBottle;

	public static ItemManaPotionBundle manaPotionBundle;
	public static ItemFlickerJar flickerJar;
	public static ItemJournal playerjournal;

	//spell staves
	public static ItemSpellStaff spellStaffMagitech;

	public static ItemCrystalWrench crystalWrench;

	public static ItemSpellPart spell_component;

	public static ItemAMBucket itemAMBucket;

	public static ItemMagicBroom magicBroom;

	public static ArsMagicaItem workbenchUpgrade;

	public static ItemBindingCatalyst bindingCatalyst;
	public static ItemKeystoneDoor itemKeystoneDoor;
	public static ItemCandle wardingCandle;
	public static ItemRuneBag runeBag;
	public static InscriptionTableUpgrade inscriptionUpgrade;

	//hee hee
	public static ItemNatureGuardianSickle scythe;
	public static ItemArcaneGuardianSpellbook arcaneSpellbook;
	public static ItemWinterGuardianArm winterGuardianArm;
	public static ItemAirSled airGuardianLower;
	public static ItemEarthGuardianArmor earthGuardianArmor;
	public static ItemWaterGuardianOrbs waterGuardianOrbs;
	public static ItemFireGuardianEars fireEars;
	public static ItemEnderBoots enderBoots;
	public static ItemLightningCharm lightningCharm;
	public static ItemLifeWard lifeWard;
	public static Item evilBook;
	public static Item woodenLeg;
	public static ItemHellCowHorn cowHorn;

	//--------------------------------------------------------------
	// Spells
	//--------------------------------------------------------------
	//public static ArsMagicaItem spellRecipeScroll;
	//--------------------------------------------------------------
	// End Spells
	//--------------------------------------------------------------

	//armor
	public static Item mageHood;
	public static Item mageArmor;
	public static Item mageLeggings;
	public static Item mageBoots;

	public static Item battlemageHood;
	public static Item battlemageArmor;
	public static Item battlemageLeggings;
	public static Item battlemageBoots;

	/*public static Item archmageHood;
	public static Item archmageArmor;
	public static Item archmageLeggings;
	public static Item archmageBoots;*/

	//bound items
	public static Item BoundHoe;
	public static Item BoundShovel;
	public static Item BoundAxe;
	public static Item BoundPickaxe;
	public static Item BoundSword;
	//public static Item boundBow;

	public static ItemCrystalPhylactery crystalPhylactery;
	public static ItemMagitechGoggles magitechGoggles;
	public static ArsMagicaItem deficitCrystal;

	public static AMCreativeTab itemTab;

	/**
	 * Indicates what armors have additional protective effects which need to be handled by the AMEventHandler class.
	 */
	public static Item[] protectiveArmors;
	public static SpellBase spell;

	public static ItemStack natureScytheEnchanted;
	public static ItemStack arcaneSpellBookEnchanted;
	public static ItemStack winterArmEnchanted;
	public static ItemStack fireEarsEnchanted;
	public static ItemStack earthArmorEnchanted;
	public static ItemStack airSledEnchanted;
	public static ItemStack enderBootsEnchanted;
	public static ItemStack waterOrbsEnchanted;
	public static ItemStack lightningCharmEnchanted;
	public static ItemStack lifeWardEnchanted;

	private final ArrayList<Item> arsMagicaItemsList;

	public ItemsCommonProxy(){
		itemTab = new AMCreativeTab("Ars Magica Items");
		arsMagicaItemsList = new ArrayList<Item>();
	}

	public List<Item> getArsMagicaItems(){
		return arsMagicaItemsList;
	}

	public void InstantiateItems(){

		FMLLog.finer("Ars Magica >> Instantiating Items");

		itemOre = (ItemOre)new ItemOre().setUnlocalizedAndTextureName("arsmagica2:itemOre").setCreativeTab(itemTab);
		essence = (ItemEssence)new ItemEssence().setUnlocalizedAndTextureName("arsmagica2:essence").setCreativeTab(itemTab);
		spellBook = (ItemSpellBook)new ItemSpellBook().setUnlocalizedAndTextureName("arsmagica2:spell_book").setCreativeTab(itemTab);
		spellParchment = (ArsMagicaItem)new ArsMagicaItem().setUnlocalizedAndTextureName("arsmagica2:spell_parchment").setCreativeTab(itemTab);
		spell = (SpellBase)new SpellBase().setUnlocalizedName("arsmagica2:spell_base");
		rune = (ItemRune)new ItemRune(0).setUnlocalizedAndTextureName("runes").setCreativeTab(itemTab);
		mageHood = (new ItemMageHood(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.MAGE, ArmorHelper.getArmorRenderIndex("mage"), 0)).setUnlocalizedAndTextureName("arsmagica2:helmet_mage").setCreativeTab(itemTab);
		mageArmor = (new AMArmor(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.MAGE, ArmorHelper.getArmorRenderIndex("mage"), 1)).setUnlocalizedAndTextureName("arsmagica2:chest_mage").setCreativeTab(itemTab);
		mageLeggings = (new AMArmor(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.MAGE, ArmorHelper.getArmorRenderIndex("mage"), 2)).setUnlocalizedAndTextureName("arsmagica2:legs_mage").setCreativeTab(itemTab);
		mageBoots = (new AMArmor(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.MAGE, ArmorHelper.getArmorRenderIndex("mage"), 3)).setUnlocalizedAndTextureName("arsmagica2:boots_mage").setCreativeTab(itemTab);
		battlemageHood = (new ItemMageHood(ArmorMaterial.IRON, ArsMagicaArmorMaterial.BATTLEMAGE, ArmorHelper.getArmorRenderIndex("battlemage"), 0)).setUnlocalizedAndTextureName("arsmagica2:helmet_battlemage").setCreativeTab(itemTab);
		battlemageArmor = (new AMArmor(ArmorMaterial.IRON, ArsMagicaArmorMaterial.BATTLEMAGE, ArmorHelper.getArmorRenderIndex("battlemage"), 1)).setUnlocalizedAndTextureName("arsmagica2:chest_battlemage").setCreativeTab(itemTab);
		battlemageLeggings = (new AMArmor(ArmorMaterial.IRON, ArsMagicaArmorMaterial.BATTLEMAGE, ArmorHelper.getArmorRenderIndex("battlemage"), 2)).setUnlocalizedAndTextureName("arsmagica2:legs_battlemage").setCreativeTab(itemTab);
		battlemageBoots = (new AMArmor(ArmorMaterial.IRON, ArsMagicaArmorMaterial.BATTLEMAGE, ArmorHelper.getArmorRenderIndex("battlemage"), 3)).setUnlocalizedAndTextureName("arsmagica2:boots_battlemage").setCreativeTab(itemTab);
		//archmageHood = (new ArsMagicaItemArmor(AMCore.config.getConfigurableItemID("archmage_hood", 20037), ArmorMaterial.DIAMOND, ArsMagicaArmorMaterial.ARCHMAGE, ArmorHelper.getArmorRenderIndex("archmage"), 0)).setUnlocalizedAndTextureName("arsmagica2:helmet_archmage").setCreativeTab(itemTab);
		//archmageArmor = (new ArsMagicaItemArmor(AMCore.config.getConfigurableItemID("archmage_chest", 20038), ArmorMaterial.DIAMOND, ArsMagicaArmorMaterial.ARCHMAGE, ArmorHelper.getArmorRenderIndex("archmage"), 1)).setUnlocalizedAndTextureName("arsmagica2:chest_archmage").setCreativeTab(itemTab);
		//archmageLeggings = (new ArsMagicaItemArmor(AMCore.config.getConfigurableItemID("archmage_leggings", 20039), ArmorMaterial.DIAMOND, ArsMagicaArmorMaterial.ARCHMAGE, ArmorHelper.getArmorRenderIndex("archmage"), 2)).setUnlocalizedAndTextureName("arsmagica2:legs_archmage").setCreativeTab(itemTab);
		//archmageBoots = (new ArsMagicaItemArmor(AMCore.config.getConfigurableItemID("archmage_boots", 20040), ArmorMaterial.DIAMOND, ArsMagicaArmorMaterial.ARCHMAGE, ArmorHelper.getArmorRenderIndex("archmage"), 3)).setUnlocalizedAndTextureName("arsmagica2:boots_archmage").setCreativeTab(itemTab);
		wizardChalk = new ItemChalk().setUnlocalizedAndTextureName("arsmagica2:wizardChalk").setCreativeTab(itemTab);
		lesserFocus = (ItemFocusLesser)new ItemFocusLesser().setUnlocalizedAndTextureName("arsmagica2:lesserfocus").setCreativeTab(itemTab);
		standardFocus = (ItemFocusStandard)new ItemFocusStandard().setUnlocalizedAndTextureName("arsmagica2:standardfocus").setCreativeTab(itemTab);
		greaterFocus = (ItemFocusGreater)new ItemFocusGreater().setUnlocalizedAndTextureName("arsmagica2:greaterfocus").setCreativeTab(itemTab);
		manaFocus = (ItemFocusMana)new ItemFocusMana().setUnlocalizedAndTextureName("arsmagica2:manafocus").setCreativeTab(itemTab);
		chargeFocus = (ItemFocusCharge)new ItemFocusCharge().setUnlocalizedAndTextureName("arsmagica2:chargefocus").setCreativeTab(itemTab);
		bookAffinity = (ItemAffinityBook)new ItemAffinityBook().setUnlocalizedAndTextureName("arsmagica2:affinity_tome").setCreativeTab(itemTab);
		keystone = (ItemKeystone)new ItemKeystone().setUnlocalizedAndTextureName("arsmagica2:keystone").setCreativeTab(itemTab);
		playerFocus = (ItemPlayerFocus)new ItemPlayerFocus().setUnlocalizedAndTextureName("arsmagica2:player_focus").setCreativeTab(itemTab);
		mobFocus = (ItemMobFocus)new ItemMobFocus().setUnlocalizedAndTextureName("arsmagica2:monster_focus").setCreativeTab(itemTab);
		itemFocus = (ItemItemFocus)new ItemItemFocus().setUnlocalizedAndTextureName("arsmagica2:item_focus").setCreativeTab(itemTab);
		creatureFocus = (ItemCreatureFocus)new ItemCreatureFocus().setUnlocalizedAndTextureName("arsmagica2:creature_focus").setCreativeTab(itemTab);
		BoundHoe = new ItemBoundHoe(ToolMaterial.IRON).setUnlocalizedAndTextureName("arsmagica2:bound_hoe");
		BoundAxe = new ItemBoundAxe(ToolMaterial.IRON).setUnlocalizedAndTextureName("arsmagica2:bound_axe");
		BoundPickaxe = new ItemBoundPickaxe(ToolMaterial.IRON).setUnlocalizedAndTextureName("arsmagica2:bound_pickaxe");
		BoundShovel = new ItemBoundShovel(ToolMaterial.IRON).setUnlocalizedAndTextureName("arsmagica2:bound_shovel");
		BoundSword = new ItemBoundSword(ToolMaterial.IRON).setUnlocalizedAndTextureName("arsmagica2:bound_sword");
		manaCake = (ItemManaCake)new ItemManaCake().setUnlocalizedAndTextureName("arsmagica2:mana_cake").setAlwaysEdible().setCreativeTab(itemTab);
		lesserManaPotion = (ItemManaPotion)new ItemManaPotion().setUnlocalizedAndTextureName("arsmagica2:mana_potion_lesser").setCreativeTab(itemTab);
		standardManaPotion = (ItemManaPotion)new ItemManaPotion().setUnlocalizedAndTextureName("arsmagica2:mana_potion_standard").setCreativeTab(itemTab);
		greaterManaPotion = (ItemManaPotion)new ItemManaPotion().setUnlocalizedAndTextureName("arsmagica2:mana_potion_greater").setCreativeTab(itemTab);
		epicManaPotion = (ItemManaPotion)new ItemManaPotion().setUnlocalizedAndTextureName("arsmagica2:mana_potion_epic").setCreativeTab(itemTab);
		legendaryManaPotion = (ItemManaPotion)new ItemManaPotion().setUnlocalizedAndTextureName("arsmagica2:mana_potion_legendary").setCreativeTab(itemTab);
		deficitCrystal = (ArsMagicaItem)new ArsMagicaItem().setUnlocalizedAndTextureName("arsmagica2:deficit_crystal").setCreativeTab(itemTab);
		essenceBag = (ItemEssenceBag)new ItemEssenceBag().setUnlocalizedAndTextureName("arsmagica2:essence_bag").setCreativeTab(itemTab);
		manaPotionBundle = (ItemManaPotionBundle)new ItemManaPotionBundle().setUnlocalizedAndTextureName("arsmagica2:mana_potion_bundle").setCreativeTab(itemTab);
		crystalWrench = (ItemCrystalWrench)new ItemCrystalWrench().setUnlocalizedAndTextureName("arsmagica2:crystal_wrench").setCreativeTab(itemTab);
		spawnEgg = (AMSpawnEgg)new AMSpawnEgg().setUnlocalizedAndTextureName("am_spawnegg").setCreativeTab(itemTab);
		arcaneCompendium = (ItemArcaneCompendium)new ItemArcaneCompendium().setUnlocalizedAndTextureName("arsmagica2:ArcaneCompendium").setCreativeTab(itemTab);
		spell_component = new ItemSpellPart();
		//TODO: Item.itemsList[BlocksCommonProxy.wakebloom.blockID] = new ItemWakebloom(BlocksCommonProxy.wakebloom.blockID - 256).setUnlocalizedName("arsmagica2:wakebloom").setCreativeTab(BlocksCommonProxy.blockTab);
		crystalPhylactery = (ItemCrystalPhylactery)new ItemCrystalPhylactery().setUnlocalizedAndTextureName("arsmagica2:crystal_phylactery").setCreativeTab(itemTab);
		itemAMBucket = (ItemAMBucket)new ItemAMBucket().setUnlocalizedAndTextureName("arsmagica2:liquidEssenceBucket").setCreativeTab(itemTab);
		scythe = (ItemNatureGuardianSickle)new ItemNatureGuardianSickle().setUnlocalizedName("arsmagica2:nature_scythe").setCreativeTab(itemTab);
		spellStaffMagitech = (ItemSpellStaff)new ItemSpellStaff(0, -1).setStaffHeadIndex(3).setUnlocalizedAndTextureName("arsmagica2:spell_staff_magitech").setCreativeTab(itemTab);
		liquidEssenceBottle = (ItemLiquidEssenceBottle)new ItemLiquidEssenceBottle().setUnlocalizedAndTextureName("arsmagica2:mana_boost_potion").setCreativeTab(itemTab);
		evilBook = new Item().setUnlocalizedName("arsmagica2:evilBook").setTextureName("arsmagica2:evilBook").setCreativeTab(itemTab);
		woodenLeg = new Item().setUnlocalizedName("arsmagica2:woodenLeg").setTextureName("arsmagica2:woodenLeg").setCreativeTab(itemTab);
		cowHorn = (ItemHellCowHorn)new ItemHellCowHorn().setUnlocalizedName("arsmagica2:cowhorn").setCreativeTab(itemTab);
		magicBroom = (ItemMagicBroom)new ItemMagicBroom().setUnlocalizedAndTextureName("arsmagica2:magic_broom").setCreativeTab(itemTab);
		//witchwoodSlab = new ItemSlab(AMCore.config.getConfigurableItemID("witchwood__doubleslab_placer", 20119), BlocksCommonProxy.witchwoodSingleSlab, BlocksCommonProxy.witchwoodDoubleSlab, true);
		arcaneSpellbook = (ItemArcaneGuardianSpellbook)new ItemArcaneGuardianSpellbook().setUnlocalizedName("arsmagica2:arcane_spellbook").setCreativeTab(itemTab);
		winterGuardianArm = (ItemWinterGuardianArm)new ItemWinterGuardianArm().setUnlocalizedName("arsmagica2:winter_arm").setCreativeTab(itemTab);
		airGuardianLower = (ItemAirSled)new ItemAirSled().setUnlocalizedName("arsmagica2:air_sled").setCreativeTab(itemTab);
		earthGuardianArmor = (ItemEarthGuardianArmor)new ItemEarthGuardianArmor(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, 1).setUnlocalizedName("arsmagica2:earth_armor").setCreativeTab(itemTab);
		waterGuardianOrbs = (ItemWaterGuardianOrbs)new ItemWaterGuardianOrbs(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, 2).setUnlocalizedName("arsmagica2:water_orbs").setCreativeTab(itemTab);
		fireEars = (ItemFireGuardianEars)new ItemFireGuardianEars(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, 0).setUnlocalizedName("arsmagica2:fire_ears").setCreativeTab(itemTab);
		workbenchUpgrade = (ArsMagicaItem)new ArsMagicaItem().setUnlocalizedAndTextureName("arsmagica2:workbenchUpgrade").setCreativeTab(itemTab);
		bindingCatalyst = (ItemBindingCatalyst)new ItemBindingCatalyst().setUnlocalizedName("arsmagica2:bindingCatalyst").setCreativeTab(itemTab);
		//boundBow = new ItemBoundBow().setUnlocalizedName("arsmagica2:bound_bow").setCreativeTab(itemTab);
		itemKeystoneDoor = (ItemKeystoneDoor)new ItemKeystoneDoor().setUnlocalizedName("arsmagica2:keystoneDoor").setCreativeTab(BlocksCommonProxy.blockTab);
		enderBoots = (ItemEnderBoots)new ItemEnderBoots(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, ArmorHelper.getArmorRenderIndex("ender"), 3).setUnlocalizedName("arsmagica2:enderBoots").setCreativeTab(itemTab);
		wardingCandle = (ItemCandle)new ItemCandle().setUnlocalizedAndTextureName("arsmagica2:warding_candle").setCreativeTab(itemTab);
		magitechGoggles = (ItemMagitechGoggles)new ItemMagitechGoggles(ArmorHelper.getArmorRenderIndex("magitech")).setUnlocalizedAndTextureName("arsmagica2:magitechGoggles").setCreativeTab(itemTab);
		flickerJar = (ItemFlickerJar)new ItemFlickerJar().setUnlocalizedAndTextureName("arsmagica2:flicker_jar").setCreativeTab(itemTab);
		runeBag = (ItemRuneBag)new ItemRuneBag().setUnlocalizedName("arsmagica2:runebag").setCreativeTab(itemTab);
		lightningCharm = (ItemLightningCharm)new ItemLightningCharm().setUnlocalizedAndTextureName("arsmagica2:lightningcharm").setCreativeTab(itemTab);
		lifeWard = (ItemLifeWard)new ItemLifeWard().setUnlocalizedAndTextureName("arsmagica2:lifeward").setCreativeTab(itemTab);
		flickerFocus = (ItemFlickerFocus)new ItemFlickerFocus().setUnlocalizedAndTextureName("arsmagica2:flickerFocus").setCreativeTab(itemTab);
		playerjournal = (ItemJournal)new ItemJournal().setUnlocalizedName("arsmagica2:playerjournal").setCreativeTab(itemTab);
		manaMartini = (ItemManaMartini)new ItemManaMartini().setUnlocalizedAndTextureName("arsmagica2:mana_martini").setCreativeTab(itemTab);
		inscriptionUpgrade = (InscriptionTableUpgrade)new InscriptionTableUpgrade().setUnlocalizedName("arsmagica2:inscription_upgrade").setCreativeTab(itemTab);

		addItemStackToChestGen(new ItemStack(rune, 1, rune.META_INF_ORB_BLUE), 1, 1, 3, ChestGenHooks.DUNGEON_CHEST, ChestGenHooks.MINESHAFT_CORRIDOR, ChestGenHooks.PYRAMID_DESERT_CHEST, ChestGenHooks.PYRAMID_JUNGLE_CHEST, ChestGenHooks.STRONGHOLD_CORRIDOR, ChestGenHooks.STRONGHOLD_CROSSING);
		addItemStackToChestGen(new ItemStack(rune, 1, rune.META_INF_ORB_BLUE), 1, 1, 5, ChestGenHooks.STRONGHOLD_LIBRARY);

		addItemStackToChestGen(new ItemStack(rune, 1, rune.META_INF_ORB_GREEN), 1, 1, 1, ChestGenHooks.DUNGEON_CHEST, ChestGenHooks.MINESHAFT_CORRIDOR, ChestGenHooks.PYRAMID_DESERT_CHEST, ChestGenHooks.PYRAMID_JUNGLE_CHEST, ChestGenHooks.STRONGHOLD_CORRIDOR, ChestGenHooks.STRONGHOLD_CROSSING);
		addItemStackToChestGen(new ItemStack(rune, 1, rune.META_INF_ORB_GREEN), 1, 1, 1, ChestGenHooks.STRONGHOLD_LIBRARY);

		addItemStackToChestGen(new ItemStack(rune, 1, rune.META_INF_ORB_RED), 1, 1, 6, ChestGenHooks.STRONGHOLD_LIBRARY);

		protectiveArmors = new Item[]{
				mageHood, mageArmor, mageLeggings, mageBoots,
				battlemageHood, battlemageArmor, battlemageLeggings, battlemageBoots,
				//archmageHood, archmageArmor, archmageLeggings, archmageBoots
		};

		RegisterItems();

		itemTab.setIconItemIndex(itemOre);

		Affinity.NONE.setRepresentItem(essence, essence.META_BASE_CORE);
		Affinity.AIR.setRepresentItem(essence, essence.META_AIR);
		Affinity.ARCANE.setRepresentItem(essence, essence.META_ARCANE);
		Affinity.EARTH.setRepresentItem(essence, essence.META_EARTH);
		Affinity.ENDER.setRepresentItem(essence, essence.META_ENDER);
		Affinity.FIRE.setRepresentItem(essence, essence.META_FIRE);
		Affinity.ICE.setRepresentItem(essence, essence.META_ICE);
		Affinity.LIFE.setRepresentItem(essence, essence.META_LIFE);
		Affinity.LIGHTNING.setRepresentItem(essence, essence.META_LIGHTNING);
		Affinity.NATURE.setRepresentItem(essence, essence.META_NATURE);
		Affinity.WATER.setRepresentItem(essence, essence.META_WATER);

		natureScytheEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(scythe));
		arcaneSpellBookEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(arcaneSpellbook));
		winterArmEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(winterGuardianArm));
		fireEarsEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(fireEars));
		earthArmorEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(earthGuardianArmor));
		airSledEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(airGuardianLower));
		waterOrbsEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(waterGuardianOrbs));
		enderBootsEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(enderBoots));
		lightningCharmEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(lightningCharm));
		lifeWardEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(lifeWard));
	}

	private void addItemStackToChestGen(ItemStack stack, int min, int max, int weight, String... categories){
		for (String s : categories){
			ChestGenHooks.addItem(s, new WeightedRandomChestContent(stack, min, max, weight));
		}
	}

	private void RegisterItems(){
		registerItem(spell, "spellBase");
		registerItem(itemOre, "itemOre");
		//Essences
		registerItem(essence, "essence");
		registerItem(essenceBag, "essenceBag");

		//spell casting items
		registerItem(spellParchment, "spellParchment");
		registerItem(spellBook, "spellBook");

		//runes
		registerItem(rune, "rune");

		registerItem(lesserFocus, "lesserFocus");
		registerItem(standardFocus, "standardFocus");
		registerItem(greaterFocus, "greaterFocus");
		registerItem(manaFocus, "manaFocus");
		registerItem(chargeFocus, "chargeFocus");
		registerItem(playerFocus, "playerFocus");
		registerItem(mobFocus, "mobFocus");
		registerItem(itemFocus, "itemFocus");
		registerItem(creatureFocus, "creatureFocus");
		registerItem(flickerFocus, "flickerFocus");

		registerItem(wizardChalk, "blueChalk");

		registerItem(manaCake, "manaCake");

		registerItem(keystone, "keystone");

		//affinity books
		registerItem(bookAffinity, "bookAffinity");

		registerItem(lesserManaPotion, "lesserManaPotion");
		registerItem(standardManaPotion, "standardManaPotion");
		registerItem(greaterManaPotion, "greaterManaPotion");
		registerItem(epicManaPotion, "epicManaPotion");
		registerItem(legendaryManaPotion, "legendaryManaPotion");

		registerItem(manaPotionBundle, "manaPotionBundle");

		//armor
		registerItem(mageHood, "mageHood");
		registerItem(mageArmor, "mageArmor");
		registerItem(mageLeggings, "mageLeggings");
		registerItem(mageBoots, "mageBoots");

		registerItem(battlemageHood, "battlemageHood");
		registerItem(battlemageArmor, "battlemageArmor");
		registerItem(battlemageLeggings, "battlemageLeggings");
		registerItem(battlemageBoots, "battlemageBoots");

		/*registerItem(archmageHood, "ArchmageHood");
		registerItem(archmageArmor, "ArchmageArmor");
		registerItem(archmageLeggings, "ArchmageLeggings");
		registerItem(archmageBoots, "ArchmageBoots");*/

		//bound items
		registerItem(BoundHoe, "BoundHoeNor");
		registerItem(BoundShovel, "BoundShovelNor");
		registerItem(BoundAxe, "BoundAxeNor");
		registerItem(BoundPickaxe, "BoundPickaxeNor");
		registerItem(BoundSword, "BoundSwordNor");
		registerItem(spawnEgg, "AMSpawnEgg");

		registerItem(deficitCrystal, "deficitCrystal");

		registerItem(crystalWrench, "crystal_wrench");

		registerItem(arcaneCompendium, "ArcaneCompendium");
		registerItem(spell_component, "SpellComponent");

		registerItem(crystalPhylactery, "Crystal Phylactery");

		registerItem(itemAMBucket, "liquidEssenceBucket");

		registerItem(scythe, "natureScythe");

		registerItem(spellStaffMagitech, "magitechStaff");

		registerItem(liquidEssenceBottle, "liquidEssenceBottle");
		registerItem(bindingCatalyst, "bindingCatalyst");

		registerItem(evilBook, "evilBook");
		registerItem(woodenLeg, "woodenLeg");
		registerItem(cowHorn, "cowHorn");

		registerItem(magicBroom, "magicBroom");

		registerItem(arcaneSpellbook, "arcane_spellbook");
		registerItem(winterGuardianArm, "winter_arm");
		registerItem(airGuardianLower, "air_sled");
		registerItem(earthGuardianArmor, "earth_armor");
		registerItem(waterGuardianOrbs, "water_orbs");
		registerItem(fireEars, "fire_ears");

		registerItem(workbenchUpgrade, "workbenchUpgrade");
		registerItem(itemKeystoneDoor, "keystoneDoorPlacer");
		registerItem(enderBoots, "enderBoots");
		registerItem(wardingCandle, "wardingCandle");
		registerItem(magitechGoggles, "magitechGoggles");
		registerItem(flickerJar, "flickerJar");
		registerItem(runeBag, "runeBag");
		registerItem(lightningCharm, "lightningCharm");
		registerItem(lifeWard, "lifeWard");
		registerItem(playerjournal, "playerjournal");
		registerItem(manaMartini, "manaMartini");
		registerItem(inscriptionUpgrade, "inscriptionUpgrade");

		//registerItem(boundBow, "boundBow");

		OreDictionary.registerOre("dustVinteum", new ItemStack(itemOre, 1, itemOre.META_VINTEUMDUST));
		OreDictionary.registerOre("arcaneAsh", new ItemStack(itemOre, 1, itemOre.META_ARCANEASH));
		OreDictionary.registerOre("gemBlueTopaz", new ItemStack(itemOre, 1, itemOre.META_BLUETOPAZ));
		OreDictionary.registerOre("gemChimerite", new ItemStack(itemOre, 1, itemOre.META_CHIMERITE));
		OreDictionary.registerOre("gemMoonstone", new ItemStack(itemOre, 1, itemOre.META_MOONSTONE));
		OreDictionary.registerOre("gemSunstone", new ItemStack(itemOre, 1, itemOre.META_SUNSTONE));
	}

	private void registerItem(Item item, String name){
		arsMagicaItemsList.add(item);
		GameRegistry.registerItem(item, name);
	}

	public void InitRecipes(){
		//crafting recipes
		GameRegistry.addRecipe(
				new ItemStack(itemOre, 1, itemOre.META_ARCANECOMPOUND),
				new Object[]
						{
								"BRN", "G G", "NRB",
								Character.valueOf('B'), Blocks.stone,
								Character.valueOf('R'), Items.redstone,
								Character.valueOf('N'), Blocks.netherrack,
								Character.valueOf('G'), Items.glowstone_dust
						});

		//spell crafting
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(spellParchment, 1),
				new Object[]{
						"S",
						"P",
						"S",
						Character.valueOf('S'), "stickWood",
						Character.valueOf('P'), Items.paper
				}));

		//spell book
		GameRegistry.addRecipe(
				new ItemStack(spellBook, 1),
				new Object[]{
						"SLL",
						"SPP",
						"SLL",
						Character.valueOf('S'), Items.string,
						Character.valueOf('L'), Items.leather,
						Character.valueOf('P'), Items.paper
				});

		//crystal wrench
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crystalWrench),
				new Object[]{
						"I I",
						"AVD",
						" I ",
						Character.valueOf('I'), Items.iron_ingot,
						Character.valueOf('A'), BlocksCommonProxy.cerublossom,
						Character.valueOf('D'), BlocksCommonProxy.desertNova,
						Character.valueOf('V'), "dustVinteum",
				}));

		//spell book colors
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 0), new Object[]{"dyeBrown", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //brown
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 1), new Object[]{"dyeCyan", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //cyan
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 2), new Object[]{"dyeGray", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //gray
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 3), new Object[]{"dyeLightBlue", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //light blue
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 4), new Object[]{"dyeWhite", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //white
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 5), new Object[]{"dyeBlack", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //black
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 6), new Object[]{"dyeOrange", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //orange
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 7), new Object[]{"dyePurple", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //purple
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 8), new Object[]{"dyeBlue", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //blue
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 9), new Object[]{"dyeGreen", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //green
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 10), new Object[]{"dyeYellow", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //yellow
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 11), new Object[]{"dyeRed", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //red
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 12), new Object[]{"dyeLime", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //lime
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 13), new Object[]{"dyePink", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //pink
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 14), new Object[]{"dyeMagenta", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //magenta
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(spellBook, 1, 15), new Object[]{"dyeLightGray", new ItemStack(spellBook, 1, AMCore.ANY_META)}));  //light gray

		GameRegistry.addRecipe(new ItemStack(runeBag), new Object[]{
				"LLL", "W W", "LLL",
				Character.valueOf('L'), Items.leather,
				Character.valueOf('W'), new ItemStack(Blocks.wool, 1, AMCore.ANY_META),
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(magicBroom, new Object[]{
				" S ",
				"ASA",
				" H ",
				Character.valueOf('S'), "stickWood",
				Character.valueOf('A'), "arcaneAsh",
				Character.valueOf('H'), Blocks.hay_block
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(woodenLeg, new Object[]{
				"P",
				"L",
				"S",
				Character.valueOf('P'), "plankWood",
				Character.valueOf('L'), "slabWood",
				Character.valueOf('S'), Items.stick
		}));

		GameRegistry.addShapelessRecipe(
				new ItemStack(evilBook), new Object[]{
						new ItemStack(woodenLeg),
						new ItemStack(arcaneCompendium)
				});

		GameRegistry.addShapelessRecipe(new ItemStack(playerjournal), new Object[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE),
				new ItemStack(Items.writable_book)
		});

		GameRegistry.addShapelessRecipe(
				new ItemStack(manaPotionBundle, 1, (0 << 8) + 3), new Object[]{
						new ItemStack(lesserManaPotion, 1, AMCore.ANY_META),
						new ItemStack(lesserManaPotion, 1, AMCore.ANY_META),
						new ItemStack(lesserManaPotion, 1, AMCore.ANY_META),
						new ItemStack(Items.string)
				});

		GameRegistry.addShapelessRecipe(
				new ItemStack(manaPotionBundle, 1, (1 << 8) + 3), new Object[]{
						new ItemStack(standardManaPotion, 1, AMCore.ANY_META),
						new ItemStack(standardManaPotion, 1, AMCore.ANY_META),
						new ItemStack(standardManaPotion, 1, AMCore.ANY_META),
						new ItemStack(Items.string)
				});

		GameRegistry.addShapelessRecipe(
				new ItemStack(manaPotionBundle, 1, (2 << 8) + 3), new Object[]{
						new ItemStack(greaterManaPotion, 1, AMCore.ANY_META),
						new ItemStack(greaterManaPotion, 1, AMCore.ANY_META),
						new ItemStack(greaterManaPotion, 1, AMCore.ANY_META),
						new ItemStack(Items.string)
				});

		GameRegistry.addShapelessRecipe(
				new ItemStack(manaPotionBundle, 1, (3 << 8) + 3), new Object[]{
						new ItemStack(epicManaPotion, 1, AMCore.ANY_META),
						new ItemStack(epicManaPotion, 1, AMCore.ANY_META),
						new ItemStack(epicManaPotion, 1, AMCore.ANY_META),
						new ItemStack(Items.string)
				});

		GameRegistry.addShapelessRecipe(
				new ItemStack(manaPotionBundle, 1, (4 << 8) + 3), new Object[]{
						new ItemStack(legendaryManaPotion, 1, AMCore.ANY_META),
						new ItemStack(legendaryManaPotion, 1, AMCore.ANY_META),
						new ItemStack(legendaryManaPotion, 1, AMCore.ANY_META),
						new ItemStack(Items.string)
				});

		//blank rune
		GameRegistry.addRecipe(
				new ItemStack(rune, 2, rune.META_BLANK),
				new Object[]{
						" S ", "SSS", "SS ",
						Character.valueOf('S'), Blocks.cobblestone
				});
		//blue rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_BLUE),
				new Object[]{
						"dyeBlue",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//red rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_RED),
				new Object[]{
						"dyeRed",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//yellow rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_YELLOW),
				new Object[]{
						"dyeYellow",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//orange rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_ORANGE),
				new Object[]{
						"dyeOrange",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//green rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_GREEN),
				new Object[]{
						"dyeGreen",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//purple rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_PURPLE),
				new Object[]{
						"dyePurple",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//white rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_WHITE),
				new Object[]{
						"dyeWhite",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//black rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_BLACK),
				new Object[]{
						"dyeBlack",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//brown rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_BROWN),
				new Object[]{
						"dyeBrown",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//cyan rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_CYAN),
				new Object[]{
						"dyeCyan",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//gray rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_GRAY),
				new Object[]{
						"dyeGray",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//light blue rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_LIGHTBLUE),
				new Object[]{
						"dyeLightBlue",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//light gray rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_LIGHTGRAY),
				new Object[]{
						"dyeLightGray",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//magenta rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_MAGENTA),
				new Object[]{
						"dyeMagenta",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//pink rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_PINK),
				new Object[]{
						"dyePink",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));
		//pink rune
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(rune, 1, rune.META_LIME),
				new Object[]{
						"dyeLime",
						new ItemStack(rune, 1, rune.META_BLANK)
				}));

		//wizard chalk
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(wizardChalk, 1),
				new Object[]{
						"dyeWhite",
						new ItemStack(Items.clay_ball),
						"dustVinteum",
						new ItemStack(Items.flint),
						new ItemStack(Items.paper)
				}));

		//empty flicker jar
		GameRegistry.addRecipe(new ItemStack(flickerJar, 1),
				new Object[]{
						"NWN",
						"G G",
						" G ",
						Character.valueOf('W'), BlocksCommonProxy.magicWall,
						Character.valueOf('N'), Items.gold_nugget,
						Character.valueOf('G'), Blocks.glass_pane
				});

		//warding candle
		GameRegistry.addRecipe(new ItemStack(wardingCandle), new Object[]{
				"S",
				"F",
				"P",
				Character.valueOf('S'), Items.string,
				Character.valueOf('F'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ANIMALFAT),
				Character.valueOf('P'), BlocksCommonProxy.witchwoodSingleSlab
		});

		//magitech goggles
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(magitechGoggles), new Object[]{
				"LLL",
				"CGC",
				"TLT",
				Character.valueOf('C'), "gemChimerite",
				Character.valueOf('T'), "gemBlueTopaz",
				Character.valueOf('L'), Items.leather,
				Character.valueOf('G'), Items.gold_nugget,
		}));

		//magitech staff
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(spellStaffMagitech), new Object[]{
				" GT",
				"G G",
				"GG ",
				Character.valueOf('T'), "gemBlueTopaz",
				Character.valueOf('G'), Items.gold_nugget,
		}));

		//armor recipes
		//MAGE
		GameRegistry.addRecipe(new ItemStack(mageHood, 1),
				new Object[]{
						"WLW", "WRW", " B ",
						Character.valueOf('W'), new ItemStack(Blocks.wool, 1, 12),
						Character.valueOf('L'), Items.leather,
						Character.valueOf('R'), new ItemStack(rune, 1, rune.META_PURPLE),
						Character.valueOf('B'), new ItemStack(Items.potionitem, 1, 0),
				});
		GameRegistry.addRecipe(new ItemStack(mageArmor, 1),
				new Object[]{
						"RCR", "WLW", "WWW",
						Character.valueOf('W'), new ItemStack(Blocks.wool, 1, 12),
						Character.valueOf('L'), Items.leather,
						Character.valueOf('R'), new ItemStack(rune, 1, rune.META_WHITE),
						Character.valueOf('C'), Items.coal
				});
		GameRegistry.addRecipe(new ItemStack(mageLeggings, 1),
				new Object[]{
						"WRW", "WGW", "L L",
						Character.valueOf('W'), new ItemStack(Blocks.wool, 1, 12),
						Character.valueOf('L'), Items.leather,
						Character.valueOf('R'), new ItemStack(rune, 1, rune.META_YELLOW),
						Character.valueOf('G'), Items.gunpowder
				});
		GameRegistry.addRecipe(new ItemStack(mageBoots, 1),
				new Object[]{
						"R R", "L L", "WFW",
						Character.valueOf('W'), new ItemStack(Blocks.wool, 1, 12),
						Character.valueOf('L'), Items.leather,
						Character.valueOf('R'), new ItemStack(rune, 1, rune.META_BLACK),
						Character.valueOf('F'), Items.feather
				});
		//BATTLEMAGE
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(battlemageHood, 1),
				new Object[]{
						"WLW", "WRW", " E ",
						Character.valueOf('W'), new ItemStack(Blocks.obsidian),
						Character.valueOf('L'), BlocksCommonProxy.goldInlay,
						Character.valueOf('R'), new ItemStack(rune, 1, 1),
						Character.valueOf('E'), new ItemStack(essence, 1, 4)
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(battlemageArmor, 1),
				new Object[]{
						"RER", "WLW", "WWW",
						Character.valueOf('W'), new ItemStack(Blocks.obsidian),
						Character.valueOf('E'), new ItemStack(essence, 1, 1),
						Character.valueOf('R'), new ItemStack(rune, 1, 1),
						Character.valueOf('L'), BlocksCommonProxy.goldInlay
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(battlemageLeggings, 1),
				new Object[]{
						"WRW", "LEL", "W W",
						Character.valueOf('W'), new ItemStack(Blocks.obsidian),
						Character.valueOf('L'), BlocksCommonProxy.goldInlay,
						Character.valueOf('R'), new ItemStack(rune, 1, 1),
						Character.valueOf('E'), new ItemStack(essence, 1, 3)
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(battlemageBoots, 1),
				new Object[]{
						"R R", "WEW", "WLW",
						Character.valueOf('W'), new ItemStack(Blocks.obsidian),
						Character.valueOf('L'), BlocksCommonProxy.goldInlay,
						Character.valueOf('R'), new ItemStack(rune, 1, 1),
						Character.valueOf('E'), new ItemStack(essence, 1, 2)
				}));
		//ARCHMAGE
		/*GameRegistry.addRecipe(new ItemStack(archmageHood, 1),
				new Object[]{
			"WPW", "WRW",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(rune, 1, 6)
		});
		GameRegistry.addRecipe(new ItemStack(archmageArmor, 1),
				new Object[]{
			"RGR", "WPW", "WWW",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(rune, 1, 6),
			Character.valueOf('G'), Item.ingotGold
		});
		GameRegistry.addRecipe(new ItemStack(archmageLeggings, 1),
				new Object[]{
			"WPW", "R R", "W W",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(rune, 1, 6)
		});
		GameRegistry.addRecipe(new ItemStack(archmageBoots, 1),
				new Object[]{
			"P R", "W W", "W W",
			Character.valueOf('W'), new ItemStack(Blocks.cloth, 1, 0),
			Character.valueOf('P'), new ItemStack(essence, 1, 10),
			Character.valueOf('R'), new ItemStack(rune, 1, 6)
		});*/

		GameRegistry.addRecipe(new ItemStack(essenceBag), new Object[]{
				"LLL", "WNW", "LLL",
				Character.valueOf('L'), Items.leather,
				Character.valueOf('W'), new ItemStack(Blocks.wool, 1, AMCore.ANY_META),
				Character.valueOf('N'), Items.gold_nugget
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crystalPhylactery), new Object[]{
				" B ", "GPG", " W ",
				Character.valueOf('B'), "gemMoonstone",
				Character.valueOf('W'), BlocksCommonProxy.magicWall,
				Character.valueOf('G'), Blocks.glass,
				Character.valueOf('P'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_PURIFIEDVINTEUM)
		}));

		//lesser mana potion
		//GameRegistry.addRecipe(new ItemStack(Item.potion, 1, ))

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(keystone, 1), new Object[]{
				"GIG",
				"IVI",
				"GIG",
				Character.valueOf('G'), Items.gold_ingot,
				Character.valueOf('I'), Items.iron_ingot,
				Character.valueOf('V'), "dustVinteum"
		}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemOre, 1, itemOre.META_PURIFIEDVINTEUM), new Object[]{
				new ItemStack(BlocksCommonProxy.cerublossom),
				new ItemStack(BlocksCommonProxy.desertNova),
				"dustVinteum",
				"arcaneAsh"
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(manaCake, 3, 0), new Object[]{
				new ItemStack(BlocksCommonProxy.cerublossom),
				new ItemStack(BlocksCommonProxy.desertNova),
				new ItemStack(Items.sugar),
				new ItemStack(Items.wheat)
		});

		GameRegistry.addShapelessRecipe(new ItemStack(lesserManaPotion), new Object[]{
				new ItemStack(Items.wheat_seeds),
				new ItemStack(Items.sugar),
				new ItemStack(Items.potionitem, 1, Short.MAX_VALUE)
		});

		GameRegistry.addShapelessRecipe(new ItemStack(standardManaPotion), new Object[]{
				new ItemStack(Items.gunpowder),
				new ItemStack(lesserManaPotion, 1, Short.MAX_VALUE)
		});

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(greaterManaPotion), new Object[]{
				"dustVinteum",
				new ItemStack(standardManaPotion, 1, Short.MAX_VALUE)
		}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(epicManaPotion), new Object[]{
				"arcaneAsh",
				new ItemStack(greaterManaPotion, 1, Short.MAX_VALUE)
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(legendaryManaPotion), new Object[]{
				new ItemStack(itemOre, 1, itemOre.META_PURIFIEDVINTEUM),
				new ItemStack(epicManaPotion, 1, Short.MAX_VALUE)
		});

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(liquidEssenceBottle), new Object[]{
				"gemChimerite",
				new ItemStack(BlocksCommonProxy.tarmaRoot),
				new ItemStack(ItemsCommonProxy.itemAMBucket)
		}));

		GameRegistry.addRecipe(new ItemStack(lesserFocus), ((ItemFocus)lesserFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(standardFocus), ((ItemFocus)standardFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(greaterFocus), ((ItemFocus)greaterFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(manaFocus), ((ItemFocus)manaFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(chargeFocus), ((ItemFocus)chargeFocus).getRecipeItems());

		GameRegistry.addRecipe(new ItemStack(playerFocus), ((ItemFocus)playerFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(mobFocus), ((ItemFocus)mobFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(itemFocus), ((ItemFocus)itemFocus).getRecipeItems());
		GameRegistry.addRecipe(new ItemStack(creatureFocus), ((ItemFocus)creatureFocus).getRecipeItems());

		//binding catalysts
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bindingCatalyst, 1, bindingCatalyst.META_AXE), new Object[]{
				"SVS",
				"SAS",
				"SVS",
				Character.valueOf('V'), "dustVinteum",
				Character.valueOf('S'), Items.slime_ball,
				Character.valueOf('A'), Items.golden_axe
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bindingCatalyst, 1, bindingCatalyst.META_PICK), new Object[]{
				"SVS",
				"SAS",
				"SVS",
				Character.valueOf('V'), "dustVinteum",
				Character.valueOf('S'), Items.slime_ball,
				Character.valueOf('A'), Items.golden_pickaxe
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bindingCatalyst, 1, bindingCatalyst.META_SHOVEL), new Object[]{
				"SVS",
				"SAS",
				"SVS",
				Character.valueOf('V'), "dustVinteum",
				Character.valueOf('S'), Items.slime_ball,
				Character.valueOf('A'), Items.golden_shovel
		}));
		GameRegistry.addRecipe(new ItemStack(bindingCatalyst, 1, bindingCatalyst.META_SWORD), new Object[]{
				"SVS",
				"SAS",
				"SVS",
				Character.valueOf('V'), new ItemStack(itemOre, 1, itemOre.META_PURIFIEDVINTEUM),
				Character.valueOf('S'), Items.slime_ball,
				Character.valueOf('A'), Items.golden_sword
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bindingCatalyst, 1, bindingCatalyst.META_HOE), new Object[]{
				"SVS",
				"SAS",
				"SVS",
				Character.valueOf('V'), "dustVinteum",
				Character.valueOf('S'), Items.slime_ball,
				Character.valueOf('A'), Items.golden_hoe
		}));

		GameRegistry.addRecipe(new ItemStack(itemKeystoneDoor, 1, itemKeystoneDoor.KEYSTONE_DOOR), new Object[]{
				"PWP",
				"RRR",
				"PWP",
				Character.valueOf('P'), BlocksCommonProxy.witchwoodPlanks,
				Character.valueOf('R'), new ItemStack(rune, 1, rune.META_BLANK),
				Character.valueOf('W'), BlocksCommonProxy.magicWall
		});

		GameRegistry.addRecipe(new ItemStack(itemKeystoneDoor, 1, itemKeystoneDoor.SPELL_SEALED_DOOR), new Object[]{
				" G ",
				"SKS",
				" L ",
				Character.valueOf('G'), ItemsCommonProxy.greaterFocus,
				Character.valueOf('S'), ItemsCommonProxy.standardFocus,
				Character.valueOf('L'), ItemsCommonProxy.lesserFocus,
				Character.valueOf('K'), new ItemStack(ItemsCommonProxy.itemKeystoneDoor, 1, itemKeystoneDoor.KEYSTONE_DOOR)
		});

		GameRegistry.addShapelessRecipe(new ItemStack(manaMartini), new Object[]{
				new ItemStack(Blocks.ice),
				new ItemStack(Items.potato),
				new ItemStack(Items.sugar),
				new ItemStack(Items.stick),
				new ItemStack(standardManaPotion)
		});

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(inscriptionUpgrade, 1, 0),
				new ItemStack(Items.book),
				new ItemStack(Items.string),
				new ItemStack(Items.feather),
				"dyeBlack"
		));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(inscriptionUpgrade, 1, 1),
				new ItemStack(Blocks.carpet, 1, Short.MAX_VALUE),
				new ItemStack(Items.book),
				new ItemStack(wizardChalk)
		));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(inscriptionUpgrade, 1, 2),
				new ItemStack(wardingCandle),
				new ItemStack(Items.flint_and_steel),
				new ItemStack(itemOre, 1, itemOre.META_ANIMALFAT),
				new ItemStack(Items.glass_bottle),
				new ItemStack(Items.book)
		));

		GameRegistry.addShapelessRecipe(new ItemStack(workbenchUpgrade), new ItemStack(BlocksCommonProxy.magiciansWorkbench), new ItemStack(Blocks.chest), new ItemStack(Blocks.crafting_table), new ItemStack(Blocks.crafting_table), new ItemStack(Items.gold_ingot));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(deficitCrystal), new ItemStack(Items.diamond), "arcaneAsh", new ItemStack(Items.ender_eye)));
	}

	public void postInit(){
		//flicker focus recipes
		for (int i : FlickerOperatorRegistry.instance.getMasks()){
			IFlickerFunctionality func = FlickerOperatorRegistry.instance.getOperatorForMask(i);
			if (func != null){
				Object[] recipeItems = func.getRecipe();
				if (recipeItems != null){
					GameRegistry.addRecipe(new ItemStack(flickerFocus, 1, i), recipeItems);
				}else{
					FMLLog.info("Ars Magica 2 >> Flicker operator %s was registered with no recipe.  It is un-craftable.  This may have been intentional.", func.getClass().getSimpleName());
				}
			}
		}
	}
}
