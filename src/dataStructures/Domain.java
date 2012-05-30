package dataStructures;

import java.util.Iterator;

public class Domain {
	private DomainValueList values;
	private AttributeKey key;
	
	public Domain(AttributeKey key){
		values=new DomainValueList();
		this.key = key;
	}
	
	public Domain(String enumeration,AttributeKey key){
		values=new DomainValueList();
		this.key = key;
		setValues(enumeration);
	}
	
	public void setValues(String enumeration){
		values.clear();
		String[] split = enumeration.split(",");
		for(String s : split){
			s.trim();
			values.add(new DomainValue(s,key));
		}
	}
	
	public DomainValueList getDomainValueList(){
		return values;
	}

	public void setDomainValueList(DomainValueList newValues){
		this.values = newValues;
	}
	
	public String toString(){
		String toReturn="";
		Iterator<DomainValue> it = values.iterator();
		while(it.hasNext()){
			toReturn+=it.next().toString()+", ";
		}
		return toReturn.substring(0, toReturn.lastIndexOf(','));
	}
}
