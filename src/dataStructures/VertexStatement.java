package dataStructures;

import dataStructures.maps.ConditionMap;
import dataStructures.maps.DomainPreferenceMap;

public class VertexStatement extends Pair<ConditionMap,DomainPreferenceMap>{

	public VertexStatement(ConditionMap left, DomainPreferenceMap right) {
		super(left, right);
	}

}
