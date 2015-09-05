package am2.armor;


public enum ArsMagicaArmorMaterial{
	//{900, 1700, 6400, 4000}
	MAGE(
			"MAGE",
			33,
			new int[]{2, 6, 4, 2},
			15,
			0.055588625f, //infusion cost
			0.06670635f, //infusion repair
			//the following arrays match up to:
			//[boots, leggings, chest, helmet]
			new float[]{0.10f, 0.18f, 0.14f, 0.10f},
			new float[]{1, 1, 1, 1}
	), //stable at L8, 52% DR w/ full set

	BATTLEMAGE(
			"BATTLEMAGE",
			48,
			new int[]{3, 8, 6, 3},
			10,
			0.31034125f, //infusion cost
			0.2464095f, //infusion repair
			//the following arrays match up to:
			//[boots, leggings, chest, helmet]
			new float[]{0.1325f, 0.2125f, 0.1725f, 0.1325f},
			new float[]{0.5f, 0.5f, 0.5f, 0.5f} //cooldown ticks of protection ( n / 20 -> time in seconds)
	), //stable at L12, ~65% DR w/ full set

	UNIQUE(
			"UNIQUE",
			200,
			new int[]{3, 8, 6, 3},
			0,
			0.31034125f, //infusion cost
			0.2464095f, //infusion repair
			//the following arrays match up to:
			//[boots, leggings, chest, helmet]
			new float[]{0, 0, 0.7f, 0},
			new float[]{1, 1, 1, 1} //cooldown ticks of protection ( n / 20 -> time in seconds)
	), //stable at L12, ~65% DR w/ full set

	ARCHMAGE(
			"ARCHMAGE",
			78,
			new int[]{3, 8, 6, 3},
			0,
			0.44065625f, //infusion cost
			0.3751875f, //infusion repair
			//the following arrays match up to:
			//[boots, leggings, chest, helmet]
			new float[]{0.18f, 0.22f, 0.2f, 0.18f},
			new float[]{0.2f, 0.2f, 0.2f, 0.2f} //cooldown ticks of protection ( n / 20 -> time in seconds)
	); //stable at L17, 78% DR w/ full set

	private int maxDamageFactor;
	private int damageReductionAmountArray[];
	private int enchantability;
	private float infusionCost;
	private float infusionRepair;
	private float[] damageReductionRatios;
	private float[] protectionCooldownModifiers;

	private ArsMagicaArmorMaterial(String identifier, int durability, int armorValueArray[], int enchantability, float infusionCost, float infusionRepair, float[] damageReductionRatios, float[] protectionCooldown){
		this.maxDamageFactor = durability;
		this.damageReductionAmountArray = armorValueArray;
		this.enchantability = enchantability;
		this.infusionCost = infusionCost;
		this.infusionRepair = infusionRepair;
		this.damageReductionRatios = damageReductionRatios;
		this.protectionCooldownModifiers = protectionCooldown;
	}

	public int func_40576_a(int i){
		return AMArmor.getMaxDamageArray()[i] * maxDamageFactor;
	}

	public int getDamageReductionAmount(int i){
		return damageReductionAmountArray[i];
	}

	public int getEnchantability(){
		return enchantability;
	}

	public float getInfusionCost(){
		return infusionCost;
	}

	public float getInfusionRepair(){
		return this.infusionRepair;
	}

	public int getMaterialID(){
		return maxDamageFactor;
	}

	public float getDamageReduceRatio(int slot){
		return damageReductionRatios[slot];
	}

	public float getProtectionCooldownModifier(int slot){
		return this.protectionCooldownModifiers[slot];
	}
}
