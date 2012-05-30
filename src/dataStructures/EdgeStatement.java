package dataStructures;

import dataStructures.maps.ConditionMap;

public class EdgeStatement extends Pair<ConditionMap, AttributePreference>{

	public EdgeStatement(ConditionMap left, AttributePreference right) {
		super(left, right);
	}
	
	@Override
	public String toString(){
		String toReturn ="if (";
		toReturn+=left.toString()+") then: ";
		toReturn+=right.toString();
		return toReturn;
	}

}
