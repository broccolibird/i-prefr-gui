package mainGUI;

import guiElements.tuples.ValueTuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Alternative;
import dataStructures.Attribute;
import dataStructures.maps.AlternativeMap;
import dataStructures.maps.AttributeMap;

/**
 * The ValuePane is an UpdatePane with fields for entry of
 * Alternative Values.
 */
@SuppressWarnings("serial")
public class ValuePane extends UpdatePane{
	private AlternativeMap alternativeMap;
	private AttributeMap attributeMap;
	private JPanel valuePanel;
	private JFrame parentFrame;

	/**
	 * Create a new ValuePane instance
	 * @param alternativeMap
	 * @param attributeMap
	 * @param parent
	 */
	public ValuePane(AlternativeMap alternativeMap, AttributeMap attributeMap,
			JFrame parent) {
		this.parentFrame = parent;
		this.alternativeMap = alternativeMap;
		this.attributeMap = attributeMap;
		this.add(initializeGUI());
	}

	/**
	 * Setup the ValuePane GUI
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
		update();
		panel.add(valuePanel);
		return panel;
	}

	@Override
	public void update() {
		valuePanel.removeAll();
		if (alternativeMap.useEntireAlternativeSpace()) {
			valuePanel.add(new JLabel("Use Entire Possible Alternative Space"));
		} else {
			// put column names at top of valuePanel
			//TODO - make sure this sorting keeps the attribute column names
			//alligned with the comboboxes
			ArrayList<Attribute> allAttributes = new ArrayList<Attribute>(
					attributeMap.values());
			for(Attribute a : allAttributes){
				System.out.println(a.toXML());
			}
			Collections.sort(allAttributes);

			JPanel columnTitles = new JPanel();
			columnTitles
					.setLayout(new BoxLayout(columnTitles, BoxLayout.X_AXIS));

			JTextField origin = new JTextField("Alternative");
			origin.setEditable(false);
			columnTitles.add(origin);

			for (Attribute att : allAttributes) {
				JTextField attName = new JTextField(att.getName());
				attName.setEditable(false);
				columnTitles.add(attName);
			}
			valuePanel.add(columnTitles);

			// make a tuple for every alternative
			LinkedList<Alternative> allAlternatives = new LinkedList<Alternative>(
					alternativeMap.values());
			//System.out.println("there are "+allAlternatives.size()+" alternatives in ValuePane");
			Collections.sort(allAlternatives);

			for (Alternative a : allAlternatives) {
				if(a.getKey()==null){
					System.out.println("why is the key null?");
				}
				valuePanel.add(new ValueTuple(a.getKey(), alternativeMap,
						parentFrame, this, allAttributes));
			}
		}
		parentFrame.pack();
	}
}
