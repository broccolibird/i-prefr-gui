package dataStructures.maps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import dataStructures.Role;

@SuppressWarnings("serial")
public class RoleMap extends SuperkeyMap<Role>{
	
	private boolean isMultipleStakeholder;
	
	public RoleMap(int mapID, boolean isMultipleStakeholder){
		super(mapID);
		this.isMultipleStakeholder = isMultipleStakeholder;
	}
	
	public RoleMap(boolean isMultipleStakeholder){
		super();
		this.isMultipleStakeholder = isMultipleStakeholder;
	}
	
	public boolean isMultipleStakeholder(){
		return isMultipleStakeholder;
	}
	
	public String toXML(File xmlfile) {
		String roles = "\t<STAKEHOLDERS>\n";
		roles += "\t\t<UNIQUEMAPID>"+uniqueID+"</UNIQUEMAPID>\n";
		roles += "\t\t<MULTISTAKEHOLDER>"+isMultipleStakeholder+"</MULTISTAKEHOLDER>\n";
		String roleFile = createRoleFile(xmlfile);
		roles += "\t\t<ROLEFILE>"+roleFile+"</ROLEFILE>\n";
		roles += "\t\t<HIERARCHYFILE>"+""+"</HIERARCHYFILE>\n";
		
		roles += "\t</STAKEHOLDERS>\n";
		return roles;
	}
	
	private String createRoleFile(File xmlfile) {
		int suffixIndex = xmlfile.getAbsolutePath().lastIndexOf('.');
		String filePrefix = (suffixIndex >= 0) ?
				xmlfile.getAbsolutePath().substring(0, suffixIndex) : xmlfile.getAbsolutePath();
		String roleFileName = filePrefix + "-roles.xml";
		File roleFile = new File(roleFileName);
		
		System.out.println("Creating role file @ "+roleFileName+"\n");
		
		String roleXML = "<ROLES>\n";
		Set<Entry<Integer, Role>> allRoles = entrySet();
		for(Entry<Integer, Role> entry : allRoles){
			roleXML += entry.getValue().toXML();
		}
		roleXML += "</ROLES>\n";
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(roleFile));
		    writer.write(roleXML);
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return roleFileName;
	}
}
