package am2.playerextensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import am2.AMCore;
import am2.api.IAffinityData;
import am2.api.math.AMVector3;
import am2.api.spell.enums.Affinity;
import am2.network.AMDataReader;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;

public class AffinityData implements IExtendedEntityProperties, IAffinityData{
	public static final String identifier = "AffinityData";

	private HashMap<Integer, Float> affinityDepths;
	public static final float MAX_DEPTH = 100;
	private static final float ADJACENT_FACTOR = 0.25f;
	private static final float MINOR_OPPOSING_FACTOR = 0.5f;
	private static final float MAJOR_OPPOSING_FACTOR = 0.75f;
	private Entity entity;
	private float diminishingReturns = 1.2f;
	
	private AMVector3 lastGroundPosition = null;
	private boolean hasActivatedNightVision = false;
	private int abilityCooldown = 0;

	public float accumulatedHungerRegen = 0f;
	public float accumulatedLifeRegen = 0f;

	private boolean hasDoneFullSync = false;

	private boolean hasUpdate = false;

	private boolean forcingSync = false;
	private int ticksToSync = 0;
	private final int syncTickDelay = 100;

	private boolean isLocked = false;

	private Affinity[] highestAffinities = new Affinity[2];

	public AffinityData(){
		setupAffinityDefaults();
	}

	public static AffinityData For(EntityLivingBase living){
		return (AffinityData) living.getExtendedProperties(identifier);
	}

	@Override
	public float getAffinityDepth(Affinity affinity){
		if (affinity == Affinity.NONE) return 0;

		return affinityDepths.get(affinity.ordinal()) / MAX_DEPTH;
	}

	@Override
	public float getDiminishingReturnsFactor(){
		return this.diminishingReturns;
	}

	public void tickDiminishingReturns(){
		if (this.diminishingReturns < 1.2f){
			this.diminishingReturns += 0.005f;
		}
	}

	public void addDiminishingReturns(boolean isChanneled){
		this.diminishingReturns -= isChanneled ? 0.1f : 0.3f;
		if (this.diminishingReturns < 0) this.diminishingReturns = 0;
	}

	/**
	 * Directly sets an affinity.  Does not take into account oppositions or locks.  Use sparingly.
	 * @param affinity The affinity to set
	 * @param depth The depth to set
	 */
	@Override
	public void setAffinityAndDepth(Affinity affinity, float depth){
		if (affinity == Affinity.NONE) return;

		if (depth > MAX_DEPTH) depth = MAX_DEPTH;
		if (depth < 0) depth = 0;
		affinityDepths.put(affinity.ordinal(), depth);
		this.hasUpdate = true;
		updateHighestAffinities();
	}

	/**
	 * Increments the affinity by the specified amount, and decrements other affinities by an amount related to
	 * how much in opposition they are to the specified affinity.
	 * @param affinity The affinity to increase
	 * @param amt The amount to increase the affinity by
	 */
	@Override
	public void incrementAffinity(Affinity affinity, float amt){
		if (affinity == Affinity.NONE || isLocked) return;

		float adjacentDecrement = amt * ADJACENT_FACTOR;
		float minorOppositeDecrement = amt * MINOR_OPPOSING_FACTOR;
		float majorOppositeDecrement = amt * MAJOR_OPPOSING_FACTOR;

		addToAffinity(affinity, amt);

		if (getAffinityDepth(affinity) * MAX_DEPTH == MAX_DEPTH){
			isLocked = true;
		}

		for (Affinity adjacent : affinity.getAdjacentAffinities()){
			subtractFromAffinity(adjacent, adjacentDecrement);
		}

		for (Affinity minorOpposite : affinity.getMinorOpposingAffinities()){
			subtractFromAffinity(minorOpposite, minorOppositeDecrement);
		}

		for (Affinity majorOpposite : affinity.getMajorOpposingAffinities()){
			subtractFromAffinity(majorOpposite, majorOppositeDecrement);
		}

		Affinity directOpposite = affinity.getOpposingAffinity();
		if (directOpposite != null){
			subtractFromAffinity(directOpposite, amt);
		}
		this.hasUpdate = true;
		updateHighestAffinities();
	}

	private void addToAffinity(Affinity affinity, float amt){
		if (affinity == Affinity.NONE || isLocked) return;
		float existingAmt = affinityDepths.get(affinity.ordinal());
		existingAmt += amt;
		if (existingAmt > MAX_DEPTH) existingAmt = MAX_DEPTH;
		else if (existingAmt < 0) existingAmt = 0;
		affinityDepths.put(affinity.ordinal(), existingAmt);
	}

	private void subtractFromAffinity(Affinity affinity, float amt){
		if (affinity == Affinity.NONE || isLocked) return;
		float existingAmt = affinityDepths.get(affinity.ordinal());
		existingAmt -= amt;
		if (existingAmt > MAX_DEPTH) existingAmt = MAX_DEPTH;
		else if (existingAmt < 0) existingAmt = 0;
		affinityDepths.put(affinity.ordinal(), existingAmt);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tags = new NBTTagCompound();
		for (Integer affinity : affinityDepths.keySet()){
			tags.setDouble(Affinity.values()[affinity].name(), affinityDepths.get(affinity));
		}
		compound.setTag("AffinityDepthData", tags);
		compound.setBoolean("AffinityLocked", isLocked);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound tags = compound.getCompoundTag("AffinityDepthData");
		setupAffinityDefaults();
		for (Affinity affinity : Affinity.values()){
			if (tags.hasKey(affinity.name())){
				float depth = (float) tags.getDouble(affinity.name());
				setAffinityAndDepth(affinity, depth);
			}
		}
		isLocked = compound.getBoolean("AffinityLocked");
	}

	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}

	private void setupAffinityDefaults(){
		affinityDepths = new HashMap<Integer, Float>();
		for (Affinity affinity : Affinity.values()){
			if (affinity == Affinity.NONE) continue;
			affinityDepths.put(affinity.ordinal(), 0f);
		}
		updateHighestAffinities();
	}

	public boolean HasDoneFullSync(){
		return this.hasDoneFullSync ;
	}

	public void setFullSync(){
		this.ticksToSync = 0;
		this.hasUpdate = true;
		this.hasDoneFullSync = true;
		this.forcingSync = true;
	}

	public void setDelayedSync(int delay){
		setFullSync();
		this.ticksToSync = delay;
	}

	public void forceSync(){
		this.forcingSync = true;
		this.ticksToSync = 0;
	}

	public boolean hasUpdate(){
		if (!(this.entity instanceof EntityPlayer) && !forcingSync ){
			return false;
		}
		this.ticksToSync--;
		if (this.ticksToSync <= 0) this.ticksToSync = this.syncTickDelay;
		return this.hasUpdate && this.ticksToSync == this.syncTickDelay;
	}

	private byte[] getUpdateData(){
		AMDataWriter writer = new AMDataWriter();
		writer.add(this.entity.getEntityId());

		writer.add(affinityDepths.get(Affinity.AIR.ordinal()));
		writer.add(affinityDepths.get(Affinity.LIGHTNING.ordinal()));
		writer.add(affinityDepths.get(Affinity.ARCANE.ordinal()));
		writer.add(affinityDepths.get(Affinity.FIRE.ordinal()));
		writer.add(affinityDepths.get(Affinity.ENDER.ordinal()));
		writer.add(affinityDepths.get(Affinity.EARTH.ordinal()));
		writer.add(affinityDepths.get(Affinity.ICE.ordinal()));
		writer.add(affinityDepths.get(Affinity.NATURE.ordinal()));
		writer.add(affinityDepths.get(Affinity.WATER.ordinal()));
		writer.add(affinityDepths.get(Affinity.LIFE.ordinal()));
		writer.add(diminishingReturns);
		writer.add(isLocked);

		this.hasUpdate = false;
		this.forcingSync = false;

		return writer.generate();
	}

	public boolean handlePacketData(byte[] data){
		AMDataReader rdr = new AMDataReader(data, false);
		int entID = rdr.getInt();

		if (entID != this.entity.getEntityId()){
			return false;
		}

		affinityDepths.put(Affinity.AIR.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.LIGHTNING.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.ARCANE.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.FIRE.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.ENDER.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.EARTH.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.ICE.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.NATURE.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.WATER.ordinal(), rdr.getFloat());
		affinityDepths.put(Affinity.LIFE.ordinal(), rdr.getFloat());
		diminishingReturns = rdr.getFloat();
		isLocked = rdr.getBoolean();

		updateHighestAffinities();

		return true;
	}

	public void handleExtendedPropertySync() {
		if (!this.HasDoneFullSync()) this.setFullSync();

		if (!entity.worldObj.isRemote && this.hasUpdate()){
			byte[] data = this.getUpdateData();
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 32, AMPacketIDs.SYNC_AFFINITY_DATA, data);
		}
	}

	private void updateHighestAffinities(){
		List<Float> values = new ArrayList<Float>(affinityDepths.values());
		Collections.sort(values);
		values = values.subList(values.size()-2, values.size());

		//remove entries that are too low
		Iterator it = values.iterator();
		while (it.hasNext()){
			Float f = (Float) it.next();
			if (Math.floor(f * 100.0f) / 100.0f == 0)
				it.remove();
		}

		List<Affinity> resolvedAffinties = new ArrayList<Affinity>();

		HashMap<Integer, Float> clone = (HashMap<Integer, Float>) affinityDepths.clone();

		for (Float f : values){
			it = clone.entrySet().iterator();
			while (it.hasNext()){
				Entry<Integer, Float> e = (Entry<Integer, Float>) it.next();
				if (compareFloats(e.getValue(), f, 2)){
					resolvedAffinties.add(Affinity.getByID(e.getKey()));
					it.remove();
					break;
				}
			}
		}

		for (int i = 0; i < highestAffinities.length; ++i){
			if (i < resolvedAffinties.size())
				highestAffinities[highestAffinities.length - 1 - i] = resolvedAffinties.get(i);
			else
				highestAffinities[highestAffinities.length - 1 - i] = null;
		}
	}

	private boolean compareFloats(float f1, float f2, int precision){
		float factor = 10f * precision;
		return (Math.floor(f1 * factor) / factor) == (Math.floor(f2 * factor) / factor);
	}

	public void setLocked(boolean locked){
		isLocked = locked;
	}

	public List getColoredAffinityEffects(Affinity aff){
		ArrayList list = new ArrayList();
		switch (aff){
		case AIR:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.agile")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.lightasafeather")).toString());
			break;
		case ARCANE:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.4f, StatCollector.translateToLocal("am2.affinity.clearcaster")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.onewithmagic")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.magicweakness")).toString());
			break;
		case EARTH:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.1f, StatCollector.translateToLocal("am2.affinity.stoneskin")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.solidbones")).toString());
			break;
		case ENDER:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.waterweakness")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) >= 1.00f, StatCollector.translateToLocal("am2.affinity.sunlightweakness")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.poisonresistance")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.75f, StatCollector.translateToLocal("am2.affinity.darkvision")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) >= 0.75, StatCollector.translateToLocal("am2.affinity.phasing")).toString());
			break;
		case FIRE:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.fireresistance")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.firepunch")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.waterweakness")).toString());
			break;
		case ICE:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.waterfreeze")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) >= 1.0f, StatCollector.translateToLocal("am2.affinity.lavafreeze")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.1f, StatCollector.translateToLocal("am2.affinity.coldblooded")).toString());
			break;
		case LIFE:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.1f, StatCollector.translateToLocal("am2.affinity.fasthealing")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.6f, StatCollector.translateToLocal("am2.affinity.pacifist")).toString());
			break;
		case WATER:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.swiftswim")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.4f, StatCollector.translateToLocal("am2.affinity.expandedlungs")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.fluidity")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) >= 1f, StatCollector.translateToLocal("am2.affinity.antiendermen")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.fireweakness")).toString());
			break;
		case NATURE:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.harvester")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.thorns")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) >= 1f, StatCollector.translateToLocal("am2.affinity.photosynthesis")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.1f, StatCollector.translateToLocal("am2.affinity.rooted")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) >= 1f, StatCollector.translateToLocal("am2.affinity.leaflike")).toString());
			break;
		case LIGHTNING:
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.65f, StatCollector.translateToLocal("am2.affinity.reflexes")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.25f, StatCollector.translateToLocal("am2.affinity.shortcircuit")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.75f, StatCollector.translateToLocal("am2.affinity.thunderpunch")).toString());
			list.add(new AffinityEffectDescriptor(getAffinityDepth(aff) > 0.5f, StatCollector.translateToLocal("am2.affinity.fulmination")).toString());
			break;
		case NONE:
			break;
		default:
			break;

		}

		return list;
	}

	class AffinityEffectDescriptor
	{
		private final boolean hasDepth;
		private final String description;

		public AffinityEffectDescriptor(boolean hasDepth, String description){
			this.hasDepth = hasDepth;
			this.description = description;
		}

		@Override
		public String toString(){
			return (hasDepth ? "\247a" : "\2474") + description;
		}
	}

	public Affinity[] getHighestAffinities() {
		return this.highestAffinities;
	}

	public void onAffinityAbility(){
	
		if (getAffinityDepth(Affinity.ENDER) >= 0.75){
			if (this.entity.isSneaking()){
				this.hasActivatedNightVision = !this.hasActivatedNightVision;
			}else{
				if (this.lastGroundPosition != null){
					if (positionIsValid(lastGroundPosition)){
						
						spawnParticlesAt(lastGroundPosition);
						spawnParticlesAt(new AMVector3(entity));
						
						this.entity.setPosition(this.lastGroundPosition.x, this.lastGroundPosition.y + 1, this.lastGroundPosition.z);
						ExtendedProperties.For((EntityLivingBase)this.entity).setFallProtection(20000);					
						this.entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);	
						
						this.setCooldown(AMCore.config.getEnderAffinityAbilityCooldown());
					}else{
						((EntityPlayer)this.entity).addChatMessage(
								new ChatComponentText("am2.affinity.enderTPFailed")
							);
					}
				}else{
					((EntityPlayer)this.entity).addChatMessage(
						new ChatComponentText("am2.affinity.enderTPFailed")
					);
				}
			}
		}
		
		//beyond here we can handle other affinities that have activatable abilities
	}
	
	private void spawnParticlesAt(AMVector3 pos){
		int numParticles  = entity.worldObj.rand.nextInt(25) + 1;
		for (int i = 0; i < numParticles; ++i){
			this.entity.worldObj.spawnParticle("portal", 
					pos.x + entity.worldObj.rand.nextDouble() - 0.5f, 
					pos.y - 1 + entity.worldObj.rand.nextDouble() - 0.5f, 
					pos.z + entity.worldObj.rand.nextDouble() - 0.5f, 
					entity.worldObj.rand.nextDouble() - 0.5, 
					entity.worldObj.rand.nextDouble() - 0.5, 
					entity.worldObj.rand.nextDouble() - 0.5
				);
		}
	}
	
	private boolean positionIsValid(AMVector3 pos){
		return 
				this.entity.worldObj.isAirBlock((int)Math.floor(pos.x), (int)Math.floor(pos.y + 1), (int)Math.floor(pos.z)) &&
				this.entity.worldObj.isAirBlock((int)Math.floor(pos.x), (int)Math.floor(pos.y + 2), (int)Math.floor(pos.z));
	}
	
	public AMVector3 getLastGroundPosition(){
		return this.lastGroundPosition;
	}
	
	public void setLastGroundPosition(AMVector3 pos){
		this.lastGroundPosition = pos;
	}
	
	public boolean hasActivatedNightVision(){
		return this.hasActivatedNightVision;
	}
	
	public void setCooldown(int cd){
		this.abilityCooldown = cd;
	}
	
	public void tickCooldown(){
		if (this.abilityCooldown > 0)
			this.abilityCooldown--;
	}
	
	public boolean isAbilityReady(){
		return this.abilityCooldown == 0;
	}
}
