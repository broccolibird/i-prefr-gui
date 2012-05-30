package guiElements;

import guiElements.tuples.EdgeTuple;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.Attribute;
import dataStructures.EdgeStatement;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.EdgeStatementMap;

@SuppressWarnings("serial")
public class EdgeAnnotationDialog extends AbstractTupleDialog<EdgeStatement>{
	Attribute attribute1;
	Attribute attribute2;
	AttributeMap attributeMap;

	public EdgeAnnotationDialog(JFrame frame, EdgeStatementMap oldMap,AttributeMap attributeMap,Attribute a1,Attribute a2) {
		super(frame, oldMap);
		this.attribute1=a1;
		this.attribute2=a2;
		this.attributeMap=attributeMap;
		updateTablePanel();
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
		
	}

	@Override
	protected void updateTablePanel() {
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		// the label at the top
		JPanel tableLabel = new JPanel();
		tableLabel.setLayout(new BoxLayout(tableLabel, BoxLayout.X_AXIS));
		JLabel left = new JLabel("ConditionMap  ");
		JLabel right = new JLabel("DomainPreferenceMap                   ");
		tableLabel.add(left);
		tableLabel.add(right);
		tablePanel.add(tableLabel);
		
		// adds an EdgeTuple for each statement in the map, and one more
		Collection<Entry<Integer, EdgeStatement>> set = map.entrySet();
		for (Entry<Integer, EdgeStatement> p : set){
			tablePanel.add(new EdgeTuple(p.getKey(), (EdgeStatementMap) map, parentWindow, tablePanel, attributeMap,attribute1, attribute2));
		}
		tablePanel.add(new EdgeTuple((EdgeStatementMap) map, parentWindow,
				tablePanel,attributeMap, attribute1, attribute2));
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			tablePanel.add(new EdgeTuple((EdgeStatementMap) map, parentWindow,
					tablePanel, attributeMap, attribute1,attribute2));
			this.pack();
		} else if (okButton == e.getSource()) {
			map.clearUnusedKeys();
			dispose();
		}

	}
}