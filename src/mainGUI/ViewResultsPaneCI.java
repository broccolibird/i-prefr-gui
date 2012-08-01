package mainGUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import reasoner.AcyclicPreferenceReasoner;
import translate.PreferenceInputTranslator;
import translate.PreferenceInputTranslatorFactory;
import translate.PreferenceInputType;

import dataStructures.AbstractDocument;
import dataStructures.Alternative;
import dataStructures.Attribute;
import dataStructures.AttributeKey;
import dataStructures.DomainValue;
import dataStructures.maps.AlternativeMap;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ValueMap;

public class ViewResultsPaneCI extends ViewResultsPane{

	String ciNetFileName;

	public ViewResultsPaneCI(AbstractDocument document, JFrame parentFrame) {
		super(document, parentFrame);
	}

	protected void initReasoner(String xmlFileName) {
		ciNetFileName = xmlToText(new File(xmlFileName)).getAbsolutePath();
		PreferenceInputTranslator translator = PreferenceInputTranslatorFactory.createTranslator(PreferenceInputType.CInet);
		String fileName = null;
		try {
			fileName = translator.convertToSMV(ciNetFileName, 0);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reasoner = new AcyclicPreferenceReasoner(fileName);
	}
	
	protected void dominance() {
		if(reasoner == null) {
			JOptionPane.showMessageDialog(parentFrame,
				    "The current user has no preferences.\n" +
				    "Please verify on the Setup Preferences pane that preferences have " +
				    "been added and saved.",
				    "Preferences Not Found",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(leftAlternative == null || rightAlternative == null) {
			return;
		}
		Set<String> morePreferredSet = getCISet(leftAlternative);
		Set<String> lessPreferredSet = getCISet(rightAlternative);
		
		System.out.println("More: " + morePreferredSet);
		System.out.println("Less: " + lessPreferredSet);
		
		boolean dominates = false;
		try {
			dominates = reasoner.dominates(morePreferredSet, lessPreferredSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dominanceField.setText(""+dominates);
	}
	

	public Set<String> getCISet(Alternative alternative) {
		AttributeMap attributeMap = document.getAttributeMap();
		Set<String> ciSet = new HashSet<String>();
		Collection<Entry<AttributeKey, DomainValue>> attributeValues = alternative.getObject().entrySet();
		for(Entry<AttributeKey, DomainValue> entry : attributeValues) {
			AttributeKey key = (AttributeKey) entry.getKey();
			DomainValue value = (DomainValue) entry.getValue();
			
			if(value.getValue().equals("1") ) {
				ciSet.add(attributeMap.get(key).getName());
			}
		}
		return ciSet;
	}
	
	protected void topNext() {
		
		if(reasoner == null) {
			JOptionPane.showMessageDialog(parentFrame,
				    "The current user has no preferences.\n" +
				    "Please verify on the Setup Preferences pane that preferences have " +
				    "been added and saved.",
				    "Preferences Not Found",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(document.getAlternativeMap().useEntireAlternativeSpace()) {
			String resultSet = getNextPreferred();
			System.out.println("resultSet= "+resultSet);
			if(resultSet == null) 
				addEndOfResults();
			else
				addResult(resultSet);
		} else {
			boolean alternativeFound = false;
			String resultSet;
			while(!alternativeFound) {
				resultSet = getNextPreferred();
				System.out.println("resultSet= "+resultSet);
				if(resultSet == null) {
					addEndOfResults();
					alternativeFound = true;
				} else {
					Alternative alt = getAlternative(resultSet);
					if(alt != null) {
						String resultString = alt.getName() + " " + resultSet;
						addResult(resultString);
						alternativeFound = true;
					}
				}
			}
		}
		
	}
	
	protected String getNextPreferred() {
		Set<String> prefResult;
		try {
			prefResult = reasoner.nextPreferred();
		} catch (Exception e) {
			System.err.println("Thread handling "+ciNetFileName+" encountered exception:");
			e.printStackTrace();
			return null;
		}
		
		if(prefResult == null) { // nulls separate different results of the same rank
			try {
				prefResult = reasoner.nextPreferred();
			} catch (Exception e) {
				System.err.println("Thread handling "+ciNetFileName+" encountered exception:");
				e.printStackTrace();
				return null;
			}
			
			//two nulls in a row define the end of the ranking
			return (prefResult==null)?null:prefResult.toString();
		}
		return prefResult.toString();
	}
	
	protected void checkConsistency() {
		
		if(reasoner == null) {
			JOptionPane.showMessageDialog(parentFrame,
				    "There is no preference file to check for consistency.\n" +
				    "Please verify on the Setup Preferences pane that preferences have " +
				    "been added and saved.",
				    "Preferences Not Found",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		boolean consistent;
		try {
			consistent = reasoner.isConsistent();
		} catch (Exception e) {
			System.err.println("Thread handling "+ciNetFileName+" encountered exception:");
			e.printStackTrace();
			return;
		}
		
		if(consistent) {
			consistencyField.setText("consistent");
		} else {
			consistencyField.setText("inconsistent");
		}
		
	}
	
	
	protected File xmlToText(File prefXml) {
		// Start converting to text
		String text = "";
		text += "VARIABLES\n";
		
		// add variable list
		AttributeMap attributeMap = document.getAttributeMap();
		Collection<Attribute> attributes = attributeMap.values();
		for(Attribute attribute : attributes) {
			text += attribute + ",";
		}
		
		// remove extra comma
		int lastIndex = text.lastIndexOf(',');
		lastIndex = (lastIndex > 0)?lastIndex:text.length();
		text = text.substring(0, lastIndex);
		
		// add preferences header to text file
		text += "\nPREFERENCES\n";
		
		
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
			text += "{"+preference[0]+"};{"+preference[1]+
					"}:{"+preference[2]+"};{"+preference[3]+"}\n";
		}
		
		String textFileLocation = prefXml.getAbsolutePath();
		int ending = textFileLocation.lastIndexOf('.');
		textFileLocation = textFileLocation
				.substring(0, (ending>0)?ending:textFileLocation.length())+"-text.xml";
		File textFile= new File(textFileLocation);
		
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(textFile));
		    writer.write(text);
		}
		catch (IOException e) {
		    e.printStackTrace();
		    // save failed, return null
		    return null;
		}
		
		try {
		    writer.close();
		}
		catch(IOException e) {
		    e.printStackTrace();
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
