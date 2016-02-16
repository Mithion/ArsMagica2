package am2.proxy;

import am2.AMClientEventHandler;
import am2.AMCore;
import am2.AMKeyBindings;
import am2.api.events.RegisterCompendiumEntries;
import am2.api.events.RegisterSkillTreeIcons;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.api.spell.ItemSpellBase;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.blocks.BlocksClientProxy;
import am2.blocks.renderers.SimpleBlockRenderHandler;
import am2.blocks.renderers.TechneBlockRenderHandler;
import am2.blocks.tileentities.TileEntityParticleEmitter;
import am2.buffs.BuffList;
import am2.commands.ConfigureAMUICommand;
import am2.entities.EntityAirSled;
import am2.guis.AMGuiHelper;
import am2.guis.GuiParticleEmitter;
import am2.guis.GuiSkillTrees;
import am2.items.ItemSpellBook;
import am2.items.ItemsCommonProxy;
import am2.items.renderers.CustomItemRenderer;
import am2.items.renderers.SpellScrollRenderer;
import am2.lore.ArcaneCompendium;
import am2.lore.CompendiumUnlockHandler;
import am2.network.AMNetHandler;
import am2.network.AMPacketProcessorClient;
import am2.particles.ParticleManagerClient;
import am2.power.PowerNodeEntry;
import am2.proxy.gui.ClientGuiManager;
import am2.proxy.tick.ClientTickHandler;
import am2.spell.SpellUtils;
import am2.spell.components.Telekinesis;
import am2.texture.SpellIconManager;
import am2.utility.ProxyUtilitiesClient;
import am2.utility.RenderUtilities;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ClientProxy extends CommonProxy{
	public static SimpleBlockRenderHandler simpleBlockRenderHandler;
	public static TechneBlockRenderHandler techneBlockRenderHandler;
	private ClientTickHandler clientTickHandler;

	public static HashMap<PowerTypes, ArrayList<LinkedList<AMVector3>>> powerPathVisuals;

	public ClientProxy(){
		particleManager = new ParticleManagerClient();
	}

	@Override
	public void InitializeAndRegisterHandlers(){
		guiManager = new ClientGuiManager();
		NetworkRegistry.INSTANCE.registerGuiHandler(AMCore.instance, guiManager);
		clientTickHandler = new ClientTickHandler();
		FMLCommonHandler.instance().bus().register(clientTickHandler);
		AMNetHandler.INSTANCE.registerChannels(new AMPacketProcessorClient());

		CompendiumUnlockHandler compendiumHandler = new CompendiumUnlockHandler();
		MinecraftForge.EVENT_BUS.register(compendiumHandler);
		FMLCommonHandler.instance().bus().register(compendiumHandler);
	}

	@Override
	public void postinit(){
		super.postinit();
		ArcaneCompendium.instance.init(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage());
		MinecraftForge.EVENT_BUS.post(new RegisterCompendiumEntries(ArcaneCompendium.instance));
		MinecraftForge.EVENT_BUS.post(new RegisterSkillTreeIcons(SpellIconManager.instance));
		MinecraftForge.EVENT_BUS.register(new AMClientEventHandler());
		((ParticleManagerClient)particleManager).registerEventHandlers();

		FMLCommonHandler.instance().bus().register(new AMKeyBindings());
	}

	@Override
	public void preinit(){
		super.preinit();
		utils = new ProxyUtilitiesClient();
		blocks = new BlocksClientProxy();
		BuffList.setupTextureOverrides();
	}

	@Override
	public void init(){
		super.init();

		entities.registerRenderInformation();
		blocks.registerRenderInformation();

		simpleBlockRenderHandler = new SimpleBlockRenderHandler();
		RenderingRegistry.registerBlockHandler(blocks.commonBlockRenderID, simpleBlockRenderHandler);

		techneBlockRenderHandler = new TechneBlockRenderHandler();
		RenderingRegistry.registerBlockHandler(blocks.blockRenderID, techneBlockRenderHandler);


		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.scythe, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.magicBroom, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.arcaneSpellbook, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.winterGuardianArm, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.airGuardianLower, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.fireEars, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.waterGuardianOrbs, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.earthGuardianArmor, CustomItemRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.wardingCandle, CustomItemRenderer.instance);

		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.spell, SpellScrollRenderer.instance);
		MinecraftForgeClient.registerItemRenderer(ItemsCommonProxy.spellBook, SpellScrollRenderer.instance);

		ClientCommandHandler.instance.registerCommand(new ConfigureAMUICommand());
	}

	@Override
	public void flashManaBar(){
		AMGuiHelper.instance.flashManaBar();
	}

	@Override
	public void blackoutArmorPiece(EntityPlayerMP entity, int index, int duration){
		AMGuiHelper.instance.blackoutArmorPiece(index, duration);
	}

	@Override
	public Entity getEntityByID(World world, int ID){
		return world.getEntityByID(ID);
	}

	@Override
	public EntityLivingBase getEntityByID(int entityID){
		Entity e = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
		if (e instanceof EntityLivingBase) return (EntityLivingBase)e;
		return null;
	}

	@Override
	public EntityPlayer getLocalPlayer(){
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public WorldServer[] getWorldServers(){
		return FMLClientHandler.instance().getServer().worldServers;
	}

	@Override
	public int getArmorRenderIndex(String prefix){
		return RenderingRegistry.addNewArmourRendererPrefix(prefix);
	}

	@Override
	public void openSkillTreeUI(World world, EntityPlayer player){
		if (world.isRemote){
			Minecraft.getMinecraft().displayGuiScreen(new GuiSkillTrees(player));
		}
	}

	@Override
	public void openParticleBlockGUI(World world, EntityPlayer player, TileEntityParticleEmitter te){
		if (world.isRemote){
			Minecraft.getMinecraft().displayGuiScreen(new GuiParticleEmitter(te));
		}
	}

	@Override
	public boolean setMouseDWheel(int dwheel){
		if (dwheel == 0) return false;

		ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		if (stack == null) return false;

		boolean store = checkForTKMove(stack);
		if (!store && stack.getItem() instanceof ItemSpellBook){
			store = Minecraft.getMinecraft().thePlayer.isSneaking();
		}

		if (store){
			clientTickHandler.setDWheel(dwheel / 120, Minecraft.getMinecraft().thePlayer.inventory.currentItem, Minecraft.getMinecraft().thePlayer.isUsingItem());
			return true;
		}else{
			clientTickHandler.setDWheel(0, -1, false);
		}
		return false;
	}

	private boolean checkForTKMove(ItemStack stack){
		if (stack.getItem() instanceof ItemSpellBook){
			ItemStack activeStack = ((ItemSpellBook)stack.getItem()).GetActiveItemStack(stack);
			if (activeStack != null)
				stack = activeStack;
		}
		if (stack.getItem() instanceof ItemSpellBase && stack.hasTagCompound() && Minecraft.getMinecraft().thePlayer.isUsingItem()){
			for (ISpellComponent component : SpellUtils.instance.getComponentsForStage(stack, 0)){
				if (component instanceof Telekinesis){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void renderGameOverlay(){
		clientTickHandler.renderOverlays();
	}

	/* LOCALIZATION */
	@Override
	public String getCurrentLanguage(){
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public void addName(Object obj, String s){
		LanguageRegistry.addName(obj, s);
	}

	@Override
	public void addLocalization(String s1, String string){
		LanguageRegistry.instance().addStringLocalization(s1, string);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		if (stack.getItem() == null)
			return "";

		return stack.getItem().getItemStackDisplayName(stack);
	}

	@Override
	public void sendLocalMovementData(EntityLivingBase ent){
		if (ent == Minecraft.getMinecraft().thePlayer){
			if (ent.worldObj.isRemote && ent.ridingEntity instanceof EntityAirSled){
				EntityClientPlayerMP player = (EntityClientPlayerMP)ent;
				player.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(player.rotationYaw, player.rotationPitch, player.onGround));
				player.sendQueue.addToSendQueue(new C0CPacketInput(player.moveStrafing, player.moveForward, player.movementInput.jump, player.movementInput.sneak));
			}
		}
	}

	@Override
	public void setCompendiumSaveBase(String compendiumBase){
		ArcaneCompendium.instance.setSaveLocation(compendiumBase);
	}

	@Override
	public void requestPowerPathVisuals(IPowerNode node, EntityPlayerMP player){
		AMNetHandler.INSTANCE.syncPowerPaths(node, player);
	}

	@Override
	public void receivePowerPathVisuals(HashMap<PowerTypes, ArrayList<LinkedList<AMVector3>>> paths){
		powerPathVisuals = paths;
	}

	@Override
	public HashMap<PowerTypes, ArrayList<LinkedList<AMVector3>>> getPowerPathVisuals(){
		return powerPathVisuals;
	}

	@Override
	public boolean isClientPlayer(EntityLivingBase ent){
		return ent instanceof AbstractClientPlayer;
	}

	@Override
	public void setTrackedLocation(AMVector3 location){
		clientTickHandler.setTrackLocation(location);
	}

	@Override
	public void setTrackedPowerCompound(NBTTagCompound compound){
		clientTickHandler.setTrackData(compound);
	}

	@Override
	public boolean hasTrackedLocationSynced(){
		return clientTickHandler.getHasSynced();
	}

	@Override
	public PowerNodeEntry getTrackedData(){
		return clientTickHandler.getTrackData();
	}

	/**
	 * Proxied compendium unlocks.  Do not call directly - use the CompendiumUnlockHandler instead.
	 */
	@Override
	public void unlockCompendiumEntry(String id){
		if (ArcaneCompendium.instance.isCategory(id))
			unlockCompendiumCategory(id);
		else
			ArcaneCompendium.instance.unlockEntry(id);
	}

	/**
	 * Proxied compendium unlocks.  Do not call directly - use the CompendiumUnlockHandler instead.
	 */
	@Override
	public void unlockCompendiumCategory(String id){
		ArcaneCompendium.instance.unlockCategory(id);
	}

	@Override
	public void drawPowerOnBlockHighlight(EntityPlayer player, MovingObjectPosition target, float partialTicks){
		if (AMCore.proxy.getLocalPlayer().getCurrentArmor(3) != null &&
				(AMCore.proxy.getLocalPlayer().getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles ||
						ArmorHelper.isInfusionPreset(AMCore.proxy.getLocalPlayer().getCurrentArmor(3), GenericImbuement.magitechGoggleIntegration))
				){

			TileEntity te = player.worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);
			if (te != null && te instanceof IPowerNode){
				AMCore.proxy.setTrackedLocation(new AMVector3(target.blockX, target.blockY, target.blockZ));
			}else{
				AMCore.proxy.setTrackedLocation(AMVector3.zero());
			}

			if (AMCore.proxy.hasTrackedLocationSynced()){
				PowerNodeEntry data = AMCore.proxy.getTrackedData();
				Block block = player.worldObj.getBlock(target.blockX, target.blockY, target.blockZ);
				float yOff = 0.5f;
				if (data != null){
					GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
					for (PowerTypes type : ((IPowerNode)te).getValidPowerTypes()){
						float pwr = data.getPower(type);
						float pct = pwr / ((IPowerNode)te).getCapacity() * 100;
						RenderUtilities.drawTextInWorldAtOffset(String.format("%s%.2f (%.2f%%)", type.chatColor(), pwr, pct),
								target.blockX - (player.prevPosX - (player.prevPosX - player.posX) * partialTicks) + 0.5f,
								target.blockY + yOff - (player.prevPosY - (player.prevPosY - player.posY) * partialTicks) + block.getBlockBoundsMaxY() * 0.8f,
								target.blockZ - (player.prevPosZ - (player.prevPosZ - player.posZ) * partialTicks) + 0.5f,
								0xFFFFFF);
						yOff += 0.12f;
					}
					GL11.glPopAttrib();
				}
			}
		}
	}

	public void addDeferredTargetSet(EntityLiving ent, EntityLivingBase target){
		clientTickHandler.addDeferredTarget(ent, target);
	}

	public void addDigParticle(World worldObj, int xCoord, int yCoord, int zCoord, Block block, int meta){
		Minecraft.getMinecraft().
				effectRenderer.addEffect(new EntityDiggingFX(worldObj,
				xCoord + worldObj.rand.nextDouble(),
				yCoord + worldObj.rand.nextDouble(),
				zCoord + worldObj.rand.nextDouble(),
				0,
				0,
				0,
				block,
				meta,
				0
		));
	}
}

