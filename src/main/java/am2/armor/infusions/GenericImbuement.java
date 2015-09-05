package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.UUID;

public class GenericImbuement implements IArmorImbuement{

	private String id = "";
	private int iconIndex = 0;
	private ImbuementTiers tier;
	private int[] validSlots;

	public static final UUID imbuedHasteID = UUID.fromString("3b51a94c-8866-470b-8b69-e1d5cb50a72f");
	public static final AttributeModifier imbuedHaste = new AttributeModifier(imbuedHasteID, "Imbued Haste", 0.2, 2);

	public static final String manaRegen = "mn_reg";
	public static final String burnoutReduction = "bn_red";
	public static final String flickerLure = "fl_lure";
	public static final String magicXP = "mg_xp";
	public static final String pinpointOres = "pp_ore";
	public static final String magitechGoggleIntegration = "mg_gog";
	public static final String thaumcraftNodeReveal = "tc_nrv";
	public static final String stepAssist = "step_up";
	public static final String runSpeed = "run_spd";
	public static final String soulbound = "soulbnd";

	public GenericImbuement(String id, int IIconIndex, ImbuementTiers tier, int[] validSlots){
		this.id = id;
		this.iconIndex = IIconIndex;
		this.tier = tier;
		this.validSlots = validSlots;
	}

	public static void registerAll(){
		//all armors
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(manaRegen, 0, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_BOOTS, ImbuementRegistry.SLOT_LEGS, ImbuementRegistry.SLOT_CHEST, ImbuementRegistry.SLOT_HELM}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(burnoutReduction, 1, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_BOOTS, ImbuementRegistry.SLOT_LEGS, ImbuementRegistry.SLOT_CHEST, ImbuementRegistry.SLOT_HELM}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(soulbound, 31, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_BOOTS, ImbuementRegistry.SLOT_LEGS, ImbuementRegistry.SLOT_CHEST, ImbuementRegistry.SLOT_HELM}));

		//chest
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(flickerLure, 3, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_CHEST}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(magicXP, 15, ImbuementTiers.FOURTH, new int[]{ImbuementRegistry.SLOT_CHEST}));

		//helmet
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(pinpointOres, 16, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_HELM}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(magitechGoggleIntegration, 17, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_HELM}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(thaumcraftNodeReveal, 2, ImbuementTiers.FOURTH, new int[]{ImbuementRegistry.SLOT_HELM}));

		//legs
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(stepAssist, 21, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_LEGS}));

		//boots
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(runSpeed, 26, ImbuementTiers.FIRST, new int[]{ImbuementRegistry.SLOT_BOOTS}));
	}

	@Override
	public String getID(){
		return id;
	}

	@Override
	public int getIconIndex(){
		return iconIndex;
	}

	@Override
	public ImbuementTiers getTier(){
		return tier;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.NONE);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){
		return false;
	}

	@Override
	public int[] getValidSlots(){
		return validSlots;
	}

	@Override
	public boolean canApplyOnCooldown(){
		return true;
	}

	@Override
	public int getCooldown(){
		return 0;
	}

	@Override
	public int getArmorDamage(){
		return 0;
	}
}
