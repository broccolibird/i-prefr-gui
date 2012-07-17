package dataStructures;

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
	public CIDocument(org.w3c.dom.Document doc) {
		super(doc);
	}

	@Override
	public String getNetworkXML() {
		String net = "\t<NETWORK>\n";
		net += "\t\t<TYPE>CI</TYPE>\n";
		//net += importanceMap.toXML();
		net += "\t</NETWORK>\n";
		return net;
	}

}
