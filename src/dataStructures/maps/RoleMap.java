package dataStructures.maps;

import java.util.Set;
import java.util.Map.Entry;

import dataStructures.Role;

@SuppressWarnings("serial")
public class RoleMap extends SuperkeyMap<Role>{
	
	private boolean isMultipleStakeholder;
	
	public RoleMap(int parsedInt) {
		super(parsedInt);
	}
	
	public RoleMap(boolean isMultipleStakeholder){
		super();
		this.isMultipleStakeholder = isMultipleStakeholder;
	}
	
	public boolean isMultipleStakeholder(){
		return isMultipleStakeholder;
	}
	
	public String toXML() {
		String roles = "\t<STAKEHOLDERS>\n";
		roles += "\t\t<MULTISTAKEHOLDER>"+isMultipleStakeholder+"</MULTISTAKEHOLDER>\n";
		roles += "\t\t<UNIQUEMAPID>"+uniqueID+"</UNIQUEMAPID>\n";
		Set<Entry<Integer, Role>> allRoles = entrySet();
		for(Entry<Integer, Role> entry : allRoles){
			roles += entry.getValue().toXML();
		}
		roles += "\t</STAKEHOLDERS>\n";
		return roles;
	}
}
