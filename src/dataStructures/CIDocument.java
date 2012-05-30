package dataStructures;

import mainGUI.Util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dataStructures.maps.ImportanceMap;
import dataStructures.maps.SuperkeyMap;

public class CIDocument extends AbstractDocument {

	private ImportanceMap importanceMap;

	public CIDocument() {
		super();
		this.importanceMap = new ImportanceMap();
	}

	public CIDocument(org.w3c.dom.Document doc) {
		super(doc);

		// create the importanceMap
		Element network = (Element) ((doc.getElementsByTagName("NETWORK"))
				.item(0));
		Element importances = (Element) ((network
				.getElementsByTagName("IMPORTANCES")).item(0));
		int uniqueID = Integer.parseInt(Util.getOnlyChildText(importances,
				"UNIQUEMAPID"));
		importanceMap = new ImportanceMap(uniqueID);

		// populate it with importances
		NodeList importanceList = importances
				.getElementsByTagName("IMPORTANCE");
		int nImportances = importanceList.getLength();
		for (int i = 0; i < nImportances; i++) {

			// each importance has 4 lists
			Element importance = (Element) importanceList.item(i);
			int thisKey = Integer.parseInt(importance.getAttribute("ID"));
			NodeList listList = ((Element) ((importance
					.getElementsByTagName("LISTS")).item(0)))
					.getElementsByTagName("LIST");
			int nLists = listList.getLength();
			Importance thisValue = new Importance(thisKey);
			for (int j = 0; j < nLists; j++) {
				Element list = (Element) listList.item(j);
				int index = Integer.parseInt(Util.getOnlyChildText(list,
						"INDEX"));

				// and each list has 0 to many attribute keys
				AttributeList thisList = new AttributeList();
				NodeList attributeKeyList = list
						.getElementsByTagName("ATTRIBUTEKEY");
				int nAttributeKeys = attributeKeyList.getLength();
				for (int k = 0; k < nAttributeKeys; k++) {
					Element thisAttributeKey = (Element) attributeKeyList
							.item(k);
					Integer attributeKey = Integer.parseInt(thisAttributeKey
							.getTextContent());
					thisList.add(super.attributeMap.get(attributeKey));
				}
				thisValue.setList(index, thisList);
			}
			importanceMap.put(thisKey, thisValue);
		}
		SuperkeyMap.setNextUniqueID(Util.maxOf(uniqueID,
				SuperkeyMap.getNextUniqueID()));
	}

	public ImportanceMap getImportanceMap() {
		return importanceMap;
	}

	public String getNetworkXML() {
		String net = "\t<NETWORK>\n";
		net += "\t\t<TYPE>CI</TYPE>\n";
		net += importanceMap.toXML();
		net += "\t</NETWORK>\n";
		return net;
	}

}
