package am2;

import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.enchantments.AMEnchantments;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.RiftStorage;
import am2.playerextensions.SkillData;
import am2.proxy.tick.ServerTickHandler;
import am2.spell.SkillTreeManager;
import am2.utility.EntityUtilities;
import am2.utility.WebRequestUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class PlayerTracker{

	public static HashMap<UUID, NBTTagCompound> storedExtProps_death;
	public static HashMap<UUID, NBTTagCompound> riftStorage_death;
	public static HashMap<UUID, NBTTagCompound> affinityStorage_death;
	public static HashMap<UUID, NBTTagCompound> spellKnowledgeStorage_death;

	public static HashMap<UUID, NBTTagCompound> storedExtProps_dimension;
	public static HashMap<UUID, NBTTagCompound> riftStorage_dimension;
	public static HashMap<UUID, NBTTagCompound> affinityStorage_dimension;
	public static HashMap<UUID, NBTTagCompound> spellKnowledgeStorage_dimension;

	public static HashMap<UUID, HashMap<Integer, ItemStack>> soulbound_Storage;

	private TreeMap<String, Integer> aals;
	private TreeMap<String, String> clls;
	private TreeMap<String, Integer> cldm;

	public PlayerTracker(){
		storedExtProps_death = new HashMap<UUID, NBTTagCompound>();
		storedExtProps_dimension = new HashMap<UUID, NBTTagCompound>();
		affinityStorage_death = new HashMap<UUID, NBTTagCompound>();
		spellKnowledgeStorage_death = new HashMap<UUID, NBTTagCompound>();

		riftStorage_death = new HashMap<UUID, NBTTagCompound>();
		riftStorage_dimension = new HashMap<UUID, NBTTagCompound>();
		affinityStorage_dimension = new HashMap<UUID, NBTTagCompound>();
		spellKnowledgeStorage_dimension = new HashMap<UUID, NBTTagCompound>();

		soulbound_Storage = new HashMap<UUID, HashMap<Integer, ItemStack>>();

		aals = new TreeMap<String, Integer>();
		clls = new TreeMap<String, String>();
		cldm = new TreeMap<String, Integer>();
	}

	public void postInit(){
		populateAALList();
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event){
		if (hasAA(event.player)){
			AMNetHandler.INSTANCE.requestClientAuras((EntityPlayerMP)event.player);
		}

		int[] disabledSkills = SkillTreeManager.instance.getDisabledSkillIDs();

		AMDataWriter writer = new AMDataWriter();
		writer.add(AMCore.config.getSkillTreeSecondaryTierCap()).add(disabledSkills);
		writer.add(AMCore.config.getManaCap());
		byte[] data = writer.generate();

		AMNetHandler.INSTANCE.syncLoginData((EntityPlayerMP)event.player, data);
		if (ServerTickHandler.lastWorldName != null)
			AMNetHandler.INSTANCE.syncWorldName((EntityPlayerMP)event.player, ServerTickHandler.lastWorldName);
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event){
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
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event){
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
	public void onPlayerRespawn(PlayerRespawnEvent event){
		//extended properties
		//================================================================================
		if (storedExtProps_death.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = storedExtProps_death.get(event.player.getUniqueID());
			storedExtProps_death.remove(event.player.getUniqueID());

			ExtendedProperties.For(event.player).loadNBTData(stored);
			ExtendedProperties.For(event.player).setDelayedSync(40);
		}else if (storedExtProps_dimension.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = storedExtProps_dimension.get(event.player.getUniqueID());
			storedExtProps_dimension.remove(event.player.getUniqueID());

			ExtendedProperties.For(event.player).loadNBTData(stored);
			ExtendedProperties.For(event.player).setDelayedSync(40);
		}
		//================================================================================
		//rift storage
		//================================================================================
		if (riftStorage_death.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = riftStorage_death.get(event.player.getUniqueID());
			riftStorage_death.remove(event.player.getUniqueID());

			RiftStorage.For(event.player).loadNBTData(stored);
		}else if (riftStorage_dimension.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = riftStorage_dimension.get(event.player.getUniqueID());
			riftStorage_dimension.remove(event.player.getUniqueID());

			RiftStorage.For(event.player).loadNBTData(stored);
		}
		//================================================================================
		//affinity data
		//================================================================================
		if (affinityStorage_death.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = affinityStorage_death.get(event.player.getUniqueID());
			affinityStorage_death.remove(event.player.getUniqueID());

			AffinityData.For(event.player).loadNBTData(stored);
		}else if (affinityStorage_dimension.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = affinityStorage_dimension.get(event.player.getUniqueID());
			affinityStorage_dimension.remove(event.player.getUniqueID());

			AffinityData.For(event.player).loadNBTData(stored);
		}
		//================================================================================
		//spell knowledge data
		//================================================================================
		if (spellKnowledgeStorage_death.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = spellKnowledgeStorage_death.get(event.player.getUniqueID());
			spellKnowledgeStorage_death.remove(event.player.getUniqueID());

			SkillData.For(event.player).loadNBTData(stored);
		}else if (spellKnowledgeStorage_dimension.containsKey(event.player.getUniqueID())){
			NBTTagCompound stored = spellKnowledgeStorage_dimension.get(event.player.getUniqueID());
			spellKnowledgeStorage_dimension.remove(event.player.getUniqueID());

			SkillData.For(event.player).loadNBTData(stored);
		}
		//================================================================================
		//soulbound items
		//================================================================================
		if (soulbound_Storage.containsKey(event.player.getUniqueID())){
			HashMap<Integer, ItemStack> soulboundItems = soulbound_Storage.get(event.player.getUniqueID());
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
		if (storedExtProps_death.containsKey(player.getUniqueID()))
			storedExtProps_death.remove(player.getUniqueID());

		NBTTagCompound save = new NBTTagCompound();
		ExtendedProperties.For(player).saveNBTData(save);

		storedExtProps_death.put(player.getUniqueID(), save);

		//================================================================================
		//rift storage
		//================================================================================
		if (riftStorage_death.containsKey(player.getUniqueID()))
			riftStorage_death.remove(player.getUniqueID());
		NBTTagCompound saveRift = new NBTTagCompound();
		RiftStorage.For(player).saveNBTData(saveRift);

		riftStorage_death.put(player.getUniqueID(), saveRift);

		//================================================================================
		//affinity storage
		//================================================================================
		if (affinityStorage_death.containsKey(player.getUniqueID()))
			affinityStorage_death.remove(player.getUniqueID());
		NBTTagCompound saveAffinity = new NBTTagCompound();
		AffinityData.For(player).saveNBTData(saveAffinity);

		affinityStorage_death.put(player.getUniqueID(), saveAffinity);
		//================================================================================
		//affinity storage
		//================================================================================
		if (spellKnowledgeStorage_death.containsKey(player.getUniqueID()))
			spellKnowledgeStorage_death.remove(player.getUniqueID());
		NBTTagCompound saveSpellKnowledge = new NBTTagCompound();
		SkillData.For(player).saveNBTData(saveSpellKnowledge);

		spellKnowledgeStorage_death.put(player.getUniqueID(), saveSpellKnowledge);
		//================================================================================
	}

	public static void storeSoulboundItemsForRespawn(EntityPlayer player){
		if (soulbound_Storage.containsKey(player.getUniqueID()))
			soulbound_Storage.remove(player.getUniqueID());

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

		soulbound_Storage.put(player.getUniqueID(), soulboundItems);
	}

	public static void storeExtendedPropertiesForDimensionChange(EntityPlayer player){
		//extended properties
		//================================================================================
		if (!storedExtProps_death.containsKey(player.getUniqueID())){
			if (storedExtProps_dimension.containsKey(player.getUniqueID()))
				storedExtProps_dimension.remove(player.getUniqueID());

			NBTTagCompound saveExprop = new NBTTagCompound();
			ExtendedProperties.For(player).saveNBTData(saveExprop);

			storedExtProps_dimension.put(player.getUniqueID(), saveExprop);
		}
		//================================================================================
		//rift storage
		//================================================================================
		if (!riftStorage_death.containsKey(player.getUniqueID())){
			if (riftStorage_dimension.containsKey(player.getUniqueID()))
				riftStorage_dimension.remove(player.getUniqueID());

			NBTTagCompound saveRift = new NBTTagCompound();
			RiftStorage.For(player).saveNBTData(saveRift);

			riftStorage_dimension.put(player.getUniqueID(), saveRift);
		}
		//================================================================================
		//affinity storage
		//================================================================================
		if (!affinityStorage_death.containsKey(player.getUniqueID())){
			if (affinityStorage_dimension.containsKey(player.getUniqueID()))
				affinityStorage_dimension.remove(player.getUniqueID());

			NBTTagCompound saveAffinity = new NBTTagCompound();
			AffinityData.For(player).saveNBTData(saveAffinity);

			affinityStorage_dimension.put(player.getUniqueID(), saveAffinity);
		}
		//================================================================================
		//spell knowledge storage
		//================================================================================
		if (!spellKnowledgeStorage_death.containsKey(player.getUniqueID())){
			if (spellKnowledgeStorage_dimension.containsKey(player.getUniqueID()))
				spellKnowledgeStorage_dimension.remove(player.getUniqueID());

			NBTTagCompound spellKnowledge = new NBTTagCompound();
			SkillData.For(player).saveNBTData(spellKnowledge);

			spellKnowledgeStorage_dimension.put(player.getUniqueID(), spellKnowledge);
		}
		//================================================================================
	}

	public static void storeSoulboundItemForRespawn(EntityPlayer player, ItemStack stack){
		if (!soulbound_Storage.containsKey(player.getUniqueID()))
			return;

		HashMap<Integer, ItemStack> soulboundItems = soulbound_Storage.get(player.getUniqueID());

		int slotTest = 0;
		while (soulboundItems.containsKey(slotTest)){
			slotTest++;
			if (slotTest == player.inventory.mainInventory.length)
				slotTest += player.inventory.armorInventory.length;
		}

		soulboundItems.put(slotTest, stack);
	}

	public boolean hasAA(EntityPlayer entity){
		return getAAL(entity) > 0;
	}

	public int getAAL(EntityPlayer thePlayer){
		try{
			thePlayer.getDisplayName();
		}catch (Throwable t){
			return 0;
		}

		if (aals == null || clls == null)
			populateAALList();
		if (aals.containsKey(thePlayer.getDisplayName().getUnformattedText().toLowerCase()))
			return aals.get(thePlayer.getDisplayName().getUnformattedText().toLowerCase());
		return 0;
	}

	private void populateAALList(){

		aals = new TreeMap<String, Integer>();
		clls = new TreeMap<String, String>();
		cldm = new TreeMap<String, Integer>();

		String dls = "http://qorconcept.com/mc/AREW0152.txt";
		char[] dl = dls.toCharArray();
		
		
		try{
			String s = WebRequestUtils.sendPost(new String(dl), new HashMap<String, String>());
			String[] lines = s.replace("\r\n", "\n").split("\n");
			for (String line : lines){
				
				String[] split = line.split(",");
				for (int i = 1; i < split.length; ++i){
					if (split[i].equals(":AL")){
						try{
							aals.put(split[0].toLowerCase(), Integer.parseInt(split[i+1]));
						}catch(Throwable t){
							
						}
					}else if (split[i].equals(":CL")){
						try{
							clls.put(split[0].toLowerCase(), split[i+1]);
							cldm.put(split[0].toLowerCase(), Integer.parseInt(split[i+2]));
						}catch(Throwable t){
							
						}
					}
				}
			}
		}catch (Throwable t){
			//well, we tried.
		}
	}

	public String getCLF(String uuid){
		return clls.get(uuid.toLowerCase());
	}

	public boolean hasCLS(String uuid){
		return clls.containsKey(uuid.toLowerCase());
	}

	public boolean hasCLDM(String uuid){
		return cldm.containsKey(uuid.toLowerCase());
	}

	public int getCLDM(String uuid){
		return cldm.get(uuid.toLowerCase());
	}
}
