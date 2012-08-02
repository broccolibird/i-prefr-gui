package mainGUI.panes;

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

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;
import guiElements.tuples.AttributeTuple;

/**
 * The AttributePane is an UpdatePane with fields for entry of
 * Preference Network Attributes.
 */
@SuppressWarnings("serial")
public class AttributePane extends UpdatePane implements ActionListener {

	private AttributeMap attributeMap;
	private JPanel attributePanel;
	private JButton plusButton;
	private JFrame parentFrame;
	private PreferencePane preferencePane;

	/**
	 * Creates a new instance of AttributePane
	 * @param attributeMap 
	 * @param preferencePane
	 * @param parentFrame
	 */
	public AttributePane(AttributeMap attributeMap,
			PreferencePane preferencePane, JFrame parentFrame) {
		this.attributeMap = attributeMap;
		this.parentFrame = parentFrame;
		this.preferencePane = preferencePane;
		this.add(initializeGUI());
		setVisible(true);
	}

	/**
	 * Setup the GUI
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		attributePanel = new JPanel();
		attributePanel
				.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
		
		// add attribute tuples to panel
		update();
		
		// add plus button to panel
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
		
		// add attribute tuples and plus button to panel
		panel.add(attributePanel);
		panel.add(plusButton);
		return panel;
	}

	/**
	 * Handles actions performed on the plus button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			AttributeTuple tuple = (AttributeTuple) attributePanel.add(new AttributeTuple(
					attributeMap, parentFrame, attributePanel, preferencePane));
			tuple.getTextField().requestFocusInWindow();
			pack();
		}
	}

	public void pack() {
		parentFrame.pack();
	}

	@Override
	public void update() {
		// remove all previous attribute tuples
		attributePanel.removeAll();
		
		JPanel label = new JPanel();
		// TODO - is this necessary to set the layout?
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		JLabel name = new JLabel("Attribute Name");
		label.add(name);
		attributePanel.add(label);

		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Attribute>> set = attributeMap.entrySet();
		for (Entry<Integer, Attribute> p : set)
			attributePanel.add(new AttributeTuple(p.getKey(), attributeMap, parentFrame,
					attributePanel, preferencePane));
		AttributeTuple tuple = (AttributeTuple) attributePanel.add(new AttributeTuple(attributeMap, 
									parentFrame, attributePanel, preferencePane));
		tuple.getTextField().requestFocusInWindow();
		parentFrame.pack();
	}
}
