package mainGUI;

import guiElements.tuples.AlternativeTuple;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import dataStructures.Alternative;
import dataStructures.maps.AlternativeMap;

/**
 * AlternativePane is an UpdatePane with fields for entry of
 * Preference Network Alternatives.
 */
@SuppressWarnings("serial")
public class AlternativePane extends UpdatePane implements ActionListener {
	private AlternativeMap map;
	private JPanel alternativePanel;
	private JFrame parentFrame;
	private JButton plusButton;
	private JRadioButton useEntireSpace;
	private JRadioButton specifySpace;

	/**
	 * Create new AlternativePane instance
	 * @param alternativeMap
	 * @param parent
	 */
	public AlternativePane(AlternativeMap alternativeMap, JFrame parent) {
		this.parentFrame = parent;
		this.map = alternativeMap;
		this.add(initializeGUI());
	}

	/**
	 * Setup GUI for AlternativePane
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		alternativePanel = new JPanel();
		alternativePanel.setLayout(new BoxLayout(alternativePanel,
				BoxLayout.Y_AXIS));
		
		panel.add(getRadioButtonPanel());
		
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
		
		
		update();
		panel.add(alternativePanel);
		panel.add(plusButton);
		return panel;
	}

	/**
	 * Create radio buttons
	 * @return radio button JPanel
	 */
	private JPanel getRadioButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// create radio buttons
		useEntireSpace = new JRadioButton("Use Entire Possible Alternative Space");
		useEntireSpace.addActionListener(this);
		useEntireSpace.setMnemonic(KeyEvent.VK_U);
		
		specifySpace = new JRadioButton("Specify Specific Alternatives");
		specifySpace.addActionListener(this);	
		specifySpace.setMnemonic(KeyEvent.VK_S);

		// add radio buttons to button group
		ButtonGroup group = new ButtonGroup();
		group.add(useEntireSpace);
		group.add(specifySpace);

		// add radio buttons to appropriate panels
		JPanel useEntirePanel = new JPanel();
		useEntirePanel
				.setLayout(new BoxLayout(useEntirePanel, BoxLayout.X_AXIS));
		JPanel specifyPanel = new JPanel();
		specifyPanel.setLayout(new BoxLayout(specifyPanel, BoxLayout.X_AXIS));

		useEntirePanel.add(useEntireSpace);

		specifyPanel.add(specifySpace);

		panel.add(useEntirePanel);
		panel.add(specifyPanel);
		
		// Add keyboard shortcuts for radio buttons
		InputMap panelInputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		panelInputMap.put(KeyStroke.getKeyStroke("U"), "selectUseEntire");
		panelInputMap.put(KeyStroke.getKeyStroke("S"), "selectSpecify");
		
		panel.getActionMap().put("selectUseEntire", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				useEntireSpace.doClick();
			}
		});
		
		panel.getActionMap().put("selectSpecify", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e){
				specifySpace.doClick();
			}
		});
		
		return panel;
	}

	/**
	 * Set correct radio button
	 */
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
		AlternativeTuple tuple = (AlternativeTuple) alternativePanel.add(
				new AlternativeTuple(map, parentFrame, alternativePanel));
		tuple.getTextField().requestFocusInWindow();
		parentFrame.pack();
		enableDisableAlternativePanel();
	}
	
	/**
	 * Enable or disable Alternative entry fields based on the current
	 * radio button selection
	 */
	private void enableDisableAlternativePanel(){
		Component[] components = alternativePanel.getComponents();
		boolean enabled = !map.useEntireAlternativeSpace();
		for (Component c : components) {
			c.setEnabled(enabled);
		}
		plusButton.setEnabled(enabled);
	}

	/**
	 * Handles actions performed on the plus button and
	 * radio buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (plusButton == source) {
			AlternativeTuple tuple = (AlternativeTuple) alternativePanel.add(
					new AlternativeTuple(map, parentFrame, alternativePanel));
			tuple.getTextField().requestFocusInWindow();
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
