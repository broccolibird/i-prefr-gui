package multiStakeholderGUI;

import java.awt.Component;
import java.awt.event.ActionEvent;

import guiElements.SelectableTextPane;

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

import dataStructures.Attribute;
import dataStructures.maps.EdgeStatementMap;
import edu.uci.ics.jung.graph.Graph;

import mainGUI.AlternativePane;
import mainGUI.AttributePane;
import mainGUI.DomainPane;
import mainGUI.SetupGraphPane;
import mainGUI.SetupProjectPane;
import mainGUI.StakeholderPane;
import mainGUI.UpdatePane;
import mainGUI.ValuePane;
import mainGUI.ViewResultsPaneTCP;

public class MSPaneTurner extends JSplitPane {

	protected JFrame parent;
	
	protected JButton nextButton;
	protected JButton prevButton;

	protected Action next;
	protected Action prev;

	protected SelectableTextPane[] metaPanes;
	protected UpdatePane[] viewPanes;
	protected int currentSelected;
	
	public MSPaneTurner(JFrame parent) {
		this.parent = parent;

		setupActions();
		setLeftComponent(getChooser());
		
		setRightComponent(initializeViewPanes());
	}
	
	protected Component initializeViewPanes() {
		viewPanes = new UpdatePane[metaPanes.length];

		viewPanes[0] = new TestPane(parent, "one");
		viewPanes[1] = new TestPane(parent, "two");
		viewPanes[2] = new TestPane(parent, "three");
		
		return viewPanes[currentSelected];
	}
	
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
		String[] steps = { "Create Roles", "Create Role\nHierarchy", 
				"Assign Stakeholders"};
		metaPanes = new SelectableTextPane[steps.length];
		for (int i = 0; i < steps.length; i++) {
			metaPanes[i] = new SelectableTextPane(steps[i]);
			chooser.add(metaPanes[i]);
		}
		currentSelected = 0;
		metaPanes[currentSelected].toggleColor();
		return chooser;
	}

	private void setupActions() {
		next = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				metaPanes[currentSelected].toggleColor();
				if (currentSelected < metaPanes.length - 1) {
					currentSelected++;
					viewPanes[currentSelected].update();
					setRightComponent(viewPanes[currentSelected]);
				}
				metaPanes[currentSelected].toggleColor();
			}
		};
		prev = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				metaPanes[currentSelected].toggleColor();
				if (currentSelected > 0) {
					currentSelected--;
					viewPanes[currentSelected].update();
					setRightComponent(viewPanes[currentSelected]);
				}
				metaPanes[currentSelected].toggleColor();
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
}
