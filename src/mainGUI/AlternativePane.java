package mainGUI;

import guiElements.tuples.AlternativeTuple;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dataStructures.Alternative;
import dataStructures.maps.AlternativeMap;

@SuppressWarnings("serial")
public class AlternativePane extends UpdatePane implements ActionListener {
	private AlternativeMap map;
	private JPanel alternativePanel;
	private JFrame parentFrame;
	private JButton plusButton;
	private JRadioButton useEntireSpace;
	private JRadioButton specifySpace;

	public AlternativePane(AlternativeMap alternativeMap, JFrame parent) {
		this.parentFrame = parent;
		this.map = alternativeMap;
		this.add(initializeGUI());
	}

	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		alternativePanel = new JPanel();
		alternativePanel.setLayout(new BoxLayout(alternativePanel,
				BoxLayout.Y_AXIS));
		panel.add(getRadioButtonPanel());
		update();
		panel.add(alternativePanel);
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		panel.add(plusButton);
		return panel;
	}

	private JPanel getRadioButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		useEntireSpace = new JRadioButton();
		useEntireSpace.addActionListener(this);

		specifySpace = new JRadioButton();
		specifySpace.addActionListener(this);

		ButtonGroup group = new ButtonGroup();
		group.add(useEntireSpace);
		group.add(specifySpace);

		JPanel useEntirePanel = new JPanel();
		useEntirePanel
				.setLayout(new BoxLayout(useEntirePanel, BoxLayout.X_AXIS));
		JPanel specifyPanel = new JPanel();
		specifyPanel.setLayout(new BoxLayout(specifyPanel, BoxLayout.X_AXIS));

		useEntirePanel.add(useEntireSpace);
		useEntirePanel.add(new JLabel("Use Entire Possible Alternative Space"));

		specifyPanel.add(specifySpace);
		specifyPanel.add(new JLabel("Specify Specific Alternatives"));

		panel.add(useEntirePanel);
		panel.add(specifyPanel);
		return panel;
	}

	private void updateRadioButtons() {
		if (map.useEntireAlternativeSpace()) {
			useEntireSpace.setSelected(true);
		} else {
			specifySpace.setSelected(true);
		}
	}

	@Override
	public void update() {
		updateRadioButtons();
		alternativePanel.removeAll();

		JTextField columnName = new JTextField("Alternative");
		columnName.setEditable(false);
		alternativePanel.add(columnName);

		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Alternative>> set = map.entrySet();
		for (Entry<Integer, Alternative> p : set)
			alternativePanel.add(new AlternativeTuple(p.getKey(), map,
					parentFrame, alternativePanel));
		alternativePanel.add(new AlternativeTuple(map, parentFrame,
				alternativePanel));
		parentFrame.pack();
		enableDisableAlternativePanel();
	}
	
	private void enableDisableAlternativePanel(){
		Component[] components = alternativePanel.getComponents();
		if (map.useEntireAlternativeSpace()) {
			for (Component c : components) {
				c.setEnabled(false);
			}
		}else{
			for (Component c : components) {
				c.setEnabled(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (plusButton == source) {
			alternativePanel.add(new AlternativeTuple(map, parentFrame,
					alternativePanel));
			pack();
		} else if (source == useEntireSpace) {
			map.setUseEntireAlternativeSpace(true);
			useEntireSpace.setSelected(true);
			enableDisableAlternativePanel();
		} else if (source == specifySpace) {
			map.setUseEntireAlternativeSpace(false);
			specifySpace.setSelected(true);
			enableDisableAlternativePanel();
		}
	}

	public void pack() {
		parentFrame.pack();
	}

}
