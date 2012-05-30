package mainGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import multiStakeholderGUI.MultipleStakeholders;

@SuppressWarnings("serial")
public class StakeholderPane extends UpdatePane implements ActionListener {

	private JFrame parentFrame;
	private JButton addButton;
	
	StakeholderPane(JFrame parentFrame){
		this.parentFrame = parentFrame;
		this.add(createGUI());
		setVisible(true);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	private JPanel createGUI(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		addButton = new JButton("Add Stakeholders");
		addButton.addActionListener(this);
		panel.add(addButton);
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(addButton == e.getSource()){
			MultipleStakeholders ms = new MultipleStakeholders();
		}
		
		
	}
}
