package dataStructures;

public class CIDocument extends AbstractDocument {

	public CIDocument(boolean isMultiStakeholder) {
		super(isMultiStakeholder);
	}

	public CIDocument(org.w3c.dom.Document doc) {
		super(doc);
	}

	public String getNetworkXML() {
		String net = "\t<NETWORK>\n";
		net += "\t\t<TYPE>CI</TYPE>\n";
		//net += importanceMap.toXML();
		net += "\t</NETWORK>\n";
		return net;
	}

	@Override
	public boolean isCINetworkType() {
		return true;
	}

}
