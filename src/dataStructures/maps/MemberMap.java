package dataStructures.maps;

import java.util.Collection;

import dataStructures.Member;

@SuppressWarnings("serial")
public class MemberMap extends SuperkeyMap<Member>{

	@Override
	public String toString() {
		Collection<Member> members = values();
		String toReturn = "";
		for(Member member : members) {
			toReturn += member.toString()+", ";
		}
		int lastIndex = toReturn.lastIndexOf(',');
		return (lastIndex >= 0) ? toReturn.substring(0, lastIndex) : "";
	}
}
