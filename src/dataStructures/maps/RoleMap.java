package dataStructures.maps;

import graph.RoleHierarchy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	public Element toXML(File projectFolder, Document doc) {
		Element rolesElem = doc.createElement("STAKEHOLDERS");
		
		Element uniqueIDElem = doc.createElement("UNIQUEMAPID");
		uniqueIDElem.appendChild(doc.createTextNode(Integer.toString(uniqueID)));
		rolesElem.appendChild(uniqueIDElem);
		
		Element multiElem = doc.createElement("MULTISTAKEHOLDER");
		multiElem.appendChild(doc.createTextNode(Boolean.toString(isMultipleStakeholder)));
		rolesElem.appendChild(multiElem);
		
		Element roleFile = doc.createElement("ROLEFILE");
		roleFile.appendChild(doc.createTextNode(createRoleFile(projectFolder)));
		rolesElem.appendChild(roleFile);
		
		if ( isMultipleStakeholder ) {
			Element hierarchyElem = doc.createElement("HIERARCHYFILE");
			String hierarchyFile = createHierarchyFile(projectFolder);
			if(hierarchyFile != null) {
				hierarchyElem.appendChild(doc.createTextNode(createHierarchyFile(projectFolder)));
			}
			rolesElem.appendChild(hierarchyElem);
		}
		
		return rolesElem;
	}
	
	/**
	 * Create a single Role Element to add to export xml.
	 * @param doc
	 * @return
	 */
	/*public Element toExportXML(Document doc) {
		Element rolesElem = doc.createElement("STAKEHOLDERS");
		
		Element uniqueIDElem = doc.createElement("UNIQUEMAPID");
		uniqueIDElem.appendChild(doc.createTextNode(Integer.toString(uniqueID)));
		rolesElem.appendChild(uniqueIDElem);
		
		Element multiElem = doc.createElement("MULTISTAKEHOLDER");
		multiElem.appendChild(doc.createTextNode(Boolean.toString(isMultipleStakeholder)));
		rolesElem.appendChild(multiElem);
		
		Element roleFile = doc.createElement("ROLEFILE");
		roleFile.appendChild(doc.createTextNode(createRoleFile(projectFolder)));
		rolesElem.appendChild(roleFile);
		
		if ( isMultipleStakeholder ) {
			Element hierarchyElem = doc.createElement("HIERARCHYFILE");
			String hierarchyFile = createHierarchyFile(projectFolder);
			if(hierarchyFile != null) {
				hierarchyElem.appendChild(doc.createTextNode(createHierarchyFile(projectFolder)));
			}
			rolesElem.appendChild(hierarchyElem);
		}
		
		return rolesElem;
		return null;
	}*/
	
	private String createRoleFile(File projectFolder) {
		String fileName = "roles.xml";
		String roleFileName = projectFolder + System.getProperty("file.separator") + fileName;
		File roleFile = new File(roleFileName);
		
		System.out.println("Creating role file @ "+roleFileName+"\n");
		
		// create xml document
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		
		Document doc = docBuilder.newDocument();
		
		// create root element
		Element rolesElem = doc.createElement("ROLES");
		doc.appendChild(rolesElem);
				
				
		Set<Entry<Integer, Role>> allRoles = entrySet();
		for(Entry<Integer, Role> entry : allRoles){
			Element roleElem = entry.getValue().toXML(projectFolder, doc);
			rolesElem.appendChild(roleElem);
		}
		
		// write xml to file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;
				try {
					transformer = transformerFactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
					return null;
				}
				
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(roleFile);
				
				try {
					transformer.transform(source,  result);
				} catch (TransformerException e) {
					e.printStackTrace();
					return null;
				}
		
		return fileName;
	}
	
	private String createHierarchyFile(File projectFolder) {
		String fileName = "hierarchy.xml";
		String hierarchyFileName = projectFolder + System.getProperty("file.separator") + fileName;
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
		
		return fileName;
	}
}
