package am2.entities;

import am2.items.ItemsCommonProxy;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityManaCreeper extends EntityCreeper{

	int timeSinceIgnited_Local;
	int lastActiveTime;

	protected int fuseLength = 20;

	public EntityManaCreeper(World par1World){
		super(par1World);
	}

	@Override
	protected Item getDropItem(){
		return ItemsCommonProxy.manaCake;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1){
			this.dropItem(getDropItem(), par2 > 0 ? 1 + getRNG().nextInt(par2) : 1);
		}
	}

	private void ForceZeroCreeperFuse(){
		ReflectionHelper.setPrivateValue(EntityCreeper.class, this, 0, 1);
	}

	@Override
	public float getCreeperFlashIntensity(float par1){
		return (this.lastActiveTime + (this.timeSinceIgnited_Local - this.lastActiveTime) * par1) / (this.fuseLength - 2);
	}

	@Override
	public void onUpdate(){
		if (this.isEntityAlive()){
			this.lastActiveTime = this.timeSinceIgnited_Local;
			int var1 = this.getCreeperState();

			if (var1 > 0 && this.timeSinceIgnited_Local == 0){
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.timeSinceIgnited_Local += var1;

			if (this.timeSinceIgnited_Local < 0){
				this.timeSinceIgnited_Local = 0;
			}

			if (this.timeSinceIgnited_Local >= 10){
				this.timeSinceIgnited_Local = 10;

				if (!this.worldObj.isRemote){
					createManaVortex();
					this.onDeath(DamageSource.generic);
					this.setDead();
				}
			}
		}

		super.onUpdate();
	}

	private void createManaVortex(){
		if (worldObj.isRemote){
			return;
		}
		EntityManaVortex vortex = new EntityManaVortex(worldObj);
		vortex.setPosition(this.posX, this.posY + 1, this.posZ);
		worldObj.spawnEntityInWorld(vortex);
	}

	@Override
	public boolean getCanSpawnHere(){
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		return super.getCanSpawnHere();
	}
}
