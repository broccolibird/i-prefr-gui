package mainGUI;

import guiElements.ScrollPanePlus;
import guiElements.tuples.AbstractTuple;
import guiElements.tuples.AlternativeTuple;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dataStructures.Alternative;
import dataStructures.maps.AlternativeMap;

/**
 * AlternativePane is an UpdatePane with fields for entry of
 * Preference Network Alternatives.
 */
@SuppressWarnings("serial")
public class AlternativePane extends ScrollPanePlus implements ActionListener {
	private AlternativeMap map;
	
	private JRadioButton useEntireSpace;
	private JRadioButton specifySpace;
	
	private AlternativePane alternativePane= this;
	
	private static int prefWidth = 700;
	private static int prefHeight = 570;

	private TupleFactory tupleFactory = new TupleFactory() {
		public AlternativeTuple create() {
			return new AlternativeTuple(map, parentFrame, 
					alternativePane, containerPanel);
		}
		
		public AlternativeTuple create(Integer key) {
			return new AlternativeTuple(key, map, 
					parentFrame, alternativePane, containerPanel);
		}
	};
	
	/**
	 * Create new AlternativePane instance
	 * @param alternativeMap
	 * @param parent
	 */
	public AlternativePane(AlternativeMap alternativeMap, JFrame parent) {
		super(parent);
		
		super.setTupleFactory(tupleFactory);
		
		this.map = alternativeMap;
		initializeGUI();
	}

	/**
	 * Setup GUI for AlternativePane
	 * @return JPanel
	 */
	private void initializeGUI() {
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.add(getRadioButtonPanel());
		JTextField columnName = new JTextField("Alternative");
		columnName.setEditable(false);
		headerPanel.add(columnName);
		
		containerPanel.setMaximumSize(new Dimension(prefWidth-20, prefHeight));
		containerPanel.setPreferredSize(new Dimension(680, prefHeight));
		
		scrollPane.setMinimumSize(new Dimension(prefWidth, 50));
		scrollPane.setPreferredSize(new Dimension(prefWidth, prefHeight));
		scrollPane.setMaximumSize(new Dimension(prefWidth, prefHeight));
				
		update();
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
		specifySpace.setMnemonic(KeyEvent.VK_P);

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
		containerPanel.removeAll();


		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Alternative>> set = map.entrySet();
		for (Entry<Integer, Alternative> p : set)
			addTuple(p.getKey());
		AlternativeTuple tuple = (AlternativeTuple) addTuple(null);
		tuple.getTextField().requestFocusInWindow();
		parentFrame.pack();
		enableDisableAlternativePanel();
	}
	
	/**
	 * Enable or disable Alternative entry fields based on the current
	 * radio button selection
	 */
	private void enableDisableAlternativePanel(){
		Component[] components = containerPanel.getComponents();
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
			AlternativeTuple tuple = (AlternativeTuple)addTuple(null);
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
	
	@Override
	protected void resize(AbstractTuple tuple) {
		int height = (int)tuple.getPreferredSize().getHeight();
		containerPanel.setPreferredSize(new Dimension(prefWidth-20, height*tuples.size()));
		repaint();
		
	}

}
