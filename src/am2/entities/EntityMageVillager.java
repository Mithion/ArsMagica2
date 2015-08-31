package am2.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.AMCore;
import am2.entities.ai.EntityAIRangedAttackSpell;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.utility.NPCSpells;

public class EntityMageVillager extends EntityVillager{

	private float actionSpeed;

	private static final ItemStack heldItem = new ItemStack(ItemsCommonProxy.spellBook);

	public EntityMageVillager(World par1World) {
		super(par1World);
		ExtendedProperties.For(this).setMagicLevelWithMana(37);

		initAI();
	}

	public String getTexture()
    {
		return "/assets/ArsMagica/textures/mobs/light_mages/wizard_villager3.png";
    }

	@Override
	public int getProfession() {
		return AMCore.config.getVillagerProfessionID();
	}

	private void initAI(){
		this.tasks.taskEntries.clear();
		this.setAIMoveSpeed(0.5F);
		this.actionSpeed = 0.7f;
		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITradePlayer(this));
		this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));

		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, actionSpeed, 60, NPCSpells.instance.darkMage_DiminishedAttack));
		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, actionSpeed, 140, NPCSpells.instance.healSelf));
		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, actionSpeed, 40, NPCSpells.instance.fireBolt));
		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, actionSpeed, 100, NPCSpells.instance.arcaneBolt));
		this.tasks.addTask(4, new EntityAIRangedAttackSpell(this, actionSpeed, 120, NPCSpells.instance.lightMage_NormalAttack));

		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.3F));
		this.tasks.addTask(7, new EntityAIFollowGolem(this));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
		this.tasks.addTask(9, new EntityAIWander(this, 0.3F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));

		this.targetTasks.taskEntries.clear();

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, 5, false));
	}

	@Override
	public ItemStack getHeldItem() {
		return null;
	}
}
