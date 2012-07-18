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
public class PreferenceReasoner extends JApplet{
	
	private static AbstractPaneTurner paneTurner;
	private static JFrame frame;
	public static boolean loading;

	public static void main(String[] args) {
		// new JDialog: name your project
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(900, 750));
		frame.addWindowListener(new ReasonerWindowListener(frame));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		paneTurner = null;
		
		// Setup menu options
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenuItem item;
		
		item = fileMenu.add("Save");
		item.setMnemonic(KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				save();
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
				boolean switchProject = true;
				if ( existChanges() ) {
					//offer to save old abstractDocument before opening new one
					switchProject = showSaveChangesDialog();
				}
				if (switchProject) {
					showNewDialog();
				}
			}
		});

		item = fileMenu.add("Open");
		item.setMnemonic(KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Open") {
			public void actionPerformed(ActionEvent e) {
				//offer to save old abstractDocument before opening new one
				boolean switchProject = true;
				
				if( existChanges() )
					switchProject = showSaveChangesDialog();
				
				if ( switchProject ) {
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
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.pack();
	}
	
	/**
	 * Returns true if the project has been changed
	 * since opening/last save.
	 * @return true if changes exist
	 */
	public static boolean existChanges() {
		if (paneTurner != null) {
			paneTurner.checkChangesInPreferences();
			return paneTurner.existChanges();
		}
		
		return false;
	}
	
	/**
	 * Opens a dialog asking the user if they would like to save changes to the current project
	 * @return true if the user successfully saves the project or if they choose not to save,
	 * 			returns false if the user cancels out of the action.
	 */
	protected static boolean showSaveChangesDialog(){
		int choice = JOptionPane.showConfirmDialog(frame,
			    "You are about to leave the current project, would you like to save your changes?",
			    "Save Changes",
			    JOptionPane.YES_NO_CANCEL_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			return save();
		} else if (choice == JOptionPane.NO_OPTION) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * Opens a file chooser for the user to select a location to save the project
	 * @return true if the file is saved successfully
	 */
	private static boolean showSaveDialog(){
		JFileChooser chooser = new JFileChooser();
		
		// suggest filename used on SetupProjectPane
		String filename = paneTurner.getProjectFileName();
		chooser.setSelectedFile(new File(filename));
		
		int option = chooser.showSaveDialog(paneTurner);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			return save(file);
		}
		return false;
	}
	
	/**
	 * Opens a dialog and creates a new project based on the user's selections
	 */
	private static void showNewDialog(){
		
		NewDialog nd = new NewDialog(frame);
		RunConfiguration config= nd.showDialog();
		
		if (config == null){
			return;
		} 
		
		if (paneTurner != null)
			frame.remove(paneTurner);
		
		if ( config.cpSelected == true ){
			paneTurner = new PaneTurnerTCP(frame, new TCPDocument(config.multipleSelected), config.multipleSelected);
		} else {
			paneTurner = new PaneTurnerCI(frame, new CIDocument(config.multipleSelected), config.multipleSelected);	
		}
		
		frame.getContentPane().add(paneTurner);
		frame.pack();
		
		paneTurner.updateRightPane();
	}

	/**
	 * @return true if the project was saved successfully
	 */
	public static boolean save() {
		if(paneTurner == null) {
			JOptionPane.showMessageDialog(frame, "Please create a project before saving.",
					"Project does not exist", JOptionPane.PLAIN_MESSAGE);
			return false;
		} else if (paneTurner.getCurrentFile() == null){
			return showSaveDialog();
		} else {
			return save(paneTurner.getCurrentFile());
		}
	}
	
	/**
	 * Saves an xml representation of the project to the
	 * given file.
	 * @param xmlfile - file to save to
	 * @return true if the file is saved successfully
	 */
	private static boolean save(File xmlfile) {
		String xmlRepresentation = paneTurner.toXML(xmlfile);
		System.out.println(xmlRepresentation);
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(xmlfile)); 
		}
		catch (IOException e) {
		    e.printStackTrace();
		    return false;
		}
		try {
		    writer.write(xmlRepresentation);
		}
		catch(IOException e) {
		    e.printStackTrace();
		    return false;
		}
		try {
		    writer.close();
		}
		catch(IOException e) {
		    e.printStackTrace();
		}
		
		paneTurner.setCurrentFile(xmlfile);
		paneTurner.setProjectFileName(paneTurner.getCurrentFile().getName());
		paneTurner.setSaved(true);
		return true;
	}

	/**
	 * Opens the given file and loads the project
	 * @param file
	 */
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
		
		if (paneTurner != null)
			frame.remove(paneTurner);
		
		if(networkType.equals("CI")){
			//frame.removeAll();
			CIDocument oldCIDocument = new CIDocument(doc);
			loading = false;
			paneTurner = new PaneTurnerCI(frame, oldCIDocument, isMultiStakeholder, file);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}else if(networkType.equals("TCP")){
			//frame.removeAll();
			TCPDocument oldTCPDocument = new TCPDocument(doc);
			loading = false;
			paneTurner = new PaneTurnerTCP(frame, oldTCPDocument, isMultiStakeholder, file);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}

		// ...
		// same for TCPDocument
		
	}

}
