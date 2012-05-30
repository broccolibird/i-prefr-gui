package dataStructures.maps;

import java.util.Collection;

import dataStructures.ConditionElement;


@SuppressWarnings("serial")
public class ConditionMap extends SuperkeyMap<ConditionElement>{	
	
	@Override
	public String toString(){
		String toReturn="";
		Collection<ConditionElement> conditionElements = values();
		for(ConditionElement ce : conditionElements){
			toReturn+=ce.toString()+" && ";
			System.out.println("in ConditionMap, adding string: "+ce.toString());
		}
		if(toReturn.length()>3){
			toReturn = toReturn.substring(0, toReturn.length()-4);
		}
		return toReturn;
	}
}
