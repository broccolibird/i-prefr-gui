package dataStructures;

import java.util.LinkedList;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Importance {

	private AttributeList[] lists;
	private Integer key;
	
	public Importance(Integer key){
		this.key=key;
		lists = new AttributeList[4];
	}
	
	public AttributeList getList(int index){
		return lists[index];
	}
	
	public void setList(int index, LinkedList<Attribute> newList){
		lists[index]=new AttributeList(newList);
	}
	
	public Integer getKey(){
		return key;
	}
	
	public Element toXML(Document doc){
		Element impElem = doc.createElement("IMPORTANCE");
		Attr idAttr = doc.createAttribute("ID");
		idAttr.setValue(key.toString());
		impElem.setAttributeNode(idAttr);
		
		Element listsElem = doc.createElement("LISTS");
		
		Element listElem;
		for(int i=0;i<lists.length;i++){
			listElem = doc.createElement("LIST");
			
			Element indexElem = doc.createElement("INDEX");
			indexElem.appendChild(doc.createTextNode(Integer.toString(i)));
			listElem.appendChild(indexElem);
			
			Element attrKeyElem;
			AttributeList list = lists[i];
			if(list!=null){
				for(Attribute a : list){
					attrKeyElem = doc.createElement("ATTRIBUTEKEY");
					attrKeyElem.appendChild(doc.createTextNode(a.getAttributeKey().getKey().toString()));
					listElem.appendChild(attrKeyElem);
				}
			}
			listsElem.appendChild(listElem);
		}
		
		impElem.appendChild(listsElem);
		
		return impElem;
	}
	
}
