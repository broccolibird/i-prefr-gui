package mainGUI;

import guiElements.tuples.DomainTupleCI;

import javax.swing.JFrame;
import dataStructures.maps.AttributeMap;

/**
 * The DomainPaneCI is an extension of DomainPane 
 * that displays the Attribute Domains of a 
 * CI Preference Network.
 */
@SuppressWarnings("serial")
public class DomainPaneCI extends DomainPane{

	public DomainPaneCI(AttributeMap oldMap, JFrame parentFrame) {
		super(oldMap, parentFrame);
	}
	
	@Override
	protected void addTuple(Integer key) {
		DomainTupleCI tuple = new DomainTupleCI(key, map, parentFrame, domainPanel);
		domainPanel.add(tuple.getKey());
		domainPanel.add(tuple.getValue());
	}
	

}
