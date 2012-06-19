package dataStructures.maps;

import graph.RoleHierarchy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import dataStructures.Member;
import dataStructures.MemberList;
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
		MemberList ml = new MemberList(0);
		ml.add(m);
		Role r = new Role("default", 0, ml);
		put(0, r);
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
	
	public String toXML(File xmlfile) {
		String roles = "\t<STAKEHOLDERS>\n";
		roles += "\t\t<UNIQUEMAPID>"+uniqueID+"</UNIQUEMAPID>\n";
		roles += "\t\t<MULTISTAKEHOLDER>"+isMultipleStakeholder+"</MULTISTAKEHOLDER>\n";
		
		String roleFile = createRoleFile(xmlfile);
		roles += "\t\t<ROLEFILE>"+roleFile+"</ROLEFILE>\n";
		
		if ( isMultipleStakeholder )
			roles += "\t\t<HIERARCHYFILE>"+createHierarchyFile(xmlfile)+"</HIERARCHYFILE>\n";
		
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
	
	private String createHierarchyFile(File xmlfile) {
		int suffixIndex = xmlfile.getAbsolutePath().lastIndexOf('.');
		String filePrefix = (suffixIndex >= 0) ?
				xmlfile.getAbsolutePath().substring(0, suffixIndex) : xmlfile.getAbsolutePath();
		String hierarchyFileName = filePrefix + "-hierarchy.xml";
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
