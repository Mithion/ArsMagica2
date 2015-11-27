package am2.entities;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.api.spell.enums.Affinity;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import am2.utility.InventoryUtilities;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.ArrayList;

public class EntityFlicker extends EntityAmbientCreature{

	private static final int WATCHER_FLICKERTYPE = 20;
	private static final int WATCHER_AMBIENTFLICK = 21;

	private static final int DIRECTION_CHANGE_TIME = 200;

	private AMVector3 targetPosition = null;
	private AMVector3 normalizedMovementVector = AMVector3.zero();

	private int flickCount = 0;

	public EntityFlicker(World par1World){
		super(par1World);
		this.setSize(0.5f, 0.5f);
		setFlickerType(Affinity.values()[par1World.rand.nextInt(Affinity.values().length - 1)]);
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(WATCHER_FLICKERTYPE, 0);
		this.dataWatcher.addObject(WATCHER_AMBIENTFLICK, (byte)0);
	}

	@Override
	public void setDead(){
		AMCore.proxy.decrementFlickerCount();
		super.setDead();
	}

	public void setFlickerType(Affinity affinity){
		this.dataWatcher.updateObject(WATCHER_FLICKERTYPE, affinity.ID);
	}

	public Affinity getFlickerAffinity(){
		int flickerType = dataWatcher.getWatchableObjectInt(WATCHER_FLICKERTYPE);
		return Affinity.values()[flickerType];
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if (this.worldObj.isRemote)
			return false;
		flick();
		return !par1DamageSource.isUnblockable();
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate(){
		return true;
	}

	@Override
	protected void fall(float par1){
	}

	@Override
	public boolean allowLeashing(){
		return false;
	}

	@Override
	public boolean canBePushed(){
		return false;
	}

	@Override
	public boolean canBreatheUnderwater(){
		return true;
	}

	@Override
	public boolean canTriggerWalking(){
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound){
		AMCore.proxy.incrementFlickerCount();
		super.readFromNBT(par1nbtTagCompound);
	}

	@Override
	public void onUpdate(){
		super.onUpdate();

		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;

		long time = worldObj.getWorldTime() % 24000;
		if (!worldObj.isRemote && time >= 18500 && time <= 18600){
			//this.dataWatcher.updateObject(WATCHER_AMBIENTFLICK, (byte)1);
			this.setDead();
			return;
		}

		boolean playerClose = false;
		AMVector3 me = new AMVector3(this);
		if (!worldObj.isRemote){
			for (Object o : worldObj.playerEntities){
				if (me.distanceSqTo(new AMVector3((EntityPlayer)o)) < 25){

					ItemStack chestArmor = ((EntityPlayer)o).getCurrentArmor(2);
					if (chestArmor == null || !ArmorHelper.isInfusionPreset(chestArmor, GenericImbuement.flickerLure))
						playerClose = true;
					break;
				}
			}
		}

		if (this.ticksExisted > 100 && playerClose && this.dataWatcher.getWatchableObjectByte(WATCHER_AMBIENTFLICK) == (byte)0){
			if (this.getActivePotionEffects().size() == 0 || (this.getActivePotionEffects().size() == 1 && worldObj.rand.nextDouble() < 0.1f))
				this.dataWatcher.updateObject(WATCHER_AMBIENTFLICK, (byte)1);
		}else if (this.dataWatcher.getWatchableObjectByte(WATCHER_AMBIENTFLICK) == (byte)1){
			flickCount++;
			if (worldObj.isRemote && flickCount > 7)
				flick(); //client flick
			else if (!worldObj.isRemote && flickCount > 10)
				flick(); //server flick
		}

		if (worldObj.isRemote){
			//for (int i = 0; i < + 1; ++i){
			if (getRNG().nextInt(10) < AMCore.config.getGFXLevel()){
				AMParticle effect = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, AMParticleIcons.instance.getParticleForAffinity(getFlickerAffinity()), posX, posY, posZ);
				if (effect != null){
					effect.addRandomOffset(this.width, this.height, this.width);
					effect.setDontRequireControllers();
					effect.setMaxAge(10);
					if (getFlickerAffinity() == Affinity.EARTH)
						effect.setParticleScale(0.01f + rand.nextFloat() * 0.05f);
					else
						effect.setParticleScale(0.05f + rand.nextFloat() * 0.05f);
				}
			}
		}
	}

	@Override
	protected void updateEntityActionState(){
		super.updateEntityActionState();

		AMVector3 me = new AMVector3(this);

		boolean needsNewPath = targetPosition == null || this.ticksExisted % DIRECTION_CHANGE_TIME == 0;
		if (needsNewPath && worldObj.rand.nextDouble() < 0.1f)
			pickNewTargetPosition();

		if (targetPosition == null) //this represents the pause state in between picking new waypoints
			return;

		if (me.distanceSqTo(targetPosition) < 1f){
			targetPosition = null;
			return;
		}

		this.rotationYaw = AMVector3.anglePreNorm(me, targetPosition);

		normalizedMovementVector = me.copy().sub(targetPosition).normalize();

		if (normalizedMovementVector.y > 0)
			rotatePitchTowards(-70 * normalizedMovementVector.y, 30);
		else
			rotatePitchTowards(0, 30);

		float speed = 0.2f;
		this.addVelocity(-normalizedMovementVector.x * speed, -normalizedMovementVector.y * speed, -normalizedMovementVector.z * speed);
	}

	public AMVector3 getNormalizedMovement(){
		return this.normalizedMovementVector;
	}

	private void rotatePitchTowards(float p, float step){
		if (this.rotationPitch != p){
			if (step > 0 && this.rotationPitch + step > p){
				step = p - this.rotationPitch;
			}else if (step < 0 && this.rotationPitch + step < p){
				step = p - this.rotationPitch;
			}
			this.rotationPitch += step;
		}
	}

	private void flick(){
		if (this.worldObj.isRemote){
			for (int i = 0; i < 10 * AMCore.config.getGFXLevel(); ++i){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "radiant", posX, posY, posZ);
				if (particle != null){
					particle.AddParticleController(
							new ParticleMoveOnHeading(
									particle,
									worldObj.rand.nextDouble() * 360,
									worldObj.rand.nextDouble() * 360,
									worldObj.rand.nextDouble() * 0.3f + 0.01f,
									1,
									false));
					particle.setRGBColorI(getFlickerAffinity().color);
					particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed((float)(worldObj.rand.nextDouble() * 0.1 + 0.05)).setKillParticleOnFinish(true));
					particle.setIgnoreMaxAge(true);
					particle.setParticleScale(0.1f);
				}
			}
		}else{
			this.setDead();
		}
	}

	private void pickNewTargetPosition(){
		int groundLevel = 0;

		switch (this.getFlickerAffinity()){
		case WATER: //water flicker
			for (int i = 0; i < 5; ++i){
				targetPosition = new AMVector3(this.posX - 5 + worldObj.rand.nextInt(10), this.posY - 5 + worldObj.rand.nextInt(10), this.posZ - 5 + worldObj.rand.nextInt(10));
				Block block = worldObj.getBlock((int)Math.floor(targetPosition.x), (int)Math.floor(targetPosition.y), (int)Math.floor(targetPosition.z));
				if (block == Blocks.water || block == Blocks.flowing_water){
					break;
				}
			}
			break;
		case AIR: //air flicker
			groundLevel = getTopBlockNearMe();
			targetPosition = new AMVector3(this.posX - 5 + worldObj.rand.nextInt(10), groundLevel + 10 + worldObj.rand.nextInt(15), this.posZ - 5 + worldObj.rand.nextInt(10));
			break;
		case EARTH: //earth flicker
			groundLevel = getTopBlockNearMe();
			targetPosition = new AMVector3(this.posX - 5 + worldObj.rand.nextInt(10), groundLevel + worldObj.rand.nextInt(1) + 1, this.posZ - 5 + worldObj.rand.nextInt(10));
			break;
		default: //all others
			groundLevel = getTopBlockNearMe();
			targetPosition = new AMVector3(this.posX - 5 + worldObj.rand.nextInt(10), groundLevel + 3 + worldObj.rand.nextInt(5), this.posZ - 5 + worldObj.rand.nextInt(10));
		}
	}

	private int getTopBlockNearMe(){
		int x = (int)Math.floor(posX);
		int y = (int)Math.floor(posY);
		int z = (int)Math.floor(posZ);

		while (y > 0 && worldObj.isAirBlock(x, y, z))
			y--;
		while (y < worldObj.getActualHeight() && !worldObj.isAirBlock(x, y, z))
			y++;

		return y;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("flickerType", dataWatcher.getWatchableObjectInt(WATCHER_FLICKERTYPE));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readEntityFromNBT(par1nbtTagCompound);
		dataWatcher.updateObject(WATCHER_FLICKERTYPE, par1nbtTagCompound.getInteger("flickerType"));
		AMCore.proxy.incrementFlickerCount();
	}

	@Override
	protected boolean interact(EntityPlayer player){
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() == ItemsCommonProxy.flickerJar && !this.isDead){
			if (stack.getItemDamage() == 0){
				if (!worldObj.isRemote){
					setDead();
					InventoryUtilities.decrementStackQuantity(player.inventory, player.inventory.currentItem, 1);
					ItemStack newStack = new ItemStack(ItemsCommonProxy.flickerJar);
					ItemsCommonProxy.flickerJar.setFlickerJarTypeFromFlicker(newStack, this);
					if (!InventoryUtilities.mergeIntoInventory(player.inventory, newStack)){
						if (!worldObj.isRemote)
							player.dropItem(newStack.getItem(), newStack.getItemDamage());
					}
				}
				return true;
			}else{
				flick();
			}
		}
		return false;
	}

	@Override
	public boolean getCanSpawnHere(){
		if (AMCore.proxy.getTotalFlickerCount() > 8 * worldObj.playerEntities.size() || worldObj.rand.nextDouble() > 0.2f){
			return false;
		}
		//get the biome we're trying to spawn in
		BiomeGenBase biome = worldObj.getBiomeGenForCoords((int)Math.floor(this.posX), (int)Math.floor(this.posZ));
		if (biome != null){
			//get the tags on this biome
			Type[] biomeTags = BiomeDictionary.getTypesForBiome(biome);
			//pick a random tag to focus on
			Type tagType = biomeTags[worldObj.rand.nextInt(biomeTags.length)];
			//create a list of valid types based on that tag
			ArrayList<Affinity> validAffinities = new ArrayList<Affinity>();
			//populate the list
			//DO NOT USE THIS LIST FOR AIR/EARTH/LIFE - they are handled by special cases.
			switch (tagType){
			case END:
				validAffinities.add(Affinity.ENDER);
				break;
			case FOREST:
			case CONIFEROUS:
			case JUNGLE:
				validAffinities.add(Affinity.NATURE);
				break;
			case COLD:
			case SNOWY:
				validAffinities.add(Affinity.ICE);
				break;
			case MAGICAL:
				validAffinities.add(Affinity.ARCANE);
				break;
			case NETHER:
				validAffinities.add(Affinity.FIRE);
				break;
			case OCEAN:
				validAffinities.add(Affinity.LIGHTNING);
			case SWAMP:
			case WATER:
			case RIVER:
			case WET:
			case BEACH:
				validAffinities.add(Affinity.WATER);
				break;
			case DEAD:
			case DENSE:
			case DRY:
			case HOT:
			case LUSH:
			case MESA:
			case SANDY:
			case SAVANNA:
			case SPARSE:
			case SPOOKY:
			case WASTELAND:
			case PLAINS:
			case HILLS:
			case MOUNTAIN:
			case MUSHROOM:
			default:
				break;
			}

			//special conditions for air/earth flickers based on y coordinate
			if (posY < 55){
				validAffinities.add(Affinity.EARTH);
			}

			if (worldObj.canBlockSeeTheSky((int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ)))
				validAffinities.add(Affinity.AIR);

			if (worldObj.isRaining() && worldObj.rand.nextBoolean()){
				validAffinities.clear();
				validAffinities.add(Affinity.LIGHTNING);
			}


			if (validAffinities.size() <= 0)
				return false;

			//life flickers always have a chance to spawn?
			if (worldObj.rand.nextDouble() < 0.1f){
				this.setFlickerType(Affinity.LIFE);
			}else{
				this.setFlickerType(validAffinities.get(worldObj.rand.nextInt(validAffinities.size())));
			}

			if (this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()){
				AMCore.proxy.incrementFlickerCount();
				return true;
			}
		}
		return false;
	}
}
