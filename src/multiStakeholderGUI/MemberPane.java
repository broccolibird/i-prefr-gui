package multiStakeholderGUI;

import java.awt.Dimension;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainGUI.UpdatePane;

import dataStructures.Role;
import dataStructures.maps.RoleMap;

/**
 * MemberPane is an UpdatePane which contains a MemberListPane for each
 * Role within the project.
 */
@SuppressWarnings("serial")
public class MemberPane extends UpdatePane {

	protected RoleMap map;
	protected JPanel stakeholderPanel;
	protected JFrame parentFrame;

	/**
	 * create a new MemberPane instance
	 * @param oldMap
	 * @param parentFrame
	 */
	public MemberPane(RoleMap oldMap, JFrame parentFrame) {
		this.map = oldMap;
		this.parentFrame = parentFrame;
		this.add(initializeGUI());
		setVisible(true);
	}

	/**
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		stakeholderPanel = new JPanel();
		stakeholderPanel.setLayout(new BoxLayout(stakeholderPanel, BoxLayout.Y_AXIS));
		
		update();
		
		panel.add(stakeholderPanel);
		
		return panel;
	}
 
	public void update() {
		
		//create table header
		stakeholderPanel.removeAll();
		JPanel label = new JPanel();
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		JTextField title = new JTextField("Enter Role Members");
		title.setEditable(false);
		title.setHorizontalAlignment(JTextField.CENTER);
		label.add(title);
		stakeholderPanel.add(label);
		
		// for every role, add a panel to the table
		Collection<Role> roles = map.values();
		
		for (Role role : roles){
			JPanel rolePanel = new MemberListPane(role, role.getObject(), parentFrame);
			stakeholderPanel.add(rolePanel);
			stakeholderPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}
		parentFrame.pack();
	}
	
	@Override
	public void leave() {/*do nothing*/}
}
