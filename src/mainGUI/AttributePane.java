package mainGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.EdgeStatementMap;
import edu.uci.ics.jung.graph.Graph;
import guiElements.tuples.AttributeTuple;

@SuppressWarnings("serial")
public class AttributePane extends UpdatePane implements ActionListener {

	private AttributeMap map;
	private JPanel attributePanel;
	private JButton plusButton;
	private JFrame parentFrame;
	private Graph<Attribute, EdgeStatementMap> graph;

	public AttributePane(AttributeMap oldMap,
			Graph<Attribute, EdgeStatementMap> graph2, JFrame parentFrame) {
		this.map = oldMap;
		this.parentFrame = parentFrame;
		this.graph = graph2;
		this.add(createGUI());
		setVisible(true);
	}

	private JPanel createGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		attributePanel = new JPanel();
		attributePanel
				.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
		update();
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		panel.add(attributePanel);
		panel.add(plusButton);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			attributePanel.add(new AttributeTuple(map, parentFrame,
					attributePanel, graph));
			pack();
		}
	}

	public void pack() {
		parentFrame.pack();
	}

	@Override
	public void update() {
		attributePanel.removeAll();
		JPanel label = new JPanel();
		// TODO - is this necessary to set the layout?
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		JLabel name = new JLabel("Attribute Name");
		label.add(name);
		attributePanel.add(label);

		// for every map entry, add a tuple to the table,then one more
		Collection<Entry<Integer, Attribute>> set = map.entrySet();
		for (Entry<Integer, Attribute> p : set)
			attributePanel.add(new AttributeTuple(p.getKey(), map, parentFrame,
					attributePanel, graph));
		attributePanel.add(new AttributeTuple(map, parentFrame, attributePanel,
				graph));
		parentFrame.pack();
	}
}
