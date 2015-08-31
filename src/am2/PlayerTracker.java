package am2;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerManager;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.armor.infusions.ImbuementRegistry;
import am2.enchantments.AMEnchantments;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.RiftStorage;
import am2.playerextensions.SkillData;
import am2.proxy.tick.ServerTickHandler;
import am2.spell.SkillTreeManager;
import am2.utility.EntityUtilities;
import am2.utility.WebRequestUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class PlayerTracker{

	public static HashMap<String, NBTTagCompound> storedExtProps_death;
	public static HashMap<String, NBTTagCompound> riftStorage_death;
	public static HashMap<String, NBTTagCompound> affinityStorage_death;
	public static HashMap<String, NBTTagCompound> spellKnowledgeStorage_death;

	public static HashMap<String, NBTTagCompound> storedExtProps_dimension;
	public static HashMap<String, NBTTagCompound> riftStorage_dimension;
	public static HashMap<String, NBTTagCompound> affinityStorage_dimension;
	public static HashMap<String, NBTTagCompound> spellKnowledgeStorage_dimension;

	public static HashMap<String, HashMap<Integer, ItemStack>> soulbound_Storage;

	private TreeMap<String, Integer> aals;
	private TreeMap<String, String> clls;
	private TreeMap<String, Integer> cldm;

	public PlayerTracker(){
		storedExtProps_death = new HashMap<String, NBTTagCompound>();
		storedExtProps_dimension = new HashMap<String, NBTTagCompound>();
		affinityStorage_death = new HashMap<String, NBTTagCompound>();
		spellKnowledgeStorage_death = new HashMap<String, NBTTagCompound>();

		riftStorage_death = new HashMap<String, NBTTagCompound>();
		riftStorage_dimension = new HashMap<String, NBTTagCompound>();
		affinityStorage_dimension = new HashMap<String, NBTTagCompound>();
		spellKnowledgeStorage_dimension = new HashMap<String, NBTTagCompound>();

		soulbound_Storage = new HashMap<String, HashMap<Integer,ItemStack>>();

		aals = new TreeMap<String, Integer>();
		clls = new TreeMap<String, String>();
		cldm = new TreeMap<String, Integer>();
	}

	public void postInit(){
		populateAALList();
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if (hasAA(event.player)){
			AMNetHandler.INSTANCE.requestClientAuras((EntityPlayerMP) event.player);
		}

		int[] disabledSkills = SkillTreeManager.instance.getDisabledSkillIDs();

		AMDataWriter writer = new AMDataWriter();
		writer.add(AMCore.config.getSkillTreeSecondaryTierCap()).add(disabledSkills);
		writer.add(AMCore.config.getManaCap());
		byte[] data = writer.generate();

		AMNetHandler.INSTANCE.syncLoginData((EntityPlayerMP) event.player, data);
		if (ServerTickHandler.lastWorldName != null)
			AMNetHandler.INSTANCE.syncWorldName((EntityPlayerMP) event.player, ServerTickHandler.lastWorldName);
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		//kill any summoned creatures
		if (!event.player.worldObj.isRemote){
			List list = event.player.worldObj.loadedEntityList;
			for (Object o : list){
				if (o instanceof EntityLivingBase && EntityUtilities.isSummon((EntityLivingBase)o) && EntityUtilities.getOwner((EntityLivingBase)o) == event.player.getEntityId()){
					((EntityLivingBase)o).setDead();
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		//kill any summoned creatures, eventually respawn them in the new dimension
		if (!event.player.worldObj.isRemote){
			storeExtendedPropertiesForDimensionChange(event.player);
			List list = event.player.worldObj.loadedEntityList;
			for (Object o : list){
				if (o instanceof EntityLivingBase && EntityUtilities.isSummon((EntityLivingBase)o) && EntityUtilities.getOwner((EntityLivingBase)o) == event.player.getEntityId()){
					((EntityLivingBase)o).setDead();
				}
			}
			ExtendedProperties.For(event.player).setDelayedSync(40);
			AffinityData.For(event.player).setDelayedSync(40);
			SkillData.For(event.player).setDelayedSync(40);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		//extended properties
		//================================================================================
		if (storedExtProps_death.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = storedExtProps_death.get(event.player.getDisplayName());
			storedExtProps_death.remove(event.player.getDisplayName());

			ExtendedProperties.For(event.player).loadNBTData(stored);
			ExtendedProperties.For(event.player).setDelayedSync(40);
		}else if (storedExtProps_dimension.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = storedExtProps_dimension.get(event.player.getDisplayName());
			storedExtProps_dimension.remove(event.player.getDisplayName());

			ExtendedProperties.For(event.player).loadNBTData(stored);
			ExtendedProperties.For(event.player).setDelayedSync(40);
		}
		//================================================================================
		//rift storage
		//================================================================================
		if (riftStorage_death.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = riftStorage_death.get(event.player.getDisplayName());
			riftStorage_death.remove(event.player.getDisplayName());

			RiftStorage.For(event.player).loadNBTData(stored);
		}else if (riftStorage_dimension.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = riftStorage_dimension.get(event.player.getDisplayName());
			riftStorage_dimension.remove(event.player.getDisplayName());

			RiftStorage.For(event.player).loadNBTData(stored);
		}
		//================================================================================
		//affinity data
		//================================================================================
		if (affinityStorage_death.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = affinityStorage_death.get(event.player.getDisplayName());
			affinityStorage_death.remove(event.player.getDisplayName());

			AffinityData.For(event.player).loadNBTData(stored);
		}else if (affinityStorage_dimension.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = affinityStorage_dimension.get(event.player.getDisplayName());
			affinityStorage_dimension.remove(event.player.getDisplayName());

			AffinityData.For(event.player).loadNBTData(stored);
		}
		//================================================================================
		//spell knowledge data
		//================================================================================
		if (spellKnowledgeStorage_death.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = spellKnowledgeStorage_death.get(event.player.getDisplayName());
			spellKnowledgeStorage_death.remove(event.player.getDisplayName());

			SkillData.For(event.player).loadNBTData(stored);
		}else if (spellKnowledgeStorage_dimension.containsKey(event.player.getDisplayName())){
			NBTTagCompound stored = spellKnowledgeStorage_dimension.get(event.player.getDisplayName());
			spellKnowledgeStorage_dimension.remove(event.player.getDisplayName());

			SkillData.For(event.player).loadNBTData(stored);
		}
		//================================================================================
		//soulbound items
		//================================================================================
		if (soulbound_Storage.containsKey(event.player.getDisplayName())){
			HashMap<Integer, ItemStack> soulboundItems = soulbound_Storage.get(event.player.getDisplayName());
			for (Integer i : soulboundItems.keySet()){
				if (i < event.player.inventory.getSizeInventory())
					event.player.inventory.setInventorySlotContents(i, soulboundItems.get(i));
				else
					event.player.entityDropItem(soulboundItems.get(i), 0);
			}
		}
		//================================================================================
	}

	public void onPlayerDeath(EntityPlayer player){
		storeExtendedPropertiesForRespawn(player);
		storeSoulboundItemsForRespawn(player);
	}

	public static void storeExtendedPropertiesForRespawn(EntityPlayer player){
		//extended properties
		//================================================================================
		if (storedExtProps_death.containsKey(player.getDisplayName()))
			storedExtProps_death.remove(player.getDisplayName());

		NBTTagCompound save = new NBTTagCompound();
		ExtendedProperties.For(player).saveNBTData(save);

		storedExtProps_death.put(player.getDisplayName(), save);

		//================================================================================
		//rift storage
		//================================================================================
		if (riftStorage_death.containsKey(player.getDisplayName()))
			riftStorage_death.remove(player.getDisplayName());
		NBTTagCompound saveRift = new NBTTagCompound();
		RiftStorage.For(player).saveNBTData(saveRift);

		riftStorage_death.put(player.getDisplayName(), saveRift);

		//================================================================================
		//affinity storage
		//================================================================================
		if (affinityStorage_death.containsKey(player.getDisplayName()))
			affinityStorage_death.remove(player.getDisplayName());
		NBTTagCompound saveAffinity = new NBTTagCompound();
		AffinityData.For(player).saveNBTData(saveAffinity);

		affinityStorage_death.put(player.getDisplayName(), saveAffinity);
		//================================================================================
		//affinity storage
		//================================================================================
		if (spellKnowledgeStorage_death.containsKey(player.getDisplayName()))
			spellKnowledgeStorage_death.remove(player.getDisplayName());
		NBTTagCompound saveSpellKnowledge = new NBTTagCompound();
		SkillData.For(player).saveNBTData(saveSpellKnowledge);

		spellKnowledgeStorage_death.put(player.getDisplayName(), saveSpellKnowledge);
		//================================================================================
	}

	public static void storeSoulboundItemsForRespawn(EntityPlayer player){
		if (soulbound_Storage.containsKey(player.getDisplayName()))
			soulbound_Storage.remove(player.getDisplayName());

		HashMap<Integer, ItemStack> soulboundItems = new HashMap<Integer, ItemStack>();

		int slotCount = 0;
		for (ItemStack stack : player.inventory.mainInventory){
			int soulbound_level = EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound.effectId, stack);
			if (soulbound_level > 0){
				soulboundItems.put(slotCount, stack.copy());
				player.inventory.setInventorySlotContents(slotCount, null);
			}
			slotCount++;
		}
		slotCount = 0;
		for (ItemStack stack : player.inventory.armorInventory){
			int soulbound_level = EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound.effectId, stack);
			if (soulbound_level > 0 || ArmorHelper.isInfusionPreset(stack, GenericImbuement.soulbound)){
				soulboundItems.put(slotCount + player.inventory.mainInventory.length, stack.copy());
				player.inventory.setInventorySlotContents(slotCount + player.inventory.mainInventory.length, null);
			}
			slotCount++;
		}

		soulbound_Storage.put(player.getDisplayName(), soulboundItems);
	}

	public static void storeExtendedPropertiesForDimensionChange(EntityPlayer player){
		//extended properties
		//================================================================================
		if (!storedExtProps_death.containsKey(player.getDisplayName())){
			if (storedExtProps_dimension.containsKey(player.getDisplayName()))
				storedExtProps_dimension.remove(player.getDisplayName());

			NBTTagCompound saveExprop = new NBTTagCompound();
			ExtendedProperties.For(player).saveNBTData(saveExprop);

			storedExtProps_dimension.put(player.getDisplayName(), saveExprop);
		}
		//================================================================================
		//rift storage
		//================================================================================
		if (!riftStorage_death.containsKey(player.getDisplayName())){
			if (riftStorage_dimension.containsKey(player.getDisplayName()))
				riftStorage_dimension.remove(player.getDisplayName());

			NBTTagCompound saveRift = new NBTTagCompound();
			RiftStorage.For(player).saveNBTData(saveRift);

			riftStorage_dimension.put(player.getDisplayName(), saveRift);
		}
		//================================================================================
		//affinity storage
		//================================================================================
		if (!affinityStorage_death.containsKey(player.getDisplayName())){
			if (affinityStorage_dimension.containsKey(player.getDisplayName()))
				affinityStorage_dimension.remove(player.getDisplayName());

			NBTTagCompound saveAffinity = new NBTTagCompound();
			AffinityData.For(player).saveNBTData(saveAffinity);

			affinityStorage_dimension.put(player.getDisplayName(), saveAffinity);
		}
		//================================================================================
		//spell knowledge storage
		//================================================================================
		if (!spellKnowledgeStorage_death.containsKey(player.getDisplayName())){
			if (spellKnowledgeStorage_dimension.containsKey(player.getDisplayName()))
				spellKnowledgeStorage_dimension.remove(player.getDisplayName());

			NBTTagCompound spellKnowledge = new NBTTagCompound();
			SkillData.For(player).saveNBTData(spellKnowledge);

			spellKnowledgeStorage_dimension.put(player.getDisplayName(), spellKnowledge);
		}
		//================================================================================
	}

	public static void storeSoulboundItemForRespawn(EntityPlayer player, ItemStack stack){
		if (!soulbound_Storage.containsKey(player.getDisplayName()))
			return;

		HashMap<Integer, ItemStack> soulboundItems = soulbound_Storage.get(player.getDisplayName());

		int slotTest = 0;
		while (soulboundItems.containsKey(slotTest)){
			slotTest++;
			if (slotTest == player.inventory.mainInventory.length)
				slotTest += player.inventory.armorInventory.length;
		}

		soulboundItems.put(slotTest, stack);
	}

	public boolean hasAA(EntityPlayer entity) {
		return getAAL(entity) > 0;
	}

	public int getAAL(EntityPlayer thePlayer) {
		try{
			thePlayer.getDisplayName();
		}catch(Throwable t){
			return 0;
		}

		if (aals == null || clls == null)
			populateAALList();
		if (aals.containsKey(thePlayer.getDisplayName().toLowerCase()))
			return aals.get(thePlayer.getDisplayName().toLowerCase());
		return 0;
	}

	private void populateAALList(){

		aals = new TreeMap<String, Integer>();
		clls = new TreeMap<String, String>();
		cldm = new TreeMap<String, Integer>();

		char[] dl = new char[] {
				104,116,116,112,58,47,47,97,114,99,97,110,97,99,114,97,102,116,46,113,111,114,99,111,110,99,101,112,116,46,99,111,109,47,109,99,47,68,71,83,86,78,84,51,53,50,46,116,120,116
		};

		try{
			String s = WebRequestUtils.sendPost(new String(dl), new HashMap<String, String>());
			String[] lines = s.replace("\r\n", "\n").split("\n");
			for (String line : lines){
				if (line.startsWith(":AL")){
					String[] vals = line.replace(":AL", "").split(",");
					if (vals.length==2){
						aals.put(vals[0].toLowerCase(), Integer.parseInt(vals[1]));
					}
				}else if (line.startsWith(":CL")){
					String[] vals = line.replace(":CL", "").split(",");
					if (vals.length==3){
						clls.put(vals[0].toLowerCase(), vals[1]);
						int cdm = 0;
						try{ cdm = Integer.parseInt(vals[2]); } catch (Throwable t) { }
						cldm.put(vals[0].toLowerCase(), cdm);
					}
				}
			}
		}catch(Throwable t){
			//well, we tried.
		}
	}

	public String getCLF(String userName) {
		return clls.get(userName.toLowerCase());
	}

	public boolean hasCLS(String userName){
		return clls.containsKey(userName.toLowerCase());
	}

	public boolean hasCLDM(String userName){
		return cldm.containsKey(userName.toLowerCase());
	}

	public int getCLDM(String userName){
		return cldm.get(userName.toLowerCase());
	}
}
