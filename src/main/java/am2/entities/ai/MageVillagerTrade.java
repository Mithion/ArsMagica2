package am2.entities.ai;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class MageVillagerTrade implements IVillageTradeHandler {

	private static int min = 1;
	private static int max = 2;

	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {

		recipeList.clear();
		int numChoices = random.nextInt(4) + 1;

		for (int i = 0; i < numChoices; ++i){
			ItemStack choice = getRandomChoiceForTrade(random);
			if (choice != null){
				MerchantRecipe itemToSell = new MerchantRecipe(new ItemStack(Items.emerald, random.nextInt(max - min) + min), choice);
				recipeList.add(itemToSell);				
			}
		}
	}

	private ItemStack getRandomChoiceForTrade(Random random){
		int itemSeed = random.nextInt(1000);

		HashMap<ItemStack, Integer> weightedRandomChoices = new HashMap<ItemStack, Integer>();
		ItemStack choice = null;

		//15% chance for an essence/ash
		//15% chance for a spell book
		//40% chance for a focus
		//10% chance for magic armor
		//20% chance for spell recipe

		if (itemSeed < 150){	
			//essence
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH), 1000);
			for (int i = 0; i < 13; ++i)
				if (i == 11)
					weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.essence, 1, i), 250);
				else
					weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.essence, 1, i), 120);

			choice = pickRandomWeightedItem(random, weightedRandomChoices);			

			if (choice.getItemDamage() == 11){ //pure essence
				min = 11;
				max = 16;
			}else if (choice.getItem() == ItemsCommonProxy.itemOre){
				min = 2;
				max = 5;
			}else{
				min = 4;
				max = 7;
			}
		}else if (itemSeed >= 150 && itemSeed < 300){			
			//spell book
			min = 2;
			max = 4;
			choice = new ItemStack(ItemsCommonProxy.spellBook);
		}else if (itemSeed >= 300 && itemSeed < 700){
			//focus
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.lesserFocus), 500);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.standardFocus), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.greaterFocus), 50);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.chargeFocus), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.manaFocus), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.playerFocus), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.itemFocus), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.creatureFocus), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.mobFocus), 250);

			choice = pickRandomWeightedItem(random, weightedRandomChoices);			

			if (choice.getItem() == ItemsCommonProxy.greaterFocus){
				min = 4;
				max = 11;
			}else if (choice.getItem() == ItemsCommonProxy.lesserFocus){
				min = 2;
				max = 5;
			}else{
				min = 4;
				max = 7;
			}
		}else if (itemSeed >= 700 && itemSeed < 800){
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.mageHood), 750);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.mageArmor), 750);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.mageLeggings), 750);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.mageBoots), 750);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.battlemageHood), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.battlemageArmor), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.battlemageLeggings), 250);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.battlemageBoots), 250);
			/*weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.archmageHood), 50);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.archmageArmor), 50);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.archmageLeggings), 50);
			weightedRandomChoices.put(new ItemStack(ItemsCommonProxy.archmageBoots), 50);*/

			choice = pickRandomWeightedItem(random, weightedRandomChoices);			

			/*if (choice.getItem() == ItemsCommonProxy.archmageHood || choice.getItem() == ItemsCommonProxy.archmageArmor || choice.getItem() == ItemsCommonProxy.archmageBoots || choice.getItem() == ItemsCommonProxy.archmageLeggings){
				min = 13;
				max = 21;				
			}else*/ if (choice.getItem() == ItemsCommonProxy.battlemageHood || choice.getItem() == ItemsCommonProxy.battlemageArmor || choice.getItem() == ItemsCommonProxy.battlemageBoots || choice.getItem() == ItemsCommonProxy.battlemageLeggings){
				min = 8;
				max = 17;	
			}else if (choice.getItem() == ItemsCommonProxy.mageHood || choice.getItem() == ItemsCommonProxy.mageArmor || choice.getItem() == ItemsCommonProxy.mageBoots || choice.getItem() == ItemsCommonProxy.mageLeggings){			
				min = 4;
				max = 9;
			}else{
				min = 2;
				max = 6;
			}
		}else if (itemSeed >= 800){
			min = 1;
			max = 1;
			choice = new ItemStack(BlocksCommonProxy.seerStone);
		}

		return choice;
	}

	private ItemStack pickRandomWeightedItem(Random random, HashMap<ItemStack, Integer> weightedChoices){
		int totalWeight = 0;

		for (Integer weight : weightedChoices.values()){
			totalWeight += weight;
		}

		int randWeight = random.nextInt(totalWeight);
		ItemStack choice = null;

		for (ItemStack item : weightedChoices.keySet()){
			int weight = weightedChoices.get(item);
			randWeight -= weight;
			if (randWeight <= 0){
				choice = item;
				break;
			}
		}

		return choice;
	}

}
