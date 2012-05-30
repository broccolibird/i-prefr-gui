package multiStakeholderGUI;

import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MultipleStakeholders extends JFrame {
	
	private static MSPaneTurner paneTurner;

	public MultipleStakeholders(){
		setPreferredSize(new Dimension(900, 800));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		paneTurner = new MSPaneTurner(this);
		getContentPane().add(paneTurner);
		pack();
		
		setVisible(true);
	}
}
