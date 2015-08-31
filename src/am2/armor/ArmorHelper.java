package am2.armor;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementTiers;
import am2.armor.infusions.ImbuementRegistry;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.utility.EntityUtilities;

public class ArmorHelper {

	private static final HashMap<String, Integer> armorRenderPrefixes = new HashMap<String, Integer>();

	private static final int IMBUE_TIER_COST = 12;

	public static int getArmorRenderIndex(String armorName){
		if (armorRenderPrefixes.containsKey(armorName)){
			return armorRenderPrefixes.get(armorName);
		}
		int newPrefix = AMCore.instance.proxy.getArmorRenderIndex(armorName);
		armorRenderPrefixes.put(armorName, newPrefix);

		return newPrefix;
	}

	public static boolean PlayerHasArmorInSlot(EntityPlayer player, int armorSlot){
		ItemStack[] armor = player.inventory.armorInventory;
		return (armor[armorSlot] != null);
	}

	//======================================================================================================
	// Infusion
	//======================================================================================================
	public static void HandleArmorInfusion(EntityPlayer player) {
		ExtendedProperties extendedProperties = ExtendedProperties.For(player);

		float infusionCost = 0.0f;
		boolean bFullSet = getFullArsMagicaArmorSet(player) != -1;
		for (int i = 0; i < 4; ++i){
			infusionCost += GetArsMagicaArmorInfusionCostFromSlot(player, i);
		}
		if (bFullSet){
			infusionCost *= 0.75;
		}
		if (infusionCost > 0 && extendedProperties.getCurrentMana() > infusionCost){
			//deduct mana
			extendedProperties.setCurrentMana(extendedProperties.getCurrentMana()
					- infusionCost);
			//bank infusion
			extendedProperties.bankedInfusionHelm += GetArsMagicaArmorRepairAmountFromSlot(player, 3);
			extendedProperties.bankedInfusionChest += GetArsMagicaArmorRepairAmountFromSlot(player, 2);
			extendedProperties.bankedInfusionLegs += GetArsMagicaArmorRepairAmountFromSlot(player, 1);
			extendedProperties.bankedInfusionBoots += GetArsMagicaArmorRepairAmountFromSlot(player, 0);


			//repair armor if infusion bank is above 1
			if (extendedProperties.bankedInfusionHelm > 1){
				int repairAmount = (int)Math.floor(extendedProperties.bankedInfusionHelm);
				RepairEquippedArsMagicaArmorItem(player, repairAmount, 3);
				extendedProperties.bankedInfusionHelm -= repairAmount;
			}

			if (extendedProperties.bankedInfusionChest > 1){
				int repairAmount = (int)Math.floor(extendedProperties.bankedInfusionChest);
				RepairEquippedArsMagicaArmorItem(player, repairAmount, 2);
				extendedProperties.bankedInfusionChest -= repairAmount;
			}

			if (extendedProperties.bankedInfusionLegs > 1){
				int repairAmount = (int)Math.floor(extendedProperties.bankedInfusionLegs);
				RepairEquippedArsMagicaArmorItem(player, repairAmount, 1);
				extendedProperties.bankedInfusionLegs -= repairAmount;
			}

			if (extendedProperties.bankedInfusionBoots > 1){
				int repairAmount = (int)Math.floor(extendedProperties.bankedInfusionBoots);
				RepairEquippedArsMagicaArmorItem(player, repairAmount, 0);
				extendedProperties.bankedInfusionBoots -= repairAmount;
			}
		}
	}

	private static void RepairEquippedArsMagicaArmorItem(EntityPlayer player, int repairAmount, int slot) {
		if (!PlayerHasArmorInSlot(player, slot)){
			return;
		}
		ItemStack armor = player.inventory.armorInventory[slot];
		if (armor.isItemDamaged()){
			armor.damageItem(-repairAmount, player); //negative damage actually acts as a repair
		}
	}

	private static float GetArsMagicaArmorRepairAmountFromSlot(EntityPlayer player, int armorSlot) {
		if (!PlayerHasArsInfusableInSlot(player, armorSlot)){
			return 0;
		}
		if (player.inventory.armorInventory[armorSlot].isItemDamaged()){
			AMArmor armor = (AMArmor)player.inventory.armorInventory[armorSlot].getItem();
			return armor.GetInfusionRepair();
		}
		return 0;
	}

	private static boolean PlayerHasArsInfusableInSlot(EntityPlayer player, int armorSlot) {
		ItemStack[] armor = player.inventory.armorInventory;
		return (armor[armorSlot] != null && (armor[armorSlot].getItem() instanceof AMArmor));
	}

	private static float GetArsMagicaArmorInfusionCostFromSlot(EntityPlayer player, int armorSlot) {
		if (!PlayerHasArsInfusableInSlot(player, armorSlot)){
			return 0;
		}
		if (player.inventory.armorInventory[armorSlot].isItemDamaged()){
			AMArmor armor = (AMArmor)player.inventory.armorInventory[armorSlot].getItem();
			return armor.GetInfusionCost();
		}
		return 0;
	}

	public static int getFullArsMagicaArmorSet(EntityPlayer player) {
		int matlID = -1;
		for (int i = 0; i < 4; ++i){
			if (!PlayerHasArsInfusableInSlot(player, i)){
				return -1;
			}
			AMArmor armor = (AMArmor)player.inventory.armorInventory[i].getItem();
			if (matlID == -1){
				matlID = armor.getMaterialID();
			}else{
				if (matlID != armor.getMaterialID()){
					return -1;
				}
			}
		}
		return matlID;
	}


	//======================================================================================================
	// Armor Effects
	//======================================================================================================
	public static void HandleArmorEffects(EntityPlayer player, World world) {
		if (world.isRemote){
			return;
		}
		ExtendedProperties extendedProperties = ExtendedProperties.For(player);

		//decrement counters
		for (int i = 0; i < extendedProperties.armorProcCooldowns.length; ++i){
			if (extendedProperties.armorProcCooldowns[i] > 0)
				extendedProperties.armorProcCooldowns[i]--;
		}
	}

	private static ItemStack GetAMProtectiveArmorInSlot(EntityPlayer player, int armorSlot) {
		ItemStack[] armor = player.inventory.armorInventory;
		if (armor[armorSlot] != null){
			for (Item i : ItemsCommonProxy.protectiveArmors){
				if (armor[armorSlot].getItem() == i){
					return armor[armorSlot];
				}
			}
		}
		return null;
	}

	public static IArmorImbuement[] getInfusionsOnArmor(EntityPlayer player, int armorSlot){
		ItemStack stack = player.getCurrentArmor(armorSlot);
		return getInfusionsOnArmor(stack);
	}

	public static IArmorImbuement[] getInfusionsOnArmor(ItemStack stack){
		if (stack == null || !stack.hasTagCompound() || !(stack.getItem() instanceof ItemArmor))
			return new IArmorImbuement[0];
		NBTTagCompound armorProps = (NBTTagCompound) stack.stackTagCompound.getTag(AMArmor.NBT_KEY_AMPROPS);
		if (armorProps != null){
			String infusionList = armorProps.getString(AMArmor.NBT_KEY_EFFECTS);
			if (infusionList != null && infusionList != ""){
				String[] ids = infusionList.split(AMArmor.INFUSION_DELIMITER);
				IArmorImbuement[] infusions = new IArmorImbuement[ids.length];
				for (int i = 0; i < ids.length; ++i){
					infusions[i] = ImbuementRegistry.instance.getImbuementByID(ids[i]);
				}
				return infusions;
			}
		}
		return new IArmorImbuement[0];
	}

	public static boolean isInfusionPreset(ItemStack stack, String id){
		if (stack == null || !stack.hasTagCompound())
			return false;
		NBTTagCompound armorProps = (NBTTagCompound) stack.stackTagCompound.getTag(AMArmor.NBT_KEY_AMPROPS);
		if (armorProps != null){
			String infusionList = armorProps.getString(AMArmor.NBT_KEY_EFFECTS);
			if (infusionList != null){
				return infusionList.contains(id);
			}
		}
		return false;
	}

	public static void imbueArmor(ItemStack armorStack, String id, boolean ignoreLevelRequirement) {
		IArmorImbuement imbuement = ImbuementRegistry.instance.getImbuementByID(id);
		if (armorStack != null && imbuement != null && armorStack.getItem() instanceof ItemArmor){

			if (!ignoreLevelRequirement && getArmorLevel(armorStack) < getImbueCost(imbuement.getTier()))
				return;

			for (int i : imbuement.getValidSlots()){
				if (i == ((ItemArmor)armorStack.getItem()).armorType){
					if (!armorStack.hasTagCompound())
						armorStack.setTagCompound(new NBTTagCompound());
					NBTTagCompound armorProps = (NBTTagCompound) armorStack.stackTagCompound.getTag(AMArmor.NBT_KEY_AMPROPS);
					if (armorProps == null)
						armorProps = new NBTTagCompound();
					String infusionList = armorProps.getString(AMArmor.NBT_KEY_EFFECTS);
					if (infusionList == null || infusionList == "")
						infusionList = id;
					else
						infusionList += "|" + id;
					armorProps.setString(AMArmor.NBT_KEY_EFFECTS, infusionList);
					armorStack.stackTagCompound.setTag(AMArmor.NBT_KEY_AMPROPS, armorProps);

					deductXPFromArmor(EntityUtilities.getXPFromLevel(getImbueCost(imbuement.getTier())), armorStack);
					break;
				}
			}
		}
	}

	public static int getArmorLevel(ItemStack stack){
		if (stack == null || !stack.hasTagCompound())
			return 0;
		NBTTagCompound armorProps = (NBTTagCompound) stack.stackTagCompound.getTag(AMArmor.NBT_KEY_AMPROPS);
		if (armorProps != null){
			return armorProps.getInteger(AMArmor.NBT_KEY_ARMORLEVEL);
		}
		return 0;
	}

	public static int getImbueCost(ImbuementTiers tier){
		return 30 + tier.ordinal() * IMBUE_TIER_COST;
	}

	public static void addXPToArmor(float totalAmt, EntityPlayer player){
		int numPieces = 0;
		for (int i = 0; i < 4; ++i)
			if (player.getCurrentArmor(i) != null)
				numPieces++;
		float xpPerPiece = totalAmt / numPieces;
		for (int i = 0; i < 4; ++i){
			addXPToArmor(xpPerPiece, player.getCurrentArmor(i));
		}
	}

	public static void deductXPFromArmor(float amt, ItemStack armor){
		if (armor != null && armor.getItem() instanceof ItemArmor){
			if (!armor.hasTagCompound())
				armor.setTagCompound(new NBTTagCompound());
			NBTTagCompound armorProps = (NBTTagCompound) armor.stackTagCompound.getTag(AMArmor.NBT_KEY_AMPROPS);
			if (armorProps == null)
				armorProps = new NBTTagCompound();
			armorProps.setDouble(AMArmor.NBT_KEY_TOTALXP, Math.max(armorProps.getDouble(AMArmor.NBT_KEY_TOTALXP) - amt, 0));
			armorProps.setInteger(AMArmor.NBT_KEY_ARMORLEVEL, EntityUtilities.getLevelFromXP((float) armorProps.getDouble(AMArmor.NBT_KEY_TOTALXP)));
			armor.stackTagCompound.setTag(AMArmor.NBT_KEY_AMPROPS, armorProps);
		}
	}

	public static void addXPToArmor(float amt, ItemStack armor){
		if (armor != null && armor.getItem() instanceof ItemArmor){
			if (!armor.hasTagCompound())
				armor.setTagCompound(new NBTTagCompound());
			NBTTagCompound armorProps = (NBTTagCompound) armor.stackTagCompound.getTag(AMArmor.NBT_KEY_AMPROPS);
			if (armorProps == null)
				armorProps = new NBTTagCompound();
			armorProps.setDouble(AMArmor.NBT_KEY_TOTALXP, armorProps.getDouble(AMArmor.NBT_KEY_TOTALXP) + amt);
			armorProps.setInteger(AMArmor.NBT_KEY_ARMORLEVEL, EntityUtilities.getLevelFromXP((float) armorProps.getDouble(AMArmor.NBT_KEY_TOTALXP)));
			armor.stackTagCompound.setTag(AMArmor.NBT_KEY_AMPROPS, armorProps);
		}
	}
}
