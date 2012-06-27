package mainGUI;

import guiElements.CustomAlternativeDialog;
import guiElements.ExistingAlternativeDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import mainGUI.ViewResultsPaneTCP.AlternativeListener;

import dataStructures.AbstractDocument;
import dataStructures.Alternative;
import dataStructures.AlternativeList;
import dataStructures.Member;
import dataStructures.Role;
import dataStructures.maps.MemberMap;
import dataStructures.maps.RoleMap;

public class ViewResultsPane extends UpdatePane implements ActionListener {
	public static final int CONSISTENCY = 0;
	public static final int DOMINANCE = 1;
	public static final int TOP = 2;
	public static final int NEXT = 3;
	
	protected AbstractDocument document;
	
	protected JButton consistencyButton;
	protected JButton dominanceButton;
	protected JButton topNextButton;
	
	protected JTextField consistencyField;
	protected JTextField leftDominanceSet;
	protected JTextField rightDominanceSet;
	protected JTextField dominanceField;
	protected JTextArea resultsField;

	protected AlternativeList alreadyChosen;
	private Alternative leftAlternative;
	private Alternative rightAlternative;
	
	private JPanel dominancePanel;
	
	private JComboBox stakeholderBox;
	private Member curMember;
	private boolean allMembers;

	protected JFrame parentFrame;
	
	
	public ViewResultsPane(AbstractDocument document, JFrame parentFrame) {
		this.document = document;
		this.parentFrame = parentFrame;
		this.alreadyChosen = new AlternativeList();
		this.add(createGUI());
		setVisible(true);
	}
	
	private JPanel createGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel consistencyPanel = createConsistencyPanel();
	
		JPanel resultsPanel = createResultsPanel();
		
		dominancePanel = createDominancePanel();

		update();
		panel.add(consistencyPanel);
		panel.add(dominancePanel);
		panel.add(resultsPanel);
		return panel;
	}
	
	@Override
	public void update() {
		dominancePanel.removeAll();
		dominanceButton = new JButton("Dominance");
		dominanceButton.addActionListener(this);
		leftDominanceSet= new JTextField("{}");
		leftDominanceSet.setEditable(false);
		leftDominanceSet.addMouseListener(new AlternativeListener(leftDominanceSet,leftAlternative));
		JLabel greaterThan = new JLabel(">");
		rightDominanceSet=new JTextField("{}");
		rightDominanceSet.setEditable(false);
		rightDominanceSet.addMouseListener(new AlternativeListener(rightDominanceSet,rightAlternative));
		dominanceField=new JTextField("result");
		dominanceField.setEditable(false);
		
		dominancePanel.add(dominanceButton);
		dominancePanel.add(leftDominanceSet);
		dominancePanel.add(greaterThan);
		dominancePanel.add(rightDominanceSet);
		dominancePanel.add(dominanceField);
		//TODO - update the resultsField
		parentFrame.pack();
	}
	
	protected JPanel createConsistencyPanel() {
		JPanel consistencyPanel = new JPanel();
		consistencyPanel.setLayout(new BoxLayout(consistencyPanel, BoxLayout.X_AXIS));
		consistencyButton = new JButton("Consistency");
		consistencyButton.addActionListener(this);
		consistencyField=new JTextField("result");
		consistencyField.setEditable(false);
		consistencyPanel.add(consistencyButton);
		consistencyPanel.add(consistencyField);
		return consistencyPanel;
	}
	
	protected JPanel createDominancePanel() {
		JPanel dominancePanel = new JPanel();
		dominancePanel.setLayout(new BoxLayout(dominancePanel, BoxLayout.X_AXIS));
		return dominancePanel;
	}
	
	protected JPanel createResultsPanel() {
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		topNextButton = new JButton("Top");
		topNextButton.addActionListener(this);
		resultsField = new JTextArea(3,38);
		resultsField.setEditable(false);
		resultsField.setText("list of results fetched one by one");
		resultsPanel.add(topNextButton);
		resultsPanel.add(resultsField);
		return resultsPanel;
	}
	
	/**
	 * Setup the combobox containing role members
	 * -- only used in multistakeholder
	 */
	private void setupStakeholderBox() {
		RoleMap rm = document.getRoleMap();
		Role[] roles = (Role[]) rm.values().toArray(new Role[0]);
		Vector<Member> allMembers = new Vector<Member>();
		for(Role role: roles) {
			MemberMap members = role.getObject();
			if (members != null) {
				Collection<Member> roleMembers = members.values();
				allMembers.addAll(roleMembers);

			}
		}
		
		stakeholderBox = new JComboBox<Member>(allMembers);
		stakeholderBox.insertItemAt("All Members", 0);
		stakeholderBox.addActionListener(this);
		stakeholderBox.invalidate();
		stakeholderBox.setSelectedIndex(0); // select All Members
		
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
	
	protected void sendQuery(int type) {
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
	
	class AlternativeListener implements MouseListener {
		private JTextComponent field;
		private Alternative singleAlternative;
		public AlternativeListener(JTextComponent field,Alternative singleAlternative) {
			this.field=field;
			this.singleAlternative = singleAlternative;
		}

		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(document.getAlternativeMap().useEntireAlternativeSpace()){
				CustomAlternativeDialog dialog = new CustomAlternativeDialog(parentFrame,document.getAttributeMap(),singleAlternative);
				singleAlternative = dialog.getAlternative();	
			}else{
				ExistingAlternativeDialog dialog = new ExistingAlternativeDialog(parentFrame,document.getAlternativeMap(),singleAlternative);
				singleAlternative = dialog.getAlternative();	
			}

			//System.out.println("alternative to text: "+singleAlternative.toString());
			field.setText(singleAlternative.toExpandedString(document.getAttributeMap()));
			parentFrame.pack();
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
