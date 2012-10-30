package dataStructures.maps;

import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataStructures.Importance;

@SuppressWarnings("serial")
public class ImportanceMap extends SuperkeyMap<Importance>{
	
	public ImportanceMap(){
		super();
	}
	
	public ImportanceMap(int uniqueID){
		super(uniqueID);
	}

	public Document toXML(){
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
		Element rootElement = doc.createElement("IMPORTANCES");
		doc.appendChild(rootElement);
		
		Element uniqueIDElem = doc.createElement("UNIQUEMAPID");
		uniqueIDElem.appendChild(doc.createTextNode(Integer.toString(uniqueID)));
		rootElement.appendChild(uniqueIDElem);
		
		Element impElem;
		
		Set<Entry<Integer, Importance>> allAttributes = entrySet();
		for(Entry<Integer, Importance> entry : allAttributes){
			impElem = entry.getValue().toXML(doc);
			rootElement.appendChild(impElem);
		}
		
		return doc;
	}
	
}
