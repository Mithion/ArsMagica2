package am2.items;

import am2.AMCore;
import net.minecraft.item.ItemStack;

public class RecipeArsMagica{
	private ItemStack[] craftingComponents;
	private ItemStack output;
	private int recipeID;
	private int componentLength;

	public RecipeArsMagica(ItemStack[] craftingComponents, ItemStack output){
		componentLength = craftingComponents.length;
		this.craftingComponents = craftingComponents;
		this.output = output;
	}

	public boolean recipeIsMatch(ItemStack[] recipeItems){
		if (recipeItems.length != componentLength
				|| recipeItems.length != craftingComponents.length){
			return false;
		}
		for (int i = 0; i < recipeItems.length; ++i){
			if (recipeItems[i] == null && craftingComponents[i] == null)
				continue;
			if (recipeItems[i] == null && craftingComponents[i] != null)
				return false;
			if (recipeItems[i] != null && craftingComponents[i] == null)
				return false;
			if (recipeItems[i].getItem() != craftingComponents[i].getItem()
					|| (craftingComponents[i].getMetadata() != AMCore.ANY_META && recipeItems[i]
					.getMetadata() != craftingComponents[i]
					.getMetadata())){
				return false;
			}
		}
		return true;
	}

	public ItemStack getOutput(){
		return this.output;
	}

	public ItemStack[] getRecipeItems(){
		return this.craftingComponents;
	}

	public ItemStack getFuelID(){
		if (craftingComponents.length == 5)
			return craftingComponents[2];
		return null;
	}
}
