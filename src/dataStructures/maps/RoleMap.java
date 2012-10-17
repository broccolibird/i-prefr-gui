package dataStructures.maps;

import graph.RoleHierarchy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import dataStructures.Member;
import dataStructures.Role;

@SuppressWarnings("serial")
public class RoleMap extends SuperkeyMap<Role>{
	
	private boolean isMultipleStakeholder;
	private RoleHierarchy roleHierarchy = null;
	
	/**
	 * Create a new RoleMap instance. Used when loading project from a file
	 *
	 * @param mapID
	 * @param isMultipleStakeholder
	 */
	public RoleMap(int mapID, boolean isMultipleStakeholder){
		super(mapID);
		this.isMultipleStakeholder = isMultipleStakeholder;
	}
	
	/**
	 * Create a new RoleMap instance
	 * 
	 * @param isMultipleStakeholder
	 */
	public RoleMap(boolean isMultipleStakeholder){
		super();
		this.isMultipleStakeholder = isMultipleStakeholder;
		if(!isMultipleStakeholder) {
			addDefaultRoleMember();
		}
	}
	
	private void addDefaultRoleMember(){
		Member m = new Member("default", 0);
		MemberMap map = new MemberMap();
		map.put(0, m);
		Role r = new Role("default", 0, map);
		put(0, r);
		setSaved(true); //default member should not be a "change"
	}
	
	public boolean isMultipleStakeholder(){
		return isMultipleStakeholder;
	}
	
	public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
		this.roleHierarchy = roleHierarchy;
	}
	
	public RoleHierarchy getRoleHierarchy() {
		return roleHierarchy;
	}
	
	@Override
	public boolean existUnsavedChanges() {
		// check if the map has unsaved changes
		if(!saved) 
			return true;
		
		// check if each role has unsaved changes
		Collection<Role> allRoles = values();
		for(Role role : allRoles){
			if (role.getObject().existUnsavedChanges())
				return true;
		}
		
		// there are no unsaved changes
		return false;
		
	}
	
	@Override
	public void setSaved(boolean saved) {
		this.saved = saved;
		if(saved) {
			Collection<Role> allRoles = values();
			for(Role role : allRoles){
				role.getObject().setSaved(saved);
			}
		}
	}
	
	/**
	 * Create xml for Role information.
	 * 
	 * @param projectFolder
	 * @return xml
	 */
	public String toXML(File projectFolder) {
		String roles = "\t<STAKEHOLDERS>\n";
		roles += "\t\t<UNIQUEMAPID>"+uniqueID+"</UNIQUEMAPID>\n";
		roles += "\t\t<MULTISTAKEHOLDER>"+isMultipleStakeholder+"</MULTISTAKEHOLDER>\n";
		
		String roleFile = createRoleFile(projectFolder);
		roles += "\t\t<ROLEFILE>"+roleFile+"</ROLEFILE>\n";
		
		if ( isMultipleStakeholder )
			roles += "\t\t<HIERARCHYFILE>"+createHierarchyFile(projectFolder)+"</HIERARCHYFILE>\n";
		
		roles += "\t</STAKEHOLDERS>\n";
		return roles;
	}
	
	private String createRoleFile(File projectFolder) {
		String roleFileName = projectFolder + System.getProperty("file.separator") + "roles.xml";
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
	
	private String createHierarchyFile(File projectFolder) {
		String hierarchyFileName = projectFolder + System.getProperty("file.separator") + "hierarchy.xml";
		File hierarchyFile = new File(hierarchyFileName);
		
		System.out.println("Creating hierarchy file @ "+hierarchyFileName+"\n");

		String hierarchyXML = roleHierarchy.toXML();
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(hierarchyFile));
			writer.write(hierarchyXML);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return hierarchyFileName;
	}
}
