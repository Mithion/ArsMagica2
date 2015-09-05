package am2.utility;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeUtilities{

	public static Object[] getRecipeItems(Object recipe){
		if (recipe instanceof ShapedRecipes){
			return getShapedRecipeItems((ShapedRecipes)recipe);
		}else if (recipe instanceof ShapelessRecipes){
			return getShapelessRecipeItems((ShapelessRecipes)recipe);
		}else if (recipe instanceof ShapedOreRecipe){
			return getShapedOreRecipeItems((ShapedOreRecipe)recipe);
		}else if (recipe instanceof ShapelessOreRecipe){
			return getShapelessOreRecipeItems((ShapelessOreRecipe)recipe);
		}
		return new Object[0];
	}

	private static Object[] getShapedRecipeItems(ShapedRecipes recipe){
		return recipe.recipeItems;
	}

	private static Object[] getShapelessRecipeItems(ShapelessRecipes recipe){
		return recipe.recipeItems.toArray();
	}

	private static Object[] getShapedOreRecipeItems(ShapedOreRecipe recipe){
		Object[] components = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, 3);
		return components;
	}

	private static Object[] getShapelessOreRecipeItems(ShapelessOreRecipe recipe){
		ArrayList components = ReflectionHelper.getPrivateValue(ShapelessOreRecipe.class, recipe, 1);
		return components.toArray();
	}

	public static IRecipe getRecipeFor(ItemStack item){

		if (item == null || item.getItem() == null) return null;

		try{
			List list = CraftingManager.getInstance().getRecipeList();
			ArrayList possibleRecipes = new ArrayList();
			for (Object recipe : list){
				if (recipe instanceof IRecipe){
					ItemStack output = ((IRecipe)recipe).getRecipeOutput();
					if (output == null) continue;
					if (output.getItem() == item.getItem() && (output.getItemDamage() == Short.MAX_VALUE || output.getItemDamage() == item.getItemDamage())){
						possibleRecipes.add(recipe);
					}
				}
			}

			if (possibleRecipes.size() > 0){
				for (Object recipe : possibleRecipes){
					if (((IRecipe)recipe).getRecipeOutput().getItemDamage() == item.getItemDamage()){
						return (IRecipe)recipe;
					}
				}
				return (IRecipe)possibleRecipes.get(0);
			}
		}catch (Throwable t){

		}

		return null;
	}

	public static void addShapedRecipeFirst(List recipeList, ItemStack itemstack, Object... objArray){
		String var3 = "";
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;

		if (objArray[var4] instanceof String[]){
			String[] var7 = ((String[])objArray[var4++]);

			for (int var8 = 0; var8 < var7.length; ++var8){
				String var9 = var7[var8];
				++var6;
				var5 = var9.length();
				var3 = var3 + var9;
			}
		}else{
			while (objArray[var4] instanceof String){
				String var11 = (String)objArray[var4++];
				++var6;
				var5 = var11.length();
				var3 = var3 + var11;
			}
		}

		HashMap var12;

		for (var12 = new HashMap(); var4 < objArray.length; var4 += 2){
			Character var13 = (Character)objArray[var4];
			ItemStack var14 = null;

			if (objArray[var4 + 1] instanceof Item){
				var14 = new ItemStack((Item)objArray[var4 + 1]);
			}else if (objArray[var4 + 1] instanceof Block){
				var14 = new ItemStack((Block)objArray[var4 + 1], 1, Short.MAX_VALUE);
			}else if (objArray[var4 + 1] instanceof ItemStack){
				var14 = (ItemStack)objArray[var4 + 1];
			}

			var12.put(var13, var14);
		}

		ItemStack[] var15 = new ItemStack[var5 * var6];

		for (int var16 = 0; var16 < var5 * var6; ++var16){
			char var10 = var3.charAt(var16);

			if (var12.containsKey(Character.valueOf(var10))){
				var15[var16] = ((ItemStack)var12.get(Character.valueOf(var10))).copy();
			}else{
				var15[var16] = null;
			}
		}

		ShapedRecipes var17 = new ShapedRecipes(var5, var6, var15, itemstack);
		recipeList.add(0, var17);
	}

	public static void addShapelessRecipeFirst(List recipeList, ItemStack par1ItemStack, Object... par2ArrayOfObj){
		ArrayList arraylist = new ArrayList();
		Object[] aobject = par2ArrayOfObj;
		int i = par2ArrayOfObj.length;

		for (int j = 0; j < i; ++j){
			Object object1 = aobject[j];

			if (object1 instanceof ItemStack){
				arraylist.add(((ItemStack)object1).copy());
			}else if (object1 instanceof Item){
				arraylist.add(new ItemStack((Item)object1));
			}else{
				if (!(object1 instanceof Block)){
					throw new RuntimeException("Invalid shapeless recipy!");
				}

				arraylist.add(new ItemStack((Block)object1));
			}
		}

		recipeList.add(0, new ShapelessRecipes(par1ItemStack, arraylist));
	}
}
