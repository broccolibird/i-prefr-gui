package dataStructures.maps;

import java.util.Set;
import java.util.Map.Entry;

import dataStructures.Alternative;

@SuppressWarnings("serial")
public class AlternativeMap extends SuperkeyMap<Alternative>{
	private boolean useEntireAlternativeSpace;
	
	public AlternativeMap(){
		super();
		this.useEntireAlternativeSpace = true;
	}
	
	public AlternativeMap(int id,boolean useEntireSpace){
		super(id);
		this.useEntireAlternativeSpace=useEntireSpace;
	}
	
	public void setUseEntireAlternativeSpace(boolean newValue){
		this.useEntireAlternativeSpace = newValue;
	}
	
	public boolean useEntireAlternativeSpace(){
		return useEntireAlternativeSpace;
	}
	
	public String toXML(AttributeMap attributeMap){
		String alternatives = "\t<ALTERNATIVES>\n";
		alternatives += "\t\t<UNIQUEMAPID>"+uniqueID+"</UNIQUEMAPID>\n";
		alternatives += "\t\t<USEENTIRE>"+useEntireAlternativeSpace+"</USEENTIRE>\n";
		Set<Entry<Integer, Alternative>> allOptions = entrySet();
		for(Entry<Integer, Alternative> entry : allOptions){
			alternatives += entry.getValue().toXML(attributeMap);
		}
		alternatives += "\t</ALTERNATIVES>\n";
		return alternatives;
	}
	
}
