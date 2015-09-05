package am2.items;

import am2.AMCore;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemKeystone extends ArsMagicaItem{

	public static final int KEYSTONE_INVENTORY_SIZE = 3;

	public ItemKeystone(){
		super();
		setMaxStackSize(1);
	}

	public void addCombination(ItemStack stack, String name, int[] metas){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		int comboID = numCombinations(stack);
		boolean isNew = true;

		for (int i = 0; i < comboID; ++i){
			if (name.equals(stack.stackTagCompound.getString("Combination_" + i + "_name"))){
				comboID = i;
				isNew = false;
				break;
			}
		}

		stack.stackTagCompound.setString("Combination_" + comboID + "_name", name);
		stack.stackTagCompound.setIntArray("Combination_" + comboID + "_metas", metas);

		if (isNew)
			stack.stackTagCompound.setInteger("numKeystoneCombinations", comboID + 1);
	}

	public void removeCombination(ItemStack stack, String name){
		int c = numCombinations(stack);
		int removedIndex = -1;
		for (int i = 0; i < c; ++i){
			KeystoneCombination combo = getCombinationAt(stack, i);
			if (combo.name.equals(name)){
				removedIndex = i;
				break;
			}
		}

		if (removedIndex == -1)
			return;

		for (int i = removedIndex + 1; i < c; ++i){
			String tName = stack.stackTagCompound.getString("Combination_" + i + "_name");
			int[] tMetas = stack.stackTagCompound.getIntArray("Combination_" + i + "_metas");

			stack.stackTagCompound.setString("Combination_" + (i - 1) + "_name", tName);
			stack.stackTagCompound.setIntArray("Combination_" + (i - 1) + "_metas", tMetas);
		}

		stack.stackTagCompound.removeTag("Combination_" + c + "_name");
		stack.stackTagCompound.removeTag("Combination_" + c + "_metas");
		stack.stackTagCompound.setInteger("numKeystoneCombinations", c - 1);
	}

	public int numCombinations(ItemStack stack){
		if (!stack.hasTagCompound()) return 0;
		return stack.stackTagCompound.getInteger("numKeystoneCombinations");
	}

	public KeystoneCombination getCombinationAt(ItemStack stack, int index){
		if (!stack.hasTagCompound()) return null;

		if (numCombinations(stack) <= index) return null;

		String name = stack.stackTagCompound.getString("Combination_" + index + "_name");
		int[] metas = stack.stackTagCompound.getIntArray("Combination_" + index + "_metas");

		return new KeystoneCombination(name, metas);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if (player.isSneaking()){
			FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}

		return stack;
	}

	private ItemStack[] getMyInventory(ItemStack itemStack){
		return ReadFromStackTagCompound(itemStack);
	}

	public String getRecipeAsString(ItemStack keystoneStack){
		String s = "Recipe: ";
		for (ItemStack stack : getMyInventory(keystoneStack)){
			s += stack.getDisplayName().replace("Rune ", "") + " ";
		}
		return s;
	}

	public void UpdateStackTagCompound(ItemStack itemStack, ItemStack[] values){
		if (itemStack.stackTagCompound == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}
		for (int i = 0; i < values.length; ++i){
			ItemStack stack = values[i];
			if (stack == null){
				itemStack.stackTagCompound.setInteger("keystonemeta" + i, -1);
			}else{
				itemStack.stackTagCompound.setInteger("keystonemeta" + i, stack.getItemDamage());
			}
		}
	}

	public ItemStack[] ReadFromStackTagCompound(ItemStack itemStack){
		if (itemStack.stackTagCompound == null){
			return new ItemStack[InventoryKeyStone.inventorySize];
		}
		ItemStack[] items = new ItemStack[InventoryKeyStone.inventorySize];
		for (int i = 0; i < items.length; ++i){
			int meta = 0;
			if (!itemStack.stackTagCompound.hasKey("keystonemeta" + i)){
				items[i] = null;
				continue;
			}else if (itemStack.stackTagCompound.getInteger("keystonemeta" + i) == -1){
				items[i] = null;
				continue;
			}else{
				meta = itemStack.stackTagCompound.getInteger("keystonemeta" + i);
			}

			items[i] = new ItemStack(ItemsCommonProxy.rune, 1, meta);
		}
		return items;
	}

	public InventoryKeyStone ConvertToInventory(ItemStack keyStoneStack){
		InventoryKeyStone iks = new InventoryKeyStone();
		iks.SetInventoryContents(getMyInventory(keyStoneStack));
		return iks;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		ItemStack[] items = getMyInventory(par1ItemStack);

		String s = StatCollector.translateToLocal("am2.tooltip.open");
		par3List.add((new StringBuilder()).append("\2477").append(s).toString());

		if (items.length > 0){
			s = StatCollector.translateToLocal("am2.tooltip.runes") + ": ";
			par3List.add((new StringBuilder()).append("\2477").append(s).toString());
			s = "";
			for (int i = 0; i < KEYSTONE_INVENTORY_SIZE; ++i){
				if (items[i] == null) continue;
				s += items[i].getDisplayName().replace("Rune", "").trim() + " ";
			}
			if (s == "") s = StatCollector.translateToLocal("am2.tooltip.none");
			par3List.add((new StringBuilder()).append("\2477").append(s).toString());
		}
	}

	public long getKey(ItemStack keystoneStack){
		ItemStack[] inventory = getMyInventory(keystoneStack);
		if (inventory == null) return 0;
		return KeystoneUtilities.instance.getKeyFromRunes(inventory);
	}

	public class KeystoneCombination{
		public int[] metas;
		public String name;

		public KeystoneCombination(String name, int[] metas){
			this.metas = metas;
			this.name = name;
		}

		@Override
		public boolean equals(Object obj){
			if (obj instanceof KeystoneCombination){
				boolean match = ((KeystoneCombination)obj).metas.length == metas.length;
				if (!match) return false;

				for (int i = 0; i < this.metas.length; ++i){
					match &= (this.metas[i] == ((KeystoneCombination)obj).metas[i]);
				}

				return match;
			}
			return false;
		}

		@Override
		public int hashCode(){
			int sum = 0;
			for (int i : metas)
				sum += i;
			return sum;
		}
	}
}
