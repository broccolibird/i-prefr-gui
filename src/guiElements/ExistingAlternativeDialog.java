package guiElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.Alternative;
import dataStructures.maps.AlternativeMap;

@SuppressWarnings("serial")
public class ExistingAlternativeDialog extends JDialog implements ActionListener{
	//private JFrame parentFrame;
	private AlternativeMap map;
	private JComboBox box;
	private Alternative singleAlternative;
	private JButton okButton;
	
	public ExistingAlternativeDialog(JFrame parentFrame, AlternativeMap map,Alternative singleAlternative){
		super(parentFrame,true);
		//this.parentFrame=parentFrame;
		this.map=map;
		this.singleAlternative = singleAlternative;
		getContentPane().add(createGUI());
		pack();
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}
	
	private JPanel createGUI(){
		JPanel panel = new JPanel();
		
		Alternative[] allAlternatives = map.values().toArray(new Alternative[0]);
		int selectedIndex = 0;
		if(singleAlternative != null) {
			for(int i=0;i<allAlternatives.length;i++){
				if (allAlternatives[i].equals(singleAlternative)){
					selectedIndex = i;
					break;
				}
			}
		}
		// set up the boxes
		box = new JComboBox(allAlternatives);
		box.addActionListener(this);
		box.setSelectedIndex(selectedIndex);
		panel.add(box);

		okButton = new JButton("ok");
		okButton.addActionListener(this);
		panel.add(okButton);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == box) {
			singleAlternative = (Alternative) box.getSelectedItem();
		}else if(e.getSource()==okButton){
			dispose();
		}
	}
	
	public Alternative getAlternative() {
		return singleAlternative;
	}
	
}
