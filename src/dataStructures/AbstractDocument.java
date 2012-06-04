package dataStructures;


import mainGUI.Util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dataStructures.maps.AlternativeMap;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.OptionMap;
import dataStructures.maps.RoleMap;
import dataStructures.maps.SuperkeyMap;
import dataStructures.maps.ValueMap;

public abstract class AbstractDocument {

	protected AttributeMap attributeMap;
	protected AlternativeMap alternativeMap;
	protected RoleMap roleMap;
	protected MetaData metaData;

	public AbstractDocument(boolean isMultiStakeholder) {
		attributeMap = new AttributeMap();
		alternativeMap = new AlternativeMap();
		if (isMultiStakeholder)
			roleMap = new RoleMap();
		metaData = new MetaData();
	}
	
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

	public AttributeMap getAttributeMap() {
		return attributeMap;
	}

	public AlternativeMap getAlternativeMap() {
		return alternativeMap;
	}

	public RoleMap getRoleMap(){
		return roleMap;
	}
	
	public MetaData getMetaData() {
		return metaData;
	}
	
	public abstract String getNetworkXML();
	
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
	public String toXML() {
		String doc = "<DOCUMENT>\n";
		doc += metaData.toXML();
		doc += attributeMap.toXML();
		doc += alternativeMap.toXML(attributeMap);
		doc += getNetworkXML();
		doc += "</DOCUMENT>";
		return doc;
	}
}
