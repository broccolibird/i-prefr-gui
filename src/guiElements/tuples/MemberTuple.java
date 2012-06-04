package guiElements.tuples;

import guiElements.AbstractTextListener;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import dataStructures.Attribute;
import dataStructures.Domain;
import dataStructures.MemberList;
import dataStructures.Role;
import dataStructures.maps.RoleMap;

public class MemberTuple extends AbstractTuple<Role> implements
		ActionListener {

	protected JTextArea memberField;
	
	public MemberTuple(Integer key, RoleMap map, JFrame parent, 
			JPanel parentPanel){
		super(key, map, parent, parentPanel);
		initializeGUI();
	}
	
	public MemberTuple(RoleMap map, JFrame parent, JPanel parentPanel){
		super(map, parent, parentPanel);
		initializeGUI();
		
	}
	
	@Override
	public void initializeGUI(){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		memberField = new JTextArea(3, 25);
		Role r = map.get(key);
		
		if(r == null) {
			System.err.println("role should not be null in MemberTuple");
		}
		String memTitle = r.getName();
		JTextField memberTitle = new JTextField(memTitle);
		memberTitle.setEditable(false);
		memberTitle.setPreferredSize(new Dimension(75, 20));
		this.add(memberTitle);
		MemberList m = r.getObject();
		
		String enumeration = "";
		if (m != null)
			enumeration = m.toString();
		else
			r.setObject(new MemberList(enumeration, r.getKey()));
		
		memberField.setText(enumeration);
		memberField.getDocument().addDocumentListener(new MemberTextListener(memberField));
		memberField.setPreferredSize(new Dimension(100, 20));
		this.add(memberField);
	}
	
	class MemberTextListener extends AbstractTextListener {

		public MemberTextListener(JTextArea field) {
			super(field);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			handleChange();

		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			handleChange();
		}

		private void handleChange() {
			Role r = map.get(key);
			if (r == null) {
				System.err.println("role should not be null");
			}
			if (field == memberField) {
				MemberList m = r.getObject();
				m.setNames(memberField.getText());
				parentWindow.pack();
			}
		}

	}
}
