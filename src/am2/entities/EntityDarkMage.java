package am2.entities;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import am2.entities.ai.EntityAIRangedAttackSpell;
import am2.entities.ai.selectors.DarkMageEntitySelector;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;
import am2.utility.NPCSpells;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityDarkMage extends EntityMob{

	private static ItemStack diminishedHeldItem = new ItemStack(ItemsCommonProxy.bookAffinity, 1, 6);
	private static ItemStack normalHeldItem = new ItemStack(ItemsCommonProxy.bookAffinity, 1, 3);
	private static ItemStack augmentedHeldItem = new ItemStack(ItemsCommonProxy.bookAffinity, 1, 0);

	private boolean hasUpdated = false;

	public static final int DW_MAGE_SKIN = 20;
	public static final int DW_MAGE_BOOK = 21;

	public EntityDarkMage(World world) {
		super(world);
		setSize(1F, 2F);
		ExtendedProperties.For(this).setMagicLevelWithMana(10 + rand.nextInt(20));
		initAI();
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(DW_MAGE_BOOK, 0);
		this.dataWatcher.addObject(DW_MAGE_SKIN, rand.nextInt(10) + 1);
	}

	@Override
	public ItemStack getHeldItem() {
		int cm = this.dataWatcher.getWatchableObjectInt(DW_MAGE_BOOK);
		if (cm == 0)
			return diminishedHeldItem;
		else if (cm == 1)
			return this.normalHeldItem;
		else
			return this.augmentedHeldItem;
	}

	private void initAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityLightMage.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.tasks.addTask(5, new EntityAIWander(this, MovementSpeed()));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityManaVortex.class, 10, MovementSpeed(), ActionSpeed()));

		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, MovementSpeed(), 40, NPCSpells.instance.darkMage_DiminishedAttack));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLightMage.class, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, false, DarkMageEntitySelector.instance));

	}

	@Override
	public int getTotalArmorValue()
	{
		return 5;
	}

	protected float MovementSpeed(){
		return 0.4f;
	}

	protected float ActionSpeed(){
		return 0.5f;
	}

	@Override
	public boolean isAIEnabled(){
		return true;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		if (par1 && getRNG().nextDouble() < 0.2)
			for (int j = 0; j < getRNG().nextInt(3); ++j)
				this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, getRNG().nextInt(16)), 0.0f);

		if (par1 && getRNG().nextDouble() < 0.2)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.spellParchment, 1, 0), 0.0f);

		if (par1 && getRNG().nextDouble() < 0.05)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.spellBook, 1, 0), 0.0f);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		hasUpdated = true;
		ExtendedProperties.For(this).forceSync();
		this.dataWatcher.updateObject(DW_MAGE_SKIN, par1nbtTagCompound.getInteger("am2_dm_skin"));
		this.dataWatcher.updateObject(DW_MAGE_BOOK, par1nbtTagCompound.getInteger("am2_dm_book"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger("am2_dm_skin", this.dataWatcher.getWatchableObjectInt(DW_MAGE_SKIN));
		par1nbtTagCompound.setInteger("am2_dm_book", this.dataWatcher.getWatchableObjectInt(DW_MAGE_BOOK));
	}

	private int getAverageNearbyPlayerMagicLevel(){
		if (this.worldObj == null) return 0;
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.posX - 250, 0, this.posZ - 250, this.posX + 250, 250, this.posZ + 250));
		if (players.size() == 0) return 0;
		int avgLvl = 0;
		for (EntityPlayer player : players){
			avgLvl += ExtendedProperties.For(player).getMagicLevel();
		}
		return (int)Math.ceil(avgLvl/players.size());
	}

	@Override
	public boolean getCanSpawnHere() {
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		if (getAverageNearbyPlayerMagicLevel() < 8){
			return false;
		}

		ExtendedProperties.For(this).setMagicLevelWithMana(5);
		int avgLevel = getAverageNearbyPlayerMagicLevel();
		if (avgLevel == 0){
			if (rand.nextInt(100)<10){
				this.tasks.addTask(3, new EntityAIRangedAttackSpell(this, MovementSpeed(), 80, NPCSpells.instance.darkMage_NormalAttack));
				this.dataWatcher.updateObject(DW_MAGE_BOOK, 1);
			}
		}else{
			int levelRand = rand.nextInt(avgLevel * 2);
			if (levelRand > 60){
				this.tasks.addTask(2, new EntityAIRangedAttackSpell(this, MovementSpeed(), 160, NPCSpells.instance.darkMage_AugmentedAttack));
				this.dataWatcher.updateObject(DW_MAGE_BOOK, 2);
			}else if (levelRand > 30) {
				this.tasks.addTask(3, new EntityAIRangedAttackSpell(this, MovementSpeed(), 80, NPCSpells.instance.darkMage_NormalAttack));
				this.dataWatcher.updateObject(DW_MAGE_BOOK, 1);
			}
		}
		ExtendedProperties.For(this).setFullSync();
		hasUpdated = true;

		return super.getCanSpawnHere();
	}

	@Override
	protected void dropRareDrop(int par1) {
	}

	@SideOnly(Side.CLIENT)
	public String getTexture(){
		return ResourceManager.getMobTexturePath(String.format("dark_mages/dark_mage_%d.png", this.dataWatcher.getWatchableObjectInt(DW_MAGE_SKIN)));
	}


}
