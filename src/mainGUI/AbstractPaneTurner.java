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
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.w3c.dom.Document;

import dataStructures.AbstractDocument;

/**
 * AbstractPaneTurner is an extension of JSplitPane which shows
 * a menu (metaPanes) with previous and next buttons on the left and content
 * panels (viewPanes) on the right.
 */
@SuppressWarnings("serial")
public abstract class AbstractPaneTurner extends JSplitPane {

	protected PreferenceReasoner reasoner;
	protected boolean initializing;

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
	protected PreferencePane preferencesPane;
	
//	private File currentProjectFolder;
	
	static String[] s_prefReasSteps = { "Setup Project", "Add Attributes", 
		"Attribute\nDomains", "Add\nAlternatives", "Alternative\nValues", 
		"Setup Preferences", "View Result" }; 
	static String[] s_plusMulStakeSteps = { "Setup Project", "Add Attributes", 
		"Attribute\nDomains", "Add\nAlternatives", "Alternative\nValues", 
		"Add Multiple\nStakeholders", "Setup Preferences", "View Result" };

	// TODO - make currentSelected come from AbstractDocument MetaData
	
	/**
	 * Create a new instance of AbstractPaneTurner
	 * @param reasoner
	 * @param document
	 * @param isMultipleStakeholder
	 */
	public AbstractPaneTurner(PreferenceReasoner reasoner, AbstractDocument document, boolean isMultipleStakeholder) {
		this.reasoner = reasoner;
		this.document = document;
		this.isMultipleStakeholder = isMultipleStakeholder;
	
		setupActions();
		setLeftComponent(getChooser());
	}

	protected abstract Component initializeViewPanes();
	
	/**
	 * Creates and returns the main xml document for 
	 * the project and creates the other xml documents.
	 * @param xmlfile
	 * @return xml DOMSource representation of project
	 */
	public Document toXML(){
		return document.toXML();
	}
	
	/**
	 * Creates and returns a single xml file representation of 
	 * the project.
	 * @return
	 */
	public Document toExportXML() {
		return document.toExportXML();
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
		reasoner.getFrame().pack();
	}
	
	/**
	 * Switch view to the previous panel
	 */
	public void previous(){
		metaPanes[currentSelected].toggleColor();
		if (currentSelected > 0) {
			viewPanes[currentSelected].leave();
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
			viewPanes[currentSelected].leave();
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
	
	public String getProjectName() {
		return document.getMetaData().getProjectName();
	}

	/**
	 * Set the project file
	 * @param curFile
	 */
	public void setCurrentProject(File curFolder) {
		document.setProjectFolder(curFolder);
		document.getMetaData().setProjectName(curFolder.getName());
		projectPane.updateProjectNameField();
	}
	
	/**
	 * calls update on the current panel
	 */
	public void updateRightPane() {
		viewPanes[currentSelected].update();
	}

	public boolean isInitializing() {
		return initializing;
	}

	public AbstractDocument getDocument() {
		return document;
	}
}
