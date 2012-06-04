package dataStructures;

import java.util.Iterator;
import java.util.LinkedList;

public class MemberList {
	private LinkedList<String> names;
	private Integer key;
	
	public MemberList(Integer key){
		names = new LinkedList<String>();
		this.key = key;
	}
	
	public MemberList(String enumeration, Integer key){
		names = new LinkedList<String>();
		this.key = key;
		setNames(enumeration);
	}
	
	public void setNames(String enumeration){
		names.clear();
		String[] split = enumeration.split(",");
		for(String s : split){
			s.trim();
			names.add(s);
		}
	}
	
	public LinkedList<String> getMemberList(){
		return names;
	}

	public void setMemberList(LinkedList<String> newNames){
		this.names = newNames;
	}
	
	public String toString(){
		String toReturn="";
		Iterator<String> it = names.iterator();
		while(it.hasNext()){
			toReturn+=it.next().toString()+", ";
		}
		return toReturn.substring(0, toReturn.lastIndexOf(','));
	}
}
