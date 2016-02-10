package am2.playerextensions;

import am2.AMCore;
import am2.LogHelper;
import am2.api.ISkillData;
import am2.api.SkillTreeEntry;
import am2.api.events.SkillLearnedEvent;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.LearnStates;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTrees;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class SkillData implements IExtendedEntityProperties, ISkillData{
	private Entity entity;

	private static final byte SPELL_LEARNED = 0x01;
	private static final byte CLIENT_SYNC = 0x02;

	private static final byte KNOWN_SHAPE_UPDATE = 0x1;
	private static final byte KNOWN_COMPONENT_UPDATE = 0x2;
	private static final byte KNOWN_MODIFIER_UPDATE = 0x4;
	private static final byte NUM_SPELL_POINTS = 0x8;
	private static final byte KNOWN_TALENTS_UPDATE = 0x10;

	private boolean hasDoneFullSync = false;

	private int updateFlags = 0;

	private boolean forcingSync = false;
	private int ticksToSync = 0;
	private final int syncTickDelay = 2400;

	private ArrayList<Integer> shapesKnown;
	private ArrayList<Integer> componentsKnown;
	private ArrayList<Integer> modifiersKnown;
	private ArrayList<Integer> talentsKnown;
	private SkillTrees primaryTree;
	private final EntityPlayer player;

	private int[] spellPoints;

	public static final String identifier = "SpellKnowledgeData";

	public static SkillData For(EntityPlayer player){
		return (SkillData)player.getExtendedProperties(identifier);
	}

	public SkillData(EntityPlayer player){
		shapesKnown = new ArrayList<Integer>();
		componentsKnown = new ArrayList<Integer>();
		modifiersKnown = new ArrayList<Integer>();
		talentsKnown = new ArrayList<Integer>();
		spellPoints = new int[]{3, 0, 0, 0};
		this.primaryTree = SkillTrees.None;
		this.player = player;
	}

	private boolean isShapeKnown(int shapeID){
		return shapesKnown.contains(shapeID);
	}

	private boolean isComponentKnown(int componentID){
		return componentsKnown.contains(componentID);
	}

	private boolean isModifierKnown(int modifierID){
		return modifiersKnown.contains(modifierID);
	}

	private boolean isTalentKnown(int skillID){
		return talentsKnown.contains(skillID);
	}

	public SkillTrees getPrimaryTree(){
		return primaryTree;
	}

	@Override
	public boolean isEntryKnown(SkillTreeEntry entry){
		if (player.capabilities.isCreativeMode) return true;
		if (entry.registeredItem instanceof ISpellShape)
			return isShapeKnown(((ISpellShape)entry.registeredItem).getID());
		else if (entry.registeredItem instanceof ISpellComponent)
			return isComponentKnown(((ISpellComponent)entry.registeredItem).getID() + SkillManager.COMPONENT_OFFSET);
		else if (entry.registeredItem instanceof ISpellModifier)
			return isModifierKnown(((ISpellModifier)entry.registeredItem).getID() + SkillManager.MODIFIER_OFFSET);
		else if (entry.registeredItem instanceof ISkillTreeEntry)
			return isTalentKnown(entry.registeredItem.getID() + SkillManager.TALENT_OFFSET);
		return false;
	}

	@Override
	public int getSpellPoints(SkillPointTypes type){
		return spellPoints[type.ordinal()];
	}

	@Override
	public void setSpellPoints(int spellPoints, SkillPointTypes type){
		this.spellPoints[type.ordinal()] = spellPoints;
		if (this.spellPoints[type.ordinal()] < 0) this.spellPoints[type.ordinal()] = 0;
		this.updateFlags |= NUM_SPELL_POINTS;
	}

	@Override
	public void setSpellPoints(int spellPoints_blue, int spellPoints_green, int spellPoints_red){
		setSpellPoints(spellPoints_blue, SkillPointTypes.BLUE);
		setSpellPoints(spellPoints_green, SkillPointTypes.GREEN);
		setSpellPoints(spellPoints_red, SkillPointTypes.RED);
	}

	@Override
	public void incrementSpellPoints(SkillPointTypes type){
		this.spellPoints[type.ordinal()]++;
		this.updateFlags |= NUM_SPELL_POINTS;
	}

	@Override
	public void decrementSpellPoints(SkillPointTypes type){
		this.spellPoints[type.ordinal()]--;
		if (this.spellPoints[type.ordinal()] < 0) this.spellPoints[type.ordinal()] = 0;
		this.updateFlags |= NUM_SPELL_POINTS;
	}

	private void setShapeKnown(int shapeID){
		if (!isShapeKnown(shapeID)){
			shapesKnown.add(shapeID);
			if (primaryTree == SkillTrees.None){
				ISkillTreeEntry part = SkillManager.instance.getSkill(shapeID);
				SkillTreeEntry ste = SkillTreeManager.instance.getSkillTreeEntry(part);
				if (ste != null && ste.tree != SkillTrees.Talents && ste.tree != SkillTrees.None){
					primaryTree = ste.tree;
				}
			}
		}
	}

	private void setComponentKnown(int componentID){
		if (!isComponentKnown(componentID)){
			componentsKnown.add(componentID);
			if (primaryTree == SkillTrees.None){
				ISkillTreeEntry part = SkillManager.instance.getSkill(componentID);
				SkillTreeEntry ste = SkillTreeManager.instance.getSkillTreeEntry(part);
				if (ste != null && ste.tree != SkillTrees.Talents && ste.tree != SkillTrees.None){
					primaryTree = ste.tree;
				}
			}
		}
	}

	private void setModifierKnown(int modifierID){
		if (!isModifierKnown(modifierID)){
			modifiersKnown.add(modifierID);
			if (primaryTree == SkillTrees.None){
				ISkillTreeEntry part = SkillManager.instance.getSkill(modifierID);
				SkillTreeEntry ste = SkillTreeManager.instance.getSkillTreeEntry(part);
				if (ste != null && ste.tree != SkillTrees.Talents && ste.tree != SkillTrees.None){
					primaryTree = ste.tree;
				}
			}
		}
	}

	private void setTalentKnown(int talentID){
		if (!isTalentKnown(talentID)){
			talentsKnown.add(talentID);
		}
	}

	private void learnInternal(ISkillTreeEntry entry){
		if (entry instanceof ISpellShape) setShapeKnown(((ISpellShape)entry).getID());
		else if (entry instanceof ISpellComponent) setComponentKnown(((ISpellComponent)entry).getID());
		else if (entry instanceof ISpellModifier) setModifierKnown(((ISpellModifier)entry).getID());
		else if (entry instanceof ISkillTreeEntry) setTalentKnown(entry.getID());
	}

	@Override
	public void learn(ISkillTreeEntry entry){
		if (entity.worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(SPELL_LEARNED);
			writer.add(this.entity.getEntityId());
			int id = entry.getID();
			if (entry instanceof ISpellComponent)
				id += SkillManager.instance.COMPONENT_OFFSET;
			else if (entry instanceof ISpellModifier)
				id += SkillManager.instance.MODIFIER_OFFSET;
			else if (!(entry instanceof ISpellShape))
				id += SkillManager.instance.TALENT_OFFSET;
			writer.add(id);
			if (entry instanceof ISpellShape) writer.add(0);
			else if (entry instanceof ISpellComponent) writer.add(1);
			else if (entry instanceof ISpellModifier) writer.add(2);
			else writer.add(3);
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SYNC_SPELL_KNOWLEDGE, writer.generate());

		}else{
			int id = entry.getID();
			if (entry instanceof ISpellComponent)
				id += SkillManager.instance.COMPONENT_OFFSET;
			else if (entry instanceof ISpellModifier)
				id += SkillManager.instance.MODIFIER_OFFSET;
			else if (!(entry instanceof ISpellShape))
				id += SkillManager.instance.TALENT_OFFSET;

			int type = 3;
			if (entry instanceof ISpellShape) type = 0;
			else if (entry instanceof ISpellComponent) type = 1;
			else if (entry instanceof ISpellModifier) type = 2;

			learn(id, type);
			MinecraftForge.EVENT_BUS.post(new SkillLearnedEvent(player, entry));
		}
	}

	public void learn(int id, int type){
		if (entity.worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(SPELL_LEARNED);
			writer.add(this.entity.getEntityId());
			writer.add(id);
			writer.add(type);
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SYNC_SPELL_KNOWLEDGE, writer.generate());

			MinecraftForge.EVENT_BUS.post(new SkillLearnedEvent(player, SkillManager.instance.getSkill(id)));
		}else{
			SkillPointTypes skillType = SkillTreeManager.instance.getSkillPointTypeForPart(id);
			boolean learned = false;
			switch (type){
			case 0:
				if (!isShapeKnown(id) && getSpellPoints(skillType) > 0){
					setShapeKnown(id);
					decrementSpellPoints(skillType);
					learned = true;
				}
				updateFlags |= KNOWN_SHAPE_UPDATE;
				break;
			case 1:
				if (!isComponentKnown(id) && getSpellPoints(skillType) > 0){
					setComponentKnown(id);
					decrementSpellPoints(skillType);
					learned = true;
				}
				updateFlags |= KNOWN_COMPONENT_UPDATE;
				break;
			case 2:
				if (!isModifierKnown(id) && getSpellPoints(skillType) > 0){
					setModifierKnown(id);
					decrementSpellPoints(skillType);
					learned = true;
				}
				updateFlags |= KNOWN_MODIFIER_UPDATE;
				break;
			case 3:
				if (!isTalentKnown(id) && getSpellPoints(skillType) > 0){
					setTalentKnown(id);
					decrementSpellPoints(skillType);
					learned = true;
				}
				updateFlags |= KNOWN_TALENTS_UPDATE;
				break;
			}

			if (entity instanceof EntityPlayerMP && learned && skillType == SkillPointTypes.SILVER){
				AMNetHandler.INSTANCE.sendSilverSkillPointPacket((EntityPlayerMP)entity);
			}
		}
	}

	@Override
	public Integer[] getKnownShapes(){
		if (this.entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode){
			ArrayList<Integer> allKnownShapes = SkillManager.instance.getAllShapes();
			return allKnownShapes.toArray(new Integer[allKnownShapes.size()]);
		}
		return shapesKnown.toArray(new Integer[shapesKnown.size()]);
	}

	@Override
	public Integer[] getKnownComponents(){
		if (this.entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode){
			ArrayList<Integer> allKnownComponents = SkillManager.instance.getAllComponents();
			return allKnownComponents.toArray(new Integer[allKnownComponents.size()]);
		}
		return componentsKnown.toArray(new Integer[componentsKnown.size()]);
	}

	@Override
	public Integer[] getKnownModifiers(){
		if (this.entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode){
			ArrayList<Integer> allKnownModifiers = SkillManager.instance.getAllModifiers();
			return allKnownModifiers.toArray(new Integer[allKnownModifiers.size()]);
		}
		return modifiersKnown.toArray(new Integer[modifiersKnown.size()]);
	}

	@Override
	public Integer[] getKnownTalents(){
		if (this.entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode){
			ArrayList<Integer> allKnownTalents = SkillManager.instance.getAllTalents();
			return allKnownTalents.toArray(new Integer[allKnownTalents.size()]);
		}
		return talentsKnown.toArray(new Integer[talentsKnown.size()]);
	}

	public boolean handlePacketData(byte[] data){
		try{
			AMDataReader rdr = new AMDataReader(data, false);
			byte subID = rdr.getByte();
			int entityID = rdr.getInt();
			switch (subID){
			case SPELL_LEARNED:
				int id = rdr.getInt();
				int type = rdr.getInt();
				learn(id, type);
				if (!entity.worldObj.isRemote)
					forceSync();
				break;
			case CLIENT_SYNC:
				int flags = rdr.getInt();

				if (rdr.getBoolean()){
					this.primaryTree = SkillTrees.values()[rdr.getInt()];
				}

				if ((flags & KNOWN_SHAPE_UPDATE) == KNOWN_SHAPE_UPDATE){
					shapesKnown.clear();
					int numShapes = rdr.getInt();
					for (int i = 0; i < numShapes; ++i){
						setShapeKnown(rdr.getInt());
					}
				}

				if ((flags & KNOWN_COMPONENT_UPDATE) == KNOWN_COMPONENT_UPDATE){
					componentsKnown.clear();
					int numComponents = rdr.getInt();
					for (int i = 0; i < numComponents; ++i){
						setComponentKnown(rdr.getInt());
					}
				}

				if ((flags & KNOWN_MODIFIER_UPDATE) == KNOWN_MODIFIER_UPDATE){
					modifiersKnown.clear();
					int numModifiers = rdr.getInt();
					for (int i = 0; i < numModifiers; ++i){
						setModifierKnown(rdr.getInt());
					}
				}

				if ((flags & KNOWN_TALENTS_UPDATE) == KNOWN_TALENTS_UPDATE){
					talentsKnown.clear();
					int numTalents = rdr.getInt();
					for (int i = 0; i < numTalents; ++i){
						setTalentKnown(rdr.getInt());
					}
				}

				if ((flags & NUM_SPELL_POINTS) == NUM_SPELL_POINTS){
					setSpellPoints(rdr.getInt(), rdr.getInt(), rdr.getInt());
				}
				break;
			}
		}catch (Throwable t){
			t.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean HasDoneFullSync(){
		return this.hasDoneFullSync;
	}

	public void setFullSync(){
		this.ticksToSync = 0;
		this.updateFlags = this.updateFlags | KNOWN_SHAPE_UPDATE | KNOWN_COMPONENT_UPDATE | KNOWN_MODIFIER_UPDATE | KNOWN_TALENTS_UPDATE | NUM_SPELL_POINTS;
		this.hasDoneFullSync = true;
		this.forcingSync = true;
	}

	public void setDelayedSync(int delay){
		setFullSync();
		this.ticksToSync = delay;
	}

	@Override
	public void forceSync(){
		this.forcingSync = true;
		this.ticksToSync = 0;
	}

	public boolean hasUpdate(){
		if (!(this.entity instanceof EntityPlayer) && !forcingSync){
			return false;
		}
		this.ticksToSync--;
		if (this.ticksToSync <= 0) this.ticksToSync = this.syncTickDelay;
		return (this.updateFlags != 0 || this.forcingSync) && this.ticksToSync == this.syncTickDelay;
	}

	private byte[] getUpdateData(){
		AMDataWriter writer = new AMDataWriter();

		writer.add(CLIENT_SYNC);
		writer.add(this.entity.getEntityId());
		writer.add(updateFlags);

		writer.add(this.primaryTree != null);
		if (this.primaryTree != null) writer.add(this.primaryTree.ordinal());

		if ((updateFlags & KNOWN_SHAPE_UPDATE) == KNOWN_SHAPE_UPDATE){
			writer.add(shapesKnown.size());
			for (Integer i : shapesKnown){
				writer.add(i);
			}
		}

		if ((updateFlags & KNOWN_COMPONENT_UPDATE) == KNOWN_COMPONENT_UPDATE){
			writer.add(componentsKnown.size());
			for (Integer i : componentsKnown){
				writer.add(i);
			}
		}

		if ((updateFlags & KNOWN_MODIFIER_UPDATE) == KNOWN_MODIFIER_UPDATE){
			writer.add(modifiersKnown.size());
			for (Integer i : modifiersKnown){
				writer.add(i);
			}
		}

		if ((updateFlags & KNOWN_TALENTS_UPDATE) == KNOWN_TALENTS_UPDATE){
			writer.add(talentsKnown.size());
			for (Integer i : talentsKnown){
				writer.add(i);
			}
		}

		if ((updateFlags & NUM_SPELL_POINTS) == NUM_SPELL_POINTS){
			writer.add(this.spellPoints[0]);
			writer.add(this.spellPoints[1]);
			writer.add(this.spellPoints[2]);
			writer.add(this.spellPoints[3]);
		}

		this.updateFlags = 0;
		this.forcingSync = false;

		return writer.generate();
	}

	public void handleExtendedPropertySync(){
		if (!this.HasDoneFullSync()) this.setFullSync();

		if (!entity.worldObj.isRemote && this.hasUpdate()){
			byte[] data = this.getUpdateData();
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(entity.dimension, entity.posX, entity.posY, entity.posZ, 32, AMPacketIDs.SYNC_SPELL_KNOWLEDGE, data);
		}
	}

	private int[] arrayListToIntArray(ArrayList<Integer> source){
		int[] newArr = new int[source.size()];
		int count = 0;
		for (Integer i : source){
			if (i == null) newArr[count++] = 0;
			else newArr[count++] = i;
		}
		return newArr;
	}

	private void addAllIntsToArrayList(ArrayList<Integer> list, int[] array){
		if (array == null) return;
		for (int i : array){
			list.add(i);
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound){
		NBTTagCompound spellKnowledgeData = new NBTTagCompound();
		spellKnowledgeData.setIntArray("KnownShapes", arrayListToIntArray(shapesKnown));
		spellKnowledgeData.setIntArray("KnownComponents", arrayListToIntArray(componentsKnown));
		spellKnowledgeData.setIntArray("KnownModifiers", arrayListToIntArray(modifiersKnown));
		spellKnowledgeData.setIntArray("KnownTalents", arrayListToIntArray(talentsKnown));
		spellKnowledgeData.setIntArray("SpellPoints", spellPoints);
		spellKnowledgeData.setInteger("PrimarySkillTree", primaryTree != null ? primaryTree.ordinal() : -1);
		compound.setTag("SpellKnowledge", spellKnowledgeData);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound){
		shapesKnown = new ArrayList<Integer>();
		componentsKnown = new ArrayList<Integer>();
		modifiersKnown = new ArrayList<Integer>();
		talentsKnown = new ArrayList<Integer>();
		NBTTagCompound spellKnowledgeData = compound.getCompoundTag("SpellKnowledge");
		if (spellKnowledgeData != null){
			addAllIntsToArrayList(shapesKnown, spellKnowledgeData.getIntArray("KnownShapes"));
			addAllIntsToArrayList(componentsKnown, spellKnowledgeData.getIntArray("KnownComponents"));
			addAllIntsToArrayList(modifiersKnown, spellKnowledgeData.getIntArray("KnownModifiers"));
			addAllIntsToArrayList(talentsKnown, spellKnowledgeData.getIntArray("KnownTalents"));
			spellPoints = spellKnowledgeData.getIntArray("SpellPoints");

			if (spellPoints.length != 4){
				spellPoints = new int[]{3, 0, 0, 0};
			}

			int ordinal = spellKnowledgeData.getInteger("PrimarySkillTree");
			if (ordinal > -1){
				this.primaryTree = SkillTrees.values()[ordinal];
			}
		}
	}

	@Override
	public void init(Entity entity, World world){
		this.entity = entity;
	}

	@Override
	public void clearAllKnowledge(){
		shapesKnown.clear();
		componentsKnown.clear();
		modifiersKnown.clear();
		talentsKnown.clear();
		this.primaryTree = SkillTrees.None;
		this.spellPoints = new int[]{0, 0, 0, 0};
		this.setFullSync();
	}

	private boolean RecursivePrerequisiteCheck(SkillData sk, SkillTreeEntry entry){
		for (SkillTreeEntry prereq : entry.prerequisites){
			if (prereq.enabled){
				if (!sk.isEntryKnown(prereq)){
					return false;
				}
			}else{
				if (!RecursivePrerequisiteCheck(sk, prereq)){
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public LearnStates getLearnState(SkillTreeEntry entry, EntityPlayer player){
		SkillData sk = SkillData.For(player);

		if (sk.isEntryKnown(entry)){
			return LearnStates.LEARNED;
		}

		SkillPointTypes pointType = SkillTreeManager.instance.getSkillPointTypeForPart(entry.registeredItem);
		LearnStates state = entry.enabled ? (sk.getSpellPoints(pointType) > 0 ? LearnStates.CAN_LEARN : LearnStates.CANNOT_LEARN) : LearnStates.DISABLED;
		if (state == LearnStates.CAN_LEARN){
			if (!RecursivePrerequisiteCheck(sk, entry)){
				state = LearnStates.CANNOT_LEARN;
			}
		}

		if (sk.getPrimaryTree() != SkillTrees.None && entry.tree != SkillTrees.Talents && sk.getPrimaryTree() != entry.tree && entry.tier >= AMCore.config.getSkillTreeSecondaryTierCap()){
			state = LearnStates.LOCKED;
		}

		return state;
	}

	public void respec(){
		LogHelper.info("Respeccing %s", player.getName());

		int[] addPoints = new int[4];
		addPoints[0] = this.spellPoints[0];
		addPoints[1] = this.spellPoints[1];
		addPoints[2] = this.spellPoints[2];
		addPoints[3] = this.spellPoints[3]; //placeholder for silver points; these are not refunded.

		for (Integer i : shapesKnown){
			SkillPointTypes type = SkillTreeManager.instance.getSkillPointTypeForPart(SkillManager.instance.getSkill(i));
			addPoints[type.ordinal()]++;
		}

		for (Integer i : componentsKnown){
			SkillPointTypes type = SkillTreeManager.instance.getSkillPointTypeForPart(SkillManager.instance.getSkill(i));
			addPoints[type.ordinal()]++;
		}

		for (Integer i : modifiersKnown){
			SkillPointTypes type = SkillTreeManager.instance.getSkillPointTypeForPart(SkillManager.instance.getSkill(i));
			addPoints[type.ordinal()]++;
		}

		for (Integer i : talentsKnown){
			SkillPointTypes type = SkillTreeManager.instance.getSkillPointTypeForPart(SkillManager.instance.getSkill(i));
			addPoints[type.ordinal()]++;
		}

		clearAllKnowledge();

		this.spellPoints[0] = addPoints[0];
		this.spellPoints[1] = addPoints[1];
		this.spellPoints[2] = addPoints[2];
	}
}
