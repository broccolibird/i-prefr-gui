package dataStructures.maps;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import mainGUI.PreferenceReasoner;

@SuppressWarnings("serial")
public class SuperkeyMap<A> extends TreeMap<Integer, A> {
	private static int nextUniqueID = 0;
	protected int uniqueID;
	private ArrayList<Integer> keySuperset;
	private boolean saved;
	
	public static void setNextUniqueID(int newUniqueID){
		nextUniqueID = newUniqueID;
	}
	
	public static int getNextUniqueID(){
		return nextUniqueID;
	}

	
	public SuperkeyMap() {
		super();
		keySuperset = new ArrayList<Integer>();
		this.uniqueID = nextUniqueID++;
		saved = true;
	}

	public SuperkeyMap(int uniqueID) {
		super();
		this.uniqueID = uniqueID;
		this.keySuperset = new ArrayList<Integer>();
		saved = true;
	}

	public Integer getUniqueKey() {
		Integer i = 0;
		while (i < Integer.MAX_VALUE) {
			if (!keySuperset.contains(i)) {
				keySuperset.add(i);
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	public A put(Integer key, A value) {
		A toReturn = super.put(key, value);
		if (!keySuperset.contains(key)) {
			keySuperset.add(key);
		} else if (PreferenceReasoner.loading) {
			// this is not necessarily bad behavior, as sometimes a value needs
			// to be replaced
			// this should never happen during loading or a value will be lost.
			System.err.println("in SuperkeyMap, key " + key.toString()
					+ " was used to add value: " + value.toString()
					+ " replacing " + toReturn.toString()
					+ " durring a load proceedure.");
		}
		saved = false;
		return toReturn;
	}

	public void clearUnusedKeys() {
		Set<Integer> usedKeys = this.keySet();
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (Integer I : keySuperset)
			if (!usedKeys.contains(I))
				toRemove.add(I);
		keySuperset.removeAll(toRemove);
	}

	@Override
	public A remove(Object key) {
		if (key.getClass() != Integer.class)
			throw new IllegalArgumentException();
		keySuperset.remove(key);
		saved = false;
		return super.remove(key);
	}
	
	public int getUniqueID(){
		return uniqueID;
	}

	@Override
	public String toString() {
		String toReturn = "TupleMap: [ ";
		Set<Entry<Integer, A>> set = this.entrySet();
		for (Entry<Integer, A> e : set)
			toReturn += "key: " + e.getKey().toString() + " value: "
					+ e.getValue().toString() + " | ";
		toReturn += "keySuperset: ";
		for (Integer I : keySuperset)
			toReturn += I.toString() + " ";
		toReturn += "]";
		return toReturn;
	}

	@Override
	public int hashCode() {
		// because edges are EdgeStatementMaps, which are SuperkeyMaps, which
		// are keys to a map of edges and their endpoints and the return value
		// of super().hashcode() will change after an edge is annotated, it must
		// not be included in the hashcode function, as that will generate a new
		// and thus incorrect key when trying to fetch the endpoints of an edge
		// from the SparseMultigraph's 'edges' hashmap.
		return uniqueID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		SuperkeyMap other = (SuperkeyMap) obj;
		if (uniqueID != other.uniqueID)
			return false;
		return true;
	}
	
	public boolean existUnsavedChanges() {
		return !saved;
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
}
