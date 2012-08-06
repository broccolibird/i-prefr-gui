package mainGUI;

import guiElements.tuples.DomainTuple;

import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;

/**
 * The DomainPane is an UpdatePane with fields for entry of
 * Attribute Domains.
 */
@SuppressWarnings("serial")
public class DomainPane extends UpdatePane {

	protected AttributeMap map;
	protected JPanel domainPanel;
	protected JFrame parentFrame;

	/**
	 * Create a new instance of DomainPane
	 * @param oldMap
	 * @param parentFrame
	 */
	public DomainPane(AttributeMap oldMap, JFrame parentFrame) {
		this.map = oldMap;
		this.parentFrame = parentFrame;
		this.add(initializeGUI());
		setVisible(true);
	}

	/**
	 * Setup GUI for DomainPane
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		domainPanel = new JPanel();
		domainPanel.setLayout(new BoxLayout(domainPanel, BoxLayout.Y_AXIS));
		update();
		panel.add(domainPanel);
		return panel;
	}
 
	public void update() {
//		ArrayList<Attribute> allAttributes = new ArrayList<Attribute>(
//				map.values());
//		for(Attribute a : allAttributes){
//			System.out.println("in ValueTuple: Attribute "+ a.getName()+"'s key is: "+a.getAttributeKey());
//			DomainValueList list = a.getObject().getDomainValueList();
//			for (DomainValue dv : list){
//				System.out.println("dv: "+dv.getValue()+" dv's attributeKey: "+dv.getAttributeKey());
//			}
//			System.out.println("*****");
//		}
		// for every map entry, add a tuple to the table
		domainPanel.removeAll();
		JPanel label = new JPanel();
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		//label.setPreferredSize(new Dimension(150,20));
		JTextField name = new JTextField("Attribute Name");
		JTextField domainEnum = new JTextField("Domain Enumeration");
		name.setEditable(false);
		domainEnum.setEditable(false);
		label.add(name);
		label.add(domainEnum);
		domainPanel.add(label);
		Collection<Entry<Integer, Attribute>> set = map.entrySet();
		for (Entry<Integer, Attribute> p : set){
			domainPanel.add(new DomainTuple(p.getKey(), map, parentFrame,
					domainPanel));
		}
		parentFrame.pack();
	}
	
	@Override
	public void leave() {/*do nothing*/}
}
