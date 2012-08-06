package guiElements.tuples;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.Domain;
import dataStructures.maps.AttributeMap;

@SuppressWarnings("serial")
public class DomainTupleCI extends AbstractTuple<Attribute>{
	protected JTextArea domainField;
	protected JTextField attributeName;

	public DomainTupleCI(Integer key, AttributeMap map, JFrame parent,
			JPanel parentPanel) {
		super(key, map, parent, parentPanel);
		initializeGUI();
	}

	public DomainTupleCI(AttributeMap map, JFrame parent, JPanel parentPanel) {
		super(map, parent, parentPanel);
		initializeGUI();
	}
	
	@Override
	public void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		domainField = new JTextArea(3, 10);
		Attribute a = map.get(key);
		if (a == null) {
			System.err.println("attribute should not be null in DomainTupleCI");
		}
		String atName = a.getName();
		attributeName = new JTextField(atName);
		attributeName.setEditable(false);
//		attributeName.setPreferredSize(new Dimension(75, 20));
		this.add(attributeName);
		Domain d = a.getObject();
		
		
		System.out.println("in DomainTupleCI, attributeKey: "+a.getAttributeKey()+" key: "+a.getKey());
		String standardCIDomainValues = "0,1";
		if(d==null){
			d = new Domain(standardCIDomainValues, a.getAttributeKey());
			a.setObject(d);
		}
			
		domainField.setText(standardCIDomainValues);
//		domainField.setPreferredSize(new Dimension(100, 20));
		this.add(domainField);
	}
	
	public JTextField getKey() {
		return attributeName;
	}
	
	public JTextArea getValue() {
		return domainField;
	}
	
//	@Override
//	public void initializeGUI() {
//		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//
//		domainField = new JTextArea(3, 25);
//		Attribute a = map.get(key);
//		if (a == null) {
//			System.err.println("attribute should not be null in DomainTuple");
//		}
//		String atName = a.getName();
//		JTextField attributeName = new JTextField(atName);
//		attributeName.setEditable(false);
//		attributeName.setPreferredSize(new Dimension(75, 20));
//		this.add(attributeName);
//		Domain d = a.getObject();
//		
//		//System.out.println("in DomainTuple, attributeKey: "+a.getAttributeKey());
//		String enumeration = "";
//		if (d != null)
//			enumeration = d.toString();
//		else
//			a.setObject(new Domain(enumeration, a.getAttributeKey()));
//		
//		domainField.setText(enumeration);
//		domainField.getDocument().addDocumentListener(
//				new DomainTextListener(domainField));
//		domainField.setPreferredSize(new Dimension(100, 20));
//		this.add(domainField);
//	}

}
