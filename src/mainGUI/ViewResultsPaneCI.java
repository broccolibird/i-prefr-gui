package mainGUI;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dataStructures.AbstractDocument;
import dataStructures.Alternative;
import dataStructures.Attribute;
import dataStructures.AttributeKey;
import dataStructures.DomainValue;
import dataStructures.maps.AlternativeMap;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ValueMap;

public class ViewResultsPaneCI extends ViewResultsPane{

	public ViewResultsPaneCI(AbstractDocument document, JFrame parentFrame) {
		super(document, parentFrame);
	}

	protected String xmlToText(File prefXml) {
		// Start text file
		String textFile = "";
		textFile += "VARIABLES\n";
		
		// add variable list
		AttributeMap attributeMap = document.getAttributeMap();
		Collection<Attribute> attributes = attributeMap.values();
		for(Attribute attribute : attributes) {
			textFile += attribute + ",";
		}
		
		// remove extra comma
		int lastIndex = textFile.lastIndexOf(',');
		lastIndex = (lastIndex > 0)?lastIndex:textFile.length();
		textFile = textFile.substring(0, lastIndex);
		
		// add preferences header to text file
		textFile += "\nPREFERENCES\n";
		
		
		// Create dom object from prefXml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder prefDBuilder;
		Document prefDoc = null;
		try { 
			prefDBuilder = dbFactory.newDocumentBuilder();
			prefDoc = prefDBuilder.parse(prefXml);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// get list of importances
		Element importances = (Element) (prefDoc
				.getElementsByTagName("IMPORTANCES").item(0));
		NodeList importanceList = importances.getElementsByTagName("IMPORTANCE");
		int nImportances = importanceList.getLength();
		
		// parse importance
		for(int i = 0; i < nImportances; i++) {
			Element importance = (Element) importanceList.item(i);
			int key = Integer.parseInt(importance.getAttribute("ID"));
			NodeList listList = ((Element) ((importance
					.getElementsByTagName("LISTS")).item(0)))
					.getElementsByTagName("LIST");
			
			int nLists = listList.getLength();
			
			// parse importance list
			String[] preference = {"", "", "", ""};
			for(int j = 0; j < nLists; j++) {
				Element list = (Element) listList.item(j);
				int index = Integer.parseInt(Util.getOnlyChildText(list, "INDEX"));
				
				NodeList attributeKeyList = list.getElementsByTagName("ATTRIBUTEKEY");
				int nAttributeKeys = attributeKeyList.getLength();
				for (int k = 0; k < nAttributeKeys; k++) {
					Element attributeKeyElem = (Element) attributeKeyList.item(k);
					Integer attributeKey = Integer.parseInt(attributeKeyElem.getTextContent());
					preference[index] += attributeMap.get(attributeKey).toString() + ',';
				}
				
				// remove ending comma
				lastIndex = preference[index].lastIndexOf(',');
				lastIndex = (lastIndex > 0)?lastIndex:preference[index].length();
				preference[index] = preference[index].substring(0, lastIndex);
			}
			
			// add to text file
			textFile += "{"+preference[0]+"};{"+preference[1]+
					"}:{"+preference[2]+"};{"+preference[3]+"}\n";
		}
		
		
		return textFile;
	}
	
	@Override
	protected String getVariableSet(String line) {
		String[] resultFields = line.split(":");
		return resultFields[1].trim();
	}
	
	protected Alternative getAlternative(String variableSet) {
		AlternativeMap alternativeMap = document.getAlternativeMap();
		if (alternativeMap.useEntireAlternativeSpace())
			return null;
		
		// list of attributes in result
		String[] attributeList = variableSet.split(",");
		
		// remove leading spaces and brackets from attributes
		for(int i=0 ; i < attributeList.length; i++) {	
			attributeList[i] = attributeList[i].replaceAll("\\[|\\]", "");
			attributeList[i] = attributeList[i].trim();
		}
		
		// get keys of all attributes in result
		Collection<Attribute> attributes = document.getAttributeMap().values();
		Integer[] attributeKeyList = new Integer[attributeList.length];
		for(int i = 0; i < attributeList.length; i++) {
			for(Attribute attribute : attributes) {
				if(attributeList[i].compareTo(attribute.getName()) == 0) {
					attributeKeyList[i] = attribute.getKey();
					break;
				}
			}
		}
		
		// compare the variable set to all alternatives until a match is found
		Collection<Alternative> alternatives = alternativeMap.values();
		
		nextAlternative:
		for(Alternative alternative : alternatives) {
			ValueMap alternativeValue = alternative.getObject();
			
			Set<Entry<AttributeKey, DomainValue>> alternativeValueSet = alternativeValue.entrySet();
			Iterator<Entry<AttributeKey, DomainValue>> it = alternativeValueSet.iterator();
			Entry alternativeValueEntry;
			
			// iterate through alternative values 
			// to see if this alternative is a match
			while(it.hasNext()) {
				alternativeValueEntry = it.next();
				Integer key = ((AttributeKey)alternativeValueEntry.getKey()).getKey();
				
				// see if key is in attribute key list
				for(int i = 0; i < attributeKeyList.length; i++) {
					if(key == attributeKeyList[i]){
						if(((DomainValue) alternativeValueEntry.getValue()).getValue().compareTo("1") == 0){
							break; //matching alternative value, continue checking
						} else {
							continue nextAlternative; 
						}	
					//attribute is not in input set, verify attribute value in Alternative is 0	
					} else if( i == attributeKeyList.length - 1) { 
						if(((DomainValue)alternativeValueEntry.getValue()).getValue().compareTo("0") == 0) {
							break; //matches
						} else {
							continue nextAlternative;
						}
					}
				}
			}
			return alternative;
		}
		
		return null;
	}
	
}
