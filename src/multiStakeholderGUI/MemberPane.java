package multiStakeholderGUI;

import guiElements.tuples.MemberTuple;

import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainGUI.UpdatePane;

import dataStructures.Role;
import dataStructures.maps.RoleMap;

@SuppressWarnings("serial")
public class MemberPane extends UpdatePane {

	protected RoleMap map;
	protected JPanel stakeholderPanel;
	protected JFrame parentFrame;

	public MemberPane(RoleMap oldMap, JFrame parentFrame) {
		this.map = oldMap;
		this.parentFrame = parentFrame;
		this.add(createGUI());
		setVisible(true);
	}

	private JPanel createGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		stakeholderPanel = new JPanel();
		stakeholderPanel.setLayout(new BoxLayout(stakeholderPanel, BoxLayout.Y_AXIS));
		update();
		panel.add(stakeholderPanel);
		return panel;
	}
 
	public void update() {
		// for every map entry, add a tuple to the table
		stakeholderPanel.removeAll();
		JPanel label = new JPanel();
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		//label.setPreferredSize(new Dimension(150,20));
		JTextField title = new JTextField("Role Title");
		JTextField members = new JTextField("Role Members");
		title.setEditable(false);
		members.setEditable(false);
		label.add(title);
		label.add(members);
		stakeholderPanel.add(label);
		Collection<Entry<Integer, Role>> set = map.entrySet();
		for (Entry<Integer, Role> p : set){
			stakeholderPanel.add(new MemberTuple(p.getKey(), map, parentFrame,
					stakeholderPanel));
		}
		parentFrame.pack();
	}
}
