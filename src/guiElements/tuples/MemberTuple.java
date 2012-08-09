package guiElements.tuples;

import guiElements.AbstractTextListener;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import dataStructures.Member;
import dataStructures.maps.MemberMap;

public class MemberTuple extends AbstractTuple<Member> implements
		ActionListener {

	protected JTextField memberName;
	protected JButton xButton;
	
	public MemberTuple(Integer key, MemberMap map, JFrame parent, 
			JPanel parentPanel){
		super(key, map, parent, parentPanel);
		initializeGUI();
	}
	
	public MemberTuple(MemberMap map, JFrame parent, JPanel parentPanel){
		super(map, parent, parentPanel);
		initializeGUI();
		
	}
	
	@Override
	public void initializeGUI(){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		memberName = new JTextField(12);
		memberName.setMaximumSize(new Dimension(125, 20));
		Member m = map.get(key);
		
		if(m != null)
			memberName.setText(m.getName());
		memberName.getDocument().addDocumentListener(
				new MemberTextListener(memberName));
		this.add(memberName);
		
		xButton = new JButton("x");
		xButton.addActionListener(this);
		this.add(xButton);
		
	}

	class MemberTextListener extends AbstractTextListener {

		public MemberTextListener(JTextField field) {
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
			boolean newEntry = false;
			Member m = map.get(key);
			
			if ( !memberName.getText().equals("")) {
				if(m == null) {
					newEntry = true;
					m = new Member("", key);
				}
				
				if (field == memberName) {
					m.setName(memberName.getText());
					if (newEntry) {
						map.put(key, m);
					}
					// Member info has changed, set map to unsaved
					map.setSaved(false);
					parentWindow.pack();
				}
			}
		}

	}
}
