package am2.entities;

import java.io.File;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.blocks.tileentities.TileEntityCraftingAltar;
import am2.entities.ai.EntityAISpellmaking;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.proxy.ShadowSkinHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class EntityShadowHelper extends EntityLiving{

	private static final int DW_MIMIC_USER = 21; //who are we going to mimic (MC skin)?	
	private static final int DW_SEARCH_ITEM = 22; //what are we currently looking for?
	private static final int DW_TRANS_LOC_X = 23; //x-coordinate of search
	private static final int DW_TRANS_LOC_Y = 24; //y-coordinate of search
	private static final int DW_TRANS_LOC_Z = 25; //z-coordinate of search
	private static final int DW_HELD_ITEM = 26; //current held item
	private static final int DW_DROP_LOC_X = 27; //x-coordinate of search
	private static final int DW_DROP_LOC_Y = 28; //y-coordinate of search
	private static final int DW_DROP_LOC_Z = 29; //z-coordinate of search

	private TileEntityCraftingAltar altarTarget = null;
	private String lastDWString = "";	
	
	@SideOnly(Side.CLIENT)
	private ShadowSkinHelper skinHelper;

	public EntityShadowHelper(World par1World) {
		super(par1World);
		initAI();		
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}
	
	@Override
	public void onDeath(DamageSource par1DamageSource) {	
		super.onDeath(par1DamageSource);			
		if (worldObj.isRemote){
			spawnParticles();
			worldObj.playSound(posX, posY, posZ, "arsmagica2:misc.craftingaltar.create_spell", 1.0f, 1.0f, true);
		}
	}
	
	private void spawnParticles(){
		if (worldObj.isRemote){
			for (int i = 0; i < 25 * AMCore.config.getGFXLevel() + 1; ++i){
				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "arcane", posX, posY, posZ);
				if (particle != null){
					particle.addRandomOffset(1, 1, 1);
					particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.02f + getRNG().nextFloat() * 0.2f, 1, false));
					particle.setIgnoreMaxAge(false);
					particle.setMaxAge(20 + getRNG().nextInt(20));
				}
			}
		}
	}
	
	public void onJoinWorld(World world){
		spawnParticles();
		if (world.isRemote)
			this.skinHelper = new ShadowSkinHelper();
	}
	
	@Override
	protected String getDeathSound() {
		return null;
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	protected void onDeathUpdate() {		
		this.setDead();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(DW_MIMIC_USER, "");
		this.dataWatcher.addObject(DW_SEARCH_ITEM, new ItemStack(Items.apple));
		this.dataWatcher.addObject(DW_TRANS_LOC_X, 0);
		this.dataWatcher.addObject(DW_TRANS_LOC_Y, 0);
		this.dataWatcher.addObject(DW_TRANS_LOC_Z, 0);
		this.dataWatcher.addObject(DW_HELD_ITEM, new ItemStack(Items.paper));
		this.dataWatcher.addObject(DW_DROP_LOC_X, 0);
		this.dataWatcher.addObject(DW_DROP_LOC_Y, 0);
		this.dataWatcher.addObject(DW_DROP_LOC_Z, 0);
	}

	public void setSearchLocationAndItem(AMVector3 location, ItemStack item){
		if (this.worldObj.isRemote) return;
		this.dataWatcher.updateObject(DW_SEARCH_ITEM, item);
		this.dataWatcher.updateObject(DW_TRANS_LOC_X, (int)location.x);
		this.dataWatcher.updateObject(DW_TRANS_LOC_Y, (int)location.y);
		this.dataWatcher.updateObject(DW_TRANS_LOC_Z, (int)location.z);
	}

	public void setDropoffLocation(AMVector3 location){
		this.dataWatcher.updateObject(DW_DROP_LOC_X, (int)location.x);
		this.dataWatcher.updateObject(DW_DROP_LOC_Y, (int)location.y);
		this.dataWatcher.updateObject(DW_DROP_LOC_Z, (int)location.z);
	}
	
	public AMVector3 getSearchLocation(){
		return new AMVector3(this.dataWatcher.getWatchableObjectInt(DW_TRANS_LOC_X), this.dataWatcher.getWatchableObjectInt(DW_TRANS_LOC_Y), this.dataWatcher.getWatchableObjectInt(DW_TRANS_LOC_Z));
	}

	public AMVector3 getDropLocation(){
		return new AMVector3(this.dataWatcher.getWatchableObjectInt(DW_DROP_LOC_X), this.dataWatcher.getWatchableObjectInt(DW_DROP_LOC_Y), this.dataWatcher.getWatchableObjectInt(DW_DROP_LOC_Z));
	}

	public ItemStack getSearchItem(){
		return this.dataWatcher.getWatchableObjectItemStack(DW_SEARCH_ITEM);
	}

	public void setHeldItem(ItemStack item){
		this.dataWatcher.updateObject(DW_HELD_ITEM, item);
	}

	public void setMimicUser(String userName){
		this.dataWatcher.updateObject(DW_MIMIC_USER, userName);
	}

	public String getMimicUser(){
		return this.dataWatcher.getWatchableObjectString(DW_MIMIC_USER);
	}

	public boolean hasSearchLocation(){
		return !this.getSearchLocation().equals(AMVector3.zero());
	}

	public TileEntityCraftingAltar getAltarTarget(){
		return this.altarTarget;
	}

	public void setAltarTarget(TileEntityCraftingAltar target){
		this.altarTarget = target;
	}

	private void initAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAISpellmaking(this));
	}

	@Override
	public ItemStack getHeldItem() {
		return this.dataWatcher.getWatchableObjectItemStack(DW_HELD_ITEM);
	}

	@Override
	public void onUpdate() {	
		super.onUpdate();
		if (this.worldObj.isRemote){
			if (this.getMimicUser() != lastDWString){
				lastDWString = getMimicUser();
				this.skinHelper.setupCustomSkin(lastDWString);
			}
		}
		if (!worldObj.isRemote && (altarTarget == null || !altarTarget.isCrafting())){
			this.unSummon();
		}
	}
	
	@Override
	protected String getHurtSound() {
		return null;
	}	
	
	public void unSummon(){
		this.attackEntityFrom(DamageSource.generic, 5000);
	}
	
	public ResourceLocation getLocationSkin()
	{
		return this.skinHelper.getLocationSkin();
	}
	
	public ThreadDownloadImageData getTextureSkin()
	{
		return this.skinHelper.getTextureSkin();
	}
}
