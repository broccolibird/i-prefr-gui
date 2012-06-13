package guiElements.tuples;

import graph.RoleHierarchy;
import guiElements.AbstractTextListener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import dataStructures.Role;
import dataStructures.maps.SuperkeyMap;
import edu.uci.ics.jung.graph.Graph;

@SuppressWarnings("serial")
public class RoleTuple extends AbstractTuple<Role> implements ActionListener{

	protected JTextField roleName;
	protected JButton xButton;
	protected RoleHierarchy graph;
	
	public RoleTuple(Integer key, SuperkeyMap<Role> map, JFrame parent,
			JPanel parentPanel, RoleHierarchy graph) {
		super(key, map, parent, parentPanel);
		this.graph = graph;
		initializeGUI();
	}
	
	public RoleTuple(SuperkeyMap<Role> map, JFrame parent, JPanel parentPanel,
			RoleHierarchy graph){
		super(map, parent, parentPanel);
		this.graph = graph;
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		// TODO Auto-generated method stub
		roleName = new JTextField(20);
		Role r = map.get(key);
		if (r != null)
			roleName.setText(r.getName());
		roleName.getDocument().addDocumentListener( new RoleTextListener(roleName));
		this.add(roleName);
		xButton = new JButton("x");
		xButton.addActionListener(this);
		this.add(xButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == xButton) {
			Role r = map.get(key);
			if (graph != null) {
				graph.removeVertex(r);
			}
			parentPanel.remove(this);
			map.remove(key);
			parentWindow.pack();

		}
	}
	
	class RoleTextListener extends AbstractTextListener {

		public RoleTextListener(JTextField field) {
			super(field);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			handleChange();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			handleChange();
		}

		private void handleChange() {
			boolean newEntry = false;
			Role r = map.get(key);
			if (r == null) {
				newEntry = true;
				System.out.println("here making the new Role with key: "+key);
				r = new Role("", key, null);
			}
			if (field == roleName) {
				r.setName(roleName.getText());
				if (newEntry)
					map.put(key, r);
				parentWindow.pack();
			}
		}

	}
}
