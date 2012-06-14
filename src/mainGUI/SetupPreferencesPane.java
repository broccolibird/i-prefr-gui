package mainGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.AbstractDocument;
import dataStructures.Attribute;
import dataStructures.CIDocument;
import dataStructures.Member;
import dataStructures.MemberList;
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
	
	UpdatePane preferencesPanel;
	JPanel stakeholderControls;
	
	JButton load;
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
		if(isMultipleStakeholder) {
			stakeholderControls.removeAll();
			
			setupStakeholderBox();
			stakeholderControls.add(stakeholderBox);
			stakeholderControls.add(load);
			stakeholderControls.add(save);
			
		} 
	}
	
	private void createGUI(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		
		
		if(document.isCINetworkType()) {
			preferencesPanel = new ImportancePane(document.getAttributeMap(), parent, ((CIDocument) document).getImportanceMap());
		} else {
			preferencesPanel = new SetupGraphPane(document, parent);
		}
		
		if(isMultipleStakeholder) {
			load = new JButton("Load");
			save = new JButton("Save");
		}
		stakeholderControls = new JPanel();
		stakeholderControls.setLayout(new FlowLayout());
		update();
		add(stakeholderControls);
		
		add(preferencesPanel);
		add(Box.createVerticalGlue());
		
	}
	
	public Graph<Attribute, EdgeStatementMap> getGraph(){
		if(isTCPPref)
			return ((SetupGraphPane) preferencesPanel).getGraph();
		else return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == stakeholderBox ) {
			Member selectedMember = (Member) stakeholderBox.getSelectedItem();
		}
	}

}
