package mainGUI;

import guiElements.tuples.DomainTuple;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;

/**
 * The DomainPane is an UpdatePane with fields for entry of
 * Attribute Domains.
 */
@SuppressWarnings("serial")
public class DomainPane extends UpdatePane {

	protected AttributeMap map;
	protected JPanel domainPanel;
	protected JFrame parentFrame;

	/**
	 * Create a new instance of DomainPane
	 * @param oldMap
	 * @param parentFrame
	 */
	public DomainPane(AttributeMap oldMap, JFrame parentFrame) {
		this.map = oldMap;
		this.parentFrame = parentFrame;
		this.add(initializeGUI());
		setVisible(true);
	}

	/**
	 * Setup GUI for DomainPane
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(0, 2));
		
		JTextField name = new JTextField("Attribute Name");
		JTextField domainEnum = new JTextField("Domain Enumeration");
		name.setEditable(false);
		domainEnum.setEditable(false);
		headerPanel.add(name);
		headerPanel.add(domainEnum);
		panel.add(headerPanel);
		
		domainPanel = new JPanel();
		domainPanel.setLayout(new GridLayout(0,2));
		
		JScrollPane scrollPane = new JScrollPane(domainPanel);
		scrollPane.setPreferredSize(new Dimension(550, 650));
		scrollPane.setBorder(null);
		
		update();
		panel.add(scrollPane);
		return panel;
	}
 
	public void update() {
		// for every map entry, add a tuple to the table
		domainPanel.removeAll();
		
		Collection<Entry<Integer, Attribute>> set = map.entrySet();
		for (Entry<Integer, Attribute> p : set){
			addTuple(p.getKey());
		}
		parentFrame.pack();
	}
	
	protected void addTuple(Integer key){
		DomainTuple tuple = new DomainTuple(key, map, parentFrame, domainPanel);
		domainPanel.add(tuple.getKey());
		domainPanel.add(tuple.getValue());
	}
}
