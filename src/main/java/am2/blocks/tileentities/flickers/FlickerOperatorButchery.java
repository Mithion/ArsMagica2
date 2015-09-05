package am2.blocks.tileentities.flickers;

import am2.AMCore;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.entities.SpawnBlacklists;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class FlickerOperatorButchery implements IFlickerFunctionality{

	@Override
	public boolean RequiresPower(){
		return true;
	}

	@Override
	public int PowerPerOperation(){
		return 500;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered){
		HashMap<Class, Integer> entityCount = new HashMap<Class, Integer>();
		int radius = 6;
		List<EntityAnimal> creatures = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(((TileEntity)habitat).xCoord - radius, ((TileEntity)habitat).yCoord - radius, ((TileEntity)habitat).zCoord - radius, ((TileEntity)habitat).xCoord + radius + 1, ((TileEntity)habitat).yCoord + radius + 1, ((TileEntity)habitat).zCoord + radius + 1));
		for (EntityAnimal creature : creatures){
			Class clazz = creature.getClass();
			if (!SpawnBlacklists.canButcheryAffect(clazz))
				continue;
			Integer count = entityCount.get(clazz);
			if (count == null)
				count = 0;
			if (!creature.isChild())
				count++;
			entityCount.put(clazz, count);
			if (count > 2){
				if (worldObj.isRemote){
					AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "ghost", ((TileEntity)habitat).xCoord + 0.5, ((TileEntity)habitat).yCoord + 0.7, ((TileEntity)habitat).zCoord + 0.5);
					if (particle != null){
						particle.setMaxAge(20);
						particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.05f, 1, false));
					}
				}else{
					creature.attackEntityFrom(DamageSource.generic, 500);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered){
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers){
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return 600;
	}


	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"PBC",
				"FGL",
				"RER",
				Character.valueOf('P'), new ItemStack(Items.porkchop),
				Character.valueOf('B'), new ItemStack(Items.beef),
				Character.valueOf('C'), new ItemStack(Items.chicken),
				Character.valueOf('F'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.FIRE.ordinal()),
				Character.valueOf('G'), new ItemStack(Items.golden_sword),
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.LIFE.ordinal()),
				Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				Character.valueOf('E'), new ItemStack(ItemsCommonProxy.evilBook)
		};
	}

}
