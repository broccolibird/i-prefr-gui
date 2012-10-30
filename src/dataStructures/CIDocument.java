package dataStructures;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CIDocument extends AbstractDocument {

	/**
	 * Create new CIDocument instance
	 * @param isMultiStakeholder
	 */
	public CIDocument(boolean isMultiStakeholder) {
		super(isMultiStakeholder);
	}

	/**
	 * Create new CIDocument instance based on saved
	 * document
	 * @param doc
	 */
	public CIDocument(File projectFolder, org.w3c.dom.Document doc) {
		super(projectFolder, doc);
	}

	@Override
	public Element getNetworkXML(Document doc) {
		Element netElem = doc.createElement("NETWORK");
		
		Element typeElem = doc.createElement("TYPE");
		typeElem.appendChild(doc.createTextNode("CI"));
		netElem.appendChild(typeElem);
				
		return netElem;
	}

}
