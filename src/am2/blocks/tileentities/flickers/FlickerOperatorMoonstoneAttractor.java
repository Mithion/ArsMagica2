package am2.blocks.tileentities.flickers;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.math.AMVector3;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;

public class FlickerOperatorMoonstoneAttractor implements IFlickerFunctionality{

	private static final ArrayList<AMVector3> attractors = new ArrayList<AMVector3>();

	public static AMVector3 getMeteorAttractor(AMVector3 target){
		for (AMVector3 attractor : attractors.toArray(new AMVector3[attractors.size()])){
			if (attractor.distanceSqTo(target) <= 16384)
				return attractor.copy();
		}
		return null;
	}

	@Override
	public boolean RequiresPower() {
		return true;
	}

	@Override
	public int PowerPerOperation() {
		return 10;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered) {
		AMVector3 vec = new AMVector3((TileEntity)habitat);
		if (powered){
			if (!attractors.contains(vec)){
				attractors.add(vec);
			}
			return true;
		}else{
			attractors.remove(vec);
		}
		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered) {
		AMVector3 vec = new AMVector3((TileEntity)habitat);
		attractors.remove(vec);
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers) {
		return 100;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
		RemoveOperator(worldObj, habitat, powered);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
			"RLR",
			"AME",
			"I T",
			Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_ORANGE),
			Character.valueOf('L'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.LIGHTNING.ordinal()),
			Character.valueOf('A'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.ARCANE.ordinal()),
			Character.valueOf('E'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.EARTH.ordinal()),
			Character.valueOf('M'), new ItemStack(BlocksCommonProxy.AMOres, 1 ,BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK),
			Character.valueOf('I'), new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_AIR),
			Character.valueOf('T'), new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH)
		};
	}

}
