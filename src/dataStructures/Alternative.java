package dataStructures;

import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataStructures.maps.AttributeMap;
import dataStructures.maps.ValueMap;


public class Alternative extends
		NameKeyObject<ValueMap> implements
		Comparable<Alternative> {

	/**
	 * Create a new Alternative instance
	 * @param name
	 * @param key
	 * @param values
	 */
	public Alternative(String name, Integer key, ValueMap values) {
		super(name, key, values);
		//System.out.println("alternative created with name: "+name+" key: "+key+" value: "+values);
	}

	@Override
	public int compareTo(Alternative other) {
		return this.getKey().compareTo(other.getKey());
	}
	
	/**
	 * Create an xml representation of the Alternative
	 * @param attributeMap
	 * @return xml string representation of the Alternative
	 */
	public Element toXML(AttributeMap attributeMap, Document doc){
		Element altElem = doc.createElement("ALTERNATIVE");
		Attr idAttr = doc.createAttribute("ID");
		idAttr.setValue(key.toString());
		altElem.setAttributeNode(idAttr);
		
		Element nameElem = doc.createElement("NAME");
		nameElem.appendChild(doc.createTextNode(name));
		altElem.appendChild(nameElem);
		
		Element valsElem = doc.createElement("VALUES");
		
		Element valElem;
		Set<Entry<AttributeKey, DomainValue>> allValues = getObject().entrySet();
		for(Entry<AttributeKey, DomainValue> entry : allValues){
			valElem = doc.createElement("VALUE");
			
			Element attrKeyElem = doc.createElement("ATTRIBUTEKEY");
			attrKeyElem.appendChild(doc.createTextNode(entry.getKey().getKey().toString()));
			valElem.appendChild(attrKeyElem);
			
			Element domValElem = doc.createElement("DOMAINVALUE");
			domValElem.appendChild(doc.createTextNode(entry.getValue().getValue().toString()));
			valElem.appendChild(domValElem);
			
			valsElem.appendChild(valElem);
		}
		
		altElem.appendChild(valsElem);
		
		return altElem;
	}
	
	/**
	 * Return the expanded string representation of this Alternative
	 * @param attributeMap
	 * @return expanded string representation
	 */
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
