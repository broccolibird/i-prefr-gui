package guiElements;

import guiElements.tuples.ConditionTuple;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.ConditionElement;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ConditionMap;

@SuppressWarnings("serial")
public class ConditionDialog extends AbstractTupleDialog<ConditionElement> {
	AttributeMap attributeMap;

	public ConditionDialog(Window parentWindow, ConditionMap map,
			AttributeMap attributeMap) {
		super(parentWindow, map);
		this.attributeMap=attributeMap;
		updateTablePanel();
		pack();
		setLocationRelativeTo(parentWindow);
		setVisible(true);
		if(attributeMap==null){
			System.out.println("attributeMap null in ConditionDialog111111111");
		}
	}

	@Override
	protected void updateTablePanel() {
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		
		JPanel labelPanel=new JPanel();
		labelPanel.setLayout(new GridLayout(1,4));
		labelPanel.add(new JLabel("TRUE     NOT"));
		labelPanel.add(new JLabel("Attribute"));
		labelPanel.add(new JLabel("OP"));
		labelPanel.add(new JLabel("Domain Value"));
		tablePanel.add(labelPanel);
		
		// add a ConditionTuple for each condition element in the map, and one more
		Collection<Entry<Integer, ConditionElement>> set = map.entrySet();
		for (Entry<Integer, ConditionElement> p : set){
			ConditionTuple tuple = new ConditionTuple(p.getKey(), map,this,
					tablePanel,attributeMap);
			tablePanel.add(tuple);
		}
		
		ConditionTuple tuple = new ConditionTuple(map, this, tablePanel,attributeMap);
		tablePanel.add(tuple);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			ConditionTuple tuple = new ConditionTuple(map, this, tablePanel,attributeMap);
			tablePanel.add(tuple);
			this.pack();
		} else if (okButton == e.getSource()) {
			map.clearUnusedKeys();
			dispose();
		}

	}

}
