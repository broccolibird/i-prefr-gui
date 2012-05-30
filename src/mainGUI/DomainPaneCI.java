package mainGUI;

import guiElements.tuples.DomainTupleCI;

import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;

@SuppressWarnings("serial")
public class DomainPaneCI extends DomainPane{

	public DomainPaneCI(AttributeMap oldMap, JFrame parentFrame) {
		super(oldMap, parentFrame);
	}
	
	@Override
	public void update() {
		// for every map entry, add a tuple to the table
		domainPanel.removeAll();
		JPanel label = new JPanel();
		label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
		JTextField name = new JTextField("Attribute Name");
		JTextField domainEnum = new JTextField("Domain Enumeration");
		name.setEditable(false);
		domainEnum.setEditable(false);
		label.add(name);
		label.add(domainEnum);
		domainPanel.add(label);
		Collection<Entry<Integer, Attribute>> set = map.entrySet();
		for (Entry<Integer, Attribute> p : set){
			//System.out.println("Creating DomainTuple with key: "+p.getKey()+ " maybe need key: "+p.getValue().getAttributeKey());
			domainPanel.add(new DomainTupleCI(p.getKey(), map, parentFrame,
					domainPanel));
		}
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
		parentFrame.pack();
	}
	

}
