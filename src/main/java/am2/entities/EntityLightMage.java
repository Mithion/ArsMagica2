package am2.entities;

import am2.entities.ai.EntityAIAllyManaLink;
import am2.entities.ai.EntityAIRangedAttackSpell;
import am2.entities.ai.selectors.LightMageEntitySelector;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.texture.ResourceManager;
import am2.utility.EntityUtilities;
import am2.utility.NPCSpells;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.List;

public class EntityLightMage extends EntityCreature{

	int hp;
	private static ItemStack diminishedHeldItem = new ItemStack(ItemsCommonProxy.bookAffinity, 1, 2);
	private static ItemStack normalHeldItem = new ItemStack(ItemsCommonProxy.bookAffinity, 1, 7);
	private static ItemStack augmentedHeldItem = new ItemStack(ItemsCommonProxy.bookAffinity, 1, 5);

	private boolean hasUpdated = false;

	public static final int DW_MAGE_SKIN = 20;
	public static final int DW_MAGE_BOOK = 21;

	public EntityLightMage(World world){
		super(world);
		setSize(1F, 2F);
		hp = rand.nextInt(10) + 12;
		initAI();
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(DW_MAGE_BOOK, 0);
		this.dataWatcher.addObject(DW_MAGE_SKIN, rand.nextInt(12) + 1);
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22D);
	}

	@Override
	public ItemStack getHeldItem(){
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
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.tasks.addTask(5, new EntityAIWander(this, MovementSpeed()));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityManaVortex.class, 10, MovementSpeed(), ActionSpeed()));

		this.tasks.addTask(3, new EntityAIAllyManaLink(this));

		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, MovementSpeed(), 20, NPCSpells.instance.lightMage_DiminishedAttack));

		//Retaliation to attacks
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));

		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityDarkMage.class, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySlime.class, 0, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityMob.class, 0, true, false, LightMageEntitySelector.instance));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readEntityFromNBT(par1nbtTagCompound);
		hasUpdated = true;
		ExtendedProperties.For(this).forceSync();
		this.dataWatcher.updateObject(DW_MAGE_SKIN, par1nbtTagCompound.getInteger("am2_lm_skin"));
		this.dataWatcher.updateObject(DW_MAGE_BOOK, par1nbtTagCompound.getInteger("am2_lm_book"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger("am2_lm_skin", this.dataWatcher.getWatchableObjectInt(DW_MAGE_SKIN));
		par1nbtTagCompound.setInteger("am2_lm_book", this.dataWatcher.getWatchableObjectInt(DW_MAGE_BOOK));
	}

	@Override
	public int getTotalArmorValue(){
		return 1;
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

	private int getAverageNearbyPlayerMagicLevel(){
		if (this.worldObj == null) return 0;
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand(250, 250, 250));
		if (players.size() == 0) return 0;
		int avgLvl = 0;
		for (EntityPlayer player : players){
			avgLvl += ExtendedProperties.For(player).getMagicLevel();
		}
		return (int)Math.ceil(avgLvl / players.size());
	}

	@Override
	public boolean getCanSpawnHere(){
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		if (getAverageNearbyPlayerMagicLevel() < 8){
			return false;
		}

		int avgLevel = getAverageNearbyPlayerMagicLevel();
		IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		if (avgLevel == 0){
			ExtendedProperties.For(this).setMagicLevelWithMana(10);
			if (rand.nextInt(100) < 10){
				this.tasks.addTask(3, new EntityAIRangedAttackSpell(this, MovementSpeed(), 40, NPCSpells.instance.lightMage_NormalAttack));
				this.dataWatcher.updateObject(DW_MAGE_BOOK, 1);
			}
		}else{
			ExtendedProperties.For(this).setMagicLevelWithMana(10 + rand.nextInt(avgLevel));
			int levelRand = rand.nextInt(avgLevel * 2);
			if (levelRand > 60){
				this.tasks.addTask(2, new EntityAIRangedAttackSpell(this, MovementSpeed(), 100, NPCSpells.instance.lightMage_AugmentedAttack));
				this.dataWatcher.updateObject(DW_MAGE_BOOK, 2);
			}
			if (levelRand > 30){
				this.tasks.addTask(3, new EntityAIRangedAttackSpell(this, MovementSpeed(), 40, NPCSpells.instance.lightMage_NormalAttack));
				this.dataWatcher.updateObject(DW_MAGE_BOOK, 1);
			}
		}
		if (!worldObj.isRemote)
			ExtendedProperties.For(this).setFullSync();
		hasUpdated = true;

		return isValidLightLevel() && super.getCanSpawnHere();
	}

	protected boolean isValidLightLevel(){
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);

		if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32)){
			return false;
		}else{
			int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);

			if (this.worldObj.isThundering()){
				int var5 = this.worldObj.skylightSubtracted;
				this.worldObj.skylightSubtracted = 10;
				var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
				this.worldObj.skylightSubtracted = var5;
			}

			return var4 <= this.rand.nextInt(8);
		}
	}

	@Override
	protected void dropRareDrop(int par1){
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1 && getRNG().nextDouble() < 0.2)
			for (int j = 0; j < getRNG().nextInt(3); ++j)
				this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, getRNG().nextInt(16)), 0.0f);

		if (par1 && getRNG().nextDouble() < 0.2)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.spellParchment, 1, 0), 0.0f);

		if (par1 && getRNG().nextDouble() < 0.05)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.spellBook, 1, 0), 0.0f);
	}

	@Override
	protected boolean interact(EntityPlayer player){
		if (worldObj.isRemote)
			return false;

		if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemNameTag)
			return false;

		if (SkillData.For(player).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("MageBandI")))){
			if (EntityUtilities.isSummon(this)){
				player.addChatMessage(new ChatComponentText(String.format("\247o%s", StatCollector.translateToLocal("am2.npc.partyleave"))));
				EntityUtilities.revertAI(this);
			}else{
				if (ExtendedProperties.For(player).getCanHaveMoreSummons()){
					if (ExtendedProperties.For(player).getMagicLevel() - 5 >= ExtendedProperties.For(this).getMagicLevel()){
						player.addChatMessage(new ChatComponentText(String.format("\247o%s", StatCollector.translateToLocal("am2.npc.partyjoin"))));
						EntityUtilities.setOwner(this, player);
						EntityUtilities.makeSummon_PlayerFaction(this, player, true);
						EntityUtilities.setSummonDuration(this, -1);
					}else{
						player.addChatMessage(new ChatComponentText(String.format("\247o%s", StatCollector.translateToLocal("am2.npc.partyrefuse"))));
					}
				}else{
					player.addChatMessage(new ChatComponentText(String.format("\247o%s", StatCollector.translateToLocal("am2.npc.partyfull"))));
				}
			}
		}else{
			player.addChatMessage(new ChatComponentText(String.format("\247o%s", StatCollector.translateToLocal("am2.npc.nopartyskill"))));
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public String getTexture(){
		return ResourceManager.getMobTexturePath(String.format("light_mages/light_mage_%d.png", this.dataWatcher.getWatchableObjectInt(DW_MAGE_SKIN)));
	}
}
