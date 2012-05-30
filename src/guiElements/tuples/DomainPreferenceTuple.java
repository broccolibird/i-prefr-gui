package guiElements.tuples;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.Attribute;
import dataStructures.Domain;
import dataStructures.DomainPreference;
import dataStructures.DomainValue;
import dataStructures.maps.SuperkeyMap;

@SuppressWarnings("serial")
public class DomainPreferenceTuple extends AbstractTuple<DomainPreference> {
	private Attribute attribute;
	private JComboBox leftDomain;
	private JComboBox rightDomain;
	private JButton xButton;

	public DomainPreferenceTuple(Integer key, SuperkeyMap<DomainPreference> map,
			Window parentWindow, JPanel parentPanel, Attribute attribute) {
		super(key, map, parentWindow, parentPanel);
		this.attribute = attribute;
		initializeGUI();
	}

	public DomainPreferenceTuple(SuperkeyMap<DomainPreference> map,
			Window parentWindow, JPanel parentPanel, Attribute attribute) {
		super(map, parentWindow, parentPanel);
		this.attribute = attribute;
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		if (attribute == null)
			System.out.println("attribute is null in PreferenceTuple");
		Domain object = attribute.getObject();
		if (object != null)
			System.out.println("domain object: " + object.toString());
		else
			System.out.println("domain object is null");
		DomainValue[] values = attribute.getObject().getDomainValueList()
				.toArray();
		leftDomain = new JComboBox(values);
		leftDomain.addActionListener(this);
		JLabel greaterThan = new JLabel(">");
		rightDomain = new JComboBox(values);
		rightDomain.addActionListener(this);
		xButton = new JButton("x");
		xButton.addActionListener(this);

		DomainPreference preference = map.get(key);
		if (preference != null) {
			int leftIndex = 0;
			int rightIndex = 0;
			DomainValue leftValue = preference.getLeft();
			DomainValue rightValue = preference.getRight();
			for (int i = 0; i < values.length; i++) {
				if (leftValue.equals(values[i])) {
					leftIndex = i;
				}
				if (rightValue.equals(values[i])) {
					rightIndex = i;
				}
			}
			leftDomain.setSelectedIndex(leftIndex);
			rightDomain.setSelectedIndex(rightIndex);
		}

		add(leftDomain);
		add(greaterThan);
		add(rightDomain);
		add(xButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		// if asked to remove, then simply remove
		if (source == xButton) {
			parentPanel.remove(this);
			map.remove(key);
			parentWindow.pack();

			// if this is a new item, then create it before acting
		} else if (map.get(key) == null) {
			DomainPreference preference = new DomainPreference(
					(DomainValue) leftDomain.getSelectedItem(),
					(DomainValue) rightDomain.getSelectedItem());
			map.put(key, preference);

			// this is not a new item, so adjust the model
		} else {
			DomainPreference preference = map.get(key);
			if (source == leftDomain) {
				preference.setLeft((DomainValue) leftDomain.getSelectedItem());
			} else if (source == rightDomain) {
				preference
						.setRight((DomainValue) rightDomain.getSelectedItem());
			}
		}
	}
}
