package guiElements;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import dataStructures.maps.SuperkeyMap;

@SuppressWarnings("serial")
public abstract class AbstractTupleDialog<E> extends JDialog implements ActionListener{
	protected Window parentWindow;
	protected JPanel tablePanel;
	protected JButton plusButton;
	protected JButton okButton;
	protected SuperkeyMap<E> map;

	public SuperkeyMap<E> getMap() {
		return map;
	}
	
	public AbstractTupleDialog(Window window, SuperkeyMap<E> map) {
		super(window,Dialog.ModalityType.DOCUMENT_MODAL);
		this.parentWindow = window;
		this.map = map;
		getContentPane().add(createGUI());
	}

	private JPanel createGUI() {
		// 'panel' holds the tablePanel above the plusButton and okButton
		JPanel panel = new JPanel();
		
		// create the plus and ok buttons
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		tablePanel = new JPanel();
		panel.add(tablePanel);
		panel.add(plusButton);
		panel.add(okButton);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}
	protected void removeThis(){
		
	}

	
	protected abstract void updateTablePanel();

	@Override
	public abstract void actionPerformed(ActionEvent e);
}
