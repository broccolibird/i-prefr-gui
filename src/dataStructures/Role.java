package dataStructures;


import java.util.Collection;

import dataStructures.maps.MemberMap;

/**
 * Contains information about each Role, including a SuperkeyMap of
 * stakeholders (members)
 */
public class Role extends NameKeyObject<MemberMap>
		implements Vertex {

	// Role has been added to the RoleHierarchy graph
	private boolean isUsed;

	public Role(String name, Integer key, MemberMap object) {
		super(name, key, object);
		isUsed = false;
	}
	
	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public Collection<Member> getMemberCollection() {
		if (object == null)
			return null;
		
		return object.values();
	}

	/**
	 * Creates xml for -roles.xml file
	 * @return contents for -roles.xml file
	 */
	public String toXML() {
		String role = "\t<ROLE ID = '"+key.toString()+"'>\n";
		role += "\t\t<ISUSED>"+isUsed+"</ISUSED>\n";
		role += "\t\t<TITLE>"+name+"</TITLE>\n";
		role += "\t\t<MEMBERS>\n";
		Collection<Member> allMembers = (Collection<Member>) getObject().values();
		for(Member m : allMembers){
			role += m.toXML();		
		}
		role += "\t\t</MEMBERS>\n";
		role += "\t</ROLE>\n";
		return role;
		
	}
}
