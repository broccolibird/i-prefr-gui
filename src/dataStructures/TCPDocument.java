package dataStructures;

import java.io.File;

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
	
	public String getNetworkXML(){
		String net = "\t<NETWORK>\n";
		net += "\t\t<TYPE>TCP</TYPE>\n";
		net += "\t</NETWORK>\n";
		return net;
	}

}
