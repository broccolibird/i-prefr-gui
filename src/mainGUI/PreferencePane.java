package mainGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import dataStructures.AbstractDocument;
import dataStructures.Member;
import dataStructures.Role;
import dataStructures.maps.MemberMap;
import dataStructures.maps.RoleMap;

/**
 * PreferencePane is an UpdatePane in which stakeholders 
 * enter preferences over the previously created
 * Attributes.
 */
@SuppressWarnings("serial")
public abstract class PreferencePane extends UpdatePane implements ActionListener{

	protected JFrame parent;
	protected AbstractDocument document;
	
	boolean isMultipleStakeholder;
	
	private JComboBox<Member> stakeholderBox;
	private JPanel stakeholderControls;
	private JTextField curFile;
	protected JPanel fileControls;
	
	protected JPanel preferencePanel;
	private Member curMember;
	
	private JButton save;
	private JButton saveAs;
	private JButton load;
	private JButton clear;
	
	protected JTextArea noMembers;
	
	public PreferencePane(JFrame parent, AbstractDocument document){
		this.parent = parent;
		this.document = document;
		this.isMultipleStakeholder = document.getRoleMap().isMultipleStakeholder();
	}
	
	/**
	 * Resets all maps and graphs associated 
	 * with Preference input.
	 */
	public abstract void clearPreferenceData();
	
	/**
	 * Loads member preferences from given file.
	 * @param file
	 * @return true if preferences were loaded successfully
	 */
	public abstract boolean loadMemberPreferences(File file);
	
	/**
	 * Update GUI associated with specific panel/ Preference
	 * Network type.
	 */
	protected abstract void updatePreferencePanel();
	
	/**
	 * Saves the member's preferences to the provided file.
	 * @param preferenceFile
	 * @return true if the file was saved sucessfully
	 */
	public abstract boolean saveMemberPreferences(File preferenceFile);
	
	/**
	 * @return true if the panel contains any changes
	 */
	public abstract boolean existUnsavedChanges();
	
	/**
	 * Setup the combobox containing role members
	 * -- only used in multistakeholder
	 */
	private void setupStakeholderBox() {
		RoleMap rm = document.getRoleMap();
		Role[] roles = (Role[]) rm.values().toArray(new Role[0]);
		Vector<Member> allMembers = new Vector<Member>();
		for(Role role: roles) {
			MemberMap members = role.getObject();
			if (members != null) {
				Collection<Member> roleMembers = members.values();
				allMembers.addAll(roleMembers);

			}
		}
		
		stakeholderBox = new JComboBox<Member>(allMembers);
		stakeholderBox.addActionListener(this);
		stakeholderBox.invalidate();
		if(allMembers.size() > 0){
			if (curMember != null)
				stakeholderBox.setSelectedItem(curMember);
			else
				stakeholderBox.setSelectedIndex(0);
		} else {
			curMember = null;
		}
	}
	
	@Override
	public void update() {		
		stakeholderControls.removeAll();
		
		if(isMultipleStakeholder) {	
			setupStakeholderBox();
			stakeholderControls.add(stakeholderBox);
		}
		
		stakeholderControls.add(save); 
		stakeholderControls.add(saveAs);
		stakeholderControls.add(load);
		stakeholderControls.add(clear);
		
		if(curMember == null) {
			fileControls.setVisible(false);
			preferencePanel.setVisible(false);
			noMembers.setVisible(true);
		} else {
			fileControls.setVisible(true);
			preferencePanel.setVisible(true);
			noMembers.setVisible(false);
			
			setCurrentFileField();
		}
	}
	
	protected void createFileControls() {
		// Add file controls
		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(curMember == null) {
					System.out.println("SetupPreferencesPane -- member == null!!!");
				} else if (curMember.getPreferenceFilePath() == null) {
					savePreferencesAs();
				} else {
					savePreferences();
				}
				
			}
		});
		
		saveAs = new JButton("Save As");
		saveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePreferencesAs();
			}
		});
		load = new JButton("Load Existing File");
		load.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkForUnsavedChanges();
				loadExistingPreferences();
			}
		});
		
		clear = new JButton("Clear");
		clear.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(parent,
					    "You are about to clear the current preferences,"+
					    " would you like to continue?",
					    "Clear preferences",
					    JOptionPane.YES_NO_OPTION);
				
				if (choice == JOptionPane.YES_OPTION)
					clearMemberPreferences();				
			}
		});
		
		stakeholderControls = new JPanel();
		stakeholderControls.setLayout(new FlowLayout());
		
		// Set text for field showing user's preference file path
		curFile = new JTextField();		
		curFile.setEditable(false);
		
		// in non-multistakeholder project
		// Set current Member to default member
		if( !isMultipleStakeholder) {
			RoleMap rm = document.getRoleMap();
			Role defaultRole = rm.get(rm.firstKey());
			MemberMap mMap = defaultRole.getObject();
			Member defaultMember = mMap.get(0); // default member is always key 0
			curMember = defaultMember;
			loadMemberPreferences();
		}
		
		fileControls = new JPanel();
		fileControls.setLayout(new BoxLayout(fileControls, BoxLayout.PAGE_AXIS));
		fileControls.setMaximumSize(new Dimension(450, 10));
		fileControls.add(stakeholderControls);
		fileControls.add(curFile);
	}
	
	/**
	 * Creates a field to be displayed when there are 
	 * no Role Members in the project and preferences
	 * cannot be made.
	 */
	protected void createNoMemberField() {
		// Create text area to show to user when there are no members in the project
		noMembers = new JTextArea("There are currently no stakeholders in your project.\n"+
				"Please create a stakeholder to input preferences.");
		noMembers.setEditable(false);
		noMembers.setPreferredSize(new Dimension(300, 40));
		noMembers.setMaximumSize(noMembers.getPreferredSize());
		noMembers.setBackground(new Color(255,255,255,0));
	}
	
	/**
	 * Creates a new preference panel based on the network type
	 */
	protected abstract void initializePreferencePanel();
	
	/**
	 * Sets the text in the current file field to show which preference file
	 * is in use
	 */
	private void setCurrentFileField() {
		if(curMember == null){
			curFile.setText("No member selected");
		} else if (curMember.getPreferenceFilePath() == null) {
			curFile.setText("None set");
		} else {
			curFile.setText(curMember.getPreferenceFilePath());
		}
	}

	/**
	 * Clears used attributes and replaces the current preference panel with a new one
	 */
	private void clearMemberPreferences() {
		// sets attributes to unused
		clearPreferenceData();
		remove(preferencePanel);
			
		initializePreferencePanel();
		add(preferencePanel);
		revalidate();
	}
	
	/**
	 * Clears previous member's preferences and loads next member's preferences
	 */
	protected void loadMemberPreferences() {
		// Clear previous user's preferences					
		clearMemberPreferences();
		
		if(curMember != null && curMember.getPreferenceFilePath() != null) {
			File file = new File(curMember.getPreferenceFilePath());
			loadMemberPreferences(file);
		}
		updatePreferencePanel();
		setCurrentFileField();
		revalidate();
	}
	
	/**
	 * Saves member preferences to the member's current preference file
	 */
	private void savePreferences() {
		File memberFile = new File(curMember.getPreferenceFilePath());
		saveMemberPreferences(memberFile);
	}
	
	/**
	 * Opens a file chooser to allow the user to select the location
	 * of the save file before saving
	 */
	private void savePreferencesAs() {
		JFileChooser chooser = new JFileChooser();
		AbstractPaneTurner paneTurner = (AbstractPaneTurner) getParent();
		
		// suggest a name for the preference file based on project file name
		String suggestedName;
		File currentFile = paneTurner.getCurrentFile();
		if ( currentFile != null) {
			suggestedName = currentFile.getAbsolutePath();
		} else {
			suggestedName = paneTurner.getProjectFileName();
		}
		int suffixIndex = suggestedName.lastIndexOf('.');
		suggestedName = (suffixIndex >= 0) ? 
				suggestedName.substring(0, suffixIndex) : suggestedName;
		suggestedName += "-preference-"+curMember.getName()+".xml";
		
		chooser.setSelectedFile(new File(suggestedName));
		int option = chooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			curMember.setPreferenceFilePath(file.getAbsolutePath());
			savePreferences();
			setCurrentFileField();
		}
	}
	
	/**
	 * Locate existing preference file
	 */
	private File selectPreferencesFile() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	    		"XML (*.xml)","xml");
	    chooser.setFileFilter(filter);
	    
	    // suggest a starting folder based on the project
	    // file location
 		String suggestedLocation;
 		AbstractPaneTurner paneTurner = (AbstractPaneTurner) getParent();
 		File currentFile = paneTurner.getCurrentFile();
 		if ( currentFile != null) {
 			suggestedLocation = currentFile.getAbsolutePath();
 			int suffixIndex = suggestedLocation.lastIndexOf('/');
 	 		suggestedLocation = (suffixIndex >= 0) ? 
 	 				suggestedLocation.substring(0, suffixIndex) : suggestedLocation;
 	 		chooser.setSelectedFile(new File(suggestedLocation));
 		}
	 		
	    int option = chooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			return file;
		}
		return null;
	}
	
	/**
	 * Load an existing preference file
	 */
	private void loadExistingPreferences() {
		File file = selectPreferencesFile();
		if( file != null) { //user must select a file in order to load
			curMember.setPreferenceFilePath(file.getAbsolutePath());
			loadMemberPreferences();
		}
	}
	
	/**
	 * Creates a dialog asking the user if they would like to save unsaved
	 * changes
	 * @return true if the user would like to save changes
	 */
	private boolean showUnsavedChangesDialog() {
		int choice = JOptionPane.showConfirmDialog(parent,
			    "The action you have selected will cause any unsaved"+
			    " changes to "+curMember.toString()+"'s preferences to be lost.\n"+
			    " Would you like to save changes now?",
			    "Save unsaved preferences",
			    JOptionPane.YES_NO_OPTION);
		
		if (choice == JOptionPane.YES_OPTION)
			return true;
		
		return false;
	}
	
	/**
	 * Checks for unsaved changes in the current importance map
	 * If they exist, ask user if they would like to save changes
	 */
	public void checkForUnsavedChanges() {
		if(existUnsavedChanges()){
			if(showUnsavedChangesDialog()){
				if(curMember.getPreferenceFilePath() == null) {
					savePreferencesAs();
				}else{
					savePreferences();
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == stakeholderBox && curMember != stakeholderBox.getSelectedItem()) {
			checkForUnsavedChanges();
			curMember = (Member) stakeholderBox.getSelectedItem();
			loadMemberPreferences();
		}
	}
	
}
