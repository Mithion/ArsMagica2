package am2.entities.ai;

import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.utility.EntityUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityAIAllyManaLink extends EntityAIBase{

	private EntityCreature host;
	private static final ItemStack spellStack = new ItemStack(ItemsCommonProxy.spell);

	public EntityAIAllyManaLink(EntityCreature host){
		this.host = host;
		SpellUtils.instance.addSpellStageToScroll(spellStack, "Touch", new String[]{"ManaLink"}, new String[0]);
	}

	@Override
	public boolean shouldExecute(){
		boolean isSummon = EntityUtilities.isSummon(host);
		if (!isSummon)
			return false;
		EntityPlayer owner = getHostOwner();
		if (owner == null || !SkillData.For(owner).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("MageBandII"))) || host.getDistanceSqToEntity(host) > 64D || ExtendedProperties.For(owner).isManaLinkedTo(host))
			return false;
		return true;
	}

	private EntityPlayer getHostOwner(){
		int ownerID = EntityUtilities.getOwner(host);
		Entity owner = host.worldObj.getEntityByID(ownerID);
		if (owner == null || !(owner instanceof EntityPlayer))
			return null;
		return (EntityPlayer)owner;
	}

	@Override
	public void updateTask(){
		EntityPlayer owner = getHostOwner();
		if (owner == null)
			return;
		if (host.getDistanceToEntity(owner) < 1)
			host.getNavigator().tryMoveToXYZ(host.posX, host.posY, host.posZ, 0.5f);
		else
			SpellHelper.instance.applyStackStage(spellStack, host, owner, owner.posX, owner.posY, owner.posZ, 0, host.worldObj, false, false, 0);
	}

}
