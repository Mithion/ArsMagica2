package am2.spell;

import am2.LogHelper;
import am2.api.events.SpellRecipeItemsEvent;
import am2.api.power.PowerTypes;
import am2.api.spell.component.interfaces.ISpellPart;
import am2.items.ItemEssence;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;

public class SpellRecipeManager{
	private final HashMap<ArrayList<Object>, ISpellPart> recipes;

	public static final SpellRecipeManager instance = new SpellRecipeManager();

	private SpellRecipeManager(){
		recipes = new HashMap<ArrayList<Object>, ISpellPart>();
	}

	public void RegisterRecipe(ISpellPart part){
		ArrayList<Object> recipeItems = new ArrayList<Object>();

		Object[] recipe = part.getRecipeItems();
		SpellRecipeItemsEvent event = new SpellRecipeItemsEvent(SkillManager.instance.getSkillName(part), SkillManager.instance.getShiftedPartID(part), recipe);
		MinecraftForge.EVENT_BUS.post(event);
		recipe = event.recipeItems;

		if (recipe == null){
			LogHelper.info("Component %s has been registered with no craftable recipe - is this intentional?  If so, return a 0-length array for recipe.", SkillManager.instance.getDisplayName(part));
			return;
		}
		if (recipe.length == 0){
			return;
		}
		for (int i = 0; i < recipe.length; ++i){
			Object o = recipe[i];
			if (o instanceof Item){
				recipeItems.add(new ItemStack((Item)o));
			}else if (o instanceof Block){
				recipeItems.add(new ItemStack((Block)o));
			}else if (o instanceof String){
				if (((String)o).toLowerCase().startsWith("e:")){
					if (i == recipe.length - 1 || !(recipe[i + 1] instanceof Integer)){
						LogHelper.warn("Error registering recipe.  Power must be declared in Integer pairs (type flags, quantity)).");
						return;
					}

					int[] ids = ParseEssenceIDs((String)o);
					int flag = 0;
					for (int f : ids){
						flag |= f;
					}

					int quantity = (Integer)recipe[++i];
					ItemStack stack = new ItemStack(ItemsCommonProxy.essence, quantity, ItemEssence.META_MAX + flag);
					recipeItems.add(stack);
				}else{
					recipeItems.add(o);
				}
			}else{
				recipeItems.add(o);
			}
		}
		recipes.put(recipeItems, part);
	}

	public static int[] ParseEssenceIDs(String s){
		if (s.toLowerCase().equals("e:*")){
			int[] all = new int[PowerTypes.all().length];
			int count = 0;
			for (PowerTypes type : PowerTypes.all()){
				all[count++] = type.ID();
			}
			return all;
		}
		s = s.toLowerCase().replace("e:", "");
		String[] split = s.split("\\|");
		int[] ids = new int[split.length];
		for (int i = 0; i < split.length; ++i){
			try{
				ids[i] = Integer.parseInt(split[i]);
			}catch (NumberFormatException nex){
				LogHelper.warn("Invalid power type ID while parsing value %s", s);
				ids[i] = 0;
			}
		}
		return ids;
	}

	public ISpellPart getPartByRecipe(ArrayList<ItemStack> recipe){
		return matchRecipe(recipe);
	}

	private ISpellPart matchRecipe(ArrayList<ItemStack> recipe){
		//make a safe working copy
		HashMap<ArrayList<Object>, ISpellPart> safeCopy = new HashMap<ArrayList<Object>, ISpellPart>();
		safeCopy.putAll(recipes);

		//list of non-matching items to be removed at the next opportunity
		ArrayList<ArrayList<Object>> toRemove = new ArrayList<ArrayList<Object>>();

		//remove all recipes not of the correct length
		//==========================================================
		for (ArrayList<Object> arr : safeCopy.keySet()){
			if (arr.size() != recipe.size())
				toRemove.add(arr);
		}

		for (ArrayList<Object> arr : toRemove)
			safeCopy.remove(arr);
		//==========================================================


		//spin through the list and check each item.  If the recipe at the current index does not match,
		//then the entire recipe is not a match.  Remove all non-matching recipes from the possibility list.
		//==========================================================
		int index = 0;

		for (ItemStack recipeItem : recipe){
			toRemove.clear();

			for (ArrayList<Object> arr : safeCopy.keySet()){
				Object o = arr.get(index);
				boolean matches = false;

				if (o instanceof ItemStack){
					matches = compareItemStacks(((ItemStack)o), recipeItem);
				}else if (o instanceof Item){
					matches = ((Item)o) == recipeItem.getItem();
				}else if (o instanceof Block){
					matches = ((Block)o) == Block.getBlockFromItem(recipeItem.getItem());
				}else if (o instanceof String){
					if (((String)o).startsWith("P:")){
						//potion
						String potionDefinition = ((String)o).substring(2);
						matches |= matchPotion(recipeItem, potionDefinition);
					}else{
						ArrayList<ItemStack> oreDictItems = OreDictionary.getOres((String)o);
						for (ItemStack stack : oreDictItems){
							matches |= OreDictionary.itemMatches(stack, recipeItem, false);
						}
					}
				}

				if (!matches){
					toRemove.add(arr);
				}
			}

			index++;

			for (ArrayList<Object> arr : toRemove)
				safeCopy.remove(arr);
		}
		//==========================================================

		if (safeCopy.size() > 1){
			StringBuilder sb = new StringBuilder();
			sb.append("Ars Magica >> Duplicate recipe match on the following spell parts: ");
			for (ISpellPart part : safeCopy.values()){
				sb.append(SkillManager.instance.getSkillName(part) + " ");
			}
			sb.append("- this should be corrected as soon as possible!");
			LogHelper.warn(sb.toString());
		}

		if (safeCopy.size() > 0){
			ISpellPart part = safeCopy.values().iterator().next();
			LogHelper.info("Matched Spell Component: %s", part.getClass().toString());
			return part;
		}

		return null;
	}

	private boolean matchPotion(ItemStack potionStack, String potionDefinition){
		if (potionStack == null || !(potionStack.getItem() instanceof ItemPotion)){
			return false;
		}

		int potionMeta = potionStack.getMetadata();

		String[] potionSections = potionDefinition.split("&");

		boolean match = true;

		for (String s : potionSections){
			s = s.trim();

			if (s.contains("+")) continue;

			boolean bitSet = true;
			for (char c : s.toCharArray()){
				if (c == '!'){
					bitSet = false;
					continue;
				}
				if (Character.isDigit(c)){
					int value = Character.getNumericValue(c);
					match &= (isBitSet(potionMeta, value) == bitSet);
					bitSet = true;
					continue;
				}
			}
		}

		return match;
	}

	public static int parsePotionMeta(String potionDefinition){
		String[] potionSections = potionDefinition.split("&");

		int potionMeta = 0;

		for (String s : potionSections){
			s = s.trim();

			if (s.contains("+")) continue;

			boolean bitSet = true;
			for (char c : s.toCharArray()){
				if (c == '!'){
					bitSet = false;
					continue;
				}
				if (Character.isDigit(c)){
					int value = Character.getNumericValue(c);
					if (bitSet){
						potionMeta = setBit(potionMeta, value);
					}else{
						potionMeta = clearBit(potionMeta, value);
					}
					bitSet = true;
					continue;
				}
			}

		}

		return potionMeta;
	}

	private static int setBit(int value, int bitToSet){
		value |= (1 << bitToSet);
		return value;
	}

	private static int clearBit(int value, int bitToSet){
		value &= ~(1 << bitToSet);
		return value;
	}

	private boolean isBitSet(int value, int bitIndex){
		return (value & (1 << bitIndex)) != 0;
	}

	private boolean compareItemStacks(ItemStack target, ItemStack input){
		if (target.getItem() == ItemsCommonProxy.essence && target.getMetadata() > ItemsCommonProxy.essence.META_MAX){
			int targetMetaMask = target.getMetadata() - ItemsCommonProxy.essence.META_MAX;
			int inputMetaMask = input.getMetadata() - ItemsCommonProxy.essence.META_MAX;

			return target.getItem() == input.getItem() && (targetMetaMask & inputMetaMask) != 0;
		}else{
			return target.getItem() == input.getItem() && (target.getMetadata() == input.getMetadata() || target.getMetadata() == Short.MAX_VALUE) && target.stackSize >= input.stackSize;
		}
	}
}
