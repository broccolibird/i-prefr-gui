package dataStructures.maps;

import java.util.Map.Entry;
import java.util.Set;

import dataStructures.Attribute;

@SuppressWarnings("serial")
public class AttributeMap extends SuperkeyMap<Attribute>{
	
	public AttributeMap(int parsedInt) {
		super(parsedInt);
	}
	
	public AttributeMap(){
		super();
	}

	public String toXML(){
		String attributes = "\t<ATTRIBUTES>\n";
		attributes += "\t\t<UNIQUEMAPID>"+uniqueID+"</UNIQUEMAPID>\n";
		Set<Entry<Integer, Attribute>> allAttributes = entrySet();
		for(Entry<Integer, Attribute> entry : allAttributes){
			attributes += entry.getValue().toXML();
		}
		attributes += "\t</ATTRIBUTES>\n";
		return attributes;
	}

}
