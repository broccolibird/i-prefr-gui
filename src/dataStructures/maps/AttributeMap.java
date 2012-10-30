package dataStructures.maps;

import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataStructures.Attribute;

@SuppressWarnings("serial")
public class AttributeMap extends SuperkeyMap<Attribute>{
	
	public AttributeMap(int parsedInt) {
		super(parsedInt);
	}
	
	public AttributeMap(){
		super();
	}

	public Element toXML(Document doc){
		
		Element attrsElem = doc.createElement("ATTRIBUTES");
		
		Element mapIDElem = doc.createElement("UNIQUEMAPID");
		mapIDElem.appendChild(doc.createTextNode(Integer.toString(uniqueID)));
		attrsElem.appendChild(mapIDElem);
		
		Element attrElem;
		Set<Entry<Integer, Attribute>> allAttributes = entrySet();
		for(Entry<Integer, Attribute> entry : allAttributes){
			attrElem = entry.getValue().toXML(doc);
			attrsElem.appendChild(attrElem);
		}
		
		return attrsElem;
	}

}
