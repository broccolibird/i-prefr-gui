package dataStructures;


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

	public String toXML() {
		String attribute = "\t\t<ATTRIBUTE ID = '"+key.toString()+"'>\n";
		attribute += "\t\t\t<ISUSED>"+isUsed+"</ISUSED>\n";
		attribute += "\t\t\t<NAME>"+name+"</NAME>\n";
		attribute += "\t\t\t<DOMAIN>\n";
		if(getObject() != null) {
			DomainValueList allValues = getObject().getDomainValueList();
			for(DomainValue dv : allValues){
			attribute += "\t\t\t\t<DOMAINVALUE>"+dv.getValue().toString()+"</DOMAINVALUE>\n";		
			}
		}
		attribute += "\t\t\t</DOMAIN>\n";
		attribute += "\t\t</ATTRIBUTE>\n";
		return attribute;
	}
}
