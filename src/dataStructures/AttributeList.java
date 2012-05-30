package dataStructures;

import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class AttributeList extends LinkedList<Attribute>{
	
	public AttributeList(){
		super();
	}
	
	public AttributeList(LinkedList<Attribute> list){
		super();
		addAll(list);
	}
	
	public String toString(){
		String toReturn = "";
		Iterator<Attribute> it = this.iterator();
		while(it.hasNext()){
			toReturn+=it.next().toString()+", ";			
		}
		if(toReturn.length()>1){
			toReturn = toReturn.substring(0, toReturn.length()-2);
		}
		return "{"+toReturn+"}";
	}
}
