package mainGUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ReasonerWindowListener implements WindowListener{

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		if ( PreferenceReasoner.existChanges() ) {
			//offer to save
			PreferenceReasoner.showSaveChangesDialog();
		}
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

}
