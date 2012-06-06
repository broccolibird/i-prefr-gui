package multiStakeholderGUI;

import guiElements.tuples.RoleTuple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.Role;
import dataStructures.maps.EdgeStatementMap;
import dataStructures.maps.RoleMap;
import edu.uci.ics.jung.graph.Graph;

import mainGUI.UpdatePane;

@SuppressWarnings("serial")
public class RolePane extends UpdatePane implements ActionListener{

	private JFrame parentFrame;
	private JPanel rolePanel;
	private JButton plusButton;
	private Graph<Role, Integer> graph;
	private RoleMap map;

	
	public RolePane(RoleMap map, Graph<Role,Integer> graph, JFrame parentFrame){
		this.parentFrame = parentFrame;
		this.graph = graph;
		this.map = map;
		
		this.add(createGUI());
		setVisible(true);
	}
	
	private JPanel createGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		rolePanel = new JPanel();
		rolePanel
				.setLayout(new BoxLayout(rolePanel, BoxLayout.Y_AXIS));
		update();
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		panel.add(rolePanel);
		panel.add(plusButton);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			rolePanel.add(new RoleTuple(map, parentFrame,
					rolePanel, graph));
			pack();
		}
		
	}	
	
	public void pack() {
		parentFrame.pack();
	}
	
	@Override
	public void update() {
		rolePanel.removeAll();
		JPanel label = new JPanel();
		// TODO - is this necessary to set the layout?
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		JLabel name = new JLabel("Role Name");
		label.add(name);
		rolePanel.add(label);

		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Role>> set = map.entrySet();
		for (Entry<Integer, Role> p : set)
			rolePanel.add(new RoleTuple(p.getKey(), map, parentFrame,
					rolePanel, graph));
		rolePanel.add(new RoleTuple(map, parentFrame, rolePanel, graph));
		parentFrame.pack();

	}
}
