package multiStakeholderGUI;

import guiElements.ScrollPane;

import java.awt.Dimension;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import dataStructures.Role;
import dataStructures.maps.RoleMap;

/**
 * MemberPane is an UpdatePane which contains a MemberListPane for each
 * Role within the project.
 */
@SuppressWarnings("serial")
public class MemberPane extends ScrollPane {

	protected RoleMap map;
	protected JFrame parentFrame;

	/**
	 * create a new MemberPane instance
	 * @param oldMap
	 * @param parentFrame
	 */
	public MemberPane(RoleMap oldMap, JFrame parentFrame) {
		super();
		this.map = oldMap;
		this.parentFrame = parentFrame;
		initializeGUI();
		
		setVisible(true);
		resize();
	}

	/**
	 * @return JPanel
	 */
	private void initializeGUI() {		
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		JTextField title = new JTextField("Enter Role Members");
		title.setEditable(false);
		title.setHorizontalAlignment(JTextField.CENTER);
		headerPanel.add(title);
		
		scrollPane.setPreferredSize(new Dimension(600, 650));
		
		update();
	}
 
	@Override
	public void update() {
		
		//create table header
		containerPanel.removeAll();
		tuples.clear();
		
		// for every role, add a panel to the table
		Collection<Role> roles = map.values();
		
		JPanel lastPanel = null;
		
		for (Role role : roles){
			JPanel rolePanel = new MemberListPane(role, role.getObject(), parentFrame, this);
			containerPanel.add(rolePanel);
			
			if(lastPanel != null ) {
				layout.putConstraint(SpringLayout.NORTH, rolePanel, 5,
						SpringLayout.SOUTH, lastPanel);
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, rolePanel, 0,
						SpringLayout.HORIZONTAL_CENTER, lastPanel);
			} else {
				layout.putConstraint(SpringLayout.NORTH, rolePanel, 5,
						SpringLayout.NORTH, containerPanel);
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, rolePanel, 5,
						SpringLayout.HORIZONTAL_CENTER, containerPanel);
			}
			
			tuples.add(rolePanel);
			lastPanel = rolePanel;
		}
		
		resize();
		parentFrame.pack();
		
	}
	
	/**
	 * Resizes the panel holding MemberListPanes so that the scroll bar works properly
	 */
	public void resize() {
		double height = 40;
		
		for(JPanel panel: tuples) {
			height += panel.getPreferredSize().getHeight();
		}
		
		containerPanel.setPreferredSize(new Dimension(570, (int)height));
		containerPanel.setMinimumSize(new Dimension(570, (int)height));
	
	}
}
