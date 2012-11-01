package mainGUI;

import guiElements.ScrollPanePlus;
import guiElements.tuples.AbstractTuple;
import guiElements.tuples.AttributeTuple;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;

/**
 * The AttributePane is an UpdatePane with fields for entry of
 * Preference Network Attributes.
 */
@SuppressWarnings("serial")
public class AttributePane extends ScrollPanePlus implements ActionListener {

	private AttributeMap attributeMap;
	private PreferencePane preferencePane;
		
	private static int prefWidth = 700;
	private static int prefHeight = 620;
	
	final AttributePane attributePane = this;
	
	private TupleFactory tupleFactory = new TupleFactory() {
		public AttributeTuple create() {
			return new AttributeTuple(attributeMap, parentFrame,
					containerPanel, attributePane , preferencePane);
		}
		
		public AttributeTuple create(Integer key) {
			return new AttributeTuple(key, attributeMap, parentFrame,
					containerPanel, attributePane , preferencePane);
		}
	};
	
	/**
	 * Creates a new instance of AttributePane
	 * @param attributeMap 
	 * @param preferencePane
	 * @param parentFrame
	 */
	public AttributePane(AttributeMap attributeMap,
			PreferencePane preferencePane, JFrame parentFrame) {
		super(parentFrame);
		
		this.attributeMap = attributeMap;
		this.preferencePane = preferencePane;

		super.setTupleFactory(tupleFactory);
		
		initializeGUI();
		
		setVisible(true);
	}
	

	/**
	 * Setup the GUI
	 */
	private void initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel name = new JLabel("Attribute Name");
		name.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(name);
		
		containerPanel.setMaximumSize(new Dimension(prefWidth-20, prefHeight));
		containerPanel.setPreferredSize(new Dimension(680, prefHeight));
		
		scrollPane.setMinimumSize(new Dimension(prefWidth, 50));
		scrollPane.setPreferredSize(new Dimension(prefWidth, prefHeight));
		scrollPane.setMaximumSize(new Dimension(prefWidth, prefHeight));
		
		// add attribute tuples to panel
		update();
	}
	
	@Override
	public void update() {
		// remove all previous attribute tuples
		tuples.clear();
		containerPanel.removeAll();

		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Attribute>> set = attributeMap.entrySet();
		for (Entry<Integer, Attribute> p : set)
			addTuple(p.getKey());
		addTuple(null);
		
		parentFrame.pack();
	}
	
	protected AbstractTuple addTuple(Integer key) {
		AttributeTuple tuple = (AttributeTuple) super.addTuple(key);
		
		tuple.getTextField().requestFocusInWindow();
		
		return tuple;
	}
	
	protected void resize(AbstractTuple tuple) {
		int height = (int)tuple.getPreferredSize().getHeight();
		containerPanel.setPreferredSize(new Dimension(prefWidth-20, height*tuples.size()));
		repaint();
	}
}
