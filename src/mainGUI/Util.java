package mainGUI;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Util {

	public static int maxOf(int int1, int int2){
		if(int1>int2)
			return int1;
		else
			return int2;
	}
	
	public static String getOnlyChildText(org.w3c.dom.Document doc,String parent,String child){
		NodeList parentList = doc.getElementsByTagName(parent);
		Element parentElement = (Element)parentList.item(0);
		return getOnlyChildText(parentElement,child);
	}
	
	public static String getOnlyChildText(Element parentElement,String child){
		String childString="";
		NodeList childList = parentElement.getElementsByTagName(child);
		Element onlyChild = (Element)childList.item(0);
		if(onlyChild==null){
			System.err.println("null element");
		}else{
			childString = onlyChild.getTextContent();
			childString = childString.trim();
		}
		return childString;
	}
	
}
