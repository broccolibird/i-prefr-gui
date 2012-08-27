package guiElements;

import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import mainGUI.UpdatePane;

public abstract class ScrollPane extends UpdatePane {
	
	protected JPanel headerPanel;
	
	protected JPanel containerPanel;
	protected SpringLayout layout;
	protected JScrollPane scrollPane;
	protected LinkedList<JPanel> tuples;
	
	public ScrollPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		headerPanel = new JPanel();
		panel.add(headerPanel);
		
		layout = new SpringLayout();
		containerPanel = new JPanel(layout);
		scrollPane = new JScrollPane(containerPanel);
		scrollPane.setBorder(null);
		panel.add(scrollPane);
		
		tuples = new LinkedList<JPanel>();
		
		this.add(panel);
	}
}
