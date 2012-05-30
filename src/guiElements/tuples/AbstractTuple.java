package guiElements.tuples;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import dataStructures.maps.SuperkeyMap;

@SuppressWarnings("serial")
public abstract class AbstractTuple<A> extends JPanel implements ActionListener{
	protected Integer key;
	protected SuperkeyMap<A> map;
	protected Window parentWindow;
	protected JPanel parentPanel;
	
	public AbstractTuple(Integer key,SuperkeyMap<A> map,Window parentWindow,JPanel parentPanel) {
		super();
		this.map=map;
		this.key = key;
		this.parentWindow=parentWindow;
		this.parentPanel=parentPanel;
	}
	
	public AbstractTuple(SuperkeyMap<A> map,Window parentWindow,JPanel parentPanel) {
		super();
		this.map=map;
		this.key = map.getUniqueKey();
		this.parentWindow=parentWindow;
		this.parentPanel=parentPanel;
	}
	
	public abstract void initializeGUI();
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		parentPanel.remove(this);
		map.remove(key);
		parentWindow.pack();
		
	}
}