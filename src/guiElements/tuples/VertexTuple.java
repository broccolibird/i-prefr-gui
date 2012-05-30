package guiElements.tuples;

import guiElements.ConditionDialog;
import guiElements.PreferenceDialog;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Attribute;
import dataStructures.Pair;
import dataStructures.VertexStatement;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ConditionMap;
import dataStructures.maps.DomainPreferenceMap;
import dataStructures.maps.VertexStatementMap;

@SuppressWarnings("serial")
public class VertexTuple extends AbstractTuple<VertexStatement> {

	private JTextField left;
	private JTextField right;
	private JButton xButton;
	private AttributeMap attributeMap;
	private Attribute attribute;

	public VertexTuple(Integer key, VertexStatementMap map, Window parentWindow,
			JPanel parentPanel, AttributeMap attributeMap, Attribute attribute) {
		super(key, map, parentWindow, parentPanel);
		this.attributeMap = attributeMap;
		this.attribute = attribute;
		initializeGUI();
	}

	public VertexTuple(VertexStatementMap map, Window parentWindow,
			JPanel parentPanel, AttributeMap attributeMap, Attribute attribute) {
		super(map, parentWindow, parentPanel);
		this.attributeMap = attributeMap;
		this.attribute = attribute;
		initializeGUI();
	}

	@Override
	public void initializeGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		left = new JTextField(17);
		left.addMouseListener(new ConditionFieldListener());
		left.setEditable(false);
		this.add(left);

		right = new JTextField(17);
		right.addMouseListener(new PreferenceFieldListener());
		right.setEditable(false);
		this.add(right);

		xButton = new JButton("x");
		xButton.addActionListener(this);
		this.add(xButton);

		setTupleText(map.get(key));
	}

	public void setTupleText(Pair<ConditionMap, DomainPreferenceMap> pair) {
		String lString = "click to enter ConditionMap";
		String rString = "click to enter DomainPreferenceMap";
		if (pair != null) {
			lString = pair.getLeft() == null ? lString : pair.getLeft()
					.toString();
			rString = pair.getRight() == null ? rString : pair.getRight()
					.toString();
		}
		this.left.setText(lString);
		this.right.setText(rString);
	}

	class ConditionFieldListener implements MouseListener {

		public ConditionFieldListener() {

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			VertexStatement vertexStatement = map.get(key);
			if (vertexStatement == null) {
				vertexStatement = new VertexStatement(null, null);
			}
			ConditionMap conditionMap = vertexStatement.getLeft();
			boolean newItem = false;
			if (conditionMap == null) {
				conditionMap = new ConditionMap();
				newItem = true;
				System.out.println("newItem - conditionMap");
			}
			@SuppressWarnings("unused")
			ConditionDialog conditionDialog = new ConditionDialog(parentWindow,
					conditionMap, attributeMap);

			// if the item is a new condition, then the statement map has no
			// reference to it, so put it into the statement map (as long as it
			// is not empty) - if it is empty then the user probably clicked on
			// it by mistake and closed it
			System.out.println("conditionMap size: " + conditionMap.size());
			if (newItem && !conditionMap.isEmpty()) {
				System.out.println("putting conditionMap into statement: "
						+ conditionMap.toString());
				map.put(key, new VertexStatement(conditionMap, vertexStatement.getRight()));
			}
			setTupleText(map.get(key));
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	class PreferenceFieldListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			VertexStatement vertexStatement = map.get(key);
			if (vertexStatement == null) {
				vertexStatement = new VertexStatement(null, null);
			}
			DomainPreferenceMap domainPreferenceMap = vertexStatement.getRight();
			boolean newItem = false;
			if (domainPreferenceMap == null) {
				domainPreferenceMap = new DomainPreferenceMap();
				newItem = true;
				System.out.println("newItem - preferenceMap");
			}
			if (attribute != null) {
				System.out.println("attribute in VertexTuple: " + attribute);
			} else {
				System.out.println("attribute in VertexTuple is null");
			}
			@SuppressWarnings("unused")
			PreferenceDialog preferenceDialog = new PreferenceDialog(
					parentWindow, domainPreferenceMap, attribute);

			// if the item is a new condition, then the statement map has no
			// reference to it, so put it into the statement map (as long as it
			// is not empty) - if it is empty then the user probably clicked on
			// it by mistake and closed it
			if (newItem && !domainPreferenceMap.isEmpty()) {
				System.out.println("putting preferenceMap into statement: "
						+ domainPreferenceMap.toString());
				map.put(key, new VertexStatement(vertexStatement.getLeft(), domainPreferenceMap));
			}
			setTupleText(map.get(key));
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

	}
}