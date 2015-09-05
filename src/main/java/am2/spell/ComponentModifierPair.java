package am2.spell;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;

public class ComponentModifierPair{
	public ArrayList<Integer> components;
	public ListMultimap<Integer, byte[]> modifiers;

	public ComponentModifierPair(){
		components = new ArrayList<Integer>();
		modifiers = ArrayListMultimap.create();
	}

	public void addComponent(int id){
		components.add(id);
	}

	public void addModifier(int id, byte[] metadata){
		modifiers.put(id, metadata);
	}

	public int[] getComponents(){
		int[] ret = new int[components.size()];
		for (int i = 0; i < components.size(); ++i){
			ret[i] = components.get(i);
		}
		return ret;
	}

	public ListMultimap<Integer, byte[]> getModifiers(){
		return modifiers;
	}
}
