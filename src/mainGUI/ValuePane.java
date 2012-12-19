package mainGUI;

import guiElements.tuples.ValueTuple;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

		JScrollPane scrollPane = new JScrollPane(valuePanel);
		scrollPane.setMaximumSize(new Dimension(675, 650));
		scrollPane.setBorder(null);
		
		update();
		
		panel.add(scrollPane);
		return panel;
	}

	@Override
	public void update() {
		valuePanel.removeAll();
		if (alternativeMap.useEntireAlternativeSpace()) {
			valuePanel.add(new JLabel("Use Entire Possible Alternative Space"));
		} else {
			// put column names at top of valuePanel
			ArrayList<Attribute> allAttributes = new ArrayList<Attribute>(
					attributeMap.values());			
			int numAttributes = allAttributes.size();

			valuePanel.setLayout(new GridLayout(0, numAttributes+1));
			
			JTextField origin = new JTextField("Alternative");
			origin.setEditable(false);
			valuePanel.add(origin);

			for (Attribute att : allAttributes) {
				JTextField attName = new JTextField(att.getName());
				attName.setEditable(false);
				valuePanel.add(attName);
			}

			// make a tuple for every alternative
			LinkedList<Alternative> allAlternatives = new LinkedList<Alternative>(
					alternativeMap.values());
//			Collections.sort(allAlternatives);

			for (Alternative a : allAlternatives) {
				if(a.getKey()==null){
					System.out.println("why is the key null?");
				}
				ValueTuple tuple = new ValueTuple(a.getKey(), alternativeMap,
						parentFrame, this, allAttributes);
				valuePanel.add(tuple.getKey());
				JComboBox boxes[] = tuple.getValues();
				for(int i = 0; i < boxes.length; i++) {
					valuePanel.add(boxes[i]);
				}
			}
		}
		parentFrame.pack();
	}
}
