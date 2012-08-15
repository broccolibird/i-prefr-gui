package multiStakeholderGUI;

import guiElements.tuples.MemberTuple;

import java.awt.Color;
import java.awt.Component;
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

/**
 * The MemberListPane is an UpdatePane which contains text fields
 * to add new Members to a Role
 */
@SuppressWarnings("serial")
public class MemberListPane extends UpdatePane implements ActionListener {

	public final static int COLUMNS = 3;
	private Role role;
	private MemberMap map;
	private JFrame parentFrame;
	private MemberPane memberPane;
	private JPanel memberListEntry;
	private JButton plusButton;
	
	private GridLayout layout;
	
	/** number of MemberTuples removed from the Panel */
	int removed;
	
	/**
	 * Create a new MemberListPane
	 * @param role
	 * @param map
	 * @param parentFrame
	 */
	public MemberListPane(Role role, MemberMap map, JFrame parentFrame,
			MemberPane memberPane) {
		this.role = role;
		this.map = map;
		this.memberPane = memberPane;
		this.parentFrame = parentFrame;
		this.removed = 0;
		
		createGUI();
	}
	
	/**
	 * Setup the GUI for this panel
	 */
	private void createGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Create label for Panel
		JTextField roleTitle = new JTextField(role.getName());
		roleTitle.setBackground(Color.GRAY);
		roleTitle.setForeground(Color.WHITE);
		roleTitle.setHorizontalAlignment(JTextField.CENTER);
		roleTitle.setMaximumSize(new Dimension(600, 20));
		roleTitle.setEditable(false);
		add(roleTitle);
		
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		// Setup layout for MemberTuples
		memberListEntry = new JPanel();
		layout = new GridLayout(0, COLUMNS, 10, 10);
		memberListEntry.setLayout(layout);
		update();
		add(memberListEntry);
		
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		add(plusButton);
		
	}
	
	/**
	 * Removes the MemberTuple from the Panel and updates
	 * the removed variable
	 */
	public void remove(Component comp) {
		memberListEntry.remove(comp);
		removed++;
		
		repaint();
		memberPane.resize();
	}
	
	/**
	 * Add a new row of MemberTuples
	 */
	private void addRow() {
		finishRow(COLUMNS);
	}
	
	/**
	 * Add given number of MemberTuples to the existing row
	 * @param entries
	 */
	private void finishRow(int entries) {
		for(int i = 0; i < entries; i++) {
			memberListEntry.add(new MemberTuple(
					map, parentFrame, this));
		}
		memberPane.resize();
	}
	
	@Override
	/**
	 * Handles actions performed on the plus button
	 */
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			removed = removed % COLUMNS;
			if( removed == 0 )
				addRow();
			else {
				finishRow(removed);
				removed = 0;
			}
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
			
			if(numMembers%COLUMNS == 0)
				addRow();
			else
				finishRow(COLUMNS - numMembers%COLUMNS);
		}
		
	}

}
