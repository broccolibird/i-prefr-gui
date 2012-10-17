package guiElements.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SaveProjectDialog extends JDialog implements TreeSelectionListener {

	private JFrame frame;
	JTree tree;
	DirectoryTreeModel model;
	JTextField projectNameField;
	JTextField selectedFolderField;
	DefaultMutableTreeNode selectedNode;
	File selectedFile = null;
	
	
	public SaveProjectDialog(JFrame frame, String projectName) {
		super(frame, Dialog.ModalityType.DOCUMENT_MODAL);
		this.frame = frame;
		
		createGUI(projectName);
	}
	
	private void createGUI(String projectName) {
		BorderLayout layout = new BorderLayout(10, 10);
		setLayout(layout);
		
		File root = new File(System.getProperty("user.home"));
		
		// Create a DirectoryModel object to represent tree of directories
	    model = new DirectoryTreeModel(root);

	    // Create a JTree and tell it to display our model
	    tree = new JTree();
	    tree.setModel(model);
	    tree.addTreeWillExpandListener(new DirectoryExpansionListener());
	    tree.setCellRenderer(new DirectoryRenderer());
	    
	    // allow the user to select only one directory at a time
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    
	    tree.addTreeSelectionListener(this);

	    // Add scroll to JTree
	    JScrollPane scrollpane = new JScrollPane(tree);
	    scrollpane.setBorder(new LineBorder(Color.BLACK));
	    scrollpane.setMaximumSize(new Dimension(350,350));
	    
	    
	    // Create panel for project name input
	    JPanel projectNamePanel = new JPanel();
	    projectNamePanel.setLayout(new BoxLayout(projectNamePanel, BoxLayout.PAGE_AXIS));
	    projectNamePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
	    
	    // Add label to top panel
	    JLabel projectNameLabel = new JLabel("New Folder (Project Name):"); 
	    projectNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    projectNamePanel.add(projectNameLabel);
	  
	    // Add field to top panel
	    projectNameField = new JTextField(projectName);
	    projectNamePanel.add(projectNameField);
	  
	    add(projectNamePanel, BorderLayout.NORTH);	    
	    
	    
	    // create center panel to hold file directory scrollpane
	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
	    centerPanel.add(scrollpane);
	    
	    // create 'add new folder' button
	    JButton addFolderButton = new JButton("Add New Folder to Selected Location");
	    addFolderButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String projectName = projectNameField.getText();
				
				if(selectedFile == null) {
					JOptionPane.showMessageDialog(null, "Please select a location for the project folder.");
				} else if(projectName == null || projectName.length() == 0) {
					JOptionPane.showMessageDialog(null, "Please enter a name for the project folder.");
				} else if(!selectedFile.isDirectory()) {
					JOptionPane.showMessageDialog(null, "Must create folder in a directory.");
				} else {
					String pathname = selectedFile.getAbsolutePath();
					pathname = pathname + System.getProperty("file.separator") + projectName;
					File newFile = new File(pathname);
					newFile.mkdir();
					
					
					//add to tree
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(newFile);
					model.insertNodeInto(childNode, selectedNode, selectedNode.getChildCount());
					
					tree.scrollPathToVisible(new TreePath(childNode.getPath()));
				}
					
			}
	    });
	    
	    addFolderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    centerPanel.add(addFolderButton);
	    
	    add(centerPanel, BorderLayout.CENTER);
	    
	    // create panel to hold project folder field and use button
	    JPanel southPanel = new JPanel();
	    southPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
	    southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
	    
	    // Add field to show currently selected folder
	    selectedFolderField = new JTextField();
	    selectedFolderField.setEditable(false);
	    southPanel.add(selectedFolderField);
	    
	    // Add "Use as Project Folder" button
	    JButton useButton = new JButton("Use as Project Folder");
	    useButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    useButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				
			}
	    
	    });
	    southPanel.add(useButton);
	    
	    add(southPanel, BorderLayout.SOUTH);
	    
	    setSize(400, 400);
	    
	}
	
	public File showDialog() {
		setVisible(true);
		return selectedFile;
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		selectedNode = 
				(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		selectedFile = (File) selectedNode.getUserObject();
		selectedFolderField.setText(selectedFile.getAbsolutePath());
	}

}
