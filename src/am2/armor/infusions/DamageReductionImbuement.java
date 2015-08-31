package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;

public class DamageReductionImbuement implements IArmorImbuement{

	private String id = "";
	private String dmgType = "";
	private int iconIndex = 0;
	private ImbuementTiers tier;
	int[] allArmor = new int[] { ImbuementRegistry.SLOT_BOOTS, ImbuementRegistry.SLOT_LEGS, ImbuementRegistry.SLOT_CHEST, ImbuementRegistry.SLOT_HELM };

	private DamageReductionImbuement(String id, String dmgType, int IIconIndex, ImbuementTiers tier){
		this.id = id;
		this.dmgType = dmgType;
		this.iconIndex = IIconIndex;
		this.tier = tier;
	}

	public static void registerAll(){

		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_phy", "generic", 4, ImbuementTiers.SECOND));
		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_drn", "drown", 5, ImbuementTiers.SECOND));
		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_fall", "fall", 6, ImbuementTiers.SECOND));
		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_exp", "explosion", 7, ImbuementTiers.SECOND));

		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_fire", "fire", 8, ImbuementTiers.THIRD));
		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_frst", "frost", 9, ImbuementTiers.THIRD));
		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_mage", "magic", 10, ImbuementTiers.THIRD));
		ImbuementRegistry.instance.registerImbuement(new DamageReductionImbuement("dr_litn", "lightning", 11, ImbuementTiers.THIRD));
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public int getIconIndex() {
		return iconIndex;
	}

	@Override
	public ImbuementTiers getTier() {
		return tier;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes() {
		return EnumSet.of(ImbuementApplicationTypes.ON_HIT);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params) {
		LivingHurtEvent event = (LivingHurtEvent) params[0];
		if (event.source.damageType.equals(dmgType) ||
				(dmgType.equals("fire") && event.source.isFireDamage()) ||
				(dmgType.equals("magic") && event.source.isMagicDamage()) ||
				(dmgType.equals("explosion") && event.source.isExplosion())
			){
			event.ammount *= 0.85f;
			return true;
		}
		return false;
	}

	@Override
	public int[] getValidSlots() {
		return allArmor;
	}

	@Override
	public boolean canApplyOnCooldown() {
		return true;
	}

	@Override
	public int getCooldown() {
		return 0;
	}


	@Override
	public int getArmorDamage() {
		return 0;
	}
}
