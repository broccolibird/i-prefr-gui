package mainGUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class ReasonerWindowListener implements WindowListener{

	private JFrame frame;
	private PreferenceReasoner reasoner;
	
	public ReasonerWindowListener(JFrame frame, PreferenceReasoner reasoner){
		this.frame = frame;
		this.reasoner = reasoner;
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		if ( reasoner.existChanges() ) {
			//offer to save
			if(reasoner.showSaveChangesDialog())
				frame.dispose();
		} else {
			frame.dispose();
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
