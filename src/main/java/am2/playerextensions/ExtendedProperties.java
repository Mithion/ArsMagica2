package am2.playerextensions;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.IExtendedProperties;
import am2.api.events.PlayerMagicLevelChangeEvent;
import am2.api.math.AMVector2;
import am2.api.math.AMVector3;
import am2.api.spell.enums.ContingencyTypes;
import am2.api.spell.enums.SkillPointTypes;
import am2.armor.ArmorHelper;
import am2.armor.ArsMagicaArmorMaterial;
import am2.armor.infusions.GenericImbuement;
import am2.armor.infusions.ImbuementRegistry;
import am2.bosses.EntityLifeGuardian;
import am2.buffs.BuffList;
import am2.guis.AMGuiHelper;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.particles.AMLineArc;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.spell.SpellHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ExtendedProperties implements IExtendedProperties, IExtendedEntityProperties{
	private EntityLivingBase entity;

	public static final String identifier = "ArsMagicaExProps";
	public static final int maxMagicLevel = 99;

	private static float magicRegenPerLevelPerTick = 0.15f;
	private static float entityMagicPerLevelBase = 0.20f;
	private static int baseTicksForFullRegen = 2400;
	private int ticksForFullRegen = baseTicksForFullRegen;

	private ArrayList<Integer> summon_ent_ids = new ArrayList<Integer>();

	private double markX;
	private double markY;
	private double markZ;
	private int markDimension;

	private int healCooldown;

	private float flipRotation;
	private float prevFlipRotation;

	private float shrinkPct;
	private float prevShrinkPct;
	public float shrinkAmount = 0;
	public AMVector2 originalSize;
	public float yOffsetOrig = 0.75f;

	private float currentMana;
	private float maxMana;
	private float currentFatigue;
	private float maxFatigue;

	private int magicLevel;
	private float magicXP;

	private int numSummons;
	private ArrayList<ManaLinkEntry> manaLinks;

	private byte bitFlag;
	private boolean hasDoneFullSync;

	public int sprintTimer = 0;
	public int sneakTimer = 0;
	public int itemUseTimer = 0;

	private int fallProtection = 0;
	private int previousBreath = 300;

	private ContingencyTypes contingencyType = ContingencyTypes.NONE;
	private ItemStack contingencyStack = null;

	public float TK_Distance = 8;

	public float bankedInfusionHelm = 0.0f;
	public float bankedInfusionChest = 0.0f;
	public float bankedInfusionLegs = 0.0f;
	public float bankedInfusionBoots = 0.0f;

	private Entity inanimateTarget;

	public int[] armorProcCooldowns = new int[4];

	private int ticksSinceLastRegen = 0;
	private int ticksToRegen = 0;

	private boolean needsArmorTickCounterSync = false;

	private int AuraIndex = 15;
	private int AuraBehaviour = 0;
	private float AuraScale = 0.5f;
	private int AuraColor = 0xFFFFFF;
	private float AuraAlpha = 1f;
	private boolean AuraColorRandomize = true;
	private boolean AuraColorDefault = true;
	private int AuraDelay = 1;
	private int AuraQuantity = 2;
	private float AuraSpeed = 0.5f;

	private boolean hasInitialized = false;
	public boolean astralBarrierBlocked = false;
	private boolean forcingSync = false;
	private boolean isCritical;
	public boolean isRecoveringKeystone = false;
	public boolean hadFlight = false;
	private boolean disableGravity = false;

	private static final int UPD_CURRENT_MANA_FATIGUE = 0x1;
	private static final int UPD_MAX_MANA_FATIGUE = 0x2;
	private static final int UPD_MAGIC_LEVEL = 0x4;
	private static final int UPD_DISABLE_GRAVITY = 0x8;
	private static final int UPD_NUM_SUMMONS = 0x10;
	private static final int UPD_MARK = 0x20;
	private static final int UPD_CONTINGENCY = 0x40;
	private static final int UPD_BITFLAG = 0x80;
	private static final int UPD_BETA_PARTICLES = 0x100;
	private static final int UPD_TK_DISTANCE = 0x200;
	private static final int UPD_MANALINK = 0x400;
	private static final int BIT_MARK_SET = 0x2;
	private static final int BIT_FLIPPED = 0x4;
	private static final int BIT_SHRUNK = 0x8;

	private int updateFlags;
	private static final int syncTickDelay = 200; //10 seconds
	private int ticksToSync;

	public ExtendedProperties(){
		hasInitialized = false;
		hasDoneFullSync = false;
		manaLinks = new ArrayList<ManaLinkEntry>();
	}

	public static ExtendedProperties For(EntityLivingBase living){
		return (ExtendedProperties)living.getExtendedProperties(identifier);
	}

	//=======================================================================================
	// Getters
	//=======================================================================================
	public boolean getHasDoneFullSync(){
		return this.hasDoneFullSync;
	}

	@Override
	public boolean getHasUnlockedAugmented(){
		if (entity instanceof EntityPlayer){
			return SkillData.For((EntityPlayer)entity).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")));
		}
		return true;
	}

	public int getBreathAmount(){
		return this.previousBreath;
	}

	@Override
	public int getNumSummons(){
		return this.numSummons;
	}

	@Override
	public AMVector3 getMarkLocation(){
		return new AMVector3(markX, markY, markZ);
	}

	@Override
	public boolean getMarkSet(){
		return (this.bitFlag & BIT_MARK_SET) == BIT_MARK_SET;
	}

	public int getAuraBehaviour(){
		return AuraBehaviour;
	}

	public int getAuraIndex(){
		return AuraIndex;
	}

	public float getAuraScale(){
		return AuraScale;
	}

	public float getAuraAlpha(){
		return AuraAlpha;
	}

	public boolean getAuraColorDefault(){
		return AuraColorDefault;
	}

	public boolean getAuraColorRandomize(){
		return AuraColorRandomize;
	}

	public int getAuraColor(){
		return AuraColor;
	}

	public int getAuraDelay(){
		return AuraDelay;
	}

	public int getAuraQuantity(){
		return AuraQuantity;
	}

	public float getAuraSpeed(){
		return AuraSpeed;
	}

	public ContingencyTypes getContingencyType(){
		return this.contingencyType;
	}

	public ItemStack getContingencyEffect(){
		return this.contingencyStack;
	}

	public int getFallProtection(){
		return fallProtection;
	}

	@Override
	public int getMarkDimension(){
		return this.markDimension;
	}

	public double getMarkX(){
		return this.markX;
	}

	public double getMarkY(){
		return this.markY;
	}

	public double getMarkZ(){
		return this.markZ;
	}

	public float getCurrentFatigue(){
		return this.currentFatigue;
	}

	public float getMaxFatigue(){
		return (float)(this.maxFatigue + this.entity.getAttributeMap().getAttributeInstance(ArsMagicaApi.maxBurnoutBonus).getAttributeValue());
	}

	@Override
	public float getCurrentMana(){
		return this.currentMana;
	}

	@Override
	public float getMaxMana(){
		float max = this.maxMana;
		if (this.entity.isPotionActive(BuffList.manaBoost))
			max *= 1 + (0.25 * (this.entity.getActivePotionEffect(BuffList.manaBoost).getAmplifier() + 1));
		return (float)(max + this.entity.getAttributeMap().getAttributeInstance(ArsMagicaApi.maxManaBonus).getAttributeValue());
	}

	public void setMaxMana(float maxMana){
		if (AMCore.config.getManaCap() > 0){
			this.maxMana = (float)Math.min(maxMana, AMCore.config.getManaCap());
		}else{
			this.maxMana = maxMana;
		}
		this.setUpdateFlag(UPD_MAX_MANA_FATIGUE);
	}

	public byte[] getUpdateData(){
		AMDataWriter writer = new AMDataWriter();
		writer.add(this.entity.getEntityId());
		writer.add(this.updateFlags);

		if ((this.updateFlags & UPD_BITFLAG) == UPD_BITFLAG){
			writer.add(this.bitFlag);
		}
		if ((this.updateFlags & UPD_CURRENT_MANA_FATIGUE) == UPD_CURRENT_MANA_FATIGUE){
			writer.add(this.currentMana);
			writer.add(this.currentFatigue);
		}
		if ((this.updateFlags & UPD_MAGIC_LEVEL) == UPD_MAGIC_LEVEL){
			writer.add(this.magicLevel);
			writer.add(this.magicXP);
		}
		if ((this.updateFlags & UPD_MARK) == UPD_MARK){
			writer.add(this.markX);
			writer.add(this.markY);
			writer.add(this.markZ);
			writer.add(this.markDimension);
			writer.add(this.getMarkSet());
		}
		if ((this.updateFlags & UPD_MAX_MANA_FATIGUE) == UPD_MAX_MANA_FATIGUE){
			writer.add(this.maxMana);
			writer.add(this.maxFatigue);
		}
		if ((this.updateFlags & UPD_NUM_SUMMONS) == UPD_NUM_SUMMONS){
			writer.add(this.numSummons);
		}
		if ((this.updateFlags & UPD_BETA_PARTICLES) == UPD_BETA_PARTICLES && entity instanceof EntityPlayer && AMCore.proxy.playerTracker.hasAA((EntityPlayer)entity)){
			writer.add(this.getAuraIndex());
			writer.add(this.getAuraBehaviour());
			writer.add(this.getAuraScale());
			writer.add(this.getAuraAlpha());
			writer.add(this.getAuraColorRandomize());
			writer.add(this.getAuraColorDefault());
			writer.add(this.getAuraColor());
			writer.add(this.getAuraDelay());
			writer.add(this.getAuraQuantity());
			writer.add(this.getAuraSpeed());
		}
		if ((this.updateFlags & UPD_CONTINGENCY) == UPD_CONTINGENCY){
			writer.add(this.contingencyType.ordinal());
			if (this.contingencyType != ContingencyTypes.NONE){
				writer.add(this.contingencyStack);
			}
		}
		if ((this.updateFlags & UPD_MANALINK) == UPD_MANALINK){
			writer.add(this.manaLinks.size());
			for (ManaLinkEntry entry : this.manaLinks)
				writer.add(entry.entityID);
		}
		if ((this.updateFlags & UPD_DISABLE_GRAVITY) == UPD_DISABLE_GRAVITY){
			writer.add(this.disableGravity);
		}

		this.updateFlags = 0;
		this.forcingSync = false;
		return writer.generate();
	}

	@Override
	public int getMagicLevel(){
		return this.magicLevel;
	}

	public float getXPToNextLevel(){
		return (float)Math.pow(magicLevel * 0.25f, 1.5f);
	}

	public float getMagicXP(){
		return this.magicXP;
	}

	public Entity getInanimateTarget(){
		return this.inanimateTarget;
	}

	public boolean getHasUpdate(){
		if (!(this.entity instanceof EntityPlayer) && !forcingSync){
			return false;
		}
		this.ticksToSync--;
		if (this.ticksToSync <= 0) this.ticksToSync = this.syncTickDelay;
		return this.updateFlags != 0 && this.ticksToSync == this.syncTickDelay;
	}

	public boolean getCanHaveMoreSummons(){

		if (entity instanceof EntityLifeGuardian)
			return true;

		int maxSummons = 1;
		if (entity instanceof EntityPlayer && SkillData.For((EntityPlayer)entity).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("ExtraSummon"))))
			maxSummons++;

		verifySummons();

		return this.numSummons < maxSummons;
	}

	public boolean getIsFlipped(){
		return (bitFlag & BIT_FLIPPED) == BIT_FLIPPED;
	}

	public boolean getIsShrunk(){
		return (bitFlag & BIT_SHRUNK) == BIT_SHRUNK;
	}

	public float getFlipRotation(){
		return this.flipRotation;
	}

	public float getPrevFlipRotation(){
		return this.prevFlipRotation;
	}

	public float getShrinkPct(){
		return this.shrinkPct;
	}

	public float getPrevShrinkPct(){
		return this.prevShrinkPct;
	}

	public boolean shouldReverseInput(){
		return getFlipRotation() > 0 || this.entity.isPotionActive(BuffList.scrambleSynapses.id);
	}

	public AMVector2 getOriginalSize(){
		return this.originalSize;
	}

	public void overrideEyeHeight(){
		if (SkillTreeManager.instance.isSkillDisabled(SkillManager.instance.getSkill("Shrink")))
			return;

		if (entity instanceof EntityPlayer){
			float baseEyeHeight = ((EntityPlayer)entity).getDefaultEyeHeight();
			float baseYOffset = entity.worldObj.isRemote ? (entity == AMCore.proxy.getLocalPlayer() ? 1.62f : 0) : 0;
			if (entity.isPotionActive(BuffList.shrink)){
				((EntityPlayer)entity).eyeHeight = baseEyeHeight / 2;
				entity.yOffset = baseYOffset / 2;
			}else{
				((EntityPlayer)entity).eyeHeight = baseEyeHeight;
				entity.yOffset = baseYOffset;
			}
		}
	}

	public boolean getCanHeal(){
		return healCooldown <= 0;
	}

	//=======================================================================================
	// Setters
	//=======================================================================================
	public void setMarkLocation(double x, double y, double z, int dimension){
		setMarkX(x);
		setMarkY(y);
		setMarkZ(z);
		setMarkDimension(dimension);
		setMarkSet(true);
		setUpdateFlag(UPD_MARK);
	}

	public void setNoMarkLocation(){
		setMarkSet(false);
		setUpdateFlag(UPD_MARK);
	}

	public void setBreathAmount(int breath){
		this.previousBreath = breath;
	}

	@Override
	public boolean setMagicLevelWithMana(int level){

		if (level > maxMagicLevel) level = maxMagicLevel;
		if (level < 0) level = 0;
		setMagicLevel(level);
		setMaxMana((float)(Math.pow(level, 1.5f) * (85f * ((float)level / maxMagicLevel)) + 500f));
		setCurrentMana(getMaxMana());
		setCurrentFatigue(0);
		setMaxFatigue(level * 10 + 1);

		return true;
	}

	public void setInanimateTarget(Entity ent){
		if (ent instanceof EntityLivingBase)
			return;
		this.inanimateTarget = ent;
	}

	public void setMarkY(double markY){
		this.markY = markY;
	}

	public void setMarkZ(double markZ){
		this.markZ = markZ;
	}

	public void setCurrentFatigue(float currentFatigue){
		if (currentFatigue < 0) currentFatigue = 0;
		if (currentFatigue > getMaxFatigue()) currentFatigue = getMaxFatigue();
		this.currentFatigue = currentFatigue;
		this.setUpdateFlag(UPD_CURRENT_MANA_FATIGUE);
	}

	public void setMaxFatigue(float maxFatigue){
		this.maxFatigue = maxFatigue;
		this.setUpdateFlag(UPD_MAX_MANA_FATIGUE);
	}

	@Override
	public void setCurrentMana(float currentMana){
		if (currentMana < 0) currentMana = 0;
		if (currentMana > getMaxMana()) currentMana = getMaxMana();
		this.currentMana = currentMana;
		this.setUpdateFlag(UPD_CURRENT_MANA_FATIGUE);
	}

	public void setMagicLevel(int magicLevel){
		if (magicLevel < 0) magicLevel = 0;
		if (magicLevel > maxMagicLevel) magicLevel = maxMagicLevel;

		if (entity instanceof EntityPlayer)
			MinecraftForge.EVENT_BUS.post(new PlayerMagicLevelChangeEvent(entity, magicLevel));

		ticksForFullRegen = (int)Math.round(baseTicksForFullRegen * (0.75 - (0.25 * (getMagicLevel() / maxMagicLevel))));

		this.magicLevel = magicLevel;
		this.setUpdateFlag(UPD_MAGIC_LEVEL);
	}

	public void setFallProtection(int protection){
		this.fallProtection = protection;
	}

	public void setContingency(ContingencyTypes type, ItemStack effect){
		this.contingencyType = type;
		this.contingencyStack = effect;
		this.setUpdateFlag(UPD_CONTINGENCY);
	}

	public void setFullSync(){
		this.ticksToSync = 0;
		this.setUpdateFlag(UPD_CONTINGENCY);
		this.setUpdateFlag(UPD_BITFLAG);
		this.setUpdateFlag(UPD_CURRENT_MANA_FATIGUE);
		this.setUpdateFlag(UPD_MAGIC_LEVEL);
		this.setUpdateFlag(UPD_MARK);
		this.setUpdateFlag(UPD_MAX_MANA_FATIGUE);
		this.setUpdateFlag(UPD_NUM_SUMMONS);
		this.setUpdateFlag(UPD_BETA_PARTICLES);
		this.hasDoneFullSync = true;
		this.forcingSync = true;
	}

	public void updateManaLink(EntityLivingBase entity){
		if (!entity.worldObj.isRemote){
			this.updateFlags |= UPD_MANALINK;
		}
		ManaLinkEntry mle = new ManaLinkEntry(entity.getEntityId(), 20);
		if (!this.manaLinks.contains(mle))
			this.manaLinks.add(mle);
		else
			this.manaLinks.remove(mle);
	}

	public float getBonusCurrentMana(){
		float bonus = 0;
		for (ManaLinkEntry entry : this.manaLinks){
			bonus += entry.getAdditionalCurrentMana(entity.worldObj, entity);
		}
		return bonus;
	}

	public float getBonusMaxMana(){
		float bonus = 0;
		for (ManaLinkEntry entry : this.manaLinks){
			bonus += entry.getAdditionalMaxMana(entity.worldObj, entity);
		}
		return bonus;
	}

	public void setIsFlipped(boolean flipped){
		if (flipped)
			this.bitFlag |= BIT_FLIPPED;
		else
			this.bitFlag &= ~BIT_FLIPPED;

		if (!entity.worldObj.isRemote)
			this.setUpdateFlag(UPD_BITFLAG);
	}

	public void setIsShrunk(boolean shrunk){
		if (shrunk)
			this.bitFlag |= BIT_SHRUNK;
		else
			this.bitFlag &= ~BIT_SHRUNK;

		if (!entity.worldObj.isRemote){
			this.setUpdateFlag(UPD_BITFLAG);
			this.forceSync();
		}
	}

	public void setDisableGravity(boolean disabled){
		this.disableGravity = disabled;
		if (!this.entity.worldObj.isRemote){
			this.setUpdateFlag(UPD_DISABLE_GRAVITY);
			this.forceSync();
		}
	}

	public void setOriginalSize(AMVector2 size){
		this.originalSize = size;
	}

	public void setHealCooldown(int length){
		this.healCooldown = length;
	}
	//=======================================================================================
	// Private Setters
	//=======================================================================================

	private void setMarkSet(boolean markSet){
		byte curValue = this.bitFlag;
		if (markSet){
			curValue |= BIT_MARK_SET;
		}else{
			curValue &= ~BIT_MARK_SET;
		}
		this.bitFlag = curValue;
		this.setUpdateFlag(UPD_BITFLAG);
	}

	private void setMarkDimension(int markDimension){
		this.markDimension = markDimension;
	}

	private void setMarkX(double markX){
		this.markX = markX;
	}

	private void setNumSummons(int numSummons){
		if (this.entity == null || this.entity.worldObj == null){
			return;
		}
		this.numSummons = numSummons;
	}

	private void setUpdateFlag(int flag){
		this.updateFlags |= flag;
	}

	private void clearUpdateFlag(int flag){
		this.updateFlags &= ~flag;
	}

	//=======================================================================================
	// Utility Methods
	//=======================================================================================

	public void setEntityReference(EntityLivingBase entity){
		this.entity = entity;
		setOriginalSize(new AMVector2(entity.width, entity.height));
		hasInitialized = true;
		isCritical = entity instanceof EntityPlayerMP;
		yOffsetOrig = entity.yOffset;

		if (isCritical)
			ticksToRegen = 5;
		else
			ticksToRegen = 20;

		if (isCritical){
			if (armorProcCooldowns[3] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 3, armorProcCooldowns[3]);
			}
			if (armorProcCooldowns[1] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 1, armorProcCooldowns[1]);
			}
			if (armorProcCooldowns[2] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 2, armorProcCooldowns[2]);
			}
			if (armorProcCooldowns[0] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 0, armorProcCooldowns[0]);
			}
		}

		if (entity.worldObj != null && entity.worldObj.isRemote && entity instanceof EntityPlayer && AMCore.proxy.playerTracker.hasAA((EntityPlayer)entity)){
			EntityLivingBase localPlayer = AMCore.instance.proxy.getLocalPlayer();
			if (entity != localPlayer)
				AMNetHandler.INSTANCE.requestAuras((EntityPlayer)entity);
		}

	}

	public void handleSpecialSyncData(){
		if (needsArmorTickCounterSync && entity instanceof EntityPlayerMP){
			needsArmorTickCounterSync = false;
			if (armorProcCooldowns[3] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 3, armorProcCooldowns[3]);
			}
			if (armorProcCooldowns[1] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 1, armorProcCooldowns[1]);
			}
			if (armorProcCooldowns[2] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 2, armorProcCooldowns[2]);
			}
			if (armorProcCooldowns[0] > 0){
				AMCore.instance.proxy.blackoutArmorPiece((EntityPlayerMP)entity, 0, armorProcCooldowns[0]);
			}
		}
	}

	private void verifySummons(){
		for (int i = 0; i < summon_ent_ids.size(); ++i){
			int id = summon_ent_ids.get(i);
			Entity e = entity.worldObj.getEntityByID(id);
			if (e == null || !(e instanceof EntityLivingBase)){
				summon_ent_ids.remove(i);
				i--;
				removeSummon();
			}
		}
	}

	public boolean addSummon(EntityLivingBase entity){
		if (!entity.worldObj.isRemote){
			summon_ent_ids.add(entity.getEntityId());
			setNumSummons(getNumSummons() + 1);
			setUpdateFlag(UPD_NUM_SUMMONS);
		}
		return true;
	}

	public boolean removeSummon(){
		if (getNumSummons() == 0){
			return false;
		}
		if (!entity.worldObj.isRemote){
			setNumSummons(getNumSummons() - 1);
			setUpdateFlag(UPD_NUM_SUMMONS);
		}
		return true;
	}

	public void setSyncAuras(){
		if (entity instanceof EntityPlayer && AMCore.proxy.playerTracker.hasAA((EntityPlayer)entity))
			this.setUpdateFlag(UPD_BETA_PARTICLES);
	}

	public boolean handleDataPacket(byte[] data){
		AMDataReader rdr = new AMDataReader(data, false);
		int entID = rdr.getInt();

		if (entID != this.entity.getEntityId()){
			return false;
		}
		int flags = rdr.getInt();

		if ((flags & UPD_BITFLAG) == UPD_BITFLAG){
			this.bitFlag = rdr.getByte();
		}
		if ((flags & UPD_CURRENT_MANA_FATIGUE) == UPD_CURRENT_MANA_FATIGUE){
			this.currentMana = rdr.getFloat();
			this.currentFatigue = rdr.getFloat();
		}
		if ((flags & UPD_MAGIC_LEVEL) == UPD_MAGIC_LEVEL){
			this.magicLevel = rdr.getInt();
			float newMagicXP = rdr.getFloat();
			if (entity.worldObj.isRemote && newMagicXP != magicXP){
				AMGuiHelper.instance.showMagicXPBar();
			}
			this.magicXP = newMagicXP;
		}
		if ((flags & UPD_MARK) == UPD_MARK){
			this.markX = rdr.getDouble();
			this.markY = rdr.getDouble();
			this.markZ = rdr.getDouble();
			this.markDimension = rdr.getInt();
			this.setMarkSet(rdr.getBoolean());
		}
		if ((flags & UPD_MAX_MANA_FATIGUE) == UPD_MAX_MANA_FATIGUE){
			this.maxMana = rdr.getFloat();
			this.maxFatigue = rdr.getFloat();
		}
		if ((flags & UPD_NUM_SUMMONS) == UPD_NUM_SUMMONS){
			this.numSummons = rdr.getInt();
		}
		if ((flags & UPD_BETA_PARTICLES) == UPD_BETA_PARTICLES && entity instanceof EntityPlayer && AMCore.proxy.playerTracker.hasAA((EntityPlayer)entity)){
			this.AuraIndex = rdr.getInt();
			this.AuraBehaviour = rdr.getInt();
			this.AuraScale = rdr.getFloat();
			this.AuraAlpha = rdr.getFloat();
			this.AuraColorRandomize = rdr.getBoolean();
			this.AuraColorDefault = rdr.getBoolean();
			this.AuraColor = rdr.getInt();
			this.AuraDelay = rdr.getInt();
			this.AuraQuantity = rdr.getInt();
			this.AuraSpeed = rdr.getFloat();
		}
		if ((flags & UPD_CONTINGENCY) == UPD_CONTINGENCY){
			this.contingencyType = ContingencyTypes.values()[rdr.getInt()];
			if (this.contingencyType != ContingencyTypes.NONE){
				this.contingencyStack = rdr.getItemStack();
			}
		}
		if ((flags & UPD_MANALINK) == UPD_MANALINK){
			this.manaLinks.clear();
			int numLinks = rdr.getInt();
			for (int i = 0; i < numLinks; ++i){
				Entity e = entity.worldObj.getEntityByID(rdr.getInt());
				if (e != null && e instanceof EntityLivingBase)
					updateManaLink((EntityLivingBase)e);
			}
		}
		if ((flags & UPD_DISABLE_GRAVITY) == UPD_DISABLE_GRAVITY){
			this.disableGravity = rdr.getBoolean();
		}

		return true;
	}

	public void setDelayedSync(int delay){
		setFullSync();
		this.ticksToSync = delay;
	}

	public void forceSync(){
		this.ticksToSync = 0;
		this.forcingSync = true;
	}

	public void updateAuraData(int index, int behaviour, float scale, float alpha, boolean colorRandom, boolean colorDefault, int color, int delay, int quantity, float speed){
		this.AuraIndex = index;
		this.AuraBehaviour = behaviour;
		this.AuraScale = scale;
		this.AuraAlpha = alpha;
		this.AuraColorRandomize = colorRandom;
		this.AuraColorDefault = colorDefault;
		this.AuraColor = color;
		this.AuraDelay = delay;
		this.AuraQuantity = quantity;
		this.AuraSpeed = speed;

		this.setUpdateFlag(UPD_BETA_PARTICLES);
	}

	public byte[] getAuraData(){
		AMDataWriter writer = new AMDataWriter();
		writer.add(this.AuraIndex);
		writer.add(this.AuraBehaviour);
		writer.add(this.AuraScale);
		writer.add(this.AuraAlpha);
		writer.add(this.AuraColorRandomize);
		writer.add(this.AuraColorDefault);
		writer.add(this.AuraColor);
		writer.add(this.AuraDelay);
		writer.add(this.AuraQuantity);
		writer.add(this.AuraSpeed);

		return writer.generate();
	}

	public void readAuraData(byte[] data){
		AMDataReader rdr = new AMDataReader(data, false);
		this.AuraIndex = rdr.getInt();
		this.AuraBehaviour = rdr.getInt();
		this.AuraScale = rdr.getFloat();
		this.AuraAlpha = rdr.getFloat();
		this.AuraColorRandomize = rdr.getBoolean();
		this.AuraColorDefault = rdr.getBoolean();
		this.AuraColor = rdr.getInt();
		this.AuraDelay = rdr.getInt();
		this.AuraQuantity = rdr.getInt();
		this.AuraSpeed = rdr.getFloat();
	}

	public void handleWaterMovement(){
		if (this.entity.isPotionActive(BuffList.swiftSwim.id)){
			this.entity.motionX *= 0.96;
			this.entity.motionY *= 0.96;
			this.entity.motionZ *= 0.96;
		}
	}

	public boolean detectPossibleDesync(){
		return false;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound){
		compound.setFloat("curMana", getCurrentMana());
		compound.setFloat("curFatigue", getCurrentFatigue());
		compound.setShort("magicLevel", (short)getMagicLevel());
		compound.setBoolean("hasUnlockedAugmented", getHasUnlockedAugmented());

		compound.setIntArray("armorCooldowns", armorProcCooldowns);
		//compound.setBoolean("isFlipped", this.getIsFlipped());
		compound.setBoolean("isShrunk", this.getIsShrunk());

		compound.setBoolean("isCritical", isCritical);

		compound.setFloat("magicXP", magicXP);

		if (contingencyType != ContingencyTypes.NONE){
			compound.setInteger("contingency_type", contingencyType.ordinal());
			NBTTagCompound effectSave = contingencyStack.writeToNBT(new NBTTagCompound());
			compound.setTag("contingency_effect", effectSave);
		}

		//mark location
		if (getMarkSet()){
			compound.setDouble("marklocationx", this.getMarkX());
			compound.setDouble("marklocationy", this.getMarkY());
			compound.setDouble("marklocationz", this.getMarkZ());
			compound.setInteger("markdimension", this.getMarkDimension());
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound compound){
		setMagicLevelWithMana(compound.getShort("magicLevel"));
		setCurrentMana(compound.getFloat("curMana"));
		setCurrentFatigue(compound.getFloat("curFatigue"));

		armorProcCooldowns = compound.getIntArray("armorCooldowns");
		if (armorProcCooldowns == null)
			armorProcCooldowns = new int[4];
		else if (armorProcCooldowns.length != 4){
			int[] tmp = armorProcCooldowns;
			armorProcCooldowns = new int[4];
			for (int i = 0; i < Math.min(tmp.length, 4); ++i){
				armorProcCooldowns[i] = tmp[i];
			}
		}

		//setIsFlipped(compound.getBoolean("isFlipped"));
		setIsShrunk(compound.getBoolean("isShrunk"));
		//flipRotation = getIsFlipped() ? 0 : 180;

		isCritical = compound.getBoolean("isCritical");

		magicXP = compound.getFloat("magicXP");

		for (int i = 0; i < 4; ++i){
			if (armorProcCooldowns[i] > 0){
				needsArmorTickCounterSync = true;
				break;
			}
		}

		if (compound.hasKey("marklocationx")){
			setMarkX(compound.getDouble("marklocationx"));
			setMarkY(compound.getDouble("marklocationy"));
			setMarkZ(compound.getDouble("marklocationz"));
			setMarkDimension(compound.getInteger("markdimension"));
			this.setMarkSet(true);
		}

		if (compound.hasKey("contingency_type")){
			this.contingencyType = ContingencyTypes.values()[compound.getInteger("contingency_type")];
			this.contingencyStack = ItemStack.loadItemStackFromNBT((NBTTagCompound)compound.getTag("contingency_effect"));
		}
	}

	@Override
	public void init(Entity entity, World world){
		if (world == null || entity == null || !(entity instanceof EntityLivingBase)) return;

		setEntityReference((EntityLivingBase)entity);

		if (entity instanceof EntityPlayer){
			maxMana = 0;
			currentMana = 0;
			magicLevel = 0;
			maxFatigue = 1;
			currentFatigue = 0;
		}else{
			maxMana = 100;
			currentMana = 100;
			magicLevel = 1;
			maxFatigue = 1;
			currentFatigue = 0;
		}
		numSummons = 0;
		armorProcCooldowns = new int[4];

		markX = 0;
		markY = 0;
		markZ = 0;
		markDimension = -512;

		updateFlags = 0;

		ticksToSync = new Random().nextInt(syncTickDelay);

		hasInitialized = true;
	}

	public void deductMana(float manaCost){
		float leftOver = manaCost - currentMana;
		this.setCurrentMana(currentMana - manaCost);
		if (leftOver > 0){
			for (ManaLinkEntry entry : this.manaLinks){
				leftOver -= entry.deductMana(entity.worldObj, entity, leftOver);
				if (leftOver <= 0)
					break;
			}
		}
	}

	public void toggleFlipped(){
		if (entity.worldObj.isRemote){
			AMNetHandler.INSTANCE.sendExPropCommandToServer(this.BIT_FLIPPED);
		}
		if (this.getIsFlipped())
			this.setIsFlipped(false);
		else
			this.setIsFlipped(true);
	}

	public void spawnManaLinkParticles(){
		if (entity.worldObj != null && entity.worldObj.isRemote){
			for (ManaLinkEntry entry : this.manaLinks){
				Entity e = entity.worldObj.getEntityByID(entry.entityID);
				if (e != null && e.getDistanceSqToEntity(entity) < entry.range && e.ticksExisted % 90 == 0){
					AMLineArc arc = (AMLineArc)AMCore.proxy.particleManager.spawn(entity.worldObj, "textures/blocks/oreblockbluetopaz.png", e, entity);
					if (arc != null){
						arc.setIgnoreAge(false);
						arc.setRBGColorF(0.17f, 0.88f, 0.88f);
					}
				}
			}
		}
	}

	public void addBurnout(float burnout){
		if (entity.isPotionActive(BuffList.burnoutReduction))
			burnout *= 0.75f;
		this.setCurrentFatigue(currentFatigue + burnout);
	}

	public void handleExtendedPropertySync(){
		if (!this.getHasDoneFullSync()) this.setFullSync();

		if (!entity.worldObj.isRemote && this.getHasUpdate()){
			byte[] data = this.getUpdateData();
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(entity.dimension, entity.posX, entity.posY, entity.posZ, 32, AMPacketIDs.SYNC_EXTENDED_PROPS, data);
		}

		if (entity.worldObj.isRemote){
			if (this.detectPossibleDesync()){
				AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.POSSIBLE_CLIENT_EXPROP_DESYNC, new AMDataWriter().add(entity.getEntityId()).generate());
			}
		}
	}

	public void syncTKDistance(){
		AMDataWriter writer = new AMDataWriter();
		writer.add(this.TK_Distance);
		AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.TK_DISTANCE_SYNC, writer.generate());
	}

	public void addMagicXP(float amt){
		if (magicLevel == maxMagicLevel || !(this.entity instanceof EntityPlayer)) return;

		this.magicXP += amt;
		float xpToLevel = getXPToNextLevel();
		if (magicXP >= xpToLevel){
			magicXP = 0;

			setMagicLevelWithMana(magicLevel + 1);

			if (this.entity instanceof EntityPlayer && magicLevel % 2 == 0){
				EntityPlayer ent = (EntityPlayer)this.entity;
				if (magicLevel <= 20)
					SkillData.For(ent).incrementSpellPoints(SkillPointTypes.BLUE);
				else if (magicLevel <= 40)
					SkillData.For(ent).incrementSpellPoints(SkillPointTypes.GREEN);
				else if (magicLevel <= 50)
					SkillData.For(ent).incrementSpellPoints(SkillPointTypes.RED);

			}
			this.entity.worldObj.playSoundAtEntity(entity, "arsmagica2:misc.event.magic_level_up", 1, 1);
		}
		setUpdateFlag(UPD_MAGIC_LEVEL);
		forceSync();
	}

	public void procContingency(){
		SpellHelper.instance.applyStackStage(contingencyStack, entity, entity, entity.posX, entity.posY, entity.posZ, 0, entity.worldObj, false, false, 0);
		AMNetHandler.INSTANCE.sendSpellApplyEffectToAllAround(entity, entity, entity.posX, entity.posY, entity.posZ, entity.worldObj, contingencyStack);

		this.setContingency(ContingencyTypes.NONE, null);
		this.forceSync();
	}

	public void manaBurnoutTick(){
		ticksSinceLastRegen++;
		healCooldown--;

		if (disableGravity){
			this.entity.motionY = 0;
		}

		if (ticksSinceLastRegen >= ticksToRegen){
			//mana regeneration
			if (getCurrentMana() < getMaxMana()){
				if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode){
					setCurrentMana(getMaxMana());
				}else{
					if (getCurrentMana() < 0){
						setCurrentMana(0);
					}

					int regenTicks = (int)Math.ceil(ticksForFullRegen * entity.getAttributeMap().getAttributeInstance(ArsMagicaApi.manaRegenTimeModifier).getAttributeValue());

					//mana regen buff
					if (entity.isPotionActive(BuffList.manaRegen.id)){
						PotionEffect pe = entity.getActivePotionEffect(BuffList.manaRegen);
						regenTicks *= (1.0f - Math.max(0.9f, (0.25 * (pe.getAmplifier() + 1))));
					}

					//mana scepter handling - 10% boost to mana regen
					if (entity instanceof EntityPlayer){
						EntityPlayer player = (EntityPlayer)entity;
						int armorSet = ArmorHelper.getFullArsMagicaArmorSet(player);
						if (armorSet == ArsMagicaArmorMaterial.MAGE.getMaterialID()){
							regenTicks *= 0.8;
						}else if (armorSet == ArsMagicaArmorMaterial.BATTLEMAGE.getMaterialID()){
							regenTicks *= 0.95;
						}else if (armorSet == ArsMagicaArmorMaterial.ARCHMAGE.getMaterialID()){
							regenTicks *= 0.5;
						}

						if (SkillData.For(player).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("ManaRegenIII")))){
							regenTicks *= 0.7f;
						}else if (SkillData.For(player).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("ManaRegenII")))){
							regenTicks *= 0.85f;
						}else if (SkillData.For(player).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("ManaRegenI")))){
							regenTicks *= 0.95f;
						}

						//armor infusions
						int numArmorPieces = 0;
						for (int i = 0; i < 4; ++i){
							ItemStack stack = player.inventory.armorItemInSlot(i);
							if (ImbuementRegistry.instance.isImbuementPresent(stack, GenericImbuement.manaRegen))
								numArmorPieces++;
						}
						regenTicks *= 1.0f - (0.15f * numArmorPieces);
					}

					//actual mana regen
					float manaToAdd = (getMaxMana() / regenTicks) * ticksSinceLastRegen;

					setCurrentMana(getCurrentMana() + manaToAdd);
					if (getCurrentMana() > getMaxMana()){
						setCurrentMana(getMaxMana());
					}
				}
			}
			//fatigue decrease
			if (getCurrentFatigue() > 0){
				int numArmorPieces = 0;
				if (entity instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer)entity;
					for (int i = 0; i < 4; ++i){
						ItemStack stack = player.inventory.armorItemInSlot(i);
						if (ImbuementRegistry.instance.isImbuementPresent(stack, GenericImbuement.burnoutReduction))
							numArmorPieces++;
					}
				}
				float factor = (float)((0.01f + (0.015f * numArmorPieces)) * entity.getAttributeMap().getAttributeInstance(ArsMagicaApi.burnoutReductionRate).getAttributeValue());
				float decreaseamt = (factor * getMagicLevel()) * ticksSinceLastRegen;
				//actual fatigue decrease
				setCurrentFatigue(getCurrentFatigue() - decreaseamt);
				if (getCurrentFatigue() < 0){
					setCurrentFatigue(0);
				}
			}

			ticksSinceLastRegen = 0;
		}
	}

	public void flipTick(){
		boolean flipped = getIsFlipped();

		ItemStack boots = ((EntityPlayer)entity).inventory.armorInventory[0];
		if (boots == null || boots.getItem() != ItemsCommonProxy.enderBoots)
			setIsFlipped(false);

		prevFlipRotation = flipRotation;
		if (flipped && flipRotation < 180)
			flipRotation += 15;
		else if (!flipped && flipRotation > 0)
			flipRotation -= 15;
	}

	public void shrinkTick(){

		if (SkillTreeManager.instance.isSkillDisabled(SkillManager.instance.getSkill("Shrink")))
			return;

		boolean shrunk = getIsShrunk();

		if (!entity.worldObj.isRemote && shrunk && !entity.isPotionActive(BuffList.shrink)){
			setIsShrunk(false);
			return;
		}else if (!entity.worldObj.isRemote && !shrunk && entity.isPotionActive(BuffList.shrink)){
			setIsShrunk(true);
			return;
		}

		prevShrinkPct = shrinkPct;
		if (shrunk && shrinkPct < 1f){
			shrinkPct += 0.05f;
		}else if (!shrunk && shrinkPct > 0f){
			shrinkPct -= 0.05f;
		}
	}

	public void cleanupManaLinks(){
		Iterator<ManaLinkEntry> it = this.manaLinks.iterator();
		while (it.hasNext()){
			ManaLinkEntry entry = it.next();
			Entity e = this.entity.worldObj.getEntityByID(entry.entityID);
			if (e == null)
				it.remove();
		}
	}

	public boolean isManaLinkedTo(EntityLivingBase entity){
		for (ManaLinkEntry entry : manaLinks){
			if (entry.entityID == entity.getEntityId())
				return true;
		}
		return false;
	}

	private class ManaLinkEntry{
		private final int entityID;
		private final int range;

		public ManaLinkEntry(int entityID, int range){
			this.entityID = entityID;
			this.range = range * range;
		}

		private EntityLivingBase getEntity(World world){
			Entity e = world.getEntityByID(entityID);
			if (e == null || !(e instanceof EntityLivingBase))
				return null;
			return (EntityLivingBase)e;
		}

		public float getAdditionalCurrentMana(World world, EntityLivingBase host){
			EntityLivingBase e = getEntity(world);
			if (e == null || e.getDistanceSqToEntity(host) > range)
				return 0;
			return For(e).getCurrentMana();
		}

		public float getAdditionalMaxMana(World world, EntityLivingBase host){
			EntityLivingBase e = getEntity(world);
			if (e == null || e.getDistanceSqToEntity(host) > range)
				return 0;
			return For(e).getMaxMana();
		}

		public float deductMana(World world, EntityLivingBase host, float amt){
			EntityLivingBase e = getEntity(world);
			if (e == null || e.getDistanceSqToEntity(host) > range)
				return 0;
			amt = Math.min(For(e).getCurrentMana(), amt);
			For(e).deductMana(amt);
			if (!world.isRemote)
				For(e).forceSync();
			return amt;
		}

		@Override
		public int hashCode(){
			return entityID;
		}

		@Override
		public boolean equals(Object obj){
			if (obj instanceof ManaLinkEntry)
				return ((ManaLinkEntry)obj).entityID == this.entityID;
			return false;
		}
	}

	public void performRemoteOp(int mask){
		if (entity.worldObj.isRemote)
			return;
		switch (mask){
		case BIT_FLIPPED:
			toggleFlipped();
			forceSync();
			break;
		}
	}
}

