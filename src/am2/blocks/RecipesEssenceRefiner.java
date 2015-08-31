package am2.blocks;

import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import am2.items.ItemsCommonProxy;
import am2.items.RecipesArsMagica;

public class RecipesEssenceRefiner extends RecipesArsMagica {
	private static final RecipesEssenceRefiner essenceExtractorRecipesBase = new RecipesEssenceRefiner();

	public static final RecipesEssenceRefiner essenceRefinement(){
		return essenceExtractorRecipesBase;
	}

	private RecipesEssenceRefiner(){
		RecipeList = new HashMap();
		InitRecipes();
	}

	private void InitRecipes(){
		//arcane essence
		AddRecipe(new ItemStack[] {
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE));
		//earth essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Blocks.dirt),
				new ItemStack(Blocks.stone),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Blocks.stone),
				new ItemStack(Blocks.obsidian)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH));
		//air essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.feather),
				new ItemStack(BlocksCommonProxy.tarmaRoot),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(BlocksCommonProxy.tarmaRoot),
				new ItemStack(Items.feather)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_AIR));
		AddRecipe(new ItemStack[] {
				new ItemStack(BlocksCommonProxy.tarmaRoot),
				new ItemStack(Items.feather),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.feather),
				new ItemStack(BlocksCommonProxy.tarmaRoot)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_AIR));
		//fire essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.coal),
				new ItemStack(Items.blaze_powder),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.blaze_powder),
				new ItemStack(Items.coal)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_FIRE));
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.blaze_powder),
				new ItemStack(Items.coal),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.coal),
				new ItemStack(Items.blaze_powder)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_FIRE));
		//water essence
		AddRecipe(new ItemStack[] {
				new ItemStack(BlocksCommonProxy.wakebloom),
				new ItemStack(Items.water_bucket),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.water_bucket),
				new ItemStack(BlocksCommonProxy.wakebloom)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER));

		AddRecipe(new ItemStack[] {
				new ItemStack(Items.water_bucket),
				new ItemStack(BlocksCommonProxy.wakebloom),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(BlocksCommonProxy.wakebloom),
				new ItemStack(Items.water_bucket)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER));

		AddRecipe(new ItemStack[] {
				new ItemStack(Items.potionitem, 1, 0),
				new ItemStack(BlocksCommonProxy.wakebloom),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(BlocksCommonProxy.wakebloom),
				new ItemStack(Items.potionitem, 1, 0)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER));

		AddRecipe(new ItemStack[] {
				new ItemStack(BlocksCommonProxy.wakebloom),
				new ItemStack(Items.potionitem, 1, 0),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.potionitem, 1, 0),
				new ItemStack(BlocksCommonProxy.wakebloom)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER));
		//ice essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Blocks.snow),
				new ItemStack(Blocks.ice),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Blocks.ice),
				new ItemStack(Blocks.snow)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE));
		AddRecipe(new ItemStack[] {
				new ItemStack(Blocks.ice),
				new ItemStack(Blocks.snow),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Blocks.snow),
				new ItemStack(Blocks.ice)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE));
		//lightning essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.redstone),
				new ItemStack(Items.glowstone_dust),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.glowstone_dust),
				new ItemStack(Items.redstone),
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIGHTNING));
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.glowstone_dust),
				new ItemStack(Items.redstone),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.redstone),
				new ItemStack(Items.glowstone_dust),
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIGHTNING));
		//plant essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Blocks.leaves, 1, -1),
				new ItemStack(Blocks.waterlily),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Blocks.cactus),
				new ItemStack(Blocks.vine)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_NATURE));
		//life essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.egg),
				new ItemStack(Items.golden_apple, 1, 0),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.golden_apple, 1, 0),
				new ItemStack(Items.egg)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE));
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.golden_apple),
				new ItemStack(Items.egg, 1, 0),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.egg, 1, 0),
				new ItemStack(Items.golden_apple)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE));
		//ender essence
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.ender_pearl),
				new ItemStack(Items.ender_eye),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.ender_eye),
				new ItemStack(Items.ender_pearl)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER));
		AddRecipe(new ItemStack[] {
				new ItemStack(Items.ender_eye),
				new ItemStack(Items.ender_pearl),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(Items.ender_pearl),
				new ItemStack(Items.ender_eye)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER));

		//base essence core
		AddRecipe(new ItemStack[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_AIR),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_FIRE),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_BASE_CORE));
		//high essence core
		AddRecipe(new ItemStack[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIGHTNING),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_NATURE),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_HIGH_CORE));
		//pure essence
		AddRecipe(new ItemStack[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_HIGH_CORE),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE),
				new ItemStack(Items.diamond),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_BASE_CORE)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_PURE));

		AddRecipe(new ItemStack[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_HIGH_CORE),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
				new ItemStack(Items.diamond),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_BASE_CORE)
		},
		new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_PURE));

		//deficit crystal
		AddRecipe(new ItemStack[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
				new ItemStack(Items.magma_cream),
				new ItemStack(Items.emerald),
				new ItemStack(Blocks.obsidian),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
		}, new ItemStack(ItemsCommonProxy.deficitCrystal));

		AddRecipe(new ItemStack[]{
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
				new ItemStack(Blocks.obsidian),
				new ItemStack(Items.emerald),
				new ItemStack(Items.magma_cream),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER),
		}, new ItemStack(ItemsCommonProxy.deficitCrystal));
	}
}
