package dataStructures.maps;

import java.util.Collection;

import dataStructures.DomainPreference;

@SuppressWarnings("serial")
public class DomainPreferenceMap extends SuperkeyMap<DomainPreference> {

	@Override
	public String toString() {
		String toReturn = "";
		Collection<DomainPreference> preferences = values();
		for (DomainPreference dps : preferences) {
			toReturn += " " + dps.toString() + ", ";
		}
		if (toReturn.length() > 1) {
			toReturn = toReturn.substring(0, toReturn.length() - 2);
		}
		return toReturn;
	}

}
