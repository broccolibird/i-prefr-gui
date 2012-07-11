package dataStructures;


import graph.RoleHierarchy;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mainGUI.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dataStructures.maps.AlternativeMap;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.MemberMap;
import dataStructures.maps.OptionMap;
import dataStructures.maps.RoleMap;
import dataStructures.maps.SuperkeyMap;
import dataStructures.maps.ValueMap;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;

/**
 * The Document contains all of the data for the project, 
 * including: AttributeMap, AlternativeMap, RoleMap and 
 * MetaData
 * 
 * @author Carl and Kat
 *
 */
public abstract class AbstractDocument {

	protected AttributeMap attributeMap;
	protected AlternativeMap alternativeMap;
	protected RoleMap roleMap;
	protected MetaData metaData;
	
	int roleEdge = 0;

	/**
	 * Create a new AbstractDocument
	 * @param isMultiStakeholder
	 */
	public AbstractDocument(boolean isMultiStakeholder) {
		attributeMap = new AttributeMap();
		alternativeMap = new AlternativeMap();
		roleMap = new RoleMap(isMultiStakeholder);
		metaData = new MetaData();
	}
	
	/**
	 * Create a new AbstractDocument based on a saved
	 * document
	 * @param doc
	 */
	public AbstractDocument(org.w3c.dom.Document doc){
		
		//first create the attributeMap
		int uniqueMapID = Integer.parseInt(Util.getOnlyChildText(doc,"ATTRIBUTES","UNIQUEMAPID"));
		int maxUniqueID = uniqueMapID;
		attributeMap = new AttributeMap(uniqueMapID);
		
		//populate it with attributes
		NodeList attributeList = doc.getElementsByTagName("ATTRIBUTE");
		int nAttributes = attributeList.getLength();
		for(int i=0;i<nAttributes;i++){
			boolean thisIsUsed;
			String thisName;
			Integer thisKey;
			Domain thisObject;
			
			//get the attribute key
			Element attribute = (Element)attributeList.item(i);
			thisKey = Integer.parseInt(attribute.getAttribute("ID"));
			AttributeKey thisAttributeKey = new AttributeKey(thisKey);
			
			//create the list of domain values (each needs the attributekey)
			thisObject = new Domain(thisAttributeKey);
			DomainValueList thisList = new DomainValueList();
//			NodeList domainList = attribute.getElementsByTagName("DOMAIN");
//			Element domainNode = (Element)domainList.item(0);
			NodeList domainValueList = attribute.getElementsByTagName("DOMAINVALUE");
			int nDomainValues = domainValueList.getLength();
			for(int j=0;j<nDomainValues;j++){
				String domainValue = ((Element)domainValueList.item(j)).getTextContent().trim();
				thisList.add(new DomainValue(domainValue,thisAttributeKey));
			}
			thisObject.setDomainValueList(thisList);
			
			//create an attribute from the above with the correct name
			thisName = Util.getOnlyChildText(attribute,"NAME");
			Attribute thisValue = new Attribute(thisName,thisAttributeKey,thisObject);
			
			//set its isUsed boolean and put into map
			thisIsUsed = Boolean.parseBoolean(Util.getOnlyChildText(attribute,"ISUSED"));
			thisValue.setUsed(thisIsUsed);
			attributeMap.put(thisKey, thisValue);
		}
		
		//then create the alternativeMap
		uniqueMapID = Integer.parseInt(Util.getOnlyChildText(doc,"ALTERNATIVES","UNIQUEMAPID"));
		maxUniqueID = Util.maxOf(maxUniqueID,uniqueMapID);
		boolean useEntire = Boolean.parseBoolean(Util.getOnlyChildText(doc,"ALTERNATIVES","USEENTIRE"));
		alternativeMap = new AlternativeMap(uniqueMapID,useEntire);
		
		//populate it with Alternatives
		NodeList alternativeList = doc.getElementsByTagName("ALTERNATIVE");
		int nAlternatives = alternativeList.getLength();
		for(int i=0;i<nAlternatives;i++){
			String thisName;
			Integer thisKey;
			ValueMap thisObject;
			
			//get the alternative key and name
			Element alternative = (Element)alternativeList.item(i);
			thisKey = Integer.parseInt(alternative.getAttribute("ID"));
			thisName = Util.getOnlyChildText(alternative,"NAME");
			
			//create a valueMap and then put the alternative into the map
			NodeList valuesList = alternative.getElementsByTagName("VALUES");
			NodeList valueList = ((Element)valuesList.item(0)).getElementsByTagName("VALUE");
			int nValues = valueList.getLength();
			thisObject = new ValueMap();
			for(int j = 0;j<nValues;j++){
				Element valueElement = (Element)valueList.item(j);
				AttributeKey thisAttributeKey = new AttributeKey(Integer.parseInt(Util.getOnlyChildText(valueElement,"ATTRIBUTEKEY")));
				DomainValue thisValue = new DomainValue(Util.getOnlyChildText(valueElement,"DOMAINVALUE"),thisAttributeKey);
				thisObject.put(thisAttributeKey, thisValue);
			}
			alternativeMap.put(thisKey, new Alternative(thisName,thisKey,thisObject));
		}

		// initialize RoleMap
		maxUniqueID = setupRoleMap(doc, maxUniqueID);
		
		//now initialize the MetaData
		Element metaDataElement = (Element)((doc.getElementsByTagName("METADATA")).item(0));
		String filename = Util.getOnlyChildText(metaDataElement,"FILENAME");
		String projectName = Util.getOnlyChildText(metaDataElement,"PROJECTNAME");
		String modelChecker = Util.getOnlyChildText(metaDataElement,"MODELCHECKER");
		String dateCreated = Util.getOnlyChildText(metaDataElement,"DATECREATED");
		Element displayOptions = (Element)(metaDataElement.getElementsByTagName("DISPLAYOPTIONS")).item(0);
		NodeList optionList = displayOptions.getElementsByTagName("OPTION");
		int nOptions = optionList.getLength();
		OptionMap optionMap = new OptionMap();
		for(int i=0;i<nOptions;i++){
			Element option = (Element)optionList.item(i);
			String thisKey = option.getAttribute("ID");
			Integer thisValue = Integer.parseInt(option.getTextContent());
			optionMap.put(thisKey, thisValue);
		}
		metaData = new MetaData(filename, projectName,modelChecker,dateCreated,optionMap);
		SuperkeyMap.setNextUniqueID(maxUniqueID+1);
	}

	/**
	 * Create a RoleMap based on a document
	 * @param doc
	 * @param maxUniqueID
	 * @return new maxUniqueID
	 */
	private int setupRoleMap(org.w3c.dom.Document doc, int maxUniqueID) {
		//create the roleMap
		int uniqueMapID = Integer.parseInt(Util.getOnlyChildText(doc, "STAKEHOLDERS", "UNIQUEMAPID"));
		maxUniqueID = Util.maxOf(maxUniqueID, uniqueMapID);
		boolean multistakeholder = Boolean.parseBoolean(Util.getOnlyChildText(doc, "STAKEHOLDERS", "MULTISTAKEHOLDER"));
		roleMap = new RoleMap(uniqueMapID, multistakeholder);
				
		//open roles file
		String roleFile = Util.getOnlyChildText(doc, "STAKEHOLDERS", "ROLEFILE");
		Document roleDoc = null;
		DocumentBuilder dBuilder;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			roleDoc = dBuilder.parse(roleFile);
					
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//populate roleMap
		NodeList roleList = roleDoc.getElementsByTagName("ROLE");
		int nRoles = roleList.getLength();
		for(int i=0;i<nRoles;i++){
			boolean thisIsUsed;
			String roleTitle;
			Integer roleKey;
			MemberMap memberMap;
					
			//get the key
			Element roleNode = (Element)roleList.item(i);
			roleKey = Integer.parseInt(roleNode.getAttribute("ID"));
								
			//create the list of members (each needs the key)
			memberMap = new MemberMap();
					
			NodeList memberNList = roleNode.getElementsByTagName("MEMBER");
			int nMembers = memberNList.getLength();
			int thisKey;
			for(int j=0;j<nMembers;j++){
				Element member = (Element) memberNList.item(j);
				thisKey = Integer.parseInt(member.getAttribute("ID"));
				String memberName = member.getElementsByTagName("NAME").item(0).getTextContent();
				NodeList preferenceFile = member.getElementsByTagName("PREFERENCEFILE");
				if (preferenceFile.item(0) != null) { // Member already has a preference file
					String preferenceFilePath = preferenceFile.item(0).getTextContent();
					memberMap.put(thisKey, new Member (memberName, thisKey, preferenceFilePath));
				} else {
					memberMap.put(thisKey, new Member(memberName,thisKey));
				}
			}
					
			//create a role from the above with the correct name
			roleTitle = Util.getOnlyChildText(roleNode,"TITLE");
			Role thisValue = new Role(roleTitle,roleKey,memberMap);
					
			//set its isUsed boolean and put into map
			thisIsUsed = Boolean.parseBoolean(Util.getOnlyChildText(roleNode,"ISUSED"));
			thisValue.setUsed(thisIsUsed);
			roleMap.put(roleKey, thisValue);
		}
		
		if ( multistakeholder ) {
			//open role hierarchy file
			String hierarchyFile = Util.getOnlyChildText(doc, "STAKEHOLDERS", "HIERARCHYFILE");
			Document hierarchyDoc = null;
			DocumentBuilder hierarchyDBuilder;
			DocumentBuilderFactory hierarchyDBFactory = DocumentBuilderFactory.newInstance();
			try {
				hierarchyDBuilder = hierarchyDBFactory.newDocumentBuilder();
				hierarchyDoc = hierarchyDBuilder.parse(hierarchyFile);
								
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			RoleHierarchy rh = new RoleHierarchy(roleMap);
			StaticLayout sl = new StaticLayout(rh);
			rh.setLayout(sl);
			roleMap.setRoleHierarchy(rh);
			
			//populate role hierarchy
			roleList = hierarchyDoc.getElementsByTagName("ROLE");
			nRoles = roleList.getLength();
			for(int i=0;i<nRoles;i++){
				Element thisRole = (Element) roleList.item(i);
				createRoleHierarchy(thisRole, rh, sl);
			}
		}
		return maxUniqueID;
	}
	
	/**
	 * Creates a RoleHierarchy based on a document
	 * @param thisRole
	 * @param rh
	 * @param sl
	 */
	private void createRoleHierarchy(Element thisRole, RoleHierarchy rh, StaticLayout sl){
		String roleTitle = thisRole.getElementsByTagName("TITLE").item(0).getTextContent();
		
		int roleKey = Integer.parseInt(thisRole.getAttribute("ID"));
		
		// retrieve role based on ID
		Role roleToAdd = roleMap.get(roleKey);
		
		// add role to graph
		rh.addVertex(roleToAdd);
		
		// set location of vertex
		Element coordElem = (Element) thisRole.getElementsByTagName("COORDINATES").item(0);
		double xcoord = Double.parseDouble(coordElem.getElementsByTagName("X").item(0).getTextContent());
		double ycoord = Double.parseDouble(coordElem.getElementsByTagName("Y").item(0).getTextContent());
		Point2D location = new Point2D.Double(xcoord,ycoord);
		sl.setLocation(roleToAdd, location);
		
		// set annotation if it has one
		NodeList annotationNL = thisRole.getElementsByTagName("ANNOTATION");
		if (annotationNL.getLength() > 0) {
			String annotation = annotationNL.item(0).getTextContent();
			rh.addSavedAnnotation(new SavedAnnotation(annotation, roleToAdd, location));
		}
		
		// link node to its superiors
		NodeList superiorList = thisRole.getElementsByTagName("SUPERIOR");
		for( int i=0; i<superiorList.getLength(); i++) {
			Element parentElem = (Element) superiorList.item(i);
			int parentKey = Integer.parseInt(parentElem.getAttribute("ID"));
			Role parentRole = roleMap.get(parentKey);
			
			rh.addEdge(roleEdge++, parentRole, roleToAdd);
		}
		
		// update nextEdge to unused value
		rh.setNextEdge(roleEdge);
		
	}
	
	/**
	 * Returns the AttributeMap
	 * @return attributeMap
	 */
	public AttributeMap getAttributeMap() {
		return attributeMap;
	}

	/**
	 * Returns the AlternativeMap
	 * @return alternativeMap
	 */
	public AlternativeMap getAlternativeMap() {
		return alternativeMap;
	}

	/**
	 * Returns the RoleMap
	 * @return roleMap
	 */
	public RoleMap getRoleMap(){
		return roleMap;
	}
	
	/**
	 * Returns the MetaData
	 * @return metaDAta
	 */
	public MetaData getMetaData() {
		return metaData;
	}
	
	public abstract boolean isCINetworkType();
	
	public abstract String getNetworkXML();
	
	/**
	 * Returns true if the Document or any of its subcomponents have
	 * changed since the last save.
	 * @return true if unsaved changes exist
	 */
	public boolean existChanges() {
		
		boolean changes = metaData.existChanges();
		changes |= attributeMap.existUnsavedChanges();
		changes |= alternativeMap.existUnsavedChanges();
		changes |= roleMap.existUnsavedChanges();
		
		System.out.println("changes:\n"+
				"\tmetadata: "+ metaData.existChanges()+
				"\n\tattribute: "+attributeMap.existUnsavedChanges()+
				"\n\talternative: "+alternativeMap.existUnsavedChanges()+
				"\n\trole: "+roleMap.existUnsavedChanges());
		
		return changes;
	}
	
	/**
	 * Set all subcomponents to saved if saved is true
	 * @param saved
	 */
	public void setSaved(boolean saved) {
		if(saved) {
			metaData.setSaved(true);
			attributeMap.setSaved(true);
			alternativeMap.setSaved(true);
			roleMap.setSaved(true);
		}
	}
	
	//Some protocol for writing 'toXML()' functions:
	//all tags should be all caps for simplicity
	//the depth of an element determines the number of tabs, as expected
	//every line is written on a single line with the correct
	//number of tabs before it and a newline after it
	//only leaf elements have closing tags on the same line as the opening
	//tag
	//calls to functions that create a bit of XML do not get
	//tabs or newlines put onto them - those chars are added in the function
	//ID attributes are used only for keys of a map value and not for other
	//numeric identifiers, which should have specific tags
	/**
	 * Creates an xml string of the current document
	 * @param xmlfile - file to be saved to
	 * @return xml string
	 */
	public String toXML(File xmlfile) {
		String doc = "<DOCUMENT>\n";
		doc += metaData.toXML();
		doc += attributeMap.toXML();
		doc += alternativeMap.toXML(attributeMap);
		doc += getNetworkXML();
		doc += roleMap.toXML(xmlfile);
		doc += "</DOCUMENT>";
		return doc;
	}

	
}
