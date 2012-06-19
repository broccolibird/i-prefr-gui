package dataStructures;

/**
 * Contains information about each Role, including a LinkedList of
 * stakeholders (members)
 * 
 * @author Kat
 *
 */
public class Role extends NameKeyObject<MemberList>{

	// Role has been added to the RoleHierarchy graph
	private boolean isUsed;

	public Role(String name, Integer key, MemberList object) {
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

	/**
	 * Creates xml for -roles.xml file
	 * @return contents for -roles.xml file
	 */
	public String toXML() {
		String role = "\t<ROLE ID = '"+key.toString()+"'>\n";
		role += "\t\t<ISUSED>"+isUsed+"</ISUSED>\n";
		role += "\t\t<TITLE>"+name+"</TITLE>\n";
		role += "\t\t<MEMBERS>\n";
		MemberList allMembers = (MemberList) getObject().getMemberList();
		for(Member m : allMembers){
			role += "\t\t\t<MEMBER>\n";
			role += "\t\t\t\t<NAME>"+m.getName()+"</NAME>\n";
			if(m.getPreferenceFilePath() != null)
				role += "\t\t\t\t<PREFERENCEFILE>"+m.getPreferenceFilePath()+
							"</PREFERENCEFILE>\n";
			role += "\t\t\t</MEMBER>\n";		
		}
		role += "\t\t</MEMBERS>\n";
		role += "\t</ROLE>\n";
		return role;
		
	}
}
