package dataStructures.maps;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class OptionMap extends HashMap<String,Integer>{
	public static final String SAME_NAME = "SAME_NAME";
	
	
	public Element toXML(Document doc){
		Element dispOptionsElem = doc.createElement("DISPLAYOPTIONS");
		
		Element option;
		
		Set<Entry<String, Integer>> allOptions = entrySet();
		for(Entry<String,Integer> entry : allOptions){
			option = doc.createElement("OPTION");
			
			Attr idAttr = doc.createAttribute("ID");
			idAttr.setValue(entry.getKey());
			option.setAttributeNode(idAttr);
			
			option.appendChild(doc.createTextNode(entry.getValue().toString()));
		}
		return dispOptionsElem;
	}

}
