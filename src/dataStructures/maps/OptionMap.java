package dataStructures.maps;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings("serial")
public class OptionMap extends HashMap<String,Integer>{
	public static final String SAME_NAME = "SAME_NAME";
	
	
	public String toXML(){
		String optionMap = "\t\t<DISPLAYOPTIONS>"+"\n";
		Set<Entry<String, Integer>> allOptions = entrySet();
		for(Entry<String,Integer> entry : allOptions){
			optionMap += "\t\t\t<OPTION ID = '"+entry.getKey()+"'>"+entry.getValue().toString()+"</OPTION>\n";
		}
		optionMap += "\t\t</DISPLAYOPTIONS>\n";
		return optionMap;
	}

}
