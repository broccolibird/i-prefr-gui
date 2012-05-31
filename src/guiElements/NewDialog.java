package guiElements;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import dataStructures.RunConfiguration;

import mainGUI.PreferenceReasoner;

public class NewDialog extends JDialog implements ActionListener {

	private JFrame parentFrame;
	
	private JRadioButton single;
	private JRadioButton multiple;
	private JRadioButton cp;
	private JRadioButton ci;
	
	private JButton ok;
	
	Boolean singleSelected;
	Boolean cpSelected;
	
	public NewDialog(JFrame parentFrame ){
		super(parentFrame, Dialog.ModalityType.DOCUMENT_MODAL);
		//setPreferredSize(new Dimension(200, 200));

		this.parentFrame = parentFrame;
		
		getContentPane().add(createGUI());
		pack();
		setLocationRelativeTo(parentFrame);
		
		
	}
	
	public RunConfiguration showDialog(){
		setVisible(true);
		return new RunConfiguration(singleSelected, cpSelected);
		
	}
	
	private JPanel createGUI(){		
		
		JLabel stakeholderLabel = new JLabel("Select number of stakeholders:");
		single = new JRadioButton("Single");
		single.addActionListener(this);
		multiple = new JRadioButton("Multiple");
		multiple.addActionListener(this);
		
		ButtonGroup stakeholderGroup = new ButtonGroup();
		stakeholderGroup.add(single);
		stakeholderGroup.add(multiple);
		
		JPanel stakeholderButtonPanel = new JPanel();
		stakeholderButtonPanel.setLayout(new BoxLayout(stakeholderButtonPanel, BoxLayout.X_AXIS));
		stakeholderButtonPanel.add(single);
		stakeholderButtonPanel.add(multiple);
		
		JPanel stakeholderPanel = new JPanel();
		stakeholderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		stakeholderPanel.add(stakeholderLabel);
		stakeholderPanel.add(stakeholderButtonPanel);
		
		JLabel networkLabel = new JLabel("Select preference network type:");
		cp = new JRadioButton("(T)CP-NET");
		cp.addActionListener(this);
		ci = new JRadioButton("CI-NET");
		ci.addActionListener(this);
		
		ButtonGroup networkGroup = new ButtonGroup();
		networkGroup.add(cp);
		networkGroup.add(ci);
		
		JPanel networkButtonPanel = new JPanel();
		networkButtonPanel.setLayout(new BoxLayout(networkButtonPanel, BoxLayout.X_AXIS));
		networkButtonPanel.add(cp);
		networkButtonPanel.add(ci);
		
		JPanel networkPanel = new JPanel();
		networkPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		networkPanel.add(networkLabel);
		networkPanel.add(networkButtonPanel);
		
		JButton cancel = new JButton("cancel");
		cancel.setEnabled(true);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				singleSelected = null;
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
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(cancel);
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonsPanel.add(ok);

			
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(stakeholderPanel);
		panel.add(networkPanel);
		panel.add(buttonsPanel);
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if(single == src){
			singleSelected = true;
		}else if(multiple == src){
			singleSelected = false;
		}else if(cp == src){
			cpSelected = true;
		}else if(ci == src){
			cpSelected = false;
		}
		
		if(singleSelected != null && cpSelected != null){
			ok.setEnabled(true);
		}
		
	}

}
