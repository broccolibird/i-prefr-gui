package mainGUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dataStructures.CIDocument;
import dataStructures.TCPDocument;

@SuppressWarnings("serial")
public class PreferenceReasoner extends JApplet {
	private static AbstractPaneTurner paneTurner;
	private static JFrame frame;
	public static boolean loading;

	public static void main(String[] args) {
		// new JDialog: name your project
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(900, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		paneTurner = null;
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				showSaveDialog();
			}
		});
		
		JMenu newMenu = new JMenu("New");
		newMenu.add(new AbstractAction("(T)CP-NET") {
			public void actionPerformed(ActionEvent e) {
				if (paneTurner != null) {
					//TODO - offer to save old abstractDocument before opening new one
				}
				paneTurner = new PaneTurnerTCP(frame, new TCPDocument());
				frame.getContentPane().add(paneTurner);
				frame.pack();
			}
		});
		newMenu.add(new AbstractAction("CI-NET") {
			public void actionPerformed(ActionEvent e) {
				if (paneTurner != null) {
					//TODO -  offer to save old abstractDocument before opening new one
				}
				paneTurner = new PaneTurnerCI(frame, new CIDocument());
				frame.getContentPane().add(paneTurner);
				frame.pack();
			}
		});
		fileMenu.add(newMenu);
		fileMenu.add(new AbstractAction("Open") {
			public void actionPerformed(ActionEvent e) {
				//TODO -  offer to save old abstractDocument before opening new one
				
				
				//use a chooser to get the file to open
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			    		"XML (*.xml)","xml");
			    chooser.setFileFilter(filter);
			    int option = chooser.showOpenDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					open(file);
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.pack();
	}
	
	private static void showSaveDialog(){
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showSaveDialog(paneTurner);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			save(file);
		}
	}

	private static void save(File xmlfile) {
		String xmlRepresentation = paneTurner.toXML();
		System.out.println(xmlRepresentation);
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(xmlfile)); 
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
		try {
		    writer.write(xmlRepresentation);
		}
		catch(IOException e) {
		    e.printStackTrace();
		}
		try {
		    writer.close();
		}
		catch(IOException e) {
		    e.printStackTrace();
		}
	}

	private static void open(File file) {
		loading = true;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc=null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NodeList nList = doc.getElementsByTagName("NETWORK");
		Element e = (Element)nList.item(0);
		NodeList nList2 = (e).getElementsByTagName("TYPE");
		Element e2 = (Element)nList2.item(0);
		String networkType="";
		if(e2==null){
			System.out.println("null element");
		}else{
			networkType = e2.getTextContent();
			networkType = networkType.trim();
		}
		 
		System.out.println("netType!!: "+networkType);
		if(networkType.equals("CI")){
			//frame.removeAll();
			CIDocument oldCIDocument = new CIDocument(doc);
			loading = false;
			paneTurner = new PaneTurnerCI(frame, oldCIDocument);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}else if(networkType.equals("TCP")){
			//frame.removeAll();
			TCPDocument oldTCPDocument = new TCPDocument(doc);
			loading = false;
			paneTurner = new PaneTurnerTCP(frame, oldTCPDocument);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}

		// ...
		// same for TCPDocument
	}
}
