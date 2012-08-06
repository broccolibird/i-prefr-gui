package guiElements.tuples;

import guiElements.ImportanceDialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.AttributeList;
import dataStructures.Importance;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ImportanceMap;

@SuppressWarnings("serial")
public class ImportanceTuple extends AbstractTuple<Importance> implements
		ActionListener {
	
	private static final int VALID_EXPRESSION = 0;
	private static final int INCOMPLETE_EXPRESSION = 1;
	private static final int INVALID_EXPRESSION = 2;
	
	protected JTextField alternativeName;
	protected JButton xButton;
	protected JButton validateButton;
	protected Attribute[] allAttributes;
	protected JTextField[] fields;

	public ImportanceTuple(Integer key, ImportanceMap map, JFrame parent,
			JPanel parentPanel,Attribute[] allAttributes) {
		super(key, map, parent, parentPanel);
		this.allAttributes=allAttributes;
		initializeGUI();
	}

	public ImportanceTuple(ImportanceMap map, JFrame parent, JPanel parentPanel,Attribute[] allAttributes) {
		super(map, parent, parentPanel);
		this.allAttributes=allAttributes;
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		String[] fieldContents = {"{}","{}","{}","{}"};
		
		//if this tuple maps to an existing entry, get the field contents from it
		Importance existingImportance = map.get(key);
		if(existingImportance!=null){
			for(int i=0;i<4;i++){
				AttributeList attributeList = existingImportance.getList(i);
				if(attributeList!=null){
					fieldContents[i]=attributeList.toString();
				}
			}
		}
		fields = new JTextField[4];
		for(int i=0;i<4;i++){
			fields[i] = new JTextField(10);
			fields[i].setText(fieldContents[i]);
			fields[i].setEditable(false);
			fields[i].addMouseListener(new ImportanceFieldListener(i));
		}
		
		fields[0].setToolTipText("If I have");
		this.add(fields[0]);
		this.add(new JLabel(";"));
		
		fields[1].setToolTipText("and I do not have");
		this.add(fields[1]);
		this.add(new JLabel(":"));
		
		fields[2].setToolTipText("then I prefer");
		this.add(fields[2]);
		this.add(new JLabel(";"));
		
		fields[3].setToolTipText("over");
		this.add(fields[3]);
		
		validateButton = new JButton("validate");
		validateButton.addActionListener(this);
		this.add(validateButton);
		
		xButton = new JButton("remove");
		xButton.addActionListener(this);
		this.add(xButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==xButton){
			parentPanel.remove(this);
			map.remove(key);
			parentWindow.pack();
		}else if(e.getSource()==validateButton){
			int valid = validateExpression();
			if(valid == VALID_EXPRESSION) {
				validateButton.setText("valid");
				validateButton.setEnabled(false);
				validateButton.setOpaque(true);
				validateButton.setBackground(Color.GREEN);
				validateButton.setToolTipText("valid");
			} else if (valid == INCOMPLETE_EXPRESSION) {
				validateButton.setText("invalid");
				validateButton.setEnabled(false);
				validateButton.setOpaque(true);
				validateButton.setBackground(Color.RED);
				validateButton.setToolTipText("Incomplete expression.");
			} else if (valid == INVALID_EXPRESSION) {
				validateButton.setText("invalid");
				validateButton.setEnabled(false);
				validateButton.setOpaque(true);
				validateButton.setBackground(Color.RED);
				validateButton.setToolTipText("Attributes may only be used once per expression");
			}
		}
	}
	
	/**
	 * Validates the Importance Expression
	 * @return 	0 if the expression is valid
	 * 			1 if the expression is incomplete
	 * 			2 if the expression uses an attribute more than once
	 */
	private int validateExpression() {
		Importance importance = map.get(key);
		AttributeList lists[] = new AttributeList[4];
		
		if(importance == null)
			return INCOMPLETE_EXPRESSION;
		
		// retrieve final two lists to verify they contain
		// attributes
		lists[2] = importance.getList(2);
		lists[3] = importance.getList(3);
		
		if(lists[2] == null || lists[2].size() == 0 
				|| lists[3] == null || lists[3].size() == 0)
			return INCOMPLETE_EXPRESSION;
		
		// retrieve final two lists to verify that no
		// attribute is used more than once
		lists[0] = importance.getList(0);
		lists[1] = importance.getList(1);
		
		AttributeMap attributeMap = new AttributeMap();
		
		for(int i=0; i < 4; i++) {
			
			int listSize;
			if(lists[i] == null){
				listSize = 0;
			} else {
				listSize = lists[i].size();
			}
			
			for(int j=0; j < listSize; j++) {
				Attribute curAttribute = lists[i].get(j);
				Attribute found = attributeMap.get(curAttribute.getAttributeKey());
				if(found != null)
					return INVALID_EXPRESSION;
				attributeMap.put(curAttribute.getAttributeKey().getKey(), curAttribute);
			}
		}
		
		return VALID_EXPRESSION;
	}
	
	private void resetValidateButton() {
		validateButton.setText("validate");
		validateButton.setEnabled(true);
		validateButton.setOpaque(true);
		validateButton.setBackground(null);
		validateButton.setToolTipText("validate");
	}
	
	class ImportanceFieldListener implements MouseListener {
		private int index;
		public ImportanceFieldListener(int index) {
			this.index=index;
		}

		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			//enable validate button
			
			Importance importance = map.get(key);
			boolean isNew =false;
			if (importance == null) {
				importance = new Importance(key);
				isNew=true;
			}
			AttributeList list = importance.getList(index);

			if(list == null){
				list = new AttributeList();
			}
			//@SuppressWarnings("unused")
			ImportanceDialog dialog = new ImportanceDialog(parentWindow,map,list,allAttributes);
			importance.setList(index, dialog.getList());
			
			fields[index].setText(list.toString());
			if(isNew){
				map.put(key, importance);
//				System.out.println("new importance added to map with index: "+index+" and key: "+key);
//				Importance justAdded = map.get(key);
//				AttributeList justList = justAdded.getList(index);
//				System.out.println("testing map permenance: ");
//				printList(justList);
			}
			// made change to importance, map has changes
			map.setSaved(false);
			
			resetValidateButton();
			
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
