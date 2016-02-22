package am2.network;

import am2.LogHelper;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.blocks.tileentities.TileEntityArmorImbuer;
import am2.blocks.tileentities.TileEntityCalefactor;
import am2.blocks.tileentities.TileEntityObelisk;
import am2.bosses.IArsMagicaBoss;
import am2.entities.EntityHecate;
import am2.power.PowerNodeRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AMNetHandler{

	private static final String ChannelLabel = "AM2DataTunnel";
	private static FMLEventChannel Channel;

	private boolean registeredChannels = false;

	private AMNetHandler(){

	}

	public static final AMNetHandler INSTANCE = new AMNetHandler();

	public void init(){
		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(ChannelLabel);
	}

	public void registerChannels(AMPacketProcessorServer proc){
		if (!registeredChannels){
			registeredChannels = true;
			Channel.register(proc);
			FMLCommonHandler.instance().bus().register(proc);
		}else{
			LogHelper.info("Redundant call to register channels.");
		}
	}

	public void sendPacketToClientPlayer(EntityPlayerMP player, byte packetID, byte[] data){

		//first byte is ID, followed by data
		byte[] pkt_data = new byte[data.length + 1];
		pkt_data[0] = packetID;

		//copy the data
		for (int i = 0; i < data.length; ++i){
			pkt_data[i + 1] = data[i];
		}

		FMLProxyPacket packet = new FMLProxyPacket(Unpooled.copiedBuffer(pkt_data), ChannelLabel);
		packet.setTarget(Side.CLIENT);
		Channel.sendTo(packet, player);
	}

	public void sendPacketToServer(byte packetID, byte[] data){
		byte[] pkt_data = new byte[data.length + 1];
		//first byte is ID
		pkt_data[0] = packetID;

		//copy the data
		for (int i = 0; i < data.length; ++i){
			pkt_data[i + 1] = data[i];
		}

		FMLProxyPacket packet = new FMLProxyPacket(Unpooled.copiedBuffer(pkt_data), ChannelLabel);
		packet.setTarget(Side.SERVER);
		Channel.sendToServer(packet);
	}

	public void sendPacketToAllClientsNear(int dimension, double ox, double oy, double oz, double radius, byte packetID, byte[] data){
		//first byte is ID, followed by data
		byte[] pkt_data = new byte[data.length + 1];
		pkt_data[0] = packetID;

		//copy the data
		for (int i = 0; i < data.length; ++i){
			pkt_data[i + 1] = data[i];
		}

		FMLProxyPacket packet = new FMLProxyPacket(Unpooled.copiedBuffer(pkt_data), ChannelLabel);
		packet.setTarget(Side.CLIENT);
		Channel.sendToAllAround(packet, new TargetPoint(dimension, ox, oy, oz, radius));
	}

	public void sendVelocityAddPacket(World world, EntityLivingBase target, double velX, double velY, double velZ){
		if (world.isRemote){
			return;
		}

		byte[] data = new AMDataWriter().add(target.getEntityId()).add(velX).add(velY).add(velZ).generate();

		sendPacketToAllClientsNear(world.provider.getDimensionId(), target.posX, target.posY, target.posZ, 50, AMPacketIDs.PLAYER_VELOCITY_ADD, data);
	}

	@SideOnly(Side.CLIENT)
	public void requestAuras(EntityPlayer player){
		AMDataWriter writer = new AMDataWriter();
		EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;
		if (localPlayer == null) return;
		writer.add(localPlayer.getEntityId());
		writer.add(player.getEntityId());

		sendPacketToServer(AMPacketIDs.REQUEST_BETA_PARTICLES, writer.generate());
	}

	public void requestClientAuras(EntityPlayerMP player){
		sendPacketToClientPlayer((EntityPlayerMP)player, AMPacketIDs.SYNC_BETA_PARTICLES, new byte[0]);
	}

	public void syncWorldName(EntityPlayerMP player, String name){
		sendPacketToClientPlayer((EntityPlayerMP)player, AMPacketIDs.SYNC_WORLD_NAME, new AMDataWriter().add(name).generate());
	}

	public void syncLoginData(EntityPlayerMP player, byte[] data){
		sendPacketToClientPlayer((EntityPlayerMP)player, AMPacketIDs.PLAYER_LOGIN_DATA, data);
	}

	public void sendSpellbookSlotChange(EntityPlayer player, int inventoryIndex, byte subID){
		sendPacketToServer(AMPacketIDs.SPELLBOOK_CHANGE_ACTIVE_SLOT,
				new AMDataWriter()
						.add(subID)
						.add(player.getEntityId())
						.add(inventoryIndex)
						.generate());
	}

	public void sendShapeGroupChangePacket(int newCastingMode, int entityid){
		byte[] packetData = new AMDataWriter().add(newCastingMode).add(entityid).generate();
		sendPacketToServer(AMPacketIDs.SPELL_SHAPE_GROUP_CHANGE, packetData);
	}

	public <T extends EntityLivingBase & IArsMagicaBoss> void sendActionUpdateToAllAround(T boss){
		if (boss.worldObj != null && !boss.worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(boss.getEntityId());
			writer.add(boss.getCurrentAction().ordinal());

			sendPacketToAllClientsNear(boss.worldObj.provider.getDimensionId(), boss.posX, boss.posY, boss.posZ, 64, AMPacketIDs.ENTITY_ACTION_UPDATE, writer.generate());
		}
	}

	public void sendStarImpactToClients(double x, double y, double z, World world, ItemStack spellStack){
		AMDataWriter writer = new AMDataWriter().add(x).add(y).add(z);
		if (spellStack != null)
			writer.add(true).add(spellStack);
		else
			writer.add(false);

		sendPacketToAllClientsNear(world.provider.getDimensionId(), x, y, z, 64, AMPacketIDs.STAR_FALL, writer.generate());
	}

	public void sendSilverSkillPointPacket(EntityPlayerMP player){
		sendPacketToClientPlayer(player, AMPacketIDs.HIDDEN_COMPONENT_UNLOCK, new byte[0]);
	}

	public void sendSpellApplyEffectToAllAround(EntityLivingBase caster, Entity target, double x, double y, double z, World world, ItemStack spellStack){
		AMDataWriter writer = new AMDataWriter().add(x).add(y).add(z);
		if (spellStack != null)
			writer.add(true).add(spellStack);
		else
			writer.add(false);
		writer.add(caster.getEntityId());
		writer.add(target.getEntityId());

		sendPacketToAllClientsNear(world.provider.getDimensionId(), x, y, z, 64, AMPacketIDs.SPELL_APPLY_EFFECT, writer.generate());
	}

	public void sendHecateDeathToAllAround(EntityHecate hecate){
		AMDataWriter writer = new AMDataWriter();
		writer.add(hecate.posX).add(hecate.posY).add(hecate.posZ);

		sendPacketToAllClientsNear(hecate.worldObj.provider.getDimensionId(), hecate.posX, hecate.posY, hecate.posZ, 32, AMPacketIDs.HECATE_DEATH, writer.generate());
	}

	public void syncPowerPaths(IPowerNode node, EntityPlayerMP player){
		AMDataWriter writer = new AMDataWriter();
		if (((TileEntity)node).getWorld().isRemote){
			writer.add((byte)0);
			writer.add(((TileEntity)node).getWorld().provider.getDimensionId());
			writer.add(((TileEntity)node).getPos().getX());
			writer.add(((TileEntity)node).getPos().getY());
			writer.add(((TileEntity)node).getPos().getZ());

			sendPacketToServer(AMPacketIDs.REQUEST_PWR_PATHS, writer.generate());
		}else{
			NBTTagCompound compound = PowerNodeRegistry.For(((TileEntity)node).getWorld()).getDataCompoundForNode(node);
			if (compound != null){
				writer.add((byte)0);
				writer.add(compound);

				sendPacketToClientPlayer(player, AMPacketIDs.REQUEST_PWR_PATHS, writer.generate());
			}
		}
	}

	public void sendImbueToServer(TileEntityArmorImbuer tileEntity, String hoveredID){
		AMDataWriter writer = new AMDataWriter();
		writer.add(tileEntity.getPos().getX());
		writer.add(tileEntity.getPos().getY());
		writer.add(tileEntity.getPos().getZ());
		writer.add(hoveredID);

		sendPacketToServer(AMPacketIDs.IMBUE_ARMOR, writer.generate());
	}

	public void sendPowerRequestToServer(AMVector3 location){
		AMDataWriter writer = new AMDataWriter();
		writer.add((byte)1);
		writer.add(location.x);
		writer.add(location.y);
		writer.add(location.z);

		sendPacketToServer(AMPacketIDs.REQUEST_PWR_PATHS, writer.generate());
	}

	public void sendPowerResponseToClient(NBTTagCompound powerData, EntityPlayerMP player, TileEntity te){
		AMDataWriter writer = new AMDataWriter();
		writer.add((byte)1);
		writer.add(powerData);
		writer.add(te.getPos().getX());
        writer.add(te.getPos().getY());
        writer.add(te.getPos().getZ());
		sendPacketToClientPlayer(player, AMPacketIDs.REQUEST_PWR_PATHS, writer.generate());
	}

	/**
	 * Sets a property remotely for capabilities
	 *
	 * @param player     The player
	 * @param capability The capability.  1 == AllowFlying, 2 == IsFlying
	 * @param state      True or False
	 */
	public void sendCapabilityChangePacket(EntityPlayerMP player, int capability, boolean state){
		AMDataWriter writer = new AMDataWriter();
		writer.add(capability);
		writer.add(state);

		sendPacketToClientPlayer(player, AMPacketIDs.CAPABILITY_CHANGE, writer.generate());
	}

	public void sendExPropCommandToServer(int flag){
		AMDataWriter writer = new AMDataWriter();
		writer.add(flag);
		sendPacketToServer(AMPacketIDs.SYNC_EXTENDED_PROPS, writer.generate());
	}

	public void sendCompendiumUnlockPacket(EntityPlayerMP player, String id, boolean isCategory){
		AMDataWriter writer = new AMDataWriter();
		writer.add(id);
		writer.add(isCategory);

		sendPacketToClientPlayer(player, AMPacketIDs.COMPENDIUM_UNLOCK, writer.generate());
	}

	public void sendCalefactorCookUpdate(TileEntityCalefactor calefactor, byte[] data){
		AMDataWriter writer = new AMDataWriter();
		writer.add(calefactor.getPos().getX());
		writer.add(calefactor.getPos().getY());
		writer.add(calefactor.getPos().getZ());
		writer.add(data);
		sendPacketToAllClientsNear(calefactor.getWorld().provider.getDimensionId(), calefactor.getPos().getX(), calefactor.getPos().getY(), calefactor.getPos().getZ(), 32, AMPacketIDs.CALEFACTOR_DATA, writer.generate());
	}

	public void sendObeliskUpdate(TileEntityObelisk obelisk, byte[] data){
		AMDataWriter writer = new AMDataWriter();
		writer.add(obelisk.getPos().getX());
		writer.add(obelisk.getPos().getY());
		writer.add(obelisk.getPos().getZ());
		writer.add(data);
		sendPacketToAllClientsNear(obelisk.getWorld().provider.getDimensionId(), obelisk.getPos().getX(), obelisk.getPos().getY(), obelisk.getPos().getZ(), 32, AMPacketIDs.OBELISK_DATA, writer.generate());
	}

	public void sendAffinityActivate(){
		sendPacketToServer(AMPacketIDs.AFFINITY_ACTIVATE, new byte[0]);
	}
}
