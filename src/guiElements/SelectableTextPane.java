package guiElements;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class SelectableTextPane extends JPanel {
	private JTextPane textPane;
	private boolean isSelected;

	public SelectableTextPane(String message) {
		this.textPane = new JTextPane();
		//center all text horizontally
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		textPane.setContentType("text/plain");
		textPane.setText(message);
		textPane.setEditable(false);
		this.add(textPane,BorderLayout.CENTER);
		isSelected = false;
		this.setBackground(Color.GRAY);
	}

	public void toggleColor() {
		if (isSelected) {
			this.setBackground(Color.GRAY);
		} else
			this.setBackground(Color.BLUE);
		isSelected = !isSelected;
	}

}
