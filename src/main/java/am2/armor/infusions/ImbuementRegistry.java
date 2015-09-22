package am2.armor.infusions;

import am2.LogHelper;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.IImbuementRegistry;
import am2.api.items.armor.ImbuementTiers;
import am2.armor.ArmorHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.TreeMap;

public class ImbuementRegistry implements IImbuementRegistry{
	private TreeMap<String, IArmorImbuement> registeredImbuements;

	public static final ImbuementRegistry instance = new ImbuementRegistry();

	public static final int SLOT_BOOTS = 3;
	public static final int SLOT_LEGS = 2;
	public static final int SLOT_CHEST = 1;
	public static final int SLOT_HELM = 0;

	private ImbuementRegistry(){
		registeredImbuements = new TreeMap<String, IArmorImbuement>();
	}

	@Override
	public void registerImbuement(IArmorImbuement imbuementInstance){
		registeredImbuements.put(imbuementInstance.getID(), imbuementInstance);
		LogHelper.info("Registered imbuement: %s", imbuementInstance.getID());
	}

	@Override
	public IArmorImbuement getImbuementByID(String ID){
		return registeredImbuements.get(ID);
	}

	@Override
	public IArmorImbuement[] getImbuementsForTier(ImbuementTiers tier, int armorType){
		ArrayList<IArmorImbuement> list = new ArrayList<IArmorImbuement>();

		for (IArmorImbuement imbuement : registeredImbuements.values()){
			if (imbuement.getTier() == tier){
				for (int i : imbuement.getValidSlots()){
					if (i == armorType){
						list.add(imbuement);
						break;
					}
				}
			}
		}

		return list.toArray(new IArmorImbuement[list.size()]);
	}

	@Override
	public boolean isImbuementPresent(ItemStack stack, IArmorImbuement imbuement){
		return isImbuementPresent(stack, imbuement.getID());
	}

	@Override
	public boolean isImbuementPresent(ItemStack stack, String id){
		return ArmorHelper.isInfusionPreset(stack, id);
	}
}
