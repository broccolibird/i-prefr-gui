package mainGUI;

import guiElements.SelectableTextPane;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import dataStructures.AbstractDocument;

@SuppressWarnings("serial")
public abstract class AbstractPaneTurner extends JSplitPane {

	protected JFrame parent;

	protected JButton nextButton;
	protected JButton prevButton;

	protected Action next;
	protected Action prev;

	protected SelectableTextPane[] metaPanes;
	protected UpdatePane[] viewPanes;
	protected int currentSelected;
	
	protected boolean isMultipleStakeholder;
	
	protected AbstractDocument document;
	
	protected SetupProjectPane projectPane;
	protected SetupPreferencesPane preferencesPane;
	
	private File currentFile;
	
	static String[] s_prefReasSteps = { "Setup Project", "Add Attributes", 
		"Attribute\nDomains", "Add\nAlternatives", "Alternative\nValues", 
		"Setup Preferences", "View Result" }; 
	static String[] s_plusMulStakeSteps = { "Setup Project", "Add Attributes", 
		"Attribute\nDomains", "Add\nAlternatives", "Alternative\nValues", 
		"Add Multiple\nStakeholders", "Setup Preferences", "View Result" };

	// TODO - make currentSelected come from AbstractDocument MetaData
	
	/**
	 * Create a new instance of AbstractPaneTurner
	 * @param parent
	 * @param document
	 * @param isMultipleStakeholder
	 */
	public AbstractPaneTurner(JFrame parent, AbstractDocument document, boolean isMultipleStakeholder,
			File currentFile) {
		this.parent = parent;
		this.document = document;
		this.isMultipleStakeholder = isMultipleStakeholder;
		this.currentFile = currentFile;
	
		setupActions();
		setLeftComponent(getChooser());
	}

	protected abstract Component initializeViewPanes();
	
	/**
	 * Creates and returns the xml representation of the project.
	 * @param xmlfile
	 * @return xml string representation of project
	 */
	public String toXML(File xmlfile){
		return document.toXML(xmlfile);
	}

	/**
	 * Create left-side chooser panel
	 * @return chooser panel
	 */
	protected JPanel getChooser() {
		JPanel chooser = new JPanel();
		chooser.setLayout(new BoxLayout(chooser, BoxLayout.Y_AXIS));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		nextButton = new JButton("Next");
		prevButton = new JButton("Prev");
		nextButton.addActionListener(next);
		prevButton.addActionListener(prev);
		buttonPanel.add(prevButton);
		buttonPanel.add(nextButton);
		chooser.add(buttonPanel);
		
		String[] steps = null;
		
		if( isMultipleStakeholder ) {
			steps = s_plusMulStakeSteps;
		}else {
			steps = s_prefReasSteps;
		} 
		metaPanes = new SelectableTextPane[steps.length];
		for (int i = 0; i < steps.length; i++) {
			metaPanes[i] = new SelectableTextPane(steps[i]);
			chooser.add(metaPanes[i]);
		}
		currentSelected = 0;
		metaPanes[currentSelected].toggleColor();
		return chooser;
	}

	/**
	 * Sets up keyboard shortcuts for chooser panel's
	 * previous and next buttons
	 */
	private void setupActions() {
		next = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				next();
			}
		};
		prev = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				previous();
			}
		};
		KeyStroke down = KeyStroke.getKeyStroke("DOWN");
		KeyStroke left = KeyStroke.getKeyStroke("LEFT");
		KeyStroke up = KeyStroke.getKeyStroke("UP");
		KeyStroke right = KeyStroke.getKeyStroke("RIGHT");

		// put the keystroke-keyword mapping into both InputMaps so that the
		// keystrokes always cause the action
		InputMap ancestorInputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ancestorInputMap.put(down, "next");
		ancestorInputMap.put(right, "next");
		ancestorInputMap.put(up, "prev");
		ancestorInputMap.put(left, "prev");

		InputMap windowInputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		windowInputMap.put(down, "next");
		windowInputMap.put(right, "next");
		windowInputMap.put(up, "prev");
		windowInputMap.put(left, "prev");

		getActionMap().put("next", next);
		getActionMap().put("prev", prev);
	}

	public void pack() {
		parent.pack();
	}
	
	/**
	 * Switch view to the previous panel
	 */
	public void previous(){
		metaPanes[currentSelected].toggleColor();
		if (currentSelected > 0) {
			currentSelected--;
			setRightComponent(viewPanes[currentSelected]);
			viewPanes[currentSelected].update();
		}
		metaPanes[currentSelected].toggleColor();
	}
	
	/**
	 * Switch view to the next panel
	 */
	public void next() {
		metaPanes[currentSelected].toggleColor();
		if (currentSelected < metaPanes.length - 1) {
			currentSelected++;
			setRightComponent(viewPanes[currentSelected]);
			viewPanes[currentSelected].update();

		}
		metaPanes[currentSelected].toggleColor();
	}
	
	/**
	 * @return true if the document contains changes
	 */
	public boolean existChanges() {
		return document.existChanges();
	}

	/**
	 * Set the document's saved value
	 * @param saved
	 */
	public void setSaved(boolean saved) {
		document.setSaved(saved);
		
	}
	
	/**
	 * Checks for unsaved changes in the user preference input
	 * pane.
	 */
	public void checkChangesInPreferences() {
		preferencesPane.checkForUnsavedChanges();
	}
	
	/**
	 * Returns filename input by user on the SetupProjectPane
	 * @return filename
	 */
	public String getProjectFileName() {
		return document.getMetaData().getFilename();
	}
	
	public void setProjectFileName(String fileName){
		projectPane.setSavedFileName(fileName);
	}

	/**
	 * Set the project file
	 * @param curFile
	 */
	public void setCurrentFile(File curFile) {
		this.currentFile = curFile;
	}
	
	/**
	 * @return the current project file
	 */
	public File getCurrentFile() {
		return currentFile;
	}

}
