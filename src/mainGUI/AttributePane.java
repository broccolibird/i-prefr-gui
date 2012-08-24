package mainGUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

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
	JScrollPane attributeScroll;
	private JButton plusButton;
	private JFrame parentFrame;
	private PreferencePane preferencePane;
	private SpringLayout layout;
	private LinkedList<AttributeTuple> tuples;
	
	private static int prefWidth = 700;
	private static int prefHeight = 620;
	
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
		tuples = new LinkedList<AttributeTuple>();
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
		
		JLabel name = new JLabel("Attribute Name");
		name.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(name);
		
		attributePanel = new JPanel();
		layout = new SpringLayout();
		attributePanel.setLayout(layout);
		attributePanel.setMaximumSize(new Dimension(prefWidth-20, prefHeight));
		attributePanel.setPreferredSize(new Dimension(680, prefHeight));
		attributeScroll = new JScrollPane(attributePanel);
		attributeScroll.setMinimumSize(new Dimension(prefWidth, 50));
		attributeScroll.setPreferredSize(new Dimension(prefWidth, prefHeight));
		attributeScroll.setMaximumSize(new Dimension(prefWidth, prefHeight));
		attributeScroll.setBorder(null);
		
		// add attribute tuples to panel
		update();
		
		// add plus button to panel
		plusButton = new JButton("+");
		plusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		panel.add(attributeScroll);
		panel.add(plusButton);
		return panel;
	}

	/**
	 * Handles actions performed on the plus button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			addTuple(null);
			pack();
		}
	}

	public void pack() {
		parentFrame.pack();
	}

	@Override
	public void update() {
		tuples.clear();
		System.out.println("min size: " + attributeScroll.getMinimumSize());
		System.out.println("preferred size: " + attributePanel.getPreferredSize());
		// remove all previous attribute tuples
		attributePanel.removeAll();

		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Attribute>> set = attributeMap.entrySet();
		for (Entry<Integer, Attribute> p : set)
			addTuple(p.getKey());
		addTuple(null);
		
		parentFrame.pack();
	}
	
	private void addTuple(Integer key) {
		AttributeTuple tuple;
		if(key != null) {
			tuple = new AttributeTuple(key, attributeMap, parentFrame,
					attributePanel, this, preferencePane);
			
		} else {
			tuple = new AttributeTuple(attributeMap, 
					parentFrame, attributePanel, this, preferencePane);
		}
		
		tuples.add(tuple);
		attributePanel.add(tuple);
		
		int lastTupleIndex = tuples.indexOf(tuple) - 1;
		if(lastTupleIndex >= 0) {
			layout.putConstraint(SpringLayout.NORTH, tuple, 0,
					SpringLayout.SOUTH, tuples.get(lastTupleIndex));
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, tuple, 0,
					SpringLayout.HORIZONTAL_CENTER, tuples.get(lastTupleIndex));
		} else {
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, attributePanel, 0,
					SpringLayout.HORIZONTAL_CENTER, tuple);
		}
		
		int height = (int)tuple.getPreferredSize().getHeight();
		attributePanel.setPreferredSize(new Dimension(prefWidth-20, height*tuples.size()));
		
		tuple.getTextField().requestFocusInWindow();
	}
	
	public void removeTuple(AttributeTuple tuple) {
		int index = tuples.indexOf(tuple);
		
		if (index == 0 && tuples.size() > 1) {
			layout.putConstraint(SpringLayout.NORTH, tuples.get(index+1), 0,
					SpringLayout.NORTH, attributePanel);
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, tuples.get(index+1), 0,
					SpringLayout.HORIZONTAL_CENTER, attributePanel);
		} else if(index == tuples.size()-1){
			layout.removeLayoutComponent(tuple);
		} else if (index > 0 && index != tuples.size()-1) {
			layout.putConstraint(SpringLayout.NORTH, tuples.get(index+1), 0,
					SpringLayout.SOUTH, tuples.get(index-1));
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, tuples.get(index+1), 0,
					SpringLayout.HORIZONTAL_CENTER, tuples.get(index-1));
		}
		
		tuples.remove(tuple);
		int height = (int)tuple.getPreferredSize().getHeight();
		attributePanel.setPreferredSize(new Dimension(prefWidth, height*tuples.size()));
		repaint();
	}
}
