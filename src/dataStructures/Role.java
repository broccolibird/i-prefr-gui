package dataStructures;


import java.io.File;
import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	public Element toXML(File projectFolder, Document doc) {
		Element roleElem = doc.createElement("ROLE");
		Attr roleIDAttr = doc.createAttribute("ID");
		roleIDAttr.setValue(key.toString());
		roleElem.setAttributeNode(roleIDAttr);
		
		Element usedElem = doc.createElement("ISUSED");
		usedElem.appendChild(doc.createTextNode(Boolean.toString(isUsed)));
		roleElem.appendChild(usedElem);
		
		Element titleElem = doc.createElement("TITLE");
		titleElem.appendChild(doc.createTextNode(name));
		roleElem.appendChild(titleElem);
		
		Element membersElem = doc.createElement("MEMBERS");
		
		Collection<Member> allMembers = (Collection<Member>) getObject().values();
		for(Member m : allMembers){
			Element memberElem = m.toXML(projectFolder, doc);
			membersElem.appendChild(memberElem);
		}
		
		roleElem.appendChild(membersElem);
		
		return roleElem;
		
	}
}
