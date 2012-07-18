package dataStructures;

import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("serial")
/**
 * A LinkedList of Alternatives
 * 
 * @author Carl
 *
 */
public class AlternativeList extends LinkedList<Alternative>{
	
	/**
	 * Create an empty AlternativeList
	 */
	public AlternativeList(){
		super();
	}
	
	/**
	 * Create an AlternativeList using an existing list
	 * @param list
	 */
	public AlternativeList(LinkedList<Alternative> list){
		super();
		addAll(list);
	}
	
	@Override
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
