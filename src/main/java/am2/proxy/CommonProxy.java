package am2.proxy;

import am2.*;
import am2.affinity.AffinityHelper;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.armor.ArmorEventHandler;
import am2.armor.infusions.*;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityParticleEmitter;
import am2.buffs.BuffList;
import am2.commands.ConfigureAMUICommand;
import am2.enchantments.AMEnchantments;
import am2.entities.EntityManager;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.network.AMPacketProcessorServer;
import am2.particles.ParticleManagerServer;
import am2.playerextensions.ExtendedProperties;
import am2.power.PowerNodeCache;
import am2.power.PowerNodeEntry;
import am2.proxy.gui.ServerGuiManager;
import am2.proxy.tick.ServerTickHandler;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.spell.SpellUnlockManager;
import am2.utility.ProxyUtilitiesCommon;
import am2.worldgen.AM2WorldDecorator;
import am2.worldgen.RetroactiveWorldgenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CommonProxy{
	private ArrayList<AMVector3> pendingFlickerLinks;

	public BlocksCommonProxy blocks;
	public ItemsCommonProxy items;
	public PlayerTracker playerTracker;
	public static HashMap<String, String> teamHostility;
	public ParticleManagerServer particleManager;
	public NBTTagCompound cwCopyLoc;
	public AM2WorldDecorator worldGen;
	public AMEnchantments enchantments;

	public ItemFrameWatcher itemFrameWatcher;
	protected ProxyUtilitiesCommon utils;
	public static EntityManager entities;
	public static ServerGuiManager guiManager;

	private ServerTickHandler serverTickHandler;

	private HashMap<EntityLivingBase, ArrayList<PotionEffect>> deferredPotionEffects = new HashMap<EntityLivingBase, ArrayList<PotionEffect>>();
	private HashMap<EntityLivingBase, Integer> deferredDimensionTransfers = new HashMap<EntityLivingBase, Integer>();

	private int totalFlickerCount = 0;

	public CommonProxy(){
		teamHostility = new HashMap<String, String>();
		playerTracker = new PlayerTracker();
		particleManager = new ParticleManagerServer();
		itemFrameWatcher = new ItemFrameWatcher();
		pendingFlickerLinks = new ArrayList<AMVector3>();
		cwCopyLoc = null;
	}

	public void postinit(){
		blocks.setupSpellConstraints();
		items.postInit();
		playerTracker.postInit();
		
		BuffList.postInit();

		MinecraftForge.EVENT_BUS.register(new AMEventHandler());
		MinecraftForge.EVENT_BUS.register(PowerNodeCache.instance);
		MinecraftForge.EVENT_BUS.register(new AffinityHelper());
		MinecraftForge.EVENT_BUS.register(new SpellUnlockManager());
		MinecraftForge.EVENT_BUS.register(new ArmorEventHandler());
		MinecraftForge.EVENT_BUS.register(EntityManager.instance);
		MinecraftForge.EVENT_BUS.register(new AMWorldEventHandler());
		if (!SkillTreeManager.instance.isSkillDisabled(SkillManager.instance.getSkill("Shrink")))
			MinecraftForge.EVENT_BUS.register(new ShrinkHandler());

		MinecraftForge.EVENT_BUS.register(particleManager);
		FMLCommonHandler.instance().bus().register(particleManager);

		FMLCommonHandler.instance().bus().register(playerTracker);
	}

	public void preinit(){
		AMCore.config.init();
		AMCore.skillConfig.init();
		utils = new ProxyUtilitiesCommon();

		blocks = new BlocksCommonProxy();
		items = new ItemsCommonProxy();
		entities = EntityManager.instance;

		BuffList.Init();
		BuffList.Instantiate();

		blocks.InstantiateBlocks();
		items.InstantiateItems();

		ObeliskFuelHelper.instance.registerFuelType(new ItemStack(ItemsCommonProxy.itemOre, 0, ItemsCommonProxy.itemOre.META_VINTEUMDUST), 200);
		ObeliskFuelHelper.instance.registerFuelType(new ItemStack(ItemsCommonProxy.itemAMBucket, 0, Short.MAX_VALUE), 2000);

		registerInfusions();
	}

	public void init(){
		blocks.RegisterBlocks();
		blocks.RegisterTileEntities();

		blocks.InitRecipes();
		items.InitRecipes();

		entities.registerEntities();
		entities.initializeSpawns();

		SkillManager.instance.init();

		SkillTreeManager.instance.init();
		worldGen = new AM2WorldDecorator();
		GameRegistry.registerWorldGenerator(worldGen, 0);

		EntityItemWatcher.instance.init();

		enchantments = new AMEnchantments();
		enchantments.Init();

		ClientCommandHandler.instance.registerCommand(new ConfigureAMUICommand());
	}

	public AM2WorldDecorator getWorldGen(){
		return worldGen;
	}

	public void InitializeAndRegisterHandlers(){
		guiManager = new ServerGuiManager();
		NetworkRegistry.INSTANCE.registerGuiHandler(AMCore.instance, guiManager);

		serverTickHandler = new ServerTickHandler();

		FMLCommonHandler.instance().bus().register(serverTickHandler);

		AMNetHandler.INSTANCE.registerChannels(new AMPacketProcessorServer());
	}

	public void addQueuedRetrogen(int dimensionID, ChunkCoordIntPair pair){
		ArrayList<ChunkCoordIntPair> chunks;
		if (RetroactiveWorldgenerator.deferredChunkGeneration.containsKey(dimensionID)){
			chunks = RetroactiveWorldgenerator.deferredChunkGeneration.get(dimensionID);
		}else{
			chunks = new ArrayList<ChunkCoordIntPair>();
		}

		chunks.add(pair);
		RetroactiveWorldgenerator.instance.deferredChunkGeneration.put(dimensionID, chunks);
	}

	public void flashManaBar(){
	}

	public void blackoutArmorPiece(EntityPlayerMP player, int slot, int cooldown){
		serverTickHandler.blackoutArmorPiece(player, slot, cooldown);
	}

	public Entity getEntityByID(World world, int ID){
		Entity ent = null;
		for (Object o : world.loadedEntityList){
			if (o instanceof EntityLivingBase){
				ent = (EntityLivingBase)o;
				if (ent.getEntityId() == ID){
					return ent;
				}
			}
		}
		return null;
	}

	public WorldServer[] getWorldServers(){
		return FMLServerHandler.instance().getServer().worldServers;
	}

	public EntityLivingBase getEntityByID(int entityID){
		Entity ent = null;
		for (WorldServer ws : getWorldServers()){
			ent = ws.getEntityByID(entityID);
			if (ent != null){
				if (!(ent instanceof EntityLivingBase)) return null;
				else break;
			}
		}
		return (EntityLivingBase)ent;
	}

	public EntityPlayer getLocalPlayer(){
		return null;
	}

	public ProxyUtilitiesCommon getProxyUtils(){
		return utils;
	}

	public int getArmorRenderIndex(String prefix){
		return 0;
	}

	public void openSkillTreeUI(World world, EntityPlayer player){
	}

	public void openParticleBlockGUI(World world, EntityPlayer player, TileEntityParticleEmitter te){
	}

	public void setMouseDWheel(int dwheel){
	}

	public void renderGameOverlay(){
	}

	/* LOCALIZATION */
	public void addName(Object obj, String s){
	}

	public void addLocalization(String s1, String string){
	}

	public String getItemStackDisplayName(ItemStack newStack){
		return "";
	}

	public String getCurrentLanguage(){
		return null;
	}

	public void sendLocalMovementData(EntityLivingBase ent){

	}

	public void setCompendiumSaveBase(String compendiumBase){

	}

	public void addDeferredPotionEffect(EntityLivingBase ent, PotionEffect pe){
		if (!deferredPotionEffects.containsKey(ent))
			deferredPotionEffects.put(ent, new ArrayList<PotionEffect>());

		ArrayList<PotionEffect> effects = deferredPotionEffects.get(ent);
		effects.add(pe);
	}

	public void addDeferredDimensionTransfer(EntityLivingBase ent, int dimension){
		deferredDimensionTransfers.put(ent, dimension);
	}

	public HashMap<EntityLivingBase, ArrayList<PotionEffect>> getDeferredPotionEffects(){
		return (HashMap<EntityLivingBase, ArrayList<PotionEffect>>)deferredPotionEffects.clone();
	}

	public void clearDeferredDimensionTransfers(){
		deferredDimensionTransfers.clear();
	}

	public HashMap<EntityLivingBase, Integer> getDeferredDimensionTransfers(){
		return (HashMap<EntityLivingBase, Integer>)deferredDimensionTransfers.clone();
	}

	public void clearDeferredPotionEffects(){
		deferredPotionEffects.clear();
	}

	public void requestPowerPathVisuals(IPowerNode node, EntityPlayerMP player){

	}

	public void receivePowerPathVisuals(HashMap<PowerTypes, ArrayList<LinkedList<AMVector3>>> paths){

	}

	public HashMap<PowerTypes, ArrayList<LinkedList<AMVector3>>> getPowerPathVisuals(){
		return null;
	}

	public boolean isClientPlayer(EntityLivingBase ent){
		return false;
	}

	public void setViewSettings(){
		if (ExtendedProperties.For(getLocalPlayer()).getFlipRotation() > 0)
			Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
	}

	public void registerInfusions(){
		DamageReductionImbuement.registerAll();
		GenericImbuement.registerAll();
		ImbuementRegistry.instance.registerImbuement(new Dispelling());
		ImbuementRegistry.instance.registerImbuement(new FallProtection());
		ImbuementRegistry.instance.registerImbuement(new FireProtection());
		ImbuementRegistry.instance.registerImbuement(new Freedom());
		ImbuementRegistry.instance.registerImbuement(new Healing());
		ImbuementRegistry.instance.registerImbuement(new HungerBoost());
		ImbuementRegistry.instance.registerImbuement(new JumpBoost());
		ImbuementRegistry.instance.registerImbuement(new LifeSaving());
		ImbuementRegistry.instance.registerImbuement(new Lightstep());
		ImbuementRegistry.instance.registerImbuement(new MiningSpeed());
		ImbuementRegistry.instance.registerImbuement(new Recoil());
		ImbuementRegistry.instance.registerImbuement(new SwimSpeed());
		ImbuementRegistry.instance.registerImbuement(new WaterBreathing());
		ImbuementRegistry.instance.registerImbuement(new WaterWalking());
	}

	public void setTrackedPowerCompound(NBTTagCompound compound){
	}

	public void setTrackedLocation(AMVector3 location){
	}

	public boolean hasTrackedLocationSynced(){
		return false;
	}

	public PowerNodeEntry getTrackedData(){
		return null;
	}

	public void addDeferredTargetSet(EntityLiving ent, EntityLivingBase target){
		serverTickHandler.addDeferredTarget(ent, target);
	}

	/**
	 * Proxied compendium unlocks.  Do not call directly - use the CompendiumUnlockHandler instead.
	 */
	public void unlockCompendiumEntry(String id){

	}

	/**
	 * Proxied compendium unlocks.  Do not call directly - use the CompendiumUnlockHandler instead.
	 */
	public void unlockCompendiumCategory(String id){

	}

	public void drawPowerOnBlockHighlight(EntityPlayer player, MovingObjectPosition target, float partialTicks){

	}

	public void incrementFlickerCount(){
		this.totalFlickerCount++;
	}

	public void decrementFlickerCount(){
		this.totalFlickerCount--;
		if (this.totalFlickerCount < 0)
			this.totalFlickerCount = 0;
	}

	public int getTotalFlickerCount(){
		return this.totalFlickerCount;
	}

	public void addDigParticle(World worldObj, int xCoord, int yCoord, int zCoord, Block block, int meta){
	}
}
