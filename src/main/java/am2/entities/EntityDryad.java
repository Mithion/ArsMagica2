package am2.entities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.bosses.BossSpawnHelper;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleOrbitEntity;

public class EntityDryad extends EntityCreature {

	public EntityDryad(World par1World) {
		super(par1World);
		getNavigator().setAvoidsWater(true);

		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 0.68F));
		tasks.addTask(1, new EntityAITempt(this, getAIMoveSpeed(), Item.getItemFromBlock(Blocks.sapling), false));
		tasks.addTask(5, new EntityAIWander(this, 0.5F));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}


	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	public void onUpdate(){
		World world = this.worldObj;
		super.onUpdate();
		if (!world.isRemote || world == null){
			return;
		}
		if (new Random().nextInt(100) == 3){
			AMParticle effect = (AMParticle) AMCore.instance.proxy.particleManager.spawn(world, "hr_sparkles_1", this.posX, this.posY+2, this.posZ);
			if (effect != null){
				effect.AddParticleController(new ParticleOrbitEntity(effect, this, new Random().nextDouble() * 0.2 + 0.2, 1, false));
				effect.setIgnoreMaxAge(false);
				effect.setRGBColorF(0.1f,0.8f,0.1f);
			}
		}
	}

	@Override
	protected boolean canDespawn() {
		return AMCore.config.canDraydsDespawn();
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		int i = rand.nextInt(1);

		for (int j = 0; j < i; j++)
		{
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, 5), 0.0f);
		}

		i = rand.nextInt(10);

		if (i == 3){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, 8), 0.0f);
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer){
			BossSpawnHelper.instance.onDryadKilled(this);
		}
		super.onDeath(par1DamageSource);
	}

	@Override
	public boolean getCanSpawnHere() {
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		return super.getCanSpawnHere();
	};
}
