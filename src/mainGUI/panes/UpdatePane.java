package mainGUI.panes;

import javax.swing.JPanel;

/**
 * The UpdatePane class is an extension of JPanel
 * and contains a method to update the panel when
 * data outside of the pane has been changed.
 */
@SuppressWarnings("serial")
public abstract class UpdatePane extends JPanel {

	/**
	 * Updates the panel to display the most recent
	 * data.
	 */
	public abstract void update();
}
