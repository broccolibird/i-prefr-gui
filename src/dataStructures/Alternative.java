package dataStructures;

import java.util.Map.Entry;
import java.util.Set;

import dataStructures.maps.AttributeMap;
import dataStructures.maps.ValueMap;


public class Alternative extends
		NameKeyObject<ValueMap> implements
		Comparable<Alternative> {

	public Alternative(String name, Integer key, ValueMap values) {
		super(name, key, values);
		//System.out.println("alternative created with name: "+name+" key: "+key+" value: "+values);
	}

	@Override
	public int compareTo(Alternative other) {
		return this.getKey().compareTo(other.getKey());
	}
	
	public String toXML(AttributeMap attributeMap){
		String alternative = "\t\t<ALTERNATIVE ID = '"+key.toString()+"'>\n";
		alternative += "\t\t\t<NAME>"+name+"</NAME>\n";
		alternative += "\t\t\t<VALUES>\n";
		Set<Entry<AttributeKey, DomainValue>> allValues = getObject().entrySet();
		for(Entry<AttributeKey, DomainValue> entry : allValues){
			alternative += "\t\t\t\t<VALUE>\n";
			alternative += "\t\t\t\t\t<ATTRIBUTEKEY>"+entry.getKey().getKey().toString()+"</ATTRIBUTEKEY>\n";
			alternative += "\t\t\t\t\t<DOMAINVALUE>"+ entry.getValue().getValue().toString()+"</DOMAINVALUE>\n";
			alternative += "\t\t\t\t</VALUE>\n";
		}
		alternative += "\t\t\t</VALUES>\n";		
		alternative += "\t\t</ALTERNATIVE>\n";
		return alternative;
	}
	
	public String toExpandedString(AttributeMap attributeMap){
		String toReturn = "";
		Set<Entry<AttributeKey, DomainValue>> set = getObject().entrySet();
		for(Entry<AttributeKey, DomainValue> entry : set){
			toReturn += attributeMap.get(entry.getKey()).getName() + " = " + entry.getValue().toString() + ", ";
		}
		if(toReturn.length()>1){
			toReturn = toReturn.substring(0, toReturn.length()-2);
		}
		return toReturn;
	}

	@Override
	public String toString() {
		return name;
	}

}
