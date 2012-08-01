package guiElements.tuples;

import guiElements.ImportanceDialog;

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
import dataStructures.maps.ImportanceMap;

@SuppressWarnings("serial")
public class ImportanceTuple extends AbstractTuple<Importance> implements
		ActionListener {
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
		
		xButton = new JButton("x");
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
			//validate Importance element, disable button
			System.out.println("validated");
		}
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
