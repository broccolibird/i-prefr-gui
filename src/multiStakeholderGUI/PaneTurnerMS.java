package multiStakeholderGUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import graph.RoleForest;
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

import dataStructures.AbstractDocument;
import dataStructures.Role;
import edu.uci.ics.jung.graph.Graph;

import mainGUI.AbstractPaneTurner;
import mainGUI.UpdatePane;

@SuppressWarnings("serial")
public class PaneTurnerMS extends JSplitPane {

	protected JFrame parent;
	
	protected JButton nextButton;
	protected JButton prevButton;

	protected Action next;
	protected Action prev;

	protected SelectableTextPane[] metaPanes;
	protected UpdatePane[] viewPanes;
	protected int currentSelected;
	
	private AbstractDocument document;
	private AbstractPaneTurner parentTurner;
	
	public PaneTurnerMS(JFrame parent, AbstractDocument document, AbstractPaneTurner parentTurner) {
		this.parent = parent;
		this.document = document;
		this.parentTurner = parentTurner;
		
		setupActions();
		setLeftComponent(getChooser());
		
		setRightComponent(initializeViewPanes());
		
		setPreferredSize(new Dimension(700, 800));
	}
	
	protected Component initializeViewPanes() {
		viewPanes = new UpdatePane[metaPanes.length];
		
		viewPanes[1] = new HierarchyPane(document, parent);
		RoleForest<Role, Integer> graph = ((HierarchyPane) viewPanes[1]).getGraph();

		viewPanes[0] = new RolePane( document.getRoleMap(), graph, parent);
		
		viewPanes[2] = new MemberPane(document.getRoleMap(), parent);
		
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
				"Assign\nStakeholders"};
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
				System.out.println(currentSelected);
				metaPanes[currentSelected].toggleColor();
				if (currentSelected < metaPanes.length - 1) {
					currentSelected++;
					viewPanes[currentSelected].update();
					setRightComponent(viewPanes[currentSelected]);
				} else if (currentSelected == metaPanes.length - 1){
					parentTurner.next();
				} 
				metaPanes[currentSelected].toggleColor();
			}
		};
		prev = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(currentSelected);
				metaPanes[currentSelected].toggleColor();
				if (currentSelected > 0) {
					currentSelected--;
					viewPanes[currentSelected].update();
					setRightComponent(viewPanes[currentSelected]);
				} else if ( currentSelected == 0 ) {
					parentTurner.previous();
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
