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
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class FlickerOperatorProgeny implements IFlickerFunctionality{

	private static final int BASE_COOLDOWN = 2000;
	private static final float COOLDOWN_BONUS = 0.75f;
	private static final int COOLDOWN_MINIMUM = 600; //minimum 30 seconds

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
		int radius = 8;
		int diameter = radius * 2 + 1;
		List<EntityAnimal> creatures = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(((TileEntity)habitat).xCoord - radius, ((TileEntity)habitat).yCoord - radius, ((TileEntity)habitat).zCoord - radius, ((TileEntity)habitat).xCoord + radius + 1, ((TileEntity)habitat).yCoord + radius + 1, ((TileEntity)habitat).zCoord + radius + 1));
		for (EntityAnimal creature : creatures){
			Class clazz = creature.getClass();
			if (!SpawnBlacklists.canProgenyAffect(clazz))
				continue;
			Integer count = entityCount.get(clazz);
			if (count == null)
				count = 0;
			if (!creature.isInLove() && creature.getGrowingAge() == 0)
				count++;
			entityCount.put(clazz, count);
			if (count == 2){
				if (worldObj.isRemote){
					AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "heart", ((TileEntity)habitat).xCoord + 0.5, ((TileEntity)habitat).yCoord + 0.7, ((TileEntity)habitat).zCoord + 0.5);
					if (particle != null){
						particle.setMaxAge(20);
						particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.05f, 1, false));
					}
				}else{
					creatures = worldObj.getEntitiesWithinAABB(clazz, new AxisAlignedBB(((TileEntity)habitat).xCoord - radius, ((TileEntity)habitat).yCoord - radius, ((TileEntity)habitat).zCoord - radius, ((TileEntity)habitat).xCoord + radius + 1, ((TileEntity)habitat).yCoord + radius + 1, ((TileEntity)habitat).zCoord + radius + 1));
					count = 0;
					for (EntityAnimal animal : creatures){
						if (!animal.isChild()){
							animal.func_146082_f(null);
							count++;
							if (count == 2)
								break;
						}
					}
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers){
		return DoOperation(worldObj, controller, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered){
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		if (powered){
			float base = BASE_COOLDOWN;
			for (Affinity aff : flickers)
				if (aff == Affinity.LIGHTNING)
					base *= COOLDOWN_BONUS;

			return (int)Math.max(base, COOLDOWN_MINIMUM);
		}else{
			return BASE_COOLDOWN;
		}
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers){
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"ELE",
				"EFE",
				"EWE",
				Character.valueOf('E'), Items.egg,
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE),
				Character.valueOf('F'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.LIFE.ordinal()),
				Character.valueOf('W'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE)

		};
	}

}
