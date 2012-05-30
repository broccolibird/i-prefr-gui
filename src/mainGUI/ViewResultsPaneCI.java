package mainGUI;

import guiElements.DominanceSetDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import dataStructures.AlternativeList;
import dataStructures.Attribute;
import dataStructures.AttributeList;
import dataStructures.CIDocument;

@SuppressWarnings("serial")
public class ViewResultsPaneCI extends UpdatePane implements ActionListener {
	public static final int CONSISTENCY = 0;
	public static final int DOMINANCE = 1;
	public static final int TOP = 2;
	public static final int NEXT = 3;

	private CIDocument document;

	private JButton consistencyButton;
	private JButton dominanceButton;
	private JButton topNextButton;

	private JTextField consistencyField;
	private JTextField leftDominanceSet;
	private JTextField rightDominanceSet;
	private JTextField dominanceField;
	private JTextArea resultsField;

	private AttributeList leftList;
	private AttributeList rightList;
	private AlternativeList alreadyChosen;

	private JFrame parentFrame;

	public ViewResultsPaneCI(CIDocument document, JFrame parentFrame) {
		this.document = document;
		this.parentFrame = parentFrame;
		this.alreadyChosen = new AlternativeList();
		this.leftList = new AttributeList();
		this.rightList = new AttributeList();
		this.add(createGUI());
		setVisible(true);
	}

	private JPanel createGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel consistencyPanel = new JPanel();
		consistencyPanel.setLayout(new BoxLayout(consistencyPanel, BoxLayout.X_AXIS));
		consistencyButton = new JButton("Consistency");
		consistencyButton.addActionListener(this);
		consistencyField=new JTextField("result");
		consistencyField.setEditable(false);
		consistencyPanel.add(consistencyButton);
		consistencyPanel.add(consistencyField);

		JPanel dominancePanel = new JPanel();
		dominancePanel.setLayout(new BoxLayout(dominancePanel, BoxLayout.X_AXIS));
		dominanceButton = new JButton("Dominance");
		dominanceButton.addActionListener(this);
		leftDominanceSet= new JTextField("{}");
		leftDominanceSet.setEditable(false);
		leftDominanceSet.addMouseListener(new DominanceFieldListener(leftDominanceSet,leftList));
		JLabel greaterThan = new JLabel(">");
		rightDominanceSet=new JTextField("{}");
		rightDominanceSet.setEditable(false);
		rightDominanceSet.addMouseListener(new DominanceFieldListener(rightDominanceSet,rightList));
		dominanceField=new JTextField("result");
		dominanceField.setEditable(false);
		
		dominancePanel.add(dominanceButton);
		dominancePanel.add(leftDominanceSet);
		dominancePanel.add(greaterThan);
		dominancePanel.add(rightDominanceSet);
		dominancePanel.add(dominanceField);
		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		topNextButton = new JButton("Top");
		topNextButton.addActionListener(this);
		resultsField = new JTextArea(3,38);
		resultsField.setEditable(false);
		resultsPanel.add(topNextButton);
		resultsPanel.add(resultsField);

		update();
		panel.add(consistencyPanel);
		panel.add(dominancePanel);
		panel.add(resultsPanel);
		return panel;
	}

	@Override
	public void update() {
		String alreadyChosenAlternatives = "";
		for(int i = 0 ; i<alreadyChosen.size();i++){
			alreadyChosenAlternatives += alreadyChosen.get(i).toString()+"\n";
		}
		if(alreadyChosenAlternatives.equals("")){
			alreadyChosenAlternatives = "list of results fetched one by one";
		}
		resultsField.setText(alreadyChosenAlternatives);
		// TODO - maybe remove all options, screen 'alreadyChosen' for deleted
		// possibilities and then re-create the current output from some saved
		// state info
		parentFrame.pack();
	}

	
	
	public AlternativeList getAlreadyChosen() {
		return alreadyChosen;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == consistencyButton) {
			sendQuery(CONSISTENCY);
		} else if (source == dominanceButton) {
			sendQuery(DOMINANCE);
		} else if (source == topNextButton) {
			if(topNextButton.getText().contains("Top")){
				topNextButton.setText("Next");
				sendQuery(TOP);
			}else{
				sendQuery(NEXT);
			}
		}		
	}

	private void sendQuery(int type) {
		switch (type) {
		case CONSISTENCY:
			System.out.println("send cosistency query");
			//TODO - update result field
			break;
		case DOMINANCE:
			System.out.println("send dominance query");
			//TODO - update result field
			break;
		case TOP:
			System.out.println("send top query");
			//TODO - add result to alreadyChosen
			break;
		case NEXT:
			int n = alreadyChosen.size();
			System.out.println("send next query with n =" + n
					+ " and alreadyChosen = " + alreadyChosen.toString());
			//TODO - add result to alreadyChosen
			break;
		default:
			System.err.println("In ViewResultsPaneCI - sendQuery triggered by invalid type integer");
			break;
		}
		parentFrame.pack();
	}
	
	class DominanceFieldListener implements MouseListener {
		private JTextComponent field;
		private AttributeList list;
		public DominanceFieldListener(JTextComponent field,AttributeList list) {
			this.field=field;
			this.list=list;
		}

		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Attribute[] allAttributes = document.getAttributeMap().values().toArray(new Attribute[0]);
			DominanceSetDialog dialog = new DominanceSetDialog(parentFrame,list,allAttributes);
			list = dialog.getList();
			field.setText(list.toString());
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
