package dataStructures;

public class Role extends NameKeyObject<MemberList>{

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

	public String toXML() {
		String role = "\t\t<ROLE ID = '"+key.toString()+"'>\n";
		role += "\t\t\t<ISUSED>"+isUsed+"</ISUSED>\n";
		role += "\t\t\t<TITLE>"+name+"</TITLE>\n";
		role += "\t\t\t<MEMBERS>\n";
		MemberList allMembers = (MemberList) getObject().getMemberList();
		for(Member m : allMembers){
		role += "\t\t\t\t<MEMBER>"+m.getName()+"</MEMBER>\n";		
		}
		role += "\t\t\t</MEMBERS>\n";
		role += "\t\t</ROLE>\n";
		return role;
		
	}
}
