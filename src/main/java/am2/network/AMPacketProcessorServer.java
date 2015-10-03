package am2.network;

import am2.AMCore;
import am2.LogHelper;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.blocks.tileentities.TileEntityArmorImbuer;
import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.blocks.tileentities.TileEntityMagiciansWorkbench;
import am2.blocks.tileentities.TileEntityParticleEmitter;
import am2.containers.ContainerMagiciansWorkbench;
import am2.containers.ContainerSpellCustomization;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ContainerKeystone;
import am2.items.ItemSpellBook;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.power.PowerNodeRegistry;
import am2.spell.SpellUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class AMPacketProcessorServer{

	@SubscribeEvent
	public void onServerPacketData(ServerCustomPacketEvent event){
		ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
		byte packetID = -1;
		try{
			if (event.packet.getTarget() != Side.SERVER){
				return;
			}

			//constant details all packets share:  ID, player, and remaining data
			packetID = bbis.readByte();
			NetHandlerPlayServer srv = (NetHandlerPlayServer)event.packet.handler();
			EntityPlayerMP player = srv.playerEntity;
			byte[] remaining = new byte[bbis.available()];
			bbis.readFully(remaining);

			switch (packetID){
			case AMPacketIDs.SPELL_SHAPE_GROUP_CHANGE:
				handleCastingModeChange(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.MAGIC_LEVEL_UP:
				handleMagicLevelUp(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SYNC_BETA_PARTICLES:
				handleSyncBetaParticles(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.POSSIBLE_CLIENT_EXPROP_DESYNC:
				handlePossibleClientExpropDesync(remaining);
				break;
			case AMPacketIDs.REQUEST_BETA_PARTICLES:
				handleRequestBetaParticles(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SPELL_CUSTOMIZE:
				handleSpellCustomize(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SPELLBOOK_CHANGE_ACTIVE_SLOT:
				handleSpellBookChangeActiveSlot(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SYNC_SPELL_KNOWLEDGE:
				handleSyncSpellKnowledge(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.DECO_BLOCK_UPDATE:
				handleDecoBlockUpdate(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.INSCRIPTION_TABLE_UPDATE:
				handleInscriptionTableUpdate(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.TK_DISTANCE_SYNC:
				ExtendedProperties.For((EntityPlayerMP)player).TK_Distance = new AMDataReader(remaining).getFloat();
				break;
			case AMPacketIDs.SAVE_KEYSTONE_COMBO:
				handleSaveKeystoneCombo(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SET_KEYSTONE_COMBO:
				handleSetKeystoneCombo(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SET_MAG_WORK_REC:
				handleSetMagiciansWorkbenchRecipe(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.RUNE_BAG_GUI_OPEN:
				handleRuneBagGUIOpen(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.M_BENCH_LOCK_RECIPE:
				handleMBenchLockRecipe(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.IMBUE_ARMOR:
				handleImbueArmor(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.REQUEST_PWR_PATHS:
				handlePowerPathSync(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.SYNC_EXTENDED_PROPS:
				handleExpropOperation(remaining, (EntityPlayerMP)player);
				break;
			case AMPacketIDs.AFFINITY_ACTIVATE:
				handleAffinityActivate(remaining, player);
				break;
			}
		}catch (Throwable t){
			LogHelper.error("Server Packet Failed to Handle!");
			LogHelper.error("Packet Type: " + packetID);
			t.printStackTrace();
		}finally{
			try{
				if (bbis != null)
					bbis.close();
			}catch (Throwable t){
				t.printStackTrace();
			}
		}
	}

	private void handleAffinityActivate(byte[] data, EntityPlayerMP entity){
		AffinityData.For(entity).onAffinityAbility();
	}

	private void handleExpropOperation(byte[] data, EntityPlayerMP player){
		ExtendedProperties.For(player).performRemoteOp(new AMDataReader(data, false).getInt());
	}

	private void handlePowerPathSync(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		byte nom = rdr.getByte();
		if (nom == 1){
			AMVector3 loc = new AMVector3(rdr.getFloat(), rdr.getFloat(), rdr.getFloat());
			TileEntity te = player.worldObj.getTileEntity((int)loc.x, (int)loc.y, (int)loc.z);
			if (te != null && te instanceof IPowerNode){
				AMNetHandler.INSTANCE.sendPowerResponseToClient(PowerNodeRegistry.For(player.worldObj).getDataCompoundForNode((IPowerNode)te), player, te);
			}
		}
	}

	private void handleImbueArmor(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		TileEntity te = player.worldObj.getTileEntity(rdr.getInt(), rdr.getInt(), rdr.getInt());
		if (te != null && te instanceof TileEntityArmorImbuer){
			((TileEntityArmorImbuer)te).imbueCurrentArmor(rdr.getString());
		}
	}

	private void handleMBenchLockRecipe(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		int x = rdr.getInt();
		int y = rdr.getInt();
		int z = rdr.getInt();

		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityMagiciansWorkbench){
			((TileEntityMagiciansWorkbench)te).setRecipeLocked(rdr.getInt(), rdr.getBoolean());
			te.getWorld().markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
		}
	}

	private void handleRuneBagGUIOpen(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		int entityID = rdr.getInt();

		if (player == null){
			return;
		}

		//open the GUI
		player.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_RUNE_BAG, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
	}

	private void handleSetMagiciansWorkbenchRecipe(byte[] data, EntityPlayerMP player){
		if (player.openContainer != null && player.openContainer instanceof ContainerMagiciansWorkbench){
			((ContainerMagiciansWorkbench)player.openContainer).moveRecipeToCraftingGrid(new AMDataReader(data, false).getInt());
		}
	}

	private void handleSetKeystoneCombo(byte[] data, EntityPlayerMP player){
		if (player.openContainer instanceof ContainerKeystone){
			AMDataReader rdr = new AMDataReader(data, false);
			((ContainerKeystone)player.openContainer).setInventoryToCombination(rdr.getInt());
		}
	}

	private void handleSaveKeystoneCombo(byte[] data, EntityPlayerMP player){
		if (player.openContainer instanceof ContainerKeystone){
			AMDataReader rdr = new AMDataReader(data, false);
			boolean add = rdr.getBoolean();
			String name = rdr.getString();
			int[] metas = new int[]{rdr.getInt(), rdr.getInt(), rdr.getInt()};
			if (add)
				ItemsCommonProxy.keystone.addCombination(((ContainerKeystone)player.openContainer).getKeystoneStack(), name, metas);
			else
				ItemsCommonProxy.keystone.removeCombination(((ContainerKeystone)player.openContainer).getKeystoneStack(), name);
		}
	}

	private void handleInscriptionTableUpdate(byte[] data, EntityPlayerMP player){
		World world = player.worldObj;
		AMDataReader rdr = new AMDataReader(data, false);
		TileEntity te = world.getTileEntity(rdr.getInt(), rdr.getInt(), rdr.getInt());
		if (te == null || !(te instanceof TileEntityInscriptionTable)) return;
		((TileEntityInscriptionTable)te).HandleUpdatePacket(rdr.getRemainingBytes());

		world.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
	}

	private void handleDecoBlockUpdate(byte[] data, EntityPlayerMP player){
		World world = player.worldObj;
		AMDataReader rdr = new AMDataReader(data, false);
		TileEntity te = world.getTileEntity(rdr.getInt(), rdr.getInt(), rdr.getInt());
		if (te == null || !(te instanceof TileEntityParticleEmitter)) return;
		((TileEntityParticleEmitter)te).readFromNBT(rdr.getNBTTagCompound());

		world.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
	}

	private void handleSyncSpellKnowledge(byte[] data, EntityPlayerMP player){
		SkillData.For(player).handlePacketData(data);
	}

	private void handleSpellBookChangeActiveSlot(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		byte subID = rdr.getByte();
		int entityID = rdr.getInt();
		int inventorySlot = rdr.getInt();

		ItemStack stack = player.inventory.getStackInSlot(inventorySlot);
		if (stack == null || !(stack.getItem() instanceof ItemSpellBook)) return;

		int newIndex = 0;

		if (subID == ItemSpellBook.ID_NEXT_SPELL)
			newIndex = ItemsCommonProxy.spellBook.SetNextSlot(stack);
		else if (subID == ItemSpellBook.ID_PREV_SPELL)
			newIndex = ItemsCommonProxy.spellBook.SetPrevSlot(stack);
		else
			return;
	}

	private void handleSpellCustomize(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		int entityID = rdr.getInt();

		EntityLivingBase ent = getEntityByID(entityID);

		if (player == null){
			return;
		}

		int IIconIndex = rdr.getInt();
		String name = rdr.getString();

		if (player.openContainer instanceof ContainerSpellCustomization){
			((ContainerSpellCustomization)player.openContainer).setNameAndIndex(name, IIconIndex);
		}
	}

	private void handleRequestBetaParticles(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		int requesterID = rdr.getInt();
		int entityID = rdr.getInt();
		EntityLivingBase entity = getEntityByID(entityID);

		if (player == null || entity == null || !(entity instanceof EntityPlayer)) return;

		if (!AMCore.proxy.playerTracker.hasAA((EntityPlayer)entity)) return;

		byte[] expropData = ExtendedProperties.For(entity).getAuraData();

		AMDataWriter writer = new AMDataWriter();
		writer.add(entity.getEntityId());
		writer.add(expropData);

		AMNetHandler.INSTANCE.sendPacketToClientPlayer(player, AMPacketIDs.REQUEST_BETA_PARTICLES, writer.generate());
	}

	private void handlePossibleClientExpropDesync(byte[] data){
		AMDataReader rdr = new AMDataReader(data, false);
		int entityID = rdr.getInt();

		EntityLivingBase e = getEntityByID(entityID);

		if (e != null && e instanceof EntityPlayer){
			ExtendedProperties props = ExtendedProperties.For(e);
			if (!props.detectPossibleDesync()){
				props.setFullSync();
				props.forceSync();
			}
		}
	}

	private void handleSyncBetaParticles(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);

		if (player == null || !AMCore.proxy.playerTracker.hasAA(player)){
			return;
		}

		int index = rdr.getInt();
		int behaviour = rdr.getInt();
		float scale = rdr.getFloat();
		float alpha = rdr.getFloat();
		boolean randomColor = rdr.getBoolean();
		boolean defaultColor = rdr.getBoolean();
		int color = rdr.getInt();
		int delay = rdr.getInt();
		int quantity = rdr.getInt();
		float speed = rdr.getFloat();

		ExtendedProperties.For(player).updateAuraData(index, behaviour, scale, alpha, randomColor, defaultColor, color, delay, quantity, speed);
	}

	private void handleCastingModeChange(byte[] data, EntityPlayerMP player){
		AMDataReader rdr = new AMDataReader(data, false);
		int newShapeGroupOrdinal = rdr.getInt();
		int index = rdr.getInt();

		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null){
			if (stack.getItem() == ItemsCommonProxy.spell){
				SpellUtils.instance.setShapeGroup(stack, newShapeGroupOrdinal);
			}else if (stack.getItem() == ItemsCommonProxy.spellBook || stack.getItem() == ItemsCommonProxy.arcaneSpellbook){
				ItemStack spellStack = ((ItemSpellBook)stack.getItem()).GetActiveItemStack(stack);
				SpellUtils.instance.setShapeGroup(spellStack, newShapeGroupOrdinal);
				((ItemSpellBook)stack.getItem()).replaceAciveItemStack(stack, spellStack);
			}
		}
	}

	private void handleMagicLevelUp(byte[] data, EntityPlayerMP player){
		/*AMDataReader reader = new AMDataReader(data, false);
		int entityID = reader.getInt();
		Entity ent = getEntityByID(entityID);

		if (ent == null || !(ent instanceof EntityLiving)) return;

		if (AMCore.proxy.IncreaseEntityMagicLevel((EntityLiving)ent, ent.worldObj)){
			if (ent instanceof EntityPlayerMP){
				EntityPlayerMP player = (EntityPlayerMP)ent;
				AMDataWriter writer = new AMDataWriter();
				writer.add(true);
				writer.add(player.experienceLevel);
				Packet pkt = createArsMagicaClientPacket(AMPacketIDs.MAGIC_LEVEL_UP, writer.generate());
				PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
			}
		}*/

		//TODO
	}

	public WorldServer[] getWorldServers(){
		return FMLServerHandler.instance().getServer().worldServers;
	}

	public EntityLivingBase getEntityByID(int entityID){
		return AMCore.proxy.getEntityByID(entityID);
	}
}
