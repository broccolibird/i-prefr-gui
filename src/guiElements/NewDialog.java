package guiElements;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import dataStructures.RunConfiguration;

@SuppressWarnings("serial")
public class NewDialog extends JDialog implements ActionListener {
	
	JPanel stakeholderButtonPanel;
	private JRadioButton single;
	private JRadioButton multiple;
	private Action setMultipleStakeholder;
	private Action setSingleStakeholder;
	
	JPanel networkButtonPanel;
	private JRadioButton cp;
	private JRadioButton ci;
	private Action setCINet;
	private Action setTCPNet;
	
	JPanel buttonsPanel;
	private JButton ok;
	private JButton cancel;
	private Action selectOk;
	private Action selectCancel;
	
	Boolean multipleSelected;
	Boolean cpSelected;
	
	/**
	 * Create new New Dialog
	 * @param parentFrame
	 */
	public NewDialog(JFrame parentFrame ){
		super(parentFrame, Dialog.ModalityType.DOCUMENT_MODAL);
		
		getContentPane().add(createGUI());
		pack();
		setLocationRelativeTo(parentFrame);
	}
	
	/**
	 * Sets the Dialog to be visible
	 * @return RunConfiguration - user's selected multistakeholder and network options
	 */
	public RunConfiguration showDialog(){
		setVisible(true);
		
		if ( multipleSelected != null && cpSelected != null ) 
			return new RunConfiguration(multipleSelected, cpSelected);
		else
			return null;
		
	}
	
	private JPanel createGUI(){		
		
		// Add stakeholders options (single/multiple)
		JLabel stakeholderLabel = new JLabel("Select number of stakeholders:");
		single = new JRadioButton("Single");
		single.setMnemonic(KeyEvent.VK_S);
		single.addActionListener(this);
		multiple = new JRadioButton("Multiple");
		multiple.setMnemonic(KeyEvent.VK_M);
		multiple.addActionListener(this);
//		multiple.setEnabled(false);
		single.setSelected(true);
		multipleSelected = false;
		
		// create stakeholder button group
		ButtonGroup stakeholderGroup = new ButtonGroup();
		stakeholderGroup.add(single);
		stakeholderGroup.add(multiple);
		
		// add stakeholder buttons to stakeholder button panel
		stakeholderButtonPanel = new JPanel();
		stakeholderButtonPanel.setLayout(new BoxLayout(stakeholderButtonPanel, BoxLayout.X_AXIS));
		stakeholderButtonPanel.add(single);
		stakeholderButtonPanel.add(multiple);
		
		// add label and stakeholder button panel to stakeholder panel
		JPanel stakeholderPanel = new JPanel();
		stakeholderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		stakeholderPanel.add(stakeholderLabel);
		stakeholderPanel.add(stakeholderButtonPanel);
		
		// Add network options (TCP-net/CI-net)
		JLabel networkLabel = new JLabel("Select preference network type:");
		cp = new JRadioButton("(T)CP-NET");
		cp.setMnemonic(KeyEvent.VK_T);
//		cp.setEnabled(false);
		cp.addActionListener(this);
		ci = new JRadioButton("CI-NET");
		ci.setMnemonic(KeyEvent.VK_C);
		ci.setSelected(true);
		cpSelected = false;
		ci.addActionListener(this);
		
		// add network options to button group
		ButtonGroup networkGroup = new ButtonGroup();
		networkGroup.add(cp);
		networkGroup.add(ci);
		
		// add network radio buttons to network button panel
		networkButtonPanel = new JPanel();
		networkButtonPanel.setLayout(new BoxLayout(networkButtonPanel, BoxLayout.X_AXIS));
		networkButtonPanel.add(cp);
		networkButtonPanel.add(ci);
		
		// add label and network button panel to network panel
		JPanel networkPanel = new JPanel();
		networkPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		networkPanel.add(networkLabel);
		networkPanel.add(networkButtonPanel);
		
		// Add cancel/ok buttons
		cancel = new JButton("cancel");
		cancel.setEnabled(true);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				multipleSelected = null;
				cpSelected = null;
				setVisible(false);
				dispose();
			}
			
		});
		
		ok = new JButton("ok");
		ok.setEnabled(false);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				setVisible(false);
				dispose();
			}
		});
		enableOk();
		
		// add cancel/ok buttons to button panel
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(cancel);
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonsPanel.add(ok);

		// add all panels to JDialog panel	
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(stakeholderPanel);
		panel.add(networkPanel);
		panel.add(buttonsPanel);
		
		// setup keyboard shortcuts
		setupActions();
		
		return panel;
	}

	/**
	 * Set-up keyboard shortcuts for RadioButtons and Buttons within
	 * New Dialog
	 */
	private void setupActions() {
		// create shortcuts for multistakeholder options
		setMultipleStakeholder = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setMultipleStakeholder(true);
			}
		};
		setSingleStakeholder = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setMultipleStakeholder(false);
			}
		};
		InputMap windowStakeholderInputMap = stakeholderButtonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		windowStakeholderInputMap.put(KeyStroke.getKeyStroke("S"), "selectSingle");
		windowStakeholderInputMap.put(KeyStroke.getKeyStroke("M"), "selectMultiple");

		stakeholderButtonPanel.getActionMap().put("selectSingle", setSingleStakeholder);
		stakeholderButtonPanel.getActionMap().put("selectMultiple", setMultipleStakeholder);
		
		
		//create shortcuts for network options
		setCINet = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCPNet(false);
			}
		};
		setTCPNet = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCPNet(true);
			}
		};
		
		InputMap windowCINetInputMap = networkButtonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		windowCINetInputMap.put(KeyStroke.getKeyStroke("C"), "selectCINet");
		windowCINetInputMap.put(KeyStroke.getKeyStroke("T"), "selectTCPNet");

		networkButtonPanel.getActionMap().put("selectCINet", setCINet);
		networkButtonPanel.getActionMap().put("selectTCPNet", setTCPNet);
		
		//create shortcuts for cancel/ok options
		selectOk = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok.doClick();
			}
		};
		selectCancel = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel.doClick();
			}
		};
		
		InputMap windowButtonInputMap = buttonsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		windowButtonInputMap.put(KeyStroke.getKeyStroke("ENTER"), "selectOk");
		windowButtonInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "selectCancel");

		buttonsPanel.getActionMap().put("selectOk", selectOk);
		buttonsPanel.getActionMap().put("selectCancel", selectCancel);
	}
	
	
	private void setMultipleStakeholder(boolean bool) {
		multipleSelected = bool;
		if (bool == true) {
			multiple.setSelected(true);
		} else {
			single.setSelected(true);
		}
		enableOk();
	}
	
	private void setCPNet(boolean bool) {
		cpSelected = bool;
		if (bool == true) {
			cp.setSelected(true);
		} else {
			ci.setSelected(true);
		}
		enableOk();
	}
	
	/**
	 * Checks if selections have been made in both radio groups
	 * before enabling ok button
	 */
	private void enableOk() {
		if(multipleSelected != null && cpSelected != null){
			ok.setEnabled(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(single == src){
			multipleSelected = false;
		}else if(multiple == src){
			multipleSelected = true;
		}else if(cp == src){
			cpSelected = true;
		}else if(ci == src){
			cpSelected = false;
		}
		
		enableOk();
		
	}

}
