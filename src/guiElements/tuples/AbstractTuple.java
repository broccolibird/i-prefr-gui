package guiElements.tuples;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import dataStructures.maps.SuperkeyMap;

/**
 * The AbstractTuple class associates an input field with the object(s)
 * it represents, such that any change to the input field has a direct
 * affect on the object.
 *
 * @param <A>
 */
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
	/**
	 * Removes the object from its parent panel
	 * and map.
	 */
	public void actionPerformed(ActionEvent e) {
		parentPanel.remove(this);
		map.remove(key);
		parentWindow.pack();
		
	}
}