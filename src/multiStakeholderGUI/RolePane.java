package multiStakeholderGUI;

import graph.RoleHierarchy;
import guiElements.tuples.RoleTuple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import dataStructures.Role;
import dataStructures.maps.RoleMap;

import mainGUI.UpdatePane;

@SuppressWarnings("serial")
public class RolePane extends UpdatePane implements ActionListener{

	private JFrame parentFrame;
	private JPanel rolePanel;
	private JButton plusButton;
	private RoleHierarchy graph;
	private RoleMap map;

	
	public RolePane(RoleMap map, RoleHierarchy graph, JFrame parentFrame){
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
		InputMap plusInputMap = plusButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		plusInputMap.put(KeyStroke.getKeyStroke("ENTER"), "selectPlus");
		plusButton.getActionMap().put("selectPlus", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plusButton.doClick();				
			}
		});
		
		panel.add(rolePanel);
		panel.add(plusButton);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			RoleTuple tuple = (RoleTuple) rolePanel.add(new RoleTuple(map, parentFrame,
					rolePanel, graph));
			tuple.getTextField().requestFocusInWindow();
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
		RoleTuple tuple = (RoleTuple) rolePanel.add(
				new RoleTuple(map, parentFrame, rolePanel, graph));
		System.out.println("focus: "+tuple.getTextField().requestFocusInWindow());
		parentFrame.pack();

	}
}
