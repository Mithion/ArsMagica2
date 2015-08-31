package am2.entities;

import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundList;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.entities.ai.EntityAIChestDeposit;
import am2.entities.ai.EntityAIPickup;
import am2.entities.ai.EntityAITargetNearbyInanimate;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.utility.InventoryUtilities;

public class EntityBroom extends EntityCreature {

	private EntityBroomInventory inventory;
	private AMVector3 chestLocation;
	private float moveCounter = 0;
	private float moveRotation = 0;

	public EntityBroom(World par1World) {
		super(par1World);
		inventory = new EntityBroomInventory();
		initAI();
		this.setSize(0.5f, 0.5f);
		this.stepHeight = 1.05f;
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void updateFallState(double par1, boolean par3) {
		if (par3)
		{
			if (this.fallDistance > 0.0F)
			{
				this.fall(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		}
		else if (par1 < 0.0D)
		{
			this.fallDistance = (float)(this.fallDistance - par1);
		}

		if (!this.isInWater())
		{
			this.handleWaterMovement();
		}
	}

	private void initAI(){
		this.tasks.taskEntries.clear();

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIPickup(this));
		this.tasks.addTask(2, new EntityAIChestDeposit(this));

		this.targetTasks.taskEntries.clear();

		this.targetTasks.addTask(1, new EntityAITargetNearbyInanimate(this, 8, false, EntityItem.class));
	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote){
			updateRotations();
			if (isMoving()){
				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "smoke", posX, posY, posZ);
				if (particle != null)
				{
					particle.addRandomOffset(0.5, 0.5, 0.5);
					particle.setRGBColorF(0.8f, 0.6f, 0.4f);
					particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.03f).setKillParticleOnFinish(true));
				}
			}
		}
		super.onUpdate();
	}

	private void updateRotations(){
		if (isMoving()){
			moveCounter+= 0.3f;
			moveRotation = (float) Math.sin(moveCounter) - (float) Math.sin((moveCounter-1));
			if (((int)(moveCounter)) % 6 == 0){
				//TODO: worldObj.playSoundAtEntity(this, .soundGrassFootstep.stepSoundName, 10.6f, worldObj.rand.nextFloat());
			}
		}else{
			moveCounter = 3.14f / 2f;
			if (moveRotation > 0){
				moveRotation -= 0.1f;
				if (moveRotation < 0)
					moveRotation = 0;
			}else if (moveRotation < 0){
				moveRotation += 0.1f;
				if (moveRotation > 0)
					moveRotation = 0;
			}
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	public float getRotation(){
		return moveRotation;
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	public boolean isInventoryFull(){
		return InventoryUtilities.isInventoryFull(inventory);
	}

	public boolean isInventoryEmpty(){
		return InventoryUtilities.isInventoryEmpty(inventory);
	}

	public void setChestLocation(AMVector3 chestLoc){
		chestLocation = chestLoc.copy();
	}

	public boolean hasRoomInInventoryFor(ItemStack stack){
		return InventoryUtilities.inventoryHasRoomFor(inventory, stack);
	}

	public void addItemStackToInventory(ItemStack stack){
		InventoryUtilities.mergeIntoInventory(inventory, stack);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		inventory.saveBroomInventory(nbttagcompound);
		if (chestLocation != null)
			nbttagcompound.setIntArray("chestLoc", new int[] { (int)chestLocation.x, (int)chestLocation.y, (int)chestLocation.z });
	}

	public boolean isMoving(){
		return (this.prevPosX != this.posX) || (this.prevPosZ != this.posZ);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		inventory.loadBroomInventory(nbttagcompound);
		int[] chestLoc = nbttagcompound.getIntArray("chestLoc");
		if (chestLoc.length == 3)
			chestLocation = new AMVector3(chestLoc[0], chestLoc[1], chestLoc[2]);
		else
			chestLocation = new AMVector3(posX, posY, posZ);
	}

	@Override
	protected boolean interact(EntityPlayer par1EntityPlayer) {
		if (par1EntityPlayer.getCurrentEquippedItem() != null && par1EntityPlayer.getCurrentEquippedItem().getItem() == ItemsCommonProxy.spellStaffMagitech){
			if (this.worldObj.isRemote){
				for (int i = 0; i < AMCore.config.getGFXLevel() * 2; ++i){
					AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "smoke", posX, posY, posZ);
					if (particle != null){
						particle.AddParticleController(new ParticleFloatUpward(particle, 0.1f, 0.3f, 1, false));
						particle.addRandomOffset(0.3, 1, 0.3);
						particle.setMaxAge(10);
					}
				}
			}else{
				this.entityDropItem(new ItemStack(ItemsCommonProxy.magicBroom), 0);
				dropInventoryItems();
				this.setDead();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		dropInventoryItems();
		super.onDeath(par1DamageSource);
	}

	private void dropInventoryItems(){
		EntityBroomInventory inventory = this.getInventory();
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack == null) continue;
			this.entityDropItem(stack, 0);
		}
	}

	public AMVector3 getChestLocation() {
		return chestLocation;
	}

	public EntityBroomInventory getInventory() {
		return inventory;
	}

}
