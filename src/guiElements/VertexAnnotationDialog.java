package guiElements;

import guiElements.tuples.VertexTuple;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.Attribute;
import dataStructures.VertexStatement;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.VertexStatementMap;

@SuppressWarnings("serial")
public class VertexAnnotationDialog extends AbstractTupleDialog<VertexStatement> {
	private Attribute attribute;
	private AttributeMap attributeMap;

	public VertexAnnotationDialog(JFrame frame, VertexStatementMap map,
			AttributeMap attributeMap, Attribute attribute) {
		super(frame, map);
		this.attributeMap = attributeMap;
		this.attribute = attribute;
		// if(this.attribute==null){
		// System.out.println("attribute null in VertexAnnotationDialog");
		// }
		if (this.attributeMap == null) {
			System.out.println("attributeMap null in VertexAnnotationDialog");
		}
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

		// adds a VertexTuple for each statement in the map, and one more
		Collection<Entry<Integer, VertexStatement>> set = map.entrySet();
		for (Entry<Integer, VertexStatement> p : set) {
			tablePanel.add(new VertexTuple(p.getKey(), (VertexStatementMap) map,
					parentWindow, tablePanel, attributeMap, attribute));
		}
		// if(attributeMap==null){
		// System.out.println("attributeMap null in Vertex Annotation Dialog");
		// }
		// if(attribute==null){
		// System.out.println("attribute null in Vertex Annotation Dialog");
		// }
		tablePanel.add(new VertexTuple((VertexStatementMap) map, parentWindow,
				tablePanel, attributeMap, attribute));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (attribute == null) {
			System.out.println("attribute null in Vertex Annotation Dialog");
		}
		if (plusButton == e.getSource()) {
			tablePanel.add(new VertexTuple((VertexStatementMap) map, parentWindow,
					tablePanel, attributeMap, attribute));
			this.pack();
		} else if (okButton == e.getSource()) {
			map.clearUnusedKeys();
			dispose();
		}

	}

}
