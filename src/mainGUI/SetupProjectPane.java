package mainGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dataStructures.MetaData;
import dataStructures.ModelCheckerOption;
import dataStructures.maps.OptionMap;

@SuppressWarnings("serial")
public class SetupProjectPane extends UpdatePane implements DocumentListener,
		ItemListener, ActionListener {

	// private JPanel inputPanel;
	private MetaData metaData;
	private JTextField projectNameField;
	private JTextField filenameField;
	private JCheckBox sameNameCheckBox;
	private JComboBox modelCheckerComboBox;

	/**
	 * Create a new instanceof the SetupProjectPane
	 * @param metaData
	 */
	public SetupProjectPane(MetaData metaData) {
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
		projectNameField = new JTextField(35);
		projectNameField.setText(metaData.getProjectName());
		projectNameField.getDocument().addDocumentListener(this);
		panel.add(projectNameField);

		JPanel filenameHeader = new JPanel();
		filenameHeader
				.setLayout(new BoxLayout(filenameHeader, BoxLayout.X_AXIS));
		filenameHeader.add(new JLabel("Filename"));
		sameNameCheckBox = new JCheckBox("same as project name");
		
		//use the option to inform the state of the sameNameCheckBox
		Integer selected = metaData.getDisplayOptions().get(OptionMap.SAME_NAME);
		if(selected != null && selected == 0){
			selectSameName(false);
		}else{
			selectSameName(true);	
		}
		
		sameNameCheckBox.addItemListener(this);
		filenameHeader.add(sameNameCheckBox);
		panel.add(filenameHeader);

		filenameField = new JTextField(35);
		filenameField.setText(metaData.getFilename());
		filenameField.getDocument().addDocumentListener(this);
		if(selected == null || selected == 1) // same name selected
			filenameField.setEnabled(false);
		panel.add(filenameField);
		
		// Add Model Checker options
		panel.add(new JLabel("Selected Model Checker"));

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
		// Do nothing
	}
	
	private void selectSameName(boolean selected){
		sameNameCheckBox.setSelected(selected);
		if(selected){
			metaData.getDisplayOptions().put(OptionMap.SAME_NAME, 1);
		}else{
			metaData.getDisplayOptions().put(OptionMap.SAME_NAME, 0);
		}		
	}
	
	/**
	 * Set File Name field after saving project changes
	 * @param fileName
	 */
	public void setSavedFileName(String fileName) {
		if(!(fileName.equals(filenameField.getText()) ||
				fileName.equals(projectNameField.getText()+".xml"))){
			sameNameCheckBox.setSelected(false);
		}
		filenameField.setText(fileName);
		metaData.setSaved(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object o = e.getItemSelectable();
		if (o == sameNameCheckBox) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				selectSameName(false);
				filenameField.setEnabled(true);
			} else {
				selectSameName(true);
				filenameField.setEnabled(false);
				filenameField.setText(projectNameField.getText()+".xml");
			}
		}
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

			if (sameNameCheckBox.isSelected()) {
				filenameField.setText(projectName+".xml");
				metaData.setFilename(projectName+".xml");
			}

		} else if (e.getDocument() == filenameField.getDocument()) {
			if (!sameNameCheckBox.isSelected()) {
				metaData.setFilename(filenameField.getText());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		ModelCheckerOption selectedOption = (ModelCheckerOption) cb
				.getSelectedItem();
		metaData.setSelectedModelChecker(selectedOption);
	}

}
