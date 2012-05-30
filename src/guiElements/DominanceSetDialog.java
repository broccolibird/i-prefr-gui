package guiElements;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import dataStructures.Attribute;
import dataStructures.AttributeList;

@SuppressWarnings("serial")
public class DominanceSetDialog extends JDialog implements ItemListener,
ActionListener {
	protected Window parentWindow;
	protected JPanel tablePanel;

	private JCheckBox[] checkBoxes;
	private JButton okButton;

	protected AttributeList list;
	protected Attribute[] allAttributes;
	
	public DominanceSetDialog(Window parentWindow,AttributeList list,Attribute[] allAttributes){
		super(parentWindow,Dialog.ModalityType.DOCUMENT_MODAL);
		this.parentWindow=parentWindow;
		this.list = list;
		this.allAttributes = allAttributes;
		getContentPane().add(createGUI());
		pack();
		setLocationRelativeTo(parentWindow);
		setVisible(true);
	}
	
	private JPanel createGUI(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		checkBoxes = new JCheckBox[allAttributes.length];
		for (int i = 0; i < allAttributes.length; i++) {
			checkBoxes[i] = new JCheckBox(allAttributes[i].getName());
			checkBoxes[i].addItemListener(this);
			//System.out.println("attribute "+i+": "+allAttributes[i]+" ...list contains that one? "+list.contains(allAttributes[i]));
			if (list.contains(allAttributes[i])) {
				checkBoxes[i].setSelected(true);
			}
			tablePanel.add(checkBoxes[i]);
		}
		panel.add(tablePanel);
		panel.add(okButton);
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			dispose();
		}	
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object o = e.getItemSelectable();
		for (int i = 0; i < checkBoxes.length; i++) {
			if (o == checkBoxes[i]) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					list.remove(allAttributes[i]);
				} else {
					if(!list.contains(allAttributes[i])){
						list.add(allAttributes[i]);
					}
				}
			}
		}
	}

	public AttributeList getList() {
		return list;
	}

}
