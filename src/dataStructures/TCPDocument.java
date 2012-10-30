package dataStructures;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TCPDocument extends AbstractDocument{
	
	/**
	 * Create new TCPDocument instance
	 * @param isMultiStakeholder
	 */
	public TCPDocument(boolean isMultiStakeholder){
		super(isMultiStakeholder);
	}
	
	/**
	 * Create new TCPDocument instance based on saved
	 * document
	 * @param doc
	 */
	public TCPDocument(File projectFolder, org.w3c.dom.Document doc){
		super(projectFolder, doc);
		//TODO - parse network into graph, pass graph into GUI
	}
	
	public Element getNetworkXML(Document doc){
		Element netElem = doc.createElement("NETWORK");
		
		Element typeElem = doc.createElement("TYPE");
		typeElem.appendChild(doc.createTextNode("TCP"));
		netElem.appendChild(typeElem);

		return netElem;
	}

}
