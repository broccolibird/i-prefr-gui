package mainGUI;

import guiElements.NewDialog;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dataStructures.CIDocument;
import dataStructures.RunConfiguration;
import dataStructures.TCPDocument;

@SuppressWarnings("serial")
public class PreferenceReasoner extends JApplet {
	
	private static AbstractPaneTurner paneTurner;
	private static JFrame frame;
	public static boolean loading;
	
	private static File curFile;

	public static void main(String[] args) {
		// new JDialog: name your project
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(900, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		paneTurner = null;
		curFile = null;
		
		// Setup menu options
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenuItem item;
		
		item = fileMenu.add("Save");
		item.setMnemonic(KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				if(paneTurner == null) {
					JOptionPane.showMessageDialog(frame, "Please create a project before saving.",
							"Project does not exist", JOptionPane.PLAIN_MESSAGE);
				} else if (curFile == null){
					showSaveDialog();
				} else {
					save(curFile);
				}
			}
		});
		
		item = fileMenu.add("SaveAs");
		item.addActionListener( new AbstractAction("SaveAs") {
			public void actionPerformed(ActionEvent e){
				if(paneTurner == null)
					JOptionPane.showMessageDialog(frame, "Please create a project before saving.",
							"Project does not exist", JOptionPane.PLAIN_MESSAGE);
				else
					showSaveDialog();
			}
		});
		
		item = fileMenu.add("New");
		item.setMnemonic(KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("New") {
			public void actionPerformed(ActionEvent e) {
				if (paneTurner != null ) {
					//TODO - offer to save old abstractDocument before opening new one

				}
				showNewDialog();
			}
		});

		item = fileMenu.add("Open");
		item.setMnemonic(KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Open") {
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
	
	public AbstractPaneTurner getPaneTurner(){
		return paneTurner;
	}
	
	private static void showSaveDialog(){
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showSaveDialog(paneTurner);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			save(file);
		}
	}
	
	private static void showNewDialog(){
		
		NewDialog nd = new NewDialog(frame);
		RunConfiguration config= nd.showDialog();
		
		if (config == null){
			//do nothing
		} else if ( config.cpSelected == true ){
			paneTurner = new PaneTurnerTCP(frame, new TCPDocument(config.multipleSelected), config.multipleSelected);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		} else {
			paneTurner = new PaneTurnerCI(frame, new CIDocument(config.multipleSelected), config.multipleSelected);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}
	}

	private static void save(File xmlfile) {
		String xmlRepresentation = paneTurner.toXML(xmlfile);
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
		
		curFile = xmlfile;
	}

	private static void open(File file) {
		loading = true;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc=null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Failed to open file: "+e.getMessage(),
					"Error Loading File", JOptionPane.PLAIN_MESSAGE);
			return;
		} 
		
		curFile = file;
		
		// retrieve network type from document
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
		
		// retrieve multistakeholder status from document
		NodeList stakeholderList = doc.getElementsByTagName("STAKEHOLDERS");
		int stakeholderLength = stakeholderList.getLength();
		boolean isMultiStakeholder;
		if (stakeholderLength <= 0 ) {
			isMultiStakeholder = false;
		} else {
			isMultiStakeholder = Boolean.parseBoolean(Util.getOnlyChildText(doc, "STAKEHOLDERS", "MULTISTAKEHOLDER"));
		}
		
		System.out.println("netType!!: "+networkType);
		if(networkType.equals("CI")){
			//frame.removeAll();
			CIDocument oldCIDocument = new CIDocument(doc);
			loading = false;
			paneTurner = new PaneTurnerCI(frame, oldCIDocument, isMultiStakeholder);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}else if(networkType.equals("TCP")){
			//frame.removeAll();
			TCPDocument oldTCPDocument = new TCPDocument(doc);
			loading = false;
			paneTurner = new PaneTurnerTCP(frame, oldTCPDocument, isMultiStakeholder);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}

		// ...
		// same for TCPDocument
		
	}
}
