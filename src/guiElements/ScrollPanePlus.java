package guiElements;

import guiElements.tuples.AbstractTuple;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import org.apache.commons.collections15.Factory;

public abstract class ScrollPanePlus extends ScrollPane implements ActionListener{

	protected JButton plusButton;
	protected JFrame parentFrame;
	protected TupleFactory tupleFactory;
	
	public ScrollPanePlus(JFrame parentFrame) {
		super();
		this.parentFrame = parentFrame;
		
		plusButton = new JButton("+");
		plusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		plusButton.addActionListener(this);
		InputMap plusInputMap = plusButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		plusInputMap.put(KeyStroke.getKeyStroke("ENTER"), "selectPlus");
		plusButton.getActionMap().put("selectPlus", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plusButton.doClick();				
			}
		});
		
		panel.add(plusButton);
	}
	
	public void setTupleFactory(TupleFactory tupleFactory) {
		this.tupleFactory = tupleFactory;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (plusButton == e.getSource()) {
			addTuple(null);
			pack();
		}
	}
	
	public void pack() {
		parentFrame.pack();
	}
	
	protected AbstractTuple addTuple(Integer key) {
		AbstractTuple tuple;
		if(key != null) {
			tuple = tupleFactory.create(key);
			
		} else {
			tuple = tupleFactory.create();
		}
		
		tuples.add(tuple);
		containerPanel.add(tuple);
		
		int lastTupleIndex = tuples.indexOf(tuple) - 1;
		if(lastTupleIndex >= 0) {
			layout.putConstraint(SpringLayout.NORTH, tuple, 0,
					SpringLayout.SOUTH, tuples.get(lastTupleIndex));
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, tuple, 0,
					SpringLayout.HORIZONTAL_CENTER, tuples.get(lastTupleIndex));
		} else {
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, containerPanel, 0,
					SpringLayout.HORIZONTAL_CENTER, tuple);
		}
		
		resize(tuple);
		
		return tuple;
	}
	
	public AbstractTuple removeTuple(AbstractTuple tuple) {
		int index = tuples.indexOf(tuple);
		
		if (index == 0 && tuples.size() > 1) {
			layout.putConstraint(SpringLayout.NORTH, tuples.get(index+1), 0,
					SpringLayout.NORTH, containerPanel);
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, tuples.get(index+1), 0,
					SpringLayout.HORIZONTAL_CENTER, containerPanel);
		} else if(index == tuples.size()-1){
			layout.removeLayoutComponent(tuple);
		} else if (index > 0 && index != tuples.size()-1) {
			layout.putConstraint(SpringLayout.NORTH, tuples.get(index+1), 0,
					SpringLayout.SOUTH, tuples.get(index-1));
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, tuples.get(index+1), 0,
					SpringLayout.HORIZONTAL_CENTER, tuples.get(index-1));
		}
		
		tuples.remove(tuple);
		
		resize(tuple);
		
		repaint();
		
		return tuple;
	}
	
	protected abstract void resize(AbstractTuple tuple);
	
	public abstract class TupleFactory implements Factory<AbstractTuple> {

		public abstract AbstractTuple create(Integer key);
		
	}
}
