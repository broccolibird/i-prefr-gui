package multiStakeholderGUI;

import guiElements.tuples.MemberTuple;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainGUI.UpdatePane;

import dataStructures.Member;
import dataStructures.Role;
import dataStructures.maps.MemberMap;

@SuppressWarnings("serial")
public class MemberListPane extends UpdatePane implements ActionListener {

	Role role;
	MemberMap map;
	JFrame parentFrame;
	JPanel memberListEntry;
	JButton plusButton;
	
	GridLayout layout;
	
	public MemberListPane(Role role, MemberMap map, JFrame parentFrame) {
		this.role = role;
		this.map = map;
		this.parentFrame = parentFrame;
		
		createGUI();
	}
	
	private void createGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JTextField roleTitle = new JTextField(role.getName());
		roleTitle.setHorizontalAlignment(JTextField.CENTER);
		roleTitle.setEditable(false);
		add(roleTitle);
		
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		memberListEntry = new JPanel();
		layout = new GridLayout(0, 3, 10, 10);
		memberListEntry.setLayout(layout);
		update();
		add(memberListEntry);
		
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		add(plusButton);
		
	}
	
	public void addRow() {
		finishRow(3);
	}
	
	public void finishRow(int entries) {
		for(int i = 0; i < entries; i++) {
			memberListEntry.add(new MemberTuple(
					map, parentFrame, memberListEntry));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			addRow();
			revalidate();
		}
	}

	@Override
	public void update() {
		memberListEntry.removeAll();
		
		MemberMap mMap = role.getObject();
		if( mMap != null ) {
			Collection<Member> allMembers = mMap.values();
			int numMembers = allMembers.size();
			
			for(Member m : allMembers) {
				memberListEntry.add(new MemberTuple(m.getKey(), 
						map, parentFrame, this));
			}
			
			if(numMembers%3 == 0)
				addRow();
			else
				finishRow(3 - numMembers%3);
		}
		
		
		
		
	}

}
