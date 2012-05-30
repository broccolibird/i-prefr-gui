package dataStructures;

public class TCPDocument extends AbstractDocument{
	
	public TCPDocument(){
		super();
	}
	public TCPDocument(org.w3c.dom.Document doc){
		super(doc);
		//TODO - parse network into graph, pass graph into GUI
	}
	
	public String getNetworkXML(){
		String net = "\t<NETWORK>\n";
		net += "\t\t<TYPE>TCP</TYPE>\n";
		net += "\t</NETWORK>\n";
		return net;
	}

}
