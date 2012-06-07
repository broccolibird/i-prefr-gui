package dataStructures;

import java.util.Iterator;
import java.util.LinkedList;

public class MemberList extends LinkedList<Member> {
	private Integer key;
	
	public MemberList(Integer key){
		super();
		this.key = key;
	}
	
	public MemberList(String enumeration, Integer key){
		super();
		this.key = key;
		setNames(enumeration);
	}
	
	public void setNames(String enumeration){
		clear();
		String[] split = enumeration.split(",");
		for(String s : split){
			s.trim();
			add(new Member(s, key));
		}
	}
	
	public LinkedList<Member> getMemberList(){
		return this;
	}

	public void setMemberList(LinkedList<Member> newNames){
		clear();
		addAll(newNames);
	}
	
	public String toString(){
		String toReturn="";
		Iterator<Member> it = this.iterator();
		while(it.hasNext()){
			toReturn+=it.next().toString()+", ";
		}
		return toReturn.substring(0, toReturn.lastIndexOf(','));
	}
}
