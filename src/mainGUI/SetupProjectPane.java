package mainGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dataStructures.MetaData;
import dataStructures.ModelCheckerOption;

/**
 * The SetupProjectPane is an UpdatePane with entry fields and
 * display fields for project MetaData.
 */
@SuppressWarnings("serial")
public class SetupProjectPane extends UpdatePane implements DocumentListener,
		ActionListener {

	// private JPanel inputPanel;
	private PreferenceReasoner reasoner;
	private MetaData metaData;
	private JTextField projectNameField;
	private JComboBox modelCheckerComboBox;

	/**
	 * Create a new instance of the SetupProjectPane
	 * @param metaData
	 */
	public SetupProjectPane(PreferenceReasoner reasoner, MetaData metaData) {
		this.reasoner = reasoner;
		this.metaData = metaData;
		this.add(initializeGUI());
		setVisible(true);
	}

	/**
	 * Setup the interface for this panel
	 * @return JPanel
	 */
	private JPanel initializeGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Project Name"));
		
		JPanel projectFolderPanel = new JPanel();
		
		projectNameField = new JTextField(35);
		projectNameField.setText(metaData.getProjectName());
		projectNameField.getDocument().addDocumentListener(this);
		projectFolderPanel.add(projectNameField);
		
		JButton createFolderButton = new JButton("Create Project Folder");
		createFolderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reasoner.showSaveDialog();
				
			}
		});
		projectFolderPanel.add(createFolderButton);
		
		panel.add(projectFolderPanel);

		JPanel projectNameHeader = new JPanel();
		projectNameHeader
				.setLayout(new BoxLayout(projectNameHeader, BoxLayout.X_AXIS));
				
		// Add Model Checker options
		panel.add(new JLabel("Select Model Checker"));

		ModelCheckerOption[] options = ModelCheckerOption.getAllOptions();
		modelCheckerComboBox = new JComboBox(options);
		modelCheckerComboBox.addActionListener(this);
		ModelCheckerOption oldOption = metaData.getSelectedModelChecker();
		if(oldOption!=null){
			for(int i=0;i<options.length;i++){
				if (options[i].equals(oldOption)){
					modelCheckerComboBox.setSelectedIndex(i);
				}
			}
		}else{
			metaData.setSelectedModelChecker(options[0]);
			modelCheckerComboBox.setSelectedIndex(0);
		}

		panel.add(modelCheckerComboBox);

		// Add project date label
		panel.add(new JLabel("Project created on: "
				+ metaData.getCreationDate().get(Calendar.MONTH) + "/"
				+ metaData.getCreationDate().get(Calendar.DATE) + "/"
				+ metaData.getCreationDate().get(Calendar.YEAR)));
		return panel;
	}

	@Override
	public void update() {
		projectNameField.requestFocusInWindow();
		projectNameField.select(0, projectNameField.getSelectionEnd());
		projectNameField.setSelectionColor(new Color(0, 0, 0, 25));
	}
	
	public void updateProjectNameField() {
		projectNameField.setText(metaData.getProjectName());
	}


	@Override
	public void changedUpdate(DocumentEvent e) {
		// not triggered by plain text
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		handleEvent(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		handleEvent(e);
	}

	private void handleEvent(DocumentEvent e) {
		if (e.getDocument() == projectNameField.getDocument()) {
			String projectName = projectNameField.getText();
			metaData.setProjectName(projectName);
		} 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == modelCheckerComboBox) {
			ModelCheckerOption selectedOption = (ModelCheckerOption) modelCheckerComboBox
					.getSelectedItem();
			metaData.setSelectedModelChecker(selectedOption);
		}
	}

}
