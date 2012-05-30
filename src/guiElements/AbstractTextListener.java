package guiElements;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public abstract class AbstractTextListener implements DocumentListener {
	protected JTextComponent field;

	public AbstractTextListener(JTextComponent field) {
		this.field = field;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// not triggered by plain text
	}

	@Override
	public abstract void insertUpdate(DocumentEvent e);

	@Override
	public abstract void removeUpdate(DocumentEvent arg0);

}
