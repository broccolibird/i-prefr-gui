package guiElements.tuples;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import dataStructures.Attribute;
import dataStructures.BinaryOperator;
import dataStructures.ConditionElement;
import dataStructures.DomainValue;
import dataStructures.DomainValueList;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.SuperkeyMap;

@SuppressWarnings("serial")
public class ConditionTuple extends AbstractTuple<ConditionElement> {
	private AttributeMap attributeMap;
	private JRadioButton negatedButton;
	private JRadioButton trueButton;
	private JComboBox attributeBox;
	private JComboBox operatorBox;
	private JComboBox domainBox;
	private JButton xButton;

	public ConditionTuple(Integer key, SuperkeyMap<ConditionElement> map,
			Window window, JPanel parentPanel, AttributeMap attributeMap) {
		super(key, map, window, parentPanel);
		this.attributeMap = attributeMap;
		initializeGUI();

	}

	public ConditionTuple(SuperkeyMap<ConditionElement> map, Window window,
			JPanel parentPanel, AttributeMap attributeMap) {
		super(map, window, parentPanel);
		this.attributeMap = attributeMap;
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		Attribute[] attributes = attributeMap.values()
				.toArray(new Attribute[0]);
		ConditionElement condition = map.get(key);

		boolean isNegated = false;
		boolean isTrue = false;
		int selectedAttributeIndex = 0;
		int selectedOperatorIndex = 0;
		int selectedDomainValueIndex = 0;
		boolean oldItem = false;

		if (condition != null) {
			isNegated = condition.isNegated();
			isTrue = condition.isTrue();
			oldItem = true;
			for (int i = 0; i < attributes.length; i++) {
				if (attributes[i].equals(condition.getAttribute())) {
					selectedAttributeIndex = i;
					break;
				}
			}
			selectedOperatorIndex = condition.getOp().getIdentifier();
			DomainValueList values = attributes[selectedAttributeIndex]
					.getObject().getDomainValueList();
			Iterator<DomainValue> it = values.iterator();
			int i = 0;
			DomainValue selectedValue = condition.getDomainValue();
			while (it.hasNext()) {
				if (it.next().equals(selectedValue)) {
					selectedDomainValueIndex = i;
					break;
				}
				i++;
			}
		}

		trueButton = new JRadioButton();
		trueButton.setSelected(isTrue);
		trueButton.addActionListener(this);

		negatedButton = new JRadioButton();
		negatedButton.setSelected(isNegated);
		negatedButton.addActionListener(this);

		attributeBox = new JComboBox(attributes);
		attributeBox.setSelectedIndex(selectedAttributeIndex);
		attributeBox.addActionListener(this);

		operatorBox = new JComboBox(BinaryOperator.getOperators());
		operatorBox.setSelectedIndex(selectedOperatorIndex);
		operatorBox.addActionListener(this);

		domainBox = new JComboBox(attributes[selectedAttributeIndex]
				.getObject().getDomainValueList().toArray());
		domainBox.setSelectedIndex(selectedDomainValueIndex);
		domainBox.addActionListener(this);

		xButton = new JButton("x");
		xButton.addActionListener(this);
		xButton.setEnabled(oldItem);

		this.add(trueButton);
		this.add(Box.createRigidArea(new Dimension(20, 0)));
		this.add(negatedButton);
		this.add(Box.createRigidArea(new Dimension(7, 0)));
		this.add(attributeBox);
		this.add(Box.createRigidArea(new Dimension(2, 0)));
		this.add(operatorBox);
		this.add(Box.createRigidArea(new Dimension(2, 0)));
		this.add(domainBox);
		this.add(Box.createRigidArea(new Dimension(2, 0)));
		this.add(xButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println("actionPerfomed");
		Object source = e.getSource();
		// if asked to remove, then simply remove
		if (source == xButton) {

			System.out.println("what?");
			parentPanel.remove(this);
			map.remove(key);
			parentWindow.pack();
			parentWindow.invalidate();

			// if this is a new item, then create it before acting
		} else if (map.get(key) == null) {
			// System.out.println("key was null");
			checkDomainBox();
			ConditionElement condition = new ConditionElement(
					negatedButton.isSelected(),
					(Attribute) attributeBox.getSelectedItem(),
					(BinaryOperator) operatorBox.getSelectedItem(),
					(DomainValue) domainBox.getSelectedItem(),
					trueButton.isSelected());

			map.put(key, condition);
			xButton.setEnabled(true);
			// this is not a new item, so adjust the model
		} else {
			// System.out.println("old item");
			ConditionElement condition = map.get(key);
			if (source == trueButton) {

			} else if (source == negatedButton) {
				condition.setNegated(negatedButton.isSelected());
			} else if (source == attributeBox) {
				condition.setAttribute((Attribute) attributeBox
						.getSelectedItem());
				checkDomainBox();
			} else if (source == operatorBox) {
				condition.setOp((BinaryOperator) operatorBox.getSelectedItem());
			} else if (source == domainBox) {
				condition.setDomainValue((DomainValue) domainBox
						.getSelectedItem());
			}
		}
	}

	private void checkDomainBox() {
		// if the domainBox's domain values correspond to a different
		// Attribute than the currently selected one, then swap them out
		int attboxkey = ((Attribute) attributeBox.getSelectedItem())
				.getAttributeKey().getKey();

		// the domain value might be null if no domains have been entered, in
		// which case the domainBox is as close to correct as possible already
		DomainValue value = (DomainValue) domainBox.getSelectedItem();
		if (value != null&&value.getAttributeKey()!=null) {
			int domboxkey = value.getAttributeKey().getKey();
			if (attboxkey != domboxkey) {
				DefaultComboBoxModel newModel = new DefaultComboBoxModel(
						((Attribute) attributeBox.getSelectedItem())
								.getObject().getDomainValueList().toArray());
				domainBox.setModel(newModel);
				domainBox.setSelectedIndex(0);
				domainBox.invalidate();
			}
		}

	}
}