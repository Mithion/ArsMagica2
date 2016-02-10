package am2.proxy.tick;

import am2.AMCore;
import am2.EntityItemWatcher;
import am2.MeteorSpawnHelper;
import am2.bosses.BossSpawnHelper;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.utility.DimensionUtilities;
import am2.worldgen.RetroactiveWorldgenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ServerTickHandler{

	private boolean firstTick = true;
	public static HashMap<EntityLiving, EntityLivingBase> targetsToSet = new HashMap<EntityLiving, EntityLivingBase>();

	public static String lastWorldName;

	private void gameTick_Start(){

		if (MinecraftServer.getServer().getFolderName() != lastWorldName){
			lastWorldName = MinecraftServer.getServer().getFolderName();
			firstTick = true;
		}

		if (firstTick){
			ItemsCommonProxy.crystalPhylactery.getSpawnableEntities(MinecraftServer.getServer().worldServers[0]);
			firstTick = false;
		}

		AMCore.proxy.itemFrameWatcher.checkWatchedFrames();
	}

	private void gameTick_End(){
		BossSpawnHelper.instance.tick();
		MeteorSpawnHelper.instance.tick();
		EntityItemWatcher.instance.tick();
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		if (event.phase == TickEvent.Phase.START){
			gameTick_Start();
		}else if (event.phase == TickEvent.Phase.END){
			gameTick_End();
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		if (AMCore.config.retroactiveWorldgen())
			RetroactiveWorldgenerator.instance.continueRetrogen(event.world);

		applyDeferredPotionEffects();
		if (event.phase == TickEvent.Phase.END){
			applyDeferredDimensionTransfers();
		}
	}

	private void applyDeferredPotionEffects(){
		for (EntityLivingBase ent : AMCore.proxy.getDeferredPotionEffects().keySet()){
			ArrayList<PotionEffect> potions = AMCore.proxy.getDeferredPotionEffects().get(ent);
			for (PotionEffect effect : potions)
				ent.addPotionEffect(effect);
		}

		AMCore.proxy.clearDeferredPotionEffects();
	}

	private void applyDeferredDimensionTransfers(){
		for (EntityLivingBase ent : AMCore.proxy.getDeferredDimensionTransfers().keySet()){
			DimensionUtilities.doDimensionTransfer(ent, AMCore.proxy.getDeferredDimensionTransfers().get(ent));
		}

		AMCore.proxy.clearDeferredDimensionTransfers();
	}

	private void applyDeferredTargetSets(){
		Iterator<Entry<EntityLiving, EntityLivingBase>> it = targetsToSet.entrySet().iterator();
		while (it.hasNext()){
			Entry<EntityLiving, EntityLivingBase> entry = it.next();
			if (entry.getKey() != null && !entry.getKey().isDead)
				entry.getKey().setAttackTarget(entry.getValue());
			it.remove();
		}
	}

	public void addDeferredTarget(EntityLiving ent, EntityLivingBase target){
		targetsToSet.put(ent, target);
	}

	public void blackoutArmorPiece(EntityPlayerMP player, int slot, int cooldown){
		AMNetHandler.INSTANCE.sendPacketToClientPlayer(player, AMPacketIDs.FLASH_ARMOR_PIECE, new AMDataWriter().add(slot).add(cooldown).generate());
	}

}
