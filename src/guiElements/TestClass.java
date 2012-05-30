package guiElements;

import javax.swing.JApplet;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TestClass extends JApplet {
	private static JFrame frame;

	public static void main(String[] args) {
		// new JDialog: name your project
		frame = new JFrame();
		//frame.setPreferredSize(new Dimension(900, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String[] stuff = {"hello","goodbye","meow","bark","uuhngh"};
		LabeledComboBox box = new LabeledComboBox("label",stuff);
		frame.add(box);
		frame.setVisible(true);
		frame.pack();
	}
}
