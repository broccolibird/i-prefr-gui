package mainGUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.AbstractDocument;
import dataStructures.Attribute;
import dataStructures.Member;
import dataStructures.Role;
import dataStructures.maps.EdgeStatementMap;
import dataStructures.maps.RoleMap;
import edu.uci.ics.jung.graph.Graph;

public class SetupPreferencesPane extends UpdatePane implements ActionListener {

	JFrame parent;
	AbstractDocument document;
	
	boolean isMultipleStakeholder;
	boolean isTCPPref;
	
	JComboBox stakeholderBox;
	JPanel stakeholderControls;
	
	PreferencePane preferencesPanel;
	Member curMember;
	
	JButton save;
	
	public SetupPreferencesPane(JFrame parent, AbstractDocument document,
			boolean isTCPPref){
		this.parent = parent;
		this.document = document;
		this.isMultipleStakeholder = document.getRoleMap().isMultipleStakeholder();
		this.isTCPPref = isTCPPref;
		createGUI();
	}
	
	private void setupStakeholderBox() {
		RoleMap rm = document.getRoleMap();
		Role[] roles = (Role[]) rm.values().toArray(new Role[0]);
		Vector<Member> allMembers = new Vector<Member>();
		for(Role role: roles) {
			allMembers.addAll((LinkedList<Member>)role.getObject());
		}
		
		stakeholderBox = new JComboBox<Member>(allMembers);
		stakeholderBox.addActionListener(this);
		stakeholderBox.invalidate();
		if(allMembers.size() > 0){
			stakeholderBox.setSelectedIndex(0);
		}
	}
	
	@Override
	public void update() {
		preferencesPanel.update();
		
		stakeholderControls.removeAll();
		
		if(isMultipleStakeholder) {	
			setupStakeholderBox();
			stakeholderControls.add(stakeholderBox);
		}
		
		stakeholderControls.add(save); 
	}
	
	private void createGUI(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				
		initializePreferencePanel();
		
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
		
		stakeholderControls = new JPanel();
		stakeholderControls.setLayout(new FlowLayout());
		update();
		add(stakeholderControls);
		
		add(preferencesPanel);
		add(Box.createVerticalGlue());
		
	}
	
	private void initializePreferencePanel(){
		if(document.isCINetworkType()) {
			preferencesPanel = new ImportancePane(document.getAttributeMap(), parent);
		} else {
			preferencesPanel = new SetupGraphPane(document, parent);
		}
	}
	private PreferencePane getPreferencePanel() {
		return preferencesPanel;
	}
	
	public Graph<Attribute, EdgeStatementMap> getGraph(){
		if(isTCPPref)
			return ((SetupGraphPane) preferencesPanel).getGraph();
		else return null;
	}

	private void loadMemberPreferences() {
		// Clear previous user's preferences					
		// sets attributes to unused
		preferencesPanel.clearPane();
		remove(preferencesPanel);
		
		initializePreferencePanel();
		if(curMember != null && curMember.getPreferenceFilePath() != null)
			preferencesPanel.loadMemberPreferences(curMember);
		add(preferencesPanel);
		preferencesPanel.update();
		revalidate();
	}
	
	private void savePreferences() {
		File memberFile = new File(curMember.getPreferenceFilePath());
		preferencesPanel.saveMemberPreferences(memberFile);
	}
	
	private void savePreferencesAs() {
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			curMember.setPreferenceFilePath(file.getAbsolutePath());
			savePreferences();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == stakeholderBox ) {
			curMember = (Member) stakeholderBox.getSelectedItem();
			loadMemberPreferences();
		}
	}

}
