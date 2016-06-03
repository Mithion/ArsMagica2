package am2.armor;

import am2.api.items.armor.ArmorTextureEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;

public class AMArmor extends ItemArmor implements ISpecialArmor{

	private static final int maxDamageArray[] = {
			11, 16, 15, 13
	};
	public final int armorType;
	public final int damageReduceAmount;
	private final ArsMagicaArmorMaterial material;
	private final int damageReduction;
	private final float infusionCost;
	private final float infusionRepair;

	public static final String NBT_KEY_AMPROPS = "AMArmorProperties";
	public static final String NBT_KEY_EFFECTS = "armorEffects";
	public static final String NBT_KEY_TOTALXP = "infusedXP";
	public static final String NBT_KEY_ARMORLEVEL = "XPLevel";
	public static final String INFUSION_DELIMITER = "\\|";

	public AMArmor(ArmorMaterial inheritFrom, ArsMagicaArmorMaterial enumarmormaterial, int par3, int par4){
		super(inheritFrom, par3, par4);
		material = enumarmormaterial;
		armorType = par4;
		damageReduceAmount = 0;
		setMaxDamage(enumarmormaterial.func_40576_a(par4));
		maxStackSize = 1;
		damageReduction = enumarmormaterial.getDamageReductionAmount(par4);
		infusionCost = enumarmormaterial.getInfusionCost();
		infusionRepair = enumarmormaterial.getInfusionRepair();
	}

	public AMArmor setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	@Override
	public int getItemEnchantability(){
		return material.getEnchantability();
	}

	static int[] getMaxDamageArray(){
		return maxDamageArray;
	}

	public int GetDamageReduction(){
		return damageReduction;
	}

	public float GetInfusionCost(){
		return infusionCost;
	}

	public float GetInfusionRepair(){
		return infusionRepair;
	}

	public int getMaterialID(){
		return material.getMaterialID();
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return GetDamageReduction();
	}

	private int GetArsMagicatArmorReductionFromSlot(EntityPlayer player, int armorSlot){
		AMArmor armor = (AMArmor)player.inventory.armorInventory[armorSlot].getItem();
		return armor.GetDamageReduction();
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot){
		AMArmor armorItem = (AMArmor)armor.getItem();
		ArmorProperties ap = new ArmorProperties(1, material.getDamageReduceRatio(slot), 1000);
		return ap;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot){
		if (source == DamageSource.onFire){
			stack.damageItem(damage * 7, entity);
		}else if (source == DamageSource.fall || source == DamageSource.inWall || source == DamageSource.drown || source == DamageSource.starve || source == DamageSource.outOfWorld){
			return;
		}else if (source == DamageSource.magic){
			stack.damageItem(damage * 7, entity);
		}else{
			stack.damageItem(damage * 10, entity);
		}
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type){
		int armorType = -1;
		if (stack.getItem() instanceof AMArmor){
			armorType = ((AMArmor)stack.getItem()).renderIndex;
		}

		ArmorTextureEvent event = new ArmorTextureEvent(slot, armorType);
		MinecraftForge.EVENT_BUS.post(event);

		return event.texture;
	}

	@Override
	public boolean hasEffect(ItemStack stack, int pass){
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(NBT_KEY_AMPROPS)){
			String s = ((NBTTagCompound)stack.stackTagCompound.getTag(NBT_KEY_AMPROPS)).getString(NBT_KEY_EFFECTS);
			return s != null && s.length() > 0;
		}
		return false;

	}
}
