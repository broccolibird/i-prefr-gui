package mainGUI.panes;

import guiElements.CustomAlternativeDialog;
import guiElements.ExistingAlternativeDialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import dataStructures.AbstractDocument;
import dataStructures.Alternative;
import dataStructures.AlternativeList;
import dataStructures.Member;
import dataStructures.Role;
import dataStructures.maps.MemberMap;
import dataStructures.maps.RoleMap;

/**
 * The ViewResultsPane is an UpdatePane which compiles all previous
 * input, queries the PreferenceReasoner back-end, and displays
 * the query results.
 */
@SuppressWarnings("serial")
public class ViewResultsPane extends UpdatePane implements ActionListener {
	public static final int CONSISTENCY = 0;
	public static final int DOMINANCE = 1;
	public static final int TOP = 2;
	public static final int NEXT = 3;
	
	private AbstractDocument document;
	
	private JButton consistencyButton;
	private JButton dominanceButton;
	private JButton topNextButton;
	
	private JTextField consistencyField;
	private JTextField leftDominanceSet;
	private JTextField rightDominanceSet;
	private JTextField dominanceField;
	private JTextArea resultsField;
	private JTextArea justificationField;

	private AlternativeList alreadyChosen;
	private Alternative leftAlternative;
	private Alternative rightAlternative;
	
	private JPanel dominancePanel;
	
	JPanel stakeholderPanel;
	private JComboBox stakeholderBox;
	private Member curMember;
	private boolean allMembers;

	private JFrame parentFrame;
	
	/**
	 * Create new ViewResultsPane instance
	 * @param document
	 * @param parentFrame
	 */
	public ViewResultsPane(AbstractDocument document, JFrame parentFrame) {
		this.document = document;
		this.parentFrame = parentFrame;
		this.alreadyChosen = new AlternativeList();
		this.add(initializeGUI());
		setVisible(true);
	}
	
	/**
	 * Setup GUI
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		stakeholderPanel = new JPanel();
		
		JPanel consistencyPanel = createConsistencyPanel();
	
		JPanel resultsPanel = createResultsPanel();
		
		dominancePanel = createDominancePanel();
		
		JPanel justificationPanel = createJustificationPanel();

		update();
		panel.add(stakeholderPanel);
		panel.add(consistencyPanel);
		panel.add(dominancePanel);
		panel.add(resultsPanel);
		panel.add(justificationPanel);
		return panel;
	}
	
	@Override
	public void update() {
		
		RoleMap rm = document.getRoleMap();
		
		if (rm.isMultipleStakeholder()) {
			stakeholderPanel.removeAll();
			setupStakeholderBox();
			stakeholderPanel.add(stakeholderBox);
		} else {
			// set curMember to default member
			curMember = rm.get(0).getObject().get(0); 
			allMembers = false;
		}
		
		resetResultFields();
		
		parentFrame.pack();
	}
	
	/**
	 * Create the consistency panel
	 * @return JPanel
	 */
	private JPanel createConsistencyPanel() {
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
	
	/**
	 * Create Dominance Panel
	 * @return JPanel
	 */
	private JPanel createDominancePanel() {
		JPanel dominancePanel = new JPanel();
		dominancePanel.setLayout(new BoxLayout(dominancePanel, BoxLayout.X_AXIS));
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
		return dominancePanel;
	}
	
	/**
	 * Create Results Panel
	 * @return JPanel
	 */
	private JPanel createResultsPanel() {
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		topNextButton = new JButton("Top");
		topNextButton.addActionListener(this);
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
		
		JTextField resultLabel = new JTextField("Results");
		resultLabel.setEditable(false);
		resultLabel.setPreferredSize(new Dimension(75, 50));
		
		resultsField = new JTextArea(3,38);
		resultsField.setEditable(false);
		resultsField.setPreferredSize(new Dimension(400, 50));
		resultsField.setText("list of results fetched one by one");
		resultsField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		innerPanel.add(resultLabel);
		innerPanel.add(resultsField);
		
		resultsPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		resultsPanel.add(topNextButton);
		resultsPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		resultsPanel.add(innerPanel);
		resultsPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		return resultsPanel;
	}
	
	/**
	 * create Justification Panel
	 * @return JPanel
	 */
	private JPanel createJustificationPanel() {
		JPanel justificationPanel = new JPanel();
		justificationPanel.setLayout(new BoxLayout(justificationPanel, BoxLayout.X_AXIS));
		
		JTextField justificationLabel = new JTextField("Justification");
		justificationLabel.setEditable(false);
		justificationLabel.setPreferredSize(new Dimension(75, 50));
		
		justificationField = new JTextArea(3,38);
		justificationField.setEditable(false);
		justificationField.setPreferredSize(new Dimension(400, 50));
		justificationField.setText("result justification");
		justificationField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		
		justificationPanel.add(justificationLabel);
		justificationPanel.add(justificationField);
		
		return justificationPanel;
	}
	
	/**
	 * Clear results fields
	 */
	private void resetResultFields() {
		consistencyField.setText("result");
		dominanceField.setText("result");
		topNextButton.setText("Top");
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
		} else if (source == stakeholderBox) {
			Object selectedItem = stakeholderBox.getSelectedItem();
			if (selectedItem instanceof Member) {
				curMember = (Member) selectedItem;
				allMembers = false;
			} else if (selectedItem.equals("All Members")) {
				curMember = null;
				allMembers = true;
			}
			resetResultFields();
		}
	}
	
	/**
	 * Send Query to back end
	 * @param query type
	 */
	private void sendQuery(int type) {
		switch (type) {
		case CONSISTENCY:
			System.out.println("send cosistency query");
			if (allMembers)
				consistencyField.setText("new results for All Members");
			else
				consistencyField.setText("new results for "+curMember);

			//TODO - update result field
			break;
		case DOMINANCE:
			System.out.println("send dominance query");
			if (allMembers)
				dominanceField.setText("new results for All Members");
			else
				dominanceField.setText("new results for "+curMember);
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
