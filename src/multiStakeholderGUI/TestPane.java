package multiStakeholderGUI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mainGUI.UpdatePane;

@SuppressWarnings("serial")
public class TestPane extends UpdatePane{

	private JFrame parentFrame;
	
	TestPane(JFrame parentFrame, String text){
		this.parentFrame = parentFrame;
		this.add(createGUI(text));
		setVisible(true);
	}
	
	private JPanel createGUI(String text){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JTextArea textArea = new JTextArea(text);
		panel.add(textArea);
		
		return panel;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
