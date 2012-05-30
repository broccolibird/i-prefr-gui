package mainGUI;


import guiElements.tuples.ImportanceTuple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.Importance;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ImportanceMap;

@SuppressWarnings("serial")
public class ImportancePane extends UpdatePane implements ActionListener{

	private AttributeMap attributeMap;
	private ImportanceMap map;
	private JPanel importancePanel;
	private JFrame parentFrame;
	private JButton plusButton;
		
		public ImportancePane(AttributeMap attributeMap,JFrame parent,ImportanceMap map) {
			this.parentFrame=parent;
			this.attributeMap=attributeMap;
			this.map=map;
			this.add(initializeGUI());
		}
		
		private JPanel initializeGUI(){
			JPanel panel= new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			importancePanel = new JPanel();
			importancePanel.setLayout(new BoxLayout(importancePanel, BoxLayout.Y_AXIS));
			update();
			panel.add(importancePanel);
			plusButton = new JButton("+");
			plusButton.addActionListener(this);
			panel.add(plusButton);
			return panel;
		}

		@Override
		public void update() {
			importancePanel.removeAll();
			
			JTextField columnName = new JTextField("Conditional Importance Expression");
			columnName.setEditable(false);
			importancePanel.add(columnName);
			
			
			//for every map entry, add a tuple to the table,then one more
			Collection<Entry<Integer, Importance>> set = map.entrySet();
			System.out.println("set size: "+set.size());
			Attribute[] allAttributes = getAttributes();
			for (Entry<Integer, Importance> p : set)
				importancePanel.add(new ImportanceTuple(p.getKey(),map,parentFrame,importancePanel,allAttributes));
			importancePanel.add(new ImportanceTuple(map,parentFrame,importancePanel,allAttributes));
			parentFrame.pack();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (plusButton == e.getSource()) {
				importancePanel.add(new ImportanceTuple(map,parentFrame,importancePanel,getAttributes()));
				parentFrame.pack();
			}
		}
		
		private Attribute[] getAttributes(){
			return (Attribute[])attributeMap.values().toArray(new Attribute[attributeMap.size()]);
		}

}
