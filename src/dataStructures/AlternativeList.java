package dataStructures;

import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class AlternativeList extends LinkedList<Alternative>{
	
	public AlternativeList(){
		super();
	}
	
	public AlternativeList(LinkedList<Alternative> list){
		super();
		addAll(list);
	}
	
	public String toString(){
		String toReturn = "";
		Iterator<Alternative> it = this.iterator();
		while(it.hasNext()){
			toReturn+=it.next().toString()+", ";
		}
		if(toReturn.length()>1){
			toReturn = toReturn.substring(0, toReturn.length()-2);
		}
		return "{"+toReturn+"}";
	}
}
