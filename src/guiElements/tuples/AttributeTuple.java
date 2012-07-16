package guiElements.tuples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import mainGUI.PreferencePane;
import mainGUI.SetupGraphPane;

import dataStructures.Attribute;
import dataStructures.AttributeKey;
import dataStructures.maps.AttributeMap;
import guiElements.AbstractTextListener;

@SuppressWarnings("serial")
public class AttributeTuple extends AbstractTuple<Attribute> implements
		ActionListener {

	protected JTextField attributeName;
	protected JButton xButton;
	protected PreferencePane preferencePane;

	public AttributeTuple(Integer key, AttributeMap map, JFrame parent,
			JPanel parentPanel, PreferencePane preferencePane) {
		super(key, map, parent, parentPanel);

		this.preferencePane = preferencePane;
		initializeGUI();
	}

	public AttributeTuple(AttributeMap map, JFrame parent, JPanel parentPanel,
			PreferencePane preferencePane) {
		super(map, parent, parentPanel);
		this.preferencePane = preferencePane;
		initializeGUI();
	}

	public JTextField getTextField(){
		return attributeName;
	}
	
	@Override
	public void initializeGUI() {
		attributeName = new JTextField(20);
		Attribute a = map.get(key);
		if (a != null)
			attributeName.setText(a.getName());
		attributeName.getDocument().addDocumentListener(
				new AttributeTextListener(attributeName));
		this.add(attributeName);
		xButton = new JButton("x");
		xButton.addActionListener(this);
		this.add(xButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == xButton) {
			Attribute a = map.get(key);
			if (preferencePane != null && preferencePane instanceof SetupGraphPane) {
				((SetupGraphPane) preferencePane).getGraph().removeVertex(a);
			}
			parentPanel.remove(this);
			map.remove(key);
			parentWindow.pack();

		}
	}

	class AttributeTextListener extends AbstractTextListener {

		public AttributeTextListener(JTextField field) {
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
			Attribute a = map.get(key);
			if (a == null) {
				newEntry = true;
				System.out.println("here making the new Attribute with key: "+key);
				AttributeKey attrKey = new AttributeKey(key);
				a = new Attribute("", attrKey, null);
			}
			if (field == attributeName) {
				a.setName(attributeName.getText());
				if (newEntry)
					map.put(key, a);
				parentWindow.pack();
			}
		}

	}

}
