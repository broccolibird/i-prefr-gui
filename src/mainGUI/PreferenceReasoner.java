package mainGUI;

import guiElements.NewDialog;
import guiElements.project.SaveProjectDialog;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dataStructures.CIDocument;
import dataStructures.RunConfiguration;
import dataStructures.TCPDocument;

@SuppressWarnings("serial")
public class PreferenceReasoner extends JApplet{
	
	private static AbstractPaneTurner paneTurner;
	private static JFrame frame;
	public static boolean loading;
	
	AbstractAction save = new AbstractAction("Save") {
		public void actionPerformed(ActionEvent e) {
			save();
		}
	};
	
	AbstractAction saveAs = new AbstractAction("SaveAs") {
		public void actionPerformed(ActionEvent e){
			if(paneTurner == null)
				JOptionPane.showMessageDialog(frame, "Please start a project before saving.",
						"Nothing to save", JOptionPane.PLAIN_MESSAGE);
			else {
				showSaveDialog();
			}
		}
	};
	
	AbstractAction newProj = new AbstractAction("New") {
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
	};
	
	AbstractAction open = new AbstractAction("Open") {
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
	};
	
	public PreferenceReasoner() {
		// new JDialog: name your project
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(900, 750));
		frame.addWindowListener(new ReasonerWindowListener(frame, this));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		paneTurner = null;
		
		// Setup menu options
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenuItem item;
		
		item = fileMenu.add("Save");
		item.setMnemonic(KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		item.addActionListener(save);
		
		item = fileMenu.add("SaveAs");
		item.addActionListener(saveAs);
		
		item = fileMenu.add("New");
		item.setMnemonic(KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		item.addActionListener(newProj);

		item = fileMenu.add("Open");
		item.setMnemonic(KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		item.addActionListener(open);

		JMenu helpMenu = new JMenu("?");
		
		//setup help
		JMenuItem help = new JMenuItem("Help");
		HelpSet hs = getHelpSet("master.hs");
		HelpBroker hb = hs.createHelpBroker();
		CSH.setHelpIDString(help, "intro");
		help.addActionListener(new CSH.DisplayHelpFromSource(hb));
		help.setMnemonic('h');
		helpMenu.add(help);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.pack();
	}
	
	/**
	 * Returns true if the project has been changed
	 * since opening/last save.
	 * @return true if changes exist
	 */
	public boolean existChanges() {
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
	protected boolean showSaveChangesDialog(){
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
	 * Opens a file chooser for the user to select a location 
	 * to save the project folder
	 * @return true if the file is saved successfully
	 */
	private boolean showSaveDialog(){
		SaveProjectDialog saveDialog = new SaveProjectDialog(frame, paneTurner.getProjectName());
		File saveFile = saveDialog.showDialog();
		if(saveFile != null)
			return save(saveFile);
		
		return false;
	}
	
	/**
	 * Opens a dialog and creates a new project based on the user's selections
	 */
	private void showNewDialog(){
		
		NewDialog nd = new NewDialog(frame);
		RunConfiguration config= nd.showDialog();
		
		if (config == null){
			return;
		} 
		
		if (paneTurner != null)
			frame.remove(paneTurner);
		
		if ( config.cpSelected == true ){
			paneTurner = new PaneTurnerTCP(this, new TCPDocument(config.multipleSelected), config.multipleSelected);
		} else {
			paneTurner = new PaneTurnerCI(this, new CIDocument(config.multipleSelected), config.multipleSelected);	
		}
		
		frame.getContentPane().add(paneTurner);
		frame.pack();
		
		paneTurner.updateRightPane();
	}

	/**
	 * If a project has not yet been saved, a save dialog opens.
	 * If the project has already been created and saved, project
	 * is saved to project location.
	 * 
	 * @return true if the project was saved successfully
	 */
	public boolean save() {
		if(paneTurner == null) {
			JOptionPane.showMessageDialog(frame, "Please start a project before saving.",
					"Nothing to save", JOptionPane.PLAIN_MESSAGE);
			return false;
		} else if (paneTurner.getDocument().getProjectFolder() == null){
			return showSaveDialog();
		} else {
			return save(paneTurner.getDocument().getProjectFolder());
		}
	}
	
	/**
	 * Saves an xml representation of the project to the
	 * given project directory.
	 * @param xmlfile - file to save to
	 * @return true if the file is saved successfully
	 */
	private boolean save(File projectDirectory) {
	
		// Create main xml file
		File xmlfile = new File(projectDirectory.getAbsolutePath()
				+System.getProperty("file.separator")+"main.xml");
		
		// sets the current project field as well as project name
		paneTurner.setCurrentProject(projectDirectory);
		
		String xmlRepresentation = paneTurner.toXML();
		
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
		    try {
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    return false;
		}
		try {
		    writer.close();
		}
		catch(IOException e) {
		    e.printStackTrace();
		}
		
		paneTurner.setSaved(true);
		return true;
	}

	/**
	 * Opens the given file and loads the project
	 * @param file
	 */
	private void open(File file) {
		loading = true;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc=null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Failed to open file: "+e.getMessage(),
					"Error Loading File", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		// retrieve network type from document
		NodeList nList = doc.getElementsByTagName("NETWORK");
		
		// if NETWORK not part of file, incorrect file type
		if(nList.getLength() <= 0) {
			displayIncorrectFileType(file, doc);
			return;
		}
		
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
			CIDocument oldCIDocument = new CIDocument(file.getParentFile(), doc);
			loading = false;
			paneTurner = new PaneTurnerCI(this, oldCIDocument, isMultiStakeholder);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}else if(networkType.equals("TCP")){
			TCPDocument oldTCPDocument = new TCPDocument(file.getParentFile(), doc);
			loading = false;
			paneTurner = new PaneTurnerTCP(this, oldTCPDocument, isMultiStakeholder);
			frame.getContentPane().add(paneTurner);
			frame.pack();
		}
		
		paneTurner.setSaved(true);
		
	}
	
	/**
	 * Inform the user that the file they selected is not a project file
	 * @param xmlFile
	 * @param doc
	 */
	private void displayIncorrectFileType(File xmlFile, Document doc) {
		String filePath = xmlFile.getAbsolutePath();
		
		// Determine file type and create suggested file path
		NodeList roleList = doc.getElementsByTagName("ROLES");
		if(roleList.getLength() > 0) {
			System.out.println("this is a role file");
			
			if(filePath.endsWith("-roles.xml")) {
				int index = filePath.indexOf("-roles.xml");
				filePath = filePath.substring(0, index);
				filePath += ".xml";
			}
		}
		
		NodeList hierarchyList = doc.getElementsByTagName("ROLEHIERARCHY");
		if(hierarchyList.getLength() > 0 ) {
			System.out.println("this is a rolehierarchy file");
			
			if(filePath.endsWith("-hierarchy.xml")) {
				int index = filePath.indexOf("-hierarchy.xml");
				filePath = filePath.substring(0, index);
				filePath += ".xml";
			}
		}
		
		NodeList importancesList = doc.getElementsByTagName("IMPORTANCES");
		if(importancesList.getLength() > 0 ) {
			System.out.println("this is a preference file");
			
			if(filePath.contains("-preference-")) {
				int index = filePath.indexOf("-preference-");
				filePath = filePath.substring(0, index);
				filePath += ".xml";
			}
		}
		
		//Suggest a new file the user can open
		File suggestedFile = new File(filePath);
		
		if(suggestedFile.exists()) {
			
			Object[] options = {"Open suggested file", "Open different file", "Cancel"};
			int chosenOption = JOptionPane.showOptionDialog(frame, "The file you selected " +
					"("+xmlFile.getAbsolutePath()+") is " +
					"not a project file.\nWould you like to open " + filePath + " instead?",
					"Incorrect file type", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
					null, options, options[0]);
			
			if(chosenOption == 0) {
				//Open suggested file
				open(suggestedFile);
				
			} else if( chosenOption == 1) {
				//Open file chooser to select new file
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
		
		} else { //suggested file does not exist
			Object[] options = {"Open different file", "Cancel"};
			int chosenOption = JOptionPane.showOptionDialog(frame, "The file you selected " +
					"("+xmlFile.getAbsolutePath()+") is " +
					"not a project file.\nWould you like to open " + filePath + " instead?",
					"Incorrect file type", JOptionPane.YES_NO_OPTION, (Integer) null, 
					null, options, options[0]);
			
			if( chosenOption == 0) {
				//let the user select a new file
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
	}
	
	/**
	 * find the helpset file and create a HelpSet object
	 */
	 public HelpSet getHelpSet(String helpsetfile) {
		 HelpSet hs = null;
	     ClassLoader cl = PreferenceReasoner.class.getClassLoader();
	     try {
	    	 URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
	    	 hs = new HelpSet(null, hsURL);
    	 } catch(Exception ee) {
    		 System.out.println("HelpSet: "+ee.getMessage());
    		 System.out.println("HelpSet: "+ helpsetfile + " not found");
    	 }
	     return hs;
	 }

	 public static void main(String[] args) {
		 new PreferenceReasoner();
	 }

	public JFrame getFrame() {
		return frame;
	}
		
}
