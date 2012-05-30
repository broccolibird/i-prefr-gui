package guiElements.tuples;

import guiElements.ConditionDialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.AttributePreference;
import dataStructures.EdgeStatement;
import dataStructures.Pair;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ConditionMap;
import dataStructures.maps.EdgeStatementMap;

@SuppressWarnings("serial")
public class EdgeTuple extends AbstractTuple<EdgeStatement> {

	private JTextField left;
	// the greater Attribute will be the one on the left of the '>' symbol,
	// which is why this is selected when the left of the pair matches the
	// 'first' attribute
	private JRadioButton firstGreater;
	private JRadioButton secondGreater;
	
	private JButton xButton;
	private AttributeMap attributeMap;
	private Attribute attribute1;
	private Attribute attribute2;

	public EdgeTuple(Integer key, EdgeStatementMap map, Window parentWindow,
			JPanel parentPanel, AttributeMap attributeMap,
			Attribute attribute1, Attribute attribute2) {
		super(key, map, parentWindow, parentPanel);
		this.attributeMap = attributeMap;
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		initializeGUI();
	}

	public EdgeTuple(EdgeStatementMap map, Window parentWindow,
			JPanel parentPanel, AttributeMap attributeMap,
			Attribute attribute1, Attribute attribute2) {
		super(map, parentWindow, parentPanel);
		this.attributeMap = attributeMap;
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		left = new JTextField(17);
		left.addMouseListener(new ConditionFieldListener());
		left.setEditable(false);
		this.add(left);
		
		firstGreater = new JRadioButton();
		firstGreater.addActionListener(this);

		secondGreater = new JRadioButton();
		secondGreater.addActionListener(this);
		
	    ButtonGroup group = new ButtonGroup();
	    group.add(firstGreater);
	    group.add(secondGreater);
	    
		JPanel preferencePanel = new JPanel();
		preferencePanel.setLayout(new BoxLayout(preferencePanel,
				BoxLayout.Y_AXIS));
		JPanel firstStatementPanel = new JPanel();
		firstStatementPanel.setLayout(new BoxLayout(firstStatementPanel,BoxLayout.X_AXIS));
		JPanel secondStatementPanel = new JPanel();
		secondStatementPanel.setLayout(new BoxLayout(secondStatementPanel,BoxLayout.X_AXIS));
		firstStatementPanel.add(new JLabel(attribute1.toString()+" > "+attribute2.toString()));
		firstStatementPanel.add(firstGreater);
		secondStatementPanel.add(new JLabel(attribute2.toString()+" > "+attribute1.toString()));
		secondStatementPanel.add(secondGreater);
		preferencePanel.add(firstStatementPanel);
		preferencePanel.add(secondStatementPanel);
		this.add(preferencePanel);


		xButton = new JButton("x");
		xButton.addActionListener(this);
		this.add(xButton);

		updateTuple(map.get(key));
	}

	public void updateTuple(Pair<ConditionMap, AttributePreference> pair) {
		String lString = "click to enter ConditionMap";
		Attribute leftAttribute = null;
		if (pair != null) {
			lString = pair.getLeft() == null ? lString : pair.getLeft()
					.toString();
			if(pair.getRight()!=null){
				leftAttribute = pair.getRight().getLeft();
			}
		}
		this.left.setText(lString);
		
		if(leftAttribute!=null){
			if(leftAttribute.getKey()==attribute1.getKey()){
				firstGreater.setSelected(true);
			}else if(leftAttribute.getKey()==attribute2.getKey()){
				secondGreater.setSelected(true);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source==xButton){
			parentPanel.remove(this);
			map.remove(key);
			parentWindow.pack();
		}else if(source==firstGreater){
			AttributePreference newPreference = new AttributePreference(attribute1,attribute2);
			Pair<ConditionMap, AttributePreference> pair = map.get(key);
			if(pair==null){
				map.put(key, new EdgeStatement(null,newPreference));
			}else{
				pair.setRight(newPreference);
			}
			firstGreater.setSelected(true);
		}else if(source==secondGreater){
			AttributePreference newPreference = new AttributePreference(attribute2,attribute1);
			Pair<ConditionMap, AttributePreference> pair = map.get(key);
			if(pair==null){
				map.put(key, new EdgeStatement(null,newPreference));
			}else{
				pair.setRight(newPreference);
			}
			secondGreater.setSelected(true);
		}
	}

	class ConditionFieldListener implements MouseListener {

		public ConditionFieldListener() {

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			EdgeStatement statement = map.get(key);
			if (statement == null) {
				statement = new EdgeStatement(null, null);
			}
			ConditionMap conditionMap = statement.getLeft();
			boolean newItem = false;
			if (conditionMap == null) {
				conditionMap = new ConditionMap();
				newItem = true;
				System.out.println("newItem - conditionMap");
			}
			@SuppressWarnings("unused")
			ConditionDialog conditionDialog = new ConditionDialog(parentWindow,
					conditionMap, attributeMap);

			// if the item is a new condition, then the statement map has no
			// reference to it, so put it into the statement map (as long as it
			// is not empty) - if it is empty then the user probably clicked on
			// it by mistake and closed it
			System.out.println("conditionMap size: " + conditionMap.size());
			if (newItem && !conditionMap.isEmpty()) {
				System.out.println("putting conditionMap into statement: "
						+ conditionMap.toString());
				map.put(key,
						new EdgeStatement(conditionMap, statement.getRight()));
			}
			updateTuple(map.get(key));
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
