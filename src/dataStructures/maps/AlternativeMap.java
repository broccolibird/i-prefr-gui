package dataStructures.maps;

import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	
	public Element toXML(AttributeMap attributeMap, Document doc){
		Element altsElem = doc.createElement("ALTERNATIVES");
		
		Element uniqueIDElem = doc.createElement("UNIQUEMAPID");
		uniqueIDElem.appendChild(doc.createTextNode(Integer.toString(uniqueID)));
		altsElem.appendChild(uniqueIDElem);
		
		Element useElem = doc.createElement("USEENTIRE");
		useElem.appendChild(doc.createTextNode(Boolean.toString(useEntireAlternativeSpace)));
		altsElem.appendChild(useElem);
		
		Element altElem;
		Set<Entry<Integer, Alternative>> allOptions = entrySet();
		for(Entry<Integer, Alternative> entry : allOptions){
			altElem = entry.getValue().toXML(attributeMap, doc);
			altsElem.appendChild(altElem);
		}
		
		return altsElem;
	}
	
}
