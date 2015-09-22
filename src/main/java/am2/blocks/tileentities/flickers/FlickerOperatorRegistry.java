package am2.blocks.tileentities.flickers;

import am2.AMCore;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.flickers.IFlickerRegistry;
import am2.api.spell.enums.Affinity;

import java.util.TreeMap;

public class FlickerOperatorRegistry implements IFlickerRegistry{

	private final TreeMap<Integer, IFlickerFunctionality> registeredOperators = new TreeMap<Integer, IFlickerFunctionality>();
	public static final FlickerOperatorRegistry instance = new FlickerOperatorRegistry();

	private FlickerOperatorRegistry(){
	}

	@Override
	public boolean registerFlickerOperator(IFlickerFunctionality singleton, int mask){
		if (registeredOperators.containsKey(mask)){
			AMCore.log.warn(String.format("An addon attempted to register a flicker operator (%s) with a mask (%d) that is already in use.  The operator was NOT registered!", singleton.getClass().getName(), mask));
			return false;
		}
		registeredOperators.put(mask, singleton);
		AMCore.log.debug(String.format("Registered Flicker operator %s to mask %d", singleton.getClass().getName(), mask));
		return true;
	}

	public boolean registerFlickerOperator(IFlickerFunctionality singleton, Affinity... affinities){
		int mask = 0;
		for (Affinity aff : affinities)
			mask |= aff.getAffinityMask();
		return registerFlickerOperator(singleton, mask);
	}

	public int getMaskForOperator(IFlickerFunctionality operator){
		for (Integer i : registeredOperators.keySet()){
			IFlickerFunctionality op = registeredOperators.get(i);
			if (op == operator)
				return i;
		}
		return 0;
	}

	public IFlickerFunctionality getOperatorForMask(int mask){
		return registeredOperators.get(mask);
	}

	public int[] getMasks(){
		int[] maskList = new int[registeredOperators.size()];
		int count = 0;
		for (Integer i : registeredOperators.keySet()){
			maskList[count++] = i != null ? i.intValue() : -1;
		}
		return maskList;
	}

}
