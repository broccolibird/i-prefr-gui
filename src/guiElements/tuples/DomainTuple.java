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
import dataStructures.maps.AttributeMap;

@SuppressWarnings("serial")
public class DomainTuple extends AbstractTuple<Attribute> implements
		ActionListener {

	protected JTextArea domainField;

	public DomainTuple(Integer key, AttributeMap map, JFrame parent,
			JPanel parentPanel) {
		super(key, map, parent, parentPanel);
		initializeGUI();
	}

	public DomainTuple(AttributeMap map, JFrame parent, JPanel parentPanel) {
		super(map, parent, parentPanel);
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		domainField = new JTextArea(3, 25);
		Attribute a = map.get(key);
		if (a == null) {
			System.err.println("attribute should not be null in DomainTuple");
		}
		String atName = a.getName();
		JTextField attributeName = new JTextField(atName);
		attributeName.setEditable(false);
		attributeName.setPreferredSize(new Dimension(75, 20));
		this.add(attributeName);
		Domain d = a.getObject();
		
		//System.out.println("in DomainTuple, attributeKey: "+a.getAttributeKey());
		String enumeration = "";
		if (d != null)
			enumeration = d.toString();
		else
			a.setObject(new Domain(enumeration, a.getAttributeKey()));
		
		domainField.setText(enumeration);
		domainField.getDocument().addDocumentListener(
				new DomainTextListener(domainField));
		domainField.setPreferredSize(new Dimension(100, 20));
		this.add(domainField);
	}

	class DomainTextListener extends AbstractTextListener {

		public DomainTextListener(JTextArea field) {
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
			Attribute a = map.get(key);
			if (a == null) {
				System.err.println("attribute should not be null");
			}
			if (field == domainField) {
				Domain d = a.getObject();
				d.setValues(domainField.getText());
				parentWindow.pack();
			}
		}

	}

}
