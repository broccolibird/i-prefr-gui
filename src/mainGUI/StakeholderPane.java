package mainGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import multiStakeholderGUI.MSPaneTurner;

@SuppressWarnings("serial")
public class StakeholderPane extends UpdatePane {

	private JFrame parentFrame;
	private MSPaneTurner paneTurner;
	
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
		panel.setLayout(new BorderLayout());

		paneTurner = new MSPaneTurner(parentFrame, null);
		
		panel.add(paneTurner, BorderLayout.CENTER);
		return panel;
	}

}
