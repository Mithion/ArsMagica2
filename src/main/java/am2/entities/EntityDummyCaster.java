package am2.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityDummyCaster extends EntityLiving{

	public EntityDummyCaster(World par1World){
		super(par1World);
	}

	@Override
	public void onUpdate(){

	}

	@Override
	public float getEyeHeight(){
		return 0;
	}

}
