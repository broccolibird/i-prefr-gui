package guiElements;

import guiElements.tuples.DomainPreferenceTuple;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;

import dataStructures.Attribute;
import dataStructures.DomainPreference;
import dataStructures.maps.DomainPreferenceMap;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractTupleDialog<DomainPreference> {
	private Attribute attribute;

	public PreferenceDialog(Window owner, DomainPreferenceMap map, Attribute attribute) {
		super(owner, map);
		this.attribute = attribute;
		updateTablePanel();
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	@Override
	protected void updateTablePanel() {
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		// add a PreferenceTuple for each preference element in the map, and one
		// more
		Collection<Entry<Integer, DomainPreference>> set = map.entrySet();
		for (Entry<Integer, DomainPreference> p : set)
			tablePanel.add(new DomainPreferenceTuple(p.getKey(), map, this,
					tablePanel, attribute));
		tablePanel.add(new DomainPreferenceTuple(map, this, tablePanel, attribute));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			tablePanel
					.add(new DomainPreferenceTuple(map, this, tablePanel, attribute));
			this.pack();
		} else if (okButton == e.getSource()) {
			map.clearUnusedKeys();
			dispose();
		}

	}

}
