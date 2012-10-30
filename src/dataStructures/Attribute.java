package dataStructures;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Attribute extends NameKeyObject<Domain> implements
		Comparable<Attribute>, Vertex {
	private AttributeKey attributeKey;
	private boolean isUsed;

	public Attribute(String name, AttributeKey key, Domain object) {
		super(name, key.getKey(), object);
		attributeKey = key;
		isUsed = false;
	}

	@Override
	public int compareTo(Attribute other) {
		return this.getName().toLowerCase()
				.compareTo(other.getName().toLowerCase());
	}

	public AttributeKey getAttributeKey() {
		return attributeKey;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Element toXML(Document doc) {
		Element attrElem = doc.createElement("ATTRIBUTE");
		Attr idAttr = doc.createAttribute("ID");
		idAttr.setValue(key.toString());
		attrElem.setAttributeNode(idAttr);
		
		Element usedElem = doc.createElement("ISUSED");
		usedElem.appendChild(doc.createTextNode(Boolean.toString(isUsed)));
		attrElem.appendChild(usedElem);
		
		Element nameElem = doc.createElement("NAME");
		nameElem.appendChild(doc.createTextNode(name));
		attrElem.appendChild(nameElem);
		
		Element domainElem = doc.createElement("DOMAIN");
		
		Element domValElem;
		if(getObject() != null) {
			DomainValueList allValues = getObject().getDomainValueList();
			for(DomainValue dv : allValues){
				domValElem = doc.createElement("DOMAINVALUE");
				domValElem.appendChild(doc.createTextNode(dv.getValue().toString()));
				domainElem.appendChild(domValElem);	
			}
		}
		
		attrElem.appendChild(domainElem);
		
		return attrElem;
	}
}
